package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class SetZoneCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!player.isOp()) {
			$.playLackPermissionMessage(player);
			return true;
		}
		if (args.length == 0) {
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
		} else {
			Server.getWarpConfig().getData().set(args[0].toLowerCase() + ".world", player.getWorld().getName());
			Server.getWarpConfig().getData().set(args[0].toLowerCase() + ".x", player.getLocation().getX());
			Server.getWarpConfig().getData().set(args[0].toLowerCase() + ".y", player.getLocation().getY());
			Server.getWarpConfig().getData().set(args[0].toLowerCase() + ".z", player.getLocation().getZ());
			Server.getWarpConfig().getData().set(args[0].toLowerCase() + ".yaw", player.getLocation().getYaw());
			Server.getWarpConfig().getData().set(args[0].toLowerCase() + ".pitch", player.getLocation().getPitch());
			Server.getWarpConfig().saveData();
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Zone successfully created named : " + ChatColor.RED + args[0].toLowerCase());
		}
		return true;
	}

}
