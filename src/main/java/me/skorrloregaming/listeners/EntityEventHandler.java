package me.skorrloregaming.listeners;

import me.skorrloregaming.*;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

public class EntityEventHandler implements Listener {

    private static Map.Entry<Long, Location> lastCreeperSpawnEgg = null;
    private int lastSecond = 0;
    private int hits = 0;

    public static void setLastCreeperSpawnEgg(long time, Location location) {
        lastCreeperSpawnEgg = new AbstractMap.SimpleEntry<>(time, location);
    }

    @EventHandler
    public void onEntitySpawnEvent(EntitySpawnEvent event) {
        if (event.getEntity() instanceof Phantom)
            event.setCancelled(true);
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
            if (lastCreeperSpawnEgg != null)
                if (System.currentTimeMillis() - lastCreeperSpawnEgg.getKey() < 500)
                    if (event.getLocation().getWorld().getName().equals(lastCreeperSpawnEgg.getValue().getWorld().getName()))
                        if (event.getLocation().distance(lastCreeperSpawnEgg.getValue()) < 5)
                            event.setCancelled(false);
        }
        if ($.getMinigameFromWorld(event.getLocation().getWorld()) == ServerMinigame.SURVIVAL) {
            Location survivalSpawn = $.getZoneLocation($.getMinigameFromWorld(event.getLocation().getWorld()).toString().toLowerCase());
            if (event.getLocation().getWorld().getName().equals(survivalSpawn.getWorld().getName()))
                if (event.getLocation().distance(survivalSpawn) < 150)
                    event.setCancelled(true);
        }
        if (event.getEntity() instanceof Item) {
            if ($.getMinigameFromWorld(event.getLocation().getWorld()) == ServerMinigame.CREATIVE) {
                event.setCancelled(true);
                int currentSecond = (int) (System.currentTimeMillis() / 1000);
                if (lastSecond != currentSecond) {
                    lastSecond = currentSecond;
                    hits = 0;
                } else {
                    hits++;
                }
                if (hits > 1500) {
                    hits = 0;
                    Location loc = event.getLocation();
                    String rawMessage = Link$.italicGray + "Lag generator detected in " + loc.getWorld().getName() + " at {x: " + loc.getBlockX() + ", y: " + loc.getBlockY() + ", z: " + loc.getBlockZ() + "}";
                    Map<String, String> message = new MapBuilder().message(rawMessage).range(0).build();
                    LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, message);
                    Logger.info(rawMessage);
                }
            }
        }
    }

    @EventHandler
    public void ItemDespawnEvent(ItemDespawnEvent event) {
        ItemStack item = event.getEntity().getItemStack();
        if (item.hasItemMeta() && $.isRawRepairable(item)) {
            ItemMeta meta = item.getItemMeta();
            ItemStack book = Link$.createMaterial(Material.ENCHANTED_BOOK);
            int foundEnchants = 0;
            for (Enchantment enchant : meta.getEnchants().keySet().toArray(new Enchantment[0])) {
                int multiplier = meta.getEnchants().get(enchant);
                Link$.addBookEnchantment(book, enchant, multiplier);
                foundEnchants = foundEnchants + 1;
            }
            if (foundEnchants > 0) {
                event.getEntity().getWorld().dropItemNaturally(event.getLocation(), book);
                event.getEntity().getWorld().playSound(event.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
            }
        }
    }

    @EventHandler
    public void onItemEntityMerge(ItemMergeEvent event) {
        ServerMinigame minigame = $.getMinigameFromWorld(event.getEntity().getWorld());
        if (minigame == ServerMinigame.FACTIONS || minigame == ServerMinigame.SKYBLOCK) {
            Material type = event.getEntity().getItemStack().getType();
            if ($.isMaterialLog(type))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
		/*if (event.isCancelled())
			return;*/
        Location loc = event.getSpawner().getLocation();
        String code = loc.getWorld().getName() + loc.getBlockX() + loc.getBlockY() + loc.getBlockZ();
        if (Server.getInstance().getSpawnerConfig().getData().contains(code)) {
            EntityType previousSpawnedType = event.getSpawner().getSpawnedType();
            Block block = event.getSpawner().getBlock();
            block.breakNaturally();
            block.setType(Material.SPAWNER);
            block.getState().update(true);
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            int upgrade = 0;
            if (Server.getInstance().getSpawnerConfig().getData().contains(code + ".selectedUpgrade"))
                upgrade = Integer.parseInt(Server.getInstance().getSpawnerConfig().getData().getString(code + ".selectedUpgrade"));
            spawner.setSpawnedType(previousSpawnedType);
            spawner.setDelay(300 - ((upgrade + 1) * 50));
            spawner.update(true);
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (event.getEntityType() == EntityType.CREEPER || event.getEntityType() == EntityType.PRIMED_TNT) {
            ServerMinigame minigame = $.getMinigameFromWorld(entity.getWorld());
            if (minigame != ServerMinigame.SURVIVAL)
                event.setCancelled(true);
            if (minigame == ServerMinigame.FACTIONS) {
                float power = 0.3F;
                if (event.getEntity().isGlowing())
                    power = 0.4F;
                CraftExplosion ce = new CraftExplosion(entity.getLocation(), power, true);
                ce.explodeNaturally();
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
        if (event.getEntity().getKiller() instanceof Player)
            return;
        boolean lavaSkeleton = false;
        boolean lavaCause = false;
        if (!(damageEvent == null) && damageEvent.getCause() == DamageCause.LAVA)
            lavaCause = true;
        if (!(damageEvent == null) && damageEvent.getCause() == DamageCause.FIRE)
            lavaCause = true;
        if (!(damageEvent == null) && damageEvent.getCause() == DamageCause.FIRE_TICK)
            lavaCause = true;
        if (lavaCause && event.getEntity() instanceof Skeleton) {
            event.getDrops().clear();
            Random ran = new Random();
            if (ran.nextInt(90) == 0) {
                event.getDrops().add(Link$.createMaterial(Material.WITHER_SKELETON_SKULL));
            }
            int boneAmount = ran.nextInt(3);
            if (boneAmount == 0)
                boneAmount = 1;
            event.getDrops().add(Link$.createMaterial(Material.BONE, boneAmount));
            lavaSkeleton = true;
        }
        if (!lavaSkeleton && event.getEntity() instanceof Skeleton) {
            event.getDrops().clear();
            Random ran = new Random();
            int boneAmount = ran.nextInt(4);
            if (boneAmount == 0)
                boneAmount = 1;
            event.getDrops().add(Link$.createMaterial(Material.BONE, boneAmount));
        } else if (event.getEntity() instanceof Zombie) {
            event.getDrops().clear();
            Random ran = new Random();
            int fleshAmount = ran.nextInt(3);
            if (fleshAmount == 0)
                fleshAmount = 1;
            event.getDrops().add(Link$.createMaterial(Material.ROTTEN_FLESH, fleshAmount));
        } else if (event.getEntity() instanceof IronGolem) {
            event.getDrops().clear();
            Random ran = new Random();
            int ironAmount = ran.nextInt(6);
            if (ironAmount == 0)
                ironAmount = 1;
            event.getDrops().add(Link$.createMaterial(Material.IRON_INGOT, ironAmount));
        }
    }

    @EventHandler
    public void onBlockEat(EntityChangeBlockEvent eat) {
        Material type = eat.getBlock().getType();
        if (eat.getEntity() instanceof Wither) {
            if (type == Material.OBSIDIAN) {
                eat.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        if (event.getEntity() instanceof Enderman)
            event.setCancelled(true);
    }

}
