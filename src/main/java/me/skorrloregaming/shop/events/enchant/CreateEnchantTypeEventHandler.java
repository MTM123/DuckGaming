package me.skorrloregaming.shop.events.enchant;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.shop.LaShoppe;
import me.skorrloregaming.shop.LaShoppeFrame;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

public class CreateEnchantTypeEventHandler implements AnvilGUI.AnvilClickEventHandler {

    private LaShoppe shoppe;

    public CreateEnchantTypeEventHandler(LaShoppe shoppe) {
        this.shoppe = shoppe;
    }

    @Override
    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
        String enchantmentName = event.getName().toUpperCase().replace(" ", "_");
        Enchantment enchantment;
        try {
            enchantment = Enchantment.getByName(Link$.unformatEnchantment(enchantmentName));
        } catch (Exception ex) {
            event.getPlayer().sendMessage("Sorry, that's not a valid enchant name.");
            return;
        }
        final Enchantment fEnchantment = enchantment;
        Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> new AnvilGUI(event.getPlayer(), Server.getInstance().getShoppe().getInventoryName(LaShoppeFrame.CREATE_ENCHANT), new CreateEnchantPriceEventHandler(shoppe, fEnchantment))
                .setInputName("Enter price")
                .open(), 1L);
    }
}
