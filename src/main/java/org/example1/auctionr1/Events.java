package org.example1.auctionr1;


import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.example1.auctionr1.Commands.Get;
import org.example1.auctionr1.Commands.Open;
import org.example1.auctionr1.Commands.Time;

import java.util.List;
import java.util.Objects;

public class Events implements Listener {
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
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        Get.Custom_get.join_p(player);
    }
    @EventHandler
    public void onInteract(InventoryClickEvent e) {
        final Player clicker = (Player) e.getWhoClicked();
        String title = e.getView().getTitle();
        if(e.getClick() == ClickType.DOUBLE_CLICK ){
            e.setCancelled(true);
        }
        if(e.getSlotType()== InventoryType.SlotType.CONTAINER && e.getClickedInventory().getType() != InventoryType.PLAYER) {
            ItemStack i_item = e.getClickedInventory().getItem(e.getSlot());
            if (i_item != null && i_item.getType() != Material.AIR && i_item.hasItemMeta() && i_item.getItemMeta().hasDisplayName()) {
                if (Objects.equals(i_item.getItemMeta().getDisplayName(), (ChatColor.BLACK + " "))) {
                    e.setCancelled(true);
                }
            }
            if (title.equals(ChatColor.YELLOW + "Action")) {
                if (i_item != null && i_item.getType() != Material.AIR && i_item.hasItemMeta()) {
                    if (i_item.getItemMeta().hasDisplayName() && Objects.equals(i_item.getItemMeta().getDisplayName(), (ChatColor.GREEN + " "))) {
                        Open.Custom_auction.ch_page(clicker, e.getSlot());
                        e.setCancelled(true);
                    } else if (i_item.hasItemMeta() && i_item.getItemMeta().hasLore()) {
                        List<String> lore = i_item.getItemMeta().getLore();
                        if (lore.contains(ChatColor.GREEN + "item_selling")) {
                            e.setCancelled(true);
                            int i = e.getSlot() - 9;
                            Time.Custom_sell.pre_open_item(clicker, i);
                        }
                    }
                }
            } else if (title.equals(ChatColor.YELLOW + "Action Item")) {
                if (i_item != null && i_item.getType() != Material.AIR && i_item.hasItemMeta()) {
                    if (i_item.getItemMeta().hasDisplayName() && Objects.equals(i_item.getItemMeta().getDisplayName(), (ChatColor.GREEN + " "))) {
                        Time.Custom_sell.ch_b(clicker, e.getSlot());
                        e.setCancelled(true);
                    } else if (i_item.hasItemMeta() && i_item.getItemMeta().hasLore()) {
                        List<String> lore = i_item.getItemMeta().getLore();
                        if (lore.contains(ChatColor.GREEN + "item_selling")) {
                            e.setCancelled(true);
                            int i = e.getSlot() - 9;
                            Time.Custom_sell.pre_open_item(clicker, i);
                        }
                    }
                }
            } else if (title.equals(ChatColor.YELLOW + "Your Action Inventory")) {
                if (i_item != null && i_item.getType() != Material.AIR && i_item.hasItemMeta()) {
                    if (i_item.getItemMeta().hasDisplayName() && Objects.equals(i_item.getItemMeta().getDisplayName(), (ChatColor.GREEN + "Collect Money"))) {
                        Get.Custom_get.get_money(clicker);
                        e.setCancelled(true);
                    } else if (i_item.hasItemMeta() && i_item.getItemMeta().hasLore()) {
                        List<String> lore = i_item.getItemMeta().getLore();
                        if (lore.contains(ChatColor.GREEN + "auction_item")) {
                            if (e.getSlot() == 3 || e.getSlot() == 4 || e.getSlot() == 5){
                                Get.Custom_get.get_item(clicker,(e.getSlot()-3));
                            }
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}
