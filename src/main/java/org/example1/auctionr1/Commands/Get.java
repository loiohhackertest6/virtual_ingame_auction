package org.example1.auctionr1.Commands;

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
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Get implements CommandExecutor {
    static JavaPlugin plugin;
    public Get(JavaPlugin plugin){
        Get.plugin = plugin;
    }
    public static class Custom_get {
        public static ItemStack num1 = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        public static ItemStack num2 = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        public static void nums(ItemStack num1, ItemStack num2) {
            ItemMeta n1_meta = num1.getItemMeta();
            n1_meta.setDisplayName(ChatColor.BLACK + " ");
            num1.setItemMeta(n1_meta);
            ItemMeta n2_meta = num2.getItemMeta();
            n2_meta.setDisplayName(ChatColor.GREEN + "Collect Money");
            num2.setItemMeta(n2_meta);
        }
        public static void update_user(Player player, InventoryView auction_user){
            nums(num1,num2);
            for(int i =0;i<3;i++){
                auction_user.setItem((3+i),num1);
            }
            for(int i =0;i<3;i++){
                String path_i = String.join(".", "auc_users_inv", player.getName(), "item" + i);
                if (plugin.getConfig().contains(path_i) && plugin.getConfig().get(path_i) != null && plugin.getConfig().get(path_i) != "{}") {
                    ItemStack i_item = (ItemStack) plugin.getConfig().get(path_i);

                    String path_l = String.join(".", "auction_items", "item" + i, "lore");
                    List<String> lore = (List<String>)plugin.getConfig().get(path_l);
                    ItemMeta i_meta = i_item.getItemMeta();
                    i_meta.setLore(lore);
                    if(lore!=null && !lore.contains(ChatColor.GREEN + "auction_item")) {
                        lore.add(ChatColor.GREEN + "auction_item");
                        i_meta.setLore(lore);
                    }else{
                        i_meta.setLore(Collections.singletonList(ChatColor.GREEN + "auction_item"));
                    }
                    ItemStack r_item = new ItemStack(i_item);
                    r_item.setItemMeta(i_meta);
                    auction_user.setItem((3+i),r_item);
                }
            }
            String path_m = String.join(".", "auc_users_inv", player.getName(), "money");
            if(plugin.getConfig().contains(path_m)&&plugin.getConfig().get(path_m)!=null) {
                auction_user.setItem(13,num2);
            }else{
                auction_user.setItem(13,num1);
            }
        }
        public static void open_user(Player player){
            Inventory auction_user = Bukkit.createInventory(null,2*9,ChatColor.YELLOW + "Your Action Inventory");
            nums(num1,num2);
            for (int i=0;i<(2*9);i++){
                auction_user.setItem(i,num1);
            }

            player.openInventory(auction_user);
            update_user(player, player.getOpenInventory());
        }

        public static void add_money(int cost, Player player, String path_b,int mes) {
            if (player != null && player.isOnline()) {
                if(mes == 0) {
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.RED + "your payment at auction was dropped: " + cost);
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.RED + "use command /auc_get");
                }else{
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.GREEN + "your item was sold for: " + cost);
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.GREEN + "use command /auc_get");
                }
            }
            String path_m = String.join(".", "auc_users_inv", (String)plugin.getConfig().get(path_b), "money");
            if (!plugin.getConfig().contains(path_m) || plugin.getConfig().get(path_m) == null) {
                plugin.getConfig().set(path_m, cost);
                plugin.saveConfig();
            } else {
                plugin.getConfig().set(path_m, ((int) plugin.getConfig().get(path_m) + cost));
                plugin.saveConfig();
            }
        }
        public static void add_item(ItemStack i_item, Player player, String path_b, String path_l, int mes) {
            if (player != null && player.isOnline()) {
                if(mes == 0) {
                    if (i_item.hasItemMeta() && i_item.getItemMeta().hasDisplayName()) {
                        player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.GREEN + "you buy item from auction: " + i_item.getItemMeta().getDisplayName() + " .");
                    } else {
                        player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.GREEN + "you buy item from auction: " + i_item.getType() + " .");
                    }
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.GREEN + "use command /auc_get");
                }else{
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.RED + "your item wasn't sold");
                    player.sendMessage(ChatColor.YELLOW+"[Auction]:"+ChatColor.RED + "use command /auc_get");
                }
            }
            for(int i=0;i<3;i++) {
                String path_i = String.join(".", "auc_users_inv", (String) plugin.getConfig().get(path_b), "item" + i);
                if (!plugin.getConfig().contains(path_i) || plugin.getConfig().get(path_i) == null) {
                    ItemMeta l_meta = i_item.getItemMeta();
                    if(plugin.getConfig().contains(path_l) && plugin.getConfig().get(path_l)!=null && plugin.getConfig().get(path_l)!="{}"){
                        l_meta.setLore((List<String>)plugin.getConfig().get(path_l));
                        i_item.setItemMeta(l_meta);
                    }else{
                        l_meta.setLore(null);
                        i_item.setItemMeta(l_meta);
                    }
                    plugin.getConfig().set(path_i, i_item);
                    plugin.saveConfig();
                    break;
                } else if(i==2){
                    String path_i0 = String.join(".", "auc_users_inv", (String) plugin.getConfig().get(path_b), "item0");
                    ItemMeta l_meta = i_item.getItemMeta();
                    if(plugin.getConfig().contains(path_l) && plugin.getConfig().get(path_l)!=null && plugin.getConfig().get(path_l)!="{}"){
                        l_meta.setLore((List<String>)plugin.getConfig().get(path_l));
                        i_item.setItemMeta(l_meta);
                    }else{
                        l_meta.setLore(null);
                        i_item.setItemMeta(l_meta);
                    }
                    plugin.getConfig().set(path_i0, i_item);
                    plugin.saveConfig();
                }
            }
        }
        public static void join_p(Player player) {
            String path_p = String.join(".", "auc_users_inv", player.getName()+"");
            String path_m = String.join(".", "auc_users_inv", player.getName()+"", "money");
            if (plugin.getConfig().contains(path_m) || plugin.getConfig().get(path_m) != null) {
                player.sendMessage(ChatColor.RED+"you have "+plugin.getConfig().get(path_m)+" bal at auction");

                int size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_p)).getKeys(false).size();
                if(size>=2) {
                    player.sendMessage(ChatColor.RED+"and "+(size-1)+" item(if items more than 3, first will be cleared)");
                }
                player.sendMessage(ChatColor.RED+"use command /auc_get");
            }else if(!plugin.getConfig().contains(path_m)){
                int size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_p)).getKeys(false).size();
                if(size>=2) {
                    player.sendMessage(ChatColor.RED+"you have "+size+" item(if items more than 3, first will be cleared)");
                    player.sendMessage(ChatColor.RED+"use command /auc_get");
                }
            }
        }
        public static void get_money(Player player){
            String path_m = String.join(".", "auc_users_inv", player.getName(), "money");
            if (plugin.getConfig().contains(path_m) || plugin.getConfig().get(path_m) != null) {
                Open.Custom_auction.add_money_vault(player, (int) plugin.getConfig().get(path_m));
                plugin.getConfig().set(path_m, null);
                plugin.saveConfig();
            }else{
                player.sendMessage("you haven't money at auction");
            }
        }
        public static void get_item(Player player,int i){
            String path_m = String.join(".", "auc_users_inv", player.getName(), "item"+i);
            if (plugin.getConfig().contains(path_m) || plugin.getConfig().get(path_m) != null) {
                ItemStack i_item = (ItemStack) plugin.getConfig().get(path_m);
                player.getInventory().addItem(i_item);
                plugin.getConfig().set(path_m, null);
                plugin.saveConfig();
            }
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("auction_get") || command.getName().equalsIgnoreCase("auc_get")) {
            String path_a = String.join(".", "auc_users_inv", player.getName());
            if(plugin.getConfig().contains(path_a)) {
                Get.Custom_get.open_user(player);
            }
        }
        return true;
    }
}
