package me.skorrloregaming;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.UUID;

public class ComplexParticle {

    public static ComplexParticle EXPLOSION_NORMAL = ComplexParticle.get(Particle.EXPLOSION_NORMAL);
    public static ComplexParticle EXPLOSION_LARGE = ComplexParticle.get(Particle.EXPLOSION_LARGE);
    public static ComplexParticle EXPLOSION_HUGE = ComplexParticle.get(Particle.EXPLOSION_HUGE);
    public static ComplexParticle FIREWORKS_SPARK = ComplexParticle.get(Particle.FIREWORKS_SPARK);
    public static ComplexParticle WATER_BUBBLE = ComplexParticle.get(Particle.WATER_BUBBLE);
    public static ComplexParticle WATER_SPLASH = ComplexParticle.get(Particle.WATER_SPLASH);
    public static ComplexParticle WATER_WAKE = ComplexParticle.get(Particle.WATER_WAKE);
    public static ComplexParticle SUSPENDED = ComplexParticle.get(Particle.SUSPENDED);
    public static ComplexParticle SUSPENDED_DEPTH = ComplexParticle.get(Particle.SUSPENDED_DEPTH);
    public static ComplexParticle CRIT = ComplexParticle.get(Particle.CRIT);
    public static ComplexParticle CRIT_MAGIC = ComplexParticle.get(Particle.CRIT_MAGIC);
    public static ComplexParticle SMOKE_NORMAL = ComplexParticle.get(Particle.SMOKE_NORMAL);
    public static ComplexParticle SMOKE_LARGE = ComplexParticle.get(Particle.SMOKE_LARGE);
    public static ComplexParticle SPELL = ComplexParticle.get(Particle.SPELL);
    public static ComplexParticle SPELL_INSTANT = ComplexParticle.get(Particle.SPELL_INSTANT);
    public static ComplexParticle SPELL_MOB = ComplexParticle.get(Particle.SPELL_MOB);
    public static ComplexParticle SPELL_MOB_AMBIENT = ComplexParticle.get(Particle.SPELL_MOB_AMBIENT);
    public static ComplexParticle SPELL_WITCH = ComplexParticle.get(Particle.SPELL_WITCH);
    public static ComplexParticle DRIP_WATER = ComplexParticle.get(Particle.DRIP_WATER);
    public static ComplexParticle DRIP_LAVA = ComplexParticle.get(Particle.DRIP_LAVA);
    public static ComplexParticle VILLAGER_ANGRY = ComplexParticle.get(Particle.VILLAGER_ANGRY);
    public static ComplexParticle VILLAGER_HAPPY = ComplexParticle.get(Particle.VILLAGER_HAPPY);
    public static ComplexParticle TOWN_AURA = ComplexParticle.get(Particle.TOWN_AURA);
    public static ComplexParticle NOTE = ComplexParticle.get(Particle.NOTE);
    public static ComplexParticle PORTAL = ComplexParticle.get(Particle.PORTAL);
    public static ComplexParticle ENCHANTMENT_TABLE = ComplexParticle.get(Particle.ENCHANTMENT_TABLE);
    public static ComplexParticle FLAME = ComplexParticle.get(Particle.FLAME);
    public static ComplexParticle LAVA = ComplexParticle.get(Particle.LAVA);
    public static ComplexParticle CLOUD = ComplexParticle.get(Particle.CLOUD);
    public static ComplexParticle REDSTONE = ComplexParticle.get(Particle.REDSTONE);
    public static ComplexParticle SNOWBALL = ComplexParticle.get(Particle.SNOWBALL);
    public static ComplexParticle SNOW_SHOVEL = ComplexParticle.get(Particle.SNOW_SHOVEL);
    public static ComplexParticle SLIME = ComplexParticle.get(Particle.SLIME);
    public static ComplexParticle HEART = ComplexParticle.get(Particle.HEART);
    public static ComplexParticle BARRIER = ComplexParticle.get(Particle.BARRIER);
    public static ComplexParticle ITEM_CRACK = ComplexParticle.get(Particle.ITEM_CRACK);
    public static ComplexParticle BLOCK_CRACK = ComplexParticle.get(Particle.BLOCK_CRACK);
    public static ComplexParticle BLOCK_DUST = ComplexParticle.get(Particle.BLOCK_DUST);
    public static ComplexParticle WATER_DROP = ComplexParticle.get(Particle.WATER_DROP);
    public static ComplexParticle MOB_APPEARANCE = ComplexParticle.get(Particle.MOB_APPEARANCE);
    public static ComplexParticle DRAGON_BREATH = ComplexParticle.get(Particle.END_ROD);
    public static ComplexParticle END_ROD = ComplexParticle.get(Particle.EXPLOSION_NORMAL);
    public static ComplexParticle DAMAGE_INDICATOR = ComplexParticle.get(Particle.DAMAGE_INDICATOR);
    public static ComplexParticle SWEEP_ATTACK = ComplexParticle.get(Particle.SWEEP_ATTACK);
    public static ComplexParticle FALLING_DUST = ComplexParticle.get(Particle.FALLING_DUST);
    public static ComplexParticle TOTEM = ComplexParticle.get(Particle.TOTEM);
    public static ComplexParticle SPIT = ComplexParticle.get(Particle.SPIT);
    public static ComplexParticle SQUID_INK = ComplexParticle.get(Particle.SQUID_INK);
    public static ComplexParticle BUBBLE_POP = ComplexParticle.get(Particle.BUBBLE_POP);
    public static ComplexParticle CURRENT_DOWN = ComplexParticle.get(Particle.CURRENT_DOWN);
    public static ComplexParticle BUBBLE_COLUMN_UP = ComplexParticle.get(Particle.BUBBLE_COLUMN_UP);
    public static ComplexParticle NAUTILUS = ComplexParticle.get(Particle.NAUTILUS);
    public static ComplexParticle DOLPHIN = ComplexParticle.get(Particle.DOLPHIN);
    private final float extra = 0.001f;
    private Particle particleType;
    private int particleCount;
    private float radius = 4f;
    private Class<?> requiredDataClass;
    private Object particleData = null;

    public ComplexParticle(Particle particleType, int particleCount) {
        this.particleType = particleType;
        this.particleCount = particleCount;
        this.requiredDataClass = getRequiredDataClass();
    }

    public ComplexParticle(Particle particleType, int particleCount, float radius) {
        this.particleType = particleType;
        this.particleCount = particleCount;
        this.radius = radius;
        this.requiredDataClass = getRequiredDataClass();
    }

    public <T> ComplexParticle(Particle particleType, int particleCount, float radius, T particleData) {
        this.particleType = particleType;
        this.particleCount = particleCount;
        this.radius = radius;
        this.requiredDataClass = getRequiredDataClass();
        this.particleData = particleData;
    }

    public static ComplexParticle get(Particle particleType) {
        return new ComplexParticle(particleType, 0);
    }

    public ComplexParticle count(int particleCount) {
        return new ComplexParticle(particleType, particleCount, radius);
    }

    public ComplexParticle radius(float radius) {
        return new ComplexParticle(particleType, particleCount, radius);
    }

    public <T> ComplexParticle data(T particleData) {
        return new ComplexParticle(particleType, particleCount, radius, particleData);
    }

    public Class<?> getRequiredDataClass() {
        switch (toString()) {
            case "REDSTONE":
                return DustOptions.class;
            case "ITEM_CRACK":
                return ItemStack.class;
            case "BLOCK_CRACK":
            case "FALLING_DUST":
            case "BLOCK_DUST":
                return BlockData.class;
            default:
                return null;
        }
    }

    public void display(Player player, Location particleLocation) {
        display(player, player.getWorld(), particleLocation.getX(), particleLocation.getY(), particleLocation.getZ());
    }

    public void display(Location particleLocation) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            display(player, player.getWorld(), particleLocation.getX(), particleLocation.getY(), particleLocation.getZ());
        }
    }

    public void display(Player player, World world, double x, double y, double z) {
        if (!$.isEffectsEnabled(player))
            return;
        if (particleCount <= 0)
            throw new IllegalArgumentException("Particle count <= 0");
        for (int i = 0; i < particleCount; i++) {
            double offsetX = (new Random(UUID.randomUUID().hashCode()).nextInt((int) (radius * 2)) - radius) / 2;
            double offsetY = (new Random(UUID.randomUUID().hashCode()).nextInt((int) (radius * 2)) - radius) / 2;
            double offsetZ = (new Random(UUID.randomUUID().hashCode()).nextInt((int) (radius * 2)) - radius) / 2;
            try {
                if (particleData == null) {
                    player.spawnParticle(particleType, x + offsetX, y + offsetY, z + offsetZ, 1, 0, 0, 0, extra);
                } else {
                    player.spawnParticle(particleType, x + offsetX, y + offsetY, z + offsetZ, 1, 0, 0, 0, extra, particleData);
                }
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Particle requires " + requiredDataClass.getName() + ", null provided");
            }
        }
    }

    public void display(World world, double x, double y, double z) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            display(player, player.getWorld(), x, y, z);
        }
    }

    public String toString() {
        return particleType.toString();
    }

}
