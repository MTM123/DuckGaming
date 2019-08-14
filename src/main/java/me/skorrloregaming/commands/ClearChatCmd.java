package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.discord.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCmd implements CommandExecutor {

    private void clearChat(String playerName) {
        for (int i = 0; i < 300; i++) {
            Bukkit.broadcastMessage("");
        }
        String message = Server.getInstance().getPluginLabel() + ChatColor.GRAY + "Chat has been cleared by " + ChatColor.RED + playerName;
        Bukkit.broadcastMessage(message);
        message = message.substring(message.indexOf(ChatColor.GRAY + ""));
        Server.getInstance().getDiscordBot().broadcast(
                ChatColor.stripColor(message)
                , Channel.SERVER_CHAT
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp() || sender instanceof Player) {
            if (sender instanceof Player) {
                if (Link$.getRankId((Player) sender) == 3) {
                    clearChat(sender.getName());
                } else {
                    Link$.playLackPermissionMessage(sender);
                }
            } else {
                clearChat("Console");
            }
        } else {
            Link$.playLackPermissionMessage(sender);
        }
        return true;
    }

}
