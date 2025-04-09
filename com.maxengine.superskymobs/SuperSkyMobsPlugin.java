package com.maxengine.superskymobs;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.maxengine.superskymobs.commands.SuperSkyMobsCommand;
import com.maxengine.superskymobs.listeners.CreatureSpawnListener;
import com.maxengine.superskymobs.listeners.EntityDeathListener;

public class SuperSkyMobsPlugin extends JavaPlugin {
  private CreatureSpawnListener creatureSpawnListener;
  private EntityDeathListener entityDeathListener;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    registerListeners();
    registerCommands();
    getLogger().info("SuperSkyMobs has been enabled!");
  }
  
  @Override
  public void onDisable() {
    getLogger().info("SuperSkyMobs has been disabled!");
  }
  
  private void registerListeners() {
    PluginManager pluginManager = Bukkit.getPluginManager();
    
    creatureSpawnListener = new CreatureSpawnListener(getConfig());
    entityDeathListener = new EntityDeathListener(getConfig());
    
    pluginManager.registerEvents(creatureSpawnListener, this);
    pluginManager.registerEvents(entityDeathListener, this);
  }
  
  private void registerCommands() {
    getCommand("superskymobs").setExecutor(new SuperSkyMobsCommand(this));
  }
  
  public void reload() {
    reloadConfig();
    
    // Обновляем конфигурацию для слушателей
    if (creatureSpawnListener != null) {
      creatureSpawnListener.reloadConfig(getConfig());
    }
    
    if (entityDeathListener != null) {
      entityDeathListener.reloadConfig(getConfig());
    }
    
    getLogger().info("SuperSkyMobs configuration reloaded!");
  }
}
