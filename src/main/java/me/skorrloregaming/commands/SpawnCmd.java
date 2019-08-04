package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.DelayedTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String subDomain = $.getMinigameDomain(player);
		Location zoneLocation = $.getZoneLocation(subDomain);
		DelayedTeleport dt = new DelayedTeleport(player, Server.getTeleportationDelay(), zoneLocation, false);
		Server.getBukkitTasks().add(dt.runTaskTimerAsynchronously(Server.getPlugin(), 4, 4));
		return true;
	}

}
