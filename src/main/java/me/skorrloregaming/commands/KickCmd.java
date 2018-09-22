package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;

public class KickCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length < 2) {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <msg>");
				return true;
			}
			StringBuilder sb = new StringBuilder($.italicGray + "Server: Kicked " + args[0] + " '");
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i] + " ");
			}
			sb.append(ChatColor.RESET + $.italicGray + "'");
			String msg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
			Player player = Bukkit.getPlayerExact(args[0]);
			if (player == null) {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				if (!(player == null)) {
					player.kickPlayer(msg);
				}
				Logger.info(msg);
			}
		} else {
			$.playLackPermissionMessage(sender);
		}
		return true;
	}

}
