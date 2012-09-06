package com.kolinkrewinkel.BitLimitPvP;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import java.util.*;

public class BitLimitPvP extends JavaPlugin {

    public HashMap data = new HashMap();

    @Override
    public void onEnable() {
        new BitLimitPvPListener(this);

        this.getCommand("map").setExecutor(new MapCommandExecutor(this));
        this.getCommand("match").setExecutor(new MatchCommandExecutor(this));
        this.getCommand("team").setExecutor(new TeamCommandExecutor(this));
        this.getCommand("stats").setExecutor(new StatsCommandExecutor(this));
    }

    @Override
    public void onDisable() {        
        // save the configuration file, if there are no values, write the defaults.
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}

