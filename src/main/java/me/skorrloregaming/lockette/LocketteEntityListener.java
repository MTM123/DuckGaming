package me.skorrloregaming.lockette;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.PluginManager;

import java.util.Iterator;

public class LocketteEntityListener implements Listener {
	public LocketteEntityListener(Lockette instance) {
	}

	protected void registerEvents() {
		PluginManager pm = Server.getInstance().getPlugin().getServer().getPluginManager();
		pm.registerEvents(this, Server.getInstance().getPlugin());
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onEntityExplode(EntityExplodeEvent event) {
		if (event.isCancelled())
			return;
		if (!$.validLocketteMinigames.contains($.getMinigameFromWorld(event.getEntity().getWorld()).toString().toLowerCase()))
			return;
		if (Lockette.explosionProtectionAll) {
			Iterator<Block> it = event.blockList().iterator();
			while (it.hasNext()) {
				Block block = it.next();
				if (Lockette.isProtected(block)) {
					it.remove();
					continue;
				}
				if (BlockUtil.isInList(block.getType(), BlockUtil.materialListNonDoors)) {
					it.remove();
					continue;
				}
			}
		}
	}
}
