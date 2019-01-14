package me.skorrloregaming;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import me.skorrloregaming.commands.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.discord.DiscordBot;
import me.skorrloregaming.factions.auction.Auctioneer;
import me.skorrloregaming.factions.shop.LaShoppe;
import me.skorrloregaming.impl.*;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;

import me.skorrloregaming.CraftGo.BarApi;
import me.skorrloregaming.hooks.AuthMe_Listener;
import me.skorrloregaming.hooks.Factions_Listener;
import me.skorrloregaming.hooks.ProtocolLib_Listener;
import me.skorrloregaming.hooks.ProtocolSupportPocketStuff_Listener;
import me.skorrloregaming.hooks.ProtocolSupport_Listener;
import me.skorrloregaming.hooks.Votifier_Listener;
import me.skorrloregaming.hooks.mcMMO_Listener;
import me.skorrloregaming.impl.Switches.SwitchIntDouble;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import me.skorrloregaming.listeners.BlockEventHandler;
import me.skorrloregaming.listeners.EntityEventHandler;
import me.skorrloregaming.listeners.PlayerEventHandler;
import me.skorrloregaming.lockette.Lockette;
import me.skorrloregaming.ping.PingInjector;
import me.skorrloregaming.runnable.AutoBroadcaster;
import me.skorrloregaming.runnable.DelayedTeleport;
import me.skorrloregaming.runnable.GCandAutoDemotion;
import me.skorrloregaming.scoreboard.DisplayType;
import me.skorrloregaming.scoreboard.boards.Kitpvp_LeaderboardScoreboard;
import me.skorrloregaming.scoreboard.boards.Kitpvp_StatisticsScoreboard;
import me.skorrloregaming.skins.SkinStorage;

public class Server extends JavaPlugin implements Listener {

	public PerWorldPlugin perWorldPlugins;
	public Votifier nuVotifier;
	public TopVotersHttpServer topVotersHttpServer = null;
	public PingInjector pingInjector;
	public Lockette lockette = null;
	public ChatItem chatitem = null;
	public GCandAutoDemotion garbageCollector = null;

	private static ConfigurationManager ramConfig;
	private static ConfigurationManager warpConfig;
	private static ConfigurationManager signConfig;
	private static ConfigurationManager factionsConfig;
	private static ConfigurationManager factionsShoppeConfig;
	private static ConfigurationManager survivalConfig;
	private static ConfigurationManager marriageHomesConfig;
	private static ConfigurationManager npcConfig;
	private static ConfigurationManager spawnerConfig;
	private static ConfigurationManager banConfig;
	private static ConfigurationManager uuidCacheConfig;
	private static ConfigurationManager geolCacheConfig;
	private static ConfigurationManager monthlyVoteConfig;
	private static ConfigurationManager locketteConfig;
	private static ConfigurationManager chatitemConfig;

	private static Plugin plugin;
	private static Server instance;
	private static AntiCheat anticheat = null;
	private static SkinStorage skinStorage = null;
	private static PlaytimeManager playtimeManager = null;
	private static BarApi barApi = null;
	private static SessionManager sessionManager = null;
	private static Auctioneer auctioneer = null;
	private static LaShoppe shoppe = null;
	private static final int BASIC_TELEPORT_TIME = 3;

	private static ArrayList<UUID> hub = new ArrayList<>();
	private static ArrayList<UUID> kitpvp = new ArrayList<>();
	private static ArrayList<UUID> factions = new ArrayList<>();
	private static ArrayList<UUID> survival = new ArrayList<>();
	private static ConcurrentMap<UUID, $.Skyfight.Player> skyfight = new ConcurrentHashMap<>();
	private static ArrayList<UUID> creative = new ArrayList<>();
	private static ArrayList<UUID> skyblock = new ArrayList<>();

	private static String tempMotd = "/unspecified";
	private static ArrayList<UUID> simpleDelayedTask = new ArrayList<>();
	private static ConcurrentMap<UUID, Integer> delaySkinUpdate = new ConcurrentHashMap<>();
	private static ArrayList<UUID> confirmRepairShop = new ArrayList<>();
	private static ArrayList<UUID> confirmUnregisterNpc = new ArrayList<>();
	private static ArrayList<UUID> currentFellers = new ArrayList<>();
	private static ArrayList<String> disabledVersions = new ArrayList<String>();
	private static ArrayList<NpcPlayer> npcPlayers = new ArrayList<>();

	private static ConcurrentMap<UUID, Integer> potionsKitCooldownKitpvp = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> starterKitCooldownKitpvp = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> recruitKitCooldownSurvival = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> recruitKitCooldownFactions = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> donatorKitCooldownFactions = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> redstoneKitCooldownFactions = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> obsidianKitCooldownFactions = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> bedrockKitCooldownFactions = new ConcurrentHashMap<>();

	private static ConcurrentMap<UUID, UUID> tpaRequests = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, UUID> marriageRequests = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, UUID> messageRequests = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, VanishedInfo> vanishedPlayers = new ConcurrentHashMap<>();
	private static ConcurrentMap<Player, Player> savePersonalChest = new ConcurrentHashMap<>();
	private static ConcurrentMap<Player, Player> saveOtherInventory = new ConcurrentHashMap<>();
	private static ConcurrentMap<Player, SignInfo> signEditParam = new ConcurrentHashMap<>();
	private static ConcurrentMap<Integer, Integer> spawnerPrices = new ConcurrentHashMap<>();

	private static ConcurrentMap<UUID, Boolean> onlineMode = new ConcurrentHashMap<>();

	private static ArrayList<UUID> doubleJumpCandidates = new ArrayList<>();
	private static ArrayList<UUID> staffChatPlayers = new ArrayList<>();
	private static ArrayList<UUID> marriageChatPlayers = new ArrayList<>();
	private static ArrayList<UUID> spectatingPlayers = new ArrayList<>();
	private static ArrayList<UUID> mutedPlayers = new ArrayList<UUID>();
	private static ArrayList<UUID> opmePlayers = new ArrayList<UUID>();
	private static ArrayList<UUID> factionFlyPlayers = new ArrayList<UUID>();
	private static ConcurrentMap<String, Integer> timeSinceLastLogin = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, SwitchUUIDString> transferAcceptPlayers = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, DelayedTeleport> delayedTeleports = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, SwitchIntDouble> playersInCombat = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, ServerMinigame> moderatingPlayers = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, UUID> ignoredPlayers = new ConcurrentHashMap<>();

	private static String defaultJoinMessage = null;
	private static String defaultQuitMessage = null;

	private static String serverMotd = null;
	private static String pluginName = null;
	private static String pluginLabel;
	private static String lastKnownHubWorld;

	private static long serverStartTime = 0L;
	private static boolean pluginDebug = false;
	private static boolean ingameAnticheatDebug = true;

	private static boolean running = true;

	private static final long BASIC_INVENTORY_UPDATE_DELAY = 5L;
	private static final boolean USE_FACTIONS_AS_HUB = false;

	private static ConcurrentMap<UUID, Integer> hubScoreboardTitleIndex = new ConcurrentHashMap<>();
	private static ConcurrentMap<UUID, Integer> barApiTitleIndex = new ConcurrentHashMap<>();
	private static ConcurrentMap<Integer, Long> hideLoginMessage = new ConcurrentHashMap<>();

	private static ProtocolLib_Listener protoListener = null;
	private static AuthMe_Listener authListener = null;
	private static Factions_Listener factionsListener = null;
	private static ProtocolSupport_Listener protoSupportListener = null;
	private static ProtocolSupportPocketStuff_Listener protoSupportPocketApi = null;
	private static Votifier_Listener voteListener = null;
	private static mcMMO_Listener mcmmoListener = null;

	private static DiscordBot discordBot;

	public static ProtocolLib_Listener getProtoListener() {
		return protoListener;
	}

	public static AuthMe_Listener getAuthListener() {
		return authListener;
	}

	public static Factions_Listener getFactionsListener() {
		return factionsListener;
	}

	public static ProtocolSupport_Listener getProtoSupportListener() {
		return protoSupportListener;
	}

	public static ProtocolSupportPocketStuff_Listener getProtoSupportPocketApi() {
		return protoSupportPocketApi;
	}

	public static Votifier_Listener getVoteListener() {
		return voteListener;
	}

	public static mcMMO_Listener getMcmmoListener() {
		return mcmmoListener;
	}

	public static ConcurrentMap<UUID, Integer> getHubScoreboardTitleIndex() {
		return hubScoreboardTitleIndex;
	}

	public static ConcurrentMap<UUID, Integer> getBarApiTitleIndex() {
		return barApiTitleIndex;
	}

	public static ConcurrentMap<Integer, Long> getHideLoginMessage() {
		return hideLoginMessage;
	}

	public static long getServerStartTime() {
		return serverStartTime;
	}

	public static boolean getPluginDebug() {
		return pluginDebug;
	}

	public static void setPluginDebug(boolean enabled) {
		Server.pluginDebug = enabled;
	}

	public static boolean getIngameAnticheatDebug() {
		return ingameAnticheatDebug;
	}

	public static void setIngameAnticheatDebug(boolean enabled) {
		Server.ingameAnticheatDebug = enabled;
	}

	public static boolean isRunning() {
		return running;
	}

	public static long getInventoryUpdateDelay() {
		return BASIC_INVENTORY_UPDATE_DELAY;
	}

	public static boolean getUseFactionsAsHub() {
		return USE_FACTIONS_AS_HUB;
	}

	public static String getDefaultJoinMessage() {
		return defaultJoinMessage;
	}

	public static void setDefaultJoinMessage(String defaultJoinMessage) {
		Server.defaultJoinMessage = defaultJoinMessage;
	}

	public static String setDefaultQuitMessage() {
		return defaultQuitMessage;
	}

	public static void setDefaultQuitMessage(String defaultQuitMessage) {
		Server.defaultQuitMessage = defaultQuitMessage;
	}

	public static String getServerMotd() {
		return serverMotd;
	}

	public static String getPluginName() {
		return pluginName;
	}

	public static String getPluginLabel() {
		return pluginLabel;
	}

	public static String getLastKnownHubWorld() {
		return lastKnownHubWorld;
	}

	public static void setLastKnownHubWorld(String lastKnownHubWorld) {
		Server.lastKnownHubWorld = lastKnownHubWorld;
	}

	public static ConcurrentMap<String, Integer> getTimeSinceLastLogin() {
		return timeSinceLastLogin;
	}

	public static ConcurrentMap<UUID, SwitchUUIDString> getTransferAcceptPlayers() {
		return transferAcceptPlayers;
	}

	public static ConcurrentMap<UUID, DelayedTeleport> getDelayedTeleports() {
		return delayedTeleports;
	}

	public static ConcurrentMap<UUID, SwitchIntDouble> getPlayersInCombat() {
		return playersInCombat;
	}

	public static ConcurrentMap<UUID, ServerMinigame> getModeratingPlayers() {
		return moderatingPlayers;
	}

	public static ConcurrentMap<UUID, UUID> getIgnoredPlayers() {
		return ignoredPlayers;
	}

	public static ArrayList<UUID> getDoubleJumpCandidates() {
		return doubleJumpCandidates;
	}

	public static ArrayList<UUID> getStaffChatPlayers() {
		return staffChatPlayers;
	}

	public static ArrayList<UUID> getMarriageChatPlayers() {
		return marriageChatPlayers;
	}

	public static ArrayList<UUID> getSpectatingPlayers() {
		return spectatingPlayers;
	}

	public static ArrayList<UUID> getMutedPlayers() {
		return mutedPlayers;
	}

	public static ArrayList<UUID> getOpmePlayers() {
		return opmePlayers;
	}

	public static ArrayList<UUID> getFactionFlyPlayers() {
		return factionFlyPlayers;
	}

	public static ConcurrentMap<UUID, UUID> getTpaRequests() {
		return tpaRequests;
	}

	public static ConcurrentMap<UUID, UUID> getMarriageRequests() {
		return marriageRequests;
	}

	public static ConcurrentMap<UUID, UUID> getMessageRequests() {
		return messageRequests;
	}

	public static ConcurrentMap<UUID, VanishedInfo> getVanishedPlayers() {
		return vanishedPlayers;
	}

	public static ConcurrentMap<Player, Player> getSavePersonalChest() {
		return savePersonalChest;
	}

	public static ConcurrentMap<Player, Player> getSaveOtherInventory() {
		return saveOtherInventory;
	}

	public static ConcurrentMap<Player, SignInfo> getSignEditParam() {
		return signEditParam;
	}

	public static ConcurrentMap<Integer, Integer> getSpawnerPrices() {
		return spawnerPrices;
	}

	public static ConcurrentMap<UUID, Boolean> getOnlineMode() {
		return onlineMode;
	}

	public static class Kitpvp {

		public static ConcurrentMap<UUID, Integer> getPotionsKitCooldown() {
			return potionsKitCooldownKitpvp;
		}

		public static ConcurrentMap<UUID, Integer> getStarterKitCooldown() {
			return starterKitCooldownKitpvp;
		}

	}

	public static class Survival {

		public static ConcurrentMap<UUID, Integer> getRecruitKitCooldown() {
			return recruitKitCooldownSurvival;
		}

	}

	public static class Factions {

		public static ConcurrentMap<UUID, Integer> getRecruitKitCooldown() {
			return recruitKitCooldownFactions;
		}

		public static ConcurrentMap<UUID, Integer> getDonatorKitCooldown() {
			return donatorKitCooldownFactions;
		}

		public static ConcurrentMap<UUID, Integer> getRedstoneKitCooldown() {
			return redstoneKitCooldownFactions;
		}

		public static ConcurrentMap<UUID, Integer> getObsidianKitCooldown() {
			return obsidianKitCooldownFactions;
		}

		public static ConcurrentMap<UUID, Integer> getBedrockKitCooldown() {
			return bedrockKitCooldownFactions;
		}

	}

	public static Server getInstance() {
		return instance;
	}

	public static String getTempMotd() {
		return tempMotd;
	}

	public static void setTempMotd(String tempMotd) {
		Server.tempMotd = tempMotd;
	}

	public static ArrayList<UUID> getDelayedTasks() {
		return simpleDelayedTask;
	}

	public static ConcurrentMap<UUID, Integer> getDelaySkinUpdate() {
		return delaySkinUpdate;
	}

	public static ArrayList<UUID> getConfirmRepairShop() {
		return confirmRepairShop;
	}

	public static ArrayList<UUID> getConfirmUnregisterNpc() {
		return confirmUnregisterNpc;
	}

	public static ArrayList<UUID> getCurrentFellers() {
		return currentFellers;
	}

	public static ArrayList<String> getDisabledVersions() {
		return disabledVersions;
	}

	public static ArrayList<NpcPlayer> getNpcPlayers() {
		return npcPlayers;
	}

	public static ArrayList<UUID> getHub() {
		return hub;
	}

	public static ArrayList<UUID> getKitpvp() {
		return kitpvp;
	}

	public static ArrayList<UUID> getFactions() {
		return factions;
	}

	public static ArrayList<UUID> getSurvival() {
		return survival;
	}

	public static ConcurrentMap<UUID, $.Skyfight.Player> getSkyfight() {
		return skyfight;
	}

	public static ArrayList<UUID> getCreative() {
		return creative;
	}

	public static ArrayList<UUID> getSkyblock() {
		return skyblock;
	}

	public static ConfigurationManager getRamConfig() {
		return ramConfig;
	}

	public static ConfigurationManager getWarpConfig() {
		return warpConfig;
	}

	public static ConfigurationManager getSignConfig() {
		return signConfig;
	}

	public static ConfigurationManager getFactionsConfig() {
		return factionsConfig;
	}

	public static ConfigurationManager getFactionsShoppeConfig() {
		return factionsShoppeConfig;
	}

	public static ConfigurationManager getSurvivalConfig() {
		return survivalConfig;
	}

	public static ConfigurationManager getMarriageHomesConfig() {
		return marriageHomesConfig;
	}

	public static ConfigurationManager getNpcConfig() {
		return npcConfig;
	}

	public static ConfigurationManager getSpawnerConfig() {
		return spawnerConfig;
	}

	public static ConfigurationManager getBanConfig() {
		return banConfig;
	}

	public static ConfigurationManager getUUIDCache() {
		return uuidCacheConfig;
	}

	public static ConfigurationManager getGeolocationCache() {
		return geolCacheConfig;
	}

	public static ConfigurationManager getMonthlyVoteConfig() {
		return monthlyVoteConfig;
	}

	public static ConfigurationManager getLocketteConfig() {
		return locketteConfig;
	}

	public static ConfigurationManager getChatItemConfig() {
		return chatitemConfig;
	}

	public static AntiCheat getAntiCheat() {
		return anticheat;
	}

	public static SkinStorage getSkinStorage() {
		return skinStorage;
	}

	public static PlaytimeManager getPlaytimeManager() {
		return playtimeManager;
	}

	public static BarApi getBarApi() {
		return barApi;
	}

	public static Auctioneer getAuctioneer() {
		return auctioneer;
	}

	public static LaShoppe getShoppe() {
		return shoppe;
	}

	public static SessionManager getSessionManager() {
		return sessionManager;
	}

	public static int getTeleportationDelay() {
		return BASIC_TELEPORT_TIME;
	}

	public static DiscordBot getDiscordBot() {
		return discordBot;
	}

	@Override
	public void onLoad() {
		serverStartTime = System.currentTimeMillis();
		pluginName = this.getDescription().getName();
		pluginLabel = $.Legacy.tag;
		plugin = this;
		instance = this;
		String lineOne = ChatColor.RED + pluginName + ChatColor.GRAY + " has been running since late 2013.";
		String lineTwo = ChatColor.GRAY + "Server has been updated to support 1.13 clients.";
		serverMotd = lineOne + '\n' + lineTwo;
		anticheat = new AntiCheat();
		ramConfig = new ConfigurationManager();
		warpConfig = new ConfigurationManager();
		signConfig = new ConfigurationManager();
		factionsConfig = new ConfigurationManager();
		factionsShoppeConfig = new ConfigurationManager();
		survivalConfig = new ConfigurationManager();
		banConfig = new ConfigurationManager();
		npcConfig = new ConfigurationManager();
		spawnerConfig = new ConfigurationManager();
		marriageHomesConfig = new ConfigurationManager();
		uuidCacheConfig = new ConfigurationManager();
		geolCacheConfig = new ConfigurationManager();
		monthlyVoteConfig = new ConfigurationManager();
		locketteConfig = new ConfigurationManager();
		chatitemConfig = new ConfigurationManager();
		barApi = new BarApi();
		skinStorage = new SkinStorage();
		$.createDataFolder();
		warpConfig.setup(new File(this.getDataFolder(), "warps.yml"));
		signConfig.setup(new File(this.getDataFolder(), "shops.yml"));
		factionsConfig.setup(new File(this.getDataFolder(), "factions.yml"));
		factionsShoppeConfig.setup(new File(this.getDataFolder(), "factions_shoppe.yml"));
		survivalConfig.setup(new File(this.getDataFolder(), "survival.yml"));
		banConfig.setup(new File(this.getDataFolder(), "banned.yml"));
		npcConfig.setup(new File(this.getDataFolder(), "npc_storage.yml"));
		spawnerConfig.setup(new File(this.getDataFolder(), "spawners.yml"));
		marriageHomesConfig.setup(new File(this.getDataFolder(), "marriage_homes.yml"));
		uuidCacheConfig.setup(new File(this.getDataFolder(), "uuid_cache.yml"));
		geolCacheConfig.setup(new File(this.getDataFolder(), "geolocation_cache.yml"));
		monthlyVoteConfig.setup(new File(this.getDataFolder(), "monthly_votes.yml"));
		locketteConfig.setup(new File(this.getDataFolder(), "lockette_config.yml"));
		File chatItemConfig = new File(this.getDataFolder(), "chatitem_config.yml");
		if (!chatItemConfig.exists()) {
			try {
				FileUtils.copyInputStreamToFile(this.getResource("chatitem_config.yml"), chatItemConfig);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		chatitemConfig.setup(chatItemConfig);
		spawnerPrices.put(0, 6500);
		spawnerPrices.put(1, 6500);
		spawnerPrices.put(2, 9500);
		spawnerPrices.put(3, 9500);
		spawnerPrices.put(4, 34500);
		spawnerPrices.put(5, 9500);
		spawnerPrices.put(6, 4500);
		spawnerPrices.put(7, 3000);
		spawnerPrices.put(8, 3000);
		if (!$.isPluginLoaded("ploader") && !CraftGo.Minecraft.getPackageVersion().startsWith("v1_13")) {
			perWorldPlugins = new PerWorldPlugin();
			perWorldPlugins.onLoad();
		}
	}

	@Override
	public void onEnable() {
		discordBot = new DiscordBot(getPluginName(), getConfig().getString("settings.discordBot.token", "TOKEN"));
		discordBot.register();
		lockette = new Lockette();
		lockette.onEnable();
		nuVotifier = new Votifier();
		nuVotifier.onEnable();
		auctioneer = new Auctioneer();
		shoppe = new LaShoppe();
		garbageCollector = new GCandAutoDemotion();
		getConfig().options().copyDefaults(true);
		saveConfig();
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new BlockEventHandler(), this);
		getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);
		getServer().getPluginManager().registerEvents(new EntityEventHandler(), this);
		pingInjector = new PingInjector();
		if (getConfig().contains("settings.enable.pingInjector")) {
			if (getConfig().getBoolean("settings.enable.pingInjector"))
				pingInjector.register();
		} else {
			pingInjector.register();
		}
		lastKnownHubWorld = $.getZoneLocation("hub").getWorld().getName().toString();
		if ($.isPluginEnabled("Vault")) {
			new VaultEconomy().setupVault();
		}
		if (!getConfig().contains("settings.topVotersHttpServerPort")) {
			getConfig().set("settings.topVotersHttpServerPort", 2095);
		}
		if ($.isPluginEnabled("ProtocolLib")) {
			protoListener = new ProtocolLib_Listener();
			protoListener.register();
			chatitem = new ChatItem();
			chatitem.onEnable();
		}
		if ($.isPluginEnabled("mcMMO")) {
			mcmmoListener = new mcMMO_Listener();
			mcmmoListener.register();
		}
		if ($.isPluginEnabled("ProtocolSupportPocketStuff") && $.isPluginEnabled("AuthMe")) {
			protoSupportPocketApi = new ProtocolSupportPocketStuff_Listener();
			protoSupportPocketApi.bakeModals();
			protoSupportPocketApi.bakeCallbacks();
			protoSupportPocketApi.register();
		}
		if ($.isPluginEnabled("AuthMe")) {
			authListener = new AuthMe_Listener();
			authListener.register();
		}
		if ($.isPluginEnabled("ProtocolSupport")) {
			protoSupportListener = new ProtocolSupport_Listener();
			protoSupportListener.register();
		}
		voteListener = new Votifier_Listener();
		Bukkit.getScheduler().runTask(this, new Runnable() {
			@Override
			public void run() {
				if ($.isPluginEnabled("Factions")) {
					factionsListener = new Factions_Listener();
					factionsListener.register();
				}
				if (!$.isPluginLoaded("ploader") && !CraftGo.Minecraft.getPackageVersion().equals("v1_13_R1")) {
					if (perWorldPlugins != null)
						perWorldPlugins.onEnable();
				}
				for (Player player : Bukkit.getOnlinePlayers())
					player.sendMessage($.modernMsgPrefix + "Psst, did you know the server finished updating?");
				for (Player player : Bukkit.getOnlinePlayers()) {
					player.performCommand("hub");
					playtimeManager.handle_JoinEvent(player);
				}
				if (!(protoSupportListener == null)) {
					protoSupportListener.disableProtocolVersions();
				}
				List<Entity> entity = new LinkedList<Entity>($.getZoneLocation("creative").getWorld().getEntities());
				for (Entity e : entity) {
					if (e instanceof Item)
						e.remove();
				}
			}
		});
		anticheat.register();
		playtimeManager = new PlaytimeManager();
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				plugin.saveConfig();
			}
		}, 0L, 1200L);
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					try {
						SolidStorage.savePlayerData(player, $.getMinigameDomain(player));
					} catch (Exception e) {
					}
				}
			}
		}, 0L, 6000L);
		Bukkit.getScheduler().runTaskTimer(this, new AutoBroadcaster(), 6000L, 12000L);
		Bukkit.getScheduler().runTaskTimer(this, garbageCollector, 0L, 36000L);
		reload();
		barApi.onEnable();
		sessionManager = new SessionManager();
		sessionManager.setup();
		topVotersHttpServer = new TopVotersHttpServer(getConfig().getInt("settings.topVotersHttpServerPort", 2096));
		CustomRecipes.loadRecipes();
		getCommand("fly").setExecutor(new FlyCmd());
		getCommand("printblockstate").setExecutor(new PrintBlockStateCmd());
		getCommand("ignore").setExecutor(new IgnoreCmd());
		getCommand("logger").setExecutor(new LoggerCmd());
		getCommand("desync").setExecutor(new DesyncCmd());
		getCommand("sync").setExecutor(new SyncCmd());
		getCommand("spoof-vote").setExecutor(new SpoofVoteCmd());
		getCommand("buy").setExecutor(new BuyCmd());
		getCommand("playertime").setExecutor(new PlayertimeCmd());
		getCommand("sessions").setExecutor(new SessionsCmd());
		getCommand("transfer").setExecutor(new TransferCmd());
		getCommand("transferaccept").setExecutor(new TransferAcceptCmd());
		getCommand("transferdeny").setExecutor(new TransferDenyCmd());
		getCommand("warnings").setExecutor(new WarningsCmd());
		getCommand("vote").setExecutor(new VoteCmd());
		getCommand("update-skin").setExecutor(new UpdateSkinCmd());
		getCommand("spectate").setExecutor(new SpectateCmd());
		getCommand("discord").setExecutor(new DiscordCmd());
		getCommand("playtime-report").setExecutor(new PlaytimeReportCmd());
		getCommand("address").setExecutor(new AddressCmd());
		getCommand("rollback").setExecutor(new RollbackCmd());
		getCommand("setsuffixrank").setExecutor(new SetSuffixRankCmd());
		getCommand("center").setExecutor(new CenterCmd());
		getCommand("marrychat").setExecutor(new MarryCmd());
		getCommand("marry").setExecutor(new MarryCmd());
		getCommand("divorce").setExecutor(new DivorceCmd());
		getCommand("modifications").setExecutor(new ModificationsCmd());
		getCommand("spawner").setExecutor(new SpawnerCmd());
		getCommand("staffchat").setExecutor(new StaffChatCmd());
		getCommand("vote").setExecutor(new VoteCmd());
		getCommand("effect").setExecutor(new EffectCmd());
		getCommand("debug").setExecutor(new DebugCmd());
		getCommand("website").setExecutor(new WebsiteCmd());
		getCommand("trails").setExecutor(new TrailsCmd());
		getCommand("clear").setExecutor(new ClearCmd());
		getCommand("upgrade-kit").setExecutor(new UpgradeKitCmd());
		getCommand("spawn-npc").setExecutor(new SpawnNpcCmd());
		getCommand("remove-npc").setExecutor(new RemoveNpcCmd());
		getCommand("autologin").setExecutor(new AutoLoginCmd());
		getCommand("reset").setExecutor(new ResetCmd());
		getCommand("skull").setExecutor(new SkullCmd());
		getCommand("playtime").setExecutor(new PlaytimeCmd());
		getCommand("who").setExecutor(new WhoisCmd());
		getCommand("list").setExecutor(new ListCmd());
		getCommand("build-time").setExecutor(new BuildTimeCmd());
		getCommand("moderate").setExecutor(new ModerateCmd());
		getCommand("rules").setExecutor(new RulesCmd());
		getCommand("motd").setExecutor(new MotdCmd());
		getCommand("wild").setExecutor(new WildCmd());
		getCommand("give").setExecutor(new GiveCmd());
		getCommand("rename").setExecutor(new RenameCmd());
		getCommand("opme").setExecutor(new OpmeCmd());
		getCommand("vanish").setExecutor(new VanishCmd());
		getCommand("workbench").setExecutor(new WorkbenchCmd());
		getCommand("dispose").setExecutor(new DisposeCmd());
		getCommand("sign-edit").setExecutor(new SignEditCmd());
		getCommand("chest").setExecutor(new ChestCmd());
		getCommand("inventorysee").setExecutor(new InventorySeeCmd());
		getCommand("ping").setExecutor(new PingCmd());
		getCommand("mute").setExecutor(new MuteCmd());
		getCommand("warning").setExecutor(new WarningCmd());
		getCommand("kick").setExecutor(new KickCmd());
		getCommand("ban").setExecutor(new BanCmd());
		getCommand("pardon").setExecutor(new PardonCmd());
		getCommand("clearchat").setExecutor(new ClearChatCmd());
		getCommand("reboot").setExecutor(new RebootCmd());
		getCommand("say").setExecutor(new SayCmd());
		getCommand("activate-shop").setExecutor(new ActivateShopCmd());
		getCommand("reload-cfg").setExecutor(new ReloadConfigCmd());
		getCommand("hub").setExecutor(new HubCmd());
		getCommand("home").setExecutor(new HomeCmd());
		getCommand("sethome").setExecutor(new SetHomeCmd());
		getCommand("delhome").setExecutor(new DelHomeCmd());
		getCommand("tpa").setExecutor(new TpaCmd());
		getCommand("tpaccept").setExecutor(new TpacceptCmd());
		getCommand("shop").setExecutor(new ShopCmd());
		getCommand("setzone").setExecutor(new SetZoneCmd());
		getCommand("warp").setExecutor(new WarpCmd());
		getCommand("setrank").setExecutor(new SetRankCmd());
		getCommand("disable-plugin").setExecutor(new DisablePluginCmd());
		getCommand("enable-plugin").setExecutor(new EnablePluginCmd());
		getCommand("servers").setExecutor(new ServersCmd());
		getCommand("server").setExecutor(new ServerCmd());
		getCommand("reply").setExecutor(new ReplyCmd());
		getCommand("tell").setExecutor(new TellCmd());
		getCommand("statistics").setExecutor(new StatisticsCmd());
		getCommand("spawn").setExecutor(new SpawnCmd());
		getCommand("kit").setExecutor(new KitCmd());
		getCommand("kits").setExecutor(new KitsCmd());
		getCommand("balancetop").setExecutor(new BalanceTopCmd());
		getCommand("balance").setExecutor(new BalanceCmd());
		getCommand("pay").setExecutor(new PayCmd());
		getCommand("suicide").setExecutor(new SuicideCmd());
	}

	@Override
	public void onDisable() {
		running = false;
		plugin.saveConfig();
		barApi.onDisable();
		nuVotifier.onDisable();
		discordBot.broadcast(":octagonal_sign: **Server has stopped**", Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
		discordBot.unregister();
		if (!(lockette == null))
			lockette.onDisable();
		if (!(chatitem == null))
			chatitem.onDisable();
		if (!(protoListener == null))
			protoListener.unregister();
		try {
			topVotersHttpServer.server.close();
			getLogger().info("Top voters web server disabled.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		topVotersHttpServer.stop();
		for (Player player : Bukkit.getOnlinePlayers())
			player.sendMessage($.modernMsgPrefix + "Psst, the server is now updating; please be patient.");
		for (Player player : Bukkit.getOnlinePlayers()) {
			this.performBuggedLeave(player, false, false);
			if (USE_FACTIONS_AS_HUB) {
				enterFactions(player, false, false);
			} else {
				player.teleport($.getZoneLocation("hub"));
				if (!hub.contains(player.getUniqueId()))
					hub.add(player.getUniqueId());
				fetchLobby(player);
				playtimeManager.handle_QuitEvent(player);
			}
		}
	}

	public void fetchLobby(Player player) {
		if (hub.contains(player.getUniqueId())) {
			if (USE_FACTIONS_AS_HUB) {
				if ($.isAuthenticated(player))
					enterFactions(player, false, true);
			} else {
				if (!player.getAllowFlight())
					player.setAllowFlight(true);
				boolean isNothing = player.getInventory().getItem(0) == null;
				if (isNothing || !(player.getInventory().getItem(0).getType() == Material.COMPASS)) {
					$.clearPlayer(player);
					ItemStack compass = $.createMaterial(Material.COMPASS, ChatColor.LIGHT_PURPLE + "Server Selector");
					player.setTotalExperience(0);
					player.getInventory().addItem(compass);
					player.getInventory().setHeldItemSlot(8);
					player.getInventory().setHeldItemSlot(0);
				}
				player.setHealth(20.0);
				player.setLevel(0);
				player.setExp(0);
				player.setFoodLevel(20);
				player.setFireTicks(0);
				String message = "SkorrloreGaming";// " Welcome " + player.getName() + ", to our Minecraft server! ";
				if (message.length() > 16)
					message += message.substring(0, 16);
				Hashtable<String, Integer> array = new Hashtable<String, Integer>();
				array.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Servers", 2018);
				if ($.isMinigameEnabled(ServerMinigame.KITPVP))
					array.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Kitpvp", kitpvp.size());
				if ($.isMinigameEnabled(ServerMinigame.FACTIONS))
					array.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Factions", factions.size());
				if ($.isMinigameEnabled(ServerMinigame.SURVIVAL))
					array.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Survival", survival.size());
				if ($.isMinigameEnabled(ServerMinigame.SKYFIGHT))
					array.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Skyfight", skyfight.size());
				if ($.isMinigameEnabled(ServerMinigame.CREATIVE))
					array.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Creative", creative.size());
				if ($.isMinigameEnabled(ServerMinigame.SKYBLOCK))
					array.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Skyblock", skyblock.size());
				hubScoreboardTitleIndex.putIfAbsent(player.getUniqueId(), 0);
				int index = hubScoreboardTitleIndex.get(player.getUniqueId());
				if (message.length() <= 16) {
					$.Scoreboard.configureSidebar(player, ChatColor.RESET + message, array, false, false);
				} else {
					int finalIndex = index + 16;
					if (index < message.length() && finalIndex < message.length()) {
						$.Scoreboard.configureSidebar(player, message.substring(index, finalIndex), array, false, false);
						hubScoreboardTitleIndex.put(player.getUniqueId(), index + 1);
					} else {
						hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
					}
				}
			}
		}
	}

	public void reload() {
		boolean epiB = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PE"));
		reloadConfig();
		boolean epiA = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PE"));
		boolean _1m13 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m12"));
		boolean _1m12 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m12"));
		boolean _1m11 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m11"));
		boolean _1m10 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m10"));
		boolean _1m9 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m9"));
		boolean _1m8 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m8"));
		boolean _1m7 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m7"));
		boolean _1m6 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m6"));
		boolean _1m5 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m5"));
		boolean _1m4 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PC.1m4"));
		boolean _1m3 = Boolean.parseBoolean(getConfig().getString("settings.enable.protocolsupport.versions.PE"));
		if (!epiB && epiA)
			pingInjector.register();
		if (epiB && !epiA)
			pingInjector.unregister();
		disabledVersions.clear();
		if (!_1m13) {
			disabledVersions.add("1.13.1");
			disabledVersions.add("1.13");
		}
		if (!_1m12) {
			disabledVersions.add("1.12.2");
			disabledVersions.add("1.12.1");
			disabledVersions.add("1.12");
		}
		if (!_1m11) {
			disabledVersions.add("1.11.2");
			disabledVersions.add("1.11");
		}
		if (!_1m10) {
			disabledVersions.add("1.10");
		}
		if (!_1m9) {
			disabledVersions.add("1.9.4");
			disabledVersions.add("1.9.2");
			disabledVersions.add("1.9.1");
			disabledVersions.add("1.9");
		}
		if (!_1m8) {
			disabledVersions.add("1.8");
		}
		if (!_1m7) {
			disabledVersions.add("1.7.10");
			disabledVersions.add("1.7.5");
		}
		if (!_1m6) {
			disabledVersions.add("1.6.4");
			disabledVersions.add("1.6.2");
			disabledVersions.add("1.6.1");
		}
		if (!_1m5) {
			disabledVersions.add("1.5.2");
			disabledVersions.add("1.5.1");
		}
		if (!_1m4) {
			disabledVersions.add("1.4.7");
		}
		if (!_1m3) {
			disabledVersions.add("pe");
		}
	}

	public static long getUptime() {
		return System.currentTimeMillis() - serverStartTime;
	}

	public static Plugin getPlugin() {
		return plugin;
	}

	public String centerText(String text, int lineLength) {
		StringBuilder builder = new StringBuilder(text);
		char space = ' ';
		int distance = (lineLength - text.length()) / 2;
		for (int i = 0; i < distance; ++i) {
			builder.insert(0, space);
			builder.append(space);
		}
		return builder.toString();
	}

	@EventHandler
	public void onServerListPing(ServerListPingEvent event) {
		if (!pingInjector.running) {
			if (getConfig().contains("settings.enable.pingInjector")) {
				if (!getConfig().getBoolean("settings.enable.pingInjector"))
					event.setMotd(serverMotd);
			} else {
				event.setMotd(serverMotd);
			}
		}
	}

	public int performBuggedLeave(Player player, boolean noRestore, boolean noLog) {
		int changes = 0;
		int rawChanges = 0;
		changes += leaveSkyfight(player, noLog);
		int factionsLeaveReturnValue = leaveFactions(player, noRestore, noLog);
		if (!USE_FACTIONS_AS_HUB)
			changes += factionsLeaveReturnValue;
		rawChanges += factionsLeaveReturnValue;
		changes += changes += leaveCreative(player, noRestore, noLog);
		changes += leaveSurvival(player, noRestore, noLog);
		changes += leaveKitpvp(player, noRestore, noLog);
		changes += leaveSkyblock(player, noRestore, noLog);
		rawChanges += changes;
		if (changes > 0)
			$.clearPlayer(player);
		return rawChanges;
	}

	public void enterKitpvp(Player player, boolean noRestore, boolean noLog) {
		if ($.isPlayerNotAllowedToJoin(player, ServerMinigame.KITPVP)) {
			player.sendMessage("You are not allowed to enter this minigame.");
			return;
		}
		if (!kitpvp.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = $.getCurrentMinigame(player);
			int changes = performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN))
				return;
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			kitpvp.add(player.getUniqueId());
			$.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "kitpvp");
				if (!success) {
					Location teleportLocation = $.getZoneLocation("kitpvp");
					if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
						if (teleportLocation.distance(player.getLocation()) > 0.1) {
							$.teleport(player, teleportLocation);
						}
					} else {
						$.teleport(player, teleportLocation);
					}
					player.performCommand("kit starter");
				}
			} else {
				Location teleportLocation = $.getZoneLocation("kitpvp");
				if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
					if (teleportLocation.distance(player.getLocation()) > 0.1) {
						$.teleport(player, teleportLocation);
					}
				} else {
					$.teleport(player, teleportLocation);
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "KitPvP";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			player.setAllowFlight(false);
			$.kitpvpStatisticsScoreboard.schedule(player, DisplayType.Ten_Second_Period, Kitpvp_StatisticsScoreboard.class, Kitpvp_LeaderboardScoreboard.class);
		}
	}

	public int leaveKitpvp(Player player, boolean noSave, boolean noLog) {
		if (kitpvp.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (vanishedPlayers.containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "kitpvp");
				} catch (Exception ig) {
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has quit " + ChatColor.RED + "KitPvP";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			kitpvp.remove(player.getUniqueId());
			player.setAllowFlight(true);
			$.kitpvpStatisticsScoreboard.unregister(player);
			$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			return 1;
		}
		return 0;
	}

	public void enterFactions(Player player, boolean noRestore, boolean noLog) {
		if ($.isPlayerNotAllowedToJoin(player, ServerMinigame.FACTIONS)) {
			player.sendMessage("You are not allowed to enter this minigame.");
			return;
		}
		if (!factions.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = $.getCurrentMinigame(player);
			int changes = performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN)) {
				return;
			}
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			factions.add(player.getUniqueId());
			$.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "factions");
				if (!success) {
					Location teleportLocation = $.getZoneLocation("factions");
					if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
						if (teleportLocation.distance(player.getLocation()) > 0.1) {
							$.teleport(player, teleportLocation);
						}
					} else {
						$.teleport(player, teleportLocation);
					}
					player.performCommand("kit recruit");
				}
			} else {
				Location teleportLocation = $.getZoneLocation("factions");
				if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
					if (teleportLocation.distance(player.getLocation()) > 0.1) {
						$.teleport(player, teleportLocation);
					}
				} else {
					$.teleport(player, teleportLocation);
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Factions";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			player.setAllowFlight(false);
			$.factionsScoreboard.schedule(player);
		}
	}

	public int leaveFactions(Player player, boolean noSave, boolean noLog) {
		if (factions.contains(player.getUniqueId())) {
			if (USE_FACTIONS_AS_HUB)
				noLog = true;
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (vanishedPlayers.containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "factions");
				} catch (Exception ig) {
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has quit " + ChatColor.RED + "Factions";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			factions.remove(player.getUniqueId());
			player.setAllowFlight(true);
			if (Server.getFactionFlyPlayers().contains(player.getUniqueId()))
				Server.getFactionFlyPlayers().remove(player.getUniqueId());
			$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			return 1;
		}
		return 0;
	}

	public void enterSurvival(Player player, boolean noRestore, boolean noLog) {
		if ($.isPlayerNotAllowedToJoin(player, ServerMinigame.SURVIVAL)) {
			player.sendMessage("You are not allowed to enter this minigame.");
			return;
		}
		if (!survival.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = $.getCurrentMinigame(player);
			int changes = performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN))
				return;
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			survival.add(player.getUniqueId());
			$.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "survival");
				if (!success) {
					Location teleportLocation = $.getZoneLocation("survival");
					if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
						if (teleportLocation.distance(player.getLocation()) > 0.1) {
							$.teleport(player, teleportLocation);
						}
					} else {
						$.teleport(player, teleportLocation);
					}
					player.performCommand("kit recruit");
				}
			} else {
				Location teleportLocation = $.getZoneLocation("survival");
				if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
					if (teleportLocation.distance(player.getLocation()) > 0.1) {
						$.teleport(player, teleportLocation);
					}
				} else {
					$.teleport(player, teleportLocation);
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Survival";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
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
			if (vanishedPlayers.containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "survival");
				} catch (Exception ig) {
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has quit " + ChatColor.RED + "Survival";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			survival.remove(player.getUniqueId());
			player.setAllowFlight(true);
			return 1;
		}
		return 0;
	}

	public void enterSkyfight(Player player, boolean noRestore, boolean noLog) {
		if ($.isPlayerNotAllowedToJoin(player, ServerMinigame.SKYFIGHT)) {
			player.sendMessage("You are not allowed to enter this minigame.");
			return;
		}
		if (moderatingPlayers.containsKey(player.getUniqueId())) {
			noRestore = true;
			noLog = true;
		}
		ServerMinigame minigame = $.getCurrentMinigame(player);
		if (!(minigame == ServerMinigame.SKYFIGHT)) {
			int changes = performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN))
				return;
		}
		if (hub.contains(player.getUniqueId())) {
			hub.remove(player.getUniqueId());
			$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
		}
		boolean initialConnect = false;
		if (!skyfight.containsKey(player.getUniqueId())) {
			skyfight.put(player.getUniqueId(), new $.Skyfight.Player(player));
			initialConnect = true;
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Skyfight";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
		}
		$.Skyfight.Player sfPlayer = skyfight.get(player.getUniqueId());
		player.setHealth(20.0);
		player.setLevel(0);
		player.setExp(0);
		player.setFoodLevel(20);
		player.setFireTicks(0);
		player.setAllowFlight(true);
		Random random = new Random();
		int ran = random.nextInt(4);
		if (initialConnect)
			$.clearPlayer(player);
		ItemStack sword = $.createMaterial(Material.STONE_SWORD, ChatColor.GOLD + "The Forbidding Katana");
		ItemStack bow = $.createMaterial(Material.BOW, ChatColor.GOLD + "The Forbidding Bow");
		ItemStack arrow = $.createMaterial(Material.ARROW, 1, ChatColor.GOLD + "The Forbidding Darts");
		ItemStack helmet = $.createMaterial(Material.LEATHER_HELMET, 1, ChatColor.GOLD + "The Forbidding Helmet");
		ItemStack chestplate = $.createMaterial(Material.LEATHER_CHESTPLATE, ChatColor.GOLD + "The Forbidding Chestpeice");
		ItemStack leggings = $.createMaterial(Material.LEATHER_LEGGINGS, ChatColor.GOLD + "The Forbidding Leggings");
		ItemStack boots = $.createMaterial(Material.LEATHER_BOOTS, ChatColor.GOLD + "The Forbidding Boots");
		bow = $.addEnchant(bow, new EnchantInfo(Enchantment.ARROW_KNOCKBACK, 5));
		bow = $.addEnchant(bow, new EnchantInfo(Enchantment.ARROW_INFINITE, 1));
		bow = $.addEnchant(bow, new EnchantInfo(Enchantment.KNOCKBACK, 5));
		bow = $.setUnbreakable(bow, true);
		sword = $.addEnchant(sword, new EnchantInfo(Enchantment.KNOCKBACK, 5));
		sword = $.setUnbreakable(sword, true);
		helmet = $.setUnbreakable(helmet, true);
		chestplate = $.setUnbreakable(chestplate, true);
		leggings = $.setUnbreakable(leggings, true);
		boots = $.setUnbreakable(boots, true);
		Color leatherColor = null;
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.BLUE)
			leatherColor = Color.BLUE;
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.RED)
			leatherColor = Color.RED;
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.GREEN)
			leatherColor = Color.GREEN;
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.YELLOW)
			leatherColor = Color.YELLOW;
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.PINK)
			leatherColor = Color.fromRGB(255, 105, 180);
		helmet = $.addLeatherColor(helmet, leatherColor);
		chestplate = $.addLeatherColor(chestplate, leatherColor);
		leggings = $.addLeatherColor(leggings, leatherColor);
		boots = $.addLeatherColor(boots, leatherColor);
		$.clearPlayer(player);
		player.getInventory().setItem(0, sword);
		player.getInventory().setItem(1, bow);
		player.getInventory().setItem(8, chestplate);
		player.getInventory().setItem(9, arrow);
		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestplate);
		player.getInventory().setLeggings(leggings);
		player.getInventory().setBoots(boots);
		player.updateInventory();
		Location teleportLocation = $.getZoneLocation("skyfight" + ran);
		if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
			if (teleportLocation.distance(player.getLocation()) > 0.1) {
				$.teleport(player, teleportLocation);
			}
		} else {
			$.teleport(player, teleportLocation);
		}
		player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
		for (Entry<UUID, $.Skyfight.Player> id : skyfight.entrySet()) {
			if (!(id.getValue().getTag().getDamagee() == null)) {
				if (id.getValue().getTag().getDamagee().getUniqueId().toString().equals(player.getUniqueId().toString())) {
					id.getValue().setScore(id.getValue().getScore() + 1);
					id.getValue().getTag().setDamagee(null);
					Player tp = Bukkit.getPlayer(id.getKey());
					tp.playSound(tp.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, 1);
					break;
				}
			}
		}
		CraftGo.Player.clearArrowsInBody(player);
		for (Player op : Bukkit.getOnlinePlayers()) {
			if (skyfight.containsKey(op.getUniqueId()))
				$.skyfightScoreboard.schedule(op, true);
			$.Scoreboard.configureHealth(op);
		}
	}

	public int leaveSkyfight(Player player, boolean noLog) {
		if (skyfight.containsKey(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noLog = true;
			}
			if (vanishedPlayers.containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has quit " + ChatColor.RED + "Skyfight";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			skyfight.remove(player.getUniqueId());
			player.getActivePotionEffects().clear();
			CraftGo.Player.clearArrowsInBody(player);
			$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			$.clearPlayer(player);
			player.setAllowFlight(false);
			player.setLevel(0);
			player.setExp(0);
			if (!vanishedPlayers.containsKey(player.getUniqueId())) {
				for (Player p : getServer().getOnlinePlayers()) {
					p.showPlayer(this, player);
				}
				if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
					player.removePotionEffect(PotionEffectType.INVISIBILITY);
				player.setGameMode(GameMode.SURVIVAL);
			}
			return 1;
		}
		return 0;
	}

	public void enterCreative(Player player, boolean noRestore, boolean noLog) {
		if ($.isPlayerNotAllowedToJoin(player, ServerMinigame.CREATIVE)) {
			player.sendMessage("You are not allowed to enter this minigame.");
			return;
		}
		if (!creative.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = $.getCurrentMinigame(player);
			int changes = performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN))
				return;
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			creative.add(player.getUniqueId());
			$.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "creative");
				if (!success) {
					Location teleportLocation = $.getZoneLocation("creative");
					if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
						if (teleportLocation.distance(player.getLocation()) > 0.1) {
							$.teleport(player, teleportLocation);
						}
					} else {
						$.teleport(player, teleportLocation);
					}
				}
			} else {
				Location teleportLocation = $.getZoneLocation("creative");
				if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
					if (teleportLocation.distance(player.getLocation()) > 0.1) {
						$.teleport(player, teleportLocation);
					}
				} else {
					$.teleport(player, teleportLocation);
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Creative";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			Bukkit.getScheduler().runTaskLater(this, new Runnable() {
				@Override
				public void run() {
					if (creative.contains(player.getUniqueId())) {
						if (!(player.getGameMode() == GameMode.CREATIVE))
							player.setGameMode(GameMode.CREATIVE);
						if (!player.getAllowFlight())
							player.setAllowFlight(true);
						player.addAttachment(plugin, "plots.use", true);
						player.addAttachment(plugin, "plots.permpack.basic", true);
						player.addAttachment(plugin, "plots.plot.1", true);
						player.addAttachment(plugin, "plots.visit.other", true);
						if ($.getRankId(player) < -1 || $.getRankId(player) > -1) {
							for (String permission : Directory.basicWorldEditPermissions) {
								player.addAttachment(plugin, permission, true);
							}
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
			if (vanishedPlayers.containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "creative");
				} catch (Exception ig) {
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has quit " + ChatColor.RED + "Creative";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			creative.remove(player.getUniqueId());
			player.setGameMode(GameMode.SURVIVAL);
			player.setAllowFlight(true);
			if (running) {
				player.addAttachment(this, "plots.use", false);
				player.addAttachment(this, "plots.permpack.basic", false);
				player.addAttachment(this, "plots.plot.1", false);
				player.addAttachment(this, "plots.visit.other", false);
				if ($.getRankId(player) < -1 || $.getRankId(player) > -1) {
					for (String permission : Directory.basicWorldEditPermissions) {
						player.addAttachment(plugin, permission, false);
					}
				}
			}
			$.clearPlayer(player);
			return 1;
		}
		return 0;
	}

	public void enterSkyblock(Player player, boolean noRestore, boolean noLog) {
		if ($.isPlayerNotAllowedToJoin(player, ServerMinigame.SKYBLOCK)) {
			player.sendMessage("You are not allowed to enter this minigame.");
			return;
		}
		if (!skyblock.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noRestore = true;
				noLog = true;
			}
			ServerMinigame minigame = $.getCurrentMinigame(player);
			int changes = performBuggedLeave(player, noRestore, noLog);
			if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN))
				return;
			if (hub.contains(player.getUniqueId())) {
				hub.remove(player.getUniqueId());
				$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
				hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
			}
			skyblock.add(player.getUniqueId());
			$.clearPlayer(player);
			if (!noRestore) {
				boolean success = SolidStorage.restorePlayerData(player, "skyblock");
				if (!success) {
					Location teleportLocation = $.getZoneLocation("skyblock");
					if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
						if (teleportLocation.distance(player.getLocation()) > 0.1) {
							$.teleport(player, teleportLocation);
						}
					} else {
						$.teleport(player, teleportLocation);
					}
				}
			} else {
				Location teleportLocation = $.getZoneLocation("skyblock");
				if (teleportLocation.getWorld().getName().equals(player.getWorld().getName())) {
					if (teleportLocation.distance(player.getLocation()) > 0.1) {
						$.teleport(player, teleportLocation);
					}
				} else {
					$.teleport(player, teleportLocation);
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Skyblock";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			player.setAllowFlight(false);
			$.skyblockScoreboard.schedule(player);
		}
	}

	public int leaveSkyblock(Player player, boolean noSave, boolean noLog) {
		if (skyblock.contains(player.getUniqueId())) {
			if (moderatingPlayers.containsKey(player.getUniqueId())) {
				noSave = true;
				noLog = true;
			}
			if (vanishedPlayers.containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!noSave) {
				try {
					SolidStorage.savePlayerData(player, "skyblock");
				} catch (Exception ig) {
				}
			}
			if (!noLog) {
				String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has quit " + ChatColor.RED + "Skyblock";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
			skyblock.remove(player.getUniqueId());
			$.clearPlayer(player);
			player.setAllowFlight(false);
			$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
			return 1;
		}
		return 0;
	}
}
