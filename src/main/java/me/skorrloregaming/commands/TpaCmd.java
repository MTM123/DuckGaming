package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.DelayedTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpaCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (!Server.getInstance().getModeratingPlayers().containsKey(player.getUniqueId()) && (!Server.getInstance().getCreative().contains(player.getUniqueId()) && !Server.getInstance().getFactions().contains(player.getUniqueId()) && !Server.getInstance().getSurvival().contains(player.getUniqueId()) && !Server.getInstance().getKitpvp().contains(player.getUniqueId()) && !Server.getInstance().getSkyblock().contains(player.getUniqueId()))) {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
            return true;
        }
        if (Server.getInstance().getPlayersInCombat().containsKey(player.getUniqueId())) {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
            return true;
        }
        String tag = $.getMinigameTag(player);
        if (args.length == 0) {
            player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
        } else {
            Player targetPlayer = Server.getInstance().getPlugin().getServer().getPlayer(args[0]);
            if (targetPlayer == null) {
                player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player could not be found.");
            } else {
                if ($.getCurrentMinigame(targetPlayer) == $.getCurrentMinigame(player)) {
                    if (Server.getInstance().getModeratingPlayers().containsKey(player.getUniqueId())) {
                        DelayedTeleport dt = new DelayedTeleport(player, 0, targetPlayer.getLocation(), false);
                        dt.runTask(Server.getInstance().getPlugin());
                    } else {
                        for (Map.Entry<UUID, UUID> id : Server.getInstance().getTpaRequests().entrySet()) {
                            if (id.getValue().equals(targetPlayer.getUniqueId())) {
                                Server.getInstance().getTpaRequests().remove(id.getKey());
                            }
                        }
                        Server.getInstance().getTpaRequests().remove(player.getUniqueId());
                        Server.getInstance().getTpaRequests().put(player.getUniqueId(), targetPlayer.getUniqueId());
                        player.sendMessage(tag + ChatColor.WHITE + "Teleport request sent to " + ChatColor.YELLOW + targetPlayer.getName() + ChatColor.WHITE + ".");
                        targetPlayer.sendMessage(tag + ChatColor.WHITE + "Teleport request received from " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + ".");
                        targetPlayer.sendMessage(tag + ChatColor.WHITE + "To accept this request, type " + ChatColor.YELLOW + "/tpaccept" + ChatColor.WHITE + " in chat.");
                    }
                } else {
                    player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player is in another minigame.");
                }
            }
        }
        return true;
    }

}
