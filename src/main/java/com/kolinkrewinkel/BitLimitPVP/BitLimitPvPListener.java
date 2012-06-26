package com.kolinkrewinkel.BitLimitPvP;

import java.text.MessageFormat;
import java.util.*; 

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;


import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import static com.sk89q.worldguard.bukkit.BukkitUtil.*;

public class BitLimitPvPListener implements Listener {
    private final BitLimitPvP plugin; // Reference main plugin
//	private Random rand = new Random(System.nanoTime()); // Random respawn controller
//    
    public BitLimitPvPListener(BitLimitPvP plugin) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
    }
//
//    /*
//     * Send the sample message to all players that join
//     */
//    @EventHandler
//    public void onPlayerJoin(PlayerJoinEvent event) {
//        Player player = event.getPlayer(); // The player who joined
//        PlayerInventory inventory = player.getInventory(); // The player's inventory
//        
//        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
//        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//
//        ItemStack bow = new ItemStack(Material.BOW, 1);
//        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
//        bow.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//
//        if (!inventory.contains(sword) && !inventory.contains(bow)) {
//            ItemStack arrow = new ItemStack(Material.ARROW, 1);
//            ItemStack food = new ItemStack(Material.COOKED_BEEF, 8);
//            
//            ItemStack[] armor = new ItemStack[4];
//            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
//            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
//            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
//            ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
//            
//            boots.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//            leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//            chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//            helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//            
//            armor[0] = boots;
//            armor[1] = leggings;
//            armor[2] = chestplate;
//            armor[3] = helmet;
//
//            inventory.setArmorContents(armor);
//            
//            inventory.addItem(sword);
//            inventory.addItem(bow);
//            inventory.addItem(arrow);
//            inventory.addItem(food);
//        }
//    }
//    
//    @EventHandler
//    public void onPlayerRespawn(PlayerRespawnEvent event) {
//        Player player = event.getPlayer(); // Get player who respawned
//        Location respawnLocation = event.getRespawnLocation();
//        
//        //      Respawn Location Randomization
//        WorldGuardPlugin worldGuard = getWorldGuard(player);
//        Vector pt = toVector(respawnLocation); // This also takes a location
//        
//        RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
//        ApplicableRegionSet set = regionManager.getApplicableRegions(pt);
//        
//        Iterator itr = set.iterator();
//        ProtectedRegion region = null;
//        while (itr.hasNext()) {
//            region = (ProtectedRegion) itr.next();
//        }
//        event.setRespawnLocation(getRandomLocationInRegionWithPlayerVerifyIntersection(region, player));
//        
//        //      Auto equipping
//        PlayerInventory inventory = player.getInventory(); // The player's inventory
//        
//        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
//        sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//
//        ItemStack bow = new ItemStack(Material.BOW, 1);
//        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
//        bow.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//
//        ItemStack arrow = new ItemStack(Material.ARROW, 1);
//        ItemStack food = new ItemStack(Material.COOKED_BEEF, 8);
//        
//        ItemStack[] armor = new ItemStack[4];
//        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
//        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
//        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
//        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
//
//        boots.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//        leggings.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//        chestplate.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//        helmet.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
//
//        armor[0] = boots;
//        armor[1] = leggings;
//        armor[2] = chestplate;
//        armor[3] = helmet;
//
//        inventory.setArmorContents(armor);
//        
//        inventory.addItem(sword);
//        inventory.addItem(bow);
//        inventory.addItem(arrow);
//        inventory.addItem(food);
//    }
//    
//    @EventHandler
//    public void onPlayerDeath(PlayerDeathEvent event) {
//        event.getDrops().clear(); // Remove drops.
//        event.setDroppedExp(0);
//        
//        Player deadPlayer = event.getEntity();
//        if (deadPlayer.getKiller() instanceof Player) {
//            Player killer = deadPlayer.getKiller();
//
//            int oldLevel = killer.getLevel();
//            killer.setLevel(oldLevel + 1);
//        }
//    }
//
//    private WorldGuardPlugin getWorldGuard(Player player)
//    {
//    Plugin plugin = player.getServer().getPluginManager().getPlugin("WorldGuard");
//    if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
//        return null; //throws a NullPointerException, telling the Admin that WG is not loaded.
//    }
//
//    return (WorldGuardPlugin)plugin;
//    }
//
//    private Location getRandomLocationInRegionWithPlayerVerifyIntersection(ProtectedRegion region, Player player)
//    {
//    Location randomLocation = getRandomLocationInRegionWithPlayer(region, player);
//    while (!region.contains(toVector(randomLocation))) {
//        randomLocation = getRandomLocationInRegionWithPlayer(region, player);
//    }
//
//    return randomLocation;
//    }
//
//    private Location getRandomLocationInRegionWithPlayer(ProtectedRegion region, Player player)
//    {
//    Vector minPoint = region.getMinimumPoint();
//    Vector maxPoint = region.getMaximumPoint();
//
//    int diffX = Math.abs((int)maxPoint.getX() - (int)minPoint.getX() + 1);
//    int diffZ = Math.abs((int)maxPoint.getZ() - (int)minPoint.getZ() + 1);
//
//    int x = rand.nextInt(diffX) + (int)minPoint.getX();
//    int z = rand.nextInt(diffZ) + (int)minPoint.getZ();
//
//
//    // Adapted from RobertZenz/SpawnRandomizer
//    int y = player.getWorld().getHighestBlockYAt(x, z);
//
//    Location randomLocation = new Location(player.getWorld(), (double)x, (double)y, (double)z);
//
//    return randomLocation;
//    }
}
