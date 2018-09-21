package me.skorrloregaming.lockette;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.PluginManager;

import me.skorrloregaming.$;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;

public class LockettePrefixListener implements Listener {
	public LockettePrefixListener(Lockette instance) {
	}

	protected void registerEvents() {
		PluginManager pm = Server.getPlugin().getServer().getPluginManager();
		pm.registerEvents(this, Server.getPlugin());
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		Block block = event.getBlock();
		if (!$.validLocketteMinigames.contains($.getMinigameFromWorld(block.getWorld()).toString().toLowerCase()))
			return;
		if (Lockette.DEBUG) {
			Logger.debug("[" + Lockette.pluginName + "] onSignChange (LockettePrefixListenre)");
		}
		Player player = event.getPlayer();
		Material blockType = block.getType();
		boolean typeWallSign = (blockType == Material.WALL_SIGN);
		boolean typeSignPost = (blockType == Material.SIGN);
		if (typeWallSign) {
			Sign sign = (Sign) block.getState();
			String text = ChatColor.stripColor(sign.getLine(0));
			if ((text.equalsIgnoreCase("[Private]") || text.equalsIgnoreCase(Lockette.altPrivate) || text.equalsIgnoreCase("[More Users]") || text.equalsIgnoreCase(Lockette.altMoreUsers)) && LocketteBlockListener.isEmptyChange(event)) {
				if (Lockette.DEBUG) {
					Logger.debug("[" + Lockette.pluginName + "] Sign already exists, resetting");
				}
				event.setCancelled(true);
				event.setLine(0, sign.getLine(0));
				event.setLine(1, sign.getLine(1));
				event.setLine(2, sign.getLine(2));
				event.setLine(3, sign.getLine(3));
				Logger.debug("[" + Lockette.pluginName + "] " + player.getName() + " just tried to change a non-editable sign. (Bukkit bug, or plugin conflict?)");
				return;
			}
		} else if (typeSignPost) {
		} else {
			event.setCancelled(true);
			Logger.debug("[" + Lockette.pluginName + "] " + player.getName() + " just tried to set text for a non-sign. (Bukkit bug, or hacked client?)");
			return;
		}
		if (Lockette.colorTags) {
			event.setLine(0, ChatColor.translateAlternateColorCodes('&', event.getLine(0)));
			event.setLine(1, ChatColor.translateAlternateColorCodes('&', event.getLine(1)));
			event.setLine(2, ChatColor.translateAlternateColorCodes('&', event.getLine(2)));
			event.setLine(3, ChatColor.translateAlternateColorCodes('&', event.getLine(3)));
		}
		if (Lockette.DEBUG) {
			Logger.debug(" line 0 : " + event.getLine(0));
			Logger.debug(" line 1 : " + event.getLine(1));
			Logger.debug(" line 2 : " + event.getLine(2));
			Logger.debug(" line 3 : " + event.getLine(3));
		}
	}
}
