package me.skorrloregaming;

import me.skorrloregaming.auction.Auctioneer;
import me.skorrloregaming.commands.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.discord.DiscordBot;
import me.skorrloregaming.events.PlayerMinigameChangeEvent;
import me.skorrloregaming.events.PlayerPreMinigameChangeEvent;
import me.skorrloregaming.hooks.*;
import me.skorrloregaming.impl.*;
import me.skorrloregaming.impl.Switches.SwitchIntDouble;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import me.skorrloregaming.listeners.BlockEventHandler;
import me.skorrloregaming.listeners.EntityEventHandler;
import me.skorrloregaming.listeners.PaperEventHandler;
import me.skorrloregaming.listeners.PlayerEventHandler;
import me.skorrloregaming.lockette.Lockette;
import me.skorrloregaming.ping.PingInjector;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import me.skorrloregaming.runnable.DelayedTeleport;
import me.skorrloregaming.runnable.GCandAutoDemotion;
import me.skorrloregaming.shop.LaShoppe;
import me.skorrloregaming.skins.SkinStorage;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Server extends JavaPlugin {

    private static Server instance;
    private final int BASIC_TELEPORT_TIME = 3;
    private final long BASIC_INVENTORY_UPDATE_DELAY = 5L;
    private final boolean USE_FACTIONS_AS_HUB = false;
    public TopVotersHttpServer topVotersHttpServer = null;
    public PingInjector pingInjector;
    public Lockette lockette = null;
    public ChatItem chatitem = null;
    public GCandAutoDemotion garbageCollector = null;
    private ConfigurationManager ramConfig;
    private ConfigurationManager warpConfig;
    private ConfigurationManager signConfig;
    private ConfigurationManager factionsConfig;
    private ConfigurationManager shoppeConfig;
    private ConfigurationManager survivalConfig;
    private ConfigurationManager marriageHomesConfig;
    private ConfigurationManager npcConfig;
    private ConfigurationManager spawnerConfig;
    private ConfigurationManager banConfig;
    private ConfigurationManager monthlyVoteConfig;
    private ConfigurationManager locketteConfig;
    private ConfigurationManager chatItemConfig;
    private ConfigurationManager chestShopConfig;
    private ConfigurationManager discordVerifyConfig;
    private Plugin plugin;
    private SkinStorage skinStorage = null;
    private SessionManager sessionManager = null;
    private Auctioneer auctioneer = null;
    private LaShoppe shoppe = null;
    private ArrayList<UUID> hub = new ArrayList<>();
    private ArrayList<UUID> kitpvp = new ArrayList<>();
    private ArrayList<UUID> factions = new ArrayList<>();
    private ArrayList<UUID> survival = new ArrayList<>();
    private ConcurrentMap<UUID, $.Skyfight.Player> skyfight = new ConcurrentHashMap<>();
    private ArrayList<UUID> creative = new ArrayList<>();
    private ArrayList<UUID> skyblock = new ArrayList<>();
    private String tempMotd = "/unspecified";
    private ArrayList<UUID> simpleDelayedTask = new ArrayList<>();
    private ConcurrentMap<UUID, Integer> delaySkinUpdate = new ConcurrentHashMap<>();
    private ArrayList<UUID> confirmRepairShop = new ArrayList<>();
    private ArrayList<UUID> confirmUnregisterNpc = new ArrayList<>();
    private ArrayList<UUID> currentFellers = new ArrayList<>();
    private ArrayList<String> disabledVersions = new ArrayList<>();
    private ArrayList<NpcPlayer> npcPlayers = new ArrayList<>();
    private ConcurrentMap<UUID, Integer> explosiveFunpowderCooldown = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> potionsKitCooldownKitpvp = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> starterKitCooldownKitpvp = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> recruitKitCooldownSurvival = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> recruitKitCooldownFactions = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> donatorKitCooldownFactions = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> redstoneKitCooldownFactions = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> obsidianKitCooldownFactions = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Integer> bedrockKitCooldownFactions = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, UUID> tpaRequests = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, UUID> marriageRequests = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, VanishedInfo> vanishedPlayers = new ConcurrentHashMap<>();
    private ConcurrentMap<Player, Player> savePersonalChest = new ConcurrentHashMap<>();
    private ConcurrentMap<Player, Player> saveOtherInventory = new ConcurrentHashMap<>();
    private ConcurrentMap<Player, SignInfo> signEditParam = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, Integer> spawnerPrices = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, Boolean> onlineMode = new ConcurrentHashMap<>();
    private ArrayList<UUID> doubleJumpCandidates = new ArrayList<>();
    private ArrayList<UUID> staffChatPlayers = new ArrayList<>();
    private ArrayList<UUID> marriageChatPlayers = new ArrayList<>();
    private ArrayList<UUID> spectatingPlayers = new ArrayList<>();
    private ArrayList<UUID> mutedPlayers = new ArrayList<>();
    private ArrayList<UUID> opmePlayers = new ArrayList<>();
    private ArrayList<UUID> factionFlyPlayers = new ArrayList<>();
    private ArrayList<UUID> survivalFlyPlayers = new ArrayList<>();
    private ConcurrentMap<String, Integer> timeSinceLastLogin = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, SwitchUUIDString> transferAcceptPlayers = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, UUID> discordVerifyPlayers = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, DelayedTeleport> delayedTeleports = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, SwitchIntDouble> playersInCombat = new ConcurrentHashMap<>();
    private ConcurrentMap<UUID, ServerMinigame> moderatingPlayers = new ConcurrentHashMap<>();
    private String defaultJoinMessage = null;
    private String defaultQuitMessage = null;
    private String serverMotd = null;
    private String pluginName = null;
    private String pluginLabel;
    private String lastKnownHubWorld;
    private long serverStartTime = 0L;
    private boolean pluginDebug = false;
    private boolean ingameAnticheatDebug = true;
    private boolean running = true;
    private ConcurrentMap<UUID, Integer> hubScoreboardTitleIndex = new ConcurrentHashMap<>();
    private ConcurrentMap<Integer, Long> hideLoginMessage = new ConcurrentHashMap<>();

    private AuthMe_Listener authListener = null;
    private Factions_Listener factionsListener = null;
    private ProtocolLib_Listener protoListener = null;
    private ProtocolSupportPocketStuff_Listener protoSupportPocketApi = null;
    private Votifier_Listener voteListener = null;
    private mcMMO_Listener mcmmoListener = null;

    private DiscordBot discordBot;

    private VoteManager voteManager;

    private ConcurrentMap<Player, ItemStack> storedItem = new ConcurrentHashMap<>();

    private ArrayList<UUID> waiverAcceptPlayers = new ArrayList<>();

    private long lastVoteTime = 0L;

    private ArrayList<BukkitTask> bukkitTasks = new ArrayList<>();

    public static Server getInstance() {
        return instance;
    }

    public static void setInstance(Server newInstance) {
        instance = newInstance;
    }

    public ServerType getServerType() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            String version = Bukkit.getVersion().toLowerCase();
            if (version.contains("paper")) {
                return ServerType.PaperSpigot;
            } else if (version.contains("taco")) {
                return ServerType.TacoSpigot;
            } else if (version.contains("torch")) {
                return ServerType.TorchSpigot;
            } else {
                return ServerType.Spigot;
            }
        } catch (Exception ex) {
            return ServerType.CraftBukkit;
        }
    }

    public ArrayList<UUID> getWaiverAcceptPlayers() {
        return waiverAcceptPlayers;
    }

    public ConcurrentMap<Player, ItemStack> getStoredItem() {
        return storedItem;
    }

    public void doReturnItem(Player player) {
        if (storedItem.containsKey(player.getPlayer())) {
            player.getInventory().setItem(9, storedItem.get(player));
            player.updateInventory();
            storedItem.remove(player);
        }
    }

    public long getLastVoteTime() {
        return lastVoteTime;
    }

    public void setLastVoteTime(long voteTime) {
        lastVoteTime = voteTime;
    }

    public AuthMe_Listener getAuthListener() {
        return authListener;
    }

    public Factions_Listener getFactionsListener() {
        return factionsListener;
    }

    public ProtocolLib_Listener getProtoListener() {
        return protoListener;
    }

    public ProtocolSupportPocketStuff_Listener getProtoSupportPocketApi() {
        return protoSupportPocketApi;
    }

    public Votifier_Listener getVoteListener() {
        return voteListener;
    }

    public mcMMO_Listener getMcmmoListener() {
        return mcmmoListener;
    }

    public ConcurrentMap<UUID, Integer> getHubScoreboardTitleIndex() {
        return hubScoreboardTitleIndex;
    }

    public ConcurrentMap<Integer, Long> getHideLoginMessage() {
        return hideLoginMessage;
    }

    public long getServerStartTime() {
        return serverStartTime;
    }

    public boolean getPluginDebug() {
        return pluginDebug;
    }

    public void setPluginDebug(boolean enabled) {
        pluginDebug = enabled;
    }

    public boolean getIngameAnticheatDebug() {
        return ingameAnticheatDebug;
    }

    public void setIngameAnticheatDebug(boolean enabled) {
        ingameAnticheatDebug = enabled;
    }

    public boolean isRunning() {
        return running;
    }

    public long getInventoryUpdateDelay() {
        return BASIC_INVENTORY_UPDATE_DELAY;
    }

    public boolean getUseFactionsAsHub() {
        return USE_FACTIONS_AS_HUB;
    }

    public String getDefaultJoinMessage() {
        return defaultJoinMessage;
    }

    public void setDefaultJoinMessage(String defaultJoinMessage) {
        this.defaultJoinMessage = defaultJoinMessage;
    }

    public String setDefaultQuitMessage() {
        return defaultQuitMessage;
    }

    public void setDefaultQuitMessage(String defaultQuitMessage) {
        this.defaultQuitMessage = defaultQuitMessage;
    }

    public String getServerMotd() {
        return serverMotd;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getPluginLabel() {
        return pluginLabel;
    }

    public String getLastKnownHubWorld() {
        return lastKnownHubWorld;
    }

    public void setLastKnownHubWorld(String lastKnownHubWorld) {
        this.lastKnownHubWorld = lastKnownHubWorld;
    }

    public ConcurrentMap<String, Integer> getTimeSinceLastLogin() {
        return timeSinceLastLogin;
    }

    public ConcurrentMap<UUID, SwitchUUIDString> getTransferAcceptPlayers() {
        return transferAcceptPlayers;
    }

    public ConcurrentMap<Integer, UUID> getDiscordVerifyPlayers() {
        return discordVerifyPlayers;
    }

    public ConcurrentMap<UUID, DelayedTeleport> getDelayedTeleports() {
        return delayedTeleports;
    }

    public ConcurrentMap<UUID, SwitchIntDouble> getPlayersInCombat() {
        return playersInCombat;
    }

    public ConcurrentMap<UUID, ServerMinigame> getModeratingPlayers() {
        return moderatingPlayers;
    }

    public ArrayList<UUID> getDoubleJumpCandidates() {
        return doubleJumpCandidates;
    }

    public ArrayList<UUID> getStaffChatPlayers() {
        return staffChatPlayers;
    }

    public ArrayList<UUID> getMarriageChatPlayers() {
        return marriageChatPlayers;
    }

    public ArrayList<UUID> getSpectatingPlayers() {
        return spectatingPlayers;
    }

    public ArrayList<UUID> getMutedPlayers() {
        return mutedPlayers;
    }

    public ArrayList<UUID> getOpmePlayers() {
        return opmePlayers;
    }

    public ArrayList<UUID> getFactionFlyPlayers() {
        return factionFlyPlayers;
    }

    public ArrayList<UUID> getSurvivalFlyPlayers() {
        return survivalFlyPlayers;
    }

    public ConcurrentMap<UUID, UUID> getTpaRequests() {
        return tpaRequests;
    }

    public ConcurrentMap<UUID, UUID> getMarriageRequests() {
        return marriageRequests;
    }

    public ConcurrentMap<UUID, VanishedInfo> getVanishedPlayers() {
        return vanishedPlayers;
    }

    public ConcurrentMap<Player, Player> getSavePersonalChest() {
        return savePersonalChest;
    }

    public ConcurrentMap<Player, Player> getSaveOtherInventory() {
        return saveOtherInventory;
    }

    public ConcurrentMap<Player, SignInfo> getSignEditParam() {
        return signEditParam;
    }

    public ConcurrentMap<Integer, Integer> getSpawnerPrices() {
        return spawnerPrices;
    }

    public ConcurrentMap<UUID, Boolean> getOnlineMode() {
        return onlineMode;
    }

    public ConcurrentMap<UUID, Integer> getExplosiveFunpowderCooldown() {
        return explosiveFunpowderCooldown;
    }

    public ConcurrentMap<UUID, Integer> getPotionsKitCooldown() {
        return potionsKitCooldownKitpvp;
    }

    public ConcurrentMap<UUID, Integer> getStarterKitCooldown() {
        return starterKitCooldownKitpvp;
    }

    public ConcurrentMap<UUID, Integer> getRecruitKitCooldown(ServerMinigame minigame) {
        switch (minigame) {
            case SURVIVAL:
                return recruitKitCooldownSurvival;
            case FACTIONS:
                return recruitKitCooldownFactions;
            default:
                return null;
        }
    }

    public ConcurrentMap<UUID, Integer> getDonatorKitCooldown() {
        return donatorKitCooldownFactions;
    }

    public ConcurrentMap<UUID, Integer> getRedstoneKitCooldown() {
        return redstoneKitCooldownFactions;
    }

    public ConcurrentMap<UUID, Integer> getObsidianKitCooldown() {
        return obsidianKitCooldownFactions;
    }

    public ConcurrentMap<UUID, Integer> getBedrockKitCooldown() {
        return bedrockKitCooldownFactions;
    }

    public String getTempMotd() {
        return tempMotd;
    }

    public void setTempMotd(String tempMotd) {
        this.tempMotd = tempMotd;
    }

    public ArrayList<UUID> getDelayedTasks() {
        return simpleDelayedTask;
    }

    public ConcurrentMap<UUID, Integer> getDelaySkinUpdate() {
        return delaySkinUpdate;
    }

    public ArrayList<UUID> getConfirmRepairShop() {
        return confirmRepairShop;
    }

    public ArrayList<UUID> getConfirmUnregisterNpc() {
        return confirmUnregisterNpc;
    }

    public ArrayList<UUID> getCurrentFellers() {
        return currentFellers;
    }

    public ArrayList<String> getDisabledVersions() {
        return disabledVersions;
    }

    public ArrayList<NpcPlayer> getNpcPlayers() {
        return npcPlayers;
    }

    public ArrayList<UUID> getHub() {
        return hub;
    }

    public ArrayList<UUID> getKitpvp() {
        return kitpvp;
    }

    public ArrayList<UUID> getFactions() {
        return factions;
    }

    public ArrayList<UUID> getSurvival() {
        return survival;
    }

    public ConcurrentMap<UUID, $.Skyfight.Player> getSkyfight() {
        return skyfight;
    }

    public ArrayList<UUID> getCreative() {
        return creative;
    }

    public ArrayList<UUID> getSkyblock() {
        return skyblock;
    }

    public ConfigurationManager getRamConfig() {
        return ramConfig;
    }

    public ConfigurationManager getWarpConfig() {
        return warpConfig;
    }

    public ConfigurationManager getSignConfig() {
        return signConfig;
    }

    public ConfigurationManager getFactionsConfig() {
        return factionsConfig;
    }

    public ConfigurationManager getShoppeConfig() {
        return shoppeConfig;
    }

    public ConfigurationManager getSurvivalConfig() {
        return survivalConfig;
    }

    public ConfigurationManager getMarriageHomesConfig() {
        return marriageHomesConfig;
    }

    public ConfigurationManager getNpcConfig() {
        return npcConfig;
    }

    public ConfigurationManager getSpawnerConfig() {
        return spawnerConfig;
    }

    public ConfigurationManager getBanConfig() {
        return banConfig;
    }

    public ConfigurationManager getMonthlyVoteConfig() {
        return monthlyVoteConfig;
    }

    public ConfigurationManager getLocketteConfig() {
        return locketteConfig;
    }

    public ConfigurationManager getChatItemConfig() {
        return chatItemConfig;
    }

    public ConfigurationManager getChestShopConfig() {
        return chestShopConfig;
    }

    public ConfigurationManager getDiscordVerifyConfig() {
        return discordVerifyConfig;
    }

    public GCandAutoDemotion getGarbageCollector() {
        return garbageCollector;
    }

    public ChatItem getChatItem() {
        return chatitem;
    }

    public SkinStorage getSkinStorage() {
        return skinStorage;
    }

    public Auctioneer getAuctioneer() {
        return auctioneer;
    }

    public LaShoppe getShoppe() {
        return shoppe;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public int getTeleportationDelay() {
        return BASIC_TELEPORT_TIME;
    }

    public DiscordBot getDiscordBot() {
        return discordBot;
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public ArrayList<BukkitTask> getBukkitTasks() {
        return bukkitTasks;
    }

    @Override
    public void onEnable() {
        serverStartTime = System.currentTimeMillis();
        pluginName = this.getDescription().getName();
        pluginLabel = Link$.Legacy.tag;
        plugin = this;
        Server.setInstance(this);
        instance = this;
        String lineOne = ChatColor.RED + pluginName + ChatColor.GRAY + " has been running since late 2013.";
        String lineTwo = ChatColor.GRAY + "Server has been updated to support 1.14 clients.";
        serverMotd = lineOne + '\n' + lineTwo;
        ramConfig = new ConfigurationManager();
        warpConfig = new ConfigurationManager();
        signConfig = new ConfigurationManager();
        factionsConfig = new ConfigurationManager();
        shoppeConfig = new ConfigurationManager();
        survivalConfig = new ConfigurationManager();
        banConfig = new ConfigurationManager();
        npcConfig = new ConfigurationManager();
        spawnerConfig = new ConfigurationManager();
        marriageHomesConfig = new ConfigurationManager();
        monthlyVoteConfig = new ConfigurationManager();
        locketteConfig = new ConfigurationManager();
        chatItemConfig = new ConfigurationManager();
        chestShopConfig = new ConfigurationManager();
        discordVerifyConfig = new ConfigurationManager();
        $.createDataFolder();
        getConfig().options().copyDefaults(true);
        saveConfig();
        warpConfig.setup(new File(this.getDataFolder(), "warps.yml"));
        signConfig.setup(new File(this.getDataFolder(), "shops.yml"));
        factionsConfig.setup(new File(this.getDataFolder(), "factions.yml"));
        survivalConfig.setup(new File(this.getDataFolder(), "survival.yml"));
        banConfig.setup(new File(this.getDataFolder(), "banned.yml"));
        npcConfig.setup(new File(this.getDataFolder(), "npc_storage.yml"));
        spawnerConfig.setup(new File(this.getDataFolder(), "spawners.yml"));
        marriageHomesConfig.setup(new File(this.getDataFolder(), "marriage_homes.yml"));
        monthlyVoteConfig.setup(new File(this.getDataFolder(), "monthly_votes.yml"));
        locketteConfig.setup(new File(this.getDataFolder(), "lockette_config.yml"));
        shoppeConfig.setup(new File(this.getDataFolder(), "shoppe_config.yml"));
        chestShopConfig.setup(new File(this.getDataFolder(), "chestshop_config.yml"));
        discordVerifyConfig.setup(new File(this.getDataFolder(), "discord_config.yml"));
        File chatItemConfig = new File(this.getDataFolder(), "chatitem_config.yml");
        if (!chatItemConfig.exists()) {
            try {
                FileUtils.copyInputStreamToFile(this.getResource("chatitem_config.yml"), chatItemConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.chatItemConfig.setup(chatItemConfig);
        spawnerPrices.put(0, 6500);
        spawnerPrices.put(1, 6500);
        spawnerPrices.put(2, 9500);
        spawnerPrices.put(3, 9500);
        spawnerPrices.put(4, 34500);
        spawnerPrices.put(5, 9500);
        spawnerPrices.put(6, 4500);
        spawnerPrices.put(7, 3000);
        spawnerPrices.put(8, 3000);
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities())
                if (entity.getType() == EntityType.ENDER_DRAGON)
                    entity.remove();
        }
        discordBot = new DiscordBot(getPluginName(), getConfig().getString("settings.discordBot.token", "TOKEN"), getConfig().getLong("settings.discordBot.guild", 0));
        discordBot.register();
        lockette = new Lockette();
        lockette.onEnable();
        auctioneer = new Auctioneer();
        shoppe = new LaShoppe();
        garbageCollector = new GCandAutoDemotion();
        getServer().getPluginManager().registerEvents(new BlockEventHandler(), this);
        getServer().getPluginManager().registerEvents(new PlayerEventHandler(), this);
        getServer().getPluginManager().registerEvents(new EntityEventHandler(), this);
        ServerType serverType = getServerType();
        getLogger().info("Detected " + serverType + " server type, handling accordingly..");
        if (serverType == ServerType.PaperSpigot)
            getServer().getPluginManager().registerEvents(new PaperEventHandler(), this);
        if (getConfig().getBoolean("settings.bungeecord", false))
            getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        pingInjector = new PingInjector();
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") != null) {
            skinStorage = new SkinStorage();
            chatitem = new ChatItem();
            chatitem.onEnable();
            protoListener = new ProtocolLib_Listener(this);
            protoListener.register();
        }
        if (getConfig().contains("settings.enable.pingInjector")) {
            if (getConfig().getBoolean("settings.enable.pingInjector"))
                pingInjector.register();
        } else {
            pingInjector.register();
        }
        lastKnownHubWorld = $.getZoneLocation("hub").getWorld().getName();
        if (Link$.isPluginEnabled("Vault")) {
            new VaultEconomy().setupVault();
        }
        if (!getConfig().contains("settings.topVotersHttpServerPort")) {
            getConfig().set("settings.topVotersHttpServerPort", 2095);
        }
        if (Link$.isPluginEnabled("mcMMO")) {
            mcmmoListener = new mcMMO_Listener();
            mcmmoListener.register();
        }
        if (Link$.isPluginEnabled("ProtocolSupportPocketStuff") && Link$.isPluginEnabled("AuthMe")) {
            protoSupportPocketApi = new ProtocolSupportPocketStuff_Listener();
            protoSupportPocketApi.bakeModals();
            protoSupportPocketApi.bakeCallbacks();
            protoSupportPocketApi.register();
        }
        if (Link$.isPluginEnabled("AuthMe")) {
            authListener = new AuthMe_Listener();
            authListener.register();
        }
        if (Link$.isPluginEnabled("Votifier")) {
            voteListener = new Votifier_Listener();
            voteListener.register();
            voteManager = new VoteManager();
        }
        Bukkit.getScheduler().runTask(this, () -> {
            if (Link$.isPluginEnabled("Factions")) {
                factionsListener = new Factions_Listener();
                factionsListener.register();
            }
            for (Player player : Bukkit.getOnlinePlayers())
                player.sendMessage(Link$.modernMsgPrefix + "Psst, did you know the server finished updating?");
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.performCommand("hub");
                LinkServer.getInstance().getPlaytimeManager().handle_JoinEvent(player);
            }
            List<Entity> entity = new LinkedList<>($.getZoneLocation("creative").getWorld().getEntities());
            for (Entity e : entity) {
                if (e instanceof Item)
                    e.remove();
            }
        });
        bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                plugin.saveConfig();
                try {
                    SolidStorage.savePlayerData(player, $.getMinigameDomain(player));
                } catch (Exception e) {
                }
            }
        }, 0L, 6000L));
        bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(Server.getInstance().getPlugin(), () -> {
            for (World world : Bukkit.getWorlds()) {
                ServerMinigame minigame = $.getMinigameFromWorld(world);
                if ($.daylightMinigames.contains(minigame.toString().toLowerCase())) {
                    world.setTime(8000L);
                } else if ($.nightlightMinigames.contains(minigame.toString().toLowerCase())) {
                    world.setTime(14000L);
                }
            }
        }, 20L, 20L));
        bukkitTasks.add(Bukkit.getScheduler().runTaskTimer(this, garbageCollector, 0L, 36000L));
        reload();
        sessionManager = new SessionManager();
        sessionManager.setup();
        topVotersHttpServer = new TopVotersHttpServer(getConfig().getInt("settings.topVotersHttpServerPort", 2096));
        CustomRecipes.loadRecipes();
        getCommand("verify").setExecutor(new VerifyCmd());
        getCommand("feed").setExecutor(new FeedCmd());
        getCommand("fly").setExecutor(new FlyCmd());
        getCommand("printblockstate").setExecutor(new PrintBlockStateCmd());
        getCommand("spoof-vote").setExecutor(new SpoofVoteCmd());
        getCommand("playertime").setExecutor(new PlayertimeCmd());
        getCommand("sessions").setExecutor(new SessionsCmd());
        getCommand("transfer").setExecutor(new TransferCmd());
        getCommand("transferaccept").setExecutor(new TransferAcceptCmd());
        getCommand("transferdeny").setExecutor(new TransferDenyCmd());
        getCommand("warnings").setExecutor(new WarningsCmd());
        getCommand("update-skin").setExecutor(new UpdateSkinCmd());
        getCommand("spectate").setExecutor(new SpectateCmd());
        getCommand("rollback").setExecutor(new RollbackCmd());
        getCommand("center").setExecutor(new CenterCmd());
        getCommand("marrychat").setExecutor(new MarryCmd());
        getCommand("marry").setExecutor(new MarryCmd());
        getCommand("divorce").setExecutor(new DivorceCmd());
        getCommand("spawner").setExecutor(new SpawnerCmd());
        getCommand("staffchat").setExecutor(new StaffChatCmd());
        getCommand("effect").setExecutor(new EffectCmd());
        getCommand("trails").setExecutor(new TrailsCmd());
        getCommand("clear").setExecutor(new ClearCmd());
        getCommand("upgrade-kit").setExecutor(new UpgradeKitCmd());
        getCommand("spawn-npc").setExecutor(new SpawnNpcCmd());
        getCommand("remove-npc").setExecutor(new RemoveNpcCmd());
        getCommand("autologin").setExecutor(new AutoLoginCmd());
        getCommand("reset").setExecutor(new ResetCmd());
        getCommand("skull").setExecutor(new SkullCmd());
        getCommand("who").setExecutor(new WhoisCmd());
        getCommand("list").setExecutor(new ListCmd());
        getCommand("build-time").setExecutor(new BuildTimeCmd());
        getCommand("moderate").setExecutor(new ModerateCmd());
        getCommand("motd").setExecutor(new MotdCmd());
        getCommand("wild").setExecutor(new WildCmd());
        getCommand("give").setExecutor(new GiveCmd());
        getCommand("rename").setExecutor(new RenameCmd());
        getCommand("opme").setExecutor(new OpmeCmd());
        getCommand("vanish").setExecutor(new VanishCmd());
        getCommand("sign-edit").setExecutor(new SignEditCmd());
        getCommand("chest").setExecutor(new ChestCmd());
        getCommand("inventorysee").setExecutor(new InventorySeeCmd());
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
        getCommand("disable-plugin").setExecutor(new DisablePluginCmd());
        getCommand("enable-plugin").setExecutor(new EnablePluginCmd());
        getCommand("servers").setExecutor(new ServersCmd());
        getCommand("server").setExecutor(new ServerCmd());
        getCommand("statistics").setExecutor(new StatisticsCmd());
        getCommand("spawn").setExecutor(new SpawnCmd());
        getCommand("kit").setExecutor(new KitCmd());
        getCommand("kits").setExecutor(new KitsCmd());
        getCommand("balancetop").setExecutor(new BalanceTopCmd());
        getCommand("balance").setExecutor(new BalanceCmd());
        getCommand("pay").setExecutor(new PayCmd());
        getCommand("suicide").setExecutor(new SuicideCmd());
        getCommand("transport").setExecutor(new TransportCmd());
        getCommand("deposit").setExecutor(new DepositCmd());
        getCommand("withdraw").setExecutor(new WithdrawCmd());
    }

    @Override
    public void onDisable() {
        running = false;
        saveConfig();
        discordBot.broadcast(":octagonal_sign: **Server has stopped**", Channel.SERVER_CHAT);
        discordBot.unregister();
        if (!(lockette == null))
            lockette.onDisable();
        if (!(chatitem == null))
            chatitem.onDisable();
        try {
            topVotersHttpServer.server.close();
            getLogger().info("Top voters web server disabled.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        topVotersHttpServer.stop();
        for (Player player : Bukkit.getOnlinePlayers())
            player.sendMessage(Link$.modernMsgPrefix + "Psst, the server is now updating; please be patient.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.performBuggedLeave(player, false, false);
            if (USE_FACTIONS_AS_HUB) {
                enterFactions(player, false, false);
            } else {
                player.teleport($.getZoneLocation("hub"));
                if (!hub.contains(player.getUniqueId()))
                    hub.add(player.getUniqueId());
                fetchLobby(player);
                LinkServer.getInstance().getPlaytimeManager().handle_QuitEvent(player);
            }
        }
        LinkServer.getInstance().getRedisDatabase().unregister();
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
                    ItemStack compass = Link$.createMaterial(Material.COMPASS, ChatColor.LIGHT_PURPLE + "Server Selector");
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
                Hashtable<String, Integer> array = new Hashtable<>();
                array.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Servers", 2019);
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
                    $.Scoreboard.configureSidebar(player, message, array, false, false);
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

    public long getUptime() {
        return System.currentTimeMillis() - serverStartTime;
    }

    public Plugin getPlugin() {
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

    public int performBuggedLeave(Player player, boolean noRestore, boolean noLog) {
        int changes = 0;
        int rawChanges = 0;
        changes += leaveSkyfight(player, noLog);
        int factionsLeaveReturnValue = leaveFactions(player, noRestore, noLog);
        if (!USE_FACTIONS_AS_HUB)
            changes += factionsLeaveReturnValue;
        rawChanges += factionsLeaveReturnValue;
        changes += leaveCreative(player, noRestore, noLog);
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
            if (CraftGo.Player.getProtocolVersion(player) < 107) {
                player.sendMessage("You are not allowed to enter this minigame.");
                player.sendMessage("To enter this minigame please use 1.9 or above.");
                return;
            }
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.KITPVP));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            player.setAllowFlight(false);
            $.Kitpvp.refreshScoreboard(player, false);
            Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.KITPVP));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            kitpvp.remove(player.getUniqueId());
            player.setAllowFlight(true);
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
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.FACTIONS));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            player.setAllowFlight(false);
            $.Factions.refreshScoreboard(player, false);
            Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.FACTIONS));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            factions.remove(player.getUniqueId());
            player.setAllowFlight(true);
            Server.getInstance().getFactionFlyPlayers().remove(player.getUniqueId());
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
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.SURVIVAL));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            player.setAllowFlight(false);
            Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.SURVIVAL));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
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
        if (hub.contains(player.getUniqueId())) {
            hub.remove(player.getUniqueId());
            $.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
            hubScoreboardTitleIndex.put(player.getUniqueId(), 0);
        }
        boolean initialConnect = false;
        if (!skyfight.containsKey(player.getUniqueId())) {
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.SKYFIGHT));
            ServerMinigame minigame = $.getCurrentMinigame(player);
            if (!(minigame == ServerMinigame.SKYFIGHT)) {
                int changes = performBuggedLeave(player, noRestore, noLog);
                if (changes == 0 && !(minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN))
                    return;
            }
            skyfight.put(player.getUniqueId(), new $.Skyfight.Player(player));
            initialConnect = true;
            if (!noLog) {
                String message = pluginLabel + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Skyfight";
                Bukkit.broadcastMessage(message);
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
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
        ItemStack sword = Link$.createMaterial(Material.STONE_SWORD, ChatColor.GOLD + "The Forbidding Katana");
        ItemStack bow = Link$.createMaterial(Material.BOW, ChatColor.GOLD + "The Forbidding Bow");
        ItemStack crossBow = Link$.createMaterial(Material.CROSSBOW, ChatColor.GOLD + "The Forbidding Crossbow");
        ItemStack trident = Link$.createMaterial(Material.TRIDENT, ChatColor.GOLD + "The Forbidding Trident");
        CrossbowMeta crossBowMeta = (CrossbowMeta) crossBow.getItemMeta();
        crossBowMeta.addChargedProjectile(Link$.createMaterial(Material.ARROW));
        crossBowMeta.addChargedProjectile(Link$.createMaterial(Material.ARROW));
        crossBowMeta.addChargedProjectile(Link$.createMaterial(Material.ARROW));
        crossBow.setItemMeta(crossBowMeta);
        ItemStack arrow = Link$.createMaterial(Material.ARROW, 1, ChatColor.GOLD + "The Forbidding Darts");
        ItemStack helmet = Link$.createMaterial(Material.LEATHER_HELMET, 1, ChatColor.GOLD + "The Forbidding Helmet");
        ItemStack chestplate = Link$.createMaterial(Material.LEATHER_CHESTPLATE, ChatColor.GOLD + "The Forbidding Chestpeice");
        ItemStack leggings = Link$.createMaterial(Material.LEATHER_LEGGINGS, ChatColor.GOLD + "The Forbidding Leggings");
        ItemStack boots = Link$.createMaterial(Material.LEATHER_BOOTS, ChatColor.GOLD + "The Forbidding Boots");
        bow = Link$.addEnchant(bow, new EnchantInfo(Enchantment.ARROW_KNOCKBACK, 5));
        bow = Link$.addEnchant(bow, new EnchantInfo(Enchantment.ARROW_INFINITE, 1));
        bow = Link$.addEnchant(bow, new EnchantInfo(Enchantment.KNOCKBACK, 5));
        crossBow = Link$.addEnchant(crossBow, new EnchantInfo(Enchantment.ARROW_KNOCKBACK, 5));
        crossBow = Link$.addEnchant(crossBow, new EnchantInfo(Enchantment.ARROW_INFINITE, 1));
        crossBow = Link$.addEnchant(crossBow, new EnchantInfo(Enchantment.KNOCKBACK, 5));
        crossBow = Link$.addEnchant(crossBow, new EnchantInfo(Enchantment.QUICK_CHARGE, 3));
        crossBow = Link$.addEnchant(crossBow, new EnchantInfo(Enchantment.MULTISHOT, 1));
        trident = Link$.addEnchant(trident, new EnchantInfo(Enchantment.LOYALTY, 3));
        trident = Link$.addEnchant(trident, new EnchantInfo(Enchantment.CHANNELING, 1));
        bow = Link$.setUnbreakable(bow, true);
        crossBow = Link$.setUnbreakable(crossBow, true);
        trident = Link$.setUnbreakable(trident, true);
        sword = Link$.addEnchant(sword, new EnchantInfo(Enchantment.KNOCKBACK, 5));
        sword = Link$.setUnbreakable(sword, true);
        helmet = Link$.setUnbreakable(helmet, true);
        chestplate = Link$.setUnbreakable(chestplate, true);
        leggings = Link$.setUnbreakable(leggings, true);
        boots = Link$.setUnbreakable(boots, true);
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
        helmet = Link$.addLeatherColor(helmet, leatherColor);
        chestplate = Link$.addLeatherColor(chestplate, leatherColor);
        leggings = Link$.addLeatherColor(leggings, leatherColor);
        boots = Link$.addLeatherColor(boots, leatherColor);
        $.clearPlayer(player);
        player.getInventory().setItem(0, sword);
        player.getInventory().setItem(1, bow);
        player.getInventory().setItem(2, crossBow);
        player.getInventory().setItem(3, trident);
        player.getInventory().setItem(8, chestplate);
        player.getInventory().setItem(9, arrow);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
        player.updateInventory();
        Location teleportLocation = $.getZoneLocation("skyfight" + ran);
        teleportLocation.getWorld().setStorm(true);
        teleportLocation.getWorld().setTime(18000);
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
                $.Skyfight.refreshScoreboard(op, false);
            $.Scoreboard.configureHealth(op);
        }
        if (initialConnect)
            Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.SKYFIGHT));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
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
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.CREATIVE));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            Server.getInstance().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(this, () -> {
                if (creative.contains(player.getUniqueId())) {
                    if (!(player.getGameMode() == GameMode.CREATIVE))
                        player.setGameMode(GameMode.CREATIVE);
                    if (!player.getAllowFlight())
                        player.setAllowFlight(true);
                    player.addAttachment(plugin, "plots.use", true);
                    player.addAttachment(plugin, "plots.permpack.basic", true);
                    player.addAttachment(plugin, "plots.plot.1", true);
                    player.addAttachment(plugin, "plots.visit.other", true);
                    if (Link$.getDonorRankId(player) < -1 || Link$.getRankId(player) > -1) {
                        for (String permission : Directory.basicWorldEditPermissions) {
                            player.addAttachment(plugin, permission, true);
                        }
                    }
                }
            }, 100L));
            Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.CREATIVE));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
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
                if (Link$.getDonorRankId(player) < -1 || Link$.getRankId(player) > -1) {
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
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.SKYBLOCK));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
                        ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
                        , Channel.SERVER_CHAT);
            }
            player.setAllowFlight(false);
            $.Skyblock.refreshScoreboard(player, false);
            Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.SKYBLOCK));
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
                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
                message = message.substring(message.indexOf(ChatColor.RED + ""));
                Server.getInstance().getDiscordBot().broadcast(
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
