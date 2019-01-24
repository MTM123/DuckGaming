/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.skorrloregaming.Go;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.DataWatcherSerializer;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutChat;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import protocolsupport.api.ProtocolSupportAPI;

public class CraftGo {

	public static class ItemStack
			extends CraftGo {
		public static org.bukkit.inventory.ItemStack removeAttributes(org.bukkit.inventory.ItemStack item) {
			NBTTagCompound tag;
			if (item == null || item.getType() == Material.BOOK_AND_QUILL) {
				return item;
			}
			org.bukkit.inventory.ItemStack itm = item.clone();
			net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) itm);
			if (!nmsStack.hasTag()) {
				tag = new NBTTagCompound();
				nmsStack.setTag(tag);
			} else {
				tag = nmsStack.getTag();
			}
			NBTTagList am = new NBTTagList();
			tag.set("AttributeModifiers", (NBTBase) am);
			nmsStack.setTag(tag);
			return CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) nmsStack);
		}

		public static NBTTagCompound getTag(org.bukkit.inventory.ItemStack item) {
			net.minecraft.server.v1_12_R1.ItemStack nmsIs = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) item);
			NBTTagCompound tag = nmsIs.getTag();
			if (tag == null) {
				tag = new NBTTagCompound();
			}
			return tag;
		}
	}

	public static class Minecraft
			extends CraftGo {
		public static String getPackageVersion() {
			return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		}
	}

	public static class MobSpawner
			extends CraftGo {
		public static short getSpawnerEntityID(Block block) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("getSpawnerEntityID called on non-CreatureSpawner block: " +  block);
				return 0;
			}
			CreatureSpawner spawner = (CreatureSpawner) blockState;
			if (spawner.getSpawnedType() != null) {
				return spawner.getSpawnedType().getTypeId();
			}
			return 0;
		}

		public static CreatureSpawner getSpawnerInfo(Block block) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("getSpawner called on non-CreatureSpawner block: " +  block);
				throw new NullPointerException();
			}
			return (CreatureSpawner) blockState;
		}

		public static void setSpawnerSpawnRate(Block block, int spawnRate) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("setSpawnerSpawnRate called on non-CreatureSpawner block: " +  block);
				throw new NullPointerException();
			}
			CreatureSpawner spawner = (CreatureSpawner) blockState;
			spawner.setDelay(spawnRate);
			spawner.update();
		}

		public static short getStoredSpawnerItemEntityID(org.bukkit.inventory.ItemStack item) {
			short metaEntityId;
			short durability = item.getDurability();
			if (durability != 0) {
				return durability;
			}
			if (item.hasItemMeta() && (metaEntityId = MobSpawner.searchItemMeta(item.getItemMeta())) != 0) {
				return metaEntityId;
			}
			return 0;
		}

		private static short searchItemMeta(ItemMeta meta) {
			short durability = 0;
			if (meta.hasLore() && !meta.getLore().isEmpty()) {
				for (String entityIDString : meta.getLore()) {
					String[] entityIDArray;
					if (!entityIDString.contains("Entity ID:") || (entityIDArray = entityIDString.split(": ")).length != 2)
						continue;
					try {
						durability = Short.valueOf(entityIDArray[1]);
						if (durability == 0) continue;
						return durability;
					} catch (NumberFormatException e) {
						return 0;
					}
				}
			}
			return durability;
		}

		public static void setSpawnerEntityID(Block block, short entityID) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("setSpawnerEntityID called on non-CreatureSpawner block: " +  block);
				return;
			}
			EntityType ct = EntityType.fromId((int) entityID);
			if (ct == null) {
				throw new IllegalArgumentException("Failed to find creature type for " + entityID);
			}
			((CreatureSpawner) blockState).setSpawnedType(ct);
			blockState.update(true);
		}

		public static org.bukkit.inventory.ItemStack newSpawnerItem(short entityID, int amount) {
			org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.MOB_SPAWNER, amount, entityID);
			String entityName = EntityType.fromId((int) entityID).getName().toLowerCase();
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add( ChatColor.RESET + "Entity ID: " + entityID);
			meta.setLore(lore);
			meta.setDisplayName( ChatColor.RESET + Go.capitalizeAll(new StringBuilder(String.valueOf(WordUtils.capitalize((String) entityName))).append(" Spawner").toString(), "_"));
			item.setDurability(entityID);
			item.setItemMeta(meta);
			return MobSpawner.setNBTEntityID(item, entityID, entityName);
		}

		private static org.bukkit.inventory.ItemStack setNBTEntityID(org.bukkit.inventory.ItemStack item, short entityID, String entity) {
			if (item == null || entityID == 0 || entity == null || entity.isEmpty()) {
				Bukkit.getLogger().warning("Skipping invalid spawner to set NBT data on.");
				return null;
			}
			net.minecraft.server.v1_12_R1.ItemStack itemStack = null;
			CraftItemStack craftStack = CraftItemStack.asCraftCopy((org.bukkit.inventory.ItemStack) item);
			itemStack = CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) craftStack);
			NBTTagCompound tag = itemStack.getTag();
			if (tag == null) {
				tag = new NBTTagCompound();
				itemStack.setTag(tag);
			}
			if (!tag.hasKey("BlockEntityTag")) {
				tag.set("BlockEntityTag", (NBTBase) new NBTTagCompound());
			}
			tag.getCompound("BlockEntityTag").setString("EntityId", entity);
			if (!tag.hasKey("SpawnData")) {
				tag.set("SpawnData", (NBTBase) new NBTTagCompound());
			}
			tag.getCompound("SpawnData").setString("id", entity);
			if (!tag.getCompound("BlockEntityTag").hasKey("SpawnPotentials")) {
				tag.getCompound("BlockEntityTag").set("SpawnPotentials", (NBTBase) new NBTTagCompound());
			}
			if (!tag.hasKey("EntityTag")) {
				tag.set("EntityTag", (NBTBase) new NBTTagCompound());
			}
			if (!entity.startsWith("minecraft:")) {
				entity = "minecraft:" + entity;
			}
			tag.getCompound("EntityTag").setString("id", entity);
			return CraftItemStack.asCraftMirror((net.minecraft.server.v1_12_R1.ItemStack) itemStack);
		}
	}

	public static class Player
			extends CraftGo {
		public static void clearArrowsInBody(org.bukkit.entity.Player player) {
			CraftPlayer cp = (CraftPlayer) player;
			cp.getHandle().getDataWatcher().set(new DataWatcherObject(10, DataWatcherRegistry.b),  0);
		}

		public static int getConnectionLatency(org.bukkit.entity.Player player) {
			CraftPlayer cp = (CraftPlayer) player;
			return cp.getHandle().ping;
		}

		public static int getApproximatePlaytime(org.bukkit.entity.Player player) {
			return player.getStatistic(Statistic.PLAY_ONE_TICK) / 20;
		}

		public static String getProtocolVersionName(org.bukkit.entity.Player player) {
			if (Bukkit.getPluginManager().getPlugin("ProtocolSupport") == null) {
				return Go.formatPackageVersion(Minecraft.getPackageVersion());
			}
			String ver = ProtocolSupportAPI.getProtocolVersion((org.bukkit.entity.Player) player).getName();
			if (ver == null || ver.equals("null")) {
				ver = "1.4.7";
			}
			return ver.toUpperCase();
		}

		public static int getProtocolVersion(org.bukkit.entity.Player player) {
			if (Bukkit.getPluginManager().getPlugin("ProtocolSupport") == null) {
				return 0;
			}
			return ProtocolSupportAPI.getProtocolVersion((org.bukkit.entity.Player) player).getId();
		}

		public static void sendJson(org.bukkit.entity.Player player, String json) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a((String) json)));
		}

		public static String getCountry(org.bukkit.entity.Player player) {
			StringBuilder entirePage;
			block4:
			{
				try {
					String inputLine;
					URL url = new URL("http://ip-api.com/json/" + player.getAddress().getHostName());
					BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
					entirePage = new StringBuilder();
					while ((inputLine = stream.readLine()) != null) {
						entirePage.append(inputLine);
					}
					stream.close();
					if (entirePage.toString().contains("\"country\":\"")) break block4;
					return null;
				} catch (Exception ex) {
					return null;
				}
			}
			return entirePage.toString().split("\"country\":\"")[1].split("\",")[0];
		}

		public static String getState(org.bukkit.entity.Player player) {
			StringBuilder entirePage;
			block4:
			{
				try {
					String inputLine;
					URL url = new URL("http://ip-api.com/json/" + player.getAddress().getHostName());
					BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
					entirePage = new StringBuilder();
					while ((inputLine = stream.readLine()) != null) {
						entirePage.append(inputLine);
					}
					stream.close();
					if (entirePage.toString().contains("\"regionName\":\"")) break block4;
					return null;
				} catch (Exception ex) {
					return null;
				}
			}
			return entirePage.toString().split("\"regionName\":\"")[1].split("\",")[0];
		}

		public static String getCity(org.bukkit.entity.Player player) {
			StringBuilder entirePage;
			block4:
			{
				try {
					String inputLine;
					URL url = new URL("http://ip-api.com/json/" + player.getAddress().getHostName());
					BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
					entirePage = new StringBuilder();
					while ((inputLine = stream.readLine()) != null) {
						entirePage.append(inputLine);
					}
					stream.close();
					if (entirePage.toString().contains("\"city\":\"")) break block4;
					return null;
				} catch (Exception ex) {
					return null;
				}
			}
			return entirePage.toString().split("\"city\":\"")[1].split("\",")[0];
		}

		public static String getIsp(org.bukkit.entity.Player player) {
			StringBuilder entirePage;
			block4:
			{
				try {
					String inputLine;
					URL url = new URL("http://ip-api.com/json/" + player.getAddress().getHostName());
					BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
					entirePage = new StringBuilder();
					while ((inputLine = stream.readLine()) != null) {
						entirePage.append(inputLine);
					}
					stream.close();
					if (entirePage.toString().contains("\"as\":\"")) break block4;
					return null;
				} catch (Exception ex) {
					return null;
				}
			}
			return entirePage.toString().split("\"as\":\"")[1].split("\",")[0];
		}

		public static void setPlayerListHeaderFooter(org.bukkit.entity.Player player, String headerString, String footerString) {
			if (Player.getProtocolVersion(player) < 47) {
				return;
			}
			PacketPlayOutPlayerListHeaderFooter headerfooter = new PacketPlayOutPlayerListHeaderFooter();
			try {
				Field header = headerfooter.getClass().getDeclaredField("a");
				Field footer = headerfooter.getClass().getDeclaredField("b");
				header.setAccessible(true);
				footer.setAccessible(true);
				header.set( headerfooter,  IChatBaseComponent.ChatSerializer.a((String) ("\"" + headerString + "\"")));
				footer.set( headerfooter,  IChatBaseComponent.ChatSerializer.a((String) ("\"" + footerString + "\"")));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) headerfooter);
		}
	}

	public static class World
			extends CraftGo {
		public static Entity[] getNearbyEntities(Location location, int range) {
			ArrayList<Entity> found = new ArrayList<Entity>();
			for (Entity entity : location.getWorld().getEntities()) {
				if (!World.isNearby(location, entity.getLocation(), range)) continue;
				found.add(entity);
			}
			return found.toArray(new Entity[0]);
		}

		private static boolean isNearby(Location center, Location notCenter, int range) {
			int x = center.getBlockX();
			int z = center.getBlockZ();
			int x1 = notCenter.getBlockX();
			int z1 = notCenter.getBlockZ();
			if (x1 >= x + range || z1 >= z + range || x1 <= x - range || z1 <= z - range) {
				return false;
			}
			return true;
		}
	}

}

