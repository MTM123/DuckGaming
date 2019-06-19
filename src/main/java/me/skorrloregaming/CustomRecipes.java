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
	private static NamespacedKey randomKey() {
		return new NamespacedKey(Server.getPlugin(), UUID.randomUUID().toString().replace("-", ""));
	}

	public static void loadRecipes() {
		{
			ItemStack stack = Link$.createMaterial(Material.ELYTRA, ChatColor.RESET + "Elytra");
			ShapedRecipe elytra = new ShapedRecipe(randomKey(), stack);
			elytra.shape("ABA", "BDB", "BCB");
			elytra.setIngredient('A', Material.AIR);
			elytra.setIngredient('B', Material.LEATHER);
			elytra.setIngredient('C', Material.ENDER_PEARL);
			elytra.setIngredient('D', Material.DIAMOND);
			Bukkit.getServer().addRecipe(elytra);
		}
		{
			ShapelessRecipe packedIce = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.PACKED_ICE));
			packedIce.addIngredient(4, Material.ICE);
			Bukkit.getServer().addRecipe(packedIce);
		}
		{
			ShapedRecipe endPortal = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.ENDER_EYE, ChatColor.LIGHT_PURPLE + "End Portal"));
			endPortal.shape("BCB", "BDB", "AAA");
			endPortal.setIngredient('A', Material.OBSIDIAN);
			endPortal.setIngredient('B', Material.GLASS);
			endPortal.setIngredient('C', Material.ENDER_PEARL);
			endPortal.setIngredient('D', Material.NETHER_STAR);
			Bukkit.getServer().addRecipe(endPortal);
		}
		{
			ShapedRecipe goldenApple = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.GOLDEN_APPLE));
			goldenApple.shape("BBB", "BAB", "BBB");
			goldenApple.setIngredient('A', Material.APPLE);
			goldenApple.setIngredient('B', Material.GOLD_INGOT);
			Bukkit.getServer().addRecipe(goldenApple);
		}
		{
			ShapedRecipe notchApple = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.ENCHANTED_GOLDEN_APPLE));
			notchApple.shape("BBB", "BAB", "BBB");
			notchApple.setIngredient('A', Material.APPLE);
			notchApple.setIngredient('B', Material.GOLD_BLOCK);
			Bukkit.getServer().addRecipe(notchApple);
		}
		{
			ShapelessRecipe sulphur = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GUNPOWDER, 1));
			sulphur.addIngredient(1, Material.FLINT);
			Bukkit.getServer().addRecipe(sulphur);
		}
		{
			ShapelessRecipe explosiveSulphur0 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GUNPOWDER, 1, ChatColor.LIGHT_PURPLE + "Explosive Gunpowder"));
			explosiveSulphur0.addIngredient(4, Material.GUNPOWDER);
			Bukkit.getServer().addRecipe(explosiveSulphur0);
		}
		{
			ShapelessRecipe stone = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.STONE, 3));
			stone.addIngredient(3, Material.COBBLESTONE);
			stone.addIngredient(1, Material.COAL);
			Bukkit.getServer().addRecipe(stone);
		}
		{
			ShapelessRecipe vial0 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.POTION, 1, ChatColor.RESET + "Monster Vial", (short) 0, new String[]{ChatColor.RESET + "" + ChatColor.WHITE + "Capture any mob into a spawn egg using this vial."}));
			vial0.addIngredient(1, Material.GLASS_BOTTLE);
			Bukkit.getServer().addRecipe(vial0);
		}
		{
			ShapelessRecipe vial1 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GLASS_BOTTLE));
			vial1.addIngredient(1, Material.POTION);
			Bukkit.getServer().addRecipe(vial1);
		}
		{
			ShapedRecipe chainHelmet = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.CHAINMAIL_HELMET));
			chainHelmet.shape("BBB", "BAB", "AAA");
			chainHelmet.setIngredient('A', Material.AIR);
			chainHelmet.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainHelmet);
		}
		{
			ShapedRecipe chainChestplate = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.CHAINMAIL_CHESTPLATE));
			chainChestplate.shape("BAB", "BBB", "BBB");
			chainChestplate.setIngredient('A', Material.AIR);
			chainChestplate.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainChestplate);
		}
		{
			ShapedRecipe chainLeggings = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.CHAINMAIL_LEGGINGS));
			chainLeggings.shape("BBB", "BAB", "BAB");
			chainLeggings.setIngredient('A', Material.AIR);
			chainLeggings.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainLeggings);
		}
		{
			ShapedRecipe chainBoots = new ShapedRecipe(randomKey(), Link$.createMaterial(Material.CHAINMAIL_BOOTS));
			chainBoots.shape("BAB", "BAB");
			chainBoots.setIngredient('A', Material.AIR);
			chainBoots.setIngredient('B', Material.IRON_BARS);
			Bukkit.getServer().addRecipe(chainBoots);
		}
		{
			ShapelessRecipe stoneBrickToCobble = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.COBBLESTONE, 3));
			stoneBrickToCobble.addIngredient(1, Material.STONE_BRICKS);
			Bukkit.getServer().addRecipe(stoneBrickToCobble);
		}
		{
			ShapelessRecipe diamond0 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 2));
			diamond0.addIngredient(1, Material.DIAMOND_SWORD);
			Bukkit.getServer().addRecipe(diamond0);
		}
		{
			ShapelessRecipe diamond1 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 3));
			diamond1.addIngredient(1, Material.DIAMOND_PICKAXE);
			Bukkit.getServer().addRecipe(diamond1);
		}
		{
			ShapelessRecipe diamond2 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 3));
			diamond2.addIngredient(1, Material.DIAMOND_AXE);
			Bukkit.getServer().addRecipe(diamond2);
		}
		{
			ShapelessRecipe diamond3 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 1));
			diamond3.addIngredient(1, Material.DIAMOND_SHOVEL);
			Bukkit.getServer().addRecipe(diamond3);
		}
		{
			ShapelessRecipe diamond4 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 5));
			diamond4.addIngredient(1, Material.DIAMOND_HELMET);
			Bukkit.getServer().addRecipe(diamond4);
		}
		{
			ShapelessRecipe diamond5 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 8));
			diamond5.addIngredient(1, Material.DIAMOND_CHESTPLATE);
			Bukkit.getServer().addRecipe(diamond5);
		}
		{
			ShapelessRecipe diamond6 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 7));
			diamond6.addIngredient(1, Material.DIAMOND_LEGGINGS);
			Bukkit.getServer().addRecipe(diamond6);
		}
		{
			ShapelessRecipe diamond7 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 4));
			diamond7.addIngredient(1, Material.DIAMOND_BOOTS);
			Bukkit.getServer().addRecipe(diamond7);
		}
		{
			ShapelessRecipe iron0 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 2));
			iron0.addIngredient(1, Material.IRON_SWORD);
			Bukkit.getServer().addRecipe(iron0);
		}
		{
			ShapelessRecipe iron1 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 3));
			iron1.addIngredient(1, Material.IRON_PICKAXE);
			Bukkit.getServer().addRecipe(iron1);
		}
		{
			ShapelessRecipe iron2 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 3));
			iron2.addIngredient(1, Material.IRON_AXE);
			Bukkit.getServer().addRecipe(iron2);
		}
		{
			ShapelessRecipe iron3 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 1));
			iron3.addIngredient(1, Material.IRON_SHOVEL);
			Bukkit.getServer().addRecipe(iron3);
		}
		{
			ShapelessRecipe iron4 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 5));
			iron4.addIngredient(1, Material.IRON_HELMET);
			Bukkit.getServer().addRecipe(iron4);
		}
		{
			ShapelessRecipe iron5 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 8));
			iron5.addIngredient(1, Material.IRON_CHESTPLATE);
			Bukkit.getServer().addRecipe(iron5);
		}
		{
			ShapelessRecipe iron6 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 7));
			iron6.addIngredient(1, Material.IRON_LEGGINGS);
			Bukkit.getServer().addRecipe(iron6);
		}
		{
			ShapelessRecipe iron7 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 4));
			iron7.addIngredient(1, Material.IRON_BOOTS);
			Bukkit.getServer().addRecipe(iron7);
		}
		{
			ShapelessRecipe gold0 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 2));
			gold0.addIngredient(1, Material.GOLDEN_SWORD);
			Bukkit.getServer().addRecipe(gold0);
		}
		{
			ShapelessRecipe gold1 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 3));
			gold1.addIngredient(1, Material.GOLDEN_PICKAXE);
			Bukkit.getServer().addRecipe(gold1);
		}
		{
			ShapelessRecipe gold2 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 3));
			gold2.addIngredient(1, Material.GOLDEN_AXE);
			Bukkit.getServer().addRecipe(gold2);
		}
		{
			ShapelessRecipe gold3 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 1));
			gold3.addIngredient(1, Material.GOLDEN_SHOVEL);
			Bukkit.getServer().addRecipe(gold3);
		}
		{
			ShapelessRecipe gold4 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 5));
			gold4.addIngredient(1, Material.GOLDEN_HELMET);
			Bukkit.getServer().addRecipe(gold4);
		}
		{
			ShapelessRecipe gold5 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 8));
			gold5.addIngredient(1, Material.GOLDEN_CHESTPLATE);
			Bukkit.getServer().addRecipe(gold5);
		}
		{
			ShapelessRecipe gold6 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 7));
			gold6.addIngredient(1, Material.GOLDEN_LEGGINGS);
			Bukkit.getServer().addRecipe(gold6);
		}
		{
			ShapelessRecipe gold7 = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 4));
			gold7.addIngredient(1, Material.GOLDEN_BOOTS);
			Bukkit.getServer().addRecipe(gold7);
		}
		{
			ShapelessRecipe ironBarding = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.IRON_INGOT, 6));
			ironBarding.addIngredient(1, Material.IRON_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(ironBarding);
		}
		{
			ShapelessRecipe diamondBarding = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.DIAMOND, 6));
			diamondBarding.addIngredient(1, Material.DIAMOND_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(diamondBarding);
		}
		{
			ShapelessRecipe goldBarding = new ShapelessRecipe(randomKey(), Link$.createMaterial(Material.GOLD_INGOT, 6));
			goldBarding.addIngredient(1, Material.GOLDEN_HORSE_ARMOR);
			Bukkit.getServer().addRecipe(goldBarding);
		}
	}
}
