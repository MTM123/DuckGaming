package me.skorrloregaming.skins;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UniversalSkinFactory extends SkinFactory {
	private Class<?> PlayOutRespawn;
	private Class<?> EntityHuman;
	private Class<?> PlayOutNamedEntitySpawn;
	private Class<?> PlayOutEntityDestroy;
	private Class<?> PlayOutPlayerInfo;
	private Class<?> PlayOutPosition;
	private Class<?> PlayOutEntityEquipment;
	private Class<?> ItemStack;
	private Class<?> Packet;
	private Class<?> CraftItemStack;
	private Class<?> PlayOutHeldItemSlot;
	private Class<?> EnumPlayerInfoAction;
	private Enum<?> PEACEFUL;
	private Enum<?> REMOVE_PLAYER;
	private Enum<?> ADD_PLAYER;
	private Enum<?> MAINHAND;
	private Enum<?> OFFHAND;
	private Enum<?> HEAD;
	private Enum<?> FEET;
	private Enum<?> LEGS;
	private Enum<?> CHEST;

	public UniversalSkinFactory() {
		try {
			Packet = me.skorrloregaming.Reflection.getNMSClass("Packet");
			PlayOutHeldItemSlot = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutHeldItemSlot");
			CraftItemStack = me.skorrloregaming.Reflection.getBukkitClass("inventory.CraftItemStack");
			ItemStack = me.skorrloregaming.Reflection.getNMSClass("ItemStack");
			PlayOutEntityEquipment = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutEntityEquipment");
			PlayOutPosition = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutPosition");
			EntityHuman = me.skorrloregaming.Reflection.getNMSClass("EntityHuman");
			PlayOutNamedEntitySpawn = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutNamedEntitySpawn");
			PlayOutEntityDestroy = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutEntityDestroy");
			PlayOutPlayerInfo = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutPlayerInfo");
			PlayOutRespawn = me.skorrloregaming.Reflection.getNMSClass("PacketPlayOutRespawn");
			try {
				EnumPlayerInfoAction = me.skorrloregaming.Reflection.getNMSClass("EnumPlayerInfoAction");
			} catch (Exception e) {
			}
			PEACEFUL = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumDifficulty"), "PEACEFUL");
			try {
				REMOVE_PLAYER = me.skorrloregaming.Reflection.getEnum(PlayOutPlayerInfo, "EnumPlayerInfoAction", "REMOVE_PLAYER");
				ADD_PLAYER = me.skorrloregaming.Reflection.getEnum(PlayOutPlayerInfo, "EnumPlayerInfoAction", "ADD_PLAYER");
			} catch (Exception e) {
				REMOVE_PLAYER = me.skorrloregaming.Reflection.getEnum(EnumPlayerInfoAction, "REMOVE_PLAYER");
				ADD_PLAYER = me.skorrloregaming.Reflection.getEnum(EnumPlayerInfoAction, "ADD_PLAYER");
			}
			MAINHAND = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumItemSlot"), "MAINHAND");
			OFFHAND = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumItemSlot"), "OFFHAND");
			HEAD = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumItemSlot"), "HEAD");
			CHEST = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumItemSlot"), "CHEST");
			FEET = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumItemSlot"), "FEET");
			LEGS = me.skorrloregaming.Reflection.getEnum(me.skorrloregaming.Reflection.getNMSClass("EnumItemSlot"), "LEGS");
		} catch (Exception e) {
		}
	}

	private void sendPacket(Object playerConnection, Object packet) throws Exception {
		me.skorrloregaming.Reflection.invokeMethod(playerConnection.getClass(), playerConnection, "sendPacket", new Class<?>[] { Packet }, new Object[] { packet });
	}

	@SuppressWarnings("deprecation")
	public void updateSkin(Player player) {
		if (!player.isOnline())
			return;
		try {
			Object ep = me.skorrloregaming.Reflection.invokeMethod(player, "getHandle");
			Location l = player.getLocation();
			List<Object> set = new ArrayList<>();
			set.add(ep);
			Iterable<?> iterable = set;
			Object removeInfo = null;
			Object removeEntity = null;
			Object addNamed = null;
			Object addInfo = null;
			removeInfo = me.skorrloregaming.Reflection.invokeConstructor(PlayOutPlayerInfo, new Class<?>[] { REMOVE_PLAYER.getClass(), Iterable.class }, REMOVE_PLAYER, iterable);
			removeEntity = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityDestroy, new Class<?>[] { int[].class }, new int[] { player.getEntityId() });
			addNamed = me.skorrloregaming.Reflection.invokeConstructor(PlayOutNamedEntitySpawn, new Class<?>[] { EntityHuman }, ep);
			addInfo = me.skorrloregaming.Reflection.invokeConstructor(PlayOutPlayerInfo, new Class<?>[] { ADD_PLAYER.getClass(), Iterable.class }, ADD_PLAYER, iterable);
			Object world = me.skorrloregaming.Reflection.invokeMethod(ep, "getWorld");
			Object difficulty = me.skorrloregaming.Reflection.invokeMethod(world, "getDifficulty");
			Object worlddata = me.skorrloregaming.Reflection.getObject(world, "worldData");
			Object worldtype = me.skorrloregaming.Reflection.invokeMethod(worlddata, "getType");
			Object dimensionManager = me.skorrloregaming.Reflection.getObject(world, "dimension");
			Object playerIntManager = me.skorrloregaming.Reflection.getObject(ep, "playerInteractManager");
			Enum<?> enumGamemode = (Enum<?>) me.skorrloregaming.Reflection.invokeMethod(playerIntManager, "getGameMode");
			int gmid = (int) me.skorrloregaming.Reflection.invokeMethod(enumGamemode, "getId");
			Object respawn = me.skorrloregaming.Reflection.invokeConstructor(PlayOutRespawn, new Class<?>[] {CraftGo.Dimension.getDimensionManager(), PEACEFUL.getClass(), worldtype.getClass(), enumGamemode.getClass() }, dimensionManager, difficulty, worldtype, me.skorrloregaming.Reflection.invokeMethod(enumGamemode.getClass(), null, "getById", new Class<?>[] { int.class }, new Object[] { gmid }));
			Object pos = null;
			try {
				pos = me.skorrloregaming.Reflection.invokeConstructor(PlayOutPosition, new Class<?>[] { double.class, double.class, double.class, float.class, float.class, Set.class, int.class }, l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), new HashSet<Enum<?>>(), 0);
			} catch (Exception e) {
				pos = me.skorrloregaming.Reflection.invokeConstructor(PlayOutPosition, new Class<?>[] { double.class, double.class, double.class, float.class, float.class, Set.class }, l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), new HashSet<Enum<?>>());
			}
			Object hand = null;
			Object mainhand = null;
			Object offhand = null;
			Object helmet = null;
			Object boots = null;
			Object chestplate = null;
			Object leggings = null;
			if (MAINHAND == null) {
				hand = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, int.class, ItemStack }, player.getEntityId(), 0, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getItemInHand() }));
				helmet = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, int.class, ItemStack }, player.getEntityId(), 4, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getHelmet() }));
				chestplate = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, int.class, ItemStack }, player.getEntityId(), 3, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getChestplate() }));
				leggings = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, int.class, ItemStack }, player.getEntityId(), 2, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getLeggings() }));
				boots = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, int.class, ItemStack }, player.getEntityId(), 1, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getBoots() }));
			} else {
				mainhand = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, MAINHAND.getClass(), ItemStack }, player.getEntityId(), MAINHAND, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getItemInMainHand() }));
				offhand = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, OFFHAND.getClass(), ItemStack }, player.getEntityId(), OFFHAND, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getItemInOffHand() }));
				helmet = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, HEAD.getClass(), ItemStack }, player.getEntityId(), HEAD, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getHelmet() }));
				chestplate = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, CHEST.getClass(), ItemStack }, player.getEntityId(), CHEST, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getChestplate() }));
				leggings = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, LEGS.getClass(), ItemStack }, player.getEntityId(), LEGS, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getLeggings() }));
				boots = me.skorrloregaming.Reflection.invokeConstructor(PlayOutEntityEquipment, new Class<?>[] { int.class, FEET.getClass(), ItemStack }, player.getEntityId(), FEET, me.skorrloregaming.Reflection.invokeMethod(CraftItemStack, null, "asNMSCopy", new Class<?>[] { ItemStack.class }, new Object[] { player.getInventory().getBoots() }));
			}
			Object slot = me.skorrloregaming.Reflection.invokeConstructor(PlayOutHeldItemSlot, new Class<?>[] { int.class }, player.getInventory().getHeldItemSlot());
			for (Player pOnline : Bukkit.getOnlinePlayers()) {
				final Object craftHandle = me.skorrloregaming.Reflection.invokeMethod(pOnline, "getHandle");
				Object playerCon = me.skorrloregaming.Reflection.getObject(craftHandle, "playerConnection");
				if (pOnline.equals(player)) {
					sendPacket(playerCon, removeInfo);
					sendPacket(playerCon, addInfo);
					sendPacket(playerCon, respawn);
					Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
						@Override
						public void run() {
							try {
								me.skorrloregaming.Reflection.invokeMethod(craftHandle, "updateAbilities");
							} catch (Exception e) {
							}
						}
					});
					sendPacket(playerCon, pos);
					sendPacket(playerCon, slot);
					me.skorrloregaming.Reflection.invokeMethod(pOnline, "updateScaledHealth");
					me.skorrloregaming.Reflection.invokeMethod(pOnline, "updateInventory");
					me.skorrloregaming.Reflection.invokeMethod(craftHandle, "triggerHealthUpdate");
					if (pOnline.isOp()) {
						pOnline.setOp(false);
						pOnline.setOp(true);
					}
					continue;
				}
				if (pOnline.getWorld().equals(player.getWorld()) && pOnline.canSee(player) && player.isOnline()) {
					sendPacket(playerCon, removeEntity);
					sendPacket(playerCon, removeInfo);
					sendPacket(playerCon, addInfo);
					sendPacket(playerCon, addNamed);
					if (MAINHAND != null) {
						sendPacket(playerCon, mainhand);
						sendPacket(playerCon, offhand);
					} else
						sendPacket(playerCon, hand);
					sendPacket(playerCon, helmet);
					sendPacket(playerCon, chestplate);
					sendPacket(playerCon, leggings);
					sendPacket(playerCon, boots);
				} else {
					sendPacket(playerCon, removeInfo);
					sendPacket(playerCon, addInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
