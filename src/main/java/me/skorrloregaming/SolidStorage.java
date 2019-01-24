/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SolidStorage {
	public static boolean clearPlayerSave(Player player, String data) {
		if (!SolidStorage.doesPlayerSaveExist(player, data)) {
			return false;
		}
		try {
			File file1 = new File("plugins/" + File.separator + "PlayerData" + File.separator + "Inventory", String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
			File file2 = new File("plugins/" + File.separator + "PlayerData" + File.separator + "Location", String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
			File file3 = new File("plugins/" + File.separator + "PlayerData" + File.separator + "Information", String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
			File file4 = new File("plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects", String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
			file1.delete();
			file2.delete();
			file3.delete();
			file4.delete();
			return true;
		} catch (Exception ex) {
			System.out.println("SolidStorage: Failed to dispose of saved data for player " + player.getName());
			return false;
		}
	}

	public static void savePersonalChest(Player player, String data, ItemStack[] contents) throws Exception {
		if (data.equals("hub") || data.equals("skyfight")) {
			return;
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Chest";
		String file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		File chestFile = new File(folder, file);
		chestFile.getParentFile().mkdirs();
		YamlConfiguration c = new YamlConfiguration();
		c.set("inventory.content",  contents);
		c.save(chestFile);
	}

	public static Inventory restorePersonalChest(Player player, String data, boolean personal) {
		if (data.equals("hub") || data.equals("skyfight")) {
			return Bukkit.createInventory((InventoryHolder) null, (int) 27);
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Chest";
		String file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		File chestFile = new File(folder, file);
		ItemStack[] content = null;
		if (chestFile.exists()) {
			YamlConfiguration c = YamlConfiguration.loadConfiguration((File) chestFile);
			content = (ItemStack[]) ((List) c.get("inventory.content")).toArray(new ItemStack[0]);
		}
		String name =  ChatColor.BOLD + "Temporary Inventory";
		if (personal) {
			name =  ChatColor.BOLD + "Personal Inventory";
		}
		Inventory inv = Bukkit.createInventory((InventoryHolder) null, (int) 45, (String) name);
		if (chestFile.exists()) {
			inv.setContents(content);
		}
		return inv;
	}

	public static void saveInventory(Player player, String data) throws Exception {
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		String file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		File invFile = new File(folder, file);
		YamlConfiguration c = new YamlConfiguration();
		c.set("inventory.content",  player.getInventory().getContents());
		c.save(invFile);
	}

	public static boolean restoreInventory(Player player, String data) {
		String file;
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		File invFile = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (invFile.exists()) {
			YamlConfiguration c = YamlConfiguration.loadConfiguration((File) invFile);
			ItemStack[] content = (ItemStack[]) ((List) c.get("inventory.content")).toArray(new ItemStack[0]);
			player.getInventory().setContents(content);
			player.updateInventory();
			return true;
		}
		System.out.println("SolidStorage: Failed to restore inventory to player " + player.getName());
		return false;
	}

	public static void saveLocation(Player player, String data) throws Exception {
		String file;
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		File locFile = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (locFile.exists()) {
			locFile.delete();
		}
		FileWriter writer = new FileWriter(locFile);
		BufferedWriter bwriter = new BufferedWriter(writer);
		String worldName = player.getLocation().getWorld().getName();
		double x = player.getLocation().getX();
		double y = player.getLocation().getY();
		double z = player.getLocation().getZ();
		float yaw = player.getLocation().getYaw();
		float pitch = player.getLocation().getPitch();
		bwriter.write(String.valueOf(worldName) + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch);
		bwriter.close();
	}

	public static boolean restoreLocation(Player player, String data) {
		String file;
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		File locFile = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (locFile.exists()) {
			FileReader reader = null;
			try {
				reader = new FileReader(locFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			char[] chars = new char[(int) locFile.length()];
			try {
				reader.read(chars);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String content = new String(chars);
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String[] args = content.split(";");
			World world = Bukkit.getServer().getWorld(args[0]);
			double x = Double.parseDouble(args[1]);
			double y = Double.parseDouble(args[2]);
			double z = Double.parseDouble(args[3]);
			float yaw = (float) Double.parseDouble(args[4]);
			float pitch = (float) Double.parseDouble(args[5]);
			player.teleport(new Location(world, x, y, z, yaw, pitch));
			return true;
		}
		System.out.println("SolidStorage: Failed to restore location to player " + player.getName());
		return false;
	}

	public static void saveInformation(Player player, String data) throws Exception {
		String file;
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		File infoFile = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (infoFile.exists()) {
			infoFile.delete();
		}
		FileWriter writer = new FileWriter(infoFile);
		BufferedWriter bwriter = new BufferedWriter(writer);
		bwriter.write(String.valueOf(player.getHealth()) + ";");
		bwriter.write(String.valueOf(player.getFoodLevel()) + ";");
		bwriter.write(String.valueOf(player.getFireTicks()) + ";");
		bwriter.write(String.valueOf(player.getLevel()) + ";");
		bwriter.write(String.valueOf(player.getSaturation()) + ";");
		bwriter.write(String.valueOf(player.getRemainingAir()) + ";");
		bwriter.write(String.valueOf(player.isFlying()));
		bwriter.close();
	}

	public static boolean restoreInformation(Player player, String data) {
		String file;
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		File infoFile = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (infoFile.exists()) {
			FileReader reader = null;
			try {
				reader = new FileReader(infoFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			char[] chars = new char[(int) infoFile.length()];
			try {
				reader.read(chars);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String content = new String(chars);
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String[] args = content.split(";");
			player.setHealth(Double.parseDouble(args[0]));
			player.setFoodLevel(Integer.parseInt(args[1]));
			player.setFireTicks(Integer.parseInt(args[2]));
			player.setLevel(Integer.parseInt(args[3]));
			player.setSaturation(Float.parseFloat(args[4]));
			player.setRemainingAir(Integer.parseInt(args[5]));
			player.setFlying(Boolean.parseBoolean(args[6]));
			return true;
		}
		System.out.println("SolidStorage: Failed to restore information to player " + player.getName());
		return false;
	}

	public static void savePotionEffects(Player player, String data) {
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		String file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		File potFile = new File(folder, file);
		Server.ramConfig.setup(potFile);
		Server.ramConfig.clearData();
		for (PotionEffect p : player.getActivePotionEffects()) {
			Server.ramConfig.getData().set("potion." + p.getType().getName() + ".type",  p.getType().getName());
			Server.ramConfig.getData().set("potion." + p.getType().getName() + ".level",  p.getAmplifier());
			Server.ramConfig.getData().set("potion." + p.getType().getName() + ".duration",  p.getDuration());
			Server.ramConfig.saveData();
		}
	}

	public static boolean restorePotionEffects(Player player, String data) {
		String file;
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		File potFile = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (potFile.exists()) {
			for (PotionEffect effect : player.getActivePotionEffects()) {
				player.removePotionEffect(effect.getType());
			}
			Server.ramConfig.setup(potFile);
			if (Server.ramConfig.getData().contains("potion")) {
				for (String l : Server.ramConfig.getData().getConfigurationSection("potion").getKeys(false)) {
					String s = Server.ramConfig.getData().getString("potion." + l + ".type");
					int n = Server.ramConfig.getData().getInt("potion." + l + ".level");
					int i = Server.ramConfig.getData().getInt("potion." + l + ".duration");
					player.addPotionEffect(new PotionEffect(PotionEffectType.getByName((String) s), i, n));
				}
			}
			return true;
		}
		System.out.println("SolidStorage: Failed to restore potion effects to player " + player.getName());
		return false;
	}

	public static boolean restorePlayerData(Player player, String data) {
		if (data.equals("hub") || data.equals("skyfight")) {
			return false;
		}
		boolean success = true;
		success = SolidStorage.restoreLocation(player, data);
		success = SolidStorage.restoreInventory(player, data);
		success = SolidStorage.restoreInformation(player, data);
		success = SolidStorage.restorePotionEffects(player, data);
		return success;
	}

	public static void savePlayerData(Player player, String data) throws Exception {
		if (data.equals("hub") || data.equals("skyfight")) {
			return;
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator;
		new File(String.valueOf(folder) + "Location", "tmp.o").getParentFile().mkdirs();
		new File(String.valueOf(folder) + "Inventory", "tmp.o").getParentFile().mkdirs();
		new File(String.valueOf(folder) + "PotionEffects", "tmp.o").getParentFile().mkdirs();
		new File(String.valueOf(folder) + "Information", "tmp.o").getParentFile().mkdirs();
		SolidStorage.saveLocation(player, data);
		SolidStorage.saveInventory(player, data);
		SolidStorage.saveInformation(player, data);
		SolidStorage.savePotionEffects(player, data);
	}

	public static boolean doesInventorySaveExist(Player player, String data) {
		String file;
		if (data.equals("hub") || data.equals("skyfight")) {
			return false;
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		File fileSave = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (fileSave.exists()) {
			return true;
		}
		return false;
	}

	public static boolean doesInformationSaveExist(Player player, String data) {
		String file;
		if (data.equals("hub") || data.equals("skyfight")) {
			return false;
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		File fileSave = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (fileSave.exists()) {
			return true;
		}
		return false;
	}

	public static boolean doesLocationSaveExist(Player player, String data) {
		String file;
		if (data.equals("hub") || data.equals("skyfight")) {
			return false;
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		File fileSave = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (fileSave.exists()) {
			return true;
		}
		return false;
	}

	public static boolean doesPotionEffectSaveExist(Player player, String data) {
		String file;
		if (data.equals("hub") || data.equals("skyfight")) {
			return false;
		}
		String folder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		File fileSave = new File(folder, file = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml");
		if (fileSave.exists()) {
			return true;
		}
		return false;
	}

	public static boolean doesPlayerSaveExist(Player player, String data) {
		boolean informationSave = SolidStorage.doesInformationSaveExist(player, data);
		boolean inventorySave = SolidStorage.doesInventorySaveExist(player, data);
		boolean locationSave = SolidStorage.doesLocationSaveExist(player, data);
		boolean potionSave = SolidStorage.doesPotionEffectSaveExist(player, data);
		if (informationSave && inventorySave && locationSave && potionSave) {
			return true;
		}
		return false;
	}

	public static boolean dataOfflineModeToOnlineMode(Player player, String data) {
		UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes());
		int moved = 0;
		String offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		String offlineFile = String.valueOf(offlineUUID.toString()) + "_" + data + ".yml";
		File offlineFileSave = new File(offlineFolder, offlineFile);
		String onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		String onlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		File onlineFileSave = new File(onlineFolder, onlineFile);
		if (offlineFileSave.exists()) {
			if (onlineFileSave.exists()) {
				onlineFileSave.delete();
			}
			offlineFileSave.renameTo(onlineFileSave);
			offlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		offlineFile = String.valueOf(offlineUUID.toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		onlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		onlineFileSave = new File(onlineFolder, onlineFile);
		if (offlineFileSave.exists()) {
			if (onlineFileSave.exists()) {
				onlineFileSave.delete();
			}
			offlineFileSave.renameTo(onlineFileSave);
			offlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		offlineFile = String.valueOf(offlineUUID.toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		onlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		onlineFileSave = new File(onlineFolder, onlineFile);
		if (offlineFileSave.exists()) {
			if (onlineFileSave.exists()) {
				onlineFileSave.delete();
			}
			offlineFileSave.renameTo(onlineFileSave);
			offlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		offlineFile = String.valueOf(offlineUUID.toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		onlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		onlineFileSave = new File(onlineFolder, onlineFile);
		if (offlineFileSave.exists()) {
			if (onlineFileSave.exists()) {
				onlineFileSave.delete();
			}
			offlineFileSave.renameTo(onlineFileSave);
			offlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Chest";
		offlineFile = String.valueOf(offlineUUID.toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Chest";
		onlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		onlineFileSave = new File(onlineFolder, onlineFile);
		if (offlineFileSave.exists()) {
			if (onlineFileSave.exists()) {
				onlineFileSave.delete();
			}
			offlineFileSave.renameTo(onlineFileSave);
			offlineFileSave.delete();
			++moved;
		}
		if (moved > 0) {
			return true;
		}
		return false;
	}

	public static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			int read;
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1) {
				buffer.append(chars, 0, read);
			}
			String string = buffer.toString();
			return string;
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}

	public static String getPremiumUUID(String playerName) {
		try {
			String response = SolidStorage.readUrl("https://use.gameapis.net/mc/player/uuid/" + playerName);
			response = response.substring(response.indexOf("\"uuid_formatted\": \"") + "\"uuid_formatted\": \"".length());
			response = response.substring(0, response.indexOf("\""));
			return response;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean dataOnlineModeToOfflineMode(Player player, String data, String onlineUUID) {
		String onlineFile;
		if (onlineUUID == null) {
			return false;
		}
		int moved = 0;
		String offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		String offlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		File offlineFileSave = new File(offlineFolder, offlineFile);
		String onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "PotionEffects";
		File onlineFileSave = new File(onlineFolder, onlineFile = String.valueOf(onlineUUID.toString()) + "_" + data + ".yml");
		if (onlineFileSave.exists()) {
			if (offlineFileSave.exists()) {
				offlineFileSave.delete();
			}
			onlineFileSave.renameTo(onlineFileSave);
			onlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		offlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Location";
		onlineFileSave = new File(onlineFolder, onlineFile = String.valueOf(onlineUUID.toString()) + "_" + data + ".yml");
		if (onlineFileSave.exists()) {
			if (offlineFileSave.exists()) {
				offlineFileSave.delete();
			}
			onlineFileSave.renameTo(onlineFileSave);
			onlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		offlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Information";
		onlineFileSave = new File(onlineFolder, onlineFile = String.valueOf(onlineUUID.toString()) + "_" + data + ".yml");
		if (onlineFileSave.exists()) {
			if (offlineFileSave.exists()) {
				offlineFileSave.delete();
			}
			onlineFileSave.renameTo(onlineFileSave);
			onlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		offlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Inventory";
		onlineFileSave = new File(onlineFolder, onlineFile = String.valueOf(onlineUUID.toString()) + "_" + data + ".yml");
		if (onlineFileSave.exists()) {
			if (offlineFileSave.exists()) {
				offlineFileSave.delete();
			}
			onlineFileSave.renameTo(onlineFileSave);
			onlineFileSave.delete();
			++moved;
		}
		offlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Chest";
		offlineFile = String.valueOf(player.getUniqueId().toString()) + "_" + data + ".yml";
		offlineFileSave = new File(offlineFolder, offlineFile);
		onlineFolder = "plugins/" + File.separator + "PlayerData" + File.separator + "Chest";
		onlineFileSave = new File(onlineFolder, onlineFile = String.valueOf(onlineUUID.toString()) + "_" + data + ".yml");
		if (onlineFileSave.exists()) {
			if (offlineFileSave.exists()) {
				offlineFileSave.delete();
			}
			onlineFileSave.renameTo(onlineFileSave);
			onlineFileSave.delete();
			++moved;
		}
		if (moved > 0) {
			return true;
		}
		return false;
	}
}

