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

public class TeamCommandExecutor implements CommandExecutor {
    private final BitLimitPvP plugin;
    
    public TeamCommandExecutor(BitLimitPvP plugin) {
        this.plugin = plugin;
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permissioned admins
        if (sender.hasPermission("BitLimitPvP")) {

        } else {
            if (args.length > 0) {

                if (args[0].equals("join")) {
                    if (sender instanceof Player) {
                        Player player = (Player)sender;
                    } else {
                        sender.sendMessage(ChatColor.RED + "A player is required to join a team.");
                    }
                }

            } else {
                sender.sendMessage(ChatColor.RED + "No parameters specified.");
            }
        }
        return false;
    }
}
