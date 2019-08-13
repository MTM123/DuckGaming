package me.skorrloregaming.hooks;

import com.gmail.nossr50.datatypes.skills.AbilityType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;
import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class mcMMO_Listener implements Listener {

    public void register() {
        Server.getInstance().getPlugin().getServer().getPluginManager().registerEvents(this, Server.getInstance().getPlugin());
    }

    @EventHandler
    public void onPlayerActivateAbility(McMMOPlayerAbilityActivateEvent event) {
        Player player = event.getPlayer();
        ServerMinigame minigame = $.getCurrentMinigame(player);
        if (minigame == ServerMinigame.SKYBLOCK || minigame == ServerMinigame.FACTIONS || minigame == ServerMinigame.SURVIVAL) {
            if (event.getAbility() == AbilityType.TREE_FELLER) {
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(true);
        }
    }
}
