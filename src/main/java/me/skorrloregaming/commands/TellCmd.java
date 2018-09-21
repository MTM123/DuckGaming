package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class TellCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length < 2) {
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <message>");
			return true;
		} else {
			Player targetPlayer = Server.getPlugin().getServer().getPlayer(args[0]);
			if (targetPlayer == null) {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				int rank = $.getRankId(player);
				String message = sb.toString();
				if (player.isOp() || rank > -1 || rank < -2) {
					message = ChatColor.translateAlternateColorCodes('&', message);
				}
				message =Server.getAntiCheat().processAntiSwear(player, message);
				player.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + "me" + ChatColor.WHITE + " " + '\u00BB' + " " + ChatColor.RED + targetPlayer.getName() + ChatColor.WHITE + "] " + message);
				if (Server.getIgnoredPlayers().containsKey(targetPlayer.getUniqueId())) {
					Player existingIgnore = Bukkit.getPlayer(Server.getIgnoredPlayers().get(targetPlayer.getUniqueId()));
					if (!existingIgnore.getName().toString().equals(player.getName().toString())) {
						targetPlayer.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + player.getName() + ChatColor.WHITE + " " + '\u00BB' + " " + ChatColor.RED + "me" + ChatColor.WHITE + "] " + message);
						targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
					}
				} else {
					targetPlayer.sendMessage(ChatColor.WHITE + "[" + ChatColor.RED + player.getName() + ChatColor.WHITE + " " + '\u00BB' + " " + ChatColor.RED + "me" + ChatColor.WHITE + "] " + message);
					targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
				}
				Server.getMessageRequests().put(player.getUniqueId(), targetPlayer.getUniqueId());
				Server.getMessageRequests().put(targetPlayer.getUniqueId(), player.getUniqueId());
				for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
					if (!onlinePlayer.getName().equals(player.getName()) && !onlinePlayer.getName().equals(targetPlayer.getName())) {
						int rankID = $.getRankId(onlinePlayer);
						if (onlinePlayer.isOp() || rankID > -1) {
							onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
						}
					}
				}
			}
		}
		return true;
	}

}
