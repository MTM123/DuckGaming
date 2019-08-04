package me.skorrloregaming.commands;

import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class OpmeCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		int rankID = Link$.getRankId(player);
		if (rankID == 3) {
			if (player.isOp()) {
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You are already opped on this server.");
			} else {
				player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "You should now be opped temporarily.");
				player.setOp(true);
				ServerGet.get().getOpmePlayers().add(player.getUniqueId());
				ServerGet.get().getBukkitTasks().add(Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (ServerGet.get().getOpmePlayers().contains(player.getUniqueId())) {
							player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "Times up! " + ChatColor.GRAY + "You will now be deopped from this server.");
							player.setOp(false);
							ServerGet.get().getOpmePlayers().remove(player.getUniqueId());
						}
					}
				}, (20 * 60) * 3));
			}
		} else {
			Link$.playLackPermissionMessage(player);
			return true;
		}
		return true;
	}

}
