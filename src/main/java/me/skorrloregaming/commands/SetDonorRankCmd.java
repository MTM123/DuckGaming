package me.skorrloregaming.commands;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.discord.DiscordBot;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.bukkit.entity.Player;

public class SetDonorRankCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && $.getRankId((Player) sender) < 3) {
			$.playLackPermissionMessage(sender);
			return true;
		}
		if (!(sender instanceof Player) && !sender.isOp()) {
			$.playLackPermissionMessage(sender);
			return true;
		}
		if (!$.isRankingEnabled()) {
			sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "This ranking system has been disabled.");
		}
		if (!(args.length == 2)) {
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <rank>");
			String s = "";
			for (String str : $.validDonorRanks) {
				s += ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
			}
			s = s.substring(0, s.lastIndexOf(", "));
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
		} else {
			OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				if ($.validDonorRanks.contains(args[1].toLowerCase())) {
					Server.getSqlDatabase().set("donorRank", targetPlayer.getUniqueId().toString(), args[1].toLowerCase());
					if ($.isPrefixedRankingEnabled() && targetPlayer.isOnline()) {
						$.flashPlayerDisplayName(targetPlayer.getPlayer());
					}
					String message = Server.getPluginLabel() + ChatColor.RED + targetPlayer.getName() + ChatColor.GRAY + " has been given rank " + ChatColor.RED + WordUtils.capitalize(args[1].toLowerCase());
					Bukkit.broadcastMessage(message);
					message = message.substring(message.indexOf(ChatColor.RED + ""));
					Server.getDiscordBot().broadcast(
							ChatColor.stripColor(message.replace(targetPlayer.getName(), "**" + targetPlayer.getName() + "**"))
							, Channel.SERVER_CHAT, Channel.SERVER_LOG);
				} else {
					sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified rank could not be found.");
					String s = "";
					for (String str : $.validDonorRanks) {
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