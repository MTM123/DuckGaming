package me.skorrloregaming;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardAPI {

	public static Object getWorldGuard() {
		return Bukkit.getPluginManager().getPlugin("WorldGuard");
	}

	public static Object getWorldEdit() {
		return Bukkit.getPluginManager().getPlugin("WorldEdit");
	}

	public static Object getVectorFromLocation(Location location) {
		return new com.sk89q.worldedit.Vector(location.getX(), location.getY(), location.getZ());
	}

	public static int getApplicableRegionsSetSize(Location location) {
		Object platform = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform();
		Object world = ((com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getWorldByName(location.getWorld().getName())));
		Object regionContainer = ((com.sk89q.worldguard.internal.platform.WorldGuardPlatform) platform).getRegionContainer();
		Object regionManager = ((com.sk89q.worldguard.protection.regions.RegionContainer) regionContainer).get((com.sk89q.worldedit.world.World) world);
		Object vector = getVectorFromLocation(location);
		Object arSet = ((com.sk89q.worldguard.protection.managers.RegionManager) regionManager).getApplicableRegions((com.sk89q.worldedit.Vector) vector);
		return ((com.sk89q.worldguard.protection.ApplicableRegionSet) arSet).size();
	}

	public static boolean testPassthroughRegionFlag(Player player, Location location) {
		Object platform = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform();
		Object world = ((com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getWorldByName(location.getWorld().getName())));
		Object regionContainer = ((com.sk89q.worldguard.internal.platform.WorldGuardPlatform) platform).getRegionContainer();
		Object regionManager = ((com.sk89q.worldguard.protection.regions.RegionContainer) regionContainer).get((com.sk89q.worldedit.world.World) world);
		Object vector = getVectorFromLocation(location);
		Object arSet = ((com.sk89q.worldguard.protection.managers.RegionManager) regionManager).getApplicableRegions((com.sk89q.worldedit.Vector) vector);
		return ((com.sk89q.worldguard.protection.ApplicableRegionSet) arSet).testState(((com.sk89q.worldguard.bukkit.WorldGuardPlugin) getWorldGuard()).wrapPlayer(player), com.sk89q.worldguard.protection.flags.Flags.PASSTHROUGH);
	}

	public static boolean testBlockBreakRegionFlag(Player player, Location location) {
		Object platform = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform();
		Object world = ((com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getWorldByName(location.getWorld().getName())));
		Object regionContainer = ((com.sk89q.worldguard.internal.platform.WorldGuardPlatform) platform).getRegionContainer();
		Object regionManager = ((com.sk89q.worldguard.protection.regions.RegionContainer) regionContainer).get((com.sk89q.worldedit.world.World) world);
		Object vector = getVectorFromLocation(location);
		Object arSet = ((com.sk89q.worldguard.protection.managers.RegionManager) regionManager).getApplicableRegions((com.sk89q.worldedit.Vector) vector);
		return ((com.sk89q.worldguard.protection.ApplicableRegionSet) arSet).testState(((com.sk89q.worldguard.bukkit.WorldGuardPlugin) getWorldGuard()).wrapPlayer(player), com.sk89q.worldguard.protection.flags.Flags.BLOCK_BREAK);
	}

}
