/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ParticleManager {
	public static Particles Create(EnumParticle type, Location location, int radius, int speed, int amount) {
		return new Particles(type, location, radius, speed, amount);
	}

	public static void Play(Particles particles, Player player) {
		if (player.getWorld().getName().equals(particles.location.getWorld().getName())) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket((Packet) particles.packet);
		}
	}

	public static class Particles
			extends ParticleManager {
		public EnumParticle type;
		public Location location;
		public int radius;
		public int speed;
		public int amount;
		public PacketPlayOutWorldParticles packet;

		public Particles(EnumParticle type, Location location, int radius, int speed, int amount) {
			this.type = type;
			this.location = location;
			this.radius = radius;
			this.speed = speed;
			this.amount = amount;
			this.packet = new PacketPlayOutWorldParticles(type, true, (float) location.getBlockX(), (float) location.getBlockY(), (float) location.getBlockZ(), (float) radius, (float) radius, (float) radius, (float) speed, amount, null);
		}
	}

}

