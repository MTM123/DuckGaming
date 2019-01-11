package me.skorrloregaming.factions.shop;

import me.skorrloregaming.$;
import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.factions.shop.events.CreateItemTypeEventHandler;
import me.skorrloregaming.factions.shop.events.LaShoppeEventHandler;
import me.skorrloregaming.impl.EnchantInfo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

public class LaShoppe {

	private LaShoppeEventHandler eventHandler;

	public LaShoppe() {
		eventHandler = new LaShoppeEventHandler(this);
		Bukkit.getServer().getPluginManager().registerEvents(eventHandler, Server.getPlugin());
	}

	public void createItem(Material material, int price, int amount) {
		int index = 0;
		while (true) {
			index++;
			if (!Server.getFactionsShoppeConfig().getData().contains(index + ""))
				break;
		}
		Server.getFactionsShoppeConfig().getData().set(index + ".material", material.toString());
		Server.getFactionsShoppeConfig().getData().set(index + ".price", price);
		Server.getFactionsShoppeConfig().getData().set(index + ".amount", amount);
		Server.getFactionsShoppeConfig().saveData();
	}

	public LaShoppeItem retrieveItem(int index) {
		String materialString = Server.getFactionsShoppeConfig().getData().getString(index + ".material");
		Material material = Material.getMaterial(materialString);
		int price = Server.getFactionsShoppeConfig().getData().getInt(index + ".price");
		int amount = Server.getFactionsShoppeConfig().getData().getInt(index + ".amount");
		return new LaShoppeItem(material, price, amount);
	}

	public void createInventory(Player player, LaShoppeFrame frame, int page, boolean removeMode) {
		switch (frame) {
			case HOME:
				int startIndex = 0;
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
				inventory.setItem(startIndex + 9, viewPrevious);
				inventory.setItem(startIndex + 17, viewFollowing);
				for (int i = 0; i < 27; i++) {
					int horizontal = i % 7;
					int line = (int) (Math.floor(i / 7) + 1);
					int slot = (horizontal + 1) + (9 * (line - 1));
					if (slot < inventory.getSize()) {
						int index = ((page - 1) * 27) + i;
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
