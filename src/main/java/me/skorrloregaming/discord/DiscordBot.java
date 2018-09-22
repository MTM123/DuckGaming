package me.skorrloregaming.discord;

import me.skorrloregaming.discord.listeners.MessageListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;

public class DiscordBot {

	private String name;
	private String token;

	private JDA bot;

	private MessageListener messageListener;

	public DiscordBot(String name, String token) {
		this.token = token;
	}

	public void register() {
		try {
			bot = new JDABuilder(token).build();
			messageListener = new MessageListener(this);
			bot.addEventListener(messageListener);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void unregister() {
		bot.removeEventListener(messageListener);
		bot.shutdownNow();
	}

	public JDA getBot() {
		return bot;
	}

	public TextChannel getTextChannel(String channelName) throws Exception {
		if (channelName.contains(" "))
			throw new Exception("Channel name '" + channelName + "' is invalid!");
		for (TextChannel channel : bot.getTextChannels()) {
			if (channel.getName().equals(channelName))
				return channel;
		}
		throw new Exception("Channel '" + channelName + "' not found!");
	}

	public String getName() {
		return name;
	}

	public String getToken() {
		return token;
	}

}
