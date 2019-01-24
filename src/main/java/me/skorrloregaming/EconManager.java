/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import java.util.UUID;

import me.skorrloregaming.Go;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class EconManager {
	public static final int MAX_CASH = 1000000000;
	public static final int MIN_CASH = 0;

	public static void withdrawCash(UUID id, int amount, String subDomain, Plugin plugin) {
		int value;
		if (subDomain.equals("hub") || subDomain.equals("skyfight") || subDomain.equals("creative")) {
			return;
		}
		String path = "config." + id.toString() + ".balance." + subDomain;
		if (!plugin.getConfig().contains(path)) {
			plugin.getConfig().set(path,  0);
			plugin.saveConfig();
		}
		if ((value = Integer.parseInt(plugin.getConfig().getString(path)) - amount) < 0) {
			value = 0;
		}
		plugin.getConfig().set(path,  String.valueOf(value));
		plugin.saveConfig();
		if (subDomain == "kitpvp" && Bukkit.getPlayer((UUID) id) != null) {
			Go.Kitpvp.refreshScoreboard(Bukkit.getPlayer((UUID) id), plugin);
		}
	}

	public static void depositCash(UUID id, int amount, String subDomain, Plugin plugin) {
		int value;
		if (subDomain.equals("hub") || subDomain.equals("skyfight") || subDomain.equals("creative")) {
			return;
		}
		String path = "config." + id.toString() + ".balance." + subDomain;
		if (!plugin.getConfig().contains(path)) {
			plugin.getConfig().set(path,  0);
			plugin.saveConfig();
		}
		if ((value = Integer.parseInt(plugin.getConfig().getString(path)) + amount) > 1000000000) {
			value = 1000000000;
		}
		plugin.getConfig().set(path,  String.valueOf(value));
		plugin.saveConfig();
		if (subDomain == "kitpvp" && Bukkit.getPlayer((UUID) id) != null) {
			Go.Kitpvp.refreshScoreboard(Bukkit.getPlayer((UUID) id), plugin);
		}
	}

	public static int retrieveCash(UUID id, String subDomain, Plugin plugin) {
		if (subDomain.equals("hub") || subDomain.equals("skyfight") || subDomain.equals("creative")) {
			return 0;
		}
		String path = "config." + id.toString() + ".balance." + subDomain;
		if (!plugin.getConfig().contains(path)) {
			plugin.getConfig().set(path,  0);
			plugin.saveConfig();
		}
		return Integer.parseInt(plugin.getConfig().getString(path));
	}

	public static void withdrawCash(Player player, int amount, String subDomain, Plugin plugin) {
		EconManager.withdrawCash(player.getUniqueId(), amount, subDomain, plugin);
	}

	public static void depositCash(Player player, int amount, String subDomain, Plugin plugin) {
		EconManager.depositCash(player.getUniqueId(), amount, subDomain, plugin);
	}

	public static int retrieveCash(Player player, String subDomain, Plugin plugin) {
		return EconManager.retrieveCash(player.getUniqueId(), subDomain, plugin);
	}
}

