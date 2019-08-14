package me.skorrloregaming.skins;

import me.skorrloregaming.ConfigurationManager;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.skins.model.SkinModel;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class SkinStorage {
    public final long TIME_EXPIRE_MILLISECOND;
    public final boolean ENABLE_ONJOIN_MESSAGE = false;

    public SkinStorage() {
        File tempFolder = new File(Server.getInstance().getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator);
        tempFolder.mkdirs();
        this.TIME_EXPIRE_MILLISECOND = TimeUnit.MINUTES.toMillis(1);
    }

    public static Optional<SkinModel> getSkinProperty(String uuid) {
        if (uuid == null || uuid.equals("null"))
            return Optional.empty();
        if (!Server.getInstance().getPlugin().getConfig().getBoolean("settings.bungeecord", false)) {
            String skinurl = "https://sessionserver.mojang.com/session/minecraft/profile/";
            String output;
            try {
                output = CraftGo.Player.readURL(skinurl + uuid + "?unsigned=false");
                String signature = output.substring(output.indexOf("\"signature\":\"") + "\"signature\":\"".length());
                signature = signature.substring(0, signature.indexOf("\""));
                String value = output.substring(output.indexOf("\"value\":\"") + "\"value\":\"".length());
                value = value.substring(0, value.indexOf("\""));
                return Optional.of(SkinModel.createSkinFromEncoded(value, signature));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static void removeSkinData(String name) {
        name = name.toLowerCase();
        File skinFile = new File(Server.getInstance().getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
        if (skinFile.exists())
            skinFile.delete();
    }

    public Optional<SkinModel> getSkinData(Player player, boolean noUpdate) {
        String name = player.getName().toLowerCase();
        File skinFile = new File(Server.getInstance().getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
        try {
            long timestamp = System.currentTimeMillis();
            Optional<SkinModel> model = Optional.empty();
            String signature;
            String encoded;
            if (skinFile.exists()) {
                ConfigurationManager skinFileData = new ConfigurationManager();
                skinFileData.setup(skinFile);
                timestamp = skinFileData.getData().getLong("timestamp", System.currentTimeMillis());
                signature = skinFileData.getData().getString("signature");
                encoded = skinFileData.getData().getString("encoded");
                model = Optional.of(SkinModel.createSkinFromEncoded(signature, encoded));
            }
            if (!noUpdate && (!skinFile.exists() || Link$.isOld(timestamp, TIME_EXPIRE_MILLISECOND))) {
                String uid = CraftGo.Player.getUUID(name, false);
                Optional<SkinModel> skin = getSkinProperty(uid);
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
            Optional<SkinModel> model = getSkinProperty(uid);
            model.ifPresent(skinModel -> setSkinData(player, skinModel));
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            removeSkinData(name);
        }
        return Optional.empty();
    }

    public long getSkinTimestamp(Player player) {
        String name = player.getName().toLowerCase();
        File skinFile = new File(Server.getInstance().getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
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
        File skinFile = new File(Server.getInstance().getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Skins" + File.separator + name + ".skin");
        ConfigurationManager skinFileData = new ConfigurationManager();
        skinFileData.setup(skinFile);
        skinFileData.getData().set("timestamp", System.currentTimeMillis());
        skinFileData.getData().set("encoded", textures.getSignature());
        skinFileData.getData().set("signature", textures.getEncodedValue());
        skinFileData.saveData();
    }

    public void removePlayerSkin(String name) {
        name = name.toLowerCase();
        File playerFile = new File(Server.getInstance().getPlugin().getDataFolder().getAbsolutePath() + File.separator + "Players" + File.separator + name + ".player");
        if (playerFile.exists())
            playerFile.delete();
    }

    public SkinApplier getFactory(Player player, SkinModel model) {
        return new SkinApplier(player, player, model, true);
    }
}
