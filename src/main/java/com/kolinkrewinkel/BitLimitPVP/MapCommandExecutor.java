package com.kolinkrewinkel.BitLimitPvP;

import java.util.*; 
import com.google.common.base.Joiner;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import static com.sk89q.worldguard.bukkit.BukkitUtil.*;

public class MapCommandExecutor implements CommandExecutor {
    private final BitLimitPvP plugin;

    /*
     * This command executor needs to know about its plugin from which it came from
     */
    public MapCommandExecutor(BitLimitPvP plugin) {
        this.plugin = plugin;
    }

    /*
     * On command set the sample message
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("BitLimitPvP")) {
            if (args.length == 0) {
                sender.sendMessage("/map usage", "/map define");
                return true;
            }

            if (args[0].equals("define") && args.length == 1) {
                sender.sendMessage(args[1].toString());
            }

            return true;
        } 
        
        return false;
    }

    private WorldGuardPlugin getWorldGuard(CommandSender sender)
    {
        Plugin plugin = sender.getServer().getPluginManager().getPlugin("WorldGuard");
        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
            return null; //throws a NullPointerException, telling the Admin that WG is not loaded.
        }
        
        return (WorldGuardPlugin)plugin;
    }


}
