package me.skorrloregaming.hooks;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import protocolsupport.api.MaterialAPI;
import protocolsupport.api.ProtocolSupportAPI;
import protocolsupport.api.ProtocolVersion;
import protocolsupport.api.events.PlayerLoginFinishEvent;
import protocolsupport.api.events.PlayerLoginStartEvent;
import protocolsupport.api.events.PlayerProfileCompleteEvent;
import protocolsupport.api.remapper.BlockRemapperControl;
import protocolsupport.protocol.utils.ProtocolVersionsHelper;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class ProtocolSupport_Listener implements Listener {

	public boolean register() {
		if (Bukkit.getServer().getOnlineMode())
			return false;
		Server.getPlugin().getServer().getPluginManager().registerEvents(this, Server.getPlugin());
		this.<Chest>registerRemapEntryForAllStates(Arrays.asList(Material.CHEST, Material.TRAPPED_CHEST), o -> {
			Chest chest = (Chest) createBlockData(o.getMaterial());
			chest.setWaterlogged(false);
			chest.setFacing(o.getFacing());
			return chest;
		}, ProtocolVersionsHelper.BEFORE_1_13);
		return true;
	}

	protected static BlockData createBlockData(Material material) {
		if (!material.isBlock()) {
			throw new IllegalArgumentException(material + " is not a block");
		}
		return material.createBlockData();
	}

	protected void registerRemapEntryForAllStates(List<Material> materials, BlockData to, ProtocolVersion... versions) {
		for (Material material : materials) {
			registerRemapEntryForAllStates(material, to, versions);
		}
	}

	protected void registerRemapEntryForAllStates(Material from, BlockData to, ProtocolVersion... versions) {
		MaterialAPI.getBlockDataList(from).forEach(blockdata -> registerRemapEntry(blockdata, to, versions));
	}

	protected <T extends BlockData> void registerRemapEntryForAllStates(List<Material> materials, Function<T, BlockData> remapFunc, ProtocolVersion... versions) {
		for (Material material : materials) {
			registerRemapEntryForAllStates(material, remapFunc, versions);
		}
	}

	@SuppressWarnings("unchecked")
	protected <T extends BlockData> void registerRemapEntryForAllStates(Material from, Function<T, BlockData> remapFunc, ProtocolVersion... versions) {
		MaterialAPI.getBlockDataList(from).forEach(blockdata -> registerRemapEntry(blockdata, remapFunc.apply((T) blockdata), versions));
	}

	protected void registerRemapEntry(BlockData from, BlockData to, ProtocolVersion... versions) {
		for (ProtocolVersion version : versions) {
			BlockRemapperControl remapper = new BlockRemapperControl(version);
			remapper.setRemap(from, to);
		}
	}

	public void disableProtocolVersions() {
		for (String version : Server.getDisabledVersions())
			ProtocolSupportAPI.disableProtocolVersion(ProtocolVersion.valueOf("MINECRAFT_" + version.replace(".", "_")));
	}

	@EventHandler
	public void onPlayerLoginStart(PlayerLoginStartEvent event) {
		String playerName = event.getConnection().getProfile().getName().toString();
		if (!Server.getPlugin().getConfig().getBoolean("settings.bungeecord", false)) {
			if (CraftGo.Player.getUUID(playerName, false) == null)
				return;
			event.setOnlineMode(true);
		}
	}

	@EventHandler
	public void onPlayerProfileComplete(PlayerProfileCompleteEvent event) {
		String playerName = event.getConnection().getProfile().getOriginalName();
		UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + event.getConnection().getProfile().getName()).getBytes());
		event.setForcedUUID(offlineUUID);
		String path = "sync." + event.getConnection().getProfile().getOriginalName();
		if (Server.getPlugin().getConfig().contains(path)) {
			if (Server.getPlugin().getConfig().getBoolean(path + ".est", false)) {
				String targetName = Server.getPlugin().getConfig().getString(path + ".name");
				String targetUUID = Server.getPlugin().getConfig().getString(path + ".uuid");
				event.setForcedName(targetName);
				event.setForcedUUID(UUID.fromString(targetUUID));
			}
		}
	}

	@EventHandler
	public void onPlayerLoginFinish(PlayerLoginFinishEvent event) {
		String playerName = event.getConnection().getProfile().getOriginalName();
		if (event.getConnection().getVersion().getProtocolType().toString().equals("PE"))
			return;
		UUID offlineUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + event.getConnection().getProfile().getName()).getBytes());
		UUID playerUUID = event.getConnection().getProfile().getOriginalUUID();
		if (!Server.getPlugin().getConfig().getBoolean("settings.bungeecord", false))
			if (playerUUID.toString().equals(offlineUUID.toString())) {
				event.denyLogin("You are not allowed to connect with a cracked account.");
				return;
			}
		String path = "sync." + playerName;
		if (Server.getPlugin().getConfig().contains(path)) {
			if (!Server.getPlugin().getConfig().getBoolean(path + ".est")) {
				if (Server.getPlugin().getConfig().contains(path + ".id")) {
					if (!Server.getPlugin().getConfig().getBoolean(path + ".suppress", false)) {
						String syncTarget = Server.getPlugin().getConfig().getString(path + ".name");
						Server.getPlugin().getConfig().set(path + ".suppress", true);
						event.denyLogin(syncTarget + " would like you to sync your account with his/her account. If you accept, your username will be changed to " + syncTarget + ". This means that you two will not be able to play at the same time. If you want to accept this sync request, have " + syncTarget + " type the following command: /sync " + playerName + " -f -c " + Server.getPlugin().getConfig().getInt(path + ".id") + ". To deny this request, you can simply rejoin the server and this notice will be dismissed.");
					}
				}
			}
		}
	}
}
