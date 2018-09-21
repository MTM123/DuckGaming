package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;

public class SetHomeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		int marriageId = $.Marriage.getPlayerMarriageId(player);
		if (marriageId == 0) {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are currently not married to anyone.");
			return true;
		}
		ServerMinigame minigame = $.getMinigameFromWorld(player.getWorld());
		if (minigame == ServerMinigame.HUB || minigame == ServerMinigame.SKYFIGHT || minigame == ServerMinigame.UNKNOWN) {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.WHITE + "You cannot set a home in this minigame.");
			return true;
		}
		String base = "homes." + marriageId;
		Server.getMarriageHomesConfig().getData().set(base + ".world", player.getWorld().getName());
		Server.getMarriageHomesConfig().getData().set(base + ".x", player.getLocation().getX());
		Server.getMarriageHomesConfig().getData().set(base + ".y", player.getLocation().getY());
		Server.getMarriageHomesConfig().getData().set(base + ".z", player.getLocation().getZ());
		Server.getMarriageHomesConfig().getData().set(base + ".yaw", (int) player.getLocation().getYaw());
		Server.getMarriageHomesConfig().getData().set(base + ".pitch", (int) player.getLocation().getPitch());
		Server.getMarriageHomesConfig().saveData();
		player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have set your home on this server.");
		return true;
	}

}
