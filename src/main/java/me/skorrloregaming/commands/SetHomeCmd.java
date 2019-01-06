package me.skorrloregaming.commands;

import me.skorrloregaming.ConfigurationManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;

public class SetHomeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;
		if ((!Server.getFactions().contains(player.getUniqueId())) && (!Server.getSurvival().contains(player.getUniqueId()))) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		ConfigurationManager config = null;
		if (Server.getFactions().contains(player.getUniqueId())) {
			config = Server.getFactionsConfig();
		} else if (Server.getSurvival().contains(player.getUniqueId())) {
			config = Server.getSurvivalConfig();
		}
		String base = "homes." + player.getUniqueId().toString();
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
		player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have set your home on this server.");
		return true;
	}

}
