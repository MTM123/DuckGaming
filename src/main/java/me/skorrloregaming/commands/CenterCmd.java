package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CenterCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        int yaw = 0;
        int pitch = 0;
        if (args.length >= 1) {
            try {
                yaw = Integer.parseInt(args[0]);
            } catch (Exception ex) {
            }
        }
        if (args.length >= 2) {
            try {
                pitch = Integer.parseInt(args[1]);
            } catch (Exception ex) {
            }
        }
        player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "You can also do " + ChatColor.RED + "/" + label + " [yaw] [pitch]");
        Location teleportLocation = player.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
        teleportLocation.setYaw(yaw);
        teleportLocation.setPitch(pitch);
        player.teleport(teleportLocation);
        player.sendMessage(ChatColor.GRAY + "Teleporting..");
        return true;
    }

}
