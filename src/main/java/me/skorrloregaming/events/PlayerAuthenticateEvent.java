package me.skorrloregaming.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerAuthenticateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String joinMessage;

    public PlayerAuthenticateEvent(Player player, String joinMessage) {
        this.player = player;
        this.joinMessage = joinMessage;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public String getJoinMessage() {
        return joinMessage;
    }

    public void setJoinMessage(String message) {
        this.joinMessage = message;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}