package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.DelayedTeleport;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

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
		int count = 0;
		String home = "familiar";
		if (config.getData().contains("home." + player.getUniqueId().toString())) {
			Set<String> values = config.getData().getConfigurationSection("home." + player.getUniqueId().toString()).getKeys(false);
			count = values.size();
			if (count > 0) {
				home = values.toArray(new String[0])[0];
				StringBuilder homes = new StringBuilder();
				String homesString = null;
				for (String homeValue : values) {
					homes.append(ChatColor.RED + homeValue + ChatColor.GRAY + ", ");
				}
				if (homes.length() > 0) {
					homesString = homes.toString().substring(0, homes.toString().length() - 2);
				} else {
					homesString = homes.toString();
				}
				player.sendMessage($.getMinigameTag(player) + ChatColor.GRAY + "Homes: " + homesString);
			}
		}
		if (args.length > 0)
			home = args[0];
		String oldBase = "homes." + player.getUniqueId().toString();
		String base = "home." + player.getUniqueId().toString() + "." + home;
		if (!config.getData().contains(base) && config.getData().contains(oldBase)) {
			base = oldBase;
		}
		if (!config.getData().contains(base)) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You have not yet set a home on this server.");
		} else {
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
