package me.skorrloregaming.lockette;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class BlockUtil {
	public static byte faceList[] = { 5, 3, 4, 2 };
	public static byte attachList[] = { 1, 2, 0, 3 };
	static {
		if (BlockFace.NORTH.getModX() != -1) {
			faceList[0] = 3;
			faceList[1] = 4;
			faceList[2] = 2;
			faceList[3] = 5;
			attachList[0] = 1;
			attachList[0] = 2;
			attachList[0] = 0;
			attachList[0] = 3;
		}
	}
	public static final Material[] materialList = { Material.CHEST, Material.TRAPPED_CHEST, Material.DISPENSER, Material.DROPPER, Material.FURNACE, Material.BREWING_STAND, Material.LEGACY_TRAP_DOOR, Material.LEGACY_WOODEN_DOOR, Material.IRON_DOOR, Material.LEGACY_FENCE_GATE, Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE, Material.BIRCH_DOOR, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_DOOR, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_DOOR, Material.SPRUCE_FENCE_GATE, Material.LEGACY_WOODEN_DOOR, Material.IRON_TRAPDOOR };
	public static final Material[] materialListTrapDoors = { Material.LEGACY_TRAP_DOOR, Material.IRON_TRAPDOOR };
	public static final Material[] materialListNonDoors = { Material.CHEST, Material.TRAPPED_CHEST, Material.DISPENSER, Material.DROPPER, Material.FURNACE, Material.BREWING_STAND };
	public static final Material[] materialListTools = { Material.DISPENSER, Material.DROPPER, Material.FURNACE, Material.BREWING_STAND };
	public static final Material[] materialListChests = { Material.CHEST, Material.TRAPPED_CHEST };
	public static final Material[] materialListFurnaces = { Material.FURNACE };
	public static final Material[] materialListDoors = { Material.LEGACY_WOODEN_DOOR, Material.IRON_DOOR, Material.LEGACY_FENCE_GATE, Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE, Material.BIRCH_DOOR, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_DOOR, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_DOOR, Material.SPRUCE_FENCE_GATE, Material.LEGACY_WOODEN_DOOR };
	public static final Material[] materialListJustDoors = { Material.LEGACY_WOODEN_DOOR, Material.IRON_DOOR, Material.ACACIA_DOOR, Material.BIRCH_DOOR, Material.DARK_OAK_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.LEGACY_WOODEN_DOOR };
	public static final Material[] materialListWoodenDoors = { Material.LEGACY_TRAP_DOOR, Material.LEGACY_WOODEN_DOOR, Material.LEGACY_FENCE_GATE, Material.ACACIA_DOOR, Material.ACACIA_FENCE_GATE, Material.BIRCH_DOOR, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_DOOR, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_DOOR, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_DOOR, Material.SPRUCE_FENCE_GATE, Material.LEGACY_WOODEN_DOOR };
	public static final Material[] materialListGates = { Material.LEGACY_FENCE_GATE, Material.ACACIA_FENCE_GATE, Material.BIRCH_FENCE_GATE, Material.DARK_OAK_FENCE_GATE, Material.JUNGLE_FENCE_GATE, Material.SPRUCE_FENCE_GATE, };
	public static final Material[] materialListBad = { Material.TORCH, Material.SIGN, Material.LEGACY_WOODEN_DOOR, Material.LADDER, Material.WALL_SIGN, Material.IRON_DOOR, Material.LEGACY_REDSTONE_TORCH_OFF, Material.LEGACY_REDSTONE_TORCH_ON, Material.LEGACY_TRAP_DOOR };

	public static boolean isInList(Material target, Material[] list) {
		if (list == null)
			return false;
		for (int x = 0; x < list.length; x++)
			if (target == list[x])
				return true;
		return false;
	}
}
