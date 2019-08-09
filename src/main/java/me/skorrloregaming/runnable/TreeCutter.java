package me.skorrloregaming.runnable;

import me.skorrloregaming.$;
import me.skorrloregaming.Directory;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TreeCutter extends BukkitRunnable {
	private Player player;
	private Block startBlock;
	private Location startBlockCenter;
	private int searchSquareSize = 25;
	private List<String> comparisonBlockArray = new ArrayList<String>();
	private List<String> comparisonBlockArrayLeaves = new ArrayList<String>();
	private List<Block> blocks = new ArrayList<Block>();
	private int indexed = 0;

	public TreeCutter(Player cutter, Block startBlock) {
		this.player = cutter;
		this.startBlock = startBlock;
		this.startBlockCenter = startBlock.getLocation().add(0.5, 0.5, 0.5);
	}

	public void runLoop(final Block b1, final int x1, final int z1) {
		for (int x2 = -2; x2 <= 2; ++x2) {
			for (int y = -2; y <= 2; ++y) {
				for (int z2 = -2; z2 <= 1; ++z2) {
					if (x2 != 0 || y != 0 || z2 != 0) {
						final Block b2 = b1.getRelative(x2, y, z2);
						final String s = b2.getX() + ":" + b2.getY() + ":" + b2.getZ();
						if ($.isBlockLeaves(b2) && !this.comparisonBlockArrayLeaves.contains(s)) {
							this.comparisonBlockArrayLeaves.add(s);
						}
						if ($.isBlockLog(b2)) {
							if (b2.getX() > x1 + this.searchSquareSize || b2.getX() < x1 - this.searchSquareSize || b2.getZ() > z1 + this.searchSquareSize) {
								break;
							}
							if (b2.getZ() < z1 - this.searchSquareSize) {
								break;
							}
							if (!this.comparisonBlockArray.contains(s)) {
								this.comparisonBlockArray.add(s);
								this.blocks.add(b2);
								this.runLoop(b2, x1, z1);
							}
						}
					}
				}
			}
		}
	}

	public void run() {
		this.blocks.add(this.startBlock);
		this.runLoop(this.startBlock, this.startBlock.getX(), this.startBlock.getZ());
		if (this.isTree()) {
			this.cutDownTree();
		} else {
			new BukkitRunnable() {
				public void run() {
					final Location center = startBlock.getLocation().add(0.5, 0.5, 0.5);
					for (final ItemStack stack : startBlock.getDrops()) {
						startBlock.getWorld().dropItem(center, stack);
					}
					startBlock.getWorld().playEffect(center, Effect.STEP_SOUND, (Object) startBlock.getType());
					startBlock.setType(Material.AIR);
				}
			}.runTask(Server.getInstance().getPlugin());
		}
	}

	private boolean isTree() {
		return this.comparisonBlockArrayLeaves.size() * 1.0 / (this.blocks.size() * 1.0) > 0.3;
	}

	public List<Block> getBlocks() {
		return this.blocks;
	}

	public void cutDownTree() {
		this.blocks = this.blocks.stream().sorted((b, b2) -> b2.getY() - b.getY()).collect(Collectors.toList());
		if (!Server.getInstance().getCurrentFellers().contains(this.player.getUniqueId())) {
			Server.getInstance().getCurrentFellers().add(this.player.getUniqueId());
		}
		if ((this.player.getInventory().getItemInMainHand() == null || this.player.getInventory().getItemInMainHand().getType() == Material.AIR) && !this.updateItemInHand()) {
			if (Server.getInstance().getCurrentFellers().contains(this.player.getUniqueId())) {
				Server.getInstance().getCurrentFellers().remove(this.player.getUniqueId());
			}
			return;
		}
		Server.getInstance().getBukkitTasks().add(new BukkitRunnable() {
			public void run() {
				if (!player.isOnline()) {
					this.cancel();
					if (Server.getInstance().getCurrentFellers().contains(player.getUniqueId())) {
						Server.getInstance().getCurrentFellers().remove(player.getUniqueId());
					}
					return;
				}
				final ItemStack item = player.getInventory().getItemInMainHand();
				Material type = Material.AIR;
				if (!(item == null))
					type = item.getType();
				if (!Directory.axes.contains(type)) {
					this.cancel();
					if (Server.getInstance().getCurrentFellers().contains(player.getUniqueId())) {
						Server.getInstance().getCurrentFellers().remove(player.getUniqueId());
					}
					return;
				}
				if (blocks.size() < indexed - 2) {
					this.cancel();
					return;
				}
				final Block block = blocks.get(indexed++);
				final PlayerAnimationEvent animationEvent = new PlayerAnimationEvent(player);
				Bukkit.getPluginManager().callEvent((Event) animationEvent);
				final BlockBreakEvent event = new BlockBreakEvent(block, player);
				Bukkit.getPluginManager().callEvent((Event) event);
				if (!event.isCancelled()) {
					final Location center = block.getLocation().add(0.5, 0.5, 0.5);
					startBlock.getWorld().playEffect(center, Effect.STEP_SOUND, block.getType());
					for (final ItemStack drop : block.getDrops()) {
						startBlock.getWorld().dropItem(startBlockCenter, drop);
					}
					item.setDurability((short) (item.getDurability() + 1));
					if (item.getType().getMaxDurability() == item.getDurability()) {
						player.getInventory().setItemInMainHand(null);
					}
					block.setType(Material.AIR);
					block.getState().update(true);
				}
				if (blocks.size() <= indexed) {
					this.cancel();
					if (Server.getInstance().getCurrentFellers().contains(player.getUniqueId())) {
						Server.getInstance().getCurrentFellers().remove(player.getUniqueId());
					}
				}
			}
		}.runTaskTimer(Server.getInstance().getPlugin(), 0L, 0L));
	}

	public boolean updateItemInHand() {
		final ItemStack item = this.player.getInventory().getItemInMainHand();
		if (item != null && item.getType() != Material.AIR) {
			return false;
		}
		for (int index = 0; index < 36; ++index) {
			final ItemStack stack = this.player.getInventory().getItem(index);
			if (stack != null && stack.getType() != Material.AIR) {
				this.player.getInventory().setItemInMainHand(stack);
				this.player.getInventory().setItem(index, null);
				return true;
			}
		}
		return false;
	}
}