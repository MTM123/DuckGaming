/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Directory;
import me.skorrloregaming.Go;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.extreme.ExtremeSwear;
import me.skorrloregaming.impl.extreme.ExtremeSwearData;
import mkremins.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class AntiCheat
		implements Listener {
	public Plugin plugin;
	public AntiAfk antiafk;
	public ExtremeSwearData swearData;
	public ArrayList<UUID> shootDelay = new ArrayList();
	public ArrayList<UUID> attackDelay = new ArrayList();
	public ArrayList<String> swearWords = new ArrayList();
	public ConcurrentHashMap<UUID, Location> lastPlayerLocation = new ConcurrentHashMap();
	public ConcurrentHashMap<UUID, Integer> ignoredPlayers = new ConcurrentHashMap();
	public ArrayList<ServerMinigame> flightCheckDisabledMinigames = new ArrayList();
	public ArrayList<ServerMinigame> speedCheckDisabledMinigames = new ArrayList();
	public ArrayList<ServerMinigame> fastbowCheckDisabledMinigames = new ArrayList();
	public ArrayList<ServerMinigame> rangeCheckDisabledMinigames = new ArrayList();
	public final long shootDelayTime = 3L;
	public final int messagesPerSecondBeforeWarning = 2;
	public final int messagesPerSecondBeforeKick = 3;
	public final double maxAttackRange = 4.5;
	public final double maxDistanceMoved = 0.8;
	public final double maxDistanceMovedFloor = 0.44;
	public final double maxVerticalBlockChange = -0.8;
	public final double lackWeaponDamage = 0.2;
	public final double withWeaponDamageMultiplierEntity = 1.5;
	public final double withWeaponDamageMultiplierPlayer = 1.0;

	public AntiCheat(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		Bukkit.getPluginManager().registerEvents((Listener) this, Server.plugin);
		Bukkit.getScheduler().runTaskTimer(Server.plugin, new Runnable() {

			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (AntiCheat.this.getDistanceFromGround((Entity) player) > 2 && AntiCheat.this.lastPlayerLocation.containsKey(player.getUniqueId())) {
						int type;
						Location historicalLocation = AntiCheat.this.lastPlayerLocation.get(player.getUniqueId());
						if (!(!player.getWorld().getName().equals(historicalLocation.getWorld().getName()) || player.getLocation().distance(historicalLocation) != 0.0 || AntiCheat.this.isEntityNearGround((Entity) player) || player.getGameMode() == GameMode.CREATIVE || player.isFlying() || player.isOp() || (type = player.getWorld().getBlockTypeIdAt(player.getLocation())) == 65 || type == 106 || type == 8 || type == 9 || type == 10 || type == 11 || AntiCheat.this.flightCheckDisabledMinigames.contains( Go.getCurrentMinigame(player)) || AntiCheat.this.ignoredPlayers.containsKey(player.getUniqueId()))) {
							player.kickPlayer("Hovering is not allowed on this server.");
							AntiCheat.log(player, String.valueOf(player.getName()) + " has been kicked for hovering too long ( > 2 seconds )");
						}
					}
					AntiCheat.this.lastPlayerLocation.put(player.getUniqueId(), player.getLocation());
				}
			}
		}, 20L, 20L);
		this.antiafk = new AntiAfk(this.plugin);
		this.swearData = new ExtremeSwearData();
		Bukkit.getScheduler().runTaskTimer(this.plugin, (Runnable) this.antiafk, 300L, 300L);
		this.swearWords.add("fuck");
		this.swearWords.add("nigga");
		this.swearWords.add("nigger");
		this.swearWords.add("bitch");
		this.swearWords.add("dick");
		this.swearWords.add("cunt");
		this.swearWords.add("crap");
		this.swearWords.add("shit");
		this.swearWords.add("whore");
		this.swearWords.add("twat");
		this.swearWords.add("arse");
		this.swearWords.add("ass");
		this.swearWords.add("horny");
		this.swearWords.add("aroused");
		this.swearWords.add("hentai");
		this.swearWords.add("slut");
		this.swearWords.add("slag");
		this.swearWords.add("boob");
		this.swearWords.add("pussy");
		this.swearWords.add("vagina");
		this.swearWords.add("faggot");
		this.swearWords.add("bugger");
		this.swearWords.add("bastard");
		this.swearWords.add("anal");
		this.swearWords.add("wanker");
		this.swearWords.add("rape");
		this.swearWords.add("rapist");
		this.swearWords.add("cock");
		this.swearWords.add("titt");
		this.swearWords.add("piss");
		this.swearWords.add("spunk");
		this.swearWords.add("milf");
		this.swearWords.add("anus");
		this.swearWords.add("dafuq");
		for (String string : this.swearWords) {
			this.swearData.fill(string);
		}
	}

	public boolean isEntityNearGround(Entity entity) {
		int middleCenter = entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(0.0, 1.0, 0.0));
		int innerCircle = entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(1.0, 1.0, 0.0));
		int outerBlocks = entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(1.0, 1.0, 1.0));
		boolean succeededInnerCircle = false;
		if (entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(0.0, 1.0, 1.0)) == innerCircle && entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(-1.0, 1.0, 0.0)) == innerCircle && entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(0.0, 1.0, -1.0)) == innerCircle) {
			succeededInnerCircle = true;
		}
		boolean succeededOuterBlocks = false;
		if (entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(1.0, 1.0, -1.0)) == outerBlocks && entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(-1.0, 1.0, -1.0)) == outerBlocks && entity.getWorld().getBlockTypeIdAt(entity.getLocation().clone().subtract(-1.0, 1.0, 1.0)) == outerBlocks) {
			succeededOuterBlocks = true;
		}
		if (succeededOuterBlocks && succeededInnerCircle && innerCircle == 0 && outerBlocks == 0 && middleCenter == 0) {
			return false;
		}
		return true;
	}

	public int getDistanceFromGround(Entity entity) {
		Location loc = entity.getLocation().clone();
		double y = loc.getBlockY();
		int distance = 0;
		double i = y;
		while (i >= 0.0) {
			loc.setY(i);
			if (loc.getWorld().getBlockTypeIdAt(loc) > 0) break;
			++distance;
			i -= 1.0;
		}
		return distance;
	}

	public boolean disableFor(final Player player, long time) {
		try {
			long delay = (long) (20.0 * Math.floor(time / 1000L));
			int previousTaskID = 0;
			if (this.ignoredPlayers.containsKey(player.getUniqueId())) {
				previousTaskID = this.ignoredPlayers.get(player.getUniqueId());
			}
			if (previousTaskID > 0) {
				Bukkit.getScheduler().cancelTask(previousTaskID);
			}
			BukkitRunnable run = new BukkitRunnable() {

				public void run() {
					if (!AntiCheat.this.ignoredPlayers.contains(player.getUniqueId())) {
						AntiCheat.this.ignoredPlayers.remove(player.getUniqueId());
					}
				}
			};
			run.runTaskLater(this.plugin, delay);
			this.ignoredPlayers.put(player.getUniqueId(), run.getTaskId());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void handleVelocity(Player player, Vector velo) {
		this.handleVelocity(player, velo, false);
	}

	public void handleVelocity(Player player, Vector velo, boolean direct) {
		if (!direct) {
			this.disableFor(player, 2000L);
		}
		player.setVelocity(velo);
	}

	public static void log(Player detected, String msg, boolean permissionOnly, boolean consoleOnly) {
		try {
			String tag = "SimpleAC: ";
			Logger.warning(String.valueOf(tag) + msg);
			if (!consoleOnly) {
				String igMessage = new FancyMessage(String.valueOf(tag) + msg).style(ChatColor.ITALIC).suggest("/who " + detected.getName()).tooltip("/who " + detected.getName()).toJSONString();
				for (Player player : Bukkit.getOnlinePlayers()) {
					int rankID = -1;
					if (!permissionOnly) {
						rankID = Go.getRankId(player, Server.plugin);
					}
					if (!player.hasPermission("skorrloregaming.events.chatlisten") && rankID <= -1) continue;
					CraftGo.Player.sendJson(player, igMessage);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void log(Player detected, String msg) {
		AntiCheat.log(detected, msg, false, false);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		if (Server.pluginDebug) {
			AntiCheat.log(player, String.valueOf(player.getName()) + " has triggered PlayerTeleportEvent", true, true);
		}
		this.disableFor(player, 5000L);
	}

	@EventHandler
	public void onArrowShoot(ProjectileLaunchEvent event) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getEntity().getShooter() instanceof Player) {
			final Player player = (Player) event.getEntity().getShooter();
			if (Server.pluginDebug) {
				AntiCheat.log(player, String.valueOf(player.getName()) + " has triggered ProjectileLaunchEvent", true, true);
			}
			if (player.isOp()) {
				if (Server.pluginDebug) {
					AntiCheat.log(player, String.valueOf(player.getName()) + " is currently opped; Terminating ProjectileLaunchEvent", true, true);
				}
				return;
			}
			if (this.fastbowCheckDisabledMinigames.contains( Go.getCurrentMinigame(player))) {
				return;
			}
			if (this.ignoredPlayers.containsKey(player.getUniqueId())) {
				if (Server.pluginDebug) {
					AntiCheat.log(player, String.valueOf(player.getName()) + " is currently ignored; Terminating ProjectileLaunchEvent", true, true);
				}
				return;
			}
			if (this.shootDelay.contains(player.getUniqueId())) {
				event.setCancelled(true);
				AntiCheat.log(player, String.valueOf(player.getName()) + " launched projectiles faster then normal ( > 1 in " + 3L + " ticks )");
			} else {
				this.shootDelay.add(player.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Server.plugin, new Runnable() {

					@Override
					public void run() {
						AntiCheat.this.shootDelay.remove(player.getUniqueId());
					}
				}, 3L);
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Player damagee;
		if (event.isCancelled()) {
			return;
		}
		if (event.getEntity() instanceof Player) {
			damagee = (Player) event.getEntity();
			this.disableFor(damagee, 2000L);
		}
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			boolean lackWeapon = false;
			if (damager.getItemInHand() == null || damager.getItemInHand().getType() == Material.AIR) {
				event.setDamage(0.2);
				lackWeapon = true;
			} else if (!Directory.repairableItems.contains( damager.getItemInHand().getType())) {
				event.setDamage(0.2);
				lackWeapon = true;
			}
			if (!lackWeapon && !(event.getEntity() instanceof Player)) {
				event.setDamage(event.getDamage() * 1.5);
			}
		}
		if (event.getDamager() instanceof Arrow) {
			event.setDamage(event.getDamage() * 0.5);
		}
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			damagee = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if (Server.pluginDebug) {
				AntiCheat.log(damagee, String.valueOf(damagee.getName()) + " has triggered EntityDamageByEntityEvent", true, true);
			}
			if (damager.isOp()) {
				if (Server.pluginDebug) {
					AntiCheat.log(damager, String.valueOf(damager.getName()) + " is currently opped; Terminating EntityDamageByEntityEvent", true, true);
				}
				return;
			}
			if (this.rangeCheckDisabledMinigames.contains( Go.getCurrentMinigame(damager))) {
				return;
			}
			if (this.ignoredPlayers.containsKey(damager.getUniqueId())) {
				if (Server.pluginDebug) {
					AntiCheat.log(damager, String.valueOf(damager.getName()) + " is currently ignored; Terminating EntityDamageByEntityEvent", true, true);
				}
				return;
			}
			event.setDamage(event.getDamage() * 1.0);
			if (damagee.getLocation().distance(damager.getLocation()) > 4.5) {
				AntiCheat.log(damager, String.valueOf(damager.getName()) + " has attacked with a greater range then normal ( > " + 4.5 + " blocks )");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getPlayer();
		if (player.isInsideVehicle() || player.isGliding() || player.isOp()) {
			return;
		}
		if (this.speedCheckDisabledMinigames.contains( Go.getCurrentMinigame(player))) {
			return;
		}
		if (this.ignoredPlayers.containsKey(player.getUniqueId())) {
			return;
		}
		double maxSpeed = 0.8;
		if (player.getGameMode() == GameMode.CREATIVE && player.isFlying()) {
			maxSpeed = (double) Math.round(maxSpeed * 1.5 * 100.0) / 100.0;
		}
		Location from = event.getFrom().clone();
		Location to = event.getTo().clone();
		if (player.isOnGround()) {
			maxSpeed = 0.44;
		}
		if (from.getY() - to.getY() < -0.8 && !player.isInsideVehicle() && !player.isGliding() && player.getGameMode() != GameMode.CREATIVE) {
			AntiCheat.log(player, String.valueOf(player.getName()) + " has moved with a greater speed then normal ( < " + -0.8 + " blocks )");
			event.setCancelled(true);
		}
		from.setY(0.0);
		to.setY(0.0);
		if (from.distance(to) > maxSpeed) {
			AntiCheat.log(player, String.valueOf(player.getName()) + " has moved with a greater speed then normal ( > " + maxSpeed + " blocks )");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Server.pluginDebug) {
			AntiCheat.log(player, String.valueOf(player.getName()) + " has triggered PlayerQuitEvent", true, true);
		}
		if (this.lastPlayerLocation.containsKey(player.getUniqueId())) {
			this.lastPlayerLocation.remove(player.getUniqueId());
		}
	}

	public boolean onBlockBreak(Block block, Entity entity) {
		boolean bypass = false;
		if (entity != null && entity instanceof Player) {
			bypass = ((Player) entity).hasPermission("skorrloregaming.bypass.spawnProtection");
		}
		ServerMinigame minigame = Go.getMinigameFromWorld(block.getWorld(), this.plugin);
		if (!(bypass || minigame != ServerMinigame.SURVIVAL && minigame != ServerMinigame.FACTIONS)) {
			Location spawnLocation = Go.getZoneLocation(block.getWorld().getName(), Server.plugin);
			if (block.getLocation().distance(spawnLocation) < 30.0) {
				return true;
			}
		}
		return false;
	}

	public boolean onBlockPlace(Block block, Entity entity) {
		boolean bypass = false;
		if (entity != null && entity instanceof Player) {
			bypass = ((Player) entity).hasPermission("skorrloregaming.bypass.spawnProtection");
		}
		ServerMinigame minigame = Go.getMinigameFromWorld(block.getWorld(), this.plugin);
		if (!(bypass || minigame != ServerMinigame.SURVIVAL && minigame != ServerMinigame.FACTIONS)) {
			Location spawnLocation = Go.getZoneLocation(block.getWorld().getName(), Server.plugin);
			if (block.getLocation().distance(spawnLocation) < 30.0) {
				return true;
			}
		}
		return false;
	}

	public String processAntiSwear(Player player, String message) {
		String modifiedMessage = message;
		char[] messageChars = modifiedMessage.toCharArray();
		ExtremeSwear commonSwear = new ExtremeSwear(0, false, "", 0);
		boolean detectedSwearing = false;
		for (int i = 0; i < modifiedMessage.length() + 1; i++) {
			if (commonSwear.getArg1()) {
				String currentSwear = commonSwear.getArg2();
				int startIndex = commonSwear.getArg0();
				if (this.swearWords.contains(currentSwear.replace(" ", ""))) {
					int endIndex = commonSwear.getArg3();
					for (int index = startIndex; index < endIndex + 1; index++) {
						messageChars[index] = '*';
					}
					modifiedMessage = new String(messageChars);
					commonSwear = new ExtremeSwear(0, false, "", 0);
					detectedSwearing = true;
					i = 0;
					continue;
				}
				if (messageChars.length > i) {
					if (this.swearData.get(currentSwear.length()).contains(Character.valueOf(messageChars[i]))) {
						currentSwear = currentSwear + messageChars[i];
						boolean taskSuccess = false;
						for (String string : this.swearWords) {
							if (string.contains(currentSwear)) {
								taskSuccess = true;
								break;
							}
						}
						if (taskSuccess) {
							commonSwear = new ExtremeSwear(startIndex, true, currentSwear, i);
							continue;
						}
					}
					if (messageChars[i] != ' ') {
						commonSwear = new ExtremeSwear(0, false, "", 0);
					}
				}
			}
			if ((messageChars.length > i) && (this.swearData.get(0).contains(Character.valueOf(messageChars[i])))) {
				commonSwear = new ExtremeSwear(i, true, String.valueOf(messageChars[i]), i);
			}
		}
		if (detectedSwearing) {
			Logger.info(modifiedMessage);
			player.sendMessage(ChatColor.RED + "Please do not swear, otherwise action will be taken.");
		}
		return modifiedMessage;
	}

	public class AntiAfk
			extends AntiCheat
			implements Runnable {
		public ConcurrentHashMap<UUID, Location> lastPlayerLocation;
		public ConcurrentHashMap<UUID, Double> lackingMovementMinutes;

		public AntiAfk(Plugin plugin) {
			super(plugin);
			this.lastPlayerLocation = new ConcurrentHashMap();
			this.lackingMovementMinutes = new ConcurrentHashMap();
		}

		@Override
		public void run() {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (Go.getRankId(player, this.plugin) >= 2) continue;
				if (!this.lastPlayerLocation.containsKey(player.getUniqueId())) {
					this.lastPlayerLocation.put(player.getUniqueId(), player.getLocation());
				}
				Location lastLocation = this.lastPlayerLocation.get(player.getUniqueId());
				boolean hasMoved = true;
				if (lastLocation.getWorld().getName().equals(player.getWorld().getName()) && lastLocation.distance(player.getLocation()) < 4.5) {
					if (!this.lackingMovementMinutes.containsKey(player.getUniqueId())) {
						this.lackingMovementMinutes.put(player.getUniqueId(), 0.0);
					}
					double lastMinutes = this.lackingMovementMinutes.get(player.getUniqueId());
					double newMinutes = lastMinutes + 0.25;
					this.lackingMovementMinutes.put(player.getUniqueId(), newMinutes);
					if (newMinutes >= 5.0) {
						player.kickPlayer("You have been kicked for afking too long.");
						this.lackingMovementMinutes.remove(player.getUniqueId());
					}
					hasMoved = false;
				}
				if (hasMoved && this.lackingMovementMinutes.containsKey(player.getUniqueId())) {
					this.lackingMovementMinutes.remove(player.getUniqueId());
				}
				this.lastPlayerLocation.put(player.getUniqueId(), player.getLocation());
			}
		}
	}

}

