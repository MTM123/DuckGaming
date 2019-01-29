package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;

public class WarningCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp() && (!(sender instanceof Player) || (sender instanceof Player && !(Link$.getRankId((Player) sender) > 1)))) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		if (args.length < 2) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player> <amount> [message]");
			return true;
		} else {
			int id = 0;
			try {
				id = Integer.parseInt(args[1]);
			} catch (Exception ex) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must enter a valid numerical warning value.");
				return true;
			}
			OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0]);
			if (!op.hasPlayedBefore() && !op.isOnline()) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String path = "config." + op.getUniqueId().toString();
			if (!Server.getPlugin().getConfig().contains(path + ".ip")) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String ipAddress = Server.getPlugin().getConfig().getString(path + ".ip");
			String configPath = "warning." + ipAddress + ".count";
			if (!Server.getPlugin().getConfig().contains(configPath)) {
				Server.getPlugin().getConfig().set(configPath, "0");
			}
			int oldWarningCount = Integer.parseInt(Server.getPlugin().getConfig().getString(configPath));
			int newWarningCount = oldWarningCount + id;
			int rankId = 3;
			if (sender instanceof Player)
				rankId = Link$.getRankId((Player) sender);
			int targetPlayerRankId = Link$.getRankId(op.getUniqueId());
			if (sender instanceof Player && rankId < targetPlayerRankId) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You cannot warn staff ranked higher then you.");
				return true;
			}
			if (newWarningCount < oldWarningCount && rankId < 3) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are not allowed to retract warnings.");
				return true;
			}
			if (newWarningCount < 0)
				newWarningCount = 0;
			if (newWarningCount > 5)
				newWarningCount = 5;
			Server.getPlugin().getConfig().set(configPath, newWarningCount + "");
			if (args.length > 2) {
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				String msg = "You have been warned for disrespecting the server rules." + '\n' + "\"" + sb.toString() + "\"" + '\n' + "You are now at warning value " + newWarningCount + " out of 5." + '\n' + '\n' + "Issued by " + sender.getName() + " at " + new Date().toString();
				if (op.isOnline() && id > 0)
					op.getPlayer().kickPlayer(msg);
				sender.sendMessage(Link$.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5) '" + sb.toString().trim() + "'");
				for (Player pl : Server.getPlugin().getServer().getOnlinePlayers()) {
					if (pl.getName().equals(sender.getName()))
						continue;
					pl.sendMessage(Link$.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5) '" + sb.toString().trim() + "'");
				}
				if (id > 0) {
					for (int i = oldWarningCount + 1; i < newWarningCount + 1; i++) {
						Server.getPlugin().getConfig().set("warning." + ipAddress + "." + i, msg);
					}
				} else if (id < 0) {
					for (int i = oldWarningCount + 1; i >= newWarningCount + 1; i--) {
						Server.getPlugin().getConfig().set("warning." + ipAddress + "." + i, null);
					}
				}
			} else {
				String msg = "You have been warned for disrespecting the server rules." + '\n' + "You are now at warning value " + newWarningCount + " out of 5." + '\n' + '\n' + "Issued by " + sender.getName() + " at " + new Date().toString();
				if (op.isOnline() && id > 0)
					op.getPlayer().kickPlayer(msg);
				sender.sendMessage(Link$.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5)");
				for (Player pl : Server.getPlugin().getServer().getOnlinePlayers()) {
					if (pl.getName().equals(sender.getName()))
						continue;
					pl.sendMessage(Link$.italicGray + "Server: Warned " + op.getName() + " (" + newWarningCount + " / 5)");
				}
				if (id > 0) {
					for (int i = oldWarningCount + 1; i < newWarningCount + 1; i++) {
						Server.getPlugin().getConfig().set("warning." + ipAddress + "." + i, msg);
					}
				} else if (id < 0) {
					for (int i = oldWarningCount + 1; i >= newWarningCount + 1; i--) {
						Server.getPlugin().getConfig().set("warning." + ipAddress + "." + i, null);
					}
				}
			}
			if (id < 5 && oldWarningCount == 5) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pardon " + op.getName());
			}
			if (newWarningCount >= 5) {
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "ban " + op.getName() + " You have exceeded the maximum warning count for this server.");
			}
		}
		return true;
	}

}
