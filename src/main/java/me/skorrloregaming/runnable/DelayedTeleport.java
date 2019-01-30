package me.skorrloregaming.runnable;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.TitleSubtitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.Callable;

public class DelayedTeleport extends BukkitRunnable {
	public double remainingSeconds;
	public double originalSeconds;
	private boolean silent = false;
	private final Player player;
	private final Location originalPlayerLocation;
	private final Location teleportLocation;
	private boolean allowed = true;
	private DelayedTeleport instance;
	private Callable<?> finishTask = null;
	private TitleSubtitle finishTitleSubtitle = null;
	private String finishChatMessage = null;

	public DelayedTeleport(Player player, double seconds, Location teleportLocation, boolean silent) {
		this.remainingSeconds = seconds;
		this.originalSeconds = seconds;
		this.player = player;
		this.teleportLocation = teleportLocation;
		this.originalPlayerLocation = player.getLocation();
		this.silent = silent;
		if (teleportLocation == null) {
			if (!silent) {
				player.sendMessage("java.lang.NullPointerException");
			}
			allowed = false;
			return;
		}
		instance = this;
		Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (Server.getDelayedTeleports().containsKey(player.getUniqueId())) {
					Server.getDelayedTeleports().get(player.getUniqueId()).close();
				}
				Server.getDelayedTeleports().put(player.getUniqueId(), instance);
				if (!silent) {
					if ((seconds == Math.floor(seconds)) && !Double.isInfinite(seconds)) {
						final Double ctSeconds = seconds;
						player.sendMessage(ChatColor.GRAY + "Teleportation request accepted, teleporting in " + ChatColor.RED + ctSeconds.intValue() + ChatColor.GRAY + " seconds.");
					} else {
						player.sendMessage(ChatColor.GRAY + "Teleportation request accepted, teleporting in " + ChatColor.RED + seconds + ChatColor.GRAY + " seconds.");
					}
				} else {
					if (player.hasPotionEffect(PotionEffectType.CONFUSION))
						player.removePotionEffect(PotionEffectType.CONFUSION);
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, (20 * ((int) seconds * 2)), 1));
				}
			}
		});
	}

	public void setFinishTitleSubtitle(TitleSubtitle arg0) {
		this.finishTitleSubtitle = arg0;
	}

	public void setFinishChatMessage(String arg0) {
		this.finishChatMessage = arg0;
	}

	public void close(String reason) {
		if (!(reason == null)) {
			player.sendMessage(reason);
		}
		Server.getDelayedTeleports().remove(player.getUniqueId());
		allowed = false;
	}

	public void close() {
		close(null);
	}

	public void setFinishTask(Callable<?> method) {
		finishTask = method;
	}

	@Override
	public void run() {
		if (!allowed) {
			cancel();
		} else {
			remainingSeconds -= 0.20;
			if (remainingSeconds <= 0) {
				remainingSeconds = originalSeconds;
				if (Server.getPlugin().getServer().getPlayer(player.getName()) != null) {
					if (!(finishChatMessage == null) && finishChatMessage.length() > 0)
						player.sendMessage(finishChatMessage);
					if (!silent) {
						player.sendMessage(ChatColor.GRAY + "Teleporting..");
					}
					Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
						@Override
						public void run() {
							$.teleport(player, teleportLocation);
							if (!(finishTitleSubtitle == null))
								CraftGo.Player.sendTimedTitleAndSubtitle(player, finishTitleSubtitle);
							Server.getDelayedTeleports().remove(player.getUniqueId());
							if (!(finishTask == null)) {
								try {
									finishTask.call();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					});
				}
				cancel();
				return;
			}
			if (player.getWorld().getName().equals(originalPlayerLocation.getWorld().getName())) {
				if (player.getLocation().distance(originalPlayerLocation) >= 0.1) {
					Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
						@Override
						public void run() {
							if (!silent) {
								player.sendMessage(ChatColor.GRAY + "Teleportation request aborted due to movement.");
							} else {
								if (player.hasPotionEffect(PotionEffectType.CONFUSION))
									player.removePotionEffect(PotionEffectType.CONFUSION);
							}
							Server.getDelayedTeleports().remove(player.getUniqueId());
						}
					});
					cancel();
					return;
				}
			} else {
				cancel();
			}
		}
	}
}
