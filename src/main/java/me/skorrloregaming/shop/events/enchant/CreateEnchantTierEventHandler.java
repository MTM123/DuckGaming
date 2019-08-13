package me.skorrloregaming.shop.events.enchant;

import me.skorrloregaming.$;
import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.shop.LaShoppe;
import org.bukkit.enchantments.Enchantment;

public class CreateEnchantTierEventHandler implements AnvilGUI.AnvilClickEventHandler {

    private LaShoppe shoppe;

    private Enchantment enchantment;

    private int price;

    public CreateEnchantTierEventHandler(LaShoppe shoppe, Enchantment enchantment, int price) {
        this.shoppe = shoppe;
        this.enchantment = enchantment;
        this.price = price;
    }

    @Override
    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
        String tierString = event.getName();
        final int tier;
        try {
            tier = Integer.parseInt(tierString);
        } catch (Exception ex) {
            event.getPlayer().sendMessage("Sorry, that's not a valid enchant tier.");
            return;
        }
        event.getPlayer().sendMessage("Enchantment: " + enchantment.getName());
        event.getPlayer().sendMessage("Price: $" + price);
        event.getPlayer().sendMessage("Amount: " + tier);
        shoppe.createEnchant($.getCurrentMinigame(event.getPlayer()), enchantment, price, tier);
    }
}
