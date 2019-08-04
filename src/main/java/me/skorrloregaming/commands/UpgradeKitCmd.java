package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.InventoryType;
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

import me.skorrloregaming.*;

public class UpgradeKitCmd implements CommandExecutor {

	public static void openKitUpgradeInventory(Player player) {
		Inventory inventory = Bukkit.createInventory(new InventoryMenu(player, InventoryType.KIT_UPGRADES), 27, ChatColor.BOLD + "Select or upgrade your kit!");
		int upgradeCount = $.Kitpvp.getUpgradeCount(player);
		int requiredAmount = 150 * ($.Kitpvp.getUpgradeCount(player) + 1);
		String prefix = ChatColor.RESET + "" + ChatColor.BOLD;
		int preferredWeaponType = $.Kitpvp.getPreferredWeaponType(player);
		ItemStack[] weapons = new ItemStack[]{$.Kitpvp.getUpgradeClassWeapon(player, $.Kitpvp.getPreferredUpgrade(player), true), null};
		if (weapons[0].getType() == Material.STONE_AXE) {
			weapons[1] = Link$.createMaterial(Material.STONE_SWORD);
		} else if (weapons[0].getType() == Material.IRON_AXE) {
			weapons[1] = Link$.createMaterial(Material.IRON_SWORD);
		} else if (weapons[0].getType() == Material.IRON_AXE) {
			weapons[1] = Link$.createMaterial(Material.DIAMOND_SWORD);
		}
		ItemMeta axeMeta = weapons[0].getItemMeta();
		axeMeta.setDisplayName(prefix + "Select preferred weapon #1");
		weapons[0].setItemMeta(axeMeta);
		ItemMeta swordMeta = weapons[1].getItemMeta();
		swordMeta.setDisplayName(prefix + "Select preferred weapon #2");
		weapons[1].setItemMeta(swordMeta);
		int weaponPreference = -1;
		int altWeaponPreference = -1;
		if (preferredWeaponType == $.Kitpvp.WEAPON_AXE) {
			weaponPreference = 0;
			altWeaponPreference = 1;
		} else if (preferredWeaponType == $.Kitpvp.WEAPON_SWORD) {
			weaponPreference = 1;
			altWeaponPreference = 0;
		}
		List<String> preferredWeaponLore = new ArrayList<String>();
		if (CraftGo.Player.getProtocolVersion(player) > 314) {
			weapons[weaponPreference].addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
		} else {
			weapons[weaponPreference].addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
		}
		preferredWeaponLore.add(ChatColor.RESET + "This is already your preferred weapon.");
		if (preferredWeaponLore.size() > 0)
			weapons[weaponPreference] = Link$.addLore(weapons[weaponPreference], preferredWeaponLore.toArray(new String[0]));
		for (Enchantment enchant : weapons[altWeaponPreference].getEnchantments().keySet()) {
			weapons[altWeaponPreference].removeEnchantment(enchant);
		}
		inventory.setItem(2, weapons[0]);
		inventory.setItem(4, Link$.createMaterial(Material.REDSTONE, prefix + "Perform Upgrade (" + ChatColor.RED + "$" + requiredAmount + prefix + ")"));
		inventory.setItem(6, weapons[1]);
		int passes = 0;
		for (int i = 10; i < 26; i += 2) {
			if (i == 18)
				i += 1;
			passes++;
			List<String> lore = new ArrayList<String>();
			ItemStack item;
			if (upgradeCount >= passes - 1) {
				item = Link$.createMaterial(Material.LEATHER_CHESTPLATE, prefix + "Select kit upgrade #" + passes);
				if (i > 17)
					item = Link$.addLeatherColor(item, Color.YELLOW);
				if (passes == $.Kitpvp.getPreferredUpgrade(player) + 1) {
					if (CraftGo.Player.getProtocolVersion(player) > 314) {
						item.addUnsafeEnchantment(Enchantment.BINDING_CURSE, 1);
					} else {
						item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					}
					lore.add(ChatColor.RESET + "This kit is already your preferred kit.");
				}
			} else {
				item = Link$.createMaterial(Material.IRON_BARS, prefix + "This kit upgrade is locked :(");
			}
			for (ItemStack kitItem : $.Kitpvp.getUpgradeClass(player, passes - 1, false)) {
				lore.add(ChatColor.GREEN + Link$.formatMaterial(kitItem.getType()) + ChatColor.RED + " x" + kitItem.getAmount());
			}
			if (lore.size() > 0)
				item = Link$.addLore(item, lore.toArray(new String[0]));
			inventory.setItem(i, item);
		}
		player.openInventory(inventory);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!ServerGet.get().getKitpvp().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		openKitUpgradeInventory(player);
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		return true;
	}

}
