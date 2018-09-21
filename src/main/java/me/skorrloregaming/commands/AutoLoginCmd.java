package me.skorrloregaming.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class AutoLoginCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		boolean dailyAuth = Server.getPlugin().getConfig().getBoolean("settings.enable.authme.dailyAuth");
		boolean autoLoginCmd = Server.getPlugin().getConfig().getBoolean("settings.enable.authme.autoLoginCmd");
		if (!dailyAuth && autoLoginCmd) {
			String ip = player.getAddress().getAddress().getHostAddress().replace(".", "x");
			if (Server.getPlugin().getConfig().contains("autologin." + ip + "." + player.getUniqueId().toString())) {
				Server.getPlugin().getConfig().set("autologin." + ip + "." + player.getUniqueId().toString(), null);
				player.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The auto login feature has been disabled.");
			} else {
				if (args.length > 0) {
					Object authObject = $.getAuthenticationSuite();
					boolean isCorrectPassword = false;
					if (authObject == null) {
						player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "There is no need to toggle this feature.");
					} else {
						isCorrectPassword = ((fr.xephi.authme.api.v3.AuthMeApi) authObject).checkPassword(player.getName(), args[0]);
						if (isCorrectPassword) {
							Server.getPlugin().getConfig().set("autologin." + ip + "." + player.getUniqueId().toString(), args[0]);
							player.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The auto login feature has been enabled.");
						} else {
							player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified account password is incorrect.");
						}
					}
				} else {
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You must specify your password to enable this.");
				}
			}
		} else {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "There is no need to toggle this feature.");
		}
		return true;
	}

}
