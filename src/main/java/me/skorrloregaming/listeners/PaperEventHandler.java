package me.skorrloregaming.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

public class PaperEventHandler implements Listener {

	public PaperEventHandler() {
		Server.getPlugin().getLogger().info("Registered PaperSpigot specific event listener");
		Server.getPlugin().getLogger().info("If issues arise from this, report them immediately.");
	}

	@EventHandler
	public void onPlayerJump(PlayerJumpEvent event) {
		Player player = event.getPlayer();
		if (Server.getWaiverAcceptPlayers().contains(player.getUniqueId())) {
			event.setCancelled(true);
			return;
		}
	}


	@EventHandler
	public void on(PlayerLaunchProjectileEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("Hit 1");
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
			player.sendMessage("Hit 2");
			Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {

				@Override
				public void run() {
					player.sendMessage("Hit 3");
					ItemStack mainHand = player.getInventory().getItemInMainHand();
					if (mainHand.getType() == Material.CROSSBOW) {
						player.sendMessage("Hit 4");
						CrossbowMeta crossBowMeta = (CrossbowMeta) mainHand.getItemMeta();
						crossBowMeta.addChargedProjectile(Link$.createMaterial(Material.ARROW));
						mainHand.setItemMeta(crossBowMeta);
						player.getInventory().setItemInMainHand(mainHand);
						player.sendMessage("Recharged");
					}
				}
			}, 20l);
			Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
				public void run() {
					Server.doReturnItem(player);
				}
			}, 5L);
		}
	}
}
