package me.skorrloregaming;

import org.bukkit.Bukkit;

import me.skorrloregaming.*;

public class ServerGet {

	public static IServer get() {
		return (IServer) Bukkit.getPluginManager().getPlugin("SkorrloreGaming");
	}

}
