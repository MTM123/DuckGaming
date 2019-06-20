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
import org.bukkit.event.entity.ProjectileLaunchEvent;
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
}
