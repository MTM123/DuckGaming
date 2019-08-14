package me.skorrloregaming.shop.events.buy;

import me.skorrloregaming.*;
import me.skorrloregaming.shop.LaShoppe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class BuyItemAmountEventHandler implements AnvilGUI.AnvilClickEventHandler {

    private LaShoppe shoppe;

    private Player player;

    private ItemStack purchaseItem;

    private int data;

    private double price;

    public BuyItemAmountEventHandler(LaShoppe shoppe, Player player, ItemStack purchaseItem, int data, double price) {
        this.shoppe = shoppe;
        this.player = player;
        this.purchaseItem = purchaseItem;
        this.data = data;
        this.price = price;
    }

    @Override
    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
        final int amount;
        try {
            amount = Integer.parseInt(event.getName());
        } catch (Exception ex) {
            event.getPlayer().sendMessage("Sorry, that's not a valid item amount.");
            return;
        }
        DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
        String materialName = Link$.formatMaterial(purchaseItem.getType());
        String subDomain = $.getMinigameDomain(player);
        double cash = EconManager.retrieveCash(player, subDomain);
        String tag = $.getMinigameTag(player);
        price *= amount;
        purchaseItem.setAmount(amount);
        if (purchaseItem.getType() == Material.SPAWNER)
            purchaseItem = CraftGo.MobSpawner.newSpawnerItem(CraftGo.MobSpawner.convertEntityIdToEntityType(data), amount);
        if (cash >= price) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(tag + ChatColor.RED + "Inventory full. " + ChatColor.GRAY + "Empty some slots then try again.");
                return;
            }
            EconManager.withdrawCash(player, price, subDomain);
            player.getInventory().addItem(purchaseItem);
            player.updateInventory();
            player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + materialName + " x" + amount + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
            return;
        }
        player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
    }
}
