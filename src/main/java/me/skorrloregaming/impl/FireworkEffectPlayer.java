/*
 * Decompiled with CFR 0_129.
 */
package me.skorrloregaming.impl;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkEffectPlayer {
	private FireworkEffect effect = FireworkEffect.builder().with(FireworkEffect.Type.BURST).withColor(Color.WHITE).build();

	public void playFirework(World world, Location loc, FireworkEffect fe) throws Exception {
		Firework fw = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
		FireworkMeta fwm = fw.getFireworkMeta();
		fwm.clearEffects();
		fwm.setPower(1);
		fwm.addEffect(fe);
		fw.setFireworkMeta(fwm);
	}

	public FireworkEffect getEffect() {
		return this.effect;
	}
}

