package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class MotdCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <motd>");
				return true;
			} else {
				if (!Server.getTempMotd().equals("/unspecified")) {
					sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must wait before using this command again.");
				} else {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < args.length; i++) {
						sb.append(args[i] + " ");
					}
					Server.setTempMotd(sb.toString());
					Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {

						@Override
						public void run() {
							Server.setTempMotd("/unspecified");
						}
					}, 1200L);
					sender.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The motd has been temporarily changed.");
				}
			}
		} else {
			$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
