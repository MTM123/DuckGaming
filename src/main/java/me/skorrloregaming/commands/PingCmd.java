package me.skorrloregaming.commands;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0) {
			int ping = CraftGo.Player.getConnectionLatency(player);
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Connection latency (ping): " + ChatColor.RED + ping);
		} else {
			Player targetPlayer = Bukkit.getPlayer(args[0]);
			if (targetPlayer == null) {
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			int ping = CraftGo.Player.getConnectionLatency(targetPlayer);
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + targetPlayer.getName());
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Connection latency (ping): " + ChatColor.RED + ping);
		}
		return true;
	}

}
