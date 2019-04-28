package me.skorrloregaming.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperEventHandler implements Listener {


	@EventHandler
	public void onPlayerJump(PlayerJumpEvent event) {
		Player player = event.getPlayer();
		if (Server.getWaiverAcceptPlayers().contains(player.getUniqueId())) {
			event.setCancelled(true);
			return;
		}
	}


	@EventHandler
	public void onProjectileLaunch(PlayerLaunchProjectileEvent event) {
		Player player = event.getPlayer();
		if (event.getProjectile() instanceof EnderPearl) {
			if (Server.getDelayedTasks().contains(player.getUniqueId())) {
				event.setCancelled(true);
			} else {
				Server.getDelayedTasks().add(player.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Server.getDelayedTasks().remove(player.getUniqueId());
					}
				}, 7L);
			}
		} else if (event.getProjectile() instanceof Arrow) {
			Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
				public void run() {
					Server.doReturnItem(player);
				}
			}, 5L);
		}
	}
}
