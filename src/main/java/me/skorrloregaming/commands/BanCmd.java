package me.skorrloregaming.commands;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;

public class BanCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length < 2) {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player /ip> <msg>");
				return true;
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i] + " ");
			}
			OfflinePlayer offlinePlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			String path = "config." + offlinePlayer.getUniqueId().toString();
			String ipAddress = "/unspecified";
			if (Server.getPlugin().getConfig().contains(path)) {
				ipAddress = Server.getPlugin().getConfig().getString(path + ".ip");
			} else if ($.isValidAddress(args[0])) {
				ipAddress = args[0].replace(".", "x");
			} else {
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The provided data does not match any player.");
			}
			if (!ipAddress.equals("/unspecified")) {
				if (Server.getPlugin().getConfig().contains("address." + ipAddress)) {
					for (String section : Server.getPlugin().getConfig().getConfigurationSection("address." + ipAddress).getKeys(false)) {
						OfflinePlayer bannedPlayer = CraftGo.Player.getOfflinePlayer(UUID.fromString(section));
						String msg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
						if (bannedPlayer.isOnline())
							bannedPlayer.getPlayer().kickPlayer(msg);
						Server.getNotifyWorker().kickPlayer(bannedPlayer.getName(), msg);
						String message0 = $.italicGray + "Server: Banned <unknown> '" + sb.toString().trim() + "'";
						String message1 = $.italicGray + "Server: Banned " + bannedPlayer.getName() + " '" + sb.toString().trim() + "'";
						if (!bannedPlayer.hasPlayedBefore() && !bannedPlayer.isOnline()) {
							Logger.info(message0, true);
						} else {
							Logger.info(message1, true);
						}
						for (Player otherPlayer : Server.getPlugin().getServer().getOnlinePlayers()) {
							if (!bannedPlayer.hasPlayedBefore() && !bannedPlayer.isOnline()) {
								otherPlayer.sendMessage(message0);
							} else {
								otherPlayer.sendMessage(message1);
							}
						}
					}
				}
				Server.getBanConfig().getData().set(ipAddress, sb.toString().trim());
				Server.getBanConfig().saveData();
				sender.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The address should now be banned.");
			}
		} else {
			$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
