package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.DelayedTeleport;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class TpaCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getModeratingPlayers().containsKey(player.getUniqueId()) && (!Server.getCreative().contains(player.getUniqueId()) && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId()) && !Server.getKitpvp().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId()))) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		String tag = $.getMinigameTag(player);
		if (args.length == 0) {
			player.sendMessage(tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
		} else {
			Player targetPlayer = Server.getPlugin().getServer().getPlayer(args[0]);
			if (targetPlayer == null) {
				player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player could not be found.");
			} else {
				if ($.getCurrentMinigame(targetPlayer) == $.getCurrentMinigame(player)) {
					if (Server.getModeratingPlayers().containsKey(player.getUniqueId())) {
						DelayedTeleport dt = new DelayedTeleport(player, 0, targetPlayer.getLocation(), false);
						dt.runTask(Server.getPlugin());
					} else {
						for (Map.Entry<UUID, UUID> id : Server.getTpaRequests().entrySet()) {
							if (id.getValue().equals(targetPlayer.getUniqueId())) {
								Server.getTpaRequests().remove(id.getKey());
							}
						}
						if (Server.getTpaRequests().containsKey(player.getUniqueId()))
							Server.getTpaRequests().remove(player.getUniqueId());
						Server.getTpaRequests().put(player.getUniqueId(), targetPlayer.getUniqueId());
						player.sendMessage(tag + ChatColor.WHITE + "Teleport request sent to " + ChatColor.YELLOW + targetPlayer.getName() + ChatColor.WHITE + ".");
						targetPlayer.sendMessage(tag + ChatColor.WHITE + "Teleport request received from " + ChatColor.YELLOW + player.getName() + ChatColor.WHITE + ".");
						targetPlayer.sendMessage(tag + ChatColor.WHITE + "To accept this request, type " + ChatColor.YELLOW + "/tpaccept" + ChatColor.WHITE + " in chat.");
					}
				} else {
					player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "The specified player is in another minigame.");
				}
			}
		}
		return true;
	}

}
