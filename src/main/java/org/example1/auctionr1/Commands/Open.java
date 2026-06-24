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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.System.currentTimeMillis;

public class Open implements CommandExecutor {
    static JavaPlugin plugin;
    public Open(JavaPlugin plugin){
        Open.plugin = plugin;
            }
    public static class Custom_auction {
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
        public static void update_auction_item(InventoryView auction,ItemStack i_item,int slot,int plus) {
            String path_c = String.join(".", "auction_items", "item" + slot, "cost_now");
            String path_p = String.join(".", "auction_items", "item" + slot, "seller");
            String path_t = String.join(".", "auction_items", "item" + slot, "time");
            String path_tm = String.join(".", "time_in_selling", "time");
            int time_max = (int) plugin.getConfig().get(path_tm);
            int time = (int)( time_max-(currentTimeMillis() - (long) plugin.getConfig().get(path_t)))/1000;
            int time_h = time / 3600;
            int time_m = (time % 3600) / 60;
            int time_s = (time % 60);
            assert i_item != null;
            ItemMeta s_meta = i_item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "item_selling");
            lore.add(ChatColor.GREEN + "Seller:" + plugin.getConfig().get(path_p));
            lore.add(ChatColor.YELLOW + "Cost:  " + plugin.getConfig().get(path_c));
            if (time_h <= 0){
                lore.add(ChatColor.YELLOW + "EndIn: " + time_m + "m " + time_s + "s");
            }else{
                lore.add(ChatColor.YELLOW + "EndIn: " + time_h + "h " + time_m + "m " + time_s + "s");
            }
            s_meta.setLore(lore);
            ItemStack r_item = i_item;
            r_item.setItemMeta(s_meta);
            auction.setItem(plus, r_item);
        }
        public static void ch_page(Player player1,int slot) {
            String path_pages = String.join(".", "pages", player1.getName() + "");
            int a = (int)plugin.getConfig().get(path_pages);
            String path_i = String.join( ".", "auction_items");
            int size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_i)).getKeys(false).size();
            if(slot==44 && (size-1)>((a+1)*27) ){
                plugin.getConfig().set(path_pages, (a+1));
            }else if(slot==43 && a-1>=0){
                plugin.getConfig().set(path_pages, (a-1));
            }
            plugin.saveConfig();
        }
        public static void update_auction(Player player1, InventoryView auction){
            for (int i=0;i<(5*9);i++){
                auction.setItem(i,num1);
            }
            String path_i = String.join( ".", "auction_items");
            if(plugin.getConfig().contains(path_i) && plugin.getConfig().get(path_i)!=null) {
                int size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_i)).getKeys(false).size();
                if (size - 1 >= 1) {
                    if (size - 1 <= 27) {
                        String[] path_items = new String[size - 1];
                        for (int i = 0; i < size - 1; i++) {
                            path_items[i] = String.join(".", "auction_items", "item" + i, "item");
                            if (plugin.getConfig().contains(path_items[i]) && plugin.getConfig().get(path_items[i]) != null) {
                                ItemStack s_item = (ItemStack) plugin.getConfig().get(path_items[i]);
                                assert s_item != null;
                                update_auction_item(auction, s_item, i,i+9);
                            }
                        }
                    }else{
                        String[] path_items = new String[27];
                        String path_pages = String.join( ".", "pages",player1.getName()+"");
                        int page = (int)plugin.getConfig().get(path_pages);
                        for (int i = (page*27); i < (page*27)+27; i++) {
                            path_items[i%27] = String.join(".", "auction_items", "item" + i, "item");
                            if (plugin.getConfig().contains(path_items[i%27]) && plugin.getConfig().get(path_items[i%27]) != null) {
                                ItemStack s_item = (ItemStack) plugin.getConfig().get(path_items[i%27]);
                                assert s_item != null;
                                update_auction_item(auction,s_item,i%27,i+9);
                            }
                            auction.setItem(44,num2);
                            auction.setItem(43,num2);
                        }
                    }
                }
            }
        }
        public static void open_auction(Player player1){
            Inventory auction = Bukkit.createInventory(null,5*9,ChatColor.YELLOW+"Action");
            nums(num1,num2);
            for (int i=0;i<(5*9);i++){
                auction.setItem(i,num1);
            }

            String path_i = String.join( ".", "auction_items");
            if(plugin.getConfig().contains(path_i) && plugin.getConfig().get(path_i)!=null) {
                int size = Objects.requireNonNull(plugin.getConfig().getConfigurationSection(path_i)).getKeys(false).size();
                if(size-1>=1) {
                    player1.openInventory(auction);
                    InventoryView auc = player1.getOpenInventory();
                    String[] path_items = new String[size-1];
                    if (size-1 <= 27) {
                        for (int i = 0; i < size-1; i++) {
                            path_items[i] = String.join(".", "auction_items", "item" + i, "item");
                            if (plugin.getConfig().contains(path_items[i]) && plugin.getConfig().get(path_items[i]) != null) {
                                ItemStack s_item = (ItemStack) plugin.getConfig().get(path_items[i]);
                                assert s_item != null;
                                update_auction_item(auc,s_item,i,i+9);
                            } else {
                                player1.sendMessage(ChatColor.RED + "you now selling another item");
                            }
                        }
                    }else{
                        String path_pages = String.join( ".", "pages",player1.getName()+"");
                        plugin.getConfig().set(path_pages, 0);
                        plugin.saveConfig();
                        for (int i = 0; i < 27; i++) {
                            path_items[i] = String.join(".", "auction_items", "item" + i, "item");
                            if (plugin.getConfig().contains(path_items[i]) && plugin.getConfig().get(path_items[i]) != null) {
                                ItemStack s_item = (ItemStack) plugin.getConfig().get(path_items[i]);
                                assert s_item != null;
                                update_auction_item(auc,s_item,i,i+9);
                            }
                        }
                        auc.setItem(44,num2);
                        auc.setItem(43,num2);
                    }
                }else{
                    player1.sendMessage("Auction now cleared");
                }
            }

        }

        public static void add_money_vault(Player player, double count){
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            assert rsp != null;
            Economy economy = rsp.getProvider();

            if(player!=null && player.isOnline()) {
                economy.depositPlayer(player, count);
                player.sendMessage(ChatColor.YELLOW + "[Auction]:you get :"+count);
            }else{
                plugin.getServer().getLogger().info("player: "+player.getName()+" b: "+economy.getBalance(player.getName()));
                economy.depositPlayer(player.getName(), count);
                plugin.getServer().getLogger().info("player: "+player.getName()+" a: "+economy.getBalance(player.getName()));
            }
        }
        public static void remove_money_vault(Player player, double count){
            RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            assert rsp != null;
            Economy economy = rsp.getProvider();
            if(player!=null && player.isOnline()) {
                economy.withdrawPlayer(player, count);
                player.sendMessage(ChatColor.YELLOW+"[Auction]:you spend :"+count);
            }else{
                plugin.getServer().getLogger().info("player: "+player.getName()+" b: "+economy.getBalance(player.getName()));
                economy.withdrawPlayer(player.getName(), count);
                plugin.getServer().getLogger().info("player: "+player.getName()+" a: "+economy.getBalance(player.getName()));
            }
        }
    }
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("auction_open") || command.getName().equalsIgnoreCase("auc")){
            if (player.hasPermission("auctionr1.command1")) {
                Open.Custom_auction.open_auction(player);
            } else {
                player.sendMessage(ChatColor.RED + "you haven't permission");
                return true;
            }
        }
        return true;
    }
}
