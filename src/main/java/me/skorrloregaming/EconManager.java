package me.skorrloregaming;

import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EconManager {
	public static final double MAX_CASH = 1_000_000_000.0;
	public static final double MIN_CASH = 0.0;

	private static void updateScoreboards(String subDomain, UUID id) {
		Player player = Bukkit.getPlayer(id);
		if (player == null)
			return;
		ServerMinigame minigame = $.getMinigameFromWorld(player.getWorld());
		DisposableScoreboard scoreboard = $.getPrimaryScoreboard(minigame);
		if (!(scoreboard == null))
			scoreboard.schedule(player);
	}

	public static void withdrawCash(UUID id, double amount, String subDomain, boolean updateScoreboard) {
		if ($.validEconomyMinigames.contains(subDomain)) {
			String path = "config." + id.toString() + ".balance." + subDomain;
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path, 0);
			}
			double value = Double.parseDouble(Server.getPlugin().getConfig().getString(path)) - amount;
			if (value < MIN_CASH)
				value = MIN_CASH;
			Server.getPlugin().getConfig().set(path, value + "");
			if (updateScoreboard) {
				updateScoreboards(subDomain, id);
			}
		}
	}

	public static void withdrawCash(UUID id, double amount, String subDomain) {
		withdrawCash(id, amount, subDomain, true);
	}

	public static void depositCash(UUID id, double amount, String subDomain) {
		depositCash(id, amount, subDomain, true);
	}

	public static void depositCash(UUID id, double amount, String subDomain, boolean updateScoreboard) {
		if ($.validEconomyMinigames.contains(subDomain)) {
			String path = "config." + id.toString() + ".balance." + subDomain;
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path, 0);
			}
			double value = Double.parseDouble(Server.getPlugin().getConfig().getString(path)) + amount;
			if (value > MAX_CASH)
				value = MAX_CASH;
			Server.getPlugin().getConfig().set(path, value + "");
			if (updateScoreboard) {
				updateScoreboards(subDomain, id);
			}
		}
	}

	public static double retrieveCash(UUID id, String subDomain) {
		if ($.validEconomyMinigames.contains(subDomain)) {
			String path = "config." + id.toString() + ".balance." + subDomain;
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path, 0.0);
			}
			return Double.parseDouble(Server.getPlugin().getConfig().getString(path));
		} else {
			return 0.0;
		}
	}

	public static void withdrawCash(Player player, double amount, String subDomain) {
		withdrawCash(player.getUniqueId(), amount, subDomain);
	}

	public static void depositCash(Player player, double amount, String subDomain) {
		depositCash(player.getUniqueId(), amount, subDomain);
	}

	public static void withdrawCash(Player player, double amount, String subDomain, boolean updateScoreboard) {
		withdrawCash(player.getUniqueId(), amount, subDomain, updateScoreboard);
	}

	public static void depositCash(Player player, double amount, String subDomain, boolean updateScoreboard) {
		depositCash(player.getUniqueId(), amount, subDomain, updateScoreboard);
	}

	public static double retrieveCash(Player player, String subDomain) {
		return retrieveCash(player.getUniqueId(), subDomain);
	}

	public static double getWorth(Material material, ServerMinigame minigame) {
		if (Server.getPlugin().getConfig().contains("settings.economy.modifier." + minigame.toString() + "." + material.toString()))
			return Server.getPlugin().getConfig().getDouble("settings.economy.modifier." + minigame.toString() + "." + material.toString());
		return 0.0;
	}

}
