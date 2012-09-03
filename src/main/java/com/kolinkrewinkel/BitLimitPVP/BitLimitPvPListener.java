package com.kolinkrewinkel.BitLimitPvP;

import java.text.MessageFormat;
import java.util.*;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.potion.*;
import org.bukkit.event.world.*;


public class BitLimitPvPListener implements Listener {
    private final BitLimitPvP plugin; // Reference main plugin
    
    public BitLimitPvPListener(BitLimitPvP plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        // Add a teleportation effect for a couple of seconds
        PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 150, 0);
        confusion.apply(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Get player and move them to the active world.
        Player player = event.getPlayer();

        // Get active world
        String activeMapName = plugin.getConfig().getString("active-map");
        if (activeMapName == null) {
            activeMapName = "pvp";
        }
        World activeWorld = Bukkit.getServer().getWorld(activeMapName);

        // Teleport player there.
        player.teleport(activeWorld.getSpawnLocation());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!event.isBedSpawn()) {
                // Get active world
            String activeMapName = plugin.getConfig().getString("active-map");
            if (activeMapName == null) {
                activeMapName = "pvp";
            }
            World activeWorld = event.getPlayer().getWorld(activeMapName);

            // Teleport player there.
            event.setRespawnLocation(activeWorld.getSpawnLocation());

        }
    }
}

