package me.skorrloregaming.factions.shop;

import me.skorrloregaming.$;
import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.factions.shop.events.CreateItemTypeEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;

public class LaShoppe {

	public void createItem(Material material, int price, int amount, int data) {
		int index = 0;
		while (true) {
			index++;
			if (!Server.getFactionsShoppeConfig().getData().contains(index + ""))
				break;
		}
		Server.getFactionsShoppeConfig().getData().set(index + ".material", material.toString());
		Server.getFactionsShoppeConfig().getData().set(index + ".price", price);
		Server.getFactionsShoppeConfig().getData().set(index + ".amount", amount);
		Server.getFactionsShoppeConfig().getData().set(index + ".data", data);
		Server.getFactionsShoppeConfig().saveData();
	}

	public LaShoppeItem retrieveItem(int index) {
		String materialString = Server.getFactionsShoppeConfig().getData().getString(index + ".material");
		Material material = Material.getMaterial(materialString);
		int price = Server.getFactionsShoppeConfig().getData().getInt(index + ".price");
		int amount = Server.getFactionsShoppeConfig().getData().getInt(index + ".amount");
		int data = Server.getFactionsShoppeConfig().getData().getInt(index + ".data");
		return new LaShoppeItem(material, price, amount, data, index);
	}

	public void createInventory(Player player, LaShoppeFrame frame, int page, boolean removeMode) {
		switch (frame) {
			case HOME:
				int startIndex = -1;
				Inventory inventory = null;
				if (player.isOp()) {
					inventory = Bukkit.createInventory(null, 36, "La Shoppe, page " + page);
					ItemStack removeItemModeItem = $.createMaterial(Material.ROSE_RED, "Remove items from shop");
					if (removeMode) {
						if (CraftGo.Player.getProtocolVersion(player) > 314) {
							removeItemModeItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
						} else {
							removeItemModeItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
						}
					}
					inventory.setItem(0, removeItemModeItem);
					inventory.setItem(8, $.createMaterial(Material.CACTUS_GREEN, "Add new shop item"));
					startIndex = 7;
				} else {
					inventory = Bukkit.createInventory(null, 27, "La Shoppe, page " + page);
				}
				ItemStack viewPrevious = $.createMaterial(Material.EMERALD, "View previous page");
				ItemStack viewFollowing = $.createMaterial(Material.EMERALD, "View following page");
				if (player.isOp()) {
					inventory.setItem(18, viewPrevious);
					inventory.setItem(26, viewFollowing);
				} else {

					inventory.setItem(9, viewPrevious);
					inventory.setItem(17, viewFollowing);
				}
				for (int i = 0; i < 26; i++) {
					int horizontal = i % 7;
					int line = (int) (Math.floor(i / 7) + 1);
					int slot = (horizontal + 1) + (9 * (line - 1));
					if (startIndex + slot < inventory.getSize()) {
						int index = ((page - 1) * 26) + i;
						if (Server.getFactionsShoppeConfig().getData().contains(index + ""))
							inventory.setItem(startIndex + slot, retrieveItem(index).toItemStack());
					}
				}
				player.openInventory(inventory);
				break;
			case CREATE_ITEM:
				try {
					new AnvilGUI(player, new CreateItemTypeEventHandler(this))
							.setInputName("Enter item type")
							.open();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
		}
	}

}
