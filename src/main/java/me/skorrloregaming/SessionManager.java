package me.skorrloregaming;

import me.skorrloregaming.impl.InventoryType;
import me.skorrloregaming.impl.IpLocationQuery;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.skorrloregaming.*;

public class SessionManager {
	public ConfigurationManager sessionConfig;
	private final static int DAYS_TILL_SESSION_EXPIRATION = 3;

	public void setup() {
		sessionConfig = new ConfigurationManager();
		sessionConfig.setup(new File(ServerGet.get().getPlugin().getDataFolder(), "session_storage.yml"));
	}

	public boolean verifySession(Player player) {
		String ipAddress = player.getAddress().getAddress().getHostAddress();
		String key = LinkSessionManager.encodeHex(ipAddress);
		if (sessionConfig.getData().contains(player.getUniqueId().toString() + "." + key + ".lastAccessed")) {
			long lastAccessed = sessionConfig.getData().getLong(player.getUniqueId().toString() + "." + key + ".lastAccessed");
			Session session = new Session(key.toCharArray(), player, lastAccessed);
			if (session.isExpired()) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public void updateSession(OfflinePlayer player, Session session) {
		if (!sessionConfig.getData().contains(player.getUniqueId().toString() + "." + new String(session.key) + ".firstAccessed") || (sessionConfig.getData().contains(player.getUniqueId().toString()) && getStoredSession(player, new String(LinkSessionManager.decodeHex(session.getKey()))).requiresLogin())) {
			sessionConfig.getData().set(player.getUniqueId().toString() + "." + new String(session.key) + ".firstAccessed", session.getLastAccessed());
		}
		sessionConfig.getData().set(player.getUniqueId().toString() + "." + new String(session.key) + ".lastAccessed", session.getLastAccessed());
		sessionConfig.getData().set(player.getUniqueId().toString() + "." + new String(session.key) + ".discarded", false);
		sessionConfig.saveData();
	}

	public void invalidateSession(String issuedBy, OfflinePlayer player, String sessionKey, boolean doDeleteTask) {
		if (doDeleteTask) {
			sessionConfig.getData().set(player.getUniqueId().toString() + "." + sessionKey, null);
			sessionConfig.saveData();
		} else {
			sessionConfig.getData().set(player.getUniqueId().toString() + "." + sessionKey + ".discarded", true);
			sessionConfig.saveData();
		}
		if (player.isOnline() && player.getPlayer().getAddress().getAddress().getHostAddress().equals(new String(LinkSessionManager.decodeHex(sessionKey.toCharArray()))))
			player.getPlayer().kickPlayer("Your session on the server has been invalidated." + '\n' + "You will need to login again to use this account." + '\n' + '\n' + "Issued by " + issuedBy + " at " + new Date().toString() + ".");
	}

	public Session[] getAllStoredSessions(OfflinePlayer player) {
		ArrayList<Session> sessions = new ArrayList<Session>();
		for (String key : sessionConfig.getData().getConfigurationSection(player.getUniqueId().toString()).getKeys(false)) {
			sessions.add(new Session(key.toCharArray(), player, sessionConfig.getData().getLong(player.getUniqueId().toString() + "." + key + ".lastAccessed")));
		}
		return sessions.toArray(new Session[0]);
	}

	public Session getStoredSession(OfflinePlayer player, String hostAddr) {
		String key = LinkSessionManager.encodeHex(hostAddr);
		if (sessionConfig.getData().contains(player.getUniqueId().toString() + "." + key)) {
			return new Session(key.toCharArray(), player, sessionConfig.getData().getLong(player.getUniqueId().toString() + "." + key + ".lastAccessed"));
		} else {
			return null;
		}
	}

	public void openComplexInventory(Player player, OfflinePlayer tp) {
		Session[] sessions = getAllStoredSessions(tp);
		int invSize = (1 + ((int) (sessions.length / 9))) * 9;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 27;
		Inventory inventory = Bukkit.createInventory(new InventoryMenu(player, InventoryType.SESSIONS, tp.getName()), invSize, ChatColor.RESET + tp.getName() + "'s auth-sessions");
		for (int i = 0; i < sessions.length; i++) {
			Session session = sessions[i];
			Calendar calendarNow = Calendar.getInstance();
			calendarNow.setTimeInMillis(System.currentTimeMillis());
			Calendar calendarFirstAcc = Calendar.getInstance();
			calendarFirstAcc.setTimeInMillis(session.getFirstAccessedFromConfig());
			Calendar calendarLastAcc = Calendar.getInstance();
			calendarLastAcc.setTimeInMillis(session.getLastAccessed());
			byte[] decoded = LinkSessionManager.decodeHex(session.key);
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.RESET + "Session created : " + calendarFirstAcc.getTime().toString());
			lore.add(ChatColor.RESET + "Last accessed : " + calendarLastAcc.getTime().toString());
			String remoteAddr = new String(decoded);
			IpLocationQuery query = CraftGo.Player.queryIpLocation(remoteAddr);
			if (query == null)
				continue;
			if (player.getName().equals(tp.getName()) || player.isOp() || Link$.getRankId(player) > 1) {
				lore.add(ChatColor.RESET + "Remote address : " + query.getEndpoint());
				lore.add(ChatColor.RESET + "Geo-location : " + query.getCity() + ", " + query.getState() + " (" + query.getCountry() + ")");
				lore.add(ChatColor.RESET + "Internet provider : " + query.getIsp());
			}
			calendarLastAcc.setTimeInMillis(session.getLastAccessed() + ((86400 * DAYS_TILL_SESSION_EXPIRATION) * 1000));
			lore.add(ChatColor.RESET + "Session expires : " + calendarLastAcc.getTime().toString());
			Material skullType = Material.SKELETON_SKULL;
			if (tp.isOnline() && tp.getPlayer().getAddress().getAddress().getHostAddress().equals(remoteAddr)) {
				lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "This session is actively being used for your account.");
				skullType = Material.PLAYER_HEAD;
			}
			if (session.isExpired() || session.isDiscarded())
				skullType = Material.WITHER_SKELETON_SKULL;
			if (session.isExpired()) {
				for (int l = 0; l < lore.size(); l++) {
					lore.set(l, ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.stripColor(lore.get(l)));
				}
				lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "This session is expired, login is required to use it.");
			} else {
				if (!session.isDiscarded()) {
					lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Click to invalidate this session under your account.");
					lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "Doing so will result in a login requirement to use it.");
				} else {
					for (int l = 0; l < lore.size(); l++) {
						lore.set(l, ChatColor.RESET + "" + ChatColor.GRAY + "" + ChatColor.stripColor(lore.get(l)));
					}
					lore.add(ChatColor.DARK_PURPLE + "" + ChatColor.ITALIC + "This session was invalidated, login is required to use it.");
				}
			}
			for (int io = 0; io < lore.size(); io++) {
				String line = lore.get(io);
				if (line.length() > 65)
					lore.set(io, line.substring(0, 65));
			}
			ItemStack item = Link$.createMaterial(skullType, 1, ChatColor.RESET + "" + ChatColor.BOLD + new String(session.getKey()), (short) 0, lore);
			inventory.setItem(i, item);
		}
		player.openInventory(inventory);
	}

	public static class Session {
		private long lastAccessed;
		private OfflinePlayer player;
		private char[] key;

		public Session(char[] key, OfflinePlayer player, long lastAccessed) {
			this.player = player;
			this.lastAccessed = lastAccessed;
			this.key = key;
		}

		public boolean isExpired() {
			if ((System.currentTimeMillis() - this.getLastAccessed()) / 1000 > 86400 * DAYS_TILL_SESSION_EXPIRATION)
				return true;
			return false;
		}

		public long getFirstAccessedFromConfig() {
			String path = player.getUniqueId().toString() + "." + new String(key) + ".firstAccessed";
			long firstAccessed = lastAccessed;
			if (ServerGet.get().getSessionManager().sessionConfig.getData().contains(path))
				firstAccessed = ServerGet.get().getSessionManager().sessionConfig.getData().getLong(path);
			return firstAccessed;
		}

		public long getLastAccessed() {
			return lastAccessed;
		}

		public OfflinePlayer getPlayer() {
			return player;
		}

		public char[] getKey() {
			return key;
		}

		public boolean requiresLogin() {
			return isExpired() || isDiscarded();
		}

		public boolean isDiscarded() {
			String path = player.getUniqueId().toString() + "." + new String(key) + ".discarded";
			boolean discarded = false;
			if (ServerGet.get().getSessionManager().sessionConfig.getData().contains(path))
				discarded = ServerGet.get().getSessionManager().sessionConfig.getData().getBoolean(path);
			return discarded;
		}
	}
}
