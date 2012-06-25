package com.kolinkrewinkel.BitLimitPvP;

import java.util.*; 
import com.google.common.base.Joiner;

import org.bukkit.command.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import org.bukkit.metadata.*;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
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
        sender.sendMessage(args);
    
        if (sender instanceof Player || sender instanceof ConsoleCommandSender) {
            Player player = (Player)sender;
            String name = player.getPlayerListName();
            String kolin = new String("kkrewink");
            String coestar = new String("Coestar");
            String loserific = new String("Loserific");
            
            if (name.equals(kolin) || name.equals(coestar) || name.equals(loserific)) {

                if (args.length == 0) {

                    List metadataValues = player.getMetadata("vanished");
                    MetadataValue value = (MetadataValue)metadataValues.get(1);
                    boolean vanished = value.asBoolean();

                    if (vanished) {
                        FixedMetadataValue newValue = new FixedMetadataValue(plugin, false);
                        player.setMetadata("vanished", newValue);
                        sender.sendMessage(ChatColor.RED + "Returned to regular state.");
                    } else {
                        FixedMetadataValue newValue = new FixedMetadataValue(plugin, true);
                        player.setMetadata("vanished", newValue);
                        sender.sendMessage(ChatColor.GREEN + "Immunity and barrier granted.");
                    }


                    return true;
                } else if (args.length == 1) {
                    String name = args[0];
                    Player targetPlayer = Bukkit.getServer().getPlayer(name);

                    List metadataValues = targetPlayer.getMetadata("vanished");
                    MetadataValue value = (MetadataValue)metadataValues.get(1);
                    boolean vanished = value.asBoolean();
                    
                    if (vanished) {
                        FixedMetadataValue newValue = new FixedMetadataValue(plugin, false);
                        targetPlayer.setMetadata("vanished", newValue);
                        targetPlayer.sendMessage(ChatColor.RED + "Returned to regular state.");
                        player.sendMessage(ChatColor.GREEN + "Changed " + name + " to regular state.");
                    } else {
                        FixedMetadataValue newValue = new FixedMetadataValue(plugin, true);
                        targetPlayer.setMetadata("vanished", newValue);
                        targetPlayer.sendMessage(ChatColor.GREEN + "Immunity and barrier granted.");
                        player.sendMessage(ChatColor.GREEN + "Changed " + name + " to immune mode.");

                    }
                }
            }

                
        return false;
    }
}
