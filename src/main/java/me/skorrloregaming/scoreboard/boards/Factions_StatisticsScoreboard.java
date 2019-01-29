package me.skorrloregaming.scoreboard.boards;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.skorrloregaming.$;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Link$;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Hashtable;

public class Factions_StatisticsScoreboard implements DisposableScoreboard {
	@Override
	public void refreshScoreboard(Player player, boolean clearValues) {
		if (Link$.isPluginEnabled("mcMMO")) {
			if (com.gmail.nossr50.util.scoreboards.ScoreboardManager.isBoardShown(player.getName()))
				return;
		}
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Hashtable<String, Integer> list = new Hashtable<String, Integer>();
		int currentPlayerKills = $.Factions.getPlayerKills(player);
		int currentPlayerDeaths = $.Factions.getPlayerDeaths(player);
		int currentPlayerCash = EconManager.retrieveCash(player, "factions");
		FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
		Faction faction = fplayer.getFaction();
		list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Faction", 9);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Tag: " + ChatColor.RESET + faction.getTag(), 8);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Power: " + ChatColor.RESET + faction.getPowerRounded() + "/" + faction.getPowerMaxRounded(), 7);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Members: " + ChatColor.RESET + faction.getFPlayers().size(), 6);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Land: " + ChatColor.RESET + faction.getLandRounded(), 5);
		list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Statistics", 4);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Balance: " + ChatColor.RESET + "$" + formatter.format(currentPlayerCash), 3);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Kills: " + ChatColor.RESET + formatter.format(currentPlayerKills), 2);
		list.put(ChatColor.GOLD + "│" + ChatColor.GRAY + " Deaths: " + ChatColor.RESET + formatter.format(currentPlayerDeaths), 1);
		$.Scoreboard.configureSidebar(player,"SkorrloreGaming", list, clearValues, true);
	}
}
