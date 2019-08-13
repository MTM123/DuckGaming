package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (Server.getInstance().getTransferAcceptPlayers().containsKey(player.getUniqueId())) {
            SwitchUUIDString suu = Server.getInstance().getTransferAcceptPlayers().get(player.getUniqueId());
            String uuid = suu.getArg0().toString();
            for (String domain : $.validStorageMinigames)
                SolidStorage.clearPlayerSave(CraftGo.Player.getOfflinePlayer(uuid), domain);
            Server.getInstance().getPlugin().getConfig().set("config." + uuid, null);
            Server.getInstance().getPlugin().getConfig().set("denyDataTransfer." + uuid, true);
            if (LinkServer.getInstance().getRedisDatabase().contains("playtime.total", uuid)) {
                for (int day = 0; day <= 365; day++) {
                    if (LinkServer.getInstance().getRedisDatabase().contains("playtime.dayOfYear." + day, uuid))
                        LinkServer.getInstance().getRedisDatabase().set("playtime.dayOfYear." + day, uuid, null);
                }
                LinkServer.getInstance().getRedisDatabase().set("playtime.total", uuid, null);
                LinkServer.getInstance().getRedisDatabase().set("playtime.lastKnownDayOfYear", uuid, null);
            }
            Server.getInstance().getSurvivalConfig().getData().set("homes." + uuid, null);
            Server.getInstance().getSurvivalConfig().saveData();
            Server.getInstance().getFactionsConfig().getData().set("homes." + uuid, null);
            Server.getInstance().getFactionsConfig().saveData();
            player.sendMessage("Operation completed, this operation cannot be reversed.");
            Server.getInstance().getTransferAcceptPlayers().remove(player.getUniqueId());
        } else {
            player.sendMessage("You have no pending transfers from old accounts.");
        }
        return true;
    }

}
