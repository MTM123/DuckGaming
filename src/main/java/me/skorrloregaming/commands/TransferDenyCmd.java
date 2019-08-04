package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class TransferDenyCmd implements CommandExecutor {

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
			SwitchUUIDString suu = ServerGet.get().getTransferAcceptPlayers().get(player.getUniqueId());
			String uuid = suu.getArg0().toString();
			for (String domain : $.validStorageMinigames)
				SolidStorage.clearPlayerSave(CraftGo.Player.getOfflinePlayer(uuid), domain);
			ServerGet.get().getPlugin().getConfig().set("config." + uuid, null);
			ServerGet.get().getPlugin().getConfig().set("denyDataTransfer." + uuid, true);
			if (LinkServerGet.get().getRedisDatabase().contains("playtime.total", uuid)) {
				for (int day = 0; day <= 365; day++) {
					if (LinkServerGet.get().getRedisDatabase().contains("playtime.dayOfYear." + day, uuid))
						LinkServerGet.get().getRedisDatabase().set("playtime.dayOfYear." + day, uuid, null);
				}
				LinkServerGet.get().getRedisDatabase().set("playtime.total", uuid, null);
				LinkServerGet.get().getRedisDatabase().set("playtime.lastKnownDayOfYear", uuid, null);
			}
			ServerGet.get().getSurvivalConfig().getData().set("homes." + uuid, null);
			ServerGet.get().getSurvivalConfig().saveData();
			ServerGet.get().getFactionsConfig().getData().set("homes." + uuid, null);
			ServerGet.get().getFactionsConfig().saveData();
			player.sendMessage("Operation completed, this operation cannot be reversed.");
			ServerGet.get().getTransferAcceptPlayers().remove(player.getUniqueId());
		} else {
			player.sendMessage("You have no pending transfers from old accounts.");
		}
		return true;
	}

}
