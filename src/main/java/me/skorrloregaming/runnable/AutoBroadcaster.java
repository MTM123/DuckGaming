package me.skorrloregaming.runnable;

import me.skorrloregaming.Server;
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
		this.messages = new String[]{"Do you like cosmetics? You can get them with a donor rank.", "Like the server? You can support us by voting.", "Want to win the fight? Get kits with a donor rank today.", "Psst, you can earn a jackpot of money from voting."};
	}

	@Override
	public void run() {
		currentMsg %= messages.length;
		String message = ChatColor.stripColor($.modernMsgPrefix) + messages[currentMsg];
		TextComponent textComponent = new TextComponent(message);
		textComponent.setColor(ChatColor.ITALIC);
		switch (currentMsg + 1 % 2) {
			case 0:
				textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/vote"));
				textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/vote").create()));
				break;
			case 1:
				textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/store"));
				textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/store").create()));
				break;
			default:
				break;
		}
		String igMessage = ComponentSerializer.toString(textComponent);
		currentMsg++;
		for (Player player : Bukkit.getOnlinePlayers()) {
			String path = "config." + player.getUniqueId().toString();
			boolean subscribed = Boolean.parseBoolean(Server.getPlugin().getConfig().getString(path + ".subscribed", "true"));
			if (subscribed)
				CraftGo.Player.sendJson(player, igMessage);
		}
		Logger.info(message, true);
	}
}
