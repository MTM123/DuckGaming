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
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "elytra");
			ItemStack stack = Link$.createMaterial(Material.ELYTRA, ChatColor.RESET + "Elytra");
			ShapedRecipe elytra = new ShapedRecipe(key, stack);
			elytra.shape("ABA", "BDB", "BCB");
			elytra.setIngredient('A', Material.AIR);
			elytra.setIngredient('B', Material.LEATHER);
			elytra.setIngredient('C', Material.ENDER_PEARL);
			elytra.setIngredient('D', Material.DIAMOND);
			Bukkit.getServer().addRecipe(elytra);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "packed_ice");
			ShapelessRecipe packedIce = new ShapelessRecipe(key, Link$.createMaterial(Material.PACKED_ICE));
			packedIce.addIngredient(4, Material.ICE);
			Bukkit.getServer().addRecipe(packedIce);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "ender_eye");
			ShapedRecipe endPortal = new ShapedRecipe(key, Link$.createMaterial(Material.ENDER_EYE, ChatColor.LIGHT_PURPLE + "End Portal"));
			endPortal.shape("BCB", "BDB", "AAA");
			endPortal.setIngredient('A', Material.OBSIDIAN);
			endPortal.setIngredient('B', Material.GLASS);
			endPortal.setIngredient('C', Material.ENDER_PEARL);
			endPortal.setIngredient('D', Material.NETHER_STAR);
			Bukkit.getServer().addRecipe(endPortal);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_apple");
			ShapedRecipe goldenApple = new ShapedRecipe(key, Link$.createMaterial(Material.GOLDEN_APPLE));
			goldenApple.shape("BBB", "BAB", "BBB");
			goldenApple.setIngredient('A', Material.APPLE);
			goldenApple.setIngredient('B', Material.GOLD_INGOT);
			Bukkit.getServer().addRecipe(goldenApple);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "enchanted_golden_apple");
			ShapedRecipe notchApple = new ShapedRecipe(key, Link$.createMaterial(Material.ENCHANTED_GOLDEN_APPLE));
			notchApple.shape("BBB", "BAB", "BBB");
			notchApple.setIngredient('A', Material.APPLE);
			notchApple.setIngredient('B', Material.GOLD_BLOCK);
			Bukkit.getServer().addRecipe(notchApple);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "funpowder");
			ShapelessRecipe sulphur = new ShapelessRecipe(key, Link$.createMaterial(Material.GUNPOWDER, 1));
			sulphur.addIngredient(1, Material.FLINT);
			Bukkit.getServer().addRecipe(sulphur);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "explosive_funpowder");
			ShapelessRecipe explosiveSulphur0 = new ShapelessRecipe(key, Link$.createMaterial(Material.GUNPOWDER, 1, ChatColor.LIGHT_PURPLE + "Explosive Gunpowder"));
			explosiveSulphur0.addIngredient(4, Material.GUNPOWDER);
			Bukkit.getServer().addRecipe(explosiveSulphur0);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "stone");
			ShapelessRecipe stone = new ShapelessRecipe(key, Link$.createMaterial(Material.STONE, 3));
			stone.addIngredient(3, Material.COBBLESTONE);
			stone.addIngredient(1, Material.COAL);
			Bukkit.getServer().addRecipe(stone);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "monster_vial");
			ShapelessRecipe vial0 = new ShapelessRecipe(key, Link$.createMaterial(Material.POTION, 1, ChatColor.RESET + "Monster Vial", (short) 0, new String[]{ChatColor.RESET + "" + ChatColor.WHITE + "Capture any mob into a spawn egg using this vial."}));
			vial0.addIngredient(1, Material.GLASS_BOTTLE);
			Bukkit.getServer().addRecipe(vial0);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "glass_bottle");
			ShapelessRecipe vial1 = new ShapelessRecipe(key, Link$.createMaterial(Material.GLASS_BOTTLE));
			vial1.addIngredient(1, Material.POTION);
			Bukkit.getServer().addRecipe(vial1);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_helmet");
			ShapedRecipe chainHelmet = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_HELMET));
			chainHelmet.shape("BBB", "BAB", "AAA");
			chainHelmet.setIngredient('A', Material.AIR);
			chainHelmet.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainHelmet);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_chestplate");
			ShapedRecipe chainChestplate = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_CHESTPLATE));
			chainChestplate.shape("BAB", "BBB", "BBB");
			chainChestplate.setIngredient('A', Material.AIR);
			chainChestplate.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainChestplate);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_leggings");
			ShapedRecipe chainLeggings = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_LEGGINGS));
			chainLeggings.shape("BBB", "BAB", "BAB");
			chainLeggings.setIngredient('A', Material.AIR);
			chainLeggings.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainLeggings);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "chainmail_boots");
			ShapedRecipe chainBoots = new ShapedRecipe(key, Link$.createMaterial(Material.CHAINMAIL_BOOTS));
			chainBoots.shape("BAB", "BAB");
			chainBoots.setIngredient('A', Material.AIR);
			chainBoots.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainBoots);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "cobblestone");
			ShapelessRecipe stoneBrickToCobble = new ShapelessRecipe(key, Link$.createMaterial(Material.COBBLESTONE, 3));
			stoneBrickToCobble.addIngredient(1, Material.STONE_BRICKS);
			Bukkit.getServer().addRecipe(stoneBrickToCobble);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_sword_uncrafting");
			ShapelessRecipe diamond0 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 2));
			diamond0.addIngredient(1, Material.DIAMOND_SWORD);
			Bukkit.getServer().addRecipe(diamond0);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_pickaxe_uncrafting");
			ShapelessRecipe diamond1 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 3));
			diamond1.addIngredient(1, Material.DIAMOND_PICKAXE);
			Bukkit.getServer().addRecipe(diamond1);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_axe_uncrafting");
			ShapelessRecipe diamond2 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 3));
			diamond2.addIngredient(1, Material.DIAMOND_AXE);
			Bukkit.getServer().addRecipe(diamond2);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_shovel_uncrafting");
			ShapelessRecipe diamond3 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 1));
			diamond3.addIngredient(1, Material.DIAMOND_SHOVEL);
			Bukkit.getServer().addRecipe(diamond3);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_helmet_uncrafting");
			ShapelessRecipe diamond4 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 5));
			diamond4.addIngredient(1, Material.DIAMOND_HELMET);
			Bukkit.getServer().addRecipe(diamond4);
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
			ShapelessRecipe diamond6 = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 7));
			diamond6.addIngredient(1, Material.DIAMOND_LEGGINGS);
			Bukkit.getServer().addRecipe(diamond6);
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
			ShapelessRecipe iron0 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 2));
			iron0.addIngredient(1, Material.IRON_SWORD);
			Bukkit.getServer().addRecipe(iron0);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_pickaxe_uncrafting");
			ShapelessRecipe iron1 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 3));
			iron1.addIngredient(1, Material.IRON_PICKAXE);
			Bukkit.getServer().addRecipe(iron1);
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
			ShapelessRecipe iron3 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 1));
			iron3.addIngredient(1, Material.IRON_SHOVEL);
			Bukkit.getServer().addRecipe(iron3);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_helmet_uncrafting");
			ShapelessRecipe iron4 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 5));
			iron4.addIngredient(1, Material.IRON_HELMET);
			Bukkit.getServer().addRecipe(iron4);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_chestplate_uncrafting");
			ShapelessRecipe iron5 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 8));
			iron5.addIngredient(1, Material.IRON_CHESTPLATE);
			Bukkit.getServer().addRecipe(iron5);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_leggings_uncrafting");
			ShapelessRecipe iron6 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 7));
			iron6.addIngredient(1, Material.IRON_LEGGINGS);
			Bukkit.getServer().addRecipe(iron6);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_boots_uncrafting");
			ShapelessRecipe iron7 = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 4));
			iron7.addIngredient(1, Material.IRON_BOOTS);
			Bukkit.getServer().addRecipe(iron7);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_sword_uncrafting");
			ShapelessRecipe gold0 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 2));
			gold0.addIngredient(1, Material.GOLDEN_SWORD);
			Bukkit.getServer().addRecipe(gold0);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_pickaxe_uncrafting");
			ShapelessRecipe gold1 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 3));
			gold1.addIngredient(1, Material.GOLDEN_PICKAXE);
			Bukkit.getServer().addRecipe(gold1);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_axe_uncrafting");
			ShapelessRecipe gold2 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 3));
			gold2.addIngredient(1, Material.GOLDEN_AXE);
			Bukkit.getServer().addRecipe(gold2);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_shovel_uncrafting");
			ShapelessRecipe gold3 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 1));
			gold3.addIngredient(1, Material.GOLDEN_SHOVEL);
			Bukkit.getServer().addRecipe(gold3);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_helmet_uncrafting");
			ShapelessRecipe gold4 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 5));
			gold4.addIngredient(1, Material.GOLDEN_HELMET);
			Bukkit.getServer().addRecipe(gold4);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_chestplate_uncrafting");
			ShapelessRecipe gold5 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 8));
			gold5.addIngredient(1, Material.GOLDEN_CHESTPLATE);
			Bukkit.getServer().addRecipe(gold5);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_leggings_uncrafting");
			ShapelessRecipe gold6 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 7));
			gold6.addIngredient(1, Material.GOLDEN_LEGGINGS);
			Bukkit.getServer().addRecipe(gold6);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_boots_uncrafting");
			ShapelessRecipe gold7 = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 4));
			gold7.addIngredient(1, Material.GOLDEN_BOOTS);
			Bukkit.getServer().addRecipe(gold7);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "iron_horse_armor_uncrafting");
			ShapelessRecipe ironBarding = new ShapelessRecipe(key, Link$.createMaterial(Material.IRON_INGOT, 6));
			ironBarding.addIngredient(1, Material.IRON_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(ironBarding);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "diamond_horse_armor_uncrafting");
			ShapelessRecipe diamondBarding = new ShapelessRecipe(key, Link$.createMaterial(Material.DIAMOND, 6));
			diamondBarding.addIngredient(1, Material.DIAMOND_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(diamondBarding);
			log(key);
		}
		{
			NamespacedKey key = new NamespacedKey(Server.getPlugin(), "golden_horse_armor_uncrafting");
			ShapelessRecipe goldBarding = new ShapelessRecipe(key, Link$.createMaterial(Material.GOLD_INGOT, 6));
			goldBarding.addIngredient(1, Material.GOLDEN_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(goldBarding);
			log(key);
		}
	}
}
