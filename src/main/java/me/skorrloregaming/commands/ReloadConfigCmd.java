package me.skorrloregaming.commands;

import me.skorrloregaming.ChatItem;
import me.skorrloregaming.Link$;
import me.skorrloregaming.LinkServer;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.isOp()) {
            Link$.playLackPermissionMessage(sender);
            return true;
        }
        Server.getInstance().getDiscordVerifyConfig().reloadData();
        Server.getInstance().getChatItemConfig().reloadData();
        Server.getInstance().getBanConfig().reloadData();
        Server.getInstance().getWarpConfig().reloadData();
        Server.getInstance().getSignConfig().reloadData();
        Server.getInstance().getFactionsConfig().reloadData();
        Server.getInstance().getShoppeConfig().reloadData();
        Server.getInstance().getMarriageHomesConfig().reloadData();
        Server.getInstance().getNpcConfig().reloadData();
        Server.getInstance().getSpawnerConfig().reloadData();
        LinkServer.getInstance().getUUIDCache().reloadData();
        LinkServer.getInstance().getGeolocationCache().reloadData();
        Server.getInstance().getSessionManager().sessionConfig.reloadData();
        Server.getInstance().getMonthlyVoteConfig().reloadData();
        if (!(Server.getInstance().getChatItem() == null))
            ChatItem.reload(null);
        Server.getInstance().getGarbageCollector().run();
        Server.getInstance().reload();
        sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Server configuration successfully reloaded.");
        return true;
    }

}
