package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrailsCmd implements CommandExecutor {

	public static void openTrailManagementInventory(Player player) {
		if (Link$.getDonorRankId(player) > -2) {
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
			player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Sorry, you need a donor rank to use cosmetics.");
			player.performCommand("store");
			return;
		}
		int invSize = 18;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 27;
		Inventory inventory = Bukkit.createInventory(null, invSize, ChatColor.BOLD + "Select or purchase trails!");
		String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
		String path = "config." + player.getUniqueId().toString();
		int selectedTrail = -1;
		if (Server.getPlugin().getConfig().contains(path + ".kitpvp.trails.selectedTrail")) {
			selectedTrail = Integer.parseInt(Server.getPlugin().getConfig().getString(path + ".kitpvp.trails.selectedTrail"));
		} else {
			Server.getPlugin().getConfig().set(path + ".kitpvp.trails.selectedTrail", "-1");
		}
		ItemStack disableTrails = Link$.createMaterial(Material.REDSTONE, prefix + "Disable Trails");
		if (selectedTrail == -1) {
			if (CraftGo.Player.getProtocolVersion(player) > 314) {
				disableTrails.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
			} else {
				disableTrails.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			}
			disableTrails = Link$.addLore(disableTrails, new String[]{ChatColor.RESET + "This is already your preferred trail."});
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
			ItemStack trailItem = Link$.createMaterial(Material.LEATHER_BOOTS, prefix + "Select the " + trailItemColor + WordUtils.capitalize(trail) + prefix + " trail");
			if (trailItem.getType() == Material.LEATHER_BOOTS) {
				if (i == 10) {
					trailItem = Link$.addLeatherColor(trailItem, Color.GRAY);
				} else if (i == 12) {
					trailItem = Link$.addLeatherColor(trailItem, Color.GREEN);
				} else if (i == 14) {
					trailItem = Link$.addLeatherColor(trailItem, Color.RED);
				} else if (i == 16) {
					trailItem = Link$.addLeatherColor(trailItem, Color.PURPLE);
				}
			}
			if (selectedTrail == (i - 10) / 2) {
				if (CraftGo.Player.getProtocolVersion(player) > 314) {
					trailItem.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
				} else {
					trailItem.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
				}
				List<String> lore = new ArrayList<>();
				ItemMeta meta = trailItem.getItemMeta();
				lore.add(ChatColor.RESET + "This is already your preferred trail.");
				meta.setLore(lore);
				trailItem.setItemMeta(meta);
			}
			inventory.setItem(i, trailItem);
		}
		player.openInventory(inventory);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		if (Link$.getDonorRankId(player) > -2) {
			player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);
			player.sendMessage($.Kitpvp.tag + ChatColor.RED + "Sorry, you need a donor rank to use cosmetics.");
			player.performCommand("store");
			return true;
		}
		openTrailManagementInventory(player);
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		return true;
	}

}
