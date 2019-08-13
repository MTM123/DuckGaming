package me.skorrloregaming.commands;

import com.massivecraft.factions.FPlayers;
import me.skorrloregaming.$;
import me.skorrloregaming.GriefPreventionAPI;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (Server.getInstance().getFactions().contains(player.getUniqueId())) {
            if (Server.getInstance().getPlayersInCombat().containsKey(player.getUniqueId())) {
                player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
                return true;
            }
            if (Link$.getDonorRankId(player) < -3) {
                if (FPlayers.getInstance().getByPlayer(player).isInOwnTerritory()) {
                    if (Server.getInstance().getFactionFlyPlayers().contains(player.getUniqueId())) {
                        Server.getInstance().getFactionFlyPlayers().remove(player.getUniqueId());
                        if (player.isFlying())
                            player.setFlying(false);
                        player.setAllowFlight(false);
                        player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Faction flight disabled.");
                    } else {
                        Server.getInstance().getFactionFlyPlayers().add(player.getUniqueId());
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Faction flight enabled.");
                    }
                } else {
                    player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are not in your territory.");
                }
            } else {
                player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You need rank " + ChatColor.RED + "Obsidian" + ChatColor.GRAY + " to use flight.");
            }
        } else if (Server.getInstance().getSurvival().contains(player.getUniqueId())) {
            if (Server.getInstance().getPlayersInCombat().containsKey(player.getUniqueId())) {
                player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
                return true;
            }
            if (Link$.getDonorRankId(player) < -3) {
                if (GriefPreventionAPI.hasClaimInLocation(player, player.getLocation())) {
                    if (Server.getInstance().getSurvivalFlyPlayers().contains(player.getUniqueId())) {
                        Server.getInstance().getSurvivalFlyPlayers().remove(player.getUniqueId());
                        if (player.isFlying())
                            player.setFlying(false);
                        player.setAllowFlight(false);
                        player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Survival flight disabled.");
                    } else {
                        Server.getInstance().getSurvivalFlyPlayers().add(player.getUniqueId());
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Survival flight enabled.");
                    }
                } else {
                    player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are not in your territory.");
                }
            } else {
                player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You need rank " + ChatColor.RED + "Obsidian" + ChatColor.GRAY + " to use flight.");
            }
        } else {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
            return true;
        }

        return true;
    }

}
