package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class MuteCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.hasPermission("mute") || (sender instanceof Player && $.getRankId((Player) sender) >= 2)) {
			if (args.length == 0) {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			if (Server.getMutedPlayers().contains(player.getUniqueId())) {
				Server.getMutedPlayers().remove(player.getUniqueId());
				if (sender instanceof Player)
					((Player) sender).getName();
				player.sendMessage($.modernMsgPrefix + "Psst, you are no longer muted in chat.");
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Unmuted player " + ChatColor.RED + player.getName());
			} else {
				Server.getMutedPlayers().add(player.getUniqueId());
				String name = "CONSOLE";
				if (sender instanceof Player)
					name = ((Player) sender).getName();
				player.sendMessage($.modernMsgPrefix + "Psst, you have been muted by " + name + ".");
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Muted player " + ChatColor.RED + player.getName());
			}
		} else {
			$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
