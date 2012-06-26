package com.kolinkrewinkel.BitLimitPvP;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;

public class BitLimitPvP extends JavaPlugin {

    @Override
    public void onEnable() {
        new BitLimitPvPListener(this);

        this.getCommand("map").setExecutor(new MapCommandExecutor(this));
    }
    
    @Override
    public void onDisable() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}

