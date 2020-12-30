package me.pluginTest.zombies;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.pluginTest.Main;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.Material;

public class ZombieTypes implements Listener {
  private Main plugin;

  public ZombieTypes(Main plugin) {
    this.plugin = plugin;
  }

  @EventHandler
  public void onMobSpawn(CreatureSpawnEvent e) {
    EntityType type = e.getEntityType();
    if (type.equals(EntityType.CREEPER) || type.equals(EntityType.SPIDER) || type.equals(EntityType.CAVE_SPIDER)
        || type.equals(EntityType.SKELETON) || type.equals(EntityType.SILVERFISH) || type.equals(EntityType.HUSK)
        || type.equals(EntityType.SLIME) || type.equals(EntityType.WITCH)) {

      Location loc = e.getLocation();
      World w = e.getEntity().getWorld();
      e.getEntity().remove();
      Entity specialZombie = w.spawnEntity(loc, EntityType.ZOMBIE);
      setRandomEffects(specialZombie);

    } else if (type.equals(EntityType.ZOMBIE)) {
      if (!e.getSpawnReason().equals(SpawnReason.CUSTOM)) {
        setRandomEffects(e.getEntity());
      }
    } else if (type.equals(EntityType.DROWNED)) {
      if (e.getSpawnReason().equals(SpawnReason.DROWNED)) {
        e.getEntity().remove();
      }
    } else if (type.equals(EntityType.PHANTOM)) {
      Location loc = e.getLocation();
      World w = e.getEntity().getWorld();
      Entity riderZombie = w.spawnEntity(loc, EntityType.ZOMBIE);
      e.getEntity().addPassenger(riderZombie);
      setRandomEffects(riderZombie);
    }
  }

  @EventHandler
  public void onEntityCombust(EntityCombustEvent e) {
    if (e.getEntity() instanceof Zombie) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onDeath(EntityDeathEvent e) {
    if (e.getEntity() instanceof Zombie && e.getEntity().hasMetadata("Boomer")) {
      Location loc = e.getEntity().getLocation();
      World w = e.getEntity().getWorld();
      TNTPrimed explosion = (TNTPrimed) w.spawnEntity(loc, EntityType.PRIMED_TNT);
      explosion.setYield(10);
      explosion.setFuseTicks(0);
      explosion.setIsIncendiary(false);
    }
  }

  private void setRandomEffects(Entity entity) {
    entity.setFireTicks(0);

    if (!entity.getType().equals(EntityType.ZOMBIE))
      return;
    Random r = new Random();
    int geared = r.nextInt(10);
    int effects = r.nextInt(12);
    int armor = r.nextInt(15);
    int weapon = r.nextInt(18);
    Zombie zombie = (Zombie) entity;
    Material[] helmets = { Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET,
        Material.DIAMOND_HELMET, Material.GOLDEN_HELMET, Material.NETHERITE_HELMET };
    Material[] chestplates = { Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE,
        Material.DIAMOND_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.NETHERITE_CHESTPLATE };
    Material[] leggings = { Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS,
        Material.DIAMOND_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.NETHERITE_LEGGINGS };
    Material[] boots = { Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS,
        Material.GOLDEN_BOOTS, Material.NETHERITE_BOOTS };
    Material[] melee = { Material.WOODEN_AXE, Material.WOODEN_SWORD, Material.STONE_AXE, Material.STONE_SWORD,
        Material.IRON_AXE, Material.IRON_SWORD, Material.DIAMOND_AXE, Material.DIAMOND_SWORD, Material.GOLDEN_AXE,
        Material.GOLDEN_SWORD, Material.NETHERITE_AXE, Material.NETHERITE_SWORD };

    if (geared > 5) {
      if (geared <= 7) {
        if (armor < 6)
          zombie.getEquipment().setHelmet(new ItemStack(helmets[armor]));
        armor = r.nextInt(15);
        if (armor < 6)
          zombie.getEquipment().setChestplate(new ItemStack(chestplates[armor]));
        armor = r.nextInt(15);
        if (armor < 6)
          zombie.getEquipment().setLeggings(new ItemStack(leggings[armor]));
        armor = r.nextInt(15);
        if (armor < 6)
          zombie.getEquipment().setBoots(new ItemStack(boots[armor]));
        if (weapon < 12)
          zombie.getEquipment().setItemInMainHand(new ItemStack(melee[weapon]));
      } else {
        int num = r.nextInt(100);
        if (num < 15) {
          zombie.getEquipment().setHelmet(new ItemStack(helmets[5]));
          zombie.getEquipment().setChestplate(new ItemStack(chestplates[5]));
          zombie.getEquipment().setLeggings(new ItemStack(leggings[5]));
          zombie.getEquipment().setBoots(new ItemStack(boots[5]));
          weapon = r.nextInt(12);
          zombie.getEquipment().setItemInMainHand(new ItemStack(melee[weapon]));
        } else {
          if (armor == 5) {
            armor = r.nextInt(5);
          }
          if (armor < 5) {
            zombie.getEquipment().setHelmet(new ItemStack(helmets[armor]));
            zombie.getEquipment().setChestplate(new ItemStack(chestplates[armor]));
            zombie.getEquipment().setLeggings(new ItemStack(leggings[armor]));
            zombie.getEquipment().setBoots(new ItemStack(boots[armor]));
            if (weapon < 12)
              zombie.getEquipment().setItemInMainHand(new ItemStack(melee[weapon]));
          }
        }
        if (zombie.getEquipment().getHelmet().equals(new ItemStack(Material.NETHERITE_HELMET))) {
          zombie.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 1000000, 9));
          zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 1000000, 9));
          zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2));
          zombie.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 5, 100));
          zombie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 1000000, 1));
          zombie.setCustomName("Tank");
          return;
        }
      }
    }

    switch (effects) {
      case 0:
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 4));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 1));
        zombie.setCustomName("Jumper");
        // Jumper height calculates more damage?
        break;
      case 1:
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2));
        zombie.setCustomName("Zoomer");
        // Normal Faster Zombie
        break;
      case 2:
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 2));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 1));
        zombie.setMetadata("Boomer", new FixedMetadataValue(plugin, "test"));
        // Explodes upon Death
        zombie.setCustomName("Boomer");
        break;
      case 3:
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1000000, 3));
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 4));
        // Speedy, and lethal
        zombie.setCustomName("Witch");
        break;
      case 4:
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 1000000, 2));
        // Upon JUmp, give levitation for few seconds
        // Cannot be killed by height
        zombie.setCustomName("Floater");
        break;
      case 5:
        zombie.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 1000000, 1));
        // Cannot be drowned (Entity.DROWNED?)
        zombie.setCustomName("Drowner");
        break;
      case 6:
        // Special Zombie with land and water capabilities, from the water.
        // One that is adept at both the ocean and the land. (Entity.DROWNED)
        entity.remove();
        Drowned swirler=
                (Drowned) entity.getWorld().spawnEntity(entity.getLocation(),
                EntityType.DROWNED);
        swirler.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000, 0));
        swirler.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1000000, 2));
        swirler.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 1000000, 0));
        swirler.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 1000000, 1));
        swirler.getEquipment().setItemInMainHand(new ItemStack(Material.TRIDENT));
        swirler.setCustomName("Swirler");
        break;
      default:
        break;
    }
  }
}
