package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class EnablePluginCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			$.playLackPermissionMessage(sender);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage($.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <pluginName>");
			return true;
		}
		Plugin plugin = Server.getPlugin().getServer().getPluginManager().getPlugin(args[0]);
		if (!(plugin == null)) {
			Server.getPlugin().getServer().getPluginManager().enablePlugin(plugin);
			sender.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Enabled the plugin ... " + ChatColor.RED + plugin.getName());
		} else {
			sender.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified plugin could not be found.");
		}
		return true;
	}

}
