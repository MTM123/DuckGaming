package me.skorrloregaming.impl;

import me.skorrloregaming.Server;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class CustomNpc {
    public ArmorStand entity;

    public CustomNpc(ArmorStand entity) {
        this.entity = entity;
    }

    public static CustomNpc spawn(Location location, String name) {
        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);
        ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        entity.setAI(false);
        entity.setCanPickupItems(false);
        entity.setCollidable(false);
        entity.setInvulnerable(true);
        entity.setArms(true);
        if (!(name == null)) {
            entity.setCustomName(name);
            entity.setCustomNameVisible(true);
        }
        return new CustomNpc(entity);
    }

    public static String getNpcData(Entity entity) {
        if (!Server.getInstance().getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString()))
            return null;
        String value = Server.getInstance().getNpcConfig().getData().getString("npc." + entity.getUniqueId().toString());
        if (value.equals("unspecified"))
            return null;
        return value;
    }

    public boolean register(String npcData) {
        if (Server.getInstance().getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString()))
            return false;
        String hash = "unspecified";
        if (!(npcData == null))
            hash = npcData;
        Server.getInstance().getNpcConfig().getData().set("npc." + entity.getUniqueId().toString(), hash);
        Server.getInstance().getNpcConfig().saveData();
        return true;
    }

    public boolean unregister() {
        if (!Server.getInstance().getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString()))
            return false;
        Server.getInstance().getNpcConfig().getData().set("npc." + entity.getUniqueId().toString(), null);
        Server.getInstance().getNpcConfig().saveData();
        return true;
    }
}
