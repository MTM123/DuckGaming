/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import fr.xephi.authme.api.NewAPI;

import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.skorrloregaming.AntiCheat;
import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.CustomRecipes;
import me.skorrloregaming.Directory;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Go;
import me.skorrloregaming.Logger;
import me.skorrloregaming.ParticleManager;
import me.skorrloregaming.SignShop;
import me.skorrloregaming.SolidStorage;
import me.skorrloregaming.impl.CustomNpc;
import me.skorrloregaming.impl.DesignPattern;
import me.skorrloregaming.impl.EnchantInfo;
import me.skorrloregaming.impl.Explosion;
import me.skorrloregaming.impl.FireworkEffectPlayer;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.SignInfo;
import me.skorrloregaming.impl.VanishInfo;
import me.skorrloregaming.impl.extreme.ExtremeDouble;
import me.skorrloregaming.listeners.AuthMe_Listener;
import me.skorrloregaming.listeners.Factions_Listener;
import me.skorrloregaming.listeners.ProtocolLib_Listener;
import me.skorrloregaming.listeners.ProtocolSupport_Listener;
import me.skorrloregaming.listeners.Votifier_Listener;
import me.skorrloregaming.listeners.mcMMO_Listener;
import me.skorrloregaming.runnable.CombatTimer;
import me.skorrloregaming.runnable.DelayedTeleport;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.SkullType;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Directional;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

public class Server
		extends JavaPlugin
		implements Listener {
	public static String tempMotd = "/unspecified";
	public static ArrayList<UUID> hub = new ArrayList();
	public static ArrayList<UUID> kitpvp = new ArrayList();
	public static ArrayList<UUID> factions = new ArrayList();
	public static ArrayList<UUID> survival = new ArrayList();
	public static ArrayList<UUID> skyfight = new ArrayList();
	public static ArrayList<UUID> creative = new ArrayList();
	public static ArrayList<UUID> simpleDelayedTask = new ArrayList();
	public static ArrayList<UUID> confirmRepairShop = new ArrayList();
	public static ArrayList<UUID> confirmUnregisterNpc = new ArrayList();
	public ArrayList<UUID> canDoubleJump = new ArrayList();
	public ArrayList<UUID> inStaffChat = new ArrayList();
	public HashSet<UUID> mutedPlayers = new HashSet();
	public HashSet<UUID> semiOppedPlayers = new HashSet();
	public static ConcurrentMap<UUID, DelayedTeleport> delayedTeleports = new ConcurrentHashMap<UUID, DelayedTeleport>();
	public static ConcurrentMap<UUID, ExtremeDouble> inCombat = new ConcurrentHashMap<UUID, ExtremeDouble>();
	public static ConcurrentMap<UUID, ServerMinigame> moderatingPlayers = new ConcurrentHashMap<UUID, ServerMinigame>();
	public ConcurrentMap<UUID, Integer> hubScoreboardTitleIndex = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, Integer> skyfightScore = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, UUID> skyfightTag = new ConcurrentHashMap<UUID, UUID>();
	public ConcurrentMap<UUID, Integer> potionsKitCooldownKitpvp = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, Integer> starterKitCooldownKitpvp = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, Integer> recruitKitCooldownSurvival = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, Integer> recruitKitCooldownFactions = new ConcurrentHashMap<UUID, Integer>();
	public ConcurrentMap<UUID, UUID> tpaRequests = new ConcurrentHashMap<UUID, UUID>();
	public ConcurrentMap<UUID, UUID> messageRequests = new ConcurrentHashMap<UUID, UUID>();
	public ConcurrentMap<UUID, VanishInfo> vanishedPlayers = new ConcurrentHashMap<UUID, VanishInfo>();
	public ConcurrentMap<Player, Player> savePersonalChest = new ConcurrentHashMap<Player, Player>();
	public ConcurrentMap<Player, Player> saveOtherInventory = new ConcurrentHashMap<Player, Player>();
	public ConcurrentMap<Player, SignInfo> signEditParam = new ConcurrentHashMap<Player, SignInfo>();
	public static ConfigurationManager ramConfig;
	public static ConfigurationManager warpConfig;
	public static ConfigurationManager signConfig;
	public static ConfigurationManager factionsConfig;
	public static ConfigurationManager survivalConfig;
	public static ConfigurationManager npcConfig;
	public static ConfigurationManager banConfig;
	public static String pluginName;
	public static String pluginLabel;
	public static String lastKnownHubWorld;
	public static Plugin plugin;
	public static ProtocolLib_Listener protoListener;
	public static AuthMe_Listener authListener;
	public static Factions_Listener factionsListener;
	public static ProtocolSupport_Listener protoSupportListener;
	public static mcMMO_Listener mmoListener;
	public static Votifier_Listener voteListener;
	public static String defaultJoinMessage;
	public static String defaultQuitMessage;
	public static AntiCheat anticheat;
	public FireworkEffectPlayer firework = new FireworkEffectPlayer();
	public static long serverStartTime;
	public static boolean pluginDebug;
	HashMap<Player, ItemStack> storedItem = new HashMap();
	private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$Material;

	static {
		pluginName = "SkorrloreGaming";
		protoListener = null;
		authListener = null;
		factionsListener = null;
		protoSupportListener = null;
		mmoListener = null;
		voteListener = null;
		defaultJoinMessage = null;
		defaultQuitMessage = null;
		anticheat = null;
		serverStartTime = 0L;
		pluginDebug = false;
	}

	public void onEnable() {
		serverStartTime = System.currentTimeMillis();
		pluginLabel = Go.Legacy.tag;
		plugin = this;
		anticheat = new AntiCheat((Plugin) this);
		ramConfig = new ConfigurationManager();
		warpConfig = new ConfigurationManager();
		signConfig = new ConfigurationManager();
		factionsConfig = new ConfigurationManager();
		survivalConfig = new ConfigurationManager();
		banConfig = new ConfigurationManager();
		npcConfig = new ConfigurationManager();
		this.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) this);
		this.getConfig().options().copyDefaults(true);
		Go.createDataFolder((Plugin) this);
		warpConfig.setup(new File(this.getDataFolder(), "warps.yml"));
		signConfig.setup(new File(this.getDataFolder(), "shops.yml"));
		factionsConfig.setup(new File(this.getDataFolder(), "factions.yml"));
		survivalConfig.setup(new File(this.getDataFolder(), "survival.yml"));
		banConfig.setup(new File(this.getDataFolder(), "banned.yml"));
		npcConfig.setup(new File(this.getDataFolder(), "npc_storage.yml"));
		lastKnownHubWorld = Go.getZoneLocation("hub", (Plugin) this).getWorld().getName().toString();
		CustomRecipes.loadRecipes();
		if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
			protoListener = new ProtocolLib_Listener((Plugin) this);
			protoListener.register();
		} else {
			this.getLogger().warning("Could not locate 'ProtocolLib' as a plugin on your server.");
			this.getLogger().warning("Disabling helpful features associated with 'ProtocolLib'.");
		}
		if (Bukkit.getPluginManager().getPlugin("AuthMe") != null) {
			authListener = new AuthMe_Listener((Plugin) this);
			authListener.register();
		} else {
			this.getLogger().warning("Could not locate 'AuthMe' as a plugin on your server.");
			this.getLogger().warning("Disabling helpful features associated with 'AuthMe'.");
		}
		if (Bukkit.getPluginManager().getPlugin("mcMMO") != null) {
			mmoListener = new mcMMO_Listener((Plugin) this);
			mmoListener.register();
		} else {
			this.getLogger().warning("Could not locate 'mcMMO' as a plugin on your server.");
			this.getLogger().warning("Disabling helpful features associated with 'mcMMO'.");
		}
		if (Bukkit.getPluginManager().getPlugin("ProtocolSupport") != null) {
			protoSupportListener = new ProtocolSupport_Listener((Plugin) this);
			protoSupportListener.register();
		} else {
			this.getLogger().warning("Could not locate 'ProtocolSupport' as a plugin on your server.");
			this.getLogger().warning("Disabling helpful features associated with 'ProtocolSupport'.");
		}
		if (Bukkit.getPluginManager().getPlugin("Votifier") != null) {
			voteListener = new Votifier_Listener((Plugin) this);
			voteListener.register();
		} else {
			this.getLogger().warning("Could not locate 'Votifier' as a plugin on your server.");
			this.getLogger().warning("Disabling helpful features associated with 'Votifier'.");
		}
		factionsListener = new Factions_Listener((Plugin) this);
		factionsListener.register();
		anticheat.register();
		Bukkit.getScheduler().runTaskTimer((Plugin) this, new Runnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (Go.getCurrentMinigame(player) != ServerMinigame.HUB) continue;
					Server.this.fetchLobby(player);
				}
			}
		}, 10L, 10L);
	}

	public static long getUptime() {
		return System.currentTimeMillis() - serverStartTime;
	}

	public static WorldGuardPlugin getWorldGuard() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	public static WorldEditPlugin getWorldEdit() {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) {
			return null;
		}
		return (WorldEditPlugin) plugin;
	}

	String centerText(String text, int lineLength) {
		StringBuilder builder = new StringBuilder(text);
		char space = ' ';
		int distance = (lineLength - text.length()) / 2;
		int i = 0;
		while (i < distance) {
			builder.insert(0, space);
			builder.append(space);
			++i;
		}
		return builder.toString();
	}

	@EventHandler
	public void PlayerItemHeldEvent(PlayerItemHeldEvent event) {
		this.returnItem(event.getPlayer());
	}

	@EventHandler
	public void PlayerDropItemEvent(PlayerDropItemEvent event) {
		this.returnItem(event.getPlayer());
	}

	@EventHandler
	public void ItemDespawnEvent(ItemDespawnEvent event) {
		ItemStack item = event.getEntity().getItemStack();
		if (item.hasItemMeta() && Go.isRawRepairable(item)) {
			ItemMeta meta = item.getItemMeta();
			ItemStack book = Go.createMaterial(Material.ENCHANTED_BOOK);
			int foundEnchants = 0;
			Enchantment[] arrenchantment = meta.getEnchants().keySet().toArray(new Enchantment[0]);
			int n = arrenchantment.length;
			int n2 = 0;
			while (n2 < n) {
				Enchantment enchant = arrenchantment[n2];
				int multiplier = (Integer) meta.getEnchants().get( enchant);
				Go.addBookEnchantment(book, enchant, multiplier);
				++foundEnchants;
				++n2;
			}
			if (foundEnchants > 0) {
				event.getEntity().getWorld().dropItemNaturally(event.getLocation(), book);
				event.getEntity().getWorld().playSound(event.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void EntityShootBowEvent(final EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

				@Override
				public void run() {
					Server.this.returnItem((Player) event.getEntity());
				}
			}, 5L);
		}
	}

	private void returnItem(Player player) {
		if (this.storedItem.containsKey( player.getPlayer())) {
			player.getInventory().setItem(9, this.storedItem.get( player));
			player.updateInventory();
			this.storedItem.remove( player);
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Player player;
		if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player && !factions.contains((player = (Player) event.getEntity().getShooter()).getUniqueId()) && !survival.contains(player.getUniqueId())) {
			event.getEntity().remove();
		}
	}

	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		if (!Go.isPluginPirated()) {
			if (!tempMotd.equals("/unspecified")) {
				event.setMotd(tempMotd.trim());
			} else {
				String supportedVersions = "[" +  ChatColor.GREEN + "1.7" +  ChatColor.WHITE + "-" +  ChatColor.GREEN + " 1.12" +  ChatColor.WHITE + "]";
				String lineOne =  ChatColor.AQUA + " \u27f3 " +  ChatColor.WHITE + pluginName +  ChatColor.WHITE + " " + supportedVersions +  ChatColor.AQUA + " \u27f2";
				String lineTwo =  ChatColor.AQUA + "Server updated to support 1.12.1 clients.";
				event.setMotd(Go.createMotd(lineOne, lineTwo, new boolean[]{true, true}));
			}
		} else {
			event.setMotd("SkorrloreGaming | " + event.getMotd());
			event.setMaxPlayers(20);
		}
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (Server.anticheat.antiafk.lackingMovementMinutes.containsKey(player.getUniqueId())) {
			Server.anticheat.antiafk.lackingMovementMinutes.remove(player.getUniqueId());
		}
		if (Go.getRankId(player, (Plugin) this) > -1 && this.inStaffChat.contains(player.getUniqueId())) {
			String message = "[sc] " + ChatColor.stripColor((String) Go.toRankTag(Go.getRank(player, (Plugin) this))) + player.getName() + ": " + event.getMessage();
			for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
				if (Go.getRankId(otherPlayer, (Plugin) this) <= -1) continue;
				otherPlayer.sendMessage(message);
			}
			Logger.info(message);
			event.setCancelled(true);
			return;
		}
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getName().equals(player.getName())) continue;
			int rankID = Go.getRankId(onlinePlayer, (Plugin) this);
			if (onlinePlayer.hasPermission("skorrloregaming.events.chatlisten") || rankID > -1) {
				onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
				continue;
			}
			if (!event.getMessage().contains(onlinePlayer.getName())) continue;
			onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
		}
		UUID id = player.getUniqueId();
		String world = player.getWorld().getName();
		String msg =  ChatColor.GRAY + "[" +  ChatColor.WHITE + world +  ChatColor.GRAY + "] " +  ChatColor.RESET + "<" + Go.toRankTag(Go.getRank(id, (Plugin) this)) + player.getName() +  ChatColor.RESET + "> " + event.getMessage();
		if (this.mutedPlayers.contains(player.getUniqueId())) {
			player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.RED + "Failed. " +  ChatColor.GRAY + "You cannot publicly chat while you are muted.");
			msg =  ChatColor.GRAY + "[" +  ChatColor.WHITE + world +  ChatColor.GRAY + "] " +  ChatColor.RESET + "<" + Go.toRankTag(Go.getRank(id, (Plugin) this)) + player.getName() +  ChatColor.RESET + "> " +  ChatColor.GRAY +  ChatColor.ITALIC + event.getMessage();
			for (Player p : Bukkit.getOnlinePlayers()) {
				int rankID = Go.getRankId(p, (Plugin) this);
				if (!p.hasPermission("skorrloregaming.events.chatlisten") && rankID <= -1) continue;
				msg = anticheat.processAntiSwear(player, msg);
				p.sendMessage(msg);
			}
		}
		int rank = Go.getRankId(player, (Plugin) this);
		if (player.hasPermission("skorrloregaming.events.chatcolor") || rank > -1 || rank < -2) {
			msg = ChatColor.translateAlternateColorCodes((char) '&', (String) msg);
		}
		Logger.info(msg);
		boolean isCancelled = event.isCancelled();
		event.setCancelled(true);
		if (!isCancelled && !this.mutedPlayers.contains(player.getUniqueId())) {
			for (Player tp : Bukkit.getOnlinePlayers()) {
				msg = anticheat.processAntiSwear(player, msg);
				tp.sendMessage(msg);
			}
		}
	}

	public int performBuggedLeave(Player player, boolean noRestore, boolean noLog) {
		int changes = 0;
		changes += this.leaveSkyfight(player, noLog);
		changes += this.leaveFactions(player, noRestore, noLog);
		changes += this.leaveCreative(player, noRestore, noLog);
		changes += this.leaveSurvival(player, noRestore, noLog);
		Go.clearPlayer(player);
		return changes += this.leaveKitpvp(player, noRestore, noLog);
	}

	public void enterKitpvp(Player player, boolean noRestore, boolean noLog) {
		if (!kitpvp.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = Go.getCurrentMinigame(player);
			int changes = this.performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && minigame != ServerMinigame.HUB && minigame != ServerMinigame.UNKNOWN) {
				return;
			}
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				this.hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			kitpvp.add(player.getUniqueId());
			Go.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "kitpvp");
				if (!success) {
					player.teleport(Go.getZoneLocation("kitpvp", (Plugin) this));
					player.performCommand("kit starter");
				}
			} else {
				player.teleport(Go.getZoneLocation("kitpvp", (Plugin) this));
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has logged into " +  ChatColor.RED + "KitPvP"));
			}
			player.setAllowFlight(false);
			Go.Kitpvp.refreshScoreboard(player, (Plugin) this);
		}
	}

	public int leaveKitpvp(Player player, boolean noSave, boolean noLog) {
		if (kitpvp.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "kitpvp");
				} catch (Exception exception) {
					// empty catch block
				}
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has quit " +  ChatColor.RED + "KitPvP"));
			}
			kitpvp.remove(player.getUniqueId());
			player.setAllowFlight(true);
			Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			return 1;
		}
		return 0;
	}

	public void enterFactions(Player player, boolean noRestore, boolean noLog) {
		if (!factions.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = Go.getCurrentMinigame(player);
			int changes = this.performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && minigame != ServerMinigame.HUB && minigame != ServerMinigame.UNKNOWN) {
				return;
			}
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				this.hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			factions.add(player.getUniqueId());
			Go.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "factions");
				if (!success) {
					player.teleport(Go.getZoneLocation("factions", (Plugin) this));
					player.performCommand("kit recruit");
				}
			} else {
				player.teleport(Go.getZoneLocation("factions", (Plugin) this));
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has logged into " +  ChatColor.RED + "Factions"));
			}
			player.setAllowFlight(false);
		}
	}

	public int leaveFactions(Player player, boolean noSave, boolean noLog) {
		if (factions.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "factions");
				} catch (Exception exception) {
					// empty catch block
				}
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has quit " +  ChatColor.RED + "Factions"));
			}
			factions.remove(player.getUniqueId());
			player.setAllowFlight(true);
			return 1;
		}
		return 0;
	}

	public void enterSurvival(Player player, boolean noRestore, boolean noLog) {
		if (!survival.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = Go.getCurrentMinigame(player);
			int changes = this.performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && minigame != ServerMinigame.HUB && minigame != ServerMinigame.UNKNOWN) {
				return;
			}
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				this.hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			survival.add(player.getUniqueId());
			Go.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "survival");
				if (!success) {
					player.teleport(Go.getZoneLocation("survival", (Plugin) this));
					player.performCommand("kit recruit");
				}
			} else {
				player.teleport(Go.getZoneLocation("survival", (Plugin) this));
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has logged into " +  ChatColor.RED + "Survival"));
			}
			player.setAllowFlight(false);
		}
	}

	public int leaveSurvival(Player player, boolean noSave, boolean noLog) {
		if (survival.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "survival");
				} catch (Exception exception) {
					// empty catch block
				}
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has quit " +  ChatColor.RED + "Survival"));
			}
			survival.remove(player.getUniqueId());
			player.setAllowFlight(true);
			return 1;
		}
		return 0;
	}

	public void enterSkyfight(Player player, boolean noRestore, boolean noLog) {
		Player op;
		ServerMinigame minigame;
		int changes;
		UUID id;
		if (moderatingPlayers.containsKey(player.getUniqueId())) {
			noRestore = true;
			noLog = true;
		}
		if ((minigame = Go.getCurrentMinigame(player)) != ServerMinigame.SKYFIGHT && (changes = this.performBuggedLeave(player, noRestore, noLog)) == 0 && minigame != ServerMinigame.HUB && minigame != ServerMinigame.UNKNOWN) {
			return;
		}
		if (hub.contains(player.getUniqueId())) {
			hub.remove(player.getUniqueId());
			Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			this.hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
		}
		boolean initialConnect = false;
		if (!skyfight.contains(player.getUniqueId())) {
			skyfight.add(player.getUniqueId());
			initialConnect = true;
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has logged into " +  ChatColor.RED + "SkyFight"));
			}
		}
		player.setHealth(20.0);
		player.setLevel(0);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		Random random = new Random();
		int ran = random.nextInt(4);
		if (initialConnect) {
			Go.clearPlayer(player);
		}
		ItemStack sword = Go.createMaterial(Material.STONE_SWORD,  ChatColor.GOLD + "The Forbidding Katana");
		ItemStack bow = Go.createMaterial(Material.BOW,  ChatColor.GOLD + "The Forbidding Bow");
		ItemStack arrow = Go.createMaterial(Material.ARROW, 1,  ChatColor.GOLD + "The Forbidding Darts");
		ItemStack helmet = Go.createMaterial(Material.LEATHER_HELMET, 1,  ChatColor.GOLD + "The Forbidding Helmet");
		ItemStack chestplate = Go.createMaterial(Material.LEATHER_CHESTPLATE,  ChatColor.GOLD + "The Forbidding Chestpeice");
		ItemStack leggings = Go.createMaterial(Material.LEATHER_LEGGINGS,  ChatColor.GOLD + "The Forbidding Leggings");
		ItemStack boots = Go.createMaterial(Material.LEATHER_BOOTS,  ChatColor.GOLD + "The Forbidding Boots");
		bow = Go.addEnchant(bow, new EnchantInfo(Enchantment.ARROW_KNOCKBACK, 5));
		bow = Go.addEnchant(bow, new EnchantInfo(Enchantment.ARROW_INFINITE, 1));
		bow = Go.addEnchant(bow, new EnchantInfo(Enchantment.KNOCKBACK, 5));
		bow = Go.setUnbreakable(bow, true);
		sword = Go.addEnchant(sword, new EnchantInfo(Enchantment.KNOCKBACK, 5));
		sword = Go.setUnbreakable(sword, true);
		Color col = Color.BLUE;
		if (ran == 1) {
			col = Color.RED;
		}
		if (ran == 2) {
			col = Color.GREEN;
		}
		if (ran == 3) {
			col = Color.YELLOW;
		}
		helmet = Go.addLeatherColor(helmet, col);
		chestplate = Go.addLeatherColor(chestplate, col);
		leggings = Go.addLeatherColor(leggings, col);
		boots = Go.addLeatherColor(boots, col);
		player.getInventory().setItem(0, sword);
		player.getInventory().setItem(1, bow);
		if (Go.isNothing(player.getInventory().getItem(8)) || initialConnect) {
			player.getInventory().setItem(8, chestplate);
		}
		player.getInventory().setItem(9, arrow);
		if (Go.isNothing(player.getInventory().getHelmet()) || initialConnect) {
			player.getInventory().setHelmet(helmet);
		}
		if (Go.isNothing(player.getInventory().getChestplate()) || initialConnect) {
			player.getInventory().setChestplate(chestplate);
		}
		if (Go.isNothing(player.getInventory().getLeggings()) || initialConnect) {
			player.getInventory().setLeggings(leggings);
		}
		if (Go.isNothing(player.getInventory().getBoots()) || initialConnect) {
			player.getInventory().setBoots(boots);
		}
		player.teleport(Go.getZoneLocation("skyfight" + ran, plugin));
		player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
		this.skyfightScore.put(player.getUniqueId(), 0);
		if (this.skyfightTag.containsKey(player.getUniqueId())) {
			this.skyfightTag.remove(player.getUniqueId());
		}
		for (Map.Entry<UUID, UUID> id2 : this.skyfightTag.entrySet()) {
			if (!id2.getValue().toString().equals(player.getUniqueId().toString())) continue;
			this.skyfightScore.put(id2.getKey(), this.skyfightScore.get(id2.getKey()) + 1);
			Player tp = Bukkit.getPlayer((UUID) id2.getKey());
			tp.playSound(tp.getLocation(), Sound.BLOCK_NOTE_HARP, 1.0f, 1.0f);
			this.skyfightTag.remove(id2.getKey());
			break;
		}
		UUID[] skyfightPlayers = skyfight.toArray(new UUID[0]);
		Hashtable<String, Integer> list = new Hashtable<String, Integer>();
		UUID[] arruUID = skyfightPlayers;
		int n = arruUID.length;
		int n2 = 0;
		while (n2 < n) {
			id = arruUID[n2];
			op = Bukkit.getPlayer((UUID) id);
			list.put( ChatColor.GRAY + op.getName(), this.skyfightScore.get(op.getUniqueId()));
			++n2;
		}
		arruUID = skyfightPlayers;
		n = arruUID.length;
		n2 = 0;
		while (n2 < n) {
			id = arruUID[n2];
			op = Bukkit.getPlayer((UUID) id);
			Go.Scoreboard.configureSidebar(op, "Leaderboard", list, true);
			++n2;
		}
		CraftGo.Player.clearArrowsInBody(player);
		for (Player op2 : Bukkit.getOnlinePlayers()) {
			Go.Scoreboard.configureHealth(op2);
		}
	}

	public int leaveSkyfight(Player player, boolean noLog) {
		if (skyfight.contains(player.getUniqueId())) {
			UUID id;
			Player op;
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noLog = true;
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has quit " +  ChatColor.RED + "Skyfight"));
			}
			skyfight.remove(player.getUniqueId());
			player.getActivePotionEffects().clear();
			if (this.skyfightTag.containsKey(player.getUniqueId())) {
				this.skyfightTag.remove(player.getUniqueId());
			}
			if (this.skyfightScore.containsKey(player.getUniqueId())) {
				this.skyfightScore.remove(player.getUniqueId());
			}
			UUID[] skyfightPlayers = skyfight.toArray(new UUID[0]);
			Hashtable<String, Integer> list = new Hashtable<String, Integer>();
			UUID[] arruUID = skyfightPlayers;
			int n = arruUID.length;
			int n2 = 0;
			while (n2 < n) {
				id = arruUID[n2];
				op = Bukkit.getPlayer((UUID) id);
				list.put( ChatColor.GRAY + op.getName(), this.skyfightScore.get(op.getUniqueId()));
				++n2;
			}
			arruUID = skyfightPlayers;
			n = arruUID.length;
			n2 = 0;
			while (n2 < n) {
				id = arruUID[n2];
				op = Bukkit.getPlayer((UUID) id);
				Go.Scoreboard.configureSidebar(op, "Leaderboard", list, true);
				++n2;
			}
			CraftGo.Player.clearArrowsInBody(player);
			Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			Go.clearPlayer(player);
			player.setLevel(0);
			return 1;
		}
		return 0;
	}

	public void enterCreative(final Player player, boolean noRestore, boolean noLog) {
		if (!creative.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = Go.getCurrentMinigame(player);
			int changes = this.performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && minigame != ServerMinigame.HUB && minigame != ServerMinigame.UNKNOWN) {
				return;
			}
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				this.hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			creative.add(player.getUniqueId());
			Go.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "creative");
				if (!success) {
					player.teleport(Go.getZoneLocation("creative", (Plugin) this));
				}
			} else {
				player.teleport(Go.getZoneLocation("creative", (Plugin) this));
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has logged into " +  ChatColor.RED + "Creative"));
			}
			Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

				@Override
				public void run() {
					if (Server.creative.contains(player.getUniqueId())) {
						if (player.getGameMode() != GameMode.CREATIVE) {
							player.setGameMode(GameMode.CREATIVE);
						}
						player.setAllowFlight(true);
						player.addAttachment(Server.plugin, "plots.use", true);
						player.addAttachment(Server.plugin, "plots.permpack.basic", true);
						player.addAttachment(Server.plugin, "plots.plot.1", true);
						player.addAttachment(Server.plugin, "plots.visit.other", true);
						String[] arrstring = Directory.basicWorldEditPermissions;
						int n = arrstring.length;
						int n2 = 0;
						while (n2 < n) {
							String permission = arrstring[n2];
							player.addAttachment(Server.plugin, permission, true);
							++n2;
						}
					}
				}
			}, 100L);
		}
	}

	public int leaveCreative(Player player, boolean noSave, boolean noLog) {
		if (creative.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "creative");
				} catch (Exception exception) {
					// empty catch block
				}
			}
			if (!noLog) {
				Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has quit " +  ChatColor.RED + "Creative"));
			}
			creative.remove(player.getUniqueId());
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(true);
			player.addAttachment((Plugin) this, "plots.use", false);
			player.addAttachment((Plugin) this, "plots.permpack.basic", false);
			player.addAttachment((Plugin) this, "plots.plot.1", false);
			player.addAttachment((Plugin) this, "plots.visit.other", false);
			String[] arrstring = Directory.basicWorldEditPermissions;
			int n = arrstring.length;
			int n2 = 0;
			while (n2 < n) {
				String permission = arrstring[n2];
				player.addAttachment(plugin, permission, false);
				++n2;
			}
			Go.clearPlayer(player);
			return 1;
		}
		return 0;
	}

	@EventHandler
	public void onPlayerLogin(PlayerPreLoginEvent event) {
		String address = event.getAddress().getHostAddress();
		if (Bukkit.getOnlinePlayers().size() > 9 && Go.isPluginPirated()) {
			event.disallow(PlayerPreLoginEvent.Result.KICK_FULL,  ChatColor.RESET + "The capacity of this server has been hard limited to 10 players.");
		} else if (event.getResult() == PlayerPreLoginEvent.Result.KICK_FULL) {
			event.setResult(PlayerPreLoginEvent.Result.ALLOWED);
		}
		System.out.println("Checking connection (" + address + ") for banned signature... ");
		if (banConfig.getData().contains(address.replace(".", "x"))) {
			System.out.println("Complete banned signature found, disallowing connection...");
			String kickMessage = banConfig.getData().getString(address.replace(".", "x"));
			event.disallow(PlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes((char) '&', (String) kickMessage));
			Bukkit.broadcastMessage((String) (String.valueOf(Go.italicGray) + "Server: Disallow " + event.getName() + " '" + kickMessage + "'"));
		} else {
			System.out.println("No complete banned signature found, allowing connection...");
		}
	}

	@EventHandler
	public void onPlayerPreJoin(final PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		if (Go.isAuthenticated(player)) {
			player.setWalkSpeed(0.2f);
		}
		if (Go.isAuthenticated(player)) {
			player.setFlySpeed(0.1f);
		}
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getName().equals(player.getName())) continue;
			onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
		}
		hub.add(player.getUniqueId());
		if (player.isDead()) {
			player.spigot().respawn();
			Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

				@Override
				public void run() {
					Location hubLocation = Go.getZoneLocation("hub", Server.plugin);
					player.teleport(hubLocation);
				}
			}, 10L);
		} else {
			Location hubLocation = Go.getZoneLocation("hub", plugin);
			player.teleport(hubLocation);
		}
		Go.clearPlayer(player);
		this.fetchLobby(player);
		player.setAllowFlight(true);
		lastKnownHubWorld = Go.getZoneLocation("hub", plugin).getWorld().getName().toString();
		if (event.getJoinMessage() != null) {
			defaultJoinMessage = event.getJoinMessage().replace(event.getPlayer().getName(), "{player}");
		}
		player.performCommand("build-time");
		Go.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
		if (Go.isCustomJoinMessageEnabled(plugin)) {
			event.setJoinMessage( ChatColor.RED + player.getName() +  ChatColor.GRAY + " has joined the server.");
		}
		Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

			@Override
			public void run() {
				try {
					boolean dailyAuth = Server.this.getConfig().getBoolean("settings.enable.authme.dailyAuth");
					boolean autoLoginCmd = Server.this.getConfig().getBoolean("settings.enable.authme.autoLoginCmd");
					if (Bukkit.getPluginManager().getPlugin("AuthMe") == null) {
						Server.onPlayerJoin(event);
					} else {
						Object authObject = Go.getAuthenticationSuite();
						if (player.getName().equalsIgnoreCase("Player")) {
							if (((NewAPI) authObject).isRegistered(player.getName())) {
								((NewAPI) authObject).forceLogin(player);
							} else {
								((NewAPI) authObject).forceRegister(player, "password123", true);
							}
						} else {
							String ipAddress = player.getAddress().getAddress().getHostAddress().replace(".", "x");
							if (Server.this.getConfig().contains("lastLogin." + ipAddress + "." + player.getUniqueId().toString())) {
								String lastSessionDate = Server.this.getConfig().getString("lastLogin." + ipAddress + "." + player.getUniqueId().toString());
								Bukkit.broadcastMessage((String) (String.valueOf(Go.italicGray) + "Player " + player.getName() + " previously logged in on " + lastSessionDate));
								if (dailyAuth) {
									int lastYear = Integer.parseInt(lastSessionDate.split("\\.")[0]);
									int lastMonth = Integer.parseInt(lastSessionDate.split("\\.")[1]);
									int lastDay = Integer.parseInt(lastSessionDate.split("\\.")[2]);
									LocalDateTime now = LocalDateTime.now();
									int year = now.getYear();
									int month = now.getMonthValue();
									int day = now.getDayOfMonth();
									if (lastYear == year && lastMonth == month && lastDay == day && ((NewAPI) authObject).isRegistered(player.getName())) {
										((NewAPI) authObject).forceLogin(player);
									}
								}
							} else {
								Bukkit.broadcastMessage((String) (String.valueOf(Go.italicGray) + "Player " + player.getName() + " has yet to register for this server"));
							}
							if (!dailyAuth && autoLoginCmd) {
								String ip = player.getAddress().getAddress().getHostAddress().replace(".", "x");
								if (Server.this.getConfig().contains("autologin." + ip + "." + player.getUniqueId().toString())) {
									String password = Server.this.getConfig().getString("autologin." + ip + "." + player.getUniqueId().toString());
									boolean isCorrectPassword = false;
									if (authObject != null) {
										isCorrectPassword = ((NewAPI) authObject).checkPassword(player.getName(), password);
									}
									if (isCorrectPassword) {
										((NewAPI) authObject).forceLogin(player);
									} else {
										Server.this.getConfig().set("autologin." + ip + "." + player.getUniqueId().toString(),  null);
										Server.this.saveConfig();
									}
								}
							}
						}
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, 20L);
		for (Player op : Bukkit.getOnlinePlayers()) {
			Go.Scoreboard.configureHealth(op);
		}
	}

	public static void onPlayerJoin(PlayerJoinEvent event) {
		Set array;
		String valuePath;
		Player player = event.getPlayer();
		String displayName = player.getDisplayName();
		UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes());
		String path = "config." + player.getUniqueId().toString();
		String offlinePath = "config." + offlineUUID.toString();
		String onlineUUID = null;
		if (!Bukkit.getServer().getOnlineMode()) {
			onlineUUID = SolidStorage.getPremiumUUID(player.getName());
		}
		String onlinePath = null;
		if (onlineUUID != null) {
			onlinePath = "config." + onlineUUID.toString();
		}
		if (!path.equals(offlinePath) && plugin.getConfig().contains(offlinePath)) {
			array = plugin.getConfig().getConfigurationSection(offlinePath).getKeys(true);
			for (Object value : array) {
				valuePath = "config." + player.getUniqueId().toString() + "." + (String) value;
				String offlineValuePath = String.valueOf(offlinePath) + "." + (String) value;
				plugin.getConfig().set(valuePath, plugin.getConfig().get(offlineValuePath));
			}
			plugin.getConfig().set(offlinePath,  null);
			plugin.saveConfig();
		}
		if (path.equals(offlinePath) && onlinePath != null && plugin.getConfig().contains(onlinePath)) {
			array = plugin.getConfig().getConfigurationSection(onlinePath).getKeys(true);
			for (Object value : array) {
				valuePath = "config." + player.getUniqueId().toString() + "." + (String) value;
				String onlineValuePath = String.valueOf(onlinePath) + "." + (String) value;
				plugin.getConfig().set(valuePath, plugin.getConfig().get(onlineValuePath));
			}
			plugin.getConfig().set(onlinePath,  null);
			plugin.saveConfig();
		}
		for (String storageMinigame : Go.validStorageMinigames) {
			if (Bukkit.getServer().getOnlineMode()) {
				SolidStorage.dataOfflineModeToOnlineMode(player, storageMinigame);
				continue;
			}
			SolidStorage.dataOnlineModeToOfflineMode(player, storageMinigame, onlineUUID);
		}
		String ipAddress = player.getAddress().getAddress().getHostAddress().replace(".", "x");
		if (!plugin.getConfig().contains(path)) {
			plugin.getConfig().set(String.valueOf(path) + ".username",  displayName);
			plugin.getConfig().set(String.valueOf(path) + ".rank",  Go.validRanks.get(0));
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.kills",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.deaths",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.upgrades",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.preferredUpgrade",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.trails.smoke",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.trails.emerald",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.trails.redstone",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.trails.enchanting",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".kitpvp.trails.selectedTrail",  "-1");
			plugin.getConfig().set(String.valueOf(path) + ".balance.kitpvp",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".balance.prison",  "0");
			plugin.getConfig().set(String.valueOf(path) + ".balance.factions",  "250");
			plugin.getConfig().set("warning." + ipAddress,  "0");
			plugin.saveConfig();
		}
		plugin.getConfig().set(String.valueOf(path) + ".ip",  player.getAddress().getAddress().getHostAddress());
		plugin.getConfig().set("address." + ipAddress,  player.getUniqueId().toString());
		plugin.saveConfig();
		String rank = Go.getRank(player, plugin);
		if (Go.isPrefixedRankingEnabled(plugin)) {
			player.setDisplayName(String.valueOf(Go.toRankTag(rank)) + player.getName() +  ChatColor.RESET);
		}
		if (Go.isWelcomeMessageEnabled(plugin)) {
			player.sendMessage( ChatColor.GRAY + "/ Welcome to the server " +  ChatColor.RED + player.getName());
			player.sendMessage( ChatColor.GRAY + "/ Type " +  ChatColor.RED + "/help" +  ChatColor.GRAY + " for a list of authentic commands.");
			int connectedPlayers = plugin.getServer().getOnlinePlayers().size();
			player.sendMessage( ChatColor.GRAY + "/ Players online: " +  ChatColor.RED + connectedPlayers +  ChatColor.GRAY + " - Staff online: " +  ChatColor.RED + Go.getStaffOnline(plugin).length);
		}
		if (Go.isPluginPirated()) {
			player.sendMessage("This server is running a pirated copy of SkorrloreGaming.");
			player.sendMessage("Because of this, some of the major features have been disabled.");
		}
		if (moderatingPlayers.containsKey(player.getUniqueId())) {
			player.sendMessage( ChatColor.RED + "Notice. " +  ChatColor.GRAY + "You are currently still moderating the server.");
		}
		LocalDateTime now = LocalDateTime.now();
		int year = now.getYear();
		int month = now.getMonthValue();
		int day = now.getDayOfMonth();
		plugin.getConfig().set("lastLogin." + ipAddress + "." + player.getUniqueId().toString(),  (String.valueOf(year) + "." + month + "." + day));
		plugin.saveConfig();
		CraftGo.Player.setPlayerListHeaderFooter(player, "SkorrloreGaming", "skorrloregaming.enjin.com");
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (onlinePlayer.getName().equals(player.getName())) continue;
			onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
		}
		if (event.getQuitMessage() != null) {
			defaultQuitMessage = event.getQuitMessage().replace(event.getPlayer().getName(), "{player}");
		}
		if (Go.isCustomQuitMessageEnabled((Plugin) this)) {
			event.setQuitMessage( ChatColor.RED + player.getName() +  ChatColor.GRAY + " has quit the server.");
		}
		if (inCombat.containsKey(player.getUniqueId())) {
			player.setHealth(0.0);
			Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.RED + player.getName() +  ChatColor.GRAY + " has logged out during combat."));
			inCombat.remove(player.getUniqueId());
		}
		if (this.vanishedPlayers.containsKey(player.getUniqueId())) {
			VanishInfo iVanish = this.vanishedPlayers.get(player.getUniqueId());
			player.getInventory().setContents(iVanish.getContents());
			player.setGameMode(iVanish.getGameMode());
			this.vanishedPlayers.remove(player.getUniqueId());
			for (Player p : this.getServer().getOnlinePlayers()) {
				p.showPlayer(player);
			}
			if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		}
		if (this.semiOppedPlayers.contains(player.getUniqueId())) {
			player.setOp(false);
			this.semiOppedPlayers.remove(player.getUniqueId());
		}
		if (this.hubScoreboardTitleIndex.containsKey(player.getUniqueId())) {
			this.hubScoreboardTitleIndex.remove(player.getUniqueId());
		}
		this.performBuggedLeave(player, false, false);
		if (this.messageRequests.containsKey(player.getUniqueId())) {
			this.messageRequests.remove(player.getUniqueId());
		}
		for (Map.Entry id : this.messageRequests.entrySet()) {
			if (!((UUID) id.getValue()).equals(player.getUniqueId())) continue;
			this.messageRequests.remove(id.getKey());
		}
		if (player.isInsideVehicle()) {
			player.leaveVehicle();
		}
		if (Bukkit.getPluginManager().getPlugin("AuthMe") != null) {
			String ipAddress = player.getAddress().getAddress().getHostAddress().replace(".", "x");
			if (!this.getConfig().contains("lastLogin." + ipAddress + "." + player.getUniqueId().toString()) || !Go.isAuthenticated(player)) {
				Bukkit.broadcastMessage((String) (String.valueOf(Go.italicGray) + "Player " + player.getName() + " has left without registering for this server"));
			}
		}
	}

	@EventHandler
	public void onPlayerDismount(EntityDismountEvent event) {
		if (event.getDismounted().getType() == EntityType.ENDER_PEARL) {
			event.getDismounted().remove();
		}
	}

	public void openKitUpgradeInventory(Player player) {
		Inventory inventory = Bukkit.createInventory((InventoryHolder) null, (int) 18, (String) ( ChatColor.BOLD + "Select or upgrade your kit!"));
		int upgradeCount = Go.Kitpvp.getUpgradeCount(player, (Plugin) this);
		int requiredAmount = 150 * (Go.Kitpvp.getUpgradeCount(player, (Plugin) this) + 1);
		if (requiredAmount > 600) {
			requiredAmount = 600;
		}
		String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
		Material preferredWeapon = Material.getMaterial((int) Go.Kitpvp.getPreferredWeapon(player, (Plugin) this));
		ItemStack[] arritemStack = new ItemStack[2];
		arritemStack[0] = Go.Kitpvp.getUpgradeClassWeapon(player, Go.Kitpvp.getPreferredUpgrade(player, (Plugin) this), true, (Plugin) this);
		ItemStack[] weapons = arritemStack;
		if (weapons[0].getType() == Material.STONE_AXE) {
			weapons[1] = Go.createMaterial(Material.STONE_SWORD);
		} else if (weapons[0].getType() == Material.IRON_AXE) {
			weapons[1] = Go.createMaterial(Material.IRON_SWORD);
		}
		ItemMeta axeMeta = weapons[0].getItemMeta();
		axeMeta.setDisplayName(String.valueOf(prefix) + "Select preferred weapon #1");
		weapons[0].setItemMeta(axeMeta);
		ItemMeta swordMeta = weapons[1].getItemMeta();
		swordMeta.setDisplayName(String.valueOf(prefix) + "Select preferred weapon #2");
		weapons[1].setItemMeta(swordMeta);
		int weaponPreference = -1;
		int altWeaponPreference = -1;
		if (preferredWeapon == Material.STONE_AXE) {
			weaponPreference = 0;
			altWeaponPreference = 1;
		} else if (preferredWeapon == Material.STONE_SWORD) {
			weaponPreference = 1;
			altWeaponPreference = 0;
		}
		ArrayList<String> preferredWeaponLore = new ArrayList<String>();
		if (CraftGo.Player.getProtocolVersion(player) > 314) {
			weapons[weaponPreference].addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		} else {
			weapons[weaponPreference].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
		preferredWeaponLore.add( ChatColor.RESET + "This is already your preferred weapon.");
		if (preferredWeaponLore.size() > 0) {
			weapons[weaponPreference] = Go.addLore(weapons[weaponPreference], preferredWeaponLore.toArray(new String[0]));
		}
		for (Enchantment enchant : weapons[altWeaponPreference].getEnchantments().keySet()) {
			weapons[altWeaponPreference].removeEnchantment(enchant);
		}
		inventory.setItem(2, weapons[0]);
		inventory.setItem(4, Go.createMaterial(Material.REDSTONE, String.valueOf(prefix) + "Perform Upgrade (" +  ChatColor.RED + "$" + requiredAmount + prefix + ")"));
		inventory.setItem(6, weapons[1]);
		int passes = 0;
		int i = 10;
		while (i < 17) {
			ItemStack item;
			ArrayList<String> lore = new ArrayList<String>();
			if (upgradeCount >= ++passes - 1) {
				item = Go.createMaterial(Material.LEATHER_CHESTPLATE, String.valueOf(prefix) + "Select kit upgrade #" + passes);
				if (passes == Go.Kitpvp.getPreferredUpgrade(player, (Plugin) this) + 1) {
					if (CraftGo.Player.getProtocolVersion(player) > 314) {
						item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
					} else {
						item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
					lore.add( ChatColor.RESET + "This kit is already your preferred kit.");
				}
			} else {
				item = Go.createMaterial(Material.IRON_FENCE, String.valueOf(prefix) + "This kit upgrade is locked :(");
			}
			ItemStack[] arritemStack2 = Go.Kitpvp.getUpgradeClass(player, passes - 1, false, (Plugin) this);
			int n = arritemStack2.length;
			int n2 = 0;
			while (n2 < n) {
				ItemStack kitItem = arritemStack2[n2];
				lore.add( ChatColor.GREEN + Go.formatMaterial(kitItem.getType()) +  ChatColor.RED + " x" + kitItem.getAmount());
				++n2;
			}
			if (lore.size() > 0) {
				item = Go.addLore(item, lore.toArray(new String[0]));
			}
			inventory.setItem(i, item);
			i += 2;
		}
		player.openInventory(inventory);
	}

	public void openTrailManagementInventory(Player player) {
		Inventory inventory = Bukkit.createInventory((InventoryHolder) null, (int) 18, (String) ( ChatColor.BOLD + "Select or purchase trails!"));
		String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
		String path = "config." + player.getUniqueId().toString();
		if (!this.getConfig().contains(String.valueOf(path) + ".kitpvp.trails.smoke")) {
			this.getConfig().set(String.valueOf(path) + ".kitpvp.trails.smoke",  "0");
			this.saveConfig();
		}
		if (!this.getConfig().contains(String.valueOf(path) + ".kitpvp.trails.emerald")) {
			this.getConfig().set(String.valueOf(path) + ".kitpvp.trails.emerald",  "0");
			this.saveConfig();
		}
		if (!this.getConfig().contains(String.valueOf(path) + ".kitpvp.trails.redstone")) {
			this.getConfig().set(String.valueOf(path) + ".kitpvp.trails.redstone",  "0");
			this.saveConfig();
		}
		if (!this.getConfig().contains(String.valueOf(path) + ".kitpvp.trails.enchanting")) {
			this.getConfig().set(String.valueOf(path) + ".kitpvp.trails.enchanting",  "0");
			this.saveConfig();
		}
		int selectedTrail = -1;
		if (this.getConfig().contains(String.valueOf(path) + ".kitpvp.trails.selectedTrail")) {
			selectedTrail = Integer.parseInt(this.getConfig().getString(String.valueOf(path) + ".kitpvp.trails.selectedTrail"));
		} else {
			this.getConfig().set(String.valueOf(path) + ".kitpvp.trails.selectedTrail",  "-1");
			this.saveConfig();
		}
		HashMap<Integer, Integer> unlocked = new HashMap<Integer, Integer>();
		unlocked.put(0, Integer.parseInt(this.getConfig().getString(String.valueOf(path) + ".kitpvp.trails.smoke")));
		unlocked.put(1, Integer.parseInt(this.getConfig().getString(String.valueOf(path) + ".kitpvp.trails.emerald")));
		unlocked.put(2, Integer.parseInt(this.getConfig().getString(String.valueOf(path) + ".kitpvp.trails.redstone")));
		unlocked.put(3, Integer.parseInt(this.getConfig().getString(String.valueOf(path) + ".kitpvp.trails.enchanting")));
		ItemStack disableTrails = Go.createMaterial(Material.REDSTONE, String.valueOf(prefix) + "Disable Trails");
		if (selectedTrail == -1) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				disableTrails.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
			} else {
				disableTrails.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			}
			disableTrails = Go.addLore(disableTrails, new String[]{ ChatColor.RESET + "This is already your preferred trail."});
		}
		inventory.setItem(4, disableTrails);
		int i = 10;
		while (i < 17) {
			String trail = null;
			ChatColor trailItemColor = ChatColor.GOLD;
			if ((i - 10) / 2 == 0) {
				trail = "smoke";
				trailItemColor = ChatColor.GRAY;
			} else if ((i - 10) / 2 == 1) {
				trail = "emerald";
				trailItemColor = ChatColor.GREEN;
			} else if ((i - 10) / 2 == 2) {
				trail = "redstone";
				trailItemColor = ChatColor.RED;
			} else if ((i - 10) / 2 == 3) {
				trail = "enchanting";
				trailItemColor = ChatColor.LIGHT_PURPLE;
			}
			ItemStack trailItem = (Integer) unlocked.get((i - 10) / 2) == 1 ? Go.createMaterial(Material.LEATHER_BOOTS, String.valueOf(prefix) + "Select the " +  trailItemColor + WordUtils.capitalize((String) trail) + prefix + " trail") : Go.createMaterial(Material.IRON_FENCE, String.valueOf(prefix) + "This trail selection is locked :(");
			if (trailItem.getType() == Material.LEATHER_BOOTS) {
				if (i == 10) {
					trailItem = Go.addLeatherColor(trailItem, Color.GRAY);
				} else if (i == 12) {
					trailItem = Go.addLeatherColor(trailItem, Color.GREEN);
				} else if (i == 14) {
					trailItem = Go.addLeatherColor(trailItem, Color.RED);
				} else if (i == 16) {
					trailItem = Go.addLeatherColor(trailItem, Color.PURPLE);
				}
			}
			ArrayList<String> lore = new ArrayList<String>();
			if (selectedTrail == (i - 10) / 2) {
				if (CraftGo.Player.getProtocolVersion(player) > 314) {
					trailItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
				} else {
					trailItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				}
				lore.add( ChatColor.RESET + "This is already your preferred trail.");
				lore.add( ChatColor.GREEN + WordUtils.capitalize((String) trail) + " trail" +  ChatColor.RED + " x1");
			} else {
				lore.add( ChatColor.GREEN + WordUtils.capitalize((String) trail) + " trail" +  ChatColor.RED + " x1");
			}
			if (trailItem.getType() == Material.IRON_FENCE) {
				lore.add( ChatColor.GREEN + "Price to purchase " +  ChatColor.RED + "$" + ((i - 10) / 2 + 1) * 50);
			}
			ItemMeta meta = trailItem.getItemMeta();
			meta.setLore(lore);
			trailItem.setItemMeta(meta);
			inventory.setItem(i, trailItem);
			i += 2;
		}
		player.openInventory(inventory);
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		if (!Go.isAuthenticated(player)) {
			return;
		}
		if ((event.getRightClicked() instanceof ArmorStand)) {
			event.setCancelled(true);
			ArmorStand entity = (ArmorStand) event.getRightClicked();
			if (confirmUnregisterNpc.contains(player.getUniqueId())) {
				if (npcConfig.getData().contains("npc." + entity.getUniqueId().toString())) {
					npcConfig.getData().set("npc." + entity.getUniqueId().toString(), null);
					npcConfig.saveData();
					entity.remove();
					player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The custom npc has been removed.");
					event.setCancelled(true);
				}
			} else if ((npcConfig.getData().contains("npc." + entity.getUniqueId().toString())) &&
					(player.getGameMode() != GameMode.CREATIVE)) {
				String data = CustomNpc.getNpcData(entity);
				if (data != null) {
					event.setCancelled(true);
					switch (data) {
						case "KITPVP_TRAILS":
							player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
							openTrailManagementInventory(player);
							return;
						case "KITPVP_UPGRADE_KIT":
							player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
							openKitUpgradeInventory(player);
							return;
						default:
							break;
					}
					player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "A undefined inventory was assigned to this npc.");
					return;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		final Player player = event.getPlayer();
		ItemStack itm = player.getItemInHand();
		Location playerLoc = player.getLocation();
		if (!Go.isAuthenticated(player)) {
			return;
		}
		if (itm != null && itm.getType() == Material.MONSTER_EGG && itm.hasItemMeta() && !(event.getRightClicked() instanceof Player) && event.getRightClicked() instanceof LivingEntity && !simpleDelayedTask.contains(player.getUniqueId())) {
			event.getRightClicked().remove();
			simpleDelayedTask.add(player.getUniqueId());
			Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

				@Override
				public void run() {
					Server.simpleDelayedTask.remove(player.getUniqueId());
				}
			}, 20L);
			if (player.getGameMode() == GameMode.SURVIVAL) {
				if (itm.getAmount() < 2) {
					player.setItemInHand(Go.createMaterial(Material.AIR));
				} else {
					itm.setAmount(itm.getAmount() - 1);
					player.setItemInHand(itm);
				}
			}
			ItemStack mobEgg = new ItemStack(Material.MONSTER_EGG, 1, event.getRightClicked().getType().getTypeId());
			player.getInventory().addItem(new ItemStack[]{mobEgg});
			player.getWorld().playSound(playerLoc, Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(final PlayerInteractEvent event) {
		Inventory inv;
		Material type;
		Location blockLoc;
		Block block;
		Player player = event.getPlayer();
		if (Server.anticheat.antiafk.lackingMovementMinutes.containsKey(player.getUniqueId())) {
			Server.anticheat.antiafk.lackingMovementMinutes.remove(player.getUniqueId());
		}
		ItemStack itm = event.getItem();
		if (event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}
		if (!Go.isAuthenticated(player)) {
			return;
		}
		if ((event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
			NoteBlock noteBlock = (NoteBlock) event.getClickedBlock().getState();
			Note note = noteBlock.getNote();
			Note newNote = new Note((int) note.getId());
			if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				newNote = new Note(note.getId() + 1);
			}
			player.sendMessage("");
			player.sendMessage("Old Noteblock Id/Tone: " + note.getId() + "/" + note.getTone().toString());
			player.sendMessage( ChatColor.UNDERLINE + "New Noteblock Id/Tone: " + newNote.getId() + "/" + newNote.getTone().toString());
		}
		if (itm != null && itm.hasItemMeta()) {
			if (skyfight.contains(player.getUniqueId()) && itm.getType() == Material.LEATHER_CHESTPLATE && !simpleDelayedTask.contains(event.getPlayer().getUniqueId())) {
				simpleDelayedTask.add(event.getPlayer().getUniqueId());
				Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

					@Override
					public void run() {
						Server.simpleDelayedTask.remove(event.getPlayer().getUniqueId());
					}
				}, 20L);
				ItemStack helmet = Go.createMaterial(Material.LEATHER_HELMET, 1,  ChatColor.GOLD + "The Forbidding Helmet");
				ItemStack chestplate = Go.createMaterial(Material.LEATHER_CHESTPLATE,  ChatColor.GOLD + "The Forbidding Chestpeice");
				ItemStack leggings = Go.createMaterial(Material.LEATHER_LEGGINGS,  ChatColor.GOLD + "The Forbidding Leggings");
				ItemStack boots = Go.createMaterial(Material.LEATHER_BOOTS,  ChatColor.GOLD + "The Forbidding Boots");
				Color leatherColor = Go.getLeatherColor(itm);
				if (leatherColor.asRGB() == Color.BLUE.asRGB()) {
					leatherColor = Color.RED;
				} else if (leatherColor.asRGB() == Color.RED.asRGB()) {
					leatherColor = Color.GREEN;
				} else if (leatherColor.asRGB() == Color.GREEN.asRGB()) {
					leatherColor = Color.YELLOW;
				} else if (leatherColor.asRGB() == Color.YELLOW.asRGB()) {
					leatherColor = Color.BLUE;
				}
				helmet = Go.addLeatherColor(helmet, leatherColor);
				chestplate = Go.addLeatherColor(chestplate, leatherColor);
				leggings = Go.addLeatherColor(leggings, leatherColor);
				boots = Go.addLeatherColor(boots, leatherColor);
				player.getInventory().setHelmet(helmet);
				player.getInventory().setChestplate(chestplate);
				player.getInventory().setLeggings(leggings);
				player.getInventory().setBoots(boots);
				player.getInventory().setItem(8, chestplate);
				player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
				return;
			}
			if (hub.contains(player.getUniqueId()) && itm.getType() == Material.COMPASS && itm.getItemMeta().getDisplayName().equals( ChatColor.LIGHT_PURPLE + "Server Selector")) {
				ItemStack skyfight = Go.createMaterial(Material.BOW, 1,  ChatColor.LIGHT_PURPLE + "SkyFight");
				ItemStack kitpvp = Go.createMaterial(Material.IRON_SWORD, 1,  ChatColor.LIGHT_PURPLE + "KitPvP");
				ItemStack survivalFactions = Go.createMaterial(Material.STONE_PICKAXE, 1,  ChatColor.LIGHT_PURPLE + "Survival / Factions");
				ItemStack creative = Go.createMaterial(Material.GRASS, 1,  ChatColor.LIGHT_PURPLE + "Creative");
				kitpvp = CraftGo.ItemStack.removeAttributes(kitpvp);
				survivalFactions = CraftGo.ItemStack.removeAttributes(survivalFactions);
				skyfight = Go.addLore(skyfight, new String[]{ ChatColor.GOLD + "/server skyfight"});
				kitpvp = Go.addLore(kitpvp, new String[]{ ChatColor.GOLD + "/server kitpvp"});
				creative = Go.addLore(creative, new String[]{ ChatColor.GOLD + "/server creative"});
				inv = Bukkit.createInventory((InventoryHolder) null, (int) 9, (String) ( ChatColor.DARK_PURPLE + "Server Selector (0b1)"));
				if (Go.isMinigameEnabled(ServerMinigame.KITPVP, (Plugin) this)) {
					inv.setItem(1, kitpvp);
				}
				if (Go.isMinigameEnabled(ServerMinigame.FACTIONS, (Plugin) this) || Go.isMinigameEnabled(ServerMinigame.SURVIVAL, (Plugin) this)) {
					inv.setItem(3, survivalFactions);
				}
				if (Go.isMinigameEnabled(ServerMinigame.SKYFIGHT, (Plugin) this)) {
					inv.setItem(5, skyfight);
				}
				if (Go.isMinigameEnabled(ServerMinigame.CREATIVE, (Plugin) this)) {
					inv.setItem(7, creative);
				}
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
				player.openInventory(inv);
			}
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK && player.isSneaking() && ((block = event.getClickedBlock()).getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST)) {
			Sign sign = (Sign) block.getState();
			blockLoc = sign.getLocation();
			String code = String.valueOf(String.valueOf(blockLoc.getBlockX())) + ";" + String.valueOf(blockLoc.getBlockY()) + ";" + String.valueOf(blockLoc.getBlockZ());
			if (ChatColor.stripColor((String) sign.getLine(0)).equals("Sell")) {
				if (!signConfig.getData().contains("signs." + blockLoc.getWorld().getName() + code.replace(";", ""))) {
					player.sendMessage("This shop has not been activated by a qualified staff member.");
				} else {
					inv = Bukkit.createInventory((InventoryHolder) null, (int) 27, (String) ( ChatColor.BOLD + "Virtual Store [" + code + "]"));
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
					player.openInventory(inv);
				}
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (itm != null && itm.getType() == Material.FIREBALL && player.getGameMode() == GameMode.SURVIVAL && !inCombat.containsKey(player.getUniqueId()) && !simpleDelayedTask.contains(event.getPlayer().getUniqueId())) {
				simpleDelayedTask.add(event.getPlayer().getUniqueId());
				Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

					@Override
					public void run() {
						Server.simpleDelayedTask.remove(event.getPlayer().getUniqueId());
					}
				}, 20L);
				event.setCancelled(true);
				if (itm.getAmount() < 2) {
					player.setItemInHand(Go.createMaterial(Material.AIR));
				} else {
					itm.setAmount(itm.getAmount() - 1);
					player.setItemInHand(itm);
				}
				player.launchProjectile(SmallFireball.class);
			}
			if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST && creative.contains(player.getUniqueId())) {
				event.setCancelled(true);
			}
			if (itm != null && itm.getType() == Material.EYE_OF_ENDER && itm.hasItemMeta() && factions.contains(player.getUniqueId()) && itm.getItemMeta().getDisplayName().equals( ChatColor.LIGHT_PURPLE + "End Portal")) {
				World w = Bukkit.getWorld((String) (String.valueOf(Go.getMinigameDomain(player)) + "_end"));
				if (w == null) {
					return;
				}
				event.setCancelled(true);
				if (inCombat.containsKey(player.getUniqueId())) {
					player.sendMessage(String.valueOf(Go.getMinigameTag(player)) +  ChatColor.RED + "You cannot use this device during combat.");
					return;
				}
				Location exactLoc = new Location(w, 0.5, 0.0, 0.5);
				exactLoc.getChunk().load();
				exactLoc.setY((double) w.getHighestBlockYAt(exactLoc));
				if (exactLoc.getBlockY() == 0) {
					player.sendMessage("Aborted. Failed to find a safe spawn location.");
					return;
				}
				player.teleport(exactLoc, PlayerTeleportEvent.TeleportCause.END_PORTAL);
				w.playSound(exactLoc, Sound.BLOCK_PORTAL_TRAVEL, 1.0f, 1.0f);
				if (Go.isEffectsEnabled(player, (Plugin) this)) {
					int i = 0;
					while (i < 360) {
						Location flameloc = exactLoc;
						double cos = Math.cos(i) * 2.0;
						double sin = Math.sin(i) * 2.0;
						double x = flameloc.getZ() - cos - cos + cos;
						double z = flameloc.getX() - sin - sin + sin;
						player.spawnParticle(Particle.PORTAL, x, flameloc.getY() - 1.0, z, 5);
						i += 2;
					}
				}
				return;
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			type = event.getClickedBlock().getType();
			if (event.getItem() != null) {
				if (itm.getType() == Material.MONSTER_EGG) {
					if (creative.contains(player.getUniqueId())) {
						event.setCancelled(true);
					} else {
						if (type != Material.MOB_SPAWNER && !simpleDelayedTask.contains(event.getPlayer().getUniqueId())) {
							simpleDelayedTask.add(event.getPlayer().getUniqueId());
							Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

								@Override
								public void run() {
									Server.simpleDelayedTask.remove(event.getPlayer().getUniqueId());
								}
							}, 20L);
							String id = CraftGo.ItemStack.getTag(itm).getCompound("EntityTag").getString("id");
							id = id.replace("minecraft:", "");
							if (id == "") {
								event.setCancelled(true);
								return;
							}
							if (player.getGameMode() == GameMode.SURVIVAL) {
								if (itm.getAmount() < 2) {
									player.setItemInHand(Go.createMaterial(Material.AIR));
								} else {
									itm.setAmount(itm.getAmount() - 1);
									player.setItemInHand(itm);
								}
							}
							EntityType ty = EntityType.fromName((String) id);
							Location exactLoc = event.getClickedBlock().getLocation();
							exactLoc.setX((double) exactLoc.getBlockX() + 0.5);
							exactLoc.setY((double) exactLoc.getBlockY() + 1.0);
							exactLoc.setZ((double) exactLoc.getBlockZ() + 0.5);
							event.getClickedBlock().getWorld().spawnEntity(exactLoc, ty);
						}
						event.setCancelled(true);
					}
				}
				if (player.getWorld().getEnvironment() == World.Environment.NETHER && event.getItem().getType() == Material.WATER_BUCKET) {
					event.setCancelled(true);
					event.getClickedBlock().getRelative(event.getBlockFace()).setType(Material.WATER);
					if (player.getGameMode() == GameMode.SURVIVAL) {
						event.getItem().setType(Material.BUCKET);
					}
				}
			}
		}
		if (event.getAction() == Action.LEFT_CLICK_AIR && itm != null && itm.getType() == Material.EYE_OF_ENDER && itm.hasItemMeta() && factions.contains(player.getUniqueId()) && itm.getItemMeta().getDisplayName().equals( ChatColor.LIGHT_PURPLE + "End Portal") && !inCombat.containsKey(player.getUniqueId()) && !simpleDelayedTask.contains(event.getPlayer().getUniqueId())) {
			simpleDelayedTask.add(event.getPlayer().getUniqueId());
			Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

				@Override
				public void run() {
					Server.simpleDelayedTask.remove(event.getPlayer().getUniqueId());
				}
			}, 20L);
			if (player.isInsideVehicle() && player.getVehicle() instanceof EnderPearl) {
				player.getVehicle().remove();
			}
			((EnderPearl) player.launchProjectile(EnderPearl.class)).setPassenger((Entity) player);
			int foodLevel = player.getFoodLevel();
			if (--foodLevel < 0) {
				foodLevel = 0;
			}
			player.setFoodLevel(foodLevel);
		}
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
			return;
		}
		if (itm != null && itm.getType() != Material.AIR) {
			if (itm.getType() == Material.SULPHUR && factions.contains(player.getUniqueId())) {
				if (itm.hasItemMeta() && itm.getItemMeta().getDisplayName().equals( ChatColor.LIGHT_PURPLE + "Explosive Gunpowder")) {
					if (player.getGameMode() == GameMode.SURVIVAL && !inCombat.containsKey(player.getUniqueId()) && !simpleDelayedTask.contains(event.getPlayer().getUniqueId())) {
						simpleDelayedTask.add(event.getPlayer().getUniqueId());
						Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

							@Override
							public void run() {
								Server.simpleDelayedTask.remove(event.getPlayer().getUniqueId());
							}
						}, 20L);
						if (itm.getAmount() < 2) {
							player.setItemInHand(Go.createMaterial(Material.AIR));
						} else {
							itm.setAmount(itm.getAmount() - 1);
							player.setItemInHand(itm);
						}
						player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
						Explosion explosion = new Explosion(player.getLocation(), 0.4f, false);
						explosion.explodeNaturally();
						anticheat.handleVelocity(player, player.getLocation().getDirection().multiply(5.0));
					}
				} else if (player.getGameMode() == GameMode.SURVIVAL && !inCombat.containsKey(player.getUniqueId())) {
					if (itm.getAmount() < 2) {
						player.setItemInHand(Go.createMaterial(Material.AIR));
					} else {
						itm.setAmount(itm.getAmount() - 1);
						player.setItemInHand(itm);
					}
					player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
					anticheat.handleVelocity(player, player.getLocation().getDirection().multiply(2.5));
				}
			}
			if (itm.getType() == Material.BOW && kitpvp.contains(player.getUniqueId()) && !player.getInventory().contains(Material.ARROW)) {
				if (this.storedItem.containsKey( player)) {
					return;
				}
				ItemStack item = player.getInventory().getItem(9);
				this.storedItem.put(player, item);
				player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
			}
			if (itm.hasItemMeta() && itm.getItemMeta().hasLore()) {
				String[] lore = itm.getItemMeta().getLore().toArray(new String[0]);
				if (lore[0].equals("Using this wand, you will gain defined potion effects.")) {
					if (itm.getAmount() <= 1) {
						player.setItemInHand(Go.createMaterial(Material.AIR));
					} else {
						ItemStack old = new ItemStack(itm.getType(), itm.getAmount() - 1);
						player.setItemInHand(old);
					}
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2));
					player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 0));
					player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3600, 0));
					player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);
				} else if (lore[0].equals("Activation Wand") && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					Block block2 = event.getClickedBlock();
					blockLoc = block2.getLocation();
					Material blockType = block2.getType();
					if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
						Sign sign = (Sign) block2.getState();
						String[] s = sign.getLines();
						int i = 0;
						while (i < s.length) {
							s[i] = ChatColor.stripColor((String) s[i]);
							++i;
						}
						if (s[0].equals("Buy") || s[0].equals("Sell") || s[0].equals("Enchant") || s[0].equals("Repair") || s[1].equals("Kit")) {
							if (!player.hasPermission("skorrloregaming.shop.process")) {
								player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.RED + "You do not have permission to activate this shop.");
								Go.playLackPermissionMessage((CommandSender) player);
								event.setCancelled(true);
								return;
							}
							String code = String.valueOf(blockLoc.getWorld().getName()) + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
							signConfig.getData().set("signs." + code,  1);
							signConfig.saveData();
							player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Successfully processed shop " +  ChatColor.RED + code +  ChatColor.GRAY + ".");
							player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Type: " +  ChatColor.RED + s[0] + " Shop");
							player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "ID: " +  ChatColor.RED + s[1]);
							player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Price: " +  ChatColor.RED + "$" + s[2]);
							player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + ": " +  ChatColor.RED + s[3]);
							if (s[1].equals("Kit")) {
								sign.setLine(1,  ChatColor.DARK_BLUE + s[1]);
								sign.update();
							} else {
								sign.setLine(0,  ChatColor.DARK_BLUE + s[0]);
								sign.update();
							}
							return;
						}
					}
				}
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && ((type = event.getClickedBlock().getType()) == Material.SIGN_POST || type == Material.WALL_SIGN)) {
			String subDomain = Go.getMinigameDomain(player);
			String tag = Go.getMinigameTag(subDomain);
			Sign sign = (Sign) event.getClickedBlock().getState();
			if (this.signEditParam.containsKey( player)) {
				SignInfo info = this.signEditParam.get( player);
				int rank = Go.getRankId(player, (Plugin) this);
				if (player.hasPermission("skorrloregaming.events.chatcolor") || rank > -1 || rank < -2) {
					info.setText(ChatColor.translateAlternateColorCodes((char) '&', (String) info.getText()));
				}
				sign.setLine(info.getLine(), info.getText());
				sign.update();
				this.signEditParam.remove( player);
				player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Success. " +  ChatColor.GRAY + "Global sign information updated.");
				return;
			}
			SignShop.handle(sign, player, subDomain, (Plugin) this);
			this.saveConfig();
		}
	}

	public void openSurvivalServerSelectorMenu(Player player) {
		ItemStack factions = Go.createMaterial(Material.DIAMOND_SWORD, 1,  ChatColor.LIGHT_PURPLE + "Factions");
		ItemStack survival = Go.createMaterial(Material.STONE_PICKAXE, 1,  ChatColor.LIGHT_PURPLE + "Survival");
		survival = CraftGo.ItemStack.removeAttributes(survival);
		factions = CraftGo.ItemStack.removeAttributes(factions);
		survival = Go.addLore(survival, new String[]{ ChatColor.GOLD + "/server survival"});
		factions = Go.addLore(factions, new String[]{ ChatColor.GOLD + "/server factions"});
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 9, (String) ( ChatColor.DARK_PURPLE + "Server Selector (0b10)"));
		if (Go.isMinigameEnabled(ServerMinigame.FACTIONS, (Plugin) this)) {
			inv.setItem(3, factions);
		}
		if (Go.isMinigameEnabled(ServerMinigame.SURVIVAL, (Plugin) this)) {
			inv.setItem(5, survival);
		}
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 1.0f);
		player.openInventory(inv);
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		ServerMinigame minigame = Go.getCurrentMinigame(event.getPlayer());
		if (minigame == ServerMinigame.SKYFIGHT || minigame == ServerMinigame.HUB) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		final Player player = event.getEntity();
		CraftGo.Player.clearArrowsInBody(player);
		String subDomain = Go.getMinigameDomain(player);
		String tag = Go.getMinigameTag(subDomain);
		if (inCombat.containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(((ExtremeDouble) inCombat.get(player.getUniqueId())).getArg0());
			inCombat.remove(player.getUniqueId());
		}
		String format = event.getDeathMessage().replace(player.getName(), ChatColor.RED + player.getName() + ChatColor.GRAY);
		boolean playerKiller = true;
		Player k = null;
		if ((event.getEntity().getKiller() instanceof Arrow)) {
			ProjectileSource source = ((Arrow) event.getEntity().getKiller()).getShooter();
			if ((source instanceof Player)) {
				playerKiller = true;
				k = (Player) source;
			} else {
				playerKiller = false;
			}
		} else if ((event.getEntity().getKiller() instanceof Player)) {
			playerKiller = true;
			k = event.getEntity().getKiller();
		} else {
			playerKiller = false;
		}
		String processedName = "unspecified";
		String entityName = "unspecified";
		EntityType[] arrayOfEntityType;
		int j = (arrayOfEntityType = EntityType.values()).length;
		for (int i = 0; i < j; i++) {
			EntityType ty = arrayOfEntityType[i];
			entityName = Go.capitalizeAll(ty.toString().toLowerCase(), "_");
			if (format.contains(entityName)) {
				processedName = entityName;
				break;
			}
		}
		if (playerKiller) {
			processedName = ChatColor.RED + k.getName() + ChatColor.GRAY;
			format = format.replace(k.getName(), processedName);
		} else {
			processedName = ChatColor.RED + processedName + ChatColor.GRAY;
			format = format.replace(entityName, processedName);
		}
		format = format.replace("died", "was killed by magic");
		event.setDeathMessage(tag + ChatColor.GRAY + format);
		if (playerKiller) {
			player.setVelocity(new Vector(0, 0, 0));
			Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
				public void run() {
					player.spigot().respawn();
				}
			}, 10L);
		}
		try {
			if (skyfight.contains(player.getUniqueId())) {
				event.setDeathMessage("");
				event.getDrops().clear();
				return;
			}
			if ((subDomain.equals("factions")) || (subDomain.equals("kitpvp")) || (subDomain.equals("prison"))) {
				if (subDomain.equals("prison")) {
					event.getDrops().clear();
				}
				double baseHealth = Go.roundDouble(k.getHealth() / 2.0D, 1);
				event.setDeathMessage(tag + ChatColor.RED + player.getName() + ChatColor.GRAY + " has been killed by " + ChatColor.RED + k.getName() + ChatColor.GRAY + " [" + ChatColor.RED + baseHealth + ChatColor.DARK_RED + " ?" + ChatColor.GRAY + "]");
				int supplyCash = 10;
				if (subDomain.equals("kitpvp")) {
					int currentPlayerKills = Go.Kitpvp.getPlayerKills(k, this);
					Go.Kitpvp.setPlayerKills(k, currentPlayerKills + 1, this);
					int currentPlayerDeaths = Go.Kitpvp.getPlayerDeaths(player, this);
					Go.Kitpvp.setPlayerDeaths(player, currentPlayerDeaths + 1, this);
					int dpk = currentPlayerKills / 50;
					if (6 + dpk > 15) {
						dpk = 9;
					}
					supplyCash = 6 + dpk;
				}
				EconManager.depositCash(k, supplyCash, subDomain, this);
				saveConfig();
				k.sendMessage(tag + ChatColor.GRAY + "You have been given " + ChatColor.RED + "$" + supplyCash + ChatColor.GRAY + " for killing " + ChatColor.RED + player.getName());
				if (subDomain.equals("kitpvp")) {
					Go.Kitpvp.refreshScoreboard(player, this);
					Go.Kitpvp.refreshScoreboard(k, this);
				}
				this.firework.playFirework(player.getWorld(), player.getLocation(), this.firework.getEffect());
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		player.setFlying(false);
		event.setCancelled(true);
		if (hub.contains(player.getUniqueId()) || skyfight.contains(player.getUniqueId())) {
			if (!this.canDoubleJump.contains(player.getUniqueId())) {
				return;
			}
			this.canDoubleJump.remove(player.getUniqueId());
			player.setAllowFlight(false);
			anticheat.handleVelocity(player, player.getLocation().getDirection().multiply(2.5));
			anticheat.handleVelocity(player, new Vector(player.getVelocity().getX(), 1.1, player.getVelocity().getZ()), true);
			player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1.0f, 1.0f);
		}
	}

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location blockLoc = block.getLocation();
		Material blockType = block.getType();
		if (blockType == Material.WALL_SIGN || blockType == Material.SIGN_POST) {
			String[] s = event.getLines();
			int i = 0;
			while (i < s.length) {
				s[i] = ChatColor.stripColor((String) s[i]);
				++i;
			}
			if (s[0].equals("Buy") || s[0].equals("Sell") || s[0].equals("Enchant") || s[0].equals("Repair") || s[1].equals("Kit")) {
				if (!player.hasPermission("skorrloregaming.shop.process")) {
					player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.RED + "You do not have permission to activate this shop.");
					Go.playLackPermissionMessage((CommandSender) player);
					return;
				}
				String code = String.valueOf(blockLoc.getWorld().getName()) + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
				signConfig.getData().set("signs." + code,  1);
				signConfig.saveData();
				player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Successfully processed shop " +  ChatColor.RED + code +  ChatColor.GRAY + ".");
				player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Type: " +  ChatColor.RED + s[0] + " Shop");
				player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "ID: " +  ChatColor.RED + s[1]);
				player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Price: " + "$" +  ChatColor.RED + s[2]);
				player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + ": " +  ChatColor.RED + s[3]);
				if (s[1].equals("Kit")) {
					event.setLine(1,  ChatColor.DARK_BLUE + s[1]);
				} else {
					event.setLine(0,  ChatColor.DARK_BLUE + s[0]);
				}
				return;
			}
			int rank = Go.getRankId(player, (Plugin) this);
			if (player.hasPermission("skorrloregaming.events.chatcolor") || rank > -1 || rank < -2) {
				int i2 = 0;
				while (i2 < s.length) {
					event.setLine(i2, ChatColor.translateAlternateColorCodes((char) '&', (String) s[i2]));
					++i2;
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		if (!event.isCancelled()) {
			event.setCancelled(anticheat.onBlockPlace(event.getBlock(), (Entity) player));
		}
		if (event.getBlock().getType() != Material.MOB_SPAWNER) {
			return;
		}
		short entityID = CraftGo.MobSpawner.getStoredSpawnerItemEntityID(event.getItemInHand());
		CraftGo.MobSpawner.setSpawnerEntityID(event.getBlock(), entityID);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!event.isCancelled()) {
			event.setCancelled(anticheat.onBlockBreak(event.getBlock(), (Entity) player));
		}
		Block block = event.getBlock();
		Location blockLoc = block.getLocation();
		Material blockType = block.getType();
		if (Go.isWithinUnclaimedLand(blockLoc, player)) {
			if (blockType == Material.TNT && player.getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
				block.setType(Material.AIR);
				block.getState().update();
				player.getWorld().spawn(block.getLocation(), TNTPrimed.class);
			}
			if (player.getWorld().getEnvironment() == World.Environment.NETHER && blockType == Material.ICE) {
				event.setCancelled(true);
				block.setType(Material.WATER);
			}
			if (blockType == Material.MOB_SPAWNER && player.getGameMode() != GameMode.CREATIVE && player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && (player.getItemInHand().getType() == Material.IRON_PICKAXE || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
				short entityID = CraftGo.MobSpawner.getSpawnerEntityID(block);
				ItemStack stack = CraftGo.MobSpawner.newSpawnerItem(entityID, 1);
				event.setCancelled(true);
				block.setType(Material.AIR);
				block.getState().update();
				Location exactLoc = blockLoc;
				exactLoc.setX((double) exactLoc.getBlockX() + 0.5);
				exactLoc.setY((double) exactLoc.getBlockY() + 0.5);
				exactLoc.setZ((double) exactLoc.getBlockZ() + 0.5);
				player.getWorld().dropItem(exactLoc, stack);
			}
			if (blockType != null && player.getGameMode() != GameMode.CREATIVE && (player.getItemInHand().getType() == Material.IRON_PICKAXE || player.getItemInHand().getType() == Material.DIAMOND_PICKAXE) && player.getItemInHand().hasItemMeta() && player.getItemInHand().getItemMeta().hasEnchant(Enchantment.FIRE_ASPECT) && !player.getItemInHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
				Map enchants = player.getItemInHand().getEnchantments();
				int fireAspectLevel = (Integer) enchants.get( Enchantment.FIRE_ASPECT);
				boolean win = new Random().nextBoolean();
				if (fireAspectLevel == 2) {
					win = true;
				}
				if (win) {
					int fortuneLevel;
					ItemStack dropItem = null;
					int dropItemAmount = 1;
					if (player.getItemInHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && (dropItemAmount = new Random().nextInt((fortuneLevel = ((Integer) enchants.get( Enchantment.LOOT_BONUS_BLOCKS)).intValue()) + 1)) == 0) {
						dropItemAmount = 1;
					}
					if (block.getType() == Material.IRON_ORE) {
						dropItem = Go.createMaterial(Material.IRON_INGOT, dropItemAmount);
					}
					if (block.getType() == Material.GOLD_ORE) {
						dropItem = Go.createMaterial(Material.GOLD_INGOT, dropItemAmount);
					}
					if (block.getType() == Material.GLASS) {
						dropItem = Go.createMaterial(Material.GLASS, 1, block.getData());
					}
					if (block.getType() == Material.STAINED_GLASS) {
						dropItem = Go.createMaterial(Material.STAINED_GLASS, 1, block.getData());
					}
					if (dropItem != null) {
						event.setCancelled(true);
						block.setType(Material.AIR);
						block.getState().update();
						Location exactLoc = blockLoc;
						exactLoc.setX((double) exactLoc.getBlockX() + 0.5);
						exactLoc.setY((double) exactLoc.getBlockY() + 0.5);
						exactLoc.setZ((double) exactLoc.getBlockZ() + 0.5);
						player.getWorld().dropItem(exactLoc, dropItem);
					}
				}
			}
		}
		if (blockType == Material.WALL_SIGN | blockType == Material.SIGN_POST) {
			Sign sign = (Sign) event.getBlock().getState();
			String[] s = sign.getLines();
			int i = 0;
			while (i < s.length) {
				s[i] = ChatColor.stripColor((String) s[i]);
				++i;
			}
			if (s[0].equals("Buy") || s[0].equals("Sell") || s[0].equals("Enchant") || s[0].equals("Repair") || s[1].equals("Kit")) {
				String code = String.valueOf(blockLoc.getWorld().getName()) + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
				if (signConfig.getData().contains("signs." + code)) {
					if (!event.getPlayer().hasPermission("skorrloregaming.shop.process")) {
						player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.RED + "You do not have permission to deactivate this shop.");
						Go.playLackPermissionMessage((CommandSender) player);
						event.setCancelled(true);
						return;
					}
					signConfig.getData().set("signs." + code,  null);
					signConfig.saveData();
					if (s[1].equals("Kit")) {
						sign.setLine(1, s[1]);
						sign.update();
					} else {
						sign.setLine(0, s[0]);
						sign.update();
					}
					player.sendMessage(String.valueOf(Go.Legacy.tag) +  ChatColor.GRAY + "Successfully cleared shop " +  ChatColor.RED + code +  ChatColor.GRAY + ".");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void dropperDispenseEvent(BlockDispenseEvent event) {
		if (event.getBlock().getType() == Material.DROPPER && Go.getMinigameFromWorld(event.getBlock().getWorld(), (Plugin) this) == ServerMinigame.FACTIONS) {
			Block block;
			Dropper dropper = (Dropper) event.getBlock().getState();
			BlockFace face = ((Directional) dropper.getData()).getFacing();
			if (face == BlockFace.UP) {
				Block block2 = event.getBlock().getLocation().clone().add(0.0, 1.0, 0.0).getBlock();
				if (block2.getType() == Material.COBBLESTONE) {
					block2.breakNaturally();
					event.setCancelled(true);
				}
			} else if (face == BlockFace.DOWN && (block = event.getBlock().getLocation().clone().add(0.0, -1.0, 0.0).getBlock()).getType() == Material.COBBLESTONE) {
				block.breakNaturally();
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void furnaceSmeltEvent(FurnaceSmeltEvent event) {
		Furnace furnace = (Furnace) event.getBlock().getState();
		if (Go.getMinigameFromWorld(furnace.getWorld(), (Plugin) this) == ServerMinigame.FACTIONS) {
			int reactorType = 0;
			DesignPattern pattern = Go.getPattern(furnace.getLocation().clone().subtract(0.0, 1.0, 0.0));
			if (pattern.isCompletePattern() && pattern.getMiddleCenter() == Material.HOPPER) {
				if (pattern.getInnerCircle() == Material.GOLD_BLOCK && pattern.getOuterBlocks() == Material.IRON_BLOCK) {
					reactorType = 1;
				} else if (pattern.getInnerCircle() == Material.DIAMOND_BLOCK && pattern.getOuterBlocks() == Material.IRON_BLOCK) {
					reactorType = 2;
				} else if (pattern.getInnerCircle() == Material.EMERALD_BLOCK && pattern.getOuterBlocks() == Material.IRON_BLOCK) {
					reactorType = 3;
				} else if (pattern.getInnerCircle() == Material.GOLD_BLOCK && pattern.getOuterBlocks() == Material.GOLD_BLOCK) {
					reactorType = 4;
				} else if (pattern.getInnerCircle() == Material.DIAMOND_BLOCK && pattern.getOuterBlocks() == Material.GOLD_BLOCK) {
					reactorType = 5;
				} else if (pattern.getInnerCircle() == Material.EMERALD_BLOCK && pattern.getOuterBlocks() == Material.GOLD_BLOCK) {
					reactorType = 6;
				} else if (pattern.getInnerCircle() == Material.GOLD_BLOCK && pattern.getOuterBlocks() == Material.DIAMOND_BLOCK) {
					reactorType = 7;
				} else if (pattern.getInnerCircle() == Material.DIAMOND_BLOCK && pattern.getOuterBlocks() == Material.DIAMOND_BLOCK) {
					reactorType = 8;
				} else if (pattern.getInnerCircle() == Material.EMERALD_BLOCK && pattern.getOuterBlocks() == Material.DIAMOND_BLOCK) {
					reactorType = 9;
				} else if (pattern.getInnerCircle() == Material.GOLD_BLOCK && pattern.getOuterBlocks() == Material.EMERALD_BLOCK) {
					reactorType = 10;
				} else if (pattern.getInnerCircle() == Material.DIAMOND_BLOCK && pattern.getOuterBlocks() == Material.EMERALD_BLOCK) {
					reactorType = 11;
				} else if (pattern.getInnerCircle() == Material.EMERALD_BLOCK && pattern.getOuterBlocks() == Material.EMERALD_BLOCK) {
					reactorType = 12;
				}
				if (reactorType > 0) {
					furnace.setCookTime((short) 195);
					int negateBurnTime = 200;
					if (reactorType > 3 && reactorType < 7) {
						negateBurnTime = 150;
					}
					if (reactorType > 6 && reactorType < 10) {
						negateBurnTime = 100;
					}
					if (reactorType > 9 && reactorType < 13) {
						negateBurnTime = 50;
					}
					furnace.setBurnTime((short) (furnace.getBurnTime() - negateBurnTime));
					if (event.getSource().getType() == Material.COBBLESTONE) {
						furnace.getLocation().getWorld().playSound(furnace.getLocation(), Sound.BLOCK_LAVA_POP, 1.0f, 1.0f);
						Random ran = new Random();
						ItemStack result = furnace.getInventory().getResult();
						int givenSuccessChance = 100000;
						Material successItem = Material.IRON_INGOT;
						if (reactorType < 4 && reactorType > 0) {
							givenSuccessChance = 10;
							if (reactorType == 2) {
								givenSuccessChance = 6;
							}
							if (reactorType == 3) {
								givenSuccessChance = 4;
							}
							successItem = Material.IRON_INGOT;
						} else if (reactorType < 7 && reactorType > 3) {
							givenSuccessChance = 30;
							if (reactorType == 5) {
								givenSuccessChance = 18;
							}
							if (reactorType == 6) {
								givenSuccessChance = 12;
							}
							successItem = Material.GOLD_INGOT;
						} else if (reactorType < 10 && reactorType > 6) {
							givenSuccessChance = 100;
							if (reactorType == 8) {
								givenSuccessChance = 60;
							}
							if (reactorType == 9) {
								givenSuccessChance = 40;
							}
							successItem = Material.DIAMOND;
						} else if (reactorType < 13 && reactorType > 9) {
							givenSuccessChance = 200;
							if (reactorType == 11) {
								givenSuccessChance = 120;
							}
							if (reactorType == 12) {
								givenSuccessChance = 80;
							}
							successItem = Material.EMERALD;
						}
						if (ran.nextInt(givenSuccessChance) == 0) {
							if (result == null || result.getType() == Material.AIR) {
								result = Go.createMaterial(successItem);
								event.setResult(result);
								furnace.getLocation().getWorld().playSound(furnace.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
							} else if (result.getType() == successItem) {
								if (result.getAmount() < 64) {
									result.setAmount(result.getAmount() + 1);
								}
								event.setResult(result);
								furnace.getLocation().getWorld().playSound(furnace.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1.0f, 1.0f);
							} else {
								event.setResult(result);
							}
						} else {
							event.setResult(result);
						}
					}
					return;
				}
			}
			if (furnace.getLocation().clone().subtract(0.0, 1.0, 0.0).getBlock().getType() == Material.MAGMA) {
				furnace.setCookTime((short) 195);
				furnace.setBurnTime((short) (furnace.getBurnTime() - 195));
			} else {
				furnace.setCookTime((short) 150);
				furnace.setBurnTime((short) (furnace.getBurnTime() - 150));
			}
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		if (factions.contains(player.getUniqueId()) && event.getInventory().getType() == InventoryType.ENCHANTING) {
			EnchantingInventory inventory = (EnchantingInventory) event.getInventory();
			Dye dye = new Dye();
			dye.setColor(DyeColor.BLUE);
			ItemStack item = dye.toItemStack();
			item.setAmount(32);
			inventory.setItem(1, item);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String path = "config." + player.getUniqueId().toString();
		if ((event.getCurrentItem() != null) && (event.getInventory().getName().equals(ChatColor.BOLD + "Select or purchase trails!"))) {
			if (Go.getCurrentMinigame(player) == ServerMinigame.KITPVP) {
				if ((event.getCurrentItem().hasItemMeta()) && (event.getCurrentItem().getItemMeta().hasDisplayName())) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					if (item.getType() == Material.REDSTONE) {
						getConfig().set(path + ".kitpvp.trails.selectedTrail", "-1");
						saveConfig();
						player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
						player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred trail has been changed.");
						openTrailManagementInventory(player);
					} else if ((item.getType() == Material.LEATHER_BOOTS) || (item.getType() == Material.IRON_FENCE)) {
						int currentBalance = EconManager.retrieveCash(player, Go.getMinigameDomain(player), this);
						int trailType = -1;
						String trail = null;
						int lineNumber1 = -1;
						int lineNumber2 = -1;
						for (int i = 0; i < meta.getLore().size(); i++) {
							String line = (String) meta.getLore().get(i);
							if (line.contains("trail")) {
								lineNumber1 = i;
							}
							if (line.contains("purchase")) {
								lineNumber2 = i;
							}
						}
						if (String.valueOf(meta.getLore().get(lineNumber1)).contains("Smoke")) {
							trailType = 0;
							trail = "smoke";
						} else if (String.valueOf(meta.getLore().get(lineNumber1)).contains("Emerald")) {
							trailType = 1;
							trail = "emerald";
						} else if (String.valueOf(meta.getLore().get(lineNumber1)).contains("Redstone")) {
							trailType = 2;
							trail = "redstone";
						} else if (String.valueOf(meta.getLore().get(lineNumber1)).contains("Enchanting")) {
							trailType = 3;
							trail = "enchanting";
						}
						int unlocked = 0;
						if (trail == null) {
							return;
						}
						if (getConfig().contains(path + ".kitpvp.trails." + trail)) {
							unlocked = Integer.parseInt(getConfig().getString(path + ".kitpvp.trails." + trail));
						} else {
							getConfig().set(path + ".kitpvp.trails." + trail, "0");
							saveConfig();
						}
						boolean upgradedTo = false;
						if (unlocked == 0) {
							String requiredBalanceStr = String.valueOf(meta.getLore().get(lineNumber2));
							int requiredBalance = Integer.valueOf(requiredBalanceStr.substring(requiredBalanceStr.indexOf("$") + 1)).intValue();
							if (currentBalance >= requiredBalance) {
								EconManager.withdrawCash(player, requiredBalance, Go.getMinigameDomain(player), this);
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
								unlocked = 1;
								getConfig().set(path + ".kitpvp.trails." + trail, "1");
								saveConfig();
								upgradedTo = true;
							} else {
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
								player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "You do not have enough money to buy this upgrade.");
								return;
							}
						}
						getConfig().set(path + ".kitpvp.trails.selectedTrail", trailType);
						saveConfig();
						if (!upgradedTo) {
							player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
						}
						player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred trail has been changed.");
						openTrailManagementInventory(player);
					}
				}
			} else {
				player.closeInventory();
			}
		}
		if ((event.getCurrentItem() != null) && (event.getInventory().getName().equals(ChatColor.BOLD + "Select or upgrade your kit!"))) {
			String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
			if (Go.getCurrentMinigame(player) == ServerMinigame.KITPVP) {
				if ((event.getCurrentItem().hasItemMeta()) && (event.getCurrentItem().getItemMeta().hasDisplayName())) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					if (meta.getDisplayName().startsWith(prefix + "Perform Upgrade")) {
						int upgradeCount = Go.Kitpvp.getUpgradeCount(player, this);
						String requiredBalanceStr = meta.getDisplayName().substring(meta.getDisplayName().indexOf("$") + 1);
						requiredBalanceStr = requiredBalanceStr.substring(0, requiredBalanceStr.indexOf(prefix + ")"));
						int requiredBalance = Integer.parseInt(requiredBalanceStr);
						int currentBalance = EconManager.retrieveCash(player, Go.getMinigameDomain(player), this);
						if (upgradeCount + 1 <= 3) {
							if (currentBalance >= requiredBalance) {
								EconManager.withdrawCash(player, requiredBalance, Go.getMinigameDomain(player), this);
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
								Go.Kitpvp.setUpgradeCount(player, upgradeCount + 1, this);
								Go.Kitpvp.setPreferredUpgrade(player, upgradeCount + 1, this);
								player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred kit upgrade has been changed.");
								openKitUpgradeInventory(player);
							} else {
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
								player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "You do not have enough money to buy this upgrade.");
							}
						} else {
							player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
						}
					} else if (meta.getDisplayName().startsWith(prefix + "Select preferred weapon")) {
						int type = -1;
						if ((item.getType() == Material.STONE_AXE) || (item.getType() == Material.IRON_AXE)) {
							type = 1;
						} else {
							type = 0;
						}
						if (type == 1) {
							Go.Kitpvp.setPreferredWeapon(player, Material.STONE_AXE.getId(), this);
						} else if (type == 0) {
							Go.Kitpvp.setPreferredWeapon(player, Material.STONE_SWORD.getId(), this);
						}
						player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred weapon type has been changed.");
						openKitUpgradeInventory(player);
						player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
					} else if (meta.getDisplayName().startsWith(prefix + "Select kit upgrade")) {
						int upgradeNumber = Integer.parseInt(meta.getDisplayName().substring(meta.getDisplayName().indexOf("#") + 1));
						int upgradeCount = Go.Kitpvp.getUpgradeCount(player, this);
						if ((upgradeNumber > 0) && (upgradeNumber <= upgradeCount + 1)) {
							Go.Kitpvp.setPreferredUpgrade(player, upgradeNumber - 1, this);
							player.sendMessage(Go.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred kit upgrade has been changed.");
							openKitUpgradeInventory(player);
							player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
						}
					} else {
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1.0F, 1.0F);
					}
				}
			} else {
				player.closeInventory();
			}
		}
		if (event.getInventory().getType() == InventoryType.ENCHANTING) {
			if (event.getCurrentItem() == null) {
				return;
			}
			Dye dye = new Dye();
			dye.setColor(DyeColor.BLUE);
			ItemStack item = dye.toItemStack();
			item.setAmount(event.getCurrentItem().getAmount());
			if (event.getCurrentItem().isSimilar(item)) {
				event.setCancelled(true);
			}
			return;
		}
		if ((event.getCurrentItem() != null) && (event.getInventory().getName().startsWith(ChatColor.BOLD + "Virtual Store"))) {
			String name = event.getInventory().getName();
			String code = player.getWorld().getName() + ";" + name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]"));
			if (!signConfig.getData().contains("signs." + code.replace(";", ""))) {
				player.sendMessage("This shop has not been activated by a qualified staff member.");
			} else {
				int blockX = Integer.parseInt(code.split(";")[1]);
				int blockY = Integer.parseInt(code.split(";")[2]);
				int blockZ = Integer.parseInt(code.split(";")[3]);
				BlockState state = player.getWorld().getBlockAt(blockX, blockY, blockZ).getState();
				if ((state instanceof Sign)) {
					Sign sign = (Sign) state;
					if (ChatColor.stripColor(sign.getLine(0)).equals("Sell")) {
						Material material = Material.getMaterial(Integer.parseInt(String.valueOf(sign.getLine(1)).split(":")[0]));
						int amount = event.getCurrentItem().getAmount();
						int data = 0;
						try {
							data = Integer.parseInt(String.valueOf(sign.getLine(1)).split(":")[1]);
						} catch (Exception localException) {
						}
						ItemStack item = new ItemStack(material, amount, (short) data);
						if (material == Material.MOB_SPAWNER) {
							item = CraftGo.MobSpawner.newSpawnerItem((short) data, amount);
						}
						if ((event.getCurrentItem().getType() != Material.AIR) && (!event.getCurrentItem().isSimilar(item))) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
		if (event.getInventory().getName().equals(ChatColor.DARK_PURPLE + "Server Selector (0b1)")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			switch (event.getCurrentItem().getType()) {
				case BOW:
					enterSkyfight(player, false, false);
					break;
				case STONE_PICKAXE:
					openSurvivalServerSelectorMenu(player);
					break;
				case IRON_SWORD:
					enterKitpvp(player, false, false);
					break;
				case GRASS:
					enterCreative(player, false, false);
					break;
				default:
					player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1.0F, 1.0F);
					event.setCancelled(true);
					return;
			}
		} else if (event.getInventory().getName().equals(ChatColor.DARK_PURPLE + "Server Selector (0b10)")) {
			if (event.getCurrentItem() == null) {
				return;
			}
			switch (event.getCurrentItem().getType()) {
				case DIAMOND_SWORD:
					enterFactions(player, false, false);
					break;
				case STONE_PICKAXE:
					enterSurvival(player, false, false);
					break;
				default:
					player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1.0F, 1.0F);
					event.setCancelled(true);
					return;
			}
		} else if ((skyfight.contains(player.getUniqueId())) && (player.getGameMode() == GameMode.SURVIVAL)) {
			event.setCancelled(true);
		} else if (event.getInventory().getName().equals(ChatColor.BOLD + "Temporary Inventory")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		Player damager;
		if (event.getDamager() instanceof Player && Server.anticheat.antiafk.lackingMovementMinutes.containsKey((damager = (Player) event.getDamager()).getUniqueId())) {
			Server.anticheat.antiafk.lackingMovementMinutes.remove(damager.getUniqueId());
		}
		if (event.getEntity() instanceof Player) {
			Player killer;
			Player player = (Player) event.getEntity();
			if (factions.contains(player.getUniqueId()) || survival.contains(player.getUniqueId())) {
				String domain = Go.getMinigameDomain(player);
				Location spawnLocation = null;
				if (player.getWorld().getEnvironment() == World.Environment.NORMAL) {
					spawnLocation = Go.getZoneLocation(domain, (Plugin) this);
				} else if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
					spawnLocation = Go.getZoneLocation(String.valueOf(domain) + "_end", (Plugin) this);
				} else if (player.getWorld().getEnvironment() == World.Environment.THE_END) {
					spawnLocation = Go.getZoneLocation(String.valueOf(domain) + "_end", (Plugin) this);
				}
				if (player.getLocation().distance(spawnLocation) < 30.0) {
					event.setCancelled(true);
					return;
				}
			}
			boolean arrowCause = false;
			if (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
				killer = (Player) ((Arrow) event.getDamager()).getShooter();
				arrowCause = true;
			} else if (event.getDamager() instanceof Player) {
				killer = (Player) event.getDamager();
			} else {
				return;
			}
			if (player.getEntityId() == killer.getEntityId()) {
				return;
			}
			if (delayedTeleports.containsKey(player.getUniqueId())) {
				delayedTeleports.get(player.getUniqueId()).close( ChatColor.GRAY + "Teleportation request aborted due to combat.");
			}
			if (delayedTeleports.containsKey(killer.getUniqueId())) {
				delayedTeleports.get(killer.getUniqueId()).close( ChatColor.GRAY + "Teleportation request aborted due to combat.");
			}
			if (kitpvp.contains(player.getUniqueId()) || factions.contains(player.getUniqueId())) {
				ExtremeDouble existingExtreme;
				CombatTimer ct;
				String tag = Go.getMinigameTag(player);
				if (!inCombat.containsKey(player.getUniqueId())) {
					ct = new CombatTimer(player, killer, 12, tag);
					ct.runTaskTimer((Plugin) this, 4L, 4L);
				} else {
					existingExtreme = inCombat.get(player.getUniqueId());
					existingExtreme.setArg1(12.0);
					inCombat.put(player.getUniqueId(), (ExtremeDouble) existingExtreme);
				}
				if (!inCombat.containsKey(killer.getUniqueId())) {
					ct = new CombatTimer(killer, player, 12, tag);
					ct.runTaskTimer((Plugin) this, 4L, 4L);
				} else {
					existingExtreme = inCombat.get(killer.getUniqueId());
					existingExtreme.setArg1(12.0);
					inCombat.put(killer.getUniqueId(), (ExtremeDouble) existingExtreme);
				}
			}
			if (skyfight.contains(player.getUniqueId())) {
				this.skyfightTag.put(killer.getUniqueId(), player.getUniqueId());
				if (arrowCause) {
					for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
						if (!Go.isEffectsEnabled(otherPlayer, (Plugin) this)) continue;
						otherPlayer.playEffect(player.getLocation(), Effect.EXPLOSION_LARGE, 1);
					}
				}
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		int boneAmount;
		Random ran;
		EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
		if (event.getEntity().getKiller() instanceof Player) {
			return;
		}
		boolean lavaSkeleton = false;
		boolean lavaCause = false;
		if (damageEvent != null && damageEvent.getCause() == EntityDamageEvent.DamageCause.LAVA) {
			lavaCause = true;
		}
		if (damageEvent != null && damageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE) {
			lavaCause = true;
		}
		if (damageEvent != null && damageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
			lavaCause = true;
		}
		if (lavaCause && event.getEntity() instanceof Skeleton) {
			event.getDrops().clear();
			ran = new Random();
			if (ran.nextInt(90) == 0) {
				ItemStack itm = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
				event.getDrops().add(itm);
			}
			if ((boneAmount = ran.nextInt(3)) == 0) {
				boneAmount = 1;
			}
			event.getDrops().add(Go.createMaterial(Material.BONE, boneAmount));
			lavaSkeleton = true;
		}
		if (!lavaSkeleton && event.getEntity() instanceof Skeleton) {
			event.getDrops().clear();
			ran = new Random();
			boneAmount = ran.nextInt(4);
			if (boneAmount == 0) {
				boneAmount = 1;
			}
			event.getDrops().add(Go.createMaterial(Material.BONE, boneAmount));
		} else if (event.getEntity() instanceof Zombie) {
			event.getDrops().clear();
			ran = new Random();
			int fleshAmount = ran.nextInt(3);
			if (fleshAmount == 0) {
				fleshAmount = 1;
			}
			event.getDrops().add(Go.createMaterial(Material.ROTTEN_FLESH, fleshAmount));
		} else if (event.getEntity() instanceof IronGolem) {
			event.getDrops().clear();
			ran = new Random();
			int ironAmount = ran.nextInt(6);
			if (ironAmount == 0) {
				ironAmount = 1;
			}
			event.getDrops().add(Go.createMaterial(Material.IRON_INGOT, ironAmount));
		}
	}

	@EventHandler
	public void onBlockEat(EntityChangeBlockEvent eat) {
		Material type = eat.getBlock().getType();
		if (eat.getEntity() instanceof Wither && type == Material.OBSIDIAN) {
			eat.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityTeleport(EntityTeleportEvent event) {
		if (event.getEntity() instanceof Enderman) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHealthChange(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			for (Player op : Bukkit.getOnlinePlayers()) {
				Go.Scoreboard.configureHealth(op);
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (skyfight.contains(player.getUniqueId())) {
			this.enterSkyfight(player, false, false);
		}
		Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

			@Override
			public void run() {
				for (Player op : Bukkit.getOnlinePlayers()) {
					Go.Scoreboard.configureHealth(op);
				}
			}
		}, 20L);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof IronGolem) {
			if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
				event.setDamage(event.getDamage() * 8.0);
			}
			return;
		}
		if (!(event.getEntity() instanceof Player)) {
			return;
		}
		Player player = (Player) event.getEntity();
		if (skyfight.contains(player.getUniqueId()) && player.getHealth() - event.getFinalDamage() < 5.0) {
			this.enterSkyfight(player, false, false);
			event.setCancelled(true);
		}
		if (event.getCause() == EntityDamageEvent.DamageCause.FALL && !factions.contains(player.getUniqueId())) {
			event.setCancelled(true);
			return;
		}
		if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET && player.getInventory().getChestplate() != null && player.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE && player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == Material.CHAINMAIL_LEGGINGS && player.getInventory().getBoots() != null && player.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS) {
			if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
				player.setFireTicks(0);
				event.setCancelled(true);
				return;
			}
			if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
				event.setDamage(event.getDamage() / 2.0);
			} else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
				event.setCancelled(true);
				return;
			}
		}
		Bukkit.getScheduler().runTaskLater((Plugin) this, new Runnable() {

			@Override
			public void run() {
				for (Player op : Bukkit.getOnlinePlayers()) {
					Go.Scoreboard.configureHealth(op);
				}
			}
		}, 5L);
	}

	public void fetchLobby(Player player) {
		if (hub.contains(player.getUniqueId())) {
			boolean isNothing;
			boolean bl = isNothing = player.getInventory().getItem(0) == null;
			if (isNothing || player.getInventory().getItem(0).getType() != Material.COMPASS) {
				Go.clearPlayer(player);
				ItemStack compass = Go.createMaterial(Material.COMPASS,  ChatColor.LIGHT_PURPLE + "Server Selector");
				player.getInventory().addItem(new ItemStack[]{compass});
			}
			player.setHealth(20.0);
			player.setLevel(0);
			player.setFoodLevel(20);
			player.setFireTicks(0);
			String message = "   Welcome " + player.getName() + ", to our Minecraft server!  ";
			message = String.valueOf(message) + message.substring(0, 16);
			Hashtable<String, Integer> array = new Hashtable<String, Integer>();
			array.put( ChatColor.GRAY + "Kitpvp " +  ChatColor.MAGIC + "$", kitpvp.size());
			array.put( ChatColor.GRAY + "Factions  " +  ChatColor.MAGIC + "$", factions.size());
			array.put( ChatColor.GRAY + "Survival  " +  ChatColor.MAGIC + "$", survival.size());
			array.put( ChatColor.GRAY + "Skyfight  " +  ChatColor.MAGIC + "$", skyfight.size());
			array.put( ChatColor.GRAY + "Creative  " +  ChatColor.MAGIC + "$", creative.size());
			this.hubScoreboardTitleIndex.putIfAbsent(player.getUniqueId(), 0);
			int index = this.hubScoreboardTitleIndex.get(player.getUniqueId());
			if (message.length() <= 16) {
				Go.Scoreboard.configureSidebar(player, message, array, false);
			} else {
				int finalIndex = index + 16;
				if (index < message.length() && finalIndex < message.length()) {
					Go.Scoreboard.configureSidebar(player, message.substring(index, finalIndex), array, false);
					this.hubScoreboardTitleIndex.put(player.getUniqueId(), index + 1);
				} else {
					this.hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (Go.isAuthenticated(player)) {
			if (player.isBlocking()) {
				player.setWalkSpeed(0.1f);
			} else {
				player.setWalkSpeed(0.2f);
			}
		}
		player.setFlySpeed(0.1f);
		if (event.getFrom().distance(event.getTo()) > 0.1) {
			if (!Go.isAuthenticated(player)) {
				if (Go.isEffectsEnabled(player, (Plugin) this)) {
					ParticleManager.Play(ParticleManager.Create(EnumParticle.SMOKE_NORMAL, player.getLocation(), 1, 1, 10), player);
				}
				return;
			}
			if (kitpvp.contains(player.getUniqueId()) && player.getGameMode() == GameMode.SURVIVAL) {
				String path = "config." + player.getUniqueId().toString();
				if (this.getConfig().contains(String.valueOf(path) + ".kitpvp.trails.selectedTrail")) {
					int selectedTrail = Integer.parseInt(this.getConfig().getString(String.valueOf(path) + ".kitpvp.trails.selectedTrail"));
					if (selectedTrail == 0) {
						for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
							if (!Go.isEffectsEnabled(otherPlayer, (Plugin) this)) continue;
							ParticleManager.Play(ParticleManager.Create(EnumParticle.SMOKE_NORMAL, player.getLocation(), 1, 1, 10), otherPlayer);
						}
					} else if (selectedTrail == 1) {
						for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
							if (!Go.isEffectsEnabled(otherPlayer, (Plugin) this)) continue;
							ParticleManager.Play(ParticleManager.Create(EnumParticle.VILLAGER_HAPPY, player.getLocation(), 1, 1, 5), otherPlayer);
						}
					} else if (selectedTrail == 2) {
						for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
							if (!Go.isEffectsEnabled(otherPlayer, (Plugin) this)) continue;
							ParticleManager.Play(ParticleManager.Create(EnumParticle.DRIP_LAVA, player.getLocation(), 1, 1, 10), otherPlayer);
						}
					} else if (selectedTrail == 3) {
						for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
							if (!Go.isEffectsEnabled(otherPlayer, (Plugin) this)) continue;
							ParticleManager.Play(ParticleManager.Create(EnumParticle.REDSTONE, player.getLocation(), 1, 1, 15), otherPlayer);
						}
					}
				}
			} else if (skyfight.contains(player.getUniqueId())) {
				if (player.getGameMode() == GameMode.SURVIVAL) {
					for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
						if (!Go.isEffectsEnabled(otherPlayer, (Plugin) this)) continue;
						ParticleManager.Play(ParticleManager.Create(EnumParticle.REDSTONE, player.getLocation(), 1, 1, 5), otherPlayer);
					}
				}
				if (player.getHealth() < 5.0 || player.getLocation().getBlockY() < 5) {
					this.enterSkyfight(player, false, false);
				}
			}
			if (hub.contains(player.getUniqueId()) || skyfight.contains(player.getUniqueId())) {
				if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR && !this.canDoubleJump.contains(player.getUniqueId())) {
					this.canDoubleJump.add(player.getUniqueId());
					player.setAllowFlight(true);
				}
			} else if (factions.contains(player.getUniqueId()) && player.isGliding()) {
				World.Environment environment = player.getWorld().getEnvironment();
				if (environment == World.Environment.THE_END || environment == World.Environment.NETHER) {
					if (environment == World.Environment.NETHER && player.getLocation().getY() < 128.0) {
						return;
					}
					int blocksUntilGround = Math.abs(player.getLocation().getBlockY() - player.getWorld().getHighestBlockYAt(player.getLocation()));
					Vector vec = player.getLocation().getDirection();
					vec = vec.multiply(new Vector(0.5, 0.8, 0.5));
					if (player.getLocation().getY() > 175.0) {
						vec.setY(-0.5);
					}
					if (blocksUntilGround > 5) {
						anticheat.handleVelocity(player, vec);
					}
				}
				return;
			}
			if (hub.contains(player.getUniqueId())) {
				ItemStack item0;
				Material faceDownType2;
				Material faceDownType1;
				UUID id = player.getUniqueId();
				if (kitpvp.contains(id) || factions.contains(id) || skyfight.contains(id) || creative.contains(id) || survival.contains(player.getUniqueId())) {
					hub.remove(id);
					return;
				}
				if (!player.getWorld().getName().equals(lastKnownHubWorld) && !moderatingPlayers.containsKey(player.getUniqueId())) {
					player.teleport(Go.getZoneLocation("hub", (Plugin) this));
				}
				if ((item0 = player.getInventory().getItem(0)) == null || item0.getType() != Material.COMPASS) {
					this.fetchLobby(player);
				}
				if ((faceDownType1 = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType()) != (faceDownType2 = event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType())) {
					if (faceDownType1 == Material.DIAMOND_ORE && Go.isMinigameEnabled(ServerMinigame.CREATIVE, (Plugin) this) && player.getGameMode() != GameMode.CREATIVE) {
						this.enterCreative(player, false, false);
						return;
					}
					if (Go.isMinigameEnabled(ServerMinigame.SKYFIGHT, (Plugin) this) && faceDownType1 == Material.GOLD_ORE && player.getGameMode() != GameMode.CREATIVE) {
						this.enterSkyfight(player, false, false);
						return;
					}
					if (Go.isMinigameEnabled(ServerMinigame.KITPVP, (Plugin) this) && faceDownType1 == Material.IRON_ORE && player.getGameMode() != GameMode.CREATIVE) {
						this.enterKitpvp(player, false, false);
						return;
					}
					if (Go.isMinigameEnabled(ServerMinigame.FACTIONS, (Plugin) this) && faceDownType1 == Material.COAL_ORE && player.getGameMode() != GameMode.CREATIVE) {
						this.enterFactions(player, false, false);
						return;
					}
					if (Go.isMinigameEnabled(ServerMinigame.SURVIVAL, (Plugin) this) && faceDownType1 == Material.LAPIS_ORE && player.getGameMode() != GameMode.CREATIVE) {
						this.enterSurvival(player, false, false);
						return;
					}
					if ((Go.isMinigameEnabled(ServerMinigame.FACTIONS, (Plugin) this) || Go.isMinigameEnabled(ServerMinigame.SURVIVAL, (Plugin) this)) && faceDownType1 == Material.EMERALD_ORE && player.getGameMode() != GameMode.CREATIVE) {
						this.openSurvivalServerSelectorMenu(player);
						return;
					}
				}
			}
		}
	}

	private void clearChat(String playerName) {
		int i = 0;
		while (i < 150) {
			Bukkit.broadcastMessage((String) "");
			++i;
		}
		Bukkit.broadcastMessage((String) (String.valueOf(pluginLabel) +  ChatColor.GRAY + "Chat has been cleared by " +  ChatColor.RED + playerName));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPreprocessCommand(PlayerCommandPreprocessEvent event) {
		String label;
		Player player = event.getPlayer();
		if (Server.anticheat.antiafk.lackingMovementMinutes.containsKey(player.getUniqueId())) {
			Server.anticheat.antiafk.lackingMovementMinutes.remove(player.getUniqueId());
		}
		if ((label = event.getMessage().split(" ")[0]).contains(":") && !player.hasPermission("skorrloregaming.events.override")) {
			String[] strArr = event.getMessage().split(" ");
			strArr[0] = "/" + strArr[0].split(":")[1];
			if (strArr[0].length() > 0) {
				event.setMessage(String.join(" ", strArr));
			} else {
				event.setCancelled(true);
			}
		}
		label = event.getMessage().split(" ")[0];
		String world = event.getPlayer().getWorld().getName();
		UUID id = event.getPlayer().getUniqueId();
		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			int rankID = Go.getRankId(otherPlayer, (Plugin) this);
			if (!otherPlayer.hasPermission("skorrloregaming.events.chatlisten") && rankID <= -1 || Go.isAuthenticationCommand(event.getMessage()))
				continue;
			otherPlayer.sendMessage( ChatColor.GRAY + "[" +  ChatColor.WHITE + world +  ChatColor.GRAY + "] " +  ChatColor.WHITE + "<" + Go.toRankTag(Go.getRank(id, (Plugin) this)) + player.getName() +  ChatColor.WHITE + "> " +  ChatColor.GRAY +  ChatColor.ITALIC + event.getMessage());
		}
		if (Go.isAuthenticationCommand(event.getMessage())) {
			Bukkit.getConsoleSender().sendMessage(String.valueOf(player.getName()) + " issued authentication command: " + event.getMessage());
		}
		if (Go.isAuthenticationCommand(event.getMessage()) && !label.equalsIgnoreCase("/login") && player.getName().equalsIgnoreCase("Player")) {
			player.sendMessage(String.valueOf(Go.Legacy.tag) + "You are not allowed to modify account information.");
			event.setCancelled(true);
		}
		if (label.equalsIgnoreCase("/version") || label.equalsIgnoreCase("/ver") || label.equalsIgnoreCase("/icanhasbukkit")) {
			player.sendMessage(String.valueOf(Go.Legacy.tag) + "This server is running CraftBukkit version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")");
			event.setCancelled(true);
		} else if (label.equalsIgnoreCase("/plugins") || label.equalsIgnoreCase("/pl")) {
			Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
			StringBuilder sb = new StringBuilder(String.valueOf(Go.Legacy.tag) +  ChatColor.WHITE + "Plugins (" + plugins.length + "): ");
			Plugin[] arrplugin = plugins;
			int n = arrplugin.length;
			int n2 = 0;
			while (n2 < n) {
				Plugin plugin = arrplugin[n2];
				if (plugin.isEnabled()) {
					sb.append( ChatColor.GREEN + plugin.getName() +  ChatColor.WHITE + ", ");
				} else {
					sb.append( ChatColor.RED + plugin.getName() +  ChatColor.WHITE + ", ");
				}
				++n2;
			}
			String message = sb.toString();
			player.sendMessage(message.substring(0, message.lastIndexOf(", ")));
			event.setCancelled(true);
		}
		if (Go.isAuthenticated(player)) {
			if (label.equalsIgnoreCase("/party") && Go.getCurrentMinigame(event.getPlayer()) != ServerMinigame.FACTIONS) {
				event.setCancelled(true);
				player.sendMessage(String.valueOf(Go.getMinigameTag(event.getPlayer())) +  ChatColor.RED + "This minigame prevents use of this command.");
			} else if (label.equalsIgnoreCase("/skyfight")) {
				player.performCommand("server skyfight");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/creative")) {
				player.performCommand("server creative");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/kitpvp")) {
				player.performCommand("server kitpvp");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/factions")) {
				player.performCommand("server factions");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/survival")) {
				player.performCommand("server survival");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/p")) {
				event.setMessage(event.getMessage().toLowerCase().replaceFirst("/p", "/plot"));
			} else if (label.equalsIgnoreCase("/gm")) {
				event.setMessage(event.getMessage().toLowerCase().replaceFirst("/gm", "/gamemode"));
			} else if (label.equalsIgnoreCase("/gmc")) {
				event.setMessage(event.getMessage().toLowerCase().replaceFirst("/gmc", "/gamemode 1"));
			} else if (label.equalsIgnoreCase("/gms")) {
				event.setMessage(event.getMessage().toLowerCase().replaceFirst("/gms", "/gamemode 0"));
			} else if (label.equalsIgnoreCase("/me")) {
				event.setCancelled(true);
				Go.playLackPermissionMessage((CommandSender) player);
			} else if (label.equalsIgnoreCase("/f")) {
				if (inCombat.containsKey(player.getUniqueId())) {
					player.sendMessage(String.valueOf(Go.getMinigameTag(player)) +  ChatColor.RED + "You cannot use this command during combat.");
					event.setCancelled(true);
				}
				if (!factions.contains(event.getPlayer().getUniqueId())) {
					event.setCancelled(true);
					player.sendMessage(String.valueOf(Go.getMinigameTag(player)) +  ChatColor.RED + "This minigame prevents use of this command.");
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		label = label.toLowerCase();
		if (label.equals("debug")) {
			if (!sender.hasPermission("skorrloregaming.debug")) {
				Go.playLackPermissionMessage(sender);
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enable /disable>");
				return true;
			}
			if (args[0].toLowerCase().equals("enable")) {
				pluginDebug = true;
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Plugin debugging has been enabled.");
				return true;
			}
			if (args[0].toLowerCase().equals("disable")) {
				pluginDebug = false;
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Plugin debugging has been disabled.");
				return true;
			}
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enable /disable>");
			return true;
		}
		if ((label.equals("website")) || (label.equals("store")) || (label.equals("forums")) || (label.equals("forum")) || (label.equals("apply"))) {
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Website URL: " + ChatColor.RED + "http://skorrloregaming.enjin.com/");
			return true;
		}
		if ((label.equals("warning")) || (label.equals("warn"))) {
			if (!sender.hasPermission("skorrloregaming.warning")) {
				Go.playLackPermissionMessage(sender);
				return true;
			}
			if (args.length < 2) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <amount> [message]");
				return true;
			}
			int id = 0;
			try {
				id = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must enter a valid numerical warning value.");
				return true;
			}
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
			if ((!op.hasPlayedBefore()) && (!op.isOnline())) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String configPath = "config." + op.getUniqueId().toString();
			if (!getConfig().contains(configPath + ".ip")) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String ipAddress = getConfig().getString(configPath).replace("x", ".");
			String path = "warning." + ipAddress;
			if (!getConfig().contains(path + ".warning")) {
				getConfig().set(path + ".warning", "0");
				saveConfig();
			}
			int oldWarningCount = Integer.parseInt(getConfig().getString(path + ".warning"));
			int newWarningCount = oldWarningCount + id;
			if (newWarningCount < 0) {
				newWarningCount = 0;
			}
			if (newWarningCount > 5) {
				newWarningCount = 5;
			}
			getConfig().set("config." + op.getUniqueId().toString() + ".warning", newWarningCount);
			saveConfig();
			if (args.length > 2) {
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				if ((op.isOnline()) && (id > 0)) {
					op.getPlayer().kickPlayer(sb.toString());
				}
				sender.sendMessage(Go.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5) '" + sb.toString().trim() + "'");
				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (!pl.getName().equals(sender.getName())) {
						pl.sendMessage(Go.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5) '" + sb.toString().trim() + "'");
					}
				}
			} else {
				if ((op.isOnline()) && (id > 0)) {
					op.getPlayer().kickPlayer("You have been warned for disrespecting the server rules.");
				}
				sender.sendMessage(Go.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5)");
				for (Player pl : getServer().getOnlinePlayers()) {
					if (!pl.getName().equals(sender.getName())) {
						pl.sendMessage(Go.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5)");
					}
				}
			}
			if ((id < 0) && (oldWarningCount == 5)) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pardon " + op.getName());
			}
			if (newWarningCount >= 5) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + op.getName() + " You have exceeded the maximum warning count for this server.");
			}
		} else if (label.equals("reset")) {
			if (!sender.hasPermission("skorrloregaming.reset")) {
				Go.playLackPermissionMessage(sender);
				return true;
			}
			if (args.length == 0) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
				return true;
			}
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
			if (((!op.hasPlayedBefore()) && (!op.isOnline())) || (!getConfig().contains("config." + op.getUniqueId().toString()))) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			if (op.isOnline()) {
				op.getPlayer().kickPlayer(ChatColor.RED + "You are being forcefully unregistered from the server :/");
			}
			getConfig().set("config." + op.getUniqueId().toString(), null);
			saveConfig();
			sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The specified player should now be unregistered.");
		} else if (label.equals("who")) {
			if (args.length == 0) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			int latency = CraftGo.Player.getConnectionLatency(player);
			ServerMinigame minigame = Go.getCurrentMinigame(player);
			ServerMinigame worldMinigame = Go.getMinigameFromWorld(player.getWorld(), this);
			String playtime = Go.formatTime(CraftGo.Player.getApproximatePlaytime(player));
			String rank = WordUtils.capitalize(Go.getRank(player, this));
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + player.getName());
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Connection latency (ping): " + ChatColor.RED + latency);
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Detected Minecraft version: " + ChatColor.RED + ChatColor.BOLD + CraftGo.Player.getProtocolVersionName(player));
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Current minigame: " + ChatColor.RED + WordUtils.capitalize(minigame.toString().toLowerCase()));
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "World-based minigame: " + ChatColor.RED + WordUtils.capitalize(worldMinigame.toString().toLowerCase()));
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Current server-side rank: " + ChatColor.RED + rank);
			if (sender.hasPermission("skorrloregaming.who")) {
				String location = CraftGo.Player.getCity(player) + ", " + CraftGo.Player.getState(player);
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Defined IP address: " + ChatColor.RED + player.getAddress().getAddress().getHostAddress());
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Defined country: " + ChatColor.RED + CraftGo.Player.getCountry(player));
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Defined geo-location: " + ChatColor.RED + location);
			}
		} else if (label.equals("reload-cfg")) {
			if (!sender.hasPermission("skorrloregaming.reload")) {
				Go.playLackPermissionMessage(sender);
				return true;
			}
			reloadConfig();
			banConfig.reloadData();
			warpConfig.reloadData();
			signConfig.reloadData();
			factionsConfig.reloadData();
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Server configuration successfully reloaded.");
		} else if (label.equals("list")) {
			StringBuilder kitpvpArray = new StringBuilder();
			StringBuilder factionsArray = new StringBuilder();
			StringBuilder survivalArray = new StringBuilder();
			StringBuilder skyfightArray = new StringBuilder();
			StringBuilder creativeArray = new StringBuilder();
			StringBuilder hubArray = new StringBuilder();
			int length = 0;
			for (Player player : Bukkit.getOnlinePlayers()) {
				length++;
				String domain = Go.getMinigameDomain(player);
				if (domain.equals("kitpvp")) {
					kitpvpArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
				} else if (domain.equals("factions")) {
					factionsArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
				} else if (domain.equals("survival")) {
					survivalArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
				} else if (domain.equals("skyfight")) {
					skyfightArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
				} else if (domain.equals("creative")) {
					creativeArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
				} else if (domain.equals("hub")) {
					hubArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
				}
			}
			String kitpvpMsg = kitpvpArray.toString();
			String factionsMsg = factionsArray.toString();
			String survivalMsg = survivalArray.toString();
			String skyfightMsg = skyfightArray.toString();
			String creativeMsg = creativeArray.toString();
			String hubMsg = hubArray.toString();
			sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "There are currently " + ChatColor.RED + length + "/" + Bukkit.getMaxPlayers() + ChatColor.GRAY + " players online.");
			if (kitpvpMsg.length() > 0) {
				sender.sendMessage(Go.Kitpvp.tag + ChatColor.RED + kitpvpMsg.substring(0, kitpvpMsg.lastIndexOf(new StringBuilder(", ").append(ChatColor.RED).toString())));
			}
			if (factionsMsg.length() > 0) {
				sender.sendMessage(Go.Factions.tag + ChatColor.RED + factionsMsg.substring(0, factionsMsg.lastIndexOf(new StringBuilder(", ").append(ChatColor.RED).toString())));
			}
			if (survivalMsg.length() > 0) {
				sender.sendMessage(Go.Survival.tag + ChatColor.RED + survivalMsg.substring(0, survivalMsg.lastIndexOf(new StringBuilder(", ").append(ChatColor.RED).toString())));
			}
			if (skyfightMsg.length() > 0) {
				sender.sendMessage(Go.Skyfight.tag + ChatColor.RED + skyfightMsg.substring(0, skyfightMsg.lastIndexOf(new StringBuilder(", ").append(ChatColor.RED).toString())));
			}
			if (creativeMsg.length() > 0) {
				sender.sendMessage(Go.Creative.tag + ChatColor.RED + creativeMsg.substring(0, creativeMsg.lastIndexOf(new StringBuilder(", ").append(ChatColor.RED).toString())));
			}
			if (hubMsg.length() > 0) {
				sender.sendMessage(Go.Lobby.tag + ChatColor.RED + hubMsg.substring(0, hubMsg.lastIndexOf(new StringBuilder(", ").append(ChatColor.RED).toString())));
			}
		} else if (label.equals("build-time")) {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			sender.sendMessage(Go.italicGray + "Server last updated as of " + df.format(Go.getLastCompilationTime()));
		} else if (label.equals("motd")) {
			if (sender.hasPermission("skorrloregaming.motd")) {
				if (args.length == 0) {
					sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <motd>");
					return true;
				}
				if (!tempMotd.equals("/unspecified")) {
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must wait before using this command again.");
				} else {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < args.length; i++) {
						sb.append(args[i] + " ");
					}
					tempMotd = sb.toString();
					Bukkit.getScheduler().runTaskLater(this, new Runnable() {
						public void run() {
							Server.tempMotd = "/unspecified";
						}
					}, 1200L);
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The motd has been temporarily changed.");
				}
			} else {
				Go.playLackPermissionMessage(sender);
				return true;
			}
		} else {
			if ((label.equals("ban")) || (label.equals("ban-ip"))) {
				if (sender.hasPermission("skorrloregaming.ban")) {
					if (args.length < 2) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player /ip> <msg>");
						return true;
					}
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						sb.append(args[i] + " ");
					}
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
					String path = "config." + offlinePlayer.getUniqueId().toString();
					String ipAddress = "/unspecified";
					if (getConfig().contains(path)) {
						ipAddress = getConfig().getString(path + ".ip");
					} else if (Go.isValidAddress(args[0])) {
						ipAddress = args[0];
					} else {
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The provided data does not match any player.");
					}
					if (!ipAddress.equals("/unspecified")) {
						Player player;
						if (getConfig().contains("address." + ipAddress.replace(".", "x"))) {
							OfflinePlayer bannedPlayer = Bukkit.getOfflinePlayer(UUID.fromString(getConfig().getString("address." + ipAddress.replace(".", "x"))));
							if (bannedPlayer.isOnline()) {
								player = bannedPlayer.getPlayer();
								player.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString().trim()));
							}
						}
						banConfig.getData().set(ipAddress.replace(".", "x"), sb.toString().trim());
						banConfig.saveData();
						for (Player pl : getServer().getOnlinePlayers()) {
							pl.sendMessage(Go.italicGray + "Server: Banned " + offlinePlayer.getName() + " '" + sb.toString().trim() + "'");
						}
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The address should now be banned.");
					}
				} else {
					Go.playLackPermissionMessage(sender);
					return true;
				}
			} else if (label.equals("kick")) {
				if (sender.hasPermission("skorrloregaming.kick")) {
					if (args.length < 2) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <msg>");
						return true;
					}
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						sb.append(args[i] + " ");
					}
					Player player = Bukkit.getPlayer(args[0]);
					if (player == null) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						return true;
					}
					player.kickPlayer(ChatColor.translateAlternateColorCodes('&', sb.toString().trim()));
					for (Player pl : getServer().getOnlinePlayers()) {
						pl.sendMessage(Go.italicGray + "Server: Kicked " + player.getName() + " '" + sb.toString().trim() + "'");
					}
				} else {
					Go.playLackPermissionMessage(sender);
					return true;
				}
			} else if (label.equals("pardon")) {
				if (sender.hasPermission("skorrloregaming.pardon")) {
					if (args.length == 0) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player/ip>");
						return true;
					}
					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
					String path = "config." + offlinePlayer.getUniqueId().toString();
					String ipAddress = "/unspecified";
					if (getConfig().contains(path)) {
						ipAddress = getConfig().getString(path + ".ip");
					} else if (!Go.isValidAddress(args[0])) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The provided data does not match any player.");
					} else {
						ipAddress = args[0];
					}
					if (!ipAddress.equals("/unspecified")) {
						if (banConfig.getData().contains(ipAddress.replace(".", "x"))) {
							banConfig.getData().set(ipAddress.replace(".", "x"), null);
							banConfig.saveData();
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The address should no longer be banned.");
						} else {
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The address is not banned from the server.");
						}
					}
				} else {
					Go.playLackPermissionMessage(sender);
					return true;
				}
			} else if (label.equals("say")) {
				if (sender.hasPermission("skorrloregaming.say")) {
					if ((args.length < 2) || (args[0].charAt(0) != '/')) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <id> <message>");
						return true;
					}
					StringBuilder sb = new StringBuilder();
					for (int i = 1; i < args.length; i++) {
						sb.append(args[i] + " ");
					}
					if (args[0].split("/")[1].equals(".")) {
						Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + lastKnownHubWorld + ChatColor.GRAY + "] " + ChatColor.WHITE + "<" + Go.consoleTag + "Server" + ChatColor.WHITE + "> " + sb.toString());
					} else {
						OfflinePlayer op = Bukkit.getOfflinePlayer(args[0].split("/")[1]);
						Player checkPlayer = Bukkit.getPlayer(args[0].split("/")[1]);
						String world = lastKnownHubWorld;
						if (checkPlayer != null) {
							world = checkPlayer.getWorld().getName();
						}
						if (!op.hasPlayedBefore()) {
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
							return true;
						}
						UUID id = op.getUniqueId();
						Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.WHITE + "<" + Go.toRankTag(Go.getRank(id, this)) + op.getName() + ChatColor.WHITE + "> " + sb.toString());
					}
				}
			} else if (label.equals("mute")) {
				if (sender.hasPermission("mute")) {
					if (args.length == 0) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
						return true;
					}
					Player player = Bukkit.getPlayer(args[0]);
					if (player == null) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						return true;
					}
					if (this.mutedPlayers.contains(player.getUniqueId())) {
						this.mutedPlayers.remove(player.getUniqueId());
						String name = "CONSOLE";
						if ((sender instanceof Player)) {
							name = ((Player) sender).getName();
						}
						Bukkit.broadcastMessage(pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has been unmuted by " + ChatColor.RED + name);
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Unmuted player " + ChatColor.RED + player.getName());
					} else {
						this.mutedPlayers.add(player.getUniqueId());
						String name = "CONSOLE";
						if ((sender instanceof Player)) {
							name = ((Player) sender).getName();
						}
						Bukkit.broadcastMessage(pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has been muted by " + ChatColor.RED + name);
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Muted player " + ChatColor.RED + player.getName());
					}
				} else {
					Go.playLackPermissionMessage(sender);
					return true;
				}
			} else if (label.equals("reboot")) {
				if (sender.hasPermission("skorrloregaming.reboot")) {
					Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
						int value = -1;

						public void run() {
							this.value += 1;
							Bukkit.broadcastMessage(Server.pluginLabel + ChatColor.GOLD + ChatColor.BOLD + "Server restarting.. " + ChatColor.RED + ChatColor.BOLD + (5 - this.value));
							if (this.value > 4) {
								this.value = 0;
								Bukkit.getScheduler().runTask(Server.plugin, new Runnable() {
									public void run() {
										for (Player player : Bukkit.getOnlinePlayers()) {
											Server.this.performBuggedLeave(player, false, true);
											player.kickPlayer(Server.pluginName + ChatColor.AQUA + " ? " + ChatColor.RESET + "Server restarting, please rejoin soon.");
										}
										Bukkit.shutdown();
									}
								});
							}
						}
					}, 20L, 20L);
				} else {
					Go.playLackPermissionMessage(sender);
					return true;
				}
			} else if ((label.equals("clearchat")) || (label.equals("cc"))) {
				if ((sender.hasPermission("skorrloregaming.clearchat")) || ((sender instanceof Player))) {
					if ((sender instanceof Player)) {
						if (Go.getRankId((Player) sender, this) > 1) {
							clearChat(((Player) sender).getName());
						} else {
							Go.playLackPermissionMessage(sender);
							return true;
						}
					} else {
						clearChat("Console");
					}
				} else {
					Go.playLackPermissionMessage(sender);
					return true;
				}
			} else if (label.equals("disable-plugin")) {
				if (!sender.hasPermission("skorrloregaming.plugin.disable")) {
					Go.playLackPermissionMessage(sender);
					return true;
				}
				if (args.length == 0) {
					sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <pluginName>");
					return true;
				}
				Plugin plugin = getServer().getPluginManager().getPlugin(args[0]);
				if (plugin != null) {
					getServer().getPluginManager().disablePlugin(plugin);
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Disabled the plugin ... " + ChatColor.RED + plugin.getName());
				} else {
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified plugin could not be found.");
				}
			} else if (label.equals("enable-plugin")) {
				if (!sender.hasPermission("skorrloregaming.plugin.enable")) {
					Go.playLackPermissionMessage(sender);
					return true;
				}
				if (args.length == 0) {
					sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <pluginName>");
					return true;
				}
				Plugin plugin = getServer().getPluginManager().getPlugin(args[0]);
				if (plugin != null) {
					getServer().getPluginManager().enablePlugin(plugin);
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Enabled the plugin ... " + ChatColor.RED + plugin.getName());
				} else {
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified plugin could not be found.");
				}
			} else {
				if (label.equals("getenchant")) {
					if (!sender.hasPermission("skorrloregaming.getenchant")) {
						Go.playLackPermissionMessage(sender);
						return true;
					}
					if (args.length == 0) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enchantment>");
					} else {
						try {
							int i = Integer.parseInt(args[0]);
							Enchantment enchant = Enchantment.getById(i);
							if (enchant == null) {
								sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified enchantment ID could not be found.");
								return true;
							}
							String name = Go.formatEnchantment(enchant.getName(), 0);
							sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "The named alternative for " + ChatColor.RED + "ID:" + i + ChatColor.GRAY + " is " + ChatColor.RED + name);
						} catch (Exception ex) {
							String returnName = Go.unformatEnchantment(args[0]);
							Enchantment enchant = Enchantment.getByName(returnName.toUpperCase());
							if (enchant == null) {
								sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified enchantment name could not be found.");
								return true;
							}
							String name = Go.formatEnchantment(enchant.getName(), 0);
							sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "The " + ChatColor.RED + "ID" + ChatColor.GRAY + " alternative for " + ChatColor.RED + name + ChatColor.GRAY + " is " + ChatColor.RED + "ID:" + enchant.getId());
						}
					}
				} else if (label.equals("rules")) {
					sender.sendMessage(Go.Legacy.tag + ChatColor.ITALIC + "(1)" + ChatColor.RESET + ChatColor.GRAY + " No hacking or abusing glitches");
					sender.sendMessage(Go.Legacy.tag + ChatColor.ITALIC + "(2)" + ChatColor.RESET + ChatColor.GRAY + " No swearing or disrespecting staff");
					sender.sendMessage(Go.Legacy.tag + ChatColor.ITALIC + "(3)" + ChatColor.RESET + ChatColor.GRAY + " No asking for free ranks or permissions");
					sender.sendMessage(Go.Legacy.tag + ChatColor.ITALIC + "(4)" + ChatColor.RESET + ChatColor.GRAY + " Do not advertise in the public server chat");
					sender.sendMessage(Go.Legacy.tag + ChatColor.ITALIC + "(5)" + ChatColor.RESET + ChatColor.GRAY + " ... Among other common sense server rules");
				} else if (label.equals("setrank")) {
					if (!sender.hasPermission("skorrloregaming.setrank")) {
						Go.playLackPermissionMessage(sender);
						return true;
					}
					if (!Go.isRankingEnabled(this)) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "This ranking system has been disabled.");
					}
					if (args.length != 2) {
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <rank>");
						String s = "";
						for (String str : Go.validRanks) {
							s = s + ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
						}
						s = s.substring(0, s.lastIndexOf(", "));
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
					} else {
						OfflinePlayer targetPlayer = getServer().getOfflinePlayer(args[0]);
						if (targetPlayer == null) {
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						} else if (Go.validRanks.contains(args[1].toLowerCase())) {
							String path = "config." + targetPlayer.getUniqueId().toString();
							getConfig().set(path + ".rank", args[1].toLowerCase());
							saveConfig();
							if ((Go.isPrefixedRankingEnabled(this)) && (targetPlayer.isOnline())) {
								targetPlayer.getPlayer().setDisplayName(Go.toRankTag(args[1].toLowerCase()) + targetPlayer.getName() + ChatColor.RESET);
							}
							Bukkit.broadcastMessage(pluginLabel + ChatColor.RED + targetPlayer.getName() + ChatColor.GRAY + " has been given rank " + ChatColor.RED + WordUtils.capitalize(args[1].toLowerCase()));
						} else {
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified rank could not be found.");
							String s = "";
							for (String str : Go.validRanks) {
								s = s + ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
							}
							s = s.substring(0, s.lastIndexOf(", "));
							sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
						}
					}
				}
			}
		}
		if ((sender instanceof Player)) {
			final Player player = (Player) sender;
			if ((label.equals("staffchat")) || (label.equals("sc"))) {
				if (Go.getRankId(player, this) <= -1) {
					Go.playLackPermissionMessage(player);
					return true;
				}
				if (this.inStaffChat.contains(player.getUniqueId())) {
					this.inStaffChat.remove(player.getUniqueId());
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have left the staff chat.");
				} else {
					this.inStaffChat.add(player.getUniqueId());
					sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have entered the staff chat.");
				}
			} else if (label.equals("vote")) {
				sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Voting URL: " + ChatColor.RED + "http://bit.ly/2v7Xgb4");
			} else {
				if ((label.equals("effect")) || (label.equals("effects")) || (label.equals("particle")) || (label.equals("particles"))) {
					if (args.length == 0) {
						if (Go.isEffectsEnabled(player, this)) {
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Notice. " + ChatColor.GRAY + "Plugin sent particles are enabled.");
						} else {
							sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Notice. " + ChatColor.GRAY + "Plugin sent particles are disabled.");
						}
						sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enable /disable>");
						return true;
					}
					if ((args[0].toLowerCase().equals("enable")) || (args[0].toLowerCase().equals("on"))) {
						Go.setEnableEffects(player, true, this);
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Plugin sent particles has been enabled.");
						return true;
					}
					if ((args[0].toLowerCase().equals("disable")) || (args[0].toLowerCase().equals("off"))) {
						Go.setEnableEffects(player, false, this);
						sender.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Plugin sent particles has been disabled.");
						return true;
					}
					sender.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enable /disable>");
					return true;
				}
				if (label.equals("skull")) {
					if ((player.hasPermission("skorrloregaming.give")) || (Go.getCurrentMinigame(player) == ServerMinigame.CREATIVE)) {
						if (args.length < 1) {
							if (player.hasPermission("skorrloregaming.give")) {
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " [player] <skullOwner>");
							} else {
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <skullOwner>");
							}
							return true;
						}
						ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
						SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
						skull.setDurability((short) 3);
						int argValue = 0;
						if ((player.hasPermission("skorrloregaming.give")) && (args.length > 1)) {
							argValue = 1;
						}
						OfflinePlayer skullOwner = Bukkit.getOfflinePlayer(args[argValue]);
						if ((skullOwner.hasPlayedBefore()) || (skullOwner.isOnline())) {
							skullMeta.setDisplayName(skullOwner.getName() + "'s Skull");
							skullMeta.setOwner(args[argValue]);
						} else {
							skullMeta.setOwner(args[argValue]);
						}
						skull.setItemMeta(skullMeta);
						if (argValue == 1) {
							Player targetPlayer = Bukkit.getPlayer(args[0]);
							if (targetPlayer == null) {
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
								return true;
							}
							targetPlayer.getInventory().addItem(new ItemStack[]{skull});
						} else {
							player.getInventory().addItem(new ItemStack[]{skull});
						}
						player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have been given " + ChatColor.RED + Go.formatMaterial(skull.getType()) + " x1");
						return true;
					}
					Go.playLackPermissionMessage(player);
					return true;
				}
				if ((label.equals("trails")) || (label.equals("trail"))) {
					if (!kitpvp.contains(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
						return true;
					}
					if (inCombat.containsKey(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
						return true;
					}
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
					openTrailManagementInventory(player);
				} else if ((label.equals("clear")) || (label.equals("clearinventory")) || (label.equals("clearinv")) || (label.equals("ci"))) {
					if (inCombat.containsKey(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
						return true;
					}
					player.getInventory().clear();
					player.updateInventory();
					player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Your inventory has been cleared.");
				} else if ((label.equals("upgrade-kit")) || (label.equals("kit-upgrade")) || (label.equals("kit-upgrades")) || (label.equals("upgrade")) || (label.equals("upgrades"))) {
					if (!kitpvp.contains(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
						return true;
					}
					if (inCombat.containsKey(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
						return true;
					}
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
					openKitUpgradeInventory(player);
				} else if ((label.equals("spawn-npc")) || (label.equals("spawnnpc")) || (label.equals("snpc")) || (label.equals("npc"))) {
					if (!player.hasPermission("skorrloregaming.npc.register")) {
						Go.playLackPermissionMessage(player);
						return true;
					}
					if (args.length == 0) {
						player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <npcData> <npcName>");
						return true;
					}
					String name = null;
					if (args.length > 1) {
						StringBuilder sb = new StringBuilder();
						for (int i = 1; i < args.length; i++) {
							sb.append(args[i] + " ");
						}
						name = sb.toString();
					}
					CustomNpc npc = CustomNpc.spawn(player.getLocation(), name);
					ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
					SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
					skull.setDurability((short) 3);
					skullMeta.setOwner("Steve");
					skull.setItemMeta(skullMeta);
					npc.entity.setHelmet(skull);
					npc.entity.setChestplate(Go.createMaterial(Material.LEATHER_CHESTPLATE));
					npc.entity.setLeggings(Go.createMaterial(Material.LEATHER_LEGGINGS));
					npc.entity.setBoots(Go.createMaterial(Material.LEATHER_BOOTS));
					npc.entity.setItemInHand(Go.createMaterial(Material.BLAZE_ROD));
					npc.register(args[0]);
					player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Spawned ArmorStand (Entity ID: " + ChatColor.RED + npc.entity.getEntityId() + ChatColor.GRAY + ")");
					player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Registered Custom NPC (Entity ID: " + ChatColor.RED + npc.entity.getEntityId() + ChatColor.GRAY + ")");
				} else if ((label.equals("remove-npc")) || (label.equals("removenpc")) || (label.equals("rm-npc")) || (label.equals("rmnpc"))) {
					if (!player.hasPermission("skorrloregaming.npc.unregister")) {
						Go.playLackPermissionMessage(player);
						return true;
					}
					if (args.length == 0) {
						if (!confirmUnregisterNpc.contains(player.getUniqueId())) {
							confirmUnregisterNpc.add(player.getUniqueId());
						}
						player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Npc removal parameters " + ChatColor.ITALIC + "saved in memory" + ChatColor.RESET + ChatColor.GRAY + ".");
						player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Right click on the npc that you would like to remove.");
						Bukkit.getScheduler().runTaskLater(this, new Runnable() {
							public void run() {
								if (Server.confirmUnregisterNpc.contains(player.getUniqueId())) {
									Server.confirmUnregisterNpc.remove(player.getUniqueId());
								}
							}
						}, 100L);
					}
				} else if ((label.equals("autologin")) || (label.equals("autolog"))) {
					boolean dailyAuth = getConfig().getBoolean("settings.enable.authme.dailyAuth");
					boolean autoLoginCmd = getConfig().getBoolean("settings.enable.authme.autoLoginCmd");
					if ((!dailyAuth) && (autoLoginCmd)) {
						String ip = player.getAddress().getAddress().getHostAddress().replace(".", "x");
						if (getConfig().contains("autologin." + ip + "." + player.getUniqueId().toString())) {
							getConfig().set("autologin." + ip + "." + player.getUniqueId().toString(), null);
							saveConfig();
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The auto login feature has been disabled.");
						} else if (args.length > 0) {
							Object authObject = Go.getAuthenticationSuite();
							boolean isCorrectPassword = false;
							if (authObject == null) {
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "There is no need to toggle this feature.");
							} else {
								isCorrectPassword = ((NewAPI) authObject).checkPassword(player.getName(), args[0]);
								if (isCorrectPassword) {
									getConfig().set("autologin." + ip + "." + player.getUniqueId().toString(), args[0]);
									saveConfig();
									player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The auto login feature has been enabled.");
								} else {
									player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified account password is incorrect.");
								}
							}
						} else {
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must specify your password to enable this.");
						}
					} else {
						player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "There is no need to toggle this feature.");
					}
				} else if ((label.equals("playtime")) || (label.equals("pt"))) {
					if (args.length == 0) {
						String playtime = Go.formatTime(CraftGo.Player.getApproximatePlaytime(player));
						player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
					} else {
						Player tp = Bukkit.getPlayer(args[0]);
						if (tp == null) {
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						} else {
							String playtime = Go.formatTime(CraftGo.Player.getApproximatePlaytime(tp));
							player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Playtime of " + ChatColor.RED + tp.getName() + ChatColor.GRAY + ": " + ChatColor.RED + playtime);
						}
					}
				} else if ((label.equals("wild")) || (label.equals("rtp"))) {
					if (!factions.contains(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
						return true;
					}
					if (inCombat.containsKey(player.getUniqueId())) {
						player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
						return true;
					}
					Location teleportLocation;
					while (!(teleportLocation = Go.getRandomLocation(player)).clone().subtract(0.0D, 1.0D, 0.0D).getBlock().getType().isSolid()) {
					}
					if (player.getWorld().getEnvironment() == World.Environment.NETHER) {
						teleportLocation.setY(128.0D);
					}
					player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Teleport destination: {" + ChatColor.RED + teleportLocation.getX() + ChatColor.GRAY + ", " + ChatColor.RED + teleportLocation.getY() + ChatColor.GRAY + ", " + ChatColor.RED + teleportLocation.getZ() + ChatColor.GRAY + "}.");
					DelayedTeleport dt = new DelayedTeleport(player, 10, teleportLocation, this, Boolean.valueOf(false));
					dt.runTaskTimerAsynchronously(this, 4L, 4L);
				} else if (label.equals("opme")) {
					int rankID = Go.getRankId(player, this);
					if (rankID >= 2) {
						if (player.isOp()) {
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are already opped on this server.");
						} else {
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You should now be opped temporarily.");
							player.setOp(true);
							this.semiOppedPlayers.add(player.getUniqueId());
							Bukkit.getScheduler().runTaskLater(this, new Runnable() {
								public void run() {
									if (Server.this.semiOppedPlayers.contains(player.getUniqueId())) {
										player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Times up! " + ChatColor.GRAY + "You will now be deopped from this server.");
										player.setOp(false);
										Server.this.semiOppedPlayers.remove(player.getUniqueId());
									}
								}
							}, 3600L);
						}
					} else {
						Go.playLackPermissionMessage(player);
						return true;
					}
				} else if ((label.equals("give")) || (label.equals("giveme"))) {
					if ((player.hasPermission("skorrloregaming.give")) || (Go.getCurrentMinigame(player) == ServerMinigame.CREATIVE)) {
						if (args.length < 1) {
							if (player.hasPermission("skorrloregaming.give")) {
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " [player] <id> <amount>");
							} else {
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <id> <amount>");
							}
							return true;
						}
						int arg = 0;
						Player targetPlayer = player;
						if ((Bukkit.getPlayer(args[0]) != null) && (player.hasPermission("skorrloregaming.give"))) {
							targetPlayer = Bukkit.getPlayer(args[0]);
							if (targetPlayer == null) {
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
								return true;
							}
							arg++;
						}
						Material type = null;
						int[] successTimes = new int[2];
						try {
							type = Material.getMaterial(args[arg].split(":")[0].toUpperCase());
							successTimes[0] = 1;
						} catch (Exception ex) {
							successTimes[0] = 0;
						}
						try {
							type = Material.getMaterial(Integer.parseInt(args[arg].split(":")[0]));
							successTimes[1] = 1;
						} catch (Exception ex) {
							successTimes[1] = 0;
						}
						if ((successTimes[0] == 0) && (successTimes[1] == 0)) {
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified material " + ChatColor.RED + "ID" + ChatColor.GRAY + " could not be found.");
							return true;
						}
						if ((type == null) || (type == Material.AIR)) {
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified material " + ChatColor.RED + "ID" + ChatColor.GRAY + " could not be found.");
							return true;
						}
						short data = 0;
						int amount = 1;
						if (args[arg].contains(":")) {
							data = (short) Integer.parseInt(args[arg].split(":")[1]);
						}
						if (args.length > 1) {
							amount = Integer.parseInt(args[(arg + 1)]);
						}
						ItemStack itm = new ItemStack(type, amount, data);
						if (type == Material.MOB_SPAWNER) {
							itm = CraftGo.MobSpawner.newSpawnerItem(data, amount);
						}
						targetPlayer.getInventory().addItem(new ItemStack[]{itm});
						targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0F, 1.0F);
						player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have been given " + ChatColor.RED + Go.formatMaterial(type) + ":" + data + " x" + amount);
					} else {
						Go.playLackPermissionMessage(player);
						return true;
					}
				} else {
					ItemStack itm;
					if ((label.equals("rename")) || (label.equals("rn"))) {
						if (player.hasPermission("skorrloregaming.rename")) {
							if (args.length < 1) {
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
								return true;
							}
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < args.length; i++) {
								sb.append(args[i] + " ");
							}
							String message = sb.toString();
							itm = player.getItemInHand();
							if ((itm == null) || (itm.getType() == Material.AIR)) {
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please hold the item you would like to rename.");
								return true;
							}
							ItemMeta meta = itm.getItemMeta();
							meta.setDisplayName(message);
							itm.setItemMeta(meta);
							player.setItemInHand(itm);
							player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The display name of the held item has been modified.");
						} else {
							Go.playLackPermissionMessage(player);
							return true;
						}
					} else if ((label.equals("moderate")) || (label.equals("mod"))) {
						if (inCombat.containsKey(player.getUniqueId())) {
							player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
							return true;
						}
						int rankID = Go.getRankId(player, this);
						if ((rankID > 0) || (player.hasPermission("skorrloregaming.moderate"))) {
							if (moderatingPlayers.containsKey(player.getUniqueId())) {
								ServerMinigame minigame = (ServerMinigame) moderatingPlayers.get(player.getUniqueId());
								performBuggedLeave(player, true, true);
								moderatingPlayers.remove(player.getUniqueId());
								if (minigame == ServerMinigame.FACTIONS) {
									enterFactions(player, false, true);
								} else if (minigame == ServerMinigame.KITPVP) {
									enterKitpvp(player, false, true);
								} else if (minigame == ServerMinigame.SKYFIGHT) {
									enterSkyfight(player, false, true);
								} else if (minigame == ServerMinigame.CREATIVE) {
									enterCreative(player, false, true);
								} else if (minigame == ServerMinigame.SURVIVAL) {
									enterSurvival(player, false, true);
								} else if ((minigame == ServerMinigame.HUB) || (minigame == ServerMinigame.UNKNOWN)) {
									player.performCommand("hub");
								}
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are no longer moderating the server.");
							} else {
								ServerMinigame minigame = Go.getCurrentMinigame(player);
								performBuggedLeave(player, false, true);
								player.performCommand("hub");
								moderatingPlayers.put(player.getUniqueId(), minigame);
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now moderating the server.");
							}
						} else {
							Go.playLackPermissionMessage(player);
							return true;
						}
					} else if ((label.equals("vanish")) || (label.equals("v"))) {
						if (player.hasPermission("skorrloregaming.vanish")) {
							if (this.vanishedPlayers.containsKey(player.getUniqueId())) {
								VanishInfo iVanish = (VanishInfo) this.vanishedPlayers.get(player.getUniqueId());
								player.getInventory().setContents(iVanish.getContents());
								player.setGameMode(iVanish.getGameMode());
								this.vanishedPlayers.remove(player.getUniqueId());
								for (Player p : Bukkit.getOnlinePlayers()) {
									p.showPlayer(player);
								}
								if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
									player.removePotionEffect(PotionEffectType.INVISIBILITY);
								}
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are no longer vanished.");
							} else {
								this.vanishedPlayers.put(player.getUniqueId(), new VanishInfo(player.getInventory().getContents(), player.getGameMode()));
								for (Player p : getServer().getOnlinePlayers()) {
									p.hidePlayer(player);
								}
								if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
									player.removePotionEffect(PotionEffectType.INVISIBILITY);
								}
								player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 72000, 0));
								player.setGameMode(GameMode.SPECTATOR);
								Go.clearPlayer(player);
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now vanished.");
							}
						} else {
							Go.playLackPermissionMessage(player);
							return true;
						}
					} else if ((label.equals("workbench")) || (label.equals("wb")) || (label.equals("craft"))) {
						player.openWorkbench(null, isEnabled());
					} else if (label.equals("sign-edit")) {
						if (!player.hasPermission("skorrloregaming.signedit")) {
							Go.playLackPermissionMessage(player);
							return true;
						}
						String tag = Go.getMinigameTag(player);
						if (args.length < 2) {
							player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <line> <text>");
						} else {
							StringBuilder sb = new StringBuilder();
							for (int i = 1; i < args.length; i++) {
								sb.append(args[i] + " ");
							}
							String text = sb.toString().trim();
							if (this.signEditParam.containsKey(player)) {
								this.signEditParam.put(player, new SignInfo(Integer.parseInt(args[0]), text));
								player.sendMessage(tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Sign edit parameters " + ChatColor.ITALIC + "updated in memory" + ChatColor.RESET + ChatColor.GRAY + ".");
								player.sendMessage(tag + ChatColor.GRAY + "Right click on the sign that you would like to edit.");
							} else {
								this.signEditParam.put(player, new SignInfo(Integer.parseInt(args[0]), text));
								player.sendMessage(tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Sign edit parameters " + ChatColor.ITALIC + "saved to memory" + ChatColor.RESET + ChatColor.GRAY + ".");
								player.sendMessage(tag + ChatColor.GRAY + "Right click on the sign that you would like to edit.");
							}
						}
					} else if ((label.equals("dispose")) || (label.equals("trash"))) {
						if (inCombat.containsKey(player.getUniqueId())) {
							player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
							return true;
						}
						Inventory inv = Bukkit.createInventory(null, 27);
						player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
						player.openInventory(inv);
					} else if ((label.equals("chest")) || (label.equals("pv")) || (label.equals("echest")) || (label.equals("enderchest"))) {
						if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId()))) {
							player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
							return true;
						}
						if (inCombat.containsKey(player.getUniqueId())) {
							player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
							return true;
						}
						String subDomain = Go.getMinigameDomain(player);
						if (this.saveOtherInventory.containsKey(player)) {
							this.saveOtherInventory.remove(player);
						}
						int chestNumber = -1;
						if ((args.length > 0) &&
								(Bukkit.getPlayer(args[0]) == null)) {
							try {
								chestNumber = Math.abs(Integer.parseInt(args[0]));
							} catch (Exception localException1) {
							}
						}
						if ((args.length == 0) || (chestNumber > -1) || ((Bukkit.getPlayer(args[0]) != null) && (Bukkit.getPlayer(args[0]).getName().equals(player.getName())))) {
							Inventory inv = SolidStorage.restorePersonalChest(player, subDomain, true);
							player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
							player.openInventory(inv);
						} else {
							Player tp = Bukkit.getPlayer(args[0]);
							if (tp == null) {
								player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
							} else {
								boolean hasControl = false;
								if (player.hasPermission("skorrloregaming.chest")) {
									hasControl = true;
								}
								String tpSubDomain = Go.getMinigameDomain(player);
								Inventory inv = SolidStorage.restorePersonalChest(tp, tpSubDomain, hasControl);
								if (hasControl) {
									this.savePersonalChest.put(player, tp);
								}
								player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
								player.openInventory(inv);
							}
						}
					} else {
						Inventory inv;
						if ((label.equals("inventorysee")) || (label.equals("invsee"))) {
							if (inCombat.containsKey(player.getUniqueId())) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
								return true;
							}
							if (args.length == 0) {
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
							} else {
								Player tp = Bukkit.getPlayer(args[0]);
								if (tp == null) {
									player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
								} else {
									String name = ChatColor.BOLD + "Temporary Inventory";
									if (player.hasPermission("skorrloregaming.inventorysee")) {
										name = ChatColor.BOLD + "Personal Inventory";
									}
									inv = Bukkit.createInventory(null, 45, name);
									inv.setContents(tp.getInventory().getContents());
									if (name.equals(ChatColor.BOLD + "Personal Inventory")) {
										if (this.savePersonalChest.containsKey(player)) {
											this.savePersonalChest.remove(player);
										}
										this.saveOtherInventory.put(player, tp);
									}
									player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
									player.openInventory(inv);
								}
							}
						} else if ((label.equals("ping")) || (label.equals("latency"))) {
							if (args.length == 0) {
								int ping = CraftGo.Player.getConnectionLatency(player);
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Connection latency (ping): " + ChatColor.RED + ping);
							} else {
								Player targetPlayer = Bukkit.getPlayer(args[0]);
								if (targetPlayer == null) {
									player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
									return true;
								}
								int ping = CraftGo.Player.getConnectionLatency(targetPlayer);
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + targetPlayer.getName());
								player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Connection latency (ping): " + ChatColor.RED + ping);
							}
						} else if (label.equals("activate-shop")) {
							if (!player.hasPermission("skorrloregaming.activateshop")) {
								Go.playLackPermissionMessage(player);
								return true;
							}
							ItemStack item = Go.createMaterial(Material.STICK, ChatColor.LIGHT_PURPLE + "Activation Wand");
							item = Go.addLore(item, new String[]{
									"Activation Wand"});
							player.getInventory().addItem(new ItemStack[]{item});
						} else if ((label.equals("hub")) || (label.equals("lobby"))) {
							if (inCombat.containsKey(player.getUniqueId())) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
								return true;
							}
							ServerMinigame minigame = Go.getCurrentMinigame(player);
							boolean perform = false;
							if (performBuggedLeave(player, false, false) > 0) {
								perform = true;
							} else if ((minigame == ServerMinigame.HUB) || (minigame == ServerMinigame.UNKNOWN)) {
								perform = true;
							}
							if (perform) {
								if (!hub.contains(player.getUniqueId())) {
									hub.add(player.getUniqueId());
								}
								player.teleport(Go.getZoneLocation("hub", this));
								fetchLobby(player);
							}
						} else if ((label.equals("suicide")) || (label.equals("kill"))) {
							if (inCombat.containsKey(player.getUniqueId())) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
								return true;
							}
							player.setHealth(0.0D);
						} else if (label.equals("tpa")) {
							if ((!creative.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId())) && (!kitpvp.contains(player.getUniqueId()))) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
								return true;
							}
							if (inCombat.containsKey(player.getUniqueId())) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
								return true;
							}
							String tag = Go.getMinigameTag(player);
							if (args.length == 0) {
								player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
							} else {
								Player targetPlayer = getServer().getPlayer(args[0]);
								if (targetPlayer == null) {
									player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player could not be found.");
								} else if (Go.getCurrentMinigame(targetPlayer) == Go.getCurrentMinigame(player)) {
									if (this.tpaRequests.containsKey(player.getUniqueId())) {
										this.tpaRequests.remove(player.getUniqueId());
									}
									this.tpaRequests.put(player.getUniqueId(), targetPlayer.getUniqueId());
									player.sendMessage(tag + ChatColor.WHITE + "Teleport request sent to " + ChatColor.RED + targetPlayer.getName());
									targetPlayer.sendMessage(tag + ChatColor.WHITE + "Teleport request received from " + ChatColor.RED + player.getName() + ChatColor.WHITE + ".");
									targetPlayer.sendMessage(tag + ChatColor.WHITE + "To accept this request, type " + ChatColor.RED + "/tpaccept" + ChatColor.WHITE + " in chat.");
								} else {
									player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player is in another minigame.");
								}
							}
						} else if ((label.equals("tpaccept")) || (label.equals("tpyes"))) {
							if ((!creative.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId())) && (!kitpvp.contains(player.getUniqueId()))) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
								return true;
							}
							if (inCombat.containsKey(player.getUniqueId())) {
								player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
								return true;
							}
							String tag = Go.getMinigameTag(player);
							if (!this.tpaRequests.containsValue(player.getUniqueId())) {
								player.sendMessage(tag + ChatColor.RED + "You have not received any teleport requests recently.");
							} else {
								for (Map.Entry<UUID, UUID> id : this.tpaRequests.entrySet()) {
									if (((UUID) id.getValue()).equals(player.getUniqueId())) {
										this.tpaRequests.remove(id.getKey());
										Player targetPlayer = getServer().getPlayer((UUID) id.getKey());
										if (targetPlayer == null) {
											player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player could not be found.");
										} else if (Go.getCurrentMinigame(targetPlayer) == Go.getCurrentMinigame(player)) {
											Location zoneLocation = player.getLocation();
											DelayedTeleport dt = new DelayedTeleport(targetPlayer, 6, zoneLocation, this, Boolean.valueOf(false));
											dt.runTaskTimer(this, 4L, 4L);
											player.sendMessage(tag + ChatColor.WHITE + "Teleportation request accepted.");
											targetPlayer.sendMessage(tag + ChatColor.RED + player.getName() + ChatColor.WHITE + " has accepted your teleport request.");
										} else {
											player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player is in another minigame.");
										}
									}
								}
							}
						} else {
							if (label.equals("home")) {
								if ((!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId()))) {
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
									return true;
								}
								if (inCombat.containsKey(player.getUniqueId())) {
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
									return true;
								}
								ConfigurationManager config = null;
								if (factions.contains(player.getUniqueId())) {
									config = factionsConfig;
								} else if (survival.contains(player.getUniqueId())) {
									config = survivalConfig;
								}
								if (!config.getData().contains("homes." + player.getUniqueId().toString())) {
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You have not yet set a home on this server.");
								} else {
									String base = "homes." + player.getUniqueId().toString();
									World world = getServer().getWorld(config.getData().getString(base + ".world"));
									double x = config.getData().getDouble(base + ".x");
									double y = config.getData().getDouble(base + ".y");
									double z = config.getData().getDouble(base + ".z");
									float yaw = (float) config.getData().getDouble(base + ".yaw");
									float pitch = (float) config.getData().getDouble(base + ".pitch");
									Location homeLocation = new Location(world, x, y, z, yaw, pitch);
									DelayedTeleport dt = new DelayedTeleport(player, 6, homeLocation, this, Boolean.valueOf(false));
									dt.runTaskTimer(this, 4L, 4L);
								}
							} else if (label.equals("delhome")) {
								if ((!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId()))) {
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
									return true;
								}
								if (inCombat.containsKey(player.getUniqueId())) {
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
									return true;
								}
								ConfigurationManager config = null;
								if (factions.contains(player.getUniqueId())) {
									config = factionsConfig;
								} else if (survival.contains(player.getUniqueId())) {
									config = survivalConfig;
								}
								if (!config.getData().contains("homes." + player.getUniqueId().toString())) {
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You have not yet set a home on this server.");
								} else {
									config.getData().set("homes." + player.getUniqueId().toString(), null);
									config.saveData();
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have unset your home on this server.");
								}
							} else {
								World world;
								double y;
								double z;
								if (label.equals("sethome")) {
									if ((!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId()))) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
										return true;
									}
									if (inCombat.containsKey(player.getUniqueId())) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
										return true;
									}
									ConfigurationManager config = null;
									if (factions.contains(player.getUniqueId())) {
										config = factionsConfig;
									} else if (survival.contains(player.getUniqueId())) {
										config = survivalConfig;
									}
									String base = "homes." + player.getUniqueId().toString();
									world = player.getWorld();
									double x = player.getLocation().getX();
									y = player.getLocation().getY();
									z = player.getLocation().getZ();
									float yaw = player.getLocation().getYaw();
									float pitch = player.getLocation().getPitch();
									config.getData().set(base + ".world", world.getName());
									config.getData().set(base + ".x", Double.valueOf(x));
									config.getData().set(base + ".y", Double.valueOf(y));
									config.getData().set(base + ".z", Double.valueOf(z));
									config.getData().set(base + ".yaw", Integer.valueOf((int) yaw));
									config.getData().set(base + ".pitch", Integer.valueOf((int) pitch));
									config.saveData();
									player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have set your home on this server.");
								} else if (label.equals("shop")) {
									if (!factions.contains(player.getUniqueId())) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
										return true;
									}
									if (inCombat.containsKey(player.getUniqueId())) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
										return true;
									}
									Location zoneLocation = Go.getZoneLocation("factions-shop", this);
									DelayedTeleport dt = new DelayedTeleport(player, 6, zoneLocation, this, Boolean.valueOf(false));
									dt.runTaskTimerAsynchronously(this, 4L, 4L);
								} else if (label.equals("setzone")) {
									if (!player.hasPermission("skorrloregaming.setzone")) {
										Go.playLackPermissionMessage(player);
										return true;
									}
									if (args.length == 0) {
										player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
									} else {
										warpConfig.getData().set(args[0].toLowerCase() + ".world", player.getWorld().getName());
										warpConfig.getData().set(args[0].toLowerCase() + ".x", Double.valueOf(player.getLocation().getX()));
										warpConfig.getData().set(args[0].toLowerCase() + ".y", Double.valueOf(player.getLocation().getY()));
										warpConfig.getData().set(args[0].toLowerCase() + ".z", Double.valueOf(player.getLocation().getZ()));
										warpConfig.getData().set(args[0].toLowerCase() + ".yaw", Float.valueOf(player.getLocation().getYaw()));
										warpConfig.getData().set(args[0].toLowerCase() + ".pitch", Float.valueOf(player.getLocation().getPitch()));
										warpConfig.saveData();
										player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Zone successfully created named : " + ChatColor.RED + args[0].toLowerCase());
									}
								} else if (label.equals("warp")) {
									if (!player.hasPermission("skorrloregaming.warp")) {
										Go.playLackPermissionMessage(player);
										return true;
									}
									if (args.length == 0) {
										player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
									} else {
										Location zoneLocation = Go.getZoneLocation(args[0], this);
										if (zoneLocation != null) {
											player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Teleporting to " + ChatColor.RED + args[0] + ChatColor.GRAY + "..");
											player.teleport(zoneLocation);
										} else {
											player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "The warp " + ChatColor.RED + args[0] + ChatColor.GRAY + " does not exist.");
										}
									}
								} else if (label.equals("servers")) {
									String s1 = "";
									for (String str : Go.validMinigames) {
										s1 = s1 + ChatColor.ITALIC + WordUtils.capitalize(str) + ChatColor.RESET + ", ";
									}
									s1 = s1.substring(0, s1.lastIndexOf(", "));
									sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "You are currently connected to " + pluginName.toLowerCase() + ".");
									sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Servers: " + ChatColor.RESET + s1);
								} else if (label.equals("server")) {
									if (inCombat.containsKey(player.getUniqueId())) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
										return true;
									}
									if (args.length == 0) {
										player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Syntax " + ChatColor.GOLD + "/" + label + " <name>");
										player.performCommand("servers");
										return true;
									}
									if (args[0].equalsIgnoreCase("kitpvp")) {
										if (!Go.isMinigameEnabled(ServerMinigame.KITPVP, this)) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
											return true;
										}
										if (kitpvp.contains(player.getUniqueId())) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
											return true;
										}
										enterKitpvp(player, false, false);
									} else if (args[0].equalsIgnoreCase("factions")) {
										if (!Go.isMinigameEnabled(ServerMinigame.FACTIONS, this)) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
											return true;
										}
										if (factions.contains(player.getUniqueId())) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
											return true;
										}
										enterFactions(player, false, false);
									} else if (args[0].equalsIgnoreCase("survival")) {
										if (!Go.isMinigameEnabled(ServerMinigame.SURVIVAL, this)) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
											return true;
										}
										if (survival.contains(player.getUniqueId())) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
											return true;
										}
										enterSurvival(player, false, false);
									} else if (args[0].equalsIgnoreCase("skyfight")) {
										if (!Go.isMinigameEnabled(ServerMinigame.SKYFIGHT, this)) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
											return true;
										}
										if (skyfight.contains(player.getUniqueId())) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
											return true;
										}
										enterSkyfight(player, false, false);
									} else if (args[0].equalsIgnoreCase("creative")) {
										if (!Go.isMinigameEnabled(ServerMinigame.CREATIVE, this)) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
											return true;
										}
										if (creative.contains(player.getUniqueId())) {
											player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. You are already on that server!");
											return true;
										}
										enterCreative(player, false, false);
									} else {
										player.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + ChatColor.RED + "Error. That server could not be found.");
										player.performCommand("servers");
									}
								} else if (label.equals("spawn")) {
									if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId()))) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
										return true;
									}
									if (inCombat.containsKey(player.getUniqueId())) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
										return true;
									}
									String subDomain = Go.getMinigameDomain(player);
									Location zoneLocation = Go.getZoneLocation(subDomain, this);
									DelayedTeleport dt = new DelayedTeleport(player, 6, zoneLocation, this, Boolean.valueOf(false));
									dt.runTaskTimerAsynchronously(this, 4L, 4L);
								} else if ((label.equals("reply")) || (label.equals("r"))) {
									if (args.length == 0) {
										player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <message>");
										return true;
									}
									if (!this.messageRequests.containsKey(player.getUniqueId())) {
										player.sendMessage(Go.Legacy.tag + ChatColor.RED + "You have not messaged anyone on the server recently.");
										return true;
									}
									Player targetPlayer = getServer().getPlayer((UUID) this.messageRequests.get(player.getUniqueId()));
									if (targetPlayer == null) {
										player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
										return true;
									}
									StringBuilder sb = new StringBuilder();
									for (int i = 0; i < args.length; i++) {
										sb.append(args[i] + " ");
									}
									int rank = Go.getRankId(player, this);
									String message = sb.toString();
									if ((player.hasPermission("skorrloregaming.events.chatcolor")) || (rank > -1) || (rank < -2)) {
										message = ChatColor.translateAlternateColorCodes('&', message);
									}
									player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "me" + ChatColor.WHITE + " ? " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "] " + message);
									targetPlayer.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + player.getName() + ChatColor.WHITE + " ? " + ChatColor.RED + "me" + ChatColor.WHITE + "] " + message);
									this.messageRequests.put(player.getUniqueId(), targetPlayer.getUniqueId());
								} else if ((label.equals("msg")) || (label.equals("message")) || (label.equals("tell")) || (label.equals("w")) || (label.equals("pm"))) {
									if (args.length < 2) {
										player.sendMessage(Go.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <message>");
										return true;
									}
									Player targetPlayer = getServer().getPlayer(args[0]);
									if (targetPlayer == null) {
										player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
										return true;
									}
									StringBuilder sb = new StringBuilder();
									for (int i = 1; i < args.length; i++) {
										sb.append(args[i] + " ");
									}
									int rank = Go.getRankId(player, this);
									String message = sb.toString();
									if ((player.hasPermission("skorrloregaming.events.chatcolor")) || (rank > -1) || (rank < -2)) {
										message = ChatColor.translateAlternateColorCodes('&', message);
									}
									message = anticheat.processAntiSwear(player, message);
									player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "me" + ChatColor.WHITE + " ? " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "] " + message);
									targetPlayer.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + player.getName() + ChatColor.WHITE + " ? " + ChatColor.RED + "me" + ChatColor.WHITE + "] " + message);
									this.messageRequests.put(player.getUniqueId(), targetPlayer.getUniqueId());
									this.messageRequests.put(targetPlayer.getUniqueId(), player.getUniqueId());
									targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
									for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
										if ((!onlinePlayer.getName().equals(player.getName())) && (!onlinePlayer.getName().equals(targetPlayer.getName()))) {
											int rankID = Go.getRankId(onlinePlayer, this);
											if ((onlinePlayer.hasPermission("skorrloregaming.events.chatlisten")) || (rankID > -1)) {
												onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0F, 1.0F);
											}
										}
									}
								} else if (label.equals("stats")) {
									if (!kitpvp.contains(player.getUniqueId())) {
										player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
										return true;
									}
									Player targetPlayer = player;
									if (args.length > 0) {
										targetPlayer = getServer().getPlayer(args[0]);
										if (targetPlayer == null) {
											player.sendMessage(Go.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
											return true;
										}
									}
									int currentPlayerKills = Go.Kitpvp.getPlayerKills(targetPlayer, this);
									int currentPlayerDeaths = Go.Kitpvp.getPlayerDeaths(targetPlayer, this);
									int currentPlayerDPK = currentPlayerKills / 50;
									int currentPlayerCash = EconManager.retrieveCash(targetPlayer, "kitpvp", this);
									String tag = Go.getMinigameTag("kitpvp");
									player.sendMessage(tag + ChatColor.GRAY + "/ Statistics for player " + ChatColor.RED + targetPlayer.getName());
									player.sendMessage(tag + ChatColor.GRAY + "Current Balance: " + ChatColor.RED + "$" + currentPlayerCash);
									player.sendMessage(tag + ChatColor.GRAY + "Current Kills: " + ChatColor.RED + currentPlayerKills);
									player.sendMessage(tag + ChatColor.GRAY + "Current Deaths: " + ChatColor.RED + currentPlayerDeaths);
									player.sendMessage(tag + ChatColor.GRAY + "Current Level: " + ChatColor.RED + (currentPlayerDPK + 1));
								} else {
									if (label.equals("pay")) {
										if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId()))) {
											player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
											return true;
										}
										String subDomain = Go.getMinigameDomain(player);
										String tag = Go.getMinigameTag(subDomain);
										if (args.length != 2) {
											player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <amount>");
											return true;
										}
										Player targetPlayer = Bukkit.getPlayer(args[0]);
										if (targetPlayer == null) {
											player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
											return true;
										}
										int amount = Integer.parseInt(args[1]);
										if (player.hasPermission("skorrloregaming.pay")) {
											EconManager.depositCash(targetPlayer, amount, subDomain, this);
											player.sendMessage(tag + ChatColor.GRAY + "You have given " + ChatColor.RED + "$" + amount + ChatColor.GRAY + " to " + ChatColor.RED + targetPlayer.getName());
											targetPlayer.sendMessage(tag + ChatColor.GRAY + "You have been payed " + ChatColor.RED + "$" + amount + ChatColor.GRAY + ", by " + ChatColor.RED + player.getName());
											return true;
										}
										amount = Math.abs(amount);
										int currentPlayerCash = EconManager.retrieveCash(player, subDomain, this);
										if (currentPlayerCash >= amount - 1) {
											EconManager.withdrawCash(player, amount, subDomain, this);
											EconManager.depositCash(targetPlayer, amount, subDomain, this);
											player.sendMessage(tag + ChatColor.GRAY + "You have given " + ChatColor.RED + "$" + amount + ChatColor.GRAY + " to " + ChatColor.RED + targetPlayer.getName());
											targetPlayer.sendMessage(tag + ChatColor.GRAY + "You have been payed " + ChatColor.RED + "$" + amount + ChatColor.GRAY + ", by " + ChatColor.RED + player.getName());
											return true;
										}
										player.sendMessage(tag + ChatColor.GRAY + "You do not have enough money to fulfill the transaction.");
										return true;
									}
									if ((label.equals("balancetop")) || (label.equals("baltop"))) {
										if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId()))) {
											player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
											return true;
										}
										int minimumValue = 0;
										int maximumValue = 8;
										int pageNumber = 1;
										if (args.length > 0) {
											try {
												int value = Integer.parseInt(args[0]);
												minimumValue = 8 * value - 8;
												maximumValue = 8 * value;
												pageNumber = value;
											} catch (Exception ig) {
												minimumValue = 0;
												maximumValue = 8;
												pageNumber = 1;
											}
										}
										String subDomain = Go.getMinigameDomain(player);
										String minigameTag = Go.getMinigameTag(player);
										ConcurrentMap<Double, UUID> array1 = new ConcurrentHashMap<>();
										for (String uuid : getConfig().getConfigurationSection("config").getKeys(false)) {
											if (uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")) {
												double money = EconManager.retrieveCash(UUID.fromString(uuid), subDomain, this);
												if (money > 0.0D) {
													UUID id = UUID.fromString(uuid);
													while (array1.containsKey(Double.valueOf(money))) {
														money += 1.0E-5D;
													}
													array1.put(Double.valueOf(money), id);
												}
											}
										}
										double[] valueArray = Go.sortNumericallyReverse(ArrayUtils.toPrimitive((Double[]) array1.keySet().toArray(new Double[0])));
										int serverTotal = 0;
										int homeLocation = valueArray.length;
										for (int i = 0; i < homeLocation; i++) {
											double value = valueArray[i];
											serverTotal = (int) Math.floor(serverTotal + value);
										}
										sender.sendMessage(minigameTag + ChatColor.GRAY + "Server total: " + ChatColor.RED + "$" + serverTotal);
										for (int i = minimumValue; i < maximumValue; i++) {
											double value = 0.0D;
											if (i < valueArray.length) {
												value = valueArray[i];
											}
											OfflinePlayer op = null;
											if (array1.containsKey(Double.valueOf(value))) {
												op = Bukkit.getOfflinePlayer((UUID) array1.get(Double.valueOf(value)));
											}
											if ((op != null) && (value > 0.0D)) {
												value = Math.floor(value);
												sender.sendMessage(minigameTag + ChatColor.ITALIC + "(" + (i + 1) + ") " + ChatColor.RESET + ChatColor.RED + op.getName() + ChatColor.GRAY + ": " + ChatColor.RED + "$" + value);
											}
										}
										sender.sendMessage(minigameTag + ChatColor.GRAY + "Type " + ChatColor.RED + "/" + label + " " + (pageNumber + 1) + ChatColor.GRAY + " to read the next page.");
									} else if ((label.equals("balance")) || (label.equals("bal"))) {
										if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId()))) {
											player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
											return true;
										}
										if (args.length == 0) {
											String subDomain = Go.getMinigameDomain(player);
											String tag = Go.getMinigameTag(subDomain);
											sender.sendMessage(tag + ChatColor.GRAY + "Current Balance: " + ChatColor.RED + "$" + EconManager.retrieveCash(player, subDomain, this));
										} else {
											Player targetPlayer = Bukkit.getPlayer(args[0]);
											if (targetPlayer == null) {
												sender.sendMessage(ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
											} else {
												String subDomain = Go.getMinigameDomain(player);
												String tag = Go.getMinigameTag(subDomain);
												sender.sendMessage(tag + ChatColor.GRAY + "Balance of " + ChatColor.RED + targetPlayer.getName() + ChatColor.GRAY + ": " + ChatColor.RED + EconManager.retrieveCash(targetPlayer, subDomain, this));
											}
										}
									} else {
										if (label.equals("kits")) {
											if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId()))) {
												player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
												return true;
											}
											String tag = Go.getMinigameTag(player);
											if (survival.contains(player.getUniqueId())) {
												String s = "";
												for (int i = 0; i < Go.Survival.validKits.length; i++) {
													String str = Go.Survival.validKits[i];
													s = s + ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
												}
												s = s.substring(0, s.lastIndexOf(", "));
												sender.sendMessage(tag + ChatColor.GRAY + "Kits: " + ChatColor.RED + s);
												return true;
											}
											if (factions.contains(player.getUniqueId())) {
												String s = "";
												for (int i = 0; i < Go.Factions.validKits.length; i++) {
													String str = Go.Factions.validKits[i];
													s = s + ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
												}
												s = s.substring(0, s.lastIndexOf(", "));
												sender.sendMessage(tag + ChatColor.GRAY + "Kits: " + ChatColor.RED + s);
												return true;
											}
											if (kitpvp.contains(player.getUniqueId())) {
												String s = "";
												for (int i = 0; i < Go.Kitpvp.validKits.length; i++) {
													String str = Go.Kitpvp.validKits[i];
													s = s + ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
												}
												s = s.substring(0, s.lastIndexOf(", "));
												sender.sendMessage(tag + ChatColor.GRAY + "Kits: " + ChatColor.RED + s);
												return true;
											}
										} else if (label.equals("kit")) {
											if ((!kitpvp.contains(player.getUniqueId())) && (!factions.contains(player.getUniqueId())) && (!survival.contains(player.getUniqueId()))) {
												player.sendMessage(Go.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
												return true;
											}
											final String tag = Go.getMinigameTag(player);
											final Plugin fPlugin = this;
											final Player fPlayer = player;
											if (survival.contains(player.getUniqueId())) {
												if (args.length == 0) {
													player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
													player.performCommand("kits");
													return true;
												}
												if (args[0].equalsIgnoreCase("recruit")) {
													if (this.recruitKitCooldownSurvival.containsKey(player.getUniqueId())) {
														player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + this.recruitKitCooldownSurvival.get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
														return true;
													}
													for (int i = 0; i < Go.Survival.kitRecruit.length; i++) {
														ItemStack item = Go.Survival.kitRecruit[i];
														player.getInventory().addItem(new ItemStack[]{item});
													}
													player.updateInventory();
													if (!this.recruitKitCooldownSurvival.containsKey(fPlayer.getUniqueId())) {
														this.recruitKitCooldownSurvival.put(fPlayer.getUniqueId(), Integer.valueOf(300));
													}
													new BukkitRunnable() {
														int time = 300;

														public void run() {
															this.time -= 1;
															Server.this.recruitKitCooldownSurvival.put(fPlayer.getUniqueId(), Integer.valueOf(this.time));
															if (this.time <= 0) {
																Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
																if (checkPlayer != null) {
																	checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
																}
																if (Server.this.recruitKitCooldownSurvival.containsKey(fPlayer.getUniqueId())) {
																	Server.this.recruitKitCooldownSurvival.remove(fPlayer.getUniqueId());
																}
																cancel();
															}
														}
													}.runTaskTimer(this, 20L, 20L);
													player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
												} else {
													player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
													player.performCommand("kits");
													return true;
												}
												return true;
											}
											if (factions.contains(player.getUniqueId())) {
												if (args.length == 0) {
													player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
													player.performCommand("kits");
													return true;
												}
												if (args[0].equalsIgnoreCase("recruit")) {
													if (this.recruitKitCooldownFactions.containsKey(player.getUniqueId())) {
														player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + this.recruitKitCooldownFactions.get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
														return true;
													}
													for (int i = 0; i < Go.Factions.kitRecruit.length; i++) {
														ItemStack item = Go.Factions.kitRecruit[i];
														player.getInventory().addItem(new ItemStack[]{item});
													}
													player.updateInventory();
													if (!this.recruitKitCooldownFactions.containsKey(fPlayer.getUniqueId())) {
														this.recruitKitCooldownFactions.put(fPlayer.getUniqueId(), Integer.valueOf(300));
													}
													new BukkitRunnable() {
														int time = 300;

														public void run() {
															this.time -= 1;
															Server.this.recruitKitCooldownFactions.put(fPlayer.getUniqueId(), Integer.valueOf(this.time));
															if (this.time <= 0) {
																Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
																if (checkPlayer != null) {
																	checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Recruit");
																}
																if (Server.this.recruitKitCooldownFactions.containsKey(fPlayer.getUniqueId())) {
																	Server.this.recruitKitCooldownFactions.remove(fPlayer.getUniqueId());
																}
																cancel();
															}
														}
													}.runTaskTimer(this, 20L, 20L);
													player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Recruit");
												} else {
													player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
													player.performCommand("kits");
													return true;
												}
												return true;
											}
											if (kitpvp.contains(player.getUniqueId())) {
												if (args.length == 0) {
													player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
													player.performCommand("kits");
													return true;
												}
												if (args[0].equalsIgnoreCase("starter")) {
													int upgradeCount = Go.Kitpvp.getPreferredUpgrade(player, this);
													if ((this.starterKitCooldownKitpvp.containsKey(player.getUniqueId())) && (upgradeCount > 0)) {
														if (!simpleDelayedTask.contains(player.getUniqueId())) {
															simpleDelayedTask.add(player.getUniqueId());
															Bukkit.getScheduler().runTaskLater(this, new Runnable() {
																public void run() {
																	Server.simpleDelayedTask.remove(player.getUniqueId());
																}
															}, 20L);
															player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + this.starterKitCooldownKitpvp.get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
															player.sendMessage(tag + ChatColor.GRAY + "To use the default kit instead, use this kit again.");
															return true;
														}
														upgradeCount = 0;
													}
													for (int i = 0; i < Go.Kitpvp.getUpgradeClass(player, upgradeCount, false, this).length; i++) {
														ItemStack item = Go.Kitpvp.getUpgradeClass(player, upgradeCount, false, this)[i];
														player.getInventory().addItem(new ItemStack[]{item});
													}
													player.updateInventory();
													if (upgradeCount > 0) {
														if (!this.starterKitCooldownKitpvp.containsKey(fPlayer.getUniqueId())) {
															this.starterKitCooldownKitpvp.put(fPlayer.getUniqueId(), Integer.valueOf(60));
														}
														new BukkitRunnable() {
															int time = 60;

															public void run() {
																this.time -= 1;
																Server.this.starterKitCooldownKitpvp.put(fPlayer.getUniqueId(), Integer.valueOf(this.time));
																if (this.time <= 0) {
																	Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
																	if (checkPlayer != null) {
																		checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Starter");
																	}
																	if (Server.this.starterKitCooldownKitpvp.containsKey(fPlayer.getUniqueId())) {
																		Server.this.starterKitCooldownKitpvp.remove(fPlayer.getUniqueId());
																	}
																	cancel();
																}
															}
														}.runTaskTimer(this, 20L, 20L);
													}
													player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Starter #" + (upgradeCount + 1));
												} else if (args[0].equalsIgnoreCase("potions")) {
													if (this.potionsKitCooldownKitpvp.containsKey(player.getUniqueId())) {
														player.sendMessage(tag + ChatColor.GRAY + "You must wait " + ChatColor.RED + this.potionsKitCooldownKitpvp.get(player.getUniqueId()) + ChatColor.GRAY + " seconds before using that kit again.");
														return true;
													}
													for (int i = 0; i < Go.Kitpvp.kitPotions.length; i++) {
														ItemStack item = Go.Kitpvp.kitPotions[i];
														player.getInventory().addItem(new ItemStack[]{item});
													}
													player.updateInventory();
													if (!this.potionsKitCooldownKitpvp.containsKey(fPlayer.getUniqueId())) {
														this.potionsKitCooldownKitpvp.put(fPlayer.getUniqueId(), Integer.valueOf(300));
													}
													new BukkitRunnable() {
														int time = 300;

														public void run() {
															this.time -= 1;
															Server.this.potionsKitCooldownKitpvp.put(fPlayer.getUniqueId(), Integer.valueOf(this.time));
															if (this.time <= 0) {
																Player checkPlayer = fPlugin.getServer().getPlayer(fPlayer.getUniqueId());
																if (checkPlayer != null) {
																	checkPlayer.sendMessage(tag + ChatColor.GRAY + "You can now use the kit " + ChatColor.RED + "Potions");
																}
																if (Server.this.potionsKitCooldownKitpvp.containsKey(fPlayer.getUniqueId())) {
																	Server.this.potionsKitCooldownKitpvp.remove(fPlayer.getUniqueId());
																}
																cancel();
															}
														}
													}.runTaskTimer(this, 20L, 20L);
													player.sendMessage(tag + ChatColor.GRAY + "You have been given kit " + ChatColor.RED + "Potions");
												} else {
													player.sendMessage(tag + ChatColor.RED + "The specified kit could not be found.");
													player.performCommand("kits");
													return true;
												}
												return true;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}

