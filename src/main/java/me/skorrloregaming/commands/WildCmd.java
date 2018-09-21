package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.DelayedTeleport;

public class WildCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		Location teleportLocation;
		while (!(teleportLocation = $.getRandomLocation(player)).clone().subtract(0, 1, 0).getBlock().getType().isSolid()) {
		}
		if (player.getWorld().getEnvironment() == Environment.NETHER) {
			teleportLocation.setY(128);
		}
		player.sendMessage($.Legacy.tag + "Teleport destination: " + ChatColor.RED + teleportLocation.getX() + ChatColor.RESET + ", " + ChatColor.RED + teleportLocation.getY() + ChatColor.RESET + ", " + ChatColor.RED + teleportLocation.getZ() + ChatColor.RESET + ".");
		DelayedTeleport dt = new DelayedTeleport(player, Server.getTeleportationDelay() * 1.5, teleportLocation, false);
		dt.runTaskTimerAsynchronously(Server.getPlugin(), 4, 4);
		return true;
	}

}
