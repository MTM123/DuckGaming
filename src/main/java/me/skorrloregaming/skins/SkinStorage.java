package me.skorrloregaming.skins;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.concurrent.TimeUnit;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Reflection;
import me.skorrloregaming.Server;

public class SkinStorage {
	private SkinFactory factory;
	public final long TIME_EXPIRE_MILLISECOND;
	public final boolean ENABLE_ONJOIN_MESSAGE = false;

	public SkinStorage() {
		setFactory(new UniversalSkinFactory());
		File tempFolder = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator);
		tempFolder.mkdirs();
		this.TIME_EXPIRE_MILLISECOND = TimeUnit.MINUTES.toMillis(1);
	}

	public Object createProperty(String name, String value, String signature) {
		Class<?> property = null;
		try {
			property = Class.forName("com.mojang.authlib.properties.Property");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			return Reflection.invokeConstructor(property, new Class<?>[] { String.class, String.class, String.class }, name, value, signature);
		} catch (Exception e) {
		}
		return null;
	}

	public Object getOrCreateSkinForPlayer(final String name) {
		String skin = name.toLowerCase();
		Object textures = null;
		textures = getSkinData(skin, false);
		if (textures != null) {
			return textures;
		}
		final String sname = skin;
		final Object oldprops = textures;
		try {
			Object props = null;
			props = CraftGo.Player.getSkinProperty(CraftGo.Player.getUUID(sname, false));
			if (props == null)
				return props;
			boolean shouldUpdate = false;
			String value = Base64Coder.decodeString((String) Reflection.invokeMethod(props, "getValue"));
			String newurl = value.substring(value.indexOf("\"url\":\"") + "\"url\":\"".length());
			newurl = newurl.substring(0, newurl.indexOf("\""));
			try {
				value = Base64Coder.decodeString((String) Reflection.invokeMethod(oldprops, "getValue"));
				String oldurl = value.substring(value.indexOf("\"url\":\"") + "\"url\":\"".length());
				oldurl = oldurl.substring(0, oldurl.indexOf("\""));
				shouldUpdate = !oldurl.equals(newurl);
			} catch (Exception e) {
				shouldUpdate = true;
			}
			setSkinData(sname, props);
			if (shouldUpdate)
				factory.applySkin(org.bukkit.Bukkit.getPlayer(name), props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return textures;
	}

	public Object getSkinData(String name, boolean noUpdate) {
		name = name.toLowerCase();
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		try {
			if (!skinFile.exists())
				return null;
			BufferedReader buf = new BufferedReader(new FileReader(skinFile));
			String line, value = "", signature = "", timestamp = "";
			for (int i = 0; i < 3; i++)
				if ((line = buf.readLine()) != null)
					if (value.isEmpty()) {
						value = line;
					} else if (signature.isEmpty()) {
						signature = line;
					} else {
						timestamp = line;
					}
			buf.close();
			if (!noUpdate && $.isOld(Long.valueOf(timestamp), TIME_EXPIRE_MILLISECOND)) {
				Object skin = CraftGo.Player.getSkinProperty(CraftGo.Player.getUUID(name, false));
				if (skin != null) {
					setSkinData(name, skin);
				}
			}
			return createProperty("textures", value, signature);
		} catch (Exception e) {
			e.printStackTrace();
			removeSkinData(name);
		}
		return null;
	}

	public long getSkinTimestamp(String name) {
		name = name.toLowerCase();
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		try {
			if (!skinFile.exists())
				return System.currentTimeMillis();
			BufferedReader buf = new BufferedReader(new FileReader(skinFile));
			String line, value = "", signature = "", timestamp = "";
			for (int i = 0; i < 3; i++)
				if ((line = buf.readLine()) != null)
					if (value.isEmpty()) {
						value = line;
					} else if (signature.isEmpty()) {
						signature = line;
					} else {
						timestamp = line;
					}
			buf.close();
			return Long.valueOf(timestamp).longValue();
		} catch (Exception e) {
			e.printStackTrace();
			removeSkinData(name);
		}
		return System.currentTimeMillis();
	}

	public void setSkinData(String name, Object textures) {
		name = name.toLowerCase();
		String value = "";
		String signature = "";
		String timestamp = "";
		try {
			value = (String) Reflection.invokeMethod(textures, "getValue");
			signature = (String) Reflection.invokeMethod(textures, "getSignature");
			timestamp = String.valueOf(System.currentTimeMillis());
		} catch (Exception e) {
		}
		File skinFile = new File(Server.getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
		try {
			if (value.isEmpty() || signature.isEmpty() || timestamp.isEmpty())
				return;
			if (!skinFile.exists())
				skinFile.createNewFile();
			FileWriter writer = new FileWriter(skinFile);
			writer.write(value + "\n" + signature + "\n" + timestamp);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public SkinFactory getFactory() {
		return factory;
	}

	public void setFactory(SkinFactory factory) {
		this.factory = factory;
	}
}
