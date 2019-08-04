package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class RemoveNpcCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!player.isOp()) {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		if (args.length == 0) {
			if (!ServerGet.get().getConfirmUnregisterNpc().contains(player.getUniqueId()))
				ServerGet.get().getConfirmUnregisterNpc().add(player.getUniqueId());
			player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success." + ChatColor.GRAY + " Npc removal parameters " + ChatColor.ITALIC + "saved in memory" + ChatColor.RESET + ChatColor.GRAY + ".");
			player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Right click on the npc that you would like to remove.");
			ServerGet.get().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {
				@Override
				public void run() {
					if (ServerGet.get().getConfirmUnregisterNpc().contains(player.getUniqueId()))
						ServerGet.get().getConfirmUnregisterNpc().remove(player.getUniqueId());
				}
			}, 100L));
		}
		return true;
	}

}
