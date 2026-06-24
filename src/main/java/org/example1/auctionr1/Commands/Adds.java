package org.example1.auctionr1.Commands;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.Objects;

public class Adds implements CommandExecutor {
    static JavaPlugin plugin;
    public Adds(JavaPlugin plugin){
        Adds.plugin = plugin;
    }
    public static class Custom_sell {
        public static void set_time(int add1,int add2,int add3) {
            String path_t1 = String.join(".", "time_in_selling", "add1");
            String path_t2 = String.join(".", "time_in_selling", "add2");
            String path_t3 = String.join(".", "time_in_selling", "add3");
            plugin.getConfig().set(path_t1,add1);
            plugin.getConfig().set(path_t2,add2);
            plugin.getConfig().set(path_t3,add3);
            plugin.saveConfig();
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)) return true;
        if(args.length!=3) return false;

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("set_auction_adds") || command.getName().equalsIgnoreCase("auc_adds")) {
            String time1 = args[0];
            String time2 = args[1];
            String time3 = args[2];
            try {
                int add1 = Integer.parseInt(time1);
                int add2 = Integer.parseInt(time2);
                int add3 = Integer.parseInt(time3);
                if (player.hasPermission("auctionr1.command3")) {
                    Custom_sell.set_time(add1,add2,add3);
                } else {
                    player.sendMessage(ChatColor.RED + "you haven't permission");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "you must send 3 integer(add to cost auction)");
                return true;
            }
            return true;
        }
        return false;
    }
}
