package me.skorrloregaming.shop.events.item;

import me.skorrloregaming.AnvilGUI;
import me.skorrloregaming.Server;
import me.skorrloregaming.shop.LaShoppe;
import me.skorrloregaming.shop.LaShoppeFrame;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class CreateItemTypeEventHandler implements AnvilGUI.AnvilClickEventHandler {

    private LaShoppe shoppe;

    public CreateItemTypeEventHandler(LaShoppe shoppe) {
        this.shoppe = shoppe;
    }

    @Override
    public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {
        String materialName = event.getName().toUpperCase().replace(" ", "_");
        Material material;
        try {
            material = Material.getMaterial(materialName);
        } catch (Exception ex) {
            try {
                material = Material.getMaterial(materialName, true);
            } catch (Exception ex2) {
                event.getPlayer().sendMessage("Sorry, that's not a valid material name.");
                return;
            }
        }
        final Material fMaterial = material;
        Bukkit.getScheduler().runTaskLater(Server.getInstance().getPlugin(), () -> new AnvilGUI(event.getPlayer(), Server.getInstance().getShoppe().getInventoryName(LaShoppeFrame.CREATE_ITEM), new CreateItemPriceEventHandler(shoppe, fMaterial))
                .setInputName("Enter price")
                .open(), 1L);
    }
}
