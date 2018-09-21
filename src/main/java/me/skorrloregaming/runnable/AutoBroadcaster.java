package me.skorrloregaming.runnable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import mkremins.fanciful.FancyMessage;
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
		String igMessage = new FancyMessage(message).style(mkremins.fanciful.ChatColor.BOLD).suggest("/vote").tooltip("/vote").exportToJson();
		currentMsg++;
		for (Player player : Bukkit.getOnlinePlayers()) {
			CraftGo.Player.sendJson(player, igMessage);
		}
		Logger.info(message, true);
	}
}
