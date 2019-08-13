package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.*;

public class PardonCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length == 0) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player/ip>");
				return true;
			}
			OfflinePlayer offlinePlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			String path = "config." + offlinePlayer.getUniqueId().toString();
			String ipAddress = "/unspecified";
			if (Server.getInstance().getPlugin().getConfig().contains(path)) {
				ipAddress = Server.getInstance().getPlugin().getConfig().getString(path + ".ip");
			} else {
				if (!$.isValidAddress(args[0])) {
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The provided data does not match any player.");
					return true;
				} else {
					ipAddress = args[0].replace(".", "x");
				}
			}
			if (!ipAddress.equals("/unspecified")) {
				if (Server.getInstance().getBanConfig().getData().contains(ipAddress)) {
					Server.getInstance().getBanConfig().getData().set(ipAddress, null);
					Server.getInstance().getBanConfig().saveData();
					Server.getInstance().getPlugin().getConfig().set("warning." + ipAddress, null);
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The address should no longer be banned.");
				} else {
					sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The address is not banned from the server.");
				}
			}
		} else {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
