package me.skorrloregaming.scoreboard.boards;

import java.util.Hashtable;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.$.Skyfight;
import me.skorrloregaming.Server;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import net.md_5.bungee.api.ChatColor;

public class Skyfight_LeaderboardScoreboard implements DisposableScoreboard {
	@Override
	public void refreshScoreboard(Player player, boolean clearValues) {
		UUID[] skyfightPlayers = Server.getSkyfight().keySet().toArray(new UUID[0]);
		Hashtable<String, Integer> list = new Hashtable<>();
		int best = 0;
		for (UUID id : skyfightPlayers) {
			org.bukkit.entity.Player otherPlayer = Bukkit.getPlayer(id);
			int score = Server.getSkyfight().get(otherPlayer.getUniqueId()).getScore();
			if (score > best)
				best = score;
		}
		list.put(ChatColor.GOLD + "■" + ChatColor.YELLOW + " Leaderboard", (int) ((Math.floor(best / 5) + 1) * 5));
		for (int i = 0; i < skyfightPlayers.length; i++) {
			UUID id = skyfightPlayers[i];
			org.bukkit.entity.Player otherPlayer = Bukkit.getPlayer(id);
			Skyfight.Player osfPlayer = Server.getSkyfight().get(otherPlayer.getUniqueId());
			ChatColor prefix = ChatColor.RESET;
			if (osfPlayer.getTeamValue() == Skyfight.Team.BLUE) {
				prefix = ChatColor.BLUE;
			} else if (osfPlayer.getTeamValue() == Skyfight.Team.RED) {
				prefix = ChatColor.RED;
			} else if (osfPlayer.getTeamValue() == Skyfight.Team.GREEN) {
				prefix = ChatColor.GREEN;
			} else if (osfPlayer.getTeamValue() == Skyfight.Team.YELLOW) {
				prefix = ChatColor.YELLOW;
			} else if (osfPlayer.getTeamValue() == Skyfight.Team.PINK) {
				prefix = ChatColor.LIGHT_PURPLE;
			}
			list.put(ChatColor.GOLD + "│ " + prefix + otherPlayer.getName(), Server.getSkyfight().get(otherPlayer.getUniqueId()).getScore());
		}
		for (UUID id : skyfightPlayers) {
			org.bukkit.entity.Player op = Bukkit.getPlayer(id);
			$.Scoreboard.configureSidebar(op, ChatColor.RESET + "SkorrloreGaming", list, clearValues, true);
		}
	}
}
