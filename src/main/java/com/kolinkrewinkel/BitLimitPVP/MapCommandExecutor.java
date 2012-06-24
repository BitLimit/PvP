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
        if (sender.hasPermission("BitLimitPvP") && args.length > 0) {

            if (sender instanceof Player) {
                //Then we cast the sender to player,which means now we can handle him through 'player' variable like any other players
                Player player = (Player) sender;

                WorldGuardPlugin worldGuard = getWorldGuard(sender);
                Vector pt = toVector(player.getLocation()); // This also takes a location
                LocalPlayer localPlayer = worldGuard.wrapPlayer(player);
                
                RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
                ApplicableRegionSet set = regionManager.getApplicableRegions(pt);

                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    ProtectedRegion region = (ProtectedRegion)itr.next();
                    sender.sendMessage(region.getId());
                }
            }
            


//            this.plugin.getConfig().set("BitLimitPvP.message", Joiner.on(' ').join(args));
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
