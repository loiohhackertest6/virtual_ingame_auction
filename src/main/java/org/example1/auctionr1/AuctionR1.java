package org.example1.auctionr1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.example1.auctionr1.Commands.*;

import java.util.Objects;

public final class AuctionR1 extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new Events(),this);
        Objects.requireNonNull(getServer().getPluginCommand("auction_open")).setExecutor(new Open(this));
        Objects.requireNonNull(getServer().getPluginCommand("auction_sell")).setExecutor(new Sell(this));
        Objects.requireNonNull(getServer().getPluginCommand("set_auction_time")).setExecutor(new Time(this));
        Objects.requireNonNull(getServer().getPluginCommand("set_auction_adds")).setExecutor(new Adds(this));
        Objects.requireNonNull(getServer().getPluginCommand("auction_get")).setExecutor(new Get(this));


        if(getServer().getPluginManager().getPlugin("Vault") == null){
            this.getServer().getLogger().info("Vault not founded");
            getServer().getPluginManager().disablePlugin(this);
        }

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            Sell.Custom_sell.clear_time();
            for(Player player : Bukkit.getOnlinePlayers()) {
                if (player.getOpenInventory().getTitle().equals(ChatColor.YELLOW+"Action")) {
                    InventoryView auction = player.getOpenInventory();
                    Open.Custom_auction.update_auction(player, auction);
                }else if(player.getOpenInventory().getTitle().equals(ChatColor.YELLOW+"Action Item")){
                    InventoryView auction_item = player.getOpenInventory();
                    Time.Custom_sell.pre_update_item(auction_item,player);
                }else if(player.getOpenInventory().getTitle().equals(ChatColor.YELLOW + "Your Action Inventory")){
                    InventoryView auction_item = player.getOpenInventory();
                    Get.Custom_get.update_user(player,auction_item);
                }
            }
        }, 0L, 1L);
    }
}
