package me.skorrloregaming.discord.listeners;

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
		if (event instanceof ReadyEvent) {
			try {
				discordBot.getTextChannel("minecraft-chat").sendMessage(":large_blue_circle: Server started.").queue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
