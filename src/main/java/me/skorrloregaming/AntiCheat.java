package me.skorrloregaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.skorrloregaming.impl.LastLocation;
import me.skorrloregaming.impl.ServerMinigame;
import mkremins.fanciful.FancyMessage;

public class AntiCheat implements Listener {

	public AntiAfk antiafk;
	public ArrayList<UUID> shootDelay = new ArrayList<>();
	public ArrayList<UUID> attackDelay = new ArrayList<>();
	public ArrayList<String> swearWords = new ArrayList<String>();
	public ConcurrentHashMap<UUID, Location> lastPlayerLocation = new ConcurrentHashMap<>();
	public ConcurrentHashMap<UUID, Integer> ignoredPlayers = new ConcurrentHashMap<>();
	public ArrayList<ServerMinigame> flightCheckDisabledMinigames = new ArrayList<>();
	public ArrayList<ServerMinigame> speedCheckDisabledMinigames = new ArrayList<>();
	public ArrayList<ServerMinigame> fastbowCheckDisabledMinigames = new ArrayList<>();
	public ArrayList<ServerMinigame> rangeCheckDisabledMinigames = new ArrayList<>();
	public final long shootDelayTime = 3;
	public final int messagesPerSecondBeforeWarning = 2;
	public final int messagesPerSecondBeforeKick = 3;
	public final double maxAttackRange = 4.75;
	public final double maxDistanceMoved = 0.84;
	public final double maxDistanceMovedFloor = 0.48;
	public final double maxVerticalBlockChange = -0.8;
	public final double lackWeaponDamage = 0.2;
	public final double withWeaponDamageMultiplierEntity = 1.5;
	public final double withWeaponDamageMultiplierPlayer = 1;

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, Server.getPlugin());
		antiafk = new AntiAfk();
		Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), antiafk, 600L, 600L);
		swearWords.add("fuck");
		swearWords.add("nigga");
		swearWords.add("nigger");
		swearWords.add("bitch");
		swearWords.add("dick");
		swearWords.add("cunt");
		swearWords.add("crap");
		swearWords.add("shit");
		swearWords.add("whore");
		swearWords.add("twat");
		swearWords.add("arse");
		swearWords.add("ass");
		swearWords.add("horny");
		swearWords.add("aroused");
		swearWords.add("hentai");
		swearWords.add("slut");
		swearWords.add("slag");
		swearWords.add("boob");
		swearWords.add("pussy");
		swearWords.add("vagina");
		swearWords.add("faggot");
		swearWords.add("bugger");
		swearWords.add("bastard");
		swearWords.add("anal");
		swearWords.add("wanker");
		swearWords.add("rape");
		swearWords.add("rapist");
		swearWords.add("cock");
		swearWords.add("titt");
		swearWords.add("piss");
		swearWords.add("spunk");
		swearWords.add("milf");
		swearWords.add("anus");
		swearWords.add("dafuq");
		swearWords.add("damn");
	}

	public boolean isEntityNearGround(Entity entity) {
		Material air = Material.AIR;
		Material middleCenter = entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(0, 1, 0)).getType();
		Material innerCircle = entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(1, 1, 0)).getType();
		Material outerBlocks = entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(1, 1, 1)).getType();
		boolean succeededInnerCircle = false;
		if (entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(0, 1, 1)).getType() == innerCircle) {
			if (entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(-1, 1, 0)).getType() == innerCircle) {
				if (entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(0, 1, -1)).getType() == innerCircle) {
					succeededInnerCircle = true;
				}
			}
		}
		boolean succeededOuterBlocks = false;
		if (entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(1, 1, -1)).getType() == outerBlocks) {
			if (entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(-1, 1, -1)).getType() == outerBlocks) {
				if (entity.getWorld().getBlockAt(entity.getLocation().clone().subtract(-1, 1, 1)).getType() == outerBlocks) {
					succeededOuterBlocks = true;
				}
			}
		}
		if (succeededOuterBlocks && succeededInnerCircle && innerCircle == air && outerBlocks == air && middleCenter == air)
			return false;
		return true;
	}

	public int getDistanceFromGround(Entity entity) {
		Location loc = entity.getLocation().clone();
		double y = loc.getBlockY();
		int distance = 0;
		for (double i = y; i >= 0; i--) {
			loc.setY(i);
			if (!(loc.getWorld().getBlockAt(loc).getType() == Material.AIR))
				break;
			distance++;
		}
		return distance;
	}

	public boolean disableFor(Player player, long time) {
		try {
			long delay = (long) (20 * Math.floor(time / 1000));
			int previousTaskID = 0;
			if (ignoredPlayers.containsKey(player.getUniqueId())) {
				previousTaskID = ignoredPlayers.get(player.getUniqueId());
			}
			if (previousTaskID > 0) {
				Bukkit.getScheduler().cancelTask(previousTaskID);
			}
			BukkitRunnable run = new BukkitRunnable() {
				@Override
				public void run() {
					if (!ignoredPlayers.contains(player.getUniqueId())) {
						ignoredPlayers.remove(player.getUniqueId());
					}
				}
			};
			run.runTaskLater(Server.getPlugin(), delay);
			ignoredPlayers.put(player.getUniqueId(), run.getTaskId());
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public void handleVelocity(Player player, Vector velo) {
		handleVelocity(player, velo, false);
	}

	public void handleVelocity(Player player, Vector velo, boolean direct) {
		if (!direct)
			disableFor(player, 2000);
		player.setVelocity(velo);
	}

	public static class LimiterArgs {
		public String lastMsg = "";
		public int hits = 0;
		public int lastPeriod = 0;
	}

	public static HashMap<Player, LimiterArgs> consoleLimiterArgs = new HashMap<Player, LimiterArgs>();

	public static void log(Player detected, String msg, boolean permissionOnly, boolean consoleOnly) {
		boolean eventCancelled = false;
		LimiterArgs args = consoleLimiterArgs.getOrDefault(detected, new LimiterArgs());
		int currentPeriod = (int) (System.currentTimeMillis() / 1000);
		if (currentPeriod / 3 != args.lastPeriod) {
			args.lastPeriod = currentPeriod / 3;
			args.lastMsg = "";
			args.hits = 0;
		} else if (msg.equals(args.lastMsg)) {
			args.hits++;
			consoleLimiterArgs.put(detected, args);
			return;
		} else {
			args.lastMsg = msg;
		}
		consoleLimiterArgs.put(detected, args);
		if (eventCancelled)
			return;
		try {
			String tag = "SimpleAC: ";
			Logger.info(tag + msg, true);
			if (!consoleOnly && Server.getIngameAnticheatDebug()) {
				String igMessage = new FancyMessage(tag + msg).style(mkremins.fanciful.ChatColor.ITALIC).suggest("/who " + detected.getName()).tooltip("/who " + detected.getName()).exportToJson();
				for (Player player : Bukkit.getOnlinePlayers()) {
					int rankID = -1;
					if (!permissionOnly)
						rankID = $.getRankId(player);
					if (player.isOp() || rankID > -1) {
						CraftGo.Player.sendJson(player, igMessage);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void log(Player detected, String msg) {
		log(detected, msg, false, false);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		if (Server.getPluginDebug())
			log(player, player.getName() + " has triggered PlayerTeleportEvent", true, true);
		disableFor(player, 5000);
	}

	@EventHandler
	public void onArrowShoot(ProjectileLaunchEvent event) {
		if (event.isCancelled())
			return;
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			if (Server.getPluginDebug())
				log(player, player.getName() + " has triggered ProjectileLaunchEvent", true, true);
			if (player.isOp()) {
				if (Server.getPluginDebug())
					log(player, player.getName() + " is currently opped; Terminating ProjectileLaunchEvent", true, true);
				return;
			}
			if (fastbowCheckDisabledMinigames.contains($.getCurrentMinigame(player)))
				return;
			if (ignoredPlayers.containsKey(player.getUniqueId())) {
				if (Server.getPluginDebug())
					log(player, player.getName() + " is currently ignored; Terminating ProjectileLaunchEvent", true, true);
				return;
			}
			if (shootDelay.contains(player.getUniqueId())) {
				event.setCancelled(true);
				log(player, player.getName() + " launched projectiles faster then normal ( > 1 in " + shootDelayTime + " ticks )");
			} else {
				shootDelay.add(player.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						shootDelay.remove(player.getUniqueId());
					}
				}, shootDelayTime);
			}
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if (event.getEntity() instanceof Player) {
			Player damagee = (Player) event.getEntity();
			disableFor(damagee, 2000);
		}
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			boolean lackWeapon = false;
			if (damager.getInventory().getItemInMainHand() == null || damager.getInventory().getItemInMainHand().getType() == Material.AIR) {
				event.setDamage(lackWeaponDamage);
				lackWeapon = true;
			} else if (!Directory.repairableItems.contains(damager.getInventory().getItemInMainHand().getType())) {
				event.setDamage(lackWeaponDamage);
				lackWeapon = true;
			}
			if (!lackWeapon && !(event.getEntity() instanceof Player))
				event.setDamage(event.getDamage() * withWeaponDamageMultiplierEntity);
		}
		if (event.getDamager() instanceof Arrow)
			event.setDamage(event.getDamage() * 0.5);
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player damagee = (Player) event.getEntity();
			Player damager = (Player) event.getDamager();
			if (Server.getPluginDebug())
				log(damagee, damagee.getName() + " has triggered EntityDamageByEntityEvent", true, true);
			if (damager.isOp()) {
				if (Server.getPluginDebug())
					log(damager, damager.getName() + " is currently opped; Terminating EntityDamageByEntityEvent", true, true);
				return;
			}
			if (rangeCheckDisabledMinigames.contains($.getCurrentMinigame(damager)))
				return;
			if (ignoredPlayers.containsKey(damager.getUniqueId())) {
				if (Server.getPluginDebug())
					log(damager, damager.getName() + " is currently ignored; Terminating EntityDamageByEntityEvent", true, true);
				return;
			}
			event.setDamage(event.getDamage() * withWeaponDamageMultiplierPlayer);
			if (damagee.getLocation().distance(damager.getLocation()) > maxAttackRange) {
				log(damager, damager.getName() + " has attacked with a greater range then normal ( > " + maxAttackRange + " blocks )");
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		if (event.isCancelled())
			return;
		Player player = event.getPlayer();
		if (player.isInsideVehicle() || player.isGliding() || player.isOp())
			return;
		if (speedCheckDisabledMinigames.contains($.getCurrentMinigame(player)))
			return;
		if (ignoredPlayers.containsKey(player.getUniqueId()))
			return;
		double maxSpeed = maxDistanceMoved;
		if (player.getGameMode() == GameMode.CREATIVE && player.isFlying())
			maxSpeed = Math.round((maxSpeed * 1.5) * 100.0) / 100.0;
		Location from = event.getFrom().clone();
		Location to = event.getTo().clone();
		if (player.isOnGround())
			maxSpeed = maxDistanceMovedFloor;
		if (player.hasPotionEffect(PotionEffectType.SPEED))
			maxSpeed += (player.getPotionEffect(PotionEffectType.SPEED).getAmplifier() + 1) * 0.14;
		if (from.getY() - to.getY() < maxVerticalBlockChange && !player.isInsideVehicle() && !player.isGliding()) {
			if (!(player.getGameMode() == GameMode.CREATIVE)) {
				log(player, player.getName() + " has moved with a greater speed then normal ( < " + maxVerticalBlockChange + " blocks )");
				event.setCancelled(true);
			}
		}
		from.setY(0);
		to.setY(0);
		if (from.distance(to) > maxSpeed) {
			log(player, player.getName() + " has moved with a greater speed then normal ( > " + maxSpeed + " blocks )");
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if (Server.getPluginDebug())
			log(player, player.getName() + " has triggered PlayerQuitEvent", true, true);
		if (lastPlayerLocation.containsKey(player.getUniqueId()))
			lastPlayerLocation.remove(player.getUniqueId());
	}

	public boolean onBlockBreak(Block block, Entity entity) {
		boolean bypass = false;
		if (!(entity == null) && entity instanceof Player)
			bypass = ((Player) entity).isOp();
		ServerMinigame minigame = $.getMinigameFromWorld(block.getWorld());
		if (!bypass && (minigame == ServerMinigame.SURVIVAL || minigame == ServerMinigame.FACTIONS)) {
			Location spawnLocation = $.getZoneLocation(block.getWorld().getName());
			if (!(block.getWorld().getEnvironment() == Environment.THE_END) && block.getLocation().distance(spawnLocation) < 20) {
				return true;
			}
		}
		return false;
	}

	public boolean onBlockPlace(Block block, Entity entity) {
		boolean bypass = false;
		if (!(entity == null) && entity instanceof Player)
			bypass = ((Player) entity).isOp();
		ServerMinigame minigame = $.getMinigameFromWorld(block.getWorld());
		if (!bypass && (minigame == ServerMinigame.SURVIVAL || minigame == ServerMinigame.FACTIONS)) {
			Location spawnLocation = $.getZoneLocation(block.getWorld().getName());
			if (!(block.getWorld().getEnvironment() == Environment.THE_END) && block.getLocation().distance(spawnLocation) < 20) {
				return true;
			}
		}
		return false;
	}

	public String processAntiSwear(OfflinePlayer player, String message) {
		char[] messageChars = message.toCharArray();
		boolean detectedSwearing = false;
		for (String swear : swearWords) {
			int beginIndex = message.indexOf(swear);
			if (beginIndex > -1) {
				int endIndex = beginIndex + swear.length();
				for (int i = beginIndex; i < endIndex; i++) {
					messageChars[i] = '*';
				}
				detectedSwearing = true;
			}
		}
		String modifiedMessage = new String(messageChars);
		if (detectedSwearing) {
			Logger.info($.italicGray + ChatColor.stripColor(message));
			if (player.isOnline())
				player.getPlayer().sendMessage(ChatColor.RED + "Please do not swear, otherwise action will be taken.");
		}
		return modifiedMessage;
	}

	public class AntiAfk extends AntiCheat implements Runnable {

		public ConcurrentHashMap<UUID, LastLocation> lastPlayerLocation = new ConcurrentHashMap<>();
		public ConcurrentHashMap<UUID, Double> lackingActivityMinutes = new ConcurrentHashMap<>();
		public String afkKickMessage = "You have been kicked for afking too long.";

		@Override
		public void run() {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (!lastPlayerLocation.containsKey(player.getUniqueId()))
					lastPlayerLocation.put(player.getUniqueId(), new LastLocation(player, player.getLocation()));
				LastLocation lastLocation = lastPlayerLocation.get(player.getUniqueId());
				boolean hasMoved = true;
				if (lastLocation.getWorld().getName().equals(player.getWorld().getName())) {
					if (lastLocation.distance(player.getLocation()) < maxAttackRange || (lastLocation.getBlock().isLiquid() && player.getLocation().getBlock().isLiquid()) || (lastLocation.isInsideVehicle() && player.isInsideVehicle())) {
						if (!lackingActivityMinutes.containsKey(player.getUniqueId()))
							lackingActivityMinutes.put(player.getUniqueId(), 0.0);
						double lastMinutes = lackingActivityMinutes.get(player.getUniqueId());
						double newMinutes = lastMinutes + 0.5;
						lackingActivityMinutes.put(player.getUniqueId(), newMinutes);
						if (newMinutes >= 5.0 && newMinutes < 15.0) {
							Server.getPlaytimeManager().handle_QuitEvent(player);
						} else if (newMinutes > 30.0) {
							lackingActivityMinutes.remove(player.getUniqueId());
							player.playSound(player.getEyeLocation(), Sound.ENTITY_CHICKEN_EGG, 1F, 1F);
							Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									player.kickPlayer(afkKickMessage);
									for (Player pl : Server.getPlugin().getServer().getOnlinePlayers()) {
										pl.sendMessage($.italicGray + "Server: Kicked " + player.getName() + " '" + afkKickMessage + "'");
									}
								}
							}, 20L);
						}
						hasMoved = false;
					}
				}
				if (!lackingActivityMinutes.containsKey(player.getUniqueId()) || (lackingActivityMinutes.containsKey(player.getUniqueId()) && lackingActivityMinutes.get(player.getUniqueId()) < 5.0)) {
					Server.getPlaytimeManager().handle_JoinEvent(player);
				}
				if (hasMoved) {
					if (lackingActivityMinutes.containsKey(player.getUniqueId()))
						lackingActivityMinutes.remove(player.getUniqueId());
				}
				lastPlayerLocation.put(player.getUniqueId(), new LastLocation(player, player.getLocation()));
			}
		}
	}
}
