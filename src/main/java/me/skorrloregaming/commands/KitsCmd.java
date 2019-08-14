package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitsCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (!Server.getInstance().getKitpvp().contains(player.getUniqueId()) && !Server.getInstance().getFactions().contains(player.getUniqueId()) && !Server.getInstance().getSurvival().contains(player.getUniqueId())) {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
            return true;
        }
        String tag = $.getMinigameTag(player);
        if (Server.getInstance().getSurvival().contains(player.getUniqueId())) {
            StringBuilder s = new StringBuilder();
            for (String str : $.Survival.validKits) {
                s.append(ChatColor.RED).append(WordUtils.capitalize(str)).append(ChatColor.GRAY).append(", ");
            }
            s = new StringBuilder(s.substring(0, s.lastIndexOf(", ")));
            sender.sendMessage(tag + ChatColor.GRAY + "Kits: " + ChatColor.RED + s);
            return true;
        }
        if (Server.getInstance().getFactions().contains(player.getUniqueId())) {
            StringBuilder s = new StringBuilder();
            for (String str : $.Factions.validKits) {
                s.append(ChatColor.RED).append(WordUtils.capitalize(str)).append(ChatColor.GRAY).append(", ");
            }
            s = new StringBuilder(s.substring(0, s.lastIndexOf(", ")));
            sender.sendMessage(tag + ChatColor.GRAY + "Kits: " + ChatColor.RED + s);
            return true;
        }
        if (Server.getInstance().getKitpvp().contains(player.getUniqueId())) {
            StringBuilder s = new StringBuilder();
            for (String str : $.Kitpvp.validKits) {
                s.append(ChatColor.RED).append(WordUtils.capitalize(str)).append(ChatColor.GRAY).append(", ");
            }
            s = new StringBuilder(s.substring(0, s.lastIndexOf(", ")));
            sender.sendMessage(tag + ChatColor.GRAY + "Kits: " + ChatColor.RED + s);
            return true;
        }
        return true;
    }

}
