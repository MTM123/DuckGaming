package me.skorrloregaming.lockette;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.plugin.PluginManager;

import java.util.List;

public class LocketteWorldListener implements Listener {
    public LocketteWorldListener(Lockette instance) {
    }

    protected void registerEvents() {
        PluginManager pm = Server.getInstance().getPlugin().getServer().getPluginManager();
        pm.registerEvents(this, Server.getInstance().getPlugin());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        if (event.isCancelled())
            return;
        if (!$.validLocketteMinigames.contains($.getMinigameFromWorld(event.getWorld()).toString().toLowerCase()))
            return;
        List<BlockState> blockList = event.getBlocks();
        int x, count = blockList.size();
        Block block;
        for (x = 0; x < count; ++x) {
            block = blockList.get(x).getBlock();
            if (Lockette.isProtected(block)) {
                event.setCancelled(true);
                return;
            }
            if (Lockette.explosionProtectionAll) {
                if (BlockUtil.isInList(block.getType(), BlockUtil.materialListNonDoors)) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }
}
