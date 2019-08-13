package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.SolidStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RollbackCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            Link$.playLackPermissionMessage(sender);
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
            return true;
        } else {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer == null) {
                sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
                return true;
            } else {
                SolidStorage.restorePlayerData(targetPlayer, $.getMinigameDomain(targetPlayer));
                sender.sendMessage($.getMinigameTag(targetPlayer) + ChatColor.GRAY + "Player data has been restored for " + ChatColor.RED + targetPlayer.getName());
                return true;
            }
        }
    }

}
