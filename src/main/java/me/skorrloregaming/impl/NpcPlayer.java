package me.skorrloregaming.impl;

import me.skorrloregaming.CraftGo;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class NpcPlayer {

    private String name;
    private World world;
    private Location location;

    private UUID offlineId;

    public NpcPlayer(String name, World world, Location location) {
        try {
            this.name = name;
            this.world = world;
            this.location = location;
            this.offlineId = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getId() {
        if (Bukkit.getOnlineMode())
            return UUID.fromString(CraftGo.Player.getUUID(name, true));
        return offlineId;
    }

    public UUID getOnlineId() {
        return UUID.fromString(CraftGo.Player.getUUID(name, true));
    }

    public UUID getOfflineId() {
        return offlineId;
    }

}
