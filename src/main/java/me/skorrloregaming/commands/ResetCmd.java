package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ResetCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		if (args.length == 0) {
			sender.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Syntax " + ChatColor.RED + "/" + label + " <player>");
			return true;
		} else {
			OfflinePlayer op = CraftGo.Player.getOfflinePlayer(args[0]);
			if ((!op.hasPlayedBefore() && !op.isOnline()) || !Server.getPlugin().getConfig().contains("config." + op.getUniqueId().toString())) {
				sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "The specified player could not be found.");
				return true;
			}
			String msg = ChatColor.RED + "You have been forcibly unregistered from the server.";
			if (op.isOnline())
				op.getPlayer().kickPlayer(msg);
			Server.getPlugin().getConfig().set("config." + op.getUniqueId().toString(), null);
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The specified player should now be unregistered.");
		}
		return true;
	}

}
