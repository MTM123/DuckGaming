package me.skorrloregaming.discord.listeners;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import me.skorrloregaming.discord.Channel;
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
				if (event.getTextChannel().getName().equals(Channel.SERVER_CHAT.toString().toLowerCase())) {
					String rawMessage = event.getMessage().getContentDisplay();
					String memberName = event.getMember().getEffectiveName();
					String displayName = $.getFlashPlayerDisplayName(memberName);
					String username = event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator();
					TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));
					BaseComponent[] nameAndDiscriminator;
					if (event.getMember().getGame() == null) {
						nameAndDiscriminator = new ComponentBuilder(memberName).color(ChatColor.BOLD).append(newLine)
								.append(username).create();
					} else {
						nameAndDiscriminator = new ComponentBuilder(memberName).color(ChatColor.BOLD).append(newLine)
								.append(username).append(newLine).append(newLine)
								.append("Playing " + event.getMember().getGame().getName()).create();
					}
					HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, nameAndDiscriminator);
					BaseComponent[] message = new ComponentBuilder("[").color(ChatColor.GRAY).event(hoverEvent).append("discord")
							.color(ChatColor.WHITE).event(hoverEvent).append("] ").color(ChatColor.GRAY).event(hoverEvent)
							.append(displayName).color(ChatColor.RESET).event(hoverEvent).append(" " + '\u00BB' + " ")
							.color(ChatColor.RESET).event(hoverEvent).append(rawMessage).event(hoverEvent).create();
					Logger.info(new TextComponent(message).toPlainText(), true);
					for (Player player : Bukkit.getOnlinePlayers()) {
						CraftGo.Player.sendJson(player, ComponentSerializer.toString(message));
					}
				}
		}
	}
}
