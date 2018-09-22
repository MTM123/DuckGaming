package me.skorrloregaming.discord;

import me.skorrloregaming.discord.listeners.MessageListener;
import me.skorrloregaming.discord.listeners.ReadyListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;

public class DiscordBot {

	private String name;
	private String token;

	private JDA bot;

	private ReadyListener readyListener;
	private MessageListener messageListener;

	public DiscordBot(String name, String token) {
		this.token = token;
	}

	public void register() {
		try {
			readyListener = new ReadyListener(this);
			bot = new JDABuilder(token)
					.addEventListener(readyListener)
					.build();
			messageListener = new MessageListener(this);
			bot.addEventListener(messageListener);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void unregister() {
		bot.removeEventListener(messageListener);
		bot.removeEventListener(readyListener);
		bot.shutdown();
	}

	public JDA getBot() {
		return bot;
	}

	public void broadcast(String message, Channel... channels) {
		for (Channel channel : channels)
			try {
				getTextChannel(channel.toString().toLowerCase()).sendMessage(message).queue();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
