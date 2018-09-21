package me.skorrloregaming;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import mkremins.fanciful.ChatColor;

public class PlaytimeManager implements Listener {
	public ConcurrentMap<UUID, Long> playtimeTracker = new ConcurrentHashMap<>();
	private ConfigurationManager playtimeConfig;

	public ConfigurationManager getPlaytimeConfig() {
		return playtimeConfig;
	}

	public PlaytimeManager() {
		playtimeConfig = new ConfigurationManager();
		playtimeConfig.setup(new File(Server.getPlugin().getDataFolder(), "playtime.yml"));
		Server.getPlugin().getServer().getPluginManager().registerEvents(this, Server.getPlugin());
		Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				Calendar newCalendar = Calendar.getInstance();
				UUID[] array = playtimeTracker.keySet().toArray(new UUID[0]);
				for (int i = 0; i < playtimeTracker.keySet().size(); i++) {
					UUID id = array[i];
					Player player = Bukkit.getPlayer(id);
					if (player == null) {
						handle_QuitEvent(CraftGo.Player.getOfflinePlayer(id));
					} else {
						long timeOfJoin = playtimeTracker.get(id).longValue();
						newCalendar.setTimeInMillis(timeOfJoin * 1000);
						if (!(newCalendar.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR))) {
							handle_QuitEvent(player);
							handle_JoinEvent(player);
						}
						if (!(newCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR))) {
							handle_QuitEvent(player);
							for (int day = 0; day <= 365; day++) {
								if (playtimeConfig.getData().contains(player.getUniqueId().toString() + ".dayOfYear." + day)) {
									playtimeConfig.getData().set(player.getUniqueId().toString() + ".dayOfYear." + day, null);
								}
								playtimeConfig.saveData();
							}
							handle_JoinEvent(player);
						}
					}
				}
			}
		}, 20L, 20L);
	}

	public void reloadData() {
		playtimeConfig.reloadData();
	}

	public long getStoredPlayerPlaytime(OfflinePlayer player) {
		if (playtimeConfig.getData().contains(player.getUniqueId().toString() + ".total")) {
			return playtimeConfig.getData().getLong(player.getUniqueId().toString() + ".total");
		} else {
			return 0;
		}
	}

	public long getStoredPlayerPlaytime(OfflinePlayer player, int dayOfYear) {
		if (playtimeConfig.getData().contains(player.getUniqueId().toString() + ".dayOfYear." + dayOfYear)) {
			return playtimeConfig.getData().getLong(player.getUniqueId().toString() + ".dayOfYear." + dayOfYear);
		} else {
			return 0;
		}
	}

	public int getTotalStoredPlayerPlaytime(OfflinePlayer player) {
		if (playtimeConfig.getData().contains(player.getUniqueId().toString() + ".total")) {
			return playtimeConfig.getData().getInt(player.getUniqueId().toString() + ".total");
		} else {
			return 0;
		}
	}

	public long[] getRangeOfStoredPlayerPlaytime(OfflinePlayer player, int startDayOfYear, int endDayOfYear) {
		ArrayList<Long> rangeOfData = new ArrayList<Long>();
		for (int i = startDayOfYear; i < endDayOfYear; i++) {
			rangeOfData.add(getStoredPlayerPlaytime(player, i));
		}
		return ArrayUtils.toPrimitive(rangeOfData.toArray(new Long[0]));
	}

	public int getLastKnownDayOfYear(OfflinePlayer player) {
		if (playtimeConfig.getData().contains(player.getUniqueId().toString() + ".lastKnownDayOfYear")) {
			return playtimeConfig.getData().getInt(player.getUniqueId().toString() + ".lastKnownDayOfYear");
		} else {
			return 0;
		}
	}

	public void handle_JoinEvent(Player player) {
		if (playtimeTracker.containsKey(player.getUniqueId()))
			return;
		if (Server.getAntiCheat().antiafk.lackingActivityMinutes.containsKey(player.getUniqueId()))
			if (Server.getAntiCheat().antiafk.lackingActivityMinutes.get(player.getUniqueId()) > 5.0)
				return;
		long seconds = System.currentTimeMillis() / 1000l;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
		if (dayOfYear < getLastKnownDayOfYear(player)) {
			for (int day = 0; day <= 365; day++) {
				if (playtimeConfig.getData().contains(player.getUniqueId().toString() + ".dayOfYear." + day)) {
					playtimeConfig.getData().set(player.getUniqueId().toString() + ".dayOfYear." + day, null);
				}
				playtimeConfig.saveData();
			}
		}
		if (!playtimeConfig.getData().contains(player.getUniqueId().toString())) {
			playtimeConfig.getData().set(player.getUniqueId().toString() + ".total", (long) CraftGo.Player.getLegacyMinecraftPlaytime(player));
		}
		playtimeConfig.getData().set(player.getUniqueId().toString() + ".lastKnownDayOfYear", dayOfYear);
		playtimeConfig.saveData();
		playtimeTracker.put(player.getUniqueId(), seconds);
	}

	public void handle_QuitEvent(OfflinePlayer player) {
		if (playtimeTracker.containsKey(player.getUniqueId())) {
			long lastSeconds = playtimeTracker.get(player.getUniqueId()).longValue();
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(lastSeconds * 1000);
			int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
			Calendar newCalendar = Calendar.getInstance();
			newCalendar.setTimeInMillis(System.currentTimeMillis());
			long secondsPassed = (System.currentTimeMillis() / 1000l) - playtimeTracker.get(player.getUniqueId()).longValue();
			playtimeTracker.remove(player.getUniqueId());
			long currentTimeInSeconds = getStoredPlayerPlaytime(player);
			playtimeConfig.getData().set(player.getUniqueId().toString() + ".total", currentTimeInSeconds + secondsPassed);
			long currentTimeInSecondsDay = getStoredPlayerPlaytime(player, dayOfYear);
			playtimeConfig.getData().set(player.getUniqueId().toString() + ".dayOfYear." + dayOfYear, currentTimeInSecondsDay + secondsPassed);
			playtimeConfig.getData().set(player.getUniqueId().toString() + ".lastKnownDayOfYear", dayOfYear);
			playtimeConfig.saveData();
		}
	}

	public void updatePlaytime(Player player) {
		handle_QuitEvent(player);
		handle_JoinEvent(player);
	}

	public boolean onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!(event.getInventory().getTitle() == null) && event.getInventory().getTitle().endsWith("of " + player.getName())) {
			if (!(event.getCurrentItem() == null) && event.getCurrentItem().getType() == Material.EMERALD) {
				if (event.getCurrentItem().hasItemMeta()) {
					String itmDisplayName = event.getCurrentItem().getItemMeta().getDisplayName();
					int action = -2;
					if (itmDisplayName.equals("View previous month")) {
						action = -1;
					} else if (itmDisplayName.equals("View following month")) {
						action = 1;
					}
					if (action > -2) {
						String st = ChatColor.stripColor(event.getInventory().getTitle());
						String month = st.substring(st.indexOf(" ") + 1);
						String targetPlayerName = month.substring(month.indexOf(" of ") + 4);
						month = month.substring(0, month.indexOf(" "));
						int monthInt = ($.retrieveMonthId(month) + action) % 12;
						while (monthInt < 0)
							monthInt = 12 - Math.abs(monthInt);
						openComplexInventory(player, CraftGo.Player.getOfflinePlayer(targetPlayerName), monthInt);
					}
				}
			}
			return true;
		}
		return false;
	}

	public void openComplexInventory(Player player, OfflinePlayer targetPlayer, int monthId) {
		long delay = 0L;
		if (CraftGo.Player.isPocketPlayer(player)) {
			player.closeInventory();
			delay = Server.getInventoryUpdateDelay();
		}
		Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Calendar currentTime = Calendar.getInstance();
				currentTime.setTimeInMillis(System.currentTimeMillis());
				final int exactDayOfYear = currentTime.get(Calendar.DAY_OF_YEAR);
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.DAY_OF_MONTH, 0);
				calendar.set(Calendar.MONTH, monthId);
				calendar.set(Calendar.YEAR, currentTime.get(Calendar.YEAR));
				long[] range7Day = Server.getPlaytimeManager().getRangeOfStoredPlayerPlaytime(targetPlayer, exactDayOfYear - 6, exactDayOfYear + 1);
				long totalTimePlayedInSeconds = 0L;
				for (int i = 0; i < range7Day.length; i++)
					totalTimePlayedInSeconds += range7Day[i];
				int invSize = 45;
				if (CraftGo.Player.isPocketPlayer(player))
					invSize = 54;
				Inventory inventory = Bukkit.createInventory(null, invSize, ChatColor.RESET + " " + $.formatMonthId(monthId) + " of " + targetPlayer.getName());
				String day7total = ChatColor.RESET + $.formatTime((int) totalTimePlayedInSeconds) + " during the past 7 days.";
				ItemStack viewPrevious = $.createMaterial(Material.EMERALD, "View previous month");
				viewPrevious = $.addLore(viewPrevious, new String[] { day7total });
				ItemStack viewFollowing = $.createMaterial(Material.EMERALD, "View following month");
				viewFollowing = $.addLore(viewFollowing, new String[] { day7total });
				inventory.setItem(18, viewPrevious);
				inventory.setItem(26, viewFollowing);
				for (int i = 0; i < inventory.getSize(); i++) {
					int horizontal = i % 7;
					int line = (int) (Math.floor(i / 7) + 1);
					int slot = (horizontal + 1) + (9 * (line - 1));
					if (slot < inventory.getSize()) {
						int dayOfMonth = i + 1;
						calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
						int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
						int monthOf = calendar.get(Calendar.MONTH);
						calendar.set(Calendar.DAY_OF_MONTH, 0);
						if (monthOf == monthId) {
							long timePlayedInSeconds = getStoredPlayerPlaytime(targetPlayer, dayOfYear);
							calendar.set(Calendar.DAY_OF_YEAR, dayOfYear);
							calendar.set(Calendar.SECOND, 0);
							calendar.add(Calendar.SECOND, (int) timePlayedInSeconds);
							String dn = ChatColor.RESET + $.formatTime((int) timePlayedInSeconds) + " on " + $.basicFormatCalendarTime(calendar);
							ItemStack itm = null;
							if (timePlayedInSeconds > 0) {
								itm = $.createMaterial(Material.COOKED_BEEF, dn);
							} else {
								itm = $.createMaterial(Material.COOKED_PORKCHOP, dn);
							}
							if (dayOfYear == exactDayOfYear) {
								if (CraftGo.Player.getProtocolVersion(player) > 314) {
									itm.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
								} else {
									itm.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
								}
							}
							inventory.setItem(slot, itm);
						}
					}
				}
				player.openInventory(inventory);
			}
		}, delay);
	}
}
