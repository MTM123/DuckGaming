package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class ModerateCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (ServerGet.get().getModeratingPlayers().containsKey(player.getUniqueId())) {
			ServerMinigame minigame = ServerGet.get().getModeratingPlayers().get(player.getUniqueId());
			ServerGet.get().performBuggedLeave(player, true, true);
			ServerGet.get().getModeratingPlayers().remove(player.getUniqueId());
			if (minigame == ServerMinigame.FACTIONS) {
				ServerGet.get().enterFactions(player, false, true);
			} else if (minigame == ServerMinigame.KITPVP) {
				ServerGet.get().enterKitpvp(player, false, true);
			} else if (minigame == ServerMinigame.SKYFIGHT) {
				ServerGet.get().enterSkyfight(player, false, true);
			} else if (minigame == ServerMinigame.CREATIVE) {
				ServerGet.get().enterCreative(player, false, true);
			} else if (minigame == ServerMinigame.SURVIVAL) {
				ServerGet.get().enterSurvival(player, false, true);
			} else if (minigame == ServerMinigame.SKYBLOCK) {
				ServerGet.get().enterSkyblock(player, false, true);
			} else if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN) {
				player.performCommand("hub");
			}
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are no longer moderating the server.");
		} else {
			int rankID = Link$.getRankId(player);
			if (rankID > 0 || player.isOp()) {
				ServerMinigame minigame = $.getCurrentMinigame(player);
				ServerGet.get().performBuggedLeave(player, false, true);
				player.performCommand("hub");
				ServerGet.get().getModeratingPlayers().put(player.getUniqueId(), minigame);
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now moderating the server.");
			} else {
				Link$.playLackPermissionMessage(player);
				return true;
			}
		}
		return true;
	}

}
