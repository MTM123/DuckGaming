package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class DesyncCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0) {
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
			return true;
		}
		if (Server.getPlugin().getConfig().contains("sync." + args[0].toString())) {
			if (Server.getPlugin().getConfig().getString("sync." + args[0].toString() + ".name").toString().equals(player.getName().toString())) {
				Server.getPlugin().getConfig().set("sync." + args[0].toString(), null);
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The specified account is no longer synced.");
			} else {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified account is not synced with your account. Usernames are case-sensitive, so make sure you typed it correctly.");
			}
		} else {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified account is not synced with an account right now. Usernames are case-sensitive, so make sure you typed it correctly.");
		}
		return true;
	}

}
