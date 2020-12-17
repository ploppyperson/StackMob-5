package uk.antiperson.stackmob.utils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class EntityUtils {

    public static boolean isDye(ItemStack material) {
        return material.getType().toString().endsWith("_DYE");
    }

    public static void removeHandItem(Player player, int itemAmount) {
        if (itemAmount == player.getInventory().getItemInMainHand().getAmount()) {
           player.getInventory().setItemInMainHand(null);
           return;
        }
        ItemStack is = player.getInventory().getItemInMainHand();
        is.setAmount(is.getAmount() - itemAmount);
        player.getInventory().setItemInMainHand(is);
    }


}
