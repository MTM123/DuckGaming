package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ServersCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder s1 = new StringBuilder();
        for (String str : $.validMinigames) {
            if (!$.betaMinigames.contains(str) && $.isMinigameEnabled(ServerMinigame.valueOf(str.toUpperCase()))) {
                s1.append(ChatColor.ITALIC).append(WordUtils.capitalize(str)).append(ChatColor.RESET).append(", ");
            }
        }
        if (s1.length() > 0)
            s1 = new StringBuilder(s1.substring(0, s1.lastIndexOf(", ")));
        StringBuilder s2 = new StringBuilder();
        for (String str : $.betaMinigames) {
            if ($.isMinigameEnabled(ServerMinigame.valueOf(str.toUpperCase())))
                s2.append(ChatColor.ITALIC).append(WordUtils.capitalize(str)).append(ChatColor.RESET).append(", ");
        }
        if (s2.length() > 0)
            s2 = new StringBuilder(s2.substring(0, s2.lastIndexOf(", ")));
        sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "You are currently connected to " + Server.getInstance().getPluginName().toLowerCase() + ".");
        sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Servers: " + ChatColor.RESET + s1);
        if (s2.length() > 0)
            sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Beta Servers: " + ChatColor.RESET + s2);
        return true;
    }

}
