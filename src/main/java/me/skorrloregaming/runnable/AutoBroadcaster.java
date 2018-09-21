package me.skorrloregaming.runnable;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import net.md_5.bungee.api.ChatColor;

public class AutoBroadcaster implements Runnable {
	private String[] messages;
	private int currentMsg = 0;

	public AutoBroadcaster() {
		this.messages = new String[] { "Like the server? You can support us by voting.", "Enjoy playing on here? You can help out by voting.", "Psst, you can earn a jackpot of money from voting." };
	}

	@Override
	public void run() {
		currentMsg %= messages.length;
		String message = ChatColor.stripColor($.modernMsgPrefix) + messages[currentMsg];
		TextComponent textComponent = new TextComponent(message);
		textComponent.setColor(ChatColor.ITALIC);
		textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote"));
		textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/vote").create()));
		String igMessage = ComponentSerializer.toString(textComponent);
		currentMsg++;
		for (Player player : Bukkit.getOnlinePlayers()) {
			CraftGo.Player.sendJson(player, igMessage);
		}
		Logger.info(message, true);
	}
}
