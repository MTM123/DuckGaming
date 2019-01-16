package me.skorrloregaming.scoreboard.boards;

import java.text.DecimalFormat;
import java.util.Hashtable;

import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import net.md_5.bungee.api.ChatColor;

public class Kitpvp_StatisticsScoreboard implements DisposableScoreboard {
	@Override
	public void refreshScoreboard(Player player, boolean clearValues) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Hashtable<String, Integer> list = new Hashtable<String, Integer>();
		int currentPlayerKills = $.Kitpvp.getPlayerKills(player);
		int currentPlayerDeaths = $.Kitpvp.getPlayerDeaths(player);
		int currentPlayerDPK = currentPlayerKills / 50;
		int currentPlayerCash = EconManager.retrieveCash(player, "kitpvp");
		list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 5);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 4);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Kills: " + ChatColor.RESET + formatter.format(currentPlayerKills), 3);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Deaths: " + ChatColor.RESET + formatter.format(currentPlayerDeaths), 2);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Level: " + ChatColor.RESET + formatter.format(currentPlayerDPK + 1), 1);
		$.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
	}
}
