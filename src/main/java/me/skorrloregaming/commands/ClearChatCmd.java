package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class ClearChatCmd implements CommandExecutor {

	private void clearChat(String playerName) {
		for (int i = 0; i < 300; i++) {
			Bukkit.broadcastMessage("");
		}
		Bukkit.broadcastMessage(Server.getPluginLabel() + ChatColor.GRAY + "Chat has been cleared by " + ChatColor.RED + playerName);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp() || sender instanceof Player) {
			if (sender instanceof Player) {
				if ($.getRankId((Player) sender) == 3) {
					clearChat(((Player) sender).getName());
				} else {
					$.playLackPermissionMessage(sender);
				}
			} else {
				clearChat("Console");
			}
		} else {
			$.playLackPermissionMessage(sender);
		}
		return true;
	}

}
