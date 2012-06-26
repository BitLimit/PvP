package com.kolinkrewinkel.BitLimitPvP;

import java.util.*;
import java.util.logging.Level;
import com.google.common.base.Joiner;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.*;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

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

                boolean canCreate = canCreateMapDefinitionWithWorldName(worldName);
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

    private boolean canCreateMapDefinitionWithWorldName(String worldName) {
        List worlds = Bukkit.getWorlds();

        ArrayList worldNames = new ArrayList();

        Iterator worldItr = worlds.listIterator();

        while (worldItr.hasNext()) {
            World otherWorld = (World)worldItr.next();
            worldNames.add(otherWorld.getName());
        }

        plugin.getLogger().log(Level.FINEST, worldNames.toString());

        if (worldNames.contains(worldName)) {
            return true;
        }
        
        return false;
    }
    
}
