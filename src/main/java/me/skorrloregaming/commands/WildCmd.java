package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.AsyncRandomTeleport;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class WildCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!ServerGet.get().getFactions().contains(player.getUniqueId()) && !ServerGet.get().getSurvival().contains(player.getUniqueId()) && !ServerGet.get().getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (ServerGet.get().getSkyblock().contains(player.getUniqueId()) && player.getWorld().getEnvironment() == World.Environment.NORMAL) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This world prevents use of this command.");
			return true;
		}
		new AsyncRandomTeleport(player).execute();
		return true;
	}

}
