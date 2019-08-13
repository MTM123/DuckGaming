package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class PlayertimeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0 || args[0].toString().equalsIgnoreCase("reset") || args[0].toString().equalsIgnoreCase("off") || args[0].toString().equalsIgnoreCase("disable")) {
			player.resetPlayerTime();
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "If player time was set, it is now disabled.");
		}
		if (args.length == 0) {
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <time>");
		} else {
			final long time;
			if (args[0].equalsIgnoreCase("morning") || args[0].equalsIgnoreCase("sunrise") || args[0].equalsIgnoreCase("sunup")) {
				time = 0L;
			} else if (args[0].equalsIgnoreCase("day") || args[0].equalsIgnoreCase("daytime") || args[0].equalsIgnoreCase("noon")) {
				time = 6000L;
			} else if (args[0].equalsIgnoreCase("sunset") || args[0].equalsIgnoreCase("sundown")) {
				time = 12500L;
			} else if (args[0].equalsIgnoreCase("night") || args[0].equalsIgnoreCase("nighttime") || args[0].equalsIgnoreCase("bedtime") || args[0].equalsIgnoreCase("midnight")) {
				time = 18000L;
			} else {
				try {
					time = (long) Integer.parseInt(args[0]);
				} catch (Exception ex) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must specify the time in numeric format.");
					return true;
				}
			}
			player.resetPlayerTime();
			Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), new Runnable() {
				@Override
				public void run() {
					player.setPlayerTime(time, false);
				}
			}, 2L);
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Your player time has been modified.");
		}
		return true;
	}

}
