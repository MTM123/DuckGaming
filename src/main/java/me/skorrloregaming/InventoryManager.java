package me.skorrloregaming;

import me.skorrloregaming.impl.InventoryType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InventoryManager {

	private ConcurrentMap<UUID, Map.Entry<InventoryType, Map.Entry<Inventory, Object>>> inventories = new ConcurrentHashMap<>();

	public ConcurrentMap<UUID, Map.Entry<InventoryType, Map.Entry<Inventory, Object>>> getInventories() {
		return inventories;
	}

	public InventoryType getInventoryType(Player player) {
		if (inventories.containsKey(player.getUniqueId()))
			return inventories.get(player.getUniqueId()).getKey();
		return InventoryType.NULL;
	}

	public Inventory getInventory(Player player) {
		if (inventories.containsKey(player.getUniqueId()))
			return inventories.get(player.getUniqueId()).getValue().getKey();
		return null;
	}

	public Object getInventoryData(Player player) {
		if (inventories.containsKey(player.getUniqueId()))
			return inventories.get(player.getUniqueId()).getValue().getValue();
		return null;
	}

	public void doCloseInventory(Player player) {
		if (inventories.containsKey(player.getUniqueId()))
			inventories.remove(player.getUniqueId());
	}

}
