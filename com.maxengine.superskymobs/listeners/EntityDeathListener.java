package com.maxengine.superskymobs.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import com.maxengine.superskymobs.utils.Random;

public class EntityDeathListener implements Listener {
  private FileConfiguration configuration;
  
  @EventHandler
  public void onDeath(EntityDeathEvent event) {
    EntityType entityType = event.getEntityType();
    Location location = event.getEntity().getLocation();
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
    } 
    if (material == null)
      return; 
    dropHead(location, material);
  }
  
  private void dropHead(Location location, Material material) {
    if (!Random.checkChance(this.configuration.getInt("Chances.Head-drop"), 100))
      return; 
    location.getWorld().dropItemNaturally(location, new ItemStack(material));
  }
  
  public EntityDeathListener(FileConfiguration configuration) {
    this.configuration = configuration;
  }
}
