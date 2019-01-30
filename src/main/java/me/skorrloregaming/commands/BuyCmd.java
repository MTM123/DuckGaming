package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BuyCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Our buycraft: " + ChatColor.RED + "shop.skorrloregaming.com");
		return true;
	}

}
