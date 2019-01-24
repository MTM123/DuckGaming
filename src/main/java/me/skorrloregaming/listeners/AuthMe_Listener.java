/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.listeners;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import me.skorrloregaming.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class AuthMe_Listener
		implements Listener {
	private Plugin plugin;

	public AuthMe_Listener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		this.plugin.getServer().getPluginManager().registerEvents((Listener) this, this.plugin);
	}

	@EventHandler
	public void onPlayerAuth(LoginEvent event) {
		Server.onPlayerJoin(new PlayerJoinEvent(event.getPlayer(), Server.defaultJoinMessage.replace("{player}", event.getPlayer().getName())));
	}

	@EventHandler
	public void onPlayerLogout(LogoutEvent event) {
		event.getPlayer().kickPlayer("You have been logged out of the server.");
	}
}

