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

import me.skorrloregaming.*;

public class HubCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
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
		if ((changes = ServerGet.get().getInstance().performBuggedLeave(player, !save, false)) > 0) {
			perform = true;
		} else if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN || (minigame == ServerMinigame.FACTIONS && ServerGet.get().getUseFactionsAsHub())) {
			perform = true;
		}
		if (perform) {
			if (ServerGet.get().getUseFactionsAsHub()) {
				if (ServerGet.get().getHub().contains(player.getUniqueId()))
					ServerGet.get().getHub().remove(player.getUniqueId());
				ServerGet.get().getInstance().enterFactions(player, false, true);
			} else {
				if (!ServerGet.get().getHub().contains(player.getUniqueId()))
					ServerGet.get().getHub().add(player.getUniqueId());
			}
			if (ServerGet.get().getVanishedPlayers().containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!ServerGet.get().getUseFactionsAsHub()) {
				Location hubLocation = $.getZoneLocation("hub");
				$.teleport(player, hubLocation);
				ServerGet.get().getInstance().fetchLobby(player);
				player.setAllowFlight(true);
				if (changes > 0)
					Bukkit.getPluginManager().callEvent(new PlayerMinigameChangeEvent(player, ServerMinigame.HUB));
			}
		}
		return true;
	}

}
