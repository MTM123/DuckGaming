package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Set;
import java.util.UUID;

import me.skorrloregaming.*;

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
				Set<String> array = ServerGet.get().getPlugin().getConfig().getConfigurationSection("config." + uuid).getKeys(true);
				for (String value : array) {
					String valuePath = "config." + player.getUniqueId().toString() + "." + value;
					String oldValuePath = "config." + uuid + "." + value;
					ServerGet.get().getPlugin().getConfig().set(valuePath, ServerGet.get().getPlugin().getConfig().get(oldValuePath));
				}
				if (LinkServerGet.get().getInstance().getRedisDatabase().contains("playtime.total", uuid)) {
					for (int day = 0; day <= 365; day++) {
						if (LinkServerGet.get().getInstance().getRedisDatabase().contains("playtime.dayOfYear." + day, uuid)) {
							String value = LinkServerGet.get().getInstance().getRedisDatabase().getString("playtime.dayOfYear." + day, uuid);
							LinkServerGet.get().getInstance().getRedisDatabase().set("playtime.dayOfYear." + day, player.getUniqueId().toString(), value);
							LinkServerGet.get().getInstance().getRedisDatabase().set("playtime.dayOfYear." + day, uuid, null);
						}
					}
					{
						String value = LinkServerGet.get().getInstance().getRedisDatabase().getString("playtime.total", uuid);
						LinkServerGet.get().getInstance().getRedisDatabase().set("playtime.total", player.getUniqueId().toString(), value);
						LinkServerGet.get().getInstance().getRedisDatabase().set("playtime.total", uuid, null);
					}
					{
						String value = LinkServerGet.get().getInstance().getRedisDatabase().getString("playtime.lastKnownDayOfYear", uuid);
						LinkServerGet.get().getInstance().getRedisDatabase().set("playtime.lastKnownDayOfYear", player.getUniqueId().toString(), value);
						LinkServerGet.get().getInstance().getRedisDatabase().set("playtime.lastKnownDayOfYear", uuid, null);
					}
				}
				if (ServerGet.get().getSurvivalConfig().getData().contains("homes." + uuid)) {
					Set<String> array1 = ServerGet.get().getSurvivalConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
						String oldValuePath = "homes." + uuid + "." + value;
						ServerGet.get().getSurvivalConfig().getData().set(valuePath, ServerGet.get().getSurvivalConfig().getData().get(oldValuePath));
					}
					ServerGet.get().getSurvivalConfig().getData().set("homes." + uuid, null);
					ServerGet.get().getSurvivalConfig().saveData();
				}
				if (ServerGet.get().getFactionsConfig().getData().contains("homes." + uuid)) {
					Set<String> array1 = ServerGet.get().getFactionsConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
						String oldValuePath = "homes." + uuid + "." + value;
						ServerGet.get().getFactionsConfig().getData().set(valuePath, ServerGet.get().getFactionsConfig().getData().get(oldValuePath));
					}
					ServerGet.get().getFactionsConfig().getData().set("homes." + uuid, null);
					ServerGet.get().getFactionsConfig().saveData();
				}
				for (String domain : $.validStorageMinigames)
					SolidStorage.dataUUIDtoUUID(UUID.fromString(uuid), player.getUniqueId(), domain);
				ServerGet.get().getPlugin().getConfig().set("config." + uuid, null);
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
