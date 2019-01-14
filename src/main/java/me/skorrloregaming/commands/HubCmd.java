package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;

public class HubCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		boolean save = true;
		if (args.length > 0 && args[0].equalsIgnoreCase("-nosave"))
			save = false;
		ServerMinigame minigame = $.getCurrentMinigame(player);
		boolean perform = false;
		if (Server.getInstance().performBuggedLeave(player, !save, false) > 0) {
			perform = true;
		} else if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN || (minigame == ServerMinigame.FACTIONS && Server.getUseFactionsAsHub())) {
			perform = true;
		}
		if (perform) {
			if (Server.getUseFactionsAsHub()) {
				if (Server.getHub().contains(player.getUniqueId()))
					Server.getHub().remove(player.getUniqueId());
				Server.getInstance().enterFactions(player, false, true);
			} else {
				if (!Server.getHub().contains(player.getUniqueId()))
					Server.getHub().add(player.getUniqueId());
			}
			if (Server.getVanishedPlayers().containsKey(player.getUniqueId())) {
				player.performCommand("vanish");
			}
			if (!Server.getUseFactionsAsHub()) {
				Location hubLocation = $.getZoneLocation("hub");
				$.teleport(player, hubLocation);
				Server.getInstance().fetchLobby(player);
				player.setAllowFlight(true);
			}
		}
		return true;
	}

}
