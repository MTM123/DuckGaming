package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

import me.skorrloregaming.*;

public class SetHomeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if ((!ServerGet.get().getFactions().contains(player.getUniqueId())) && (!ServerGet.get().getSurvival().contains(player.getUniqueId()))) {
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
		String home = "familiar";
		if (args.length > 0)
			home = args[0];
		String base = "home." + player.getUniqueId().toString() + "." + home;
		int count = 0;
		if (config.getData().contains("home." + player.getUniqueId().toString())) {
			Set<String> values = config.getData().getConfigurationSection("home." + player.getUniqueId().toString()).getKeys(false);
			count = values.size();
		}
		if (config.getData().contains(base))
			count--;
		int rankId = Link$.getDonorRankId(player);
		switch (count) {
			case 0:
				break;
			case 1:
				if (!(rankId < -1)) {
					player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a donor rank to set more homes.");
					return true;
				}
				break;
			case 2:
				if (!(rankId < -2)) {
					if (rankId < -1) {
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a higher donor rank to set more.");
					} else {
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a donor rank to set more homes.");
					}
					return true;
				}
				break;
			case 3:
				if (!(rankId < -3)) {
					if (rankId < -1) {
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a higher donor rank to set more.");
					} else {
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a donor rank to set more homes.");
					}
					return true;
				}
				break;
			case 4:
				if (!(rankId < -4)) {
					if (rankId < -1) {
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a higher donor rank to set more.");
					} else {
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Sorry, you need a donor rank to set more homes.");
					}
					return true;
				}
				break;
			default:
				player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You cannot set any more homes on here.");
				return true;
		}
		World world = player.getWorld();
		double x = player.getLocation().getX();
		double y = player.getLocation().getY();
		double z = player.getLocation().getZ();
		float yaw = player.getLocation().getYaw();
		float pitch = player.getLocation().getPitch();
		config.getData().set(base + ".world", world.getName());
		config.getData().set(base + ".x", Double.valueOf(x));
		config.getData().set(base + ".y", Double.valueOf(y));
		config.getData().set(base + ".z", Double.valueOf(z));
		config.getData().set(base + ".yaw", Integer.valueOf((int) yaw));
		config.getData().set(base + ".pitch", Integer.valueOf((int) pitch));
		config.saveData();
		player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Home " + ChatColor.RED + home + ChatColor.GRAY + " has been set.");
		return true;
	}

}
