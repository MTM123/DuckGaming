package com.skorrloregaming.main;

import com.skorrloregaming.main.Factions;
import com.skorrloregaming.main.Mwapper;
import com.skorrloregaming.main.SettingsManager;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.Vector;

public class SkorrloreGaming
		extends JavaPlugin
		implements Listener {
	public static Plugin SkorrloreGaming;
	public SettingsManager settings = SettingsManager.getInstance();
	public Factions factions = Factions.getInstance();
	public long RANDOM = 0L;
	public static long CURR_AUCTION_CODE;
	public static int CURR_AUCTION_PRICE;
	public static int CURR_AUCTION_TIME;
	public static String CURR_AUCTION_STARTER_NAME;
	public static ItemStack CURR_AUCTION_ITEM;
	public static final String AUCTION_DISPLAY;
	public static final String AUCTION_START;
	public static final String AUCTION_ITEM;
	public static final String AUCTION_PRICE;
	public static final String AUCTION_TIME;
	public static final String AUCTION_CODEC;
	public static int TIME_DELAY;
	public static boolean AUCTION_OCCURING;
	public static int CURR_AUCTION_BID;
	public static String LAST_BID_PLAYER_NAME;
	public static int LAST_BID_AMOUNT;
	public ArrayList<Player> creative = new ArrayList();
	public ArrayList<Player> doi = new ArrayList();
	public ArrayList<Player> stpt = new ArrayList();
	public ArrayList<Player> vip = new ArrayList();
	public ArrayList<Player> bedrock = new ArrayList();
	public ChatColor AQUA = ChatColor.AQUA;
	public ChatColor RED = ChatColor.RED;
	public ChatColor GREEN = ChatColor.GREEN;
	public ChatColor DARK_GREEN = ChatColor.DARK_GREEN;
	public ChatColor GOLD = ChatColor.GOLD;
	public ChatColor DARK_RED = ChatColor.DARK_RED;
	public ArrayList<Player> sw = new ArrayList();
	public ArrayList<Player> rpg = new ArrayList();
	public ArrayList<Player> survival = new ArrayList();
	public ArrayList<Player> teampvp = new ArrayList();
	public ArrayList<Player> zahc = new ArrayList();
	public ArrayList<Player> sesmen = new ArrayList();
	public ArrayList<Player> pg = new ArrayList();
	public ArrayList<Player> locked = new ArrayList();
	public ArrayList<Player> tagged = new ArrayList();
	public ArrayList<Player> hub = new ArrayList();
	public ArrayList<Player> charged = new ArrayList();
	public ItemStack AIR = new ItemStack(Material.AIR);
	public ArrayList<Player> teampvpRespawn = new ArrayList();
	public final double defaultWalkSpeed = 0.2;
	public final double defaultSprintSpeed = 0.3;
	public final double defaultSuperSprintSpeed = 0.5;
	public final double chargedSuperSprintSpeed = 1.0;
	private Map<Player, ItemStack[]> pArmor = new HashMap<Player, ItemStack[]>();
	private Map<Player, ItemStack[]> pItems = new HashMap<Player, ItemStack[]>();
	private List<Player> CC = new ArrayList<Player>();
	public ArrayList<Player> skywars = new ArrayList();
	public ArrayList<Player> afk = new ArrayList();

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

	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents((Listener) this, (Plugin) this);
		this.settings.setup((Plugin) this);
		this.factions.setup((Plugin) this);
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();
		this.getConfig().set("Auction.Running", false);
		this.saveConfig();
	}

	public void onDisable() {
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
		Bukkit.broadcastMessage((String) (ChatColor.RED + "[" + ChatColor.GRAY + "-----------------------------------------------------------" + ChatColor.RED + "]"));
		Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "An auction has been started by " + ChatColor.GOLD + CURR_AUCTION_STARTER_NAME + ChatColor.AQUA + "!"));
		Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "Item: " + ChatColor.GOLD + ITEM.getType()));
		Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "Price: " + ChatColor.GOLD + PRICE));
		Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "Time: " + ChatColor.GOLD + VMO));
		Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "Codec: " + ChatColor.GOLD + SYS_DATACODE));
		Bukkit.broadcastMessage((String) (ChatColor.RED + "[" + ChatColor.GRAY + "-----------------------------------------------------------" + ChatColor.RED + "]"));
		this.getServer().getScheduler().scheduleSyncDelayedTask((Plugin) this, new Runnable() {

			@Override
			public void run() {
				getConfig().set("Auction.Running", false);
				saveConfig();
				if (getConfig().getBoolean("Auction.Cancelled")) {
					getConfig().set("Auction.Cancelled", false);
					saveConfig();
					return;
				}
				if (!getConfig().getString("Auction.Bids.Last.Name").equals("Console")) {
					if (getConfig().getInt("ranks." + Player2.getName() + ".money") >= getConfig().getInt("Auction.Bids.Last.Amount")) {
						int Amount = getConfig().getInt("Auction.Bids.Last.Amount");
						getConfig().set("Auction.Bids.Last.Amount", Amount);
						getConfig().set("Auction.Bids.Last.Name", Player2.getName());
						saveConfig();
						Player Winner = Bukkit.getPlayer((String) getConfig().getString("Auction.Bids.Last.Name"));
						if (Winner == null) {
							Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "The winner of the auction left the server! The auction has been cancelled!"));
							return;
						}
						MoneyTake(Winner, getConfig().getInt("Auction.Bids.Last.Amount"));
						Winner.getInventory().addItem(new ItemStack[]{ITEM});
						Player2.getInventory().remove(ITEM);
						Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "The winner of the auction is " + ChatColor.GOLD + getConfig().getString("Auction.Bids.Last.Name")));
					} else {
						Player2.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You do not have enough money to purchase this item!");
						Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "The winner did not have enough money to purchase the auction item!"));
					}
				}
				Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "The current auction has ended!"));
			}
		}, (long) this.getConfig().getInt("Auction.Time"));
	}

	@EventHandler
	public void RemoveCompass(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (this.hub.contains(p)) {
			return;
		}
		p.getInventory().remove(new ItemStack(Material.COMPASS));
		p.getInventory().remove(new ItemStack(Material.COMPASS, 1));
		p.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.COMPASS)});
		p.getInventory().removeItem(new ItemStack[]{new ItemStack(Material.COMPASS, 1)});
	}

	@EventHandler
	public void Scoreboard(PlayerMoveEvent e) {
		if (!this.teampvp.contains(e.getPlayer())) {
			Player p = e.getPlayer();
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			return;
		}
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("SkorrloreGaming", "dummy");
		Score kills = objective.getScore(ChatColor.GOLD + "Kills: ");
		Score money = objective.getScore(ChatColor.GOLD + "Money: ");
		Score deaths = objective.getScore(ChatColor.GOLD + "Deaths: ");
		Score killstreak = objective.getScore(ChatColor.GOLD + "Killstreak: ");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GREEN + "Current Status");
		kills.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".kills"));
		money.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".money"));
		deaths.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".deaths"));
		killstreak.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".ks1"));
		Player p = e.getPlayer();
		p.setScoreboard(board);
	}

	@EventHandler
	public void Scoreboard2(PlayerMoveEvent e) {
		if (!this.survival.contains(e.getPlayer())) {
			Player p = e.getPlayer();
			p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			return;
		}
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("SkorrloreGaming", "dummy");
		Score kills = objective.getScore(ChatColor.GOLD + "Kills: ");
		Score money = objective.getScore(ChatColor.GOLD + "Money: ");
		Score deaths = objective.getScore(ChatColor.GOLD + "Deaths: ");
		Score killstreak = objective.getScore(ChatColor.GOLD + "Killstreak: ");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName(ChatColor.GREEN + "Current Status");
		kills.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".kills"));
		money.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".money"));
		deaths.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".deaths"));
		killstreak.setScore(this.getConfig().getInt("ranks." + e.getPlayer().getName() + ".ks1"));
		Player p = e.getPlayer();
		p.setScoreboard(board);
	}

	@EventHandler
	public void TeamPvP_REGISTER_KILL(PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!this.teampvp.contains(e.getEntity().getPlayer())) {
			return;
		}
		Player p = e.getEntity().getPlayer();
		Player k = e.getEntity().getKiller();
		int oldKsValue = this.getConfig().getInt("ranks." + k.getName() + ".ks1");
		int newKsValue = oldKsValue + 1;
		this.getConfig().set("ranks." + k.getName() + ".ks1", newKsValue);
		this.saveConfig();
		this.getConfig().set("ranks." + p.getName() + ".ks1", 0);
		this.saveConfig();
		int oldDeathsValue = this.getConfig().getInt("ranks." + p.getName() + ".deaths");
		int newDeathsValue = oldDeathsValue + 1;
		this.getConfig().set("ranks." + p.getName() + ".deaths", newDeathsValue);
		this.saveConfig();
		int oldKillsValue = this.getConfig().getInt("ranks." + k.getName() + ".kills");
		int newKillsValue = oldKillsValue + 1;
		this.getConfig().set("ranks." + k.getName() + ".kills", newKillsValue);
		this.saveConfig();
		int oldMoneyValue = this.getConfig().getInt("ranks." + k.getName() + ".money");
		int newMoneyValue = oldMoneyValue + 5;
		this.getConfig().set("ranks." + k.getName() + ".money", newMoneyValue);
		this.saveConfig();
		k.sendMessage(ChatColor.GREEN + "You have been given " + ChatColor.GOLD + "5" + ChatColor.GREEN + " dollars for killing " + ChatColor.GOLD + p.getName());
	}

	@EventHandler
	public void Factions_REGISTER_KILL(PlayerDeathEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		if (!this.survival.contains(e.getEntity().getPlayer())) {
			return;
		}
		Player p = e.getEntity().getPlayer();
		Player k = e.getEntity().getKiller();
		int oldMoneyValue = this.getConfig().getInt("ranks." + k.getName() + ".money");
		int newMoneyValue = oldMoneyValue + 5;
		this.getConfig().set("ranks." + k.getName() + ".money", newMoneyValue);
		this.saveConfig();
		k.sendMessage(ChatColor.GREEN + "You have been given " + ChatColor.GOLD + "5" + ChatColor.GREEN + " dollars for killing " + ChatColor.GOLD + p.getName());
	}

	public void repairAll(Player p) {
		ItemStack items;
		ItemStack[] arritemStack = p.getInventory().getContents();
		int n = arritemStack.length;
		int n2 = 0;
		while (n2 < n) {
			items = arritemStack[n2];
			if (items instanceof Repairable) {
				items.setDurability((short) 0);
			}
			++n2;
		}
		arritemStack = p.getEquipment().getArmorContents();
		n = arritemStack.length;
		n2 = 0;
		while (n2 < n) {
			items = arritemStack[n2];
			if (items instanceof Repairable) {
				items.setDurability((short) 0);
			}
			++n2;
		}
	}

	public void MoneyGive(Player player, int amount) {
		Player p = player;
		int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
		this.getConfig().set("ranks." + p.getName() + ".money", (Money += amount));
		this.saveConfig();
	}

	public void MoneyTake(Player player, int amount) {
		Player p = player;
		int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
		this.getConfig().set("ranks." + p.getName() + ".money", (Money -= amount));
		this.saveConfig();
	}

	public void MoneyReset(Player player) {
		Player p = player;
		int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
		Money = -1;
		this.getConfig().set("ranks." + p.getName() + ".money", Money);
		this.saveConfig();
	}

	@EventHandler
	public void onPlayerPickupItemEvent(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (!this.teampvp.contains(p)) {
			return;
		}
		if (e.getItem().getItemStack().hasItemMeta()) {
			e.setCancelled(true);
			p.sendMessage(ChatColor.GREEN + "You cannot pick up kit items from other players!");
			return;
		}
		e.setCancelled(true);
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
	public void TeamPvP_SHOP(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		Player p = e.getPlayer();
		PlayerInventory pi = p.getInventory();
		int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
		if (e.getClickedBlock().getState() instanceof Sign) {
			Sign s = (Sign) e.getClickedBlock().getState();
			if (s.getLine(1).equalsIgnoreCase("[Kit]")) {
				if (s.getLine(2).equalsIgnoreCase("Recruit")) {
					p.getInventory().setMaxStackSize(16);
					p.getInventory().clear();
					p.getInventory().setHeldItemSlot(8);
					ItemStack HELMET = new ItemStack(Material.LEATHER_HELMET, 1);
					ItemMeta A = HELMET.getItemMeta();
					A.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					HELMET.setItemMeta(A);
					ItemStack CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
					ItemMeta B = CHESTPLATE.getItemMeta();
					B.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					CHESTPLATE.setItemMeta(B);
					ItemStack LEGGINGS = new ItemStack(Material.LEATHER_LEGGINGS, 1);
					ItemMeta C = LEGGINGS.getItemMeta();
					C.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					LEGGINGS.setItemMeta(C);
					ItemStack BOOTS = new ItemStack(Material.LEATHER_BOOTS, 1);
					ItemMeta D = BOOTS.getItemMeta();
					D.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOOTS.setItemMeta(D);
					ItemStack SWORD = new ItemStack(Material.IRON_SWORD, 1);
					ItemMeta E = SWORD.getItemMeta();
					E.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					SWORD.setItemMeta(E);
					ItemStack BOW = new ItemStack(Material.BOW, 1);
					ItemMeta F = BOW.getItemMeta();
					F.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOW.setItemMeta(F);
					p.getInventory().setHelmet(HELMET);
					p.getInventory().setChestplate(CHESTPLATE);
					p.getInventory().setLeggings(LEGGINGS);
					p.getInventory().setBoots(BOOTS);
					p.getInventory().setItemInHand(SWORD);
					p.getInventory().addItem(new ItemStack[]{BOW});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 8)});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Recruit");
					return;
				}
				if (s.getLine(2).equalsIgnoreCase("Warrior")) {
					p.getInventory().setMaxStackSize(16);
					p.getInventory().clear();
					p.getInventory().setHeldItemSlot(8);
					ItemStack HELMET = new ItemStack(Material.CHAINMAIL_HELMET, 1);
					ItemMeta A = HELMET.getItemMeta();
					A.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					HELMET.setItemMeta(A);
					ItemStack CHESTPLATE = new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1);
					ItemMeta B = CHESTPLATE.getItemMeta();
					B.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					CHESTPLATE.setItemMeta(B);
					ItemStack LEGGINGS = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
					ItemMeta C = LEGGINGS.getItemMeta();
					C.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					LEGGINGS.setItemMeta(C);
					ItemStack BOOTS = new ItemStack(Material.CHAINMAIL_BOOTS, 1);
					ItemMeta D = BOOTS.getItemMeta();
					D.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOOTS.setItemMeta(D);
					ItemStack SWORD = new ItemStack(Material.STONE_SWORD, 1);
					ItemMeta E = SWORD.getItemMeta();
					E.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					SWORD.setItemMeta(E);
					ItemStack BOW = new ItemStack(Material.BOW, 1);
					ItemMeta F = BOW.getItemMeta();
					F.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOW.setItemMeta(F);
					p.getInventory().setHelmet(HELMET);
					p.getInventory().setChestplate(CHESTPLATE);
					p.getInventory().setLeggings(LEGGINGS);
					p.getInventory().setBoots(BOOTS);
					p.getInventory().setItemInHand(SWORD);
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 8)});
					p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Warrior");
					return;
				}
				if (s.getLine(2).equalsIgnoreCase("Tank")) {
					p.getInventory().setMaxStackSize(16);
					p.getInventory().clear();
					p.getInventory().setHeldItemSlot(8);
					ItemStack HELMET = new ItemStack(Material.IRON_HELMET, 1);
					ItemMeta A = HELMET.getItemMeta();
					A.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					HELMET.setItemMeta(A);
					ItemStack CHESTPLATE = new ItemStack(Material.IRON_CHESTPLATE, 1);
					ItemMeta B = CHESTPLATE.getItemMeta();
					B.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					CHESTPLATE.setItemMeta(B);
					ItemStack LEGGINGS = new ItemStack(Material.IRON_LEGGINGS, 1);
					ItemMeta C = LEGGINGS.getItemMeta();
					C.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					LEGGINGS.setItemMeta(C);
					ItemStack BOOTS = new ItemStack(Material.IRON_BOOTS, 1);
					ItemMeta D = BOOTS.getItemMeta();
					D.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOOTS.setItemMeta(D);
					ItemStack SWORD = new ItemStack(Material.WOOD_SWORD, 1);
					ItemMeta E = SWORD.getItemMeta();
					E.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					SWORD.setItemMeta(E);
					ItemStack BOW = new ItemStack(Material.BOW, 1);
					ItemMeta F = BOW.getItemMeta();
					F.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOW.setItemMeta(F);
					p.getInventory().setHelmet(HELMET);
					p.getInventory().setChestplate(CHESTPLATE);
					p.getInventory().setLeggings(LEGGINGS);
					p.getInventory().setBoots(BOOTS);
					p.getInventory().setItemInHand(SWORD);
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 8)});
					p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Tank");
					return;
				}
				if (s.getLine(2).equalsIgnoreCase("Archer")) {
					p.getInventory().setMaxStackSize(16);
					p.getInventory().clear();
					p.getInventory().setHeldItemSlot(8);
					ItemStack HELMET = new ItemStack(Material.LEATHER_HELMET, 1);
					ItemMeta A = HELMET.getItemMeta();
					A.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					HELMET.setItemMeta(A);
					ItemStack CHESTPLATE = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
					ItemMeta B = CHESTPLATE.getItemMeta();
					B.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					CHESTPLATE.setItemMeta(B);
					ItemStack LEGGINGS = new ItemStack(Material.LEATHER_LEGGINGS, 1);
					ItemMeta C = LEGGINGS.getItemMeta();
					C.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					LEGGINGS.setItemMeta(C);
					ItemStack BOOTS = new ItemStack(Material.LEATHER_BOOTS, 1);
					ItemMeta D = BOOTS.getItemMeta();
					D.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOOTS.setItemMeta(D);
					ItemStack SWORD = new ItemStack(Material.WOOD_SWORD, 1);
					ItemMeta E = SWORD.getItemMeta();
					E.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					SWORD.setItemMeta(E);
					ItemStack BOW = new ItemStack(Material.BOW, 1);
					ItemMeta F = BOW.getItemMeta();
					F.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
					F.addEnchant(Enchantment.DURABILITY, 10, true);
					F.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Kit" + ChatColor.DARK_RED + "]");
					BOW.setItemMeta(F);
					p.getInventory().setHelmet(HELMET);
					p.getInventory().setChestplate(CHESTPLATE);
					p.getInventory().setLeggings(LEGGINGS);
					p.getInventory().setBoots(BOOTS);
					p.getInventory().setItemInHand(BOW);
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 8)});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.ARROW, 64)});
					p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Archer");
					return;
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
				String sv = s.getLine(4);
				String raw = s.getLine(2);
				String material = "Material." + s.getLine(2);
				int mp = Integer.parseInt("sv");
				Material m = Material.getMaterial((String) material);
				ItemStack item = new ItemStack(m, value);
				if (!p.getInventory().contains(item)) {
					p.sendMessage(ChatColor.RED + "You do not have enough " + ChatColor.GOLD + raw + ChatColor.RED + "!");
					return;
				}
				p.getInventory().removeItem(new ItemStack[]{item});
				this.getConfig().set("ranks." + p.getName() + ".money", (Money += mp));
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

	@EventHandler
	public void AFK(PlayerPickupItemEvent e) {
		if (this.afk.contains(e.getPlayer())) {
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (this.survival.contains(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (this.survival.contains(p)) {
			Material type = e.getBlockPlaced().getType();
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
		this.getConfig().set("Servers.Factions.Banned.Players.0", "Default");
		this.getConfig().set("Servers.GTA.Banned.Players.0", "Default");
		this.getConfig().set("Servers.TeamPvP.Banned.Players.0", "Default");
		this.saveConfig();
		if (e.getPlayer().hasPlayedBefore()) {
			Boolean timev = this.getConfig().getBoolean("ranks." + e.getPlayer() + ".kitVip");
			Boolean timeb = this.getConfig().getBoolean("ranks." + e.getPlayer() + ".kitBedrock");
			if (timev.booleanValue()) {
				this.locked.add(p);
			}
			if (timeb.booleanValue()) {
				this.locked.add(p);
			}
		}
	}

	@EventHandler
	public void ServerSelector1(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore()) {
			this.getConfig().set("level." + p.getName(), 0);
			this.saveConfig();
		}
		int pl = this.getConfig().getInt("level." + p.getName());
		p.setLevel(pl);
	}

	@EventHandler
	public void ServerSelector(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		this.hub.add(p);
		if (!p.hasPlayedBefore()) {
			this.getConfig().set("ranks." + p.getName() + ".saveItems", false);
			this.getConfig().set("level." + p.getName(), 0);
			this.saveConfig();
		}
		int pl = this.getConfig().getInt("level." + p.getName());
		p.setLevel(pl);
		p.sendMessage(ChatColor.GRAY + "------------------------------------>");
		p.sendMessage(this.GOLD + "Welcome back, " + this.GREEN + p.getName());
		p.sendMessage(this.AQUA + "Rules: " + ChatColor.GOLD + "/rules");
		p.sendMessage(this.AQUA + "Your current level: " + ChatColor.GOLD + pl);
		p.sendMessage(ChatColor.GRAY + "------------------------------------>");
		if (p.getName().equals("jacobtheman626")) {
			p.addAttachment((Plugin) this, "bukkit.command.gamemode", true);
			p.addAttachment((Plugin) this, "bukkit.command.enchant", true);
			p.addAttachment((Plugin) this, "bukkit.command.say", true);
		}
		p.addAttachment((Plugin) this, "bukkit.command.me", false);
		p.addAttachment((Plugin) this, "bukkit.command.kill", false);
		p.addAttachment((Plugin) this, "minecraft.command.kill", false);
		PlayerInventory pi = p.getInventory();
		if (this.getConfig().getBoolean("ranks." + p.getName() + ".saveItems")) {
			p.sendMessage(ChatColor.GREEN + "Your items have been successfully saved!");
		} else if (!this.getConfig().getBoolean("ranks." + p.getName() + ".saveItems")) {
			pi.clear();
			p.getInventory().setHelmet(new ItemStack(Material.AIR));
			p.getInventory().setChestplate(new ItemStack(Material.AIR));
			p.getInventory().setLeggings(new ItemStack(Material.AIR));
			p.getInventory().setBoots(new ItemStack(Material.AIR));
			ItemStack ss = new ItemStack(Material.COMPASS, 1);
			ItemMeta ssm = ss.getItemMeta();
			ssm.setDisplayName(ChatColor.GREEN + "Server Selector");
			ss.setItemMeta(ssm);
			pi.addItem(new ItemStack[]{ss});
		}
		Player player = e.getPlayer();
		e.setJoinMessage(ChatColor.GOLD + "[" + ChatColor.GREEN + "+" + ChatColor.GOLD + "] " + ChatColor.AQUA + e.getPlayer().getName());
		this.hubTeleportOnJoin(player);
		this.getConfig().set("ranks." + player.getName() + ".guest", true);
		this.saveConfig();
		if (!e.getPlayer().hasPlayedBefore()) {
			this.getConfig().set("ranks." + p.getName() + ".overlord", false);
			this.getConfig().set("ranks." + p.getName() + ".mvpplus", false);
			this.getConfig().set("ranks." + p.getName() + ".mvp", false);
			this.getConfig().set("ranks." + p.getName() + ".vipplus", false);
			this.getConfig().set("ranks." + player.getName() + ".kitBedrock", false);
			this.getConfig().set("ranks." + player.getName() + ".kitVip", false);
			this.getConfig().set("ranks." + player.getName() + ".homes", 0);
			this.getConfig().set("ranks." + player.getName() + ".vip", false);
			this.getConfig().set("ranks." + player.getName() + ".bedrock", false);
			this.getConfig().set("ranks." + player.getName() + ".builder", false);
			this.getConfig().set("ranks." + player.getName() + ".helper", false);
			this.getConfig().set("ranks." + player.getName() + ".moderator", false);
			this.getConfig().set("ranks." + player.getName() + ".admin", false);
			this.getConfig().set("ranks." + player.getName() + ".coowner", false);
			this.getConfig().set("ranks." + player.getName() + ".kills", 0);
			this.getConfig().set("ranks." + player.getName() + ".clan.inClan", true);
			int PlayerAmount = this.getConfig().getInt("clan.1.PlayerAmount");
			this.getConfig().set("ranks." + p.getName() + ".clan.codec", 1);
			this.getConfig().set("clan.1.DisplayName", "Default");
			this.getConfig().set("clan.1.ClanOwner", "SkorrloreGaming");
			this.getConfig().set("clan.1.PlayerAmount", (PlayerAmount + 1));
			this.getConfig().set("clan.1.players." + p.getName() + ".enabled", true);
			this.getConfig().set("ranks." + player.getName() + ".owner", false);
			this.getConfig().set("ranks." + player.getName() + ".mega", false);
			this.getConfig().set("ranks." + player.getName() + ".diamond", false);
			this.saveConfig();
			e.getPlayer().setDisplayName(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Guest" + ChatColor.DARK_GRAY + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			Bukkit.broadcastMessage((String) (ChatColor.GREEN + "Welcome to the server, " + ChatColor.GOLD + e.getPlayer().getName()));
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".overlord")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "OverLord" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".mvpplus")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "MVP+" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".mvp")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "MVP" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".vipplus")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP+" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".vip")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".builder")) {
			e.getPlayer().setDisplayName(ChatColor.GOLD + "[" + ChatColor.GREEN + "Builder" + ChatColor.GOLD + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".bedrock")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Bedrock" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".owner")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Owner" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".coowner")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Co-Owner" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".admin")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Admin" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".moderator")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Moderator" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".helper")) {
			e.getPlayer().setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Helper" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".mega")) {
			e.getPlayer().setDisplayName(ChatColor.GOLD + "[" + ChatColor.GREEN + "Mega" + ChatColor.GOLD + "] " + ChatColor.AQUA + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		if (this.getConfig().getBoolean("ranks." + player.getName() + ".diamond")) {
			e.getPlayer().setDisplayName(ChatColor.BLUE + "[" + ChatColor.AQUA + "Diamond" + ChatColor.BLUE + "] " + ChatColor.GREEN + e.getPlayer().getName() + ChatColor.WHITE);
			return;
		}
		e.getPlayer().setDisplayName(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Guest" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + e.getPlayer().getName() + ChatColor.WHITE);
		if (!e.getPlayer().hasPlayedBefore()) {
			p.kickPlayer(ChatColor.RED + "Please rejoin the server, This only happens for new players");
		}
	}

	@EventHandler
	public void onPlayerLevelChangeEvent(PlayerLevelChangeEvent e) {
		int oldLvl = e.getOldLevel();
		int newLvl = e.getNewLevel();
		Player p = e.getPlayer();
		int pl = this.getConfig().getInt("level." + p.getName());
		if (newLvl <= pl) {
			p.sendMessage(this.GOLD + "Your server level has been successfully updated!");
			return;
		}
		this.getConfig().set("level." + p.getName(), newLvl);
		p.sendMessage(this.GOLD + "Your server level has been successfully updated!");
		this.saveConfig();
	}

	public void openGUI(Player p) {
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 9, (String) (ChatColor.DARK_GREEN + "Server Selector"));
		ItemStack survival = new ItemStack(Material.DIAMOND_SWORD);
		ItemMeta survivalMeta = survival.getItemMeta();
		ItemStack gta = new ItemStack(Material.DIAMOND_AXE);
		ItemMeta gtaMeta = gta.getItemMeta();
		ItemStack teampvp = new ItemStack(Material.IRON_SWORD);
		ItemMeta teampvpMeta = teampvp.getItemMeta();
		ItemStack zahc = new ItemStack(Material.REDSTONE);
		ItemMeta zahcMeta = zahc.getItemMeta();
		survivalMeta.setDisplayName(ChatColor.GREEN + "Factions");
		gtaMeta.setDisplayName(ChatColor.GREEN + "GTA 2.0");
		teampvpMeta.setDisplayName(ChatColor.GREEN + "TeamPvP");
		zahcMeta.setDisplayName(ChatColor.GREEN + "ZAHC");
		survival.setItemMeta(survivalMeta);
		gta.setItemMeta(gtaMeta);
		teampvp.setItemMeta(teampvpMeta);
		zahc.setItemMeta(zahcMeta);
		inv.setItem(0, survival);
		inv.setItem(2, gta);
		inv.setItem(4, teampvp);
		inv.setItem(6, zahc);
		p.openInventory(inv);
	}

	@EventHandler
	public void TeamPvP(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		PlayerInventory pi = p.getInventory();
		if (!this.teampvp.contains(p)) {
			return;
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (!ChatColor.stripColor((String) e.getInventory().getName()).equalsIgnoreCase("Server Selector")) {
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
				if (this.getConfig().getBoolean("Servers.Factions.Banned.Players." + p.getName())) {
					p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "Factions" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
					p.closeInventory();
					return;
				}
				p.closeInventory();
				PlayerInventory pi4 = p.getInventory();
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
				Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into Factions!"));
				p.sendMessage(ChatColor.GOLD + "Teleported to " + ChatColor.GREEN + "Factions");
				break;
			}
			case DIAMOND_AXE: {
				if (this.getConfig().getBoolean("Servers.GTA.Banned.Players." + p.getName())) {
					p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "GTA" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
					p.closeInventory();
					return;
				}
				p.closeInventory();
				PlayerInventory pi = p.getInventory();
				pi.clear();
				p.getInventory().setHelmet(this.AIR);
				p.getInventory().setChestplate(this.AIR);
				p.getInventory().setLeggings(this.AIR);
				p.getInventory().setBoots(this.AIR);
				p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_HELMET)});
				p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_CHESTPLATE)});
				p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_LEGGINGS)});
				p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_BOOTS)});
				p.setGameMode(GameMode.SURVIVAL);
				pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_AXE, 1)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SPADE, 1)});
				pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
				World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
				double x = this.settings.getData().getDouble("warps.map000_rpg.x");
				if (this.hub.contains(p)) {
					this.hub.remove(p);
				}
				if (!this.rpg.contains(p)) {
					this.rpg.add(p);
				}
				double y = this.settings.getData().getDouble("warps.map000_rpg.y");
				double z = this.settings.getData().getDouble("warps.map000_rpg.z");
				p.teleport(new Location(w, x, y, z));
				p.teleport(new Location(w, x, y, z));
				p.teleport(new Location(w, x, y, z));
				p.sendMessage(ChatColor.GOLD + "Teleported to " + ChatColor.GREEN + "GTA 2.0");
				Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into GTA!"));
				break;
			}
			case IRON_SWORD: {
				if (this.getConfig().getBoolean("Servers.TeamPvP.Banned.Players." + p.getName())) {
					p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "TeamPvP" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
					p.closeInventory();
					return;
				}
				PlayerInventory pen = p.getInventory();
				pen.clear();
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
				p.getInventory().setChestplate(new ItemStack(Material.AIR));
				p.getInventory().setLeggings(new ItemStack(Material.AIR));
				p.getInventory().setBoots(new ItemStack(Material.AIR));
				pen.clear();
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
				p.getInventory().setChestplate(new ItemStack(Material.AIR));
				p.getInventory().setLeggings(new ItemStack(Material.AIR));
				p.getInventory().setBoots(new ItemStack(Material.AIR));
				World world = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
				double xer = this.settings.getData().getDouble("warps.map000_teampvp.x");
				if (this.hub.contains(p)) {
					this.hub.remove(p);
				}
				if (!this.teampvp.contains(p)) {
					this.teampvp.add(p);
				}
				double yer = this.settings.getData().getDouble("warps.map000_teampvp.y");
				double zer = this.settings.getData().getDouble("warps.map000_teampvp.z");
				p.teleport(new Location(world, xer, yer, zer));
				Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into TeamPvP!"));
				break;
			}
			case REDSTONE: {
				if (this.getConfig().getBoolean("Servers.ZAHC.Banned.Players." + p.getName())) {
					p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "ZAHC" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
					p.closeInventory();
					return;
				}
				PlayerInventory pen938 = p.getInventory();
				pen938.clear();
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
				p.getInventory().setChestplate(new ItemStack(Material.AIR));
				p.getInventory().setLeggings(new ItemStack(Material.AIR));
				p.getInventory().setBoots(new ItemStack(Material.AIR));
				pen938.clear();
				p.getInventory().setHelmet(new ItemStack(Material.AIR));
				p.getInventory().setChestplate(new ItemStack(Material.AIR));
				p.getInventory().setLeggings(new ItemStack(Material.AIR));
				p.getInventory().setBoots(new ItemStack(Material.AIR));
				World worldiben = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_zahc.world"));
				double xerben = this.settings.getData().getDouble("warps.map000_zahc.x");
				if (this.hub.contains(p)) {
					this.hub.remove(p);
				}
				if (!this.zahc.contains(p)) {
					this.zahc.add(p);
				}
				double yerben = this.settings.getData().getDouble("warps.map000_zahc.y");
				double zerben = this.settings.getData().getDouble("warps.map000_zahc.z");
				p.teleport(new Location(worldiben, xerben, yerben, zerben));
				Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into ZAHC!"));
				break;
			}
			default: {
				p.closeInventory();
			}
		}
	}

	private void teampvpJoin(Player p) {
		p.sendMessage(ChatColor.GRAY + "----------------------------------------------->");
		p.sendMessage(ChatColor.GREEN + "Welcome to the server, " + ChatColor.GOLD + p.getName() + "!");
		p.sendMessage(ChatColor.GREEN + "Rules: " + ChatColor.GOLD + "/rules");
		p.sendMessage(ChatColor.GREEN + "Donate: " + ChatColor.GOLD + "/donate");
		p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + "/money");
		p.sendMessage(ChatColor.GREEN + "Vote: " + ChatColor.GOLD + "/vote");
		p.sendMessage(ChatColor.GRAY + "----------------------------------------------->");
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
		if (!this.hub.contains(e.getPlayer()) || this.survival.contains(p) || this.teampvp.contains(p) || this.rpg.contains(p)) {
			e.getPlayer().sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
			return;
		}
		this.openGUI(e.getPlayer());
	}

	@EventHandler
	public void NoCheat(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (e.getBlock().getType() == Material.ENDER_CHEST) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place an " + ChatColor.GOLD + "Ender Chest" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.BEDROCK) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place " + ChatColor.GOLD + "Bedrock" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
		if (!this.creative.contains(p)) {
			return;
		}
		if (e.getBlock().getType() == Material.TNT) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place " + ChatColor.GOLD + "TNT" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.RAILS) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place " + ChatColor.GOLD + "Rails" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.ACTIVATOR_RAIL) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place " + ChatColor.GOLD + "Rails" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.DETECTOR_RAIL) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place " + ChatColor.GOLD + "Rails" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
		if (e.getBlock().getType() == Material.POWERED_RAIL) {
			Bukkit.broadcastMessage((String) (ChatColor.GOLD + p.getName() + ChatColor.RED + " just tried to place " + ChatColor.GOLD + "Rails" + ChatColor.RED + " in world " + ChatColor.GOLD + p.getWorld().getName()));
			e.getPlayer().getInventory().remove(e.getBlock().getType());
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void Creative(PlayerMoveEvent e) {
		if (this.creative.contains(e.getPlayer())) {
			return;
		}
		if (this.hub.contains(e.getPlayer())) {
			if (this.getConfig().getBoolean("ranks." + e.getPlayer().getName() + ".saveItems")) {
				return;
			}
			ItemStack ss = new ItemStack(Material.COMPASS, 1);
			ItemMeta ssm = ss.getItemMeta();
			ssm.setDisplayName(ChatColor.GREEN + "Server Selector");
			ss.setItemMeta(ssm);
			e.getPlayer().getInventory().clear();
			e.getPlayer().getInventory().addItem(new ItemStack[]{ss});
			e.getPlayer().setGameMode(GameMode.SURVIVAL);
			return;
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		if (e.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.REDSTONE_BLOCK) {
			e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
			e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.1, e.getPlayer().getVelocity().getZ()));
			return;
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
	public void death(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		Player k = e.getEntity().getKiller();
		if (e.getDeathMessage().contains("died")) {
			e.setDeathMessage("");
			return;
		}
		if (e.getDeathMessage().contains("tried to swim in")) {
			if (this.teampvp.contains(k)) {
				e.setDeathMessage("");
				return;
			}
			e.setDeathMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " tried to swim in lava!");
			return;
		}
		if (e.getDeathMessage().contains("died")) {
			e.setDeathMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has randomly died!");
			return;
		}
		if (e.getDeathMessage().contains("pummel")) {
			e.setDeathMessage("");
			return;
		}
		if (e.getDeathMessage().contains("fireballed") && this.rpg.contains(k)) {
			e.setDeathMessage("");
			return;
		}
		if (e.getDeathMessage().contains("burned")) {
			e.setDeathMessage("");
			return;
		}
		if (e.getDeathMessage().contains("burnt")) {
			e.setDeathMessage("");
			return;
		}
		if (e.getDeathMessage().contains("slain by")) {
			if (this.rpg.contains(k)) {
				e.setDeathMessage("");
				return;
			}
			e.setDeathMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has been killed by " + ChatColor.GOLD + k.getName());
			return;
		}
		if (e.getDeathMessage().contains("shot by")) {
			if (this.rpg.contains(k)) {
				e.setDeathMessage("");
				return;
			}
			e.setDeathMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " was shot by " + ChatColor.GOLD + k.getName());
			return;
		}
		if (e.getDeathMessage().contains("fell")) {
			e.setDeathMessage(ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " thought he could fly!");
			return;
		}
	}

	@EventHandler
	public void TeamPvP_1(PlayerRespawnEvent e) {
		if (this.teampvp.contains(e.getPlayer())) {
			this.teampvpRespawn.add(e.getPlayer());
			return;
		}
	}

	@EventHandler
	public void TeamPvP_2(PlayerMoveEvent e) {
		if (this.teampvpRespawn.contains(e.getPlayer())) {
			World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
			double x = this.settings.getData().getDouble("warps.map000_teampvp.x");
			double y = this.settings.getData().getDouble("warps.map000_teampvp.y");
			double z = this.settings.getData().getDouble("warps.map000_teampvp.z");
			e.getPlayer().teleport(new Location(w, x, y, z));
			e.getPlayer().teleport(new Location(w, x, y, z));
			e.getPlayer().teleport(new Location(w, x, y, z));
			e.getPlayer().teleport(new Location(w, x, y, z));
			e.getPlayer().teleport(new Location(w, x, y, z));
			e.getPlayer().teleport(new Location(w, x, y, z));
			this.teampvpRespawn.remove(e.getPlayer());
			this.teampvpRespawn.remove(e.getPlayer());
			this.teampvpRespawn.remove(e.getPlayer());
			this.teampvpRespawn.remove(e.getPlayer());
			this.teampvpRespawn.remove(e.getPlayer());
			this.teampvpRespawn.remove(e.getPlayer());
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
	}

	@EventHandler
	public void onPlayerDeath(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (!this.rpg.contains(p)) {
			return;
		}
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
		double x = this.settings.getData().getDouble("warps.map000_rpg.x");
		double y = this.settings.getData().getDouble("warps.map000_rpg.y");
		double z = this.settings.getData().getDouble("warps.map000_rpg.z");
		p.teleport(new Location(w, x, y, z));
	}

	@EventHandler
	public void GTA_6(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (this.rpg.contains(p)) {
			PlayerInventory pi = p.getInventory();
			this.ttg(p);
			p.setGameMode(GameMode.SURVIVAL);
			this.ttg(p);
			return;
		}
	}

	@EventHandler
	public void onPlayerRespawn2(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		Player k = e.getPlayer().getKiller();
		if (this.rpg.contains(p)) {
			this.getConfig().set("level." + k.getName(), (k.getLevel() + 30));
			this.saveConfig();
			k.sendMessage(ChatColor.AQUA + "You have killed " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " in GTA server 1!");
			k.sendMessage(ChatColor.AQUA + "You have been given " + ChatColor.GOLD + "30" + ChatColor.AQUA + " levels, Spend your money using /shop");
			this.sesmen.add(p);
			return;
		}
	}

	@EventHandler
	public void GTA_4(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (this.sesmen.contains(p)) {
			PlayerInventory pi = p.getInventory();
			this.ttg(p);
			this.ttg(p);
			this.ttg(p);
			this.ttg(p);
			this.ttg(p);
			this.ttg(p);
			p.setGameMode(GameMode.SURVIVAL);
			this.ttg(p);
			pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_AXE, 1)});
			this.ttg(p);
			this.ttg(p);
			this.ttg(p);
			this.ttg(p);
			this.sesmen.remove(p);
			return;
		}
	}

	private void ttg(Player p) {
		World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
		double x = this.settings.getData().getDouble("warps.map000_rpg.x");
		double y = this.settings.getData().getDouble("warps.map000_rpg.y");
		double z = this.settings.getData().getDouble("warps.map000_rpg.z");
		p.teleport(new Location(w, x, y, z));
	}

	@EventHandler
	public void GTA_5(PlayerDeathEvent e) {
		Player p = e.getEntity().getPlayer();
		if (this.rpg.contains(p)) {
			this.sesmen.add(p);
			return;
		}
	}

	@EventHandler
	public void GTA_2(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!this.hub.contains(p)) {
			return;
		}
		p.setFoodLevel(20);
		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.DIAMOND_ORE) {
			if (this.getConfig().getBoolean("Servers.GTA.Banned.Players." + p.getName())) {
				p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "GTA" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
				return;
			}
			p.closeInventory();
			PlayerInventory pi = p.getInventory();
			pi.clear();
			p.getInventory().setHelmet(this.AIR);
			p.getInventory().setChestplate(this.AIR);
			p.getInventory().setLeggings(this.AIR);
			p.getInventory().setBoots(this.AIR);
			p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_HELMET)});
			p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_CHESTPLATE)});
			p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_LEGGINGS)});
			p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_BOOTS)});
			p.setGameMode(GameMode.SURVIVAL);
			pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
			pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_AXE, 1)});
			pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SPADE, 1)});
			pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
			World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
			double x = this.settings.getData().getDouble("warps.map000_rpg.x");
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
			if (!this.rpg.contains(p)) {
				this.rpg.add(p);
			}
			double y = this.settings.getData().getDouble("warps.map000_rpg.y");
			double z = this.settings.getData().getDouble("warps.map000_rpg.z");
			p.teleport(new Location(w, x, y, z));
			p.sendMessage(ChatColor.GOLD + "Teleported to " + ChatColor.GREEN + "GTA 2.0");
			Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into GTA!"));
			return;
		}
		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.COAL_ORE) {
			if (this.getConfig().getBoolean("Servers.Factions.Banned.Players." + p.getName())) {
				p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "Factions" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
				return;
			}
			PlayerInventory pi = p.getInventory();
			World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_survival.world"));
			double x = this.settings.getData().getDouble("warps.map000_survival.x");
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
			if (!this.survival.contains(p)) {
				this.survival.add(p);
			}
			double y = this.settings.getData().getDouble("warps.map000_survival.y");
			double z = this.settings.getData().getDouble("warps.map000_survival.z");
			p.teleport(new Location(w, x, y, z));
			Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into Factions!"));
			return;
		}
		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_ORE) {
			if (this.getConfig().getBoolean("Servers.TeamPvP.Banned.Players." + p.getName())) {
				p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "TeamPvP" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
				return;
			}
			PlayerInventory pi = p.getInventory();
			pi.clear();
			p.getInventory().setHelmet(this.AIR);
			p.getInventory().setChestplate(this.AIR);
			p.getInventory().setLeggings(this.AIR);
			p.getInventory().setBoots(this.AIR);
			World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
			double x = this.settings.getData().getDouble("warps.map000_teampvp.x");
			if (this.hub.contains(p)) {
				this.hub.remove(p);
			}
			if (!this.teampvp.contains(p)) {
				this.teampvp.add(p);
			}
			double y = this.settings.getData().getDouble("warps.map000_teampvp.y");
			double z = this.settings.getData().getDouble("warps.map000_teampvp.z");
			p.teleport(new Location(w, x, y, z));
			Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into TeamPvP!"));
			return;
		}
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent e) {
		String motd = this.getConfig().getString("motd.system");
		motd = motd.replaceAll("&", "\u00a7");
		e.setMaxPlayers(250);
		if (!this.getConfig().getBoolean("settings.motd.custom")) {
			int value = this.getConfig().getInt("motd.amount");
			if (value == 0) {
				e.setMotd(ChatColor.GOLD + "{" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.GOLD + "} " + ChatColor.AQUA + "Join now we have <TeamPvP/Creative/Survival/GTA>");
				this.getConfig().set("motd.amount", (value + 1));
				this.saveConfig();
				return;
			}
			if (value == 1) {
				e.setMotd(ChatColor.GOLD + "{" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.GOLD + "} " + ChatColor.AQUA + "Added donor ranks and donation links!");
				this.getConfig().set("motd.amount", (value + 1));
				this.saveConfig();
				return;
			}
			if (value == 2) {
				e.setMotd(ChatColor.GOLD + "{" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.GOLD + "} " + ChatColor.AQUA + "New hosting survice and new website!");
				this.getConfig().set("motd.amount", (value + 1));
				this.saveConfig();
				return;
			}
			if (value == 3) {
				e.setMotd(ChatColor.GOLD + "{" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.GOLD + "} " + ChatColor.AQUA + "Applyments for staff are opening on June 10th 2015");
				this.getConfig().set("motd.amount", (value + 1));
				this.saveConfig();
				return;
			}
			if (value == 4) {
				e.setMotd(ChatColor.GOLD + "{" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.GOLD + "} " + ChatColor.AQUA + "BETA Clans has been released as of May 2015!");
				this.getConfig().set("motd.amount", 0);
				this.saveConfig();
				return;
			}
			e.setMotd(ChatColor.RED + "Error: " + ChatColor.AQUA + "Could not find database for MOTD");
			return;
		}
		e.setMotd(motd);
	}

	@EventHandler
	public void SuperSpeed(PlayerMoveEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (!e.getPlayer().isSprinting()) {
				e.getPlayer().setWalkSpeed(0.2f);
				return;
			}
			if (this.charged.contains(e.getPlayer())) {
				e.getPlayer().setWalkSpeed(1.0f);
				return;
			}
			e.getPlayer().setWalkSpeed(0.5f);
			return;
		}
		e.getPlayer().setWalkSpeed(0.2f);
	}

	@EventHandler
	public void Fix(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!(this.survival.contains(p) || this.creative.contains(p) || this.teampvp.contains(p) || this.rpg.contains(p) || this.hub.contains(p) || this.zahc.contains(p))) {
			p.kickPlayer(ChatColor.RED + "Reload Complete, Please rejoin the server.");
			return;
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (e.getAction() != Action.RIGHT_CLICK_AIR) {
				return;
			}
			if (e.getItem().getType() != Material.IRON_AXE) {
				return;
			}
			Snowball f = (Snowball) e.getPlayer().launchProjectile(Snowball.class);
			return;
		}
	}

	@EventHandler
	public void onPlayerInteract2(PlayerInteractEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (e.getAction() != Action.RIGHT_CLICK_AIR) {
				return;
			}
			if (e.getItem().getType() != Material.DIAMOND_SPADE) {
				return;
			}
			e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(1));
			e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 0.01, e.getPlayer().getVelocity().getZ()));
			return;
		}
	}

	@EventHandler
	public void onPlayerInteract4(PlayerInteractEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (e.getItem().getType() != Material.DIAMOND_AXE) {
				return;
			}
			if (!e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "[" + ChatColor.AQUA + "Plasma-Gun" + ChatColor.GRAY + "]")) {
				return;
			}
			Arrow f = (Arrow) e.getPlayer().launchProjectile(Arrow.class);
			this.pg.add(e.getPlayer());
			return;
		}
	}

	@EventHandler
	public void onPlayerInteract3(PlayerInteractEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (e.getAction() != Action.RIGHT_CLICK_AIR) {
				return;
			}
			if (e.getItem().getType() != Material.IRON_SPADE) {
				return;
			}
			SmallFireball f = (SmallFireball) e.getPlayer().launchProjectile(SmallFireball.class);
			return;
		}
	}

	@EventHandler
	public void SaveInventory_Factions(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (!this.survival.contains(p)) {
			return;
		}
		ItemStack[] V1 = p.getInventory().getContents();
		ItemStack[] V2 = p.getInventory().getArmorContents();
		this.factions.getData().set("Contents." + p.getName(), V1);
		this.factions.getData().set("Armor." + p.getName(), V2);
		this.factions.saveData();
	}

	@EventHandler
	public void onPlayerInteract7(PlayerInteractEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (e.getItem().getType() != Material.GOLD_SPADE) {
				return;
			}
			if (!e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "[" + ChatColor.AQUA + "Flying-Bum" + ChatColor.GRAY + "]")) {
				return;
			}
			e.getPlayer().setVelocity(e.getPlayer().getLocation().getDirection().multiply(3));
			e.getPlayer().setVelocity(new Vector(e.getPlayer().getVelocity().getX(), 1.1, e.getPlayer().getVelocity().getZ()));
			return;
		}
	}

	@EventHandler
	public void charger(PlayerInteractEvent e) {
		if (this.rpg.contains(e.getPlayer())) {
			if (e.getItem().getType() != Material.STICK) {
				return;
			}
			if (!e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GRAY + "[" + ChatColor.AQUA + "Charger" + ChatColor.GRAY + "]")) {
				return;
			}
			if (this.charged.contains(e.getPlayer())) {
				this.charged.remove(e.getPlayer());
				e.getPlayer().sendMessage(ChatColor.AQUA + "Charger DISABLED");
				return;
			}
			this.charged.add(e.getPlayer());
			e.getPlayer().sendMessage(ChatColor.AQUA + "Charger ENABLED");
			return;
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent e) {
		if (!(e.getDamager() instanceof Snowball)) {
			return;
		}
		e.setDamage(7.0);
	}

	@EventHandler
	public void onPlayerChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		String msg = e.getMessage();
		if (msg.contains("fuck")) {
			msg.replaceAll("fuck", "****");
		}
		if (msg.contains("fucker")) {
			msg.replaceAll("fucker", "******");
		}
		if (msg.contains("fucking")) {
			msg.replaceAll("fucking", "*******");
		}
		if (msg.contains("bitch")) {
			msg.replaceAll("bitch", "*****");
		}
		if (msg.contains("noob")) {
			msg.replaceAll("noob", "****");
		}
		if (msg.contains("suck")) {
			msg.replaceAll("suck", "****");
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		e.setQuitMessage(ChatColor.GOLD + "[" + ChatColor.RED + "-" + ChatColor.GOLD + "] " + ChatColor.AQUA + e.getPlayer().getName());
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (!this.CC.contains(p)) {
			if (cmd.getName().equalsIgnoreCase("respawn")) {
				if (!this.rpg.contains(p)) {
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
						p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
						return true;
					}
					Player targetPlayer = Bukkit.getPlayer((String) args[0]);
					p.sendMessage(ChatColor.AQUA + "IP: " + ChatColor.GOLD + targetPlayer.getAddress().getAddress().getHostAddress());
					return true;
				}
				p.sendMessage(ChatColor.GREEN + "Please enter a name of the player");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("server")) {
				if (this.survival.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing Factions");
					return true;
				}
				if (this.rpg.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing GTA");
					return true;
				}
				if (this.teampvp.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing TeamPvP");
					return true;
				}
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/server <Server-Name>");
					return true;
				}
				PlayerInventory pi = p.getInventory();
				if (args[0].equalsIgnoreCase("factions")) {
					if (this.getConfig().getBoolean("Servers.Factions.Banned.Players." + p.getName())) {
						p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "Factions" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
						return true;
					}
					World w2 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_survival.world"));
					double x2 = this.settings.getData().getDouble("warps.map000_survival.x");
					if (this.hub.contains(p)) {
						this.hub.remove(p);
					}
					if (!this.survival.contains(p)) {
						this.survival.add(p);
					}
					double y2 = this.settings.getData().getDouble("warps.map000_survival.y");
					double z2 = this.settings.getData().getDouble("warps.map000_survival.z");
					p.teleport(new Location(w2, x2, y2, z2));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into Factions!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("gta")) {
					if (this.getConfig().getBoolean("Servers.GTA.Banned.Players." + p.getName())) {
						p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "GTA" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
						return true;
					}
					pi.clear();
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_HELMET)});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_CHESTPLATE)});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_LEGGINGS)});
					p.getInventory().addItem(new ItemStack[]{new ItemStack(Material.IRON_BOOTS)});
					p.setGameMode(GameMode.SURVIVAL);
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.IRON_AXE, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SPADE, 1)});
					pi.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 32)});
					World w3 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_rpg.world"));
					double x3 = this.settings.getData().getDouble("warps.map000_rpg.x");
					if (this.hub.contains(p)) {
						this.hub.remove(p);
					}
					if (!this.rpg.contains(p)) {
						this.rpg.add(p);
					}
					double y3 = this.settings.getData().getDouble("warps.map000_rpg.y");
					double z3 = this.settings.getData().getDouble("warps.map000_rpg.z");
					p.teleport(new Location(w3, x3, y3, z3));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into GTA!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("teampvp")) {
					if (this.getConfig().getBoolean("Servers.TeamPvP.Banned.Players." + p.getName())) {
						p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "TeamPvP" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
						return true;
					}
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					World w4 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
					double x4 = this.settings.getData().getDouble("warps.map000_teampvp.x");
					if (this.hub.contains(p)) {
						this.hub.remove(p);
					}
					if (!this.teampvp.contains(p)) {
						this.teampvp.add(p);
					}
					double y4 = this.settings.getData().getDouble("warps.map000_teampvp.y");
					double z4 = this.settings.getData().getDouble("warps.map000_teampvp.z");
					p.teleport(new Location(w4, x4, y4, z4));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into TeamPvP!"));
					return true;
				}
				if (args[0].equalsIgnoreCase("zahc")) {
					if (this.getConfig().getBoolean("Servers.ZAHC.Banned.Players." + p.getName())) {
						p.sendMessage(ChatColor.RED + "Error connecting to server " + ChatColor.GOLD + "ZAHC" + ChatColor.RED + ": " + ChatColor.AQUA + "You have been banned from this server!");
						p.closeInventory();
						return true;
					}
					PlayerInventory pen938 = p.getInventory();
					pen938.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					pen938.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					World worldiben = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_zahc.world"));
					double xerben = this.settings.getData().getDouble("warps.map000_zahc.x");
					if (this.hub.contains(p)) {
						this.hub.remove(p);
					}
					if (!this.teampvp.contains(p)) {
						this.teampvp.add(p);
					}
					double yerben = this.settings.getData().getDouble("warps.map000_zahc.y");
					double zerben = this.settings.getData().getDouble("warps.map000_zahc.z");
					p.teleport(new Location(worldiben, xerben, yerben, zerben));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has logged into ZAHC!"));
					return true;
				}
				p.sendMessage(ChatColor.RED + "Server not found!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("vote")) {
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				p.sendMessage(ChatColor.GOLD + "PMC: " + ChatColor.AQUA + "http://www.planetminecraft.com/server/skorrloregaming/vote/");
				p.sendMessage(ChatColor.GOLD + "MMP: " + ChatColor.AQUA + "http://minecraft-mp.com/server/82306/vote/");
				p.sendMessage(ChatColor.GOLD + "MCS: " + ChatColor.AQUA + "http://minecraftservers.org/vote/231844");
				p.sendMessage(ChatColor.GOLD + "MIX: " + ChatColor.AQUA + "http://www.minecraft-index.com/36234-skorrloregaming/vote");
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("lobby")) {
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/lobby <Lobby>");
					return true;
				}
				if (args[0].equalsIgnoreCase("1")) {
					int code = this.getConfig().getInt("lobby.1");
					World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
					double x = this.settings.getData().getDouble("warps.map000_teampvp.x");
					double y = this.settings.getData().getDouble("warps.map000_teampvp.y");
					double z = this.settings.getData().getDouble("warps.map000_teampvp.z");
					p.teleport(new Location(w, x, y, z));
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
				Location YN2 = new Location(p.getWorld(), p.getLocation().getX(), p.getLocation().getY() - 2.0, p.getLocation().getZ());
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
			if (cmd.getName().equalsIgnoreCase("bid")) {
				if (!this.survival.contains(p)) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be playing " + ChatColor.GOLD + "Factions" + ChatColor.RED + " in order to use this command!");
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
				if (this.getConfig().getInt("ranks." + p.getName() + ".money") < this.getConfig().getInt("Auction.Bids.Last.Amount") + 4) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You do not have enough money to preform this bid!");
					return true;
				}
				int Amount = this.getConfig().getInt("Auction.Bids.Last.Amount");
				this.getConfig().set("Auction.Bids.Last.Amount", (Amount += 5));
				this.getConfig().set("Auction.Bids.Last.Name", p.getName());
				this.saveConfig();
				Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has placed a bid of " + ChatColor.GOLD + this.getConfig().getInt("Auction.Bids.Last.Amount") + ChatColor.AQUA + " on auction " + ChatColor.GOLD + CURR_AUCTION_CODE));
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("auc-end")) {
				if (!this.survival.contains(p)) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be playing " + ChatColor.GOLD + "Factions" + ChatColor.RED + " in order to use this command!");
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
				Bukkit.broadcastMessage((String) (String.valueOf(AUCTION_DISPLAY) + "The current auction has been cancelled!"));
				this.getConfig().set("Auction.Running", false);
				this.getConfig().set("Auction.Cancelled", true);
				this.saveConfig();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("ban")) {
				if (!p.hasPermission("skorrloregaming.ban")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 2) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/ban <Player> <Factions/GTA/TeamPvP>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer((String) args[0]);
				if (targetPlayer == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player could not be found in AsyncDatabase");
					return true;
				}
				if (args[2].equalsIgnoreCase("Factions")) {
					this.getConfig().set("Servers.Factions.Banned.Players." + targetPlayer.getUniqueId().toString(), true);
					this.saveConfig();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + targetPlayer.getName() + "(" + targetPlayer.getUniqueId().toString() + ") " + ChatColor.AQUA + "has been banned from the server " + ChatColor.GOLD + "Factions"));
					return true;
				}
				if (args[2].equalsIgnoreCase("GTA")) {
					this.getConfig().set("Servers.GTA.Banned.Players." + targetPlayer.getUniqueId().toString(), true);
					this.saveConfig();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + targetPlayer.getName() + "(" + targetPlayer.getUniqueId().toString() + ") " + ChatColor.AQUA + "has been banned from the server " + ChatColor.GOLD + "GTA"));
					return true;
				}
				if (args[2].equalsIgnoreCase("ZAHC")) {
					this.getConfig().set("Servers.ZAHC.Banned.Players." + targetPlayer.getUniqueId().toString(), true);
					this.saveConfig();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + targetPlayer.getName() + "(" + targetPlayer.getUniqueId().toString() + ") " + ChatColor.AQUA + "has been banned from the server " + ChatColor.GOLD + "ZAHC"));
					return true;
				}
				if (args[2].equalsIgnoreCase("TeamPvP")) {
					this.getConfig().set("Servers.TeamPvP.Banned.Players." + targetPlayer.getUniqueId().toString(), true);
					this.saveConfig();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.GOLD + targetPlayer.getName() + "(" + targetPlayer.getUniqueId().toString() + ") " + ChatColor.AQUA + "has been banned from the server " + ChatColor.GOLD + "TeamPvP"));
					return true;
				}
				p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Please enter a server value of Factions, GTA, ZAHC, or TeamPvP.");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("auc-start")) {
				if (!this.survival.contains(p)) {
					p.sendMessage(String.valueOf(AUCTION_DISPLAY) + ChatColor.RED + "You must be playing " + ChatColor.GOLD + "Factions" + ChatColor.RED + " in order to use this command!");
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
					Player tp2 = Bukkit.getPlayer((String) args[0]);
					if (tp2 == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					int amount = Integer.parseInt(args[1]);
					if (amount < 0) {
						p.sendMessage(ChatColor.RED + "You cannot pay players in negatives!");
						return true;
					}
					int pMoney = this.getConfig().getInt("ranks." + p.getName() + ".money");
					int tpMoney = this.getConfig().getInt("ranks." + tp2.getName() + ".money");
					if (pMoney >= amount - 1) {
						this.getConfig().set("ranks." + tp2.getName() + ".money", (tpMoney += amount));
						this.getConfig().set("ranks." + p.getName() + ".money", (pMoney -= amount));
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN + "You have payed " + ChatColor.GOLD + tp2.getName() + ChatColor.GREEN + ": " + ChatColor.GOLD + amount + ChatColor.GREEN + " dollars!");
						tp2.sendMessage(ChatColor.GREEN + "You have been payed " + ChatColor.GOLD + amount + ChatColor.GREEN + " dollars, by " + ChatColor.GOLD + p.getName());
						return true;
					}
					p.sendMessage(ChatColor.AQUA + "You do not have enough money to give");
					return true;
				}
				if (!this.rpg.contains(p)) {
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
					Player tp = Bukkit.getPlayer((String) args[0]);
					if (tp == null) {
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
						tp.setLevel(tp.getLevel() + vm);
						tp.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has payed you " + ChatColor.GOLD + vm + ChatColor.AQUA + " levels");
						p.sendMessage(ChatColor.AQUA + "You have payed " + ChatColor.GOLD + tp.getName() + " " + ChatColor.GOLD + vm + ChatColor.AQUA + " levels");
						return true;
					}
					p.sendMessage(ChatColor.AQUA + "You do not have enough levels to give");
					return true;
				}
			}
			if (cmd.getName().equalsIgnoreCase("disable-plugin")) {
				if (!p.hasPermission("skorrloregaming.disable")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/disable-plugin <Plugin-Name>");
					return true;
				}
				if (this.disablePlugin(args[0])) {
					sender.sendMessage("[SkorrloreGaming]" + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.AQUA + " is now disabled.");
				} else {
					sender.sendMessage("[SkorrloreGaming]" + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.RED + " was not found!");
				}
			}
			if (cmd.getName().equalsIgnoreCase("enable-plugin")) {
				if (!p.hasPermission("skorrloregaming.enable")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length != 1) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/disable-plugin <Plugin-Name>");
					return true;
				}
				if (this.enablePlugin(args[0])) {
					sender.sendMessage("[SkorrloreGaming]" + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.AQUA + " is now enabled.");
				} else {
					sender.sendMessage("[SkorrloreGaming]" + ChatColor.GREEN + " " + ChatColor.BOLD + args[0] + ChatColor.RED + " was not found!");
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
				Player tp3 = Bukkit.getPlayer((String) args[0]);
				if (tp3 == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found!");
					return true;
				}
				this.locked.remove(tp3);
				this.getConfig().set("ranks." + tp3.getName() + ".kitVip", false);
				this.getConfig().set("ranks." + tp3.getName() + ".kitBedrock", false);
				this.saveConfig();
				p.sendMessage(ChatColor.GREEN + "Unlocked Player: " + ChatColor.GOLD + tp3.getName());
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("addperm")) {
				if (!p.getUniqueId().toString().equals("89090d6f-02cc-4a16-ac03-9325fa6c710a")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length == 3) {
					Player tp4 = Bukkit.getPlayer((String) args[0]);
					if (tp4 == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					if (args[2].equalsIgnoreCase("true")) {
						tp4.addAttachment((Plugin) this, args[1], true);
						return true;
					}
					if (args[2].equalsIgnoreCase("false")) {
						tp4.addAttachment((Plugin) this, args[1], false);
						return true;
					}
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Please enter, true or false.");
					return true;
				}
				p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/addperm <Player> <Permission> <True/False>");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("setrank")) {
				if (!p.getUniqueId().toString().equals("89090d6f-02cc-4a16-ac03-9325fa6c710a")) {
					p.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command!");
					return true;
				}
				if (args.length == 2) {
					Player targetPlayer = Bukkit.getPlayer((String) args[0]);
					if (args[1].equalsIgnoreCase("overlord")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "OverLord" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "OverLord"));
						return true;
					}
					if (args[1].equalsIgnoreCase("mvp+")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "MVP+" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "MVP+"));
						return true;
					}
					if (args[1].equalsIgnoreCase("mvp")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "MVP"));
						return true;
					}
					if (args[1].equalsIgnoreCase("vip+")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP+" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "VIP+"));
						return true;
					}
					if (args[1].equalsIgnoreCase("vip")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "VIP" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "VIP"));
						return true;
					}
					if (args[1].equalsIgnoreCase("builder")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.GOLD + "[" + ChatColor.GREEN + "Builder" + ChatColor.GOLD + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Builder"));
						return true;
					}
					if (args[1].equalsIgnoreCase("owner")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Owner" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Owner"));
						return true;
					}
					if (args[1].equalsIgnoreCase("bedrock")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Bedrock" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Bedrock"));
						return true;
					}
					if (args[1].equalsIgnoreCase("coowner")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Co-Owner" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Co-Owner"));
						return true;
					}
					if (args[1].equalsIgnoreCase("admin")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Admin" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Admin"));
						return true;
					}
					if (args[1].equalsIgnoreCase("moderator")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Moderator" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Moderator"));
						return true;
					}
					if (args[1].equalsIgnoreCase("helper")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_RED + "[" + ChatColor.RED + "Helper" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Helper"));
						return true;
					}
					if (args[1].equalsIgnoreCase("mega")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", true);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.GOLD + "[" + ChatColor.GREEN + "Mega" + ChatColor.GOLD + "] " + ChatColor.AQUA + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Mega"));
						return true;
					}
					if (args[1].equalsIgnoreCase("diamond")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", true);
						targetPlayer.setDisplayName(ChatColor.BLUE + "[" + ChatColor.AQUA + "Diamond" + ChatColor.BLUE + "] " + ChatColor.GREEN + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Mega"));
						return true;
					}
					if (args[1].equalsIgnoreCase("guest")) {
						this.getConfig().set("ranks." + targetPlayer.getName() + ".overlord", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvpplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mvp", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vipplus", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".vip", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".bedrock", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".builder", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".owner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".coowner", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".admin", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".moderator", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".helper", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".mega", false);
						this.getConfig().set("ranks." + targetPlayer.getName() + ".diamond", false);
						targetPlayer.setDisplayName(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + "Guest" + ChatColor.DARK_GRAY + "] " + ChatColor.GRAY + targetPlayer.getPlayer().getName() + ChatColor.WHITE);
						this.saveConfig();
						Bukkit.broadcastMessage((String) (ChatColor.GOLD + targetPlayer.getName() + ChatColor.GREEN + " has been given rank " + ChatColor.RED + "Guest"));
						return true;
					}
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Rank not found!");
					return true;
				}
				p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/setrank <Player> <Rank>");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("sethub")) {
				if (!p.hasPermission("skorrloregaming.setspawn")) {
					p.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
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
				World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.map000_teampvp.world"));
				double x = this.settings.getData().getDouble("warps.map000_teampvp.x");
				double y = this.settings.getData().getDouble("warps.map000_teampvp.y");
				double z = this.settings.getData().getDouble("warps.map000_teampvp.z");
				p.teleport(new Location(w, x, y, z));
				p.sendMessage(ChatColor.GREEN + "Teleported to " + ChatColor.GOLD + "Spawn");
				this.stpt.add(p);
				this.getServer().getScheduler().scheduleAsyncDelayedTask((Plugin) this, new Runnable() {

					@Override
					public void run() {
						if (teampvp.contains(pf)) {
							pf.sendMessage(ChatColor.RED + "You can now use the command " + ChatColor.GOLD + "/spawn" + ChatColor.RED + " again.");
						}
						stpt.remove(pf);
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
						if (this.getConfig().get("ranks." + p.getName() + ".homes") == null) {
							this.getConfig().set("ranks." + p.getName() + ".homes", 0);
							this.saveConfig();
						}
						if (this.getConfig().getInt("ranks." + p.getName() + ".homes") == 1) {
							p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "You can only have 1 homes at a time!");
							return true;
						}
						this.getConfig().set("ranks." + p.getName() + ".home", hd);
						this.saveConfig();
						this.settings.getData().set("warps." + hd + ".world", p.getLocation().getWorld().getName());
						this.settings.getData().set("warps." + hd + ".x", p.getLocation().getX());
						this.settings.getData().set("warps." + hd + ".y", p.getLocation().getY());
						this.settings.getData().set("warps." + hd + ".z", p.getLocation().getZ());
						this.getConfig().set("ranks." + p.getName() + ".homes", 1);
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
				Boolean tprequest = this.getConfig().getBoolean("ranks." + p.getName() + ".tpboolean");
				if (tprequest.booleanValue()) {
					Object tp = this.getConfig().get("ranks." + p.getName() + ".tprequest");
					Boolean tphere = this.getConfig().getBoolean("ranks." + p.getName() + ".tpherebool");
					if (tphere.booleanValue()) {
						Player targetPlayer = Bukkit.getPlayer((String) tp);
						if (targetPlayer == null) {
							p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
							return true;
						}
						p.teleport((Entity) targetPlayer);
						this.getConfig().set("ranks." + p.getName() + ".tpherebool", false);
						this.getConfig().set("ranks." + p.getName() + ".tprequest", 0);
						this.getConfig().set("ranks." + p.getName() + ".tpboolean", false);
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
						targetPlayer.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
						return true;
					}
					Player targetPlayer = Bukkit.getPlayer((String) ((String) tp));
					if (targetPlayer == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					if (!this.survival.contains(targetPlayer)) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Other player is not currently playing survival!");
						return true;
					}
					targetPlayer.teleport((Entity) p);
					this.getConfig().set("ranks." + p.getName() + ".tpherebool", false);
					this.getConfig().set("ranks." + p.getName() + ".tprequest", 0);
					this.getConfig().set("ranks." + p.getName() + ".tpboolean", false);
					this.saveConfig();
					p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
					targetPlayer.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
					return true;
				}
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("toggle-saved-items")) {
				if (this.getConfig().getBoolean("ranks." + p.getName() + ".saveItems")) {
					this.getConfig().set("ranks." + p.getName() + ".saveItems", false);
					this.saveConfig();
					p.sendMessage(ChatColor.GOLD + "Save Items" + ChatColor.GREEN + " has been toggled " + ChatColor.GOLD + "OFF");
					return true;
				}
				this.getConfig().set("ranks." + p.getName() + ".saveItems", true);
				this.saveConfig();
				p.sendMessage(ChatColor.GOLD + "Save Items" + ChatColor.GREEN + " has been toggled " + ChatColor.GOLD + "ON");
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
			if (cmd.getName().equalsIgnoreCase("donate")) {
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				p.sendMessage(ChatColor.GOLD + "Link: " + ChatColor.AQUA + "https://server.pro/server/856307/donate/");
				p.sendMessage(ChatColor.GREEN + "Items on stock: ");
				p.sendMessage(ChatColor.RED + "VIP Rank - $10 ");
				p.sendMessage(ChatColor.RED + "VIP+ Rank - $15 ");
				p.sendMessage(ChatColor.RED + "MVP Rank - $20 ");
				p.sendMessage(ChatColor.RED + "MVP+ Rank - $25");
				p.sendMessage(ChatColor.GRAY + "----------------------------------------------------------------->");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("tpyes")) {
				if (!this.survival.contains(p)) {
					return true;
				}
				Boolean tprequest = this.getConfig().getBoolean("ranks." + p.getName() + ".tpboolean");
				if (tprequest.booleanValue()) {
					Object tp = this.getConfig().get("ranks." + p.getName() + ".tprequest");
					Boolean tphere = this.getConfig().getBoolean("ranks." + p.getName() + ".tpherebool");
					if (tphere.booleanValue()) {
						Player targetPlayer = Bukkit.getPlayer((String) tp);
						if (targetPlayer == null) {
							p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
							return true;
						}
						p.teleport((Entity) targetPlayer);
						this.getConfig().set("ranks." + p.getName() + ".tpherebool", false);
						this.getConfig().set("ranks." + p.getName() + ".tprequest", 0);
						this.getConfig().set("ranks." + p.getName() + ".tpboolean", false);
						this.saveConfig();
						p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
						targetPlayer.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
						return true;
					}
					Player targetPlayer = Bukkit.getPlayer((String) ((String) tp));
					if (targetPlayer == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					if (!this.survival.contains(targetPlayer)) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Other player is not currently playing survival!");
						return true;
					}
					targetPlayer.teleport((Entity) p);
					this.getConfig().set("ranks." + p.getName() + ".tpherebool", false);
					this.getConfig().set("ranks." + p.getName() + ".tprequest", 0);
					this.getConfig().set("ranks." + p.getName() + ".tpboolean", false);
					this.saveConfig();
					p.sendMessage(ChatColor.GREEN + "Teleportation request accepted!");
					targetPlayer.sendMessage(ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has accepted the teleportation request.");
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
				Player tp5 = Bukkit.getPlayer((String) args[0]);
				if (tp5 == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
					return true;
				}
				p.sendMessage(ChatColor.GREEN + "Tpahere request sent");
				tp5.sendMessage(ChatColor.GREEN + "You have been sent a tpahere request by " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + ", Type /tpaccept or /tpyes to accept.");
				this.getConfig().set("ranks." + tp5.getName() + ".tprequest", p.getName());
				this.getConfig().set("ranks." + tp5.getName() + ".tpboolean", true);
				this.getConfig().set("ranks." + tp5.getName() + ".tpherebool", true);
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
				Player tp6 = Bukkit.getPlayer((String) args[0]);
				if (tp6 == null) {
					p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
					return true;
				}
				p.sendMessage(ChatColor.GREEN + "Tpa request sent.");
				tp6.sendMessage(ChatColor.GREEN + "You have been sent a tpa request by " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + ", Type /tpaccept or /tpyes to accept.");
				this.getConfig().set("ranks." + tp6.getName() + ".tprequest", p.getName());
				this.getConfig().set("ranks." + tp6.getName() + ".tpboolean", true);
				this.getConfig().set("ranks." + p.getName() + ".tpherebool", false);
				this.saveConfig();
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("money")) {
				int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
				p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + Money);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("delhome")) {
				if (this.survival.contains(p)) {
					long hd = 0L;
					this.settings.getData().set("warps." + this.getConfig().get(new StringBuilder("ranks.").append(p.getName()).append(".home").toString()), 0);
					this.settings.saveConfig();
					this.getConfig().set("ranks." + p.getName() + ".homes", 0);
					this.saveConfig();
					this.getConfig().set("ranks." + p.getName() + ".home", hd);
					this.saveConfig();
					p.sendMessage(ChatColor.GOLD + "You have deleted your home!");
					return true;
				}
				p.sendMessage("Unknown command. Type /help for help.");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("home")) {
				if (args.length == 1) {
					String plot = args[0].replaceAll("[^0-9]+", "");
					if (this.settings.getData().getConfigurationSection("warps." + plot) == null) {
						p.sendMessage(ChatColor.GOLD + args[0] + ChatColor.AQUA + " is not a valid code!");
						return true;
					}
					World w = Bukkit.getServer().getWorld(this.settings.getData().getString("warps." + plot + ".world"));
					double x = this.settings.getData().getDouble("warps." + plot + ".x");
					double y = this.settings.getData().getDouble("warps." + plot + ".y");
					double z = this.settings.getData().getDouble("warps." + plot + ".z");
					p.teleport(new Location(w, x, y, z));
					p.sendMessage(ChatColor.GOLD + "You have teleported to: " + ChatColor.AQUA + args[0] + ChatColor.GOLD + "!");
					return true;
				}
				Object homes = this.getConfig().get("ranks." + p.getName() + ".homes");
				if (this.survival.contains(p)) {
					if (homes.equals(0)) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "You have no home set");
						return true;
					}
					Object hd = this.getConfig().get("ranks." + p.getName() + ".home");
					World w5 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps." + hd + ".world"));
					double x5 = this.settings.getData().getDouble("warps." + hd + ".x");
					double y5 = this.settings.getData().getDouble("warps." + hd + ".y");
					double z5 = this.settings.getData().getDouble("warps." + hd + ".z");
					p.teleport(new Location(w5, x5, y5, z5));
					p.sendMessage(ChatColor.GOLD + "You have teleported to your home!");
					p.sendMessage(ChatColor.AQUA + "Code: " + ChatColor.GOLD + hd);
					return true;
				}
				p.sendMessage("Unknown command. Type /help for help.");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("god")) {
				p.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + "That command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("ungod")) {
				p.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + "That command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("heal")) {
				p.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.RED + "SkorrloreGaming" + ChatColor.DARK_RED + "] " + ChatColor.AQUA + "That command is disabled on this server!");
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("def-kit")) {
				if (!this.teampvp.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				PlayerInventory pi = p.getInventory();
				if (this.getConfig().getBoolean("ranks." + p.getName() + ".mvpplus")) {
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
				if (this.getConfig().getBoolean("ranks." + p.getName() + ".mvp")) {
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
				if (this.getConfig().getBoolean("ranks." + p.getName() + ".vipplus")) {
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
				if (this.getConfig().getBoolean("ranks." + p.getName() + ".vip")) {
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
				int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
				p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + Money);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("bal")) {
				int Money = this.getConfig().getInt("ranks." + p.getName() + ".money");
				p.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.GOLD + Money);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("hub")) {
				double x6;
				double z6;
				World w6;
				double y6;
				if (this.getConfig().getBoolean("ranks." + p.getName() + ".saveItems")) {
					p.sendMessage(ChatColor.GREEN + "Your items has been saved!");
				} else {
					p.getInventory().clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
				}
				ItemStack ss = new ItemStack(Material.COMPASS, 1);
				ItemMeta ssm = ss.getItemMeta();
				if (!this.hub.contains(p)) {
					this.hub.add(p);
				}
				ssm.setDisplayName(ChatColor.GREEN + "Server Selector");
				ss.setItemMeta(ssm);
				if (!this.getConfig().getBoolean("ranks." + p.getName() + ".saveItems")) {
					p.getInventory().addItem(new ItemStack[]{ss});
					p.getInventory().clear();
					p.getInventory().addItem(new ItemStack[]{ss});
				}
				if (this.survival.contains(p)) {
					PlayerInventory pi = p.getInventory();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has quit Factions!"));
					w6 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x6 = this.settings.getData().getDouble("warps.kb0000009.x");
					this.survival.remove(p);
					y6 = this.settings.getData().getDouble("warps.kb0000009.y");
					z6 = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w6, x6, y6, z6));
					p.teleport(new Location(w6, x6, y6, z6));
					p.teleport(new Location(w6, x6, y6, z6));
					p.teleport(new Location(w6, x6, y6, z6));
				}
				if (this.rpg.contains(p)) {
					PlayerInventory pi = p.getInventory();
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has quit GTA!"));
					w6 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x6 = this.settings.getData().getDouble("warps.kb0000009.x");
					this.rpg.remove(p);
					y6 = this.settings.getData().getDouble("warps.kb0000009.y");
					z6 = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w6, x6, y6, z6));
				}
				if (this.teampvp.contains(p)) {
					PlayerInventory pi = p.getInventory();
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has quit TeamPvP!"));
					w6 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x6 = this.settings.getData().getDouble("warps.kb0000009.x");
					this.teampvp.remove(p);
					y6 = this.settings.getData().getDouble("warps.kb0000009.y");
					z6 = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w6, x6, y6, z6));
				}
				if (this.zahc.contains(p)) {
					PlayerInventory pi = p.getInventory();
					pi.clear();
					p.getInventory().setHelmet(new ItemStack(Material.AIR));
					p.getInventory().setChestplate(new ItemStack(Material.AIR));
					p.getInventory().setLeggings(new ItemStack(Material.AIR));
					p.getInventory().setBoots(new ItemStack(Material.AIR));
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " has quit ZAHC"));
					w6 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
					x6 = this.settings.getData().getDouble("warps.kb0000009.x");
					this.zahc.remove(p);
					y6 = this.settings.getData().getDouble("warps.kb0000009.y");
					z6 = this.settings.getData().getDouble("warps.kb0000009.z");
					p.teleport(new Location(w6, x6, y6, z6));
				}
				PlayerInventory pi = p.getInventory();
				w6 = Bukkit.getServer().getWorld(this.settings.getData().getString("warps.kb0000009.world"));
				x6 = this.settings.getData().getDouble("warps.kb0000009.x");
				y6 = this.settings.getData().getDouble("warps.kb0000009.y");
				z6 = this.settings.getData().getDouble("warps.kb0000009.z");
				p.teleport(new Location(w6, x6, y6, z6));
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("clan")) {
				int codec = this.getConfig().getInt("ranks." + p.getName() + ".clan.codec");
				String DisplayName = this.getConfig().getString("clan." + codec + ".DisplayName");
				int PlayerAmount = this.getConfig().getInt("clan." + codec + ".PlayerAmount");
				Boolean inClan = this.getConfig().getBoolean("ranks." + p.getName() + ".clan.inClan");
				int codeco = this.getConfig().getInt("clan." + codec + ".Codec");
				String ClanOwner = this.getConfig().getString("clan." + codec + ".ClanOwner");
				Boolean Player2 = this.getConfig().getBoolean("clan." + codec + ".players" + p.getName() + ".enabled");
				if (!this.survival.contains(p)) {
					p.sendMessage("Unknown command. Type /help for help.");
					return true;
				}
				if (args.length == 0) {
					p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/clan help");
					return true;
				}
				if (args[0].equalsIgnoreCase("help")) {
					p.sendMessage(ChatColor.GRAY + "---------------------------------------");
					p.sendMessage(ChatColor.GOLD + "Commands:");
					p.sendMessage(ChatColor.AQUA + "/clan help");
					p.sendMessage(ChatColor.AQUA + "/clan create <Display-Name>");
					p.sendMessage(ChatColor.AQUA + "/clan join <Clan-Name>");
					p.sendMessage(ChatColor.AQUA + "/clan leave");
					p.sendMessage(ChatColor.AQUA + "/clan disband");
					p.sendMessage(ChatColor.AQUA + "/clan show");
					p.sendMessage(ChatColor.AQUA + "/clan invite <Player-Name>");
					p.sendMessage(ChatColor.AQUA + "/clan help");
					p.sendMessage(ChatColor.GRAY + "---------------------------------------");
					return true;
				}
				if (args[0].equalsIgnoreCase("disband")) {
					if (args.length != 1) {
						p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/clan leave");
						return true;
					}
					String oldClanDisplayName = this.getConfig().getString("clan." + codec + ".DisplayName");
					this.getConfig().set("clan." + oldClanDisplayName, false);
					this.saveConfig();
					int oldClanCodec = this.getConfig().getInt("clan." + codec + ".DisplayName");
					int oldClanPlayerAmount = this.getConfig().getInt("clan." + codec + ".PlayerAmount");
					this.getConfig().set("ranks." + p.getName() + ".clan.inClan", false);
					this.getConfig().set("ranks." + p.getName() + ".clan.invited.boolean", false);
					this.getConfig().set("ranks." + p.getName() + ".clan.invited.player", "Zenator");
					this.getConfig().set("ranks." + p.getName() + ".clan.invited.clan", "Zenator");
					this.getConfig().set("ranks." + p.getName() + ".clan.codec", 1);
					this.getConfig().set("clan." + oldClanCodec + ".PlayerAmount", (oldClanPlayerAmount - 1));
					this.saveConfig();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has disbanded the clan " + ChatColor.GOLD + DisplayName));
					codec = this.getConfig().getInt("ranks." + p.getName() + ".clan.codec");
					DisplayName = this.getConfig().getString("clan." + codec + ".DisplayName");
					PlayerAmount = this.getConfig().getInt("clan." + codec + ".PlayerAmount");
					codeco = this.getConfig().getInt("clan." + codec + ".Codec");
					ClanOwner = this.getConfig().getString("clan." + codec + ".ClanOwner");
					Player2 = this.getConfig().getBoolean("clan." + codec + ".players" + p.getName() + ".enabled");
					p.sendMessage(ChatColor.GREEN + "You have successfully left: " + ChatColor.GOLD + oldClanDisplayName);
					return true;
				}
				if (args[0].equalsIgnoreCase("leave")) {
					if (args.length != 1) {
						p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/clan leave");
						return true;
					}
					ClanOwner = this.getConfig().getString("clan." + codec + ".ClanOwner");
					if (p.getName().equals(ClanOwner)) {
						p.sendMessage(ChatColor.RED + "You cannot leave your own faction. Type '/clan disband' instead");
						return true;
					}
					String oldClanDisplayName = this.getConfig().getString("clan." + codec + ".DisplayName");
					int oldClanCodec = this.getConfig().getInt("clan." + codec + ".DisplayName");
					int oldClanPlayerAmount = this.getConfig().getInt("clan." + codec + ".PlayerAmount");
					this.getConfig().set("ranks." + p.getName() + ".clan.inClan", false);
					this.getConfig().set("ranks." + p.getName() + ".clan.invited.boolean", false);
					this.getConfig().set("ranks." + p.getName() + ".clan.invited.player", "Zenator");
					this.getConfig().set("ranks." + p.getName() + ".clan.invited.clan", "Zenator");
					this.getConfig().set("ranks." + p.getName() + ".clan.codec", 1);
					this.getConfig().set("clan." + oldClanCodec + ".PlayerAmount", (oldClanPlayerAmount - 1));
					this.saveConfig();
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has left " + ChatColor.GOLD + DisplayName));
					codec = this.getConfig().getInt("ranks." + p.getName() + ".clan.codec");
					DisplayName = this.getConfig().getString("clan." + codec + ".DisplayName");
					PlayerAmount = this.getConfig().getInt("clan." + codec + ".PlayerAmount");
					codeco = this.getConfig().getInt("clan." + codec + ".Codec");
					ClanOwner = this.getConfig().getString("clan." + codec + ".ClanOwner");
					Player2 = this.getConfig().getBoolean("clan." + codec + ".players" + p.getName() + ".enabled");
					p.sendMessage(ChatColor.GREEN + "You have successfully left: " + ChatColor.GOLD + oldClanDisplayName);
					return true;
				}
				if (args[0].equalsIgnoreCase("invite")) {
					if (args.length != 2) {
						p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/clan invite <Player-Name>");
						return true;
					}
					codec = this.getConfig().getInt("ranks." + p.getName() + ".clan.codec");
					codeco = this.getConfig().getInt("clan." + codec + ".Codec");
					DisplayName = this.getConfig().getString("clan." + codec + ".DisplayName");
					PlayerAmount = this.getConfig().getInt("clan." + codec + ".PlayerAmount");
					String string2 = this.getConfig().getString("clan." + codec + ".ClanOwner");
					Boolean bl2 = getConfig().getBoolean("clan" + codec + ".players" + p.getName() + ".enabled");
					if (!p.getName().equals(string2)) {
						p.sendMessage(ChatColor.RED + "Only the clan owner can use this command!");
						return true;
					}
					Player player2 = Bukkit.getPlayer(args[1]);
					if (player2 == null) {
						p.sendMessage(ChatColor.RED + "Error: " + ChatColor.AQUA + "Player not found");
						return true;
					}
					getConfig().set("ranks." + player2.getName() + ".clan.invited.boolean", true);
					getConfig().set("ranks." + player2.getName() + ".clan.invited.player", p);
					getConfig().set("ranks." + player2.getName() + ".clan.invited.clan", DisplayName);
					saveConfig();
					p.sendMessage(ChatColor.GREEN + "You have successfully invited: " + ChatColor.GOLD + player2.getName());
					player2.sendMessage(ChatColor.GREEN + "You have been invited to " + ChatColor.GOLD + DisplayName + ChatColor.GREEN + " by " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + ". Type '/clan join " + DisplayName + "' to accept.");
					return true;
				}
				if (args[0].equalsIgnoreCase("create")) {
					if (args.length != 2) {
						p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/clan create <Display-Name>");
						return true;
					}
					Boolean bl = getConfig().getBoolean("ranks." + p.getName() + ".clan.inClan");
					if (getConfig().getBoolean("clan." + args[1])) {
						p.sendMessage(ChatColor.RED + "That clan already exists.");
						return true;
					}
					if (bl.booleanValue()) {
						p.sendMessage(ChatColor.RED + "You are already associated with a clan!");
						return true;
					}
					Random serializable = new Random();
					int n4 = 1;
					if (n4 <= 10) {
						int n5 = serializable.nextInt(100);
						int n6 = serializable.nextInt(1000);
						int n7 = serializable.nextInt(10000);
						int n8 = serializable.nextInt(100000);
						int n9 = serializable.nextInt(1000000);
						int n10 = serializable.nextInt(10000000);
						int n11 = serializable.nextInt(20000000);
						int n12 = serializable.nextInt(10000);
						int n13 = serializable.nextInt(1000);
						int n14 = n5 + n6 + n7 + n8 + n9 + n12 + n13 + n10 + n11;
						getConfig().set("ranks." + p.getName() + ".clan.codec", n14);
						getConfig().set("ranks." + p.getName() + ".clan.inClan", true);
						getConfig().set("clan." + n14 + ".DisplayName", args[1]);
						getConfig().set("clan." + n14 + ".ClanOwner", p.getName());
						getConfig().set("clan." + n14 + ".PlayerAmount", 1);
						getConfig().set("clan." + (String) args[1] + ".enabled", true);
						getConfig().set("clan." + (String) args[1] + ".codec", n14);
						getConfig().set("clan.code." + (String) args[1], n14);
						getConfig().set("clan." + n14 + ".Codec", n14);
						getConfig().set("clan." + n14 + ".players." + p.getName() + ".enabled", true);
						int n3 = getConfig().getInt("ranks." + p.getName() + ".clan.codec");
						int n2 = getConfig().getInt("clan." + n3 + ".Codec");
						String string = getConfig().getString("clan." + n3 + ".DisplayName");
						int n = getConfig().getInt("clan." + n3 + ".PlayerAmount");
						String string2 = getConfig().getString("clan." + n3 + ".ClanOwner");
						Boolean bl2 = getConfig().getBoolean("clan" + n3 + ".players" + p.getName() + ".enabled");
						Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has created a clan named " + ChatColor.GOLD + string));
						saveConfig();
						p.sendMessage(ChatColor.GOLD + "Clan created successfully!");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("show")) {
					if (args.length == 2) {
						String serializable = args[2];
						Boolean bl3 = getConfig().getBoolean("clan." + serializable + ".enabled");
						if (!bl3.booleanValue() || bl3 == null) {
							p.sendMessage(ChatColor.RED + "Clan not found!");
							return true;
						}
						if (bl3.booleanValue()) {
							int n15 = getConfig().getInt("clan." + serializable + ".codec");
							int n3 = getConfig().getInt("ranks." + p.getName() + ".clan.codec");
							int n2 = getConfig().getInt("clan." + n15 + ".Codec");
							String string = getConfig().getString("clan." + n15 + ".DisplayName");
							int n = getConfig().getInt("clan." + n15 + ".PlayerAmount");
							String string2 = getConfig().getString("clan." + n15 + ".ClanOwner");
							Boolean bl2 = getConfig().getBoolean("clan." + n15 + ".players." + p.getName() + ".enabled");
							p.sendMessage(ChatColor.GRAY + "---------------------------------------");
							p.sendMessage(ChatColor.GOLD + string + ":");
							p.sendMessage(ChatColor.AQUA + "Player Amount - " + ChatColor.GOLD + n);
							p.sendMessage(ChatColor.AQUA + "Owner - " + ChatColor.GOLD + string2);
							p.sendMessage(ChatColor.AQUA + "Activated - " + ChatColor.GOLD + bl2);
							p.sendMessage(ChatColor.AQUA + "Codec - " + ChatColor.GOLD + n2);
							p.sendMessage(ChatColor.GRAY + "---------------------------------------");
							return true;
						}
						p.sendMessage(ChatColor.RED + "An internal error has occured, Please contact a server admin if this error continues.");
						return true;
					}
					if (args.length == 1) {
						Boolean bl = getConfig().getBoolean("ranks." + p.getName() + ".clan.inClan");
						if (!bl.booleanValue()) {
							p.sendMessage(ChatColor.RED + "You are not associated with a clan! Type /clan create <Clan-Name>");
							return true;
						}
						int n3 = getConfig().getInt("ranks." + p.getName() + ".clan.codec");
						int n2 = getConfig().getInt("clan." + n3 + ".Codec");
						String string = getConfig().getString("clan." + n3 + ".DisplayName");
						int n = getConfig().getInt("clan." + n3 + ".PlayerAmount");
						String string2 = getConfig().getString("clan." + n3 + ".ClanOwner");
						Boolean bl2 = getConfig().getBoolean("clan." + n3 + ".players." + p.getName() + ".enabled");
						p.sendMessage(ChatColor.GRAY + "---------------------------------------");
						p.sendMessage(ChatColor.GOLD + string + ":");
						p.sendMessage(ChatColor.AQUA + "Player Amount - " + ChatColor.GOLD + n);
						p.sendMessage(ChatColor.AQUA + "Owner - " + ChatColor.GOLD + string2);
						p.sendMessage(ChatColor.AQUA + "Activated - " + ChatColor.GOLD + bl2);
						p.sendMessage(ChatColor.AQUA + "Codec - " + ChatColor.GOLD + n2);
						p.sendMessage(ChatColor.GRAY + "---------------------------------------");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("join")) {
					if (args.length != 2) {
						p.sendMessage(ChatColor.RED + "Invalid Syntax: " + ChatColor.AQUA + "/clan join <Clan-Name>");
						return true;
					}
					Boolean bl = getConfig().getBoolean("ranks." + p.getName() + ".clan.inClan");
					Boolean serializable = getConfig().getBoolean("ranks." + p.getName() + ".clan.invited.boolean");
					if (!serializable.booleanValue()) {
						p.sendMessage(ChatColor.RED + "You are not invited to this faction!");
						return true;
					}
					if (bl.booleanValue()) {
						p.sendMessage(ChatColor.RED + "You are already associated with a clan!");
						return true;
					}
					Boolean bl4 = getConfig().getBoolean("clan." + args[1]);
					int n16 = getConfig().getInt("clan.code." + args[1]);
					if (bl4.booleanValue()) {
						int n = getConfig().getInt("clan." + n16 + ".PlayerAmount");
						getConfig().set("ranks." + p.getName() + ".clan.codec", n16);
						getConfig().set("ranks." + p.getName() + ".clan.invited.boolean", false);
						getConfig().set("ranks." + p.getName() + ".clan.invited.player", "Zenator");
						getConfig().set("ranks." + p.getName() + ".clan.invited.clan", "Zenator");
						getConfig().set("clan." + n16 + ".PlayerAmount", (n + 1));
						getConfig().set("ranks." + p.getName() + ".clan.inClan", true);
						getConfig().set("clan." + n16 + ".players." + p.getName() + ".enabled", true);
						saveConfig();
						int n3 = getConfig().getInt("ranks." + p.getName() + ".clan.codec");
						int n2 = getConfig().getInt("clan." + n3 + ".Codec");
						String string = getConfig().getString("clan." + n3 + ".DisplayName");
						String string2 = getConfig().getString("clan." + n3 + ".ClanOwner");
						Boolean bl2 = getConfig().getBoolean("clan." + n3 + ".players" + p.getName() + ".enabled");
						p.sendMessage(ChatColor.GREEN + "You have successfully joined: " + ChatColor.GOLD + string);
						Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.GREEN + " has joined " + ChatColor.GOLD + string));
						return true;
					}
					p.sendMessage(ChatColor.RED + "Clan not found!");
					return true;
				}
			}
			if (cmd.getName().equalsIgnoreCase("kit")) {
				if (teampvp.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing TeamPvP");
					return true;
				}
				if (hub.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command when you are in the hub");
					return true;
				}
				if (rpg.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing GTA");
					return true;
				}
				if (creative.contains(p)) {
					p.sendMessage(ChatColor.RED + "You cannot use this command while playing Creative");
					return true;
				}
				if (args.length == 0) {
					Mwapper.listAllKits(p);
					return true;
				}
				if (args[0].equalsIgnoreCase("default")) {
					PlayerInventory playerInventory = p.getInventory();
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_SWORD, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.STONE_PICKAXE, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.STONE_AXE, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.STONE_SPADE, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_HELMET, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_CHESTPLATE, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_LEGGINGS, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.LEATHER_BOOTS, 1)});
					playerInventory.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
					p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Default");
					return true;
				}
				if (args[0].equalsIgnoreCase("vip")) {
					if (getConfig().getBoolean("ranks." + p.getName() + ".vip", true) || getConfig().getBoolean("ranks." + p.getName() + ".owner", true)) {
						PlayerInventory playerInventory = p.getInventory();
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_PICKAXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_AXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_SPADE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_HELMET, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_CHESTPLATE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_LEGGINGS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_BOOTS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
						p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "VIP");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + GOLD + "VIP" + RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("vip+")) {
					if (getConfig().getBoolean("ranks." + p.getName() + ".vipplus", true) || getConfig().getBoolean("ranks." + p.getName() + ".owner", true)) {
						PlayerInventory playerInventory = p.getInventory();
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_PICKAXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_AXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SPADE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_CHESTPLATE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_LEGGINGS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
						p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "VIP+");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + GOLD + "VIP+" + RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("mvp")) {
					if (getConfig().getBoolean("ranks." + p.getName() + ".mvp", true) || getConfig().getBoolean("ranks." + p.getName() + ".owner", true)) {
						PlayerInventory playerInventory = p.getInventory();
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_PICKAXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_AXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SPADE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 4)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_CHESTPLATE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_LEGGINGS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
						p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "MVP");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + GOLD + "MVP" + RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("mvp+")) {
					if (getConfig().getBoolean("ranks." + p.getName() + ".mvpplus", true) || getConfig().getBoolean("ranks." + p.getName() + ".owner", true)) {
						PlayerInventory playerInventory = p.getInventory();
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_PICKAXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_AXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SPADE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.GOLDEN_APPLE, 12)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_CHESTPLATE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_LEGGINGS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_BOOTS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
						p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "MVP+");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + GOLD + "MVP+" + RED + " to use this command!");
					return true;
				}
				if (args[0].equalsIgnoreCase("bedrock")) {
					if (getConfig().getBoolean("ranks." + p.getName() + ".bedrock", true) || getConfig().getBoolean("ranks." + p.getName() + ".owner", true)) {
						PlayerInventory playerInventory = p.getInventory();
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND_SWORD, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.STONE_PICKAXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_AXE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.STONE_SPADE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.BOW, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.COOKED_BEEF, 12)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_HELMET, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.GOLD_CHESTPLATE, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.GOLD_LEGGINGS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.IRON_BOOTS, 1)});
						playerInventory.addItem(new ItemStack[]{new ItemStack(Material.ARROW, 32)});
						p.sendMessage(ChatColor.GREEN + "You have been given kit: " + ChatColor.GOLD + "Bedrock");
						return true;
					}
					p.sendMessage(ChatColor.RED + "You must have rank " + GOLD + "Bedrock" + RED + " to use this command!");
					return true;
				}
				Mwapper.listAllKits(p);
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("afk")) {
				if (afk.contains(p)) {
					afk.remove(p);
					Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " is no longer afk!"));
					return true;
				}
				afk.add(p);
				Bukkit.broadcastMessage((String) (ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + "SkorrloreGaming" + ChatColor.DARK_GREEN + "] " + ChatColor.GOLD + p.getName() + ChatColor.AQUA + " is now afk!"));
				return true;
			}
			if (cmd.getName().equalsIgnoreCase("kits")) {
				Mwapper.listAllKits(p);
			}
			return false;
		}
		p.sendMessage(ChatColor.GREEN + "You must wait " + ChatColor.LIGHT_PURPLE + "15" + ChatColor.GREEN + " seconds before using any commands.");
		return false;
	}

}

