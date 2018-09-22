package me.skorrloregaming.discord.listeners;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import me.skorrloregaming.discord.DiscordBot;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageListener extends ListenerAdapter {

	private DiscordBot discordBot;

	public MessageListener(DiscordBot discordBot) {
		this.discordBot = discordBot;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (event.isFromType(ChannelType.TEXT)) {
			if (!event.getAuthor().isBot())
				if (event.getTextChannel().getName().equals("minecraft-chat")) {
					String rawMessage = event.getMessage().getContentDisplay();
					String memberName = event.getMember().getEffectiveName();
					String displayName = $.getFlashPlayerDisplayName(memberName);
					BaseComponent[] nameAndDiscriminator = new ComponentBuilder(memberName + "#" + event.getMember().getUser().getDiscriminator()).create();
					HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, nameAndDiscriminator);
					BaseComponent[] message = new ComponentBuilder("[").color(ChatColor.GRAY).append("discord")
							.color(ChatColor.WHITE).append("] ").color(ChatColor.GRAY).append(displayName).color(ChatColor.RESET)
							.append(" " + '\u00BB' + " ").color(ChatColor.RESET).append(rawMessage).event(hoverEvent).create();
					Logger.info(new TextComponent(message).toPlainText(), true);
					for (Player player : Bukkit.getOnlinePlayers()) {
						CraftGo.Player.sendJson(player, ComponentSerializer.toString(message));
					}
				}
		}
	}
}
