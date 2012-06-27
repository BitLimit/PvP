package com.kolinkrewinkel.BitLimitPvP;

import java.util.*;
import java.util.logging.Level;
import com.google.common.base.Joiner;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.potion.*;

public class MapCommandExecutor implements CommandExecutor {
    private final BitLimitPvP plugin;
    
    public MapCommandExecutor(BitLimitPvP plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permissioned admins
        if (sender.hasPermission("BitLimitPvP")) {
            // Passed no arguments, return possible arguments
            if (args.length == 0) {
                String[] messages = new String[3];
                messages[0] = ChatColor.GREEN + "/map usage";
                messages[1] = "/map define [world name]";
                messages[2] = "/map load [map/world name]";
                sender.sendMessage(messages);
                return true;
            }

            // Define a map
            if (args[0].equals("define")) {
                // Check if world name was supplied.
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Supply a world name.");
                    return false;
                }

                // Quick reference to argument after define (name.)
                String worldName = args[1];

                // Return if map is already defined in config.
                if (mapExistsWithNameNotifySender(worldName, sender))
                    return false;

                // Check for existing loaded world with name.
                World newWorld = Bukkit.getServer().getWorld(worldName);

                // No world already exists with this name, create it for them.
                if (newWorld == null) {
                    newWorld = createMapDefinitionWithWorldName(worldName);

                    // World still could not be created for some reason, fail.
                    if (newWorld == null) {
                        newWorld = Bukkit.getServer().getWorld(worldName);
                        sender.sendMessage(ChatColor.RED + "No world by this name could be found nor created.");
                        return false;
                    }
                }

                // World was loaded successfully: notify.
                sender.sendMessage(ChatColor.GREEN + "World successfully loaded with name " + ChatColor.WHITE + worldName + ChatColor.GREEN + ".");

                // Save it in config
                plugin.getConfig().set("maps." + worldName + ".world", worldName);
                plugin.saveConfig();

                return true;
            } else if (args[0].equals("load")) {
                
            }
            
            return true;
        } 
        
        return false;
    }

    private World createMapDefinitionWithWorldName(String worldName) {
        WorldCreator creator = new WorldCreator(worldName);
        creator.generateStructures(false);
        creator.type(WorldType.FLAT);

        long seed = 911002014; // Porsche reference.  Because it's always necessary to include a Porsche reference.
        creator.seed(seed);

        // Create the world with specified attributes
        World resultWorld = Bukkit.getServer().createWorld(creator);

        return resultWorld;
    }

    private boolean mapExistsWithNameNotifySender(String worldName, CommandSender sender) {
        String existingWorldCheck = plugin.getConfig().getString("maps." + worldName + ".world");
        
        if (existingWorldCheck != null) {
            sender.sendMessage(ChatColor.RED + "Map with provided name already exists.");
            
            return true;
        }

        return false;
    }
    
}
