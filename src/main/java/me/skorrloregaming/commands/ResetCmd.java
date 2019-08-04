package me.skorrloregaming.commands;

import me.skorrloregaming.$;
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

import me.skorrloregaming.*;

public class ResetCmd implements CommandExecutor {

	private int removeAllPlayerData(ServerMinigame minigame, File playerDataFolder, File altPlayerDataFolder, String subFolder) {
		File dataFolder = new File(playerDataFolder, subFolder);
		File[] listOfFiles = dataFolder.listFiles();
		int deleted = 0;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				if (listOfFiles[i].getName().contains(minigame.toString().toLowerCase())) {
					File backupFolder = new File(altPlayerDataFolder, subFolder);
					File backup = new File(altPlayerDataFolder, listOfFiles[i].getName());
					backup.getParentFile().mkdirs();
					if (backup.exists())
						backup.delete();
					listOfFiles[i].renameTo(backup);
					deleted++;
				}
			}
		}
		return deleted;
	}

	private int removeAllPlayerInfo(ServerMinigame minigame) {
		int reset = 0;
		for (String uuid : ServerGet.get().getPlugin().getConfig().getConfigurationSection("config").getKeys(false)) {
			String path = "config." + uuid;
			int balance = 0;
			switch (minigame) {
				case FACTIONS:
					balance = 250;
					break;
			}
			String game = minigame.toString().toLowerCase();
			if (ServerGet.get().getPlugin().getConfig().contains(path + ".balance." + game))
				ServerGet.get().getPlugin().getConfig().set(path + ".balance." + game, balance + "");
			if (ServerGet.get().getPlugin().getConfig().contains(path + "." + game + ".kills"))
				ServerGet.get().getPlugin().getConfig().set(path + "." + game + ".kills", "0");
			if (ServerGet.get().getPlugin().getConfig().contains(path + "." + game + ".deaths"))
				ServerGet.get().getPlugin().getConfig().set(path + "." + game + ".deaths", "0");
			if (ServerGet.get().getPlugin().getConfig().contains(path + "." + game + ".upgrades"))
				ServerGet.get().getPlugin().getConfig().set(path + "." + game + ".upgrades", "0");
			if (ServerGet.get().getPlugin().getConfig().contains(path + "." + game + ".preferredUpgrade"))
				ServerGet.get().getPlugin().getConfig().set(path + "." + game + ".preferredUpgrade", "0");
			if (ServerGet.get().getPlugin().getConfig().contains(path + "." + game + ".placed"))
				ServerGet.get().getPlugin().getConfig().set(path + "." + game + ".placed", "0");
			if (ServerGet.get().getPlugin().getConfig().contains(path + "." + game + ".broken"))
				ServerGet.get().getPlugin().getConfig().set(path + "." + game + ".broken", "0");
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
				File playerDataFolder = new File(ServerGet.get().getPlugin().getDataFolder(), "Players");
				File altPlayerDataFolder = new File(ServerGet.get().getPlugin().getDataFolder(), "BackupPlayers");
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
				if ((!op.hasPlayedBefore() && !op.isOnline()) || !ServerGet.get().getPlugin().getConfig().contains("config." + op.getUniqueId().toString())) {
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
					return true;
				}
				String msg = ChatColor.RED + "You have been forcibly unregistered from the ServerGet.get().";
				if (op.isOnline())
					op.getPlayer().kickPlayer(msg);
				ServerGet.get().getPlugin().getConfig().set("config." + op.getUniqueId().toString(), null);
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The specified player should now be unregistered.");
			}
		}
		return true;
	}

}
