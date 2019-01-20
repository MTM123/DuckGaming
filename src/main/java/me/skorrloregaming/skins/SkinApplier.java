package me.skorrloregaming.skins;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.comphenix.protocol.utility.MinecraftVersion;
import com.comphenix.protocol.wrappers.*;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.skins.model.SkinModel;
import me.skorrloregaming.skins.model.SkinProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import protocolsupport.protocol.utils.authlib.UUIDTypeAdapter;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO;
import static com.comphenix.protocol.PacketType.Play.Server.POSITION;
import static com.comphenix.protocol.PacketType.Play.Server.RESPAWN;

public class SkinApplier implements Runnable {

	public final CommandSender invoker;
	public final Player receiver;
	public final SkinModel targetSkin;
	public final boolean keepSkin;

	public SkinApplier(CommandSender invoker, Player receiver, SkinModel targetSkin, boolean keepSkin) {
		this.invoker = invoker;
		this.receiver = receiver;
		this.targetSkin = targetSkin;
		this.keepSkin = keepSkin;
	}

	@Override
	public void run() {
		if (!isConnected()) {
			return;
		}
		applySkin();
	}

	public boolean isConnected() {
		return receiver != null && receiver.isOnline();
	}

	public void applySkin() {
		applyInstantUpdate();
	}

	public void applyInstantUpdate() {
		if (!CraftGo.Player.isPocketPlayer(receiver)) {
			WrappedGameProfile gameProfile = WrappedGameProfile.fromPlayer(receiver);
			applyProperties(gameProfile, targetSkin);
			Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
				@Override
				public void run() {
					sendUpdateSelf(WrappedGameProfile.fromPlayer(receiver));
					sendUpdateOthers();
				}
			});
		}
	}

	public WrappedSignedProperty convertToProperty(SkinModel skinData) {
		String encodedValue = skinData.getEncodedValue();
		String signature = skinData.getSignature();
		return WrappedSignedProperty.fromValues(SkinProperty.SKIN_KEY, encodedValue, signature);
	}

	public void applyProperties(WrappedGameProfile profile, SkinModel targetSkin) {
		profile.getProperties().clear();
		if (targetSkin != null) {
			profile.getProperties().put(SkinProperty.SKIN_KEY, convertToProperty(targetSkin));
		}
	}


	public void sendMessage(String key) {
		invoker.sendMessage(key);
	}

	public void runAsync(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(Server.getPlugin(), runnable);
	}

	public void sendUpdateOthers() throws FieldAccessException {
		Bukkit.getOnlinePlayers().stream()
				.filter(onlinePlayer -> onlinePlayer.canSee(receiver))
				.forEach(this::hideAndShow);
	}

	public void sendUpdateSelf(WrappedGameProfile gameProfile) throws FieldAccessException {
		Optional.ofNullable(receiver.getVehicle()).ifPresent(Entity::eject);
		sendPacketsSelf(gameProfile);
		receiver.setExp(receiver.getExp());
		receiver.setWalkSpeed(receiver.getWalkSpeed());
		receiver.updateInventory();
		PlayerInventory inventory = receiver.getInventory();
		inventory.setHeldItemSlot(inventory.getHeldItemSlot());
		try {
			receiver.getClass().getDeclaredMethod("updateScaledHealth").invoke(receiver);
		} catch (ReflectiveOperationException reflectiveEx) {
			reflectiveEx.printStackTrace();
		}
	}

	public void sendPacketsSelf(WrappedGameProfile gameProfile) {
		PacketContainer removeInfo;
		PacketContainer addInfo;
		PacketContainer respawn;
		PacketContainer teleport;
		try {
			EnumWrappers.NativeGameMode gamemode = EnumWrappers.NativeGameMode.fromBukkit(receiver.getGameMode());
			WrappedChatComponent displayName = WrappedChatComponent.fromText(receiver.getPlayerListName());
			PlayerInfoData playerInfoData = new PlayerInfoData(gameProfile, 0, gamemode, displayName);
			removeInfo = new PacketContainer(PLAYER_INFO);
			removeInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
			removeInfo.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfoData));
			addInfo = removeInfo.deepClone();
			addInfo.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
			respawn = createRespawnPacket(gamemode);
			teleport = createTeleportPacket(receiver.getLocation().clone());
		} catch (ReflectiveOperationException reflectiveEx) {
			reflectiveEx.printStackTrace();
			return;
		}

		sendPackets(removeInfo, addInfo, respawn, teleport);
	}

	public void hideAndShow(Player other) {
		other.hidePlayer(Server.getPlugin(), receiver);
		other.showPlayer(Server.getPlugin(), receiver);
	}

	public void sendPackets(PacketContainer... packets) {
		try {
			ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
			for (PacketContainer packet : packets) {
				protocolManager.sendServerPacket(receiver, packet);
			}
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		}
	}

	public PacketContainer createRespawnPacket(EnumWrappers.NativeGameMode gamemode) throws ReflectiveOperationException {
		PacketContainer respawn = new PacketContainer(RESPAWN);

		EnumWrappers.Difficulty difficulty = EnumWrappers.getDifficultyConverter().getSpecific(receiver.getWorld().getDifficulty());

		//<= 1.13.1
		int dimensionId = receiver.getWorld().getEnvironment().getId();
		respawn.getIntegers().writeSafely(0, dimensionId);

		//> 1.13.1
		if (MinecraftVersion.getCurrentVersion().compareTo(MinecraftVersion.AQUATIC_UPDATE) > 0) {
			try {
				respawn.getDimensions().writeSafely(0, dimensionId);
			} catch (NoSuchMethodError noSuchMethodError) {
				noSuchMethodError.printStackTrace();
			}
		}

		respawn.getDifficulties().write(0, difficulty);
		respawn.getGameModes().write(0, gamemode);
		respawn.getWorldTypeModifier().write(0, receiver.getWorld().getWorldType());
		return respawn;
	}

	public PacketContainer createTeleportPacket(Location location) {
		PacketContainer teleport = new PacketContainer(POSITION);
		teleport.getModifier().writeDefaults();
		teleport.getDoubles().write(0, location.getX());
		teleport.getDoubles().write(1, location.getY());
		teleport.getDoubles().write(2, location.getZ());
		teleport.getFloat().write(0, location.getYaw());
		teleport.getFloat().write(1, location.getPitch());
		teleport.getIntegers().writeSafely(0, -1337);
		return teleport;
	}
}