package me.skorrloregaming.commands;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class SetRankCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			$.playLackPermissionMessage(sender);
			return true;
		}
		if (!$.isRankingEnabled()) {
			sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "This ranking system has been disabled.");
		}
		if (!(args.length == 2)) {
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <rank>");
			String s = "";
			for (String str : $.validRanks) {
				s += ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
			}
			s = s.substring(0, s.lastIndexOf(", "));
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
		} else {
			OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				if ($.validRanks.contains(args[1].toLowerCase())) {
					String path = "config." + targetPlayer.getUniqueId().toString();
					Server.getPlugin().getConfig().set(path + ".rank", args[1].toLowerCase());
					if ($.isPrefixedRankingEnabled() && targetPlayer.isOnline()) {
						$.flashPlayerDisplayName(targetPlayer.getPlayer());
					}
					Bukkit.broadcastMessage(Server.getPluginLabel() + ChatColor.RED + targetPlayer.getName() + ChatColor.GRAY + " has been given rank " + ChatColor.RED + WordUtils.capitalize(args[1].toLowerCase()));
				} else {
					sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified rank could not be found.");
					String s = "";
					for (String str : $.validRanks) {
						s += ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
					}
					s = s.substring(0, s.lastIndexOf(", "));
					sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
				}
			}
		}
		return true;
	}

}
