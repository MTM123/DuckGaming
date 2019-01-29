package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.SolidStorage;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

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
		if (Server.getTransferAcceptPlayers().containsKey(player.getUniqueId())) {
			player.sendMessage("Sending you to the hub to complete the operation..");
			player.performCommand("hub");
			SwitchUUIDString suu = Server.getTransferAcceptPlayers().get(player.getUniqueId());
			String uuid = suu.getArg0().toString();
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
			player.sendMessage("Operation completed, you may now play as usual.");
			Server.getTransferAcceptPlayers().remove(player.getUniqueId());
		} else {
			player.sendMessage("You have no pending transfers from old accounts.");
		}
		return true;
	}

}
