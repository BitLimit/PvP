package com.kolinkrewinkel.BitLimitPvP;

import java.text.MessageFormat;
import java.util.*; 

import org.bukkit.plugin.Plugin;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;

public class BitLimitPvPListener implements Listener {
    private final BitLimitPvP plugin; // Reference main plugin
    
    public BitLimitPvPListener(BitLimitPvP plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        // Add a teleportation effect for a couple of seconds
        PotionEffect confusion = new PotionEffect(PotionEffect.CONFUSION, 150, 1);
        confusion.apply(event.getPlayer());
        
    }
}

