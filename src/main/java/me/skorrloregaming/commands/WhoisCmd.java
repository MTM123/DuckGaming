package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.IpLocationQuery;
import me.skorrloregaming.impl.ServerMinigame;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class WhoisCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
			return true;
		} else {
			Player player = Bukkit.getPlayer(args[0]);
			if (player == null) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			int latency = CraftGo.Player.getConnectionLatency(player);
			ServerMinigame minigame = $.getCurrentMinigame(player);
			ServerMinigame worldMinigame = $.getMinigameFromWorld(player.getWorld());
			String playtime = Link$.formatTime((int) LinkServer.getInstance().getPlaytimeManager().getStoredPlayerPlaytime(player));
			String rank = WordUtils.capitalize(Link$.getRank(player));
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "/ Known information about " + ChatColor.RED + player.getName());
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Connection latency (ping): " + ChatColor.RED + latency);
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Detected Minecraft version: " + ChatColor.RED + ChatColor.BOLD + CraftGo.Player.getProtocolVersionName(player));
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Current minigame: " + ChatColor.RED + WordUtils.capitalize(minigame.toString().toLowerCase()));
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "World-based minigame: " + ChatColor.RED + WordUtils.capitalize(worldMinigame.toString().toLowerCase()));
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Approximate playtime: " + ChatColor.RED + playtime);
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Current server-side rank: " + ChatColor.RED + rank);
			if (sender.isOp()) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Defined ip address: " + ChatColor.RED + player.getAddress().getAddress().getHostAddress());
				Bukkit.getScheduler().runTaskAsynchronously(Server.getInstance().getPlugin(), new Runnable() {
					@Override
					public void run() {
						IpLocationQuery query = CraftGo.Player.queryIpLocation(player);
						sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Defined country: " + ChatColor.RED + query.getCountry());
						sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Defined geo-location: " + ChatColor.RED + query.getCity() + ", " + query.getState());
					}
				});
			}
		}
		return true;
	}

}
