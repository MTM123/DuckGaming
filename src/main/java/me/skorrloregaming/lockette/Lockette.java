package me.skorrloregaming.lockette;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Lockette {
	static boolean DEBUG = false;
	public static Lockette plugin;
	public static boolean enabled = false;
	public static boolean uuidSupport = false;
	public static boolean registered = false;
	public final LocketteBlockListener blockListener = new LocketteBlockListener(this);
	public final LocketteEntityListener entityListener = new LocketteEntityListener(this);
	public final LockettePlayerListener playerListener = new LockettePlayerListener(this);
	public final LockettePrefixListener prefixListener = new LockettePrefixListener(this);
	public final LocketteWorldListener worldListener = new LocketteWorldListener(this);
	public final LocketteInventoryListener inventoryListener = new LocketteInventoryListener(this);
	public final LocketteDoorCloser doorCloser = new LocketteDoorCloser(this);
	public static boolean explosionProtectionAll;
	public static boolean adminSnoop, adminBypass, adminBreak;
	public static boolean protectDoors, protectTrapDoors, usePermissions;
	public static boolean directPlacement, colorTags, debugMode;
	public static boolean blockHopper = false;
	public static int defaultDoorTimer;
	public static String broadcastSnoopTarget, broadcastBreakTarget;
	public static String broadcastReloadTarget;
	public static boolean msgUser, msgOwner, msgAdmin, msgError, msgHelp;
	public static String altPrivate, altMoreUsers, altEveryone, altOperators, altTimer, altFee;
	public static List<Material> customBlockList = null;
	public static List<String> disabledPluginList = null;
	public static FileConfiguration strings = null;
	public final HashMap<String, Block> playerList = new HashMap<String, Block>();
	public static final String META_KEY = "LocketteUUIDs";
	public static final String pluginName = "Lockette";

	public Lockette() {
		plugin = this;
	}

	public void onLoad() {
	}

	public static int majorVersion(String ver) {
		try {
			ver = ver.replaceAll("v", "");
			ver = ver.substring(0, ver.indexOf("_R"));
			String[] nums = ver.split("_");
			return Integer.parseInt(nums[0]);
		} catch (Exception ignore) {
		}
		return -1;
	}

	public static int minorVersion(String ver) {
		try {
			ver = ver.replaceAll("v", "");
			ver = ver.substring(0, ver.indexOf("_R"));
			String[] nums = ver.split("_");
			return Integer.parseInt(nums[1]);
		} catch (Exception ignore) {
		}
		return -1;
	}

	public static boolean leqVersion(String ver, int major, int minor) {
		try {
			int mj = majorVersion(ver);
			if (mj > major) {
				return true;
			}
			if (mj == major) {
				int mi = minorVersion(ver);
				if (mi >= minor) {
					return true;
				}
			}
		} catch (Exception ignore) {
		}
		return false;
	}

	public void onEnable() {
		if (enabled)
			return;
		Logger.debug("[" + Lockette.pluginName + "] Version " + Server.getPlugin().getDescription().getVersion() + " is being enabled!");
		String bukkitVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
		float bukkitminver = 1.8F;
		if (!leqVersion(bukkitVersion, 1, 8)) {
			Logger.debug("[" + Lockette.pluginName + "] Detected Bukkit build [" + bukkitVersion + "], but requires version [" + bukkitminver + "] or greater!");
			Logger.debug("[" + Lockette.pluginName + "] Aborting enable!");
			return;
		} else {
			Logger.debug("[" + Lockette.pluginName + "] Detected Bukkit version [" + bukkitVersion + "] ok.");
		}
		loadProperties(false);
		if (!registered) {
			blockListener.registerEvents();
			entityListener.registerEvents();
			playerListener.registerEvents();
			prefixListener.registerEvents();
			worldListener.registerEvents();
			inventoryListener.registerEvents();
			registered = true;
		}
		Logger.debug("[" + Lockette.pluginName + "] Ready to protect your containers.");
		enabled = true;
	}

	public void onDisable() {
		if (!enabled)
			return;
		if (protectDoors || protectTrapDoors) {
			Logger.debug("[" + Lockette.pluginName + "] Closing all automatic doors.");
			doorCloser.cleanup();
		}
		enabled = false;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		return false;
	}

	public boolean hasPermission(World world, String playerName, String permissionNode) {
		return (hasPermission(world, Server.getPlugin().getServer().getPlayer(playerName), permissionNode));
	}

	public static BlockFace getPistonFacing(Block block) {
		Material type = block.getType();
		if ((type != Material.PISTON) && (type != Material.STICKY_PISTON) && (type != Material.PISTON_HEAD)) {
			return (BlockFace.SELF);
		}
		int face = block.getState().getData().getData() & 0x7;
		switch (face) {
			case 0:
				return (BlockFace.DOWN);
			case 1:
				return (BlockFace.UP);
			case 2:
				return (BlockFace.NORTH);
			case 3:
				return (BlockFace.SOUTH);
			case 4:
				return (BlockFace.WEST);
			case 5:
				return (BlockFace.EAST);
		}
		return (BlockFace.SELF);
	}

	public boolean hasPermission(World world, Player player, String permissionNode) {
		if (player == null)
			return (false);
		boolean result = false;
		if (player != null) {
			result = player.hasPermission(permissionNode);
			if (result)
				return (true);
		}
		return (false);
	}

	public boolean playerOnline(String truncName) {
		String text = truncName.replaceAll("(?i)\u00A7[0-F]", "");
		int length;
		for (Player p : Bukkit.getOnlinePlayers()) {
			length = p.getName().length();
			if (length > 15)
				length = 15;
			if (text.equals(p.getName().substring(0, length)))
				return (true);
		}
		return (false);
	}

	@SuppressWarnings("unchecked")
	public void loadProperties(boolean reload) {
		if (reload) {
			Logger.debug("[" + Lockette.pluginName + "] Reloading plugin configuration files.");
			Server.getLocketteConfig().reloadData();
		}
		uuidSupport = Server.getLocketteConfig().getData().getBoolean("enable-uuid-support", false);
		Server.getLocketteConfig().getData().set("enable-uuid-support", uuidSupport);
		msgUser = Server.getLocketteConfig().getData().getBoolean("enable-messages-user", true);
		Server.getLocketteConfig().getData().set("enable-messages-user", msgUser);
		msgOwner = Server.getLocketteConfig().getData().getBoolean("enable-messages-owner", false);
		Server.getLocketteConfig().getData().set("enable-messages-owner", msgOwner);
		msgAdmin = Server.getLocketteConfig().getData().getBoolean("enable-messages-admin", true);
		Server.getLocketteConfig().getData().set("enable-messages-admin", msgAdmin);
		msgError = Server.getLocketteConfig().getData().getBoolean("enable-messages-error", true);
		Server.getLocketteConfig().getData().set("enable-messages-error", msgError);
		msgHelp = Server.getLocketteConfig().getData().getBoolean("enable-messages-help", true);
		Server.getLocketteConfig().getData().set("enable-messages-help", msgHelp);
		explosionProtectionAll = Server.getLocketteConfig().getData().getBoolean("explosion-protection-all", true);
		Server.getLocketteConfig().getData().set("explosion-protection-all", explosionProtectionAll);
		usePermissions = Server.getLocketteConfig().getData().getBoolean("enable-permissions", false);
		Server.getLocketteConfig().getData().set("enable-permissions", usePermissions);
		protectDoors = Server.getLocketteConfig().getData().getBoolean("enable-protection-doors", true);
		Server.getLocketteConfig().getData().set("enable-protection-doors", protectDoors);
		protectTrapDoors = Server.getLocketteConfig().getData().getBoolean("enable-protection-trapdoors", true);
		Server.getLocketteConfig().getData().set("enable-protection-trapdoors", protectTrapDoors);
		adminSnoop = Server.getLocketteConfig().getData().getBoolean("allow-admin-snoop", false);
		Server.getLocketteConfig().getData().set("allow-admin-snoop", adminSnoop);
		adminBypass = Server.getLocketteConfig().getData().getBoolean("allow-admin-bypass", true);
		Server.getLocketteConfig().getData().set("allow-admin-bypass", adminBypass);
		adminBreak = Server.getLocketteConfig().getData().getBoolean("allow-admin-break", true);
		Server.getLocketteConfig().getData().set("allow-admin-break", adminBreak);
		blockHopper = Server.getLocketteConfig().getData().getBoolean("enable-hopper-blocking", true);
		Server.getLocketteConfig().getData().set("enable-hopper-blocking", blockHopper);
		if (protectDoors || protectTrapDoors) {
			if (doorCloser.start()) {
				Logger.debug("[" + Lockette.pluginName + "] Failed to register door closing task!");
			}
		} else
			doorCloser.stop();
		directPlacement = Server.getLocketteConfig().getData().getBoolean("enable-quick-protect", true);
		Server.getLocketteConfig().getData().set("enable-quick-protect", directPlacement);
		colorTags = Server.getLocketteConfig().getData().getBoolean("enable-color-tags", true);
		Server.getLocketteConfig().getData().set("enable-color-tags", colorTags);
		debugMode = Server.getLocketteConfig().getData().getBoolean("enable-debug", false);
		if (debugMode)
			Logger.debug("[" + Lockette.pluginName + "] Debug mode is enabled, so Lockette chests are NOT secure.");
		defaultDoorTimer = Server.getLocketteConfig().getData().getInt("default-door-timer", -1);
		if (defaultDoorTimer == -1) {
			defaultDoorTimer = 0;
			Server.getLocketteConfig().getData().set("default-door-timer", defaultDoorTimer);
		}
		List<String> strCustomBlockList = (List<String>) Server.getLocketteConfig().getData().getList("custom-lockable-block-list");
		if (strCustomBlockList == null) {
			strCustomBlockList = new ArrayList<String>();
			strCustomBlockList.add(Material.ENCHANTING_TABLE.toString());
			strCustomBlockList.add(Material.JUKEBOX.toString());
			strCustomBlockList.add(Material.DIAMOND_BLOCK.toString());
			strCustomBlockList.add(Material.ANVIL.toString());
			strCustomBlockList.add(Material.HOPPER.toString());
			Server.getLocketteConfig().getData().set("custom-lockable-block-list", strCustomBlockList);
		}
		if (strCustomBlockList != null) {
			if (customBlockList == null)
				customBlockList = new ArrayList<Material>();
			customBlockList.clear();
			for (String str : strCustomBlockList) {
				customBlockList.add(Material.valueOf(str));
			}
		}
		if (!customBlockList.isEmpty()) {
			Logger.debug("[" + Lockette.pluginName + "] Custom lockable block list: " + customBlockList.toString());
		}
		disabledPluginList = (List<String>) Server.getLocketteConfig().getData().getList("linked-plugin-ignore-list");
		if (disabledPluginList == null) {
			disabledPluginList = new ArrayList<String>(1);
			disabledPluginList.add("mcMMO");
			Server.getLocketteConfig().getData().set("linked-plugin-ignore-list", disabledPluginList);
		}
		if (!disabledPluginList.isEmpty()) {
			Logger.debug("[" + Lockette.pluginName + "] Ignoring linked plugins: " + disabledPluginList.toString());
		}
		broadcastSnoopTarget = Server.getLocketteConfig().getData().getString("broadcastMessage-snoop-target");
		if (broadcastSnoopTarget == null) {
			broadcastSnoopTarget = "[Everyone]";
			Server.getLocketteConfig().getData().set("broadcastMessage-snoop-target", broadcastSnoopTarget);
		}
		broadcastBreakTarget = Server.getLocketteConfig().getData().getString("broadcastMessage-break-target");
		if (broadcastBreakTarget == null) {
			broadcastBreakTarget = "[Everyone]";
			Server.getLocketteConfig().getData().set("broadcastMessage-break-target", broadcastBreakTarget);
		}
		broadcastReloadTarget = Server.getLocketteConfig().getData().getString("broadcastMessage-reload-target");
		if (broadcastReloadTarget == null) {
			broadcastReloadTarget = "[Operators]";
			Server.getLocketteConfig().getData().set("broadcastMessage-reload-target", broadcastReloadTarget);
		}
		String stringsFileName = Server.getLocketteConfig().getData().getString("strings-file-name");
		if ((stringsFileName == null) || stringsFileName.isEmpty()) {
			stringsFileName = "strings-en.yml";
			Server.getLocketteConfig().getData().set("strings-file-name", stringsFileName);
		}
		Server.getLocketteConfig().saveData();
		loadStrings(reload, stringsFileName);
	}

	public void loadStrings(boolean reload, String fileName) {
		boolean stringChanged = false;
		String tempString;
		File stringsFile = new File(Server.getPlugin().getDataFolder(), fileName);
		if (strings != null) {
			strings = null;
		}
		strings = new YamlConfiguration();
		try {
			strings.load(stringsFile);
		} catch (InvalidConfigurationException ex) {
			Logger.debug("[" + Lockette.pluginName + "] Error loading " + fileName + ": " + ex.getMessage());
			if (!fileName.equals("strings-en.yml")) {
				loadStrings(reload, "strings-en.yml");
				return;
			} else
				Logger.debug("[" + Lockette.pluginName + "] Returning to default strings.");
		} catch (Exception ex) {
		}
		boolean original = false;
		if (fileName.equals("strings-en.yml")) {
			original = true;
			strings.set("language", "English");
			if (original) {
				try {
					strings.save(stringsFile);
					strings.load(stringsFile);
				} catch (Exception ex) {
				}
			}
			strings.set("author", "Acru");
			strings.set("editors", "");
			strings.set("version", 0);
		}
		tempString = strings.getString("language");
		if ((tempString == null) || tempString.isEmpty()) {
			Logger.debug("[" + Lockette.pluginName + "] Loading strings file " + fileName);
		} else
			Logger.debug("[" + Lockette.pluginName + "] Loading strings file for " + tempString + " by " + strings.getString("author"));
		altPrivate = strings.getString("alternate-private-tag");
		if ((altPrivate == null) || altPrivate.isEmpty() || (original && altPrivate.equals("Priv�"))) {
			altPrivate = "Private";
			strings.set("alternate-private-tag", altPrivate);
		}
		altPrivate = "[" + altPrivate + "]";
		altMoreUsers = strings.getString("alternate-moreusers-tag");
		if ((altMoreUsers == null) || altMoreUsers.isEmpty() || (original && altMoreUsers.equals("Autre Noms"))) {
			altMoreUsers = "More Users";
			strings.set("alternate-moreusers-tag", altMoreUsers);
			stringChanged = true;
		}
		altMoreUsers = "[" + altMoreUsers + "]";
		altEveryone = strings.getString("alternate-everyone-tag");
		if ((altEveryone == null) || altEveryone.isEmpty() || (original && altEveryone.equals("Tout le Monde"))) {
			altEveryone = "Everyone";
			strings.set("alternate-everyone-tag", altEveryone);
			stringChanged = true;
		}
		altEveryone = "[" + altEveryone + "]";
		altOperators = strings.getString("alternate-operators-tag");
		if ((altOperators == null) || altOperators.isEmpty() || (original && altOperators.equals("Op�rateurs"))) {
			altOperators = "Operators";
			strings.set("alternate-operators-tag", altOperators);
			stringChanged = true;
		}
		altOperators = "[" + altOperators + "]";
		altTimer = strings.getString("alternate-timer-tag");
		if ((altTimer == null) || altTimer.isEmpty() || (original && altTimer.equals("Minuterie"))) {
			altTimer = "Timer";
			strings.set("alternate-timer-tag", altTimer);
			stringChanged = true;
		}
		altFee = strings.getString("alternate-fee-tag");
		if ((altFee == null) || altFee.isEmpty()) {
			altFee = "Fee";
			strings.set("alternate-fee-tag", altFee);
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-conflict-door");
		if (tempString == null) {
			strings.set("msg-user-conflict-door", "Conflicting door removed!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-illegal");
		if (tempString == null) {
			strings.set("msg-user-illegal", "Illegal chest removed!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-resize-owned");
		if (tempString == null) {
			strings.set("msg-user-resize-owned", "You cannot resize a chest claimed by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-help-chest");
		if (tempString == null) {
			strings.set("msg-help-chest", "Place a sign headed [Private] next to a chest to lock it.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-owner-release");
		if (tempString == null) {
			strings.set("msg-owner-release", "You have released a container!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-admin-release");
		if (tempString == null) {
			strings.set("msg-admin-release", "(Admin) @@@ has broken open a container owned by ***!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-release-owned");
		if (tempString == null) {
			strings.set("msg-user-release-owned", "You cannot release a container claimed by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-owner-remove");
		if (tempString == null) {
			strings.set("msg-owner-remove", "You have removed users from a container!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-remove-owned");
		if (tempString == null) {
			strings.set("msg-user-remove-owned", "You cannot remove users from a container claimed by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-break-owned");
		if (tempString == null) {
			strings.set("msg-user-break-owned", "You cannot break a container claimed by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-denied-door");
		if (tempString == null) {
			strings.set("msg-user-denied-door", "You don't have permission to use this door.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-touch-fee");
		if (tempString == null) {
			strings.set("msg-user-touch-fee", "A fee of ### will be paid to ***, to open.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-touch-owned");
		if (tempString == null) {
			strings.set("msg-user-touch-owned", "This container has been claimed by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-help-select");
		if (tempString == null) {
			strings.set("msg-help-select", "Sign selected, use /lockette <line number> <text> to edit.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-admin-bypass");
		if (tempString == null) {
			strings.set("msg-admin-bypass", "Bypassed a door owned by ***, be sure to close it behind you.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-admin-snoop");
		if (tempString == null) {
			strings.set("msg-admin-snoop", "(Admin) @@@ has snooped around in a container owned by ***!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-user-denied");
		if (tempString == null) {
			strings.set("msg-user-denied", "You don't have permission to open this container.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-zone");
		if (tempString == null) {
			strings.set("msg-error-zone", "This zone is public by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-permission");
		if (tempString == null) {
			strings.set("msg-error-permission", "Permission to lock container denied.");
			stringChanged = true;
		} else if (tempString.equals("Permission to lock containers denied.")) {
			strings.set("msg-error-permission", "Permission to lock container denied.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-claim");
		if (tempString == null) {
			strings.set("msg-error-claim", "No unclaimed container nearby to make Private!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-claim-conflict");
		if (tempString == null) {
			strings.set("msg-error-claim-conflict", "Conflict with an existing public door.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-admin-claim-error");
		if (tempString == null) {
			strings.set("msg-admin-claim-error", "Player *** is not online, be sure you have the correct name.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-admin-claim");
		if (tempString == null) {
			strings.set("msg-admin-claim", "You have claimed a container for ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-owner-claim");
		if (tempString == null) {
			strings.set("msg-owner-claim", "You have claimed a container!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-adduser-owned");
		if (tempString == null) {
			strings.set("msg-error-adduser-owned", "You cannot add users to a container claimed by ***.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-adduser");
		if (tempString == null) {
			strings.set("msg-error-adduser", "No claimed container nearby to add users to!");
			stringChanged = true;
		}
		tempString = strings.getString("msg-owner-adduser");
		if (tempString == null) {
			strings.set("msg-owner-adduser", "You have added users to a container!");
			stringChanged = true;
		}
		if (original) {
			strings.set("msg-help-command1", "&C/lockette <line number> <text> - Edits signs on locked containers. Right click on the sign to edit.");
			strings.set("msg-help-command2", "&C/lockette fix - Fixes an automatic door that is in the wrong position. Look at the door to edit.");
			strings.set("msg-help-command3", "&C/lockette reload - Reloads the configuration files. Operators only.");
			strings.set("msg-help-command4", "&C/lockette version - Reports Lockette version.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-admin-reload");
		if (tempString == null) {
			strings.set("msg-admin-reload", "Reloading plugin configuration files.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-fix");
		if (tempString == null) {
			strings.set("msg-error-fix", "No owned door found.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-error-edit");
		if (tempString == null) {
			strings.set("msg-error-edit", "First select a sign by right clicking it.");
			stringChanged = true;
		}
		tempString = strings.getString("msg-owner-edit");
		if (tempString == null) {
			strings.set("msg-owner-edit", "Sign edited successfully.");
			stringChanged = true;
		}
		if (original)
			if (stringChanged) {
				try {
					strings.save(stringsFile);
				} catch (Exception ex) {
				}
			}
	}

	public static boolean isProtected(Block block) {
		if (!enabled)
			return (false);
		Material type = block.getType();
		if ($.isWallSign(type)) {
			Sign sign = (Sign) block.getState();
			String text = ChatColor.stripColor(sign.getLine(0)).toLowerCase();
			if (text.equals("[private]") || text.equalsIgnoreCase(altPrivate)) {
				return (true);
			} else if (text.equals("[more users]") || text.equalsIgnoreCase(altMoreUsers)) {
				Block checkBlock = getSignAttachedBlock(block);
				if (checkBlock != null)
					if (findBlockOwner(checkBlock) != null) {
						return (true);
					}
			}
		} else if (Lockette.findBlockOwner(block) != null)
			return (true);
		return (false);
	}

	public static String getProtectedOwner(Block block) {
		return Bukkit.getOfflinePlayer(getProtectedOwnerUUID(block)).getName();
	}

	public static UUID getProtectedOwnerUUID(Block block) {
		if (!enabled)
			return (null);
		Material type = block.getType();
		if ($.isWallSign(type)) {
			Sign sign = (Sign) block.getState();
			String text = ChatColor.stripColor(sign.getLine(0)).toLowerCase();
			if (text.equals("[private]") || text.equalsIgnoreCase(altPrivate)) {
				return getUUIDFromMeta(sign, 1);
			} else if (text.equals("[more users]") || text.equalsIgnoreCase(altMoreUsers)) {
				Block checkBlock = getSignAttachedBlock(block);
				if (checkBlock != null) {
					Block signBlock = findBlockOwner(checkBlock);
					if (signBlock != null) {
						sign = (Sign) signBlock.getState();
						return getUUIDFromMeta(sign, 1);
					}
				}
			}
		} else {
			Block signBlock = Lockette.findBlockOwner(block);
			if (signBlock != null) {
				Sign sign = (Sign) signBlock.getState();
				return getUUIDFromMeta(sign, 1);
			}
		}
		return null;
	}

	public static boolean isEveryone(Block block) {
		if (!enabled)
			return (true);
		Block signBlock = Lockette.findBlockOwner(block);
		if (signBlock == null)
			return (true);
		Sign sign = (Sign) signBlock.getState();
		String line;
		int y;
		for (y = 1; y <= 3; ++y)
			if (!sign.getLine(y).isEmpty()) {
				line = sign.getLine(y).replaceAll("(?i)\u00A7[0-F]", "");
				if (line.equalsIgnoreCase("[Everyone]") || line.equalsIgnoreCase(Lockette.altEveryone))
					return (true);
			}
		List<Block> list = Lockette.findBlockUsers(block, signBlock);
		int x, count = list.size();
		for (x = 0; x < count; ++x) {
			sign = (Sign) list.get(x).getState();
			for (y = 1; y <= 3; ++y)
				if (!sign.getLine(y).isEmpty()) {
					line = sign.getLine(y).replaceAll("(?i)\u00A7[0-F]", "");
					if (line.equalsIgnoreCase("[Everyone]") || line.equalsIgnoreCase(Lockette.altEveryone))
						return (true);
				}
		}
		return (false);
	}

	public boolean pluginEnableOverride(String pluginName) {
		return (isInList(pluginName, Lockette.disabledPluginList));
	}

	public String getLocalizedEveryone() {
		return (altEveryone);
	}

	public String getLocalizedOperators() {
		return (altOperators);
	}

	public void localizedMessage(Player player, String broadcast, String key) {
		localizedMessage(player, broadcast, key, null, null);
	}

	public void localizedMessage(Player player, String broadcast, String key, String sub) {
		localizedMessage(player, broadcast, key, sub, null);
	}

	public void localizedMessage(Player player, String broadcast, String key, String sub, String num) {
		String color = "";
		if (key.startsWith("msg-user-")) {
			if (broadcast == null)
				if (!Lockette.msgUser)
					return;
			color = ChatColor.YELLOW.toString();
		} else if (key.startsWith("msg-owner-")) {
			if (broadcast == null)
				if (!Lockette.msgOwner)
					return;
			color = ChatColor.GOLD.toString();
		} else if (key.startsWith("msg-admin-")) {
			if (broadcast == null)
				if (!Lockette.msgAdmin)
					return;
			color = ChatColor.RED.toString();
		} else if (key.startsWith("msg-error-")) {
			if (broadcast == null)
				if (!Lockette.msgError)
					return;
			color = ChatColor.RED.toString();
		} else if (key.startsWith("msg-help-")) {
			if (broadcast == null)
				if (!Lockette.msgHelp)
					return;
			color = ChatColor.GOLD.toString();
		}
		String message = strings.getString(key);
		if ((message == null) || message.isEmpty())
			return;
		message = message.replaceAll("&([0-9A-Fa-f])", "\u00A7$1");
		if (sub != null)
			message = message.replaceAll("\\*\\*\\*", sub + color);
		if (num != null)
			message = message.replaceAll("###", num);
		if (player != null)
			message = message.replaceAll("@@@", player.getName());
		if (broadcast != null)
			selectiveBroadcast(broadcast, color + "[" + Lockette.pluginName + "] " + message);
		else if (player != null)
			player.sendMessage(color + "[" + Lockette.pluginName + "] " + message);
	}

	public void selectiveBroadcast(String target, String message) {
		if (target == null)
			return;
		if (target.isEmpty())
			return;
		if (message == null)
			return;
		if (message.isEmpty())
			return;
		if (target.charAt(0) == '[') {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (inGroup(p.getWorld(), p, target)) {
					p.sendMessage(message);
				}
			}
		} else {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (target.equalsIgnoreCase(p.getName())) {
					p.sendMessage(message);
				}
			}
		}
	}

	public static Block findBlockOwner(Block block) {
		return (findBlockOwner(block, null, false));
	}

	public static Block findBlockOwnerBreak(Block block) {
		Material type = block.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListChests)) {
			return (findBlockOwnerBase(block, null, false, false, false, false, false));
		}
		if (BlockUtil.isInList(type, BlockUtil.materialListTools) || Lockette.isInList(type, Lockette.customBlockList)) {
			return (findBlockOwnerBase(block, null, false, false, false, false, false));
		}
		if (Lockette.protectTrapDoors)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				return (findBlockOwnerBase(block, null, false, false, false, false, false));
			}
		if (Lockette.protectDoors)
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				return (findBlockOwnerBase(block, null, false, true, true, false, false));
			}
		Block checkBlock;
		checkBlock = findBlockOwnerBase(block, null, false, false, false, false, false);
		if (checkBlock != null)
			return (checkBlock);
		if (Lockette.protectTrapDoors) {
			checkBlock = block.getRelative(BlockFace.NORTH);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 2) {
					checkBlock = findBlockOwnerBase(checkBlock, null, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
			checkBlock = block.getRelative(BlockFace.EAST);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 0) {
					checkBlock = findBlockOwnerBase(checkBlock, null, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
			checkBlock = block.getRelative(BlockFace.SOUTH);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 3) {
					checkBlock = findBlockOwnerBase(checkBlock, null, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
			checkBlock = block.getRelative(BlockFace.WEST);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 1) {
					checkBlock = findBlockOwnerBase(checkBlock, null, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
		}
		if (Lockette.protectDoors) {
			checkBlock = block.getRelative(BlockFace.UP);
			type = checkBlock.getType();
			if (!BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				return (findBlockOwnerBase(checkBlock, null, false, true, true, false, false));
			}
		}
		return (null);
	}

	public static Block findBlockOwner(Block block, Block ignoreBlock, boolean iterateFurther) {
		if (block == null)
			return null;
		Material type = block.getType();
		Location ignore;
		if (ignoreBlock != null)
			ignore = ignoreBlock.getLocation();
		else
			ignore = null;
		if (BlockUtil.isInList(type, BlockUtil.materialListChests)) {
			return (findBlockOwnerBase(block, ignore, true, false, false, false, false));
		}
		if (BlockUtil.isInList(type, BlockUtil.materialListTools) || Lockette.isInList(type, Lockette.customBlockList)) {
			return (findBlockOwnerBase(block, ignore, false, false, false, false, false));
		}
		if (Lockette.protectTrapDoors)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				return (findBlockOwner(getTrapDoorAttachedBlock(block), ignoreBlock, false));
			}
		if (Lockette.protectDoors)
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				return (findBlockOwnerBase(block, ignore, true, true, true, true, iterateFurther));
			}
		Block checkBlock, result;
		if (Lockette.protectTrapDoors) {
			checkBlock = findBlockOwnerBase(block, ignore, false, false, false, false, false);
			if (checkBlock != null)
				return (checkBlock);
			checkBlock = block.getRelative(BlockFace.NORTH);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 2) {
					checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
			checkBlock = block.getRelative(BlockFace.EAST);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 0) {
					checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
			checkBlock = block.getRelative(BlockFace.SOUTH);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 3) {
					checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
			checkBlock = block.getRelative(BlockFace.WEST);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 0x3) == 1) {
					checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, false, false, false);
					if (checkBlock != null)
						return (checkBlock);
				}
			}
		}
		if (Lockette.protectDoors) {
			checkBlock = block.getRelative(BlockFace.UP);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				result = findBlockOwnerBase(checkBlock, ignore, true, true, true, true, iterateFurther);
				if (result != null)
					return (result);
			}
			checkBlock = block.getRelative(BlockFace.DOWN);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				Block checkBlock2 = checkBlock.getRelative(BlockFace.DOWN);
				type = checkBlock2.getType();
				if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
					return (findBlockOwnerBase(checkBlock2, ignore, true, true, false, true, iterateFurther));
				} else {
					return (findBlockOwnerBase(checkBlock, ignore, true, true, false, true, iterateFurther));
				}
			}
		}
		return (null);
	}

	public static Block findBlockOwnerBase(Block block, Location ignore, boolean iterate, boolean iterateUp, boolean iterateDown, boolean includeEnds, boolean iterateFurther) {
		Block checkBlock;
		Material type;
		boolean doCheck;
		if (iterateUp) {
			checkBlock = block.getRelative(BlockFace.UP);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				checkBlock = findBlockOwnerBase(checkBlock, ignore, false, iterateUp, false, includeEnds, false);
			} else if (includeEnds)
				checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, false, includeEnds, false);
			else
				checkBlock = null;
			if (checkBlock != null)
				return (checkBlock);
		}
		if (iterateDown) {
			checkBlock = block.getRelative(BlockFace.DOWN);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, iterateDown, includeEnds, false);
			} else if (includeEnds)
				checkBlock = findBlockOwnerBase(checkBlock, ignore, false, false, false, includeEnds, false);
			else
				checkBlock = null;
			if (checkBlock != null)
				return (checkBlock);
		}
		checkBlock = block.getRelative(BlockFace.NORTH);
		if ($.isWallSign(checkBlock.getType())) {
			if (ignore == null)
				doCheck = true;
			else if (checkBlock.getLocation().equals(ignore))
				doCheck = false;
			else
				doCheck = true;
			if (doCheck) {
				Sign sign = (Sign) checkBlock.getState();
				String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
				if (text.equals("[private]") || text.equalsIgnoreCase(altPrivate))
					return (checkBlock);
			}
		} else if (iterate)
			if (checkBlock.getType() == block.getType()) {
				checkBlock = findBlockOwnerBase(checkBlock, ignore, iterateFurther, iterateUp, iterateDown, includeEnds, false);
				if (checkBlock != null)
					return (checkBlock);
			}
		checkBlock = block.getRelative(BlockFace.EAST);
		if ($.isWallSign(checkBlock.getType())) {
			if (ignore == null)
				doCheck = true;
			else if (checkBlock.getLocation().equals(ignore))
				doCheck = false;
			else
				doCheck = true;
			if (doCheck) {
				Sign sign = (Sign) checkBlock.getState();
				String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
				if (text.equals("[private]") || text.equalsIgnoreCase(altPrivate))
					return (checkBlock);
			}
		} else if (iterate)
			if (checkBlock.getType() == block.getType()) {
				checkBlock = findBlockOwnerBase(checkBlock, ignore, iterateFurther, iterateUp, iterateDown, includeEnds, false);
				if (checkBlock != null)
					return (checkBlock);
			}
		checkBlock = block.getRelative(BlockFace.SOUTH);
		if ($.isWallSign(checkBlock.getType())) {
			if (ignore == null)
				doCheck = true;
			else if (checkBlock.getLocation().equals(ignore))
				doCheck = false;
			else
				doCheck = true;
			if (doCheck) {
				Sign sign = (Sign) checkBlock.getState();
				String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
				if (text.equals("[private]") || text.equalsIgnoreCase(altPrivate))
					return (checkBlock);
			}
		} else if (iterate)
			if (checkBlock.getType() == block.getType()) {
				checkBlock = findBlockOwnerBase(checkBlock, ignore, iterateFurther, iterateUp, iterateDown, includeEnds, false);
				if (checkBlock != null)
					return (checkBlock);
			}
		checkBlock = block.getRelative(BlockFace.WEST);
		if ($.isWallSign(checkBlock.getType())) {
			if (ignore == null)
				doCheck = true;
			else if (checkBlock.getLocation().equals(ignore))
				doCheck = false;
			else
				doCheck = true;
			if (doCheck) {
				Sign sign = (Sign) checkBlock.getState();
				String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
				if (text.equals("[private]") || text.equalsIgnoreCase(altPrivate))
					return (checkBlock);
			}
		} else if (iterate)
			if (checkBlock.getType() == block.getType()) {
				checkBlock = findBlockOwnerBase(checkBlock, ignore, iterateFurther, iterateUp, iterateDown, includeEnds, false);
				if (checkBlock != null)
					return (checkBlock);
			}
		return (null);
	}

	public static Block getSignAttachedBlock(Block block) {
		if (!$.isWallSign(block.getType()))
			return (null);
		int face = block.getData() & 0x7;
		if (face == BlockUtil.faceList[0])
			return (block.getRelative(BlockFace.NORTH));
		if (face == BlockUtil.faceList[1])
			return (block.getRelative(BlockFace.EAST));
		if (face == BlockUtil.faceList[2])
			return (block.getRelative(BlockFace.SOUTH));
		if (face == BlockUtil.faceList[3])
			return (block.getRelative(BlockFace.WEST));
		return (null);
	}

	public static Block getTrapDoorAttachedBlock(Block block) {
		Material type = block.getType();
		if (type != Material.LEGACY_TRAP_DOOR && type != Material.IRON_TRAPDOOR)
			return (null);
		int face = block.getData() & 0x3;
		if (face == BlockUtil.attachList[0])
			return (block.getRelative(BlockFace.NORTH));
		if (face == BlockUtil.attachList[1])
			return (block.getRelative(BlockFace.EAST));
		if (face == BlockUtil.attachList[2])
			return (block.getRelative(BlockFace.SOUTH));
		if (face == BlockUtil.attachList[3])
			return (block.getRelative(BlockFace.WEST));
		return (null);
	}

	public static List<Block> findBlockUsers(Block block, Block signBlock) {
		Material type = block.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListChests))
			return (findBlockUsersBase(block, true, false, false, false, 0));
		if (Lockette.protectTrapDoors)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				return (findBlockUsersBase(getTrapDoorAttachedBlock(block), false, false, false, true, 0));
			}
		if (Lockette.protectDoors)
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				return (findBlockUsersBase(block, true, true, true, false, signBlock.getY()));
			}
		return (findBlockUsersBase(block, false, false, false, false, 0));
	}

	public static List<Block> findBlockUsersBase(Block block, boolean iterate, boolean iterateUp, boolean iterateDown, boolean traps, int includeYPos) {
		Block checkBlock;
		Material type;
		List<Block> list = new ArrayList<Block>();
		if (iterateUp) {
			checkBlock = block.getRelative(BlockFace.UP);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				list.addAll(findBlockUsersBase(checkBlock, false, iterateUp, false, false, includeYPos));
			} else if (checkBlock.getY() == includeYPos)
				list.addAll(findBlockUsersBase(checkBlock, false, false, false, false, includeYPos));
		}
		if (iterateDown) {
			checkBlock = block.getRelative(BlockFace.DOWN);
			type = checkBlock.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
				list.addAll(findBlockUsersBase(checkBlock, false, false, iterateDown, false, includeYPos));
			} else
				list.addAll(findBlockUsersBase(checkBlock, false, false, false, false, includeYPos));
		}
		checkBlock = block.getRelative(BlockFace.NORTH);
		type = checkBlock.getType();
		if ($.isWallSign(type)) {
			Sign sign = (Sign) checkBlock.getState();
			String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
			if (text.equals("[more users]") || text.equalsIgnoreCase(altMoreUsers))
				list.add(checkBlock);
		} else if (iterate) {
			if (type == block.getType()) {
				list.addAll(findBlockUsersBase(checkBlock, false, iterateUp, iterateDown, false, includeYPos));
			}
		} else if (traps)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 3) == 2) {
					list.addAll(findBlockUsersBase(checkBlock, false, false, false, false, includeYPos));
				}
			}
		checkBlock = block.getRelative(BlockFace.EAST);
		type = checkBlock.getType();
		if ($.isWallSign(type)) {
			Sign sign = (Sign) checkBlock.getState();
			String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
			if (text.equals("[more users]") || text.equalsIgnoreCase(altMoreUsers))
				list.add(checkBlock);
		} else if (iterate) {
			if (type == block.getType()) {
				list.addAll(findBlockUsersBase(checkBlock, false, iterateUp, iterateDown, false, includeYPos));
			}
		} else if (traps)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 3) == 0) {
					list.addAll(findBlockUsersBase(checkBlock, false, false, false, false, includeYPos));
				}
			}
		checkBlock = block.getRelative(BlockFace.SOUTH);
		type = checkBlock.getType();
		if ($.isWallSign(type)) {
			Sign sign = (Sign) checkBlock.getState();
			String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
			if (text.equals("[more users]") || text.equalsIgnoreCase(altMoreUsers))
				list.add(checkBlock);
		} else if (iterate) {
			if (type == block.getType()) {
				list.addAll(findBlockUsersBase(checkBlock, false, iterateUp, iterateDown, false, includeYPos));
			}
		} else if (traps)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 3) == 3) {
					list.addAll(findBlockUsersBase(checkBlock, false, false, false, false, includeYPos));
				}
			}
		checkBlock = block.getRelative(BlockFace.WEST);
		type = checkBlock.getType();
		if ($.isWallSign(type)) {
			Sign sign = (Sign) checkBlock.getState();
			String text = sign.getLine(0).replaceAll("(?i)\u00A7[0-F]", "").toLowerCase();
			if (text.equals("[more users]") || text.equalsIgnoreCase(altMoreUsers))
				list.add(checkBlock);
		} else if (iterate) {
			if (type == block.getType()) {
				list.addAll(findBlockUsersBase(checkBlock, false, iterateUp, iterateDown, false, includeYPos));
			}
		} else if (traps)
			if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors)) {
				if ((checkBlock.getData() & 3) == 1) {
					list.addAll(findBlockUsersBase(checkBlock, false, false, false, false, includeYPos));
				}
			}
		return (list);
	}

	public static int findChestCountNear(Block block) {
		return (findChestCountNearBase(block, (byte) 0));
	}

	public static int findChestCountNearBase(Block block, byte face) {
		int count = 0;
		Block checkBlock;
		if (face != 2) {
			checkBlock = block.getRelative(BlockFace.NORTH);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListChests) && (checkBlock.getType() == block.getType())) {
				++count;
				if (face == 0)
					count += findChestCountNearBase(checkBlock, (byte) 3);
			}
		}
		if (face != 5) {
			checkBlock = block.getRelative(BlockFace.EAST);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListChests) && (checkBlock.getType() == block.getType())) {
				++count;
				if (face == 0)
					count += findChestCountNearBase(checkBlock, (byte) 4);
			}
		}
		if (face != 3) {
			checkBlock = block.getRelative(BlockFace.SOUTH);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListChests) && (checkBlock.getType() == block.getType())) {
				++count;
				if (face == 0)
					count += findChestCountNearBase(checkBlock, (byte) 2);
			}
		}
		if (face != 4) {
			checkBlock = block.getRelative(BlockFace.WEST);
			if (BlockUtil.isInList(checkBlock.getType(), BlockUtil.materialListChests) && (checkBlock.getType() == block.getType())) {
				++count;
				if (face == 0)
					count += findChestCountNearBase(checkBlock, (byte) 5);
			}
		}
		return (count);
	}

	public static List<Block> toggleDoors(Block block, Block keyBlock, boolean wooden, boolean trap) {
		List<Block> list = new ArrayList<Block>();
		toggleDoorBase(block, keyBlock, !trap, wooden, list);
		try {
			if (!wooden)
				block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
		} catch (NoSuchFieldError ex) {
		} catch (NoSuchMethodError ex) {
		} catch (NoClassDefFoundError ex) {
		}
		return (list);
	}

	public static void toggleSingleDoor(Block block) {
		Material type = block.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListJustDoors)) {
			toggleDoorBase(block, null, true, false, null);
		} else if (BlockUtil.isInList(type, BlockUtil.materialListTrapDoors) || BlockUtil.isInList(type, BlockUtil.materialListGates)) {
			toggleDoorBase(block, null, false, false, null);
		}
	}

	public static void toggleHalfDoor(Block block, boolean effect) {
		Material type = block.getType();
		if (BlockUtil.isInList(type, BlockUtil.materialListDoors)) {
			block.getState().setData(new MaterialData(block.getType(), (byte) (block.getData() ^ 4)));
			try {
				if (effect)
					block.getWorld().playEffect(block.getLocation(), Effect.DOOR_TOGGLE, 0);
			} catch (NoSuchFieldError ex) {
			} catch (NoSuchMethodError ex) {
			} catch (NoClassDefFoundError ex) {
			}
		}
	}

	public static void toggleDoorBase(Block block, Block keyBlock, boolean iterateUpDown, boolean skipDoor, List<Block> list) {
		Block checkBlock;
		if (list != null)
			list.add(block);
		if (!skipDoor)
			block.getState().setData(new MaterialData(block.getType(), (byte) (block.getData() ^ 4)));
		if (iterateUpDown) {
			checkBlock = block.getRelative(BlockFace.UP);
			if (checkBlock.getType() == block.getType())
				toggleDoorBase(checkBlock, null, false, skipDoor, list);
			checkBlock = block.getRelative(BlockFace.DOWN);
			if (checkBlock.getType() == block.getType())
				toggleDoorBase(checkBlock, null, false, skipDoor, list);
		}
		if (keyBlock != null) {
			checkBlock = block.getRelative(BlockFace.NORTH);
			if (checkBlock.getType() == block.getType()) {
				if (((checkBlock.getX() == keyBlock.getX()) && (checkBlock.getZ() == keyBlock.getZ())) || ((block.getX() == keyBlock.getX()) && (block.getZ() == keyBlock.getZ()))) {
					toggleDoorBase(checkBlock, null, true, false, list);
				}
			}
			checkBlock = block.getRelative(BlockFace.EAST);
			if (checkBlock.getType() == block.getType()) {
				if (((checkBlock.getX() == keyBlock.getX()) && (checkBlock.getZ() == keyBlock.getZ())) || ((block.getX() == keyBlock.getX()) && (block.getZ() == keyBlock.getZ()))) {
					toggleDoorBase(checkBlock, null, true, false, list);
				}
			}
			checkBlock = block.getRelative(BlockFace.SOUTH);
			if (checkBlock.getType() == block.getType()) {
				if (((checkBlock.getX() == keyBlock.getX()) && (checkBlock.getZ() == keyBlock.getZ())) || ((block.getX() == keyBlock.getX()) && (block.getZ() == keyBlock.getZ()))) {
					toggleDoorBase(checkBlock, null, true, false, list);
				}
			}
			checkBlock = block.getRelative(BlockFace.WEST);
			if (checkBlock.getType() == block.getType()) {
				if (((checkBlock.getX() == keyBlock.getX()) && (checkBlock.getZ() == keyBlock.getZ())) || ((block.getX() == keyBlock.getX()) && (block.getZ() == keyBlock.getZ()))) {
					toggleDoorBase(checkBlock, null, true, false, list);
				}
			}
		}
	}

	public static int getSignOption(Block signBlock, String tag, String altTag, int defaultValue) {
		Sign sign = (Sign) signBlock.getState();
		String line;
		int x, y, end, index;
		for (y = 2; y <= 3; ++y)
			if (!sign.getLine(y).isEmpty()) {
				line = sign.getLine(y).replaceAll("(?i)\u00A7[0-F]", "");
				end = line.length() - 1;
				if (end >= 2)
					if ((line.charAt(0) == '[') && (line.charAt(end) == ']')) {
						index = line.indexOf(":");
						if (index == -1) {
							if (line.substring(1, end).equalsIgnoreCase(tag) || line.substring(1, end).equalsIgnoreCase(altTag)) {
								return (defaultValue);
							}
						} else {
							if (line.substring(1, index).equalsIgnoreCase(tag) || line.substring(1, index).equalsIgnoreCase(altTag)) {
								for (x = index; x < end; ++x) {
									if (Character.isDigit(line.charAt(x))) {
										index = x;
										break;
									}
								}
								for (x = index + 1; x < end; ++x) {
									if (!Character.isDigit(line.charAt(x))) {
										end = x;
										break;
									}
								}
								try {
									int value = Integer.parseInt(line.substring(index, end));
									return (value);
								} catch (NumberFormatException ex) {
									return (defaultValue);
								}
							}
						}
					}
			}
		return (defaultValue);
	}

	public static boolean isInList(Object target, List<?> list) {
		if (list == null)
			return (false);
		for (int x = 0; x < list.size(); ++x)
			if (list.get(x).equals(target))
				return (true);
		return (false);
	}

	public static boolean isHackFormat(String line) {
		String[] strs = line.split(":");
		return (line.indexOf(":") > 1 && strs[1].length() == 36) ? true : false;
	}

	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	static public String getPlayerName(String str) {
		return trim(((str.indexOf(":") > 0) ? str.split(":")[0] : str));
	}

	static public String getPlayerUUIDString(String str) {
		return trim(((str.indexOf(":") > 0) ? str.split(":")[1] : str));
	}

	static public UUID getPlayerUUID(String str) {
		return UUID.fromString(getPlayerUUIDString(str));
	}

	static void setLine(Sign sign, int index, String typed) {
		OfflinePlayer player = null;
		if (!typed.isEmpty() && typed.indexOf("[") != 0) {
			String id = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', typed));
			player = CraftGo.Player.getOfflinePlayer(id);
		}
		setLine(sign, index, typed, player);
	}

	static void setLine(Sign sign, int index, String typed, OfflinePlayer player) {
		String cline = ChatColor.translateAlternateColorCodes('&', typed);
		if (Lockette.DEBUG) {
			System.out.println("[Lockette](setLine) cline : " + cline);
		}
		sign.setLine(index, cline);
		sign.update(true);
		UUID[] uuids = null;
		if (!sign.hasMetadata(META_KEY) || sign.getMetadata(META_KEY).size() < 1) {
			uuids = new UUID[3];
			sign.setMetadata(META_KEY, new FixedMetadataValue(Server.getPlugin(), uuids));
		} else {
			List<MetadataValue> list = sign.getMetadata(META_KEY);
			uuids = (UUID[]) list.get(0).value();
		}
		uuids[index - 1] = (player != null) ? player.getUniqueId() : null;
		if (Lockette.DEBUG) {
			Logger.debug("[" + Lockette.pluginName + "] setting the line " + index + " to " + cline);
			Logger.debug("[" + Lockette.pluginName + "] corresponding player is " + player);
			Logger.debug("[" + Lockette.pluginName + "] uuid has been attached: " + uuids[index - 1]);
		}
	}

	public static UUID getUUIDFromMeta(Sign sign, int index) {
		if (sign.hasMetadata(META_KEY) && sign.getMetadata(META_KEY).size() > 0) {
			List<MetadataValue> list = sign.getMetadata(META_KEY);
			UUID uuid = ((UUID[]) list.get(0).value())[index - 1];
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] uuid : " + uuid);
			}
			return uuid;
		}
		return null;
	}

	static void removeUUIDMetadata(Sign sign) {
		if (sign.hasMetadata(META_KEY)) {
			sign.removeMetadata(META_KEY, Server.getPlugin());
		}
	}

	static public boolean oldFormatCheck(String signname, String pname) {
		signname = ChatColor.stripColor(signname);
		pname = ChatColor.stripColor(pname);
		int length = pname.length();
		if (length > 15)
			length = 15;
		return signname.equalsIgnoreCase(pname.substring(0, length));
	}

	static public boolean matchUserUUID(Sign sign, int index, OfflinePlayer player, boolean update) {
		try {
			String line = sign.getLine(index);
			String checkline = ChatColor.stripColor(line);
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] matchUserUUID : checkline = " + checkline);
			}
			if ((checkline.indexOf("[") == 0 && checkline.indexOf("]") > 1) || line.isEmpty()) {
				return false;
			}
			if (!uuidSupport) {
				if (Lockette.DEBUG) {
					Logger.debug("[" + Lockette.pluginName + "] NO UUID support, doing old name checking.");
				}
				String pname = player.getName();
				String against = checkline.split(":")[0].trim();
				return oldFormatCheck(against, pname);
			}
			UUID uuid = null;
			String name = getPlayerName(line);
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] Name on the sign is : " + name);
			}
			if (isHackFormat(line)) {
				try {
					uuid = getPlayerUUID(line);
				} catch (IllegalArgumentException e) {
					Logger.debug("[" + Lockette.pluginName + "] Invalid Player UUID!");
					return false;
				}
				if (uuid != null && update) {
					OfflinePlayer p = Bukkit.getOfflinePlayer(uuid);
					if (Lockette.DEBUG) {
						Logger.debug("[" + Lockette.pluginName + "] updating the old hacked format for " + p);
					}
					setLine(sign, index, name, p);
				}
				sign.update();
			}
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] hasMeta? : " + sign.hasMetadata(META_KEY));
				Logger.debug("[" + Lockette.pluginName + "] uuid ? : " + getUUIDFromMeta(sign, index));
			}
			if (!sign.hasMetadata(META_KEY) || getUUIDFromMeta(sign, index) == null) {
				if (Lockette.DEBUG) {
					Logger.debug("[" + Lockette.pluginName + "] Checking for original format for " + checkline);
				}
				OfflinePlayer oplayer = CraftGo.Player.getOfflinePlayer(checkline);
				if (oplayer != null && oplayer.hasPlayedBefore()) {
					if (Lockette.DEBUG) {
						Logger.debug("[" + Lockette.pluginName + "] converting original format for " + oplayer + " name = " + checkline);
					}
					setLine(sign, index, line, oplayer);
				} else {
					String pname = player.getName();
					String against = checkline.split(":")[0].trim();
					if (oldFormatCheck(against, pname)) {
						if (Lockette.DEBUG) {
							Logger.debug("[" + Lockette.pluginName + "] Partial match! Converting original format for " + player.getName() + " name = " + checkline);
						}
						setLine(sign, index, player.getName(), player);
					}
				}
				sign.update();
			}
			uuid = getUUIDFromMeta(sign, index);
			if (Lockette.DEBUG) {
				Logger.debug("[" + Lockette.pluginName + "] uuid on the sign = " + uuid);
				Logger.debug("[" + Lockette.pluginName + "] player's uuid    = " + player.getUniqueId());
			}
			if (uuid != null) {
				OfflinePlayer oplayer = Bukkit.getOfflinePlayer(uuid);
				if (!oplayer.hasPlayedBefore()) {
					if (Lockette.DEBUG) {
						Logger.debug("[" + Lockette.pluginName + "] removing bad UUID");
					}
					removeUUIDMetadata(sign);
				} else {
					if (uuid.equals(player.getUniqueId())) {
						if (!ChatColor.stripColor(ChatColor.stripColor(name)).equals(player.getName())) {
							if (Lockette.DEBUG) {
								Logger.debug("[" + Lockette.pluginName + "] setting name : " + player.getName());
							}
							sign.setLine(index, player.getName());
							sign.update();
						}
						if (Lockette.DEBUG) {
							Logger.debug("[" + Lockette.pluginName + "] uuid equal");
						}
						return true;
					} else {
						if (Lockette.DEBUG) {
							Logger.debug("[" + Lockette.pluginName + "] reset name to " + oplayer.getName());
						}
						sign.setLine(index, oplayer.getName());
						sign.update();
					}
				}
			} else {
				List<String> names = getPreviousNames(player.getUniqueId());
				for (String n : names) {
					if (n.equalsIgnoreCase(name)) {
						if (Lockette.DEBUG) {
							Logger.debug("[" + Lockette.pluginName + "] Found the match in the name history!");
						}
						setLine(sign, index, player.getName(), player);
						return true;
					}
				}
			}
		} catch (Exception e) {
			Logger.debug("[" + Lockette.pluginName + "] Something bad happened returning match = false");
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isOwner(Block block, String name) {
		return isOwner(block, CraftGo.Player.getOfflinePlayer(name));
	}

	public static boolean isUser(Block block, String name, boolean withGroups) {
		return isUser(block, CraftGo.Player.getOfflinePlayer(name), withGroups);
	}

	public static boolean isOwner(Block block, OfflinePlayer player) {
		if (!enabled)
			return true;
		Block checkBlock = Lockette.findBlockOwner(block);
		if (checkBlock == null)
			return true;
		Sign sign = (Sign) checkBlock.getState();
		return matchUserUUID(sign, 1, player, true);
	}

	public static boolean isOwner(Sign sign, OfflinePlayer player) {
		return matchUserUUID(sign, 1, player, true);
	}

	public boolean inGroup(World world, Player player, String groupName) {
		return (inGroup(world, player, player.getName(), groupName));
	}

	public boolean inGroup(World world, String playerName, String groupName) {
		return (inGroup(world, Server.getPlugin().getServer().getPlayer(playerName), playerName, groupName));
	}

	public boolean inGroup(World world, Player player, String playerName, String groupName) {
		String local;
		if (groupName.equalsIgnoreCase("[Everyone]"))
			return (true);
		local = getLocalizedEveryone();
		if (local != null)
			if (groupName.equalsIgnoreCase(local))
				return (true);
		if (player != null)
			if (player.isOp()) {
				if (groupName.equalsIgnoreCase("[Operators]"))
					return (true);
				local = getLocalizedOperators();
				if (local != null)
					if (groupName.equalsIgnoreCase(local))
						return (true);
			}
		return (false);
	}

	public static boolean isUser(Block block, OfflinePlayer player, boolean withGroups) {
		if (!enabled)
			return true;
		Block signBlock = Lockette.findBlockOwner(block);
		if (signBlock == null)
			return true;
		Sign sign = (Sign) signBlock.getState();
		for (int y = 1; y <= 3; ++y) {
			String line = sign.getLine(y);
			if (matchUserUUID(sign, y, player, true)) {
				return true;
			}
			if (withGroups) {
				if (plugin.inGroup(block.getWorld(), player.getName(), line))
					return true;
			}
		}
		List<Block> list = Lockette.findBlockUsers(block, signBlock);
		for (Block blk : list) {
			sign = (Sign) blk.getState();
			for (int y = 1; y <= 3; y++) {
				String line = sign.getLine(y);
				if (matchUserUUID(sign, y, player, true)) {
					return true;
				}
				if (withGroups)
					if (plugin.inGroup(block.getWorld(), player.getName(), line))
						return true;
			}
		}
		return false;
	}

	public static String NAME_HISTORY_URL = "https://api.mojang.com/user/profiles/";
	public static final JSONParser jsonParser = new JSONParser();

	public static List<String> getPreviousNames(UUID uuid) {
		String name = null;
		List<String> list = new ArrayList<String>();
		try {
			if (name == null) {
				HttpURLConnection connection = (HttpURLConnection) new URL(NAME_HISTORY_URL + uuid.toString().replace("-", "") + "/names").openConnection();
				connection.setConnectTimeout(5000);
				connection.setReadTimeout(5000);
				JSONArray array = (JSONArray) jsonParser.parse(new InputStreamReader(connection.getInputStream()));
				Iterator<JSONObject> iterator = array.iterator();
				while (iterator.hasNext()) {
					JSONObject obj = iterator.next();
					list.add((String) obj.get("name"));
				}
			}
		} catch (java.net.SocketTimeoutException ste) {
			Logger.debug("[" + Lockette.pluginName + "] Connection timeout (to Mojang site)");
		} catch (Exception ioe) {
			Logger.debug("[" + Lockette.pluginName + "] Failed to get Name history!");
		}
		return list;
	}
}
