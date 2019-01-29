package me.skorrloregaming.hooks;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.discord.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
