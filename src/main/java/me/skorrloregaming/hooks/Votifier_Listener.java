package me.skorrloregaming.hooks;

import com.vexsoftware.votifier.model.VotifierEvent;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Votifier_Listener implements Listener {

    public void register() {
        Bukkit.getPluginManager().registerEvents(this, Server.getInstance().getPlugin());
    }

    @EventHandler
    public void onVote(VotifierEvent event) {
        Server.getInstance().getVoteManager().onVote(event, true);
    }
}