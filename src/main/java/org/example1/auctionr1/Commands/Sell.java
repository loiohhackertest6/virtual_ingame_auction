package org.example1.auctionr1.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class Sell implements CommandExecutor {
    static JavaPlugin plugin;
    public Sell(JavaPlugin plugin){
        Sell.plugin = plugin;
    }
    public static class Custom_sell {
        public static void clear_time() {
            String path_i = String.join( ".", "auction_items");
            if(plugin.getConfig().contains(path_i) && plugin.getConfig().get(path_i)!=null) {
                int size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_i)).getKeys(false).size();
                if(size-1>0) {
                    for (int i = 0; i < size - 1; i++) {
                        String path_t = String.join(".", "auction_items", "item" + i, "time");
                        long time = Math.abs((currentTimeMillis() - (long) plugin.getConfig().get(path_t)));
                        String path_tm = String.join(".", "time_in_selling", "time");
                        int time_max = (int) plugin.getConfig().get(path_tm);
                        if (time > time_max) {
                            String path_ai = String.join(".", "auction_items", "item" + i, "item");
                            String path_p = String.join(".", "auction_items", "item" + i, "seller");
                            String path_c = String.join(".", "auction_items", "item" + i, "cost_now");
                            String path_b = String.join(".", "auction_items", "item" + i, "buyer");
                            String path_l = String.join(".", "auction_items", "item" + i, "lore");
                            if(!plugin.getConfig().get(path_b).equals("null")) {
                                Get.Custom_get.add_money( (int) (plugin.getConfig().get(path_c)) , Bukkit.getPlayer((String)plugin.getConfig().get(path_p)), path_p,1);
                                Get.Custom_get.add_item( (ItemStack) (plugin.getConfig().get(path_ai)) , Bukkit.getPlayer((String)plugin.getConfig().get(path_b)), path_b,path_l,0);
                            }else{
                                Get.Custom_get.add_item( (ItemStack) (plugin.getConfig().get(path_ai)) , Bukkit.getPlayer((String)plugin.getConfig().get(path_p)), path_p,path_l,1);
                            }
                            if (size > 2) {
                                for (int j = i; j < size - 2; j++) {
                                    String path_auc11 = String.join(".", "auction_items", "item" + j,"item");
                                    String path_auc12 = String.join(".", "auction_items", "item" + j,"cost_now");
                                    String path_auc13 = String.join(".", "auction_items", "item" + j,"seller");
                                    String path_auc14 = String.join(".", "auction_items", "item" + j,"time");
                                    String path_auc15 = String.join(".", "auction_items", "item" + j,"buyer");

                                    String path_auc21 = String.join(".", "auction_items", "item" + (j+1),"item");
                                    String path_auc22 = String.join(".", "auction_items", "item" + (j+1),"cost_now");
                                    String path_auc23 = String.join(".", "auction_items", "item" + (j+1),"seller");
                                    String path_auc24 = String.join(".", "auction_items", "item" + (j+1),"time");
                                    String path_auc25 = String.join(".", "auction_items", "item" + (j+1),"buyer");
                                    plugin.getConfig().set(path_auc11, plugin.getConfig().get(path_auc21));
                                    plugin.getConfig().set(path_auc12, plugin.getConfig().get(path_auc22));
                                    plugin.getConfig().set(path_auc13, plugin.getConfig().get(path_auc23));
                                    plugin.getConfig().set(path_auc14, plugin.getConfig().get(path_auc24));
                                    plugin.getConfig().set(path_auc15, plugin.getConfig().get(path_auc25));
                                    plugin.saveConfig();

                                }
                                String path_li = String.join(".", "auction_items", "last");
                                String path_aucl = String.join(".", "auction_items", "item" + ((int)plugin.getConfig().get(path_li)-1));
                                plugin.getConfig().set(path_aucl,null);
                                int a =(int)plugin.getConfig().get(path_li);
                                plugin.getConfig().set(path_li, (a-1));
                                for(Player player: Bukkit.getOnlinePlayers()) {
                                    String path_ii = String.join(".", "watch", player.getName() + "");
                                    if(plugin.getConfig().contains(path_ii) && (int)plugin.getConfig().get(path_ii)>0) {
                                        int ii = (int)plugin.getConfig().get(path_ii);
                                        plugin.getConfig().set(path_ii, (ii-1));
                                    }else if(plugin.getConfig().contains(path_ii)){
                                        player.closeInventory();
                                        player.sendMessage("Item End Time");
                                        plugin.getConfig().set(path_ii, null);
                                    }
                                }
                                plugin.saveConfig();
                                break;
                            } else if(size==2){
                                String path_li = String.join(".", "auction_items", "last");
                                String path_auc = String.join(".", "auction_items", "item" + (((int)plugin.getConfig().get(path_li))-1));

                                plugin.getConfig().set(path_auc, null);
                                plugin.getConfig().set(path_li, 0);
                                for(Player player: Bukkit.getOnlinePlayers()) {
                                    String path_ii = String.join(".", "watch", player.getName() + "");
                                    if(plugin.getConfig().contains(path_ii)) {
                                        player.closeInventory();
                                        player.sendMessage("Item End Time");
                                        plugin.getConfig().set(path_ii, null);
                                    }
                                }
                                plugin.saveConfig();
                                size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_i)).getKeys(false).size();
                            }
                        }
                    }
                }
            }
        }
        public static void sell_item(Player player1,int cost){
            String path_i = String.join( ".", "auction_items","last");
            int i=0;
            if(plugin.getConfig().contains(path_i) && plugin.getConfig().get(path_i)!=null) {
                i = (int) plugin.getConfig().get(path_i);
                plugin.getConfig().set(path_i, i+1);
                plugin.saveConfig();
            }else{
                plugin.getConfig().set(path_i, 0);
                plugin.saveConfig();
            }
            String path_s = String.join( ".", "auction_items","item"+i,"item");
            if(!plugin.getConfig().contains(path_s)||plugin.getConfig().get(path_s)==null) {
                plugin.getConfig().set(path_s, player1.getInventory().getItemInMainHand());
                String path_c = String.join(".", "auction_items", "item"+i, "cost_now");
                plugin.getConfig().set(path_c, cost);
                String path_p = String.join(".", "auction_items", "item"+i, "seller");
                plugin.getConfig().set(path_p, player1.getName()+"");
                String path_t = String.join(".", "auction_items", "item"+i, "time");
                plugin.getConfig().set(path_t, currentTimeMillis());
                String path_b = String.join(".", "auction_items", "item"+i, "buyer");
                plugin.getConfig().set(path_b, "null");
                if(player1.getInventory().getItemInMainHand().hasItemMeta() && player1.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                    String path_l = String.join(".", "auction_items", "item" + i, "lore");
                    plugin.getConfig().set(path_l, player1.getInventory().getItemInMainHand().getItemMeta().getLore());
                }
                plugin.saveConfig();
                player1.sendMessage(ChatColor.GREEN+"Successfully upload to auction");
            }
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)) return true;
        if(args.length!=1) return false;

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("auction_sell") || command.getName().equalsIgnoreCase("auc_sell")) {
            String cost = args[0];
            try {
                int cost_item = Integer.parseInt(cost);
                if (player.hasPermission("auctionr1.command2")) {
                    player.getInventory().getItemInMainHand();
                    if(player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                        Custom_sell.sell_item(player, cost_item);
                        player.getInventory().setItemInMainHand(null);
                    }else{
                        player.sendMessage(ChatColor.RED + "you haven't a item in main hand");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "you haven't permission");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "you must send cost of sell item as integer");
                return true;
            }
            return true;
        }
        return false;
    }
}
