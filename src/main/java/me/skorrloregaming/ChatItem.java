package me.skorrloregaming;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.skorrloregaming.ChatItem.ItemRewriter.ItemRewriter_1_11_TO_1_10;
import me.skorrloregaming.ChatItem.ItemRewriter.ItemRewriter_1_9_TO_1_8;
import me.skorrloregaming.commands.ChatItemReloadCmd;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class ChatItem {
	public final static int CFG_VER = 12;
	private static ChatItem instance;
	private ChatEventListener chatEventListener;
	private Storage storage;
	private ProtocolManager pm;
	private ChatPacketListener packetListener;
	private ChatPacketValidator packetValidator;
	private static Class<?> chatMessageTypeClass;
	private static boolean post17 = false;
	private static boolean post111 = false;
	private static boolean post112 = false;
	private static boolean baseComponentAvailable = true;
	private static boolean protocolSupport = false;

	public static void reload(CommandSender sender) {
		ChatItem obj = getInstance();
		obj.pm = ProtocolLibrary.getProtocolManager();
		Server.getChatItemConfig().reloadData();
		obj.storage = new Storage(Server.getChatItemConfig().getData());
		obj.packetListener.setStorage(obj.storage);
		obj.packetValidator.setStorage(obj.storage);
		obj.chatEventListener.setStorage(obj.storage);
		if (sender == null)
			return;
		if (!obj.storage.RELOAD_MESSAGE.isEmpty())
			sender.sendMessage(obj.storage.RELOAD_MESSAGE);
	}

	public static ChatItem getInstance() {
		return instance;
	}

	public void onEnable() {
		instance = this;
		pm = ProtocolLibrary.getProtocolManager();
		storage = new Storage(Server.getChatItemConfig().getData());
		if (isMc18OrLater()) {
			post17 = true;
		}
		if (isMc111OrLater()) {
			post111 = true;
		}
		if (isMc112Orlater()) {
			post112 = true;
			try {
				chatMessageTypeClass = Class.forName("net.minecraft.server." + getVersion(Bukkit.getServer()) + ".ChatMessageType");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		packetListener = new ChatPacketListener(Server.getPlugin(), ListenerPriority.LOW, storage, PacketType.Play.Server.CHAT);
		packetValidator = new ChatPacketValidator(Server.getPlugin(), ListenerPriority.LOWEST, storage, PacketType.Play.Server.CHAT);
		pm.addPacketListener(packetValidator);
		pm.addPacketListener(packetListener);
		if (Link$.isPluginEnabled("ProtocolSupport")) {
			protocolSupport = true;
		}
		Bukkit.getPluginCommand("cireload").setExecutor(new ChatItemReloadCmd());
		chatEventListener = new ChatEventListener(storage);
		Bukkit.getPluginManager().registerEvents(chatEventListener, Server.getPlugin());
		try {
			Class.forName("net.md_5.bungee.api.chat.BaseComponent");
		} catch (ClassNotFoundException e) {
			baseComponentAvailable = false;
		}
	}

	public void onDisable() {
		instance = null;
		post17 = false;
		pm.removePacketListener(packetListener);
		pm.removePacketListener(packetValidator);
	}

	private boolean isMc18OrLater() {
		switch (getVersion(Bukkit.getServer())) {
			case "v1_7_R1":
				return false;
			case "v1_7_R2":
				return false;
			case "v1_7_R3":
				return false;
			case "v1_7_R4":
				return false;
			default:
				return true;
		}
	}

	private boolean isMc111OrLater() {
		switch (getVersion(Bukkit.getServer())) {
			case "v1_7_R1":
				return false;
			case "v1_7_R2":
				return false;
			case "v1_7_R3":
				return false;
			case "v1_7_R4":
				return false;
			case "v1_8_R1":
				return false;
			case "v1_8_R2":
				return false;
			case "v1_8_R3":
				return false;
			case "v1_9_R1":
				return false;
			case "v1_9_R2":
				return false;
			case "v1_10_R1":
				return false;
			case "v1_10_R2":
				return false;
			default:
				return true;
		}
	}

	private boolean isMc112Orlater() {
		switch (getVersion(Bukkit.getServer())) {
			case "v1_7_R1":
				return false;
			case "v1_7_R2":
				return false;
			case "v1_7_R3":
				return false;
			case "v1_7_R4":
				return false;
			case "v1_8_R1":
				return false;
			case "v1_8_R2":
				return false;
			case "v1_8_R3":
				return false;
			case "v1_9_R1":
				return false;
			case "v1_9_R2":
				return false;
			case "v1_10_R1":
				return false;
			case "v1_10_R2":
				return false;
			case "v1_11_R1":
				return false;
			default:
				return true;
		}
	}

	public static String getVersion(org.bukkit.Server server) {
		final String packageName = server.getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}

	public static boolean supportsActionBar() {
		return post17;
	}

	public static boolean supportsShulkerBoxes() {
		return post111;
	}

	public static boolean supportsChatComponentApi() {
		return baseComponentAvailable;
	}

	public static boolean supportsChatTypeEnum() {
		return post112;
	}

	public static JSONManipulator getManipulator() {
		return new JSONManipulatorCurrent();
	}

	public static boolean usesProtocolSupport() {
		return protocolSupport;
	}

	public static Class<?> getChatMessageTypeClass() {
		return chatMessageTypeClass;
	}

	public static enum ProtocolVersion {
		PRE_1_8(0, 5, 0), V1_8_X(6, 47, 1), V1_9_X(49, 110, 2), V1_10_X(201, 210, 3), V1_11_X(301, 316, 4), V1_12_X(317, 340, 5), V1_13_X(383, 404, 5), V1_14_X(477, Integer.MAX_VALUE, 6), INVALID(-1, -1, 6);
		public final int MIN_VER;
		public final int MAX_VER;
		public final int INDEX;
		private static ProtocolVersion serverVersion;
		private static ConcurrentHashMap<String, Integer> PLAYER_VERSION_MAP = new ConcurrentHashMap<>();

		ProtocolVersion(int min, int max, int index) {
			this.MIN_VER = min;
			this.MAX_VER = max;
			this.INDEX = index;
		}

		public static ProtocolVersion getVersion(int protocolVersion) {
			for (ProtocolVersion ver : ProtocolVersion.values()) {
				if (protocolVersion >= ver.MIN_VER && protocolVersion <= ver.MAX_VER) {
					return ver;
				}
			}
			return INVALID;
		}

		public static ProtocolVersion getServerVersion() {
			if (serverVersion == null) {
				String version = ChatItem.getVersion(Bukkit.getServer());
				switch (version) {
					case "v1_7_R1":
						serverVersion = PRE_1_8;
						break;
					case "v1_7_R2":
						serverVersion = PRE_1_8;
						break;
					case "v1_7_R3":
						serverVersion = PRE_1_8;
						break;
					case "v1_8_R1":
						serverVersion = V1_8_X;
						break;
					case "v1_8_R2":
						serverVersion = V1_8_X;
						break;
					case "v1_8_R3":
						serverVersion = V1_8_X;
						break;
					case "v1_9_R1":
						serverVersion = V1_9_X;
						break;
					case "v1_9_R2":
						serverVersion = V1_9_X;
						break;
					case "v1_10_R1":
						serverVersion = V1_10_X;
						break;
					case "v1_10_R2":
						serverVersion = V1_10_X;
						break;
					case "v1_11_R1":
						serverVersion = V1_11_X;
						break;
					case "v1_12_R1":
						serverVersion = V1_12_X;
						break;
					case "v1_12_R2":
						serverVersion = V1_12_X;
						break;
					case "v1_13_R1":
						serverVersion = V1_13_X;
						break;
					case "v1_13_R2":
						serverVersion = V1_13_X;
						break;
					case "v1_14_R1":
						serverVersion = V1_14_X;
						break;
				}
			}
			return serverVersion;
		}

		public static void remapIds(int server, int player, Item item) {
			if (areIdsCompatible(server, player)) {
				return;
			}
			if ((server >= V1_9_X.MIN_VER && player <= V1_8_X.MAX_VER) || (player >= V1_9_X.MIN_VER && server <= V1_8_X.MAX_VER)) {
				if ((server >= V1_9_X.MIN_VER && player <= V1_8_X.MAX_VER)) {
					ItemRewriter_1_9_TO_1_8.reversedToClient(item);
					return;
				}
				ItemRewriter_1_9_TO_1_8.toClient(item);
				return;
			}
			if ((server <= V1_10_X.MAX_VER && player >= V1_11_X.MIN_VER) || (player <= V1_10_X.MAX_VER && server >= V1_11_X.MIN_VER)) {
				if (server <= V1_10_X.MAX_VER && player >= V1_11_X.MIN_VER) {
					ItemRewriter_1_11_TO_1_10.toClient(item);
				} else {
					ItemRewriter_1_11_TO_1_10.reverseToClient(item);
				}
			}
		}

		public static boolean areIdsCompatible(int version1, int version2) {
			if ((version1 >= V1_9_X.MIN_VER && version2 <= V1_8_X.MAX_VER) || (version2 >= V1_9_X.MIN_VER && version1 <= V1_8_X.MAX_VER)) {
				return false;
			}
			if ((version1 <= V1_10_X.MAX_VER && version2 >= V1_11_X.MIN_VER) || (version1 <= V1_10_X.MAX_VER && version2 >= V1_11_X.MIN_VER)) {
				return false;
			}
			return true;
		}

		public static int getClientVersion(final Player p) {
			if (p == null) {
				throw new NullPointerException("Player cannot be null!");
			}
			if (ChatItem.usesProtocolSupport()) {
				return CraftGo.Player.getProtocolVersion(p);
			}
			return getServerVersion().MAX_VER;
		}

		public static String stringifyAdress(InetSocketAddress address) {
			String port = String.valueOf(address.getPort());
			String ip = address.getAddress().getHostAddress();
			return ip + ":" + port;
		}

		public static Map<String, Integer> getPlayerVersionMap() {
			return PLAYER_VERSION_MAP;
		}
	}

	public interface JSONManipulator {
		String parse(String json, List<String> replacements, ItemStack item, String repl, int protocol) throws InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchFieldException, NoSuchMethodException;

		String parseEmpty(String json, List<String> replacements, String repl, List<String> tooltip, Player sender);
	}

	public static class Translator {
		private static final String STYLES = "klmnor";

		public static JsonArray toJson(String old) {
			if (old.lastIndexOf(ChatColor.COLOR_CHAR) == -1) {
				JsonArray arr = new JsonArray();
				JsonObject obj = new JsonObject();
				obj.addProperty("text", old);
				arr.add(obj);
				return arr;
			}
			boolean startsWithCode = old.startsWith(Character.toString(ChatColor.COLOR_CHAR));
			JsonArray message = new JsonArray();
			String[] parts = old.split(Character.toString(ChatColor.COLOR_CHAR));
			JsonObject next = null;
			for (int i = parts.length - 1; i >= 0; --i) {
				String part = parts[i];
				if (part.isEmpty()) {
					continue;
				}
				if (i == 0 && !startsWithCode) {
					JsonObject toAdd = new JsonObject();
					toAdd.addProperty("text", part);
					message.add(toAdd);
					break;
				}
				char code = Character.toLowerCase(part.charAt(0));
				if (!isColorOrStyle(code)) {
					if (next != null) {
						String text = next.get("text").getAsString();
						text = ChatColor.COLOR_CHAR + part + text;
						next.addProperty("text", text);
					} else {
						JsonObject added = new JsonObject();
						added.addProperty("text", ChatColor.COLOR_CHAR + part);
						message.add(added);
						next = added;
					}
					continue;
				}
				if (part.length() == 1) {
					if (next == null)
						continue;
					if (isStyle(code)) {
						next.addProperty(getStyleName(code), true);
					} else {
						if (isAlreadyColored(next)) {
							continue;
						}
						next.addProperty("color", getColorName(code));
					}
					continue;
				}
				JsonObject added = new JsonObject();
				added.addProperty("text", part.substring(1));
				if (isStyle(code)) {
					added.addProperty(getStyleName(code), true);
					message.add(added);
					next = added;
					continue;
				}
				added.addProperty("color", getColorName(code));
				if (next != null) {
					if (!isAlreadyColored(next)) {
						next.addProperty("color", getColorName(code));
					}
				}
				message.add(added);
				next = added;
			}
			int i = message.size() - 1;
			JsonArray orderedMessage = new JsonArray();
			for (JsonElement el : message) {
				orderedMessage.add(el);
			}
			for (JsonElement element : message) {
				JsonObject obj = (JsonObject) element;
				orderedMessage.set(i, obj);
				--i;
			}
			return orderedMessage;
		}

		private static String getColorName(char code) {
			return ChatColor.getByChar(code).name().toLowerCase();
		}

		private static String getStyleName(char code) {
			switch (code) {
				case 'k':
					return "obfuscated";
				case 'l':
					return "bold";
				case 'm':
					return "strikethrough";
				case 'n':
					return "underlined";
				case 'o':
					return "italic";
				case 'r':
					return "reset";
				default:
					return null;
			}
		}

		private static boolean isColorOrStyle(char code) {
			return ChatColor.getByChar(code) != null;
		}

		private static boolean isStyle(char c) {
			return STYLES.indexOf(c) != -1;
		}

		private static boolean isAlreadyColored(JsonObject obj) {
			return obj.has("color");
		}
	}

	public static class ChatEventListener implements Listener {
		private Storage c;
		public final static char SEPARATOR = ((char) 0x0007);
		private final static String LEFT = "{remaining}";
		private final HashMap<String, Long> COOLDOWNS = new HashMap<>();

		public ChatEventListener(Storage storage) {
			c = storage;
		}

		private String calculateTime(long seconds) {
			if (seconds < 60) {
				return seconds + c.SECONDS;
			}
			if (seconds < 3600) {
				StringBuilder builder = new StringBuilder();
				int minutes = (int) seconds / 60;
				builder.append(minutes).append(c.MINUTES);
				int secs = (int) seconds - minutes * 60;
				if (secs != 0) {
					builder.append(" ").append(secs).append(c.SECONDS);
				}
				return builder.toString();
			}
			StringBuilder builder = new StringBuilder();
			int hours = (int) seconds / 3600;
			builder.append(hours).append(c.HOURS);
			int minutes = (int) (seconds / 60) - (hours * 60);
			if (minutes != 0) {
				builder.append(" ").append(minutes).append(c.MINUTES);
			}
			int secs = (int) (seconds - ((seconds / 60) * 60));
			if (secs != 0) {
				builder.append(" ").append(secs).append(c.SECONDS);
			}
			return builder.toString();
		}

		private int countOccurrences(String findStr, String str) {
			int lastIndex = 0;
			int count = 0;
			while (lastIndex != -1) {
				lastIndex = str.indexOf(findStr, lastIndex);
				if (lastIndex != -1) {
					count++;
					lastIndex += findStr.length();
				}
			}
			return count;
		}

		@EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
		public void onChat(final AsyncPlayerChatEvent e) {
			if (e.getMessage().indexOf(SEPARATOR) != -1) {
				String msg = e.getMessage().replace(Character.toString(SEPARATOR), "");
				e.setMessage(msg);
			}
			boolean found = false;
			for (String rep : c.PLACEHOLDERS)
				if (e.getMessage().contains(rep)) {
					found = true;
					break;
				}
			if (!found) {
				return;
			}
			Player p = e.getPlayer();
			if (!p.hasPermission("chatitem.use")) {
				if (!c.LET_MESSAGE_THROUGH) {
					e.setCancelled(true);
				}
				if (!c.NO_PERMISSION_MESSAGE.isEmpty() && c.SHOW_NO_PERM_NORMAL) {
					p.sendMessage(c.NO_PERMISSION_MESSAGE);
				}
				return;
			}
			if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
				if (c.DENY_IF_NO_ITEM) {
					e.setCancelled(true);
					if (!c.DENY_MESSAGE.isEmpty())
						e.getPlayer().sendMessage(c.DENY_MESSAGE);
					return;
				}
				if (c.HAND_DISABLED) {
					return;
				}
			}
			if (c.COOLDOWN > 0 && !p.hasPermission("chatitem.ignore-cooldown")) {
				if (COOLDOWNS.containsKey(p.getName())) {
					long start = COOLDOWNS.get(p.getName());
					long current = System.currentTimeMillis() / 1000;
					long elapsed = current - start;
					if (elapsed >= c.COOLDOWN) {
						COOLDOWNS.remove(p.getName());
					} else {
						if (!c.LET_MESSAGE_THROUGH) {
							e.setCancelled(true);
						}
						if (!c.COOLDOWN_MESSAGE.isEmpty()) {
							long left = (start + c.COOLDOWN) - current;
							p.sendMessage(c.COOLDOWN_MESSAGE.replace(LEFT, calculateTime(left)));
						}
						return;
					}
				}
			}
			String s = e.getMessage();
			for (String placeholder : c.PLACEHOLDERS) {
				s = s.replace(placeholder, c.PLACEHOLDERS.get(0));
			}
			int occurrences = countOccurrences(c.PLACEHOLDERS.get(0), s);
			if (occurrences > c.LIMIT) {
				e.setCancelled(true);
				if (c.LIMIT_MESSAGE.isEmpty()) {
					return;
				}
				e.getPlayer().sendMessage(c.LIMIT_MESSAGE);
				return;
			}
			String oldmsg = e.getMessage();
			StringBuilder sb = new StringBuilder(oldmsg);
			sb.append(SEPARATOR).append(e.getPlayer().getName());
			e.setMessage(sb.toString());
			Bukkit.getConsoleSender().sendMessage(String.format(e.getFormat(), e.getPlayer().getDisplayName(), oldmsg));
			if (!p.hasPermission("chatitem.ignore-cooldown")) {
				COOLDOWNS.put(p.getName(), System.currentTimeMillis() / 1000);
			}
		}

		@EventHandler(priority = EventPriority.LOWEST)
		public void onCommand(final PlayerCommandPreprocessEvent e) {
			if (e.getMessage().indexOf(SEPARATOR) != -1) {
				String msg = e.getMessage().replace(Character.toString(SEPARATOR), "");
				e.setMessage(msg);
			}
			String commandString = e.getMessage().split(" ")[0].replaceAll("^/+", "");
			Command cmd = Bukkit.getPluginCommand(commandString);
			if (cmd == null) {
				if (!c.ALLOWED_DEFAULT_COMMANDS.contains(commandString)) {
					return;
				}
			} else {
				if (!c.ALLOWED_PLUGIN_COMMANDS.contains(cmd)) {
					return;
				}
			}
			Player p = e.getPlayer();
			boolean found = false;
			for (String rep : c.PLACEHOLDERS) {
				if (e.getMessage().contains(rep)) {
					found = true;
					break;
				}
			}
			if (!found) {
				return;
			}
			if (!p.hasPermission("chatitem.use")) {
				if (!c.NO_PERMISSION_MESSAGE.isEmpty() && c.SHOW_NO_PERM_COMMAND) {
					p.sendMessage(c.NO_PERMISSION_MESSAGE);
				}
				e.setCancelled(true);
				return;
			}
			if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
				if (c.DENY_IF_NO_ITEM) {
					e.setCancelled(true);
					if (!c.DENY_MESSAGE.isEmpty()) {
						e.getPlayer().sendMessage(c.DENY_MESSAGE);
					}
				}
				if (c.HAND_DISABLED) {
					return;
				}
			}
			if (c.COOLDOWN > 0 && !p.hasPermission("chatitem.ignore-cooldown")) {
				if (COOLDOWNS.containsKey(p.getName())) {
					long start = COOLDOWNS.get(p.getName());
					long current = System.currentTimeMillis() / 1000;
					long elapsed = current - start;
					if (elapsed >= c.COOLDOWN) {
						COOLDOWNS.remove(p.getName());
					} else {
						if (!c.LET_MESSAGE_THROUGH) {
							e.setCancelled(true);
						}
						if (!c.COOLDOWN_MESSAGE.isEmpty()) {
							long left = (start + c.COOLDOWN) - current;
							p.sendMessage(c.COOLDOWN_MESSAGE.replace(LEFT, calculateTime(left)));
						}
						return;
					}
				}
			}
			String s = e.getMessage();
			for (String placeholder : c.PLACEHOLDERS) {
				s = s.replace(placeholder, c.PLACEHOLDERS.get(0));
			}
			int occurrences = countOccurrences(c.PLACEHOLDERS.get(0), s);
			if (occurrences > c.LIMIT) {
				e.setCancelled(true);
				if (c.LIMIT_MESSAGE.isEmpty()) {
					return;
				}
				e.getPlayer().sendMessage(c.LIMIT_MESSAGE);
				return;
			}
			StringBuilder sb = new StringBuilder(e.getMessage());
			sb.append(SEPARATOR).append(e.getPlayer().getName());
			e.setMessage(sb.toString());
			if (!p.hasPermission("chatitem.ignore-cooldown")) {
				COOLDOWNS.put(p.getName(), System.currentTimeMillis() / 1000);
			}
		}

		public void setStorage(Storage st) {
			c = st;
		}
	}

	public static class ChatPacketListener extends PacketAdapter {
		private final static String NAME = "{name}";
		private final static String AMOUNT = "{amount}";
		private final static String TIMES = "{times}";
		private final static List<Material> SHULKER_BOXES = new ArrayList<>();
		private Storage c;

		public ChatPacketListener(Plugin plugin, ListenerPriority listenerPriority, Storage s, PacketType... types) {
			super(plugin, listenerPriority, types);
			if (ChatItem.supportsShulkerBoxes()) {
				SHULKER_BOXES.addAll(Arrays.asList(Material.BLACK_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIME_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX));
			}
			c = s;
		}

		private static String materialToName(Material m) {
			if (m.equals(Material.TNT)) {
				return "TNT";
			}
			String orig = m.toString().toLowerCase();
			String[] splits = orig.split("_");
			StringBuilder sb = new StringBuilder(orig.length());
			int pos = 0;
			for (String split : splits) {
				sb.append(split);
				int loc = sb.lastIndexOf(split);
				char charLoc = sb.charAt(loc);
				if (!(split.equalsIgnoreCase("of") || split.equalsIgnoreCase("and") || split.equalsIgnoreCase("with") || split.equalsIgnoreCase("on")))
					sb.setCharAt(loc, Character.toUpperCase(charLoc));
				if (pos != splits.length - 1)
					sb.append(' ');
				++pos;
			}
			return sb.toString();
		}

		private void stripData(ItemStack i) {
			if (i == null) {
				return;
			}
			if (i.getType().equals(Material.AIR)) {
				return;
			}
			if (!i.hasItemMeta()) {
				return;
			}
			ItemMeta im = Bukkit.getItemFactory().getItemMeta(i.getType());
			ItemMeta original = i.getItemMeta();
			if (original.hasDisplayName()) {
				im.setDisplayName(original.getDisplayName());
			}
			i.setItemMeta(im);
		}

		@Override
		public void onPacketSending(final PacketEvent e) {
			try {
				final PacketContainer packet = e.getPacket();
				if (!packet.getMeta("parse").isPresent())
					return;
				final boolean usesBaseComponents = (boolean) packet.getMeta("base-component").orElse(null);
				e.setCancelled(true);
				Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), () -> {
					String json = (String) packet.getMeta("json").orElse(null);
					int topIndex = -1;
					String name = null;
					for (Player p : Bukkit.getOnlinePlayers()) {
						String pname = "\\u0007" + p.getName();
						if (!json.contains(pname)) {
							continue;
						}
						int index = json.lastIndexOf(pname) + pname.length();
						if (index > topIndex) {
							topIndex = index;
							name = pname.replace("\\u0007", "");
						}
					}
					if (name == null)
						return;
					Player p = Bukkit.getPlayer(name);
					StringBuilder builder = new StringBuilder(json);
					builder.replace(topIndex - (name.length() + 6), topIndex, "");
					json = builder.toString();
					String message = null;
					try {
						if (!p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) {
							ItemStack copy = p.getInventory().getItemInMainHand().clone();
							if (copy.getType().equals(Material.WRITABLE_BOOK) || copy.getType().equals(Material.WRITTEN_BOOK)) {
								BookMeta bm = (BookMeta) copy.getItemMeta();
								bm.setPages(Collections.emptyList());
								copy.setItemMeta(bm);
							} else {
								if (ChatItem.supportsShulkerBoxes()) {
									if (SHULKER_BOXES.contains(copy.getType())) {
										if (copy.hasItemMeta()) {
											BlockStateMeta bsm = (BlockStateMeta) copy.getItemMeta();
											if (bsm.hasBlockState()) {
												ShulkerBox sb = (ShulkerBox) bsm.getBlockState();
												for (ItemStack item : sb.getInventory()) {
													stripData(item);
												}
												bsm.setBlockState(sb);
											}
											copy.setItemMeta(bsm);
										}
									}
								}
							}
							message = ChatItem.getManipulator().parse(json, c.PLACEHOLDERS, copy, styleItem(copy, c), ProtocolVersion.getClientVersion(e.getPlayer()));
						} else {
							if (!c.HAND_DISABLED) {
								message = ChatItem.getManipulator().parseEmpty(json, c.PLACEHOLDERS, c.HAND_NAME, c.HAND_TOOLTIP, p);
							}
						}
					} catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException | NoSuchMethodException e1) {
						e1.printStackTrace();
					}
					if (message != null) {
						if (!usesBaseComponents) {
							packet.getChatComponents().writeSafely(0, WrappedChatComponent.fromJson(message));
						} else {
							packet.getSpecificModifier(BaseComponent[].class).writeSafely(0, ComponentSerializer.parse(message));
						}
						try {
							ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), packet, false);
						} catch (InvocationTargetException e1) {
							e1.printStackTrace();
						}
					} else {
						try {
							ProtocolLibrary.getProtocolManager().sendServerPacket(e.getPlayer(), packet, false);
						} catch (InvocationTargetException e1) {
							e1.printStackTrace();
						}
					}
				});
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Error occured in ChatPacketListener.class");
			}
		}

		public void setStorage(Storage st) {
			c = st;
		}

		public static String styleItem(ItemStack item, Storage c) {
			String replacer = c.NAME_FORMAT;
			String amount = c.AMOUNT_FORMAT;
			boolean dname = item.hasItemMeta() ? item.getItemMeta().hasDisplayName() : false;
			if (item.getAmount() == 1) {
				if (c.FORCE_ADD_AMOUNT) {
					amount = amount.replace(TIMES, "1");
					replacer = replacer.replace(AMOUNT, amount);
				} else
					replacer = replacer.replace(AMOUNT, "");
			} else {
				amount = amount.replace(TIMES, String.valueOf(item.getAmount()));
				replacer = replacer.replace(AMOUNT, amount);
			}
			if (dname) {
				String trp = item.getItemMeta().getDisplayName();
				if (c.COLOR_IF_ALREADY_COLORED) {
					replacer = replacer.replace(NAME, ChatColor.stripColor(trp));
				} else
					replacer = replacer.replace(NAME, trp);
			} else {
				HashMap<Short, String> translationSection = c.TRANSLATIONS.get(item.getType().name());
				if (translationSection == null) {
					String trp = materialToName(item.getType());
					replacer = replacer.replace(NAME, trp);
				} else {
					String translated = translationSection.get(item.getDurability());
					if (translated != null) {
						replacer = replacer.replace(NAME, translated);
					} else
						replacer = replacer.replace(NAME, materialToName(item.getType()));
				}
			}
			return replacer;
		}
	}

	public class ChatPacketValidator extends PacketAdapter {
		private Storage c;

		public ChatPacketValidator(Plugin plugin, ListenerPriority listenerPriority, Storage s, PacketType... types) {
			super(plugin, listenerPriority, types);
			c = s;
		}

		public void onPacketSending(PacketEvent e) {
			try {
				if (ChatItem.supportsActionBar()) {
					if (ChatItem.supportsChatTypeEnum()) {
						if (((Enum<?>) e.getPacket().getSpecificModifier(ChatItem.getChatMessageTypeClass()).read(0)).name().equals("GAME_INFO"))
							return;
					} else if (e.getPacket().getBytes().readSafely(0) == (byte) 2)
						return;
				}
				boolean usesBaseComponents = false;
				PacketContainer packet = e.getPacket();
				String json;
				if (packet.getChatComponents().readSafely(0) == null) {
					if (ChatItem.supportsChatComponentApi()) {
						BaseComponent[] components = packet.getSpecificModifier(BaseComponent[].class).readSafely(0);
						if (components == null) {
							return;
						}
						json = ComponentSerializer.toString(components);
						usesBaseComponents = true;
					} else
						return;
				} else
					json = packet.getChatComponents().readSafely(0).getJson();
				boolean found = false;
				for (String rep : c.PLACEHOLDERS)
					if (json.contains(rep)) {
						found = true;
						break;
					}
				if (!found)
					return;
				if (json.lastIndexOf("\\u0007") == -1)
					return;
				packet.setMeta("parse", true);
				packet.setMeta("base-component", usesBaseComponents);
				packet.setMeta("json", json);
			} catch (Exception ex) {
				System.out.println("Error occured in ChatPacketValidator.class");
				ex.printStackTrace();
			}
		}

		public void setStorage(Storage st) {
			c = st;
		}
	}

	public class HandshakeListener extends PacketAdapter {
		public HandshakeListener(Plugin plugin, ListenerPriority listenerPriority, PacketType... types) {
			super(plugin, listenerPriority, types);
		}

		@Override
		public void onPacketReceiving(final PacketEvent e) {
			PacketType.Protocol p = e.getPacket().getProtocols().read(0);
			if (p == PacketType.Protocol.STATUS || p == PacketType.Protocol.LEGACY) {
				return;
			}
			final int version = e.getPacket().getIntegers().readSafely(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Server.getPlugin(), () -> ProtocolVersion.getPlayerVersionMap().put(ProtocolVersion.stringifyAdress(e.getPlayer().getAddress()), version), 10L);
		}
	}

	public static class Item {
		private String id;
		private byte amount;
		private short data;
		private CompoundTag tag;

		public CompoundTag getTag() {
			return tag;
		}

		public void setTag(CompoundTag newTag) {
			this.tag = newTag;
		}

		public void setId(String newId) {
			this.id = newId;
		}

		public void setData(short newData) {
			data = newData;
		}

		public String getId() {
			return id;
		}

		public short getData() {
			return data;
		}

		public void setAmount(byte newAmount) {
			this.amount = newAmount;
		}

		public byte getAmount() {
			return amount;
		}
	}

	public static class ItemRewriter {
		public static class ItemRewriter_1_11_TO_1_10 {
			public static BiMap<String, String> oldToNewNames = HashBiMap.create();

			static {
				oldToNewNames.put("AreaEffectCloud", "minecraft:area_effect_cloud");
				oldToNewNames.put("ArmorStand", "minecraft:armor_stand");
				oldToNewNames.put("Arrow", "minecraft:arrow");
				oldToNewNames.put("Bat", "minecraft:bat");
				oldToNewNames.put("Blaze", "minecraft:blaze");
				oldToNewNames.put("Boat", "minecraft:boat");
				oldToNewNames.put("CaveSpider", "minecraft:cave_spider");
				oldToNewNames.put("Chicken", "minecraft:chicken");
				oldToNewNames.put("Cow", "minecraft:cow");
				oldToNewNames.put("Creeper", "minecraft:creeper");
				oldToNewNames.put("Donkey", "minecraft:donkey");
				oldToNewNames.put("DragonFireball", "minecraft:dragon_fireball");
				oldToNewNames.put("ElderGuardian", "minecraft:elder_guardian");
				oldToNewNames.put("EnderCrystal", "minecraft:ender_crystal");
				oldToNewNames.put("EnderDragon", "minecraft:ender_dragon");
				oldToNewNames.put("Enderman", "minecraft:enderman");
				oldToNewNames.put("Endermite", "minecraft:endermite");
				oldToNewNames.put("EntityHorse", "minecraft:horse");
				oldToNewNames.put("EyeOfEnderSignal", "minecraft:eye_of_ender_signal");
				oldToNewNames.put("FallingSand", "minecraft:falling_block");
				oldToNewNames.put("Fireball", "minecraft:fireball");
				oldToNewNames.put("FireworksRocketEntity", "minecraft:fireworks_rocket");
				oldToNewNames.put("Ghast", "minecraft:ghast");
				oldToNewNames.put("Giant", "minecraft:giant");
				oldToNewNames.put("Guardian", "minecraft:guardian");
				oldToNewNames.put("Husk", "minecraft:husk");
				oldToNewNames.put("Item", "minecraft:item");
				oldToNewNames.put("ItemFrame", "minecraft:item_frame");
				oldToNewNames.put("LavaSlime", "minecraft:magma_cube");
				oldToNewNames.put("LeashKnot", "minecraft:leash_knot");
				oldToNewNames.put("MinecartChest", "minecraft:chest_minecart");
				oldToNewNames.put("MinecartCommandBlock", "minecraft:commandblock_minecart");
				oldToNewNames.put("MinecartFurnace", "minecraft:furnace_minecart");
				oldToNewNames.put("MinecartHopper", "minecraft:hopper_minecart");
				oldToNewNames.put("MinecartRideable", "minecraft:minecart");
				oldToNewNames.put("MinecartSpawner", "minecraft:spawner_minecart");
				oldToNewNames.put("MinecartTNT", "minecraft:tnt_minecart");
				oldToNewNames.put("Mule", "minecraft:mule");
				oldToNewNames.put("MushroomCow", "minecraft:mooshroom");
				oldToNewNames.put("Ozelot", "minecraft:ocelot");
				oldToNewNames.put("Painting", "minecraft:painting");
				oldToNewNames.put("Pig", "minecraft:pig");
				oldToNewNames.put("PigZombie", "minecraft:zombie_pigman");
				oldToNewNames.put("PolarBear", "minecraft:polar_bear");
				oldToNewNames.put("PrimedTnt", "minecraft:tnt");
				oldToNewNames.put("Rabbit", "minecraft:rabbit");
				oldToNewNames.put("Sheep", "minecraft:sheep");
				oldToNewNames.put("Shulker", "minecraft:shulker");
				oldToNewNames.put("ShulkerBullet", "minecraft:shulker_bullet");
				oldToNewNames.put("Silverfish", "minecraft:silverfish");
				oldToNewNames.put("Skeleton", "minecraft:skeleton");
				oldToNewNames.put("SkeletonHorse", "minecraft:skeleton_horse");
				oldToNewNames.put("Slime", "minecraft:slime");
				oldToNewNames.put("SmallFireball", "minecraft:small_fireball");
				oldToNewNames.put("Snowball", "minecraft:snowball");
				oldToNewNames.put("SnowMan", "minecraft:snowman");
				oldToNewNames.put("SpectralArrow", "minecraft:spectral_arrow");
				oldToNewNames.put("Spider", "minecraft:spider");
				oldToNewNames.put("Squid", "minecraft:squid");
				oldToNewNames.put("Stray", "minecraft:stray");
				oldToNewNames.put("ThrownEgg", "minecraft:egg");
				oldToNewNames.put("ThrownEnderpearl", "minecraft:ender_pearl");
				oldToNewNames.put("ThrownExpBottle", "minecraft:xp_bottle");
				oldToNewNames.put("ThrownPotion", "minecraft:potion");
				oldToNewNames.put("Villager", "minecraft:villager");
				oldToNewNames.put("VillagerGolem", "minecraft:villager_golem");
				oldToNewNames.put("Witch", "minecraft:witch");
				oldToNewNames.put("WitherBoss", "minecraft:wither");
				oldToNewNames.put("WitherSkeleton", "minecraft:wither_skeleton");
				oldToNewNames.put("WitherSkull", "minecraft:wither_skull");
				oldToNewNames.put("Wolf", "minecraft:wolf");
				oldToNewNames.put("XPOrb", "minecraft:xp_orb");
				oldToNewNames.put("Zombie", "minecraft:zombie");
				oldToNewNames.put("ZombieHorse", "minecraft:zombie_horse");
				oldToNewNames.put("ZombieVillager", "minecraft:zombie_villager");
			}

			public static void toClient(Item item) {
				if (hasEntityTag(item)) {
					CompoundTag entityTag = item.getTag().get("EntityTag");
					if (entityTag.get("id") instanceof StringTag) {
						StringTag id = entityTag.get("id");
						if (oldToNewNames.containsKey(id.getValue())) {
							id.setValue(oldToNewNames.get(id.getValue()));
						}
					}
				}
			}

			public static void reverseToClient(Item item) {
				if (hasEntityTag(item)) {
					CompoundTag entityTag = item.getTag().get("EntityTag");
					if (entityTag.get("id") instanceof StringTag) {
						StringTag id = entityTag.get("id");
						if (oldToNewNames.containsKey(id.getValue())) {
							id.setValue(oldToNewNames.get(id.getValue()));
						}
					}
				}
			}

			public static boolean hasEntityTag(Item item) {
				if (item != null && item.getId().equals("minecraft:spawn_egg")) {
					CompoundTag tag = item.getTag();
					if (tag != null && tag.contains("EntityTag") && tag.get("EntityTag") instanceof CompoundTag) {
						if (((CompoundTag) tag.get("EntityTag")).get("id") instanceof StringTag) {
							return true;
						}
					}
				}
				return false;
			}
		}

		public static class ItemRewriter_1_9_TO_1_8 {
			public static final Map<String, Integer> ENTTIY_NAME_TO_ID = new HashMap<>();
			public static final Map<Integer, String> ENTTIY_ID_TO_NAME = new HashMap<>();
			public static final Map<String, Integer> POTION_NAME_TO_ID = new HashMap<>();
			public static final Map<Integer, String> POTION_ID_TO_NAME = new HashMap<>();
			public static final Map<Integer, Integer> POTION_INDEX = new HashMap<>();

			static {
				/* Entities */
				registerEntity(1, "Item");
				registerEntity(2, "XPOrb");
				registerEntity(7, "ThrownEgg");
				registerEntity(8, "LeashKnot");
				registerEntity(9, "Painting");
				registerEntity(10, "Arrow");
				registerEntity(11, "Snowball");
				registerEntity(12, "Fireball");
				registerEntity(13, "SmallFireball");
				registerEntity(14, "ThrownEnderpearl");
				registerEntity(15, "EyeOfEnderSignal");
				registerEntity(16, "ThrownPotion");
				registerEntity(17, "ThrownExpBottle");
				registerEntity(18, "ItemFrame");
				registerEntity(19, "WitherSkull");
				registerEntity(20, "PrimedTnt");
				registerEntity(21, "FallingSand");
				registerEntity(22, "FireworksRocketEntity");
				registerEntity(30, "ArmorStand");
				registerEntity(40, "MinecartCommandBlock");
				registerEntity(41, "Boat");
				registerEntity(42, "MinecartRideable");
				registerEntity(43, "MinecartChest");
				registerEntity(44, "MinecartFurnace");
				registerEntity(45, "MinecartTNT");
				registerEntity(46, "MinecartHopper");
				registerEntity(47, "MinecartSpawner");
				registerEntity(48, "Mob");
				registerEntity(49, "Monster");
				registerEntity(50, "Creeper");
				registerEntity(51, "Skeleton");
				registerEntity(52, "Spider");
				registerEntity(53, "Giant");
				registerEntity(54, "Zombie");
				registerEntity(55, "Slime");
				registerEntity(56, "Ghast");
				registerEntity(57, "PigZombie");
				registerEntity(58, "Enderman");
				registerEntity(59, "CaveSpider");
				registerEntity(60, "Silverfish");
				registerEntity(61, "Blaze");
				registerEntity(62, "LavaSlime");
				registerEntity(63, "EnderDragon");
				registerEntity(64, "WitherBoss");
				registerEntity(65, "Bat");
				registerEntity(66, "Witch");
				registerEntity(67, "Endermite");
				registerEntity(68, "Guardian");
				registerEntity(90, "Pig");
				registerEntity(91, "Sheep");
				registerEntity(92, "Cow");
				registerEntity(93, "Chicken");
				registerEntity(94, "Squid");
				registerEntity(95, "Wolf");
				registerEntity(96, "MushroomCow");
				registerEntity(97, "SnowMan");
				registerEntity(98, "Ozelot");
				registerEntity(99, "VillagerGolem");
				registerEntity(100, "EntityHorse");
				registerEntity(101, "Rabbit");
				registerEntity(120, "Villager");
				registerEntity(200, "EnderCrystal");
				/* Potions */
				registerPotion(-1, "empty");
				registerPotion(0, "water");
				registerPotion(64, "mundane");
				registerPotion(32, "thick");
				registerPotion(16, "awkward");
				registerPotion(8198, "night_vision");
				registerPotion(8262, "long_night_vision");
				registerPotion(8206, "invisibility");
				registerPotion(8270, "long_invisibility");
				registerPotion(8203, "leaping");
				registerPotion(8267, "long_leaping");
				registerPotion(8235, "strong_leaping");
				registerPotion(8195, "fire_resistance");
				registerPotion(8259, "long_fire_resistance");
				registerPotion(8194, "swiftness");
				registerPotion(8258, "long_swiftness");
				registerPotion(8226, "strong_swiftness");
				registerPotion(8202, "slowness");
				registerPotion(8266, "long_slowness");
				registerPotion(8205, "water_breathing");
				registerPotion(8269, "long_water_breathing");
				registerPotion(8261, "healing");
				registerPotion(8229, "strong_healing");
				registerPotion(8204, "harming");
				registerPotion(8236, "strong_harming");
				registerPotion(8196, "poison");
				registerPotion(8260, "long_poison");
				registerPotion(8228, "strong_poison");
				registerPotion(8193, "regeneration");
				registerPotion(8257, "long_regeneration");
				registerPotion(8225, "strong_regeneration");
				registerPotion(8201, "strength");
				registerPotion(8265, "long_strength");
				registerPotion(8233, "strong_strength");
				registerPotion(8200, "weakness");
				registerPotion(8264, "long_weakness");
			}

			public static void toClient(Item item) {
				if (item != null) {
					if (item.getId().equals("minecraft:spawn_egg") && item.getData() != 0) {
						CompoundTag tag = item.getTag();
						if (tag == null) {
							tag = new CompoundTag("tag");
						}
						CompoundTag entityTag = new CompoundTag("EntityTag");
						if (ENTTIY_ID_TO_NAME.containsKey((int) item.getData())) {
							StringTag id = new StringTag("id", ENTTIY_ID_TO_NAME.get((int) item.getData()));
							entityTag.put(id);
							tag.put(entityTag);
						}
						item.setTag(tag);
						item.setData((short) 0);
					}
					if (item.getId().equals("minecraft:potion")) {
						CompoundTag tag = item.getTag();
						if (tag == null) {
							tag = new CompoundTag("tag");
						}
						if (item.getData() >= 16384) {
							item.setId("minecraft:splash_potion");
							item.setData((short) (item.getData() - 8192));
						}
						String name = potionNameFromDamage(item.getData());
						StringTag potion = new StringTag("Potion", "minecraft:" + name);
						tag.put(potion);
						item.setTag(tag);
						item.setData((short) 0);
					}
				}
			}

			public static void reversedToClient(Item item) {
				if (item != null) {
					if (item.getId().equals("minecraft:spawn_egg") && item.getData() == 0) {
						CompoundTag tag = item.getTag();
						int data = 0;
						if (tag != null) {
							if (tag.get("EntityTag") instanceof CompoundTag) {
								CompoundTag entityTag = tag.get("EntityTag");
								if (entityTag.get("id") instanceof StringTag) {
									StringTag id = entityTag.get("id");
									if (ENTTIY_NAME_TO_ID.containsKey(id.getValue()))
										data = ENTTIY_NAME_TO_ID.get(id.getValue());
								}
								tag.remove("EntityTag");
							}
						}
						item.setTag(tag);
						item.setData((short) data);
					}
					if (item.getId().equals("minecraft:potion")) {
						CompoundTag tag = item.getTag();
						int data = 0;
						if (tag != null) {
							if (tag.get("Potion") instanceof StringTag) {
								StringTag potion = tag.get("Potion");
								String potionName = potion.getValue().replace("minecraft:", "");
								if (POTION_NAME_TO_ID.containsKey(potionName)) {
									data = POTION_NAME_TO_ID.get(potionName);
								}
								tag.remove("Potion");
							}
						}
						item.setTag(tag);
						item.setData((short) data);
					}
					if (item.getId().equals("minecraft:splash_potion")) {
						CompoundTag tag = item.getTag();
						int data = 0;
						item.setId("minecraft:potion");
						if (tag != null) {
							if (tag.get("Potion") instanceof StringTag) {
								StringTag potion = tag.get("Potion");
								String potionName = potion.getValue().replace("minecraft:", "");
								if (POTION_NAME_TO_ID.containsKey(potionName)) {
									data = POTION_NAME_TO_ID.get(potionName) + 8192;
								}
								tag.remove("Potion");
							}
						}
						item.setTag(tag);
						item.setData((short) data);
					}
				}
			}

			public static String potionNameFromDamage(short damage) {
				String cached = POTION_ID_TO_NAME.get((int) damage);
				if (cached != null) {
					return cached;
				}
				if (damage == 0) {
					return "water";
				}
				int effect = damage & 0xF;
				int name = damage & 0x3F;
				boolean enhanced = (damage & 0x20) > 0;
				boolean extended = (damage & 0x40) > 0;
				boolean canEnhance = true;
				boolean canExtend = true;
				String id;
				switch (effect) {
					case 1:
						id = "regeneration";
						break;
					case 2:
						id = "swiftness";
						break;
					case 3:
						id = "fire_resistance";
						canEnhance = false;
						break;
					case 4:
						id = "poison";
						break;
					case 5:
						id = "healing";
						canExtend = false;
						break;
					case 6:
						id = "night_vision";
						canEnhance = false;
						break;
					case 8:
						id = "weakness";
						canEnhance = false;
						break;
					case 9:
						id = "strength";
						break;
					case 10:
						id = "slowness";
						canEnhance = false;
						break;
					case 11:
						id = "leaping";
						break;
					case 12:
						id = "harming";
						canExtend = false;
						break;
					case 13:
						id = "water_breathing";
						canEnhance = false;
						break;
					case 14:
						id = "invisibility";
						canEnhance = false;
						break;
					default:
						canEnhance = false;
						canExtend = false;
						switch (name) {
							case 0:
								id = "mundane";
								break;
							case 16:
								id = "awkward";
								break;
							case 32:
								id = "thick";
								break;
							default:
								id = "empty";
						}
				}
				if (effect > 0) {
					if (canEnhance && enhanced) {
						id = "strong_" + id;
					} else if (canExtend && extended) {
						id = "long_" + id;
					}
				}
				return id;
			}

			public static void registerEntity(Integer id, String name) {
				ENTTIY_ID_TO_NAME.put(id, name);
				ENTTIY_NAME_TO_ID.put(name, id);
			}

			public static void registerPotion(Integer id, String name) {
				POTION_INDEX.put(id, POTION_ID_TO_NAME.size());
				POTION_ID_TO_NAME.put(id, name);
				POTION_NAME_TO_ID.put(name, id);
			}
		}
	}

	public static class Reflect {
		private static String versionString;

		private static String getVersion() {
			if (versionString == null) {
				String name = Bukkit.getServer().getClass().getPackage().getName();
				versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
			}
			return versionString;
		}

		public static Class<?> getNMSClass(String nmsClassName) {
			String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
			Class<?> clazz;
			try {
				clazz = Class.forName(clazzName);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			return clazz;
		}

		public static Class<?> getOBCClass(String obcClassName) {
			String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
			Class<?> clazz;
			try {
				clazz = Class.forName(clazzName);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			return clazz;
		}

		public static Class<?> getOBCEntityClass(String obcClassName) {
			String clazzName = "org.bukkit.craftbukkit." + getVersion() + "entity." + obcClassName;
			Class<?> clazz;
			try {
				clazz = Class.forName(clazzName);
			} catch (Throwable t) {
				t.printStackTrace();
				return null;
			}
			return clazz;
		}

		public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
			try {
				return clazz.getMethod(methodName, params);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public static Field getField(Class<?> clazz, String fieldName) {
			try {
				Field f = clazz.getDeclaredField(fieldName);
				f.setAccessible(true);
				return f;
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public static class Storage {
		private FileConfiguration conf;
		public final HashMap<String, HashMap<Short, String>> TRANSLATIONS = new HashMap<>();
		public final Boolean COLOR_IF_ALREADY_COLORED;
		public final Boolean FORCE_ADD_AMOUNT;
		public final Boolean LET_MESSAGE_THROUGH;
		public final Boolean DENY_IF_NO_ITEM;
		public final Boolean HAND_DISABLED;
		public final Boolean SHOW_NO_PERM_NORMAL;
		public final Boolean SHOW_NO_PERM_COMMAND;
		public final String HAND_NAME;
		public final String NAME_FORMAT;
		public final String AMOUNT_FORMAT;
		public final String NO_PERMISSION_MESSAGE;
		public final String DENY_MESSAGE;
		public final String LIMIT_MESSAGE;
		public final String RELOAD_MESSAGE;
		public final String COOLDOWN_MESSAGE;
		public final String SECONDS;
		public final String MINUTES;
		public final String HOURS;
		private final Integer CONFIG_VERSION;
		public final Long COOLDOWN;
		public final Integer LIMIT;
		public final List<Command> ALLOWED_PLUGIN_COMMANDS = new ArrayList<>();
		public final List<String> ALLOWED_DEFAULT_COMMANDS = new ArrayList<>();
		public final List<String> PLACEHOLDERS;
		public final List<String> HAND_TOOLTIP;

		public Storage(FileConfiguration cnf) {
			this.conf = cnf;
			CONFIG_VERSION = conf.getInt("config-version");
			checkConfigVersion();
			Set<String> keys = conf.getConfigurationSection("Translations").getKeys(false);
			for (String key : keys) {
				HashMap<Short, String> entry = new HashMap<>();
				Set<String> subKeys = conf.getConfigurationSection("Translations.".concat(key)).getKeys(false);
				for (String subKey : subKeys) {
					entry.put(Short.parseShort(subKey), color(conf.getString("Translations.".concat(key).concat(".").concat(subKey))));
				}
				TRANSLATIONS.put(key, entry);
			}
			COOLDOWN = conf.getLong("General.cooldown");
			LIMIT = conf.getInt("General.limit");
			List<String> added = conf.getStringList("General.placeholders");
			PLACEHOLDERS = ImmutableList.copyOf(added);
			NAME_FORMAT = color(conf.getString("General.name-format"));
			AMOUNT_FORMAT = color(conf.getString("General.amount-format"));
			COLOR_IF_ALREADY_COLORED = conf.getBoolean("General.color-if-already-colored");
			LET_MESSAGE_THROUGH = conf.getBoolean("General.let-message-through");
			FORCE_ADD_AMOUNT = conf.getBoolean("General.force-add-amount");
			DENY_IF_NO_ITEM = conf.getBoolean("General.deny-if-no-item");
			HAND_DISABLED = conf.getBoolean("General.hand.disabled");
			SHOW_NO_PERM_NORMAL = conf.getBoolean("General.show-no-permission-message.normal");
			SHOW_NO_PERM_COMMAND = conf.getBoolean("General.show-no-permission-message.command");
			DENY_MESSAGE = color(conf.getString("Messages.deny-message"));
			HAND_NAME = color(conf.getString("General.hand.name"));
			LIMIT_MESSAGE = color(conf.getString("Messages.limit-message"));
			RELOAD_MESSAGE = color(conf.getString("Messages.reload-success"));
			NO_PERMISSION_MESSAGE = color(conf.getString("Messages.no-permission"));
			COOLDOWN_MESSAGE = color(conf.getString("Messages.cooldown-message"));
			SECONDS = color(conf.getString("Messages.seconds"));
			MINUTES = color(conf.getString("Messages.minutes"));
			HOURS = color(conf.getString("Messages.hours"));
			HAND_TOOLTIP = conf.getStringList("General.hand.tooltip");
			colorStringList(HAND_TOOLTIP);
			final List<String> cmds = conf.getStringList("General.commands");
			Server.getBukkitTasks().add(Bukkit.getScheduler().runTaskLaterAsynchronously(Server.getPlugin(), () -> {
				for (String s : cmds) {
					Command c = Bukkit.getPluginCommand(s);
					if (c != null) {
						ALLOWED_PLUGIN_COMMANDS.add(c);
					} else {
						ALLOWED_DEFAULT_COMMANDS.add(s);
					}
				}
			}, 100L));
		}

		private static String color(String s) {
			return ChatColor.translateAlternateColorCodes('&', s);
		}

		private void checkConfigVersion() {
			int latestVersion = ChatItem.CFG_VER;
			if (latestVersion != CONFIG_VERSION) {
				Bukkit.getLogger().log(Level.WARNING, ChatColor.RED + "ChatItem detected an older or invalid configuration file. Replacing it with the default config...");
				performOverwrite();
				conf = Server.getChatItemConfig().getData();
				Bukkit.getLogger().log(Level.WARNING, ChatColor.RED + "Replacement complete!");
			}
		}

		private void performOverwrite() {
			File chatItemConfig = new File(Server.getPlugin().getDataFolder(), "chatitem_config.yml");
			try {
				FileUtils.copyInputStreamToFile(Server.getPlugin().getResource("chatitem_config.yml"), chatItemConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private static void colorStringList(List<String> input) {
			for (int i = 0; i < input.size(); ++i) {
				input.set(i, color(input.get(i)));
			}
		}
	}
}
