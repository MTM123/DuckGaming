package me.skorrloregaming.shop.events.item;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.shop.LaShoppe;
import me.skorrloregaming.shop.LaShoppeFrame;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class CreateItemAmountEventHandler implements AnvilGUI.AnvilClickEventHandler {

    private LaShoppe shoppe;

    private Material material;

    private int price;

    public CreateItemAmountEventHandler(LaShoppe shoppe, Material material, int price) {
        this.shoppe = shoppe;
        this.material = material;
        this.price = price;
    }

    @Override
    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
        String amountString = event.getName().replace("x", "");
        final int amount;
        try {
            amount = Integer.parseInt(amountString);
        } catch (Exception ex) {
            event.getPlayer().sendMessage("Sorry, that's not a valid item amount.");
            return;
        }
        Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> new AnvilGUI(event.getPlayer(), Server.getInstance().getShoppe().getInventoryName(LaShoppeFrame.CREATE_ITEM), new CreateItemDataEventHandler(shoppe, material, price, amount))
                .setInputName("Enter data")
                .open(), 1L);
    }
}
