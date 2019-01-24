/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.runnable;

import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;

import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DelayedTeleport
		extends BukkitRunnable {
	public double remainingSeconds;
	public int originalSeconds;
	private boolean silent = false;
	private final Player player;
	private final Location originalPlayerLocation;
	private final Location teleportLocation;
	private Plugin plugin;
	private boolean allowed = true;
	private DelayedTeleport instance;
	private Callable<?> finishTask = null;

	public DelayedTeleport(final Player player, final int seconds, Location teleportLocation, Plugin plugin, final Boolean silent) {
		this.remainingSeconds = seconds;
		this.originalSeconds = seconds;
		this.player = player;
		this.teleportLocation = teleportLocation;
		this.plugin = plugin;
		this.originalPlayerLocation = player.getLocation();
		this.silent = silent;
		if (teleportLocation == null) {
			if (!silent.booleanValue()) {
				player.sendMessage("java.lang.NullPointerException");
			}
			this.allowed = false;
			return;
		}
		this.instance = this;
		Bukkit.getScheduler().runTask(plugin, new Runnable() {

			@Override
			public void run() {
				if (Server.delayedTeleports.containsKey(player.getUniqueId())) {
					Server.delayedTeleports.get(player.getUniqueId()).close();
				}
				Server.delayedTeleports.put(player.getUniqueId(), DelayedTeleport.this.instance);
				if (!silent.booleanValue()) {
					player.sendMessage( ChatColor.GRAY + "Teleportation request accepted, teleporting in " +  ChatColor.RED + seconds +  ChatColor.GRAY + " seconds.");
				} else {
					if (player.hasPotionEffect(PotionEffectType.CONFUSION)) {
						player.removePotionEffect(PotionEffectType.CONFUSION);
					}
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * (seconds * 2), 1));
				}
			}
		});
	}

	public void close(String reason) {
		if (reason != null) {
			this.player.sendMessage(reason);
		}
		Server.delayedTeleports.remove(this.player.getUniqueId());
		this.allowed = false;
	}

	public void close() {
		this.close(null);
	}

	public void setFinishTask(Callable<?> method) {
		this.finishTask = method;
	}

	public void run() {
		if (!this.allowed) {
			this.cancel();
		} else {
			this.remainingSeconds -= 0.2;
			if (this.remainingSeconds <= 0.0) {
				this.remainingSeconds = this.originalSeconds;
				if (this.plugin.getServer().getPlayer(this.player.getName()) != null) {
					if (!this.silent) {
						this.player.sendMessage( ChatColor.GRAY + "Teleporting..");
					}
					Bukkit.getScheduler().runTask(this.plugin, new Runnable() {

						@Override
						public void run() {
							DelayedTeleport.this.player.teleport(DelayedTeleport.this.teleportLocation);
							Server.delayedTeleports.remove(DelayedTeleport.this.player.getUniqueId());
							if (DelayedTeleport.this.finishTask != null) {
								try {
									DelayedTeleport.this.finishTask.call();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
				this.cancel();
				return;
			}
			if (this.player.getLocation().distance(this.originalPlayerLocation) >= 0.1) {
				Bukkit.getScheduler().runTask(this.plugin, new Runnable() {

					@Override
					public void run() {
						if (!DelayedTeleport.this.silent) {
							DelayedTeleport.this.player.sendMessage( ChatColor.GRAY + "Teleportation request aborted due to movement.");
						} else if (DelayedTeleport.this.player.hasPotionEffect(PotionEffectType.CONFUSION)) {
							DelayedTeleport.this.player.removePotionEffect(PotionEffectType.CONFUSION);
						}
						Server.delayedTeleports.remove(DelayedTeleport.this.player.getUniqueId());
					}
				});
				this.cancel();
				return;
			}
		}
	}

}

