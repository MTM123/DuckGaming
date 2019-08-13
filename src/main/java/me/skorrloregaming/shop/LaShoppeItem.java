package me.skorrloregaming.shop;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LaShoppeItem {

    private Material material;

    private int price;

    private int amount;

    private int index;

    private int data;

    public LaShoppeItem(Material material, int price, int amount, int data, int index) {
        this.material = material;
        this.price = price;
        this.amount = amount;
        this.data = data;
        this.index = index;
    }

    public Material getMaterial() {
        return material;
    }

    public int getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public int getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public ItemStack toItemStack() {
        ItemStack itemStack = null;
        if (material == Material.SPAWNER) {
            String entityName = CraftGo.MobSpawner.convertEntityIdToEntityType(data).toString().toLowerCase();
            String displayName = ChatColor.RESET + Link$.capitalizeAll(WordUtils.capitalize(entityName) + " Spawner", "_");
            itemStack = Link$.createMaterial(material, displayName);
        } else {
            itemStack = Link$.createMaterial(material);
        }
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.RESET + "Index: " + index);
        lore.add(ChatColor.RESET + "Price: $" + price);
        lore.add(ChatColor.RESET + "Amount: " + amount + "x");
        lore.add(ChatColor.RESET + "Data: " + data);
        lore.add("");
        lore.add(ChatColor.RESET + "Use LEFT click to SELL this item");
        lore.add(ChatColor.RESET + "Use RIGHT click to BUY this item");
        return Link$.addLore(itemStack, lore.toArray(new String[0]));
    }

}
