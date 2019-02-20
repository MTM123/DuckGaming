package me.skorrloregaming.discord.listeners;

import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.discord.DiscordBot;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.hooks.EventListener;

public class ReadyListener implements EventListener {

	private DiscordBot discordBot;

	public ReadyListener(DiscordBot discordBot) {
		this.discordBot = discordBot;
	}

	@Override
	public void onEvent(Event event) {
		if (event instanceof ReadyEvent)
			discordBot.broadcast(":white_check_mark: **Server has started**", Channel.SERVER_CHAT);
	}
}
