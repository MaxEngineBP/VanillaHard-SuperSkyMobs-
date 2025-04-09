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
  public void onEnable() {
    saveDefaultConfig();
    registerListeners();
    registerCommands();
  }
  
  private void registerListeners() {
    PluginManager pluginManager = Bukkit.getPluginManager();
    pluginManager.registerEvents((Listener)new CreatureSpawnListener(getConfig()), (Plugin)this);
    pluginManager.registerEvents((Listener)new EntityDeathListener(getConfig()), (Plugin)this);
  }
  
  private void registerCommands() {
    getCommand("superskymobs").setExecutor((CommandExecutor)new SuperSkyMobsCommand(this));
  }
  
  public void reload() {
    reloadConfig();
  }
}
