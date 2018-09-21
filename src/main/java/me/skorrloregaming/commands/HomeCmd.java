package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.DelayedTeleport;

public class HomeCmd implements CommandExecutor {

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
		ConfigurationManager config = null;
		if (Server.getFactions().contains(player.getUniqueId())) {
			config = Server.getFactionsConfig();
		} else if (Server.getSurvival().contains(player.getUniqueId())) {
			config = Server.getSurvivalConfig();
		}
		if (!config.getData().contains("homes." + player.getUniqueId().toString())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You have not yet set a home on this server.");
		} else {
			String base = "homes." + player.getUniqueId().toString();
			World world = Server.getPlugin().getServer().getWorld(config.getData().getString(base + ".world"));
			double x = config.getData().getDouble(base + ".x");
			double y = config.getData().getDouble(base + ".y");
			double z = config.getData().getDouble(base + ".z");
			float yaw = (float) config.getData().getDouble(base + ".yaw");
			float pitch = (float) config.getData().getDouble(base + ".pitch");
			Location homeLocation = new Location(world, x, y, z, yaw, pitch);
			DelayedTeleport dt = new DelayedTeleport(player, Server.getTeleportationDelay(), homeLocation, false);
			dt.runTaskTimer(Server.getPlugin(), 4, 4);
		}
		return true;
	}

}
