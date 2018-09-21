package me.skorrloregaming.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;

public class RemoveNpcCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!player.isOp()) {
			$.playLackPermissionMessage(player);
			return true;
		}
		if (args.length == 0) {
			if (!Server.getConfirmUnregisterNpc().contains(player.getUniqueId()))
				Server.getConfirmUnregisterNpc().add(player.getUniqueId());
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Npc removal parameters " + ChatColor.ITALIC + "saved in memory" + ChatColor.RESET + ChatColor.GRAY + ".");
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Right click on the npc that you would like to remove.");
			Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
				@Override
				public void run() {
					if (Server.getConfirmUnregisterNpc().contains(player.getUniqueId()))
						Server.getConfirmUnregisterNpc().remove(player.getUniqueId());
				}
			}, 100L);
		}
		return true;
	}

}
