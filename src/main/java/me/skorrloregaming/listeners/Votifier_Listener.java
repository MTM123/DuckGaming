/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.listeners;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import java.util.UUID;

import me.skorrloregaming.EconManager;
import me.skorrloregaming.Go;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class Votifier_Listener
		implements Listener {
	private Plugin plugin;

	public Votifier_Listener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		this.plugin.getServer().getPluginManager().registerEvents((Listener) this, this.plugin);
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		OfflinePlayer player = Bukkit.getOfflinePlayer((String) event.getVote().getUsername());
		EconManager.depositCash(player.getUniqueId(), 125, "factions", this.plugin);
		if (player.isOnline()) {
			player.getPlayer().sendMessage(String.valueOf(Go.Factions.tag) + "Cha-ching! You have been given " +  ChatColor.BOLD + "$125" +  ChatColor.RESET + " for voting.");
		}
		EconManager.depositCash(player.getUniqueId(), 75, "kitpvp", this.plugin);
		if (player.isOnline()) {
			player.getPlayer().sendMessage(String.valueOf(Go.Kitpvp.tag) + "Cha-ching! You have been given " +  ChatColor.BOLD + "$75" +  ChatColor.RESET + " for voting.");
		}
		if (player.isOnline()) {
			player.getPlayer().sendMessage(String.valueOf(Go.Legacy.tag) + "Thank you for voting for our server.");
			player.getPlayer().playSound(player.getPlayer().getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
		}
	}
}

