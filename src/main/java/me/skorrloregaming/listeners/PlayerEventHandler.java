package me.skorrloregaming.listeners;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.destroystokyo.paper.event.player.PlayerLaunchProjectileEvent;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.factions.shop.LaShoppeEnchant;
import me.skorrloregaming.factions.shop.LaShoppeFrame;
import me.skorrloregaming.factions.shop.LaShoppeItem;
import me.skorrloregaming.impl.*;
import me.skorrloregaming.skins.model.SkinModel;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Sign;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import io.netty.buffer.Unpooled;
import me.skorrloregaming.$;
import me.skorrloregaming.ComplexParticle;
import me.skorrloregaming.CraftExplosion;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Directory;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import me.skorrloregaming.SessionManager;
import me.skorrloregaming.SessionManager.Session;
import me.skorrloregaming.SignShop;
import me.skorrloregaming.SolidStorage;
import me.skorrloregaming.commands.TrailsCmd;
import me.skorrloregaming.commands.UpgradeKitCmd;
import me.skorrloregaming.events.PlayerAuthenticateEvent;
import me.skorrloregaming.impl.Switches.SwitchIntDouble;
import me.skorrloregaming.impl.Switches.SwitchUUIDString;
import me.skorrloregaming.runnable.CombatTimer;
import me.skorrloregaming.scoreboard.DisplayType;
import me.skorrloregaming.scoreboard.DisposableScoreboard;
import me.skorrloregaming.scoreboard.boards.Kitpvp_LeaderboardScoreboard;
import me.skorrloregaming.scoreboard.boards.Kitpvp_StatisticsScoreboard;

public class PlayerEventHandler implements Listener {

	private ConcurrentMap<Player, ItemStack> storedItem = new ConcurrentHashMap<Player, ItemStack>();

	public int rainbowIndex = 0;

	public PlayerEventHandler() {

		Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if ($.isAuthenticated(player) && $.getCurrentMinigame(player) == ServerMinigame.HUB)
						Server.getInstance().fetchLobby(player);
					String message = "   Thank you for playing on the server, please invite your friends. ";
					message += message.substring(0, 32);
					Server.getBarApiTitleIndex().putIfAbsent(player.getUniqueId(), 0);
					int index = Server.getBarApiTitleIndex().get(player.getUniqueId());
					if (message.length() <= 32) {
						Server.getBarApi().setMessage(player, message, BarColor.RED, BarStyle.SOLID);
					} else {
						int finalIndex = index + 32;
						if (index < message.length() && finalIndex < message.length()) {
							Server.getBarApi().setMessage(player, message.substring(index, finalIndex), BarColor.RED, BarStyle.SOLID);
							Server.getBarApiTitleIndex().put(player.getUniqueId(), index + 1);
						} else {
							Server.getBarApiTitleIndex().put(player.getUniqueId(), 0);
						}
					}
				}
			}
		}, 7L, 7L);
		Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					ServerMinigame minigame = $.getCurrentMinigame(player);
					DisposableScoreboard scoreboard = $.getPrimaryScoreboard(minigame);
					if (!(scoreboard == null)) {
						if ($.scoreboardAutoUpdateMinigames.contains(minigame.toString().toLowerCase())) {
							scoreboard.schedule(player);
						}
					}
					/*rainbowIndex++;
					rainbowIndex %= 10;
					try {
						CraftGo.Packet.DataSerializer dataSerializer = CraftGo.Packet.DataSerializer.newInstance(Unpooled.buffer());
						Object serializedData = dataSerializer.serializePacketData($.getRainbowColor(rainbowIndex) + "play.skorrloregaming.com" + ChatColor.RESET);
						Object packet = CraftGo.Packet.Payload.newInstance(CraftGo.Packet.Payload.BRAND, serializedData);
						CraftGo.Packet.sendPacket(CraftGo.Player.getPlayerConnection(player), packet);
					} catch (Exception e) {
						e.printStackTrace();
					}*/
				}
			}
		}, 5L, 5L);
	}

	public void openSurvivalServerSelectorMenu(Player player) {
		long delay = 0L;
		if (CraftGo.Player.isPocketPlayer(player)) {
			player.closeInventory();
			delay = Server.getInventoryUpdateDelay();
		}
		Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				ItemStack factions = $.createMaterial(Material.DIAMOND_SWORD, 1, ChatColor.LIGHT_PURPLE + "Factions");
				ItemStack survival = $.createMaterial(Material.STONE_PICKAXE, 1, ChatColor.LIGHT_PURPLE + "Survival");
				survival = CraftGo.ItemStack.removeAttributes(survival);
				factions = CraftGo.ItemStack.removeAttributes(factions);
				survival = $.addLore(survival, new String[]{ChatColor.GOLD + "/server survival"});
				factions = $.addLore(factions, new String[]{ChatColor.GOLD + "/server factions"});
				int invSize = 9;
				if (CraftGo.Player.isPocketPlayer(player))
					invSize = 27;
				Inventory inv = Bukkit.createInventory(null, invSize, ChatColor.DARK_PURPLE + "Server Selector (0b10)");
				int add = 0;
				if (CraftGo.Player.isPocketPlayer(player))
					add = 9;
				if ($.isMinigameEnabled(ServerMinigame.FACTIONS)) {
					inv.setItem(3 + add, factions);
				}
				if ($.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
					inv.setItem(5 + add, survival);
				}
				player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
				player.openInventory(inv);
			}
		}, delay);
	}

	public void openSpawnerUpgradeInventory(Player player, Block block) {
		if (block.getType() == Material.SPAWNER) {
			CreatureSpawner spawner = (CreatureSpawner) block.getState();
			Location loc = spawner.getLocation();
			String code = loc.getWorld().getName() + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ());
			if (!Server.getSpawnerConfig().getData().contains(code)) {
				Server.getSpawnerConfig().getData().set(code + ".upgrade", "0");
				Server.getSpawnerConfig().getData().set(code + ".selectedUpgrade", "0");
				Server.getSpawnerConfig().saveData();
			}
			int invSize = 18;
			if (CraftGo.Player.isPocketPlayer(player))
				invSize = 27;
			Inventory inventory = Bukkit.createInventory(null, invSize, ChatColor.BOLD + "Upgrade your spawner!");
			int upgradeCount = Integer.parseInt(Server.getSpawnerConfig().getData().getString(code + ".upgrade"));
			int selectedUpgrade = Integer.parseInt(Server.getSpawnerConfig().getData().getString(code + ".selectedUpgrade"));
			int requiredAmount = 1200 * (upgradeCount + 1);
			if (requiredAmount > 4800)
				requiredAmount = 4800;
			String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
			ItemStack performUpgradeItem = $.createMaterial(Material.REDSTONE, prefix + "Perform Upgrade (" + ChatColor.RED + "$" + requiredAmount + prefix + ")");
			ItemStack spawnerItem = CraftGo.MobSpawner.newSpawnerItem(spawner.getSpawnedType(), 1);
			List<String> spawnerItemLore = new ArrayList<String>();
			spawnerItemLore.add("");
			spawnerItemLore.add(ChatColor.RESET + "code: " + code);
			spawnerItemLore.add(ChatColor.RESET + "world: " + spawner.getWorld().getName());
			spawnerItemLore.add(ChatColor.RESET + "x: " + spawner.getX());
			spawnerItemLore.add(ChatColor.RESET + "y: " + spawner.getY());
			spawnerItemLore.add(ChatColor.RESET + "z: " + spawner.getZ());
			spawnerItem = $.appendLore(spawnerItem, spawnerItemLore.toArray(new String[0]));
			inventory.setItem(0, spawnerItem);
			inventory.setItem(4, performUpgradeItem);
			int passes = -1;
			for (int i = 9; i < 18; i += 2) {
				passes++;
				List<String> lore = new ArrayList<String>();
				ItemStack item;
				if (upgradeCount >= passes) {
					item = $.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select spawner upgrade #" + passes);
					if (passes == selectedUpgrade) {
						if (CraftGo.Player.getProtocolVersion(player) > 314) {
							item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
						} else {
							item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
						}
						lore.add(ChatColor.RESET + "This is already your preferred upgrade.");
					}
				} else {
					item = $.createMaterial(Material.IRON_BARS, prefix + "This spawner upgrade is locked :(");
				}
				lore.add(ChatColor.GREEN + "Spawning rate " + ChatColor.RED + "+" + (passes * 15) + "%");
				if (lore.size() > 0)
					item = $.addLore(item, lore.toArray(new String[0]));
				inventory.setItem(i, item);
			}
			player.openInventory(inventory);
		}
	}

	public void openSkyfightTeamSelectionInventory(Player player) {
		if (!Server.getSkyfight().containsKey(player.getUniqueId()))
			return;
		$.Skyfight.Player sfPlayer = Server.getSkyfight().get(player.getUniqueId());
		int invSize = 18;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 27;
		Inventory inventory = Bukkit.createInventory(null, invSize, "Select your preferred team.");
		String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
		ItemStack a = $.createMaterial(Material.REDSTONE, prefix + "Select the " + ChatColor.ITALIC + "No Team " + prefix + "team.");
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.NO_TEAM) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				a = $.addEnchant(a, new EnchantInfo(Enchantment.BINDING_CURSE, 1));
			} else {
				a = $.addEnchant(a, new EnchantInfo(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
			}
			a = $.addLore(a, new String[]{ChatColor.RESET + "This is already your preferred team."});
		}
		ItemStack b = $.addLeatherColor($.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select the " + ChatColor.BLUE + "Blue " + prefix + "team."), Color.BLUE);
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.BLUE) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				b = $.addEnchant(b, new EnchantInfo(Enchantment.BINDING_CURSE, 1));
			} else {
				b = $.addEnchant(b, new EnchantInfo(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
			}
			b = $.addLore(b, new String[]{ChatColor.RESET + "This is already your preferred team."});
		}
		ItemStack c = $.addLeatherColor($.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select the " + ChatColor.RED + "Red " + prefix + "team."), Color.RED);
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.RED) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				c = $.addEnchant(c, new EnchantInfo(Enchantment.BINDING_CURSE, 1));
			} else {
				c = $.addEnchant(c, new EnchantInfo(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
			}
			c = $.addLore(c, new String[]{ChatColor.RESET + "This is already your preferred team."});
		}
		ItemStack d = $.addLeatherColor($.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select the " + ChatColor.GREEN + "Green " + prefix + "team."), Color.GREEN);
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.GREEN) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				d = $.addEnchant(d, new EnchantInfo(Enchantment.BINDING_CURSE, 1));
			} else {
				d = $.addEnchant(d, new EnchantInfo(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
			}
			d = $.addLore(d, new String[]{ChatColor.RESET + "This is already your preferred team."});
		}
		ItemStack e = $.addLeatherColor($.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select the " + ChatColor.YELLOW + "Yellow " + prefix + "team."), Color.YELLOW);
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.YELLOW) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				e = $.addEnchant(e, new EnchantInfo(Enchantment.BINDING_CURSE, 1));
			} else {
				e = $.addEnchant(e, new EnchantInfo(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
			}
			e = $.addLore(e, new String[]{ChatColor.RESET + "This is already your preferred team."});
		}
		ItemStack f = $.addLeatherColor($.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select the " + ChatColor.LIGHT_PURPLE + "Pink " + prefix + "team."), Color.fromRGB(255, 105, 180));
		if (sfPlayer.getTeamValue() == $.Skyfight.Team.PINK) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				f = $.addEnchant(f, new EnchantInfo(Enchantment.BINDING_CURSE, 1));
			} else {
				f = $.addEnchant(f, new EnchantInfo(Enchantment.PROTECTION_ENVIRONMENTAL, 1));
			}
			f = $.addLore(f, new String[]{ChatColor.RESET + "This is already your preferred team."});
		}
		inventory.setItem(4, a);
		inventory.setItem(9, b);
		inventory.setItem(11, c);
		inventory.setItem(13, d);
		inventory.setItem(15, e);
		inventory.setItem(17, f);
		Server.getSkyfight().get(player.getUniqueId()).setHasTeamSelectionGuiOpen(true);
		Server.getSkyfight().get(player.getUniqueId()).setScore(0);
		player.openInventory(inventory);
	}

	@EventHandler
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (item.getType() == Material.POTION) {
			if (item.hasItemMeta() && event.getItem().getItemMeta().hasLore()) {
				if (item.getItemMeta().getDisplayName().contains("Monster Vial")) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 600, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 900, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 600, 1));
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 600, 1));
					TitleSubtitle tst = new TitleSubtitle(null, "Monster vials are supposed to be used on mobs, not players.");
					CraftGo.Player.sendTimedTitleAndSubtitle(player, tst);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		if (!$.isAuthenticated(player))
			return;
		if (event.getRightClicked() instanceof ArmorStand) {
			ArmorStand entity = (ArmorStand) event.getRightClicked();
			if (Server.getConfirmUnregisterNpc().contains(player.getUniqueId())) {
				event.setCancelled(true);
				if (Server.getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString())) {
					Server.getNpcConfig().getData().set("npc." + entity.getUniqueId().toString(), null);
					Server.getNpcConfig().saveData();
					entity.remove();
					player.sendMessage($.Legacy.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "The custom npc has been removed.");
					event.setCancelled(true);
				}
			} else if (Server.getNpcConfig().getData().contains("npc." + entity.getUniqueId().toString())) {
				event.setCancelled(true);
				if (!(player.getGameMode() == GameMode.CREATIVE)) {
					String data = CustomNpc.getNpcData(entity);
					if (!(data == null)) {
						event.setCancelled(true);
						switch (data) {
							case "KITPVP_UPGRADE_KIT":
								player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
								UpgradeKitCmd.openKitUpgradeInventory(player);
								return;
							case "KITPVP_TRAILS":
								player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
								TrailsCmd.openTrailManagementInventory(player);
								return;
							default:
								if (player.isOp())
									player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Invalid data was assigned to this npc.");
								return;
						}
					}
				}
			}
			return;
		}
	}

	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		ItemStack itm = player.getInventory().getItemInMainHand();
		Location playerLoc = player.getLocation();
		if (!$.isAuthenticated(player))
			return;
		if (event.getRightClicked() instanceof Player && player.isSneaking()) {
			Player targetPlayer = (Player) event.getRightClicked();
			int particleCount = 3;
			if (player.isSneaking() && $.Marriage.getPlayerMarriageId(player) > 0) {
				if ($.Marriage.getMarriedOfflinePlayer(player).getName().equals(targetPlayer.getName())) {
					particleCount = 5;
				}
			}
			ComplexParticle.HEART.count(particleCount).display(targetPlayer.getLocation());
			ComplexParticle.HEART.count(particleCount).display(player.getLocation());
			return;
		}
		if (!(itm == null) && itm.getType() == Material.POTION && itm.hasItemMeta()) {
			if (!(event.getRightClicked() instanceof Player) && event.getRightClicked() instanceof LivingEntity) {
				if (!(player.getGameMode() == GameMode.CREATIVE)) {
					if (itm.getItemMeta().getDisplayName().contains("Monster Vial") && itm.getItemMeta().hasLore()) {
						if (!Server.getDelayedTasks().contains(player.getUniqueId())) {
							event.getRightClicked().remove();
							Server.getDelayedTasks().add(player.getUniqueId());
							Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									Server.getDelayedTasks().remove(player.getUniqueId());
								}
							}, 20L);
							if (player.getGameMode() == GameMode.SURVIVAL) {
								if (itm.getAmount() < 2) {
									player.getInventory().setItemInMainHand($.createMaterial(Material.AIR));
								} else {
									itm.setAmount(itm.getAmount() - 1);
									player.getInventory().setItemInMainHand(itm);
								}
							}
							EntityType entityType = event.getRightClicked().getType();
							ItemStack mobEgg = new ItemStack(Material.valueOf(entityType.toString().replace(" ", "_") + "_SPAWN_EGG"));
							player.getInventory().addItem(mobEgg);
							player.getWorld().playSound(playerLoc, Sound.ENTITY_ITEM_PICKUP, 1, 1);
						}
					}
				}
			}
		}
		return;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (Server.getAntiCheat().antiafk.lastPlayerLocation.containsKey(player.getUniqueId()) && Server.getAntiCheat().antiafk.lastPlayerLocation.get(player.getUniqueId()).getWorld().getName().equals(player.getWorld().getName())) {
			if (!player.isInsideVehicle() && Server.getAntiCheat().antiafk.lastPlayerLocation.get(player.getUniqueId()).distance(player.getLocation()) > 1.25) {
				if (Server.getAntiCheat().antiafk.lackingActivityMinutes.containsKey(player.getUniqueId()))
					Server.getAntiCheat().antiafk.lackingActivityMinutes.remove(player.getUniqueId());
			}
		}
		if (event.getHand() == EquipmentSlot.OFF_HAND) {
			return;
		}
		if (!$.isAuthenticated(player)) {
			return;
		}
		ItemStack itm = event.getItem();
		String subDomain = $.getMinigameDomain(player);
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			if (type == Material.SIGN || type == Material.WALL_SIGN) {
				String tag = $.getMinigameTag(subDomain);
				Sign sign = (Sign) event.getClickedBlock().getState();
				Block block = event.getClickedBlock();
				Location blockLoc = block.getLocation();
				if (!(itm == null) && itm.hasItemMeta() && itm.getItemMeta().hasLore()) {
					String[] lore = itm.getItemMeta().getLore().toArray(new String[0]);
					if (lore[0].equals("Activation Wand") && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
						String[] s = sign.getLines();
						for (int i = 0; i < s.length; i++) {
							s[i] = ChatColor.stripColor(s[i]);
						}
						if (s[0].equals("Buy") || s[0].equals("Sell") || s[0].equals("Enchant") || s[0].equals("Repair") || s[1].equals("Kit")) {
							if (!player.isOp()) {
								player.sendMessage($.Legacy.tag + ChatColor.RED + "You do not have permission to activate this shop.");
								$.playLackPermissionMessage(player);
								event.setCancelled(true);
								return;
							}
							DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
							String code = blockLoc.getWorld().getName() + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
							Server.getSignConfig().getData().set("signs." + code, 1);
							Server.getSignConfig().saveData();
							player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Successfully processed shop " + ChatColor.RED + code + ChatColor.GRAY + ".");
							player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Type: " + ChatColor.RED + s[0] + " Shop");
							player.sendMessage($.Legacy.tag + ChatColor.GRAY + "ID: " + ChatColor.RED + s[1]);
							player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Price: " + ChatColor.RED + "$" + s[2]);
							player.sendMessage($.Legacy.tag + ChatColor.GRAY + ": " + ChatColor.RED + s[3]);
							if (ChatColor.stripColor(s[1]).equals("Kit")) {
								sign.setLine(1, ChatColor.DARK_BLUE + s[1]);
								sign.update();
							} else {
								sign.setLine(0, ChatColor.DARK_BLUE + s[0]);
								sign.update();
							}
							if (ChatColor.stripColor(s[0]).equals("Buy") || ChatColor.stripColor(s[0]).equals("Sell")) {
								String materialName = ChatColor.stripColor(s[1]).toUpperCase().replace(" ", "_").split(":")[0];
								if (Material.getMaterial(materialName) == null) {
									Material legacyMaterial;
									if (!((legacyMaterial = Material.matchMaterial(materialName, true)) == null)) {
										sign.setLine(1, $.formatMaterial(legacyMaterial));
										sign.update();
									}
								}
							}
							if (!ChatColor.stripColor(s[1]).equals("Kit") && !s[2].startsWith("$")) {
								sign.setLine(2, "$" + formatter.format(Integer.parseInt(s[2])));
								sign.update();
							}
							return;
						}
					}
				}
				if (Server.getSignEditParam().containsKey(player)) {
					SignInfo info = Server.getSignEditParam().get(player);
					int rank = $.getRankId(player);
					if (player.isOp() || rank > -1 || rank < -2) {
						info.setText(ChatColor.translateAlternateColorCodes('&', info.getText()));
					}
					sign.setLine(info.getLine(), info.getText());
					sign.update();
					Server.getSignEditParam().remove(player);
					player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Global sign information updated.");
					return;
				}
				if (ChatColor.stripColor(sign.getLines()[0]).equals("[Shop]")) {
					String code = null;
					String[] s = sign.getLines();
					int x = 0, y = 0, z = 0;
					try {
						String[] split = s[1].split(",");
						x = Integer.parseInt(split[0]);
						y = Integer.parseInt(split[1]);
						z = Integer.parseInt(split[2]);
						code = blockLoc.getWorld().getName() + (x + "") + (y + "") + (z + "");
					} catch (Exception ex) {
						player.sendMessage("Failed to parse shop code on line 2 of the sign.");
						return;
					}
					if (!Server.getSignConfig().getData().contains("signs." + code)) {
						player.sendMessage("The shop code is invalid or target shop has not been activated.");
						return;
					}
					Block targetBlock = player.getWorld().getBlockAt(x, y, z);
					if (targetBlock.getType() == Material.SIGN || targetBlock.getType() == Material.SIGN || targetBlock.getType() == Material.WALL_SIGN) {
						Sign targetSign = (Sign) targetBlock.getState();
						SignShop.handle(targetSign, player, subDomain);
						return;
					} else {
						player.sendMessage("The shop code is invalid or target shop has not been activated.");
						return;
					}
				}
				for (int i = 0; i < sign.getLines().length; i++) {
					String line = String.valueOf(sign.getLines()[i]);
					if (line.startsWith(ChatColor.BOLD + "/")) {
						player.performCommand(line.substring(line.indexOf("/") + 1));
						return;
					}
				}
				SignShop.handle(sign, player, subDomain);
				return;
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (itm == null || itm.getType() == Material.AIR)) {
			Block block = event.getClickedBlock();
			if (Directory.stairs.contains(event.getClickedBlock().getType()) && $.validStairSeatMinigames.contains($.getMinigameDomain(player))) {
				Entity chair = block.getWorld().spawnEntity(player.getLocation(), EntityType.ARROW);
				chair.teleport(block.getLocation().add(0.5D, 0.2D, 0.5D));
				chair.addPassenger(player);
				chair.setGravity(false);
				event.setCancelled(true);
				return;
			}
			if (event.getClickedBlock().getType() == Material.SPAWNER) {
				if ($.isWithinUnclaimedLand(event.getClickedBlock().getLocation(), player)) {
					openSpawnerUpgradeInventory(player, event.getClickedBlock());
					return;
				}
			}
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Block block = event.getClickedBlock();
			if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN) {
				Sign sign = (Sign) block.getState();
				Location blockLoc = sign.getLocation();
				String code = String.valueOf(blockLoc.getBlockX()) + ";" + String.valueOf(blockLoc.getBlockY()) + ";" + String.valueOf(blockLoc.getBlockZ());
				if (ChatColor.stripColor(sign.getLine(0)).equals("Sell")) {
					if (!Server.getSignConfig().getData().contains("signs." + blockLoc.getWorld().getName() + code.replace(";", ""))) {
						return;
					} else {
						SignShop.playShopTitlePopup(player, block);
						Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BOLD + "Virtual Store [" + code + "]");
						player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
						player.openInventory(inv);
						return;
					}
				} else if (ChatColor.stripColor(sign.getLine(0)).equals("[Shop]")) {
					String targetCode = null;
					String[] s = sign.getLines();
					int x = 0, y = 0, z = 0;
					try {
						String[] split = s[1].split(",");
						x = Integer.parseInt(split[0]);
						y = Integer.parseInt(split[1]);
						z = Integer.parseInt(split[2]);
						targetCode = blockLoc.getWorld().getName() + (x + "") + (y + "") + (z + "");
					} catch (Exception ex) {
						player.sendMessage("Failed to parse shop code on line 2 of the sign.");
						return;
					}
					if (!Server.getSignConfig().getData().contains("signs." + targetCode)) {
						player.sendMessage("The shop code is invalid or target shop has not been activated.");
						return;
					}
					Block targetBlock = player.getWorld().getBlockAt(x, y, z);
					if (targetBlock.getType() == Material.SIGN || targetBlock.getType() == Material.SIGN || targetBlock.getType() == Material.WALL_SIGN) {
						Sign targetSign = (Sign) targetBlock.getState();
						if (ChatColor.stripColor(targetSign.getLines()[0]).equals("Sell")) {
							SignShop.playShopTitlePopup(player, block);
							Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BOLD + "Virtual Store [" + x + ";" + y + ";" + z + "]");
							player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
							player.openInventory(inv);
						}
						return;
					} else {
						player.sendMessage("The shop code is invalid or target shop has not been activated.");
						return;
					}
				}
				return;
			}
		}
		if (!(itm == null) && itm.hasItemMeta()) {
			if (Server.getSkyfight().containsKey(player.getUniqueId())) {
				if (itm.getType() == Material.LEATHER_CHESTPLATE) {
					if (!Server.getDelayedTasks().contains(event.getPlayer().getUniqueId())) {
						Server.getDelayedTasks().add(event.getPlayer().getUniqueId());
						Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
							@Override
							public void run() {
								Server.getDelayedTasks().remove(event.getPlayer().getUniqueId());
							}
						}, 20L);
						openSkyfightTeamSelectionInventory(player);
						return;
					}
				}
			} else if (Server.getHub().contains(player.getUniqueId()) && itm.getItemMeta().hasDisplayName()) {
				if (itm.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Server Selector")) {
					ItemStack skyfight = $.createMaterial(Material.BOW, 1, ChatColor.LIGHT_PURPLE + "SkyFight");
					ItemStack kitpvp = $.createMaterial(Material.IRON_SWORD, 1, ChatColor.LIGHT_PURPLE + "KitPvP");
					ItemStack survivalFactions = $.createMaterial(Material.STONE_PICKAXE, 1, ChatColor.LIGHT_PURPLE + "Survival / Factions");
					ItemStack creative = $.createMaterial(Material.GRASS_BLOCK, 1, ChatColor.LIGHT_PURPLE + "Creative");
					ItemStack skyblock = $.createMaterial(Material.OAK_SAPLING, 1, ChatColor.LIGHT_PURPLE + "Skyblock");
					kitpvp = CraftGo.ItemStack.removeAttributes(kitpvp);
					survivalFactions = CraftGo.ItemStack.removeAttributes(survivalFactions);
					skyfight = $.addLore(skyfight, new String[]{ChatColor.GOLD + "/server skyfight"});
					kitpvp = $.addLore(kitpvp, new String[]{ChatColor.GOLD + "/server kitpvp"});
					creative = $.addLore(creative, new String[]{ChatColor.GOLD + "/server creative"});
					skyblock = $.addLore(skyblock, new String[]{ChatColor.GOLD + "/server skyblock"});
					ItemStack factions = $.createMaterial(Material.DIAMOND_SWORD, 1, ChatColor.LIGHT_PURPLE + "Factions");
					ItemStack survival = $.createMaterial(Material.STONE_PICKAXE, 1, ChatColor.LIGHT_PURPLE + "Survival");
					survival = CraftGo.ItemStack.removeAttributes(survival);
					factions = CraftGo.ItemStack.removeAttributes(factions);
					survival = $.addLore(survival, new String[]{ChatColor.GOLD + "/server survival"});
					factions = $.addLore(factions, new String[]{ChatColor.GOLD + "/server factions"});
					int invSize = 9;
					if (CraftGo.Player.isPocketPlayer(player))
						invSize = 27;
					Inventory inv = Bukkit.createInventory(null, invSize, ChatColor.DARK_PURPLE + "Server Selector (0b1)");
					int add = 0;
					if (CraftGo.Player.isPocketPlayer(player))
						add = 9;
					if ($.isMinigameEnabled(ServerMinigame.KITPVP)) {
						inv.setItem(0 + add, kitpvp);
					}
					if ($.isMinigameEnabled(ServerMinigame.FACTIONS) && $.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
						inv.setItem(2 + add, survivalFactions);
					} else if ($.isMinigameEnabled(ServerMinigame.FACTIONS)) {
						factions.setType(Material.STONE_PICKAXE);
						inv.setItem(2 + add, factions);
					} else if ($.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
						inv.setItem(2 + add, survival);
					}
					if ($.isMinigameEnabled(ServerMinigame.SKYFIGHT)) {
						inv.setItem(4 + add, skyfight);
					}
					if ($.isMinigameEnabled(ServerMinigame.CREATIVE)) {
						inv.setItem(6 + add, creative);
					}
					if ($.isMinigameEnabled(ServerMinigame.SKYBLOCK)) {
						inv.setItem(8 + add, skyblock);
					}
					player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
					player.openInventory(inv);
					return;
				}
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (!(itm == null) && !(itm.getType() == Material.AIR)) {
				if (itm.getType() == Material.BOW && Server.getKitpvp().contains(player.getUniqueId()) && !player.getInventory().contains(Material.ARROW)) {
					if (storedItem.containsKey(player)) {
						return;
					}
					ItemStack item = player.getInventory().getItem(9);
					storedItem.put(player, item);
					player.getInventory().setItem(9, new ItemStack(Material.ARROW, 1));
					return;
				}
				if (itm.getType() == Material.FIRE_CHARGE) {
					if (player.getGameMode() == GameMode.SURVIVAL && !Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
						if (!Server.getDelayedTasks().contains(event.getPlayer().getUniqueId())) {
							Server.getDelayedTasks().add(event.getPlayer().getUniqueId());
							Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {

								@Override
								public void run() {
									Server.getDelayedTasks().remove(event.getPlayer().getUniqueId());
								}
							}, 20L);
							event.setCancelled(true);
							if (itm.getAmount() < 2) {
								player.getInventory().setItemInMainHand($.createMaterial(Material.AIR));
							} else {
								itm.setAmount(itm.getAmount() - 1);
								player.getInventory().setItemInMainHand(itm);
							}
							player.launchProjectile(SmallFireball.class);
						}
					}
					return;
				}
				if (itm.getType() == Material.ENDER_EYE && itm.hasItemMeta() && Server.getFactions().contains(player.getUniqueId())) {
					if (itm.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "End Portal")) {
						World w = Bukkit.getWorld($.getMinigameDomain(player) + "_end");
						if (w == null) {
							return;
						}
						event.setCancelled(true);
						if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
							player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this device during combat.");
							return;
						}
						Location exactLoc = new Location(w, 0.5, 0, 0.5);
						exactLoc.setY(w.getHighestBlockYAt(exactLoc));
						if (exactLoc.getY() < 1 && exactLoc.getY() > -1) {
							player.sendMessage("Aborted. Failed to find a safe spawn location.");
							return;
						}
						$.teleport(player, exactLoc);
						w.playSound(exactLoc, Sound.BLOCK_PORTAL_TRAVEL, 1, 1);
						if ($.isEffectsEnabled(player)) {
							for (int i = 0; i < 360; i += 2) {
								Location flameloc = exactLoc;
								double cos = Math.cos(i) * 2;
								double sin = Math.sin(i) * 2;
								double x = flameloc.getZ() - cos - cos + cos;
								double z = flameloc.getX() - sin - sin + sin;
								player.getWorld().spawnParticle(Particle.PORTAL, x, flameloc.getY() - 1, z, 5);
							}
						}
						return;
					}
				}
				if (itm.getType() == Material.GUNPOWDER && Server.getFactions().contains(player.getUniqueId())) {
					if (itm.hasItemMeta() && itm.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "Explosive Gunpowder")) {
						if (player.getGameMode() == GameMode.SURVIVAL && !Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
							if (!Server.getDelayedTasks().contains(event.getPlayer().getUniqueId())) {
								Server.getDelayedTasks().add(event.getPlayer().getUniqueId());
								Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
									@Override
									public void run() {
										Server.getDelayedTasks().remove(event.getPlayer().getUniqueId());
									}
								}, 20L);
								if (itm.getAmount() < 2) {
									player.getInventory().setItemInMainHand($.createMaterial(Material.AIR));
								} else {
									itm.setAmount(itm.getAmount() - 1);
									player.getInventory().setItemInMainHand(itm);
								}
								player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
								CraftExplosion explosion = new CraftExplosion(player.getLocation(), 0.4F, false);
								explosion.explodeNaturally();
								Server.getAntiCheat().handleVelocity(player, player.getLocation().getDirection().multiply(5.0));
							}
						}
						return;
					} else {
						if (player.getGameMode() == GameMode.SURVIVAL && !Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
							if (itm.getAmount() < 2) {
								player.getInventory().setItemInMainHand($.createMaterial(Material.AIR));
							} else {
								itm.setAmount(itm.getAmount() - 1);
								player.getInventory().setItemInMainHand(itm);
							}
							player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
							Server.getAntiCheat().handleVelocity(player, player.getLocation().getDirection().multiply(2.5));
						}
						return;
					}
				}
				if (itm.hasItemMeta() && itm.getItemMeta().hasLore()) {
					String[] lore = itm.getItemMeta().getLore().toArray(new String[0]);
					if (lore[0].equals("Using this wand, you will gain defined potion effects.")) {
						if (itm.getAmount() <= 1) {
							player.getInventory().setItemInMainHand($.createMaterial(Material.AIR));
						} else {
							ItemStack old = new ItemStack(itm.getType(), itm.getAmount() - 1);
							player.getInventory().setItemInMainHand(old);
						}
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 2));
						player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3600, 0));
						player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3600, 0));
						player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3600, 0));
						player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3600, 0));
						player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 1);
						return;
					}
				}
			}
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material type = event.getClickedBlock().getType();
			if (type == Material.ENDER_CHEST && Server.getCreative().contains(player.getUniqueId())) {
				event.setCancelled(true);
				return;
			}
			if (!(event.getItem() == null)) {
				if ($.isSpawnEgg(itm.getType())) {
					if (Server.getCreative().contains(player.getUniqueId())) {
						event.setCancelled(true);
						return;
					} else {
						if (!(type == Material.SPAWNER)) {
							if (!Server.getDelayedTasks().contains(event.getPlayer().getUniqueId())) {
								Server.getDelayedTasks().add(event.getPlayer().getUniqueId());
								Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
									@Override
									public void run() {
										Server.getDelayedTasks().remove(event.getPlayer().getUniqueId());
									}
								}, 20L);
								String entityName = itm.getType().toString();
								entityName = entityName.substring(0, entityName.indexOf(" "));
								if (player.getGameMode() == GameMode.SURVIVAL) {
									if (itm.getAmount() < 2) {
										player.getInventory().setItemInMainHand($.createMaterial(Material.AIR));
									} else {
										itm.setAmount(itm.getAmount() - 1);
										player.getInventory().setItemInMainHand(itm);
									}
								}
								EntityType entityType = EntityType.valueOf(entityName);
								Location exactLoc = event.getClickedBlock().getLocation();
								exactLoc.setX(exactLoc.getBlockX() + 0.5);
								exactLoc.setY(exactLoc.getBlockY() + 1.0);
								exactLoc.setZ(exactLoc.getBlockZ() + 0.5);
								event.getClickedBlock().getWorld().spawnEntity(exactLoc, entityType);
							}
						}
						event.setCancelled(true);
						return;
					}
				}
				if ((player.getWorld().getEnvironment() == World.Environment.NETHER) && (event.getItem().getType() == Material.WATER_BUCKET)) {
					event.setCancelled(true);
					event.getClickedBlock().getRelative(event.getBlockFace()).setType(Material.WATER);
					if (player.getGameMode() == GameMode.SURVIVAL) {
						event.getItem().setType(Material.BUCKET);
					}
					return;
				}
			}
		}
		if (event.getAction() == Action.LEFT_CLICK_AIR) {
			if (!(itm == null) && itm.getType() == Material.ENDER_EYE && itm.hasItemMeta() && Server.getFactions().contains(player.getUniqueId())) {
				if (itm.getItemMeta().getDisplayName().equals(ChatColor.LIGHT_PURPLE + "End Portal")) {
					if (!Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
						if (!Server.getDelayedTasks().contains(event.getPlayer().getUniqueId())) {
							Server.getDelayedTasks().add(event.getPlayer().getUniqueId());
							Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {

								@Override
								public void run() {
									Server.getDelayedTasks().remove(event.getPlayer().getUniqueId());
								}
							}, 20L);
							if (player.isInsideVehicle() && player.getVehicle() instanceof EnderPearl)
								player.getVehicle().remove();
							player.launchProjectile(EnderPearl.class).addPassenger(player);

							int foodLevel = player.getFoodLevel();
							foodLevel = foodLevel - 1;
							if (foodLevel < 0)
								foodLevel = 0;
							player.setFoodLevel(foodLevel);
						}
					}
					return;
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (!event.isCancelled() && !$.isAuthenticated(player)) {
			event.setCancelled(true);
			return;
		}
		if (Server.getAntiCheat().antiafk.lackingActivityMinutes.containsKey(player.getUniqueId()))
			Server.getAntiCheat().antiafk.lackingActivityMinutes.remove(player.getUniqueId());
		if ($.getRankId(player) > -1 && Server.getStaffChatPlayers().contains(player.getUniqueId())) {
			Logger.info("[sc] " + ChatColor.stripColor(player.getDisplayName()) + ": " + event.getMessage());
			event.setCancelled(true);
			return;
		}
		if (Server.getMarriageChatPlayers().contains(player.getUniqueId()) && $.Marriage.getPlayerMarriageId(player) > 0) {
			String message = "[mc] " + player.getDisplayName().replace(player.getName(), ChatColor.LIGHT_PURPLE + player.getName()) + ChatColor.DARK_GRAY + ": " + ChatColor.RESET + event.getMessage();
			if ($.Marriage.getPlayerSwearFilter(player))
				message = Server.getAntiCheat().processAntiSwear(player, message);
			message = ChatColor.translateAlternateColorCodes('&', message);
			OfflinePlayer targetPlayer = $.Marriage.getMarriedOfflinePlayer(player);
			if (targetPlayer.isOnline()) {
				if (Server.getIgnoredPlayers().containsKey(targetPlayer.getUniqueId())) {
					Player existingIgnore = Bukkit.getPlayer(Server.getIgnoredPlayers().get(targetPlayer.getUniqueId()));
					if (!existingIgnore.getName().toString().equals(player.getName().toString())) {
						targetPlayer.getPlayer().sendMessage(message);
					}
				} else {
					targetPlayer.getPlayer().sendMessage(message);
				}
				player.sendMessage(message);
				Logger.info($.italicGray + ChatColor.stripColor(message));
			} else {
				player.sendMessage("Target player is not online to chat right now.");
				player.performCommand("marry chat");
			}
			event.setCancelled(true);
			return;
		}
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (!onlinePlayer.getName().equals(player.getName())) {
				int rankID = $.getRankId(onlinePlayer);
				if (onlinePlayer.isOp() || rankID > -1) {
					onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
				} else if (event.getMessage().contains(onlinePlayer.getName())) {
					onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
				}
			}
		}
		String world = player.getWorld().getName();
		String msg = ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.RESET + " " + '\u00BB' + " " + event.getMessage();
		if (Server.getMutedPlayers().contains(player.getUniqueId())) {
			player.sendMessage($.Legacy.tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You cannot chat while you are muted.");
			msg = ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.RESET + " " + '\u00BB' + " " + $.italicGray + event.getMessage();
		}
		msg = Server.getAntiCheat().processAntiSwear(player, msg);
		int rank = $.getRankId(player);
		if (player.isOp() || rank > -1 || rank < -2) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		Logger.info(msg, true);
		boolean isCancelled = event.isCancelled();
		event.setCancelled(true);
		if (!isCancelled) {
			boolean muted = Server.getMutedPlayers().contains(player.getUniqueId());
			if (!muted) {
				String rankName = WordUtils.capitalize($.toRankDisplayName($.getRank(player)));
				if (rankName.equals("Youtube"))
					rankName = "YouTube";
				if ($.isPrefixedRankingEnabled()) {
					Server.getDiscordBot().broadcast(
							"**" + rankName + "** " + player.getName() + " " + '\u00BB' + " " + Server.getAntiCheat().processAntiSwear(player, event.getMessage())
							, Channel.SERVER_CHAT);
				} else {
					Server.getDiscordBot().broadcast(
							"**" + player.getName() + "** " + '\u00BB' + " " + Server.getAntiCheat().processAntiSwear(player, event.getMessage())
							, Channel.SERVER_CHAT);
				}
			}
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (muted) {
					int rankID = $.getRankId(onlinePlayer);
					if (onlinePlayer.isOp() || rankID > -1) {
						onlinePlayer.sendMessage(msg);
					}
				} else {
					if (Server.getIgnoredPlayers().containsKey(onlinePlayer.getUniqueId())) {
						Player existingIgnore = Bukkit.getPlayer(Server.getIgnoredPlayers().get(onlinePlayer.getUniqueId()));
						if (!existingIgnore.getName().toString().equals(player.getName().toString())) {
							onlinePlayer.sendMessage(msg);
						}
					} else {
						onlinePlayer.sendMessage(msg);
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDismount(EntityDismountEvent event) {
		if (event.getDismounted().getType() == EntityType.ENDER_PEARL) {
			event.getDismounted().remove();
		}
		if (event.getDismounted().getType() == EntityType.ARROW) {
			event.getDismounted().remove();
		}
	}

	@EventHandler
	public void PlayerItemHeldEvent(PlayerItemHeldEvent event) {
		returnItem(event.getPlayer());
	}

	@EventHandler
	public void PlayerDropItemEvent(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE && !player.isOp()) {
			if (!Server.getDelayedTasks().contains(player.getUniqueId())) {
				player.sendMessage("Sorry, you are not allowed to drop items in creative.");
				Server.getDelayedTasks().add(player.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Server.getDelayedTasks().remove(player.getUniqueId());
					}
				}, 20L);
			}
			event.setCancelled(true);
		}
		returnItem(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void EntityShootBowEvent(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
				public void run() {
					returnItem((Player) event.getEntity());
				}
			}, 5L);
		}
	}

	private void returnItem(Player player) {
		if (storedItem.containsKey(player.getPlayer())) {
			player.getInventory().setItem(9, storedItem.get(player));
			player.updateInventory();
			storedItem.remove(player);
		}
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			if (event.getEntity().getShooter() instanceof Player) {
				Player player = (Player) event.getEntity().getShooter();
				if (!Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId())) {
					event.getEntity().remove();
				}
			} else {
				event.getEntity().remove();
			}
		}
	}

	@EventHandler
	public void onPlayerPostLogin(PlayerLoginEvent event) {
		if (event.getHostname().toLowerCase().split(":")[0].endsWith(".ml")) {
			LocalDate ld = LocalDate.of(2019, Month.JULY, 4);
			StringBuilder disallowMessage = new StringBuilder("TL;DR GENERAL INFORMATION" + '\n');
			disallowMessage.append("This address is deprecated, please use the following address." + '\n');
			disallowMessage.append("play.skorrloregaming.com" + '\n');
			disallowMessage.append("For more information you can contact the server administrators." + '\n' + '\n');
			if (ld.isAfter(LocalDate.now())) {
				String disallowMsg = disallowMessage.toString();
				event.disallow(PlayerLoginEvent.Result.KICK_OTHER, disallowMsg);
				Logger.info($.italicGray + "Server: Disallow " + event.getPlayer().getName() + " '" + disallowMsg + "'", false);
			}
		}
	}

	@EventHandler
	public void onPlayerLogin(AsyncPlayerPreLoginEvent event) {
		String address = event.getAddress().getHostAddress();
		String altAddress = null;
		if (event.getLoginResult() == Result.KICK_FULL) {
			event.setLoginResult(Result.ALLOWED);
		}
		OfflinePlayer op = CraftGo.Player.getOfflinePlayer(event.getName());
		if (op.hasPlayedBefore() || op.isOnline()) {
			String path = "config." + op.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path + ".ip")) {
				altAddress = Server.getPlugin().getConfig().getString(path + ".ip");
			}
		}
		int uuidHash = UUID.nameUUIDFromBytes(address.replace(".", "-").getBytes()).hashCode();
		System.out.println("Checking banned signature (" + address + ") ...");
		boolean proxyAddress = false;
		if (Server.getBanConfig().getData().contains(address.replace(".", "x")) || (!(altAddress == null) && Server.getBanConfig().getData().contains(altAddress.replace(".", "x"))) || (proxyAddress = CraftGo.Player.isProxyAddress(event.getAddress().getHostAddress()))) {
			System.out.println("Complete banned signature found, disallowing connection..");
			String kickMessage = null;
			if (proxyAddress) {
				IpLocationQuery query = CraftGo.Player.queryIpLocationNoCache(event.getAddress().getHostAddress());
				String uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + event.getName()).getBytes()).toString();
				if (Bukkit.getOnlineMode())
					uuid = CraftGo.Player.getUUID(event.getName(), true);
				String path = "config." + uuid + ".ip";
				String realAddress = Server.getPlugin().getConfig().getString(path, "null").replace("x", ".");
				IpLocationQuery real = CraftGo.Player.queryStoredIpLocation(realAddress);
				if (real == null)
					real = new IpLocationQuery("null", "null", "null", "null", "null", "null", null);
				kickMessage = "You are not allowed to connect to the server with a proxy." + '\n' + '\n' + "Ip Address: " + query.getEndpoint() + ",     expected: " + real.getEndpoint() + '\n' + "Country: " + query.getCountry() + ",     expected: " + real.getCountry() + '\n' + "State: " + query.getState() + ",     expected: " + real.getState() + '\n' + "City: " + query.getCity() + ",     expected: " + real.getCity() + '\n' + "Isp: " + query.getIsp() + ",     expected: " + real.getIsp();
			} else if (Server.getBanConfig().getData().contains(address.replace(".", "x"))) {
				kickMessage = Server.getBanConfig().getData().getString(address.replace(".", "x"));
			} else {
				kickMessage = Server.getBanConfig().getData().getString(altAddress.replace(".", "x"));
			}
			event.disallow(Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', kickMessage));
			boolean consoleOnly = false;
			if (Server.getHideLoginMessage().containsKey(Integer.valueOf(uuidHash))) {
				long epoch = Server.getHideLoginMessage().get(Integer.valueOf(uuidHash)).longValue();
				if (epoch > Instant.now().getEpochSecond()) {
					consoleOnly = true;
					Server.getHideLoginMessage().put(Integer.valueOf(uuidHash), epoch + 30);
					System.out.println("Connection was attempted within the future epoch, adding time..");
				} else {
					Server.getHideLoginMessage().put(Integer.valueOf(uuidHash), Long.valueOf(Instant.now().getEpochSecond()) + 10);
				}
			} else {
				Server.getHideLoginMessage().put(Integer.valueOf(uuidHash), Long.valueOf(Instant.now().getEpochSecond()) + 30);
			}
			Logger.info($.italicGray + "Server: Disallow " + event.getName() + " '" + kickMessage + "'", consoleOnly);
		} else {
			if (Server.getHideLoginMessage().containsKey(Integer.valueOf(uuidHash)))
				Server.getHideLoginMessage().remove(Integer.valueOf(uuidHash));
		}
	}

	@EventHandler
	public void onPlayerPreJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		for (NpcPlayer npc : Server.getNpcPlayers())
			CraftGo.Packet.PlayerInfo.spawnNpc(player, npc.getWorld(), npc.getLocation(), npc.getName());
		Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				IpLocationQuery query = CraftGo.Player.queryIpLocation(player);
				Logger.info("Defined country of " + player.getName() + ": " + query.getCountry(), true);
				Logger.info("Defined geo-location of " + player.getName() + ": " + query.getCity() + ", " + query.getState(), true);
			}
		});
		CraftGo.Player.getUUID(player.getName(), false, true);
		Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (!Bukkit.getOnlineMode()) {
					Optional<SkinModel> model = Server.getSkinStorage().getSkinData(player, true);
					Server.getSkinStorage().getFactory(player, model.get()).applySkin();
				}
			}
		});
		if ($.isAuthenticated(player)) {
			player.setWalkSpeed(0.2F);
			player.setFlySpeed(0.1F);
		} else {
			player.setWalkSpeed(0.0F);
		}
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (!onlinePlayer.getName().equals(player.getName())) {
				onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
			}
		}
		Server.getHub().add(player.getUniqueId());
		if (!Server.getUseFactionsAsHub()) {
			if (player.isDead()) {
				player.spigot().respawn();
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Location hubLocation = $.getZoneLocation("hub");
						$.teleport(player, hubLocation);
					}
				}, 10L);
			} else {
				Location hubLocation = $.getZoneLocation("hub");
				$.teleport(player, hubLocation);
			}
			$.clearPlayer(player);
		}
		if (Server.getProtoSupportPocketApi() == null)
			Server.getInstance().fetchLobby(player);
		player.setAllowFlight(true);
		Server.setLastKnownHubWorld($.getZoneLocation("hub").getWorld().getName().toString());
		if ($.isCustomJoinMessageEnabled()) {
			event.setJoinMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has joined the server.");
		}
		if (!(event.getJoinMessage() == null))
			Server.setDefaultJoinMessage(event.getJoinMessage().replace(event.getPlayer().getName(), "{player}"));
		player.performCommand("build-time");
		$.Scoreboard.clearDisplaySlot(player, DisplaySlot.SIDEBAR);
		for (Player op : Bukkit.getOnlinePlayers())
			$.Scoreboard.configureHealth(op);
		String ipAddress = player.getAddress().getAddress().getHostAddress().replace(".", "x");
		try {
			if (Server.getPlugin().getConfig().contains("warning." + ipAddress + ".count")) {
				int warningCt = Integer.parseInt(Server.getPlugin().getConfig().getString("warning." + ipAddress + ".count"));
				int lastKnownWarningCt = Server.getPlugin().getConfig().getInt("config." + player.getUniqueId().toString() + ".lastKnownWarningCt");
				if (warningCt > lastKnownWarningCt) {
					String[] messages = new String[warningCt - lastKnownWarningCt];
					ArrayList<String> quotedReasons = new ArrayList<String>();
					for (int i = 0; i < messages.length; i++) {
						messages[i] = Server.getPlugin().getConfig().getString("warning." + ipAddress + "." + (i + lastKnownWarningCt + 1));
						String quotedReason = messages[i].substring(messages[i].indexOf("\""));
						quotedReason = quotedReason.substring(0, quotedReason.lastIndexOf("\"") + 1);
						if (quotedReasons.contains(quotedReason)) {
							continue;
						}
						quotedReasons.add(quotedReason);
						String[] lines = messages[i].split("[\\r?\\n]+");
						for (int l = 0; l < lines.length; l++) {
							String line = lines[l];
							switch (l) {
								case 1:
									player.sendMessage("► " + ChatColor.BOLD + line.replace("\"", ""));
									break;
								default:
									player.sendMessage(line);
									break;
							}
						}
					}
					quotedReasons.clear();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (CraftGo.Player.getUUID(player.getName(), false) == null) {
			String message = $.italicGray + "Player " + player.getName() + " is using an offline/cracked account";
			Logger.info(message);
			Server.getDiscordBot().broadcast(
					ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
					, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
		}
		if (!$.isPluginEnabled("AuthMe")) {
			String joinMessage = Server.getDefaultJoinMessage().replace("{player}", player.getName());
			PlayerAuthenticateEvent authEvent = new PlayerAuthenticateEvent(player, joinMessage);
			Bukkit.getPluginManager().callEvent(authEvent);
		} else {
			Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
				@Override
				public void run() {
					try {
						boolean bypass = false;
						if (!(Server.getProtoSupportListener() == null))
							bypass = Server.getProtoSupportListener().isOnlineMode(player);
						if (bypass) {
							Object authObject = $.getAuthenticationSuite();
							Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									if (((fr.xephi.authme.api.v3.AuthMeApi) authObject).isRegistered(player.getName())) {
										((fr.xephi.authme.api.v3.AuthMeApi) authObject).forceLogin(player);
									} else {
										((fr.xephi.authme.api.v3.AuthMeApi) authObject).forceRegister(player, UUID.nameUUIDFromBytes(player.getName().getBytes()).toString().substring(0, 30), true);
									}
								}
							});
							return;
						}
						boolean dailyAuth = Server.getPlugin().getConfig().getBoolean("settings.enable.authme.dailyAuth");
						boolean autoLoginCmd = Server.getPlugin().getConfig().getBoolean("settings.enable.authme.autoLoginCmd");
						Object authObject = $.getAuthenticationSuite();
						if (player.getName().equalsIgnoreCase("Player")) {
							if (((fr.xephi.authme.api.v3.AuthMeApi) authObject).isRegistered(player.getName())) {
								((fr.xephi.authme.api.v3.AuthMeApi) authObject).forceLogin(player);
							} else {
								((fr.xephi.authme.api.v3.AuthMeApi) authObject).forceRegister(player, "password123", true);
							}
						} else {
							String hostAddr = player.getAddress().getAddress().getHostAddress();
							Session session = Server.getSessionManager().getStoredSession(player, hostAddr);
							if (!(session == null)) {
								long lastAccessed = session.getLastAccessed();
								Calendar cal = Calendar.getInstance();
								cal.setTimeInMillis(lastAccessed);
								int year = cal.get(Calendar.YEAR);
								int month = cal.get(Calendar.MONTH);
								int day = cal.get(Calendar.DAY_OF_MONTH);
								String message = $.italicGray + "Player " + player.getName() + " previously logged in on " + $.formatMonthIdAbbrev(month) + " " + day + $.formatDayOfMonthSuffix(day) + " " + year;
								Bukkit.broadcastMessage(message);
								Server.getDiscordBot().broadcast(
										ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
										, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
								if (Server.getSessionManager().verifySession(player) && dailyAuth) {
									if (session.isDiscarded()) {
										player.sendMessage($.italicGray + "Your session was invalidated, please login to your account again.");
									} else {
										try {
											Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), new Runnable() {
												@Override
												public void run() {
													if (((fr.xephi.authme.api.v3.AuthMeApi) authObject).isRegistered(player.getName())) {
														((fr.xephi.authme.api.v3.AuthMeApi) authObject).forceLogin(player);
														Server.getInstance().fetchLobby(player);
													}
												}
											});
											return;
										} catch (Exception ex) {
											ex.printStackTrace();
											Server.getAuthListener().onPlayerAuth(new fr.xephi.authme.events.LoginEvent(player));
										}
									}
								}
							}
							if (!Server.getPlugin().getConfig().contains("config." + player.getUniqueId().toString())) {
								String message = $.italicGray + "Player " + player.getName() + " has yet to register for the server";
								Bukkit.broadcastMessage(message);
								Server.getDiscordBot().broadcast(
										ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
										, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
							}
							if (Server.getSessionManager().getStoredSession(player, hostAddr) == null) {
								String message = $.italicGray + "Player " + player.getName() + " has yet to register new session";
								Bukkit.broadcastMessage(message);
								Server.getDiscordBot().broadcast(
										ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
										, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
							}
							if (!dailyAuth && autoLoginCmd) {
								String ip = player.getAddress().getAddress().getHostAddress().replace(".", "x");
								if (Server.getPlugin().getConfig().contains("autologin." + ip + "." + player.getUniqueId().toString())) {
									String password = Server.getPlugin().getConfig().getString("autologin." + ip + "." + player.getUniqueId().toString());
									boolean isCorrectPassword = false;
									if (!(authObject == null)) {
										isCorrectPassword = ((fr.xephi.authme.api.v3.AuthMeApi) authObject).checkPassword(player.getName(), password);
									}
									if (isCorrectPassword) {
										((fr.xephi.authme.api.v3.AuthMeApi) authObject).forceLogin(player);
										Server.getInstance().fetchLobby(player);
										return;
									} else {
										Server.getPlugin().getConfig().set("autologin." + ip + "." + player.getUniqueId().toString(), null);
									}
								}
							}
						}
						if (!(Server.getProtoSupportPocketApi() == null)) {
							Server.getProtoSupportPocketApi().onLogin(event);
						} else {
							Server.getInstance().fetchLobby(player);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}, 20);
		}
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void onPlayerAuthenticate(PlayerAuthenticateEvent event) {
		Player player = event.getPlayer();
		Server.getOnlineMode().put(player.getUniqueId(), CraftGo.Player.getOnlineMode(player));
		player.setWalkSpeed(0.2F);
		player.setFlySpeed(0.1F);
		Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				if (!Bukkit.getOnlineMode()) {
					Optional<SkinModel> model = Server.getSkinStorage().getSkinData(player, false);
					if (!model.isPresent())
						return;
					Server.getSkinStorage().getFactory(player, model.get()).applySkin();
				}
			}
		});
		String displayName = player.getDisplayName();
		String path = "config." + player.getUniqueId().toString();
		String ipAddress = player.getAddress().getAddress().getHostAddress().replace(".", "x");
		Server.getSessionManager().updateSession(player, new Session(SessionManager.encodeHex(ipAddress.replace("x", ".")).toCharArray(), player, System.currentTimeMillis()));
		if (!Server.getPlugin().getConfig().contains(path)) {
			Server.getPlugin().getConfig().set(path + ".username", displayName);
			Server.getPlugin().getConfig().set(path + ".rank", $.validRanks.get(0));
			Server.getPlugin().getConfig().set(path + ".suffixRank", $.validRanks.get(0));
			Server.getPlugin().getConfig().set(path + ".marry.marriedTo", "0");
			Server.getPlugin().getConfig().set(path + ".marry.marriageId", "0");
			Server.getPlugin().getConfig().set(path + ".marry.marriedPvp", "true");
			Server.getPlugin().getConfig().set(path + ".marry.swearFilter", "true");
			Server.getPlugin().getConfig().set(path + ".kitpvp.kills", "0");
			Server.getPlugin().getConfig().set(path + ".kitpvp.deaths", "0");
			Server.getPlugin().getConfig().set(path + ".factions.kills", "0");
			Server.getPlugin().getConfig().set(path + ".factions.deaths", "0");
			Server.getPlugin().getConfig().set(path + ".kitpvp.upgrades", "0");
			Server.getPlugin().getConfig().set(path + ".kitpvp.preferredUpgrade", "0");
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.selectedTrail", "-1");
			Server.getPlugin().getConfig().set(path + ".skyblock.broken", "0");
			Server.getPlugin().getConfig().set(path + ".skyblock.placed", "0");
			Server.getPlugin().getConfig().set(path + ".balance.kitpvp", "0");
			Server.getPlugin().getConfig().set(path + ".balance.factions", "250");
			Server.getPlugin().getConfig().set(path + ".balance.skyblock", "0");
			Server.getPlugin().getConfig().set("warning." + ipAddress + ".count", "0");
		}
		if (!Server.getPlugin().getConfig().contains(path + ".joined.value")) {
			Server.getPlugin().getConfig().set(path + ".joined.value", System.currentTimeMillis() + "");
			if (Server.getPlugin().getConfig().contains(path)) {
				Server.getPlugin().getConfig().set(path + ".joined.inaccurate", "true");
			} else {
				Server.getPlugin().getConfig().set(path + ".joined.inaccurate", "false");
			}
			String message = ChatColor.RESET + "Welcome to the server, " + ChatColor.BOLD + player.getName() + ChatColor.RESET + ".";
			Bukkit.broadcastMessage(message);
			Server.getDiscordBot().broadcast(
					ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
					, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
		}
		Server.getPlugin().getConfig().set(path + ".ip", ipAddress);
		Server.getPlugin().getConfig().set("address." + ipAddress + "." + player.getUniqueId().toString(), "0");
		if ($.isPrefixedRankingEnabled())
			$.flashPlayerDisplayName(player);
		if ($.isWelcomeMessageEnabled()) {
			player.sendMessage(ChatColor.GRAY + "/ Welcome to the server " + ChatColor.RED + player.getName());
			player.sendMessage(ChatColor.GRAY + "/ Type " + ChatColor.RED + "/help" + ChatColor.GRAY + " for a list of authentic commands.");
			int connectedPlayers = Server.getPlugin().getServer().getOnlinePlayers().size() + Server.getNpcPlayers().size();
			player.sendMessage(ChatColor.GRAY + "/ Type " + ChatColor.RED + "/vote" + ChatColor.GRAY + " for a list of server voting links.");
			player.sendMessage(ChatColor.GRAY + "/ Type " + ChatColor.RED + "/discord" + ChatColor.GRAY + " for a direct link to our discord.");
			player.sendMessage(ChatColor.GRAY + "/ Players online: " + ChatColor.RED + (connectedPlayers - 1) + ChatColor.GRAY + " - Staff online: " + ChatColor.RED + $.getStaffOnline(Server.getPlugin(), player).length);
			if (!Bukkit.getOnlineMode() && Server.getSkinStorage().ENABLE_ONJOIN_MESSAGE) {
				long timestamp = Server.getSkinStorage().getSkinTimestamp(player);
				long timeTillExpire = (timestamp + Server.getSkinStorage().TIME_EXPIRE_MILLISECOND) - System.currentTimeMillis();
				if (timeTillExpire < 0)
					timeTillExpire = Server.getSkinStorage().TIME_EXPIRE_MILLISECOND;
				player.sendMessage("► Your skin cache will naturally expire in " + $.formatTime((int) (timeTillExpire / 1000)) + ".");
				player.sendMessage("If you want to update your skin now, simply use /updateskin.");
			}
		}
		if (Server.getModeratingPlayers().containsKey(player.getUniqueId())) {
			int rankID = $.getRankId(player);
			if (rankID > 0 || player.isOp()) {
				player.sendMessage(ChatColor.RED + "Notice. " + ChatColor.GRAY + "You are currently still moderating the server.");
			} else {
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						if (!(player == null) && player.isOnGround())
							player.performCommand("moderate");
					}
				}, 10L);
			}
		}
		player.addAttachment(Server.getPlugin(), "usb.social", true);
		player.addAttachment(Server.getPlugin(), "usb.biome.*", true);
		CraftGo.Player.setPlayerListHeaderFooter(player, "SkorrloreGaming", "play.skorrloregaming.com");
		if (player.getName().equals("GemStone166")) {
			CraftGo.Player.sendTimedTitleAndSubtitle(player, new TitleSubtitle("SkorrloreGaming", "Welcome back to the server, Jaiden :)", 10, 40, 5));
		} else {
			CraftGo.Player.sendTimedTitleAndSubtitle(player, new TitleSubtitle("SkorrloreGaming", "Welcome to our minecraft server.", 10, 40, 5));
		}
		Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes());
				String offlinePath = "config." + offlineUUID.toString();
				String onlineUUID = null;
				if (!Bukkit.getServer().getOnlineMode()) {
					onlineUUID = CraftGo.Player.getUUID(player.getName(), true);
					if (onlineUUID == null)
						return;
				}
				String onlinePath = null;
				if (!(onlineUUID == null))
					onlinePath = "config." + onlineUUID.toString();
				if (!path.equals(offlinePath) && Server.getPlugin().getConfig().contains(offlinePath)) {
					Set<String> array = Server.getPlugin().getConfig().getConfigurationSection(offlinePath).getKeys(true);
					for (String value : array) {
						String valuePath = "config." + player.getUniqueId().toString() + "." + value;
						String offlineValuePath = offlinePath + "." + value;
						Server.getPlugin().getConfig().set(valuePath, Server.getPlugin().getConfig().get(offlineValuePath));
					}
					Server.getPlugin().getConfig().set(offlinePath, null);
				}
				if (path.equals(offlinePath) && !(onlinePath == null) && Server.getPlugin().getConfig().contains(onlinePath)) {
					Set<String> array = Server.getPlugin().getConfig().getConfigurationSection(onlinePath).getKeys(true);
					for (String value : array) {
						String valuePath = "config." + player.getUniqueId().toString() + "." + value;
						String onlineValuePath = onlinePath + "." + value;
						Server.getPlugin().getConfig().set(valuePath, Server.getPlugin().getConfig().get(onlineValuePath));
					}
					Server.getPlugin().getConfig().set(onlinePath, null);
				}
				String uuid = offlineUUID.toString();
				if (!Bukkit.getOnlineMode())
					uuid = onlineUUID;
				if (Server.getPlaytimeManager().getPlaytimeConfig().getData().contains(uuid)) {
					Set<String> array1 = Server.getPlaytimeManager().getPlaytimeConfig().getData().getConfigurationSection(uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = player.getUniqueId().toString() + "." + value;
						String oldValuePath = uuid + "." + value;
						Server.getPlaytimeManager().getPlaytimeConfig().getData().set(valuePath, Server.getPlaytimeManager().getPlaytimeConfig().getData().get(oldValuePath));
					}
					Server.getPlaytimeManager().getPlaytimeConfig().getData().set(uuid, null);
					Server.getPlaytimeManager().getPlaytimeConfig().saveData();
				}
				if (Server.getSurvivalConfig().getData().contains("homes." + uuid)) {
					Set<String> array1 = Server.getSurvivalConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
						String oldValuePath = "homes." + uuid + "." + value;
						Server.getSurvivalConfig().getData().set(valuePath, Server.getSurvivalConfig().getData().get(oldValuePath));
					}
					Server.getSurvivalConfig().getData().set("homes." + uuid, null);
					Server.getSurvivalConfig().saveData();
				}
				if (Server.getFactionsConfig().getData().contains("homes." + uuid)) {
					Set<String> array1 = Server.getFactionsConfig().getData().getConfigurationSection("homes." + uuid).getKeys(true);
					for (String value : array1) {
						String valuePath = "homes." + player.getUniqueId().toString() + "." + value;
						String oldValuePath = "homes." + uuid + "." + value;
						Server.getFactionsConfig().getData().set(valuePath, Server.getFactionsConfig().getData().get(oldValuePath));
					}
					Server.getFactionsConfig().getData().set("homes." + uuid, null);
					Server.getFactionsConfig().saveData();
				}
				for (String storageMinigame : $.validStorageMinigames) {
					if (Bukkit.getServer().getOnlineMode()) {
						if (SolidStorage.dataOfflineModeToOnlineMode(player, storageMinigame)) {
							Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									player.performCommand("hub -nosave");
								}
							});
						}
					} else {
						if (SolidStorage.dataOnlineModeToOfflineMode(player, storageMinigame, onlineUUID)) {
							Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									player.performCommand("hub -nosave");
								}
							});
						}
					}
				}

				String[] rawNameChanges = null;
				if (Bukkit.getOnlineMode()) {
					rawNameChanges = CraftGo.Player.getNameHistory(player.getUniqueId().toString());
				} else {
					rawNameChanges = CraftGo.Player.getNameHistory(uuid);
				}
				if (!(rawNameChanges == null)) {
					List<String> nameChangeHistory = Arrays.asList(rawNameChanges);
					for (String uuid2 : Server.getPlugin().getConfig().getConfigurationSection("config").getKeys(false)) {
						if (Server.getPlugin().getConfig().contains("config." + uuid2 + ".username")) {
							if (!Server.getPlugin().getConfig().contains("denyDataTransfer." + uuid2)) {
								String username = Server.getPlugin().getConfig().getString("config." + uuid2 + ".username");
								if (!username.equals(player.getName())) {
									if (nameChangeHistory.contains(username)) {
										Server.getTransferAcceptPlayers().put(player.getUniqueId(), new SwitchUUIDString(UUID.fromString(uuid2), username));
										player.sendMessage($.modernMsgPrefix + "Data from your old username, " + username + ", is ready to be transferred into your current username. This will overwrite all data on your current username. If you changed your name since the last time you played, this is normal and won't damage anything if you accept. Type /tfaccept to accept and transfer the data under your old username, or type /tfdeny to clear the data under the old username and stop future notifications like this.");
										break;
									}
								}
							}
						}
					}
				}
			}
		});
		Server.getPlaytimeManager().handle_JoinEvent(player);
		Server.getDiscordBot().broadcast(
				":heavy_plus_sign:" + ChatColor.stripColor(
						event.getJoinMessage().replace(player.getName(), "**" + player.getName() + "**")
				)
				, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY
		);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		Server.getPlaytimeManager().handle_QuitEvent(player);
		for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
			if (!onlinePlayer.getName().equals(player.getName())) {
				onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
			}
		}
		if (Server.getPlugin().getConfig().contains("config." + player.getUniqueId().toString() + ".ip")) {
			String ipAddress = Server.getPlugin().getConfig().getString("config." + player.getUniqueId().toString() + ".ip");
			if (Server.getPlugin().getConfig().contains("warning." + ipAddress + ".count")) {
				String configPath = "warning." + ipAddress + ".count";
				if (!Server.getPlugin().getConfig().contains(configPath)) {
					Server.getPlugin().getConfig().set(configPath, "0");
				}
				int oldWarningCount = Integer.parseInt(Server.getPlugin().getConfig().getString(configPath));
				Server.getPlugin().getConfig().set("config." + player.getUniqueId().toString() + ".lastKnownWarningCt", oldWarningCount);
			}
		}
		if ($.isCustomQuitMessageEnabled()) {
			event.setQuitMessage(ChatColor.RED + player.getName() + ChatColor.GRAY + " has left the server.");
		}
		if (!(event.getQuitMessage() == null)) {
			Server.setDefaultQuitMessage(event.getQuitMessage().replace(event.getPlayer().getName(), "{player}"));
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.setHealth(0.0);
			String message = Server.getPluginLabel() + ChatColor.RED + player.getName() + ChatColor.GRAY + " has logged out during combat.";
			Bukkit.broadcastMessage(message);
			message = message.substring(message.indexOf(ChatColor.RED + ""));
			Server.getDiscordBot().broadcast(
					ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
					, Channel.SERVER_CHAT
			);
			Server.getPlayersInCombat().remove(player.getUniqueId());
		}
		if (Server.getVanishedPlayers().containsKey(player.getUniqueId())) {
			VanishedInfo iVanish = Server.getVanishedPlayers().get(player.getUniqueId());
			player.getInventory().setContents(iVanish.getContents());
			player.setGameMode(iVanish.getGameMode());
			Server.getVanishedPlayers().remove(player.getUniqueId());
			for (Player p : Server.getPlugin().getServer().getOnlinePlayers()) {
				p.showPlayer(Server.getPlugin(), player);
			}
			if (player.hasPotionEffect(PotionEffectType.INVISIBILITY))
				player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
		if (Server.getOpmePlayers().contains(player.getUniqueId())) {
			player.setOp(false);
			Server.getOpmePlayers().remove(player.getUniqueId());
		}
		if (Server.getTransferAcceptPlayers().containsKey(player.getUniqueId())) {
			Server.getTransferAcceptPlayers().remove(player.getUniqueId());
		}
		if (Server.getHubScoreboardTitleIndex().containsKey(player.getUniqueId())) {
			Server.getHubScoreboardTitleIndex().remove(player.getUniqueId());
		}
		Server.getInstance().performBuggedLeave(player, false, false);
		if (Server.getUseFactionsAsHub()) {
			Server.getInstance().enterFactions(player, false, true);
		} else {
			if (!player.isDead()) {
				Location hubLocation = $.getZoneLocation("hub");
				$.teleport(player, hubLocation);
				Server.getInstance().fetchLobby(player);
				player.setAllowFlight(true);
			}
		}
		if (Server.getMessageRequests().containsKey(player.getUniqueId()))
			Server.getMessageRequests().remove(player.getUniqueId());
		for (Map.Entry<UUID, UUID> id : Server.getMessageRequests().entrySet()) {
			if (id.getValue().equals(player.getUniqueId())) {
				Server.getMessageRequests().remove(id.getKey());
			}
		}
		if (player.isInsideVehicle())
			player.leaveVehicle();
		if ($.isPluginEnabled("AuthMe")) {
			if (!Server.getPlugin().getConfig().contains("config." + player.getUniqueId().toString()) || !$.isAuthenticated(player)) {
				if (!Server.getOnlineMode().getOrDefault(player.getUniqueId(), false)) {
					String message = $.italicGray + "Player " + player.getName() + " has left without registering for this server";
					Bukkit.broadcastMessage(message);
					Server.getDiscordBot().broadcast(
							ChatColor.stripColor(message.replace(player.getName(), "**" + player.getName() + "**"))
							, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY);
				}
			}
		}
		if (Server.getHubScoreboardTitleIndex().containsKey(player.getUniqueId()))
			Server.getHubScoreboardTitleIndex().remove(player.getUniqueId());
		if (Server.getBarApiTitleIndex().containsKey(player.getUniqueId()))
			Server.getBarApiTitleIndex().remove(player.getUniqueId());
		for (Player op : Bukkit.getOnlinePlayers())
			if (Server.getSkyfight().containsKey(op.getUniqueId()))
				$.skyfightScoreboard.schedule(op, true);
		Server.getDiscordBot().broadcast(
				":heavy_minus_sign:" + ChatColor.stripColor(
						event.getQuitMessage().replace(player.getName(), "**" + player.getName() + "**")
				)
				, Channel.SERVER_CHAT, Channel.SERVER_ACTIVITY
		);
		if (Server.getOnlineMode().containsKey(player.getUniqueId()))
			Server.getOnlineMode().remove(player.getUniqueId());
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		ServerMinigame minigame = $.getCurrentMinigame(event.getPlayer());
		if (minigame == ServerMinigame.SKYFIGHT || minigame == ServerMinigame.HUB) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		CraftGo.Player.clearArrowsInBody(player);
		String subDomain = $.getMinigameDomain(player);
		String tag = $.getMinigameTag(subDomain);
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			Bukkit.getScheduler().cancelTask(Server.getPlayersInCombat().get(player.getUniqueId()).getArg0());
			Server.getPlayersInCombat().remove(player.getUniqueId());
		}
		{
			String message = event.getDeathMessage().replace(player.getName(), ChatColor.RED + player.getName() + ChatColor.GRAY);
			boolean playerKiller = true;
			Player k = null;
			if (event.getEntity().getKiller() instanceof Arrow) {
				ProjectileSource source = ((Arrow) event.getEntity().getKiller()).getShooter();
				if (source instanceof Player) {
					playerKiller = true;
					k = (Player) source;
				} else {
					playerKiller = false;
				}
			} else if (event.getEntity().getKiller() instanceof Player) {
				playerKiller = true;
				k = (Player) event.getEntity().getKiller();
			} else {
				playerKiller = false;
			}
			String processedName = "unspecified";
			String entityName = "unspecified";
			for (EntityType ty : EntityType.values()) {
				entityName = $.capitalizeAll(ty.toString().toLowerCase(), "_");
				if (message.contains(entityName)) {
					processedName = entityName;
					break;
				}
			}
			if (playerKiller) {
				processedName = ChatColor.RED + k.getName() + ChatColor.GRAY;
				message = message.replace(k.getName(), processedName);
			} else {
				processedName = ChatColor.RED + processedName + ChatColor.GRAY;
				message = message.replace(entityName, processedName);
			}
			message = message.replace("died", "was killed by magic");
			event.setDeathMessage(tag + message);
			if (playerKiller) {
				player.setVelocity(new Vector(0, 0, 0));
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						player.spigot().respawn();
					}
				}, 10L);
			}
		}
		try {
			if (Server.getSkyfight().containsKey(player.getUniqueId())) {
				event.setDeathMessage("");
				event.getDrops().clear();
				return;
			} else if (subDomain.equals("factions") || subDomain.equals("kitpvp")) {
				Player k;
				if (event.getEntity().getKiller() instanceof Arrow) {
					ProjectileSource source = ((Arrow) event.getEntity().getKiller()).getShooter();
					if (source instanceof Player) {
						k = (Player) source;
					} else {
						String discordMsg = event.getDeathMessage().substring(tag.length()).replace(ChatColor.RED + "", "**")
								.replace(ChatColor.GRAY + "", "**").replace(ChatColor.DARK_RED + "", "");
						Server.getDiscordBot().broadcast(discordMsg, Channel.SERVER_CHAT);
						return;
					}
				} else if (event.getEntity().getKiller() instanceof Player) {
					k = (Player) event.getEntity().getKiller();
				} else {
					String discordMsg = event.getDeathMessage().substring(tag.length()).replace(ChatColor.RED + "", "**")
							.replace(ChatColor.GRAY + "", "**").replace(ChatColor.DARK_RED + "", "");
					Server.getDiscordBot().broadcast(discordMsg, Channel.SERVER_CHAT);
					return;
				}
				double baseHealth = $.roundDouble(k.getHealth() / 2, 1);
				event.setDeathMessage(tag + ChatColor.RED + player.getName() + ChatColor.GRAY + " has been killed by " + ChatColor.RED + k.getName() + ChatColor.GRAY + " [" + ChatColor.RED + baseHealth + ChatColor.DARK_RED + " \u2764" + ChatColor.GRAY + "]");
				int supplyCash = 10;
				if (CraftGo.Player.getAccounts(k, k.getName()).contains(player.getName().toString())) {
					supplyCash = 0;
					k.sendMessage("Sorry, but you cannot farm kills on alternative accounts.");
					k.sendMessage("If you believe this is an error, contact server administration.");
				}
				if (supplyCash > 0) {
					int currentPlayerKills = 0, currentPlayerDeaths = 0;
					if (subDomain.equals("kitpvp")) {
						currentPlayerKills = $.Kitpvp.getPlayerKills(k);
						$.Kitpvp.setPlayerKills(k, currentPlayerKills + 1);
						currentPlayerDeaths = $.Kitpvp.getPlayerDeaths(player);
						$.Kitpvp.setPlayerDeaths(player, currentPlayerDeaths + 1);
					} else if (subDomain.equals("factions")) {
						currentPlayerKills = $.Factions.getPlayerKills(k);
						$.Factions.setPlayerKills(k, currentPlayerKills + 1);
						currentPlayerDeaths = $.Factions.getPlayerDeaths(player);
						$.Factions.setPlayerDeaths(player, currentPlayerDeaths + 1);
					}
					int dpk = currentPlayerKills / 50;
					if ($.getRankId(k) >= -1 && 6 + dpk > 15)
						dpk = 15 - 6;
					if ($.getRankId(k) < -1 && 6 + dpk > 30)
						dpk = 30 - 6;
					supplyCash = 6 + dpk;
				}
				EconManager.depositCash(k, supplyCash, subDomain);
				k.sendMessage(tag + ChatColor.GRAY + "You have been given " + ChatColor.RED + "$" + (supplyCash) + ChatColor.GRAY + " for killing " + ChatColor.RED + player.getName());
				if (subDomain.equals("kitpvp")) {
					$.kitpvpStatisticsScoreboard.schedule(player);
					$.kitpvpStatisticsScoreboard.schedule(k);
					$.kitpvpStatisticsScoreboard.schedule(player, DisplayType.Secondary, Kitpvp_StatisticsScoreboard.class, Kitpvp_LeaderboardScoreboard.class);
					$.kitpvpStatisticsScoreboard.schedule(k, DisplayType.Secondary, Kitpvp_StatisticsScoreboard.class, Kitpvp_LeaderboardScoreboard.class);
				}
				if (subDomain.equals("factions")) {
					$.factionsScoreboard.schedule(player);
					$.factionsScoreboard.schedule(k);
				}
				$.playFirework(player.getLocation());
			}
		} catch (Exception ig) {
		}
		String discordMsg = event.getDeathMessage().substring(tag.length()).replace(ChatColor.RED + "", "**")
				.replace(ChatColor.GRAY + "", "**").replace(ChatColor.DARK_RED + "", "");
		Server.getDiscordBot().broadcast(discordMsg, Channel.SERVER_CHAT);
	}

	@EventHandler
	public void onFlightAttempt(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE)
			return;
		if (!(Server.getFactionFlyPlayers().contains(player.getUniqueId()))) {
			player.setFlying(false);
			event.setCancelled(true);
		}
		if (Server.getHub().contains(player.getUniqueId()) || Server.getSkyfight().containsKey(player.getUniqueId())) {
			if (player.getAllowFlight())
				player.setAllowFlight(false);
			if (!Server.getDoubleJumpCandidates().contains(player.getUniqueId()))
				return;
			Server.getDoubleJumpCandidates().remove(player.getUniqueId());
			Server.getAntiCheat().handleVelocity(player, player.getLocation().getDirection().multiply(2.5));
			Server.getAntiCheat().handleVelocity(player, new Vector(player.getVelocity().getX(), 1.1D, player.getVelocity().getZ()), true);
			player.playSound(player.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1, 1);
		}
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Player player = (Player) event.getPlayer();
		if (Server.getFactions().contains(player.getUniqueId()) || Server.getSurvival().contains(player.getUniqueId()) || Server.getSkyblock().contains(player.getUniqueId())) {
			if (event.getInventory().getType() == InventoryType.ENCHANTING) {
				EnchantingInventory inventory = (EnchantingInventory) event.getInventory();
				inventory.setItem(1, $.createMaterial(Material.LAPIS_LAZULI, 32));
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		String path = "config." + player.getUniqueId().toString();
		event.setCancelled(Server.getPlaytimeManager().onInventoryClick(event));
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().startsWith(ChatColor.MAGIC + ""))
			event.setCancelled(true);
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().startsWith(ChatColor.BOLD + "") && event.getInventory().getName().endsWith("warnings"))
			event.setCancelled(true);
		if (!(event.getCurrentItem() == null)) {
			boolean removeMode = false;
			if (event.getInventory().getItem(0) != null) {
				if (event.getInventory().getItem(0).getType() == Material.ROSE_RED) {
					if (event.getInventory().getItem(0).getItemMeta().hasEnchants()) {
						if (event.getInventory().getItem(0).getItemMeta().getDisplayName().equals("Remove items from shop")) {
							removeMode = true;
						}
					}
				}
			}
			if (event.getInventory().getName().startsWith(ChatColor.BOLD + "Virtual Store")) {
				String name = ChatColor.stripColor(event.getInventory().getName());
				Material material = null;
				int amount = 0;
				int data = 0;
				String code = player.getWorld().getName() + ";" + name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]"));
				if (event.getInventory().getName().contains(";")) {
					if (Server.getSignConfig().getData().contains("signs." + code.replace(";", ""))) {
						int blockX = Integer.parseInt(code.split(";")[1]);
						int blockY = Integer.parseInt(code.split(";")[2]);
						int blockZ = Integer.parseInt(code.split(";")[3]);
						BlockState state = player.getWorld().getBlockAt(blockX, blockY, blockZ).getState();
						if (state instanceof Sign) {
							Sign sign = (Sign) state;
							if (ChatColor.stripColor(sign.getLine(0)).equals("Sell")) {
								material = Material.getMaterial(String.valueOf(sign.getLine(1)).replace(" ", "_").toUpperCase().split(":")[0]);
								amount = event.getCurrentItem().getAmount();
								data = 0;
								try {
									data = Integer.parseInt(String.valueOf(sign.getLine(1)).split(":")[1]);
								} catch (Exception ig) {
								}
							}
						}
					}
				} else {
					code = code.substring(code.indexOf(";") + 1);
					int index = Integer.parseInt(code);
					if (Server.getFactionsShoppeConfig().getData().contains("items." + index)) {
						LaShoppeItem item = Server.getShoppe().retrieveItem(index);
						material = item.getMaterial();
						amount = event.getCurrentItem().getAmount();
						data = item.getData();
					}
				}
				ItemStack item = new ItemStack(material, amount, (short) data);
				if (material == Material.SPAWNER)
					item = CraftGo.MobSpawner.newSpawnerItem(CraftGo.MobSpawner.convertEntityIdToEntityType(data), amount);
				if (!(event.getCurrentItem().getType() == Material.AIR)) {
					if (!$.removeLore(event.getCurrentItem()).isSimilar($.removeLore(item))) {
						event.setCancelled(true);
					}
				}
			}
			if (event.getInventory().getName().startsWith("La Shoppe")) {
				event.setCancelled(true);
				String pageString = event.getInventory().getName().substring(event.getInventory().getName().indexOf("page ") + 5);
				int page = Integer.parseInt(pageString);
				if (event.getCurrentItem() == null)
					return;
				if (event.getCurrentItem().getType() == Material.ROSE_RED) {
					if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Remove items from shop")) {
						Server.getShoppe().createInventory(player, LaShoppeFrame.HOME, page, !removeMode);
						return;
					}
				} else if (event.getCurrentItem().getType() == Material.CACTUS_GREEN) {
					if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Add new shop item")) {
						Server.getShoppe().createInventory(player, LaShoppeFrame.CREATE_ITEM, page, removeMode);
					}
				} else if (event.getCurrentItem().getType() == Material.LAPIS_LAZULI) {
					if (event.getCurrentItem().getItemMeta().getDisplayName().equals("Add new shop enchant")) {
						Server.getShoppe().createInventory(player, LaShoppeFrame.CREATE_ENCHANT, page, removeMode);
					}
				} else if (event.getCurrentItem().getType() == Material.EMERALD) {
					if (event.getCurrentItem().getItemMeta().getDisplayName().equals("View previous page")) {
						if (page == 1) {
							player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
						} else {
							Server.getShoppe().createInventory(player, LaShoppeFrame.HOME, page - 1, removeMode);
						}
					} else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("View following page")) {
						Server.getShoppe().createInventory(player, LaShoppeFrame.HOME, page + 1, removeMode);
					}
				}
				if (event.getCurrentItem().hasItemMeta()) {
					if (event.getCurrentItem().getItemMeta().hasLore()) {
						if (event.getCurrentItem().getType() == Material.ENCHANTED_BOOK) {
							String indexIndexString = event.getCurrentItem().getItemMeta().getLore().get(0);
							String indexString = indexIndexString.substring(indexIndexString.indexOf("Index: ") + 7);
							int index = Integer.parseInt(indexString);
							LaShoppeEnchant enchant = Server.getShoppe().retrieveEnchant(index);
							int price = enchant.getPrice();
							int tier = enchant.getTier();
							if (removeMode) {
								if (player.isOp()) {
									Server.getFactionsShoppeConfig().getData().set("enchant." + index, null);
									Server.getFactionsShoppeConfig().saveData();
									final boolean fRemoveMode = removeMode;
									Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
										@Override
										public void run() {
											Server.getShoppe().createInventory(player, LaShoppeFrame.HOME, page, fRemoveMode);
										}
									}, 1L);
								}
							}
							if (event.isRightClick() || event.isShiftClick()) {
								try {
									DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
									String materialName = $.formatMaterial(event.getCurrentItem().getType());
									String subDomain = $.getMinigameDomain(player);
									int cash = EconManager.retrieveCash(player, subDomain);
									String tag = $.getMinigameTag(player);
									String enchantName = $.formatEnchantment(String.valueOf(enchant.getEnchantment().getKey().getKey().trim()), tier);
									if (cash >= price) {
										ItemStack currentItem = player.getInventory().getItemInMainHand();
										if (currentItem.getType() == Material.AIR || currentItem == null || currentItem.getType() == null) {
											player.sendMessage(tag + ChatColor.RED + "Failed. " + enchantName + ChatColor.GRAY + " cannot be applied to this item.");
											return;
										}
										if (currentItem.getEnchantments().containsKey(enchant) && currentItem.getEnchantments().get(enchant) == tier) {
											player.sendMessage(tag + ChatColor.RED + "Failed. " + enchantName + ChatColor.GRAY + " cannot be applied to this item.");
											return;
										}
										try {
											currentItem.addUnsafeEnchantment(enchant.getEnchantment(), tier);
										} catch (Exception ex) {
											player.sendMessage(tag + ChatColor.RED + "Failed. " + enchantName + ChatColor.GRAY + " cannot be applied to this item.");
											return;
										}
										EconManager.withdrawCash(player, price, subDomain);
										player.getInventory().setItemInMainHand(currentItem);
										player.updateInventory();
										player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + enchantName + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
										return;
									} else {
										player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
										return;
									}
								} catch (Exception ex) {
									player.sendMessage("An internal error has occured whilist processing this shop information.");
									ex.printStackTrace();
									return;
								}
							}
						} else {
							String indexIndexString = event.getCurrentItem().getItemMeta().getLore().get(0);
							String indexString = indexIndexString.substring(indexIndexString.indexOf("Index: ") + 7);
							int index = Integer.parseInt(indexString);
							String priceIndexString = event.getCurrentItem().getItemMeta().getLore().get(1);
							String priceString = priceIndexString.substring(priceIndexString.indexOf("Price: $") + 8);
							int price = Integer.parseInt(priceString);
							String amountIndexString = event.getCurrentItem().getItemMeta().getLore().get(2);
							String amountString = amountIndexString.substring(amountIndexString.indexOf("Amount: ") + 8, amountIndexString.length() - 1);
							int amount = Integer.parseInt(amountString);
							String dataIndexString = event.getCurrentItem().getItemMeta().getLore().get(3);
							String dataString = dataIndexString.substring(dataIndexString.indexOf("Data: ") + 6);
							int data = Integer.parseInt(dataString);
							if (removeMode) {
								if (player.isOp()) {
									Server.getFactionsShoppeConfig().getData().set("items." + index, null);
									Server.getFactionsShoppeConfig().saveData();
									final boolean fRemoveMode = removeMode;
									Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
										@Override
										public void run() {
											Server.getShoppe().createInventory(player, LaShoppeFrame.HOME, page, fRemoveMode);
										}
									}, 1L);
								}
							}
							if (event.isLeftClick()) {
								Inventory inv = Bukkit.createInventory(null, 54, ChatColor.BOLD + "Virtual Store [" + index + "]");
								player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
								player.openInventory(inv);
							} else if (event.isRightClick()) {
								try {
									DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
									String materialName = $.formatMaterial(event.getCurrentItem().getType());
									String subDomain = $.getMinigameDomain(player);
									int cash = EconManager.retrieveCash(player, subDomain);
									String tag = $.getMinigameTag(player);
									if (cash >= price * 2 && player.isSneaking()) {
										amount = amount * 2;
										price = price * 2;
									}
									ItemStack purchaseItem = new ItemStack(event.getCurrentItem().getType(), amount, (short) data);
									if (event.getCurrentItem().getType() == Material.SPAWNER)
										purchaseItem = CraftGo.MobSpawner.newSpawnerItem(CraftGo.MobSpawner.convertEntityIdToEntityType(data), amount);
									if (cash >= price) {
										if (player.getInventory().firstEmpty() == -1) {
											player.sendMessage(tag + ChatColor.RED + "Inventory full. " + ChatColor.GRAY + "Empty some slots then try again.");
											return;
										}
										EconManager.withdrawCash(player, price, subDomain);
										player.getInventory().addItem(purchaseItem);
										player.updateInventory();
										player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + materialName + " x" + amount + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
										return;
									}
									player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
									return;
								} catch (Exception ex) {
									player.sendMessage("An internal error has occured whilist processing this shop information.");
									ex.printStackTrace();
									return;
								}
							}
						}
					}
				}
			}
		}
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().startsWith(ChatColor.RESET + "") && event.getInventory().getName().endsWith("sessions")) {
			String sessionKey = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
			String playerName = ChatColor.stripColor(event.getInventory().getName().substring(0, event.getInventory().getName().indexOf("'")));
			OfflinePlayer op = CraftGo.Player.getOfflinePlayer(playerName);
			if (op.getName().equals(player.getName()) || player.isOp() || $.getRankId(player) > 2) {
				if (player.isOp() || $.getRankId(player) > 2) {
					Server.getSessionManager().invalidateSession(player.getName(), op, sessionKey, true);
				} else {
					Server.getSessionManager().invalidateSession(player.getName(), op, sessionKey, false);
				}
				Server.getSessionManager().openComplexInventory(player, op);
				event.setCancelled(true);
			} else {
				event.setCancelled(true);
			}
			return;
		}
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().equals("Select your preferred team.")) {
			if ($.getCurrentMinigame(player) == ServerMinigame.SKYFIGHT) {
				if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					if (meta.getDisplayName().contains("Blue")) {
						Server.getSkyfight().get(player.getUniqueId()).setTeamValue($.Skyfight.Team.BLUE);
					} else if (meta.getDisplayName().contains("Red")) {
						Server.getSkyfight().get(player.getUniqueId()).setTeamValue($.Skyfight.Team.RED);
					} else if (meta.getDisplayName().contains("Green")) {
						Server.getSkyfight().get(player.getUniqueId()).setTeamValue($.Skyfight.Team.GREEN);
					} else if (meta.getDisplayName().contains("Yellow")) {
						Server.getSkyfight().get(player.getUniqueId()).setTeamValue($.Skyfight.Team.YELLOW);
					} else if (meta.getDisplayName().contains("Pink")) {
						Server.getSkyfight().get(player.getUniqueId()).setTeamValue($.Skyfight.Team.PINK);
					} else {
						Server.getSkyfight().get(player.getUniqueId()).setTeamValue($.Skyfight.Team.NO_TEAM);
					}
					Server.getSkyfight().get(player.getUniqueId()).getTag().setDamagee(null);
					Server.getSkyfight().get(player.getUniqueId()).setScore(0);
					Server.getInstance().enterSkyfight(player, false, false);
				}
			}
		}
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().equals(ChatColor.BOLD + "Select or purchase trails!")) {
			if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
				event.setCancelled(true);
				ItemStack item = event.getCurrentItem();
				ItemMeta meta = item.getItemMeta();
				if (item.getType() == Material.REDSTONE) {
					Server.getPlugin().getConfig().set(path + ".kitpvp.trails.selectedTrail", "-1");
					player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
					player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred trail has been changed.");
					TrailsCmd.openTrailManagementInventory(player);
				} else if (item.getType() == Material.LEATHER_BOOTS) {
					int trailType = -1;
					if (String.valueOf(meta.getDisplayName()).contains("Smoke")) {
						trailType = 0;
					} else if (String.valueOf(meta.getDisplayName()).contains("Emerald")) {
						trailType = 1;
					} else if (String.valueOf(meta.getDisplayName()).contains("Redstone")) {
						trailType = 2;
					} else if (String.valueOf(meta.getDisplayName()).contains("Enchanting")) {
						trailType = 3;
					}
					Server.getPlugin().getConfig().set(path + ".kitpvp.trails.selectedTrail", trailType + "");
					player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
					player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred trail has been changed.");
					TrailsCmd.openTrailManagementInventory(player);
				}
			} else {
				player.closeInventory();
			}
		}
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().equals(ChatColor.BOLD + "Upgrade your spawner!")) {
			if (Server.getFactions().contains(player.getUniqueId()) || Server.getSkyblock().contains(player.getUniqueId())) {
				if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					ItemStack zero = event.getInventory().getItem(0);
					String code = $.scanStringArrayAndSplitBy(zero.getItemMeta().getLore().toArray(new String[0]), "code: ".toCharArray());
					World world = Bukkit.getWorld($.scanStringArrayAndSplitBy(zero.getItemMeta().getLore().toArray(new String[0]), "world: ".toCharArray()));
					int x = Integer.parseInt($.scanStringArrayAndSplitBy(zero.getItemMeta().getLore().toArray(new String[0]), "x: ".toCharArray()));
					int y = Integer.parseInt($.scanStringArrayAndSplitBy(zero.getItemMeta().getLore().toArray(new String[0]), "y: ".toCharArray()));
					int z = Integer.parseInt($.scanStringArrayAndSplitBy(zero.getItemMeta().getLore().toArray(new String[0]), "z: ".toCharArray()));
					if (meta.getDisplayName().contains("Perform Upgrade")) {
						int upgradeCount = Integer.parseInt(Server.getSpawnerConfig().getData().getString(code + ".upgrade"));
						String requiredBalanceStr = ChatColor.stripColor(meta.getDisplayName().substring(meta.getDisplayName().indexOf("$") + 1));
						requiredBalanceStr = requiredBalanceStr.substring(0, requiredBalanceStr.indexOf(")"));
						int requiredBalance = Integer.parseInt(requiredBalanceStr);
						int currentBalance = EconManager.retrieveCash(player, $.getMinigameDomain(player));
						if (upgradeCount + 1 <= 4) {
							if (currentBalance >= requiredBalance) {
								EconManager.withdrawCash(player, requiredBalance, $.getMinigameDomain(player));
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
								Server.getSpawnerConfig().getData().set(code + ".upgrade", (upgradeCount + 1) + "");
								Server.getSpawnerConfig().getData().set(code + ".selectedUpgrade", (upgradeCount + 1) + "");
								Server.getSpawnerConfig().saveData();
								player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred upgrade has been changed.");
								openSpawnerUpgradeInventory(player, world.getBlockAt(x, y, z));
							} else {
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
								player.sendMessage($.Kitpvp.tag + ChatColor.RED + "You do not have enough money to buy this upgrade.");
							}
						} else {
							player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
						}
					} else if (meta.getDisplayName().contains("Select spawner upgrade")) {
						int upgradeNumber = Integer.parseInt(meta.getDisplayName().substring(meta.getDisplayName().indexOf("#") + 1));
						int upgradeCount = Integer.parseInt(Server.getSpawnerConfig().getData().getString(code + ".upgrade"));
						if (upgradeNumber >= 0 && upgradeNumber <= upgradeCount + 1) {
							Server.getSpawnerConfig().getData().set(code + ".selectedUpgrade", upgradeNumber + "");
							Server.getSpawnerConfig().saveData();
							player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred upgrade has been changed.");
							openSpawnerUpgradeInventory(player, world.getBlockAt(x, y, z));
							player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
						}
					} else {
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
					}
				}
			} else {
				player.closeInventory();
			}
		}
		if (!(event.getCurrentItem() == null) && event.getInventory().getName().equals(ChatColor.BOLD + "Select or upgrade your kit!")) {
			if ($.getCurrentMinigame(player) == ServerMinigame.KITPVP) {
				if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
					event.setCancelled(true);
					ItemStack item = event.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					if (meta.getDisplayName().contains("Perform Upgrade")) {
						int upgradeCount = $.Kitpvp.getUpgradeCount(player);
						String requiredBalanceStr = ChatColor.stripColor(meta.getDisplayName().substring(meta.getDisplayName().indexOf("$") + 1));
						requiredBalanceStr = requiredBalanceStr.substring(0, requiredBalanceStr.indexOf(")"));
						int requiredBalance = Integer.parseInt(requiredBalanceStr);
						int currentBalance = EconManager.retrieveCash(player, $.getMinigameDomain(player));
						if (upgradeCount + 1 <= $.Kitpvp.DONOR_MAX_UPGRADE_VALUE) {
							if (upgradeCount + 1 > $.Kitpvp.DEFAULT_MAX_UPGRADE_VALUE) {
								if ($.getRankId(player) > -2) {
									player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
									player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Sorry, you need a donor rank to buy this upgrade.");
									player.performCommand("store");
									return;
								}
							}
							if (currentBalance >= requiredBalance) {
								EconManager.withdrawCash(player, requiredBalance, $.getMinigameDomain(player));
								player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
								$.Kitpvp.setUpgradeCount(player, upgradeCount + 1);
								$.Kitpvp.setPreferredUpgrade(player, upgradeCount + 1);
								player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred kit upgrade has been changed.");
								UpgradeKitCmd.openKitUpgradeInventory(player);
							} else {
								player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
								player.sendMessage($.Kitpvp.tag + ChatColor.RED + "You do not have enough money to buy this upgrade.");
							}
						} else {
							player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
						}
					} else if (meta.getDisplayName().contains("Select preferred weapon")) {
						int type = -1;
						if (item.getType() == Material.STONE_AXE || item.getType() == Material.IRON_AXE) {
							type = 0;
						} else {
							type = 1;
						}
						if (type == 0) {
							$.Kitpvp.setPreferredWeaponType(player, $.Kitpvp.WEAPON_AXE);
						} else if (type == 1) {
							$.Kitpvp.setPreferredWeaponType(player, $.Kitpvp.WEAPON_SWORD);
						}
						player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred weapon type has been changed.");
						UpgradeKitCmd.openKitUpgradeInventory(player);
						player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
					} else if (meta.getDisplayName().contains("Select kit upgrade")) {
						int upgradeNumber = Integer.parseInt(meta.getDisplayName().substring(meta.getDisplayName().indexOf("#") + 1));
						int upgradeCount = $.Kitpvp.getUpgradeCount(player);
						if (upgradeNumber > 0 && upgradeNumber <= upgradeCount + 1) {
							$.Kitpvp.setPreferredUpgrade(player, upgradeNumber - 1);
							player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Preferred kit upgrade has been changed.");
							UpgradeKitCmd.openKitUpgradeInventory(player);
							player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
						}
					} else {
						player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
					}
				}
			} else {
				player.closeInventory();
			}
		}
		if (event.getInventory().getType() == InventoryType.ENCHANTING) {
			if (event.getCurrentItem() == null)
				return;
			if (event.getCurrentItem().getType() == Material.LAPIS_LAZULI)
				event.setCancelled(true);
			return;
		}
		if (event.getInventory().getName().equals(ChatColor.BOLD + "Buy Monster Spawners")) {
			if (event.getCurrentItem() == null)
				return;
			Material material = Material.SPAWNER;
			String materialName = $.formatMaterial(material);
			String domain = $.getMinigameDomain(player);
			String tag = $.getMinigameTag(player);
			EntityType entityType = CraftGo.MobSpawner.getStoredSpawnerItemEntityType(event.getCurrentItem());
			int price = 0;
			int cash = EconManager.retrieveCash(player, domain);
			int amount = 1;
			switch (entityType) {
				case SKELETON:
					price = Server.getSpawnerPrices().get(0).intValue();
					break;
				case ZOMBIE:
					price = Server.getSpawnerPrices().get(1).intValue();
					break;
				case SPIDER:
					price = Server.getSpawnerPrices().get(2).intValue();
					break;
				case BLAZE:
					price = Server.getSpawnerPrices().get(3).intValue();
					break;
				case CREEPER:
					price = Server.getSpawnerPrices().get(4).intValue();
					break;
				case IRON_GOLEM:
					price = Server.getSpawnerPrices().get(5).intValue();
					break;
				case COW:
					price = Server.getSpawnerPrices().get(6).intValue();
					break;
				case PIG:
					price = Server.getSpawnerPrices().get(7).intValue();
					break;
				case CHICKEN:
					price = Server.getSpawnerPrices().get(8).intValue();
					break;
				default:
					player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
					event.setCancelled(true);
					return;
			}
			if (cash >= price * 2 && player.isSneaking()) {
				amount = amount * 2;
				price = price * 2;
			}
			ItemStack item = CraftGo.MobSpawner.newSpawnerItem(entityType, amount);
			if (cash >= price) {
				DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
				if (player.getInventory().firstEmpty() == -1) {
					player.sendMessage(tag + ChatColor.RED + "Inventory full. " + ChatColor.GRAY + "Empty some slots then try again.");
					event.setCancelled(true);
					return;
				}
				EconManager.withdrawCash(player, price, domain);
				player.getInventory().addItem(item);
				player.updateInventory();
				player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + materialName + " x" + amount + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
			} else {
				player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money in your account.");
			}
			event.setCancelled(true);
			player.closeInventory();
		} else if (event.getInventory().getName().equals(ChatColor.DARK_PURPLE + "Server Selector (0b1)")) {
			if (event.getCurrentItem() == null)
				return;
			event.setCancelled(true);
			switch (event.getCurrentItem().getType()) {
				case BOW:
					Server.getInstance().enterSkyfight(player, false, false);
					break;
				case STONE_PICKAXE:
					if ($.isMinigameEnabled(ServerMinigame.FACTIONS) && $.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
						openSurvivalServerSelectorMenu(player);
					} else if ($.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
						Server.getInstance().enterSurvival(player, false, false);
					} else if ($.isMinigameEnabled(ServerMinigame.FACTIONS)) {
						Server.getInstance().enterFactions(player, false, false);
					}
					break;
				case DIAMOND_SWORD:
					Server.getInstance().enterFactions(player, false, false);
					break;
				case IRON_SWORD:
					Server.getInstance().enterKitpvp(player, false, false);
					break;
				case GRASS_BLOCK:
					Server.getInstance().enterCreative(player, false, false);
					break;
				case OAK_SAPLING:
					Server.getInstance().enterSkyblock(player, false, false);
					break;
				default:
					player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
					return;
			}
		} else if (event.getInventory().getName().equals(ChatColor.DARK_PURPLE + "Server Selector (0b10)")) {
			if (event.getCurrentItem() == null)
				return;
			switch (event.getCurrentItem().getType()) {
				case DIAMOND_SWORD:
					Server.getInstance().enterFactions(player, false, false);
					break;
				case STONE_PICKAXE:
					Server.getInstance().enterSurvival(player, false, false);
					break;
				default:
					player.playSound(player.getLocation(), Sound.ENTITY_BAT_HURT, 1, 1);
					event.setCancelled(true);
					return;
			}
		} else if (Server.getSkyfight().containsKey(player.getUniqueId()) && player.getGameMode() == GameMode.SURVIVAL) {
			event.setCancelled(true);
		} else if (event.getInventory().getName().startsWith(ChatColor.BOLD + "Temporary Inventory")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if (event.getInventory().getType() == InventoryType.ENCHANTING) {
			EnchantingInventory inventory = (EnchantingInventory) event.getInventory();
			inventory.setItem(1, $.createMaterial(Material.AIR));
			return;
		}
		if (event.getInventory().getName().startsWith("Select your preferred team.")) {
			if (Server.getSkyfight().containsKey(player.getUniqueId())) {
				Server.getSkyfight().get(player.getUniqueId()).setHasTeamSelectionGuiOpen(false);
			}
		}
		if (event.getInventory().getName().startsWith(ChatColor.BOLD + "Virtual Store")) {
			String name = ChatColor.stripColor(event.getInventory().getName());
			Material material = null;
			int amount = 0;
			int price = 0;
			int data = 0;
			String code = player.getWorld().getName() + ";" + name.substring(name.lastIndexOf("[") + 1, name.lastIndexOf("]"));
			if (event.getInventory().getName().contains(";")) {
				if (Server.getSignConfig().getData().contains("signs." + code.replace(";", ""))) {
					int blockX = Integer.parseInt(code.split(";")[1]);
					int blockY = Integer.parseInt(code.split(";")[2]);
					int blockZ = Integer.parseInt(code.split(";")[3]);
					BlockState state = player.getWorld().getBlockAt(blockX, blockY, blockZ).getState();
					if (state instanceof Sign) {
						Sign sign = (Sign) state;
						if (ChatColor.stripColor(sign.getLine(0)).equals("Sell")) {
							material = Material.getMaterial(String.valueOf(sign.getLine(1)).replace(" ", "_").toUpperCase().split(":")[0]);
							amount = Integer.parseInt(String.valueOf(sign.getLine(3)));
							price = Integer.parseInt(String.valueOf(sign.getLine(2).replace("$", "").replace(",", "")));
							data = 0;
							try {
								data = Integer.parseInt(String.valueOf(sign.getLine(1)).split(":")[1]);
							} catch (Exception ig) {
							}
						}
					}
				}
			} else {
				code = code.substring(code.indexOf(";") + 1);
				int index = Integer.parseInt(code);
				if (Server.getFactionsShoppeConfig().getData().contains("items." + index)) {
					LaShoppeItem item = Server.getShoppe().retrieveItem(index);
					material = item.getMaterial();
					price = item.getPrice();
					amount = item.getAmount();
					data = item.getData();
				}
			}
			DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
			int singleAmount = 1;
			int singlePrice = (int) $.getSinglePricing(amount, price);
			String materialName = $.formatMaterial(material);
			ItemStack item = new ItemStack(material, singleAmount, (short) data);
			if (material == Material.SPAWNER)
				item = CraftGo.MobSpawner.newSpawnerItem(CraftGo.MobSpawner.convertEntityIdToEntityType(data), singleAmount);
			int totalPrice = 0;
			int totalAmount = 0;
			for (ItemStack itm : event.getInventory().getContents()) {
				if (!(itm == null)) {
					if (item.getType() == itm.getType()) {
						if (item.getDurability() == itm.getDurability()) {
							int givenPrice = singlePrice * itm.getAmount();
							totalPrice = totalPrice + givenPrice;
							totalAmount = totalAmount + itm.getAmount();
						}
					}
				}
			}
			EconManager.depositCash(player, totalPrice, $.getMinigameDomain(player));
			if (totalAmount > 0) {
				player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Sold " + ChatColor.RED + materialName + " x" + totalAmount + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(totalPrice));
			}
		}
		if (event.getInventory().getName().startsWith(ChatColor.BOLD + "Personal Inventory")) {
			try {
				Player tp = player;
				String chestNumberStr = event.getInventory().getName().substring(event.getInventory().getName().indexOf("[") + 1, event.getInventory().getName().length() - 1);
				int chestNumber = Integer.parseInt(chestNumberStr);
				boolean saveInventory = false;
				if (Server.getSavePersonalChest().containsKey(player)) {
					tp = Server.getSavePersonalChest().get(player);
					Server.getSavePersonalChest().remove(player);
				} else if (Server.getSaveOtherInventory().containsKey(player)) {
					tp = Server.getSaveOtherInventory().get(player);
					Server.getSaveOtherInventory().remove(player);
					saveInventory = true;
				}
				String subDomain = $.getMinigameDomain(tp);
				if (saveInventory) {
					ItemStack[] contents = new ItemStack[Directory.INVENTORY_SIZE];
					for (int i = 0; i < Directory.INVENTORY_SIZE; i++) {
						contents[i] = event.getInventory().getContents()[i];
					}
					tp.getInventory().setContents(contents);
				} else {
					SolidStorage.savePersonalChest(tp, subDomain, event.getInventory().getContents(), chestNumber);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityAttack(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
			event.setCancelled(true);
			return;
		}
		if (event.getDamager() instanceof Player) {
			Player damager = (Player) event.getDamager();
			if (Server.getSkyblock().contains(damager.getUniqueId()) && !(event.getEntity() instanceof Player)) {
				if (damager.getWorld().getEnvironment() == Environment.NETHER)
					event.setCancelled(false);
			}
			if (Server.getAntiCheat().antiafk.lackingActivityMinutes.containsKey(damager.getUniqueId()))
				Server.getAntiCheat().antiafk.lackingActivityMinutes.remove(damager.getUniqueId());
		}
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (Server.getFactions().contains(player.getUniqueId()) || Server.getSurvival().contains(player.getUniqueId())) {
				String domain = $.getMinigameDomain(player);
				Location spawnLocation = null;
				if (player.getWorld().getEnvironment() == Environment.NORMAL) {
					spawnLocation = $.getZoneLocation(domain);
				} else if (player.getWorld().getEnvironment() == Environment.NETHER) {
					spawnLocation = $.getZoneLocation(domain + "_end");
				}
				if (!(spawnLocation == null) && player.getWorld().getName().equals(spawnLocation.getWorld().getName())) {
					if (player.getLocation().distance(spawnLocation) < 20) {
						event.setCancelled(true);
						return;
					}
				}
			}
			Player killer;
			if (event.getDamager() instanceof Arrow && ((Arrow) event.getDamager()).getShooter() instanceof Player) {
				killer = (Player) ((Arrow) event.getDamager()).getShooter();
			} else if (event.getDamager() instanceof Player) {
				killer = (Player) event.getDamager();
			} else
				return;
			if (player.getEntityId() == killer.getEntityId())
				return;
			if (Server.getDelayedTeleports().containsKey(player.getUniqueId()))
				Server.getDelayedTeleports().get(player.getUniqueId()).close(ChatColor.GRAY + "Teleportation request aborted due to combat.");
			if (Server.getDelayedTeleports().containsKey(killer.getUniqueId()))
				Server.getDelayedTeleports().get(killer.getUniqueId()).close(ChatColor.GRAY + "Teleportation request aborted due to combat.");
			if ($.Marriage.getPlayerMarriageId(player) > 0) {
				if ($.Marriage.getMarriedOfflinePlayer(player).getName().equals(killer.getName())) {
					if (!$.Marriage.getPlayerMarriedPvp(killer)) {
						event.setCancelled(true);
						return;
					}
				}
			}
			if (Server.getKitpvp().contains(player.getUniqueId()) || Server.getFactions().contains(player.getUniqueId())) {
				String tag = $.getMinigameTag(player);
				if (Server.getFactions().contains(player.getUniqueId())) {
					FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);
					FPlayer fkiller = FPlayers.getInstance().getByPlayer(killer);
					if (fplayer.getFactionId().equals(fkiller.getFactionId()))
						return;
				}
				if (!Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
					CombatTimer ct = new CombatTimer(player, killer, 12, tag);
					ct.runTaskTimer(Server.getPlugin(), 4, 4);
				} else {
					SwitchIntDouble existingExtreme = Server.getPlayersInCombat().get(player.getUniqueId());
					existingExtreme.setArg1(12);
					Server.getPlayersInCombat().put(player.getUniqueId(), existingExtreme);
				}
				if (!Server.getPlayersInCombat().containsKey(killer.getUniqueId())) {
					CombatTimer ct = new CombatTimer(killer, player, 12, tag);
					ct.runTaskTimer(Server.getPlugin(), 4, 4);
				} else {
					SwitchIntDouble existingExtreme = Server.getPlayersInCombat().get(killer.getUniqueId());
					existingExtreme.setArg1(12);
					Server.getPlayersInCombat().put(killer.getUniqueId(), existingExtreme);
				}
			}
			if (Server.getSkyfight().containsKey(player.getUniqueId()) && Server.getSkyfight().containsKey(killer.getUniqueId())) {
				$.Skyfight.Player sfKiller = Server.getSkyfight().get(killer.getUniqueId());
				$.Skyfight.Player sfPlayer = Server.getSkyfight().get(player.getUniqueId());
				if (sfKiller.getTeamValue() == $.Skyfight.Team.NO_TEAM || !(sfKiller.getTeamValue() == sfPlayer.getTeamValue())) {
					Server.getSkyfight().get(killer.getUniqueId()).getTag().setDamagee(player);
				} else {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		ServerMinigame minigame = $.getCurrentMinigame(player);
		if (!player.isOp()) {
			if (!(minigame == ServerMinigame.HUB) && !(minigame == ServerMinigame.UNKNOWN)) {
				if (SolidStorage.isForbiddenWorld(event.getTo().getWorld(), minigame)) {
					event.setCancelled(true);
					$.playForbiddenTeleportMessage(player, minigame);
				}
			}
		}
	}

	@EventHandler
	public void onHealthChange(EntityRegainHealthEvent event) {
		if (event.getEntity() instanceof Player) {
			for (Player op : Bukkit.getOnlinePlayers()) {
				$.Scoreboard.configureHealth(op);
			}
		}
	}

	@EventHandler
	public void onProjectileLaunch(PlayerLaunchProjectileEvent event) {
		Player player = event.getPlayer();
		if (event.getProjectile() instanceof EnderPearl) {
			if (Server.getDelayedTasks().contains(player.getUniqueId())) {
				event.setCancelled(true);
			} else {
				Server.getDelayedTasks().add(player.getUniqueId());
				Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
					@Override
					public void run() {
						Server.getDelayedTasks().remove(player.getUniqueId());
					}
				}, 7L);
			}
		}
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if (Server.getSkyfight().containsKey(player.getUniqueId()))
			Server.getInstance().enterSkyfight(player, false, false);
		Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Player op : Bukkit.getOnlinePlayers()) {
					$.Scoreboard.configureHealth(op);
				}
			}
		}, 20L);
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof IronGolem) {
			if (event.getCause() == DamageCause.LAVA) {
				event.setDamage(event.getDamage() * 8);
			}
			return;
		}
		if (!(event.getEntity() instanceof Player))
			return;
		Player player = (Player) event.getEntity();
		if (Server.getSkyfight().containsKey(player.getUniqueId())) {
			if (player.getHealth() - event.getFinalDamage() < 5) {
				Server.getSkyfight().get(player.getUniqueId()).getTag().setDamagee(null);
				Server.getSkyfight().get(player.getUniqueId()).setScore(0);
				Server.getInstance().enterSkyfight(player, false, false);
				event.setCancelled(true);
			}
		}
		if (event.getCause() == DamageCause.FLY_INTO_WALL) {
			event.setCancelled(true);
			return;
		}
		if (event.getCause() == DamageCause.FALL && !Server.getFactions().contains(player.getUniqueId()) && !Server.getSurvival().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			event.setCancelled(true);
			return;
		}
		if (!(player.getInventory().getHelmet() == null) && player.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET) {
			if (!(player.getInventory().getChestplate() == null) && player.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE) {
				if (!(player.getInventory().getLeggings() == null) && player.getInventory().getLeggings().getType() == Material.CHAINMAIL_LEGGINGS) {
					if (!(player.getInventory().getBoots() == null) && player.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS) {
						if (event.getCause() == DamageCause.FIRE_TICK) {
							player.setFireTicks(0);
							event.setCancelled(true);
							return;
						} else if (event.getCause() == DamageCause.LAVA) {
							event.setDamage(event.getDamage() / 2);
						}
					}
				}
			}
		}
		Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (Player op : Bukkit.getOnlinePlayers()) {
					$.Scoreboard.configureHealth(op);
				}
			}
		}, 5L);
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if ($.isAuthenticated(player)) {
			if (!player.isInsideVehicle()) {
				if (player.isBlocking()) {
					player.setWalkSpeed(0.1F);
				} else {
					player.setWalkSpeed(0.2F);
				}
			}
			if (player.getGameMode() == GameMode.SURVIVAL) {
				Material faceDownType1 = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
				Material faceDownType2 = event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType();
				if (!(faceDownType1 == faceDownType2)) {
					if (faceDownType1 == Material.GLOWSTONE && event.getTo().clone().subtract(0, 2, 0).getBlock().getType() == Material.LAPIS_BLOCK && !(player.getGameMode() == GameMode.CREATIVE)) {
						Server.getAntiCheat().handleVelocity(player, player.getVelocity().add(new Vector(0, 1.5, 0)));
					}
				}
			}
		} else {
			player.setWalkSpeed(0.0F);
		}
		player.setFlySpeed(0.1F);
		if (event.getFrom().distance(event.getTo()) > 0.1) {
			if (!$.isAuthenticated(player)) {
				ComplexParticle.SMOKE_NORMAL.count(10).display(player, player.getLocation());
				return;
			}
			boolean isPlayerInHub = false;
			if (isPlayerInHub = Server.getHub().contains(player.getUniqueId()) || Server.getSkyfight().containsKey(player.getUniqueId())) {
				if (isPlayerInHub || Server.getFactions().contains(player.getUniqueId())) {
					if (Server.getFactions().contains(player.getUniqueId()) && !Server.getUseFactionsAsHub())
						return;
					UUID id = player.getUniqueId();
					if (Server.getHub().contains(player.getUniqueId())) {
						if (Server.getKitpvp().contains(id) || Server.getFactions().contains(id) || Server.getSkyfight().containsKey(id) || Server.getCreative().contains(id) || Server.getSurvival().contains(id) || Server.getSkyblock().contains(id)) {
							Server.getHub().remove(id);
							return;
						}
						if (!player.getWorld().getName().equals(Server.getLastKnownHubWorld()) && !Server.getModeratingPlayers().containsKey(player.getUniqueId())) {
							Location hubLocation = $.getZoneLocation("hub");
							$.teleport(player, hubLocation);
						}
						ItemStack item0 = player.getInventory().getItem(0);
						if (item0 == null || !(item0.getType() == Material.COMPASS))
							Server.getInstance().fetchLobby(player);
					}
					Material faceDownType1 = event.getTo().getBlock().getRelative(BlockFace.DOWN).getType();
					Material faceDownType2 = event.getFrom().getBlock().getRelative(BlockFace.DOWN).getType();
					if (!(faceDownType1 == faceDownType2)) {
						switch (faceDownType1) {
							case DIAMOND_ORE:
								if ($.isMinigameEnabled(ServerMinigame.CREATIVE)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										Server.getInstance().enterCreative(player, false, false);
										return;
									}
								}
								break;
							case GOLD_ORE:
								if ($.isMinigameEnabled(ServerMinigame.SKYFIGHT)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										Server.getInstance().enterSkyfight(player, false, false);
										return;
									}
								}
							case IRON_ORE:
								if ($.isMinigameEnabled(ServerMinigame.KITPVP)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										Server.getInstance().enterKitpvp(player, false, false);
										return;
									}
								}
								break;
							case COAL_ORE:
								if ($.isMinigameEnabled(ServerMinigame.FACTIONS)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										Server.getInstance().enterFactions(player, false, false);
										return;
									}
								}
							case LAPIS_ORE:
								if ($.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										Server.getInstance().enterSurvival(player, false, false);
										return;
									}
								}
								break;
							case EMERALD_ORE:
								if ($.isMinigameEnabled(ServerMinigame.FACTIONS) || $.isMinigameEnabled(ServerMinigame.SURVIVAL)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										openSurvivalServerSelectorMenu(player);
										return;
									}
								}
								break;
							case NETHER_QUARTZ_ORE:
								if ($.isMinigameEnabled(ServerMinigame.SKYBLOCK)) {
									if (player.getGameMode() == GameMode.SURVIVAL) {
										Server.getInstance().enterSkyblock(player, false, false);
										return;
									}
								}
								break;
							default:
								break;
						}
					}
				}
				if (!(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR)) {
					if (!Server.getDoubleJumpCandidates().contains(player.getUniqueId())) {
						Server.getDoubleJumpCandidates().add(player.getUniqueId());
						if (!player.getAllowFlight())
							player.setAllowFlight(true);
						return;
					}
				}
				if (Server.getSkyfight().containsKey(player.getUniqueId())) {
					if (player.getHealth() < 5 || (player.getLocation().getBlockY() < 5 && player.getGameMode() == GameMode.SURVIVAL)) {
						if (!Server.getDelayedTasks().contains(player.getUniqueId())) {
							Server.getDelayedTasks().add(player.getUniqueId());
							Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
								@Override
								public void run() {
									Server.getDelayedTasks().remove(player.getUniqueId());
								}
							}, 20L);
							Server.getSkyfight().get(player.getUniqueId()).getTag().setDamagee(null);
							Server.getSkyfight().get(player.getUniqueId()).setScore(0);
							Server.getInstance().enterSkyfight(player, false, false);
						}
					}
					return;
				}
			}
			String path = "config." + player.getUniqueId().toString();
			if (Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.selectedTrail")) {
				int selectedTrail = Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.selectedTrail"));
				switch (selectedTrail) {
					case 0:
						ComplexParticle.SMOKE_NORMAL.count(10).display(player.getLocation());
						break;
					case 1:
						ComplexParticle.VILLAGER_HAPPY.count(5).display(player.getLocation());
						break;
					case 2:
						ComplexParticle.DRIP_LAVA.count(5).display(player.getLocation());
						break;
					case 3:
						ComplexParticle.REDSTONE.count(15).data(new Particle.DustOptions(Color.WHITE, 1f)).display(player.getLocation());
						break;
					default:
						break;
				}
			}
			if (Server.getFactionFlyPlayers().contains(player.getUniqueId())) {
				if (!FPlayers.getInstance().getByPlayer(player).isInOwnTerritory()) {
					if (Server.getFactionFlyPlayers().contains(player.getUniqueId())) {
						Server.getFactionFlyPlayers().remove(player.getUniqueId());
						if (player.isFlying())
							player.setFlying(false);
						player.setAllowFlight(false);
						player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "Success. " + ChatColor.GRAY + "Faction flight disabled.");
					}
				}
			}
			if (player.isGliding() && (Server.getSkyblock().contains(player.getUniqueId()) || Server.getFactions().contains(player.getUniqueId()))) {
				Environment environment = player.getWorld().getEnvironment();
				if ((Server.getSkyblock().contains(player.getUniqueId()) && environment == Environment.NORMAL) || environment == Environment.THE_END) {
					int blocksUntilGround = player.getLocation().getBlockY() - player.getWorld().getHighestBlockYAt(player.getLocation());
					Vector vec = player.getLocation().getDirection();
					vec = vec.multiply(new Vector(0.5, 0.8, 0.5));
					if (player.getLocation().getY() > 256 * 1.2)
						vec.setY(-0.5);
					if (blocksUntilGround > 5 || blocksUntilGround < 0) {
						Server.getAntiCheat().handleVelocity(player, vec);
					}
				}
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPreprocessCommand(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		String world = event.getPlayer().getWorld().getName();
		if (Server.getAntiCheat().antiafk.lackingActivityMinutes.containsKey(player.getUniqueId()))
			Server.getAntiCheat().antiafk.lackingActivityMinutes.remove(player.getUniqueId());
		String formattedAlertMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + world + ChatColor.GRAY + "] " + ChatColor.RESET + player.getDisplayName() + ChatColor.RESET + " " + '\u00BB' + " " + ChatColor.GRAY + "" + ChatColor.ITALIC + event.getMessage();
		String label = event.getMessage().split(" ")[0];
		if (label.contains(":") && !player.isOp()) {
			String[] strArr = event.getMessage().split(" ");
			strArr[0] = "/" + strArr[0].split(":")[1];
			if (strArr[0].length() > 0) {
				event.setMessage(String.join(" ", strArr));
			} else {
				event.setCancelled(true);
			}
		}
		label = event.getMessage().split(" ")[0];
		for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
			int rankID = $.getRankId(otherPlayer);
			if (otherPlayer.isOp() || rankID > -1) {
				if (!Server.getSpectatingPlayers().contains(player.getUniqueId()) || (Server.getSpectatingPlayers().contains(player.getUniqueId()) && rankID > $.getRankId(player))) {
					if (!$.isAuthenticationCommand(label)) {
						otherPlayer.sendMessage(formattedAlertMessage);
					}
				}
			}
		}
		if ($.isAuthenticationCommand(label)) {
			Bukkit.getConsoleSender().sendMessage(player.getName() + " issued authentication command: " + event.getMessage());
		}
		if ($.isAuthenticationCommand(label) && !label.equalsIgnoreCase("/login")) {
			if (player.getName().equalsIgnoreCase("Player")) {
				player.sendMessage($.Legacy.tag + "You are not allowed to modify account information.");
				event.setCancelled(true);
			}
		}
		if (label.equalsIgnoreCase("/version") || label.equalsIgnoreCase("/ver") || label.equalsIgnoreCase("/icanhasbukkit")) {
			player.sendMessage($.Legacy.tag + "This server is running CraftBukkit version " + Bukkit.getVersion() + " (Implementing API version " + Bukkit.getBukkitVersion() + ")");
			event.setCancelled(true);
		} else if (label.equalsIgnoreCase("/buy")) {
			player.sendMessage($.Legacy.tag + ChatColor.GRAY + "Our buycraft: " + ChatColor.RED + "https://shop.skorrloregaming.com/");
			event.setCancelled(true);
		} else if (label.equalsIgnoreCase("/plugins") || label.equalsIgnoreCase("/pl")) {
			List<Plugin> plugins = new LinkedList<Plugin>(Arrays.asList(Bukkit.getPluginManager().getPlugins()));
			if (!player.hasPermission("bukkit.command.plugins") || $.getRankId(player) < 0) {
				if (!(Server.getProtoSupportListener() == null) && Server.getProtoSupportListener().isOnlineMode(player))
					if ($.isPluginEnabled("AuthMe"))
						for (int i = 0; i < plugins.size(); i++)
							if (plugins.get(i).getName().equals("AuthMe"))
								plugins.remove(i);
				if ($.isPluginEnabled("JPanel"))
					for (int i = 0; i < plugins.size(); i++)
						if (plugins.get(i).getName().equals("JPanel"))
							plugins.remove(i);
			}
			StringBuilder sb = new StringBuilder($.Legacy.tag + "Plugins (" + plugins.size() + "): ");
			for (Plugin plugin : plugins) {
				if (plugin.isEnabled()) {
					sb.append(ChatColor.GREEN + plugin.getName() + ChatColor.WHITE + ", ");
				} else {
					sb.append(ChatColor.RED + plugin.getName() + ChatColor.WHITE + ", ");
				}
			}
			String message = sb.toString();
			player.sendMessage(message.substring(0, message.lastIndexOf(", ")));
			event.setCancelled(true);
		}
		if ($.isAuthenticated(player)) {
			if (label.equalsIgnoreCase("/party") && !($.getCurrentMinigame(event.getPlayer()) == ServerMinigame.FACTIONS)) {
				event.setCancelled(true);
				player.sendMessage($.getMinigameTag(event.getPlayer()) + ChatColor.RED + "This minigame prevents use of this command.");
			} else if (label.equalsIgnoreCase("/skyfight")) {
				player.performCommand("server skyfight");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/creative")) {
				player.performCommand("server creative");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/kitpvp")) {
				player.performCommand("server kitpvp");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/factions")) {
				player.performCommand("server factions");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/survival")) {
				player.performCommand("server survival");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/skyblock")) {
				player.performCommand("server skyblock");
				event.setCancelled(true);
			} else if (event.getMessage().equalsIgnoreCase("/f fly")) {
				player.performCommand("fly");
				event.setCancelled(true);
			} else if (label.equalsIgnoreCase("/pt")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/playtime"));
			} else if (label.equalsIgnoreCase("/tp")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/tpa"));
			} else if (label.equalsIgnoreCase("/sc")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/staffchat"));
			} else if (label.equalsIgnoreCase("/p")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/plot"));
			} else if (label.equalsIgnoreCase("/gm")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/gamemode"));
			} else if (label.equalsIgnoreCase("/gmc")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/gamemode 1"));
			} else if (label.equalsIgnoreCase("/gms")) {
				event.setMessage($.replaceCommandLabelInCommand(event.getMessage(), "/gamemode 0"));
			} else if (label.equalsIgnoreCase("/me")) {
				event.setCancelled(true);
				$.playLackPermissionMessage(player);
			} else if (label.equalsIgnoreCase("/f")) {
				if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
					player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
					event.setCancelled(true);
				}
				if (!Server.getFactions().contains(event.getPlayer().getUniqueId())) {
					event.setCancelled(true);
					player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
				}
			} else if (label.equalsIgnoreCase("/partytalk") || label.equalsIgnoreCase("ptalk") || label.equalsIgnoreCase("ptk") || label.equalsIgnoreCase("/islandtalk") || label.equalsIgnoreCase("/istalk") || label.equalsIgnoreCase("/it")) {
				event.setCancelled(true);
				$.playLackPermissionMessage(player);
			} else if (label.equalsIgnoreCase("/is") || label.equalsIgnoreCase("/island") || label.equalsIgnoreCase("/c") || label.equalsIgnoreCase("/challenges") || label.equalsIgnoreCase("/usb")) {
				if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
					player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
					event.setCancelled(true);
				}
				if (!Server.getSkyblock().contains(event.getPlayer().getUniqueId())) {
					event.setCancelled(true);
					player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
				}
			}
		}
	}
}
