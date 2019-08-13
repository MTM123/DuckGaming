package me.skorrloregaming.events;

import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerMinigameChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ServerMinigame minigame;

    public PlayerMinigameChangeEvent(Player player, ServerMinigame minigame) {
        this.player = player;
        this.minigame = minigame;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public ServerMinigame getMinigame() {
        return minigame;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
