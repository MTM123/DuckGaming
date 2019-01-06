package me.skorrloregaming;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

import me.skorrloregaming.impl.EnchantInfo;
import me.skorrloregaming.impl.MarriageGender;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.Switches.SwitchStringMinigame;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import me.skorrloregaming.scoreboard.boards.Factions_StatisticsScoreboard;
import me.skorrloregaming.scoreboard.boards.Kitpvp_LeaderboardScoreboard;
import me.skorrloregaming.scoreboard.boards.Kitpvp_StatisticsScoreboard;
import me.skorrloregaming.scoreboard.boards.Skyblock_StatisticsScoreboard;
import me.skorrloregaming.scoreboard.boards.Skyfight_LeaderboardScoreboard;
import net.md_5.bungee.api.ChatColor;

public class $ {
	public static String italicGray = ChatColor.GRAY + "" + ChatColor.ITALIC;
	public static String modernMsgPrefix = ChatColor.BOLD + "\u00BB" + " ";
	public static String consoleTag = ChatColor.RED + "[" + ChatColor.GRAY + "Console" + ChatColor.RED + "] " + ChatColor.RED;
	public static String pricePrefix = ChatColor.RESET + "Purchase Price: " + ChatColor.RED + "$";
	public static List<String> validRanks = Arrays.asList(new String[]{"default", "default-plus", "founder", "owner", "manager", "admin", "moderator", "helper", "developer", "builder", "senior", "youtube", "donator", "redstone", "obsidian", "bedrock"});
	public static List<String> validRanksNotifyWorkerExecuteCommand = Arrays.asList(new String[]{"founder"});
	public static List<String> validMinigames = Arrays.asList(new String[]{"kitpvp", "factions", "survival", "skyfight", "creative", "skyblock"});
	public static List<String> validStorageMinigames = Arrays.asList(new String[]{"kitpvp", "factions", "survival", "creative", "skyblock"});
	public static List<String> validLocketteMinigames = Arrays.asList(new String[]{"skyblock", "factions"});
	public static List<String> validStairSeatMinigames = Arrays.asList(new String[]{"creative", "skyblock", "factions"});
	public static List<String> scoreboardAutoUpdateMinigames = Arrays.asList(new String[]{"skyblock", "factions", "kitpvp"});
	public static List<String> betaMinigames = Arrays.asList(new String[]{});
	public static Kitpvp_LeaderboardScoreboard kitpvpLeaderboardScoreboard = new Kitpvp_LeaderboardScoreboard();
	public static Kitpvp_StatisticsScoreboard kitpvpStatisticsScoreboard = new Kitpvp_StatisticsScoreboard();
	public static Skyblock_StatisticsScoreboard skyblockScoreboard = new Skyblock_StatisticsScoreboard();
	public static Skyfight_LeaderboardScoreboard skyfightScoreboard = new Skyfight_LeaderboardScoreboard();
	public static Factions_StatisticsScoreboard factionsScoreboard = new Factions_StatisticsScoreboard();
	public static List<SwitchStringMinigame> playersNotAllowedToJoinSpecificMinigames = Arrays.asList(new SwitchStringMinigame[]{new SwitchStringMinigame("LuckyPlayz01_", ServerMinigame.KITPVP), new SwitchStringMinigame("LuckyPlayz01_", ServerMinigame.SKYFIGHT)});

	public static DisposableScoreboard getPrimaryScoreboard(ServerMinigame minigame) {
		switch (minigame) {
			case KITPVP:
				return kitpvpStatisticsScoreboard;
			case SKYBLOCK:
				return skyblockScoreboard;
			case SKYFIGHT:
				return skyfightScoreboard;
			case FACTIONS:
				return factionsScoreboard;
			default:
				return null;
		}
	}

	public static boolean isPluginLoaded(String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null)
			return false;
		return true;
	}

	public static boolean isPluginEnabled(String pluginName) {
		Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
		if (plugin == null)
			return false;
		if (plugin.isEnabled())
			return true;
		return false;
	}

	public static void playLackPermissionMessage(CommandSender player) {
		player.sendMessage($.Legacy.tag + ChatColor.RED + "You do not have permission to use this command.");
	}

	public static void playForbiddenTeleportMessage(CommandSender player, ServerMinigame minigame) {
		player.sendMessage("Sorry, teleportation was cancelled, please contact an admin.");
		player.sendMessage("Most likely the teleport destination is forbidden in " + WordUtils.capitalize(minigame.toString().toLowerCase()) + ".");
	}

	public static boolean isPlayerNotAllowedToJoin(Player player, ServerMinigame minigame) {
		for (SwitchStringMinigame pair : $.playersNotAllowedToJoinSpecificMinigames.toArray(new SwitchStringMinigame[0])) {
			String username = pair.getArg0();
			ServerMinigame notAllowedToJoinMinigame = pair.getArg1();
			if (minigame.toString().equals(notAllowedToJoinMinigame.toString())) {
				if (username.toString().equals(player.getName().toString()))
					return true;
			}
		}
		return false;
	}

	public static String toSuffixRankTag(Player player, String str) {
		switch (str.toString()) {
			case "default-plus":
				if (getRank(player.getUniqueId()).equals("builder")) {
					return ChatColor.GRAY + "[" + ChatColor.WHITE + "Member+" + ChatColor.GRAY + "]" + ChatColor.RESET;
				} else {
					return toRankTag(str).substring(0, toRankTag(str).lastIndexOf(" ")) + ChatColor.RESET;
				}
			default:
				return toRankTag(str).substring(0, toRankTag(str).lastIndexOf(" ")) + ChatColor.RESET;
		}
	}

	public static String toRankTag(String str) {
		if (str.equals("default"))
			return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Member" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
		if (str.equals("default-plus"))
			return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Member+" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
		if (str.equals("helper"))
			return ChatColor.RED + "[" + ChatColor.GRAY + "Helper" + ChatColor.RED + "] ";
		if (str.equals("developer"))
			return ChatColor.DARK_PURPLE + "[" + ChatColor.LIGHT_PURPLE + "Developer" + ChatColor.DARK_PURPLE + "] " + ChatColor.LIGHT_PURPLE;
		if (str.equals("builder"))
			return ChatColor.GRAY + "[" + ChatColor.WHITE + "Builder" + ChatColor.GRAY + "] " + ChatColor.WHITE;
		if (str.equals("moderator"))
			return ChatColor.RED + "[" + ChatColor.GRAY + "Moderator" + ChatColor.RED + "] ";
		if (str.equals("admin"))
			return ChatColor.RED + "[" + ChatColor.GRAY + "Admin" + ChatColor.RED + "] ";
		if (str.equals("manager"))
			return ChatColor.RED + "[" + ChatColor.GRAY + "Manager" + ChatColor.RED + "] ";
		if (str.equals("owner"))
			return ChatColor.RED + "[" + ChatColor.GRAY + "Owner" + ChatColor.RED + "] ";
		if (str.equals("founder"))
			return ChatColor.RED + "[" + ChatColor.GRAY + "Founder" + ChatColor.RED + "] ";
		if (str.equals("donator"))
			return ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Donator" + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY;
		if (str.equals("senior"))
			return ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Senior" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
		if (str.equals("youtube"))
			return ChatColor.GRAY + "[" + ChatColor.RED + "You" + ChatColor.WHITE + "Tube" + ChatColor.GRAY + "] " + ChatColor.RESET;
		if (str.equals("redstone"))
			return ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Redstone" + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY;
		if (str.equals("obsidian"))
			return ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Obsidian" + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY;
		if (str.equals("bedrock"))
			return ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "Bedrock" + ChatColor.DARK_GREEN + "] " + ChatColor.GRAY;
		return "";
	}

	public static String toRankDisplayName(String str) {
		if (str.equals("default"))
			return "member";
		if (str.equals("default-plus"))
			return "member+";
		if (str.equals("helper"))
			return "helper";
		if (str.equals("developer"))
			return "developer";
		if (str.equals("builder"))
			return "builder";
		if (str.equals("moderator"))
			return "moderator";
		if (str.equals("admin"))
			return "admin";
		if (str.equals("manager"))
			return "manager";
		if (str.equals("owner"))
			return "owner";
		if (str.equals("founder"))
			return "founder";
		if (str.equals("donator"))
			return "donator";
		if (str.equals("senior"))
			return "senior";
		if (str.equals("youtube"))
			return "youtube";
		if (str.equals("redstone"))
			return "redstone";
		if (str.equals("obsidian"))
			return "obsidian";
		if (str.equals("bedrock"))
			return "bedrock";
		return "";
	}

	public static String getRank(UUID id) {
		if (isRankingEnabled() && Server.getPlugin().getConfig().contains("config." + id.toString() + ".rank")) {
			return Server.getPlugin().getConfig().getString("config." + id.toString() + ".rank");
		} else {
			return validRanks.get(0);
		}
	}

	public static String getRank(Player player) {
		return getRank(player.getUniqueId());
	}

	public static String getSuffixRank(UUID id) {
		if (isRankingEnabled() && Server.getPlugin().getConfig().contains("config." + id.toString() + ".suffixRank")) {
			return Server.getPlugin().getConfig().getString("config." + id.toString() + ".suffixRank");
		} else {
			return validRanks.get(0);
		}
	}

	public static String getSuffixRank(Player player) {
		return getSuffixRank(player.getUniqueId());
	}

	public static int getRankId(UUID id) {
		String rank = getRank(id);
		if (rank.equals("default") || rank.equals("default-plus") || rank.equals("senior") || rank.equals("youtube") || !isRankingEnabled())
			return -1;
		if (rank.equals("bedrock"))
			return -5;
		if (rank.equals("obsidian"))
			return -4;
		if (rank.equals("redstone"))
			return -3;
		if (rank.equals("donator"))
			return -2;
		if (rank.equals("helper") || rank.equals("developer") || rank.equals("builder"))
			return 0;
		if (rank.equals("moderator"))
			return 1;
		if (rank.equals("admin") || rank.equals("manager"))
			return 2;
		if (rank.equals("founder") || rank.equals("owner"))
			return 3;
		return -100;
	}

	public static int getRankId(Player player) {
		return getRankId(player.getUniqueId());
	}

	public static String formatMonthId(int month) {
		switch (month % 12) {
			case 0:
				return "January";
			case 1:
				return "February";
			case 2:
				return "March";
			case 3:
				return "April";
			case 4:
				return "May";
			case 5:
				return "June";
			case 6:
				return "July";
			case 7:
				return "August";
			case 8:
				return "September";
			case 9:
				return "October";
			case 10:
				return "November";
			case 11:
				return "December";
			default:
				return null;
		}
	}

	public static String formatMonthIdAbbrev(int month) {
		switch (month % 12) {
			case 0:
				return "Jan.";
			case 1:
				return "Feb.";
			case 2:
				return "Mar.";
			case 3:
				return "Apr.";
			case 4:
				return "May";
			case 5:
				return "June";
			case 6:
				return "July";
			case 7:
				return "Aug.";
			case 8:
				return "Sept.";
			case 9:
				return "Oct.";
			case 10:
				return "Nov.";
			case 11:
				return "Dec.";
			default:
				return null;
		}
	}

	public static String formatDayOfMonthSuffix(int dayOfMonth) {
		if (dayOfMonth > 10 && dayOfMonth < 21)
			return "th";
		if (dayOfMonth % 10 == 1)
			return "st";
		if (dayOfMonth % 10 == 2)
			return "nd";
		if (dayOfMonth % 10 == 3)
			return "rd";
		return "th";
	}

	public static int retrieveMonthId(String month) {
		switch (month) {
			case "January":
				return 0;
			case "February":
				return 1;
			case "March":
				return 2;
			case "April":
				return 3;
			case "May":
				return 4;
			case "June":
				return 5;
			case "July":
				return 6;
			case "August":
				return 7;
			case "September":
				return 8;
			case "October":
				return 9;
			case "November":
				return 10;
			case "December":
				return 11;
			default:
				return -1;
		}
	}

	public static boolean isOld(long timestamp, long timespan) {
		if (timestamp + timespan <= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}

	public static String basicFormatCalendarTime(Calendar cal) {
		return formatMonthId(cal.get(Calendar.MONTH)) + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR);
	}

	public static int getSuffixRankId(UUID id) {
		String rank = getSuffixRank(id);
		if (rank.equals("default") || rank.equals("default-plus") || rank.equals("senior") || !isRankingEnabled())
			return -1;
		if (rank.equals("bedrock"))
			return -5;
		if (rank.equals("obsidian"))
			return -4;
		if (rank.equals("redstone"))
			return -3;
		if (rank.equals("donator"))
			return -2;
		if (rank.equals("helper") || rank.equals("developer") || rank.equals("builder"))
			return 0;
		if (rank.equals("moderator"))
			return 1;
		if (rank.equals("admin") || rank.equals("manager"))
			return 2;
		if (rank.equals("founder") || rank.equals("owner"))
			return 3;
		return -100;
	}

	public static int getSuffixRankId(Player player) {
		return getSuffixRankId(player.getUniqueId());
	}

	public static void flashPlayerDisplayName(Player player) {
		String suffixRank = getSuffixRank(player);
		if (!(suffixRank.equals("default"))) {
			player.setDisplayName(toRankTag(getRank(player)) + player.getName() + " " + toSuffixRankTag(player, suffixRank) + ChatColor.RESET);
		} else {
			player.setDisplayName(toRankTag(getRank(player)) + player.getName() + ChatColor.RESET);
		}
	}

	public static String getFlashPlayerDisplayName(OfflinePlayer player) {
		String suffixRank = getSuffixRank(player.getUniqueId());
		if (!(suffixRank.equals("default"))) {
			return toRankTag(getRank(player.getUniqueId())) + player.getName() + " " + toRankTag(suffixRank) + ChatColor.RESET;
		} else {
			return toRankTag(getRank(player.getUniqueId())) + player.getName() + ChatColor.RESET;
		}
	}

	public static String getFlashPlayerDisplayName(String playerName) {
		UUID id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes());
		if (Bukkit.getOnlineMode())
			id = UUID.fromString(CraftGo.Player.getUUID(playerName, true));
		String suffixRank = getSuffixRank(id);
		if (!(suffixRank.equals("default"))) {
			return toRankTag(getRank(id)) + playerName + " " + toRankTag(suffixRank) + ChatColor.RESET;
		} else {
			return toRankTag(getRank(id)) + playerName + ChatColor.RESET;
		}
	}

	public static String formatTime(long seconds) {
		if (seconds < 60) {
			return seconds + "s";
		}
		long minutes = seconds / 60;
		long s = 60 * minutes;
		long secondsLeft = seconds - s;
		if (minutes < 60) {
			if (secondsLeft > 0) {
				return String.valueOf(minutes + "m " + secondsLeft + "s");
			}
			return String.valueOf(minutes + "m");
		}
		if (minutes < 1440) {
			String time = "";
			long hours = minutes / 60;
			time = hours + "h";
			long inMins = 60 * hours;
			long leftOver = minutes - inMins;
			if (leftOver >= 1) {
				time = time + " " + leftOver + "m";
			}
			if (secondsLeft > 0) {
				time = time + " " + secondsLeft + "s";
			}
			return time;
		}
		String time = "";
		long days = minutes / 1440;
		time = days + "d";
		long inMins = 1440 * days;
		long leftOver = minutes - inMins;
		if (leftOver >= 1) {
			if (leftOver < 60) {
				time = time + " " + leftOver + "m";
			} else {
				long hours = leftOver / 60;
				time = time + " " + hours + "h";
				long hoursInMins = 60 * hours;
				long minsLeft = leftOver - hoursInMins;
				if (leftOver >= 1) {
					time = time + " " + minsLeft + "m";
				}
			}
		}
		if (secondsLeft > 0) {
			time = time + " " + secondsLeft + "s";
		}
		return time;
	}

	public static void setEnableEffects(Player player, boolean value) {
		String path = "config." + player.getUniqueId().toString();
		Server.getPlugin().getConfig().set(path + ".effects", value);
	}

	public static boolean isEffectsEnabled(Player player) {
		String path = "config." + player.getUniqueId().toString();
		if (Server.getPlugin().getConfig().contains(path + ".effects")) {
			return Server.getPlugin().getConfig().getBoolean(path + ".effects");
		} else {
			return true;
		}
	}

	public static String basicFormatMotdLine(String line) {
		int perLineLength = 45;
		int lineOneSpaces = perLineLength - ChatColor.stripColor(line).length();
		StringBuilder sbLineOneSpaces = new StringBuilder(line);
		for (int i = 0; i < lineOneSpaces + 1; i++) {
			sbLineOneSpaces.append(" ");
		}
		return sbLineOneSpaces.toString();
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

	public static void playFirework(Location location) {
		FireworkEffect effect = FireworkEffect.builder().with(Type.BURST).withColor(Color.WHITE).build();
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.clearEffects();
		fwm.setPower(1);
		fwm.addEffect(effect);
		fw.setFireworkMeta(fwm);
	}

	public static String formatPackageVersion(String version) {
		return version.replace("_", ".").substring(0, version.lastIndexOf("R") - 1).substring(1);
	}

	public static String scanStringArrayAndSplitBy(String[] array, char[] split) {
		for (String string : array) {
			if (!(string.indexOf(new String(split)) == -1)) {
				return string.split(new String(split))[1];
			}
		}
		return null;
	}

	public static int getAvailableSlot(ItemStack[] itemArray) {
		for (int i = 0; i < itemArray.length; i++) {
			ItemStack item = itemArray[i];
			if (item == null || item.getType() == Material.AIR)
				return i;
		}
		return -1;
	}

	public static Object getAuthenticationSuite() {
		if (!$.isPluginEnabled("AuthMe")) {
			return null;
		} else {
			return fr.xephi.authme.api.v3.AuthMeApi.getInstance();
		}
	}

	public static boolean isMinigameEnabled(ServerMinigame minigame) {
		if (minigame == ServerMinigame.HUB)
			return true;
		if (minigame == ServerMinigame.UNKNOWN)
			return false;
		if (minigame == ServerMinigame.FACTIONS) {
			if (!$.isPluginEnabled("Factions"))
				return false;
		}
		if (Server.getPlugin().getConfig().contains("settings.enable." + minigame.toString().toLowerCase())) {
			return Server.getPlugin().getConfig().getBoolean("settings.enable." + minigame.toString().toLowerCase());
		} else {
			return true;
		}
	}

	public static boolean isRankingEnabled() {
		if (Server.getPlugin().getConfig().contains("settings.ranking.enable")) {
			return Server.getPlugin().getConfig().getBoolean("settings.ranking.enable");
		} else {
			return true;
		}
	}

	public static boolean preloadChunk(Location loc) {
		if (Server.getPlugin().getConfig().contains("settings.enable.preloadChunks")) {
			if (Server.getPlugin().getConfig().getBoolean("settings.enable.preloadChunks")) {
				loc.getChunk().load();
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	public static boolean isPrefixedRankingEnabled() {
		if (!isRankingEnabled())
			return false;
		if (Server.getPlugin().getConfig().contains("settings.ranking.prefix")) {
			return Server.getPlugin().getConfig().getBoolean("settings.ranking.prefix");
		} else {
			return true;
		}
	}

	public static boolean isWelcomeMessageEnabled() {
		if (Server.getPlugin().getConfig().contains("settings.enable.welcomeMessage")) {
			return Server.getPlugin().getConfig().getBoolean("settings.enable.welcomeMessage");
		} else {
			return true;
		}
	}

	public static boolean isCustomJoinMessageEnabled() {
		if (Server.getPlugin().getConfig().contains("settings.customJoinMessage")) {
			return Server.getPlugin().getConfig().getBoolean("settings.customQuitMessage");
		} else {
			return true;
		}
	}

	public static boolean isCustomQuitMessageEnabled() {
		if (Server.getPlugin().getConfig().contains("settings.customJoinMessage")) {
			return Server.getPlugin().getConfig().getBoolean("settings.customQuitMessage");
		} else {
			return true;
		}
	}

	public static String replaceCommandLabelInCommand(String command, String replaceLabel) {
		String[] args = command.split(" ");
		if (args[0].charAt(0) == '/' && !(replaceLabel.charAt(0) == '/')) {
			args[0] = '/' + replaceLabel;
		} else {
			args[0] = replaceLabel;
		}
		return String.join(" ", args);
	}

	public static int[] sortNumericallyReverse(int[] integerArray) {
		int[] sortedArray = sortNumerically(integerArray);
		int[] reverseSortedNum = new int[sortedArray.length];
		for (int i = 0; i < sortedArray.length; i++) {
			reverseSortedNum[i] = sortedArray[sortedArray.length - 1 - i];
		}
		return reverseSortedNum;
	}

	public static int[] sortNumerically(int[] integerArray) {
		int[] sorted = integerArray.clone();
		for (int i = 0; i < sorted.length; i++) {
			for (int j = i + 1; j < sorted.length; j++) {
				if ((sorted[i] > sorted[j]) && (i != j)) {
					int temp = sorted[j];
					sorted[j] = sorted[i];
					sorted[i] = temp;
				}
			}
		}
		return sorted;
	}

	public static double[] sortNumericallyReverse(double[] doubleArray) {
		double[] sortedArray = sortNumerically(doubleArray);
		double[] reverseSortedNum = new double[sortedArray.length];
		for (int i = 0; i < sortedArray.length; i++) {
			reverseSortedNum[i] = sortedArray[sortedArray.length - 1 - i];
		}
		return reverseSortedNum;
	}

	public static double[] sortNumerically(double[] doubleArray) {
		double[] sorted = doubleArray.clone();
		for (int i = 0; i < sorted.length; i++) {
			for (int j = i + 1; j < sorted.length; j++) {
				if ((sorted[i] > sorted[j]) && (i != j)) {
					double temp = sorted[j];
					sorted[j] = sorted[i];
					sorted[i] = temp;
				}
			}
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
		return item == null || item.getType() == Material.AIR;
	}

	public static ItemStack createMaterial(Material material) {
		return createMaterial(material, 1, "unspecified", (short) 0, new String[0]);
	}

	public static ItemStack createMaterial(Material material, int amount) {
		return createMaterial(material, amount, "unspecified", (short) 0, new String[0]);
	}

	public static ItemStack createMaterial(Material material, int amount, short durability) {
		return createMaterial(material, amount, "unspecified", durability, new String[0]);
	}

	public static ItemStack createMaterial(Material material, int amount, String displayName) {
		return createMaterial(material, amount, displayName, (short) 0, new String[0]);
	}

	public static ItemStack createMaterial(Material material, String displayName) {
		return createMaterial(material, 1, displayName, (short) 0, new String[0]);
	}

	public static ItemStack createMaterial(Material material, int amount, String displayName, short durability, List<String> lore) {
		return createMaterial(material, amount, displayName, durability, lore.toArray(new String[0]));
	}

	public static ItemStack createMaterial(Material material, int amount, String displayName, short durability, String[] lore) {
		try {
			ItemStack itemStack = new ItemStack(material, amount, durability);
			if (displayName.equals("unspecified"))
				return itemStack;
			ItemMeta meta = itemStack.getItemMeta();
			meta.setDisplayName(displayName);
			if (lore.length > 0)
				meta.setLore(Arrays.asList(lore));
			itemStack.setItemMeta(meta);
			return itemStack;
		} catch (Exception ex) {
			ex.printStackTrace();
			return createMaterial(Material.AIR);
		}
	}

	public static boolean isStringNumeric(String string) {
		for (char c : string.toCharArray()) {
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	public static boolean isValidAddress(String string) {
		String[] parts = string.split("\\.", -1);
		return parts.length == 4 && Arrays.stream(parts).filter($::isStringNumeric).map(Integer::parseInt).filter(i -> i <= 255 && i >= 0).count() == 4;
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

	public static ItemStack appendLore(ItemStack stack, String[] lore) {
		ItemStack item = stack.clone();
		ItemMeta itemMeta = item.getItemMeta();
		List<String> currLore = new ArrayList<String>();
		if (itemMeta.hasLore())
			currLore = itemMeta.getLore();
		for (String append : lore)
			currLore.add(append);
		itemMeta.setLore(currLore);
		item.setItemMeta(itemMeta);
		return item;
	}

	public static ItemStack appendLore(ItemStack stack, String lore) {
		return $.appendLore(stack, new String[]{lore});
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
		if (color == null)
			return stack;
		ItemStack itm = stack;
		LeatherArmorMeta im = (LeatherArmorMeta) itm.getItemMeta();
		im.setColor(color);
		itm.setItemMeta(im);
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
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack setUnbreakable(ItemStack stack, boolean value) {
		ItemMeta meta = stack.getItemMeta();
		meta.setUnbreakable(value);
		stack.setItemMeta(meta);
		return stack;
	}

	public static boolean isShulkerBox(ItemStack stack) {
		if (stack.getType() == Material.BLACK_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.BLUE_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.BROWN_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.CYAN_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.GREEN_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.GRAY_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.LIGHT_BLUE_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.LIME_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.MAGENTA_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.ORANGE_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.PINK_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.PURPLE_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.RED_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.LIGHT_GRAY_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.WHITE_SHULKER_BOX)
			return true;
		if (stack.getType() == Material.YELLOW_SHULKER_BOX)
			return true;
		return false;
	}

	public static BlockFace getBlockFaceFromId(byte id) {
		switch (id) {
			case 2:
				return BlockFace.NORTH;
			case 5:
				return BlockFace.SOUTH;
			case 3:
				return BlockFace.WEST;
			case 4:
				return BlockFace.EAST;
			default:
				return null;
		}
	}

	public static ChatColor getRainbowColor(int rainboxIndex) {
		Random random = new Random(UUID.randomUUID().hashCode());
		int id = random.nextInt(10);
		switch (id) {
			case 0:
				return ChatColor.WHITE;
			case 1:
				return ChatColor.GOLD;
			case 3:
				return ChatColor.AQUA;
			case 4:
				return ChatColor.YELLOW;
			case 5:
				return ChatColor.GREEN;
			case 6:
				return ChatColor.LIGHT_PURPLE;
			case 7:
				return ChatColor.GRAY;
			case 8:
				return ChatColor.BLUE;
			case 9:
				return ChatColor.RED;
			default:
				return ChatColor.RESET;
		}
	}

	public static boolean isAuthenticated(Player player) {
		Object authObject = $.getAuthenticationSuite();
		boolean isAuthenticated = false;
		if (authObject == null) {
			isAuthenticated = true;
		} else {
			isAuthenticated = ((fr.xephi.authme.api.v3.AuthMeApi) authObject).isAuthenticated(player);
		}
		return isAuthenticated;
	}

	public static boolean isAuthenticationCommand(String command) {
		if (command.equalsIgnoreCase("/register"))
			return true;
		if (command.equalsIgnoreCase("/reg"))
			return true;
		if (command.equalsIgnoreCase("/login"))
			return true;
		if (command.equalsIgnoreCase("/log"))
			return true;
		if (command.equalsIgnoreCase("/l"))
			return true;
		if (command.equalsIgnoreCase("/unregister"))
			return true;
		if (command.equalsIgnoreCase("/unreg"))
			return true;
		if (command.equalsIgnoreCase("/changepassword"))
			return true;
		if (command.equalsIgnoreCase("/cp"))
			return true;
		if (command.equalsIgnoreCase("/captcha"))
			return true;
		if (command.equalsIgnoreCase("/autologin"))
			return true;
		if (command.equalsIgnoreCase("/autolog"))
			return true;
		return false;
	}

	public static boolean isBlockLog(Block block) {
		return isMaterialLog(block.getType());
	}

	public static boolean isMaterialLog(Material material) {
		switch (material) {
			case ACACIA_LOG:
				return true;
			case BIRCH_LOG:
				return true;
			case DARK_OAK_LOG:
				return true;
			case JUNGLE_LOG:
				return true;
			case OAK_LOG:
				return true;
			case SPRUCE_LOG:
				return true;
			case STRIPPED_ACACIA_LOG:
				return true;
			case STRIPPED_BIRCH_LOG:
				return true;
			case STRIPPED_DARK_OAK_LOG:
				return true;
			case STRIPPED_JUNGLE_LOG:
				return true;
			case STRIPPED_OAK_LOG:
				return true;
			case STRIPPED_SPRUCE_LOG:
				return true;
			default:
				return false;
		}
	}

	public static boolean isSpawnEgg(Material material) {
		return material.toString().contains("SPAWN_EGG");
	}

	public static boolean isBlockLeaves(Block block) {
		return isMaterialLeaves(block.getType());
	}

	public static boolean isMaterialLeaves(Material material) {
		switch (material) {
			case ACACIA_LEAVES:
				return true;
			case BIRCH_LEAVES:
				return true;
			case DARK_OAK_LEAVES:
				return true;
			case JUNGLE_LEAVES:
				return true;
			case OAK_LEAVES:
				return true;
			case SPRUCE_LEAVES:
				return true;
			default:
				return false;
		}
	}

	public static boolean isBlockStainedGlass(Block block) {
		return isMaterialStainedGlass(block.getType());
	}

	public static boolean isMaterialStainedGlass(Material material) {
		switch (material) {
			case BLACK_STAINED_GLASS:
				return true;
			case BLUE_STAINED_GLASS:
				return true;
			case BROWN_STAINED_GLASS:
				return true;
			case CYAN_STAINED_GLASS:
				return true;
			case GRAY_STAINED_GLASS:
				return true;
			case GREEN_STAINED_GLASS:
				return true;
			case LIGHT_BLUE_STAINED_GLASS:
				return true;
			case LIGHT_GRAY_STAINED_GLASS:
				return true;
			case LIME_STAINED_GLASS:
				return true;
			case MAGENTA_STAINED_GLASS:
				return true;
			case ORANGE_STAINED_GLASS:
				return true;
			case PINK_STAINED_GLASS:
				return true;
			case PURPLE_STAINED_GLASS:
				return true;
			case RED_STAINED_GLASS:
				return true;
			case WHITE_STAINED_GLASS:
				return true;
			case YELLOW_STAINED_GLASS:
				return true;
			default:
				return false;
		}
	}

	public static boolean isBlockStainedGlassPane(Block block) {
		return isMaterialStainedGlassPane(block.getType());
	}

	public static boolean isMaterialStainedGlassPane(Material material) {
		switch (material) {
			case BLACK_STAINED_GLASS:
				return true;
			case BLUE_STAINED_GLASS:
				return true;
			case BROWN_STAINED_GLASS:
				return true;
			case CYAN_STAINED_GLASS:
				return true;
			case GRAY_STAINED_GLASS:
				return true;
			case GREEN_STAINED_GLASS:
				return true;
			case LIGHT_BLUE_STAINED_GLASS:
				return true;
			case LIGHT_GRAY_STAINED_GLASS:
				return true;
			case LIME_STAINED_GLASS:
				return true;
			case MAGENTA_STAINED_GLASS:
				return true;
			case ORANGE_STAINED_GLASS:
				return true;
			case PINK_STAINED_GLASS:
				return true;
			case PURPLE_STAINED_GLASS:
				return true;
			case RED_STAINED_GLASS:
				return true;
			case WHITE_STAINED_GLASS:
				return true;
			case YELLOW_STAINED_GLASS:
				return true;
			default:
				return false;
		}
	}

	public static boolean isRawItemRepairable(ItemStack stack) {
		if (Directory.repairableItems.contains(stack.getType()))
			return true;
		return false;
	}

	public static boolean isRawArmorRepairable(ItemStack stack) {
		if (Directory.repairableArmor.contains(stack.getType()))
			return true;
		return false;
	}

	public static boolean isRawRepairable(ItemStack stack) {
		if (Directory.repairableArmor.contains(stack.getType()) || Directory.repairableItems.contains(stack.getType()))
			return true;
		return false;
	}

	public static boolean isRepairable(ItemStack stack) {
		if (stack.getDurability() == (short) 0)
			return false;
		if (Directory.repairableArmor.contains(stack.getType()) || Directory.repairableItems.contains(stack.getType()))
			return true;
		return false;
	}

	public static void destroyTree(Block block) {
		if (!isBlockLog(block) && !isBlockLeaves(block))
			return;
		block.breakNaturally();
		for (BlockFace face : BlockFace.values())
			destroyTree(block.getRelative(face));
	}

	public static void clearPlayer(Player player) {
		player.getInventory().clear();
		player.getInventory().setHelmet(createMaterial(Material.AIR));
		player.getInventory().setChestplate(createMaterial(Material.AIR));
		player.getInventory().setLeggings(createMaterial(Material.AIR));
		player.getInventory().setBoots(createMaterial(Material.AIR));
		player.updateInventory();
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}
	}

	public static boolean createDataFolder() {
		if (!Server.getPlugin().getDataFolder().exists()) {
			Server.getPlugin().getDataFolder().mkdir();
			return true;
		}
		return false;
	}

	public static Location getRandomLocation(Player player) {
		double x = 0;
		while (x < 500 && x > -500) {
			x = -5000 + new Random(UUID.randomUUID().hashCode()).nextInt(10000) + 0.5;
		}
		double z = 0;
		while (z < 500 && z > -500) {
			z = -5000 + new Random(UUID.randomUUID().hashCode()).nextInt(10000) + 0.5;
		}
		double y = player.getWorld().getHighestBlockAt((int) x, (int) z).getY();
		return new Location(player.getWorld(), x, y + 1, z);
	}

	public static Location getZoneLocation(String zone) {
		if (Server.getWarpConfig().getData().contains(zone)) {
			World world = Server.getPlugin().getServer().getWorld(Server.getWarpConfig().getData().getString(zone + ".world"));
			double x = Server.getWarpConfig().getData().getDouble(zone + ".x");
			double y = Server.getWarpConfig().getData().getDouble(zone + ".y");
			double z = Server.getWarpConfig().getData().getDouble(zone + ".z");
			float yaw = (float) Server.getWarpConfig().getData().getDouble(zone + ".yaw");
			float pitch = (float) Server.getWarpConfig().getData().getDouble(zone + ".pitch");
			return new Location(world, x, y, z, yaw, pitch);
		}
		Bukkit.getConsoleSender().sendMessage("Critical error: Failed to find a saved zone named '" + zone + "'");
		World basicWorld = Bukkit.getWorlds().get(0);
		return new Location(basicWorld, 0, basicWorld.getHighestBlockYAt(0, 0), 0, 0, 0);
	}

	public static void sendMessageToNearbyPlayers(Location centre, String message, int radius) {
		for (Entity entity : centre.getWorld().getNearbyEntities(centre, radius, radius, radius)) {
			if (entity instanceof Player) {
				((Player) entity).sendMessage(message);
			}
		}
	}

	public static Player[] getStaffOnline() {
		return getStaffOnline(Server.getPlugin(), null);
	}

	public static Player[] getStaffOnline(Plugin plugin, Player ignorePlayer) {
		ArrayList<Player> players = new ArrayList<>();
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (getRankId(player.getUniqueId()) > -1)
				if (ignorePlayer == null || !ignorePlayer.getName().equals(player.getName()))
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
		} catch (Exception ig) {
		}
		return WordUtils.capitalize(type.toString().toLowerCase());
	}

	public static void setNickname(Player player, String nickname) {
		String path = "config." + player.getUniqueId().toString();
		Server.getPlugin().getConfig().set(path + ".nickname", nickname);
	}

	public static String formatEnchantPower(int power) {
		if (power > 10) {
			return "STATE_UNKNOWN_POWER";
		} else if (power == 10) {
			return "X";
		} else if (power == 9) {
			return "IX";
		} else if (power == 8) {
			return "VIII";
		} else if (power == 7) {
			return "VII";
		} else if (power == 6) {
			return "VI";
		} else if (power == 5) {
			return "V";
		} else if (power == 4) {
			return "IV";
		} else if (power == 3) {
			return "III";
		} else if (power == 2) {
			return "II";
		} else if (power == 1) {
			return "I";
		} else {
			return "";
		}
	}

	public static String formatEnchantment(String enchantmentID, int enchantPower) {
		String power = formatEnchantPower(enchantPower);
		if (power.length() > 0)
			power = " " + power;
		if (enchantmentID.equals("PROTECTION_ENVIRONMENTAL")) {
			return "Protection" + power;
		} else if (enchantmentID.equals("PROTECTION_FALL")) {
			return "Feather Falling" + power;
		} else if (enchantmentID.equals("DIG_SPEED")) {
			return "Efficiency" + power;
		} else if (enchantmentID.equals("DAMAGE_ALL")) {
			return "Sharpness" + power;
		} else if (enchantmentID.equals("ARROW_DAMAGE")) {
			return "Power" + power;
		} else if (enchantmentID.equals("ARROW_KNOCKBACK")) {
			return "Punch" + power;
		} else if (enchantmentID.equals("ARROW_FIRE")) {
			return "Flame" + power;
		} else if (enchantmentID.equals("DURABILITY")) {
			return "Unbreaking" + power;
		} else if (enchantmentID.equals("LOOT_BONUS_BLOCKS")) {
			return "Fortune" + power;
		} else if (enchantmentID.equals("LOOT_BONUS_MOBS")) {
			return "Looting" + power;
		} else {
			return capitalizeAll(enchantmentID, "_") + " " + power;
		}
	}

	public static String unformatEnchantment(String enchantment) {
		if (enchantment.equalsIgnoreCase("Protection")) {
			return "PROTECTION_ENVIRONMENTAL";
		} else if (enchantment.equalsIgnoreCase("Feather Falling")) {
			return "PROTECTION_FALL";
		} else if (enchantment.equalsIgnoreCase("Efficiency")) {
			return "DIG_SPEED";
		} else if (enchantment.equalsIgnoreCase("Sharpness")) {
			return "DAMAGE_ALL";
		} else if (enchantment.equalsIgnoreCase("Power")) {
			return "ARROW_DAMAGE";
		} else if (enchantment.equalsIgnoreCase("Punch")) {
			return "ARROW_KNOCKBACK";
		} else if (enchantment.equalsIgnoreCase("Flame")) {
			return "ARROW_FIRE";
		} else if (enchantment.equalsIgnoreCase("Unbreaking")) {
			return "DURABILITY";
		} else if (enchantment.equalsIgnoreCase("Fortune")) {
			return "LOOT_BONUS_BLOCKS";
		} else if (enchantment.equalsIgnoreCase("Looting")) {
			return "LOOT_BONUS_MOBS";
		} else {
			return enchantment.toUpperCase().replace(" ", "_");
		}
	}

	public static String formatMaterial(Material mat) {
		return capitalizeAll(mat.toString(), "_");
	}

	public static String capitalizeAll(String str) {
		return capitalizeAll(str, " ");
	}

	public static String capitalizeAll(String str, String var) {
		String[] arr = str.split(var);
		StringBuilder sb = new StringBuilder();
		for (String s : arr) {
			sb.append(WordUtils.capitalize(s.toLowerCase()) + " ");
		}
		return sb.toString().trim();
	}

	public static double roundDouble(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static boolean hasPotionEffect(Player player, PotionEffectType type) {
		for (PotionEffect p : player.getActivePotionEffects()) {
			if (p.getType() == type)
				return true;
		}
		return false;
	}

	public static ServerMinigame getCurrentMinigame(Player player) {
		String domain = getMinigameDomain(player);
		for (ServerMinigame minigame : ServerMinigame.values()) {
			if (domain.equals(minigame.toString().toLowerCase()))
				return minigame;
		}
		return ServerMinigame.UNKNOWN;
	}

	public static ServerMinigame getMinigameFromWorld(World world) {
		if ($.getZoneLocation("factions").getWorld().getName().equals(world.getName()))
			return ServerMinigame.FACTIONS;
		if ($.getZoneLocation("factions_nether").getWorld().getName().equals(world.getName()))
			return ServerMinigame.FACTIONS;
		if ($.getZoneLocation("factions_end").getWorld().getName().equals(world.getName()))
			return ServerMinigame.FACTIONS;
		if ($.getZoneLocation("survival").getWorld().getName().equals(world.getName()))
			return ServerMinigame.SURVIVAL;
		if ($.getZoneLocation("survival_nether").getWorld().getName().equals(world.getName()))
			return ServerMinigame.SURVIVAL;
		if ($.getZoneLocation("survival_end").getWorld().getName().equals(world.getName()))
			return ServerMinigame.SURVIVAL;
		if ($.getZoneLocation("kitpvp").getWorld().getName().equals(world.getName()))
			return ServerMinigame.KITPVP;
		if ($.getZoneLocation("skyfight0").getWorld().getName().equals(world.getName()))
			return ServerMinigame.SKYFIGHT;
		if ($.getZoneLocation("creative").getWorld().getName().equals(world.getName()))
			return ServerMinigame.CREATIVE;
		if ($.getZoneLocation("skyblock").getWorld().getName().equals(world.getName()))
			return ServerMinigame.SKYBLOCK;
		if ($.getZoneLocation("skyblock_nether").getWorld().getName().equals(world.getName()))
			return ServerMinigame.SKYBLOCK;
		if ($.getZoneLocation("hub").getWorld().getName().equals(world.getName()))
			return ServerMinigame.HUB;
		return ServerMinigame.UNKNOWN;
	}

	public static ArrayList<UUID> getMinigamePlayerList(String domain) {
		if (domain.equals("factions"))
			return Server.getFactions();
		if (domain.equals("kitpvp"))
			return Server.getKitpvp();
		if (domain.equals("skyfight"))
			return new ArrayList(Server.getSkyfight().keySet());
		if (domain.equals("creative"))
			return Server.getCreative();
		if (domain.equals("survival"))
			return Server.getSurvival();
		if (domain.equals("skyblock"))
			return Server.getSkyblock();
		if (domain.equals("hub"))
			return Server.getHub();
		return null;
	}

	public static String getMinigameDomain(Player player) {
		if (Server.getFactions().contains(player.getUniqueId()))
			return "factions";
		if (Server.getKitpvp().contains(player.getUniqueId()))
			return "kitpvp";
		if (Server.getSkyfight().containsKey(player.getUniqueId()))
			return "skyfight";
		if (Server.getCreative().contains(player.getUniqueId()))
			return "creative";
		if (Server.getSurvival().contains(player.getUniqueId()))
			return "survival";
		if (Server.getSkyblock().contains(player.getUniqueId()))
			return "skyblock";
		if (Server.getHub().contains(player.getUniqueId()))
			return "hub";
		return "hub";
	}

	public static String getMinigameTag(Player player) {
		return getMinigameTag(getMinigameDomain(player));
	}

	public static String getMinigameTag(String domain) {
		if (domain.equals("factions"))
			return Factions.tag;
		if (domain.equals("kitpvp"))
			return Kitpvp.tag;
		if (domain.equals("skyfight"))
			return Skyfight.tag;
		if (domain.equals("creative"))
			return Creative.tag;
		if (domain.equals("survival"))
			return Survival.tag;
		if (domain.equals("skyblock"))
			return Skyblock.tag;
		if (domain.equals("hub"))
			return Lobby.tag;
		return Lobby.tag;
	}

	public static boolean isNoteSharp(byte magicValue) {
		switch (magicValue % 25) {
			case 0:
				return true;
			case 1:
				return false;
			case 2:
				return true;
			case 3:
				return false;
			case 4:
				return true;
			case 5:
				return false;
			case 6:
				return false;
			case 7:
				return true;
			case 8:
				return false;
			case 9:
				return true;
			case 10:
				return false;
			case 11:
				return false;
			case 12:
				return true;
			case 13:
				return false;
			case 14:
				return true;
			case 15:
				return false;
			case 16:
				return true;
			case 17:
				return false;
			case 18:
				return false;
			case 19:
				return true;
			case 20:
				return false;
			case 21:
				return true;
			case 22:
				return false;
			case 23:
				return false;
			case 24:
				return true;
			default:
				return false;
		}
	}

	public static Object getWorldGuard() {
		return Bukkit.getPluginManager().getPlugin("WorldGuard");
	}

	public static Object getWorldEdit() {
		return Bukkit.getPluginManager().getPlugin("WorldEdit");
	}

	public static boolean isWithinSystemClaimedLand(Location location) {
		int size = 0;
		if (getWorldGuard() != null) {
			size = me.skorrloregaming.WorldGuardAPI.getApplicableRegionsSetSize(location);
		}
		if (size == 0) {
			Faction faction = Board.getInstance().getFactionAt(new FLocation(location));
			if (faction.isWarZone() || faction.isSafeZone()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public static boolean isWithinUnclaimedLand(Location location, Player player, boolean worldGuardOnly) {
		int size = 0;
		boolean[] hits = new boolean[]{false, false};
		if (getWorldGuard() != null) {
			size = me.skorrloregaming.WorldGuardAPI.getApplicableRegionsSetSize(location);
			hits[0] = me.skorrloregaming.WorldGuardAPI.testPassthroughRegionFlag(player, location);
			hits[1] = me.skorrloregaming.WorldGuardAPI.testBlockBreakRegionFlag(player, location);
		}
		if (size == 0 || hits[0] || hits[1]) {
			if (worldGuardOnly)
				return true;
			Faction faction = Board.getInstance().getFactionAt(new FLocation(location));
			if (faction.isWilderness()) {
				return true;
			} else if (!(player == null)) {
				if (FPlayers.getInstance().getByPlayer(player).isInOwnTerritory())
					return true;
			}
		}
		return false;
	}

	public static boolean isWithinUnclaimedLand(Location location, Player player) {
		return isWithinUnclaimedLand(location, player, false);
	}

	public static class Marriage {
		public static int setPlayerMarriedPlayer(OfflinePlayer player, Player targetPlayer, int marriageId) {
			int id = marriageId;
			String path = "config." + player.getUniqueId().toString();
			if (targetPlayer == null) {
				Server.getPlugin().getConfig().set(path + ".marry.marriedTo", "0");
				Server.getPlugin().getConfig().set(path + ".marry.marriageId", "0");
				id = 0;
			} else {
				Server.getPlugin().getConfig().set(path + ".marry.marriedTo", targetPlayer.getUniqueId().toString());
				Server.getPlugin().getConfig().set(path + ".marry.marriageId", marriageId);
			}
			return id;
		}

		public static int setPlayerMarriedPlayer(OfflinePlayer player, Player targetPlayer) {
			return setPlayerMarriedPlayer(player, targetPlayer, new Random(UUID.randomUUID().hashCode()).nextInt(100000));
		}

		public static OfflinePlayer getMarriedOfflinePlayer(Player player) {
			UUID id = getPlayerMarriedPlayerUUID(player);
			if (id == null)
				return null;
			return CraftGo.Player.getOfflinePlayer(id);
		}

		public static UUID getPlayerMarriedPlayerUUID(Player player) {
			String path = "config." + player.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".marry.marriedTo")) {
				String value = String.valueOf(Server.getPlugin().getConfig().getString(path + ".marry.marriedTo"));
				if (value.equals("0"))
					return null;
				return UUID.fromString(value);
			}
			Server.getPlugin().getConfig().set(path + ".marry", "0");
			return null;
		}

		public static int getPlayerMarriageId(Player player) {
			String path = "config." + player.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".marry.marriageId")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".marry.marriageId"));
			}
			Server.getPlugin().getConfig().set(path + ".marry.marriageId", "0");
			return 0;
		}

		public static void setPlayerGender(Player player, MarriageGender gender) {
			String path = "config." + player.getUniqueId().toString();
			Server.getPlugin().getConfig().set(path + ".marry.gender", gender.toString().toLowerCase());
		}

		public static MarriageGender getPlayerGender(OfflinePlayer player) {
			String path = "config." + player.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".marry.gender")) {
				String value = String.valueOf(Server.getPlugin().getConfig().getString(path + ".marry.gender"));
				if (value.equals("male")) {
					return MarriageGender.MALE;
				} else if (value.equals("female")) {
					return MarriageGender.FEMALE;
				}
			}
			return MarriageGender.UNDEFINED;
		}

		public static void setPlayerMarriedPvp(Player player, boolean pvp) {
			String path = "config." + player.getUniqueId().toString();
			Server.getPlugin().getConfig().set(path + ".marry.marriedPvp", pvp + "");
		}

		public static boolean getPlayerMarriedPvp(Player player) {
			String path = "config." + player.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".marry.marriedPvp")) {
				String value = String.valueOf(Server.getPlugin().getConfig().getString(path + ".marry.marriedPvp")).toLowerCase();
				return Boolean.valueOf(value);
			}
			return true;
		}

		public static void setPlayerSwearFilter(Player player, boolean pvp) {
			String path = "config." + player.getUniqueId().toString();
			Server.getPlugin().getConfig().set(path + ".marry.swearFilter", pvp + "");
		}

		public static boolean getPlayerSwearFilter(Player player) {
			String path = "config." + player.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".marry.swearFilter")) {
				String value = String.valueOf(Server.getPlugin().getConfig().getString(path + ".marry.swearFilter")).toLowerCase();
				return Boolean.valueOf(value);
			}
			return true;
		}
	}

	public static class Legacy {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "Minecraft" + ChatColor.GRAY + "] " + ChatColor.RESET;
	}

	public static class Lobby {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "hub" + ChatColor.GRAY + "] " + ChatColor.RESET;
	}

	public static class NotifyWorker {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "notify" + ChatColor.GRAY + "] " + ChatColor.RESET;
	}

	public static class Skyfight {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "skyfight" + ChatColor.GRAY + "] " + ChatColor.RESET;

		public static class Player {
			private org.bukkit.entity.Player player;
			private Tag tag;
			private int score = 0;
			private int teamValue = 0;
			private boolean hasTeamSelectionGuiOpen = false;

			public Player(org.bukkit.entity.Player player, int score, Tag tag) {
				this.setPlayer(player);
				if (tag == null) {
					this.tag = new Tag(player, null);
				} else {
					this.tag = tag;
				}
				this.setScore(score);
			}

			public Player(org.bukkit.entity.Player player, int score) {
				this.setPlayer(player);
				this.tag = new Tag(player, null);
				this.setScore(score);
			}

			public Player(org.bukkit.entity.Player player) {
				this.setPlayer(player);
				this.tag = new Tag(player, null);
			}

			public org.bukkit.entity.Player getPlayer() {
				return player;
			}

			public void setPlayer(org.bukkit.entity.Player player) {
				this.player = player;
			}

			public int getScore() {
				return score;
			}

			public void setScore(int score) {
				this.score = score;
			}

			public String getTeamName() {
				if (teamValue == Team.NO_TEAM) {
					return "No Team";
				} else if (teamValue == Team.BLUE) {
					return "Blue";
				} else if (teamValue == Team.RED) {
					return "Red";
				} else if (teamValue == Team.GREEN) {
					return "Green";
				} else if (teamValue == Team.YELLOW) {
					return "Yellow";
				} else if (teamValue == Team.PINK) {
					return "Pink";
				}
				return null;
			}

			public int getTeamValue() {
				return teamValue;
			}

			public void setTeamValue(int teamValue) {
				this.teamValue = teamValue;
			}

			public boolean isHasTeamSelectionGuiOpen() {
				return hasTeamSelectionGuiOpen;
			}

			public void setHasTeamSelectionGuiOpen(boolean hasTeamSelectionGuiOpen) {
				this.hasTeamSelectionGuiOpen = hasTeamSelectionGuiOpen;
			}

			public Tag getTag() {
				return tag;
			}
		}

		public class Team {
			public static final int NO_TEAM = 0;
			public static final int BLUE = 1;
			public static final int RED = 2;
			public static final int GREEN = 3;
			public static final int YELLOW = 4;
			public static final int PINK = 5;
		}

		public static class Tag {
			private org.bukkit.entity.Player damager, damagee;
			private int damageeHash = 0;

			public Tag(org.bukkit.entity.Player damager, org.bukkit.entity.Player damagee) {
				this.damager = damager;
				this.setDamagee(damagee);
			}

			public org.bukkit.entity.Player getDamager() {
				return damager;
			}

			public org.bukkit.entity.Player getDamagee() {
				return damagee;
			}

			public void setDamagee(org.bukkit.entity.Player arg0) {
				this.damagee = arg0;
				if (damagee == null) {
					damageeHash = 0;
				} else {
					damageeHash = UUID.randomUUID().hashCode();
				}
				if (!(damagee == null)) {
					final org.bukkit.entity.Player taggedDamagee = damagee;
					final int taggedDamageeHash = damageeHash;
					Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
						@Override
						public void run() {
							if (!(damagee == null) && damagee.getName().equals(taggedDamagee.getName()) && damageeHash == taggedDamageeHash)
								setDamagee(null);
						}
					}, 20L * 6L);
				}
			}
		}
	}

	public static class Factions {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "factions" + ChatColor.GRAY + "] " + ChatColor.RESET;
		public static String[] validKits = new String[]{"recruit", "donator", "redstone", "obsidian", "bedrock"};
		public static ItemStack[] kitRecruit = new ItemStack[]{$.createMaterial(Material.STONE_SWORD), $.createMaterial(Material.STONE_PICKAXE), $.createMaterial(Material.STONE_AXE), $.createMaterial(Material.STONE_SHOVEL), $.createMaterial(Material.COOKED_BEEF, 8), $.createMaterial(Material.LEATHER_HELMET), $.createMaterial(Material.LEATHER_CHESTPLATE), $.createMaterial(Material.LEATHER_LEGGINGS), $.createMaterial(Material.LEATHER_BOOTS)};
		public static ItemStack[] kitDonator = new ItemStack[]{$.createMaterial(Material.IRON_SWORD), $.createMaterial(Material.IRON_PICKAXE), $.createMaterial(Material.IRON_AXE), $.createMaterial(Material.IRON_SHOVEL), $.createMaterial(Material.COOKED_BEEF, 16), $.createMaterial(Material.IRON_HELMET), $.createMaterial(Material.LEATHER_CHESTPLATE), $.createMaterial(Material.LEATHER_LEGGINGS), $.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitRedstone = new ItemStack[]{$.createMaterial(Material.IRON_SWORD), $.createMaterial(Material.DIAMOND_PICKAXE), $.createMaterial(Material.IRON_AXE), $.createMaterial(Material.IRON_SHOVEL), $.createMaterial(Material.COOKED_BEEF, 24), $.createMaterial(Material.IRON_HELMET), $.createMaterial(Material.IRON_CHESTPLATE), $.createMaterial(Material.IRON_LEGGINGS), $.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitObsidian = new ItemStack[]{$.createMaterial(Material.DIAMOND_SWORD), $.createMaterial(Material.DIAMOND_PICKAXE), $.createMaterial(Material.DIAMOND_AXE), $.createMaterial(Material.DIAMOND_SHOVEL), $.createMaterial(Material.COOKED_BEEF, 32), $.createMaterial(Material.DIAMOND_HELMET), $.createMaterial(Material.IRON_CHESTPLATE), $.createMaterial(Material.IRON_LEGGINGS), $.createMaterial(Material.DIAMOND_BOOTS)};
		public static ItemStack[] kitBedrock = new ItemStack[]{$.createMaterial(Material.DIAMOND_SWORD), $.createMaterial(Material.DIAMOND_PICKAXE), $.createMaterial(Material.DIAMOND_AXE), $.createMaterial(Material.DIAMOND_SHOVEL), $.createMaterial(Material.COOKED_BEEF, 48), $.createMaterial(Material.DIAMOND_HELMET), $.createMaterial(Material.DIAMOND_CHESTPLATE), $.createMaterial(Material.DIAMOND_LEGGINGS), $.createMaterial(Material.DIAMOND_BOOTS)};



		public static int getPlayerKills(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".factions";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".kills")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kills"));
			}
			Server.getPlugin().getConfig().set(path + ".kills", "0");
			return 0;
		}

		public static void setPlayerKills(Player player, int value) {
			String path = "config." + player.getUniqueId().toString() + ".factions";
			Server.getPlugin().getConfig().set(path + ".kills", value + "");
		}

		public static int getPlayerDeaths(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".factions";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".deaths")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".deaths"));
			}
			Server.getPlugin().getConfig().set(path + ".deaths", "0");
			return 0;
		}

		public static void setPlayerDeaths(Player player, int value) {
			String path = "config." + player.getUniqueId().toString() + ".factions";
			Server.getPlugin().getConfig().set(path + ".deaths", value + "");
		}
	}

	public static class Survival {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "survival" + ChatColor.GRAY + "] " + ChatColor.RESET;
		public static String[] validKits = new String[]{"recruit"};
		public static ItemStack[] kitRecruit = new ItemStack[]{$.createMaterial(Material.STONE_SWORD), $.createMaterial(Material.STONE_PICKAXE), $.createMaterial(Material.STONE_AXE), $.createMaterial(Material.STONE_SHOVEL), $.createMaterial(Material.COOKED_BEEF, 16), $.createMaterial(Material.LEATHER_HELMET), $.createMaterial(Material.LEATHER_CHESTPLATE), $.createMaterial(Material.LEATHER_LEGGINGS), $.createMaterial(Material.LEATHER_BOOTS), $.createMaterial(Material.GOLDEN_SHOVEL)};
	}

	public static class Kitpvp {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "kitpvp" + ChatColor.GRAY + "] " + ChatColor.RESET;
		public static String[] validKits = new String[]{"starter", "potions"};
		public static ItemStack kitStarter1_Weapon = $.createMaterial(Material.STONE_AXE);
		public static ItemStack kitStarter2_4_Weapon = $.createMaterial(Material.IRON_AXE);
		public static ItemStack kitStarter5_8_Weapon = $.createMaterial(Material.DIAMOND_AXE);
		public static ItemStack[] kitStarter1_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.LEATHER_HELMET), $.createMaterial(Material.LEATHER_CHESTPLATE), $.createMaterial(Material.LEATHER_LEGGINGS), $.createMaterial(Material.LEATHER_BOOTS)};
		public static ItemStack[] kitStarter2_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.IRON_HELMET), $.createMaterial(Material.LEATHER_CHESTPLATE), $.createMaterial(Material.LEATHER_LEGGINGS), $.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitStarter3_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.IRON_HELMET), $.createMaterial(Material.CHAINMAIL_CHESTPLATE), $.createMaterial(Material.CHAINMAIL_LEGGINGS), $.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitStarter4_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.IRON_HELMET), $.createMaterial(Material.IRON_CHESTPLATE), $.createMaterial(Material.IRON_LEGGINGS), $.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitStarter5_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.IRON_HELMET), $.createMaterial(Material.IRON_CHESTPLATE), $.createMaterial(Material.IRON_LEGGINGS), $.createMaterial(Material.IRON_BOOTS)};
		public static ItemStack[] kitStarter6_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.DIAMOND_HELMET), $.createMaterial(Material.IRON_CHESTPLATE), $.createMaterial(Material.IRON_LEGGINGS), $.createMaterial(Material.DIAMOND_BOOTS), $.createMaterial(Material.GOLDEN_APPLE, 1)};
		public static ItemStack[] kitStarter7_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.DIAMOND_HELMET), $.createMaterial(Material.IRON_CHESTPLATE), $.createMaterial(Material.DIAMOND_LEGGINGS), $.createMaterial(Material.DIAMOND_BOOTS), $.createMaterial(Material.GOLDEN_APPLE, 2)};
		public static ItemStack[] kitStarter8_Items = new ItemStack[]{$.createMaterial(Material.BOW), $.createMaterial(Material.ARROW, 24), $.createMaterial(Material.DIAMOND_HELMET), $.createMaterial(Material.DIAMOND_CHESTPLATE), $.createMaterial(Material.DIAMOND_LEGGINGS), $.createMaterial(Material.DIAMOND_BOOTS), $.createMaterial(Material.GOLDEN_APPLE, 3)};
		public static ItemStack[] kitPotions = new ItemStack[]{$.addLore($.createMaterial(Material.BLAZE_ROD, 1, ChatColor.LIGHT_PURPLE + "Enchanted Wand"), new String[]{"Using this wand, you will gain defined potion effects."})};
		public static final int DONOR_MAX_UPGRADE_VALUE = 7;
		public static final int DEFAULT_MAX_UPGRADE_VALUE = 3;
		public static final int WEAPON_AXE = 0;
		public static final int WEAPON_SWORD = 1;

		public static int getPreferredWeaponType(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".preferredWeapon")) {
				String weapon = Server.getPlugin().getConfig().getString(path + ".preferredWeapon");
				if (weapon.equals("AXE")) {
					return WEAPON_AXE;
				} else if (weapon.equals("SWORD")) {
					return WEAPON_SWORD;
				}
			}
			Server.getPlugin().getConfig().set(path + ".preferredWeapon", "AXE");
			return WEAPON_AXE;
		}

		public static void setPreferredWeaponType(Player player, int type) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (type == WEAPON_AXE) {
				Server.getPlugin().getConfig().set(path + ".preferredWeapon", "AXE");
			} else if (type == WEAPON_SWORD) {
				Server.getPlugin().getConfig().set(path + ".preferredWeapon", "SWORD");
			}
		}

		public static int getPlayerKills(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".kills")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kills"));
			}
			Server.getPlugin().getConfig().set(path + ".kills", "0");
			return 0;
		}

		public static void setPlayerKills(Player player, int value) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			Server.getPlugin().getConfig().set(path + ".kills", value + "");
		}

		public static int getPlayerDeaths(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".deaths")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".deaths"));
			}
			Server.getPlugin().getConfig().set(path + ".deaths", "0");
			return 0;
		}

		public static void setPlayerDeaths(Player player, int value) {
			String path = "config." + player.getUniqueId().toString() + ".kitpvp";
			Server.getPlugin().getConfig().set(path + ".deaths", value + "");
		}

		public static int getUpgradeCount(Player player) {
			String path = "config." + player.getUniqueId().toString();
			if (!Server.getPlugin().getConfig().contains(path + ".kitpvp.upgrades"))
				setUpgradeCount(player, 0);
			int upgrades = Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.upgrades"));
			if (upgrades > DONOR_MAX_UPGRADE_VALUE)
				upgrades = DONOR_MAX_UPGRADE_VALUE;
			return upgrades;
		}

		public static void setUpgradeCount(Player player, int upgrades) {
			if (upgrades > DONOR_MAX_UPGRADE_VALUE)
				upgrades = DONOR_MAX_UPGRADE_VALUE;
			String path = "config." + player.getUniqueId().toString();
			Server.getPlugin().getConfig().set(path + ".kitpvp.upgrades", upgrades + "");
		}

		public static int getPreferredUpgrade(Player player) {
			String path = "config." + player.getUniqueId().toString();
			if (!Server.getPlugin().getConfig().contains(path + ".kitpvp.preferredUpgrade"))
				setPreferredUpgrade(player, getUpgradeCount(player));
			int preferredUpgrade = Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.preferredUpgrade"));
			int upgradeCount = getUpgradeCount(player);
			if (preferredUpgrade > upgradeCount) {
				setPreferredUpgrade(player, upgradeCount);
				preferredUpgrade = upgradeCount;
			}
			if (preferredUpgrade > DONOR_MAX_UPGRADE_VALUE)
				preferredUpgrade = DONOR_MAX_UPGRADE_VALUE;
			return preferredUpgrade;
		}

		public static void setPreferredUpgrade(Player player, int upgrade) {
			if (upgrade > DONOR_MAX_UPGRADE_VALUE)
				upgrade = DONOR_MAX_UPGRADE_VALUE;
			String path = "config." + player.getUniqueId().toString();
			Server.getPlugin().getConfig().set(path + ".kitpvp.preferredUpgrade", upgrade + "");
		}

		public static ItemStack getUpgradeClassWeapon(Player player, int upgrade, boolean axesOnly) {
			boolean swordType = false;
			if ($.Kitpvp.getPreferredWeaponType(player) == WEAPON_SWORD)
				swordType = true;
			if (upgrade > DONOR_MAX_UPGRADE_VALUE)
				upgrade = DONOR_MAX_UPGRADE_VALUE;
			ItemStack weapon = null;
			if (upgrade <= 0) {
				weapon = kitStarter1_Weapon.clone();
			} else if (upgrade > 0 && upgrade <= 3) {
				weapon = kitStarter2_4_Weapon.clone();
			} else if (upgrade > 3 && upgrade <= 7) {
				weapon = kitStarter5_8_Weapon.clone();
			}
			if (!axesOnly && swordType && weapon.getType() == Material.STONE_AXE)
				weapon = $.createMaterial(Material.STONE_SWORD);
			if (!axesOnly && swordType && weapon.getType() == Material.IRON_AXE)
				weapon = $.createMaterial(Material.IRON_SWORD);
			return weapon;
		}

		public static ItemStack[] getUpgradeClass(Player player, int upgrade, boolean axesOnly) {
			boolean swordType = false;
			if ($.Kitpvp.getPreferredWeaponType(player) == WEAPON_SWORD)
				swordType = true;
			if (upgrade > DONOR_MAX_UPGRADE_VALUE)
				upgrade = DONOR_MAX_UPGRADE_VALUE;
			ArrayList<ItemStack> kitArray = new ArrayList<>();
			ItemStack[] array = null;
			if (upgrade <= 0) {
				kitArray.add(kitStarter1_Weapon);
				array = kitStarter1_Items;
			} else if (upgrade == 1) {
				kitArray.add(kitStarter2_4_Weapon);
				array = kitStarter2_Items;
			} else if (upgrade == 2) {
				kitArray.add(kitStarter2_4_Weapon);
				array = kitStarter3_Items;
			} else if (upgrade == 3) {
				kitArray.add(kitStarter2_4_Weapon);
				array = kitStarter4_Items;
			} else if (upgrade == 4) {
				kitArray.add(kitStarter5_8_Weapon);
				array = kitStarter5_Items;
			} else if (upgrade == 5) {
				kitArray.add(kitStarter5_8_Weapon);
				array = kitStarter6_Items;
			} else if (upgrade == 6) {
				kitArray.add(kitStarter5_8_Weapon);
				array = kitStarter7_Items;
			} else if (upgrade >= 7) {
				kitArray.add(kitStarter5_8_Weapon);
				array = kitStarter8_Items;
			}
			for (int i = 0; i < array.length; i++) {
				kitArray.add(i + 1, array[i]);
			}
			if (!axesOnly && swordType && kitArray.get(0).getType() == Material.STONE_AXE)
				kitArray.set(0, $.createMaterial(Material.STONE_SWORD));
			if (!axesOnly && swordType && kitArray.get(0).getType() == Material.IRON_AXE)
				kitArray.set(0, $.createMaterial(Material.IRON_SWORD));
			if (!axesOnly && swordType && kitArray.get(0).getType() == Material.DIAMOND_AXE)
				kitArray.set(0, $.createMaterial(Material.DIAMOND_SWORD));
			return kitArray.toArray(new ItemStack[0]);
		}
	}

	public static class Creative {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "creative" + ChatColor.GRAY + "] " + ChatColor.RESET;
	}

	public static class Skyblock {
		public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "skyblock" + ChatColor.GRAY + "] " + ChatColor.RESET;

		public static int getPlayerPlacedBlocks(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".skyblock";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".placed")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".placed"));
			}
			Server.getPlugin().getConfig().set(path + ".placed", "0");
			return 0;
		}

		public static void setPlayerPlacedBlocks(Player player, int value) {
			String path = "config." + player.getUniqueId().toString() + ".skyblock";
			Server.getPlugin().getConfig().set(path + ".placed", value + "");
		}

		public static int getPlayerBrokenBlocks(Player player) {
			String path = "config." + player.getUniqueId().toString() + ".skyblock";
			if (Server.getPlugin().getConfig().contains(path) && Server.getPlugin().getConfig().contains(path + ".broken")) {
				return Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".broken"));
			}
			Server.getPlugin().getConfig().set(path + ".broken", "0");
			return 0;
		}

		public static void setPlayerBrokenBlocks(Player player, int value) {
			String path = "config." + player.getUniqueId().toString() + ".skyblock";
			Server.getPlugin().getConfig().set(path + ".broken", value + "");
		}
	}

	public static class Scoreboard extends $ {
		public static String getEntryFromScore(Objective objective, int score) {
			if (objective == null)
				return null;
			if (!hasScoreTaken(objective, score))
				return null;
			for (String entry : objective.getScoreboard().getEntries()) {
				Score objEntry = objective.getScore(entry);
				if (objEntry.getScore() == score)
					return objEntry.getEntry();
			}
			return null;
		}

		public static boolean hasScoreTaken(Objective objective, int score) {
			for (String s : objective.getScoreboard().getEntries()) {
				if (objective.getScore(s).getScore() == score)
					return true;
			}
			return false;
		}

		public static int replaceScore(Objective objective, int score, String newEntry) {
			if (hasScoreTaken(objective, score)) {
				String oldEntry = getEntryFromScore(objective, score);
				if (oldEntry.equalsIgnoreCase(newEntry))
					return 0;
				if (!(oldEntry.equalsIgnoreCase(newEntry)))
					objective.getScoreboard().resetScores(oldEntry);
			}
			objective.getScore(newEntry).setScore(score);
			return 1;
		}

		public static void configureSidebar(Player player, String title, Hashtable<String, Integer> table, boolean override, boolean replaceIntScoreValues) {
			ScoreboardManager manager = Bukkit.getScoreboardManager();
			org.bukkit.scoreboard.Scoreboard board;
			if (player.getScoreboard().getObjective("placeholder") == null) {
				board = player.getScoreboard();
				if (Server.getPluginDebug())
					Logger.debug("Using existing scoreboard for " + player.getName());
			} else {
				board = manager.getNewScoreboard();
				if (Server.getPluginDebug())
					Logger.debug("Generated new scoreboard for " + player.getName());
			}
			boolean found = false;
			for (Objective objective : board.getObjectives()) {
				if (objective.getName().equals("sidebar-obj")) {
					found = true;
					if (Server.getPluginDebug())
						Logger.debug("Found sidebar objective for " + player.getName());
					break;
				}
			}
			Objective objective;
			if (found) {
				objective = board.getObjective("sidebar-obj");
				if (override) {
					objective.unregister();
					objective = board.registerNewObjective("sidebar-obj", "dummy", title);
					if (Server.getPluginDebug())
						Logger.debug("Override! Generating new sidebar objective for " + player.getName());
				} else {
					if (Server.getPluginDebug())
						Logger.debug("Using existing sidebar objective for " + player.getName());
				}
			} else {
				objective = board.registerNewObjective("sidebar-obj", "dummy", title);
				if (Server.getPluginDebug())
					Logger.debug("Generating new sidebar objective for " + player.getName());
			}
			if (!(objective.getDisplayName().equals(title))) {
				objective.unregister();
				objective = board.registerNewObjective("sidebar-obj", "dummy", title);
				if (Server.getPluginDebug())
					Logger.debug("Mismatch! Generating new sidebar objective for " + player.getName());
			}
			int changed = 0;
			if (replaceIntScoreValues) {
				if (Server.getPluginDebug())
					Logger.debug("Recursively replacing appropriate scores on scoreboard for " + player.getName());
			} else {
				if (Server.getPluginDebug())
					Logger.debug("Recursively setting all scores on scoreboard for " + player.getName());
			}
			for (Entry<String, Integer> score : table.entrySet()) {
				if (!replaceIntScoreValues) {
					Score objEntry = objective.getScore(score.getKey());
					objEntry.setScore(score.getValue());
					changed++;
				} else {
					changed += replaceScore(objective, score.getValue().intValue(), score.getKey());
				}
			}
			if (changed > 0) {
				if (Server.getPluginDebug())
					Logger.debug("Changed " + changed + " line(s) on scoreboard for " + player.getName());
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
				objective = board.registerNewObjective("health", "dummy", "/ 20 Health");
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
}
