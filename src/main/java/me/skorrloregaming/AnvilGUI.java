package me.skorrloregaming;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class AnvilGUI {

    private static Class<?> BlockPosition;
    private static Class<?> PacketPlayOutOpenWindow;
    private static Class<?> ContainerAnvil;
    private static Class<?> ChatMessage;
    private static Class<?> EntityHuman;
    private static Class<?> ContainerAccess;
    private static Class<?> Containers;
    private static Method at;
    private Player player;
    private AnvilClickEventHandler handler;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();
    private Inventory inv;
    private Listener listener;

    private String inventoryName;

    public AnvilGUI(final Player player, String inventoryName, final AnvilClickEventHandler handler) {
        loadClasses();
        this.player = player;
        this.handler = handler;
        this.inventoryName = inventoryName;
        this.listener = new Listener() {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player) {
                    if (event.getInventory().equals(inv)) {
                        event.setCancelled(true);
                        Player player = (Player) event.getWhoClicked();
                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";
                        if (item != null) {
                            if (item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();
                                if (meta.hasDisplayName()) {
                                    name = meta.getDisplayName();
                                }
                            }
                        }
                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name, player);
                        handler.onAnvilClick(clickEvent);
                        if (clickEvent.getWillClose()) {
                            event.getWhoClicked().closeInventory();
                        }
                        if (clickEvent.getWillDestroy()) {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event) {
                if (event.getPlayer() instanceof Player) {
                    Inventory inv = event.getInventory();
                    player.setLevel(player.getLevel() - 1);
                    if (inv.equals(AnvilGUI.this.inv)) {
                        inv.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event) {
                if (event.getPlayer().equals(getPlayer())) {
                    player.setLevel(player.getLevel() - 1);
                    destroy();
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, Server.getInstance().getPlugin());
    }

    private void loadClasses() {
        BlockPosition = CraftGo.Reflection.getNMSClass("BlockPosition");
        PacketPlayOutOpenWindow = CraftGo.Reflection.getNMSClass("PacketPlayOutOpenWindow");
        ContainerAnvil = CraftGo.Reflection.getNMSClass("ContainerAnvil");
        EntityHuman = CraftGo.Reflection.getNMSClass("EntityHuman");
        ChatMessage = CraftGo.Reflection.getNMSClass("ChatMessage");
        ContainerAccess = CraftGo.Reflection.getNMSClass("ContainerAccess");
        Containers = CraftGo.Reflection.getNMSClass("Containers");
        at = CraftGo.Reflection.getMethod(ContainerAccess, "at", CraftGo.Reflection.getNMSClass("World"), BlockPosition);
    }

    public Player getPlayer() {
        return player;
    }

    public AnvilGUI setInputName(String name) {
        return setSlot(AnvilSlot.INPUT_LEFT, Link$.createMaterial(Material.PAPER, name));
    }

    public AnvilGUI setSlot(AnvilSlot slot, ItemStack item) {
        items.put(slot, item);
        return this;
    }

    public void open() {
        player.setLevel(player.getLevel() + 1);
        try {
            Object p = CraftGo.Reflection.getHandle(player);
            int c = (int) CraftGo.Reflection.invokeMethod("nextContainerCounter", p);
            Object ep = CraftGo.Reflection.getHandle(p);
            Object world = CraftGo.Reflection.getHandle(player.getWorld());
            Object blockPosition = BlockPosition.getConstructor(int.class, int.class, int.class).newInstance(0, 0, 0);
            Object container = ContainerAnvil.getConstructor(int.class, CraftGo.Reflection.getNMSClass("PlayerInventory"), CraftGo.Reflection.getNMSClass("ContainerAccess")).newInstance(c, CraftGo.Reflection.getPlayerField(player, "inventory"), at.invoke(null, world, blockPosition));
            CraftGo.Reflection.getField(CraftGo.Reflection.getNMSClass("Container"), "checkReachable").set(container, false);
            Object bukkitView = CraftGo.Reflection.invokeMethod("getBukkitView", container);
            inv = (Inventory) CraftGo.Reflection.invokeMethod("getTopInventory", bukkitView);
            for (AnvilSlot slot : items.keySet()) {
                inv.setItem(slot.getSlot(), items.get(slot));
            }
            Constructor<?> chatMessageConstructor = ChatMessage.getConstructor(String.class, Object[].class);
            Object playerConnection = CraftGo.Reflection.getPlayerField(player, "playerConnection");
            Object ANVIL = CraftGo.Reflection.getField(CraftGo.Reflection.getNMSClass("Containers"), "ANVIL").get(null);
            Object packet = PacketPlayOutOpenWindow.getConstructor(int.class, Containers, CraftGo.Reflection.getNMSClass("IChatBaseComponent")).newInstance(c, ANVIL, chatMessageConstructor.newInstance(inventoryName, new Object[]{}));
            Method sendPacket = CraftGo.Reflection.getMethod("sendPacket", playerConnection.getClass(), PacketPlayOutOpenWindow);
            sendPacket.invoke(playerConnection, packet);
            Field activeContainerField = CraftGo.Reflection.getField(EntityHuman, "activeContainer");
            if (activeContainerField != null) {
                activeContainerField.set(p, container);
                CraftGo.Reflection.getField(CraftGo.Reflection.getNMSClass("Container"), "windowId").set(activeContainerField.get(p), c);
                CraftGo.Reflection.getMethod("addSlotListener", activeContainerField.get(p).getClass(), p.getClass()).invoke(activeContainerField.get(p), p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        player = null;
        handler = null;
        items = null;
        HandlerList.unregisterAll(listener);
        listener = null;
    }

    public enum AnvilSlot {

        INPUT_LEFT(0),
        INPUT_RIGHT(1),
        OUTPUT(2);
        private int slot;

        AnvilSlot(int slot) {
            this.slot = slot;
        }

        public static AnvilSlot bySlot(int slot) {
            for (AnvilSlot anvilSlot : values()) {
                if (anvilSlot.getSlot() == slot) {
                    return anvilSlot;
                }
            }
            return null;
        }

        public int getSlot() {
            return slot;
        }
    }

    public interface AnvilClickEventHandler {
        void onAnvilClick(AnvilClickEvent event);
    }

    public class AnvilClickEvent {

        private AnvilSlot slot;
        private String name;
        private Player player;
        private boolean close = true;
        private boolean destroy = true;

        public AnvilClickEvent(AnvilSlot slot, String name, Player player) {
            this.slot = slot;
            this.name = name;
            this.player = player;
        }

        public AnvilSlot getSlot() {
            return slot;
        }

        public String getName() {
            return name;
        }

        public Player getPlayer() {
            return player;
        }

        public boolean getWillClose() {
            return close;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public boolean getWillDestroy() {
            return destroy;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }
    }
}