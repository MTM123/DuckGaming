package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StaffChatCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!(Link$.getRankId(player) > -1)) {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		if (Server.getInstance().getStaffChatPlayers().contains(player.getUniqueId())) {
			Server.getInstance().getStaffChatPlayers().remove(player.getUniqueId());
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have left the staff chat.");
		} else {
			Server.getInstance().getStaffChatPlayers().add(player.getUniqueId());
			sender.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You have entered the staff chat.");
		}
		return true;
	}

}
