package com.maxengine.superskymobs.listeners;

import java.util.AbstractMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import com.maxengine.superskymobs.data.EquipmentType;
import com.maxengine.superskymobs.utils.Random;

public class CreatureSpawnListener implements Listener {
  private static final Enchantment[] HELMET_ENCHANTMENTS = new Enchantment[] { Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE, Enchantment.THORNS, Enchantment.WATER_WORKER, Enchantment.DURABILITY, Enchantment.BINDING_CURSE, Enchantment.VANISHING_CURSE };
  
  private static final Enchantment[] CHESTPLATE_ENCHANTMENTS = new Enchantment[] { Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE, Enchantment.THORNS, Enchantment.DURABILITY, Enchantment.BINDING_CURSE, Enchantment.VANISHING_CURSE };
  
  private static final Enchantment[] LEGGINGS_ENCHANTMENTS = new Enchantment[] { Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE, Enchantment.THORNS, Enchantment.DURABILITY, Enchantment.BINDING_CURSE, Enchantment.VANISHING_CURSE };
  
  private static final Enchantment[] BOOTS_ENCHANTMENTS = new Enchantment[] { 
      Enchantment.PROTECTION_ENVIRONMENTAL, Enchantment.PROTECTION_FIRE, Enchantment.PROTECTION_EXPLOSIONS, Enchantment.PROTECTION_PROJECTILE, Enchantment.PROTECTION_FALL, Enchantment.THORNS, Enchantment.DEPTH_STRIDER, Enchantment.FROST_WALKER, Enchantment.DURABILITY, Enchantment.BINDING_CURSE, 
      Enchantment.VANISHING_CURSE, Enchantment.SOUL_SPEED };
  
  private static final Enchantment[] BOW_ENCHANTMENTS = new Enchantment[] { Enchantment.DURABILITY, Enchantment.ARROW_INFINITE, Enchantment.ARROW_DAMAGE, Enchantment.ARROW_KNOCKBACK, Enchantment.ARROW_FIRE };
  
  private static final Enchantment[] SWORD_ENCHANTMENTS = new Enchantment[] { Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT, Enchantment.KNOCKBACK, Enchantment.DAMAGE_UNDEAD, Enchantment.DIG_SPEED, Enchantment.LOOT_BONUS_MOBS, Enchantment.DURABILITY };
  
  private static final Enchantment[] SHOVEL_ENCHANTMENTS = new Enchantment[] { Enchantment.DIG_SPEED, Enchantment.DURABILITY, Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT };
  
  private static final Enchantment[] AXE_ENCHANTMENTS = new Enchantment[] { Enchantment.DIG_SPEED, Enchantment.DURABILITY, Enchantment.DAMAGE_ALL, Enchantment.FIRE_ASPECT };
  
  private FileConfiguration configuration;
  
  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
    EntityEquipment equipment;
    int chance;
    EntityType entityType = event.getEntityType();
    LivingEntity entity = event.getEntity();
    
    // Проверяем тип сущности и применяем соответствующую логику
    switch (entityType) {
      case CREEPER:
        ((Creeper)entity).setPowered(Random.checkChance(this.configuration.getInt("Chances.Powered-creeper"), 100));
        return;
      case ZOMBIE:
      case ZOMBIE_VILLAGER:
      case HUSK:
      case DROWNED:
        equipment = entity.getEquipment();
        if (equipment == null)
          return; 
        generateArmor(equipment);
        if (!Random.checkChance(85, 100) || entityType == EntityType.DROWNED)
          return; 
        chance = Random.generate(100);
        if (chance <= 20) {
          equipment.setItemInMainHand(getItemStack(EquipmentType.AXE, null));
        } else if (chance <= 25) {
          equipment.setItemInMainHand(getItemStack(EquipmentType.SHOVEL, null));
        } else {
          equipment.setItemInMainHand(getItemStack(EquipmentType.SWORD, null));
        } 
        return;
      case SKELETON:
      case STRAY:
        equipment = entity.getEquipment();
        if (equipment == null)
          return; 
        generateArmor(equipment);
        if (!Random.checkChance(85, 100))
          return; 
        equipment.setItemInMainHand(getItemStack(EquipmentType.BOW, null));
        return;
      case WITHER_SKELETON:
      case PIGLIN:
        equipment = entity.getEquipment();
        if (equipment == null)
          return; 
        if (!Random.checkChance(85, 100))
          break; 
        equipment.setItemInMainHand(getItemStack(EquipmentType.SWORD, null));
        break;
    } 
  }
  
  private void generateArmor(EntityEquipment equipment) {
    String material = generateMaterial(false);
    if (Random.checkChance(this.configuration.getInt("Chances.Armor.Full-set"), 100)) {
      equipment.setHelmet(getItemStack(EquipmentType.HELMET, material));
      equipment.setChestplate(getItemStack(EquipmentType.CHESTPLATE, material));
      equipment.setLeggings(getItemStack(EquipmentType.LEGGINGS, material));
      equipment.setBoots(getItemStack(EquipmentType.BOOTS, material));
      return;
    } 
    if (Random.checkChance(this.configuration.getInt("Chances.Armor.Helmet"), 100))
      equipment.setHelmet(getItemStack(EquipmentType.HELMET, material)); 
    if (Random.checkChance(this.configuration.getInt("Chances.Armor.Chestplate"), 100))
      equipment.setChestplate(getItemStack(EquipmentType.CHESTPLATE, material)); 
    if (Random.checkChance(this.configuration.getInt("Chances.Armor.Leggings"), 100))
      equipment.setLeggings(getItemStack(EquipmentType.LEGGINGS, material)); 
    if (Random.checkChance(this.configuration.getInt("Chances.Armor.Boots"), 100))
      equipment.setBoots(getItemStack(EquipmentType.BOOTS, material)); 
  }
  
  private String generateMaterial(boolean wooden) {
    String material = "";
    int chance = Random.generate(100);
    material = (chance <= 15) ? (wooden ? "WOODEN_" : "LEATHER_") : ((chance <= 25) ? "IRON_" : ((chance <= 40) ? "GOLDEN_" : "DIAMOND_"));
    
    // Добавляем шанс на незеритовую броню для версии 1.16+
    if (chance > 40 && chance <= 45) {
      material = "NETHERITE_";
    }
    
    return material;
  }
  
  private Map.Entry<Enchantment, Integer> generateEnchantment(Enchantment[] enchantments, boolean six) {
    int chance = Random.generate(100);
    int enchantmentLevel = 1;
    if (six) {
      if (chance <= 15) {
        enchantmentLevel = 6;
      } else if (chance <= 25) {
        enchantmentLevel = 5;
      } else if (chance <= 40) {
        enchantmentLevel = 4;
      } else if (chance <= 55) {
        enchantmentLevel = 3;
      } else if (chance <= 75) {
        enchantmentLevel = 2;
      } 
    } else if (chance <= 15) {
      enchantmentLevel = 4;
    } else if (chance <= 35) {
      enchantmentLevel = 3;
    } else if (chance <= 60) {
      enchantmentLevel = 2;
    } 
    return new AbstractMap.SimpleEntry<>(enchantments[Random.generate(enchantments.length - 1)], Integer.valueOf(enchantmentLevel));
  }
  
  private ItemStack getItemStack(EquipmentType equipmentType, String material) {
    String materialName = (equipmentType == EquipmentType.BOW) ? "BOW" : (((material == null) ? generateMaterial((equipmentType == EquipmentType.SWORD || equipmentType == EquipmentType.SHOVEL || equipmentType == EquipmentType.AXE)) : material) + equipmentType.name());
    ItemStack itemStack = new ItemStack(Material.valueOf(materialName));
    if (Random.checkChance(this.configuration.getInt("Chances.Enchant"), 100)) {
      Enchantment[] enchantments = null;
      switch (equipmentType) {
        case HELMET:
          enchantments = HELMET_ENCHANTMENTS;
          break;
        case CHESTPLATE:
          enchantments = CHESTPLATE_ENCHANTMENTS;
          break;
        case LEGGINGS:
          enchantments = LEGGINGS_ENCHANTMENTS;
          break;
        case BOOTS:
          enchantments = BOOTS_ENCHANTMENTS;
          break;
        case SWORD:
          enchantments = SWORD_ENCHANTMENTS;
          break;
        case BOW:
          enchantments = BOW_ENCHANTMENTS;
          break;
        case AXE:
          enchantments = AXE_ENCHANTMENTS;
          break;
        case SHOVEL:
          enchantments = SHOVEL_ENCHANTMENTS;
          break;
      } 
      Map.Entry<Enchantment, Integer> generatedEnchantment = generateEnchantment(enchantments, (equipmentType == EquipmentType.BOW || equipmentType == EquipmentType.SWORD));
      itemStack.addUnsafeEnchantment(generatedEnchantment.getKey(), ((Integer)generatedEnchantment.getValue()).intValue());
    } 
    return itemStack;
  }
  
  public CreatureSpawnListener(FileConfiguration configuration) {
    this.configuration = configuration;
  }
}
