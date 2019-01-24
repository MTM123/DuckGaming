/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import org.bukkit.Bukkit;

public class Logger {
	public static void info(String msg) {
		Bukkit.getLogger().info(msg);
	}

	public static void warning(String msg) {
		Bukkit.getLogger().warning(msg);
	}

	public static void severe(String msg) {
		Bukkit.getLogger().severe(msg);
	}
}

