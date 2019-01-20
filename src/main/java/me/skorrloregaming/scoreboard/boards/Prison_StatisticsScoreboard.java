package me.skorrloregaming.scoreboard.boards;

import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Hashtable;

public class Prison_StatisticsScoreboard implements DisposableScoreboard {
	@Override
	public void refreshScoreboard(Player player, boolean clearValues) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Hashtable<String, Integer> list = new Hashtable<String, Integer>();
		int currentPlayerCash = EconManager.retrieveCash(player, "prison");
		int currentPlayerLevel = 0;
		list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 3);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 2);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Level: " + ChatColor.RESET + formatter.format(currentPlayerLevel), 1);
		$.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
	}
}
