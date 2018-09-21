package me.skorrloregaming.commands;

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

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;

public class SpawnerCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (!Server.getFactions().contains(player.getUniqueId()) && !Server.getSkyblock().contains(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "This minigame prevents use of this command.");
			return true;
		}
		if (Server.getPlayersInCombat().containsKey(player.getUniqueId())) {
			player.sendMessage($.getMinigameTag(player) + ChatColor.RED + "You cannot use this command during combat.");
			return true;
		}
		player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
		int invSize = 9;
		if (CraftGo.Player.isPocketPlayer(player))
			invSize = 27;
		Inventory inventory = Bukkit.createInventory(null, invSize, ChatColor.BOLD + "Buy Monster Spawners");
		int add = 0;
		if (CraftGo.Player.isPocketPlayer(player))
			add = 9;
		ItemStack skeletonSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.SKELETON, 1);
		skeletonSpawner = $.appendLore(skeletonSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(0) });
		ItemStack zombieSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.ZOMBIE, 1);
		zombieSpawner = $.appendLore(zombieSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(1) });
		ItemStack spiderSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.SPIDER, 1);
		spiderSpawner = $.appendLore(spiderSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(2) });
		ItemStack blazeSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.BLAZE, 1);
		blazeSpawner = $.appendLore(blazeSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(3) });
		ItemStack creeperSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.CREEPER, 1);
		creeperSpawner = $.appendLore(creeperSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(4) });
		ItemStack golemSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.IRON_GOLEM, 1);
		golemSpawner = $.appendLore(golemSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(5) });
		ItemStack cowSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.COW, 1);
		cowSpawner = $.appendLore(cowSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(6) });
		ItemStack pigSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.PIG, 1);
		pigSpawner = $.appendLore(pigSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(7) });
		ItemStack chickenSpawner = CraftGo.MobSpawner.newSpawnerItem(EntityType.CHICKEN, 1);
		chickenSpawner = $.appendLore(chickenSpawner, new String[] { "", $.pricePrefix + Server.getSpawnerPrices().get(8) });
		inventory.setItem(0 + add, skeletonSpawner);
		inventory.setItem(1 + add, zombieSpawner);
		inventory.setItem(2 + add, spiderSpawner);
		inventory.setItem(3 + add, blazeSpawner);
		inventory.setItem(4 + add, creeperSpawner);
		inventory.setItem(5 + add, golemSpawner);
		inventory.setItem(6 + add, cowSpawner);
		inventory.setItem(7 + add, pigSpawner);
		inventory.setItem(8 + add, chickenSpawner);
		player.openInventory(inventory);
		return true;
	}

}
