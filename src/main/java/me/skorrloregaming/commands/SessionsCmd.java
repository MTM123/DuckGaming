package me.skorrloregaming.commands;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

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
			if (player.getName().equals(op.getName()) || player.isOp() || Link$.getRankId(player) > 1) {
				if (op.isOnline() || op.hasPlayedBefore()) {
					Server.getInstance().getSessionManager().openComplexInventory(player, op);
				} else {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				}
			} else {
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are only allowed to view your own sessions.");
			}
			return true;
		}
	}

}
