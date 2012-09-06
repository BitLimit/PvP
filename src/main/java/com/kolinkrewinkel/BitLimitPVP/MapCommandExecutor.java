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
import org.bukkit.inventory.*;
import org.bukkit.block.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

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
                String[] messages = new String[4];
                messages[0] = ChatColor.GOLD + "/map usage";
                messages[1] = "/map define [world name]" + ChatColor.GRAY + " • Creates a new map definition based on provided world name (inherits name).";
                messages[2] = "/map load [map/world name]" + ChatColor.GRAY + " • Loads provided map if it's defined and teleports connected players to it.";
                messages[3] = "/map flag [map name] key [optional replacement value]" + ChatColor.GRAY + " • Gets or sets a flag for a map.";
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
                plugin.getConfig().set("maps." + worldName + ".run", 0);

                plugin.saveConfig();
                
                return true;
            } else if (args[0].equals("load")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "Supply a map name.");
                    return false;
                }
                
                // Get the world name and load it from disk
                String worldName = plugin.getConfig().getString("maps." + args[1] + ".world");

                int previousRun = plugin.getConfig().getInt("maps." + args[1] + ".run");

                previousRun++;

                plugin.getConfig().set("maps." + args[1] + ".run", previousRun);
                plugin.saveConfig();

                copyFolder(new File("./" + worldName), new File("./" + worldName + "-" + previousRun));

                WorldCreator creator = new WorldCreator(worldName + "-" + previousRun);
                World nextWorld = Bukkit.getServer().createWorld(creator);
                
                if (nextWorld != null) {
                    teleportPlayersToWorld(Bukkit.getServer().getOnlinePlayers(), nextWorld);

                    unloadAllKeepWorld(nextWorld);
                    saveActiveMapToConfig(worldName);
                } else {
                    sender.sendMessage(ChatColor.RED + "The world configured for this map could not be found.");
                }
            } else if (args[0].equals("flag")) {
                // Load config object from disk
                FileConfiguration configuration = plugin.getConfig();

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "A map name must be specified.");
                    return false;
                } else if (args.length == 2) {
                    String mapName = args[1];
                    
                    ConfigurationSection section = configuration.getConfigurationSection("maps." + mapName);
                    if (section != null) {
                        Map <String, Object> values = section.getValues(true);
                        
                        if (values != null) {
                            for (Map.Entry<String, Object> entry : values.entrySet()) {
                                sender.sendMessage(ChatColor.GOLD + entry.getKey() + ": " + ChatColor.WHITE + entry.getValue().toString());
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Map with specified name could not be found.");
                    }
                    
                } else if (args.length == 3) {
                    
                    String mapName = args[1];
                    String requestedKey = args[2];
                    
                    Object returnValue = configuration.get("maps." + mapName + "." + requestedKey);
                    
                    if (returnValue == null) {
                        sender.sendMessage(ChatColor.RED + "Requested flag not found.");
                    } else {
                        sender.sendMessage(ChatColor.WHITE + returnValue.toString());
                    }

                } else if (args.length >= 4) {
                    String mapName = args[1];
                    String keyToSet = args[2];
                    String replacementValue = args[3];

                    List potentialValues = new ArrayList() {{ add("world"); add("respawn-items"); add("armor-items"); add("team-mode"); add("defined-teams"); }};
                    

                    if (potentialValues.contains(keyToSet)) {
                        if (datatypeMatchesKey(keyToSet, replacementValue)) {
                            if (keyToSet.equals("respawn-items") || keyToSet.equals("armor-items")) {

                                if (!args[3].equals("set")) {
                                    sender.sendMessage(ChatColor.RED + "This property can only be read or set by standing on a chest and appending set to the normal getter.");

                                    return false;
                                }

                                if (sender instanceof Player) {
                                    Player player = (Player)sender;
                                    Block block = player.getLocation().subtract(0, 1, 0).getBlock();
                                    if (block.getType() == Material.CHEST) {
                                        Chest chest = (Chest)block.getState();

                                        ItemStack[] contents = chest.getInventory().getContents();
                                        ArrayList items = new ArrayList<ItemStack>(Arrays.asList(contents));
                                        items.removeAll(Collections.singleton(null));

                                        if (keyToSet.equals("respawn-items") && items.size() > 36) {
                                            sender.sendMessage(ChatColor.RED + "The maximum number of items for this property is 36.");

                                            return false;
                                        } else if (keyToSet.equals("armor-items") && items.size() > 4) {
                                            sender.sendMessage(ChatColor.RED + "The maximum number of items for this property is 4.");

                                            return false;
                                        }

                                        configuration.set("maps." + mapName + "." + keyToSet, items.toArray());
                                    } else {
                                        sender.sendMessage(ChatColor.RED + "A chest is required to set items.");
                                        return false;
                                    }

                                } else {
                                    sender.sendMessage(ChatColor.RED + "A player is required to set this command.");
                                    return false;
                                }
                            
                                configuration.set("maps." + mapName + "." + keyToSet, replacementValue);
                            } else if (keyToSet.equals("team-mode")) {
                                configuration.set("maps." + mapName + "." + keyToSet, replacementValue);
                            } else if (keyToSet.equals("defined-teams")) {
                                List teams = new ArrayList();
                                int index = 0;
                                for (String s : args) {
                                    if (index > 2){
                                        teams.add(s);
                                    }
                                    index++;
                                }
                                
                                configuration.set("maps." + mapName + "." + keyToSet, teams);
                            }
                            
                            plugin.saveConfig();
                            sender.sendMessage(ChatColor.GREEN + "Configuration updated.");
                        } else {
                            sender.sendMessage(ChatColor.RED + "Invalid datatype.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.DARK_RED + "Invalid key.  These keys are valid: " + ChatColor.RED + "world (do not edit without changing map name manually in config), respawn-items");
                    }

                    

                }
                    return true;                
            }
            
            return true;
        } 
        
        return false;
    }

    private void copyFolder(File src, File dest) {
        if (src.isDirectory()) {
             
            // If directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
            }
             
            // List all the directory contents
            String files[] = src.list();
             
            for (String file : files) {
                // Construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // Recursive copy
                copyFolder(srcFile,destFile);
            }
             
        } else {
            // If file, then copy it
            // Use bytes stream to support all file types
            if (src.getName().equals("uid.dat"))
                return;

            try {
                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(dest);

                 
                byte[] buffer = new byte[1024];
                 
                int length;
                // Copy the file content in bytes
                while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
                }
                 
                in.close();
                out.close();
            } catch (Exception e) {

            } finally {

            }
        }
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
    

    private boolean datatypeMatchesKey(String key, Object dataType) {
        if (key.equals("world")) {
            if (dataType instanceof String)
                return true;
            else
                return false;

        } else if (key.equals("respawn-items") || key.equals("armor-items") || key.equals("team-mode") || key.equals("defined-teams")) {
            return true;
        }

        return false;
    }

    private void unloadAllKeepWorld(World keepWorld) {
        Server server = Bukkit.getServer();
        for (World unloadWorld : server.getWorlds()) {
            if (!unloadWorld.equals(keepWorld))
                server.unloadWorld(unloadWorld, true);
        }
        Bukkit.getServer().broadcastMessage(server.getWorlds().toString());
    }

    private void saveActiveMapToConfig(String map) {
        plugin.getConfig().set("active-map", map);
    }
}
