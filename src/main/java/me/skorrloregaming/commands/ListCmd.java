package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.notify.AlternateClient;
import me.skorrloregaming.notify.Client;

public class ListCmd implements CommandExecutor {

	public static void listOnlinePlayers(AlternateClient sender) {
		StringBuilder kitpvpArray = new StringBuilder();
		StringBuilder factionsArray = new StringBuilder();
		StringBuilder survivalArray = new StringBuilder();
		StringBuilder skyfightArray = new StringBuilder();
		StringBuilder creativeArray = new StringBuilder();
		StringBuilder skyblockArray = new StringBuilder();
		StringBuilder hubArray = new StringBuilder();
		StringBuilder notifyWorkerArray = new StringBuilder();
		int length = 0;
		for (Player player : Bukkit.getOnlinePlayers()) {
			length++;
			String domain = $.getMinigameDomain(player);
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
			} else if (domain.equals("skyblock")) {
				skyblockArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
			} else if (domain.equals("hub")) {
				hubArray.append(ChatColor.RED + player.getName() + ChatColor.GRAY + ", " + ChatColor.RED);
			}
		}
		Client[] clientsArr = Server.getNotifyWorker().clients.toArray(new Client[0]).clone();
		for (int i = 0; i < clientsArr.length; i++) {
			Client client = clientsArr[i];
			notifyWorkerArray.append(ChatColor.RED + client.getUsername() + ChatColor.GRAY + ", " + ChatColor.RED);
		}
		String kitpvpMsg = kitpvpArray.toString();
		String factionsMsg = factionsArray.toString();
		String survivalMsg = survivalArray.toString();
		String skyfightMsg = skyfightArray.toString();
		String creativeMsg = creativeArray.toString();
		String skyblockMsg = skyblockArray.toString();
		String hubMsg = hubArray.toString();
		String notifyWorkerMsg = notifyWorkerArray.toString();
		sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "There are currently " + ChatColor.RED + length + "/" + Bukkit.getMaxPlayers() + ChatColor.GRAY + " players online.");
		sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "There are " + ChatColor.RED + clientsArr.length + "/" + Bukkit.getMaxPlayers() + ChatColor.GRAY + " notify clients online.");
		if (kitpvpMsg.length() > 0) {
			sender.sendMessage($.Kitpvp.tag + ChatColor.RED + kitpvpMsg.substring(0, kitpvpMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (factionsMsg.length() > 0) {
			sender.sendMessage($.Factions.tag + ChatColor.RED + factionsMsg.substring(0, factionsMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (survivalMsg.length() > 0) {
			sender.sendMessage($.Survival.tag + ChatColor.RED + survivalMsg.substring(0, survivalMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (skyfightMsg.length() > 0) {
			sender.sendMessage($.Skyfight.tag + ChatColor.RED + skyfightMsg.substring(0, skyfightMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (creativeMsg.length() > 0) {
			sender.sendMessage($.Creative.tag + ChatColor.RED + creativeMsg.substring(0, creativeMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (skyblockMsg.length() > 0) {
			sender.sendMessage($.Skyblock.tag + ChatColor.RED + skyblockMsg.substring(0, skyblockMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (hubMsg.length() > 0) {
			sender.sendMessage($.Lobby.tag + ChatColor.RED + hubMsg.substring(0, hubMsg.lastIndexOf(", " + ChatColor.RED)));
		}
		if (notifyWorkerMsg.length() > 0) {
			sender.sendMessage($.NotifyWorker.tag + ChatColor.RED + notifyWorkerMsg.substring(0, notifyWorkerMsg.lastIndexOf(", " + ChatColor.RED)));
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		listOnlinePlayers(new AlternateClient(sender));
		return true;
	}

}
