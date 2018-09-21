package me.skorrloregaming.hooks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.datatypes.skills.AbilityType;
import com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent;

import me.skorrloregaming.$;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;

public class mcMMO_Listener implements Listener {

	public void register() {
		Server.getPlugin().getServer().getPluginManager().registerEvents(this, Server.getPlugin());
	}

	@EventHandler
	public void onPlayerActivateAbility(McMMOPlayerAbilityActivateEvent event) {
		Player player = event.getPlayer();
		ServerMinigame minigame = $.getCurrentMinigame(player);
		if (event.getAbility() == AbilityType.TREE_FELLER) {
			if (minigame == ServerMinigame.SKYBLOCK || minigame == ServerMinigame.FACTIONS)
				event.setCancelled(true);
		}
	}
}
