package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SuicideCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (Server.getInstance().getPlayersInCombat().containsKey(player.getUniqueId())) {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
            return true;
        }
        player.setHealth(0.0);
        return true;
    }

}
