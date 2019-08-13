package me.skorrloregaming.discord;

import me.skorrloregaming.discord.listeners.MessageListener;
import me.skorrloregaming.discord.listeners.ReadyListener;
import me.skorrloregaming.discord.listeners.RedisListener;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.requests.restaction.MessageAction;

import java.util.concurrent.TimeUnit;

import me.skorrloregaming.*;

public class DiscordBot {

	private String name;
	private String token;
	private long guild;

	private JDA bot;

	private RedisListener redisListener;
	private ReadyListener readyListener;
	private MessageListener messageListener;

	private boolean running = false;

	public DiscordBot(String name, String token, long guild) {
		this.name = name;
		this.token = token;
		this.guild = guild;
	}

	public void register() {
		try {
			readyListener = new ReadyListener(this);
			bot = new JDABuilder(token)
					.addEventListener(readyListener)
					.build();
			messageListener = new MessageListener(this);
			redisListener = new RedisListener();
			redisListener.register();
			bot.addEventListener(messageListener);
			bot.getPresence().setGame(Game.of(Game.GameType.DEFAULT, "Minecraft"));
			running = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void unregister() {
		running = false;
		bot.removeEventListener(messageListener);
		bot.removeEventListener(readyListener);
		bot.shutdown();
		redisListener.unregister();
	}

	public JDA getBot() {
		return bot;
	}

	public long getGuildId() {
		return guild;
	}

	public Guild getGuild() {
		return getBot().getGuildById(guild);
	}

	public String getChannelName(Channel channel) {
		return channel.toString().toLowerCase().replace("_", "-");
	}

	public void broadcast(String message, Channel... channels) {
		broadcast(message, 0L, channels);
	}

	public void broadcast(String message, long deleteAfter, Channel... channels) {
		if (!running)
			return;
		for (Channel channel : channels)
			try {
				MessageAction messageAction = getTextChannel(getChannelName(channel)).sendMessage(message);
				Message channelMessage = messageAction.complete();
				if (deleteAfter > 0L)
					channelMessage.delete().queueAfter(deleteAfter, TimeUnit.MILLISECONDS);
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
