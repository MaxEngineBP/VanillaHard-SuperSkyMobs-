package com.maxengine.superskymobs.listeners;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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
import org.bukkit.inventory.meta.ItemMeta;
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
  
  // Класс для хранения информации о типе брони/оружия
  private class MaterialInfo {
    private String prefix;
    private double chance;
    private Map<String, Float> dropChances;
    
    public MaterialInfo(String prefix, double chance, Map<String, Float> dropChances) {
      this.prefix = prefix;
      this.chance = chance;
      this.dropChances = dropChances;
    }
    
    public String getPrefix() {
      return prefix;
    }
    
    public double getChance() {
      return chance;
    }
    
    public float getDropChance(String part) {
      return dropChances.getOrDefault(part, 0.0f);
    }
  }
  
  // Списки типов брони и оружия
  private List<MaterialInfo> armorMaterials = new ArrayList<>();
  private List<MaterialInfo> weaponMaterials = new ArrayList<>();
  
  @EventHandler
  public void onSpawn(CreatureSpawnEvent event) {
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
        EntityEquipment equipment = entity.getEquipment();
        if (equipment == null)
          return; 
        
        generateArmor(equipment);
        
        if (!Random.checkChance(this.configuration.getInt("Weapons.Chance"), 100) || entityType == EntityType.DROWNED)
          return;
        
        int chance = Random.generate(100);
        if (chance <= 20) {
          ItemStack axe = getWeapon(EquipmentType.AXE);
          equipment.setItemInMainHand(axe);
          equipment.setItemInMainHandDropChance(getWeaponDropChance(axe, "Axe"));
        } else if (chance <= 25) {
          ItemStack shovel = getWeapon(EquipmentType.SHOVEL);
          equipment.setItemInMainHand(shovel);
          equipment.setItemInMainHandDropChance(getWeaponDropChance(shovel, "Shovel"));
        } else {
          ItemStack sword = getWeapon(EquipmentType.SWORD);
          equipment.setItemInMainHand(sword);
          equipment.setItemInMainHandDropChance(getWeaponDropChance(sword, "Sword"));
        } 
        return;
      case SKELETON:
      case STRAY:
        equipment = entity.getEquipment();
        if (equipment == null)
          return; 
        
        generateArmor(equipment);
        
        if (!Random.checkChance(this.configuration.getInt("Weapons.Chance"), 100))
          return;
        
        ItemStack bow = getWeapon(EquipmentType.BOW);
        equipment.setItemInMainHand(bow);
        equipment.setItemInMainHandDropChance(getWeaponDropChance(bow, "Bow"));
        return;
      case WITHER_SKELETON:
      case PIGLIN:
        equipment = entity.getEquipment();
        if (equipment == null)
          return; 
        
        generateArmor(equipment);
        
        if (!Random.checkChance(this.configuration.getInt("Weapons.Chance"), 100))
          return;
        
        ItemStack sword = getWeapon(EquipmentType.SWORD);
        equipment.setItemInMainHand(sword);
        equipment.setItemInMainHandDropChance(getWeaponDropChance(sword, "Sword"));
        break;
    } 
  }
  
  private void generateArmor(EntityEquipment equipment) {
    if (Random.checkChance(this.configuration.getInt("Armor.Full-set"), 100)) {
      // Генерируем полный комплект одного типа
      String material = generateArmorMaterial();
      
      if (material == null) return;
      
      ItemStack helmet = getArmorItem(EquipmentType.HELMET, material);
      ItemStack chestplate = getArmorItem(EquipmentType.CHESTPLATE, material);
      ItemStack leggings = getArmorItem(EquipmentType.LEGGINGS, material);
      ItemStack boots = getArmorItem(EquipmentType.BOOTS, material);
      
      equipment.setHelmet(helmet);
      equipment.setChestplate(chestplate);
      equipment.setLeggings(leggings);
      equipment.setBoots(boots);
      
      // Устанавливаем шансы выпадения
      equipment.setHelmetDropChance(getArmorDropChance(helmet, "Helmet"));
      equipment.setChestplateDropChance(getArmorDropChance(chestplate, "Chestplate"));
      equipment.setLeggingsDropChance(getArmorDropChance(leggings, "Leggings"));
      equipment.setBootsDropChance(getArmorDropChance(boots, "Boots"));
      
      return;
    }
    
    // Генерируем части брони по отдельности
    if (Random.checkChance(this.configuration.getInt("Armor.Parts.Helmet"), 100)) {
      ItemStack helmet = getArmorItem(EquipmentType.HELMET, null);
      equipment.setHelmet(helmet);
      equipment.setHelmetDropChance(getArmorDropChance(helmet, "Helmet"));
    }
    
    if (Random.checkChance(this.configuration.getInt("Armor.Parts.Chestplate"), 100)) {
      ItemStack chestplate = getArmorItem(EquipmentType.CHESTPLATE, null);
      equipment.setChestplate(chestplate);
      equipment.setChestplateDropChance(getArmorDropChance(chestplate, "Chestplate"));
    }
    
    if (Random.checkChance(this.configuration.getInt("Armor.Parts.Leggings"), 100)) {
      ItemStack leggings = getArmorItem(EquipmentType.LEGGINGS, null);
      equipment.setLeggings(leggings);
      equipment.setLeggingsDropChance(getArmorDropChance(leggings, "Leggings"));
    }
    
    if (Random.checkChance(this.configuration.getInt("Armor.Parts.Boots"), 100)) {
      ItemStack boots = getArmorItem(EquipmentType.BOOTS, null);
      equipment.setBoots(boots);
      equipment.setBootsDropChance(getArmorDropChance(boots, "Boots"));
    }
  }
  
  private String generateArmorMaterial() {
    if (armorMaterials.isEmpty()) {
      return null;
    }
    
    // Используем колесо рулетки для выбора материала с учетом шансов
    double totalChance = 0;
    for (MaterialInfo material : armorMaterials) {
      totalChance += material.getChance();
    }
    
    double roll = Random.generate(100);
    double currentPos = 0;
    
    for (MaterialInfo material : armorMaterials) {
      currentPos += material.getChance();
      if (roll <= currentPos) {
        return material.getPrefix();
      }
    }
    
    // Возвращаем последний материал, если что-то пошло не так
    return armorMaterials.get(armorMaterials.size() - 1).getPrefix();
  }
  
  private String generateWeaponMaterial() {
    if (weaponMaterials.isEmpty()) {
      return "WOODEN_";
    }
    
    // Используем колесо рулетки для выбора материала с учетом шансов
    double totalChance = 0;
    for (MaterialInfo material : weaponMaterials) {
      totalChance += material.getChance();
    }
    
    double roll = Random.generate(100);
    double currentPos = 0;
    
    for (MaterialInfo material : weaponMaterials) {
      currentPos += material.getChance();
      if (roll <= currentPos) {
        return material.getPrefix();
      }
    }
    
    // Возвращаем последний материал, если что-то пошло не так
    return weaponMaterials.get(weaponMaterials.size() - 1).getPrefix();
  }
  
  private ItemStack getArmorItem(EquipmentType equipmentType, String material) {
    String materialName;
    
    if (material == null) {
      material = generateArmorMaterial();
    }
    
    materialName = material + equipmentType.name();
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
      }
      
      if (enchantments != null) {
        Map.Entry<Enchantment, Integer> generatedEnchantment = generateEnchantment(enchantments, false);
        itemStack.addUnsafeEnchantment(generatedEnchantment.getKey(), generatedEnchantment.getValue());
      }
    }
    
    // Сохраняем тип материала в метаданных предмета для определения шанса выпадения
    ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      meta.setLocalizedName(material.toLowerCase());
      itemStack.setItemMeta(meta);
    }
    
    return itemStack;
  }
  
  private ItemStack getWeapon(EquipmentType equipmentType) {
    String materialPrefix = generateWeaponMaterial();
    String materialName;
    
    if (equipmentType == EquipmentType.BOW) {
      materialName = "BOW";
    } else {
      materialName = materialPrefix + equipmentType.name();
    }
    
    ItemStack itemStack = new ItemStack(Material.valueOf(materialName));
    
    if (Random.checkChance(this.configuration.getInt("Chances.Enchant"), 100)) {
      Enchantment[] enchantments = null;
      switch (equipmentType) {
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
      
      if (enchantments != null) {
        Map.Entry<Enchantment, Integer> generatedEnchantment = generateEnchantment(enchantments, 
                                                                                  (equipmentType == EquipmentType.BOW || equipmentType == EquipmentType.SWORD));
        itemStack.addUnsafeEnchantment(generatedEnchantment.getKey(), generatedEnchantment.getValue());
      }
    }
    
    // Сохраняем тип материала в метаданных предмета для определения шанса выпадения
    ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      meta.setLocalizedName(materialPrefix.toLowerCase());
      itemStack.setItemMeta(meta);
    }
    
    return itemStack;
  }
  
  private float getArmorDropChance(ItemStack item, String partName) {
    if (item == null || item.getItemMeta() == null || item.getItemMeta().getLocalizedName().isEmpty()) {
      return 0.0f;
    }
    
    String materialKey = item.getItemMeta().getLocalizedName().toUpperCase().replace("_", ""); 
    
    for (MaterialInfo material : armorMaterials) {
      String prefix = material.getPrefix().replace("_", "").toUpperCase();
      if (prefix.equals(materialKey)) {
        return material.getDropChance(partName) / 100.0f; // Конвертируем из процентов в коэффициент (0-1)
      }
    }
    
    return 0.0f;
  }
  
  private float getWeaponDropChance(ItemStack item, String weaponType) {
    if (item == null || item.getItemMeta() == null || item.getItemMeta().getLocalizedName().isEmpty()) {
      return 0.0f;
    }
    
    String materialKey = item.getItemMeta().getLocalizedName().toUpperCase().replace("_", "");
    
    for (MaterialInfo material : weaponMaterials) {
      String prefix = material.getPrefix().replace("_", "").toUpperCase();
      if (prefix.equals(materialKey)) {
        return material.getDropChance(weaponType) / 100.0f; // Конвертируем из процентов в коэффициент (0-1)
      }
    }
    
    return 0.0f;
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
    
    int index = Random.generate(enchantments.length - 1);
    if (index < 0) index = 0;
    if (index >= enchantments.length) index = enchantments.length - 1;
    
    return new AbstractMap.SimpleEntry<>(enchantments[index], Integer.valueOf(enchantmentLevel));
  }
  
  // Загружаем данные о материалах из конфигурации
  private void loadMaterialsFromConfig() {
    armorMaterials.clear();
    weaponMaterials.clear();
    
    // Загрузка материалов брони
    ConfigurationSection armorSection = configuration.getConfigurationSection("Armor.Types");
    if (armorSection != null) {
      for (String key : armorSection.getKeys(false)) {
        if (!configuration.getBoolean("Armor.Types." + key + ".Enabled", true)) {
          continue;
        }
        
        String prefix;
        if (key.equalsIgnoreCase("Leather")) {
          prefix = "LEATHER_";
        } else if (key.equalsIgnoreCase("Wooden")) {
          prefix = "WOODEN_";
        } else {
          prefix = key.toUpperCase() + "_";
        }
        
        double chance = configuration.getDouble("Armor.Types." + key + ".Chance", 0);
        Map<String, Float> dropChances = new java.util.HashMap<>();
        
        ConfigurationSection dropSection = configuration.getConfigurationSection("Armor.Types." + key + ".Drop-chance");
        if (dropSection != null) {
          for (String part : dropSection.getKeys(false)) {
            float dropChance = (float) configuration.getDouble("Armor.Types." + key + ".Drop-chance." + part, 0);
            dropChances.put(part, dropChance);
          }
        }
        
        armorMaterials.add(new MaterialInfo(prefix, chance, dropChances));
      }
    }
    
    // Загрузка материалов оружия
    ConfigurationSection weaponSection = configuration.getConfigurationSection("Weapons.Types");
    if (weaponSection != null) {
      for (String key : weaponSection.getKeys(false)) {
        if (!configuration.getBoolean("Weapons.Types." + key + ".Enabled", true)) {
          continue;
        }
        
        String prefix;
        if (key.equalsIgnoreCase("Wooden")) {
          prefix = "WOODEN_";
        } else {
          prefix = key.toUpperCase() + "_";
        }
        
        double chance = configuration.getDouble("Weapons.Types." + key + ".Chance", 0);
        Map<String, Float> dropChances = new java.util.HashMap<>();
        
        ConfigurationSection dropSection = configuration.getConfigurationSection("Weapons.Types." + key + ".Drop-chance");
        if (dropSection != null) {
          for (String weapon : dropSection.getKeys(false)) {
            float dropChance = (float) configuration.getDouble("Weapons.Types." + key + ".Drop-chance." + weapon, 0);
            dropChances.put(weapon, dropChance);
          }
        }
        
        weaponMaterials.add(new MaterialInfo(prefix, chance, dropChances));
      }
    }
  }
  
  public CreatureSpawnListener(FileConfiguration configuration) {
    this.configuration = configuration;
    loadMaterialsFromConfig();
  }
  
  // Метод для перезагрузки конфигурации
  public void reloadConfig(FileConfiguration configuration) {
    this.configuration = configuration;
    loadMaterialsFromConfig();
  }
}
