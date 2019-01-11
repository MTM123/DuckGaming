package me.skorrloregaming;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardAPI {

	public static WorldGuardPlugin getWorldGuard() {
		return ((WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard"));
	}

	public static WorldEditPlugin getWorldEdit() {
		return ((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit"));
	}

	public static World getWorldByName(String worldName) {
		for (Platform platform : WorldEdit.getInstance().getPlatformManager().getPlatforms()) {
			for (World world : platform.getWorlds()) {
				if (world.getName().equals(worldName))
					return world;
			}
		}
		throw new NullPointerException();
	}

	public static BlockVector3 getVectorFromLocation(Location location) {
		return BlockVector3.at(location.getX(), location.getY(), location.getZ());
	}

	public static int getApplicableRegionsSetSize(Location location) {
		RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(getWorldByName(location.getWorld().getName()));
		BlockVector3 vector = getVectorFromLocation(location);
		ApplicableRegionSet arSet = regionManager.getApplicableRegions(vector);
		return arSet.size();
	}

	public static boolean testPassthroughRegionFlag(Player player, Location location) {
		RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(getWorldByName(location.getWorld().getName()));
		BlockVector3 vector = getVectorFromLocation(location);
		ApplicableRegionSet arSet = regionManager.getApplicableRegions(vector);
		return arSet.testState(getWorldGuard().wrapPlayer(player), Flags.PASSTHROUGH);
	}

	public static boolean testBlockBreakRegionFlag(Player player, Location location) {
		RegionManager regionManager = WorldGuard.getInstance().getPlatform().getRegionContainer().get(getWorldByName(location.getWorld().getName()));
		BlockVector3 vector = getVectorFromLocation(location);
		ApplicableRegionSet arSet = regionManager.getApplicableRegions(vector);
		return arSet.testState(getWorldGuard().wrapPlayer(player), Flags.BLOCK_BREAK);
	}

}
