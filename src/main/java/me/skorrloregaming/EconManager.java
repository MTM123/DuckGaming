package me.skorrloregaming;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.scoreboard.DisposableScoreboard;

public class EconManager {
	public static final int MAX_CASH = 1_000_000_000;
	public static final int MIN_CASH = 0;

	private static void updateScoreboards(String subDomain, UUID id) {
		Player player = Bukkit.getPlayer(id);
		if (player == null)
			return;
		ServerMinigame minigame = $.getMinigameFromWorld(player.getWorld());
		DisposableScoreboard scoreboard = $.getPrimaryScoreboard(minigame);
		if (!(scoreboard == null))
			scoreboard.schedule(player);
	}

	public static void withdrawCash(UUID id, int amount, String subDomain, boolean updateScoreboard) {
		if (subDomain.equals("kitpvp") || subDomain.equals("factions") || subDomain.equals("skyblock")) {
			String path = "config." + id.toString() + ".balance." + subDomain;
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path, 0);
			}
			int value = Integer.parseInt(Server.getPlugin().getConfig().getString(path)) - amount;
			if (value < MIN_CASH)
				value = MIN_CASH;
			Server.getPlugin().getConfig().set(path, value + "");
			if (updateScoreboard) {
				updateScoreboards(subDomain, id);
			}
		}
	}

	public static void withdrawCash(UUID id, int amount, String subDomain) {
		withdrawCash(id, amount, subDomain, true);
	}

	public static void depositCash(UUID id, int amount, String subDomain) {
		depositCash(id, amount, subDomain, true);
	}

	public static void depositCash(UUID id, int amount, String subDomain, boolean updateScoreboard) {
		if (subDomain.equals("kitpvp") || subDomain.equals("factions") || subDomain.equals("skyblock")) {
			String path = "config." + id.toString() + ".balance." + subDomain;
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path, 0);
			}
			int value = Integer.parseInt(Server.getPlugin().getConfig().getString(path)) + amount;
			if (value > MAX_CASH)
				value = MAX_CASH;
			Server.getPlugin().getConfig().set(path, value + "");
			if (updateScoreboard) {
				updateScoreboards(subDomain, id);
			}
		}
	}

	public static int retrieveCash(UUID id, String subDomain) {
		if (subDomain.equals("kitpvp") || subDomain.equals("factions") || subDomain.equals("skyblock")) {
			String path = "config." + id.toString() + ".balance." + subDomain;
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path, 0);
			}
			return Integer.parseInt(Server.getPlugin().getConfig().getString(path));
		} else {
			return 0;
		}
	}

	public static void withdrawCash(Player player, int amount, String subDomain) {
		withdrawCash(player.getUniqueId(), amount, subDomain);
	}

	public static void depositCash(Player player, int amount, String subDomain) {
		depositCash(player.getUniqueId(), amount, subDomain);
	}

	public static void withdrawCash(Player player, int amount, String subDomain, boolean updateScoreboard) {
		withdrawCash(player.getUniqueId(), amount, subDomain, updateScoreboard);
	}

	public static void depositCash(Player player, int amount, String subDomain, boolean updateScoreboard) {
		depositCash(player.getUniqueId(), amount, subDomain, updateScoreboard);
	}

	public static int retrieveCash(Player player, String subDomain) {
		return retrieveCash(player.getUniqueId(), subDomain);
	}
}
