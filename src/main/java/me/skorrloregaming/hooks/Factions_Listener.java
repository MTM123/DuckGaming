package me.skorrloregaming.hooks;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.event.PowerLossEvent;
import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.skorrloregaming.*;

public class Factions_Listener implements Listener {

	public void register() {
		ServerGet.get().getPlugin().getServer().getPluginManager().registerEvents(this, ServerGet.get().getPlugin());
		ServerGet.get().getBukkitTasks().add(Bukkit.getScheduler().runTaskTimer(ServerGet.get().getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
					if (Link$.getRankId(fplayer.getPlayer()) > -1) {
						fplayer.setSpyingChat(true);
					} else {
						fplayer.setSpyingChat(false);
					}
				}
			}
		}, 20L, 20L));
	}

	@EventHandler
	public void onPlayerPowerLoss(PowerLossEvent event) {
		Player player = event.getfPlayer().getPlayer();
		if (!($.getCurrentMinigame(player) == ServerMinigame.FACTIONS) || ServerGet.get().getModeratingPlayers().containsKey(player.getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
