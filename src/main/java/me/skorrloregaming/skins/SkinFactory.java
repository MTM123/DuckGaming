package me.skorrloregaming.skins;

import org.bukkit.entity.Player;

import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Reflection;

public abstract class SkinFactory {
	public void applySkin(final Player p, Object props) {
		if (CraftGo.Player.isPocketPlayer(p))
			return;
		try {
			if (props == null)
				return;
			Object ep = Reflection.invokeMethod(p.getClass(), p, "getHandle");
			Object profile = Reflection.invokeMethod(ep.getClass(), ep, "getProfile");
			Object propmap = Reflection.invokeMethod(profile.getClass(), profile, "getProperties");
			Reflection.invokeMethod(propmap, "clear");
			Reflection.invokeMethod(propmap.getClass(), propmap, "put", new Class[] { Object.class, Object.class }, new Object[] { "textures", props });
			updateSkin(p);
		} catch (Exception e) {
		}
	}

	public abstract void updateSkin(Player p);
}
