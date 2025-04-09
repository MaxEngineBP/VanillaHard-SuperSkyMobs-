package com.maxengine.superskymobs.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.maxengine.superskymobs.utils.Random;

public class EntityDeathListener implements Listener {
  private FileConfiguration configuration;
  
  @EventHandler
  public void onDeath(EntityDeathEvent event) {
    LivingEntity entity = event.getEntity();
    EntityType entityType = entity.getType();
    Location location = entity.getLocation();
    
    // Process head drops
    handleHeadDrops(entityType, location);
    
    // We don't need to handle equipment drops here because Minecraft's native
    // drop system will respect the drop chances we've set during mob spawn
    // in CreatureSpawnListener
  }
  
  private void handleHeadDrops(EntityType entityType, Location location) {
    Material material = null;
    switch (entityType) {
      case SKELETON:
        material = Material.SKELETON_SKULL;
        break;
      case WITHER_SKELETON:
        material = Material.WITHER_SKELETON_SKULL;
        break;
      case ZOMBIE:
        material = Material.ZOMBIE_HEAD;
        break;
      case CREEPER:
        material = Material.CREEPER_HEAD;
        break;
      case PIGLIN:
        // Check if PIGLIN_HEAD is available (Minecraft 1.21+)
        try {
          material = Material.valueOf("PIGLIN_HEAD");
        } catch (IllegalArgumentException e) {
          // If no such value in Enum, this version doesn't support this head
          material = null;
        }
        break;
    } 
    
    if (material == null)
      return;
      
    if (!configuration.getBoolean("Heads.Enabled", true))
      return;
      
    // Get head drop chance from config, defaulting to the global head-drop chance
    int dropChance = configuration.getInt("Heads.Types." + entityType.name() + ".Chance", 
                                          configuration.getInt("Chances.Head-drop", 35));
                                          
    if (!Random.checkChance(dropChance, 100))
      return;
      
    location.getWorld().dropItemNaturally(location, new ItemStack(material));
  }
  
  public EntityDeathListener(FileConfiguration configuration) {
    this.configuration = configuration;
  }
  
  // Method to reload configuration
  public void reloadConfig(FileConfiguration configuration) {
    this.configuration = configuration;
  }
}
