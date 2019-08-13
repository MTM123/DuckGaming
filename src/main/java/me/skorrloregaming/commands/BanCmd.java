package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

import me.skorrloregaming.*;

public class BanCmd implements CommandExecutor {

	public static void ban(String hostName, String reason) {
		Server.getInstance().getBanConfig().getData().set(hostName.replace(".", "x"), reason);
		Server.getInstance().getBanConfig().saveData();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length < 2) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player /ip> <msg>");
				return true;
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i] + " ");
			}
			OfflinePlayer offlinePlayer = CraftGo.Player.getOfflinePlayer(args[0]);
			String path = "config." + offlinePlayer.getUniqueId().toString();
			String ipAddress = "/unspecified";
			if (Server.getInstance().getPlugin().getConfig().contains(path)) {
				ipAddress = Server.getInstance().getPlugin().getConfig().getString(path + ".ip");
			} else if ($.isValidAddress(args[0])) {
				ipAddress = args[0].replace(".", "x");
			} else {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The provided data does not match any player.");
			}
			if (!ipAddress.equals("/unspecified")) {
				if (Server.getInstance().getPlugin().getConfig().contains("address." + ipAddress)) {
					for (String section : Server.getInstance().getPlugin().getConfig().getConfigurationSection("address." + ipAddress).getKeys(false)) {
						OfflinePlayer bannedPlayer = CraftGo.Player.getOfflinePlayer(UUID.fromString(section));
						String msg = ChatColor.translateAlternateColorCodes('&', sb.toString().trim());
						if (bannedPlayer.isOnline())
							bannedPlayer.getPlayer().kickPlayer(msg);
						String message0 = Link$.italicGray + "Server: Banned <unknown> '" + sb.toString().trim() + "'";
						String message1 = Link$.italicGray + "Server: Banned " + bannedPlayer.getName() + " '" + sb.toString().trim() + "'";
						if (!bannedPlayer.hasPlayedBefore() && !bannedPlayer.isOnline()) {
							Map<String, String> message = new MapBuilder().message(message0).range(0).consoleOnly(true).build();
							LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
							Logger.info(message0, true);
						} else {
							Map<String, String> message = new MapBuilder().message(message1).range(0).consoleOnly(true).build();
							LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
							Logger.info(message1, true);
						}
						for (Player otherPlayer : Server.getInstance().getPlugin().getServer().getOnlinePlayers()) {
							if (!bannedPlayer.hasPlayedBefore() && !bannedPlayer.isOnline()) {
								otherPlayer.sendMessage(message0);
							} else {
								otherPlayer.sendMessage(message1);
							}
						}
					}
				}
				ban(ipAddress, sb.toString().trim());
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The address should now be banned.");
			}
		} else {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
