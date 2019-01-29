package me.skorrloregaming.hooks;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class BungeeCord_Listener implements PluginMessageListener {

	public void register() {
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Server.getPlugin(), "BungeeCord");
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(Server.getPlugin(), "BungeeCord", this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
		if (!channel.equals("BungeeCord")) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
	}
}
