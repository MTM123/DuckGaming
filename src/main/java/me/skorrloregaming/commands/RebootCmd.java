package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.events.PlayerPreMinigameChangeEvent;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import me.skorrloregaming.*;

public class RebootCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender.isOp()) {
			if (!ServerGet.get().getDelayedTasks().contains(UUID.nameUUIDFromBytes(sender.getName().getBytes()))) {
				ServerGet.get().getDelayedTasks().add(UUID.nameUUIDFromBytes(sender.getName().getBytes()));
				Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {
					@Override
					public void run() {
						ServerGet.get().getDelayedTasks().remove(UUID.nameUUIDFromBytes(sender.getName().getBytes()));
					}
				}, 45L);
				sender.sendMessage("Are you sure you want to /reboot the server? This will fully restart the ServerGet.get(). If you are wanting to simply update the server you may want to use /reload instead. Type /reboot again to confirm if you want to restart the ServerGet.get().");
			} else {
				ServerGet.get().getBukkitTasks().add(Bukkit.getScheduler().runTaskTimerAsynchronously(ServerGet.get().getPlugin(), new Runnable() {
					int value = -1;

					public void run() {
						value++;
						String message = ServerGet.get().getPluginLabel() + ChatColor.GOLD + ChatColor.BOLD + "Server restarting.. " + ChatColor.RED + ChatColor.BOLD + (5 - value);
						Bukkit.broadcastMessage(message);
						new LinkServerGet().get().getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(message).build());
						if (value > 4) {
							value = 0;
							Bukkit.getScheduler().runTask(ServerGet.get().getPlugin(), new Runnable() {
								@Override
								public void run() {
									for (Player player : Bukkit.getOnlinePlayers()) {
										Bukkit.getPluginManager().callEvent(new PlayerPreMinigameChangeEvent(player, ServerMinigame.HUB));
										ServerGet.get().getInstance().performBuggedLeave(player, false, true);
										player.kickPlayer(ServerGet.get().getPluginName() + ChatColor.AQUA + " " + '\u00BB' + " " + ChatColor.RESET + "Server restarting, please rejoin soon.");
									}
									Bukkit.getScheduler().runTaskLater(ServerGet.get().getPlugin(), new Runnable() {

										@Override
										public void run() {
											Bukkit.shutdown();
										}
									}, 20L);
								}
							});
						}
					}
				}, 20L, 20L));
			}
		} else {
			Link$.playLackPermissionMessage(sender);
			return true;
		}
		return true;
	}

}
