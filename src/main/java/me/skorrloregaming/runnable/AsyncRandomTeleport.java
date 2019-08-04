package me.skorrloregaming.runnable;

import com.destroystokyo.paper.HeightmapType;
import me.skorrloregaming.Link$;
import me.skorrloregaming.Server;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AsyncRandomTeleport implements Runnable {

	private Player player;

	public AsyncRandomTeleport(Player player) {
		this.player = player;
	}

	private double x;
	private double z;

	public void execute() {
		x = 0;
		while (x < 500 && x > -500) {
			x = -5000 + new Random(UUID.randomUUID().hashCode()).nextInt(10000) + 0.5;
		}
		z = 0;
		while (z < 500 && z > -500) {
			z = -5000 + new Random(UUID.randomUUID().hashCode()).nextInt(10000) + 0.5;
		}
		try {
			Method method = World.class.getMethod("getChunkAtAsync", int.class, int.class);
			CompletableFuture<Chunk> future = (CompletableFuture<Chunk>) method.invoke(player.getWorld(), (int) x, (int) z);
			future.thenRun(this);
		} catch (Exception ex) {
			ex.printStackTrace();
			player.getWorld().getChunkAt((int) x, (int) z).load();
			new Thread(this).run();
		}
	}

	@Override
	public void run() {
		Block block = player.getWorld().getHighestBlockAt((int) x, (int) z, HeightmapType.SOLID);
		if (block.getRelative(BlockFace.UP).getType() == Material.AIR || player.getWorld().getEnvironment() == World.Environment.NETHER) {
			Location teleportLocation = block.getLocation().clone().add(0, 1, 0);
			if (player.getWorld().getEnvironment() == World.Environment.NETHER)
				teleportLocation.setY(128);
			player.sendMessage(Link$.Legacy.tag + "Teleport destination: " + ChatColor.RED + teleportLocation.getX() + ChatColor.RESET + ", " + ChatColor.RED + teleportLocation.getY() + ChatColor.RESET + ", " + ChatColor.RED + teleportLocation.getZ() + ChatColor.RESET + ".");
			DelayedTeleport dt = new DelayedTeleport(player, Server.getTeleportationDelay() * 1.5, teleportLocation, false);
			Server.getBukkitTasks().add(dt.runTaskTimerAsynchronously(Server.getPlugin(), 4, 4));
		} else {
			player.sendMessage(Link$.Legacy.tag + "Unsafe teleport destination, trying again..");
			execute();
		}
	}

}
