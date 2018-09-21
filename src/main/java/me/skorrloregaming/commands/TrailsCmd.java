package me.skorrloregaming.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class TrailsCmd implements CommandExecutor {
	
	public static void openTrailManagementInventory(Player player) {
		int invSize = 18;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 27;
		Inventory inventory = Bukkit.createInventory(null, invSize, ChatColor.BOLD + "Select or purchase trails!");
		String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
		String path = "config." + player.getUniqueId().toString();
		if (!Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.smoke")) {
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.smoke", "0");
		}
		if (!Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.emerald")) {
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.emerald", "0");
		}
		if (!Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.redstone")) {
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.redstone", "0");
		}
		if (!Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.enchanting")) {
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.enchanting", "0");
		}
		int selectedTrail = -1;
		if (Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.selectedTrail")) {
			selectedTrail = Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.selectedTrail"));
		} else {
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.selectedTrail", "-1");
		}
		HashMap<Integer, Integer> unlocked = new HashMap<>();
		unlocked.put(0, Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.smoke")));
		unlocked.put(1, Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.emerald")));
		unlocked.put(2, Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.redstone")));
		unlocked.put(3, Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.enchanting")));
		ItemStack disableTrails = $.createMaterial(Material.REDSTONE, prefix + "Disable Trails");
		if (selectedTrail == -1) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				disableTrails.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
			} else {
				disableTrails.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			}
			disableTrails = $.addLore(disableTrails, new String[] { ChatColor.RESET + "This is already your preferred trail." });
		}
		inventory.setItem(4, disableTrails);
		for (int i = 10; i < 17; i += 2) {
			String trail = null;
			ChatColor trailItemColor = ChatColor.GOLD;
			if ((i - 10) / 2 == 0) {
				trail = "smoke";
				trailItemColor = ChatColor.GRAY;
			} else if ((i - 10) / 2 == 1) {
				trail = "emerald";
				trailItemColor = ChatColor.GREEN;
			} else if ((i - 10) / 2 == 2) {
				trail = "redstone";
				trailItemColor = ChatColor.RED;
			} else if ((i - 10) / 2 == 3) {
				trail = "enchanting";
				trailItemColor = ChatColor.LIGHT_PURPLE;
			}
			ItemStack trailItem;
			if (unlocked.get((i - 10) / 2) == 1) {
				trailItem = $.createMaterial(Material.LEATHER_BOOTS, prefix + "Select the " + trailItemColor + WordUtils.capitalize(trail) + prefix + " trail");
			} else {
				trailItem = $.createMaterial(Material.IRON_BARS, prefix + "This trail selection is locked :(");
			}
			if (trailItem.getType() == Material.LEATHER_BOOTS) {
				if (i == 10) {
					trailItem = $.addLeatherColor(trailItem, Color.GRAY);
				} else if (i == 12) {
					trailItem = $.addLeatherColor(trailItem, Color.GREEN);
				} else if (i == 14) {
					trailItem = $.addLeatherColor(trailItem, Color.RED);
				} else if (i == 16) {
					trailItem = $.addLeatherColor(trailItem, Color.PURPLE);
				}
			}
			int purchasePrice = ((((i - 10) / 2) + 1) * 50);
			List<String> lore = new ArrayList<>();
			if (selectedTrail == (i - 10) / 2) {
				if (CraftGo.Player.getProtocolVersion(player) > 314) {
					trailItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
				} else {
					trailItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				}
				lore.add(ChatColor.RESET + "This is already your preferred trail.");
				lore.add(ChatColor.GREEN + WordUtils.capitalize(trail) + " trail" + ChatColor.RED + " x1");
				lore.add("");
				lore.add($.pricePrefix + purchasePrice);
			} else {
				lore.add(ChatColor.GREEN + WordUtils.capitalize(trail) + " trail" + ChatColor.RED + " x1");
				lore.add("");
				lore.add($.pricePrefix + purchasePrice);
			}
			ItemMeta meta = trailItem.getItemMeta();
			meta.setLore(lore);
			trailItem.setItemMeta(meta);
			inventory.setItem(i, trailItem);
		}
		player.openInventory(inventory);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getKitpvp().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		openTrailManagementInventory(player);
		return true;
	}

}
