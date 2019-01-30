package me.skorrloregaming.listeners;

import me.skorrloregaming.*;
import me.skorrloregaming.Server;
import me.skorrloregaming.runnable.TreeCutter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Colorable;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BlockEventHandler implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location blockLoc = block.getLocation();
		Material blockType = block.getType();
		if (blockType == Material.WALL_SIGN || blockType == Material.SIGN) {
			String[] s = event.getLines();
			for (int i = 0; i < s.length; i++) {
				s[i] = ChatColor.stripColor(s[i]);
			}
			if (s[0].equals("[Shop]")) {
				String code = null;
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
					String[] tl = targetSign.getLines();
					for (int i = 0; i < tl.length; i++) {
						tl[i] = ChatColor.stripColor(tl[i]);
					}
					if (s[2].length() > 14)
						s[2] = s[2].substring(0, 14);
					if (s[3].length() > 14)
						s[3] = s[3].substring(0, 14);
					if (ChatColor.stripColor(s[0]).equals("Buy") || ChatColor.stripColor(s[0]).equals("Sell")) {
						String materialName = ChatColor.stripColor(s[1]).toUpperCase().replace(" ", "_").split(":")[0];
						if (Material.getMaterial(materialName) == null) {
							Material legacyMaterial;
							if (!((legacyMaterial = Material.matchMaterial(materialName, true)) == null)) {
								event.setLine(1, Link$.formatMaterial(legacyMaterial));
							}
						}
					}
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Successfully processed shop " + ChatColor.RED + code + ChatColor.GRAY + ".");
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Type: " + ChatColor.RED + tl[0] + " Shop");
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "ID: " + ChatColor.RED + tl[1]);
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Price: " + "$" + ChatColor.RED + tl[2]);
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + ": " + ChatColor.RED + tl[3]);
					event.setLine(0, ChatColor.DARK_BLUE + s[0]);
					event.setLine(2, "\"" + s[2]);
					event.setLine(3, s[3] + "\"");
				} else {
					player.sendMessage("The shop code is invalid or target shop has not been activated.");
					return;
				}
				return;
			}
			if (s[0].equals("Buy") || s[0].equals("Sell") || s[0].equals("Enchant") || s[0].equals("Repair") || s[1].equals("Kit")) {
				if (!player.isOp()) {
					player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "You do not have permission to activate this shop.");
					Link$.playLackPermissionMessage(player);
					return;
				}
				DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
				String code = blockLoc.getWorld().getName() + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
				Server.getSignConfig().getData().set("signs." + code, 1);
				Server.getSignConfig().saveData();
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Successfully processed shop " + ChatColor.RED + code + ChatColor.GRAY + ".");
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Type: " + ChatColor.RED + s[0] + " Shop");
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "ID: " + ChatColor.RED + s[1]);
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Price: " + "$" + ChatColor.RED + s[2]);
				player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + ": " + ChatColor.RED + s[3]);
				if (s[1].equals("Kit")) {
					event.setLine(1, ChatColor.DARK_BLUE + s[1]);
				} else {
					event.setLine(0, ChatColor.DARK_BLUE + s[0]);
				}
				if (!ChatColor.stripColor(s[1]).equals("Kit") && !s[2].startsWith("$")) {
					event.setLine(2, "$" + formatter.format(Integer.parseInt(s[2])));
				}
				return;
			}
			int rank = Link$.getRankId(player);
			int donorRank = Link$.getDonorRankId(player);
			if (player.isOp() || rank > -1 || donorRank < -2) {
				for (int i = 0; i < s.length; i++) {
					event.setLine(i, ChatColor.translateAlternateColorCodes('&', s[i]));
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!event.isCancelled())
			event.setCancelled(LinkServer.getInstance().getAntiCheat().onBlockPlace(event.getBlock(), player));
		if (event.isCancelled())
			return;
		if (Server.getSkyblock().contains(player.getUniqueId())) {
			$.Skyblock.setPlayerPlacedBlocks(player, $.Skyblock.getPlayerPlacedBlocks(player) + 1);
			$.skyblockScoreboard.schedule(player);
		}
		if (!(event.getBlock().getType() == Material.SPAWNER))
			return;
		if (event.getHand() == EquipmentSlot.OFF_HAND) {
			event.getPlayer().sendMessage("Placing a silk-touched spawner with your off-hand..");
			event.getPlayer().sendMessage("is unsupported, please use your main hand, thanks.");
			event.setCancelled(true);
			return;
		}
		String[] lore = event.getItemInHand().getItemMeta().getLore().toArray(new String[0]);
		EntityType type = CraftGo.MobSpawner.getStoredSpawnerItemEntityType(event.getItemInHand());
		String upgradeStr = $.scanStringArrayAndSplitBy(lore, "Upgrade: ".toCharArray());
		int upgradeInt = 0;
		if (!(upgradeStr == null))
			upgradeInt = Integer.parseInt(String.valueOf(upgradeStr));
		String selectedUpgradeStr = $.scanStringArrayAndSplitBy(lore, "Selected Upgrade: ".toCharArray());
		int selectedUpgradeInt = 0;
		if (!(selectedUpgradeStr == null))
			selectedUpgradeInt = Integer.parseInt(String.valueOf(selectedUpgradeStr));
		Location loc = event.getBlock().getLocation();
		String code = loc.getWorld().getName() + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ());
		Server.getSpawnerConfig().getData().set(code + ".upgrade", upgradeInt + "");
		Server.getSpawnerConfig().getData().set(code + ".selectedUpgrade", selectedUpgradeInt + "");
		Server.getSpawnerConfig().saveData();
		CraftGo.MobSpawner.setSpawnerEntityType(event.getBlock(), type);
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!event.isCancelled())
			event.setCancelled(LinkServer.getInstance().getAntiCheat().onBlockBreak(event.getBlock(), player));
		if (event.isCancelled())
			return;
		Block block = event.getBlock();
		Location blockLoc = block.getLocation();
		Material blockType = block.getType();
		ItemStack item = player.getInventory().getItemInMainHand();
		if (Server.getSkyblock().contains(player.getUniqueId())) {
			if (Directory.pickaxes.contains(player.getInventory().getItemInMainHand().getType())) {
				switch (blockType) {
				case NETHERRACK:
					ExperienceOrb orba = player.getWorld().spawn(player.getEyeLocation(), ExperienceOrb.class);
					orba.setExperience(1);
					return;
				case COBBLESTONE:
					event.setCancelled(true);
					ExperienceOrb orbb = player.getWorld().spawn(player.getEyeLocation(), ExperienceOrb.class);
					orbb.setExperience(3);
					block.setType(Material.AIR);
					block.getState().update();
					player.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), Link$.createMaterial(Material.COBBLESTONE));
					item.setDurability((short) (item.getDurability() + 1));
					if (item.getType().getMaxDurability() == item.getDurability()) {
						player.getInventory().setItemInMainHand(null);
					} else {
						player.getInventory().setItemInMainHand(item);
					}
					player.updateInventory();
					int emerald = new Random(UUID.randomUUID().hashCode()).nextInt(110);
					int diamond = new Random(UUID.randomUUID().hashCode()).nextInt(75);
					int gold = new Random(UUID.randomUUID().hashCode()).nextInt(30);
					int iron = new Random(UUID.randomUUID().hashCode()).nextInt(30);
					int coal = new Random(UUID.randomUUID().hashCode()).nextInt(10);
					boolean anyHit = emerald == 0 || diamond == 0 || gold == 0 || iron == 0 || coal == 0;
					if (anyHit) {
						Material dropItemType = null;
						if (emerald == 0) {
							dropItemType = Material.EMERALD;
						} else if (diamond == 0) {
							dropItemType = Material.DIAMOND;
						} else if (gold == 0) {
							dropItemType = Material.GOLD_INGOT;
						} else if (iron == 0) {
							dropItemType = Material.IRON_INGOT;
						} else if (coal == 0) {
							dropItemType = Material.COAL;
						}
						player.getWorld().dropItem(block.getLocation().add(0.5, 0.5, 0.5), Link$.createMaterial(dropItemType));
						player.playSound(blockLoc, Sound.BLOCK_LAVA_POP, 1, 1);
					}
					break;
				default:
					break;
				}
			}
			$.Skyblock.setPlayerBrokenBlocks(player, $.Skyblock.getPlayerBrokenBlocks(player) + 1);
			$.skyblockScoreboard.schedule(player);
		}
		if (Server.getFactions().contains(player.getUniqueId()) || Server.getSurvival().contains(player.getUniqueId()) || Server.getSkyblock().contains(player.getUniqueId())) {
			if ($.isWithinUnclaimedLand(blockLoc, player)) {
				if ($.isBlockLog(block)) {
					if (Server.getFactions().contains(player.getUniqueId()) || Server.getSkyblock().contains(player.getUniqueId())) {
						ItemStack heldItem = player.getInventory().getItemInMainHand();
						Material type = Material.AIR;
						if (!(heldItem == null))
							type = heldItem.getType();
						if (Directory.axes.contains(type)) {
							if (!Server.getCurrentFellers().contains(player.getUniqueId())) {
								event.setCancelled(true);
								new TreeCutter(player, block).runTaskAsynchronously(Server.getPlugin());
								return;
							}
						}
					}
				}
				if (blockType == Material.SPAWNER && !(player.getGameMode() == GameMode.CREATIVE)) {
					if (!(player.getInventory().getItemInMainHand() == null) && !(player.getInventory().getItemInMainHand().getType() == Material.AIR)) {
						if (player.getInventory().getItemInMainHand().getType() == Material.IRON_PICKAXE || player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE) {
							if (player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
								EntityType entityType = CraftGo.MobSpawner.getSpawnerEntityType(block);
								ItemStack stack = CraftGo.MobSpawner.newSpawnerItem(entityType, 1);
								Location loc = block.getLocation();
								String code = loc.getWorld().getName() + String.valueOf(loc.getBlockX()) + String.valueOf(loc.getBlockY()) + String.valueOf(loc.getBlockZ());
								int upgrade = 0;
								if (Server.getSpawnerConfig().getData().contains(code + ".upgrade"))
									upgrade = Integer.parseInt(Server.getSpawnerConfig().getData().getString(code + ".upgrade"));
								int selectedUpgrade = 0;
								if (Server.getSpawnerConfig().getData().contains(code + ".selectedUpgrade"))
									selectedUpgrade = Integer.parseInt(Server.getSpawnerConfig().getData().getString(code + ".selectedUpgrade"));
								Server.getSpawnerConfig().getData().set(code, null);
								Server.getSpawnerConfig().saveData();
								stack = Link$.appendLore(stack, new String[] { ChatColor.RESET + "Upgrade: " + upgrade, ChatColor.RESET + "Selected Upgrade: " + selectedUpgrade });
								event.setCancelled(true);
								block.setType(Material.AIR);
								block.getState().update();
								Location exactLoc = blockLoc;
								exactLoc.setX(exactLoc.getBlockX() + 0.5);
								exactLoc.setY(exactLoc.getBlockY() + 0.5);
								exactLoc.setZ(exactLoc.getBlockZ() + 0.5);
								player.getWorld().dropItem(exactLoc, stack);
							}
						}
					}
				}
				if ((player.getWorld().getEnvironment() == World.Environment.NETHER) && (blockType == Material.ICE)) {
					event.setCancelled(true);
					block.setType(Material.WATER);
				}
				if (Server.getSkyblock().contains(player.getUniqueId()))
					return;
				if (blockType == Material.TNT && !(player.getGameMode() == GameMode.CREATIVE)) {
					event.setCancelled(true);
					block.setType(Material.AIR);
					block.getState().update();
					player.getWorld().spawn(block.getLocation(), TNTPrimed.class);
				}
				if (!(blockType == null) && (player.getInventory().getItemInMainHand().getType() == Material.IRON_PICKAXE || player.getInventory().getItemInMainHand().getType() == Material.DIAMOND_PICKAXE)) {
					if (player.getInventory().getItemInMainHand().hasItemMeta() && player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.FIRE_ASPECT)) {
						if (!player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) {
							Map<Enchantment, Integer> enchants = player.getInventory().getItemInMainHand().getEnchantments();
							int fireAspectLevel = enchants.get(Enchantment.FIRE_ASPECT);
							boolean win = new Random().nextBoolean();
							if (fireAspectLevel == 2)
								win = true;
							if (win) {
								ItemStack dropItem = null;
								int dropItemAmount = 1;
								if (player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
									int fortuneLevel = enchants.get(Enchantment.LOOT_BONUS_BLOCKS);
									dropItemAmount = new Random().nextInt(fortuneLevel + 1);
									if (dropItemAmount == 0)
										dropItemAmount = 1;
								}
								if (block.getType() == Material.IRON_ORE)
									dropItem = Link$.createMaterial(Material.IRON_INGOT, dropItemAmount);
								if (block.getType() == Material.GOLD_ORE)
									dropItem = Link$.createMaterial(Material.GOLD_INGOT, dropItemAmount);
								if (block.getType() == Material.GLASS || block.getType() == Material.GLASS_PANE)
									dropItem = Link$.createMaterial(block.getType());
								boolean stainedGlassPane = false;
								if ($.isBlockStainedGlass(block) || (stainedGlassPane = $.isBlockStainedGlassPane(block))) {
									Colorable stainedGlass = (Colorable) block.getState().getData();
									DyeColor stainedGlassColor = stainedGlass.getColor();
									if (stainedGlassPane) {
										dropItem = Link$.createMaterial(Material.getMaterial(stainedGlassColor.toString() + "_STAINED_GLASS_PANE"));
									} else {
										dropItem = Link$.createMaterial(Material.getMaterial(stainedGlassColor.toString() + "_STAINED_GLASS"));
									}
								}
								if (!(dropItem == null)) {
									event.setCancelled(true);
									block.setType(Material.AIR);
									block.getState().update();
									Location exactLoc = blockLoc;
									exactLoc.setX(exactLoc.getBlockX() + 0.5);
									exactLoc.setY(exactLoc.getBlockY() + 0.5);
									exactLoc.setZ(exactLoc.getBlockZ() + 0.5);
									player.getWorld().dropItem(exactLoc, dropItem);
								}
							}
						}
					}
				}
			}
		}
		if (blockType == Material.WALL_SIGN | blockType == Material.SIGN) {
			Sign sign = (Sign) event.getBlock().getState();
			String[] s = sign.getLines();
			for (int i = 0; i < s.length; i++) {
				s[i] = ChatColor.stripColor(s[i]);
			}
			if (s[0].equals("Buy") || s[0].equals("Sell") || s[0].equals("Enchant") || s[0].equals("Repair") || s[1].equals("Kit")) {
				String code = blockLoc.getWorld().getName() + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
				if (Server.getSignConfig().getData().contains("signs." + code)) {
					if (!event.getPlayer().isOp()) {
						player.sendMessage(Link$.Legacy.tag + ChatColor.RED + "You do not have permission to deactivate this shop.");
						Link$.playLackPermissionMessage(player);
						event.setCancelled(true);
						return;
					}
					Server.getSignConfig().getData().set("signs." + code, null);
					Server.getSignConfig().saveData();
					if (s[1].equals("Kit")) {
						sign.setLine(1, s[1]);
						sign.update();
					} else {
						sign.setLine(0, s[0]);
						sign.update();
					}
					player.sendMessage(Link$.Legacy.tag + ChatColor.GRAY + "Successfully cleared shop " + ChatColor.RED + code + ChatColor.GRAY + ".");
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onLeavesDecay(LeavesDecayEvent event) {
		if (event.isCancelled())
			return;
		event.getBlock().setType(Material.AIR);
		event.setCancelled(true);
	}

}
