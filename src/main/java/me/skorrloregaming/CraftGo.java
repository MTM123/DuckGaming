package me.skorrloregaming;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.netty.buffer.ByteBuf;
import me.skorrloregaming.skins.model.SkinModel;
import me.skorrloregaming.skins.model.SkinProperty;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.ItemMeta;

import me.skorrloregaming.impl.IpLocationQuery;
import me.skorrloregaming.impl.TitleSubtitle;

public class CraftGo {

	public static class Reflection {

		public static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<Class<?>, Class<?>>();

		public static Class<?> getPrimitiveType(Class<?> clazz) {
			return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES
					.get(clazz) : clazz;
		}

		public static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
			int a = classes != null ? classes.length : 0;
			Class<?>[] types = new Class<?>[a];
			for (int i = 0; i < a; i++)
				types[i] = getPrimitiveType(classes[i]);
			return types;
		}

		public static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
			if (a.length != o.length)
				return false;
			for (int i = 0; i < a.length; i++)
				if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
					return false;
			return true;
		}

		public static Object getHandle(Object obj) {
			try {
				return getMethod("getHandle", obj.getClass()).invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public static Object invokeMethod(String method, Object obj) {
			try {
				return getMethod(method, obj.getClass()).invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public static Object invokeMethodWithArgs(String method, Object obj, Object... args) {
			try {
				return getMethod(method, obj.getClass()).invoke(obj, args);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public static Method getMethod(String name, Class<?> clazz,
									   Class<?>... paramTypes) {
			Class<?>[] t = toPrimitiveTypeArray(paramTypes);
			for (Method m : clazz.getMethods()) {
				Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
				if (m.getName().equals(name) && equalsTypeArray(types, t))
					return m;
			}
			return null;
		}

		public static Class<?> getNMSClass(String className) {
			String fullName = "net.minecraft.server." + getBukkitVersion() + "." + className;
			Class<?> clazz = null;
			try {
				clazz = Class.forName(fullName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return clazz;
		}

		public static Field getField(Class<?> clazz, String name) {
			try {
				Field field = clazz.getDeclaredField(name);
				field.setAccessible(true);
				return field;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		public static boolean set(Object object, String fieldName, Object fieldValue) {
			Class<?> clazz = object.getClass();
			while (clazz != null) {
				try {
					Field field = clazz.getDeclaredField(fieldName);
					field.setAccessible(true);
					field.set(object, fieldValue);
					return true;
				} catch (NoSuchFieldException e) {
					clazz = clazz.getSuperclass();
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
			}
			return false;
		}

		public static Object getPlayerField(org.bukkit.entity.Player player, String name) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			Method getHandle = player.getClass().getMethod("getHandle");
			Object nmsPlayer = getHandle.invoke(player);
			Field field = nmsPlayer.getClass().getField(name);
			return field.get(nmsPlayer);
		}

		public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
			for (Method m : clazz.getMethods())
				if (m.getName().equals(name)
						&& (args.length == 0 || ClassListEqual(args,
						m.getParameterTypes()))) {
					m.setAccessible(true);
					return m;
				}
			return null;
		}

		public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
			boolean equal = true;
			if (l1.length != l2.length)
				return false;
			for (int i = 0; i < l1.length; i++)
				if (l1[i] != l2[i]) {
					equal = false;
					break;
				}
			return equal;
		}

	}

	public static String getBukkitVersion() {
		return "v1_13_R2";
	}

	public static class MinecraftKey {
		public static Class<?> get() {
			try {
				return Class.forName("net.minecraft.server." + getBukkitVersion() + ".MinecraftKey");
			} catch (Exception ex) {
				ex.printStackTrace();
				return CraftEntity.class;
			}
		}
	}

	public static class CraftServer {
		private static Class<?> craftServer;
		private static Class<?> minecraftServer;
		private static Class<?> serverConnection;
		private static Class<?> networkManager;
		private static Method getServerConnection;

		static {
			try {
				craftServer = Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + ".CraftServer");
				minecraftServer = Class.forName("net.minecraft.server." + getBukkitVersion() + ".MinecraftServer");
				serverConnection = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ServerConnection");
				networkManager = Class.forName("net.minecraft.server." + getBukkitVersion() + ".NetworkManager");
				getServerConnection = minecraftServer.getDeclaredMethod("getServerConnection");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static Class<?> getCraftServer() {
			return craftServer;
		}

		public static Class<?> getMinecraftServer() {
			return minecraftServer;
		}

		public static Class<?> getNetworkManager() {
			return networkManager;
		}

		public static Object getServerConnection() {
			try {
				Object craftServerObject = craftServer.cast(Bukkit.getServer());
				Field console = getCraftServer().getDeclaredField("console");
				console.setAccessible(true);
				Object minecraftServerObject = minecraftServer.cast(console.get(Bukkit.getServer()));
				return getServerConnection.invoke(minecraftServerObject);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	public static class CraftEntity {
		public static Class<?> get() {
			try {
				return Class.forName("net.minecraft.server." + getBukkitVersion() + ".Entity");
			} catch (Exception ex) {
				ex.printStackTrace();
				return CraftEntity.class;
			}
		}
	}

	public static class ChatSerializer {
		private static Class<?> chatBaseComponent;
		private static Class<?> chatSerializer;
		private static Method serializeJson;
		private static Method deserializeChatComponent;

		static {
			try {
				chatBaseComponent = Class.forName("net.minecraft.server." + getBukkitVersion() + ".IChatBaseComponent");
				chatSerializer = Class.forName("net.minecraft.server." + getBukkitVersion() + ".IChatBaseComponent$ChatSerializer");
				serializeJson = chatSerializer.getDeclaredMethod("a", String.class);
				deserializeChatComponent = chatSerializer.getDeclaredMethod("a", chatBaseComponent);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static Class<?> getChatBaseComponent() {
			return chatBaseComponent;
		}

		public static Object serializeJson(String json) {
			try {
				return serializeJson.invoke(chatSerializer, json);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static String exportToString(Object chatComponent) {
			try {
				return (String) deserializeChatComponent.invoke(chatSerializer, chatComponent);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	public static class IDataWatcher {
		private Object entityDataWatcher;

		public IDataWatcher(Object dataWatcher) {
			entityDataWatcher = dataWatcher;
		}

		public Object get() {
			return entityDataWatcher;
		}

		public void set(Object dataWatcherObject, Object param2) {
			DataWatcher.set(entityDataWatcher, dataWatcherObject, param2);
		}
	}

	public static class DataWatcher {
		private static Class<?> dataWatcher;
		private static Class<?> dataWatcherObject;
		private static Class<?> dataWatcherRegistry;
		private static Class<?> dataWatcherSerializer;
		private static Method getDataWatcher;
		private static Method dataWatcherSet;
		private static Constructor<?> dataWatcherObjectConstructor;

		static {
			try {
				dataWatcher = Class.forName("net.minecraft.server." + getBukkitVersion() + ".DataWatcher");
				dataWatcherObject = Class.forName("net.minecraft.server." + getBukkitVersion() + ".DataWatcherObject");
				dataWatcherRegistry = Class.forName("net.minecraft.server." + getBukkitVersion() + ".DataWatcherRegistry");
				dataWatcherSerializer = Class.forName("net.minecraft.server." + getBukkitVersion() + ".DataWatcherSerializer");
				getDataWatcher = CraftEntity.get().getDeclaredMethod("getDataWatcher");
				dataWatcherSet = dataWatcher.getDeclaredMethod("set", dataWatcherObject, Object.class);
				dataWatcherObjectConstructor = dataWatcherObject.getDeclaredConstructor(int.class, dataWatcherSerializer);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static IDataWatcher getDataWatcher(org.bukkit.entity.Player player) {
			try {
				return IDataWatcher.class.getDeclaredConstructor(Object.class).newInstance(getDataWatcher.invoke(Player.getHandle(player)));
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static Object getDataWatcherSerializer() {
			try {
				return dataWatcherRegistry.getField("b").get(null);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static void set(Object dataWatcher, Object dataWatcherObject, Object param2) {
			try {
				dataWatcherSet.invoke(dataWatcher, dataWatcherObject, param2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static class DataWatcherObject {
			public static Class<?> get() {
				try {
					return dataWatcherObject;
				} catch (Exception ex) {
					ex.printStackTrace();
					return DataWatcherObject.class;
				}
			}

			public static Object newInstance(int param1, Object dataWatcherSerializer) {
				try {
					return dataWatcherObjectConstructor.newInstance(param1, dataWatcherSerializer);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}
	}

	public static class NBTTagCompound {
		private Object nbtTagCompound;

		public NBTTagCompound() {
			try {
				this.nbtTagCompound = Class.forName("net.minecraft.server." + getBukkitVersion() + ".NBTTagCompound").newInstance();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public NBTTagCompound(Object nbtTagCompound) {
			this.nbtTagCompound = nbtTagCompound;
		}

		public boolean hasKey(String key) {
			try {
				return NBT.hasKey(nbtTagCompound, key);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		public void set(String key, Object nbtBase) {
			try {
				NBT.set(nbtTagCompound, key, nbtBase);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public NBTTagCompound getCompound(String key) {
			try {
				return NBT.getCompound(nbtTagCompound, key);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public void setString(String key, String value) {
			try {
				NBT.setString(nbtTagCompound, key, value);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public Object get() {
			return nbtTagCompound;
		}
	}

	public static class NMSItemStack {
		private Object nmsItemStack;

		public NMSItemStack(Object nmsItemStack) {
			this.nmsItemStack = nmsItemStack;
		}

		public boolean hasTag() {
			try {
				return NBT.hasTag(nmsItemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		public NBTTagCompound getTag() {
			try {
				return NBT.getTag(nmsItemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public void setTag(Object tag) {
			try {
				NBT.setTag(nmsItemStack, tag);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public Object get() {
			return nmsItemStack;
		}
	}

	public static class CraftItemStack {
		private static Method asBukkitCopy;
		private static Method asCraftCopy;
		private static Method asNMSCopy;
		private static Method asCraftMirror;

		static {
			try {
				Class<?> nmsItemStack = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ItemStack");
				Class<?> craftItemStack = Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + ".inventory.CraftItemStack");
				asBukkitCopy = craftItemStack.getDeclaredMethod("asBukkitCopy", nmsItemStack);
				asCraftCopy = craftItemStack.getDeclaredMethod("asCraftCopy", org.bukkit.inventory.ItemStack.class);
				asNMSCopy = craftItemStack.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
				asCraftMirror = craftItemStack.getDeclaredMethod("asCraftMirror", nmsItemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static org.bukkit.inventory.ItemStack asBukkitCopy(Object nmsItemStack) {
			try {
				Object itemStack = nmsItemStack;
				if (itemStack instanceof NMSItemStack)
					itemStack = ((NMSItemStack) itemStack).get();
				return (org.bukkit.inventory.ItemStack) asBukkitCopy.invoke(null, itemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static Object asCraftCopy(Object bukkitItemStack) {
			try {
				return asCraftCopy.invoke(null, bukkitItemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static NMSItemStack asNMSCopy(Object bukkitItemStack) {
			try {
				return NMSItemStack.class.getDeclaredConstructor(Object.class).newInstance(asNMSCopy.invoke(null, bukkitItemStack));
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static Object asCraftMirror(Object nmsItemStack) {
			try {
				Object itemStack = nmsItemStack;
				if (itemStack instanceof NMSItemStack)
					itemStack = ((NMSItemStack) itemStack).get();
				return asCraftMirror.invoke(null, itemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	public static class NBT {
		private static Class<?> nmsItemStack;
		private static Class<?> nbtBase;
		private static Class<?> nbtTagCompound;
		private static Class<?> nbtTagList;
		private static Method hasKey;
		private static Method set;
		private static Method getCompound;
		private static Method setString;
		private static Method hasTag;
		private static Method getTag;
		private static Method setTag;
		private static Class<?> craftItemStack;

		static {
			try {
				nmsItemStack = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ItemStack");
				nbtBase = Class.forName("net.minecraft.server." + getBukkitVersion() + ".NBTBase");
				nbtTagCompound = Class.forName("net.minecraft.server." + getBukkitVersion() + ".NBTTagCompound");
				nbtTagList = Class.forName("net.minecraft.server." + getBukkitVersion() + ".NBTTagList");
				hasKey = nbtTagCompound.getDeclaredMethod("hasKey", String.class);
				set = nbtTagCompound.getDeclaredMethod("set", String.class, nbtBase);
				getCompound = nbtTagCompound.getDeclaredMethod("getCompound", String.class);
				setString = nbtTagCompound.getDeclaredMethod("setString", String.class, String.class);
				hasTag = nmsItemStack.getDeclaredMethod("hasTag");
				getTag = nmsItemStack.getDeclaredMethod("getTag");
				setTag = nmsItemStack.getDeclaredMethod("setTag", nbtTagCompound);
				craftItemStack = Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + ".inventory.CraftItemStack");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static Class<?> getNmsItemStack() {
			return nmsItemStack;
		}

		public static Class<?> getNbtBase() {
			return nbtBase;
		}

		public static Object getNbtTagList() {
			try {
				return nbtTagList.newInstance();
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static boolean hasKey(Object nbtTagCompound, String key) {
			try {
				Object compound = nbtTagCompound;
				if (compound instanceof NBTTagCompound)
					compound = ((NBTTagCompound) compound).get();
				return (boolean) hasKey.invoke(compound, key);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		public static void set(Object nbtTagCompound, String key, Object nbtBase) {
			try {
				Object compound = nbtTagCompound;
				if (compound instanceof NBTTagCompound)
					compound = ((NBTTagCompound) compound).get();
				Object base = nbtBase;
				if (base instanceof NBTTagCompound)
					base = ((NBTTagCompound) base).get();
				set.invoke(compound, key, base);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static NBTTagCompound getCompound(Object nbtTagCompound, String key) {
			try {
				Object compound = nbtTagCompound;
				if (compound instanceof NBTTagCompound)
					compound = ((NBTTagCompound) compound).get();
				return NBTTagCompound.class.getDeclaredConstructor(Object.class).newInstance(getCompound.invoke(compound, key));
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static void setString(Object nbtTagCompound, String key, String value) {
			try {
				Object compound = nbtTagCompound;
				if (compound instanceof NBTTagCompound)
					compound = ((NBTTagCompound) compound).get();
				setString.invoke(compound, key, value);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static boolean hasTag(Object nmsItemStack) {
			try {
				Object itemStack = nmsItemStack;
				if (itemStack instanceof NMSItemStack)
					itemStack = ((NMSItemStack) itemStack).get();
				return (boolean) hasTag.invoke(itemStack);
			} catch (Exception ex) {
				ex.printStackTrace();
				return false;
			}
		}

		public static NBTTagCompound getTag(Object nmsItemStack) {
			try {
				Object itemStack = nmsItemStack;
				if (itemStack instanceof NMSItemStack)
					itemStack = ((NMSItemStack) itemStack).get();
				return NBTTagCompound.class.getDeclaredConstructor(Object.class).newInstance(getTag.invoke(itemStack));
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static void setTag(Object nmsItemStack, Object tag) {
			try {
				Object itemStack = nmsItemStack;
				if (itemStack instanceof NMSItemStack)
					itemStack = ((NMSItemStack) itemStack).get();
				Object actualTag = tag;
				if (actualTag instanceof NBTTagCompound)
					actualTag = ((NBTTagCompound) actualTag).get();
				setTag.invoke(itemStack, actualTag);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static class GameProfile {
		private static Method getGameProfileUUID;
		private static Method getGameProfileName;
		private static Class<?> gameProfile;
		private static Constructor<?> gameProfileConstructor;

		private UUID id;
		private String name;

		public GameProfile(UUID id, String name) {
			this.id = id;
			this.name = name;
		}

		static {
			try {
				gameProfile = Class.forName("com.mojang.authlib.GameProfile");
				gameProfileConstructor = gameProfile.getDeclaredConstructor(UUID.class, String.class);
				getGameProfileUUID = gameProfile.getDeclaredMethod("getId");
				getGameProfileName = gameProfile.getDeclaredMethod("getName");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public UUID getId() {
			return id;
		}

		public void setId(UUID id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static Class<?> getComponentType() {
			return gameProfile;
		}

		public Object get() {
			try {
				return gameProfileConstructor.newInstance(id, name);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static GameProfile valueOf(Object profile) {
			try {
				UUID id = (UUID) getGameProfileUUID.invoke(profile);
				String name = (String) getGameProfileName.invoke(profile);
				return newInstance(id, name);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static GameProfile newInstance(UUID id, String name) {
			try {
				return GameProfile.class.getDeclaredConstructor(UUID.class, String.class).newInstance(id, name);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	public static class Packet {
		private static Class<?> packet;
		private static Method sendPacket;
		private static Class<?> packetDataSerializer;
		private static Constructor<?> packetDataSerializerConstructor;
		private static Method serializePacketData;

		static {
			try {
				packet = Class.forName("net.minecraft.server." + getBukkitVersion() + ".Packet");
				sendPacket = Player.getPlayerConnection().getDeclaredMethod("sendPacket", packet);
				packetDataSerializer = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketDataSerializer");
				packetDataSerializerConstructor = packetDataSerializer.getDeclaredConstructor(ByteBuf.class);
				serializePacketData = packetDataSerializer.getMethod("a", String.class);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static Class<?> getPacketDataSerializer() {
			return packetDataSerializer;
		}

		public static class DataSerializer {
			private Object dataSerializer;

			public DataSerializer(Object dataSerializer) {
				this.dataSerializer = dataSerializer;
			}

			public Object serializePacketData(String data) {
				try {
					return serializePacketData.invoke(dataSerializer, data);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}

			public static DataSerializer newInstance(Object byteBuf) {
				try {
					return DataSerializer.class.getDeclaredConstructor(Object.class).newInstance(packetDataSerializerConstructor.newInstance(byteBuf));
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}

		public static void sendPacket(Object playerConnection, Object packet) {
			try {
				sendPacket.invoke(playerConnection, packet);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static class Title {
			public static Object TITLE;
			public static Object SUBTITLE;
			private static Class<?> packetPlayOutTitle;
			private static Class<?> enumTitleAction;
			private static Constructor<?> packetPlayOutTitleConstructor;

			static {
				try {
					packetPlayOutTitle = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutTitle");
					enumTitleAction = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutTitle$EnumTitleAction");
					packetPlayOutTitleConstructor = packetPlayOutTitle.getDeclaredConstructor(enumTitleAction, ChatSerializer.getChatBaseComponent());
					TITLE = enumTitleAction.getField("TITLE").get(null);
					SUBTITLE = enumTitleAction.getField("SUBTITLE").get(null);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Object newInstance(Object titleAction, Object chatBaseComponent) {
				try {
					return packetPlayOutTitleConstructor.newInstance(titleAction, chatBaseComponent);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}

		public static class TitleLength {
			private static Class<?> packetPlayOutTitle;
			private static Constructor<?> packetPlayOutTitleConstructorLength;

			static {
				try {
					packetPlayOutTitle = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutTitle");
					packetPlayOutTitleConstructorLength = packetPlayOutTitle.getDeclaredConstructor(int.class, int.class, int.class);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Object newInstance(int a, int b, int c) {
				try {
					return packetPlayOutTitleConstructorLength.newInstance(a, b, c);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}

		public static class Chat {
			private static Class<?> packetPlayOutChat;
			private static Constructor<?> packetPlayOutChatConstructor;

			static {
				try {
					packetPlayOutChat = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutChat");
					packetPlayOutChatConstructor = packetPlayOutChat.getDeclaredConstructor(ChatSerializer.getChatBaseComponent());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Object newInstance(Object chatBaseComponent) {
				try {
					return packetPlayOutChatConstructor.newInstance(chatBaseComponent);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}

		public static class Payload {
			private static Class<?> packetPlayOutCustomPayload;
			private static Constructor<?> packetPlayOutCustomPayloadConstructor;

			public static Object BRAND;

			static {
				try {
					packetPlayOutCustomPayload = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutCustomPayload");
					BRAND = packetPlayOutCustomPayload.getField("b").get(null);
					packetPlayOutCustomPayloadConstructor = packetPlayOutCustomPayload.getDeclaredConstructor(MinecraftKey.get(), getPacketDataSerializer());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Object newInstance(Object key, Object packetDataSerializer) {
				try {
					return packetPlayOutCustomPayloadConstructor.newInstance(key, packetDataSerializer);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}

		public static class PlayerList {
			private static Class<?> packetPlayOutPlayerListHeaderFooter;

			static {
				try {
					packetPlayOutPlayerListHeaderFooter = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutPlayerListHeaderFooter");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Object newInstance() {
				try {
					return packetPlayOutPlayerListHeaderFooter.newInstance();
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}
		}

		public static class PlayerInfo {

			private static Class<?> playerInteractManager;
			private static Class<?> craftWorld;
			private static Class<?> world;
			private static Class<?> worldServer;
			private static Class<?> packetPlayOutPlayerInfo;
			private static Class<?> packetPlayOutNamedEntitySpawn;
			private static Class<?> enumPlayerInfoAction;
			private static Class<?> entityHuman;
			private static Method getHandle;
			private static Method teleportTo;
			private static Method setInvisible;
			private static Constructor<?> playerInteractManagerConstructor;
			private static Constructor<?> packetPlayOutPlayerInfoConstructor;
			private static Constructor<?> packetPlayOutNamedEntitySpawnConstructor;
			private static Constructor<?> entityPlayerConstructor;

			public static Object ADD_PLAYER;

			static {
				try {
					packetPlayOutPlayerInfo = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutPlayerInfo");
					packetPlayOutNamedEntitySpawn = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutNamedEntitySpawn");
					enumPlayerInfoAction = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
					ADD_PLAYER = enumPlayerInfoAction.getField("ADD_PLAYER").get(null);
					craftWorld = Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + ".CraftWorld");
					world = Class.forName("net.minecraft.server." + getBukkitVersion() + ".World");
					worldServer = Class.forName("net.minecraft.server." + getBukkitVersion() + ".WorldServer");
					playerInteractManager = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PlayerInteractManager");
					getHandle = craftWorld.getDeclaredMethod("getHandle");
					teleportTo = CraftEntity.get().getDeclaredMethod("teleportTo", org.bukkit.Location.class, boolean.class);
					setInvisible = CraftEntity.get().getDeclaredMethod("setInvisible", boolean.class);
					Class<?> entityPlayerArray = Class.forName("[Lnet.minecraft.server." + getBukkitVersion() + ".EntityPlayer;");
					entityHuman = Class.forName("net.minecraft.server." + getBukkitVersion() + ".EntityHuman");
					playerInteractManagerConstructor = playerInteractManager.getDeclaredConstructor(world);
					packetPlayOutPlayerInfoConstructor = packetPlayOutPlayerInfo.getDeclaredConstructor(enumPlayerInfoAction, entityPlayerArray);
					packetPlayOutNamedEntitySpawnConstructor = packetPlayOutNamedEntitySpawn.getDeclaredConstructor(entityHuman);
					entityPlayerConstructor = Player.getEntityPlayer().getConstructor(CraftServer.getMinecraftServer(), getWorldServer(), GameProfile.getComponentType(), getInteractManager());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Class<?> getCraftWorld() {
				return craftWorld;
			}

			public static Class<?> getWorldServer() {
				return worldServer;
			}

			public static Object getWorldServer(org.bukkit.World world) {
				try {
					return getHandle.invoke(craftWorld.cast(world));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}

			public static Class<?> getInteractManager() {
				return playerInteractManager;
			}

			public static Object getInteractManager(Object worldServer) {
				try {
					return playerInteractManagerConstructor.newInstance(worldServer);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				return null;
			}

			public static void spawnNpc(org.bukkit.entity.Player player, org.bukkit.World world, Location location, String name) {
				try {
					Object craftServerObject = CraftServer.getCraftServer().cast(Bukkit.getServer());
					Field console = CraftServer.getCraftServer().getDeclaredField("console");
					console.setAccessible(true);
					Object server = CraftServer.getMinecraftServer().cast(console.get(Bukkit.getServer()));
					UUID id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes());
					if (Player.getOnlineMode(player))
						id = UUID.fromString(Player.getUUID(name, true));
					GameProfile profile = new GameProfile(id, name);
					Object worldServer = getWorldServer(world);
					Object manager = getInteractManager(worldServer);
					Object npc = entityPlayerConstructor.newInstance(server, worldServer, profile.get(), manager);
					Object entityPlayers = Array.newInstance(Player.getEntityPlayer(), 1);
					teleportTo.invoke(npc, location, false);
					setInvisible.invoke(npc, true);
					Array.set(entityPlayers, 0, npc);
					Object infoPacket = packetPlayOutPlayerInfoConstructor.newInstance(ADD_PLAYER, entityPlayers);
					Object spawnPacket = packetPlayOutNamedEntitySpawnConstructor.newInstance(npc);
					Object connection = Player.getPlayerConnection(player);
					Packet.sendPacket(connection, infoPacket);
					Packet.sendPacket(connection, spawnPacket);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}

		public static class Ping {
			private static Method getPingMotd;
			private static Method getPingServerData;
			private static Method getPingPlayerSample;
			private static Method setPingPlayerSample;
			private static Method setPingServerData;
			private static Method setPingMotd;
			private static Method setPingFavicon;
			private static Class<?> packetStatusOutServerInfo;
			private static Class<?> serverPingPlayerSample;
			private static Class<?> serverPing;
			private static Class<?> craftIconCache;
			private static Constructor<?> packetStatusOutServerInfoConstructor;

			static {
				try {
					serverPing = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ServerPing");
					packetStatusOutServerInfo = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PacketStatusOutServerInfo");
					craftIconCache = Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + ".util.CraftIconCache");
					packetStatusOutServerInfoConstructor = packetStatusOutServerInfo.getDeclaredConstructor(serverPing);
					getPingMotd = serverPing.getDeclaredMethod("a");
					getPingServerData = serverPing.getDeclaredMethod("getServerData");
					getPingPlayerSample = serverPing.getDeclaredMethod("b");
					serverPingPlayerSample = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ServerPing$ServerPingPlayerSample");
					setPingPlayerSample = serverPing.getDeclaredMethod("setPlayerSample", serverPingPlayerSample);
					Class<?> serverData = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ServerPing$ServerData");
					setPingServerData = serverPing.getDeclaredMethod("setServerInfo", serverData);
					setPingMotd = serverPing.getDeclaredMethod("setMOTD", ChatSerializer.getChatBaseComponent());
					setPingFavicon = serverPing.getDeclaredMethod("setFavicon", String.class);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static Class<?> getPacketType() {
				return packetStatusOutServerInfo;
			}

			public static Class<?> getCraftIconCache() {
				return craftIconCache;
			}

			public static Class<?> getServerPing() {
				return serverPing;
			}

			public static Object newInstance(Object packet) {
				try {
					return packetStatusOutServerInfoConstructor.newInstance(packet);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}

			public static String getMOTD(Object packet) {
				try {
					return CraftGo.ChatSerializer.exportToString(getPingMotd.invoke(packet));
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}

			public static void setMOTD(Object packet, String motd) {
				try {
					setPingMotd.invoke(packet, ChatSerializer.serializeJson(motd));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static PlayerSample getPlayerSample(Object packet) {
				try {
					Object playerSample = getPingPlayerSample.invoke(packet);
					int max = PlayerSample.getMaxPlayers(playerSample);
					int online = PlayerSample.getOnlinePlayers(playerSample);
					Object profiles = PlayerSample.getProfiles(playerSample);
					return PlayerSample.class.getDeclaredConstructor(int.class, int.class, Object.class).newInstance(max, online, profiles);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}

			public static void setPlayerSample(Object packet, Object playerSample) {
				try {
					Object sample = playerSample;
					if (sample instanceof PlayerSample)
						sample = ((PlayerSample) sample).get();
					setPingPlayerSample.invoke(packet, sample);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static class PlayerSample {
				private static Method getPlayerSampleMax;
				private static Method getPlayerSampleOnline;
				private static Method getPlayerSampleProfiles;
				private static Method setPlayerSampleProfiles;
				private static Constructor<?> serverPingPlayerSampleConstructor;

				private int max;
				private int online;
				private Object profiles;

				public PlayerSample(int max, int online, Object profiles) {
					this.max = max;
					this.online = online;
					this.profiles = profiles;
				}

				public static int getMaxPlayers(Object playerSample) {
					try {
						return (int) getPlayerSampleMax.invoke(playerSample);
					} catch (Exception ex) {
						ex.printStackTrace();
						return 0;
					}
				}

				public int getMaxPlayers() {
					return max;
				}

				public static int getOnlinePlayers(Object playerSample) {
					try {
						return (int) getPlayerSampleOnline.invoke(playerSample);
					} catch (Exception ex) {
						ex.printStackTrace();
						return 0;
					}
				}

				public int getOnlinePlayers() {
					return online;
				}

				public static Object getProfiles(Object playerSample) {
					try {
						return getPlayerSampleProfiles.invoke(playerSample);
					} catch (Exception ex) {
						ex.printStackTrace();
						return 0;
					}
				}

				public Object getProfiles() {
					return profiles;
				}

				public static void setProfiles(Object playerSample, Object profiles) {
					try {
						setPlayerSampleProfiles.invoke(playerSample, profiles);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				public void setProfiles(Object profiles) {
					this.profiles = profiles;
				}

				public Object get() {
					try {
						Object playerSample = serverPingPlayerSampleConstructor.newInstance(max, online);
						setProfiles(playerSample, profiles);
						return playerSample;
					} catch (Exception ex) {
						ex.printStackTrace();
						return null;
					}
				}

				public static Object newInstance(int max, int online) {
					try {
						return serverPingPlayerSampleConstructor.newInstance(max, online);
					} catch (Exception ex) {
						ex.printStackTrace();
						return null;
					}
				}

				static {
					try {
						getPlayerSampleMax = serverPingPlayerSample.getDeclaredMethod("a");
						getPlayerSampleOnline = serverPingPlayerSample.getDeclaredMethod("b");
						getPlayerSampleProfiles = serverPingPlayerSample.getDeclaredMethod("c");
						setPlayerSampleProfiles = serverPingPlayerSample.getDeclaredMethod("a", Class.forName("[Lcom.mojang.authlib.GameProfile;"));
						serverPingPlayerSampleConstructor = serverPingPlayerSample.getDeclaredConstructor(int.class, int.class);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}

			public static ServerInfo getServerInfo(Object packet) {
				try {
					Object serverData = getPingServerData.invoke(packet);
					int protocolVersion = ServerInfo.getProtocolVersion(serverData);
					String protocolName = ServerInfo.getProtocolName(serverData);
					return ServerInfo.class.getDeclaredConstructor(String.class, int.class).newInstance(protocolName, protocolVersion);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}
			}

			public static void setServerInfo(Object packet, Object serverInfo) {
				try {
					Object serverData = serverInfo;
					if (serverData instanceof ServerInfo)
						serverData = ((ServerInfo) serverData).get();
					setPingServerData.invoke(packet, serverData);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static void setFavicon(Object packet, String favicon) {
				try {
					setPingFavicon.invoke(packet, favicon);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			public static class ServerInfo {
				private int protocolVersion;
				private String protocolName;

				private static Class<?> serverData;
				private static Constructor<?> serverDataConstructor;
				private static Method getProtocolName;
				private static Method getProtocolVersion;

				public ServerInfo(String protocolName, int protocolVersion) {
					this.protocolVersion = protocolVersion;
					this.protocolName = protocolName;
				}

				public static int getProtocolVersion(Object serverData) {
					try {
						return (int) getProtocolVersion.invoke(serverData);
					} catch (Exception ex) {
						ex.printStackTrace();
						return -1;
					}
				}

				public int getProtocolVersion() {
					return protocolVersion;
				}

				public static String getProtocolName(Object serverData) {
					try {
						return (String) getProtocolName.invoke(serverData);
					} catch (Exception ex) {
						ex.printStackTrace();
						return null;
					}
				}

				public String getProtocolName() {
					return protocolName;
				}

				static {
					try {
						serverData = Class.forName("net.minecraft.server." + getBukkitVersion() + ".ServerPing$ServerData");
						serverDataConstructor = serverData.getDeclaredConstructor(String.class, int.class);
						getProtocolName = serverData.getDeclaredMethod("a");
						getProtocolVersion = serverData.getDeclaredMethod("getProtocolVersion");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				public Object get() {
					try {
						return serverDataConstructor.newInstance(protocolName, protocolVersion);
					} catch (Exception ex) {
						ex.printStackTrace();
						return null;
					}
				}
			}
		}
	}

	public static class Dimension {
		private static Class<?> dimensionManager;
		public static Object UNKNOWN;
		public static Object OVERWORLD;
		public static Object NETHER;
		public static Object THE_END;

		static {
			try {
				dimensionManager = Class.forName("net.minecraft.server." + getBukkitVersion() + ".DimensionManager");
				UNKNOWN = 0;
				OVERWORLD = dimensionManager.getField("OVERWORLD").get(null);
				NETHER = dimensionManager.getField("NETHER").get(null);
				THE_END = dimensionManager.getField("THE_END").get(null);
			} catch (Exception ex) {

			}
		}

		public static Class<?> getDimensionManager() {
			return dimensionManager;
		}
	}

	public static class Minecraft extends CraftGo {
		public static String getPackageVersion() {
			return Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
		}
	}

	public static class MobSpawner extends CraftGo {
		public static EntityType getSpawnerEntityType(Block block) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("getSpawnerEntityID called on non-CreatureSpawner block: " + block);
				return null;
			}
			CreatureSpawner spawner = (CreatureSpawner) blockState;
			return spawner.getSpawnedType();
		}

		public static CreatureSpawner getSpawnerInfo(Block block) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("getSpawner called on non-CreatureSpawner block: " + block);
				throw new NullPointerException();
			}
			return (CreatureSpawner) blockState;
		}

		public static void setSpawnerSpawnRate(Block block, int spawnRate) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("setSpawnerSpawnRate called on non-CreatureSpawner block: " + block);
				throw new NullPointerException();
			}
			CreatureSpawner spawner = (CreatureSpawner) blockState;
			spawner.setDelay(spawnRate);
			spawner.update();
		}

		public static EntityType getStoredSpawnerItemEntityType(org.bukkit.inventory.ItemStack item) {
			if (item.hasItemMeta())
				return searchItemMeta(item.getItemMeta());
			return null;
		}

		private static EntityType searchItemMeta(ItemMeta meta) {
			EntityType type = null;
			if (meta.hasLore() && !meta.getLore().isEmpty()) {
				for (String entityIDString : meta.getLore()) {
					if (entityIDString.contains("Entity Type:")) {
						String[] entityIDArray = entityIDString.split(": ");
						if (entityIDArray.length == 2) {
							try {
								type = EntityType.valueOf(entityIDArray[1].toUpperCase());
							} catch (Exception e) {
								return null;
							}
						}
					}
				}
			}
			return type;
		}

		public static void setSpawnerEntityType(Block block, EntityType type) {
			BlockState blockState = block.getState();
			if (!(blockState instanceof CreatureSpawner)) {
				Bukkit.getLogger().warning("setSpawnerEntityID called on non-CreatureSpawner block: " + block);
				return;
			}
			((CreatureSpawner) blockState).setSpawnedType(type);
			blockState.update(true);
		}

		public static org.bukkit.inventory.ItemStack newSpawnerItem(EntityType type, int amount) {
			org.bukkit.inventory.ItemStack item = new org.bukkit.inventory.ItemStack(Material.SPAWNER, amount);
			String entityName = type.toString().toLowerCase();
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(ChatColor.RESET + "Entity Type: " + entityName);
			meta.setLore(lore);
			meta.setDisplayName(ChatColor.RESET + $.capitalizeAll(WordUtils.capitalize(entityName) + " Spawner", "_"));
			item.setItemMeta(meta);
			return setNBTEntityID(item, entityName);
		}

		private static org.bukkit.inventory.ItemStack setNBTEntityID(org.bukkit.inventory.ItemStack item, String entity) {
			if ((item == null) || (entity == null) || (entity.isEmpty())) {
				Bukkit.getLogger().warning("Skipping invalid spawner to set NBT data on.");
				return null;
			}
			try {
				Object craftStack = CraftItemStack.asCraftCopy(item);
				NMSItemStack itemStack = CraftItemStack.asNMSCopy(craftStack);
				NBTTagCompound tag = NBT.getTag(itemStack);
				if (tag == null) {
					tag = new NBTTagCompound();
					itemStack.setTag(tag);
				}
				if (!tag.hasKey("BlockEntityTag")) {
					tag.set("BlockEntityTag", new NBTTagCompound());
				}
				tag.getCompound("BlockEntityTag").setString("EntityId", entity);
				if (!tag.hasKey("SpawnData")) {
					tag.set("SpawnData", new NBTTagCompound());
				}
				tag.getCompound("SpawnData").setString("id", entity);
				if (!tag.getCompound("BlockEntityTag").hasKey("SpawnPotentials")) {
					tag.getCompound("BlockEntityTag").set("SpawnPotentials", new NBTTagCompound());
				}
				if (!tag.hasKey("EntityTag")) {
					tag.set("EntityTag", new NBTTagCompound());
				}
				if (!entity.startsWith("minecraft:")) {
					entity = "minecraft:" + entity;
				}
				tag.getCompound("EntityTag").setString("id", entity);
				return (org.bukkit.inventory.ItemStack) CraftItemStack.asCraftMirror(itemStack);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public static int convertEntityTypeToEntityId(EntityType entityType) {
			switch (entityType) {
				case CREEPER:
					return 50;
				case SKELETON:
					return 51;
				case SPIDER:
					return 52;
				case GIANT:
					return 53;
				case ZOMBIE:
					return 54;
				case SLIME:
					return 55;
				case GHAST:
					return 56;
				case PIG_ZOMBIE:
					return 57;
				case ENDERMAN:
					return 58;
				case CAVE_SPIDER:
					return 59;
				case SILVERFISH:
					return 60;
				case BLAZE:
					return 61;
				case MAGMA_CUBE:
					return 62;
				case ENDER_DRAGON:
					return 63;
				case WITHER:
					return 64;
				case BAT:
					return 65;
				case WITCH:
					return 66;
				case PIG:
					return 90;
				case SHEEP:
					return 91;
				case COW:
					return 92;
				case CHICKEN:
					return 93;
				case SQUID:
					return 94;
				case WOLF:
					return 95;
				case MUSHROOM_COW:
					return 96;
				case SNOWMAN:
					return 97;
				case OCELOT:
					return 98;
				case IRON_GOLEM:
					return 99;
				case HORSE:
					return 100;
				default:
					return 0;
			}
		}

		public static EntityType convertEntityIdToEntityType(int entityId) {
			switch (entityId) {
				case 50:
					return EntityType.CREEPER;
				case 51:
					return EntityType.SKELETON;
				case 52:
					return EntityType.SPIDER;
				case 53:
					return EntityType.GIANT;
				case 54:
					return EntityType.ZOMBIE;
				case 55:
					return EntityType.SLIME;
				case 56:
					return EntityType.GHAST;
				case 57:
					return EntityType.PIG_ZOMBIE;
				case 58:
					return EntityType.ENDERMAN;
				case 59:
					return EntityType.CAVE_SPIDER;
				case 60:
					return EntityType.SILVERFISH;
				case 61:
					return EntityType.BLAZE;
				case 62:
					return EntityType.MAGMA_CUBE;
				case 63:
					return EntityType.ENDER_DRAGON;
				case 64:
					return EntityType.WITHER;
				case 65:
					return EntityType.BAT;
				case 66:
					return EntityType.WITCH;
				case 90:
					return EntityType.PIG;
				case 91:
					return EntityType.SHEEP;
				case 92:
					return EntityType.COW;
				case 93:
					return EntityType.CHICKEN;
				case 94:
					return EntityType.SQUID;
				case 95:
					return EntityType.WOLF;
				case 96:
					return EntityType.MUSHROOM_COW;
				case 97:
					return EntityType.SNOWMAN;
				case 98:
					return EntityType.OCELOT;
				case 99:
					return EntityType.IRON_GOLEM;
				case 100:
					return EntityType.HORSE;
				default:
					return EntityType.UNKNOWN;
			}
		}
	}

	public static class ItemStack extends CraftGo {
		public static org.bukkit.inventory.ItemStack removeAttributes(org.bukkit.inventory.ItemStack item) {
			if (item == null || item.getType() == Material.WRITABLE_BOOK)
				return item;
			org.bukkit.inventory.ItemStack itm = item.clone();
			try {
				NMSItemStack nmsStack = CraftItemStack.asNMSCopy(itm);
				NBTTagCompound tag;
				if (!nmsStack.hasTag()) {
					tag = new NBTTagCompound();
					nmsStack.setTag(tag);
				} else
					tag = nmsStack.getTag();
				Object am = NBT.getNbtTagList();
				tag.set("AttributeModifiers", am);
				nmsStack.setTag(tag);
				return CraftItemStack.asBukkitCopy(nmsStack);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		public static Object getTag(org.bukkit.inventory.ItemStack item) {
			try {
				NMSItemStack nmsStack = CraftItemStack.asNMSCopy(item);
				NBTTagCompound tag = nmsStack.getTag();
				if (tag == null) {
					tag = new NBTTagCompound();
				}
				return tag;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
	}

	public static class BarApi implements Listener {
		private HashMap<UUID, FakeDragon> players = new HashMap<UUID, FakeDragon>();
		private HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();
		public String version;

		public void setMessage(String message, BarColor color, BarStyle style) {
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				setMessage(player, message, color, style);
			}
		}

		public void setMessage(org.bukkit.entity.Player player, String message, BarColor color, BarStyle style) {
			FakeDragon dragon = getDragon(player, message);
			dragon.getBar().setColor(color);
			dragon.getBar().setStyle(style);
			dragon.getBar().setTitle(cleanMessage(message));
			dragon.getBar().setProgress(100 / 100);
			cancelTimer(player);
			sendDragon(dragon, player);
		}

		public void setMessage(String message, float percent, BarColor color, BarStyle style) {
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				setMessage(player, message, percent, color, style);
			}
		}

		public void setMessage(org.bukkit.entity.Player player, String message, float percent, BarColor color, BarStyle style) {
			Validate.isTrue(0F <= percent && percent <= 100F, "Percent must be between 0F and 100F, but was: ", percent);
			FakeDragon dragon = getDragon(player, message);
			dragon.getBar().setColor(color);
			dragon.getBar().setStyle(style);
			dragon.getBar().setTitle(cleanMessage(message));
			dragon.getBar().setProgress(percent / 100);
			cancelTimer(player);
			sendDragon(dragon, player);
		}

		public void setMessage(String message, int seconds, BarColor color, BarStyle style) {
			for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
				setMessage(player, message, seconds, color, style);
			}
		}

		public void setMessage(final org.bukkit.entity.Player player, String message, int seconds, BarColor color, BarStyle style) {
			Validate.isTrue(seconds > 0, "Seconds must be above 1 but was: ", seconds);
			FakeDragon dragon = getDragon(player, message);
			dragon.getBar().setColor(color);
			dragon.getBar().setStyle(style);
			dragon.getBar().setTitle(cleanMessage(message));
			dragon.getBar().setProgress(100 / 100);
			final float dragonHealthMinus = (100 / seconds) / 100;
			cancelTimer(player);
			timers.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), new Runnable() {
				@Override
				public void run() {
					FakeDragon drag = getDragon(player, "");
					drag.getBar().setProgress(drag.getBar().getProgress() - dragonHealthMinus);
					if (drag.getBar().getProgress() <= 1) {
						removeBar(player);
						cancelTimer(player);
					} else {
						sendDragon(drag, player);
					}
				}
			}, 20L, 20L).getTaskId());
			sendDragon(dragon, player);
		}

		public boolean hasBar(org.bukkit.entity.Player player) {
			return players.get(player.getUniqueId()) != null;
		}

		public void removeBar(org.bukkit.entity.Player player) {
			if (!hasBar(player))
				return;
			FakeDragon dragon = getDragon(player, "");
			dragon.getBar().removePlayer(player);
			players.remove(player.getUniqueId());
			cancelTimer(player);
		}

		public void setPercent(org.bukkit.entity.Player player, int percent) {
			if (!hasBar(player))
				return;
			FakeDragon dragon = getDragon(player, "");
			dragon.getBar().setProgress(percent / 100);
			cancelTimer(player);
			if (percent == 0) {
				removeBar(player);
			} else {
				sendDragon(dragon, player);
			}
		}

		public int getPercent(org.bukkit.entity.Player player) {
			if (!hasBar(player))
				return -1;
			return (int) (getDragon(player, "").getBar().getProgress() * 100);
		}

		public String getMessage(org.bukkit.entity.Player player) {
			if (!hasBar(player))
				return "";
			return getDragon(player, "").getBar().getTitle();
		}

		private String cleanMessage(String message) {
			if (message.length() > 64)
				message = message.substring(0, 63);
			return message;
		}

		private void cancelTimer(org.bukkit.entity.Player player) {
			Integer timerID = timers.remove(player.getUniqueId());
			if (timerID != null) {
				Bukkit.getScheduler().cancelTask(timerID);
			}
		}

		private void sendDragon(FakeDragon dragon, org.bukkit.entity.Player player) {
			BossBar bar = dragon.getBar();
			bar.addPlayer(player);
		}

		private FakeDragon getDragon(org.bukkit.entity.Player player, String message) {
			if (hasBar(player)) {
				return players.get(player.getUniqueId());
			} else {
				return addDragon(player, cleanMessage(message));
			}
		}

		private FakeDragon addDragon(org.bukkit.entity.Player player, String message) {
			FakeDragon dragon = newDragon(message);
			BossBar bar = dragon.getBar();
			bar.addPlayer(player);
			players.put(player.getUniqueId(), dragon);
			return dragon;
		}

		public void onEnable() {
			Bukkit.getPluginManager().registerEvents(this, Server.getPlugin());
		}

		public void onDisable() {
			for (org.bukkit.entity.Player player : Server.getPlugin().getServer().getOnlinePlayers()) {
				quit(player);
			}
			players.clear();
			for (int timerID : timers.values()) {
				Bukkit.getScheduler().cancelTask(timerID);
			}
			timers.clear();
		}

		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void PlayerLoggout(PlayerQuitEvent event) {
			quit(event.getPlayer());
		}

		@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
		public void onPlayerKick(PlayerKickEvent event) {
			quit(event.getPlayer());
		}

		private void quit(org.bukkit.entity.Player player) {
			removeBar(player);
		}

		public FakeDragon newDragon(String message) {
			return new FakeDragon(message);
		}

		public class FakeDragon {
			private BossBar bar;

			public FakeDragon(String name, int percent) {
				bar = Bukkit.createBossBar(name, BarColor.PINK, BarStyle.SOLID);
				bar.setProgress(percent / 100);
			}

			public FakeDragon(String name) {
				bar = Bukkit.createBossBar(name, BarColor.PINK, BarStyle.SOLID);
			}

			public BossBar getBar() {
				return bar;
			}
		}
	}

	public static class Player extends CraftGo {
		private static Class<?> craftPlayer;
		private static Class<?> entityPlayer;
		private static Class<?> playerConnection;
		private static Method getHandle;

		static {
			try {
				craftPlayer = Class.forName("org.bukkit.craftbukkit." + getBukkitVersion() + ".entity.CraftPlayer");
				entityPlayer = Class.forName("net.minecraft.server." + getBukkitVersion() + ".EntityPlayer");
				playerConnection = Class.forName("net.minecraft.server." + getBukkitVersion() + ".PlayerConnection");
				getHandle = craftPlayer.getDeclaredMethod("getHandle");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static Class<?> get() {
			return craftPlayer;
		}

		public static Class<?> getEntityPlayer() {
			return entityPlayer;
		}

		public static Class<?> getPlayerConnection() {
			return playerConnection;
		}

		public static Object getPlayerConnection(org.bukkit.entity.Player player) {
			try {
				return entityPlayer.getField("playerConnection").get(getHandle(player));
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static Object getHandle(org.bukkit.entity.Player player) {
			try {
				return getHandle.invoke(player);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static void clearArrowsInBody(org.bukkit.entity.Player player) {
			try {
				Object dataWatcherObject = DataWatcher.DataWatcherObject.newInstance(10, DataWatcher.getDataWatcherSerializer());
				DataWatcher.getDataWatcher(player).set(dataWatcherObject, 0);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static int getConnectionLatency(org.bukkit.entity.Player player) {
			try {
				return (int) getEntityPlayer().getField("ping").getInt(getHandle(player));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return 0;
		}

		public static int getLegacyMinecraftPlaytime(org.bukkit.entity.Player player) {
			return (player.getStatistic(Statistic.PLAY_ONE_MINUTE) * 60 * 20) / 20;
		}

		public static String getProtocolVersionName(org.bukkit.entity.Player player) {
			if (!$.isPluginEnabled("ProtocolSupport")) {
				return $.formatPackageVersion(Minecraft.getPackageVersion());
			} else {
				String ver = protocolsupport.api.ProtocolSupportAPI.getProtocolVersion(player).getName();
				if (ver == null || ver.equals("null"))
					ver = "1.4.7";
				if (ver.equals("PE")) {
					return "Pocket Edition";
				} else {
					return ver.toUpperCase();
				}
			}
		}

		public static boolean isPocketPlayer(org.bukkit.entity.Player player) {
			if (!$.isPluginEnabled("ProtocolSupport")) {
				return false;
			} else {
				try {
					protocolsupport.api.ProtocolType protocolType = protocolsupport.api.ProtocolSupportAPI.getProtocolVersion(player).getProtocolType();
					return protocolType == protocolsupport.api.ProtocolType.valueOf("PE");
				} catch (Exception ex) {
				}
				return false;
			}
		}

		public static int getProtocolVersion(org.bukkit.entity.Player player) {
			if (!$.isPluginEnabled("ProtocolSupport")) {
				return 338;
			} else {
				return protocolsupport.api.ProtocolSupportAPI.getProtocolVersion(player).getId();
			}
		}

		public static boolean getOnlineMode(org.bukkit.entity.Player player) {
			if (!$.isPluginEnabled("ProtocolSupport")) {
				return Bukkit.getOnlineMode();
			} else {
				return protocolsupport.api.ProtocolSupportAPI.getConnection(player).getProfile().isOnlineMode();
			}
		}

		public static void sendJson(final org.bukkit.entity.Player player, String json) {
			try {
				Packet.sendPacket(getPlayerConnection(player), Packet.Chat.newInstance(ChatSerializer.serializeJson(json)));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static List<String> getAccounts(final org.bukkit.entity.Player player, String exclusion) {
			String ipAddress = player.getAddress().getAddress().getHostAddress();
			String encodedIp = SessionManager.encodeHex(ipAddress);
			if (Server.getGeolocationCache().getData().contains(encodedIp + ".accounts")) {
				List<String> accounts = Server.getGeolocationCache().getData().getStringList(encodedIp + ".accounts");
				accounts.remove(exclusion.toString());
				return accounts;
			} else {
				Bukkit.getServer().getLogger().warning("Failed to retrieve accounts under address  \"" + ipAddress + "\".");
				return Arrays.asList(new String[]{player.getName()});
			}
		}

		public static List<String> getAccounts(final org.bukkit.entity.Player player, Collection<String> exclusions) {
			String ipAddress = player.getAddress().getAddress().getHostAddress();
			String encodedIp = SessionManager.encodeHex(ipAddress);
			if (Server.getGeolocationCache().getData().contains(encodedIp + ".accounts")) {
				List<String> accounts = Server.getGeolocationCache().getData().getStringList(encodedIp + ".accounts");
				accounts.removeAll(exclusions);
				return accounts;
			} else {
				Bukkit.getServer().getLogger().warning("Failed to retrieve accounts under address  \"" + ipAddress + "\".");
				return Arrays.asList(new String[]{player.getName()});
			}
		}

		public static List<String> getAccounts(final org.bukkit.entity.Player player) {
			String ipAddress = queryIpLocation(player).getEndpoint();
			String encodedIp = SessionManager.encodeHex(ipAddress);
			if (Server.getGeolocationCache().getData().contains(encodedIp + ".accounts")) {
				return Server.getGeolocationCache().getData().getStringList(encodedIp + ".accounts");
			} else {
				Bukkit.getServer().getLogger().warning("Failed to retrieve accounts under address  \"" + ipAddress + "\".");
				return Arrays.asList(new String[]{player.getName()});
			}
		}

		public static boolean isProxyAddress(String ipAddress) {
			if ((ipAddress.equals("127.0.0.1")) || (ipAddress.matches("192\\.168\\.[01]{1}\\.[0-9]{1,3}"))) {
				return false;
			}
			try {
				String response = readURL("https://v2.api.iphub.info/ip/" + ipAddress, "MjU3NjplTEROdnhwZTR3aDNnQUlVWEFDWGJWb2dyVWdMUGxKSQ==");
				String blockValue = response.substring(response.indexOf("\"block\":") + "\"block\":".length());
				blockValue = blockValue.substring(0, blockValue.indexOf(","));
				int intBlockValue = Integer.parseInt(blockValue);
				if (intBlockValue == 1)
					return true;
				return false;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return false;
		}

		public static boolean updateGeolocationCache(final org.bukkit.entity.Player player, boolean skipLocation) {
			return updateGeolocationCache(player, skipLocation, null);
		}

		public static boolean updateGeolocationCache(final org.bukkit.entity.Player player, boolean skipLocation, String address) {
			String ipAddress = address;
			if (ipAddress == null)
				ipAddress = player.getAddress().getAddress().getHostAddress();
			String encodedIp = SessionManager.encodeHex(ipAddress);
			boolean returnValue = true;
			if (!skipLocation)
				returnValue = updateGeolocationCache(ipAddress);
			List<String> accounts = new ArrayList<String>();
			accounts.add(player.getName().toString());
			String uuidString = getUUID(player.getName().toString(), false, false);
			if (!(uuidString == null))
				for (String username : getNameHistory(uuidString))
					if (!(accounts.contains(username.toString())))
						accounts.add(username);
			Server.getGeolocationCache().getData().set(encodedIp + ".accounts", accounts);
			Server.getGeolocationCache().saveData();
			if (returnValue && address == null)
				updateGeolocationCache(player, returnValue, queryStoredIpLocation(player.getAddress().getAddress().getHostAddress()).getEndpoint());
			return returnValue;
		}

		public static boolean updateGeolocationCache(final String ipAddress) {
			try {
				String queryIpAddress = ipAddress;
				if (queryIpAddress.startsWith("192.168.") || queryIpAddress.equals("127.0.0.1"))
					queryIpAddress = "";
				URL url = new URL("http://ip-api.com/json/" + queryIpAddress);
				BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuilder entirePage = new StringBuilder();
				String inputLine;
				while ((inputLine = stream.readLine()) != null)
					entirePage.append(inputLine);
				stream.close();
				if (!(entirePage.toString().contains("\"country\":\"")))
					return false;
				if (!(entirePage.toString().contains("\"regionName\":\"")))
					return false;
				if (!(entirePage.toString().contains("\"city\":\"")))
					return false;
				if (!(entirePage.toString().contains("\"isp\":\"")))
					return false;
				String country = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"country\":\"")[1].split("\",")[0]);
				String state = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"regionName\":\"")[1].split("\",")[0]);
				String city = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"city\":\"")[1].split("\",")[0]);
				String isp = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"isp\":\"")[1].split("\",")[0]);
				String query = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"query\":\"")[1].split("\",")[0]);
				updateGeolocationCache(country, state, city, isp, query, query);
				updateGeolocationCache(country, state, city, isp, ipAddress, query);
				return true;
			} catch (Exception ex) {
				return false;
			}
		}

		public static void updateGeolocationCache(final String country, final String state, final String city, final String isp, final String ipAddress, final String endpoint) {
			String encodedIp = SessionManager.encodeHex(ipAddress);
			Server.getGeolocationCache().getData().set(encodedIp + ".country", StringEscapeUtils.unescapeJava(country));
			Server.getGeolocationCache().getData().set(encodedIp + ".state", StringEscapeUtils.unescapeJava(state));
			Server.getGeolocationCache().getData().set(encodedIp + ".city", StringEscapeUtils.unescapeJava(city));
			Server.getGeolocationCache().getData().set(encodedIp + ".isp", StringEscapeUtils.unescapeJava(isp));
			Server.getGeolocationCache().getData().set(encodedIp + ".endpoint", StringEscapeUtils.unescapeJava(endpoint));
			Server.getGeolocationCache().saveData();
		}

		public static IpLocationQuery queryStoredIpLocation(final String ipAddress) {
			try {
				String encodedIp = SessionManager.encodeHex(ipAddress);
				String country = Server.getGeolocationCache().getData().getString(encodedIp + ".country");
				String state = Server.getGeolocationCache().getData().getString(encodedIp + ".state");
				String city = Server.getGeolocationCache().getData().getString(encodedIp + ".city");
				String isp = Server.getGeolocationCache().getData().getString(encodedIp + ".isp");
				String endpoint = Server.getGeolocationCache().getData().getString(encodedIp + ".endpoint");
				String[] accounts = new String[0];
				if (Server.getGeolocationCache().getData().contains(encodedIp + ".accounts"))
					accounts = Server.getGeolocationCache().getData().getStringList(encodedIp + ".accounts").toArray(new String[0]);
				return new IpLocationQuery(country, state, city, isp, ipAddress, endpoint, accounts);
			} catch (Exception ex) {
				return null;
			}
		}

		public static IpLocationQuery queryIpLocation(final org.bukkit.entity.Player player) {
			return queryIpLocation(player.getAddress().getAddress().getHostAddress(), player);
		}

		public static IpLocationQuery queryIpLocation(final String ipAddress) {
			return queryIpLocation(ipAddress, null);
		}

		public static IpLocationQuery queryIpLocation(final String ipAddress, final org.bukkit.entity.Player player) {
			try {
				String encodedIp = SessionManager.encodeHex(ipAddress);
				boolean success = false;
				if (!Server.getGeolocationCache().getData().contains(encodedIp)) {
					if (player == null) {
						success = updateGeolocationCache(ipAddress);
					} else {
						success = updateGeolocationCache(player, false);
					}
				} else {
					success = true;
					if (!(player == null))
						updateGeolocationCache(player, true);
				}
				if (!success)
					return null;
				String country = Server.getGeolocationCache().getData().getString(encodedIp + ".country");
				String state = Server.getGeolocationCache().getData().getString(encodedIp + ".state");
				String city = Server.getGeolocationCache().getData().getString(encodedIp + ".city");
				String isp = Server.getGeolocationCache().getData().getString(encodedIp + ".isp");
				String endpoint = Server.getGeolocationCache().getData().getString(encodedIp + ".endpoint");
				String[] accounts = new String[0];
				if (Server.getGeolocationCache().getData().contains(encodedIp + ".accounts"))
					accounts = Server.getGeolocationCache().getData().getStringList(encodedIp + ".accounts").toArray(new String[0]);
				return new IpLocationQuery(country, state, city, isp, ipAddress, endpoint, accounts);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static IpLocationQuery queryIpLocationNoCache(final String ipAddress) {
			try {
				String queryIpAddress = ipAddress;
				if (queryIpAddress.startsWith("192.168.") || queryIpAddress.equals("127.0.0.1"))
					queryIpAddress = "";
				URL url = new URL("http://ip-api.com/json/" + queryIpAddress);
				BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
				StringBuilder entirePage = new StringBuilder();
				String inputLine;
				while ((inputLine = stream.readLine()) != null)
					entirePage.append(inputLine);
				stream.close();
				if (!(entirePage.toString().contains("\"country\":\"")))
					return null;
				if (!(entirePage.toString().contains("\"regionName\":\"")))
					return null;
				if (!(entirePage.toString().contains("\"city\":\"")))
					return null;
				if (!(entirePage.toString().contains("\"isp\":\"")))
					return null;
				String country = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"country\":\"")[1].split("\",")[0]);
				String state = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"regionName\":\"")[1].split("\",")[0]);
				String city = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"city\":\"")[1].split("\",")[0]);
				String isp = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"isp\":\"")[1].split("\",")[0]);
				String query = StringEscapeUtils.unescapeJava(entirePage.toString().split("\"query\":\"")[1].split("\",")[0]);
				return new IpLocationQuery(country, state, city, isp, ipAddress, query, null);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static void setPlayerListHeaderFooter(final org.bukkit.entity.Player player, String headerString, String footerString) {
			try {
				if (getProtocolVersion(player) < 47)
					return;
				Object playerListPacket = Packet.PlayerList.newInstance();
				try {
					Field header = playerListPacket.getClass().getDeclaredField("header");
					Field footer = playerListPacket.getClass().getDeclaredField("footer");
					header.setAccessible(true);
					footer.setAccessible(true);
					header.set(playerListPacket, ChatSerializer.serializeJson("\"" + headerString + "\""));
					footer.set(playerListPacket, ChatSerializer.serializeJson("\"" + footerString + "\""));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Packet.sendPacket(getPlayerConnection(player), playerListPacket);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static void sendTimedTitleAndSubtitle(final org.bukkit.entity.Player player, TitleSubtitle titleSubtitle) {
			try {
				if (titleSubtitle.getTitle() == null)
					titleSubtitle.setTitle("");
				if (titleSubtitle.getSubtitle() == null)
					titleSubtitle.setSubtitle("");
				if (getProtocolVersion(player) < 47) {
					if (titleSubtitle.getTitle().length() > 0)
						player.sendMessage(titleSubtitle.getTitle());
					if (titleSubtitle.getSubtitle().length() > 0)
						player.sendMessage(titleSubtitle.getSubtitle());
					return;
				}
				if (titleSubtitle.isForceChatNotify()) {
					if (titleSubtitle.getTitle().length() > 0)
						player.sendMessage(titleSubtitle.getTitle());
					if (titleSubtitle.getSubtitle().length() > 0)
						player.sendMessage(titleSubtitle.getSubtitle());
				}
				Object chatTitle = ChatSerializer.serializeJson(ComponentSerializer.toString(new TextComponent(titleSubtitle.getTitle())));
				Object chatSubTitle = ChatSerializer.serializeJson(ComponentSerializer.toString(new TextComponent(titleSubtitle.getSubtitle())));
				Object title = Packet.Title.newInstance(Packet.Title.TITLE, chatTitle);
				Object subTitle = Packet.Title.newInstance(Packet.Title.SUBTITLE, chatSubTitle);
				Object length = Packet.TitleLength.newInstance(titleSubtitle.getFadeIn(), titleSubtitle.getStay(), titleSubtitle.getFadeOut());
				Packet.sendPacket(getPlayerConnection(player), title);
				Packet.sendPacket(getPlayerConnection(player), subTitle);
				Packet.sendPacket(getPlayerConnection(player), length);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		public static Optional<SkinModel> getSkinProperty(String uuid) {
			if (uuid == null || uuid.equals("null"))
				return Optional.empty();
			String skinurl = "https://sessionserver.mojang.com/session/minecraft/profile/";
			String output = null;
			try {
				output = readURL(skinurl + uuid + "?unsigned=false");
				String signature = output.substring(output.indexOf("\"signature\":\"") + "\"signature\":\"".length());
				signature = signature.substring(0, signature.indexOf("\""));
				String value = output.substring(output.indexOf("\"value\":\"") + "\"value\":\"".length());
				value = value.substring(0, value.indexOf("\""));
				return Optional.of(SkinModel.createSkinFromEncoded(value, signature));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return Optional.empty();
		}

		private static String readURL(String url) {
			return readURL(url, null);
		}

		private static String readURL(String url, String apiKey) {
			try {
				HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
				con.setRequestMethod("GET");
				con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
				if (!(apiKey == null))
					con.setRequestProperty("X-Key", apiKey);
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);
				con.setDoOutput(true);
				String line;
				StringBuilder output = new StringBuilder();
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				while ((line = in.readLine()) != null)
					output.append(line);
				in.close();
				return output.toString();
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}

		public static String[] getNameHistory(String uuid) {
			if (uuid == null)
				return null;
			String response = readURL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names");
			if (response == null)
				return null;
			ArrayList<String> usernames = new ArrayList<String>();
			String[] breaks = response.split("name\":\"");
			for (int i = 0; i < breaks.length; i++) {
				if (i == 0)
					continue;
				String usernameAtBreak = breaks[i].substring(0, breaks[i].indexOf("\""));
				usernames.add(usernameAtBreak);
			}
			return usernames.toArray(new String[0]);
		}

		public static OfflinePlayer getOfflinePlayer(UUID id) {
			return Bukkit.getOfflinePlayer(id);
		}

		public static OfflinePlayer getOfflinePlayer(String name) {
			if (Bukkit.getServer().getOnlineMode()) {
				return Bukkit.getOfflinePlayer(UUID.fromString(getUUID(name, true)));
			}
			return Bukkit.getOfflinePlayer(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes()));
		}

		private static void applyUUIDCache(String name, String uuid, boolean recursive) {
			name = name.toLowerCase();
			int hits = 0;
			if (!Server.getUUIDCache().getData().contains(name)) {
				if (uuid == null) {
					Server.getUUIDCache().getData().set(name + ".id", "null");
				} else {
					Server.getUUIDCache().getData().set(name + ".id", uuid);
				}
				Server.getUUIDCache().getData().set(name + ".ts", System.currentTimeMillis());
				hits++;
			}
			if (recursive) {
				org.bukkit.entity.Player player = Bukkit.getServer().getPlayerExact(name);
				if (player == null)
					return;
				List<String> accounts = getAccounts(player);
				for (int i = 0; i < accounts.size(); i++) {
					String account = accounts.get(i).toLowerCase();
					if (!Server.getUUIDCache().getData().contains(account)) {
						if (uuid == null) {
							Server.getUUIDCache().getData().set(account + ".id", "null");
						} else {
							Server.getUUIDCache().getData().set(account + ".id", uuid);
						}
						Server.getUUIDCache().getData().set(account + ".ts", System.currentTimeMillis());
						hits++;
					}
				}
			}
			if (hits > 0)
				Server.getUUIDCache().saveData();
		}

		public static String getUUID(String name, boolean formatted, boolean recursive) {
			name = name.toLowerCase();
			try {
				if (Server.getUUIDCache().getData().contains(name)) {
					String uuid = Server.getUUIDCache().getData().getString(name + ".id");
					long timestamp = Server.getUUIDCache().getData().getLong(name + ".ts");
					if (!$.isOld(timestamp, TimeUnit.HOURS.toMillis(48))) {
						if (!uuid.equals("null")) {
							applyUUIDCache(name, uuid, recursive);
							if (formatted) {
								StringBuffer sb = new StringBuffer(uuid);
								sb.insert(8, "-");
								sb = new StringBuffer(sb.toString());
								sb.insert(13, "-");
								sb = new StringBuffer(sb.toString());
								sb.insert(18, "-");
								sb = new StringBuffer(sb.toString());
								sb.insert(23, "-");
								uuid = sb.toString();
							}
							return uuid;
						} else
							return null;
					}
				}
				String response = readURL("https://api.mojang.com/users/profiles/minecraft/" + name);
				if (response == null) {
					applyUUIDCache(name, null, false);
					return response;
				}
				response = response.substring(response.indexOf("\"id\":\"") + "\"id\":\"".length());
				response = response.substring(0, response.indexOf("\""));
				applyUUIDCache(name, response, recursive);
				if (formatted) {
					StringBuffer sb = new StringBuffer(response);
					sb.insert(8, "-");
					sb = new StringBuffer(sb.toString());
					sb.insert(13, "-");
					sb = new StringBuffer(sb.toString());
					sb.insert(18, "-");
					sb = new StringBuffer(sb.toString());
					sb.insert(23, "-");
					response = sb.toString();
				}
				return response;
			} catch (Exception e) {
				Bukkit.getServer().getLogger().warning("Failed to retrieve online-mode uuid for player \"" + name + "\".");
				applyUUIDCache(name, null, false);
				return null;
			}
		}

		public static String getUUID(String name, boolean formatted) {
			return getUUID(name, formatted, false);
		}
	}

	public static class World extends CraftGo {
		public static Entity[] getNearbyEntities(Location location, int range) {
			List<Entity> found = new ArrayList<Entity>();
			for (Entity entity : location.getWorld().getEntities()) {
				if (isNearby(location, entity.getLocation(), range)) {
					found.add(entity);
				}
			}
			return found.toArray(new Entity[0]);
		}

		private static boolean isNearby(Location center, Location notCenter, int range) {
			int x = center.getBlockX(), z = center.getBlockZ();
			int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();
			if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
				return false;
			}
			return true;
		}
	}
}
