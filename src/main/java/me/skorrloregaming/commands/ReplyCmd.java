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

public class ReplyCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0) {
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <message>");
			return true;
		} else {
			if (!Server.getMessageRequests().containsKey(player.getUniqueId())) {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "You have not messaged anyone recently.");
				return true;
			}
			Player targetPlayer = Server.getPlugin().getServer().getPlayer(Server.getMessageRequests().get(player.getUniqueId()));
			if (targetPlayer == null) {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				int rank = $.getRankId(player);
				int donorRank = $.getDonorRankId(player);
				String message = sb.toString();
				if (player.isOp() || rank > -1 || donorRank < -2) {
					message = ChatColor.translateAlternateColorCodes('&', message);
				}
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
			}
		}
		return true;
	}

}
