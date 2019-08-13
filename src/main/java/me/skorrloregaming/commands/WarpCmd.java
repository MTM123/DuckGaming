package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class WarpCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!player.isOp()) {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		if (args.length == 0) {
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
		} else {
			Location zoneLocation = $.getZoneLocation(args[0]);
			if (!(zoneLocation == null)) {
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Teleporting to " + ChatColor.RED + args[0] + ChatColor.GRAY + "..");
				$.teleport(player, zoneLocation);
			} else {
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "The warp " + ChatColor.RED + args[0] + ChatColor.GRAY + " does not exist.");
			}
		}
		return true;
	}

}
