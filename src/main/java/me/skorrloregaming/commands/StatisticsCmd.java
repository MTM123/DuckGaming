package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class StatisticsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (!Server.getInstance().getKitpvp().contains(player.getUniqueId()) && !Server.getInstance().getSkyblock().contains(player.getUniqueId())) {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
            return true;
        }
        Player targetPlayer = player;
        if (args.length > 0) {
            targetPlayer = Server.getInstance().getPlugin().getServer().getPlayer(args[0]);
            if (targetPlayer == null) {
                player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
                return true;
            }
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
        if (Server.getInstance().getSkyblock().contains(player.getUniqueId())) {
            int currentPlayerPlaced = $.Skyblock.getPlayerPlacedBlocks(targetPlayer);
            int currentPlayerBroken = $.Skyblock.getPlayerBrokenBlocks(targetPlayer);
            double currentPlayerCash = EconManager.retrieveCash(targetPlayer, "skyblock");
            String tag = $.getMinigameTag("kitpvp");
            player.sendMessage(tag + ChatColor.RESET + "/ Statistics for player " + ChatColor.YELLOW + targetPlayer.getName());
            player.sendMessage(tag + ChatColor.RESET + "Current Balance: " + ChatColor.YELLOW + "$" + formatter.format(currentPlayerCash));
            player.sendMessage(tag + ChatColor.RESET + "Broken Blocks: " + ChatColor.YELLOW + currentPlayerBroken);
            player.sendMessage(tag + ChatColor.RESET + "Placed Blocks: " + ChatColor.YELLOW + currentPlayerPlaced);
        } else if (Server.getInstance().getKitpvp().contains(player.getUniqueId())) {
            int currentPlayerKills = $.Kitpvp.getPlayerKills(targetPlayer);
            int currentPlayerDeaths = $.Kitpvp.getPlayerDeaths(targetPlayer);
            int currentPlayerDPK = currentPlayerKills / 50;
            double currentPlayerCash = EconManager.retrieveCash(targetPlayer, "kitpvp");
            String tag = $.getMinigameTag("kitpvp");
            player.sendMessage(tag + ChatColor.RESET + "/ Statistics for player " + ChatColor.YELLOW + targetPlayer.getName());
            player.sendMessage(tag + ChatColor.RESET + "Current Balance: " + ChatColor.YELLOW + "$" + formatter.format(currentPlayerCash));
            player.sendMessage(tag + ChatColor.RESET + "Current Kills: " + ChatColor.YELLOW + currentPlayerKills);
            player.sendMessage(tag + ChatColor.RESET + "Current Deaths: " + ChatColor.YELLOW + currentPlayerDeaths);
            player.sendMessage(tag + ChatColor.RESET + "Current Level: " + ChatColor.YELLOW + (currentPlayerDPK + 1));
        }
        return true;
    }

}
