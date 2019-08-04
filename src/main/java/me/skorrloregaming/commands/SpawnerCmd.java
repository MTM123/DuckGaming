package me.skorrloregaming.commands;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.InventoryType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.skorrloregaming.*;

public class SpawnerCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!ServerGet.get().getFactions().contains(player.getUniqueId()) && !ServerGet.get().getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (ServerGet.get().getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		int invSize = 9;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 27;
		Inventory inventory = Bukkit.createInventory(new InventoryMenu(player, InventoryType.SPAWNER_SHOP), invSize, ChatColor.BOLD + "Buy Monster Spawners");
		int add = 0;
		if (CraftGo.Player.isPocketPlayer(player))
			add = 9;
		ItemStack skeletonSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.SKELETON, 1);
		skeletonSpawner = Link$.appendLore(skeletonSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(0)});
		ItemStack zombieSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.ZOMBIE, 1);
		zombieSpawner = Link$.appendLore(zombieSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(1)});
		ItemStack spiderSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.SPIDER, 1);
		spiderSpawner = Link$.appendLore(spiderSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(2)});
		ItemStack blazeSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.BLAZE, 1);
		blazeSpawner = Link$.appendLore(blazeSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(3)});
		ItemStack creeperSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.CREEPER, 1);
		creeperSpawner = Link$.appendLore(creeperSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(4)});
		ItemStack caveSpiderSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.CAVE_SPIDER, 1);
		caveSpiderSpawner = Link$.appendLore(caveSpiderSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(5)});
		ItemStack cowSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.COW, 1);
		cowSpawner = Link$.appendLore(cowSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(6)});
		ItemStack pigSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.PIG, 1);
		pigSpawner = Link$.appendLore(pigSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(7)});
		ItemStack chickenSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.CHICKEN, 1);
		chickenSpawner = Link$.appendLore(chickenSpawner, new String[]{"", $.pricePrefix + ServerGet.get().getSpawnerPrices().get(8)});
		inventory.setItem(0 + add, skeletonSpawner);
		inventory.setItem(1 + add, zombieSpawner);
		inventory.setItem(2 + add, spiderSpawner);
		inventory.setItem(3 + add, blazeSpawner);
		inventory.setItem(4 + add, creeperSpawner);
		inventory.setItem(5 + add, caveSpiderSpawner);
		inventory.setItem(6 + add, cowSpawner);
		inventory.setItem(7 + add, pigSpawner);
		inventory.setItem(8 + add, chickenSpawner);
		player.openInventory(inventory);
		return true;
	}

}
