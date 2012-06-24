package com.kolinkrewinkel.BitLimitPvP;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.enchantments.Enchantment;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import static com.sk89q.worldguard.bukkit.BukkitUtil.*;

public class BitLimitPvPListener implements Listener {
    private final BitLimitPvP plugin; // Reference main plugin
	private Random rand = new Random(System.nanoTime()); // Random respawn controller

    public BitLimitPvPListener(BitLimitPvP plugin) {
        // Register the listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
        this.plugin = plugin;
    }

    /*
     * Send the sample message to all players that join
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer(); // The player who joined
        PlayerInventory inventory = player.getInventory(); // The player's inventory

        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        if (!inventory.contains(sword) && !inventory.contains(bow)) {
            ItemStack arrow = new ItemStack(Material.ARROW, 1);
            ItemStack food = new ItemStack(Material.COOKED_BEEF, 8);
            
            ItemStack[] armor = new ItemStack[4];
            armor[0] = new ItemStack(Material.LEATHER_BOOTS, 1);
            armor[1] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
            armor[2] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
            armor[3] = new ItemStack(Material.LEATHER_HELMET, 1);
            inventory.setArmorContents(armor);
            
            inventory.addItem(sword);
            inventory.addItem(bow);
            inventory.addItem(arrow);
            inventory.addItem(food);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer(); // Get player who respawned
        Location respawnLocation = event.getRespawnLocation();
        
        WorldGuardPlugin worldGuard = getWorldGuard(player);
        Vector pt = toVector(player.getLocation()); // This also takes a location
        LocalPlayer localPlayer = worldGuard.wrapPlayer(player);
        
        RegionManager regionManager = worldGuard.getRegionManager(player.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(pt);
        ProtectedRegion region = set.iterator().next();
        


        PlayerInventory inventory = player.getInventory(); // The player's inventory

        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 8);

        ItemStack[] armor = new ItemStack[4];
        armor[0] = new ItemStack(Material.LEATHER_BOOTS, 1);
        armor[1] = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        armor[2] = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        armor[3] = new ItemStack(Material.LEATHER_HELMET, 1);
        inventory.setArmorContents(armor);

        inventory.addItem(sword);
        inventory.addItem(bow);
        inventory.addItem(arrow);
        inventory.addItem(food);
        
//        player.sendMessage(ChatColor.DARK_BLUE + "Have some gear, broestar.");
    }

    /*
     * Another example of a event handler. This one will give you the name of
     * the entity you interact with, if it is a Creature it will give you the
     * creature Id.
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
//        final EntityType entityType = event.getRightClicked().getType();
//
//        event.getPlayer().sendMessage(MessageFormat.format(
//                "You interacted with a {0} it has an id of {1}",
//                entityType.getName(),
//                entityType.getTypeId()));
    }


    private WorldGuardPlugin getWorldGuard(Player player)
    {
        Plugin plugin = player.getServer().getPluginManager().getPlugin("WorldGuard");
        if ((plugin == null) || (!(plugin instanceof WorldGuardPlugin))) {
            return null; //throws a NullPointerException, telling the Admin that WG is not loaded.
        }
        
        return (WorldGuardPlugin)plugin;
    }

    private Location getRandomLocationInRegionWithPlayer(ProtectedRegion region, Player player)
    {
        Vector minPoint = region.getMinimumPoint();
        Vector maxPoint = region.getMaximumPoint();

        int diffX = Math.abs((int)maxPoint.getX() - (int)minPoint.getX() + 1);
		int diffZ = Math.abs((int)maxPoint.getZ() - (int)minPoint.getZ() + 1);

        int x = rand.nextInt(diffX) + (int)minPoint.getX();
		int z = rand.nextInt(diffZ) + (int)minPoint.getZ();


        // Adapted from RobertZenz/SpawnRandomizer
        int y = player.getWorld().getHighestBlockYAt(x, z);

        Location randomLocation = Location(player.getWorld(), x, y, z);
        return randomLocation;
    }

}
