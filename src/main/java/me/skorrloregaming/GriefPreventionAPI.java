package me.skorrloregaming;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import me.ryanhamshire.GriefPrevention.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.skorrloregaming.*;

public class GriefPreventionAPI {

	public static GriefPrevention getGriefPrevention() {
		return ((GriefPrevention) Bukkit.getPluginManager().getPlugin("GriefPrevention"));
	}

	public static Claim getClaimAtLocation(Player player, Location loc) {
		PlayerData playerData = GriefPrevention.instance.dataStore.getPlayerData(player.getUniqueId());
		return GriefPrevention.instance.dataStore.getClaimAt(loc, false, playerData.lastClaim);
	}

	public static boolean hasClaimInLocation(Player player, Location loc) {
		return getClaimAtLocation(player, loc) != null;
	}

	public static boolean isOwnerAtLocation(Player player, Location loc) {
		return hasClaimInLocation(player, loc) && getClaimAtLocation(player, loc).getOwnerName().equalsIgnoreCase(player.getName());
	}

}
