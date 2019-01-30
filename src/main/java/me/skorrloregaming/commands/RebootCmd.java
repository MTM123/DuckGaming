package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RebootCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (!Server.getDelayedTasks().contains(UUID.nameUUIDFromBytes(sender.getName().getBytes()))) {
				Server.getDelayedTasks().add(UUID.nameUUIDFromBytes(sender.getName().getBytes()));
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Server.getDelayedTasks().remove(UUID.nameUUIDFromBytes(sender.getName().getBytes()));
					}
				}, 45L);
				sender.sendMessage("Are you sure you want to /reboot the server? This will fully restart the server. If you are wanting to simply update the server you may want to use /reload instead. Type /reboot again to confirm if you want to restart the server.");
			} else {
				Bukkit.getScheduler().runTaskTimerAsynchronously(Server.getPlugin(), new Runnable() {
					int value = -1;

					public void run() {
						value++;
						Logger.info(Server.getPluginLabel() + ChatColor.GOLD + ChatColor.BOLD + "Server restarting.. " + ChatColor.RED + ChatColor.BOLD + (5 - value), false, -1);
						if (value > 4) {
							value = 0;
							Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									for (Player player : Bukkit.getOnlinePlayers()) {
										Server.getInstance().performBuggedLeave(player, false, true);
										player.kickPlayer(Server.getPluginName() + ChatColor.AQUA + " " + '\u00BB' + " " + ChatColor.RESET + "Server restarting, please rejoin soon.");
									}
									Bukkit.shutdown();
								}
							});
						}
					}
				}, 20L, 20L);
			}
		} else {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
