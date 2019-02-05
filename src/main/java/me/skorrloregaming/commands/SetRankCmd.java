package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetRankCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player && Link$.getRankId((Player) sender) < 3) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		if (!(sender instanceof Player) && !sender.isOp()) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		if (!Link$.isRankingEnabled()) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "This ranking system has been disabled.");
		}
		if (!(args.length == 2)) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <rank>");
			String s = "";
			for (String str : Link$.validRanks) {
				s += ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
			}
			s = s.substring(0, s.lastIndexOf(", "));
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
		} else {
			OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			if (targetPlayer == null) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else if (sender instanceof Player && Link$.getRankId((Player) sender) == Link$.getRankId(targetPlayer.getUniqueId())) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Sorry. " + ChatColor.GRAY + "You are not allowed to modify his/her rank.");
			} else {
				if (Link$.validRanks.contains(args[1].toLowerCase())) {
					LinkServer.getInstance().getRedisDatabase().set("rank", targetPlayer.getUniqueId().toString(), args[1].toLowerCase());
					if (Link$.isPrefixedRankingEnabled() && targetPlayer.isOnline()) {
						Link$.flashPlayerDisplayName(targetPlayer.getPlayer());
					}
					String message = Server.getPluginLabel() + ChatColor.RED + targetPlayer.getName() + ChatColor.GRAY + " has been given rank " + ChatColor.RED + WordUtils.capitalize(args[1].toLowerCase());
					Bukkit.broadcastMessage(message);
					LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
					message = message.substring(message.indexOf(ChatColor.RED + ""));
					Server.getDiscordBot().broadcast(
							ChatColor.stripColor(message.replace(targetPlayer.getName(), "**" + targetPlayer.getName() + "**"))
							, Channel.SERVER_CHAT, Channel.SERVER_LOG);
				} else {
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified rank could not be found.");
					String s = "";
					for (String str : Link$.validRanks) {
						s += ChatColor.RED + WordUtils.capitalize(str) + ChatColor.GRAY + ", ";
					}
					s = s.substring(0, s.lastIndexOf(", "));
					sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Valid ranks: " + ChatColor.RED + s);
				}
			}
		}
		return true;
	}

}
