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
        String s1 = "";
        for (String str : $.validMinigames) {
            if (!$.betaMinigames.contains(str) && $.isMinigameEnabled(ServerMinigame.valueOf(str.toUpperCase()))) {
                s1 += ChatColor.ITALIC + WordUtils.capitalize(str) + ChatColor.RESET + ", ";
            }
        }
        if (s1.length() > 0)
            s1 = s1.substring(0, s1.lastIndexOf(", "));
        String s2 = "";
        for (String str : $.betaMinigames) {
            if ($.isMinigameEnabled(ServerMinigame.valueOf(str.toUpperCase())))
                s2 += ChatColor.ITALIC + WordUtils.capitalize(str) + ChatColor.RESET + ", ";
        }
        if (s2.length() > 0)
            s2 = s2.substring(0, s2.lastIndexOf(", "));
        sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "You are currently connected to " + Server.getInstance().getPluginName().toLowerCase() + ".");
        sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Servers: " + ChatColor.RESET + s1);
        if (s2.length() > 0)
            sender.sendMessage(ChatColor.GOLD + "[Bungee] " + ChatColor.RESET + "Beta Servers: " + ChatColor.RESET + s2);
        return true;
    }

}
