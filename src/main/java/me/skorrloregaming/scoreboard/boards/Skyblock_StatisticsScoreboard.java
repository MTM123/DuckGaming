package me.skorrloregaming.scoreboard.boards;

import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Link$;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

import java.text.DecimalFormat;
import java.util.Hashtable;

public class Skyblock_StatisticsScoreboard implements DisposableScoreboard {
	@Override
	public void refreshScoreboard(Player player, boolean clearValues) {
		if (!Link$.isPluginEnabled("uSkyBlock")) {
			return;
		}
		uSkyBlockAPI api = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");
		if (Link$.isPluginEnabled("mcMMO")) {
			try {
				if (com.gmail.nossr50.util.scoreboards.ScoreboardManager.isBoardShown(player.getName()))
					return;
			} catch (Exception ex) {
			}
		}
		int members = 0, maxmembers = 0, level = 0;
		try {
			members = api.getIslandInfo(player).getPartySize();
			maxmembers = api.getIslandInfo(player).getMaxPartySize();
			level = (int) api.getIslandLevel(player);
		} catch (Exception ex) {
		}
		int placedBlocks = $.Skyblock.getPlayerPlacedBlocks(player);
		int brokenBlocks = $.Skyblock.getPlayerBrokenBlocks(player);
		double currentPlayerCash = EconManager.retrieveCash(player, "skyblock");
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Hashtable<String, Integer> list = new Hashtable<String, Integer>();
		if (maxmembers > 0) {
			list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Island", 8);
			list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Level: " + ChatColor.RESET + level, 7);
			list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Members: " + ChatColor.RESET + members + "/" + maxmembers, 6);
			list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Size: " + ChatColor.RESET + "128 x 128", 5);
			list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 4);
		} else {
			list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 4);
		}
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 3);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Placed: " + ChatColor.RESET + formatter.format(placedBlocks), 2);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Broken: " + ChatColor.RESET + formatter.format(brokenBlocks), 1);
		$.Scoreboard.configureSidebar(player, "SkorrloreGaming", list, clearValues, true);
	}
}
