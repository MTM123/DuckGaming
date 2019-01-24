package com.skorrloregaming.main;

import com.skorrloregaming.main.SkyFight.SkyFight;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SkorrloreGaming
		extends JavaPlugin
		implements Listener {
	static String NAME = "SkorrloreGaming";
	static String L216 = ChatColor.DARK_RED + "[" + ChatColor.RED + NAME + ChatColor.DARK_RED + "] " + ChatColor.GREEN;
	SettingsManager settings = SettingsManager.getInstance();
	ArrayList<Player> unlocked = new ArrayList();
	public static Plugin getInstance;
	long RANDOM = 0L;
	static long CURR_AUCTION_CODE;
	static int CURR_AUCTION_PRICE;
	static int CURR_AUCTION_TIME;
	static String CURR_AUCTION_STARTER_NAME;
	static ItemStack CURR_AUCTION_ITEM;
	static final String AUCTION_DISPLAY;
	static final String AUCTION_START;
	static final String AUCTION_ITEM;
	static final String AUCTION_PRICE;
	static final String AUCTION_TIME;
	static final String AUCTION_CODEC;
	static int TIME_DELAY;
	static boolean AUCTION_OCCURING;
	static int CURR_AUCTION_BID;
	static String LAST_BID_PLAYER_NAME;
	static int LAST_BID_AMOUNT;
	ArrayList<Player> creative = new ArrayList();
	ArrayList<Player> doi = new ArrayList();
	ArrayList<Player> stpt = new ArrayList();
	ArrayList<Player> vip = new ArrayList();
	ArrayList<Player> bedrock = new ArrayList();
	ChatColor AQUA = ChatColor.AQUA;
	ChatColor RED = ChatColor.RED;
	ChatColor GREEN = ChatColor.GREEN;
	ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
	ChatColor GOLD = ChatColor.GOLD;
	ChatColor DARK_RED = ChatColor.DARK_RED;
	ArrayList<Player> sw = new ArrayList();
	ArrayList<Player> survival = new ArrayList();
	ArrayList<Player> teampvp = new ArrayList();
	ArrayList<Player> zahc = new ArrayList();
	ArrayList<Player> sesmen = new ArrayList();
	ArrayList<Player> pg = new ArrayList();
	ArrayList<Player> locked = new ArrayList();
	ArrayList<Player> tagged = new ArrayList();
	ArrayList<Player> hub = new ArrayList();
	ArrayList<Player> charged = new ArrayList();
	ArrayList<Player> potions_cooldown = new ArrayList();
	ItemStack AIR = new ItemStack(Material.AIR);
	ArrayList<Player> l_skyblock = new ArrayList();
	ArrayList<Player> l_skyfight = new ArrayList();
	ArrayList<Player> skyfight = new ArrayList();
	ArrayList<Player> skyfight1 = new ArrayList();
	ArrayList<Player> skyfight2 = new ArrayList();
	ArrayList<Player> skyfight3 = new ArrayList();
	ArrayList<Player> teampvpRespawn = new ArrayList();
	public final double defaultWalkSpeed = 0.2;
	public final double defaultSprintSpeed = 0.3;
	public final double defaultSuperSprintSpeed = 0.5;
	public final double chargedSuperSprintSpeed = 1.0;
	ArrayList<Player> unlockedGamemode = new ArrayList();
	ArrayList<Player> weak = new ArrayList();
	ArrayList<Player> CC = new ArrayList();
	ArrayList<Player> skywars = new ArrayList();
	ArrayList<Player> sfly = new ArrayList();
	ArrayList<Player> sfeed = new ArrayList();
	ArrayList<Player> afk = new ArrayList();
	private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$Material;

	static {
		CURR_AUCTION_CODE = 0L;
		CURR_AUCTION_PRICE = 0;
		CURR_AUCTION_TIME = 0;
		CURR_AUCTION_STARTER_NAME = "Console";
		CURR_AUCTION_ITEM = new ItemStack(Material.STONE);
		AUCTION_DISPLAY = ChatColor.DARK_RED + "[" + ChatColor.RED + "Auction" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		AUCTION_START = String.valueOf(AUCTION_DISPLAY) + "An auction has been started by " + ChatColor.GOLD + CURR_AUCTION_STARTER_NAME + ChatColor.AQUA + "!";
		AUCTION_ITEM = String.valueOf(AUCTION_DISPLAY) + "Item: " + ChatColor.GOLD + CURR_AUCTION_ITEM.getItemMeta().getDisplayName();
		AUCTION_PRICE = String.valueOf(AUCTION_DISPLAY) + "Price: " + ChatColor.GOLD + CURR_AUCTION_PRICE;
		AUCTION_TIME = String.valueOf(AUCTION_DISPLAY) + "Time: " + ChatColor.GOLD + CURR_AUCTION_TIME + ChatColor.GREEN + " Seconds";
		AUCTION_CODEC = String.valueOf(AUCTION_DISPLAY) + "Codec: " + ChatColor.GOLD + CURR_AUCTION_CODE;
		TIME_DELAY = 600;
		AUCTION_OCCURING = false;
		CURR_AUCTION_BID = 0;
		LAST_BID_PLAYER_NAME = "Console";
		LAST_BID_AMOUNT = 0;
	}

	public int getMoney(Player p) {
		return this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
	}

	public String getNickname(Player p) {
		return this.getConfig().getString("nickname." + p.getUniqueId().toString());
	}

	public void setNickname(Player p, String nickname, boolean value) {
		this.getConfig().set("nickname." + p.getUniqueId().toString(), nickname);
		this.getConfig().set("ranks." + p.getUniqueId().toString() + ".nickname", value);
		this.saveConfig();
	}

	public boolean setRank(Player p, String rank) {
		if (rank.equalsIgnoreCase("owner")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "owner");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("coowner")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "coowner");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("admin")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "admin");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("helper")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "helper");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("moderator")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "moderator");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("youtube")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "youtube");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("guest")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "guest");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("member")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "member");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("member+")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "memberx");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("mvp+")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "mvpx");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("mvp")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "mvp");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("vip+")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "vipx");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		if (rank.equalsIgnoreCase("vip")) {
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "vip");
			this.saveConfig();
			p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + this.getNickname(p) + ChatColor.WHITE);
			this.loadPermissions(p);
			return true;
		}
		return false;
	}

	public String getRankPrefix(Player p) {
		if (this.getRank(p).equalsIgnoreCase("owner")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "Owner" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("coowner")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "CoOwner" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("admin")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "Admin" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("moderator")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "Moderator" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("helper")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "Helper" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("mvpx")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "MVP+" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("mvp")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "MVP" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("vipx")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP+" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("vip")) {
			return ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP" + ChatColor.DARK_RED + "] " + ChatColor.AQUA;
		}
		if (this.getRank(p).equalsIgnoreCase("youtube")) {
			return ChatColor.DARK_RED + "[" + ChatColor.AQUA + "Youtube" + ChatColor.DARK_RED + "] " + ChatColor.GREEN;
		}
		if (this.getRank(p).equalsIgnoreCase("member")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Member" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN;
		}
		if (this.getRank(p).equalsIgnoreCase("memberx")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Member" + ChatColor.GOLD + "+" + ChatColor.DARK_GRAY + "] " + ChatColor.GREEN;
		}
		if (this.getRank(p).equalsIgnoreCase("guest")) {
			return ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Guest" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY;
		}
		throw new RuntimeException();
	}

	public String getRank(Player p) {
		return this.getConfig().getString("ranks." + p.getUniqueId().toString() + ".rank");
	}

	public void toTeamPvP(Player p) {
		if (this.teampvp.contains(p)) {
			p.sendMessage(String.valueOf(L216) + ChatColor.RED + "Psst. You are already in that server!");
			return;
		}
		if (this.survival.contains(p)) {
			try {
				Storage.saveInventory(p, "survival");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.creative.contains(p)) {
			try {
				Storage.saveInventory(p, "creative");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.getConfig().getBoolean("Servers.TeamPvP.Banned.Players." + p.getName())) {
			p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "TeamPvP" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
			p.closeInventory();
			return;
		}
		World world = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
		double xer = this.settings.getData().getDouble("warps.map000_teampvp.x");
		if (this.hub.contains(p)) {
			this.hub.remove(p);
		}
		if (!this.teampvp.contains(p)) {
			this.teampvp.add(p);
			this.ManagerMain(p);
			p.addAttachment((Plugin) this, "alphachest.chest", true);
			p.addAttachment((Plugin) this, "alphachest.keepOnDeath", true);
		}
		double yer = this.settings.getData().getDouble("warps.map000_teampvp.y");
		double zer = this.settings.getData().getDouble("warps.map000_teampvp.z");
		p.teleport(new Location(world, xer, yer, zer));
		Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has logged into TeamPvP!"));
		try {
			Storage.restoreInventory(p, "teampvp");
		} catch (Exception exception) {

		}
		try {
			p.teleport(this.getLastLocation(p, "teampvp"));
		} catch (Exception exception) {

		}
	}

	public void toCreative(Player p) {
		if (this.teampvp.contains(p)) {
			try {
				Storage.saveInventory(p, "teampvp");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.survival.contains(p)) {
			try {
				Storage.saveInventory(p, "survival");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.creative.contains(p)) {
			p.sendMessage(String.valueOf(L216) + ChatColor.RED + "Psst. You are already in that server!");
			return;
		}
		if (this.getConfig().getBoolean("Servers.GTA.Banned.Players." + p.getName())) {
			p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "Creative" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
			p.closeInventory();
			return;
		}
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_creative.world"));
		double x = this.settings.getData().getDouble("warps.map000_creative.x");
		if (this.hub.contains(p)) {
			this.hub.remove(p);
		}
		if (!this.creative.contains(p)) {
			this.creative.add(p);
		}
		double y = this.settings.getData().getDouble("warps.map000_creative.y");
		double z = this.settings.getData().getDouble("warps.map000_creative.z");
		p.teleport(new Location(w, x, y, z));
		p.teleport(new Location(w, x, y, z));
		p.teleport(new Location(w, x, y, z));
		p.sendMessage(ChatColor.GOLD + "Teleported to " + ChatColor.GREEN + "Creative");
		Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has logged into Creative!"));
		try {
			Storage.restoreInventory(p, "creative");
		} catch (Exception exception) {

		}
		try {
			p.teleport(this.getLastLocation(p, "creative"));
		} catch (Exception exception) {

		}
	}

	public void toSurvival(Player p) {
		if (this.teampvp.contains(p)) {
			try {
				Storage.saveInventory(p, "teampvp");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.survival.contains(p)) {
			p.sendMessage(String.valueOf(L216) + ChatColor.RED + "Psst. You are already in that server!");
			return;
		}
		if (this.creative.contains(p)) {
			try {
				Storage.saveInventory(p, "creative");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.getConfig().getBoolean("Servers.Factions.Banned.Players." + p.getName())) {
			p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "Survival" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
			p.closeInventory();
			return;
		}
		p.closeInventory();
		World w111 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_survival.world"));
		double x111 = this.settings.getData().getDouble("warps.map000_survival.x");
		if (this.hub.contains(p)) {
			this.hub.remove(p);
		}
		if (!this.survival.contains(p)) {
			this.survival.add(p);
		}
		double y111 = this.settings.getData().getDouble("warps.map000_survival.y");
		double z111 = this.settings.getData().getDouble("warps.map000_survival.z");
		p.teleport(new Location(w111, x111, y111, z111));
		Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has logged into Survival!"));
		p.sendMessage(ChatColor.GOLD + "Teleported to " + ChatColor.GREEN + "Survival");
		try {
			Storage.restoreInventory(p, "survival");
		} catch (Exception exception) {

		}
		try {
			p.teleport(this.getLastLocation(p, "survival"));
		} catch (Exception exception) {

		}
	}

	public void loadPermissions(Player p) {
		p.addAttachment((Plugin) this, "alphachest.admin", false);
		if (this.getRank(p).equalsIgnoreCase("owner")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", true);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", true);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("coowner")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", true);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", true);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("admin")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", false);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("moderator")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", false);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("helper")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", false);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("mvpx")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", true);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", true);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("mvp")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", true);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", true);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("vipx")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", false);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("vip")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", false);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("youtube")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", true);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", true);
			p.addAttachment((Plugin) this, "skorrloregaming.nickname", true);
			return;
		}
		if (this.getRank(p).equalsIgnoreCase("guest")) {
			p.addAttachment((Plugin) this, "skorrloregaming.sfeed", false);
			p.addAttachment((Plugin) this, "skorrloregaming.sfly", false);
			p.addAttachment((Plugin) this, "skorrloregaming.srepairall", false);
			return;
		}
	}

	public void onEnable() {
		getInstance = this;
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents((Listener) this, (Plugin) this);
		this.settings.setup((Plugin) this);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.getConfig().set("Auction.Running", false);
		this.recipeA();
		this.recipeB();
		this.recipeC();
		this.recipeD();
		this.recipeE();
		this.recipeF();
		this.recipeG();
		this.recipeH();
		this.recipe1();
		this.recipe2();
		this.recipe3();
		this.recipe4();
		this.enrichedIronIngot();
		this.unstableruby();
		this.stableruby();
		this.rubySword();
		this.rubyPickaxe();
		this.rubyAxe();
		this.rubySpade();
		this.rubyHelmet();
		this.rubyChestplate();
		this.rubyLeggings();
		this.rubyBoots();
		this.finalruby();
	}

	public void onDisable() {
		this.getServer().clearRecipes();
	}

	public Location getLastLocation(Player player, String data) {
		World world = Bukkit.getWorld(this.getConfig().getString("ranks." + player.getUniqueId().toString() + "." + data + ".world"));
		int x = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + "." + data + ".x");
		int y = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + "." + data + ".y");
		int z = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + "." + data + ".z");
		float yaw = (float) this.getConfig().getDouble("ranks." + player.getUniqueId().toString() + "." + data + ".yaw");
		float pitch = (float) this.getConfig().getDouble("ranks." + player.getUniqueId().toString() + "." + data + ".pitch");
		Location loc = new Location(world, (double) x, (double) y, (double) z, yaw, pitch);
		return loc;
	}

	private void enrichedIronIngot() {
		ItemStack DF = MaterialManager.Create(Material.EMERALD, ChatColor.WHITE + "Enriched Iron Ingot", 1, 0);
		FurnaceRecipe DRR = new FurnaceRecipe(DF, Material.IRON_INGOT);
		Bukkit.getServer().addRecipe((Recipe) DRR);
	}

	private void unstableruby() {
		ItemStack DF = MaterialManager.Create(Material.RECORD_3, ChatColor.WHITE + "Unstable Ruby", 1, 0);
		ShapedRecipe DFR = new ShapedRecipe(DF);
		DFR.shape(new String[]{"RIR", "ISI", "RIR"});
		DFR.setIngredient('I', Material.EMERALD);
		DFR.setIngredient('S', Material.GHAST_TEAR);
		DFR.setIngredient('R', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe((Recipe) DFR);
	}

	private void stableruby() {
		ItemStack DF = MaterialManager.Create(Material.QUARTZ, ChatColor.WHITE + "Ruby Framework", 1, 0);
		FurnaceRecipe DRR = new FurnaceRecipe(DF, Material.RECORD_3);
		Bukkit.getServer().addRecipe((Recipe) DRR);
	}

	private void finalruby() {
		ItemStack DF = MaterialManager.Create(Material.BLAZE_POWDER, ChatColor.WHITE + "Ruby", 1, 0);
		ShapedRecipe DFR = new ShapedRecipe(DF);
		DFR.shape(new String[]{"RIR", "ISI", "RIR"});
		DFR.setIngredient('I', Material.GOLD_INGOT);
		DFR.setIngredient('S', Material.QUARTZ);
		DFR.setIngredient('R', Material.MAGMA_CREAM);
		Bukkit.getServer().addRecipe((Recipe) DFR);
	}

	private void rubySword() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), ChatColor.WHITE + "Ruby Sword", Enchantment.DAMAGE_ALL, 8, Enchantment.DURABILITY, 2600, Enchantment.LOOT_BONUS_MOBS, 3);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{" A ", " A ", " B "});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		CRR.setIngredient('B', Material.EMERALD);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubyPickaxe() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_PICKAXE, 1), ChatColor.WHITE + "Ruby Pickaxe", Enchantment.DIG_SPEED, 25, Enchantment.DURABILITY, 2600, Enchantment.LOOT_BONUS_BLOCKS, 5);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", " B ", " B "});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		CRR.setIngredient('B', Material.EMERALD);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubyAxe() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_AXE, 1), ChatColor.WHITE + "Ruby Axe", Enchantment.DIG_SPEED, 25, Enchantment.DURABILITY, 2600, Enchantment.LOOT_BONUS_BLOCKS, 5);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{" AA", " BA", " B "});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		CRR.setIngredient('B', Material.EMERALD);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubySpade() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_SPADE, 1), ChatColor.WHITE + "Ruby Shovel", Enchantment.DIG_SPEED, 25, Enchantment.DURABILITY, 2600, Enchantment.LOOT_BONUS_BLOCKS, 5);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{" A ", " B ", " B "});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		CRR.setIngredient('B', Material.EMERALD);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubyHelmet() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), ChatColor.WHITE + "Ruby Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 8, Enchantment.DURABILITY, 2600, Enchantment.LUCK, 2);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", "A A"});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubyChestplate() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), ChatColor.WHITE + "Ruby Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 8, Enchantment.DURABILITY, 2600, Enchantment.LUCK, 2);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"A A", "AAA", "AAA"});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubyLeggings() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_LEGGINGS, 1), ChatColor.WHITE + "Ruby Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 8, Enchantment.DURABILITY, 2600, Enchantment.LUCK, 2);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", "A A", "A A"});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void rubyBoots() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), ChatColor.WHITE + "Ruby Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 8, Enchantment.DURABILITY, 2600, Enchantment.LUCK, 2);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"A A", "A A"});
		CRR.setIngredient('A', Material.BLAZE_POWDER);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipeA() {
		ItemStack DF = MaterialManager.Create(Material.SLIME_BALL, ChatColor.GOLD + "Diamond Framework", 1, 0);
		ShapedRecipe DFR = new ShapedRecipe(DF);
		DFR.shape(new String[]{" I ", "ISI", " I "});
		DFR.setIngredient('I', Material.IRON_INGOT);
		DFR.setIngredient('S', Material.STICK);
		Bukkit.getServer().addRecipe((Recipe) DFR);
	}

	private void recipeB() {
		ItemStack D = MaterialManager.Create(Material.DIAMOND, null, 1, 0);
		ShapedRecipe DR = new ShapedRecipe(D);
		DR.shape(new String[]{" AS", "ASA", "SA "});
		DR.setIngredient('S', Material.STICK);
		DR.setIngredient('A', Material.SLIME_BALL);
		Bukkit.getServer().addRecipe((Recipe) DR);
	}

	private void recipeC() {
		ItemStack CR = MaterialManager.Create(Material.MAGMA_CREAM, "Emerald Framework", 1, 0);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"ABA", "DCD", "ABA"});
		CRR.setIngredient('A', Material.FLINT);
		CRR.setIngredient('D', Material.COBBLESTONE);
		CRR.setIngredient('B', Material.GOLD_INGOT);
		CRR.setIngredient('C', Material.SLIME_BALL);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipeD() {
		ItemStack CR = MaterialManager.Create(Material.GHAST_TEAR, ChatColor.GOLD + "Emerald", 1, 0);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", "ABA", "AAA"});
		CRR.setIngredient('A', Material.MAGMA_CREAM);
		CRR.setIngredient('B', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipeE() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_PICKAXE, 1), "Emerald Pickaxe", Enchantment.DIG_SPEED, 10, Enchantment.DURABILITY, 1300, Enchantment.LOOT_BONUS_BLOCKS, 3);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", " B ", " B "});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		CRR.setIngredient('B', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipeF() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_SPADE, 1), "Emerald Shovel", Enchantment.DIG_SPEED, 10, Enchantment.DURABILITY, 1300, Enchantment.LOOT_BONUS_BLOCKS, 3);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{" A ", " B ", " B "});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		CRR.setIngredient('B', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipeG() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_AXE, 1), "Emerald Axe", Enchantment.DIG_SPEED, 10, Enchantment.DURABILITY, 1300, Enchantment.LOOT_BONUS_BLOCKS, 3);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{" AA", " BA", " B "});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		CRR.setIngredient('B', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipeH() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), "Emerald Sword", Enchantment.DAMAGE_ALL, 5, Enchantment.DURABILITY, 1300, Enchantment.LOOT_BONUS_MOBS, 3);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{" A ", " A ", " B "});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		CRR.setIngredient('B', Material.REDSTONE_BLOCK);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipe1() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), "Emerald Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 1300, Enchantment.LUCK, 1);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", "A A"});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipe2() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), "Emerald Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 1300, Enchantment.LUCK, 1);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"A A", "AAA", "AAA"});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipe3() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_LEGGINGS, 1), "Emerald Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 1300, Enchantment.LUCK, 1);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"AAA", "A A", "A A"});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	private void recipe4() {
		ItemStack CR = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), "Emerald Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 1300, Enchantment.LUCK, 1);
		ShapedRecipe CRR = new ShapedRecipe(CR);
		CRR.shape(new String[]{"A A", "A A"});
		CRR.setIngredient('A', Material.GHAST_TEAR);
		Bukkit.getServer().addRecipe((Recipe) CRR);
	}

	public void Random() {
		Random randomGenerator = new Random();
		int idx = 1;
		if (idx <= 10) {
			int randomInt = randomGenerator.nextInt(100);
			int randomInt2 = randomGenerator.nextInt(1000);
			int randomInt3 = randomGenerator.nextInt(10000);
			int randomInt4 = randomGenerator.nextInt(100000);
			int randomInt5 = randomGenerator.nextInt(1000000);
			int randomInt8 = randomGenerator.nextInt(10000000);
			int randomInt9 = randomGenerator.nextInt(20000000);
			int randomInt6 = randomGenerator.nextInt(10000);
			int randomInt7 = randomGenerator.nextInt(1000);
			this.RANDOM = randomInt + randomInt2 + randomInt3 + randomInt4 + randomInt5 + randomInt6 + randomInt7 + randomInt8 + randomInt9;
			return;
		}
	}

	@EventHandler
	public void onPLayerLogin(PlayerLoginEvent e) {
		if (e.getResult() != PlayerLoginEvent.Result.KICK_BANNED) {
			return;
		}
		for (Player pl : this.getServer().getOnlinePlayers()) {
			if (!pl.hasPermission("skorrloregaming.listen.report")) continue;
			pl.sendMessage(ChatColor.GOLD + e.getPlayer().getName() + ChatColor.GREEN + " has been denied access to join for the following reason:");
			pl.sendMessage(ChatColor.GREEN + e.getKickMessage());
		}
	}

	public void Auction(final ItemStack ITEM, int PRICE, int TIME, String NAME, long SYS_DATACODE, final Player Player2) {
		if (this.getConfig().getBoolean("Auction.Running")) {
			Player2.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "There is already a auction running! Please wait until that auction is complete before creating a new auction table.");
			return;
		}
		CURR_AUCTION_CODE = SYS_DATACODE;
		CURR_AUCTION_PRICE = PRICE;
		if (TIME == 30) {
			this.getConfig().set("Auction.Time", 600);
			this.getConfig().set("Auction.TimeValue", "30 Seconds");
			this.saveConfig();
		} else if (TIME == 60) {
			this.getConfig().set("Auction.Time", 1200);
			this.getConfig().set("Auction.TimeValue", "1 Minutes");
			this.saveConfig();
		} else if (TIME == 120) {
			this.getConfig().set("Auction.Time", 2400);
			this.getConfig().set("Auction.TimeValue", "2 Minutes");
			this.saveConfig();
		} else {
			Player2.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "Error was encountered while creating auction table (Time can only be 30, 60, or 120 as a value)");
			this.getConfig().set("Auction.Running", false);
			this.saveConfig();
			return;
		}
		CURR_AUCTION_TIME = TIME_DELAY;
		CURR_AUCTION_STARTER_NAME = NAME;
		CURR_AUCTION_ITEM = ITEM;
		String VMO = this.getConfig().getString("Auction.TimeValue");
		this.getConfig().set("Auction.Running", true);
		this.saveConfig();
		Bukkit.broadcastMessage((ChatColor.RED + "[" + ChatColor.GRAY + "-----------------------------------------------------------" + ChatColor.RED + "]"));
		Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + "An auction has been started by " + ChatColor.GOLD + CURR_AUCTION_STARTER_NAME + ChatColor.AQUA + "!"));
		Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + "Item: " + ChatColor.GOLD + ITEM.getType()));
		Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + "Price: " + ChatColor.GOLD + PRICE));
		Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + "Time: " + ChatColor.GOLD + VMO));
		Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + "Codec: " + ChatColor.GOLD + SYS_DATACODE));
		Bukkit.broadcastMessage((ChatColor.RED + "[" + ChatColor.GRAY + "-----------------------------------------------------------" + ChatColor.RED + "]"));
		this.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {

			@Override
			public void run() {
				SkorrloreGaming.this.getConfig().set("Auction.Running", false);
				SkorrloreGaming.this.saveConfig();
				if (SkorrloreGaming.this.getConfig().getBoolean("Auction.Cancelled")) {
					SkorrloreGaming.this.getConfig().set("Auction.Cancelled", false);
					SkorrloreGaming.this.saveConfig();
					return;
				}
				if (!SkorrloreGaming.this.getConfig().getString("Auction.Bids.Last.Name").equals("Console")) {
					if (SkorrloreGaming.this.getConfig().getInt("ranks." + Player2.getUniqueId().toString() + ".money") >= SkorrloreGaming.this.getConfig().getInt("Auction.Bids.Last.Amount")) {
						int Amount = SkorrloreGaming.this.getConfig().getInt("Auction.Bids.Last.Amount");
						SkorrloreGaming.this.getConfig().set("Auction.Bids.Last.Amount", Amount);
						SkorrloreGaming.this.getConfig().set("Auction.Bids.Last.Name", Player2.getName());
						SkorrloreGaming.this.saveConfig();
						Player Winner = Bukkit.getPlayer(SkorrloreGaming.this.getConfig().getString("Auction.Bids.Last.Name"));
						if (Winner == null) {
							Bukkit.broadcastMessage((String.valueOf(SkorrloreGaming.AUCTION_DISPLAY) + "The winner of the auction left the server! The auction has been cancelled!"));
							return;
						}
						SkorrloreGaming.this.MoneyTake(Winner, SkorrloreGaming.this.getConfig().getInt("Auction.Bids.Last.Amount"));
						Winner.getInventory().addItem(new ItemStack[]{ITEM});
						Player2.getInventory().remove(ITEM);
						Bukkit.broadcastMessage((String.valueOf(SkorrloreGaming.AUCTION_DISPLAY) + "The winner of the auction is " + ChatColor.GOLD + SkorrloreGaming.this.getConfig().getString("Auction.Bids.Last.Name")));
					} else {
						Player2.sendMessage(String.valueOf(SkorrloreGaming.AUCTION_DISPLAY) + ChatColor.RED + "You do not have enough money to purchase this item!");
						Bukkit.broadcastMessage((String.valueOf(SkorrloreGaming.AUCTION_DISPLAY) + "The winner did not have enough money to purchase the auction item!"));
					}
				}
				Bukkit.broadcastMessage((String.valueOf(SkorrloreGaming.AUCTION_DISPLAY) + "The current auction has ended!"));
			}
		}, (long) this.getConfig().getInt("Auction.Time"));
	}

	@EventHandler
	public void PlayerChatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		try {
			if (this.getConfig().getBoolean("mute." + p.getUniqueId().toString())) {
				p.sendMessage(String.valueOf(L216) + ChatColor.RED + "You cannot chat whilist muted!");
				this.getLogger().warning("[" + e.getPlayer().getName() + "/Listen] " + e.getMessage());
				e.setCancelled(true);
				return;
			}
		} catch (Exception exception) {

		}
		String alt = e.getMessage();
		if (this.getRank(p).equalsIgnoreCase("mvpx") || this.getRank(p).equalsIgnoreCase("mvp") || this.getRank(p).equalsIgnoreCase("vipx") || this.getRank(p).equalsIgnoreCase("youtube") || this.getRank(p).equalsIgnoreCase("owner")) {
			alt = alt.replaceAll("&", "\u00a7");
		}
		e.setMessage("\u00a7a" + alt);
	}

	@EventHandler
	public void RegisterKill0(PlayerDeathEvent e) {
		try {
			if (this.skyfight.contains(e.getEntity().getPlayer())) {
				e.setDeathMessage("");
				SkyFight.removeLastAttacker(e.getEntity().getPlayer());
				return;
			}
			e.setDeathMessage("");
			Player p = e.getEntity().getPlayer();
			Player k = e.getEntity().getKiller();
			int rounded = (int) k.getHealth();
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has been killed by " + ChatColor.GOLD + this.getNickname(k) + ChatColor.GOLD + " [" + ChatColor.RED + rounded + ChatColor.GOLD + "]" + ChatColor.AQUA + "!"));
			if (!(e.getEntity() instanceof Player)) {
				return;
			}
			int oldKsValue = this.getConfig().getInt("ranks." + k.getUniqueId().toString() + ".ks1");
			int newKsValue = oldKsValue + 1;
			this.getConfig().set("ranks." + k.getUniqueId().toString() + ".ks1", newKsValue);
			this.saveConfig();
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".ks1", 0);
			this.saveConfig();
			int oldDeathsValue = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".deaths");
			int newDeathsValue = oldDeathsValue + 1;
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".deaths", newDeathsValue);
			this.saveConfig();
			int oldKillsValue = this.getConfig().getInt("ranks." + k.getUniqueId().toString() + ".kills");
			int newKillsValue = oldKillsValue + 1;
			this.getConfig().set("ranks." + k.getUniqueId().toString() + ".kills", newKillsValue);
			this.saveConfig();
			int oldMoneyValue = this.getConfig().getInt("ranks." + k.getUniqueId().toString() + ".money");
			int newMoneyValue = oldMoneyValue + 5;
			int moneyIncrement = this.getConfig().getInt("ranks." + k.getUniqueId().toString() + ".kills") / 50;
			this.getConfig().set("ranks." + k.getUniqueId().toString() + ".money", (newMoneyValue += moneyIncrement));
			this.saveConfig();
			k.sendMessage(ChatColor.GREEN + "You have been given " + ChatColor.GOLD + 5 + "+(" + moneyIncrement + "x)=" + (5 + moneyIncrement) + ChatColor.GREEN + " dollars for killing " + ChatColor.GOLD + p.getName());
		} catch (Exception p) {

		}
	}

	public void repairAll(Player p) {
		ItemStack items;
		ItemStack[] arritemStack = p.getInventory().getContents();
		int n = arritemStack.length;
		int n2 = 0;
		while (n2 < n) {
			items = arritemStack[n2];
			try {
				items.setDurability((short) 0);
			} catch (Exception exception) {

			}
			++n2;
		}
		arritemStack = p.getEquipment().getArmorContents();
		n = arritemStack.length;
		n2 = 0;
		while (n2 < n) {
			items = arritemStack[n2];
			try {
				items.setDurability((short) 0);
			} catch (Exception exception) {

			}
			++n2;
		}
		p.sendMessage(ChatColor.GOLD + "Your inventory has been successfully repaired!");
	}

	public void MoneyGive(Player player, int amount) {
		Player p = player;
		int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
		this.getConfig().set("ranks." + p.getUniqueId().toString() + ".money", (Money += amount));
		this.saveConfig();
	}

	public void MoneyTake(Player player, int amount) {
		Player p = player;
		int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
		this.getConfig().set("ranks." + p.getUniqueId().toString() + ".money", (Money -= amount));
		this.saveConfig();
	}

	public void MoneyReset(Player player) {
		Player p = player;
		int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
		Money = -1;
		this.getConfig().set("ranks." + p.getUniqueId().toString() + ".money", Money);
		this.saveConfig();
	}

	private boolean disablePlugin(String pluginName) {
		Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
		if (plugin != null) {
			this.getServer().getPluginManager().disablePlugin(plugin);
			return true;
		}
		return false;
	}

	@EventHandler
	public void Shop0(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Player p = e.getPlayer();
		if (this.survival.contains(p) || this.creative.contains(p)) {
			return;
		}
		PlayerInventory pi = p.getInventory();
		int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("[Kit]")) {
				if (s.getLine(2).equalsIgnoreCase("Starter")) {
					ItemStack HELMET = MaterialManager.Create(Material.IRON_HELMET, ChatColor.GREEN + "Starter Helmet", 1, 0);
					ItemStack CHESTPLATE = MaterialManager.Create(Material.IRON_CHESTPLATE, ChatColor.GREEN + "Starter Chestplate", 1, 0);
					ItemStack LEGGINGS = MaterialManager.Create(Material.IRON_LEGGINGS, ChatColor.GREEN + "Starter Leggings", 1, 0);
					ItemStack BOOTS = MaterialManager.Create(Material.IRON_BOOTS, ChatColor.GREEN + "Starter Boots", 1, 0);
					ItemStack SWORD = MaterialManager.Create(Material.IRON_SWORD, ChatColor.GREEN + "Starter Sword", 1, 0);
					ItemStack BOW = MaterialManager.Create(Material.BOW, ChatColor.GREEN + "Starter Bow", 1, 0);
					ItemStack STEAK = MaterialManager.Create(Material.COOKED_BEEF, ChatColor.GREEN + "Starter Beef", 8, 0);
					ItemStack ARROW = MaterialManager.Create(Material.ARROW, ChatColor.GREEN + "Starter Arrow", 64, 0);
					p.getInventory().addItem(new ItemStack[]{HELMET});
					p.getInventory().addItem(new ItemStack[]{CHESTPLATE});
					p.getInventory().addItem(new ItemStack[]{LEGGINGS});
					p.getInventory().addItem(new ItemStack[]{BOOTS});
					p.getInventory().addItem(new ItemStack[]{SWORD});
					p.getInventory().addItem(new ItemStack[]{BOW});
					p.getInventory().addItem(new ItemStack[]{STEAK});
					p.getInventory().addItem(new ItemStack[]{ARROW});
					p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "Starter" + ChatColor.GREEN + "!");
					return;
				}
				if (s.getLine(2).equalsIgnoreCase("Potions")) {
					if (this.potions_cooldown.contains(p)) {
						p.sendMessage(ChatColor.RED + "You must wait " + ChatColor.GOLD + "5" + ChatColor.RED + " minutes before using this kit again!");
						return;
					}
					ItemStack STRENGTH = MaterialManager.Create(Material.POTION, null, 1, 8201);
					ItemStack SPEED = MaterialManager.Create(Material.POTION, null, 1, 8194);
					ItemStack FIRE = MaterialManager.Create(Material.POTION, null, 1, 8227);
					ItemStack REGEN = MaterialManager.Create(Material.POTION, null, 1, 8257);
					p.getInventory().addItem(new ItemStack[]{STRENGTH});
					p.getInventory().addItem(new ItemStack[]{SPEED});
					p.getInventory().addItem(new ItemStack[]{FIRE});
					p.getInventory().addItem(new ItemStack[]{REGEN});
					p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "Potions" + ChatColor.GREEN + "!");
					final Player pf = p;
					this.potions_cooldown.add(pf);
					this.getServer().getScheduler().scheduleAsyncDelayedTask((Plugin) this, new Runnable() {

						@Override
						public void run() {
							if (SkorrloreGaming.this.teampvp.contains(pf)) {
								pf.sendMessage(ChatColor.RED + "You can now use the kit " + ChatColor.GOLD + "Potions" + ChatColor.RED + " again.");
							}
							SkorrloreGaming.this.potions_cooldown.remove(pf);
						}
					}, 6000L);
					return;
				}
				if (s.getLine(2).equalsIgnoreCase("Donator")) {
					this.Kit(p);
				}
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Protection I")) {
				if (Money >= 14) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Protection I");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Minecart")) {
				if (Money >= 24) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.MINECART, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Minecart");
					this.MoneyTake(p, 25);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Lapis Lazuli")) {
				if (Money >= 1) {
					Dye l = new Dye();
					l.setColor(DyeColor.BLUE);
					ItemStack lapis = l.toItemStack();
					pi.addItem(new ItemStack[]{lapis});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Lapis Lazuli");
					this.MoneyTake(p, 2);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Protection II")) {
				if (Money >= 29) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Protection II");
					this.MoneyTake(p, 30);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Protection III")) {
				if (Money >= 44) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Protection III");
					this.MoneyTake(p, 45);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Protection IV")) {
				if (Money >= 59) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Protection IV");
					this.MoneyTake(p, 60);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Protection V")) {
				if (Money >= 74) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 5, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Protection V");
					this.MoneyTake(p, 75);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Protection VI")) {
				if (Money >= 89) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 6, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Protection VI");
					this.MoneyTake(p, 90);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Fire Aspect I")) {
				if (Money >= 24) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.FIRE_ASPECT, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Fire Aspect I");
					this.MoneyTake(p, 25);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Fire Aspect II")) {
				if (Money >= 34) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Fire Aspect II");
					this.MoneyTake(p, 35);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Sharpness I")) {
				if (Money >= 14) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Sharpness I");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Sharpness II")) {
				if (Money >= 29) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Sharpness II");
					this.MoneyTake(p, 30);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Sharpness III")) {
				if (Money >= 44) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Sharpness III");
					this.MoneyTake(p, 45);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Sharpness IV")) {
				if (Money >= 59) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Sharpness IV");
					this.MoneyTake(p, 60);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Sharpness V")) {
				if (Money >= 74) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Sharpness V");
					this.MoneyTake(p, 75);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Unbreaking III")) {
				if (Money >= 24) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DURABILITY, 3, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Unbreaking III");
					this.MoneyTake(p, 25);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Unbreaking IV")) {
				if (Money >= 49) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DURABILITY, 4, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Unbreaking IV");
					this.MoneyTake(p, 50);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Power I")) {
				if (Money >= 14) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Power I");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Power II")) {
				if (Money >= 29) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_DAMAGE, 2, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Power II");
					this.MoneyTake(p, 30);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Power III")) {
				if (Money >= 44) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Power III");
					this.MoneyTake(p, 45);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Power IV")) {
				if (Money >= 59) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_DAMAGE, 4, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Power IV");
					this.MoneyTake(p, 60);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Power V")) {
				if (Money >= 74) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_DAMAGE, 5, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Power V");
					this.MoneyTake(p, 75);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Power VI")) {
				if (Money >= 89) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_DAMAGE, 6, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Power VI");
					this.MoneyTake(p, 90);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Punch I")) {
				if (Money >= 14) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Punch I");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Punch II")) {
				if (Money >= 29) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_KNOCKBACK, 2, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Punch II");
					this.MoneyTake(p, 30);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Infinity I")) {
				if (Money >= 14) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Infinity I");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Flame I")) {
				if (Money >= 14) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_FIRE, 1, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Flame I");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Flame II")) {
				if (Money >= 29) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.ARROW_FIRE, 2, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Flame II");
					this.MoneyTake(p, 30);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Sharpness VI")) {
				if (Money >= 89) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Sharpness VI");
					this.MoneyTake(p, 90);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Fire Aspect III")) {
				if (Money >= 44) {
					ItemStack im = p.getInventory().getItemInHand();
					ItemMeta ima = im.getItemMeta();
					ima.addEnchant(Enchantment.FIRE_ASPECT, 3, true);
					im.setItemMeta(ima);
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Fire Aspect III");
					this.MoneyTake(p, 45);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Steak")) {
				if (Money >= 0) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Steak");
					this.MoneyTake(p, 1);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Cooked Fish")) {
				if (Money >= 0) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_FISH, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Cooked Fish");
					this.MoneyTake(p, 1);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Cooked Porkchop")) {
				if (Money >= 0) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.GRILLED_PORK, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Cooked Porkchop");
					this.MoneyTake(p, 1);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Fishing Rod")) {
				if (Money >= 24) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.FISHING_ROD, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Fishing Rod");
					this.MoneyTake(p, 25);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Diamond Chest")) {
				if (Money >= 19) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_CHESTPLATE, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond Chestplate");
					this.MoneyTake(p, 20);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(0).equalsIgnoreCase("[Sell]")) {
				String sa = s.getLine(3);
				int value = Integer.parseInt(sa);
				String raw = s.getLine(2);
				String material = "Material." + s.getLine(2);
				int mp = Integer.parseInt("sv");
				Material m = Material.getMaterial(material);
				ItemStack item = new ItemStack(m, value);
				if (!p.getInventory().contains(item)) {
					p.sendMessage(ChatColor.RED + "You do not have enough " + ChatColor.GOLD + raw + ChatColor.RED + "!");
					return;
				}
				p.getInventory().removeItem(new ItemStack[]{item});
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".money", (Money += mp));
				this.saveConfig();
				p.sendMessage(ChatColor.GREEN + "Transition complete! You have been given " + ChatColor.GOLD + Money + ChatColor.GREEN + " dollars!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Diamond Pants")) {
				if (Money >= 19) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_LEGGINGS, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond Leggings");
					this.MoneyTake(p, 20);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Bow")) {
				if (Money >= 14) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Bow");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Diamond Boots")) {
				if (Money >= 14) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond Boots");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Diamond Helmet")) {
				if (Money >= 14) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond Helmet");
					this.MoneyTake(p, 15);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Diamond Sword")) {
				if (Money >= 9) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond Sword");
					this.MoneyTake(p, 10);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Diamond")) {
				if (Money >= 4) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond");
					this.MoneyTake(p, 5);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Ender Pearl")) {
				if (Money >= 19) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.ENDER_PEARL, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Ender Pearl");
					this.MoneyTake(p, 20);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("DIAMOND_AXE")) {
				if (Money >= 9) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_AXE, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Diamond Axe");
					this.MoneyTake(p, 10);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
			if (s.getLine(1).equalsIgnoreCase("Golden Apple")) {
				if (Money >= 4) {
					pi.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 1)});
					p.sendMessage(ChatColor.GREEN + "You have successfully purchased " + ChatColor.GOLD + "Golden Apple");
					this.MoneyTake(p, 5);
					return;
				}
				p.sendMessage(ChatColor.RED + "You do not have enough money to purchase this item!");
				return;
			}
		}
	}

	public void ManagerMain(Player player) {
		Player p = player;
		this.teampvpJoin(p);
		p.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "Welcome to TeamPvP, " + ChatColor.GOLD + this.getNickname(p) + ChatColor.GOLD + "!");
		p.sendMessage(String.valueOf(L216) + "If you want to ever buy a rank, Use /store!");
	}

	@EventHandler
	public void AFK(PlayerPickupItemEvent e) {
		if (this.afk.contains(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Material type;
		Player p = e.getPlayer();
		if (this.creative.contains(p)) {
			type = e.getBlockPlaced().getType();
			if (type == Material.ENDER_CHEST) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place chests in survival!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place a ender-chest in creative!");
				return;
			}
			if (type == Material.DISPENSER) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place dispenser in creative!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place a dispenser in survival!");
				return;
			}
			if (type == Material.DROPPER) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place dropper in creative!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place a dropper in survival!");
				return;
			}
			if (type == Material.COMMAND) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place command in creative!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place a command in survival!");
				return;
			}
		}
		if (this.survival.contains(p)) {
			type = e.getBlockPlaced().getType();
			if (type == Material.ENDER_CHEST) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place chests in survival!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place a ender-chest in survival!");
				return;
			}
			if (type == Material.BEDROCK) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place bedrock in survival!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place bedrock in survival!");
				return;
			}
			if (type == Material.TNT) {
				e.getBlock().setType(Material.AIR);
				p.sendMessage(ChatColor.RED + "You cannot place tnt in survival!");
				Bukkit.getConsoleSender().sendMessage(String.valueOf(p.getName()) + " tried to place a tnt in survival!");
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		boolean type = true;
		if (!type) {
			p.sendMessage(String.valueOf(L216) + ChatColor.AQUA + "Also check out 'skorrloregaming-mcs.serv.nu'!");
			p.sendMessage(String.valueOf(L216) + ChatColor.AQUA + "This server uses the plugin SkorrloreGaming!");
		}
		this.hub.add(p);
		if (!p.hasPlayedBefore()) {
			this.getConfig().set("level." + p.getUniqueId().toString(), 0);
			this.getConfig().set("nickname." + p.getUniqueId().toString(), p.getName());
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".nickname", false);
			this.saveConfig();
		}
		String nicknamex = p.getName();
		try {
			this.getNickname(p);
			nicknamex = this.getNickname(p);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			boolean changed = this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".nickname");
			if (this.getNickname(p) != p.getName() && !changed) {
				this.getConfig().set("nickname." + p.getUniqueId().toString(), p.getName());
				this.saveConfig();
			}
		} catch (Exception ex) {
			p.sendMessage(String.valueOf(L216) + ChatColor.DARK_RED + "Failed to verify nickname changed!");
			return;
		}
		p.sendMessage(ChatColor.GRAY + "------------------------------------>");
		p.sendMessage(this.GOLD + "Welcome back, " + ChatColor.GREEN + nicknamex);
		p.sendMessage(this.AQUA + "Rules: " + ChatColor.GOLD + "/rules");
		p.sendMessage(this.AQUA + "Vote: " + ChatColor.GOLD + "/vote");
		p.sendMessage(this.AQUA + "Store: " + ChatColor.GOLD + "/store");
		p.sendMessage(ChatColor.GRAY + "------------------------------------>");
		p.addAttachment((Plugin) this, "bukkit.command.me", false);
		p.addAttachment((Plugin) this, "bukkit.command.kill", false);
		p.addAttachment((Plugin) this, "minecraft.command.kill", false);
		Player player = e.getPlayer();
		e.setJoinMessage(ChatColor.GOLD + "[" + ChatColor.GREEN + "+" + ChatColor.GOLD + "] " + ChatColor.AQUA + nicknamex);
		this.hubTeleportOnJoin(player);
		if (!e.getPlayer().hasPlayedBefore()) {
			this.getConfig().set("ranks." + player.getUniqueId().toString() + ".homes", 0);
			this.getConfig().set("ranks." + player.getUniqueId().toString() + ".kills", 0);
			this.getConfig().set("ranks." + player.getUniqueId().toString() + ".money", 150);
			this.getConfig().set("ranks." + p.getUniqueId().toString() + ".rank", "guest");
			this.getConfig().set("mute." + p.getUniqueId().toString(), false);
			this.saveConfig();
			e.getPlayer().setDisplayName(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Guest" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GREEN + "Welcome to the server, " + ChatColor.GOLD + e.getPlayer().getName()));
		}
		p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + nicknamex + ChatColor.WHITE);
		this.loadPermissions(p);
		ItemStack compass = new ItemStack(Material.COMPASS, 1);
		ItemMeta compassMeta = compass.getItemMeta();
		compassMeta.setDisplayName(ChatColor.GREEN + "Server Selector");
		compass.setItemMeta(compassMeta);
		p.getInventory().clear();
		p.getInventory().setHelmet(new ItemStack(Material.AIR));
		p.getInventory().setChestplate(new ItemStack(Material.AIR));
		p.getInventory().setLeggings(new ItemStack(Material.AIR));
		p.getInventory().setBoots(new ItemStack(Material.AIR));
		p.getInventory().addItem(new ItemStack[]{compass});
	}

	public Location getSpawnLocation(String data) {
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_" + data + ".world"));
		double x = this.settings.getData().getDouble("warps.map000_" + data + ".x");
		double y = this.settings.getData().getDouble("warps.map000_" + data + ".y");
		double z = this.settings.getData().getDouble("warps.map000_" + data + ".z");
		Location loc = new Location(w, x, y, z);
		return loc;
	}

	public void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 9, (ChatColor.DARK_GREEN + "Server Selector"));
		ItemStack survival = MaterialManager.Create(Material.DIAMOND_SWORD, ChatColor.GREEN + "Survival", 1, 0);
		ItemStack teampvp = MaterialManager.Create(Material.IRON_SWORD, ChatColor.GREEN + "TeamPvP", 1, 0);
		ItemStack creative = MaterialManager.Create(Material.GRASS, ChatColor.GREEN + "Creative", 1, 0);
		ItemStack skyfight = MaterialManager.Create(Material.BOW, ChatColor.GREEN + "SkyFight [Updated 2016]", 1, 0);
		inv.setItem(1, survival);
		inv.setItem(3, teampvp);
		inv.setItem(5, creative);
		inv.setItem(7, skyfight);
		p.openInventory(inv);
	}

	public void skyfightGUI(Player player) {
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 9, (ChatColor.DARK_GREEN + "SkyFight - Map Selector"));
		ItemStack map0 = MaterialManager.Create(Material.DIRT, ChatColor.GREEN + "Select - Map 1", 1, 0);
		ItemStack map1 = MaterialManager.Create(Material.COBBLESTONE, ChatColor.GREEN + "Select - Map 2", 1, 0);
		ItemStack map2 = MaterialManager.Create(Material.STONE, ChatColor.GREEN + "Select - Map 3", 1, 0);
		inv.setItem(2, map0);
		inv.setItem(4, map1);
		inv.setItem(6, map2);
		player.openInventory(inv);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Skyfight - Map Selector")) {
			Player p = (Player) e.getWhoClicked();
			e.setCancelled(true);
			if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()) {
				p.closeInventory();
				return;
			}
			switch (e.getCurrentItem().getType()) {
				case DIRT: {
					this.toSkywars(p, 1);
					break;
				}
				case COBBLESTONE: {
					this.toSkywars(p, 2);
					break;
				}
				case STONE: {
					this.toSkywars(p, 3);
					break;
				}
				default: {
					p.closeInventory();
				}
			}
			return;
		}
		if (!ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Server Selector")) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		e.setCancelled(true);
		if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || !e.getCurrentItem().hasItemMeta()) {
			p.closeInventory();
			return;
		}
		switch (e.getCurrentItem().getType()) {
			case DIAMOND_SWORD: {
				this.toSurvival(p);
				break;
			}
			case GRASS: {
				this.toCreative(p);
				break;
			}
			case IRON_SWORD: {
				this.toTeamPvP(p);
				break;
			}
			case BOW: {
				p.closeInventory();
				this.skyfightGUI(p);
				break;
			}
			default: {
				p.closeInventory();
			}
		}
	}

	public void teampvpJoin(Player p) {
		p.sendMessage(ChatColor.GRAY + "----------------------------------------------->");
		p.sendMessage(ChatColor.GREEN + "Welcome to the server, " + ChatColor.GOLD + this.getNickname(p) + ChatColor.GREEN + "!");
		p.sendMessage(ChatColor.GREEN + "Rules: " + ChatColor.GOLD + "/rules");
		p.sendMessage(ChatColor.GREEN + "Store: " + ChatColor.GOLD + "/store");
		p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + "/money");
		p.sendMessage(ChatColor.GREEN + "Vote: " + ChatColor.GOLD + "/vote");
		p.sendMessage(ChatColor.GRAY + "----------------------------------------------->");
	}

	public void toSkywars(Player p, int mapID) {
		if (this.survival.contains(p)) {
			try {
				Storage.saveInventory(p, "survival");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.teampvp.contains(p)) {
			try {
				Storage.saveInventory(p, "teampvp");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (this.creative.contains(p)) {
			try {
				Storage.saveInventory(p, "creative");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (mapID == 1) {
			if (!this.skyfight1.contains(p)) {
				this.skyfight1.add(p);
			}
			if (this.skyfight2.contains(p)) {
				this.skyfight2.remove(p);
			}
			if (this.skyfight3.contains(p)) {
				this.skyfight3.remove(p);
			}
		} else if (mapID == 2) {
			if (this.skyfight1.contains(p)) {
				this.skyfight1.remove(p);
			}
			if (!this.skyfight2.contains(p)) {
				this.skyfight2.add(p);
			}
			if (this.skyfight3.contains(p)) {
				this.skyfight3.remove(p);
			}
		} else if (mapID == 3) {
			if (this.skyfight1.contains(p)) {
				this.skyfight1.remove(p);
			}
			if (this.skyfight2.contains(p)) {
				this.skyfight2.remove(p);
			}
			if (!this.skyfight3.contains(p)) {
				this.skyfight3.add(p);
			}
		} else {
			return;
		}
		Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has logged into SkyFight onto map " + mapID + "!"));
		this.resetPlayerSkyfightLocation(p);
	}

	@EventHandler
	public void ServerSelecterOpen(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() != Action.RIGHT_CLICK_AIR) {
			return;
		}
		if (e.getItem().getType() != Material.COMPASS) {
			return;
		}
		if (!this.hub.contains(e.getPlayer()) || this.survival.contains(p) || this.teampvp.contains(p) || this.creative.contains(p)) {
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return;
		}
		this.openGUI(e.getPlayer());
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase("teampvp")) {
			if (!this.teampvp.contains(p)) {
				this.teampvp.add(p);
				this.ManagerMain(p);
				p.addAttachment((Plugin) this, "alphachest.chest", true);
				p.addAttachment((Plugin) this, "alphachest.keepOnDeath", true);
			}
			if (this.survival.contains(p)) {
				this.survival.remove(p);
			}
			if (this.creative.contains(p)) {
				this.creative.remove(p);
			}
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
			if (this.skyfight.contains(p)) {
				this.skyfight.remove(p);
			}
		} else if (e.getPlayer().getWorld().getName().equalsIgnoreCase("survival")) {
			if (this.teampvp.contains(p)) {
				this.teampvp.remove(p);
			}
			if (!this.survival.contains(p)) {
				this.survival.add(p);
			}
			if (this.creative.contains(p)) {
				this.creative.remove(p);
			}
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
			if (this.skyfight.contains(p)) {
				this.skyfight.remove(p);
			}
		} else if (e.getPlayer().getWorld().getName().equalsIgnoreCase("plotworld")) {
			if (this.teampvp.contains(p)) {
				this.teampvp.remove(p);
			}
			if (this.survival.contains(p)) {
				this.survival.remove(p);
			}
			if (!this.creative.contains(p)) {
				this.creative.add(p);
			}
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
			if (this.skyfight.contains(p)) {
				this.skyfight.remove(p);
			}
		} else if (e.getPlayer().getWorld().getName().equalsIgnoreCase("skyfight")) {
			if (this.teampvp.contains(p)) {
				this.teampvp.remove(p);
			}
			if (this.survival.contains(p)) {
				this.survival.remove(p);
			}
			if (!this.skyfight.contains(p)) {
				this.skyfight.add(p);
			}
			if (this.creative.contains(p)) {
				this.creative.remove(p);
			}
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
		} else if (e.getPlayer().getWorld().getName().equalsIgnoreCase("skorrloregaming")) {
			if (this.teampvp.contains(p)) {
				this.teampvp.remove(p);
			}
			if (this.survival.contains(p)) {
				this.survival.remove(p);
			}
			if (this.creative.contains(p)) {
				this.creative.remove(p);
			}
			if (!this.hub.contains(p)) {
				this.hub.add(p);
			}
			if (this.skyfight.contains(p)) {
				this.skyfight.remove(p);
			}
		}
		if (this.skyfight.contains(e.getPlayer())) {
			if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
				e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
				e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.1, e.getPlayer().getVelocity().getZ()));
			}
			e.getPlayer().setFireTicks(0);
			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
				this.resetPlayerSkyfightLocation(p);
				return;
			}
			return;
		}
		if (this.hub.contains(e.getPlayer())) {
			ItemStack ss = new ItemStack(Material.COMPASS, 1);
			ItemMeta ssm = ss.getItemMeta();
			ssm.setDisplayName(ChatColor.GREEN + "Server Selector");
			ss.setItemMeta(ssm);
			p.getInventory().clear();
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			p.getInventory().setChestplate(new ItemStack(Material.AIR));
			p.getInventory().setLeggings(new ItemStack(Material.AIR));
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			p.getInventory().addItem(new ItemStack[]{ss});
			if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
				e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
				e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.1, e.getPlayer().getVelocity().getZ()));
			}
			p.setFoodLevel(20);
			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_ORE) {
				this.toCreative(p);
				return;
			}
			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.COAL_ORE) {
				this.toSurvival(p);
				return;
			}
			if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_ORE) {
				this.toTeamPvP(p);
				return;
			}
			return;
		}
		if (!this.creative.contains(e.getPlayer()) && e.getPlayer().getGameMode() == GameMode.CREATIVE && !this.unlockedGamemode.contains(p)) {
			e.getPlayer().setGameMode(GameMode.SURVIVAL);
		}
		if (this.creative.contains(e.getPlayer())) {
			if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
				e.getPlayer().setGameMode(GameMode.CREATIVE);
			}
			if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
				e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
				e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.1, e.getPlayer().getVelocity().getZ()));
			}
			if (!e.getPlayer().hasPermission("plotme.use.claim")) {
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.claim", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.auto", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.home", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.list", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.info", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.biome", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.clear", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.dispose", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.done", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.deny", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.undeny", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.middle", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.add", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.remove", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.protect", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.admin.home.other", true);
				e.getPlayer().addAttachment((Plugin) this, "plotme.limit.1", true);
			}
		} else {
			if (e.getPlayer().hasPermission("plotme.use.claim")) {
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.claim", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.auto", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.home", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.list", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.info", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.biome", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.clear", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.dispose", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.done", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.deny", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.undeny", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.middle", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.add", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.remove", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.use.protect", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.admin.home.other", false);
				e.getPlayer().addAttachment((Plugin) this, "plotme.limit.1", true);
			}
			if (this.teampvp.contains(e.getPlayer())) {
				return;
			}
		}
	}

	private boolean enablePlugin(String pluginName) {
		Plugin plugin = this.getServer().getPluginManager().getPlugin(pluginName);
		if (plugin != null) {
			this.getServer().getPluginManager().enablePlugin(plugin);
			return true;
		}
		return false;
	}

	@EventHandler
	public void Skyfight(PlayerRespawnEvent e) {
		if (this.teampvp.contains(e.getPlayer())) {
			this.teampvpRespawn.add(e.getPlayer());
			return;
		}
	}

	public void hubTeleportOnJoin(Player p) {
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
		double x = this.settings.getData().getDouble("warps.kb0000009.x");
		double y = this.settings.getData().getDouble("warps.kb0000009.y");
		double z = this.settings.getData().getDouble("warps.kb0000009.z");
		p.teleport(new Location(w, x, y, z));
	}

	@EventHandler
	public void NoFall(EntityDamageEvent e) {
		if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
			e.setCancelled(true);
			return;
		}
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) e.getEntity();
		if (e.getCause() == EntityDamageEvent.DamageCause.FIRE || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK && this.skyfight.contains(player)) {
			player.setFireTicks(0);
			e.setCancelled(true);
		}
		if (e.getCause() == EntityDamageEvent.DamageCause.LAVA && this.skyfight.contains(player)) {
			this.resetPlayerSkyfightLocation(player);
			e.setCancelled(true);
			String killer = SkyFight.lastAttacker.get(player);
			String killerNickname = "lava";
			if (killer == "" || killer == null || killer == "null" || killer == player.getName()) {
				killer = "lava";
			} else {
				killerNickname = this.getNickname(this.getServer().getPlayer(killer));
				int multiplier = SkyFight.getPlayerTokens(this.getServer().getPlayer(killer)) / 150 + 1 + SkyFight.getKillstreak(this.getServer().getPlayer(killer));
				SkyFight.handleTokenAdd(this.getServer().getPlayer(killer), multiplier);
				this.getServer().getPlayer(killer).sendMessage(ChatColor.GREEN + "You have earned " + ChatColor.GOLD + multiplier + ChatColor.GREEN + " tokens!");
				SkyFight.killstreakHandler(this.getServer().getPlayer(killer));
			}
			for (Player p : this.skyfight) {
				p.sendMessage(String.valueOf(L216) + ChatColor.GOLD + this.getNickname(player) + ChatColor.AQUA + " has been killed by " + ChatColor.GOLD + killerNickname + ChatColor.AQUA + " in SkyFight on map " + this.getCurrentSkyfightMap(player));
			}
			player.getWorld().playSound(player.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
			player.setFireTicks(0);
			SkyFight.removeLastAttacker(player);
			SkyFight.handleTokenRemove(player, 1);
			player.sendMessage(ChatColor.RED + "You have lost " + ChatColor.GOLD + "1" + ChatColor.RED + " tokens!");
		}
	}

	public int getCurrentSkyfightMap(Player p) {
		if (!this.skyfight.contains(p)) {
			return 0;
		}
		if (this.skyfight1.contains(p)) {
			return 1;
		}
		if (this.skyfight2.contains(p)) {
			return 2;
		}
		if (this.skyfight3.contains(p)) {
			return 3;
		}
		return 0;
	}

	public void resetPlayerSkyfightLocation(Player p) {
		try {
			World world;
			double yer;
			double zer;
			double xer;
			Random random = new Random();
			int random_spawn = random.nextInt(5);
			if (this.skyfight1.contains(p)) {
				world = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_skyfight_1_" + random_spawn + ".world"));
				xer = this.settings.getData().getDouble("warps.map000_skyfight_1_" + random_spawn + ".x");
				if (this.hub.contains(p)) {
					this.hub.remove(p);
				}
				if (!this.skyfight.contains(p)) {
					this.skyfight.add(p);
				}
				yer = this.settings.getData().getDouble("warps.map000_skyfight_1_" + random_spawn + ".y");
				zer = this.settings.getData().getDouble("warps.map000_skyfight_1_" + random_spawn + ".z");
				p.teleport(new Location(world, xer, yer, zer));
			} else if (this.skyfight2.contains(p)) {
				world = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_skyfight_2_" + random_spawn + ".world"));
				xer = this.settings.getData().getDouble("warps.map000_skyfight_2_" + random_spawn + ".x");
				if (this.hub.contains(p)) {
					this.hub.remove(p);
				}
				if (!this.skyfight.contains(p)) {
					this.skyfight.add(p);
				}
				yer = this.settings.getData().getDouble("warps.map000_skyfight_2_" + random_spawn + ".y");
				zer = this.settings.getData().getDouble("warps.map000_skyfight_2_" + random_spawn + ".z");
				p.teleport(new Location(world, xer, yer, zer));
			} else if (this.skyfight3.contains(p)) {
				world = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_skyfight_3_" + random_spawn + ".world"));
				xer = this.settings.getData().getDouble("warps.map000_skyfight_3_" + random_spawn + ".x");
				if (this.hub.contains(p)) {
					this.hub.remove(p);
				}
				if (!this.skyfight.contains(p)) {
					this.skyfight.add(p);
				}
				yer = this.settings.getData().getDouble("warps.map000_skyfight_3_" + random_spawn + ".y");
				zer = this.settings.getData().getDouble("warps.map000_skyfight_3_" + random_spawn + ".z");
				p.teleport(new Location(world, xer, yer, zer));
			} else {
				return;
			}
			p.setFireTicks(0);
			p.setHealth(20.0);
			p.getInventory().clear();
			int level = SkyFight.getPlayerTokens(p) / 150 + 1;
			if (level == 1) {
				ItemStack sword = MaterialManager.Create(Material.IRON_SWORD, ChatColor.GOLD + "SkyFight Blade", 1, 0);
				ItemStack bow = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "Lousy SkyFight Crossbow", 1, 0);
				ItemStack bow1 = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "SkyFight Teleportation Bow", 1, 0);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_KNOCKBACK, 10, true);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_INFINITE, 1, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.DIG_SPEED, 100, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.ARROW_INFINITE, 1, true);
				sword = MaterialManager.addEnchant(sword, Enchantment.KNOCKBACK, 5, true);
				ItemStack arrow = MaterialManager.Create(Material.ARROW, ChatColor.GOLD + "Lousy SkyFight Arrow", 1, 0);
				ItemStack helmet = MaterialManager.Create(Material.IRON_HELMET, ChatColor.GOLD + "Lousy SkyFight Helmet", 1, 0);
				ItemStack chestplate = MaterialManager.Create(Material.LEATHER_CHESTPLATE, ChatColor.GOLD + "Lousy SkyFight Chestpeice", 1, 0);
				ItemStack leggings = MaterialManager.Create(Material.LEATHER_LEGGINGS, ChatColor.GOLD + "Lousy SkyFight Pants", 1, 0);
				ItemStack boots = MaterialManager.Create(Material.IRON_BOOTS, ChatColor.GOLD + "Lousy SkyFight Boots", 1, 0);
				p.getInventory().addItem(new ItemStack[]{sword});
				p.getInventory().addItem(new ItemStack[]{bow});
				p.getInventory().addItem(new ItemStack[]{bow1});
				p.getInventory().addItem(new ItemStack[]{arrow});
				p.getInventory().setHelmet(helmet);
				p.getInventory().setChestplate(chestplate);
				p.getInventory().setLeggings(leggings);
				p.getInventory().setBoots(boots);
			} else if (level == 2) {
				ItemStack sword = MaterialManager.Create(Material.DIAMOND_SWORD, ChatColor.GOLD + "SkyFight Blade", 1, 0);
				ItemStack bow = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "Enhanced SkyFight Crossbow", 1, 0);
				ItemStack bow1 = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "SkyFight Teleportation Bow", 1, 0);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_KNOCKBACK, 10, true);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_INFINITE, 1, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.DIG_SPEED, 100, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.ARROW_INFINITE, 1, true);
				sword = MaterialManager.addEnchant(sword, Enchantment.KNOCKBACK, 7, true);
				ItemStack arrow = MaterialManager.Create(Material.ARROW, ChatColor.GOLD + "Enhanced SkyFight Arrow", 1, 0);
				ItemStack helmet = MaterialManager.Create(Material.DIAMOND_HELMET, ChatColor.GOLD + "Enhanced SkyFight Helmet", 1, 0);
				ItemStack chestplate = MaterialManager.Create(Material.IRON_CHESTPLATE, ChatColor.GOLD + "Enhanced SkyFight Chestpeice", 1, 0);
				ItemStack leggings = MaterialManager.Create(Material.IRON_LEGGINGS, ChatColor.GOLD + "Enhanced SkyFight Pants", 1, 0);
				ItemStack boots = MaterialManager.Create(Material.DIAMOND_BOOTS, ChatColor.GOLD + "Enhanced SkyFight Boots", 1, 0);
				p.getInventory().addItem(new ItemStack[]{sword});
				p.getInventory().addItem(new ItemStack[]{bow});
				p.getInventory().addItem(new ItemStack[]{bow1});
				p.getInventory().addItem(new ItemStack[]{arrow});
				p.getInventory().setHelmet(helmet);
				p.getInventory().setChestplate(chestplate);
				p.getInventory().setLeggings(leggings);
				p.getInventory().setBoots(boots);
			} else if (level == 3) {
				ItemStack sword = MaterialManager.Create(Material.DIAMOND_SWORD, ChatColor.GOLD + "SkyFight Blade", 1, 0);
				ItemStack bow = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "Powerful SkyFight Crossbow", 1, 0);
				ItemStack bow1 = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "SkyFight Teleportation Bow", 1, 0);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_KNOCKBACK, 15, true);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_INFINITE, 1, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.DIG_SPEED, 100, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.ARROW_INFINITE, 1, true);
				sword = MaterialManager.addEnchant(sword, Enchantment.KNOCKBACK, 9, true);
				ItemStack arrow = MaterialManager.Create(Material.ARROW, ChatColor.GOLD + "Powerful SkyFight Arrow", 1, 0);
				ItemStack helmet = MaterialManager.Create(Material.DIAMOND_HELMET, ChatColor.GOLD + "Powerful SkyFight Helmet", 1, 0);
				ItemStack chestplate = MaterialManager.Create(Material.IRON_CHESTPLATE, ChatColor.GOLD + "Powerful SkyFight Chestpeice", 1, 0);
				ItemStack leggings = MaterialManager.Create(Material.DIAMOND_LEGGINGS, ChatColor.GOLD + "Powerful SkyFight Pants", 1, 0);
				ItemStack boots = MaterialManager.Create(Material.DIAMOND_BOOTS, ChatColor.GOLD + "Powerful SkyFight Boots", 1, 0);
				p.getInventory().addItem(new ItemStack[]{sword});
				p.getInventory().addItem(new ItemStack[]{bow});
				p.getInventory().addItem(new ItemStack[]{bow1});
				p.getInventory().addItem(new ItemStack[]{arrow});
				p.getInventory().setHelmet(helmet);
				p.getInventory().setChestplate(chestplate);
				p.getInventory().setLeggings(leggings);
				p.getInventory().setBoots(boots);
			} else if (level >= 4) {
				ItemStack sword = MaterialManager.Create(Material.DIAMOND_SWORD, ChatColor.GOLD + "SkyFight Blade", 1, 0);
				ItemStack bow = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "Extreme SkyFight Crossbow", 1, 0);
				ItemStack bow1 = MaterialManager.Create(Material.BOW, ChatColor.GOLD + "SkyFight Teleportation Bow", 1, 0);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_KNOCKBACK, 20, true);
				bow = MaterialManager.addEnchant(bow, Enchantment.ARROW_INFINITE, 1, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.DIG_SPEED, 100, true);
				bow1 = MaterialManager.addEnchant(bow1, Enchantment.ARROW_INFINITE, 1, true);
				sword = MaterialManager.addEnchant(sword, Enchantment.KNOCKBACK, 15, true);
				ItemStack arrow = MaterialManager.Create(Material.ARROW, ChatColor.GOLD + "Extreme SkyFight Arrow", 1, 0);
				ItemStack helmet = MaterialManager.Create(Material.DIAMOND_HELMET, ChatColor.GOLD + "Extreme SkyFight Helmet", 1, 0);
				ItemStack chestplate = MaterialManager.Create(Material.DIAMOND_CHESTPLATE, ChatColor.GOLD + "Extreme SkyFight Chestpeice", 1, 0);
				ItemStack leggings = MaterialManager.Create(Material.DIAMOND_LEGGINGS, ChatColor.GOLD + "Extreme SkyFight Pants", 1, 0);
				ItemStack boots = MaterialManager.Create(Material.DIAMOND_BOOTS, ChatColor.GOLD + "Extreme SkyFight Boots", 1, 0);
				p.getInventory().addItem(new ItemStack[]{sword});
				p.getInventory().addItem(new ItemStack[]{bow});
				p.getInventory().addItem(new ItemStack[]{bow1});
				p.getInventory().addItem(new ItemStack[]{arrow});
				p.getInventory().setHelmet(helmet);
				p.getInventory().setChestplate(chestplate);
				p.getInventory().setLeggings(leggings);
				p.getInventory().setBoots(boots);
			}
		} catch (Exception ex) {
			if (this.skyfight1.contains(p)) {
				this.skyfight1.remove(p);
			}
			if (this.skyfight2.contains(p)) {
				this.skyfight2.remove(p);
			}
			if (this.skyfight3.contains(p)) {
				this.skyfight3.remove(p);
			}
			if (this.skyfight.contains(p)) {
				this.skyfight.remove(p);
			}
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has quit SkyFight!"));
			World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
			double x = this.settings.getData().getDouble("warps.kb0000009.x");
			double y = this.settings.getData().getDouble("warps.kb0000009.y");
			double z = this.settings.getData().getDouble("warps.kb0000009.z");
			p.teleport(new Location(w, x, y, z));
			p.sendMessage(String.valueOf(L216) + ChatColor.RED + "The specified map does not exist in our database!");
		}
	}

	public void Kit(Player player) {
		try {
			Player p = player;
			String rank = this.getRank(p);
			if (rank.equalsIgnoreCase("vip")) {
				ItemStack Sword = Item.Create(new ItemStack(Material.IRON_SWORD, 1), ChatColor.RED + "VIP Sword", Enchantment.DAMAGE_ALL, 4, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Bow = Item.Create(new ItemStack(Material.BOW, 1), ChatColor.RED + "VIP Bow", Enchantment.ARROW_DAMAGE, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Arrow = Item.Create(new ItemStack(Material.ARROW, 64), ChatColor.RED + "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
				ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), ChatColor.RED + "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
				ItemStack Helmet = Item.Create(new ItemStack(Material.IRON_HELMET, 1), ChatColor.RED + "VIP Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Chestplate = Item.Create(new ItemStack(Material.IRON_CHESTPLATE, 1), ChatColor.RED + "VIP Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Leggings = Item.Create(new ItemStack(Material.IRON_LEGGINGS, 1), ChatColor.RED + "VIP Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Boots = Item.Create(new ItemStack(Material.IRON_BOOTS, 1), ChatColor.RED + "VIP Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				p.getInventory().addItem(new ItemStack[]{Sword});
				p.getInventory().addItem(new ItemStack[]{Bow});
				p.getInventory().addItem(new ItemStack[]{Arrow});
				p.getInventory().addItem(new ItemStack[]{Steak});
				p.getInventory().addItem(new ItemStack[]{Helmet});
				p.getInventory().addItem(new ItemStack[]{Chestplate});
				p.getInventory().addItem(new ItemStack[]{Leggings});
				p.getInventory().addItem(new ItemStack[]{Boots});
				p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "VIP");
				return;
			}
			if (rank.equalsIgnoreCase("vipx")) {
				ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), ChatColor.RED + "VIP+ Sword", Enchantment.DAMAGE_ALL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Bow = Item.Create(new ItemStack(Material.BOW, 1), ChatColor.RED + "VIP+ Bow", Enchantment.ARROW_DAMAGE, 4, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Arrow = Item.Create(new ItemStack(Material.ARROW, 64), ChatColor.RED + "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
				ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), ChatColor.RED + "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
				ItemStack Helmet = Item.Create(new ItemStack(Material.IRON_HELMET, 1), ChatColor.RED + "VIP+ Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Chestplate = Item.Create(new ItemStack(Material.IRON_CHESTPLATE, 1), ChatColor.RED + "VIP+ Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Leggings = Item.Create(new ItemStack(Material.IRON_LEGGINGS, 1), ChatColor.RED + "VIP+ Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Boots = Item.Create(new ItemStack(Material.IRON_BOOTS, 1), ChatColor.RED + "VIP+ Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				p.getInventory().addItem(new ItemStack[]{Sword});
				p.getInventory().addItem(new ItemStack[]{Bow});
				p.getInventory().addItem(new ItemStack[]{Arrow});
				p.getInventory().addItem(new ItemStack[]{Steak});
				p.getInventory().addItem(new ItemStack[]{Helmet});
				p.getInventory().addItem(new ItemStack[]{Chestplate});
				p.getInventory().addItem(new ItemStack[]{Leggings});
				p.getInventory().addItem(new ItemStack[]{Boots});
				p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "VIP+");
				return;
			}
			if (rank.equalsIgnoreCase("mvp")) {
				ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), ChatColor.RED + "MVP Sword", Enchantment.DAMAGE_ALL, 4, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Bow = Item.Create(new ItemStack(Material.BOW, 1), ChatColor.RED + "MVP Bow", Enchantment.ARROW_DAMAGE, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Arrow = Item.Create(new ItemStack(Material.ARROW, 64), ChatColor.RED + "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
				ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), ChatColor.RED + "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
				ItemStack Helmet = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), ChatColor.RED + "MVP Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Chestplate = Item.Create(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), ChatColor.RED + "MVP Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Leggings = Item.Create(new ItemStack(Material.DIAMOND_LEGGINGS, 1), ChatColor.RED + "MVP Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Boots = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), ChatColor.RED + "MVP Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				p.getInventory().addItem(new ItemStack[]{Sword});
				p.getInventory().addItem(new ItemStack[]{Bow});
				p.getInventory().addItem(new ItemStack[]{Arrow});
				p.getInventory().addItem(new ItemStack[]{Steak});
				p.getInventory().addItem(new ItemStack[]{Helmet});
				p.getInventory().addItem(new ItemStack[]{Chestplate});
				p.getInventory().addItem(new ItemStack[]{Leggings});
				p.getInventory().addItem(new ItemStack[]{Boots});
				p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "MVP");
				return;
			}
			if (rank.equalsIgnoreCase("mvpx")) {
				ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), ChatColor.RED + "MVP+ Sword", Enchantment.DAMAGE_ALL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Bow = Item.Create(new ItemStack(Material.BOW, 1), ChatColor.RED + "MVP+ Bow", Enchantment.ARROW_DAMAGE, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Arrow = Item.Create(new ItemStack(Material.ARROW, 64), ChatColor.RED + "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
				ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), ChatColor.RED + "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
				ItemStack Helmet = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), ChatColor.RED + "MVP+ Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Chestplate = Item.Create(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), ChatColor.RED + "MVP+ Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Leggings = Item.Create(new ItemStack(Material.DIAMOND_LEGGINGS, 1), ChatColor.RED + "MVP+ Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Boots = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), ChatColor.RED + "MVP+ Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				p.getInventory().addItem(new ItemStack[]{Sword});
				p.getInventory().addItem(new ItemStack[]{Bow});
				p.getInventory().addItem(new ItemStack[]{Arrow});
				p.getInventory().addItem(new ItemStack[]{Steak});
				p.getInventory().addItem(new ItemStack[]{Helmet});
				p.getInventory().addItem(new ItemStack[]{Chestplate});
				p.getInventory().addItem(new ItemStack[]{Leggings});
				p.getInventory().addItem(new ItemStack[]{Boots});
				p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "MVP+");
				return;
			}
			if (rank.equalsIgnoreCase("youtube")) {
				ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), ChatColor.RED + "YouTube Sword", Enchantment.DAMAGE_ALL, 1, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Bow = Item.Create(new ItemStack(Material.BOW, 1), ChatColor.RED + "YouTube Bow", Enchantment.ARROW_DAMAGE, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Arrow = Item.Create(new ItemStack(Material.ARROW, 64), ChatColor.RED + "YouTube Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
				ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), ChatColor.RED + "YouTube Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
				ItemStack Helmet = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), ChatColor.RED + "YouTube Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 1, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Chestplate = Item.Create(new ItemStack(Material.IRON_CHESTPLATE, 1), ChatColor.RED + "YouTube Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 1, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Leggings = Item.Create(new ItemStack(Material.IRON_LEGGINGS, 1), ChatColor.RED + "YouTube Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 1, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				ItemStack Boots = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), ChatColor.RED + "YouTube Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 1, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
				p.getInventory().addItem(new ItemStack[]{Sword});
				p.getInventory().addItem(new ItemStack[]{Bow});
				p.getInventory().addItem(new ItemStack[]{Arrow});
				p.getInventory().addItem(new ItemStack[]{Steak});
				p.getInventory().addItem(new ItemStack[]{Helmet});
				p.getInventory().addItem(new ItemStack[]{Chestplate});
				p.getInventory().addItem(new ItemStack[]{Leggings});
				p.getInventory().addItem(new ItemStack[]{Boots});
				p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "YouTube");
				return;
			}
			p.sendMessage(String.valueOf(L216) + "You have not purchased a rank on this server!");
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
	}

	public void onPlayerDeath(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (!this.creative.contains(p)) {
			return;
		}
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
		double x = this.settings.getData().getDouble("warps.map000_rpg.x");
		double y = this.settings.getData().getDouble("warps.map000_rpg.y");
		double z = this.settings.getData().getDouble("warps.map000_rpg.z");
		p.teleport(new Location(w, x, y, z));
	}

	private void ttg(Player p) {
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
		double x = this.settings.getData().getDouble("warps.map000_rpg.x");
		double y = this.settings.getData().getDouble("warps.map000_rpg.y");
		double z = this.settings.getData().getDouble("warps.map000_rpg.z");
		p.teleport(new Location(w, x, y, z));
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		String motd = this.getConfig().getString("motd.system");
		motd = motd.replaceAll("&", "\u00a7");
		e.setMaxPlayers(5000);
		if (!this.getConfig().getBoolean("settings.motd.custom")) {
			e.setMotd(ChatColor.GRAY + NAME + " | " + ChatColor.AQUA + "Minecraft Server");
			return;
		}
		boolean type = true;
		if (!type) {
			e.setMotd(ChatColor.GRAY + NAME + " | " + motd);
		} else {
			e.setMotd(motd);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (this.teampvp.contains(p)) {
			try {
				Storage.saveInventory(p, "teampvp");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (this.survival.contains(p)) {
			try {
				Storage.saveInventory(p, "survival");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (this.creative.contains(p)) {
			try {
				Storage.saveInventory(p, "creative");
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.world", p.getLocation().getWorld().getName());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.x", p.getLocation().getX());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.y", p.getLocation().getY());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.z", p.getLocation().getZ());
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.yaw", Float.valueOf(p.getLocation().getYaw()));
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.pitch", Float.valueOf(p.getLocation().getPitch()));
				this.saveConfig();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (this.creative.contains(p)) {
			p.getInventory().clear();
		}
		e.setQuitMessage(ChatColor.GOLD + "[" + ChatColor.RED + "-" + ChatColor.GOLD + "] " + ChatColor.AQUA + this.getNickname(p));
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		try {
			Player k = (Player) e.getDamager();
			boolean pweak = this.getConfig().getBoolean("weak." + p.getUniqueId().toString());
			boolean kweak = this.getConfig().getBoolean("weak." + k.getUniqueId().toString());
			if (pweak) {
				k.sendMessage(ChatColor.RED + "You cannot fight someone weakend!");
				p.sendMessage(ChatColor.RED + "You cannot fight whilist weakend!");
				e.setCancelled(true);
			}
			if (kweak) {
				k.sendMessage(ChatColor.RED + "You cannot fight whilist weakend!");
				p.sendMessage(ChatColor.RED + "You cannot fight someone weakend!");
				e.setCancelled(true);
			}
		} catch (Exception ex) {
			try {
				Arrow a = (Arrow) e.getDamager();
				Player k = (Player) a.getShooter();
				boolean pweak = this.getConfig().getBoolean("weak." + p.getUniqueId().toString());
				boolean kweak = this.getConfig().getBoolean("weak." + k.getUniqueId().toString());
				if (pweak) {
					k.sendMessage(ChatColor.RED + "You cannot fight someone weakend!");
					p.sendMessage(ChatColor.RED + "You cannot fight whilist weakend!");
					e.setCancelled(true);
				}
				if (kweak) {
					k.sendMessage(ChatColor.RED + "You cannot fight whilist weakend!");
					p.sendMessage(ChatColor.RED + "You cannot fight someone weakend!");
					e.setCancelled(true);
				}
			} catch (Exception a) {

			}
		}
		if (e.getEntity() instanceof Player && this.skyfight.contains(((Player) e.getEntity()))) {
			SkyFight.lastAttackerEvent(e);
		}
		try {
			if (e.getDamager() instanceof Arrow) {
				Arrow attacker = (Arrow) e.getDamager();
				Player shooter = (Player) attacker.getShooter();
				if (this.skyfight.contains(p) && this.skyfight.contains(shooter)) {
					if (ChatColor.stripColor(shooter.getItemInHand().getItemMeta().getDisplayName()).equalsIgnoreCase("SkyFight Teleportation Bow")) {
						shooter.teleport(p.getLocation());
						shooter.setHealth(shooter.getHealth() - 2.0);
						if (shooter.getHealth() < 10.0) {
							this.resetPlayerSkyfightLocation(shooter);
							SkyFight.removeLastAttacker(shooter);
							shooter.sendMessage(ChatColor.RED + "You have lost " + ChatColor.GOLD + "1" + ChatColor.RED + " tokens!");
							SkyFight.handleTokenRemove(shooter, 1);
							for (Player px : this.skyfight) {
								px.sendMessage(String.valueOf(L216) + ChatColor.GOLD + this.getNickname(shooter) + ChatColor.AQUA + " has been killed by " + ChatColor.GOLD + "Teleportation" + ChatColor.AQUA + " in SkyFight on map " + this.getCurrentSkyfightMap(shooter));
							}
							shooter.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
						} else {
							shooter.getWorld().playSound(p.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0f, 1.0f);
						}
						e.setCancelled(true);
						return;
					}
					if (p.getHealth() < 5.0) {
						this.resetPlayerSkyfightLocation(p);
						SkyFight.removeLastAttacker(p);
						p.sendMessage(ChatColor.RED + "You have lost " + ChatColor.GOLD + "1" + ChatColor.RED + " tokens!");
						SkyFight.handleTokenRemove(p, 1);
						int multiplier = SkyFight.getPlayerTokens(shooter) / 150 + 1 + SkyFight.getKillstreak(shooter);
						SkyFight.handleTokenAdd(shooter, multiplier);
						shooter.sendMessage(ChatColor.GREEN + "You have earned " + ChatColor.GOLD + multiplier + ChatColor.GREEN + " tokens!");
						SkyFight.killstreakHandler(shooter);
						for (Player px : this.skyfight) {
							px.sendMessage(String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has been killed by " + ChatColor.GOLD + this.getNickname((Player) attacker.getShooter()) + ChatColor.AQUA + " in SkyFight on map " + this.getCurrentSkyfightMap(p));
						}
						p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
					}
					return;
				}
			} else if (e.getDamager() instanceof Player) {
				Player attacker = (Player) e.getDamager();
				if (this.skyfight.contains(p) && this.skyfight.contains(attacker) && p.getHealth() < 10.0) {
					this.resetPlayerSkyfightLocation(p);
					SkyFight.removeLastAttacker(p);
					p.sendMessage(ChatColor.RED + "You have lost " + ChatColor.GOLD + "1" + ChatColor.RED + " tokens!");
					SkyFight.handleTokenRemove(p, 1);
					int multiplier = SkyFight.getPlayerTokens((Player) attacker) / 150 + 1 + SkyFight.getKillstreak((Player) attacker);
					SkyFight.handleTokenAdd((Player) attacker, multiplier);
					attacker.sendMessage(ChatColor.GREEN + "You have earned " + ChatColor.GOLD + multiplier + ChatColor.GREEN + " tokens!");
					SkyFight.killstreakHandler((Player) attacker);
					for (Player px : this.skyfight) {
						px.sendMessage(String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has been killed by " + ChatColor.GOLD + this.getNickname((Player) attacker) + ChatColor.AQUA + " in SkyFight on map " + this.getCurrentSkyfightMap(p));
					}
					p.getWorld().playSound(p.getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
				}
			}
		} catch (Exception attacker) {

		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player;
		Player targetPlayer;
		if (cmd.getName().equalsIgnoreCase("weaken")) {
			if (!sender.hasPermission("skorrloregaming.weaken")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(String.valueOf(L216) + ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.GREEN + "/weaken <Player>");
				return true;
			}
			Player tp = Bukkit.getPlayer(args[0]);
			this.getConfig().set("weak." + tp.getUniqueId().toString(), true);
			this.saveConfig();
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been weakened by " + ChatColor.GOLD + sender.getName() + ChatColor.GREEN + "!"));
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("unweaken")) {
			if (!sender.hasPermission("skorrloregaming.unweaken")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(String.valueOf(L216) + ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.GREEN + "/unweaken <Player>");
				return true;
			}
			Player tp = Bukkit.getPlayer(args[0]);
			this.getConfig().set("weak." + tp.getUniqueId().toString(), false);
			this.saveConfig();
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been unweakened by " + ChatColor.GOLD + sender.getName() + ChatColor.GREEN + "!"));
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (!sender.hasPermission("skorrloregaming.mute")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(String.valueOf(L216) + ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.GREEN + "/mute <Player>");
				return true;
			}
			Player tp = Bukkit.getPlayer(args[0]);
			this.getConfig().set("mute." + tp.getUniqueId().toString(), true);
			this.saveConfig();
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been muted by " + ChatColor.GOLD + sender.getName() + ChatColor.GREEN + "!"));
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("unmute")) {
			if (!sender.hasPermission("skorrloregaming.unmute")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(String.valueOf(L216) + ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.GREEN + "/unmute <Player>");
				return true;
			}
			Player tp = Bukkit.getPlayer(args[0]);
			this.getConfig().set("mute." + tp.getUniqueId().toString(), false);
			this.saveConfig();
			Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been unmuted by " + ChatColor.GOLD + sender.getName() + ChatColor.GREEN + "!"));
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("reboot")) {
			for (Player p : this.getServer().getOnlinePlayers()) {
				if (this.teampvp.contains(p)) {
					try {
						Storage.saveInventory(p, "teampvp");
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.world", p.getLocation().getWorld().getName());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.x", p.getLocation().getX());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.y", p.getLocation().getY());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.yaw", Float.valueOf(p.getLocation().getYaw()));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.pitch", Float.valueOf(p.getLocation().getPitch()));
						this.saveConfig();
						Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GREEN + "Successfully saved all information from " + p.getName() + " to database!"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (this.survival.contains(p)) {
					try {
						Storage.saveInventory(p, "survival");
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.world", p.getLocation().getWorld().getName());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.x", p.getLocation().getX());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.y", p.getLocation().getY());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.yaw", Float.valueOf(p.getLocation().getYaw()));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.pitch", Float.valueOf(p.getLocation().getPitch()));
						this.saveConfig();
						Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GREEN + "Successfully saved all information from " + p.getName() + " to database!"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (this.creative.contains(p)) {
					try {
						Storage.saveInventory(p, "creative");
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.world", p.getLocation().getWorld().getName());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.x", p.getLocation().getX());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.y", p.getLocation().getY());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.yaw", Float.valueOf(p.getLocation().getYaw()));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.pitch", Float.valueOf(p.getLocation().getPitch()));
						this.saveConfig();
						Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GREEN + "Successfully saved all information from " + p.getName() + " to database!"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.RED + "Failed to get minigame information from " + ChatColor.GOLD + p.getName() + ChatColor.RED + "!"));
				}
				Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + sender.getName() + " has issued a server restart!"));
				p.kickPlayer(String.valueOf(L216) + ChatColor.RED + "Server restarting... Should be back online within 1-2 minutes!");
			}
			Bukkit.shutdown();
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("sgreload")) {
			if (!sender.hasPermission("skorrloregaming.reload")) {
				sender.sendMessage(String.valueOf(L216) + ChatColor.DARK_RED + "You do not have permission to use that command!");
				return true;
			}
			this.reloadConfig();
			sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Configuration has been successfully reloaded!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("msg")) {
			String name = this.getNickname((Player) sender);
			if (!(sender instanceof Player)) {
				name = "CONSOLE";
			}
			if (args.length <= 1) {
				sender.sendMessage(ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.RED + "/msg <Player> <Message>");
				return true;
			}
			String msg = "";
			int i = 1;
			while (i < args.length) {
				msg = String.valueOf(msg) + args[i] + " ";
				++i;
			}
			targetPlayer = Bukkit.getPlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage(ChatColor.DARK_RED + "The specified player is not online!");
				return true;
			}
			msg = msg.replaceAll("&", "\u00a7");
			sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + "me" + ChatColor.GOLD + "->" + ChatColor.AQUA + this.getNickname(targetPlayer) + ChatColor.GOLD + "] " + ChatColor.AQUA + msg);
			targetPlayer.sendMessage(ChatColor.GOLD + "[" + ChatColor.AQUA + name + ChatColor.GOLD + "->" + ChatColor.AQUA + "me" + ChatColor.GOLD + "] " + ChatColor.AQUA + msg);
		}
		if (cmd.getName().equalsIgnoreCase("tokens")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be used in-game!");
			}
			if (this.skyfight.contains((player = (Player) sender))) {
				int tokens = SkyFight.getPlayerTokens(player);
				int level = tokens / 150 + 1;
				sender.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "--------------------------------------------");
				sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Tokens: " + ChatColor.GOLD + tokens);
				sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Level: " + ChatColor.GOLD + level);
				sender.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "--------------------------------------------");
				return true;
			}
			sender.sendMessage("Unknown command. Type \"/help\" for help.");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("stats")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be used in-game!");
			}
			if (this.skyfight.contains((player = (Player) sender))) {
				int tokens = SkyFight.getPlayerTokens(player);
				int level = tokens / 150 + 1;
				sender.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "--------------------------------------------");
				sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Tokens: " + ChatColor.GOLD + tokens);
				sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Level: " + ChatColor.GOLD + level);
				sender.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "--------------------------------------------");
				return true;
			}
			int killCount = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".kills");
			int deathCount = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".deaths");
			int ks = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".ks");
			int money = this.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".money");
			int dpk = killCount / 50 + 5;
			sender.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "--------------------------------------------");
			sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Kills: " + ChatColor.GOLD + killCount);
			sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Deaths: " + ChatColor.GOLD + deathCount);
			sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Killstreak: " + ChatColor.GOLD + ks);
			sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current Money: " + ChatColor.GOLD + money);
			sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + "Current DPK: " + ChatColor.GOLD + dpk);
			sender.sendMessage(String.valueOf(L216) + ChatColor.GOLD + "--------------------------------------------");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("cc") || cmd.getName().equalsIgnoreCase("clearchat")) {
			if (sender.hasPermission("skorrloregaming.cc")) {
				int i = 0;
				while (i < 100) {
					Bukkit.broadcastMessage("");
					++i;
				}
				Bukkit.broadcastMessage(("The server chat history has been cleared by " + sender.getName()));
				return true;
			}
			sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("tsm")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".saveItems")) {
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".saveItems", false);
					p.sendMessage("Safe mode has been disabled!");
					return true;
				}
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".saveItems", true);
				p.sendMessage("Safe mode has been enabled!");
				return true;
			}
			sender.sendMessage("This command is for in-game players only!");
			return false;
		}
		if (cmd.getName().equalsIgnoreCase("disable-plugin")) {
			if (!sender.hasPermission("skorrloregaming.disable")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/disable-plugin <Plugin-Name>");
				return true;
			}
			if (this.disablePlugin(args[0])) {
				sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.AQUA + " is now disabled.");
			} else {
				sender.sendMessage(String.valueOf(L216) + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.RED + " was not found!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("enable-plugin")) {
			if (!sender.hasPermission("skorrloregaming.enable")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length != 1) {
				sender.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/disable-plugin <Plugin-Name>");
				return true;
			}
			if (this.enablePlugin(args[0])) {
				sender.sendMessage("[SkorrloreGaming]" + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.AQUA + " is now enabled.");
			} else {
				sender.sendMessage("[SkorrloreGaming]" + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.RED + " was not found!");
			}
		}
		if (cmd.getName().equalsIgnoreCase("setrank")) {
			if (!sender.hasPermission("skorrloregaming.setrank")) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (args.length == 2) {
				Player targetPlayer2 = Bukkit.getPlayer(args[0]);
				if (!this.setRank(targetPlayer2, args[1])) {
					sender.sendMessage(String.valueOf(L216) + ChatColor.RED + "Rank not found: " + ChatColor.GOLD + args[1] + ChatColor.RED + "!");
					return true;
				}
				Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + args[0] + ChatColor.GREEN + " has been given rank " + ChatColor.GOLD + args[1].toUpperCase() + ChatColor.GREEN + "!"));
				return true;
			}
			sender.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/setrank <Player> <Rank>");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.DARK_RED + "This command can only be used in-game!");
			return true;
		}
		player = (Player) sender;
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("unlock-gamemode")) {
			if (!p.hasPermission("skorrloregaming.unlock.gamemode")) {
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (this.unlockedGamemode.contains(p)) {
				this.unlockedGamemode.remove(p);
				p.sendMessage(String.valueOf(L216) + "You have successfully locked gamemode changing.");
			} else {
				this.unlockedGamemode.add(p);
				p.sendMessage(String.valueOf(L216) + "You have successfully unlocked gamemode changing.");
			}
		}
		if (cmd.getName().equalsIgnoreCase("report")) {
			if (args.length < 2) {
				p.sendMessage(ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.RED + "/report <Player> <Reason>");
				return true;
			}
			String reason = "";
			int i = 1;
			while (i < args.length) {
				reason = String.valueOf(reason) + args[i] + " ";
				++i;
			}
			for (Player pl : this.getServer().getOnlinePlayers()) {
				if (!pl.hasPermission("skorrloregaming.listen.report")) continue;
				pl.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has reported " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " for " + reason + "!");
			}
			p.sendMessage(ChatColor.GREEN + "You have successfully reported " + ChatColor.GOLD + args[0] + ChatColor.GREEN + " for " + reason + "!");
			return true;
		}
		if (cmd.getName().equalsIgnoreCase("ping")) {
			try {
				Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer");
				Class<?> entityPlayer = Class.forName("net.minecraft.server.v1_7_R4.EntityPlayer");
				Method getHandle = craftPlayer.getDeclaredMethod("getHandle");
				int ping = entityPlayer.getField("ping").getInt(getHandle.invoke(p));
				p.sendMessage(L216 + ChatColor.GREEN + "Current Ping: " + ChatColor.GOLD + ping);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			return true;
		}
		if (!this.CC.contains(player)) {
			Matcher m;
			double x;
			String nickname;
			World w;
			Player tp;
			Boolean tprequest;
			String Prefix;
			double y;
			if (cmd.getName().equalsIgnoreCase("respawn")) {
				if (!this.creative.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.BEDROCK) {
					this.ttg(p);
					this.ttg(p);
					p.sendMessage(ChatColor.AQUA + "You have respawned into the world!");
					this.ttg(p);
					this.ttg(p);
					return true;
				}
				p.sendMessage(ChatColor.AQUA + "You cant respawn when you are not dead!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("getPlayerIp")) {
				if (args.length == 1) {
					if (!p.hasPermission("skorrloregaming.getplayerip")) {
						player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					targetPlayer = Bukkit.getPlayer(args[0]);
					player.sendMessage(ChatColor.AQUA + "IP: " + ChatColor.GOLD + targetPlayer.getAddress());
					return true;
				}
				player.sendMessage(ChatColor.GREEN + "Please enter a name of the player");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("teampvp")) {
				p.performCommand("server teampvp");
			}
			if (cmd.getName().equalsIgnoreCase("survival")) {
				p.performCommand("server survival");
			}
			if (cmd.getName().equalsIgnoreCase("creative")) {
				p.performCommand("server creative");
			}
			if (cmd.getName().equalsIgnoreCase("skyfight")) {
				p.performCommand("server skyfight");
			}
			if (cmd.getName().equalsIgnoreCase("server")) {
				if (args.length == 0) {
					p.sendMessage(ChatColor.DARK_RED + "Invalid Syntax: " + ChatColor.RED + "/server <Server>");
					return true;
				}
				if (args[0].equalsIgnoreCase("survival")) {
					this.toSurvival(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("creative")) {
					this.toCreative(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("teampvp")) {
					this.toTeamPvP(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("skyfight")) {
					this.skyfightGUI(p);
					return true;
				}
				p.sendMessage(ChatColor.RED + "Server not found!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("vote")) {
				p.sendMessage(ChatColor.GRAY + "------------------------------------------------------------------------->");
				p.sendMessage(ChatColor.GOLD + "PMC: " + ChatColor.AQUA + "http://www.planetminecraft.com/server/skorrloregaming-21/vote/");
				p.sendMessage(ChatColor.GOLD + "MCS: " + ChatColor.AQUA + "http://minecraftservers.org/vote/318551");
				p.sendMessage(ChatColor.GRAY + "------------------------------------------------------------------------->");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("lobby")) {
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/lobby <Lobby>");
					return true;
				}
				if (args[0].equalsIgnoreCase("1")) {
					World w2 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
					double x2 = this.settings.getData().getDouble("warps.map000_teampvp.x");
					double y2 = this.settings.getData().getDouble("warps.map000_teampvp.y");
					double z = this.settings.getData().getDouble("warps.map000_teampvp.z");
					p.teleport(new Location(w2, x2, y2, z));
				}
			}
			if (cmd.getName().equalsIgnoreCase("sfixall")) {
				if (player.hasPermission("skorrloregaming.srepairall")) {
					this.repairAll(p);
				} else if (this.getConfig().getString("ranks." + player.getUniqueId().toString() + ".rank") == "Owner") {
					this.repairAll(p);
				} else {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				}
			}
			if (cmd.getName().equalsIgnoreCase("doi")) {
				if (!this.survival.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				if (args.length != 0) {
					p.sendMessage(this.RED + "Invalid Syntax: " + this.AQUA + "/doi");
					return true;
				}
				if (!p.getInventory().contains(new ItemStack(Material.CHEST))) {
					p.sendMessage(ChatColor.RED + "You must have " + ChatColor.GOLD + "1" + ChatColor.RED + " chests to use this command!");
					return true;
				}
				p.sendMessage(ChatColor.RED + "WARNING: This will clear your hole inventory and only take 27 stacks of items! Type '/cdoi' to confirm");
				this.doi.add(p);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("bb")) {
				if (!this.survival.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				if (args.length != 0) {
					p.sendMessage(this.RED + "Invalid Syntax: " + this.AQUA + "/bb");
					return true;
				}
				Location YN1 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ());
				Location YN1ZA1 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ() + 1.0);
				Location YN1ZN1 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ() - 1.0);
				Location YN1ZA2 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ() + 2.0);
				Location YN1ZN2 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ() - 2.0);
				Location YN1XA1 = new Location(p.getWorld(), p.getLocation().getX() + 1.0, p.getLocation().getY() - 1.0, p.getLocation().getZ());
				Location YN1XN1 = new Location(p.getWorld(), p.getLocation().getX() - 1.0, p.getLocation().getY() - 1.0, p.getLocation().getZ());
				Location YN1XA2 = new Location(p.getWorld(), p.getLocation().getX() + 2.0, p.getLocation().getY() - 1.0, p.getLocation().getZ());
				Location YN1XN2 = new Location(p.getWorld(), p.getLocation().getX() - 2.0, p.getLocation().getY() - 1.0, p.getLocation().getZ());
				Location YN1XZA1 = new Location(p.getWorld(), p.getLocation().getX() + 1.0, p.getLocation().getY() - 1.0, p.getLocation().getZ() + 1.0);
				Location YN1XZN1 = new Location(p.getWorld(), p.getLocation().getX() - 1.0, p.getLocation().getY() - 1.0, p.getLocation().getZ() - 1.0);
				Location YN1XZAN1 = new Location(p.getWorld(), p.getLocation().getX() + 1.0, p.getLocation().getY() - 1.0, p.getLocation().getZ() - 1.0);
				Location YN1XZNA1 = new Location(p.getWorld(), p.getLocation().getX() - 1.0, p.getLocation().getY() - 1.0, p.getLocation().getZ() + 1.0);
				YN1.getBlock().setType(Material.COBBLESTONE);
				YN1ZA1.getBlock().setType(Material.COBBLESTONE);
				YN1ZN1.getBlock().setType(Material.COBBLESTONE);
				YN1ZA2.getBlock().setType(Material.COBBLESTONE);
				YN1ZN2.getBlock().setType(Material.COBBLESTONE);
				YN1XA1.getBlock().setType(Material.COBBLESTONE);
				YN1XN1.getBlock().setType(Material.COBBLESTONE);
				YN1XA2.getBlock().setType(Material.COBBLESTONE);
				YN1XN2.getBlock().setType(Material.COBBLESTONE);
				YN1XZA1.getBlock().setType(Material.COBBLESTONE);
				YN1XZN1.getBlock().setType(Material.COBBLESTONE);
				YN1XZAN1.getBlock().setType(Material.COBBLESTONE);
				YN1XZNA1.getBlock().setType(Material.COBBLESTONE);
				p.sendMessage(this.AQUA + "AutoGenerated a doi zone!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("code-gen")) {
				this.Random();
				p.sendMessage(ChatColor.GREEN + "Random-Code: " + ChatColor.GOLD + this.RANDOM);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("cdoi")) {
				if (!this.survival.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				if (this.doi.contains(p)) {
					if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.COBBLESTONE) {
						p.sendMessage(this.RED + "You must be above a block of cobblestone!");
						return true;
					}
					p.sendMessage(ChatColor.GREEN + "DOI Confirmed. Transferring items...");
					p.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.CHEST, 1)});
					Location cl = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 1.0, p.getLocation().getZ());
					cl.getBlock().setType(Material.CHEST);
					Chest c = (Chest) cl.getBlock().getState();
					ItemStack[] ib = p.getInventory().getContents();
					ItemStack[] cc = new ItemStack[ib.length];
					int i = 0;
					while (i < p.getInventory().getSize()) {
						cc[i] = ib[i];
						++i;
					}
					i = 0;
					while (i < c.getInventory().getSize()) {
						if (cc[i] != null) {
							c.getInventory().addItem(new ItemStack[]{cc[i]});
						}
						++i;
					}
					p.getInventory().clear();
					p.sendMessage(ChatColor.AQUA + "Item(s) Have been put successfully away!");
					return true;
				}
				p.sendMessage(ChatColor.RED + "There is nothing to confirm!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("ci")) {
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
				p.getInventory().setChestplate(new ItemStack(Material.AIR));
				p.getInventory().setLeggings(new ItemStack(Material.AIR));
				p.getInventory().setBoots(new ItemStack(Material.AIR));
				p.getInventory().clear();
				p.sendMessage(ChatColor.GREEN + "Your inventory has been successfully cleared!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("bid")) {
				if (!this.survival.contains(p)) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be playing " + ChatColor.GOLD + "Survival" + ChatColor.RED + " in order to use this command!");
					return true;
				}
				if (!this.getConfig().getBoolean("Auction.Running")) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "There is no running auction to bid on!");
					return true;
				}
				if (this.getConfig().get("Auction.Cancelled") == null) {
					this.getConfig().set("Auction.Cancelled", false);
					this.saveConfig();
				}
				if (this.getConfig().getString("Auction.StarterName").equals(p.getName())) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You cannot bid on your own auction!");
					return true;
				}
				if (this.getConfig().get("Auction.Bids.Last.Name").equals(p.getName())) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You were the last one that had placed a bid on this auction.");
					return true;
				}
				if (this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money") < this.getConfig().getInt("Auction.Bids.Last.Amount") + 4) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You do not have enough money to preform this bid!");
					return true;
				}
				int Amount = this.getConfig().getInt("Auction.Bids.Last.Amount");
				this.getConfig().set("Auction.Bids.Last.Amount", (Amount += 5));
				this.getConfig().set("Auction.Bids.Last.Name", p.getName());
				this.saveConfig();
				Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has placed a bid of " + ChatColor.GOLD + this.getConfig().getInt("Auction.Bids.Last.Amount") + ChatColor.AQUA + " on auction " + ChatColor.GOLD + CURR_AUCTION_CODE));
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("auc-end")) {
				if (!this.survival.contains(p)) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be playing " + ChatColor.GOLD + "Survival" + ChatColor.RED + " in order to use this command!");
					return true;
				}
				if (this.getConfig().get("Auction.Running") == null) {
					this.getConfig().set("Auction.Running", false);
					this.saveConfig();
				}
				if (this.getConfig().get("Auction.Cancelled") == null) {
					this.getConfig().set("Auction.Cancelled", false);
					this.saveConfig();
				}
				if (this.getConfig().get("Auction.Bids") == null) {
					this.getConfig().set("Auction.Bids.Last.Amount", 0);
					this.getConfig().set("Auction.Bids.Last.Name", "Console");
					this.saveConfig();
				}
				if (this.getConfig().get("Auction.StarterName") == null) {
					this.getConfig().set("Auction.StarterName", "Console");
					this.saveConfig();
				}
				if (!this.getConfig().getBoolean("Auction.Running")) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "There is no auction running at this time!");
					return true;
				}
				if (!this.getConfig().getString("Auction.StarterName").equals(p.getName())) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You cannot end a auction that you did not start!");
					return true;
				}
				Bukkit.broadcastMessage((String.valueOf(AUCTION_DISPLAY) + "The current auction has been cancelled!"));
				this.getConfig().set("Auction.Running", false);
				this.getConfig().set("Auction.Cancelled", true);
				this.saveConfig();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("auc-start")) {
				if (!this.survival.contains(p)) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be playing " + ChatColor.GOLD + "Survival" + ChatColor.RED + " in order to use this command!");
					return true;
				}
				if (this.getConfig().get("Auction.Running") == null) {
					this.getConfig().set("Auction.Running", false);
					this.saveConfig();
				}
				if (this.getConfig().get("Auction.Cancelled") == null) {
					this.getConfig().set("Auction.Cancelled", false);
					this.saveConfig();
				}
				if (this.getConfig().get("Auction.Bids") == null) {
					this.getConfig().set("Auction.Bids.Last.Amount", 0);
					this.getConfig().set("Auction.Bids.Last.Name", "Console");
					this.saveConfig();
				}
				if (this.getConfig().get("Auction.StarterName") == null) {
					this.getConfig().set("Auction.StarterName", "Console");
					this.saveConfig();
				}
				if (this.getConfig().getBoolean("Auction.Running")) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "There is already an auction running!");
					return true;
				}
				if (this.getConfig().get("Auction.Code") == null) {
					this.getConfig().set("Auction.Code", 0);
					this.saveConfig();
				}
				if (args.length != 2) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/auc-start <Price> <Time>");
					return true;
				}
				if (p.getInventory().getItemInHand().getType() == null || p.getInventory().getItemInHand().getType() == Material.AIR || p.getInventory().getItemInHand() == null) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be holding an item to auction!");
					return true;
				}
				int CODE = this.getConfig().getInt("Auction.Code");
				this.getConfig().set("Auction.Code", (CODE + 1));
				this.getConfig().set("Auction.StarterName", p.getName());
				this.getConfig().set("Auction.Bids.Last.Name", "Console");
				this.getConfig().set("Auction.Bids.Last.Amount", 0);
				this.saveConfig();
				String plot1 = args[0].replaceAll("[^0-9]+", "");
				String plot2 = args[1].replaceAll("[^0-9]+", "");
				int V1 = Integer.parseInt(plot2);
				int V2 = Integer.parseInt(plot1);
				this.Auction(p.getInventory().getItemInHand(), V2, V1, p.getName(), CODE + 1, p);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("pay")) {
				if (this.teampvp.contains(p)) {
					if (args.length != 2) {
						p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/pay <Player> <Amount>");
						return true;
					}
					tp = Bukkit.getPlayer(args[0]);
					if (tp == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					int amount = Integer.parseInt(args[1]);
					if (amount < 0) {
						p.sendMessage(ChatColor.RED + "You cannot pay players in negatives!");
						return true;
					}
					int pMoney = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
					int tpMoney = this.getConfig().getInt("ranks." + tp.getUniqueId().toString() + ".money");
					if (pMoney >= amount - 1) {
						this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".money", (tpMoney += amount));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".money", (pMoney -= amount));
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN + "You have payed " + ChatColor.GOLD + tp.getName() + ChatColor.GREEN + ": " + ChatColor.GOLD + amount + ChatColor.GREEN + " dollars!");
						tp.sendMessage(ChatColor.GREEN + "You have been payed " + ChatColor.GOLD + amount + ChatColor.GREEN + " dollars, by " + ChatColor.GOLD + p.getName());
						return true;
					}
					p.sendMessage(ChatColor.AQUA + "You do not have enough money to give");
					return true;
				}
				if (!this.creative.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/pay <Player> <Amount>");
					return true;
				}
				if (args.length == 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/pay <Player> <Amount>");
					return true;
				}
				if (args.length == 2) {
					String amount = args[1];
					Player tp2 = Bukkit.getPlayer(args[0]);
					if (tp2 == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					int value = 0;
					int vm = 0;
					if (amount.equals("30")) {
						value = 29;
					} else if (amount.equals("60")) {
						value = 59;
					} else if (amount.equals("90")) {
						value = 89;
					} else if (amount.equals("120")) {
						value = 119;
					} else {
						p.sendMessage(ChatColor.AQUA + "Please enter " + ChatColor.GOLD + "30" + ChatColor.AQUA + ", " + ChatColor.GOLD + "60" + ChatColor.AQUA + ", " + ChatColor.GOLD + "90" + ChatColor.AQUA + ", or " + ChatColor.GOLD + "120" + ChatColor.AQUA + " as an amount");
						return true;
					}
					vm = value + 1;
					if (p.getLevel() >= value) {
						p.setLevel(p.getLevel() - vm);
						tp2.setLevel(tp2.getLevel() + vm);
						tp2.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has payed you " + ChatColor.GOLD + vm + ChatColor.AQUA + " levels");
						p.sendMessage(ChatColor.AQUA + "You have payed " + ChatColor.GOLD + tp2.getName() + " " + ChatColor.GOLD + vm + ChatColor.AQUA + " levels");
						return true;
					}
					p.sendMessage(ChatColor.AQUA + "You do not have enough levels to give");
					return true;
				}
			}
			if (cmd.getName().equalsIgnoreCase("unlock")) {
				if (!p.hasPermission("skorrloregaming.unlock")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/unlock <Player>");
					return true;
				}
				tp = Bukkit.getPlayer(args[0]);
				if (tp == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found!");
					return true;
				}
				this.locked.remove(tp);
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".kitVip", false);
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".kitBedrock", false);
				this.saveConfig();
				p.sendMessage(ChatColor.GREEN + "Unlocked Player: " + ChatColor.GOLD + tp.getName());
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("addperm")) {
				if (!p.hasPermission("skorrloregaming.addperm")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length == 3) {
					tp = Bukkit.getPlayer(args[0]);
					if (tp == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					if (args[2].equalsIgnoreCase("true")) {
						tp.addAttachment((Plugin) this, args[1], true);
						return true;
					}
					if (args[2].equalsIgnoreCase("false")) {
						tp.addAttachment((Plugin) this, args[1], false);
						return true;
					}
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Please enter, true or false.");
					return true;
				}
				p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/addperm <Player> <Permission> <True/False>");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("sethub")) {
				if (!p.hasPermission("skorrloregaming.setspawn")) {
					player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return true;
				}
				p.sendMessage(ChatColor.GREEN + "You have set the server hub!");
				this.settings.getData().set("warps.kb0000009.world", p.getLocation().getWorld().getName());
				this.settings.getData().set("warps.kb0000009.x", p.getLocation().getX());
				this.settings.getData().set("warps.kb0000009.y", p.getLocation().getY());
				this.settings.getData().set("warps.kb0000009.z", p.getLocation().getZ());
				this.settings.saveData();
				this.settings.getData().set("warps.kb00000001.world", p.getLocation().getWorld().getName());
				this.settings.getData().set("warps.kb00000001.x", p.getLocation().getX());
				this.settings.getData().set("warps.kb00000001.y", p.getLocation().getY());
				this.settings.getData().set("warps.kb00000001.z", p.getLocation().getZ());
				this.settings.saveData();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("spawn")) {
				if (!this.teampvp.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				final Player pf = p;
				if (this.stpt.contains(p)) {
					p.sendMessage(ChatColor.RED + "You must wait " + ChatColor.GOLD + "5" + ChatColor.RED + " minutes before using this command again!");
					return true;
				}
				World w3 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
				double x3 = this.settings.getData().getDouble("warps.map000_teampvp.x");
				double y3 = this.settings.getData().getDouble("warps.map000_teampvp.y");
				double z = this.settings.getData().getDouble("warps.map000_teampvp.z");
				p.teleport(new Location(w3, x3, y3, z));
				p.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.GOLD + "Spawn");
				this.stpt.add(p);
				this.getServer().getScheduler().scheduleAsyncDelayedTask((Plugin) this, new Runnable() {

					@Override
					public void run() {
						if (SkorrloreGaming.this.teampvp.contains(pf)) {
							pf.sendMessage(ChatColor.RED + "You can now use the command " + ChatColor.GOLD + "/spawn" + ChatColor.RED + " again.");
						}
						SkorrloreGaming.this.stpt.remove(pf);
					}
				}, 6000L);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("setzone")) {
				if (!p.hasPermission("skorrrloregaming.setzone")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/setzone <Zone-Name>");
					return true;
				}
				String hd = args[0];
				this.settings.getData().set("warps." + hd + ".world", p.getLocation().getWorld().getName());
				this.settings.getData().set("warps." + hd + ".x", p.getLocation().getX());
				this.settings.getData().set("warps." + hd + ".y", p.getLocation().getY());
				this.settings.getData().set("warps." + hd + ".z", p.getLocation().getZ());
				this.saveConfig();
				this.settings.saveData();
				p.sendMessage(ChatColor.GREEN + "Successfully set zone: " + ChatColor.GOLD + hd);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("sethome")) {
				if (this.survival.contains(p)) {
					Random randomGenerator = new Random();
					int idx = 1;
					if (idx <= 10) {
						int randomInt = randomGenerator.nextInt(100);
						int randomInt2 = randomGenerator.nextInt(1000);
						int randomInt3 = randomGenerator.nextInt(10000);
						int randomInt4 = randomGenerator.nextInt(100000);
						int randomInt5 = randomGenerator.nextInt(1000000);
						int randomInt8 = randomGenerator.nextInt(10000000);
						int randomInt9 = randomGenerator.nextInt(20000000);
						int randomInt6 = randomGenerator.nextInt(10000);
						int randomInt7 = randomGenerator.nextInt(1000);
						long hd = randomInt + randomInt2 + randomInt3 + randomInt4 + randomInt5 + randomInt6 + randomInt7 + randomInt8 + randomInt9;
						if (this.getConfig().get("ranks." + p.getUniqueId().toString() + ".homes") == null) {
							this.getConfig().set("ranks." + p.getUniqueId().toString() + ".homes", 0);
							this.saveConfig();
						}
						if (this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".homes") == 1) {
							p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "You can only have 1 homes at a time!");
							return true;
						}
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".home", hd);
						this.saveConfig();
						this.settings.getData().set("warps." + hd + ".world", p.getLocation().getWorld().getName());
						this.settings.getData().set("warps." + hd + ".x", p.getLocation().getX());
						this.settings.getData().set("warps." + hd + ".y", p.getLocation().getY());
						this.settings.getData().set("warps." + hd + ".z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".homes", 1);
						this.saveConfig();
						this.settings.saveData();
						p.sendMessage(ChatColor.GOLD + "You have set your home!");
						p.sendMessage(ChatColor.AQUA + "Code: " + ChatColor.GOLD + hd);
						p.sendMessage(ChatColor.AQUA + "To teleport to someones home, type /home <Code>");
						return true;
					}
				}
				p.sendMessage("Unknown command. Type /help for help.");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("tpaccept")) {
				if (!this.survival.contains(p)) {
					return true;
				}
				tprequest = this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".tpboolean");
				if (tprequest.booleanValue()) {
					Object tp3 = this.getConfig().get("ranks." + p.getUniqueId().toString() + ".tprequest");
					Boolean tphere = this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".tpherebool");
					if (tphere.booleanValue()) {
						Player targetPlayer3 = Bukkit.getPlayer((String) tp3);
						if (targetPlayer3 == null) {
							p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
							return true;
						}
						p.teleport((Entity) targetPlayer3);
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpherebool", false);
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tprequest", 0);
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpboolean", false);
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
						targetPlayer3.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
						return true;
					}
					Player targetPlayer4 = Bukkit.getPlayer((String) tp3);
					if (targetPlayer4 == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					if (!this.survival.contains(targetPlayer4)) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Other player is not currently playing survival!");
						return true;
					}
					targetPlayer4.teleport((Entity) p);
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpherebool", false);
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tprequest", 0);
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpboolean", false);
					this.saveConfig();
					p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
					targetPlayer4.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
					return true;
				}
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("rules")) {
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				p.sendMessage(ChatColor.GOLD + "[Rules]");
				p.sendMessage(ChatColor.RED + "(1) " + this.AQUA + "No hacking");
				p.sendMessage(ChatColor.RED + "(2) " + this.AQUA + "No spamming");
				p.sendMessage(ChatColor.RED + "(3) " + this.AQUA + "No advertising");
				p.sendMessage(ChatColor.RED + "(4) " + this.AQUA + "No swearing");
				p.sendMessage(ChatColor.RED + "(5) " + this.AQUA + "No asking for free ranks");
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("store")) {
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				p.sendMessage(ChatColor.GOLD + "Link: " + ChatColor.AQUA + "http://skorrloregaming.boards.net/page/store/");
				p.sendMessage(ChatColor.GREEN + "Items on stock: ");
				p.sendMessage(ChatColor.RED + "VIP Rank - $2 ");
				p.sendMessage(ChatColor.RED + "VIP+ Rank - $5 ");
				p.sendMessage(ChatColor.RED + "MVP Rank - $7 ");
				p.sendMessage(ChatColor.RED + "MVP+ Rank - $10");
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("tpyes")) {
				if (!this.survival.contains(p)) {
					return true;
				}
				tprequest = this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".tpboolean");
				if (tprequest.booleanValue()) {
					Object tp4 = this.getConfig().get("ranks." + p.getUniqueId().toString() + ".tprequest");
					Boolean tphere = this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".tpherebool");
					if (tphere.booleanValue()) {
						Player targetPlayer5 = Bukkit.getPlayer((String) tp4);
						if (targetPlayer5 == null) {
							p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
							return true;
						}
						p.teleport((Entity) targetPlayer5);
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpherebool", false);
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tprequest", 0);
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpboolean", false);
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
						targetPlayer5.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
						return true;
					}
					Player targetPlayer6 = Bukkit.getPlayer((String) tp4);
					if (targetPlayer6 == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					if (!this.survival.contains(targetPlayer6)) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Other player is not currently playing survival!");
						return true;
					}
					targetPlayer6.teleport((Entity) p);
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpherebool", false);
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tprequest", 0);
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpboolean", false);
					this.saveConfig();
					p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
					targetPlayer6.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
					return true;
				}
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("tpahere")) {
				if (!this.survival.contains(p)) {
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/tpahere <Player>");
					return true;
				}
				tp = Bukkit.getPlayer(args[0]);
				if (tp == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
					return true;
				}
				p.sendMessage(ChatColor.GREEN + "Tpahere request sent");
				tp.sendMessage(ChatColor.GREEN + "You have been sent a tpahere request by " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + ", Type /tpaccept or /tpyes to accept.");
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".tprequest", p.getName());
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".tpboolean", true);
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".tpherebool", true);
				this.saveConfig();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("tpa")) {
				if (!this.survival.contains(p)) {
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/tpa <Player>");
					return true;
				}
				tp = Bukkit.getPlayer(args[0]);
				if (tp == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
					return true;
				}
				p.sendMessage(ChatColor.GREEN + "Tpa request sent.");
				tp.sendMessage(ChatColor.GREEN + "You have been sent a tpa request by " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + ", Type /tpaccept or /tpyes to accept.");
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".tprequest", p.getName());
				this.getConfig().set("ranks." + tp.getUniqueId().toString() + ".tpboolean", true);
				this.getConfig().set("ranks." + p.getUniqueId().toString() + ".tpherebool", false);
				this.saveConfig();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("money")) {
				int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
				p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + Money);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("delhome")) {
				if (this.survival.contains(p)) {
					long hd = 0L;
					this.settings.getData().set("warps." + this.getConfig().get(new StringBuilder("ranks.").append(p.getUniqueId().toString()).append(".home").toString()), 0);
					this.settings.saveConfig();
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".homes", 0);
					this.saveConfig();
					this.getConfig().set("ranks." + p.getUniqueId().toString() + ".home", hd);
					this.saveConfig();
					p.sendMessage(ChatColor.GOLD + "You have deleted your home!");
					return true;
				}
				p.sendMessage("Unknown command. Type /help for help.");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("rename")) {
				if (this.getRank(p).equalsIgnoreCase("mvpx") || this.getRank(p).equalsIgnoreCase("mvp") || this.getRank(p).equalsIgnoreCase("vipx") || this.getRank(p).equalsIgnoreCase("youtube") || this.getRank(p).equalsIgnoreCase("owner")) {
					ItemStack xold;
					String name = "";
					int i = 0;
					while (i < args.length) {
						name = String.valueOf(name) + args[i] + " ";
						++i;
					}
					ItemStack xnew = xold = p.getItemInHand();
					ItemMeta xnewMeta = xnew.getItemMeta();
					xnewMeta.setDisplayName(name.replaceAll("&", "\u00a7"));
					xnew.setItemMeta(xnewMeta);
					p.sendMessage(String.valueOf(L216) + "Your held item has been successfully renamed!");
					return true;
				}
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("nickname")) {
				if (!p.hasPermission("skorrloregaming.nickname")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.DARK_RED + "Please specify a nickname!");
					return true;
				}
				if (args[0].length() < 8 || args[0].length() > this.getConfig().getInt("nickname.maxlength")) {
					p.sendMessage(ChatColor.DARK_RED + "Please specify a nickname length between 8-" + this.getConfig().getInt("nickname.maxlength") + "!");
					return true;
				}
				nickname = args[0];
				Pattern pattern = Pattern.compile("[^a-z0-9 ]", 2);
				m = pattern.matcher(nickname.replaceAll("&", ""));
				boolean syntax = m.find();
				if (syntax) {
					p.sendMessage(ChatColor.DARK_RED + "Invalid nickname syntax, please try again!");
					return true;
				}
				if ((nickname = nickname.replaceAll("&", "\u00a7")).equalsIgnoreCase(p.getName())) {
					this.setNickname(p, nickname, false);
				} else {
					this.setNickname(p, nickname, true);
				}
				p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + nickname + ChatColor.WHITE);
				p.sendMessage(ChatColor.GREEN + "You have successfully set your nickname: " + ChatColor.AQUA + nickname);
			}
			if (cmd.getName().equalsIgnoreCase("nick")) {
				if (!p.hasPermission("skorrloregaming.nickname")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.DARK_RED + "Please specify a nickname!");
					return true;
				}
				if (args[0].length() < 8 || args[0].length() > this.getConfig().getInt("nickname.maxlength")) {
					p.sendMessage(ChatColor.DARK_RED + "Please specify a nickname length between 8-" + this.getConfig().getInt("nickname.maxlength") + "!");
					return true;
				}
				nickname = args[0];
				Pattern pattern = Pattern.compile("[^a-z0-9 ]", 2);
				m = pattern.matcher(nickname.replaceAll("&", ""));
				boolean syntax = m.find();
				if (syntax) {
					p.sendMessage(ChatColor.DARK_RED + "Invalid nickname syntax, please try again!");
					return true;
				}
				if ((nickname = nickname.replaceAll("&", "\u00a7")).equalsIgnoreCase(p.getName())) {
					this.setNickname(p, nickname, false);
				} else {
					this.setNickname(p, nickname, true);
				}
				p.setDisplayName(String.valueOf(this.getRankPrefix(p)) + nickname + ChatColor.WHITE);
				p.sendMessage(ChatColor.GREEN + "You have successfully set your nickname: " + ChatColor.AQUA + nickname);
			}
			if (cmd.getName().equalsIgnoreCase("home")) {
				if (args.length == 1) {
					String plot = args[0].replaceAll("[^0-9]+", "");
					if (this.settings.getData().getConfigurationSection("warps." + plot) == null) {
						p.sendMessage(ChatColor.GOLD + args[0] + ChatColor.AQUA + " is not a valid code!");
						return true;
					}
					World w4 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps." + plot + ".world"));
					double x4 = this.settings.getData().getDouble("warps." + plot + ".x");
					double y4 = this.settings.getData().getDouble("warps." + plot + ".y");
					double z = this.settings.getData().getDouble("warps." + plot + ".z");
					p.teleport(new Location(w4, x4, y4, z));
					p.sendMessage(ChatColor.GOLD + "You have teleported to: " + ChatColor.AQUA + args[0] + ChatColor.GOLD + "!");
					return true;
				}
				Object homes = this.getConfig().get("ranks." + p.getUniqueId().toString() + ".homes");
				if (this.survival.contains(p)) {
					if (homes.equals(0)) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "You have no home set");
						return true;
					}
					Object hd = this.getConfig().get("ranks." + p.getUniqueId().toString() + ".home");
					w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps." + hd + ".world"));
					x = this.settings.getData().getDouble("warps." + hd + ".x");
					y = this.settings.getData().getDouble("warps." + hd + ".y");
					double z = this.settings.getData().getDouble("warps." + hd + ".z");
					p.teleport(new Location(w, x, y, z));
					p.sendMessage(ChatColor.GOLD + "You have teleported to your home!");
					p.sendMessage(ChatColor.AQUA + "Code: " + ChatColor.GOLD + hd);
					return true;
				}
				p.sendMessage("Unknown command. Type /help for help.");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("god")) {
				p.sendMessage(String.valueOf(L216) + ChatColor.AQUA + "That command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("ungod")) {
				p.sendMessage(String.valueOf(L216) + ChatColor.AQUA + "That command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("heal")) {
				p.sendMessage(String.valueOf(L216) + ChatColor.AQUA + "That command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("def-kit")) {
				if (!this.teampvp.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				PlayerInventory pi = p.getInventory();
				if (this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".mvpplus")) {
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
					p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 16)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					return true;
				}
				if (this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".mvp")) {
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
					p.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 8)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					return true;
				}
				if (this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".vipplus")) {
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
					p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
					pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_SWORD, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 6)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					return true;
				}
				if (this.getConfig().getBoolean("ranks." + p.getUniqueId().toString() + ".vip")) {
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.GOLD_HELMET));
					p.getInventory().setChestplate(new ItemStack(Material.GOLD_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.GOLD_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.GOLD_BOOTS));
					pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_SWORD, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 4)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					return true;
				}
				pi.clear();
				p.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
				p.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
				p.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
				p.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
				pi.addItem(new ItemStack[]{new ItemStack(Material.STONE_SWORD, 1)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 2)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("balance")) {
				int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
				p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + Money);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("sfly")) {
				Prefix = String.valueOf(L216) + ChatColor.AQUA;
				if (!p.hasPermission("skorrloregaming.sfly")) {
					p.sendMessage(String.valueOf(Prefix) + "You don't have permission to use this command!");
					return true;
				}
				if (!this.hub.contains(p)) {
					p.sendMessage(String.valueOf(Prefix) + "You don't have permission to use this command!");
					return true;
				}
				if (this.sfly.contains(p)) {
					p.setAllowFlight(false);
					p.setFlying(false);
					p.sendMessage(String.valueOf(Prefix) + "You have successfully disabled " + ChatColor.GOLD + "Flight");
					return true;
				}
				p.setAllowFlight(true);
				p.setFlying(true);
				p.sendMessage(String.valueOf(Prefix) + "You have successfully enabled " + ChatColor.GOLD + "Flight");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("sfeed")) {
				Prefix = String.valueOf(L216) + ChatColor.AQUA;
				if (!p.hasPermission("skorrloregaming.sfeed")) {
					p.sendMessage(String.valueOf(Prefix) + "You don't have permission to use this command!");
					return true;
				}
				p.setFoodLevel(20);
				p.setSaturation(new Float(8080.0f).floatValue());
				p.sendMessage(String.valueOf(Prefix) + "You have been successfully fed!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("heal")) {
				Prefix = String.valueOf(L216) + ChatColor.AQUA;
				p.sendMessage(String.valueOf(Prefix) + "This command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("bal")) {
				int Money = this.getConfig().getInt("ranks." + p.getUniqueId().toString() + ".money");
				p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + Money);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("hub")) {
				double z;
				if (this.teampvp.contains(p)) {
					try {
						Storage.saveInventory(p, "teampvp");
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.world", p.getLocation().getWorld().getName());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.x", p.getLocation().getX());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.y", p.getLocation().getY());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.yaw", Float.valueOf(p.getLocation().getYaw()));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".teampvp.pitch", Float.valueOf(p.getLocation().getPitch()));
						this.saveConfig();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (this.survival.contains(p)) {
					try {
						Storage.saveInventory(p, "survival");
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.world", p.getLocation().getWorld().getName());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.x", p.getLocation().getX());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.y", p.getLocation().getY());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.yaw", Float.valueOf(p.getLocation().getYaw()));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".survival.pitch", Float.valueOf(p.getLocation().getPitch()));
						this.saveConfig();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				if (this.creative.contains(p)) {
					try {
						Storage.saveInventory(p, "creative");
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.world", p.getLocation().getWorld().getName());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.x", p.getLocation().getX());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.y", p.getLocation().getY());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.yaw", Float.valueOf(p.getLocation().getYaw()));
						this.getConfig().set("ranks." + p.getUniqueId().toString() + ".creative.pitch", Float.valueOf(p.getLocation().getPitch()));
						this.saveConfig();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				p.addAttachment((Plugin) this, "alphachest.chest", false);
				p.addAttachment((Plugin) this, "alphachest.keepOnDeath", true);
				ItemStack ss = new ItemStack(Material.COMPASS, 1);
				ItemMeta ssm = ss.getItemMeta();
				if (!this.hub.contains(p)) {
					this.hub.add(p);
				}
				ssm.setDisplayName(ChatColor.GREEN + "Server Selector");
				ss.setItemMeta(ssm);
				p.getInventory().clear();
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
				p.getInventory().setChestplate(new ItemStack(Material.AIR));
				p.getInventory().setLeggings(new ItemStack(Material.AIR));
				p.getInventory().setBoots(new ItemStack(Material.AIR));
				p.getInventory().addItem(new ItemStack[]{ss});
				if (this.survival.contains(p)) {
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has quit Survival!"));
					w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x = this.settings.getData().getDouble("warps.kb0000009.x");
					this.survival.remove(p);
					y = this.settings.getData().getDouble("warps.kb0000009.y");
					z = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w, x, y, z));
					p.teleport(new Location(w, x, y, z));
					p.teleport(new Location(w, x, y, z));
					p.teleport(new Location(w, x, y, z));
				}
				if (this.creative.contains(p)) {
					p.getInventory().clear();
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has quit Creative!"));
					w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x = this.settings.getData().getDouble("warps.kb0000009.x");
					this.creative.remove(p);
					y = this.settings.getData().getDouble("warps.kb0000009.y");
					z = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w, x, y, z));
				}
				if (this.teampvp.contains(p)) {
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has quit TeamPvP!"));
					w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x = this.settings.getData().getDouble("warps.kb0000009.x");
					this.teampvp.remove(p);
					y = this.settings.getData().getDouble("warps.kb0000009.y");
					z = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w, x, y, z));
				}
				if (this.skyfight.contains(p)) {
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has quit SkyFight!"));
					w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x = this.settings.getData().getDouble("warps.kb0000009.x");
					this.skyfight.remove(p);
					y = this.settings.getData().getDouble("warps.kb0000009.y");
					z = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w, x, y, z));
				}
				if (this.zahc.contains(p)) {
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " has quit ZAHC"));
					w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x = this.settings.getData().getDouble("warps.kb0000009.x");
					this.zahc.remove(p);
					y = this.settings.getData().getDouble("warps.kb0000009.y");
					z = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w, x, y, z));
				}
				w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
				x = this.settings.getData().getDouble("warps.kb0000009.x");
				y = this.settings.getData().getDouble("warps.kb0000009.y");
				z = this.settings.getData().getDouble("warps.kb0000009.z");
				p.teleport(new Location(w, x, y, z));
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("kit")) {
				ItemStack Bow;
				if (this.teampvp.contains(p)) {
					if (args.length != 1) {
						p.sendMessage(ChatColor.GOLD + "------<" + ChatColor.RED + "Current Kits" + ChatColor.GOLD + ">------");
						p.sendMessage(ChatColor.RED + "[1] " + ChatColor.GREEN + "Starter");
						p.sendMessage(ChatColor.RED + "[2] " + ChatColor.GREEN + "Potions");
						p.sendMessage(ChatColor.RED + "[3] " + ChatColor.GREEN + "Donator");
						p.sendMessage(ChatColor.GOLD + "--------------------------");
						return true;
					}
					if (args[0].equalsIgnoreCase("starter")) {
						ItemStack HELMET = MaterialManager.Create(Material.IRON_HELMET, ChatColor.GREEN + "Starter Helmet", 1, 0);
						ItemStack CHESTPLATE = MaterialManager.Create(Material.IRON_CHESTPLATE, ChatColor.GREEN + "Starter Chestplate", 1, 0);
						ItemStack LEGGINGS = MaterialManager.Create(Material.IRON_LEGGINGS, ChatColor.GREEN + "Starter Leggings", 1, 0);
						ItemStack BOOTS = MaterialManager.Create(Material.IRON_BOOTS, ChatColor.GREEN + "Starter Boots", 1, 0);
						ItemStack SWORD = MaterialManager.Create(Material.IRON_SWORD, ChatColor.GREEN + "Starter Sword", 1, 0);
						ItemStack BOW = MaterialManager.Create(Material.BOW, ChatColor.GREEN + "Starter Bow", 1, 0);
						ItemStack STEAK = MaterialManager.Create(Material.COOKED_BEEF, ChatColor.GREEN + "Starter Beef", 8, 0);
						ItemStack ARROW = MaterialManager.Create(Material.ARROW, ChatColor.GREEN + "Starter Arrow", 64, 0);
						p.getInventory().addItem(new ItemStack[]{HELMET});
						p.getInventory().addItem(new ItemStack[]{CHESTPLATE});
						p.getInventory().addItem(new ItemStack[]{LEGGINGS});
						p.getInventory().addItem(new ItemStack[]{BOOTS});
						p.getInventory().addItem(new ItemStack[]{SWORD});
						p.getInventory().addItem(new ItemStack[]{BOW});
						p.getInventory().addItem(new ItemStack[]{STEAK});
						p.getInventory().addItem(new ItemStack[]{ARROW});
						p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "Starter" + ChatColor.GREEN + "!");
						return true;
					}
					if (args[0].equalsIgnoreCase("potions")) {
						if (this.potions_cooldown.contains(p)) {
							p.sendMessage(ChatColor.RED + "You must wait " + ChatColor.GOLD + "5" + ChatColor.RED + " minutes before using this kit again!");
							return true;
						}
						ItemStack STRENGTH = MaterialManager.Create(Material.POTION, null, 1, 8201);
						ItemStack SPEED = MaterialManager.Create(Material.POTION, null, 1, 8194);
						ItemStack FIRE = MaterialManager.Create(Material.POTION, null, 1, 8227);
						ItemStack REGEN = MaterialManager.Create(Material.POTION, null, 1, 8257);
						p.getInventory().addItem(new ItemStack[]{STRENGTH});
						p.getInventory().addItem(new ItemStack[]{SPEED});
						p.getInventory().addItem(new ItemStack[]{FIRE});
						p.getInventory().addItem(new ItemStack[]{REGEN});
						p.sendMessage(String.valueOf(L216) + "You have been given kit: " + ChatColor.GOLD + "Potions" + ChatColor.GREEN + "!");
						final Player pf = p;
						this.potions_cooldown.add(pf);
						this.getServer().getScheduler().scheduleAsyncDelayedTask((Plugin) this, new Runnable() {

							@Override
							public void run() {
								if (SkorrloreGaming.this.teampvp.contains(pf)) {
									pf.sendMessage(ChatColor.RED + "You can now use the kit " + ChatColor.GOLD + "Potions" + ChatColor.RED + " again.");
								}
								SkorrloreGaming.this.potions_cooldown.remove(pf);
							}
						}, 6000L);
						return true;
					}
					if (args[0].equalsIgnoreCase("donator")) {
						this.Kit(p);
						return true;
					}
					p.sendMessage(ChatColor.GOLD + "------<" + ChatColor.RED + "Current Kits" + ChatColor.GOLD + ">------");
					p.sendMessage(ChatColor.RED + "[1] " + ChatColor.GREEN + "Starter");
					p.sendMessage(ChatColor.RED + "[2] " + ChatColor.GREEN + "Potions");
					p.sendMessage(ChatColor.RED + "[3] " + ChatColor.GREEN + "Donator");
					p.sendMessage(ChatColor.GOLD + "--------------------------");
					return true;
				}
				if (this.hub.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command when you are in the hub");
					return true;
				}
				if (this.creative.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing Creative");
					return true;
				}
				if (this.creative.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing Creative");
					return true;
				}
				if (args.length == 0) {
					Survival.listAllKits(player);
					return true;
				}
				Prefix = String.valueOf(L216) + ChatColor.AQUA;
				if (args[0].equalsIgnoreCase("default")) {
					PlayerInventory pi = player.getInventory();
					pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_SWORD, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.STONE_PICKAXE, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.STONE_AXE, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.STONE_SPADE, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_HELMET, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_CHESTPLATE, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_LEGGINGS, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
					player.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Default");
					return true;
				}
				if (args[0].equalsIgnoreCase("vip")) {
					if (this.getRank(p).equalsIgnoreCase("vip")) {
						ItemStack Sword = Item.Create(new ItemStack(Material.IRON_SWORD, 1), "VIP Sword", Enchantment.DAMAGE_ALL, 4, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						Bow = Item.Create(new ItemStack(Material.BOW, 1), "VIP Bow", Enchantment.ARROW_DAMAGE, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Arrow2 = Item.Create(new ItemStack(Material.ARROW, 64), "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
						ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
						ItemStack Helmet = Item.Create(new ItemStack(Material.IRON_HELMET, 1), "VIP Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Chestplate = Item.Create(new ItemStack(Material.IRON_CHESTPLATE, 1), "VIP Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Leggings = Item.Create(new ItemStack(Material.IRON_LEGGINGS, 1), "VIP Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Boots = Item.Create(new ItemStack(Material.IRON_BOOTS, 1), "VIP Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						p.getInventory().addItem(new ItemStack[]{Sword});
						p.getInventory().addItem(new ItemStack[]{Bow});
						p.getInventory().addItem(new ItemStack[]{Arrow2});
						p.getInventory().addItem(new ItemStack[]{Steak});
						p.getInventory().addItem(new ItemStack[]{Helmet});
						p.getInventory().addItem(new ItemStack[]{Chestplate});
						p.getInventory().addItem(new ItemStack[]{Leggings});
						p.getInventory().addItem(new ItemStack[]{Boots});
						p.sendMessage(String.valueOf(Prefix) + "You have been given kit: " + ChatColor.GOLD + "VIP");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + this.GOLD + "VIP" + this.RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("vip+")) {
					if (this.getRank(p).equalsIgnoreCase("vipx")) {
						ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), "VIP+ Sword", Enchantment.DAMAGE_ALL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						Bow = Item.Create(new ItemStack(Material.BOW, 1), "VIP+ Bow", Enchantment.ARROW_DAMAGE, 4, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Arrow3 = Item.Create(new ItemStack(Material.ARROW, 64), "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
						ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
						ItemStack Helmet = Item.Create(new ItemStack(Material.IRON_HELMET, 1), "VIP+ Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Chestplate = Item.Create(new ItemStack(Material.IRON_CHESTPLATE, 1), "VIP+ Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Leggings = Item.Create(new ItemStack(Material.IRON_LEGGINGS, 1), "VIP+ Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Boots = Item.Create(new ItemStack(Material.IRON_BOOTS, 1), "VIP+ Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						p.getInventory().addItem(new ItemStack[]{Sword});
						p.getInventory().addItem(new ItemStack[]{Bow});
						p.getInventory().addItem(new ItemStack[]{Arrow3});
						p.getInventory().addItem(new ItemStack[]{Steak});
						p.getInventory().addItem(new ItemStack[]{Helmet});
						p.getInventory().addItem(new ItemStack[]{Chestplate});
						p.getInventory().addItem(new ItemStack[]{Leggings});
						p.getInventory().addItem(new ItemStack[]{Boots});
						p.sendMessage(String.valueOf(Prefix) + "You have been given kit: " + ChatColor.GOLD + "VIP+");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + this.GOLD + "VIP+" + this.RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("mvp")) {
					if (this.getRank(p).equalsIgnoreCase("mvp")) {
						ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), "MVP Sword", Enchantment.DAMAGE_ALL, 4, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						Bow = Item.Create(new ItemStack(Material.BOW, 1), "MVP Bow", Enchantment.ARROW_DAMAGE, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Arrow4 = Item.Create(new ItemStack(Material.ARROW, 64), "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
						ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
						ItemStack Helmet = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), "MVP Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Chestplate = Item.Create(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), "MVP Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Leggings = Item.Create(new ItemStack(Material.DIAMOND_LEGGINGS, 1), "MVP Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Boots = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), "MVP Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 2, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						p.getInventory().addItem(new ItemStack[]{Sword});
						p.getInventory().addItem(new ItemStack[]{Bow});
						p.getInventory().addItem(new ItemStack[]{Arrow4});
						p.getInventory().addItem(new ItemStack[]{Steak});
						p.getInventory().addItem(new ItemStack[]{Helmet});
						p.getInventory().addItem(new ItemStack[]{Chestplate});
						p.getInventory().addItem(new ItemStack[]{Leggings});
						p.getInventory().addItem(new ItemStack[]{Boots});
						p.sendMessage(String.valueOf(Prefix) + "You have been given kit: " + ChatColor.GOLD + "MVP");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + this.GOLD + "MVP" + this.RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("mvp+")) {
					if (this.getRank(p).equalsIgnoreCase("mvpx")) {
						ItemStack Sword = Item.Create(new ItemStack(Material.DIAMOND_SWORD, 1), "MVP+ Sword", Enchantment.DAMAGE_ALL, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						Bow = Item.Create(new ItemStack(Material.BOW, 1), "MVP+ Bow", Enchantment.ARROW_DAMAGE, 5, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Arrow5 = Item.Create(new ItemStack(Material.ARROW, 64), "Arrow", Enchantment.LUCK, 0, Enchantment.LUCK, 0, Enchantment.LUCK, 0);
						ItemStack Steak = Item.Create(new ItemStack(Material.COOKED_BEEF, 32), "Steak", Enchantment.LUCK, 0, Enchantment.DAMAGE_ALL, 3, Enchantment.LUCK, 0);
						ItemStack Helmet = Item.Create(new ItemStack(Material.DIAMOND_HELMET, 1), "MVP+ Helmet", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Chestplate = Item.Create(new ItemStack(Material.DIAMOND_CHESTPLATE, 1), "MVP+ Chestplate", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Leggings = Item.Create(new ItemStack(Material.DIAMOND_LEGGINGS, 1), "MVP+ Leggings", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						ItemStack Boots = Item.Create(new ItemStack(Material.DIAMOND_BOOTS, 1), "MVP+ Boots", Enchantment.PROTECTION_ENVIRONMENTAL, 3, Enchantment.DURABILITY, 3, Enchantment.LUCK, 0);
						p.getInventory().addItem(new ItemStack[]{Sword});
						p.getInventory().addItem(new ItemStack[]{Bow});
						p.getInventory().addItem(new ItemStack[]{Arrow5});
						p.getInventory().addItem(new ItemStack[]{Steak});
						p.getInventory().addItem(new ItemStack[]{Helmet});
						p.getInventory().addItem(new ItemStack[]{Chestplate});
						p.getInventory().addItem(new ItemStack[]{Leggings});
						p.getInventory().addItem(new ItemStack[]{Boots});
						p.sendMessage(String.valueOf(Prefix) + "You have been given kit: " + ChatColor.GOLD + "MVP+");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + this.GOLD + "MVP+" + this.RED + " to use this command!");
					return true;
				}
				Survival.listAllKits(player);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("afk")) {
				if (this.afk.contains(p)) {
					this.afk.remove(p);
					Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " is no longer afk!"));
					return true;
				}
				this.afk.add(p);
				Bukkit.broadcastMessage((String.valueOf(L216) + ChatColor.GOLD + this.getNickname(p) + ChatColor.AQUA + " is now afk!"));
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("kits")) {
				Survival.listAllKits(player);
			}
			return false;
		}
		player.sendMessage(ChatColor.GREEN + "You must wait " + ChatColor.LIGHT_PURPLE + "15" + ChatColor.GREEN + " seconds before using any commands.");
		return false;
	}

}

