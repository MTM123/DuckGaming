package me.skorrloregaming.commands;

import me.skorrloregaming.$;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import me.skorrloregaming.skins.model.SkinModel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

import me.skorrloregaming.*;

public class UpdateSkinCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player player = ((Player) sender);
		if (Bukkit.getOnlineMode() || ServerGet.get().getSkinStorage() == null) {
			player.sendMessage("This commmand is currently not applicable to you.");
			return true;
		}
		if (ServerGet.get().getDelaySkinUpdate().containsKey(player.getUniqueId())) {
			player.sendMessage("You must wait " + Link$.formatTime(ServerGet.get().getDelaySkinUpdate().get(player.getUniqueId())) + " before you can update your skin again.");
			return true;
		}
		final Player fPlayer = player;
		if (!ServerGet.get().getDelaySkinUpdate().containsKey(fPlayer.getUniqueId())) {
			ServerGet.get().getDelaySkinUpdate().put(fPlayer.getUniqueId(), 60 * 5);
		}
		ServerGet.get().getBukkitTasks().add(new BukkitRunnable() {
			int time = 60 * 5;

			public void run() {
				time--;
				ServerGet.get().getDelaySkinUpdate().put(fPlayer.getUniqueId(), time);
				if (time <= 0) {
					if (ServerGet.get().getDelaySkinUpdate().containsKey(fPlayer.getUniqueId())) {
						ServerGet.get().getDelaySkinUpdate().remove(fPlayer.getUniqueId());
					}
					cancel();
				}
			}
		}.runTaskTimer(ServerGet.get().getPlugin(), 20L, 20L));
		player.sendMessage("Attempting to fetch the latest skin available..");
		Bukkit.getScheduler().runTaskAsynchronously(ServerGet.get().getPlugin(), new Runnable() {
			@Override
			public void run() {
				Optional<SkinModel> model = ServerGet.get().getSkinStorage().forceSkinUpdate(player);
				if (!model.isPresent()) {
					player.sendMessage("Failed to fetch your latest skin from mojang " + "\u2639" + ".");
					return;
				}
				ServerGet.get().getSkinStorage().getFactory(player, model.get()).applySkin();
				player.sendMessage("Skin updated. You are now using the latest skin available.");
			}
		});
		return true;
	}

}
