/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.listeners;

import com.massivecraft.factions.event.PowerLossEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Factions_Listener
		implements Listener {
	private Plugin plugin;

	public Factions_Listener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		this.plugin.getServer().getPluginManager().registerEvents((Listener) this, this.plugin);
	}

	@EventHandler
	public void onPlayerPowerLoss(PowerLossEvent event) {
		if (Server.moderatingPlayers.containsKey(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}

