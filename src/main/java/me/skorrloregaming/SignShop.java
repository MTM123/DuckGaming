/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.EconManager;
import me.skorrloregaming.Go;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SignShop {
	public static void handle(Sign sign, final Player player, String subDomain, Plugin plugin) {
		block57:
		{
			int price;
			int amount;
			Location blockLoc = sign.getLocation();
			String code = String.valueOf(blockLoc.getWorld().getName()) + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
			int cash = EconManager.retrieveCash(player, subDomain, plugin);
			String tag = Go.getMinigameTag(subDomain);
			String[] lines = sign.getLines();
			int i = 0;
			while (i < lines.length) {
				lines[i] = ChatColor.stripColor((String) lines[i]);
				++i;
			}
			if (subDomain.equals("kitpvp")) {
				if (lines[1].equals("Kit")) {
					if (!Server.signConfig.getData().contains("signs." + code)) {
						player.sendMessage("This shop has not been activated by a qualified staff member.");
						return;
					}
					if (lines[2].equals("Starter")) {
						player.performCommand("kit starter");
					} else if (lines[2].equals("Potions")) {
						player.performCommand("kit potions");
					}
				}
			} else if (subDomain.equals("factions")) {
				if (lines[1].equals("Kit")) {
					if (!Server.signConfig.getData().contains("signs." + code)) {
						player.sendMessage("This shop has not been activated by a qualified staff member.");
						return;
					}
					if (lines[2].equals("Recruit")) {
						player.performCommand("kit recruit");
					}
				}
			} else if (subDomain.equals("prison") && lines[1].equals("Kit")) {
				if (!Server.signConfig.getData().contains("signs." + code)) {
					player.sendMessage("This shop has not been activated by a qualified staff member.");
					return;
				}
				if (lines[2].equals("Starter")) {
					player.performCommand("kit starter");
				}
			}
			if (lines[0].equals("Repair")) {
				if (!Server.signConfig.getData().contains("signs." + code)) {
					player.sendMessage("This shop has not been activated by a qualified staff member.");
					return;
				}
				int price2 = Integer.parseInt(String.valueOf(lines[2]));
				int v1 = Integer.parseInt(String.valueOf(lines[1]));
				int v2 = Integer.parseInt(String.valueOf(lines[3]));
				if (v1 == 0 && v2 == 1) {
					if (player.getItemInHand().getType() == Material.AIR || player.getItemInHand() == null) {
						player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Please hold the specified item you want to repair.");
						return;
					}
					if (cash >= price2 - 1) {
						if (Go.isRepairable(player.getItemInHand())) {
							ItemStack itm = player.getItemInHand();
							if (itm.getDurability() < itm.getType().getMaxDurability() / 2 && !Server.confirmRepairShop.contains(player.getUniqueId())) {
								player.sendMessage(String.valueOf(tag) +  ChatColor.GRAY + "The item you are holding seems to be vary durable still.");
								player.sendMessage(String.valueOf(tag) +  ChatColor.GRAY + "Use this shop again to confirm your transaction.");
								Server.confirmRepairShop.add(player.getUniqueId());
								Bukkit.getScheduler().runTaskLater(plugin, new BukkitRunnable() {

									public void run() {
										Server.confirmRepairShop.remove(player.getUniqueId());
									}
								}, 45L);
								return;
							}
							EconManager.withdrawCash(player, price2, subDomain, plugin);
							String materialName = Go.formatMaterial(itm.getType());
							itm.setDurability((short) 0);
							player.setItemInHand(itm);
							player.updateInventory();
							Logger.info(String.valueOf(tag) + player.getName() + " repaired " + materialName + " for $" + price2);
							player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Success. " +  ChatColor.GRAY + "Repaired " +  ChatColor.RED + materialName +  ChatColor.GRAY + " for " +  ChatColor.RED + "$" + price2);
						} else {
							player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "The specified item cannot be repaired.");
						}
						return;
					}
					player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "You do not have enough money to buy this item.");
					return;
				}
				player.sendMessage("Unsupported parameters {0,,1}");
			}
			if (lines[0].equals("Buy")) {
				try {
					if (!Server.signConfig.getData().contains("signs." + code)) {
						player.sendMessage("This shop has not been activated by a qualified staff member.");
						return;
					}
					Material material = Material.getMaterial((int) Integer.parseInt(String.valueOf(lines[1]).split(":")[0]));
					amount = Integer.parseInt(String.valueOf(lines[3]));
					price = Integer.parseInt(String.valueOf(lines[2]));
					int data = 0;
					String materialName = Go.formatMaterial(material);
					try {
						data = Integer.parseInt(String.valueOf(lines[1]).split(":")[1]);
					} catch (Exception exception) {
						// empty catch block
					}
					if (cash >= price * 2 - 1 && player.isSneaking()) {
						amount *= 2;
						price *= 2;
					}
					ItemStack item = new ItemStack(material, amount, (short) data);
					if (material == Material.MOB_SPAWNER) {
						item = CraftGo.MobSpawner.newSpawnerItem((short) data, amount);
					}
					if (cash >= price - 1) {
						if (player.getInventory().firstEmpty() == -1) {
							player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Inventory full. " +  ChatColor.GRAY + "Empty some slots then try again.");
							return;
						}
						EconManager.withdrawCash(player, price, subDomain, plugin);
						player.getInventory().addItem(new ItemStack[]{item});
						player.updateInventory();
						Logger.info(String.valueOf(tag) + player.getName() + " purchased " + materialName + " x" + amount + " for $" + price);
						player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Success. " +  ChatColor.GRAY + "Purchased " +  ChatColor.RED + materialName + " x" + amount +  ChatColor.GRAY + " for " +  ChatColor.RED + "$" + price);
						return;
					}
					player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Failed. " +  ChatColor.GRAY + "You do not have enough money in your account.");
					return;
				} catch (Exception ex) {
					player.sendMessage("An internal error has occured whilist processing this shop information.");
					ex.printStackTrace();
					return;
				}
			}
			if (lines[0].equals("Sell")) {
				try {
					if (!Server.signConfig.getData().contains("signs." + code)) {
						player.sendMessage("This shop has not been activated by a qualified staff member.");
						return;
					}
					Material material = Material.getMaterial((int) Integer.parseInt(String.valueOf(lines[1]).split(":")[0]));
					amount = Integer.parseInt(String.valueOf(lines[3]));
					price = Integer.parseInt(String.valueOf(lines[2]));
					int data = 0;
					String materialName = Go.formatMaterial(material);
					try {
						data = Integer.parseInt(String.valueOf(lines[1]).split(":")[1]);
					} catch (Exception item) {
						// empty catch block
					}
					boolean found = false;
					if (player.getItemInHand().getType() == Material.AIR || player.getItemInHand() == null) {
						player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Please hold the specified item you want to sell.");
						return;
					}
					ItemStack itm = player.getInventory().getItemInHand();
					short dur = itm.getDurability();
					if (material == Material.MOB_SPAWNER) {
						dur = CraftGo.MobSpawner.getStoredSpawnerItemEntityID(itm);
					}
					if (dur == (short) data && itm != null && itm.getType().equals( material)) {
						if (itm.getAmount() >= amount * 2 && player.isSneaking()) {
							amount *= 2;
							price *= 2;
						}
						if (itm.getAmount() >= amount) {
							found = true;
							int amt = itm.getAmount() - amount;
							if (amt <= 0) {
								player.getInventory().setItemInHand(null);
							} else {
								itm.setAmount(amt);
								player.getInventory().setItemInHand(itm);
							}
							player.updateInventory();
							EconManager.depositCash(player, price, subDomain, plugin);
							Logger.info(String.valueOf(tag) + player.getName() + " sold " + materialName + " x" + amount + " for $" + price);
							player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Success. " +  ChatColor.GRAY + "Sold " +  ChatColor.RED + materialName + " x" + amount +  ChatColor.GRAY + " for " +  ChatColor.RED + "$" + price);
						}
					}
					if (!found) {
						player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Please hold the specified item you want to sell.");
						return;
					}
					break block57;
				} catch (Exception ex) {
					player.sendMessage("An internal error has occured whilist processing this shop information.");
					ex.printStackTrace();
					return;
				}
			}
			if (lines[0].equals("Enchant")) {
				try {
					if (!Server.signConfig.getData().contains("signs." + code)) {
						player.sendMessage("This shop has not been activated by a qualified staff member.");
						return;
					}
					Enchantment enchant = Enchantment.getById((int) Integer.parseInt(String.valueOf(lines[1])));
					amount = Integer.parseInt(String.valueOf(lines[3]));
					price = Integer.parseInt(String.valueOf(lines[2]));
					String enchantName = Go.formatEnchantment(String.valueOf(enchant.getName()), amount);
					if (cash >= price - 1) {
						ItemStack currentItem = player.getItemInHand();
						if (player.getItemInHand().getType() == Material.AIR || player.getItemInHand() == null || player.getItemInHand().getType() == null) {
							player.sendMessage(String.valueOf(tag) +  ChatColor.GRAY + "The enchantment " +  ChatColor.RED + enchantName +  ChatColor.GRAY + " cannot be applied to this item.");
							return;
						}
						if (currentItem.getEnchantments().containsKey( enchant) && (Integer) currentItem.getEnchantments().get( enchant) == amount) {
							player.sendMessage(String.valueOf(tag) +  ChatColor.GRAY + "The enchantment " +  ChatColor.RED + enchantName +  ChatColor.GRAY + " cannot be applied to this item.");
							return;
						}
						try {
							currentItem.addUnsafeEnchantment(enchant, amount);
						} catch (Exception ex) {
							player.sendMessage(String.valueOf(tag) +  ChatColor.GRAY + "The enchantment " +  ChatColor.RED + enchantName +  ChatColor.GRAY + " cannot be applied to this item.");
							return;
						}
						EconManager.withdrawCash(player, price, subDomain, plugin);
						player.setItemInHand(currentItem);
						player.updateInventory();
						Logger.info(String.valueOf(tag) + player.getName() + " purchased " + enchantName + " for $" + price);
						player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "Success. " +  ChatColor.GRAY + "Purchased " +  ChatColor.RED + enchantName +  ChatColor.GRAY + " for " +  ChatColor.RED + "$" + price);
						return;
					}
					player.sendMessage(String.valueOf(tag) +  ChatColor.RED + "You do not have enough money in your account.");
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

