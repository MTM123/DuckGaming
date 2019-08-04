package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import me.skorrloregaming.*;

public class DelHomeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!ServerGet.get().getFactions().contains(player.getUniqueId()) && !ServerGet.get().getSurvival().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}

		ConfigurationManager config = null;
		if (ServerGet.get().getFactions().contains(player.getUniqueId())) {
			config = ServerGet.get().getFactionsConfig();
		} else if (ServerGet.get().getSurvival().contains(player.getUniqueId())) {
			config = ServerGet.get().getSurvivalConfig();
		}
		int count = 0;
		if (config.getData().contains("home." + player.getUniqueId().toString())) {
			Set<String> values = config.getData().getConfigurationSection("home." + player.getUniqueId().toString()).getKeys(false);
			count = values.size();
		}
		if (count == 0) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You have not yet set a home on this server.");
			return true;
		} else if (count == 1) {
			config.getData().set("home." + player.getUniqueId().toString(), null);
			config.saveData();
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have unset your home on this server.");
			return true;
		}
		if (args.length == 0) {
			Set<String> values = config.getData().getConfigurationSection("home." + player.getUniqueId().toString()).getKeys(false);
			StringBuilder homes = new StringBuilder();
			String homesString = null;
			for (String home : values) {
				homes.append(ChatColor.RED + home + ChatColor.GRAY + ", ");
			}
			if (homes.length() > 0) {
				homesString = homes.toString().substring(0, homes.toString().length() - 2);
			} else {
				homesString = homes.toString();
			}
			player.sendMessage($.getMinigameTag(player) + ChatColor.GRAY + "Homes: " + homesString);
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <name>");
			return true;
		}
		String home = args[0];
		if (config.getData().contains("home." + player.getUniqueId().toString() + "." + home)) {
			config.getData().set("home." + player.getUniqueId().toString() + "." + home, null);
			config.saveData();
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Home " + ChatColor.RED + home + ChatColor.GRAY + " has been unset.");
		} else {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have the specified home set.");
		}
		return true;
	}

}
