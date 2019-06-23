package me.skorrloregaming;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class GriefPreventionAPI {

	public static GriefPrevention getGriefPrevention() {
		return ((GriefPrevention) Bukkit.getPluginManager().getPlugin("GriefPrevention"));
	}

	public static Claim getClaimAtLocation(Location location) {
		return GriefPrevention.instance.dataStore.getClaimAt(location, true, null);
	}

}
