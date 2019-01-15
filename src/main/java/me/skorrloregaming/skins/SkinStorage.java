package me.skorrloregaming.skins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import me.skorrloregaming.*;
import me.skorrloregaming.skins.model.SkinModel;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class SkinStorage {
	public final long TIME_EXPIRE_MILLISECOND;
	public final boolean ENABLE_ONJOIN_MESSAGE = false;

	public SkinStorage() {
		File tempFolder = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator);
		tempFolder.mkdirs();
		this.TIME_EXPIRE_MILLISECOND = TimeUnit.MINUTES.toMillis(1);
	}

	public Optional<SkinModel> getSkinData(Player player, boolean noUpdate) {
		String name = player.getName().toLowerCase();
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		try {
			long timestamp = System.currentTimeMillis();
			Optional<SkinModel> model = Optional.empty();
			String signature = null;
			String encoded = null;
			if (skinFile.exists()) {
				ConfigurationManager skinFileData = new ConfigurationManager();
				skinFileData.setup(skinFile);
				timestamp = skinFileData.getData().getLong("timestamp", System.currentTimeMillis());
				signature = skinFileData.getData().getString("signature");
				encoded = skinFileData.getData().getString("encoded");
				model = Optional.of(SkinModel.createSkinFromEncoded(signature, encoded));
			}
			if (!noUpdate && (!skinFile.exists() || $.isOld(Long.valueOf(timestamp), TIME_EXPIRE_MILLISECOND))) {
				String uid = CraftGo.Player.getUUID(name, false);
				Optional<SkinModel> skin = CraftGo.Player.getSkinProperty(uid);
				if (skin.isPresent()) {
					setSkinData(player, skin.get());
					model = skin;
				}
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			removeSkinData(name);
		}
		return Optional.empty();
	}

	public Optional<SkinModel> forceSkinUpdate(Player player) {
		String name = player.getName().toLowerCase();
		try {
			String uid = CraftGo.Player.getUUID(name, false);
			Optional<SkinModel> model = CraftGo.Player.getSkinProperty(uid);
			if (model.isPresent())
				setSkinData(player, model.get());
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			removeSkinData(name);
		}
		return Optional.empty();
	}

	public long getSkinTimestamp(Player player) {
		String name = player.getName().toLowerCase();
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		try {
			if (!skinFile.exists())
				return System.currentTimeMillis();
			ConfigurationManager skinFileData = new ConfigurationManager();
			skinFileData.setup(skinFile);
			return skinFileData.getData().getLong("timestamp", System.currentTimeMillis());
		} catch (Exception e) {
			e.printStackTrace();
			removeSkinData(name);
		}
		return System.currentTimeMillis();
	}

	public void setSkinData(Player player, SkinModel textures) {
		String name = player.getName().toLowerCase();
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		ConfigurationManager skinFileData = new ConfigurationManager();
		skinFileData.setup(skinFile);
		skinFileData.getData().set("timestamp", System.currentTimeMillis());
		skinFileData.getData().set("encoded", textures.getSignature());
		skinFileData.getData().set("signature", textures.getEncodedValue());
		skinFileData.saveData();
	}

	public void removePlayerSkin(String name) {
		name = name.toLowerCase();
		File playerFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Players" + File.separator + name + ".player");
		if (playerFile.exists())
			playerFile.delete();
	}

	public static void removeSkinData(String name) {
		name = name.toLowerCase();
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		if (skinFile.exists())
			skinFile.delete();
	}

	public SkinApplier getFactory(Player player, SkinModel model) {
		return new SkinApplier(player, player, model, true);
	}
}
