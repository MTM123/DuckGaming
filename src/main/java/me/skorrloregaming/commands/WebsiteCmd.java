package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.skorrloregaming.$;

public class WebsiteCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Our website: " + ChatColor.RED + "play.skorrloregaming.com");
		return true;
	}

}
