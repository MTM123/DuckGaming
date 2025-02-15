package me.skorrloregaming;

import com.massivecraft.factions.*;
import me.skorrloregaming.impl.MarriageGender;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.Switches.SwitchStringMinigame;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.*;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.ScoreboardManager;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class $ {
    public static String consoleTag = ChatColor.RED + "[" + ChatColor.GRAY + "Console" + ChatColor.RED + "] " + ChatColor.RED;
    public static String pricePrefix = ChatColor.RESET + "Purchase Price: " + ChatColor.RED + "$";
    public static List<String> validMinigames = Arrays.asList("kitpvp", "factions", "survival", "skyfight", "creative", "skyblock", "prison", "dated");
    public static List<String> validStorageMinigames = Arrays.asList("kitpvp", "factions", "survival", "creative", "skyblock");
    public static List<String> validEconomyMinigames = Arrays.asList("kitpvp", "factions", "skyblock", "survival");
    public static List<String> validLocketteMinigames = Arrays.asList("skyblock", "factions", "survival");
    public static List<String> validStairSeatMinigames = Arrays.asList("creative", "skyblock", "factions");
    public static List<String> validTransportMinigames = Arrays.asList("skyblock", "factions", "survival");
    public static List<String> scoreboardAutoUpdateMinigames = Arrays.asList("skyblock", "factions", "kitpvp");
    public static List<String> betaMinigames = Collections.emptyList();
    public static List<String> daylightMinigames = Arrays.asList("skyblock", "creative", "hub");
    public static List<String> nightlightMinigames = Arrays.asList("skyfight", "kitpvp");
    public static List<SwitchStringMinigame> playersNotAllowedToJoinSpecificMinigames = Arrays.asList(new SwitchStringMinigame("LuckyPlayz01_", ServerMinigame.KITPVP), new SwitchStringMinigame("LuckyPlayz01_", ServerMinigame.SKYFIGHT));

    public static void playForbiddenTeleportMessage(CommandSender player, ServerMinigame minigame) {
        player.sendMessage("Sorry, teleportation was cancelled, please contact an admin.");
        player.sendMessage("Most likely the teleport destination is forbidden in " + WordUtils.capitalize(minigame.toString().toLowerCase()) + ".");
    }

    public static boolean isPlayerNotAllowedToJoin(Player player, ServerMinigame minigame) {
        for (SwitchStringMinigame pair : $.playersNotAllowedToJoinSpecificMinigames.toArray(new SwitchStringMinigame[0])) {
            String username = pair.getArg0();
            ServerMinigame notAllowedToJoinMinigame = pair.getArg1();
            if (minigame.toString().equals(notAllowedToJoinMinigame.toString())) {
                if (username.equals(player.getName()))
                    return true;
            }
        }
        return false;
    }

    public static void setEnableEffects(Player player, boolean value) {
        String path = "config." + player.getUniqueId().toString();
        Server.getInstance().getPlugin().getConfig().set(path + ".effects", value);
    }

    public static boolean isEffectsEnabled(Player player) {
        String path = "config." + player.getUniqueId().toString();
        if (Server.getInstance().getPlugin().getConfig().contains(path + ".effects")) {
            return Server.getInstance().getPlugin().getConfig().getBoolean(path + ".effects");
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
            sb.append(parts[0]).append(parts[0]);
            sb.append(lineOne);
            sb.append('\n');
        } else {
            sb.append(lineOne).append(strLineOneSpaces);
        }
        if (centered[1]) {
            final int middle = strLineTwoSpaces.length() / 2;
            String[] parts = {strLineTwoSpaces.substring(0, middle), strLineTwoSpaces.substring(middle)};
            sb.append(parts[0]).append(parts[0]);
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

    public static String scanStringArrayAndSplitBy(String[] array, char[] split) {
        for (String string : array) {
            if (string.contains(new String(split))) {
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
        if (!Link$.isPluginEnabled("AuthMe")) {
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
        if (minigame == ServerMinigame.PRISON)
            if (!Server.getInstance().getPlugin().getConfig().getBoolean("settings.bungeecord", false))
                return false;
        if (minigame == ServerMinigame.DATED)
            if (!Server.getInstance().getPlugin().getConfig().getBoolean("settings.bungeecord", false))
                return false;
        if (minigame == ServerMinigame.FACTIONS) {
            if (!Link$.isPluginEnabled("Factions"))
                return false;
        }
        if (Server.getInstance().getPlugin().getConfig().contains("settings.enable." + minigame.toString().toLowerCase())) {
            return Server.getInstance().getPlugin().getConfig().getBoolean("settings.enable." + minigame.toString().toLowerCase());
        } else {
            return true;
        }
    }

    public static void teleport(Entity entity, Location loc) {
        try {
            Method method = Entity.class.getMethod("teleportAsync", Location.class);
            method.invoke(entity, loc);
        } catch (Exception ex) {
            entity.teleport(loc);
        }
    }

    public static boolean isWelcomeMessageEnabled() {
        if (Server.getInstance().getPlugin().getConfig().contains("settings.enable.welcomeMessage")) {
            return Server.getInstance().getPlugin().getConfig().getBoolean("settings.enable.welcomeMessage");
        } else {
            return true;
        }
    }

    public static boolean isCustomJoinMessageEnabled() {
        if (Server.getInstance().getPlugin().getConfig().contains("settings.customJoinMessage")) {
            return Server.getInstance().getPlugin().getConfig().getBoolean("settings.customQuitMessage");
        } else {
            return true;
        }
    }

    public static boolean isCustomQuitMessageEnabled() {
        if (Server.getInstance().getPlugin().getConfig().contains("settings.customJoinMessage")) {
            return Server.getInstance().getPlugin().getConfig().getBoolean("settings.customQuitMessage");
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
            switch (resource.getProtocol()) {
                case "file":
                    try {
                        d = new Date(new File(resource.toURI()).lastModified());
                    } catch (URISyntaxException ignored) {
                    }
                    break;
                case "jar": {
                    String path = resource.getPath();
                    d = new Date(new File(path.substring(5, path.indexOf("!"))).lastModified());
                    break;
                }
                case "zip": {
                    String path = resource.getPath();
                    File jarFileOnDisk = new File(path.substring(0, path.indexOf("!")));
                    try (JarFile jf = new JarFile(jarFileOnDisk)) {
                        ZipEntry ze = jf.getEntry(path.substring(path.indexOf("!") + 2));
                        long zeTimeLong = ze.getTime();
                        d = new Date(zeTimeLong);
                    } catch (IOException | RuntimeException ignored) {
                    }
                    break;
                }
            }
        }
        return d;
    }

    public static boolean isNothing(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
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

    public static double getSinglePricing(double shopAmount, double shopPrice) {
        return shopPrice / shopAmount;
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
        boolean isAuthenticated;
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
        return command.equalsIgnoreCase("/autolog");
    }

    public static boolean isBlockLog(Block block) {
        return isMaterialLog(block.getType());
    }

    public static boolean isPostSign(Material material) {
        switch (material) {
            case OAK_SIGN:
            case SPRUCE_SIGN:
            case JUNGLE_SIGN:
            case DARK_OAK_SIGN:
            case BIRCH_SIGN:
            case ACACIA_SIGN:
                return true;
            default:
                return false;
        }
    }

    public static boolean isWallSign(Material material) {
        switch (material) {
            case OAK_WALL_SIGN:
            case SPRUCE_WALL_SIGN:
            case JUNGLE_WALL_SIGN:
            case DARK_OAK_WALL_SIGN:
            case BIRCH_WALL_SIGN:
            case ACACIA_WALL_SIGN:
                return true;
            default:
                return false;
        }
    }

    public static boolean isMaterialLog(Material material) {
        switch (material) {
            case ACACIA_LOG:
            case STRIPPED_SPRUCE_LOG:
            case STRIPPED_OAK_LOG:
            case STRIPPED_JUNGLE_LOG:
            case STRIPPED_DARK_OAK_LOG:
            case STRIPPED_BIRCH_LOG:
            case STRIPPED_ACACIA_LOG:
            case SPRUCE_LOG:
            case OAK_LOG:
            case JUNGLE_LOG:
            case DARK_OAK_LOG:
            case BIRCH_LOG:
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
            case SPRUCE_LEAVES:
            case OAK_LEAVES:
            case JUNGLE_LEAVES:
            case DARK_OAK_LEAVES:
            case BIRCH_LEAVES:
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
            case YELLOW_STAINED_GLASS:
            case WHITE_STAINED_GLASS:
            case RED_STAINED_GLASS:
            case PURPLE_STAINED_GLASS:
            case PINK_STAINED_GLASS:
            case ORANGE_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS:
            case LIME_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS:
            case GREEN_STAINED_GLASS:
            case GRAY_STAINED_GLASS:
            case CYAN_STAINED_GLASS:
            case BROWN_STAINED_GLASS:
            case BLUE_STAINED_GLASS:
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
            case YELLOW_STAINED_GLASS:
            case WHITE_STAINED_GLASS:
            case RED_STAINED_GLASS:
            case PURPLE_STAINED_GLASS:
            case PINK_STAINED_GLASS:
            case ORANGE_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS:
            case LIME_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS:
            case GREEN_STAINED_GLASS:
            case GRAY_STAINED_GLASS:
            case CYAN_STAINED_GLASS:
            case BROWN_STAINED_GLASS:
            case BLUE_STAINED_GLASS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isRawItemRepairable(ItemStack stack) {
        return Directory.repairableItems.contains(stack.getType());
    }

    public static boolean isRawArmorRepairable(ItemStack stack) {
        return Directory.repairableArmor.contains(stack.getType());
    }

    public static boolean isRawRepairable(ItemStack stack) {
        return Directory.repairableArmor.contains(stack.getType()) || Directory.repairableItems.contains(stack.getType());
    }

    public static boolean isRepairable(ItemStack stack) {
        if (stack.getDurability() == (short) 0)
            return false;
        return Directory.repairableArmor.contains(stack.getType()) || Directory.repairableItems.contains(stack.getType());
    }

    public static ItemStack removeLore(ItemStack item) {
        ItemMeta cloneItemMeta = item.getItemMeta();
        cloneItemMeta.setLore(new ArrayList<>());
        item.setItemMeta(cloneItemMeta);
        return item;
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
        player.getInventory().setHelmet(Link$.createMaterial(Material.AIR));
        player.getInventory().setChestplate(Link$.createMaterial(Material.AIR));
        player.getInventory().setLeggings(Link$.createMaterial(Material.AIR));
        player.getInventory().setBoots(Link$.createMaterial(Material.AIR));
        player.updateInventory();
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static boolean createDataFolder() {
        if (!Server.getInstance().getPlugin().getDataFolder().exists()) {
            Server.getInstance().getPlugin().getDataFolder().mkdir();
            return true;
        }
        return false;
    }

    public static Location getZoneLocation(String zone) {
        if (Server.getInstance().getWarpConfig().getData().contains(zone)) {
            World world = Server.getInstance().getPlugin().getServer().getWorld(Server.getInstance().getWarpConfig().getData().getString(zone + ".world"));
            double x = Server.getInstance().getWarpConfig().getData().getDouble(zone + ".x");
            double y = Server.getInstance().getWarpConfig().getData().getDouble(zone + ".y");
            double z = Server.getInstance().getWarpConfig().getData().getDouble(zone + ".z");
            float yaw = (float) Server.getInstance().getWarpConfig().getData().getDouble(zone + ".yaw");
            float pitch = (float) Server.getInstance().getWarpConfig().getData().getDouble(zone + ".pitch");
            return new Location(world, x, y, z, yaw, pitch);
        }
        Bukkit.getConsoleSender().sendMessage("Critical error: Failed to find a saved zone named '" + zone + "'");
        World basicWorld = Bukkit.getWorlds().get(0);
        return new Location(basicWorld, 0, basicWorld.getHighestBlockYAt(0, 0), 0, 0, 0);
    }

    public static void sendMessageToNearbyPlayers(Location centre, String message, int radius) {
        for (Entity entity : centre.getWorld().getNearbyEntities(centre, radius, radius, radius)) {
            if (entity instanceof Player) {
                entity.sendMessage(message);
            }
        }
    }

    public static Player[] getStaffOnline() {
        return getStaffOnline(Server.getInstance().getPlugin(), null);
    }

    public static Player[] getStaffOnline(Plugin plugin, Player ignorePlayer) {
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (Link$.getRankId(player.getUniqueId()) > -1)
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
        Server.getInstance().getPlugin().getConfig().set(path + ".nickname", nickname);
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

    public static ServerMinigame getLastMinigame(UUID uuid) {
        if (Bukkit.getPlayer(uuid) != null)
            return getCurrentMinigame(Bukkit.getPlayer(uuid));
        if (Server.getInstance().getPlugin().getConfig().contains("config." + uuid.toString() + ".lastMinigame")) {
            String minigameLower = Server.getInstance().getPlugin().getConfig().getString("config." + uuid.toString() + ".lastMinigame");
            return ServerMinigame.valueOf(minigameLower.toUpperCase());
        }
        return ServerMinigame.UNKNOWN;
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
            return Server.getInstance().getFactions();
        if (domain.equals("kitpvp"))
            return Server.getInstance().getKitpvp();
        if (domain.equals("skyfight"))
            return new ArrayList<>(Server.getInstance().getSkyfight().keySet());
        if (domain.equals("creative"))
            return Server.getInstance().getCreative();
        if (domain.equals("survival"))
            return Server.getInstance().getSurvival();
        if (domain.equals("skyblock"))
            return Server.getInstance().getSkyblock();
        if (domain.equals("hub"))
            return Server.getInstance().getHub();
        return null;
    }

    public static String getMinigameDomain(Player player) {
        if (Server.getInstance().getFactions().contains(player.getUniqueId()))
            return "factions";
        if (Server.getInstance().getKitpvp().contains(player.getUniqueId()))
            return "kitpvp";
        if (Server.getInstance().getSkyfight().containsKey(player.getUniqueId()))
            return "skyfight";
        if (Server.getInstance().getCreative().contains(player.getUniqueId()))
            return "creative";
        if (Server.getInstance().getSurvival().contains(player.getUniqueId()))
            return "survival";
        if (Server.getInstance().getSkyblock().contains(player.getUniqueId()))
            return "skyblock";
        if (Server.getInstance().getHub().contains(player.getUniqueId()))
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
            case 24:
            case 21:
            case 19:
            case 16:
            case 14:
            case 12:
            case 9:
            case 7:
            case 4:
            case 2:
                return true;
            case 23:
            case 22:
            case 20:
            case 18:
            case 17:
            case 15:
            case 13:
            case 11:
            case 10:
            case 8:
            case 6:
            case 5:
            case 3:
            case 1:
            default:
                return false;
        }
    }

    public static Object getGriefPrevention() {
        return Bukkit.getPluginManager().getPlugin("GriefPrevention");
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
            return faction.isWarZone() || faction.isSafeZone();
        }
        return true;
    }

    public static boolean isWithinUnclaimedLand(Location location, Player player, boolean worldGuardOnly) {
        int size = 0;
        if (getGriefPrevention() != null)
            if (me.skorrloregaming.GriefPreventionAPI.hasClaimInLocation(player, location)) {
                return false;
            }
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
                return FPlayers.getInstance().getByPlayer(player).isInOwnTerritory();
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
                Server.getInstance().getPlugin().getConfig().set(path + ".marry.marriedTo", "0");
                Server.getInstance().getPlugin().getConfig().set(path + ".marry.marriageId", "0");
                id = 0;
            } else {
                Server.getInstance().getPlugin().getConfig().set(path + ".marry.marriedTo", targetPlayer.getUniqueId().toString());
                Server.getInstance().getPlugin().getConfig().set(path + ".marry.marriageId", marriageId);
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
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".marry.marriedTo")) {
                String value = String.valueOf(Server.getInstance().getPlugin().getConfig().getString(path + ".marry.marriedTo"));
                if (value.equals("0"))
                    return null;
                return UUID.fromString(value);
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".marry", "0");
            return null;
        }

        public static int getPlayerMarriageId(Player player) {
            String path = "config." + player.getUniqueId().toString();
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".marry.marriageId")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".marry.marriageId"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".marry.marriageId", "0");
            return 0;
        }

        public static void setPlayerGender(Player player, MarriageGender gender) {
            String path = "config." + player.getUniqueId().toString();
            Server.getInstance().getPlugin().getConfig().set(path + ".marry.gender", gender.toString().toLowerCase());
        }

        public static MarriageGender getPlayerGender(OfflinePlayer player) {
            String path = "config." + player.getUniqueId().toString();
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".marry.gender")) {
                String value = String.valueOf(Server.getInstance().getPlugin().getConfig().getString(path + ".marry.gender"));
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
            Server.getInstance().getPlugin().getConfig().set(path + ".marry.marriedPvp", pvp + "");
        }

        public static boolean getPlayerMarriedPvp(Player player) {
            String path = "config." + player.getUniqueId().toString();
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".marry.marriedPvp")) {
                String value = String.valueOf(Server.getInstance().getPlugin().getConfig().getString(path + ".marry.marriedPvp")).toLowerCase();
                return Boolean.parseBoolean(value);
            }
            return true;
        }

        public static void setPlayerSwearFilter(Player player, boolean pvp) {
            String path = "config." + player.getUniqueId().toString();
            Server.getInstance().getPlugin().getConfig().set(path + ".marry.swearFilter", pvp + "");
        }

        public static boolean getPlayerSwearFilter(Player player) {
            String path = "config." + player.getUniqueId().toString();
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".marry.swearFilter")) {
                String value = String.valueOf(Server.getInstance().getPlugin().getConfig().getString(path + ".marry.swearFilter")).toLowerCase();
                return Boolean.parseBoolean(value);
            }
            return true;
        }
    }

    public static class Lobby {
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "hub" + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    public static class Skyfight {
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "skyfight" + ChatColor.GRAY + "] " + ChatColor.RESET;

        public static void refreshScoreboard(org.bukkit.entity.Player player, boolean clearValues) {
            UUID[] skyfightPlayers = Server.getInstance().getSkyfight().keySet().toArray(new UUID[0]);
            Hashtable<String, Integer> list = new Hashtable<>();
            int best = 0;
            for (UUID id : skyfightPlayers) {
                org.bukkit.entity.Player otherPlayer = Bukkit.getPlayer(id);
                int score = Server.getInstance().getSkyfight().get(otherPlayer.getUniqueId()).getScore();
                if (score > best)
                    best = score;
            }
            list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Leaderboard", (int) ((Math.floor(best / 5) + 1) * 5));
            for (UUID id : skyfightPlayers) {
                org.bukkit.entity.Player otherPlayer = Bukkit.getPlayer(id);
                Player osfPlayer = Server.getInstance().getSkyfight().get(otherPlayer.getUniqueId());
                ChatColor prefix = ChatColor.RESET;
                if (osfPlayer.getTeamValue() == Team.BLUE) {
                    prefix = ChatColor.BLUE;
                } else if (osfPlayer.getTeamValue() == Team.RED) {
                    prefix = ChatColor.RED;
                } else if (osfPlayer.getTeamValue() == Team.GREEN) {
                    prefix = ChatColor.GREEN;
                } else if (osfPlayer.getTeamValue() == Team.YELLOW) {
                    prefix = ChatColor.YELLOW;
                } else if (osfPlayer.getTeamValue() == Team.PINK) {
                    prefix = ChatColor.LIGHT_PURPLE;
                }
                list.put(ChatColor.GOLD + "│ " + prefix + otherPlayer.getName(), Server.getInstance().getSkyfight().get(otherPlayer.getUniqueId()).getScore());
            }
            $.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
        }

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
                    Server.getInstance().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> {
                        if (!(damagee == null) && damagee.getName().equals(taggedDamagee.getName()) && damageeHash == taggedDamageeHash)
                            setDamagee(null);
                    }, 20L * 6L));
                }
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
    }

    public static class Factions {
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "factions" + ChatColor.GRAY + "] " + ChatColor.RESET;
        public static String[] validKits = new String[]{"recruit", "donator", "redstone", "obsidian", "bedrock"};
        public static ItemStack[] kitRecruit = new ItemStack[]{Link$.createMaterial(Material.STONE_SWORD), Link$.createMaterial(Material.STONE_PICKAXE), Link$.createMaterial(Material.STONE_AXE), Link$.createMaterial(Material.STONE_SHOVEL), Link$.createMaterial(Material.COOKED_BEEF, 8), Link$.createMaterial(Material.LEATHER_HELMET), Link$.createMaterial(Material.LEATHER_CHESTPLATE), Link$.createMaterial(Material.LEATHER_LEGGINGS), Link$.createMaterial(Material.LEATHER_BOOTS)};
        public static ItemStack[] kitDonator = new ItemStack[]{Link$.createMaterial(Material.IRON_SWORD), Link$.createMaterial(Material.IRON_PICKAXE), Link$.createMaterial(Material.IRON_AXE), Link$.createMaterial(Material.IRON_SHOVEL), Link$.createMaterial(Material.COOKED_BEEF, 16), Link$.createMaterial(Material.IRON_HELMET), Link$.createMaterial(Material.LEATHER_CHESTPLATE), Link$.createMaterial(Material.LEATHER_LEGGINGS), Link$.createMaterial(Material.IRON_BOOTS)};
        public static ItemStack[] kitRedstone = new ItemStack[]{Link$.createMaterial(Material.IRON_SWORD), Link$.createMaterial(Material.DIAMOND_PICKAXE), Link$.createMaterial(Material.IRON_AXE), Link$.createMaterial(Material.IRON_SHOVEL), Link$.createMaterial(Material.COOKED_BEEF, 24), Link$.createMaterial(Material.IRON_HELMET), Link$.createMaterial(Material.IRON_CHESTPLATE), Link$.createMaterial(Material.IRON_LEGGINGS), Link$.createMaterial(Material.IRON_BOOTS)};
        public static ItemStack[] kitObsidian = new ItemStack[]{Link$.createMaterial(Material.DIAMOND_SWORD), Link$.createMaterial(Material.DIAMOND_PICKAXE), Link$.createMaterial(Material.DIAMOND_AXE), Link$.createMaterial(Material.DIAMOND_SHOVEL), Link$.createMaterial(Material.COOKED_BEEF, 32), Link$.createMaterial(Material.DIAMOND_HELMET), Link$.createMaterial(Material.IRON_CHESTPLATE), Link$.createMaterial(Material.IRON_LEGGINGS), Link$.createMaterial(Material.DIAMOND_BOOTS)};
        public static ItemStack[] kitBedrock = new ItemStack[]{Link$.createMaterial(Material.DIAMOND_SWORD), Link$.createMaterial(Material.DIAMOND_PICKAXE), Link$.createMaterial(Material.DIAMOND_AXE), Link$.createMaterial(Material.DIAMOND_SHOVEL), Link$.createMaterial(Material.COOKED_BEEF, 48), Link$.createMaterial(Material.DIAMOND_HELMET), Link$.createMaterial(Material.DIAMOND_CHESTPLATE), Link$.createMaterial(Material.DIAMOND_LEGGINGS), Link$.createMaterial(Material.DIAMOND_BOOTS)};

        public static int getPlayerKills(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".factions";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".kills")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".kills"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".kills", "0");
            return 0;
        }

        public static void setPlayerKills(Player player, int value) {
            String path = "config." + player.getUniqueId().toString() + ".factions";
            Server.getInstance().getPlugin().getConfig().set(path + ".kills", value + "");
        }

        public static int getPlayerDeaths(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".factions";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".deaths")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".deaths"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".deaths", "0");
            return 0;
        }

        public static void setPlayerDeaths(Player player, int value) {
            String path = "config." + player.getUniqueId().toString() + ".factions";
            Server.getInstance().getPlugin().getConfig().set(path + ".deaths", value + "");
        }

        public static void refreshScoreboard(Player player, boolean clearValues) {
            if (Link$.isPluginEnabled("mcMMO")) {
                try {
                    if (com.gmail.nossr50.util.scoreboards.ScoreboardManager.isBoardShown(player.getName()))
                        return;
                } catch (Exception ex) {
                }
            }
            DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
            Hashtable<String, Integer> list = new Hashtable<>();
            int currentPlayerKills = $.Factions.getPlayerKills(player);
            int currentPlayerDeaths = $.Factions.getPlayerDeaths(player);
            double currentPlayerCash = EconManager.retrieveCash(player, "factions");
            FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
            Faction faction = fplayer.getFaction();
            list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Faction", 9);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Tag: " + ChatColor.RESET + faction.getTag(), 8);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Power: " + ChatColor.RESET + faction.getPowerRounded() + "/" + faction.getPowerMaxRounded(), 7);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Members: " + ChatColor.RESET + faction.getFPlayers().size(), 6);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Land: " + ChatColor.RESET + faction.getLandRounded(), 5);
            list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 4);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 3);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Kills: " + ChatColor.RESET + formatter.format(currentPlayerKills), 2);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Deaths: " + ChatColor.RESET + formatter.format(currentPlayerDeaths), 1);
            $.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
        }
    }

    public static class Survival {
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "survival" + ChatColor.GRAY + "] " + ChatColor.RESET;
        public static String[] validKits = new String[]{"recruit"};
        public static ItemStack[] kitRecruit = new ItemStack[]{Link$.createMaterial(Material.STONE_SWORD), Link$.createMaterial(Material.STONE_PICKAXE), Link$.createMaterial(Material.STONE_AXE), Link$.createMaterial(Material.STONE_SHOVEL), Link$.createMaterial(Material.COOKED_BEEF, 16), Link$.createMaterial(Material.LEATHER_HELMET), Link$.createMaterial(Material.LEATHER_CHESTPLATE), Link$.createMaterial(Material.LEATHER_LEGGINGS), Link$.createMaterial(Material.LEATHER_BOOTS), Link$.createMaterial(Material.GOLDEN_SHOVEL), Link$.createMaterial(Material.COMPASS), Link$.createMaterial(Material.MAP)};
    }

    public static class Kitpvp {
        public static final int DONOR_MAX_UPGRADE_VALUE = 7;
        public static final int DEFAULT_MAX_UPGRADE_VALUE = 3;
        public static final int WEAPON_AXE = 0;
        public static final int WEAPON_SWORD = 1;
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "kitpvp" + ChatColor.GRAY + "] " + ChatColor.RESET;
        public static String[] validKits = new String[]{"starter", "potions"};
        public static ItemStack kitStarter1_Weapon = Link$.createMaterial(Material.STONE_AXE);
        public static ItemStack kitStarter2_4_Weapon = Link$.createMaterial(Material.IRON_AXE);
        public static ItemStack kitStarter5_8_Weapon = Link$.createMaterial(Material.DIAMOND_AXE);
        public static ItemStack[] kitStarter1_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.LEATHER_HELMET), Link$.createMaterial(Material.LEATHER_CHESTPLATE), Link$.createMaterial(Material.LEATHER_LEGGINGS), Link$.createMaterial(Material.LEATHER_BOOTS)};
        public static ItemStack[] kitStarter2_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.IRON_HELMET), Link$.createMaterial(Material.LEATHER_CHESTPLATE), Link$.createMaterial(Material.LEATHER_LEGGINGS), Link$.createMaterial(Material.IRON_BOOTS)};
        public static ItemStack[] kitStarter3_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.IRON_HELMET), Link$.createMaterial(Material.CHAINMAIL_CHESTPLATE), Link$.createMaterial(Material.CHAINMAIL_LEGGINGS), Link$.createMaterial(Material.IRON_BOOTS)};
        public static ItemStack[] kitStarter4_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.IRON_HELMET), Link$.createMaterial(Material.IRON_CHESTPLATE), Link$.createMaterial(Material.IRON_LEGGINGS), Link$.createMaterial(Material.IRON_BOOTS)};
        public static ItemStack[] kitStarter5_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.IRON_HELMET), Link$.createMaterial(Material.IRON_CHESTPLATE), Link$.createMaterial(Material.IRON_LEGGINGS), Link$.createMaterial(Material.IRON_BOOTS)};
        public static ItemStack[] kitStarter6_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.DIAMOND_HELMET), Link$.createMaterial(Material.IRON_CHESTPLATE), Link$.createMaterial(Material.IRON_LEGGINGS), Link$.createMaterial(Material.DIAMOND_BOOTS), Link$.createMaterial(Material.GOLDEN_APPLE, 1)};
        public static ItemStack[] kitStarter7_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.DIAMOND_HELMET), Link$.createMaterial(Material.IRON_CHESTPLATE), Link$.createMaterial(Material.DIAMOND_LEGGINGS), Link$.createMaterial(Material.DIAMOND_BOOTS), Link$.createMaterial(Material.GOLDEN_APPLE, 2)};
        public static ItemStack[] kitStarter8_Items = new ItemStack[]{Link$.createMaterial(Material.BOW), Link$.createMaterial(Material.ARROW, 24), Link$.createMaterial(Material.DIAMOND_HELMET), Link$.createMaterial(Material.DIAMOND_CHESTPLATE), Link$.createMaterial(Material.DIAMOND_LEGGINGS), Link$.createMaterial(Material.DIAMOND_BOOTS), Link$.createMaterial(Material.GOLDEN_APPLE, 3)};
        public static ItemStack[] kitPotions = new ItemStack[]{Link$.addLore(Link$.createMaterial(Material.BLAZE_ROD, 1, ChatColor.LIGHT_PURPLE + "Enchanted Wand"), new String[]{"Using this wand, you will gain defined potion effects."})};

        public static int getPreferredWeaponType(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".kitpvp";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".preferredWeapon")) {
                String weapon = Server.getInstance().getPlugin().getConfig().getString(path + ".preferredWeapon");
                if (weapon.equals("AXE")) {
                    return WEAPON_AXE;
                } else if (weapon.equals("SWORD")) {
                    return WEAPON_SWORD;
                }
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".preferredWeapon", "AXE");
            return WEAPON_AXE;
        }

        public static void setPreferredWeaponType(Player player, int type) {
            String path = "config." + player.getUniqueId().toString() + ".kitpvp";
            if (type == WEAPON_AXE) {
                Server.getInstance().getPlugin().getConfig().set(path + ".preferredWeapon", "AXE");
            } else if (type == WEAPON_SWORD) {
                Server.getInstance().getPlugin().getConfig().set(path + ".preferredWeapon", "SWORD");
            }
        }

        public static int getPlayerKills(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".kitpvp";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".kills")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".kills"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".kills", "0");
            return 0;
        }

        public static void setPlayerKills(Player player, int value) {
            String path = "config." + player.getUniqueId().toString() + ".kitpvp";
            Server.getInstance().getPlugin().getConfig().set(path + ".kills", value + "");
        }

        public static int getPlayerDeaths(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".kitpvp";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".deaths")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".deaths"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".deaths", "0");
            return 0;
        }

        public static void setPlayerDeaths(Player player, int value) {
            String path = "config." + player.getUniqueId().toString() + ".kitpvp";
            Server.getInstance().getPlugin().getConfig().set(path + ".deaths", value + "");
        }

        public static int getUpgradeCount(Player player) {
            String path = "config." + player.getUniqueId().toString();
            if (!Server.getInstance().getPlugin().getConfig().contains(path + ".kitpvp.upgrades"))
                setUpgradeCount(player, 0);
            int upgrades = Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".kitpvp.upgrades"));
            if (upgrades > DONOR_MAX_UPGRADE_VALUE)
                upgrades = DONOR_MAX_UPGRADE_VALUE;
            return upgrades;
        }

        public static void setUpgradeCount(Player player, int upgrades) {
            if (upgrades > DONOR_MAX_UPGRADE_VALUE)
                upgrades = DONOR_MAX_UPGRADE_VALUE;
            String path = "config." + player.getUniqueId().toString();
            Server.getInstance().getPlugin().getConfig().set(path + ".kitpvp.upgrades", upgrades + "");
        }

        public static int getPreferredUpgrade(Player player) {
            String path = "config." + player.getUniqueId().toString();
            if (!Server.getInstance().getPlugin().getConfig().contains(path + ".kitpvp.preferredUpgrade"))
                setPreferredUpgrade(player, getUpgradeCount(player));
            int preferredUpgrade = Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".kitpvp.preferredUpgrade"));
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
            Server.getInstance().getPlugin().getConfig().set(path + ".kitpvp.preferredUpgrade", upgrade + "");
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
                weapon = Link$.createMaterial(Material.STONE_SWORD);
            if (!axesOnly && swordType && weapon.getType() == Material.IRON_AXE)
                weapon = Link$.createMaterial(Material.IRON_SWORD);
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
                kitArray.set(0, Link$.createMaterial(Material.STONE_SWORD));
            if (!axesOnly && swordType && kitArray.get(0).getType() == Material.IRON_AXE)
                kitArray.set(0, Link$.createMaterial(Material.IRON_SWORD));
            if (!axesOnly && swordType && kitArray.get(0).getType() == Material.DIAMOND_AXE)
                kitArray.set(0, Link$.createMaterial(Material.DIAMOND_SWORD));
            return kitArray.toArray(new ItemStack[0]);
        }

        public static void refreshScoreboard(Player player, boolean clearValues) {
            DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
            Hashtable<String, Integer> list = new Hashtable<>();
            int currentPlayerKills = $.Kitpvp.getPlayerKills(player);
            int currentPlayerDeaths = $.Kitpvp.getPlayerDeaths(player);
            int currentPlayerDPK = currentPlayerKills / 50;
            double currentPlayerCash = EconManager.retrieveCash(player, "kitpvp");
            list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 5);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 4);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Kills: " + ChatColor.RESET + formatter.format(currentPlayerKills), 3);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Deaths: " + ChatColor.RESET + formatter.format(currentPlayerDeaths), 2);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Level: " + ChatColor.RESET + formatter.format(currentPlayerDPK + 1), 1);
            $.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
        }
    }

    public static class Creative {
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "creative" + ChatColor.GRAY + "] " + ChatColor.RESET;
    }

    public static class Skyblock {
        public static String tag = ChatColor.GRAY + "[" + ChatColor.RESET + "skyblock" + ChatColor.GRAY + "] " + ChatColor.RESET;

        public static int getPlayerPlacedBlocks(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".skyblock";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".placed")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".placed"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".placed", "0");
            return 0;
        }

        public static void setPlayerPlacedBlocks(Player player, int value) {
            String path = "config." + player.getUniqueId().toString() + ".skyblock";
            Server.getInstance().getPlugin().getConfig().set(path + ".placed", value + "");
        }

        public static int getPlayerBrokenBlocks(Player player) {
            String path = "config." + player.getUniqueId().toString() + ".skyblock";
            if (Server.getInstance().getPlugin().getConfig().contains(path) && Server.getInstance().getPlugin().getConfig().contains(path + ".broken")) {
                return Integer.parseInt(Server.getInstance().getPlugin().getConfig().getString(path + ".broken"));
            }
            Server.getInstance().getPlugin().getConfig().set(path + ".broken", "0");
            return 0;
        }

        public static void setPlayerBrokenBlocks(Player player, int value) {
            String path = "config." + player.getUniqueId().toString() + ".skyblock";
            Server.getInstance().getPlugin().getConfig().set(path + ".broken", value + "");
        }

        public static void refreshScoreboard(Player player, boolean clearValues) {
            if (!Link$.isPluginEnabled("uSkyBlock")) {
                return;
            }
            uSkyBlockAPI api = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");
            if (Link$.isPluginEnabled("mcMMO")) {
                try {
                    if (com.gmail.nossr50.util.scoreboards.ScoreboardManager.isBoardShown(player.getName()))
                        return;
                } catch (Exception ex) {
                }
            }
            int members = 0, maxmembers = 0, level = 0;
            try {
                members = api.getIslandInfo(player).getPartySize();
                maxmembers = api.getIslandInfo(player).getMaxPartySize();
                level = (int) api.getIslandLevel(player);
            } catch (Exception ex) {
            }
            int placedBlocks = $.Skyblock.getPlayerPlacedBlocks(player);
            int brokenBlocks = $.Skyblock.getPlayerBrokenBlocks(player);
            double currentPlayerCash = EconManager.retrieveCash(player, "skyblock");
            DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
            Hashtable<String, Integer> list = new Hashtable<>();
            if (maxmembers > 0) {
                list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Island", 8);
                list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Level: " + ChatColor.RESET + level, 7);
                list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Members: " + ChatColor.RESET + members + "/" + maxmembers, 6);
                list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Size: " + ChatColor.RESET + "128 x 128", 5);
                list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 4);
            } else {
                list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 4);
            }
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 3);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Placed: " + ChatColor.RESET + formatter.format(placedBlocks), 2);
            list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Broken: " + ChatColor.RESET + formatter.format(brokenBlocks), 1);
            $.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
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
                if (Server.getInstance().getPluginDebug())
                    Logger.debug("Using existing scoreboard for " + player.getName());
            } else {
                board = manager.getNewScoreboard();
                if (Server.getInstance().getPluginDebug())
                    Logger.debug("Generated new scoreboard for " + player.getName());
            }
            boolean found = false;
            for (Objective objective : board.getObjectives()) {
                if (objective.getName().equals("sidebar-obj")) {
                    found = true;
                    if (Server.getInstance().getPluginDebug())
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
                    if (Server.getInstance().getPluginDebug())
                        Logger.debug("Override! Generating new sidebar objective for " + player.getName());
                } else {
                    if (Server.getInstance().getPluginDebug())
                        Logger.debug("Using existing sidebar objective for " + player.getName());
                }
            } else {
                objective = board.registerNewObjective("sidebar-obj", "dummy", title);
                if (Server.getInstance().getPluginDebug())
                    Logger.debug("Generating new sidebar objective for " + player.getName());
            }
            if (!(objective.getDisplayName().equals(title))) {
                objective.unregister();
                objective = board.registerNewObjective("sidebar-obj", "dummy", title);
                if (Server.getInstance().getPluginDebug())
                    Logger.debug("Mismatch! Generating new sidebar objective for " + player.getName());
            }
            int changed = 0;
            if (replaceIntScoreValues) {
                if (Server.getInstance().getPluginDebug())
                    Logger.debug("Recursively replacing appropriate scores on scoreboard for " + player.getName());
            } else {
                if (Server.getInstance().getPluginDebug())
                    Logger.debug("Recursively setting all scores on scoreboard for " + player.getName());
            }
            for (Entry<String, Integer> score : table.entrySet()) {
                if (!replaceIntScoreValues) {
                    Score objEntry = objective.getScore(score.getKey());
                    objEntry.setScore(score.getValue());
                    changed++;
                } else {
                    changed += replaceScore(objective, score.getValue(), score.getKey());
                }
            }
            if (changed > 0) {
                if (Server.getInstance().getPluginDebug())
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
