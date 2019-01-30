package me.skorrloregaming.discord.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.skorrloregaming.LinkServer;
import me.skorrloregaming.Server;
import me.skorrloregaming.discord.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class BungeeListener implements PluginMessageListener {

	public void register() {
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(LinkServer.getPlugin(), "Discord", this);
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(LinkServer.getPlugin(), "BungeeCord");
	}

	public void unregister() {
		Bukkit.getServer().getMessenger().unregisterIncomingPluginChannel(LinkServer.getPlugin(), "Discord", this);
		Bukkit.getServer().getMessenger().unregisterOutgoingPluginChannel(LinkServer.getPlugin(), "BungeeCord");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player p, byte[] message) {
		try {
			DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
			if (in.readUTF().equals("Broadcast"))
				Server.getDiscordBot().broadcast(in.readUTF(), Channel.SERVER_CHAT);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
