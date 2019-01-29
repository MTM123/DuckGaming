package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.UUID;

public class TransferCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (Bukkit.getOnlineMode()) {
			sender.sendMessage("This commmand is currently not applicable to you.");
			return true;
		}
		if (sender.isOp()) {
			if (args.length < 2) {
				sender.sendMessage("Syntax /" + label + " <player> <targetPlayer>");
				return true;
			} else {
				OfflinePlayer targetPlayer = CraftGo.Player.getOfflinePlayer(args[0]);
				if (!targetPlayer.isOnline() && !targetPlayer.hasPlayedBefore()) {
					sender.sendMessage("Failed. The specified target player could not be found.");
					return true;
				}
				OfflinePlayer player = CraftGo.Player.getOfflinePlayer(args[1]);
				if (!player.isOnline() && !player.hasPlayedBefore()) {
					sender.sendMessage("Failed. The specified player could not be found.");
					return true;
				}
				if (player.isOnline())
					player.getPlayer().sendMessage(Link$.modernMsgPrefix + "Data from your old username, " + targetPlayer.getName() + ", is ready to be transferred into your current username. This will overwrite all data on your current username. If you changed your name since the last time you played, this is normal and won't damage anything.");
				if (player.isOnline())
					player.getPlayer().sendMessage("Sending you to the hub to complete the operation..");
				if (player.isOnline())
					player.getPlayer().performCommand("hub");
				String uuid = targetPlayer.getUniqueId().toString();
				Set<String> array = Server.getPlugin().getConfig().getConfigurationSection("config." + uuid).getKeys(true);
				for (String value : array) {
					String valuePath = "config." + player.getUniqueId().toString() + "." + value;
					String oldValuePath = "config." + uuid + "." + value;
					Server.getPlugin().getConfig().set(valuePath, Server.getPlugin().getConfig().get(oldValuePath));
				}
				if (Server.getSqlDatabase().contains("playtime.total", uuid)) {
					for (int day = 0; day <= 365; day++) {
						if (Server.getSqlDatabase().contains("playtime.dayOfYear." + day, uuid)) {
							String value = Server.getSqlDatabase().getString("playtime.dayOfYear." + day, uuid);
							Server.getSqlDatabase().set("playtime.dayOfYear." + day, player.getUniqueId().toString(), value);
							Server.getSqlDatabase().set("playtime.dayOfYear." + day, uuid, null);
						}
					}
					{
						String value = Server.getSqlDatabase().getString("playtime.total", uuid);
						Server.getSqlDatabase().set("playtime.total", player.getUniqueId().toString(), value);
						Server.getSqlDatabase().set("playtime.total", uuid, null);
					}
					{
						String value = Server.getSqlDatabase().getString("playtime.lastKnownDayOfYear", uuid);
						Server.getSqlDatabase().set("playtime.lastKnownDayOfYear", player.getUniqueId().toString(), value);
						Server.getSqlDatabase().set("playtime.lastKnownDayOfYear", uuid, null);
					}
				}
				if (Server.getSurvivalConfig().getData().contains("homes." + uuid)) {
					Set<String> array1 = Server.getSurvivalConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
						String oldValuePath = "homes." + uuid + "." + value;
						Server.getSurvivalConfig().getData().set(valuePath, Server.getSurvivalConfig().getData().get(oldValuePath));
					}
					Server.getSurvivalConfig().getData().set("homes." + uuid, null);
					Server.getSurvivalConfig().saveData();
				}
				if (Server.getFactionsConfig().getData().contains("homes." + uuid)) {
					Set<String> array1 = Server.getFactionsConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
						String oldValuePath = "homes." + uuid + "." + value;
						Server.getFactionsConfig().getData().set(valuePath, Server.getFactionsConfig().getData().get(oldValuePath));
					}
					Server.getFactionsConfig().getData().set("homes." + uuid, null);
					Server.getFactionsConfig().saveData();
				}
				for (String domain : $.validStorageMinigames)
					SolidStorage.dataUUIDtoUUID(UUID.fromString(uuid), player.getUniqueId(), domain);
				Server.getPlugin().getConfig().set("config." + uuid, null);
				if (player.isOnline())
					player.getPlayer().sendMessage("Operation completed, you may now play as usual.");
				sender.sendMessage("Operation completed, " + player.getName() + " data has been modified.");
				sender.sendMessage("Existing data has been replaced by " + targetPlayer.getName() + "'s data.");
				return true;
			}
		}
		return true;
	}

}
