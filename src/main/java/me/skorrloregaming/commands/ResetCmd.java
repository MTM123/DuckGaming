package me.skorrloregaming.commands;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.File;

public class ResetCmd implements CommandExecutor {

    private int removeAllPlayerData(ServerMinigame minigame, File playerDataFolder, File altPlayerDataFolder, String subFolder) {
        File dataFolder = new File(playerDataFolder, subFolder);
        File[] listOfFiles = dataFolder.listFiles();
        int deleted = 0;
        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                if (listOfFile.getName().contains(minigame.toString().toLowerCase())) {
                    File backupFolder = new File(altPlayerDataFolder, subFolder);
                    File backup = new File(altPlayerDataFolder, listOfFile.getName());
                    backup.getParentFile().mkdirs();
                    if (backup.exists())
                        backup.delete();
                    listOfFile.renameTo(backup);
                    deleted++;
                }
            }
        }
        return deleted;
    }

    private int removeAllPlayerInfo(ServerMinigame minigame) {
        int reset = 0;
        for (String uuid : Server.getInstance().getPlugin().getConfig().getConfigurationSection("config").getKeys(false)) {
            String path = "config." + uuid;
            int balance = 0;
            if (minigame == ServerMinigame.FACTIONS) {
                balance = 250;
            }
            String game = minigame.toString().toLowerCase();
            if (Server.getInstance().getPlugin().getConfig().contains(path + ".balance." + game))
                Server.getInstance().getPlugin().getConfig().set(path + ".balance." + game, balance + "");
            if (Server.getInstance().getPlugin().getConfig().contains(path + "." + game + ".kills"))
                Server.getInstance().getPlugin().getConfig().set(path + "." + game + ".kills", "0");
            if (Server.getInstance().getPlugin().getConfig().contains(path + "." + game + ".deaths"))
                Server.getInstance().getPlugin().getConfig().set(path + "." + game + ".deaths", "0");
            if (Server.getInstance().getPlugin().getConfig().contains(path + "." + game + ".upgrades"))
                Server.getInstance().getPlugin().getConfig().set(path + "." + game + ".upgrades", "0");
            if (Server.getInstance().getPlugin().getConfig().contains(path + "." + game + ".preferredUpgrade"))
                Server.getInstance().getPlugin().getConfig().set(path + "." + game + ".preferredUpgrade", "0");
            if (Server.getInstance().getPlugin().getConfig().contains(path + "." + game + ".placed"))
                Server.getInstance().getPlugin().getConfig().set(path + "." + game + ".placed", "0");
            if (Server.getInstance().getPlugin().getConfig().contains(path + "." + game + ".broken"))
                Server.getInstance().getPlugin().getConfig().set(path + "." + game + ".broken", "0");
            reset++;
        }
        return reset;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            Link$.playLackPermissionMessage(sender);
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
            return true;
        } else {
            ServerMinigame minigame;
            if ((minigame = ServerMinigame.valueOf(args[0].toUpperCase())) != null) {
                File playerDataFolder = new File(Server.getInstance().getPlugin().getDataFolder(), "Players");
                File altPlayerDataFolder = new File(Server.getInstance().getPlugin().getDataFolder(), "BackupPlayers");
                int deleted = 0;
                deleted += removeAllPlayerData(minigame, playerDataFolder, altPlayerDataFolder, "Chest");
                deleted += removeAllPlayerData(minigame, playerDataFolder, altPlayerDataFolder, "Information");
                deleted += removeAllPlayerData(minigame, playerDataFolder, altPlayerDataFolder, "Inventory");
                deleted += removeAllPlayerData(minigame, playerDataFolder, altPlayerDataFolder, "Location");
                deleted += removeAllPlayerData(minigame, playerDataFolder, altPlayerDataFolder, "PotionEffects");
                sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Deleted " + ChatColor.RED + deleted + ChatColor.GRAY + " files.");
                int reset = removeAllPlayerInfo(minigame);
                sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Reset " + ChatColor.RED + reset + ChatColor.GRAY + " sections.");
            } else {
                OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0]);
                if ((!op.hasPlayedBefore() && !op.isOnline()) || !Server.getInstance().getPlugin().getConfig().contains("config." + op.getUniqueId().toString())) {
                    sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
                    return true;
                }
                String msg = ChatColor.RED + "You have been forcibly unregistered from the server.";
                if (op.isOnline())
                    op.getPlayer().kickPlayer(msg);
                Server.getInstance().getPlugin().getConfig().set("config." + op.getUniqueId().toString(), null);
                sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The specified player should now be unregistered.");
            }
        }
        return true;
    }

}
