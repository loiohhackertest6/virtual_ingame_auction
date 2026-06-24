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

public class Time implements CommandExecutor {
    static JavaPlugin plugin;
    public Time(JavaPlugin plugin){
        Time.plugin = plugin;
    }
    public static class Custom_sell {
        public static ItemStack num1 = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        public static ItemStack num2 = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        public static void nums(ItemStack num1,ItemStack num2){
            ItemMeta n1_meta = num1.getItemMeta();
            n1_meta.setDisplayName(ChatColor.BLACK+" ");
            num1.setItemMeta(n1_meta);
            ItemMeta n2_meta = num2.getItemMeta();
            n2_meta.setDisplayName(ChatColor.GREEN+" ");
            num2.setItemMeta(n2_meta);
        }
        public static void money_1(String path_c,String path_b,int cost,Player player,int cl){
            if (Objects.equals(plugin.getConfig().get(path_b), "null")) {
                Open.Custom_auction.remove_money_vault(player, (cost + cl));
                plugin.getConfig().set(path_b, player.getName() + "");
                plugin.getConfig().set(path_c, (cost + cl));
                plugin.saveConfig();
            } else {
                Player p_b = Bukkit.getPlayer((String)plugin.getConfig().get(path_b));
                Get.Custom_get.add_money(cost,p_b,path_b,0);
                Open.Custom_auction.remove_money_vault(player, (cost+cl));
                plugin.getConfig().set(path_b, player.getName() + "");
                plugin.saveConfig();
            }
        }
        public static void money_2(String path_c,int cost,Player player,int cl){
            Open.Custom_auction.remove_money_vault(player, (cl));
            plugin.getConfig().set(path_c, (cost+cl));
            plugin.saveConfig();
        }
        public static void ch_b(Player player, int slot) {
            String path_i = String.join(".", "watch", player.getName()+"");
            int i= (int)plugin.getConfig().get(path_i);
            String path_c = String.join(".", "auction_items", "item" + i, "cost_now");
            String path_b = String.join(".", "auction_items", "item" + i, "buyer");
            int cost = (int)plugin.getConfig().get(path_c);

            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            assert rsp != null;
            Economy economy = rsp.getProvider();
            economy.getBalance(player);

            if(slot==29||slot==31||slot==33) {
                String path_t1 = String.join(".", "time_in_selling", "add1");
                String path_t2 = String.join(".", "time_in_selling", "add2");
                String path_t3 = String.join(".", "time_in_selling", "add3");
                int t1 = plugin.getConfig().getInt(path_t1);
                int t2 = plugin.getConfig().getInt(path_t2);
                int t3 = plugin.getConfig().getInt(path_t3);
                if (Objects.equals(plugin.getConfig().get(path_b), player.getName() + "")){
                    if (slot == 29 && economy.getBalance(player) >= (t1)) {
                        money_2(path_c,cost,player,t1);
                    }else if (slot == 31 && economy.getBalance(player) >= (t2)) {
                        money_2(path_c,cost,player,t2);
                    }else if (slot == 33 && economy.getBalance(player) >= (t3)) {
                        money_2(path_c,cost,player,t3);
                    }else{
                        player.sendMessage("you haven't enough money");
                    }
                }else {
                    if (slot == 29 && economy.getBalance(player) >= (cost + t1)) {
                        money_1(path_c,path_b,cost,player,t1);
                    } else if (slot == 31 && economy.getBalance(player) >= (cost + t2)) {
                        money_1(path_c,path_b,cost,player,t2);
                    } else if (slot == 33 && economy.getBalance(player) >= (cost + t3)) {
                        money_1(path_c,path_b,cost,player,t3);
                    }else{
                        player.sendMessage("you haven't enough money");
                    }
                }
            }

        }
        public static void update_b(InventoryView ai, int i) {
            ItemStack p_item = new ItemStack(Material.PLAYER_HEAD,1);
            ItemMeta n_meta = p_item.getItemMeta();

            String path_b = String.join(".", "auction_items", "item" + i, "buyer");
            n_meta.setDisplayName(ChatColor.BLACK + " ");
            n_meta.setLore(Collections.singletonList(ChatColor.YELLOW + "buyer_now:" + plugin.getConfig().get(path_b)));
            p_item.setItemMeta(n_meta);
            ai.setItem(13 + 9, p_item);
        }
        public static void pre_update_item(InventoryView ai,Player player1) {
            String path_i = String.join(".", "watch", player1.getName()+"");
            int i= (int)plugin.getConfig().get(path_i);
            String path_item = String.join(".", "auction_items", "item" + i, "item");
            ItemStack i_item = (ItemStack) plugin.getConfig().get(path_item);
            assert i_item != null;
            Open.Custom_auction.update_auction_item(ai,i_item,i,13);
            update_b(ai,i);
        }
        public static void pre_open_item(Player player1,int i) {
            String path_pages = String.join(".", "pages", player1.getName() + "");
            if(plugin.getConfig().contains(path_pages) && ((int)plugin.getConfig().get(path_pages)>0)) {
                int a = (int) plugin.getConfig().get(path_pages);
                i = i +(27*a);
            }
            String path_i = String.join(".", "watch", player1.getName()+"");
            plugin.getConfig().set(path_i,i);
            plugin.saveConfig();
            open_item(player1, i);
        }

        public static void open_item(Player player,int item){
            Inventory auction_item = Bukkit.createInventory(null,5*9,ChatColor.YELLOW+"Action Item");
            String path_item = String.join(".", "auction_items", "item" + item, "item");
            if(plugin.getConfig().contains(path_item) && plugin.getConfig().get(path_item)!=null) {
                ItemStack i_item = (ItemStack) plugin.getConfig().get(path_item);
                nums(num1, num2);
                for (int i = 0; i < (5 * 9); i++) {
                    auction_item.setItem(i, num1);
                }
                ItemStack r_item = i_item;
                auction_item.setItem(13, r_item);

                ItemStack p_item = new ItemStack(Material.PLAYER_HEAD,1);
                ItemMeta n_meta = p_item.getItemMeta();

                String path_b = String.join(".", "auction_items", "item" + item, "buyer");
                n_meta.setDisplayName(ChatColor.BLACK+" ");
                n_meta.setLore(Collections.singletonList(ChatColor.YELLOW+"buyer_now:" + plugin.getConfig().get(path_b)));
                p_item.setItemMeta(n_meta);
                auction_item.setItem(13+9, p_item);
                for(int j=0;j<3;j++){
                    ItemMeta n2_meta = num2.getItemMeta();
                    String path_t = String.join(".", "time_in_selling", "add"+(j+1));
                    n2_meta.setLore(Collections.singletonList(ChatColor.GREEN+""+(int)plugin.getConfig().getInt(path_t) ));
                    num2.setItemMeta(n2_meta);
                    auction_item.setItem(29+((j)*2), num2);
                }
                player.openInventory(auction_item);
            }
        }
        public static void set_time(int time) {
            String path_tm = String.join(".", "time_in_selling", "time");
            plugin.getConfig().set(path_tm,time*1000);
            plugin.saveConfig();
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)) return true;
        if(args.length!=1) return false;

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("set_auction_time") || command.getName().equalsIgnoreCase("auc_time")) {
            String time = args[0];
            try {
                int auc_time = Integer.parseInt(time);
                if (player.hasPermission("auctionr1.command3")) {
                    Custom_sell.set_time(auc_time);
                } else {
                    player.sendMessage(ChatColor.RED + "you haven't permission");
                    return true;
                }
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "you must send time of selling items as integer");
                return true;
            }
            return true;
        }
        return false;
    }
}
