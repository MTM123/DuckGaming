package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.LinkServer;
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
        if (Server.getInstance().getTransferAcceptPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage("Sending you to the hub to complete the operation..");
            player.performCommand("hub");
            SwitchUUIDString suu = Server.getInstance().getTransferAcceptPlayers().get(player.getUniqueId());
            String uuid = suu.getArg0().toString();
            Set<String> array = Server.getInstance().getPlugin().getConfig().getConfigurationSection("config." + uuid).getKeys(true);
            for (String value : array) {
                String valuePath = "config." + player.getUniqueId().toString() + "." + value;
                String oldValuePath = "config." + uuid + "." + value;
                Server.getInstance().getPlugin().getConfig().set(valuePath, Server.getInstance().getPlugin().getConfig().get(oldValuePath));
            }
            if (LinkServer.getInstance().getRedisDatabase().contains("playtime.total", uuid)) {
                for (int day = 0; day <= 365; day++) {
                    if (LinkServer.getInstance().getRedisDatabase().contains("playtime.dayOfYear." + day, uuid)) {
                        String value = LinkServer.getInstance().getRedisDatabase().getString("playtime.dayOfYear." + day, uuid);
                        LinkServer.getInstance().getRedisDatabase().set("playtime.dayOfYear." + day, player.getUniqueId().toString(), value);
                        LinkServer.getInstance().getRedisDatabase().set("playtime.dayOfYear." + day, uuid, null);
                    }
                }
                {
                    String value = LinkServer.getInstance().getRedisDatabase().getString("playtime.total", uuid);
                    LinkServer.getInstance().getRedisDatabase().set("playtime.total", player.getUniqueId().toString(), value);
                    LinkServer.getInstance().getRedisDatabase().set("playtime.total", uuid, null);
                }
                {
                    String value = LinkServer.getInstance().getRedisDatabase().getString("playtime.lastKnownDayOfYear", uuid);
                    LinkServer.getInstance().getRedisDatabase().set("playtime.lastKnownDayOfYear", player.getUniqueId().toString(), value);
                    LinkServer.getInstance().getRedisDatabase().set("playtime.lastKnownDayOfYear", uuid, null);
                }
            }
            if (Server.getInstance().getSurvivalConfig().getData().contains("homes." + uuid)) {
                Set<String> array1 = Server.getInstance().getSurvivalConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
                for (String value : array1) {
                    String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
                    String oldValuePath = "homes." + uuid + "." + value;
                    Server.getInstance().getSurvivalConfig().getData().set(valuePath, Server.getInstance().getSurvivalConfig().getData().get(oldValuePath));
                }
                Server.getInstance().getSurvivalConfig().getData().set("homes." + uuid, null);
                Server.getInstance().getSurvivalConfig().saveData();
            }
            if (Server.getInstance().getFactionsConfig().getData().contains("homes." + uuid)) {
                Set<String> array1 = Server.getInstance().getFactionsConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
                for (String value : array1) {
                    String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
                    String oldValuePath = "homes." + uuid + "." + value;
                    Server.getInstance().getFactionsConfig().getData().set(valuePath, Server.getInstance().getFactionsConfig().getData().get(oldValuePath));
                }
                Server.getInstance().getFactionsConfig().getData().set("homes." + uuid, null);
                Server.getInstance().getFactionsConfig().saveData();
            }
            for (String domain : $.validStorageMinigames)
                SolidStorage.dataUUIDtoUUID(UUID.fromString(uuid), player.getUniqueId(), domain);
            Server.getInstance().getPlugin().getConfig().set("config." + uuid, null);
            player.sendMessage("Operation completed, you may now play as usual.");
            Server.getInstance().getTransferAcceptPlayers().remove(player.getUniqueId());
        } else {
            player.sendMessage("You have no pending transfers from old accounts.");
        }
        return true;
    }

}
