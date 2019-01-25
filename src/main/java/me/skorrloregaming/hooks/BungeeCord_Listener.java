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
		String server = in.readUTF();
		if (server.equals("prison")) {
			String[] playerList = in.readUTF().split(", ");
			List<String> deadPlayers = new ArrayList<>();
			for (String playerName : playerList)
				if (!Server.getPrison().contains(playerName)) {
					Server.getPrison().add(playerName);
					String message = Server.getPluginLabel() + ChatColor.RED + playerName + ChatColor.GRAY + " has logged into " + ChatColor.RED + "Prison";
					Bukkit.broadcastMessage(message);
					message = message.substring(message.indexOf(ChatColor.RED + ""));
					Server.getDiscordBot().broadcast(
							ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
							, Channel.SERVER_CHAT);
				}
			for (String playerName : Server.getPrison()) {
				boolean hit = false;
				for (String playerName2 : playerList)
					if (playerName2.equals(playerName))
						hit = true;
				if (!hit)
					deadPlayers.add(playerName);
			}
			for (String playerName : deadPlayers) {
				Server.getPrison().remove(deadPlayers);
				String message = Server.getPluginLabel() + ChatColor.RED + playerName + ChatColor.GRAY + " has quit " + ChatColor.RED + "Prison";
				Bukkit.broadcastMessage(message);
				message = message.substring(message.indexOf(ChatColor.RED + ""));
				Server.getDiscordBot().broadcast(
						ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
						, Channel.SERVER_CHAT);
			}
		}
	}
}
