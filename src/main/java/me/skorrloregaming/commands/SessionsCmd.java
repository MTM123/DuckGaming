package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class SessionsCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0) {
			if (sender instanceof Player)
				((Player) sender).performCommand(label + " " + sender.getName());
			return true;
		} else {
			OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0]);
			if (player.getName().equals(op.getName()) || player.isOp() || $.getRankId(player) > 1) {
				if (op.isOnline() || op.hasPlayedBefore()) {
					Server.getSessionManager().openComplexInventory(player, op);
				} else {
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				}
			} else {
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are only allowed to view your own sessions.");
			}
			return true;
		}
	}

}
