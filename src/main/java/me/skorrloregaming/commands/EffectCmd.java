package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EffectCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (args.length == 0) {
			if ($.isEffectsEnabled(player)) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Notice. " + ChatColor.GRAY + "Plugin sent particles are enabled.");
			} else {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Notice. " + ChatColor.GRAY + "Plugin sent particles are disabled.");
			}
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enable /disable>");
		} else {
			if (args[0].toLowerCase().equals("enable") || args[0].toLowerCase().equals("on")) {
				$.setEnableEffects(player, true);
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Plugin sent particles has been enabled.");
			} else if (args[0].toLowerCase().equals("disable") || args[0].toLowerCase().equals("off")) {
				$.setEnableEffects(player, false);
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Plugin sent particles has been disabled.");
			} else {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <enable /disable>");
			}
		}
		return true;
	}

}
