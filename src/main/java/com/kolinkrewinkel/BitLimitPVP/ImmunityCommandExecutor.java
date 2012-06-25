package com.kolinkrewinkel.BitLimitPvP;

import java.util.*; 
import com.google.common.base.Joiner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import org.bukkit.entity.Player;

import org.bukkit.ChatColor;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import static com.sk89q.worldguard.bukkit.BukkitUtil.*;

public class ImmunityCommandExecutor implements CommandExecutor {
    private final BitLimitPvP plugin;

    public ImmunityCommandExecutor(BitLimitPvP plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Please provide a player.");
        }


        
        return false;
    }
}
