package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetZoneCmd implements CommandExecutor {

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
            Server.getInstance().getWarpConfig().getData().set(args[0].toLowerCase() + ".world", player.getWorld().getName());
            Server.getInstance().getWarpConfig().getData().set(args[0].toLowerCase() + ".x", player.getLocation().getX());
            Server.getInstance().getWarpConfig().getData().set(args[0].toLowerCase() + ".y", player.getLocation().getY());
            Server.getInstance().getWarpConfig().getData().set(args[0].toLowerCase() + ".z", player.getLocation().getZ());
            Server.getInstance().getWarpConfig().getData().set(args[0].toLowerCase() + ".yaw", player.getLocation().getYaw());
            Server.getInstance().getWarpConfig().getData().set(args[0].toLowerCase() + ".pitch", player.getLocation().getPitch());
            Server.getInstance().getWarpConfig().saveData();
            player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Zone successfully created named : " + ChatColor.RED + args[0].toLowerCase());
        }
        return true;
    }

}
