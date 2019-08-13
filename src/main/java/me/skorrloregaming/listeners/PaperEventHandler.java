package me.skorrloregaming.listeners;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import me.skorrloregaming.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PaperEventHandler implements Listener {

    public PaperEventHandler() {
        Server.getInstance().getPlugin().getLogger().info("Registered PaperSpigot specific event listener");
        Server.getInstance().getPlugin().getLogger().info("If issues arise from this, report them immediately.");
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        if (Server.getInstance().getWaiverAcceptPlayers().contains(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }
    }
}
