package me.skorrloregaming;

import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

public class SolidStorage {
	private static String getDataFolder() {
		return "plugins" + File.separator + Server.getPlugin().getDescription().getName() + File.separator + "Players";
	}

	public static boolean clearPlayerSave(OfflinePlayer player, String data) {
		if (!doesPlayerSaveExist(player, data))
			return false;
		try {
			File file1 = new File(getDataFolder() + File.separator + "Inventory", player.getUniqueId().toString() + "_" + data + ".yml");
			File file2 = new File(getDataFolder() + File.separator + "Location", player.getUniqueId().toString() + "_" + data + ".yml");
			File file3 = new File(getDataFolder() + File.separator + "Information", player.getUniqueId().toString() + "_" + data + ".yml");
			File file4 = new File(getDataFolder() + File.separator + "PotionEffects", player.getUniqueId().toString() + "_" + data + ".yml");
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

	public static void savePersonalChest(Player player, String data, ItemStack[] contents, int chestNumber) throws Exception {
		if (!$.validStorageMinigames.contains(data))
			return;
		String folder = getDataFolder() + File.separator + "Chest";
		String file = player.getUniqueId().toString() + "_" + data + "_" + chestNumber + ".yml";
		File chestFile = new File(folder, file);
		chestFile.getParentFile().mkdirs();
		YamlConfiguration c = new YamlConfiguration();
		c.set("inventory.content", contents);
		c.save(chestFile);
	}

	@SuppressWarnings("unchecked")
	public static Inventory restorePersonalChest(Player player, String data, boolean personal, int chestNumber) {
		int slotCount = 9 * 6;
		if ($.getCurrentMinigame(player) == ServerMinigame.SKYBLOCK)
			slotCount = 9 * 3;
		if (!$.validStorageMinigames.contains(data))
			return Bukkit.createInventory(null, slotCount);
		String folder = getDataFolder() + File.separator + "Chest";
		String oldFile = player.getUniqueId().toString() + "_" + data + ".yml";
		String file = player.getUniqueId().toString() + "_" + data + "_" + chestNumber + ".yml";
		File oldChestFile = new File(folder, oldFile);
		File chestFile = new File(folder, file);
		if (!chestFile.exists() && oldChestFile.exists()) {
			oldChestFile.renameTo(chestFile);
			chestFile = new File(folder, file);
		}
		ItemStack[] content = null;
		if (chestFile.exists()) {
			YamlConfiguration c = YamlConfiguration.loadConfiguration(chestFile);
			content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
		}
		String name = ChatColor.BOLD + "Temporary Inventory [" + chestNumber + "]";
		if (personal)
			name = ChatColor.BOLD + "Personal Inventory [" + chestNumber + "]";
		Inventory inv = Bukkit.createInventory(null, slotCount, name);
		if (!(content == null))
			inv.setContents(content);
		return inv;
	}

	public static void saveInventory(Player player, String data) throws Exception {
		String folder = getDataFolder() + File.separator + "Inventory";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File invFile = new File(folder, file);
		YamlConfiguration c = new YamlConfiguration();
		c.set("inventory.content", player.getInventory().getContents());
		c.save(invFile);
	}

	public static boolean restoreInventory(Player player, String data) {
		String folder = getDataFolder() + File.separator + "Inventory";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File invFile = new File(folder, file);
		if (invFile.exists()) {
			YamlConfiguration c = YamlConfiguration.loadConfiguration(invFile);
			@SuppressWarnings("unchecked")
			ItemStack[] content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
			player.getInventory().setContents(content);
			player.updateInventory();
			return true;
		} else {
			System.out.println("SolidStorage: Failed to restore inventory to player " + player.getName());
			return false;
		}
	}

	public static void saveLocation(Player player, String data) throws Exception {
		String folder = getDataFolder() + File.separator + "Location";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File locFile = new File(folder, file);
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
		bwriter.write(worldName + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch);
		bwriter.close();
	}

	public static boolean restoreLocation(Player player, String data) {
		String folder = getDataFolder() + File.separator + "Location";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File locFile = new File(folder, file);
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
			if (isForbiddenWorld(world, ServerMinigame.valueOf(data.toUpperCase()))) {
				$.playForbiddenTeleportMessage(player, ServerMinigame.valueOf(data.toUpperCase()));
				return false;
			}
			double x = Double.parseDouble(args[1]);
			double y = Double.parseDouble(args[2]);
			double z = Double.parseDouble(args[3]);
			float yaw = (float) Double.parseDouble(args[4]);
			float pitch = (float) Double.parseDouble(args[5]);
			Location teleportLocation = new Location(world, x, y, z, yaw, pitch);
			$.teleport(player, teleportLocation);
			return true;
		} else {
			System.out.println("SolidStorage: Failed to restore location to player " + player.getName());
			return false;
		}
	}

	public static void saveInformation(Player player, String data) throws Exception {
		String folder = getDataFolder() + File.separator + "Information";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File infoFile = new File(folder, file);
		if (infoFile.exists()) {
			infoFile.delete();
		}
		FileWriter writer = new FileWriter(infoFile);
		BufferedWriter bwriter = new BufferedWriter(writer);
		bwriter.write(player.getHealth() + ";");
		bwriter.write(player.getFoodLevel() + ";");
		bwriter.write(player.getFireTicks() + ";");
		bwriter.write(player.getLevel() + ";");
		bwriter.write(player.getSaturation() + ";");
		bwriter.write(player.getRemainingAir() + ";");
		bwriter.write(player.getAllowFlight() + ";");
		bwriter.write(player.isFlying() + "");
		bwriter.close();
	}

	public static boolean restoreInformation(Player player, String data) {
		String folder = getDataFolder() + File.separator + "Information";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File infoFile = new File(folder, file);
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
			player.setAllowFlight(Boolean.parseBoolean(args[6]));
			player.setFlying(Boolean.parseBoolean(args[7]));
			return true;
		} else {
			System.out.println("SolidStorage: Failed to restore information to player " + player.getName());
			return false;
		}
	}

	public static void savePotionEffects(Player player, String data) {
		String folder = getDataFolder() + File.separator + "PotionEffects";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File potFile = new File(folder, file);
		Server.getRamConfig().setup(potFile);
		Server.getRamConfig().clearData();
		for (PotionEffect p : player.getActivePotionEffects()) {
			Server.getRamConfig().getData().set("potion." + p.getType().getName() + ".type", p.getType().getName());
			Server.getRamConfig().getData().set("potion." + p.getType().getName() + ".level", p.getAmplifier());
			Server.getRamConfig().getData().set("potion." + p.getType().getName() + ".duration", p.getDuration());
			Server.getRamConfig().saveData();
		}
	}

	public static boolean restorePotionEffects(Player player, String data) {
		String folder = getDataFolder() + File.separator + "PotionEffects";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File potFile = new File(folder, file);
		if (potFile.exists()) {
			for (PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			Server.getRamConfig().setup(potFile);
			if (Server.getRamConfig().getData().contains("potion")) {
				for (String l : Server.getRamConfig().getData().getConfigurationSection("potion").getKeys(false)) {
					String s = Server.getRamConfig().getData().getString("potion." + l + ".type");
					int n = Server.getRamConfig().getData().getInt("potion." + l + ".level");
					int i = Server.getRamConfig().getData().getInt("potion." + l + ".duration");
					player.addPotionEffect(new PotionEffect(PotionEffectType.getByName(s), i, n));
				}
			}
			return true;
		} else {
			System.out.println("SolidStorage: Failed to restore potion effects to player " + player.getName());
			return false;
		}
	}

	public static boolean restorePlayerData(Player player, String data) {
		if (!$.validStorageMinigames.contains(data))
			return false;
		int success = 0;
		if (SolidStorage.restoreLocation(player, data))
			success++;
		if (SolidStorage.restoreInventory(player, data))
			success++;
		if (SolidStorage.restoreInformation(player, data))
			success++;
		if (SolidStorage.restorePotionEffects(player, data))
			success++;
		return success == 4;
	}

	public static void savePlayerData(Player player, String data) throws Exception {
		if (!$.validStorageMinigames.contains(data))
			return;
		new File(getDataFolder() + File.separator + "Location", "tmp.o").getParentFile().mkdirs();
		new File(getDataFolder() + File.separator + "Inventory", "tmp.o").getParentFile().mkdirs();
		new File(getDataFolder() + File.separator + "PotionEffects", "tmp.o").getParentFile().mkdirs();
		new File(getDataFolder() + File.separator + "Information", "tmp.o").getParentFile().mkdirs();
		SolidStorage.saveLocation(player, data);
		SolidStorage.saveInventory(player, data);
		SolidStorage.saveInformation(player, data);
		SolidStorage.savePotionEffects(player, data);
	}

	public static boolean doesInventorySaveExist(OfflinePlayer player, String data) {
		if (!$.validStorageMinigames.contains(data))
			return false;
		String folder = getDataFolder() + File.separator + "Inventory";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File fileSave = new File(folder, file);
		if (fileSave.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean doesInformationSaveExist(OfflinePlayer player, String data) {
		if (!$.validStorageMinigames.contains(data))
			return false;
		String folder = getDataFolder() + File.separator + "Information";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File fileSave = new File(folder, file);
		if (fileSave.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean doesLocationSaveExist(OfflinePlayer player, String data) {
		if (!$.validStorageMinigames.contains(data))
			return false;
		String folder = getDataFolder() + File.separator + "Location";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File fileSave = new File(folder, file);
		if (fileSave.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean doesPotionEffectSaveExist(OfflinePlayer player, String data) {
		if (!$.validStorageMinigames.contains(data))
			return false;
		String folder = getDataFolder() + File.separator + "PotionEffects";
		String file = player.getUniqueId().toString() + "_" + data + ".yml";
		File fileSave = new File(folder, file);
		if (fileSave.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean doesPlayerSaveExist(OfflinePlayer player, String data) {
		boolean informationSave = doesInformationSaveExist(player, data);
		boolean inventorySave = doesInventorySaveExist(player, data);
		boolean locationSave = doesLocationSaveExist(player, data);
		boolean potionSave = doesPotionEffectSaveExist(player, data);
		if (informationSave && inventorySave && locationSave && potionSave)
			return true;
		return false;
	}

	public static boolean dataUUIDtoUUID(UUID uuidFrom, UUID uuidTo, String data) {
		int moved = 0;
		{
			String offlineFolder = getDataFolder() + File.separator + "PotionEffects";
			String offlineFile = uuidFrom.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "PotionEffects";
			String onlineFile = uuidTo.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Location";
			String offlineFile = uuidFrom.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Location";
			String onlineFile = uuidTo.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Information";
			String offlineFile = uuidFrom.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Information";
			String onlineFile = uuidTo.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Inventory";
			String offlineFile = uuidFrom.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Inventory";
			String onlineFile = uuidTo.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Chest";
			String offlineFile = uuidFrom.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Chest";
			String onlineFile = uuidTo.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		if (moved > 0) {
			return true;
		}
		return false;
	}

	public static boolean dataOfflineModeToOnlineMode(Player player, String data) {
		UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes());
		int moved = 0;
		{
			String offlineFolder = getDataFolder() + File.separator + "PotionEffects";
			String offlineFile = offlineUUID.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "PotionEffects";
			String onlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Location";
			String offlineFile = offlineUUID.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Location";
			String onlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Information";
			String offlineFile = offlineUUID.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Information";
			String onlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Inventory";
			String offlineFile = offlineUUID.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Inventory";
			String onlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Chest";
			String offlineFile = offlineUUID.toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Chest";
			String onlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (offlineFileSave.exists()) {
				if (onlineFileSave.exists())
					onlineFileSave.delete();
				offlineFileSave.renameTo(onlineFileSave);
				offlineFileSave.delete();
				moved++;
			}
		}
		if (moved > 0) {
			return true;
		}
		return false;
	}

	public static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(2000);
			con.setReadTimeout(2000);
			InputStream in = con.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);
			return buffer.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
			if (reader != null)
				reader.close();
			return null;
		} finally {
			if (reader != null)
				reader.close();
		}
	}

	public static boolean dataOnlineModeToOfflineMode(Player player, String data, String onlineUUID) {
		if (onlineUUID == null)
			return false;
		int moved = 0;
		{
			String offlineFolder = getDataFolder() + File.separator + "PotionEffects";
			String offlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "PotionEffects";
			String onlineFile = onlineUUID.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (onlineFileSave.exists()) {
				if (offlineFileSave.exists())
					offlineFileSave.delete();
				onlineFileSave.renameTo(offlineFileSave);
				onlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Location";
			String offlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Location";
			String onlineFile = onlineUUID.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (onlineFileSave.exists()) {
				if (offlineFileSave.exists())
					offlineFileSave.delete();
				onlineFileSave.renameTo(offlineFileSave);
				onlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Information";
			String offlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Information";
			String onlineFile = onlineUUID.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (onlineFileSave.exists()) {
				if (offlineFileSave.exists())
					offlineFileSave.delete();
				onlineFileSave.renameTo(offlineFileSave);
				onlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Inventory";
			String offlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Inventory";
			String onlineFile = onlineUUID.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (onlineFileSave.exists()) {
				if (offlineFileSave.exists())
					offlineFileSave.delete();
				onlineFileSave.renameTo(offlineFileSave);
				onlineFileSave.delete();
				moved++;
			}
		}
		{
			String offlineFolder = getDataFolder() + File.separator + "Chest";
			String offlineFile = player.getUniqueId().toString() + "_" + data + ".yml";
			File offlineFileSave = new File(offlineFolder, offlineFile);
			String onlineFolder = getDataFolder() + File.separator + "Chest";
			String onlineFile = onlineUUID.toString() + "_" + data + ".yml";
			File onlineFileSave = new File(onlineFolder, onlineFile);
			if (onlineFileSave.exists()) {
				if (offlineFileSave.exists())
					offlineFileSave.delete();
				onlineFileSave.renameTo(offlineFileSave);
				onlineFileSave.delete();
				moved++;
			}
		}
		if (moved > 0) {
			return true;
		}
		return false;
	}

	public static boolean isForbiddenWorld(World world, ServerMinigame minigame) {
		if ($.getMinigameFromWorld(world) == minigame)
			return false;
		return true;
	}
}
