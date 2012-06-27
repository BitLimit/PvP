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

                boolean canCreate = createMapDefinitionWithWorldName(worldName, sender);
                if (canCreate) {
                    sender.sendMessage(ChatColor.GREEN + "Yes.");
                } else {
                    sender.sendMessage(ChatColor.RED + "No.");
                }

                return true;
            }
            
            return true;
        } 
        
        return false;
    }

    private boolean createMapDefinitionWithWorldName(String worldName, CommandSender sender) {

        WorldCreator creator = new WorldCreator(worldName);
        creator.generateStructures(false);
        long seed = 911002014; // Porsche reference.  Because it's always necessary to include a Porsche reference.
        creator.seed(seed);
        creator.type(WorldType.FLAT);
        World resultWorld = Bukkit.getServer().createWorld(creator);
        if (sender instanceof Player && resultWorld != null) {
            // Send all players to the new world
            Player[] players = Bukkit.getServer().getOnlinePlayers();

            for (Player player : players) {
                player.teleport(resultWorld.getSpawnLocation());

                PotionEffect potionEffect = new PotionEffect(PotionEffectType.CONFUSION, 175, 1);
                PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, 175, 1);
                potionEffect.apply(player);
                nightVision.apply(player);
            }

            return true;
        }

        

        plugin.getLogger().log(Level.INFO, Bukkit.getWorlds().toString());
        
        return false;
    }
    
}
