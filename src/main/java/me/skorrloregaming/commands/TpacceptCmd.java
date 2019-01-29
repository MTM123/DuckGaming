package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.runnable.DelayedTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpacceptCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getCreative().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId()) && !Server.getKitpvp().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String tag = $.getMinigameTag(player);
		if (!Server.getTpaRequests().containsValue(player.getUniqueId())) {
			player.sendMessage(tag + ChatColor.RED + "You have not received any teleport requests recently.");
		} else {
			for (Map.Entry<UUID, UUID> id : Server.getTpaRequests().entrySet()) {
				if (id.getValue().equals(player.getUniqueId())) {
					Server.getTpaRequests().remove(id.getKey());
					Player targetPlayer = Server.getPlugin().getServer().getPlayer(id.getKey());
					if (targetPlayer == null) {
						player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player could not be found.");
					} else {
						if ($.getCurrentMinigame(targetPlayer) == $.getCurrentMinigame(player)) {
							Location zoneLocation = player.getLocation();
							ServerMinigame ma = $.getMinigameFromWorld(zoneLocation.getWorld());
							ServerMinigame mb = $.getMinigameFromWorld(player.getWorld());
							DelayedTeleport dt = null;
							if (ma.toString().equals(mb.toString()) && ma.toString().equals(ServerMinigame.CREATIVE.toString())) {
								dt = new DelayedTeleport(targetPlayer, 0.0, zoneLocation, false);
								dt.runTask(Server.getPlugin());
							} else {
								dt = new DelayedTeleport(targetPlayer, Server.getTeleportationDelay(), zoneLocation, false);
								dt.runTaskTimer(Server.getPlugin(), 4, 4);
							}
							player.sendMessage(tag + ChatColor.WHITE + "Teleportation request accepted.");
							targetPlayer.sendMessage(tag + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " has accepted your teleport request.");
						} else {
							player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player is in another minigame.");
						}
					}
				}
			}
		}
		return true;
	}

}
