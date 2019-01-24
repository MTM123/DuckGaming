/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.association.RegionAssociable;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import fr.xephi.authme.AuthMe;
import fr.xephi.authme.api.NewAPI;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.Directory;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.DesignPattern;
import me.skorrloregaming.impl.EnchantInfo;
import me.skorrloregaming.impl.ServerMinigame;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

public class Go {
	public static String italicGray = ChatColor.GRAY + "" + ChatColor.ITALIC;
	public static String consoleTag =  ChatColor.RED + "[" +  ChatColor.GRAY + "Console" +  ChatColor.RED + "] " +  ChatColor.RED;
	public static List<String> validRanks = Arrays.asList("default", "founder", "admin", "moderator", "helper", "donator", "redstone", "obsidian", "bedrock");
	public static List<String> validMinigames = Arrays.asList("kitpvp", "factions", "survival", "skyfight", "creative");
	public static List<String> validStorageMinigames = Arrays.asList("kitpvp", "factions", "survival", "creative");

	public static void playLackPermissionMessage(CommandSender player) {
		player.sendMessage(String.valueOf(Legacy.tag) +  ChatColor.RED + "You do not have permission to use this command.");
	}

	public static String toRankTag(String str) {
		if (str.equals("default")) {
			return  ChatColor.DARK_GRAY + "[" +  ChatColor.GRAY + "Member" +  ChatColor.DARK_GRAY + "] " +  ChatColor.GRAY;
		}
		if (str.equals("helper")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Helper" +  ChatColor.RED + "] ";
		}
		if (str.equals("moderator")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Moderator" +  ChatColor.RED + "] ";
		}
		if (str.equals("admin")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Admin" +  ChatColor.RED + "] ";
		}
		if (str.equals("founder")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Founder" +  ChatColor.RED + "] ";
		}
		if (str.equals("donator")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Donator" +  ChatColor.RED + "] ";
		}
		if (str.equals("redstone")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Redstone" +  ChatColor.RED + "] ";
		}
		if (str.equals("obsidian")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Obsidian" +  ChatColor.RED + "] ";
		}
		if (str.equals("bedrock")) {
			return  ChatColor.RED + "[" +  ChatColor.GRAY + "Bedrock" +  ChatColor.RED + "] ";
		}
		return "";
	}

	public static String getRank(UUID id, Plugin plugin) {
		if (Go.isRankingEnabled(plugin) && plugin.getConfig().contains("config." + id.toString() + ".rank")) {
			return plugin.getConfig().getString("config." + id.toString() + ".rank");
		}
		return validRanks.get(0);
	}

	public static String getRank(Player player, Plugin plugin) {
		return Go.getRank(player.getUniqueId(), plugin);
	}

	public static int getRankId(UUID id, Plugin plugin) {
		String rank = Go.getRank(id, plugin);
		if (rank.equals("default") || !Go.isRankingEnabled(plugin)) {
			return -1;
		}
		if (rank.equals("bedrock")) {
			return -5;
		}
		if (rank.equals("obsidian")) {
			return -4;
		}
		if (rank.equals("redstone")) {
			return -3;
		}
		if (rank.equals("donator")) {
			return -2;
		}
		if (rank.equals("helper")) {
			return 0;
		}
		if (rank.equals("moderator")) {
			return 1;
		}
		if (rank.equals("admin")) {
			return 2;
		}
		if (rank.equals("founder")) {
			return 3;
		}
		return -100;
	}

	public static int getRankId(Player player, Plugin plugin) {
		return Go.getRankId(player.getUniqueId(), plugin);
	}

	public static String formatTime(int seconds) {
		if (seconds < 60) {
			return String.valueOf(seconds) + "s";
		}
		int minutes = seconds / 60;
		int s = 60 * minutes;
		int secondsLeft = seconds - s;
		if (minutes < 60) {
			if (secondsLeft > 0) {
				return String.valueOf(String.valueOf(minutes) + "m " + secondsLeft + "s");
			}
			return String.valueOf(String.valueOf(minutes) + "m");
		}
		if (minutes < 1440) {
			String time = "";
			int hours = minutes / 60;
			time = String.valueOf(hours) + "h";
			int inMins = 60 * hours;
			int leftOver = minutes - inMins;
			if (leftOver >= 1) {
				time = String.valueOf(time) + " " + leftOver + "m";
			}
			if (secondsLeft > 0) {
				time = String.valueOf(time) + " " + secondsLeft + "s";
			}
			return time;
		}
		String time = "";
		int days = minutes / 1440;
		time = String.valueOf(days) + "d";
		int inMins = 1440 * days;
		int leftOver = minutes - inMins;
		if (leftOver >= 1) {
			if (leftOver < 60) {
				time = String.valueOf(time) + " " + leftOver + "m";
			} else {
				int hours = leftOver / 60;
				time = String.valueOf(time) + " " + hours + "h";
				int hoursInMins = 60 * hours;
				int minsLeft = leftOver - hoursInMins;
				if (leftOver >= 1) {
					time = String.valueOf(time) + " " + minsLeft + "m";
				}
			}
		}
		if (secondsLeft > 0) {
			time = String.valueOf(time) + " " + secondsLeft + "s";
		}
		return time;
	}

	public static void setEnableEffects(Player player, boolean value, Plugin plugin) {
		String path = "config." + player.getUniqueId().toString();
		plugin.getConfig().set(String.valueOf(path) + ".effects",  value);
		plugin.saveConfig();
	}

	public static boolean isEffectsEnabled(Player player, Plugin plugin) {
		String path = "config." + player.getUniqueId().toString();
		if (plugin.getConfig().contains(String.valueOf(path) + ".effects")) {
			return plugin.getConfig().getBoolean(String.valueOf(path) + ".effects");
		}
		return true;
	}

	public static String createMotd(String lineOne, String lineTwo, boolean[] centered) {
		int perLineLength = 45;
		int lineOneSpaces = perLineLength - ChatColor.stripColor(lineOne).length();
		int lineTwoSpaces = perLineLength - ChatColor.stripColor(lineTwo).length();
		StringBuilder sb = new StringBuilder();
		StringBuilder sbLineOneSpaces = new StringBuilder();
		for (int i = 0; i < lineOneSpaces + 1; i++) {
			sbLineOneSpaces.append(" ");
		}
		String strLineOneSpaces = sbLineOneSpaces.toString();
		StringBuilder sbLineTwoSpaces = new StringBuilder();
		for (int i = 0; i < lineTwoSpaces + 1; i++) {
			sbLineTwoSpaces.append(" ");
		}
		String strLineTwoSpaces = sbLineTwoSpaces.toString();
		if (centered[0]) {
			final int middle = strLineOneSpaces.length() / 2;
			String[] parts = {strLineOneSpaces.substring(0, middle), strLineOneSpaces.substring(middle)};
			sb.append(parts[0] + parts[0]);
			sb.append(lineOne);
			sb.append('\n');
		} else {
			sb.append(lineOne + strLineOneSpaces);
		}
		if (centered[1]) {
			final int middle = strLineTwoSpaces.length() / 2;
			String[] parts = {strLineTwoSpaces.substring(0, middle), strLineTwoSpaces.substring(middle)};
			sb.append(parts[0] + parts[0]);
			sb.append(lineTwo);
		} else {
			sb.append(lineTwo);
		}
		return sb.toString();
	}

	public static String formatPackageVersion(String version) {
		return version.replace("_", ".").substring(0, version.lastIndexOf("R") - 1).substring(1);
	}

	public static int getAvailableSlot(ItemStack[] itemArray) {
		int i = 0;
		while (i < itemArray.length) {
			ItemStack item = itemArray[i];
			if (item == null || item.getType() == Material.AIR) {
				return i;
			}
			++i;
		}
		return -1;
	}

	public static Object getAuthenticationSuite() {
		if (Bukkit.getPluginManager().getPlugin("AuthMe") == null) {
			return null;
		}
		return AuthMe.getApi();
	}

	public static boolean isMinigameEnabled(ServerMinigame minigame, Plugin plugin) {
		if (minigame == ServerMinigame.HUB) {
			return true;
		}
		if (minigame == ServerMinigame.UNKNOWN) {
			return false;
		}
		if (plugin.getConfig().contains("settings.enable." + minigame.toString().toLowerCase())) {
			return plugin.getConfig().getBoolean("settings.enable." + minigame.toString().toLowerCase());
		}
		return true;
	}

	public static boolean isRankingEnabled(Plugin plugin) {
		if (!Go.isPluginPirated()) {
			if (plugin.getConfig().contains("settings.ranking.enable")) {
				return plugin.getConfig().getBoolean("settings.ranking.enable");
			}
			return true;
		}
		return false;
	}

	public static boolean isPrefixedRankingEnabled(Plugin plugin) {
		if (!Go.isRankingEnabled(plugin)) {
			return false;
		}
		if (plugin.getConfig().contains("settings.ranking.prefix")) {
			return plugin.getConfig().getBoolean("settings.ranking.prefix");
		}
		return true;
	}

	public static boolean isWelcomeMessageEnabled(Plugin plugin) {
		if (!Go.isPluginPirated()) {
			if (plugin.getConfig().contains("settings.enable.welcomeMessage")) {
				return plugin.getConfig().getBoolean("settings.enable.welcomeMessage");
			}
			return true;
		}
		return false;
	}

	public static boolean isCustomJoinMessageEnabled(Plugin plugin) {
		if (!Go.isPluginPirated()) {
			if (plugin.getConfig().contains("settings.customJoinMessage")) {
				return plugin.getConfig().getBoolean("settings.customQuitMessage");
			}
			return true;
		}
		return false;
	}

	public static boolean isCustomQuitMessageEnabled(Plugin plugin) {
		if (!Go.isPluginPirated()) {
			if (plugin.getConfig().contains("settings.customJoinMessage")) {
				return plugin.getConfig().getBoolean("settings.customQuitMessage");
			}
			return true;
		}
		return false;
	}

	public static boolean isPluginPirated() {
		return false;
	}

	public static int[] sortNumericallyReverse(int[] integerArray) {
		int[] sortedArray = Go.sortNumerically(integerArray);
		int[] reverseSortedNum = new int[sortedArray.length];
		int i = 0;
		while (i < sortedArray.length) {
			reverseSortedNum[i] = sortedArray[sortedArray.length - 1 - i];
			++i;
		}
		return reverseSortedNum;
	}

	public static int[] sortNumerically(int[] integerArray) {
		int[] sorted = (int[]) integerArray.clone();
		int i = 0;
		while (i < sorted.length) {
			int j = i + 1;
			while (j < sorted.length) {
				if (sorted[i] > sorted[j] && i != j) {
					int temp = sorted[j];
					sorted[j] = sorted[i];
					sorted[i] = temp;
				}
				++j;
			}
			++i;
		}
		return sorted;
	}

	public static double[] sortNumericallyReverse(double[] doubleArray) {
		double[] sortedArray = Go.sortNumerically(doubleArray);
		double[] reverseSortedNum = new double[sortedArray.length];
		int i = 0;
		while (i < sortedArray.length) {
			reverseSortedNum[i] = sortedArray[sortedArray.length - 1 - i];
			++i;
		}
		return reverseSortedNum;
	}

	public static double[] sortNumerically(double[] doubleArray) {
		double[] sorted = (double[]) doubleArray.clone();
		int i = 0;
		while (i < sorted.length) {
			int j = i + 1;
			while (j < sorted.length) {
				if (sorted[i] > sorted[j] && i != j) {
					double temp = sorted[j];
					sorted[j] = sorted[i];
					sorted[i] = temp;
				}
				++j;
			}
			++i;
		}
		return sorted;
	}

	public static Date getLastCompilationTime() {
		Date d = null;
		Class<?> currentClass = new Object() {
		}.getClass().getEnclosingClass();
		URL resource = currentClass.getResource(currentClass.getSimpleName() + ".class");
		if (resource != null) {
			if (resource.getProtocol().equals("file")) {
				try {
					d = new Date(new File(resource.toURI()).lastModified());
				} catch (URISyntaxException ignored) {
				}
			} else if (resource.getProtocol().equals("jar")) {
				String path = resource.getPath();
				d = new Date(new File(path.substring(5, path.indexOf("!"))).lastModified());
			} else if (resource.getProtocol().equals("zip")) {
				String path = resource.getPath();
				File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
				try (JarFile jf = new JarFile(jarFileOnDisk)) {
					ZipEntry ze = jf.getEntry(path.substring(path.indexOf("!") + 2));
					long zeTimeLong = ze.getTime();
					Date zeTimeDate = new Date(zeTimeLong);
					d = zeTimeDate;
				} catch (IOException | RuntimeException ignored) {
				}
			}
		}
		return d;
	}

	public static boolean isNothing(ItemStack item) {
		if (item != null && item.getType() != Material.AIR) {
			return false;
		}
		return true;
	}

	public static ItemStack createMaterial(Material material) {
		return Go.createMaterial(material, 1, "unspecified", (short) 0);
	}

	public static ItemStack createMaterial(Material material, int amount) {
		return Go.createMaterial(material, amount, "unspecified", (short) 0);
	}

	public static ItemStack createMaterial(Material material, int amount, short durability) {
		return Go.createMaterial(material, amount, "unspecified", durability);
	}

	public static ItemStack createMaterial(Material material, int amount, String displayName) {
		return Go.createMaterial(material, amount, displayName, (short) 0);
	}

	public static ItemStack createMaterial(Material material, String displayName) {
		return Go.createMaterial(material, 1, displayName, (short) 0);
	}

	public static ItemStack createMaterial(Material material, int amount, String displayName, short durability) {
		ItemStack itemStack = new ItemStack(material, amount, durability);
		if (displayName.equals("unspecified")) {
			return itemStack;
		}
		ItemMeta meta = itemStack.getItemMeta();
		meta.setDisplayName(displayName);
		itemStack.setItemMeta(meta);
		return itemStack;
	}

	public static DesignPattern getPattern(Location location) {
		Material middleCenter = location.clone().subtract(0.0, 0.0, 0.0).getBlock().getType();
		Material innerCircle = location.clone().subtract(1.0, 0.0, 0.0).getBlock().getType();
		Material outerBlocks = location.clone().subtract(1.0, 0.0, 1.0).getBlock().getType();
		boolean succeededInnerCircle = false;
		if (location.clone().subtract(0.0, 0.0, 1.0).getBlock().getType() == innerCircle && location.clone().subtract(-1.0, 0.0, 0.0).getBlock().getType() == innerCircle && location.clone().subtract(0.0, 0.0, -1.0).getBlock().getType() == innerCircle) {
			succeededInnerCircle = true;
		}
		boolean succeededOuterBlocks = false;
		if (location.clone().subtract(1.0, 0.0, -1.0).getBlock().getType() == outerBlocks && location.clone().subtract(-1.0, 0.0, -1.0).getBlock().getType() == outerBlocks && location.clone().subtract(-1.0, 0.0, 1.0).getBlock().getType() == outerBlocks) {
			succeededOuterBlocks = true;
		}
		return new DesignPattern(middleCenter, innerCircle, outerBlocks, succeededInnerCircle && succeededOuterBlocks);
	}

	public static boolean isStringNumeric(String string) {
		char[] arrc = string.toCharArray();
		int n = arrc.length;
		int n2 = 0;
		while (n2 < n) {
			char c = arrc[n2];
			if (c < '0' || c > '9') {
				return false;
			}
			++n2;
		}
		return true;
	}

	public static boolean isValidAddress(String string) {
		String[] parts = string.split("\\.", -1);
		if (parts.length == 4) {
			if (Arrays.stream(parts).filter(Go::isStringNumeric).map(Integer::parseInt).filter(i -> i <= 255 && i >= 0).count() == 4L) {
				return true;
			}
		}
		return false;
	}

	public static long getSinglePricing(double shopAmount, double shopPrice) {
		return Math.round(shopPrice / shopAmount);
	}

	public static ItemStack addLore(ItemStack stack, String[] lore) {
		ItemStack item = stack.clone();
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setLore(Arrays.asList(lore));
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack addEnchant(ItemStack stack, EnchantInfo enchantment) {
		ItemStack item = stack;
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.addEnchant(enchantment.enchant, enchantment.power, true);
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack addDamage(ItemStack stack, int damage) {
		ItemStack item = new ItemStack(stack.getType(), stack.getAmount(), (short) damage);
		return item;
	}

	public static ItemStack addLeatherColor(ItemStack stack, Color color) {
		ItemStack itm = stack;
		LeatherArmorMeta im = (LeatherArmorMeta) itm.getItemMeta();
		im.setColor(color);
		itm.setItemMeta((ItemMeta) im);
		return itm;
	}

	public static Color getLeatherColor(ItemStack stack) {
		ItemStack itm = stack;
		LeatherArmorMeta im = (LeatherArmorMeta) itm.getItemMeta();
		return im.getColor();
	}

	public static ItemStack addBookEnchantment(ItemStack item, Enchantment enchantment, int level) {
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(enchantment, level, true);
		item.setItemMeta((ItemMeta) meta);
		return item;
	}

	public static ItemStack setUnbreakable(ItemStack stack, boolean value) {
		ItemMeta meta = stack.getItemMeta();
		meta.setUnbreakable(value);
		stack.setItemMeta(meta);
		return stack;
	}

	public static boolean isShulkerBox(ItemStack stack) {
		if (stack.getType() == Material.BLACK_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.BLUE_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.BROWN_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.CYAN_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.GREEN_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.GRAY_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.LIGHT_BLUE_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.LIME_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.MAGENTA_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.ORANGE_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.PINK_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.PURPLE_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.RED_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.SILVER_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.WHITE_SHULKER_BOX) {
			return true;
		}
		if (stack.getType() == Material.YELLOW_SHULKER_BOX) {
			return true;
		}
		return false;
	}

	public static boolean isAuthenticated(Player player) {
		Object authObject = Go.getAuthenticationSuite();
		boolean isAuthenticated = false;
		isAuthenticated = authObject == null ? true : ((NewAPI) authObject).isAuthenticated(player);
		return isAuthenticated;
	}

	public static boolean isAuthenticationCommand(String command) {
		if (command.toLowerCase().startsWith("/register")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/reg")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/login")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/log")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/l")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/unregister")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/unreg")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/changepassword")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/cp")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/captcha")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/autologin")) {
			return true;
		}
		if (command.toLowerCase().startsWith("/autolog")) {
			return true;
		}
		return false;
	}

	public static boolean isRawItemRepairable(ItemStack stack) {
		if (Directory.repairableItems.contains( stack.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isRawArmorRepairable(ItemStack stack) {
		if (Directory.repairableArmor.contains( stack.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isRawRepairable(ItemStack stack) {
		if (Directory.repairableArmor.contains( stack.getType()) || Directory.repairableItems.contains( stack.getType())) {
			return true;
		}
		return false;
	}

	public static boolean isRepairable(ItemStack stack) {
		if (stack.getDurability() == 0) {
			return false;
		}
		if (Directory.repairableArmor.contains( stack.getType()) || Directory.repairableItems.contains( stack.getType())) {
			return true;
		}
		return false;
	}

	public static void clearPlayer(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(Go.createMaterial(Material.AIR));
		player.getInventory().setChestplate(Go.createMaterial(Material.AIR));
		player.getInventory().setLeggings(Go.createMaterial(Material.AIR));
		player.getInventory().setBoots(Go.createMaterial(Material.AIR));
		player.updateInventory();
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}

	public static boolean createDataFolder(Plugin plugin) {
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
			return true;
		}
		return false;
	}

	public static Location getRandomLocation(Player player) {
		double x = (double) (new Random(System.currentTimeMillis() * 100000L).nextInt(2500) + 500) + 0.5;
		double z = (double) (new Random(System.currentTimeMillis() * 250000L).nextInt(2500) + 500) + 0.5;
		double y = player.getWorld().getHighestBlockAt((int) x, (int) z).getY();
		return new Location(player.getWorld(), x, y, z);
	}

	public static Location getZoneLocation(String zone, Plugin plugin) {
		if (Server.warpConfig.getData().contains(zone)) {
			World world = plugin.getServer().getWorld(Server.warpConfig.getData().getString(String.valueOf(zone) + ".world"));
			double x = Server.warpConfig.getData().getDouble(String.valueOf(zone) + ".x");
			double y = Server.warpConfig.getData().getDouble(String.valueOf(zone) + ".y");
			double z = Server.warpConfig.getData().getDouble(String.valueOf(zone) + ".z");
			float yaw = (float) Server.warpConfig.getData().getDouble(String.valueOf(zone) + ".yaw");
			float pitch = (float) Server.warpConfig.getData().getDouble(String.valueOf(zone) + ".pitch");
			return new Location(world, x, y, z, yaw, pitch);
		}
		Bukkit.getConsoleSender().sendMessage("Fatal error: Failed to find a saved zone named '" + zone + "' --");
		World basicWorld = (World) Bukkit.getWorlds().get(0);
		return new Location(basicWorld, 0.0, (double) basicWorld.getHighestBlockYAt(0, 0), 0.0, 0.0f, 0.0f);
	}

	public static Player[] getStaffOnline(Plugin plugin) {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (Go.getRankId(player.getUniqueId(), plugin) <= -1) continue;
			players.add(player);
		}
		Player[] arr = players.toArray(new Player[0]);
		players.clear();
		return arr;
	}

	public static String getEntityName(Entity entity) {
		EntityType type = EntityType.UNKNOWN;
		try {
			type = entity.getType();
		} catch (Exception exception) {
			// empty catch block
		}
		return WordUtils.capitalize((String) type.getName().toLowerCase());
	}

	public static void setNickname(Player player, String nickname, Plugin plugin) {
		String path = "config." + player.getUniqueId().toString();
		plugin.getConfig().set(String.valueOf(path) + ".nickname",  nickname);
	}

	public static String formatEnchantPower(int power) {
		if (power > 10) {
			return "STATE_UNKNOWN_POWER";
		}
		if (power == 10) {
			return "X";
		}
		if (power == 9) {
			return "IX";
		}
		if (power == 8) {
			return "VIII";
		}
		if (power == 7) {
			return "VII";
		}
		if (power == 6) {
			return "VI";
		}
		if (power == 5) {
			return "V";
		}
		if (power == 4) {
			return "IV";
		}
		if (power == 3) {
			return "III";
		}
		if (power == 2) {
			return "II";
		}
		if (power == 1) {
			return "I";
		}
		return "";
	}

	public static String formatEnchantment(String enchantmentID, int enchantPower) {
		String power = Go.formatEnchantPower(enchantPower);
		if (enchantmentID.equals("PROTECTION_ENVIRONMENTAL")) {
			return "Protection " + power;
		}
		if (enchantmentID.equals("PROTECTION_FALL")) {
			return "Feather Falling " + power;
		}
		if (enchantmentID.equalsIgnoreCase("DIG_SPEED")) {
			return "Efficiency " + power;
		}
		if (enchantmentID.equals("DAMAGE_ALL")) {
			return "Sharpness " + power;
		}
		if (enchantmentID.equals("ARROW_DAMAGE")) {
			return "Power " + power;
		}
		if (enchantmentID.equals("ARROW_KNOCKBACK")) {
			return "Punch " + power;
		}
		if (enchantmentID.equals("ARROW_FIRE")) {
			return "Flame " + power;
		}
		if (enchantmentID.equals("DURABILITY")) {
			return "Unbreaking " + power;
		}
		if (enchantmentID.equals("LOOT_BONUS_BLOCKS")) {
			return "Fortune " + power;
		}
		if (enchantmentID.equals("LOOT_BONUS_MOBS")) {
			return "Looting " + power;
		}
		return String.valueOf(Go.capitalizeAll(enchantmentID, "_")) + " " + power;
	}

	public static String unformatEnchantment(String enchantment) {
		if (enchantment.equalsIgnoreCase("Protection")) {
			return "PROTECTION_ENVIRONMENTAL";
		}
		if (enchantment.equalsIgnoreCase("Feather Falling")) {
			return "PROTECTION_FALL";
		}
		if (enchantment.equalsIgnoreCase("Efficiency")) {
			return "DIG_SPEED";
		}
		if (enchantment.equalsIgnoreCase("Sharpness")) {
			return "DAMAGE_ALL";
		}
		if (enchantment.equalsIgnoreCase("Power")) {
			return "ARROW_DAMAGE";
		}
		if (enchantment.equalsIgnoreCase("Punch")) {
			return "ARROW_KNOCKBACK";
		}
		if (enchantment.equalsIgnoreCase("Flame")) {
			return "ARROW_FIRE";
		}
		if (enchantment.equalsIgnoreCase("Unbreaking")) {
			return "DURABILITY";
		}
		if (enchantment.equalsIgnoreCase("Fortune")) {
			return "LOOT_BONUS_BLOCKS";
		}
		if (enchantment.equalsIgnoreCase("Looting")) {
			return "LOOT_BONUS_MOBS";
		}
		return enchantment.toUpperCase().replace(" ", "_");
	}

	public static String formatMaterial(Material mat) {
		return Go.capitalizeAll(mat.toString(), "_");
	}

	public static String capitalizeAll(String str) {
		return Go.capitalizeAll(str, " ");
	}

	public static String capitalizeAll(String str, String var) {
		String[] arr = str.split(var);
		StringBuilder sb = new StringBuilder();
		String[] arrstring = arr;
		int n = arrstring.length;
		int n2 = 0;
		while (n2 < n) {
			String s = arrstring[n2];
			sb.append(String.valueOf(WordUtils.capitalize((String) s.toLowerCase())) + " ");
			++n2;
		}
		return sb.toString().trim();
	}

	public static double roundDouble(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static boolean hasPotionEffect(Player player, PotionEffectType type) {
		for (PotionEffect p : player.getActivePotionEffects()) {
			if (p.getType() != type) continue;
			return true;
		}
		return false;
	}

	public static ServerMinigame getCurrentMinigame(Player player) {
		String domain = Go.getMinigameDomain(player);
		if (domain.equals("factions")) {
			return ServerMinigame.FACTIONS;
		}
		if (domain.equals("kitpvp")) {
			return ServerMinigame.KITPVP;
		}
		if (domain.equals("skyfight")) {
			return ServerMinigame.SKYFIGHT;
		}
		if (domain.equals("creative")) {
			return ServerMinigame.CREATIVE;
		}
		if (domain.equals("survival")) {
			return ServerMinigame.SURVIVAL;
		}
		if (domain.equals("hub")) {
			return ServerMinigame.HUB;
		}
		return ServerMinigame.UNKNOWN;
	}

	public static ServerMinigame getMinigameFromWorld(World world, Plugin plugin) {
		if (Go.getZoneLocation("factions", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.FACTIONS;
		}
		if (Go.getZoneLocation("factions_nether", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.FACTIONS;
		}
		if (Go.getZoneLocation("factions_end", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.FACTIONS;
		}
		if (Go.getZoneLocation("survival", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.SURVIVAL;
		}
		if (Go.getZoneLocation("survival_nether", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.SURVIVAL;
		}
		if (Go.getZoneLocation("survival_end", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.SURVIVAL;
		}
		if (Go.getZoneLocation("kitpvp", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.KITPVP;
		}
		if (Go.getZoneLocation("skyfight0", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.SKYFIGHT;
		}
		if (Go.getZoneLocation("creative", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.CREATIVE;
		}
		if (Go.getZoneLocation("hub", plugin).getWorld().getName().equals(world.getName())) {
			return ServerMinigame.HUB;
		}
		return ServerMinigame.UNKNOWN;
	}

	public static String getMinigameDomain(Player player) {
		if (Server.factions.contains(player.getUniqueId())) {
			return "factions";
		}
		if (Server.kitpvp.contains(player.getUniqueId())) {
			return "kitpvp";
		}
		if (Server.skyfight.contains(player.getUniqueId())) {
			return "skyfight";
		}
		if (Server.creative.contains(player.getUniqueId())) {
			return "creative";
		}
		if (Server.survival.contains(player.getUniqueId())) {
			return "survival";
		}
		if (Server.hub.contains(player.getUniqueId())) {
			return "hub";
		}
		return "hub";
	}

	public static String getMinigameTag(Player player) {
		return Go.getMinigameTag(Go.getMinigameDomain(player));
	}

	public static String getMinigameTag(String domain) {
		if (domain.equals("factions")) {
			return Factions.tag;
		}
		if (domain.equals("kitpvp")) {
			return Kitpvp.tag;
		}
		if (domain.equals("skyfight")) {
			return Skyfight.tag;
		}
		if (domain.equals("creative")) {
			return Creative.tag;
		}
		if (domain.equals("survival")) {
			return Survival.tag;
		}
		if (domain.equals("hub")) {
			return Lobby.tag;
		}
		return Lobby.tag;
	}

	public static boolean isWithinSystemClaimedLand(Location location) {
		Faction faction;
		RegionManager regionManager = Server.getWorldGuard().getRegionManager(location.getWorld());
		ApplicableRegionSet arSet = regionManager.getApplicableRegions(location);
		if (arSet.size() == 0 && ((faction = Board.getInstance().getFactionAt(new FLocation(location))).isWarZone() || faction.isSafeZone())) {
			return true;
		}
		return false;
	}

	public static boolean isWithinUnclaimedLand(Location location, Player player) {
		RegionManager regionManager = Server.getWorldGuard().getRegionManager(location.getWorld());
		ApplicableRegionSet arSet = regionManager.getApplicableRegions(location);
		if (arSet.size() == 0 || arSet.testState((RegionAssociable) Server.getWorldGuard().wrapPlayer(player), new StateFlag[]{DefaultFlag.BLOCK_BREAK})) {
			Faction faction = Board.getInstance().getFactionAt(new FLocation(location));
			if (faction.isWilderness()) {
				return true;
			}
			if (player != null && FPlayers.getInstance().getByPlayer(player).isInOwnTerritory()) {
				return true;
			}
		}
		return false;
	}

	public static class Creative {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "creative" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
	}

	public static class Factions {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "factions" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
		public static String[] validKits = new String[]{"recruit"};
		public static ItemStack[] kitRecruit = new ItemStack[]{Go.createMaterial(Material.STONE_SWORD), Go.createMaterial(Material.STONE_PICKAXE), Go.createMaterial(Material.STONE_AXE), Go.createMaterial(Material.STONE_SPADE), Go.createMaterial(Material.COOKED_BEEF, 16), Go.createMaterial(Material.LEATHER_HELMET), Go.createMaterial(Material.LEATHER_CHESTPLATE), Go.createMaterial(Material.LEATHER_LEGGINGS), Go.createMaterial(Material.LEATHER_BOOTS)};
	}

	public static class Kitpvp {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "kitpvp" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
		public static String[] validKits = new String[]{"starter", "potions"};
		public static ItemStack kitStarter1_Weapon = Go.createMaterial(Material.STONE_AXE);
		public static ItemStack kitStarter2_Weapon = Go.createMaterial(Material.IRON_AXE);
		public static ItemStack kitStarter3_Weapon = Go.createMaterial(Material.IRON_AXE);
		public static ItemStack kitStarter4_Weapon = Go.createMaterial(Material.IRON_AXE);
		public static ItemStack[] kitStarter1_Items = new ItemStack[]{Go.createMaterial(Material.BOW), Go.createMaterial(Material.ARROW, 24), Go.createMaterial(Material.LEATHER_HELMET), Go.createMaterial(Material.LEATHER_CHESTPLATE), Go.createMaterial(Material.LEATHER_LEGGINGS), Go.createMaterial(Material.LEATHER_BOOTS)};
		public static ItemStack[] kitStarter2_Items = new ItemStack[]{Go.createMaterial(Material.BOW), Go.createMaterial(Material.ARROW, 24), Go.createMaterial(Material.IRON_HELMET), Go.createMaterial(Material.LEATHER_CHESTPLATE), Go.createMaterial(Material.LEATHER_LEGGINGS), Go.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitStarter3_Items = new ItemStack[]{Go.createMaterial(Material.BOW), Go.createMaterial(Material.ARROW, 24), Go.createMaterial(Material.IRON_HELMET), Go.createMaterial(Material.CHAINMAIL_CHESTPLATE), Go.createMaterial(Material.CHAINMAIL_LEGGINGS), Go.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitStarter4_Items = new ItemStack[]{Go.createMaterial(Material.BOW), Go.createMaterial(Material.ARROW, 24), Go.createMaterial(Material.IRON_HELMET), Go.createMaterial(Material.IRON_CHESTPLATE), Go.createMaterial(Material.IRON_LEGGINGS), Go.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitPotions = new ItemStack[]{Go.addLore(Go.createMaterial(Material.BLAZE_ROD, 1,  ChatColor.LIGHT_PURPLE + "Enchanted Wand"), new String[]{"Using this wand, you will gain defined potion effects."})};
		public static final int MAX_UPGRADE_VALUE = 3;

		public static int getPreferredWeapon(Player player, Plugin plugin) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (plugin.getConfig().contains(path) && plugin.getConfig().contains(String.valueOf(path) + ".preferredWeapon")) {
				return Integer.parseInt(plugin.getConfig().getString(String.valueOf(path) + ".preferredWeapon"));
			}
			plugin.getConfig().set(String.valueOf(path) + ".preferredWeapon",  Material.STONE_AXE.getId());
			plugin.saveConfig();
			return Material.STONE_AXE.getId();
		}

		public static void setPreferredWeapon(Player player, int materialId, Plugin plugin) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			plugin.getConfig().set(String.valueOf(path) + ".preferredWeapon",  materialId);
			plugin.saveConfig();
		}

		public static int getPlayerKills(Player player, Plugin plugin) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (plugin.getConfig().contains(path) && plugin.getConfig().contains(String.valueOf(path) + ".kills")) {
				return Integer.parseInt(plugin.getConfig().getString(String.valueOf(path) + ".kills"));
			}
			plugin.getConfig().set(String.valueOf(path) + ".kills",  "0");
			plugin.saveConfig();
			return 0;
		}

		public static void setPlayerKills(Player player, int value, Plugin plugin) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			plugin.getConfig().set(String.valueOf(path) + ".kills",  String.valueOf(value));
			plugin.saveConfig();
		}

		public static int getPlayerDeaths(Player player, Plugin plugin) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (plugin.getConfig().contains(path) && plugin.getConfig().contains(String.valueOf(path) + ".deaths")) {
				return Integer.parseInt(plugin.getConfig().getString(String.valueOf(path) + ".deaths"));
			}
			plugin.getConfig().set(String.valueOf(path) + ".deaths",  "0");
			plugin.saveConfig();
			return 0;
		}

		public static void setPlayerDeaths(Player player, int value, Plugin plugin) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			plugin.getConfig().set(String.valueOf(path) + ".deaths",  String.valueOf(value));
			plugin.saveConfig();
		}

		public static void refreshScoreboard(Player player, Plugin plugin) {
			int currentPlayerKills = Kitpvp.getPlayerKills(player, plugin);
			int currentPlayerDeaths = Kitpvp.getPlayerDeaths(player, plugin);
			int currentPlayerDPK = currentPlayerKills / 50;
			int currentPlayerCash = EconManager.retrieveCash(player, "kitpvp", plugin);
			Hashtable<String, Integer> list = new Hashtable<String, Integer>();
			list.put( ChatColor.GRAY + "Kills", currentPlayerKills);
			list.put( ChatColor.GRAY + "Deaths", currentPlayerDeaths);
			list.put( ChatColor.GRAY + "Level", currentPlayerDPK + 1);
			list.put( ChatColor.GRAY + "Balance", currentPlayerCash);
			Scoreboard.configureSidebar(player, "Statistics", list, false);
		}

		public static int getUpgradeCount(Player player, Plugin plugin) {
			int upgrades;
			String path = "config." + player.getUniqueId().toString();
			if (!plugin.getConfig().contains(String.valueOf(path) + ".kitpvp.upgrades")) {
				Kitpvp.setUpgradeCount(player, 0, plugin);
			}
			if ((upgrades = Integer.parseInt(plugin.getConfig().getString(String.valueOf(path) + ".kitpvp.upgrades"))) > 3) {
				upgrades = 3;
			}
			return upgrades;
		}

		public static void setUpgradeCount(Player player, int upgrades, Plugin plugin) {
			if (upgrades > 3) {
				upgrades = 3;
			}
			String path = "config." + player.getUniqueId().toString();
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.upgrades",  String.valueOf(upgrades));
			plugin.saveConfig();
		}

		public static int getPreferredUpgrade(Player player, Plugin plugin) {
			int upgradeCount;
			int preferredUpgrade;
			String path = "config." + player.getUniqueId().toString();
			if (!plugin.getConfig().contains(String.valueOf(path) + ".kitpvp.preferredUpgrade")) {
				Kitpvp.setPreferredUpgrade(player, Kitpvp.getUpgradeCount(player, plugin), plugin);
			}
			if ((preferredUpgrade = Integer.parseInt(plugin.getConfig().getString(String.valueOf(path) + ".kitpvp.preferredUpgrade"))) > (upgradeCount = Kitpvp.getUpgradeCount(player, plugin))) {
				Kitpvp.setPreferredUpgrade(player, upgradeCount, plugin);
				preferredUpgrade = upgradeCount;
			}
			if (preferredUpgrade > 3) {
				preferredUpgrade = 3;
			}
			return preferredUpgrade;
		}

		public static void setPreferredUpgrade(Player player, int upgrade, Plugin plugin) {
			if (upgrade > 3) {
				upgrade = 3;
			}
			String path = "config." + player.getUniqueId().toString();
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.preferredUpgrade",  String.valueOf(upgrade));
			plugin.saveConfig();
		}

		public static ItemStack getUpgradeClassWeapon(Player player, int upgrade, boolean axesOnly, Plugin plugin) {
			boolean swordType = false;
			if (Kitpvp.getPreferredWeapon(player, plugin) == Material.STONE_SWORD.getId()) {
				swordType = true;
			}
			if (upgrade > 3) {
				upgrade = 3;
			}
			ItemStack weapon = null;
			if (upgrade <= 0) {
				weapon = kitStarter1_Weapon.clone();
			} else if (upgrade == 1) {
				weapon = kitStarter2_Weapon.clone();
			} else if (upgrade == 2) {
				weapon = kitStarter3_Weapon.clone();
			} else if (upgrade >= 3) {
				weapon = kitStarter4_Weapon.clone();
			}
			if (!axesOnly && swordType && weapon.getType() == Material.STONE_AXE) {
				weapon = Go.createMaterial(Material.STONE_SWORD);
			}
			if (!axesOnly && swordType && weapon.getType() == Material.IRON_AXE) {
				weapon = Go.createMaterial(Material.IRON_SWORD);
			}
			return weapon;
		}

		public static ItemStack[] getUpgradeClass(Player player, int upgrade, boolean axesOnly, Plugin plugin) {
			boolean swordType = false;
			if (Kitpvp.getPreferredWeapon(player, plugin) == Material.STONE_SWORD.getId()) {
				swordType = true;
			}
			if (upgrade > 3) {
				upgrade = 3;
			}
			ArrayList<ItemStack> kitArray = new ArrayList<ItemStack>();
			ItemStack[] array = null;
			if (upgrade <= 0) {
				kitArray.add(kitStarter1_Weapon);
				array = kitStarter1_Items;
			} else if (upgrade == 1) {
				kitArray.add(kitStarter2_Weapon);
				array = kitStarter2_Items;
			} else if (upgrade == 2) {
				kitArray.add(kitStarter3_Weapon);
				array = kitStarter3_Items;
			} else if (upgrade >= 3) {
				kitArray.add(kitStarter4_Weapon);
				array = kitStarter4_Items;
			}
			int i = 0;
			while (i < array.length) {
				kitArray.add(i + 1, array[i]);
				++i;
			}
			if (!axesOnly && swordType && ((ItemStack) kitArray.get(0)).getType() == Material.STONE_AXE) {
				kitArray.set(0, Go.createMaterial(Material.STONE_SWORD));
			}
			if (!axesOnly && swordType && ((ItemStack) kitArray.get(0)).getType() == Material.IRON_AXE) {
				kitArray.set(0, Go.createMaterial(Material.IRON_SWORD));
			}
			return kitArray.toArray(new ItemStack[0]);
		}
	}

	public static class Legacy {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "Minecraft" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
	}

	public static class Lobby {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "hub" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
	}

	public static class Scoreboard
			extends Go {

		public static void configureSidebar(Player player, String title, Hashtable<String, Integer> table, boolean override) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			org.bukkit.scoreboard.Scoreboard board;
			if (player.getScoreboard().getObjective("placeholder") == null) {
				board = player.getScoreboard();
			} else {
				board = manager.getNewScoreboard();
			}
			boolean found = false;
			for (Objective objective : board.getObjectives()) {
				if (objective.getName().equals("sidebar-obj")) {
					found = true;
					break;
				}
			}
			Objective objective;
			if (found) {
				objective = board.getObjective("sidebar-obj");
				if (override) {
					objective.unregister();
					objective = board.registerNewObjective("sidebar-obj", "dummy");
					objective.setDisplayName(title);
				}
			} else {
				objective = board.registerNewObjective("sidebar-obj", "dummy");
				objective.setDisplayName(title);
			}
			if (!(objective.getDisplayName().equals(title))) {
				objective.unregister();
				objective = board.registerNewObjective("sidebar-obj", "dummy");
				objective.setDisplayName(title);
			}
			int changed = 0;
			for (Map.Entry<String, Integer> score : table.entrySet()) {
				Score objEntry = objective.getScore(score.getKey());
				objEntry.setScore(score.getValue());
				changed++;
			}
			if (changed > 0) {
				objective.setDisplaySlot(DisplaySlot.SIDEBAR);
				player.setScoreboard(board);
			}
		}

		public static void configureHealth(Player player) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			org.bukkit.scoreboard.Scoreboard board;
			if (player.getScoreboard().getObjective("placeholder") == null) {
				board = player.getScoreboard();
			} else {
				board = manager.getNewScoreboard();
			}
			boolean found = false;
			for (Objective objective : board.getObjectives()) {
				if (objective.getName().equals("health")) {
					found = true;
					break;
				}
			}
			Objective objective;
			if (found) {
				objective = board.getObjective("health");
			} else {
				objective = board.registerNewObjective("health", "dummy");
				objective.setDisplayName("/ 20 Health");
			}
			objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
			for (Player op : Bukkit.getOnlinePlayers()) {
				Score s = objective.getScore(op.getName());
				s.setScore((int) op.getHealth());
			}
			player.setScoreboard(board);
		}

		public static void clearDisplaySlot(Player player, DisplaySlot slot) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			org.bukkit.scoreboard.Scoreboard board;
			if (player.getScoreboard().getObjective("placeholder") == null) {
				board = player.getScoreboard();
			} else {
				board = manager.getNewScoreboard();
			}
			board.clearSlot(slot);
			if (slot == DisplaySlot.SIDEBAR && !(board.getObjective("sidebar-obj") == null)) {
				board.getObjective("sidebar-obj").unregister();
			}
			player.setScoreboard(board);
		}
	}

	public static class Skyfight {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "skyfight" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
	}

	public static class Survival {
		public static String tag =  ChatColor.GRAY + "[" +  ChatColor.RESET + "survival" +  ChatColor.GRAY + "] " +  ChatColor.RESET;
		public static String[] validKits = new String[]{"recruit"};
		public static ItemStack[] kitRecruit = new ItemStack[]{Go.createMaterial(Material.STONE_SWORD), Go.createMaterial(Material.STONE_PICKAXE), Go.createMaterial(Material.STONE_AXE), Go.createMaterial(Material.STONE_SPADE), Go.createMaterial(Material.COOKED_BEEF, 16), Go.createMaterial(Material.LEATHER_HELMET), Go.createMaterial(Material.LEATHER_CHESTPLATE), Go.createMaterial(Material.LEATHER_LEGGINGS), Go.createMaterial(Material.LEATHER_BOOTS)};
	}

}

