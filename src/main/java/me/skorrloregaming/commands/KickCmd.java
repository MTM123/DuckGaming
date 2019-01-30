package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length < 2) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <msg>");
				return true;
			}
			StringBuilder sb = new StringBuilder(Link$.italicGray + "Server: Kicked " + args[0] + " '");
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i] + " ");
			}
			sb.append(ChatColor.RESET + Link$.italicGray + "'");
			String msg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
			Player player = Bukkit.getPlayerExact(args[0]);
			if (player == null) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
			} else {
				if (!(player == null)) {
					player.kickPlayer(msg);
				}
				Logger.info(msg);
			}
		} else {
			Link$.playLackPermissionMessage(sender);
		}
		return true;
	}

}
