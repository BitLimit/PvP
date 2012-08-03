package com.kolinkrewinkel.BitLimitPvP;

import java.util.*;
import java.util.logging.Level;
import com.google.common.base.Joiner;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;

public class MatchCommandExecutor implements CommandExecutor {
    private final BitLimitPvP plugin;
    
    public MatchCommandExecutor(BitLimitPvP plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permissioned admins
        if (sender.hasPermission("BitLimitPvP")) {
            // No parameters were passed, fail.
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "At least one parameter is required.  See /help BitLimit PvP for valid parameters.");
                return false;
            }

            // Start and stop commands
            if (args[0].equals("start")) {
                plugin.getServer().broadcastMessage(ChatColor.GOLD + "Match starting in ten seconds...");
                
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        plugin.getServer().broadcastMessage(ChatColor.AQUA + "Match starting in five seconds...");
                    }
                }, 100L);
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    public void run() {
                        plugin.getServer().broadcastMessage(ChatColor.GREEN + "The match has started!");
                    }
                }, 200L);

            } else if (args[0].equals("stop")) {
            
            }
        }
        
        return false;
    }
}
