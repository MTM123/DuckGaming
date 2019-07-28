package me.skorrloregaming;

import me.skorrloregaming.impl.TitleSubtitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.UUID;

public class SignShop {
	public static void playShopTitlePopup(Player player, Block block) {
		if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
			if (player.isSneaking()) {
				Location blockLoc = block.getLocation();
				String code = String.valueOf(blockLoc.getBlockX()) + "," + String.valueOf(blockLoc.getBlockY()) + "," + String.valueOf(blockLoc.getBlockZ());
				TitleSubtitle title = new TitleSubtitle(null, "The code for this shop is " + code + ".", true);
				CraftGo.Player.sendTimedTitleAndSubtitle(player, title);
				player.sendMessage("Refer to the page provided in /mods for more info.");
			}
		}
	}

	public static ArrayList<UUID> simpleDelayedTask = new ArrayList<>();

	public static boolean handle(Sign sign, Player player, String subDomain) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		Location blockLoc = sign.getLocation();
		String code = blockLoc.getWorld().getName() + String.valueOf(blockLoc.getBlockX()) + String.valueOf(blockLoc.getBlockY()) + String.valueOf(blockLoc.getBlockZ());
		if (!Server.getSignConfig().getData().contains("signs." + code)) {
			return false;
		} else {
			playShopTitlePopup(player, sign.getBlock());
		}
		double cash = EconManager.retrieveCash(player, subDomain);
		String tag = $.getMinigameTag(subDomain);
		String[] lines = sign.getLines();
		for (int i = 0; i < lines.length; i++) {
			lines[i] = ChatColor.stripColor(lines[i]);
		}
		if (subDomain.equals("kitpvp")) {
			if (lines[1].equals("Kit")) {
				if (lines[2].equals("Starter")) {
					player.performCommand("kit starter");
				} else if (lines[2].equals("Potions")) {
					player.performCommand("kit potions");
					return true;
				}
			}
		} else if (subDomain.equals("factions")) {
			if (lines[1].equals("Kit")) {
				if (lines[2].equals("Recruit")) {
					player.performCommand("kit recruit");
					return true;
				}
			}
		} else if (subDomain.equals("survival")) {
			if (lines[1].equals("Kit")) {
				if (lines[2].equals("Recruit")) {
					player.performCommand("kit recruit");
					return true;
				}
			}
		}
		if (lines[0].equals("Repair")) {
			int price = Integer.parseInt(String.valueOf(lines[2]).replace("$", "").replace(",", ""));
			int v1 = Integer.parseInt(String.valueOf(lines[1]));
			int v2 = Integer.parseInt(String.valueOf(lines[3]));
			ItemStack itm = player.getInventory().getItemInMainHand();
			if (v1 == 0 && v2 == 1) {
				if (itm.getType() == Material.AIR || itm == null) {
					player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please hold the item you want to repair.");
					return false;
				}
				if (cash >= price) {
					if ($.isRepairable(itm)) {
						if (itm.getDurability() < itm.getType().getMaxDurability() / 2) {
							if (!Server.getConfirmRepairShop().contains(player.getUniqueId())) {
								player.sendMessage(tag + ChatColor.GRAY + "The item you are holding seems to be durable still.");
								player.sendMessage(tag + ChatColor.GRAY + "Use this shop again to confirm your transaction.");
								Server.getConfirmRepairShop().add(player.getUniqueId());
								Bukkit.getScheduler().runTaskLater(Server.getPlugin(), new Runnable() {
									@Override
									public void run() {
										Server.getConfirmRepairShop().remove(player.getUniqueId());
									}
								}, 45L);
								return false;
							}
						}
						EconManager.withdrawCash(player, price, subDomain);
						String materialName = Link$.formatMaterial(itm.getType());
						itm.setDurability((short) 0);
						player.getInventory().setItemInMainHand(itm);
						player.updateInventory();
						player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Repaired " + ChatColor.RED + materialName + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
						return true;
					} else {
						player.sendMessage(tag + ChatColor.RED + "The specified item cannot be repaired.");
					}
					return false;
				}
				player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
				return false;
			} else {
				player.sendMessage("Unsupported parameters {0,,1}");
				return false;
			}
		}
		if (lines[0].equals("Buy")) {
			try {
				Material material = Material.getMaterial(String.valueOf(lines[1]).replace(" ", "_").toUpperCase().split(":")[0]);
				int amount = Integer.parseInt(String.valueOf(lines[3]));
				int price = Integer.parseInt(String.valueOf(lines[2]).replace("$", "").replace(",", ""));
				int data = 0;
				String materialName = Link$.formatMaterial(material);
				try {
					data = Integer.parseInt(String.valueOf(lines[1]).split(":")[1]);
				} catch (Exception ig) {
				}
				if (cash >= price * 2 && player.isSneaking()) {
					amount = amount * 2;
					price = price * 2;
				}
				ItemStack purchaseItem = new ItemStack(material, amount, (short) data);
				if (material == Material.SPAWNER)
					purchaseItem = CraftGo.MobSpawner.newSpawnerItem(CraftGo.MobSpawner.convertEntityIdToEntityType(data), amount);
				if (cash >= price) {
					if (player.getInventory().firstEmpty() == -1) {
						player.sendMessage(tag + ChatColor.RED + "Inventory full. " + ChatColor.GRAY + "Empty some slots then try again.");
						return false;
					}
					EconManager.withdrawCash(player, price, subDomain);
					player.getInventory().addItem(purchaseItem);
					player.updateInventory();
					player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + materialName + " x" + amount + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
					return true;
				}
				player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
				return false;
			} catch (Exception ex) {
				player.sendMessage("An internal error has occured whilist processing this shop information.");
				ex.printStackTrace();
				return false;
			}
		} else if (lines[0].equals("Sell")) {
			try {
				Material material = Material.getMaterial(String.valueOf(lines[1]).replace(" ", "_").toUpperCase().split(":")[0]);
				int amount = Integer.parseInt(String.valueOf(lines[3]));
				int price = Integer.parseInt(String.valueOf(lines[2]).replace("$", "").replace(",", ""));
				int data = 0;
				String materialName = Link$.formatMaterial(material);
				try {
					data = Integer.parseInt(String.valueOf(lines[1]).split(":")[1]);
				} catch (Exception ig) {
				}
				boolean found = false;
				ItemStack itm = player.getInventory().getItemInMainHand();
				if (itm.getType() == Material.AIR || itm == null) {
					player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please hold the item you want to sell.");
					return false;
				}
				short dur = itm.getDurability();
				if (material == Material.SPAWNER)
					dur = (short) CraftGo.MobSpawner.convertEntityTypeToEntityId(CraftGo.MobSpawner.getStoredSpawnerItemEntityType(itm));
				if (dur == (short) data) {
					if (itm != null && itm.getType().equals(material)) {
						if (itm.getAmount() >= (amount * 2) && player.isSneaking()) {
							amount = amount * 2;
							price = price * 2;
						}
						if (itm.getAmount() >= amount) {
							found = true;
							int amt = itm.getAmount() - amount;
							if (amt <= 0) {
								player.getInventory().setItemInMainHand(null);
							} else {
								itm.setAmount(amt);
								player.getInventory().setItemInMainHand(itm);
							}
							player.updateInventory();
							EconManager.depositCash(player, price, subDomain);
							player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Sold " + ChatColor.RED + materialName + " x" + amount + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
						}
					}
				}
				if (!found) {
					player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "Please hold the item you want to sell.");
					return false;
				}
				return true;
			} catch (Exception ex) {
				player.sendMessage("An internal error has occured whilist processing this shop information.");
				ex.printStackTrace();
				return false;
			}
		} else if (lines[0].equals("Enchant")) {
			try {
				Enchantment enchant = Enchantment.getByName(Link$.unformatEnchantment(String.valueOf(lines[1]).trim()));
				int amount = Integer.parseInt(String.valueOf(lines[3]));
				int price = Integer.parseInt(String.valueOf(lines[2]).replace("$", "").replace(",", ""));
				String enchantName = Link$.formatEnchantment(String.valueOf(enchant.getKey().getKey().trim()), amount);
				if (cash >= price) {
					ItemStack currentItem = player.getInventory().getItemInMainHand();
					if (currentItem.getType() == Material.AIR || currentItem == null || currentItem.getType() == null) {
						player.sendMessage(tag + ChatColor.RED + "Failed. " + enchantName + ChatColor.GRAY + " cannot be applied to this item.");
						return false;
					}
					if (currentItem.getEnchantments().containsKey(enchant) && currentItem.getEnchantments().get(enchant) == amount) {
						player.sendMessage(tag + ChatColor.RED + "Failed. " + enchantName + ChatColor.GRAY + " cannot be applied to this item.");
						return false;
					}
					if (currentItem.getType() == Material.BOOK)
						currentItem.setType(Material.ENCHANTED_BOOK);
					try {
						currentItem.addUnsafeEnchantment(enchant, amount);
					} catch (Exception ex) {
						player.sendMessage(tag + ChatColor.RED + "Failed. " + enchantName + ChatColor.GRAY + " cannot be applied to this item.");
						return false;
					}
					EconManager.withdrawCash(player, price, subDomain);
					player.getInventory().setItemInMainHand(currentItem);
					player.updateInventory();
					player.sendMessage(tag + ChatColor.RED + "Success. " + ChatColor.GRAY + "Purchased " + ChatColor.RED + enchantName + ChatColor.GRAY + " for " + ChatColor.RED + "$" + formatter.format(price));
					return true;
				} else {
					player.sendMessage(tag + ChatColor.RED + "Failed. " + ChatColor.GRAY + "You do not have enough money.");
					return false;
				}
			} catch (Exception ex) {
				player.sendMessage("An internal error has occured whilist processing this shop information.");
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
