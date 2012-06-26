package com.kolinkrewinkel.BitLimitPvP;

import java.util.*; 
import com.google.common.base.Joiner;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
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
                messages[1] = "/map define [worldName]";
                sender.sendMessage(messages);
                return true;
            }

            // Define a map
            if (args[0].equals("define") && args.length == 1) {
                sender.sendMessage(args[1].toString());

                return true;
            }
            
            return true;
        } 
        
        return false;
    }   
    
}
