package me.skorrloregaming;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.UUID;

public class CustomRecipes {

	public static void log(NamespacedKey key) {
		Logger.info("Registered crafting recipe " + key.toString());
	}

	public static void loadRecipes() {
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "shulker_box");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.WHITE_SHULKER_BOX));
			recipe.shape("AAA", "ABA", "AAA");
			recipe.setIngredient('A', Material.DIAMOND);
			recipe.setIngredient('B', Material.ENDER_CHEST);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "elytra");
			ItemStack stack = Link$.createMaterial(Material.ELYTRA, ChatColor.RESET + "Elytra");
			ShapedRecipe recipe = new ShapedRecipe(key, stack);
			recipe.shape("ABA", "BDB", "BCB");
			recipe.setIngredient('A', Material.AIR);
			recipe.setIngredient('B', Material.LEATHER);
			recipe.setIngredient('C', Material.ENDER_PEARL);
			recipe.setIngredient('D', Material.DIAMOND);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "packed_ice");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.PACKED_ICE));
			recipe.addIngredient(4, Material.ICE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "ender_eye");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.ENDER_EYE, ChatColor.LIGHT_PURPLE + "End Portal"));
			recipe.shape("BCB", "BDB", "AAA");
			recipe.setIngredient('A', Material.OBSIDIAN);
			recipe.setIngredient('B', Material.GLASS);
			recipe.setIngredient('C', Material.ENDER_PEARL);
			recipe.setIngredient('D', Material.NETHER_STAR);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_apple");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.GOLDEN_APPLE));
			recipe.shape("BBB", "BAB", "BBB");
			recipe.setIngredient('A', Material.APPLE);
			recipe.setIngredient('B', Material.GOLD_INGOT);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "enchanted_golden_apple");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.ENCHANTED_GOLDEN_APPLE));
			recipe.shape("BBB", "BAB", "BBB");
			recipe.setIngredient('A', Material.APPLE);
			recipe.setIngredient('B', Material.GOLD_BLOCK);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "funpowder");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GUNPOWDER, 1));
			recipe.addIngredient(1, Material.FLINT);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "explosive_funpowder");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GUNPOWDER, 1, ChatColor.LIGHT_PURPLE + "Explosive Gunpowder"));
			recipe.addIngredient(4, Material.GUNPOWDER);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "stone");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.STONE, 3));
			recipe.addIngredient(3, Material.COBBLESTONE);
			recipe.addIngredient(1, Material.COAL);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "monster_vial");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.POTION, 1, ChatColor.RESET + "Monster Vial", (short) 0, new String[]{ChatColor.RESET + "" + ChatColor.WHITE + "Capture any mob into a spawn egg using this vial."}));
			recipe.addIngredient(1, Material.GLASS_BOTTLE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "glass_bottle");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GLASS_BOTTLE));
			recipe.addIngredient(1, Material.POTION);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_helmet");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_HELMET));
			recipe.shape("BBB", "BAB", "AAA");
			recipe.setIngredient('A', Material.AIR);
			recipe.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_chestplate");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_CHESTPLATE));
			recipe.shape("BAB", "BBB", "BBB");
			recipe.setIngredient('A', Material.AIR);
			recipe.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_leggings");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_LEGGINGS));
			recipe.shape("BBB", "BAB", "BAB");
			recipe.setIngredient('A', Material.AIR);
			recipe.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_boots");
			ShapedRecipe recipe = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_BOOTS));
			recipe.shape("BAB", "BAB");
			recipe.setIngredient('A', Material.AIR);
			recipe.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "cobblestone");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.COBBLESTONE, 3));
			recipe.addIngredient(1, Material.STONE_BRICKS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_sword_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 2));
			recipe.addIngredient(1, Material.DIAMOND_SWORD);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_pickaxe_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 3));
			recipe.addIngredient(1, Material.DIAMOND_PICKAXE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_axe_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 3));
			recipe.addIngredient(1, Material.DIAMOND_AXE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_shovel_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 1));
			recipe.addIngredient(1, Material.DIAMOND_SHOVEL);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_helmet_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 5));
			recipe.addIngredient(1, Material.DIAMOND_HELMET);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_chestplate_uncrafting");
			ShapelessRecipe diamond5 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 8));
			diamond5.addIngredient(1, Material.DIAMOND_CHESTPLATE);
			Bukkit.getServer().addRecipe(diamond5);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_leggings_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 7));
			recipe.addIngredient(1, Material.DIAMOND_LEGGINGS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_boots_uncrafting");
			ShapelessRecipe diamond7 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 4));
			diamond7.addIngredient(1, Material.DIAMOND_BOOTS);
			Bukkit.getServer().addRecipe(diamond7);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_sword_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 2));
			recipe.addIngredient(1, Material.IRON_SWORD);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_pickaxe_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 3));
			recipe.addIngredient(1, Material.IRON_PICKAXE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_axe_uncrafting");
			ShapelessRecipe iron2 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 3));
			iron2.addIngredient(1, Material.IRON_AXE);
			Bukkit.getServer().addRecipe(iron2);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_shovel_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 1));
			recipe.addIngredient(1, Material.IRON_SHOVEL);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_helmet_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 5));
			recipe.addIngredient(1, Material.IRON_HELMET);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_chestplate_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 8));
			recipe.addIngredient(1, Material.IRON_CHESTPLATE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_leggings_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 7));
			recipe.addIngredient(1, Material.IRON_LEGGINGS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_boots_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 4));
			recipe.addIngredient(1, Material.IRON_BOOTS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_sword_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 2));
			recipe.addIngredient(1, Material.GOLDEN_SWORD);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_pickaxe_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 3));
			recipe.addIngredient(1, Material.GOLDEN_PICKAXE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_axe_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 3));
			recipe.addIngredient(1, Material.GOLDEN_AXE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_shovel_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 1));
			recipe.addIngredient(1, Material.GOLDEN_SHOVEL);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_helmet_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 5));
			recipe.addIngredient(1, Material.GOLDEN_HELMET);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_chestplate_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 8));
			recipe.addIngredient(1, Material.GOLDEN_CHESTPLATE);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_leggings_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 7));
			recipe.addIngredient(1, Material.GOLDEN_LEGGINGS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_boots_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 4));
			recipe.addIngredient(1, Material.GOLDEN_BOOTS);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_horse_armor_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 6));
			recipe.addIngredient(1, Material.IRON_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_horse_armor_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 6));
			recipe.addIngredient(1, Material.DIAMOND_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_horse_armor_uncrafting");
			ShapelessRecipe recipe = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 6));
			recipe.addIngredient(1, Material.GOLDEN_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(recipe);
			log(key);
		}
	}
}
