package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModerateCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (Server.getModeratingPlayers().containsKey(player.getUniqueId())) {
			ServerMinigame minigame = Server.getModeratingPlayers().get(player.getUniqueId());
			Server.getInstance().performBuggedLeave(player, true, true);
			Server.getModeratingPlayers().remove(player.getUniqueId());
			if (minigame == ServerMinigame.FACTIONS) {
				Server.getInstance().enterFactions(player, false, true);
			} else if (minigame == ServerMinigame.KITPVP) {
				Server.getInstance().enterKitpvp(player, false, true);
			} else if (minigame == ServerMinigame.SKYFIGHT) {
				Server.getInstance().enterSkyfight(player, false, true);
			} else if (minigame == ServerMinigame.CREATIVE) {
				Server.getInstance().enterCreative(player, false, true);
			} else if (minigame == ServerMinigame.SURVIVAL) {
				Server.getInstance().enterSurvival(player, false, true);
			} else if (minigame == ServerMinigame.SKYBLOCK) {
				Server.getInstance().enterSkyblock(player, false, true);
			} else if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.UNKNOWN) {
				player.performCommand("hub");
			}
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are no longer moderating the server.");
		} else {
			int rankID = Link$.getRankId(player);
			if (rankID > 0 || player.isOp()) {
				ServerMinigame minigame = $.getCurrentMinigame(player);
				Server.getInstance().performBuggedLeave(player, false, true);
				player.performCommand("hub");
				Server.getModeratingPlayers().put(player.getUniqueId(), minigame);
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You are now moderating the server.");
			} else {
				Link$.playLackPermissionMessage(player);
				return true;
			}
		}
		return true;
	}

}
