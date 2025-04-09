package com.maxengine.superskymobs.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.maxengine.superskymobs.SuperSkyMobsPlugin;

public class SuperSkyMobsCommand implements CommandExecutor {
  private SuperSkyMobsPlugin plugin;
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    if (arguments.length != 1)
      return false; 
    if (!sender.hasPermission("superskymobs.commands.reload")) {
      sender.sendMessage("You don't have enough permissions!");
      return false;
    } 
    this.plugin.reload();
    sender.sendMessage("Plugin successfully reloaded!");
    return true;
  }
  
  public SuperSkyMobsCommand(SuperSkyMobsPlugin plugin) {
    this.plugin = plugin;
  }
}
