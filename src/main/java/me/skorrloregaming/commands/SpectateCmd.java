package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class SpectateCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		int rankID = Link$.getRankId(player);
		if (rankID > 1 || player.isOp()) {
			if (rankID > 1 || player.isOp()) {
				if (args.length == 0) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
					return true;
				}
				Player targetPlayer = Bukkit.getPlayer(args[0]);
				if (targetPlayer == null) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
					return true;
				}
				if (!ServerGet.get().getModeratingPlayers().containsKey(player.getUniqueId())) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are not currently moderating the ServerGet.get().");
					return true;
				}
				ServerGet.get().getSpectatingPlayers().add(player.getUniqueId());
				String tpDomain = $.getMinigameDomain(targetPlayer);
				String pDomain = $.getMinigameDomain(player);
				if (!tpDomain.equals(pDomain))
					player.performCommand("server " + tpDomain);
				Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (ServerGet.get().getVanishedPlayers().containsKey(player.getUniqueId())) {
							player.performCommand("vanish");
						}
						Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {
							@Override
							public void run() {
								player.performCommand("vanish");
								player.teleport(targetPlayer);
								ServerGet.get().getSpectatingPlayers().remove(player.getUniqueId());
							}
						}, 10L);
					}
				}, 20L);
			}
		}
		return true;
	}

}
