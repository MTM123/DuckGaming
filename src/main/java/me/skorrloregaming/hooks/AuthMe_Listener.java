package me.skorrloregaming.hooks;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.LinkSessionManager;
import me.skorrloregaming.Server;
import me.skorrloregaming.SessionManager;
import me.skorrloregaming.events.PlayerAuthenticateEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMe_Listener implements Listener {
	
	public void register() {
		Server.getPlugin().getServer().getPluginManager().registerEvents(this, Server.getPlugin());
	}

	public void playWelcomeMessage(Player player) {
		player.sendMessage("► Rest assured, we respect everyone's account security.");
	}

	@EventHandler
	public void onPlayerAuth(LoginEvent event) {
		Player player = event.getPlayer();
		if (!CraftGo.Player.getOnlineMode(player)) {
			playWelcomeMessage(player);
		}
		String joinMessage = Server.getDefaultJoinMessage().replace("{player}", player.getName());
		PlayerAuthenticateEvent authEvent = new PlayerAuthenticateEvent(player, joinMessage);
		Bukkit.getPluginManager().callEvent(authEvent);
	}

	@EventHandler
	public void onPlayerLogout(LogoutEvent event) {
		Player player = event.getPlayer();
		String remoteAddr = player.getAddress().getHostName();
		Server.getSessionManager().invalidateSession(player.getName(), player, LinkSessionManager.encodeHex(remoteAddr), false);
	}
}
