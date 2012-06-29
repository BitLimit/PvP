package com.kolinkrewinkel.BitLimitPvP;

import java.util.*;
import java.util.logging.Level;
import com.google.common.base.Joiner;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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
                messages[0] = ChatColor.GOLD + "/map usage";
                messages[1] = "/map define [world name]" + ChatColor.GRAY + " • Creates a new map definition based on provided world name (inherits name).";
                messages[2] = "/map load [map/world name]" + ChatColor.GRAY + " • Loads provided map if it's defined and teleports connected players to it.";
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
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Supply a map name.");
                    return false;
                }

                // Get the world name and load it from disk
                String worldName = plugin.getConfig().getString("maps." + args[1] + ".world");
                
                WorldCreator creator = new WorldCreator(worldName);
                World nextWorld = Bukkit.getServer().createWorld(creator);
                if (nextWorld != null) {
                    teleportPlayersToWorld(Bukkit.getServer().getOnlinePlayers(), nextWorld);
                } else {
                    sender.sendMessage(ChatColor.RED + "The world configured for this map could not be found.");
                }
            } else if (args[0].equals("flag")) {
                FileConfiguration configuration = plugin.getConfig();
                
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "A map name must be specified.");
                    return false;
                } else if (args.length == 2) {
                    String mapName = args[1];

                    Map <String, Object> values = configuration.getConfigurationSection("maps." + mapName).getValues(true);

                    if (values != null) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            sender.sendMessage(ChatColor.GOLD + entry.getKey() + ": " + ChatColor.WHITE + entry.getValue());
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Map with specified name could not be found.");
                    }


                    return true;
                }

                String mapName = args[1];
                String requestedKey = args[2];

                if (args.length == 3) {
                    Object returnValue = configuration.get("maps." + mapName + "." + requestedKey);

                    if (returnValue == null) {
                        sender.sendMessage(ChatColor.RED + "Requested flag not found.");
                    } else {
                        sender.sendMessage(ChatColor.WHITE + returnValue.toString());
                    }
                    
                    
                    return true;
                } else {
                    
                }

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

    private void teleportPlayersToWorld(Player[] players, World world) {
        // Location of next world's spawn
        Location spawnPoint = world.getSpawnLocation();
        for (Player player : players) {
            // Move all players to new map
            player.teleport(spawnPoint);
        }
    }
    
}
