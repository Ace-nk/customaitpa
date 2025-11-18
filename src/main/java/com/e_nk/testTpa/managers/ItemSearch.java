package com.e_nk.testTpa.managers;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.List;

public class ItemSearch {

    public static int countItemsDeep(Player player, Material material) {

        int total = 0;

        // 1. Player inventory
        total += countInInventory(player.getInventory(), material);

        // 2. Ender chest
        total += countInInventory(player.getEnderChest(), material);

        return total;
    }

    private static int countInInventory(Inventory inv, Material material) {
        int count = 0;

        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;

            if (item.getType() == material) {
                count += item.getAmount();
            }

            if (item.getItemMeta() instanceof BlockStateMeta meta &&
                    meta.getBlockState() instanceof ShulkerBox box) {

                Inventory shulkerInventory = box.getInventory();
                count += countInInventory(shulkerInventory, material);
            }
        }
        return count;
    }

    public static boolean removeItemsDeep(Player player, Material material, int amount) {

        int total = countItemsDeep(player, material);
        if (total < amount) return false;

        int remaining = amount;

        // 1. Player inventory
        remaining = removeFromInventory(player.getInventory(), material, remaining);

        // 2. Ender chest
        if (remaining > 0) {
            remaining = removeFromInventory(player.getEnderChest(), material, remaining);
        }

        return remaining <= 0;
    }

    private static int removeFromInventory(Inventory inv, Material mat, int remaining) {

        for (int i = 0; i < inv.getSize(); i++) {

            ItemStack item = inv.getItem(i);
            if (item == null) continue;

            // Direct items
            if (item.getType() == mat) {
                int take = Math.min(item.getAmount(), remaining);
                item.setAmount(item.getAmount() - take);
                remaining -= take;
                if (item.getAmount() <= 0) inv.setItem(i, null);
                if (remaining <= 0) return 0;
            }

            // Shulker boxes
            if (item.getItemMeta() instanceof BlockStateMeta meta &&
                    meta.getBlockState() instanceof ShulkerBox box) {

                Inventory shulkerInv = box.getInventory();
                remaining = removeFromInventory(shulkerInv, mat, remaining);

                meta.setBlockState(box);
                item.setItemMeta(meta);
                inv.setItem(i, item);

                if (remaining <= 0) return 0;
            }
        }

        return remaining;
    }
}
