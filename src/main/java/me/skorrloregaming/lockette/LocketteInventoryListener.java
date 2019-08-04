package me.skorrloregaming.lockette;

import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

import me.skorrloregaming.*;

public class LocketteInventoryListener implements Listener {
	public LocketteInventoryListener(Lockette instance) {
	}

	protected void registerEvents() {
		PluginManager pm = ServerGet.get().getPlugin().getServer().getPluginManager();
		pm.registerEvents(this, ServerGet.get().getPlugin());
	}

	private boolean isProtected(Inventory inv, boolean allowEveryone) {
		if (!Lockette.blockHopper)
			return false;
		InventoryHolder holder = inv.getHolder();
		if (holder instanceof DoubleChest) {
			holder = ((DoubleChest) holder).getLeftSide();
		}
		if (holder instanceof BlockState) {
			Block block = ((BlockState) holder).getBlock();
			Material type = block.getType();
			if (BlockUtil.isInList(type, BlockUtil.materialListNonDoors) || Lockette.isInList(type, Lockette.customBlockList)) {
				return (allowEveryone && Lockette.isEveryone(block)) ? false : Lockette.isProtected(block);
			}
		}
		return false;
	}

	private boolean passThrough(Inventory src, Inventory dest, Inventory me) {
		if (!Lockette.blockHopper)
			return true;
		UUID srcOwner = null;
		UUID destOwner = null;
		UUID meOwner = null;
		if (src != null) {
			InventoryHolder holder = src.getHolder();
			if (holder instanceof DoubleChest) {
				holder = ((DoubleChest) holder).getLeftSide();
			}
			if (holder instanceof BlockState) {
				Block block = ((BlockState) holder).getBlock();
				srcOwner = Lockette.getProtectedOwnerUUID(block);
				if (Lockette.isEveryone(block))
					srcOwner = null;
			}
		}
		if (dest != null) {
			InventoryHolder holder = dest.getHolder();
			if (holder instanceof DoubleChest) {
				holder = ((DoubleChest) holder).getLeftSide();
			}
			if (holder instanceof BlockState) {
				Block block = ((BlockState) holder).getBlock();
				destOwner = Lockette.getProtectedOwnerUUID(block);
				if (Lockette.isEveryone(block))
					destOwner = null;
			}
		}
		if (me != null) {
			InventoryHolder holder = me.getHolder();
			if (holder instanceof DoubleChest) {
				holder = ((DoubleChest) holder).getLeftSide();
			}
			if (holder instanceof BlockState) {
				Block block = ((BlockState) holder).getBlock();
				meOwner = Lockette.getProtectedOwnerUUID(block);
				if (Lockette.isEveryone(block))
					meOwner = null;
			}
		}
		if (((srcOwner == meOwner) && (meOwner == destOwner)) || ((srcOwner == meOwner) && (destOwner == null)) || ((srcOwner == null) && (meOwner == destOwner)) || ((srcOwner == null) && (destOwner == null)))
			return true;
		return false;
	}

	@EventHandler
	public void onInventoryItemMove(InventoryMoveItemEvent event) {
		Inventory src = event.getSource();
		Inventory dest = event.getDestination();
		Inventory me = event.getInitiator();
		if (passThrough(src, dest, me)) {
			return;
		}
		if (isProtected(event.getSource(), false) || isProtected(event.getDestination(), true)) {
			event.setCancelled(true);
		}
	}
}
