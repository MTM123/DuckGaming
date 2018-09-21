package me.skorrloregaming.notify;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AlternateClient {
	private Client client = null;
	private CommandSender sender = null;

	public AlternateClient(CommandSender sender) {
		this.sender = sender;
	}

	public AlternateClient(Client client) {
		this.client = client;
	}

	public void sendMessage(String... message) {
		for (String str : message)
			sendMessage(str);
	}

	public void sendMessage(String message) {
		if (!(client == null) && sender == null) {
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			try {
				client.socket.getOutputStream().write(ChatColor.stripColor(dateFormat.format(new Date()) + " [INFO] " + message + '\n').getBytes());
				client.socket.getOutputStream().flush();
			} catch (Exception ex) {
			}
		} else if (!(sender == null) && client == null) {
			sender.sendMessage(message);
		}
	}
}
