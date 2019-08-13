package me.skorrloregaming;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class CraftExplosion {
    private Location location;
    private float force;
    private Block[] blocks = null;
    private boolean strong;
    private HashMap<Block, Integer> durabilitySet = new HashMap<>();

    public CraftExplosion(Location location, float force, boolean strong) {
        this.setLocation(location);
        this.setForce(force);
        this.setStrong(strong);
        update();
    }

    public Block getCenterBlock() {
        return location.getWorld().getBlockAt(location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getRadius() {
        return (int) (force * 10);
    }

    public float getForce() {
        return force;
    }

    public void setForce(float force) {
        this.force = force;
    }

    public Block[] getBlocks() {
        return blocks;
    }

    public void update() {
        ArrayList<Block> blks = new ArrayList<>();
        HashMap<Integer, Integer> radiusSet = new HashMap<>();
        durabilitySet = new HashMap<>();
        for (int i = 0; i < getRadius() + 1; i++) {
            radiusSet.put(i, 0);
        }
        for (int x = -getRadius(); x < getRadius(); x++) {
            for (int y = -getRadius(); y < getRadius(); y++) {
                for (int z = -getRadius(); z < getRadius(); z++) {
                    double distanceSquared = Math.sqrt((x * x) + (y * y) + (z * z));
                    if (distanceSquared <= getRadius()) {
                        Block blk = location.getWorld().getBlockAt(location.clone().add(x, y, z));
                        int value = radiusSet.get((int) Math.round(distanceSquared)) + getBlockDurability(blk);
                        radiusSet.put((int) Math.round(distanceSquared), value);
                        for (int i = (int) distanceSquared; i > 0; i--) {
                            int currentDurability = 0;
                            if (durabilitySet.containsKey(blk))
                                currentDurability = durabilitySet.get(blk);
                            durabilitySet.put(blk, currentDurability + radiusSet.get(i));
                        }
                        if (!$.isWithinSystemClaimedLand(location)) {
                            blks.add(blk);
                        }
                    }
                }
            }
        }
        radiusSet.clear();
        blocks = blks.toArray(new Block[0]);
    }

    public void explodeNaturally() {
        Material ctr = getCenterBlock().getType();
        if (ctr != Material.WATER && ctr != Material.LAVA) {
            for (Block block : blocks) {
                if (isBlockDestructable(block)) {
                    if (block.getType() == Material.GRAVEL) {
                        block.setType(Material.AIR);
                        block.getState().update();
                        block.getWorld().dropItemNaturally(block.getLocation(), Link$.createMaterial(Material.FLINT));
                    } else {
                        if (!isDropsDestructable(block)) {
                            for (ItemStack drop : block.getDrops()) {
                                if (drop != null && drop.getType() != Material.AIR)
                                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
                            }
                        }
                        block.breakNaturally(Link$.createMaterial(Material.AIR));
                        block.getState().update();
                        LinkServer.getInstance().getAntiCheat().onBlockBreak(block, null);
                    }
                }
            }
        }
    }

    public void explode() {
        for (Block block : blocks) {
            block.breakNaturally(Link$.createMaterial(Material.AIR));
            block.getState().update();
            LinkServer.getInstance().getAntiCheat().onBlockBreak(block, null);
        }
    }

    private int getBlockDurability(Block block) {
        Material blockType = block.getType();
        if (blockType == Material.IRON_ORE)
            return 1;
        if (blockType == Material.GOLD_ORE)
            return 1;
        if (blockType == Material.DIAMOND_ORE)
            return 2;
        if (blockType == Material.EMERALD_ORE)
            return 2;
        if (blockType == Material.REDSTONE_ORE)
            return 2;
        if (blockType == Material.LAPIS_ORE)
            return 1;
        if (strong && blockType == Material.END_STONE)
            return 2;
        if (strong && blockType == Material.END_STONE_BRICKS)
            return 2;
        if (strong && blockType == Material.OBSIDIAN)
            return 3;
        return 0;
    }

    private boolean isBlockDestructable(Block block) {
        Material blockType = block.getType();
        if (!durabilitySet.containsKey(block))
            return false;
        int blockDurability = durabilitySet.get(block);
        if (blockDurability > 1) {
            Random ran = new Random();
            if (ran.nextInt(blockDurability) != 0)
                return false;
        }
        if (blockType == Material.LAVA)
            return false;
        if (blockType == Material.WATER)
            return false;
        if (blockType == Material.COMMAND_BLOCK)
            return false;
        if (blockType == Material.BEDROCK)
            return false;
        if (!strong && blockType == Material.OBSIDIAN)
            return false;
        if (blockType == Material.END_PORTAL)
            return false;
        if (blockType == Material.END_PORTAL_FRAME)
            return false;
        if (blockType == Material.NETHER_PORTAL)
            return false;
        if (blockType == Material.AIR)
            return false;
        if (!strong && blockType == Material.END_STONE)
            return false;
        if (!strong && blockType == Material.END_STONE_BRICKS)
            return false;
        return true;
    }

    private boolean isDropsDestructable(Block block) {
        Material blockType = block.getType();
        if (new Random().nextBoolean())
            return true;
        if (blockType == Material.IRON_ORE)
            return true;
        if (blockType == Material.GOLD_ORE)
            return true;
        if (blockType == Material.DIAMOND_ORE)
            return true;
        if (blockType == Material.EMERALD_ORE)
            return true;
        if (blockType == Material.REDSTONE_ORE)
            return true;
        if (blockType == Material.LAPIS_ORE)
            return true;
        if (blockType == Material.SPAWNER)
            return true;
        if (blockType == Material.COMMAND_BLOCK)
            return true;
        if (blockType == Material.OBSIDIAN)
            return true;
        if (blockType == Material.BEDROCK)
            return true;
        if (blockType == Material.END_PORTAL)
            return true;
        if (blockType == Material.END_PORTAL_FRAME)
            return true;
        if (blockType == Material.NETHER_PORTAL)
            return true;
        if (blockType == Material.AIR)
            return true;
        return false;
    }

    public boolean isStrong() {
        return strong;
    }

    public void setStrong(boolean strong) {
        this.strong = strong;
    }
}
