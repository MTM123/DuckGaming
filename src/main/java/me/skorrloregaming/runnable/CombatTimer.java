/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.runnable;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import me.skorrloregaming.Server;
import me.skorrloregaming.impl.extreme.ExtremeDouble;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatTimer
		extends BukkitRunnable {
	private final Player player;
	private String prefix;
	private int originalSeconds;
	private int passes = 0;

	public CombatTimer(Player player, Player targetPlayer, int seconds, String prefix) {
		this.player = player;
		this.prefix = prefix;
		player.sendMessage(String.valueOf(prefix) +  ChatColor.GRAY + "You have engaged in combat with " +  ChatColor.RED + targetPlayer.getName());
		this.originalSeconds = seconds;
	}

	public void run() {
		if (this.passes == 0) {
			Server.inCombat.put(this.player.getUniqueId(), new ExtremeDouble(this.getTaskId(), this.originalSeconds));
		}
		++this.passes;
		if (!Server.inCombat.containsKey(this.player.getUniqueId())) {
			this.cancel();
			return;
		}
		ExtremeDouble existingExtreme = Server.inCombat.get(this.player.getUniqueId());
		existingExtreme.setArg1(existingExtreme.getArg1() - 0.2);
		Server.inCombat.put(this.player.getUniqueId(), existingExtreme);
		if (existingExtreme.getArg1() <= 0.0) {
			this.player.sendMessage(String.valueOf(this.prefix) +  ChatColor.GRAY + "You are no longer engaged in combat.");
			Server.inCombat.remove(this.player.getUniqueId());
			this.cancel();
			return;
		}
	}
}

