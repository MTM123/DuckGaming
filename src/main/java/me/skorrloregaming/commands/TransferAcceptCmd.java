package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

import me.skorrloregaming.*;

public class TransferAcceptCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Bukkit.getOnlineMode()) {
			player.sendMessage("This commmand is currently not applicable to you.");
			return true;
		}
		if (ServerGet.get().getTransferAcceptPlayers().containsKey(player.getUniqueId())) {
			player.sendMessage("Sending you to the hub to complete the operation..");
			player.performCommand("hub");
			SwitchUUIDString suu = ServerGet.get().getTransferAcceptPlayers().get(player.getUniqueId());
			String uuid = suu.getArg0().toString();
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
			player.sendMessage("Operation completed, you may now play as usual.");
			ServerGet.get().getTransferAcceptPlayers().remove(player.getUniqueId());
		} else {
			player.sendMessage("You have no pending transfers from old accounts.");
		}
		return true;
	}

}
