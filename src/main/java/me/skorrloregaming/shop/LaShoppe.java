package me.skorrloregaming.shop;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.InventoryType;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.shop.events.enchant.CreateEnchantTypeEventHandler;
import me.skorrloregaming.shop.events.item.CreateItemTypeEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LaShoppe {

    public int getTotalItems(ServerMinigame minigame) {
        String prefix = minigame.toString().toLowerCase() + ".";
        if (Server.getInstance().getShoppeConfig().getData().contains(prefix + "items")) {
            return Server.getInstance().getShoppeConfig().getData().getConfigurationSection(prefix + "items").getKeys(false).size();
        } else {
            return 0;
        }
    }

    public void createItem(ServerMinigame minigame, Material material, int price, int amount, int data) {
        String prefix = minigame.toString().toLowerCase() + ".";
        int index = 0;
        do {
            index++;
        } while (Server.getInstance().getShoppeConfig().getData().contains(prefix + "items." + index));
        Server.getInstance().getShoppeConfig().getData().set(prefix + "items." + index + ".material", material.toString());
        Server.getInstance().getShoppeConfig().getData().set(prefix + "items." + index + ".price", price);
        Server.getInstance().getShoppeConfig().getData().set(prefix + "items." + index + ".amount", amount);
        Server.getInstance().getShoppeConfig().getData().set(prefix + "items." + index + ".data", data);
        Server.getInstance().getShoppeConfig().saveData();
    }

    public void createEnchant(ServerMinigame minigame, Enchantment enchantment, int price, int tier) {
        String prefix = minigame.toString().toLowerCase() + ".";
        int index = 0;
        do {
            index++;
        } while (Server.getInstance().getShoppeConfig().getData().contains(prefix + "enchant." + index));
        Server.getInstance().getShoppeConfig().getData().set(prefix + "enchant." + index + ".enchant", enchantment.getName());
        Server.getInstance().getShoppeConfig().getData().set(prefix + "enchant." + index + ".price", price);
        Server.getInstance().getShoppeConfig().getData().set(prefix + "enchant." + index + ".tier", tier);
        Server.getInstance().getShoppeConfig().saveData();
    }

    public LaShoppeItem retrieveItem(ServerMinigame minigame, int index) {
        String prefix = minigame.toString().toLowerCase() + ".";
        String materialString = Server.getInstance().getShoppeConfig().getData().getString(prefix + "items." + index + ".material");
        Material material = Material.getMaterial(materialString);
        int price = Server.getInstance().getShoppeConfig().getData().getInt(prefix + "items." + index + ".price");
        int amount = Server.getInstance().getShoppeConfig().getData().getInt(prefix + "items." + index + ".amount");
        int data = Server.getInstance().getShoppeConfig().getData().getInt(prefix + "items." + index + ".data");
        return new LaShoppeItem(material, price, amount, data, index);
    }

    public LaShoppeEnchant retrieveEnchant(ServerMinigame minigame, int index) {
        String prefix = minigame.toString().toLowerCase() + ".";
        String enchantString = Server.getInstance().getShoppeConfig().getData().getString(prefix + "enchant." + index + ".enchant");
        Enchantment enchantment = Enchantment.getByName(enchantString);
        int price = Server.getInstance().getShoppeConfig().getData().getInt(prefix + "enchant." + index + ".price");
        int tier = Server.getInstance().getShoppeConfig().getData().getInt(prefix + "enchant." + index + ".tier");
        return new LaShoppeEnchant(enchantment, price, tier, index);
    }

    public String getInventoryName(LaShoppeFrame frame) {
        switch (frame) {
            case CREATE_ITEM:
                return "Add item to shop";
            case CREATE_ENCHANT:
                return "Add enchant to shop";
            case BUY_ITEM:
                return "Buy item from shop";
            default:
                return "Repair & Name";
        }
    }

    public void createInventory(Player player, LaShoppeFrame frame, int page, boolean removeMode) {
        ServerMinigame minigame = $.getCurrentMinigame(player);
        String prefix = minigame.toString().toLowerCase() + ".";
        switch (frame) {
            case HOME:
                int startIndex = 0;
                Inventory inventory;
                if (player.isOp()) {
                    inventory = Bukkit.createInventory(new InventoryMenu(player, InventoryType.LA_SHOPPE, page), 45, "La Shoppe, page " + page);
                    ItemStack removeItemModeItem = Link$.createMaterial(Material.RED_DYE, "Remove items from shop");
                    if (removeMode) {
                        if (CraftGo.Player.getProtocolVersion(player) > 314) {
                            removeItemModeItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
                        } else {
                            removeItemModeItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                        }
                    }
                    inventory.setItem(0, removeItemModeItem);
                    inventory.setItem(7, Link$.createMaterial(Material.LAPIS_LAZULI, "Add new shop enchant"));
                    inventory.setItem(8, Link$.createMaterial(Material.GREEN_DYE, "Add new shop item"));
                    startIndex = 9;
                } else {
                    inventory = Bukkit.createInventory(new InventoryMenu(player, InventoryType.LA_SHOPPE, page), 36, "La Shoppe, page " + page);
                }
                ItemStack viewPrevious = Link$.createMaterial(Material.EMERALD, "View previous page");
                ItemStack viewFollowing = Link$.createMaterial(Material.EMERALD, "View following page");
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
                        int index = ((page - 1) * 21) + i + 1;
                        if (Server.getInstance().getShoppeConfig().getData().contains(prefix + "items." + index))
                            inventory.setItem(startIndex + slot, retrieveItem(minigame, index).toItemStack());
                        if (index > getTotalItems(minigame)) {
                            int enchantIndex = index - getTotalItems(minigame);
                            if (Server.getInstance().getShoppeConfig().getData().contains(prefix + "enchant." + enchantIndex))
                                inventory.setItem(startIndex + slot, retrieveEnchant(minigame, enchantIndex).toItemStack());
                        }
                    }
                }
                int slot = 1;
                for (int i = 27 + startIndex; i < 36; i++) {
                    inventory.setItem(i, Link$.createMaterial(Material.BLACK_STAINED_GLASS_PANE, "Select hotbar slot " + slot));
                    slot++;
                }
                player.openInventory(inventory);
                break;
            case CREATE_ITEM:
                new AnvilGUI(player, getInventoryName(frame), new CreateItemTypeEventHandler(this))
                        .setInputName("Enter type")
                        .open();
                break;
            case CREATE_ENCHANT:
                new AnvilGUI(player, getInventoryName(frame), new CreateEnchantTypeEventHandler(this))
                        .setInputName("Enter type")
                        .open();
                break;
            default:
                break;
        }
    }

}
