/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.listeners;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityDeactivateEvent;
import me.skorrloregaming.Go;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class mcMMO_Listener
		implements Listener {
	private Plugin plugin;

	public mcMMO_Listener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		this.plugin.getServer().getPluginManager().registerEvents((Listener) this, this.plugin);
	}

	@EventHandler
	public void onPlayerAnythingEvent(McMMOPlayerXpGainEvent event) {
		if (Go.getCurrentMinigame(event.getPlayer()) != ServerMinigame.FACTIONS) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerActivateAbility(McMMOPlayerAbilityActivateEvent event) {
		if (Go.getCurrentMinigame(event.getPlayer()) != ServerMinigame.FACTIONS) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeactivateAbility(McMMOPlayerAbilityDeactivateEvent event) {
	}
}

