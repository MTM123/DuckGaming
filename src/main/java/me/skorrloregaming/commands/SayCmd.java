package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class SayCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (args.length < 2 || !(args[0].charAt(0) == '/')) {
				sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <id> <message>");
				return true;
			} else {
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i] + " ");
				}
				if (args[0].split("/")[1].equals(".")) {
					Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + Server.getLastKnownHubWorld() + ChatColor.GRAY + "] " + ChatColor.WHITE + $.consoleTag + "Server" + ChatColor.WHITE + " " + '\u00BB' + " " + sb.toString());
				} else {
					OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0].split("/")[1]);
					Player checkPlayer = Bukkit.getPlayer(args[0].split("/")[1]);
					String world = Server.getLastKnownHubWorld();
					String message = sb.toString();
					if (!(checkPlayer == null)) {
						world = checkPlayer.getWorld().getName();
					}
					if (!op.hasPlayedBefore() && !op.isOnline()) {
						sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
						return true;
					} else {
						if (message.startsWith("/") && op.isOnline()) {
							PlayerCommandPreprocessEvent commandEvent = new PlayerCommandPreprocessEvent(op.getPlayer(), message);
							Bukkit.getServer().getPluginManager().callEvent(commandEvent);
							if (!commandEvent.isCancelled())
								op.getPlayer().performCommand(message.substring(1));
						} else {
							if ($.isPrefixedRankingEnabled()) {
								Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.WHITE + $.getFlashPlayerDisplayName(op) + ChatColor.WHITE + " " + '\u00BB' + " " + message);
							} else {
								Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.WHITE + op.getName() + ChatColor.WHITE + " " + '\u00BB' + " " + message);
							}
						}
					}
				}
			}
		} else {
			$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
