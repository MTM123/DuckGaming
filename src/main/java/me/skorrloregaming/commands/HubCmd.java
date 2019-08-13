package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.events.PlayerMinigameChangeEvent;
import me.skorrloregaming.events.PlayerPreMinigameChangeEvent;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;
        Player player = ((Player) sender);
        if (Server.getInstance().getPlayersInCombat().containsKey(player.getUniqueId())) {
            player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
            return true;
        }
        boolean save = true;
        if (args.length > 0 && args[0].equalsIgnoreCase("-nosave"))
            save = false;
        ServerMinigame minigame = $.getCurrentMinigame(player);
        boolean perform = false;
        if (!($.getCurrentMinigame(player) == ServerMinigame.HUB) && !($.getCurrentMinigame(player) == ServerMinigame.UNKNOWN))
            Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.HUB));
        int changes = 0;
        if ((changes = Server.getInstance().performBuggedLeave(player, !save, false)) > 0) {
            perform = true;
        } else if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN || (minigame == ServerMinigame.FACTIONS && Server.getInstance().getUseFactionsAsHub())) {
            perform = true;
        }
        if (perform) {
            if (Server.getInstance().getUseFactionsAsHub()) {
                if (Server.getInstance().getHub().contains(player.getUniqueId()))
                    Server.getInstance().getHub().remove(player.getUniqueId());
                Server.getInstance().enterFactions(player, false, true);
            } else {
                if (!Server.getInstance().getHub().contains(player.getUniqueId()))
                    Server.getInstance().getHub().add(player.getUniqueId());
            }
            if (Server.getInstance().getVanishedPlayers().containsKey(player.getUniqueId())) {
                player.performCommand("vanish");
            }
            if (!Server.getInstance().getUseFactionsAsHub()) {
                Location hubLocation = $.getZoneLocation("hub");
                $.teleport(player, hubLocation);
                Server.getInstance().fetchLobby(player);
                player.setAllowFlight(true);
                if (changes > 0)
                    Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.HUB));
            }
        }
        return true;
    }

}
