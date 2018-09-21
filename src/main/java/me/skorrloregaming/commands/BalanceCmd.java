package me.skorrloregaming.commands;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Server;

public class BalanceCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		if (args.length == 0) {
			String subDomain = $.getMinigameDomain(player);
			String tag = $.getMinigameTag(subDomain);
			sender.sendMessage(tag + ChatColor.GRAY + "Current Balance: " + ChatColor.RED + "$" + formatter.format(EconManager.retrieveCash(player, subDomain)));
		} else {
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage(ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				String subDomain = $.getMinigameDomain(player);
				String tag = $.getMinigameTag(subDomain);
				sender.sendMessage(tag + ChatColor.GRAY + "Balance of " + ChatColor.RED + targetPlayer.getName() + ChatColor.GRAY + ": " + ChatColor.RED + "$" + formatter.format(EconManager.retrieveCash(targetPlayer, subDomain)));
			}
		}
		return false;
	}

}
