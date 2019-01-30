package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RulesCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(Link$.Legacy.tag + ChatColor.ITALIC + "(1)" + ChatColor.RESET + "" + ChatColor.GRAY + " No hacking or abusing glitches");
		sender.sendMessage(Link$.Legacy.tag + ChatColor.ITALIC + "(2)" + ChatColor.RESET + "" + ChatColor.GRAY + " No swearing or disrespecting staff");
		sender.sendMessage(Link$.Legacy.tag + ChatColor.ITALIC + "(3)" + ChatColor.RESET + "" + ChatColor.GRAY + " No asking for free ranks or permissions");
		sender.sendMessage(Link$.Legacy.tag + ChatColor.ITALIC + "(4)" + ChatColor.RESET + "" + ChatColor.GRAY + " Do not advertise in the public server chat");
		sender.sendMessage(Link$.Legacy.tag + ChatColor.ITALIC + "(5)" + ChatColor.RESET + "" + ChatColor.GRAY + " ... Among other common sense server rules");
		return true;
	}

}
