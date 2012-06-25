package com.kolinkrewinkel.BitLimitPvP;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

public class BitLimitPvP extends JavaPlugin {

    @Override
    public void onEnable() {
        new BitLimitPvPListener(this);

        this.getCommand("map").setExecutor(new MapCommandExecutor(this));
        this.getCommand("immunity").setExecutor(new MapCommandExecutor(this));
    }
    
    /*
     * This is called when your plug-in shuts down
     */
    @Override
    public void onDisable() {        
        // save the configuration file, if there are no values, write the defaults.
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}

