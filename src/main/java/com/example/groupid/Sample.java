package com.example.groupid;

import org.bukkit.plugin.java.JavaPlugin;

/*
 * This is the main class of the sample plug-in
 */
public class Sample extends JavaPlugin {
    /*
     * This is called when your plug-in is enabled
     */
    @Override
    public void onEnable() {
        // Create the SampleListener
        new SampleListener(this);
        
        // set the command executor for sample
//        this.getCommand("sample").setExecutor(new SampleCommandExecutor(this));
    }
    
    /*
     * This is called when your plug-in shuts down
     */
    @Override
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer(); // The player who joined
        PlayerInventory inventory = player.getInventory(); // The player's inventory
        
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);

        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemStack leggings = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);

        inventory.addItem(sword);
        inventory.addItem(bow);
        inventory.addItem(arrow);
        
        inventory.addItem(helmet);
        inventory.addItem(chestplate);
        inventory.addItem(leggings);
        inventory.addItem(boots);

        player.sendMessage(ChatColor.RED + "Have some gear.");
    }

}
