package com.skorrloregaming.main.SkyFight;

import com.skorrloregaming.main.SkorrloreGaming;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class SkyFight {
	public static HashMap<Player, String> lastAttacker = new HashMap();
	public static HashMap<Player, Integer> killstreak = new HashMap();
	static Plugin plugin = SkorrloreGaming.getInstance;

	public static void lastAttackerEvent(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player attacker = (Player) event.getDamager();
			lastAttacker.put((Player) event.getEntity(), attacker.getName());
		} else if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
			Player attacker = (Player) ((Arrow) event.getDamager()).getShooter();
			lastAttacker.put((Player) event.getEntity(), attacker.getName());
		}
	}

	public static void killstreakHandler(Player player) {
		int currentKillstreak = 0;
		if (killstreak.containsKey((Object) player)) {
			currentKillstreak = killstreak.get((Object) player);
		}
		killstreak.put(player, currentKillstreak + 1);
	}

	public static void removeLastAttacker(Player player) {
		lastAttacker.remove((Object) player);
		killstreak.remove((Object) player);
	}

	public static int getKillstreak(Player player) {
		int currentKillstreak = 0;
		if (killstreak.containsKey((Object) player)) {
			currentKillstreak = killstreak.get((Object) player);
		}
		return currentKillstreak;
	}

	public static void handleTokenAdd(Player player, int amount) {
		if (!plugin.getConfig().contains("ranks." + player.getUniqueId().toString() + ".tokens")) {
			plugin.getConfig().set("ranks." + player.getUniqueId().toString() + ".tokens", (Object) 0);
			plugin.saveConfig();
		}
		int currentTokens = plugin.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".tokens");
		plugin.getConfig().set("ranks." + player.getUniqueId().toString() + ".tokens", (Object) (currentTokens + amount));
		plugin.saveConfig();
	}

	public static void handleTokenRemove(Player player, int amount) {
		int currentTokens;
		int newTokens;
		if (!plugin.getConfig().contains("ranks." + player.getUniqueId().toString() + ".tokens")) {
			plugin.getConfig().set("ranks." + player.getUniqueId().toString() + ".tokens", (Object) 0);
			plugin.saveConfig();
		}
		if ((newTokens = (currentTokens = plugin.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".tokens")) - amount) < 0) {
			newTokens = 0;
		}
		plugin.getConfig().set("ranks." + player.getUniqueId().toString() + ".tokens", (Object) newTokens);
		plugin.saveConfig();
	}

	public static int getPlayerTokens(Player player) {
		int currentTokens;
		if (!plugin.getConfig().contains("ranks." + player.getUniqueId().toString() + ".tokens")) {
			plugin.getConfig().set("ranks." + player.getUniqueId().toString() + ".tokens", (Object) 0);
			plugin.saveConfig();
		}
		if ((currentTokens = plugin.getConfig().getInt("ranks." + player.getUniqueId().toString() + ".tokens")) < 0) {
			currentTokens = 0;
		}
		return currentTokens;
	}
}

