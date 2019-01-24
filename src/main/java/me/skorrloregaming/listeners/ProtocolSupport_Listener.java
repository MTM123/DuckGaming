/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.listeners;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;

import me.skorrloregaming.Go;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.events.PlayerLoginStartEvent;

public class ProtocolSupport_Listener
		implements Listener {
	private Plugin plugin;
	List<Integer> supportedVersions = Arrays.asList(338, 335, 316, 315, 210, 110, 109, 108, 107, 47, 5, -1);

	public ProtocolSupport_Listener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		this.plugin.getServer().getPluginManager().registerEvents((Listener) this, this.plugin);
	}

	@EventHandler
	public void onPlayerLogin(PlayerLoginStartEvent event) {
		ProtocolVersion version = ProtocolSupportAPI.getProtocolVersion((SocketAddress) event.getAddress());
		int id = version.getId();
		if (!this.supportedVersions.contains(id)) {
			String kickMessage = "Please connect to this server using Minecraft 1.7.6 or above";
			event.denyLogin(kickMessage);
			Bukkit.broadcastMessage((String) (String.valueOf(Go.italicGray) + "Server: Disallow " + event.getName() + " '" + kickMessage + "'"));
		}
	}
}

