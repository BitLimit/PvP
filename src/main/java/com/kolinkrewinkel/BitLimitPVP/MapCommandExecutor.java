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
                String[] messages = new String[2];
                messages[0] = ChatColor.GREEN + "/map usage";
                messages[1] = "/map define [world name]";
                sender.sendMessage(messages);
                return true;
            }

            // Define a map
            if (args[0].equals("define")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Supply a world name.");

                    return false;
                }

                String worldName = args[1];

                List attributeList = plugin.getConfig().getStringList("maps." + worldName);
                if (attributeList != null) {
                    sender.sendMessage(ChatColor.RED + "Map with provided name already exists.");
                    return false;
                }

                // Check for existing world with name.
                World newWorld = Bukkit.getServer().getWorld(worldName);

                // No world already exists with this name, create it for them.
                if (newWorld == null) {
                    newWorld = createMapDefinitionWithWorldName(worldName);

                    if (newWorld == null) {
                        newWorld = Bukkit.getServer().getWorld(worldName);
                        if (newWorld == null) {
                            sender.sendMessage(ChatColor.RED + "No world by this name could be found nor created.");
                            return false;
                        }
                    }
                }

                sender.sendMessage(ChatColor.GREEN + "World successfully loaded with name " + ChatColor.WHITE + worldName + ChatColor.GREEN + ".");

                // Save it in config
                plugin.getConfig().set("maps." + worldName + ".world", worldName);
                plugin.saveConfig();

                return true;
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
    
}
