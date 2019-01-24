/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

import me.skorrloregaming.AntiCheat;
import me.skorrloregaming.Go;
import me.skorrloregaming.Server;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class Explosion {
	private Location location;
	private float force;
	private Block[] blocks = null;
	private boolean strong;
	private HashMap<Block, Integer> durabilitySet = new HashMap();

	public Explosion(Location location, float force, boolean strong) {
		this.setLocation(location);
		this.setForce(force);
		this.setStrong(strong);
		this.update();
	}

	public Block getCenterBlock() {
		return this.location.getWorld().getBlockAt(this.location);
	}

	public Location getLocation() {
		return this.location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getRadius() {
		return (int) (this.force * 10.0f);
	}

	public float getForce() {
		return this.force;
	}

	public void setForce(float force) {
		this.force = force;
	}

	public Block[] getBlocks() {
		return this.blocks;
	}

	public void update() {
		ArrayList<Block> blks = new ArrayList<Block>();
		HashMap<Integer, Integer> radiusSet = new HashMap<Integer, Integer>();
		this.durabilitySet = new HashMap();
		int i = 0;
		while (i < this.getRadius() + 1) {
			radiusSet.put(i, 0);
			++i;
		}
		int x = -this.getRadius();
		while (x < this.getRadius()) {
			int y = -this.getRadius();
			while (y < this.getRadius()) {
				int z = -this.getRadius();
				while (z < this.getRadius()) {
					double distanceSquared = Math.sqrt(x * x + y * y + z * z);
					if (distanceSquared <= (double) this.getRadius()) {
						Block blk = this.location.getWorld().getBlockAt(this.location.clone().add((double) x, (double) y, (double) z));
						int value = (Integer) radiusSet.get((int) Math.round(distanceSquared)) + this.getBlockDurability(blk);
						radiusSet.put((int) Math.round(distanceSquared), value);
						int i2 = (int) distanceSquared;
						while (i2 > 0) {
							int currentDurability = 0;
							if (this.durabilitySet.containsKey( blk)) {
								currentDurability = this.durabilitySet.get( blk);
							}
							this.durabilitySet.put(blk, currentDurability + (Integer) radiusSet.get(i2));
							--i2;
						}
						if (!Go.isWithinSystemClaimedLand(this.location)) {
							blks.add(blk);
						}
					}
					++z;
				}
				++y;
			}
			++x;
		}
		radiusSet.clear();
		this.blocks = blks.toArray(new Block[0]);
	}

	public void explodeNaturally() {
		Material ctr = this.getCenterBlock().getType();
		if (ctr != Material.WATER && ctr != Material.LAVA && ctr != Material.STATIONARY_WATER && ctr != Material.STATIONARY_LAVA) {
			Block[] arrblock = this.blocks;
			int n = arrblock.length;
			int n2 = 0;
			while (n2 < n) {
				Block block = arrblock[n2];
				if (this.isBlockDestructable(block)) {
					if (block.getType() == Material.GRAVEL) {
						block.setType(Material.AIR);
						block.getState().update();
						block.getWorld().dropItemNaturally(block.getLocation(), Go.createMaterial(Material.FLINT));
					} else {
						if (!this.isDropsDestructable(block)) {
							for (ItemStack drop : block.getDrops()) {
								block.getWorld().dropItemNaturally(block.getLocation(), drop);
							}
						}
						block.breakNaturally(Go.createMaterial(Material.AIR));
						block.getState().update();
						Server.anticheat.onBlockBreak(block, null);
					}
				}
				++n2;
			}
		}
	}

	public void explode() {
		Block[] arrblock = this.blocks;
		int n = arrblock.length;
		int n2 = 0;
		while (n2 < n) {
			Block block = arrblock[n2];
			block.breakNaturally(Go.createMaterial(Material.AIR));
			block.getState().update();
			Server.anticheat.onBlockBreak(block, null);
			++n2;
		}
	}

	private int getBlockDurability(Block block) {
		Material blockType = block.getType();
		if (blockType == Material.IRON_ORE) {
			return 3;
		}
		if (blockType == Material.GOLD_ORE) {
			return 3;
		}
		if (blockType == Material.DIAMOND_ORE) {
			return 3;
		}
		if (blockType == Material.EMERALD_ORE) {
			return 3;
		}
		if (blockType == Material.REDSTONE_ORE) {
			return 3;
		}
		if (blockType == Material.LAPIS_ORE) {
			return 3;
		}
		if (this.strong && blockType == Material.ENDER_STONE) {
			return 4;
		}
		if (this.strong && blockType == Material.END_BRICKS) {
			return 4;
		}
		if (this.strong && blockType == Material.OBSIDIAN) {
			return 8;
		}
		return 0;
	}

	private boolean isBlockDestructable(Block block) {
		Random ran;
		Material blockType = block.getType();
		if (!this.durabilitySet.containsKey( block)) {
			return false;
		}
		int blockDurability = this.durabilitySet.get( block);
		if (blockDurability > 1 && (ran = new Random()).nextInt(blockDurability) != 0) {
			return false;
		}
		if (blockType == Material.LAVA || blockType == Material.STATIONARY_LAVA) {
			return false;
		}
		if (blockType == Material.WATER || blockType == Material.STATIONARY_WATER) {
			return false;
		}
		if (blockType == Material.COMMAND) {
			return false;
		}
		if (blockType == Material.BEDROCK) {
			return false;
		}
		if (!this.strong && blockType == Material.OBSIDIAN) {
			return false;
		}
		if (blockType == Material.ENDER_PORTAL) {
			return false;
		}
		if (blockType == Material.ENDER_PORTAL_FRAME) {
			return false;
		}
		if (blockType == Material.PORTAL) {
			return false;
		}
		if (blockType == Material.AIR) {
			return false;
		}
		if (!this.strong && blockType == Material.ENDER_STONE) {
			return false;
		}
		if (!this.strong && blockType == Material.END_BRICKS) {
			return false;
		}
		return true;
	}

	private boolean isDropsDestructable(Block block) {
		Material blockType = block.getType();
		if (new Random().nextBoolean()) {
			return true;
		}
		if (blockType == Material.IRON_ORE) {
			return true;
		}
		if (blockType == Material.GOLD_ORE) {
			return true;
		}
		if (blockType == Material.DIAMOND_ORE) {
			return true;
		}
		if (blockType == Material.EMERALD_ORE) {
			return true;
		}
		if (blockType == Material.REDSTONE_ORE) {
			return true;
		}
		if (blockType == Material.LAPIS_ORE) {
			return true;
		}
		if (blockType == Material.MOB_SPAWNER) {
			return true;
		}
		if (blockType == Material.COMMAND) {
			return true;
		}
		if (blockType == Material.OBSIDIAN) {
			return true;
		}
		if (blockType == Material.BEDROCK) {
			return true;
		}
		if (blockType == Material.ENDER_PORTAL) {
			return true;
		}
		if (blockType == Material.ENDER_PORTAL_FRAME) {
			return true;
		}
		if (blockType == Material.PORTAL) {
			return true;
		}
		if (blockType == Material.AIR) {
			return true;
		}
		return false;
	}

	public boolean isStrong() {
		return this.strong;
	}

	public void setStrong(boolean strong) {
		this.strong = strong;
	}
}

