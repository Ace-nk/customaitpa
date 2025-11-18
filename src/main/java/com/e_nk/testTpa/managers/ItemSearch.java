package com.e_nk.testTpa.managers;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import org.bukkit.inventory.meta.BlockStateMeta;

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

        if (inv == null) return 0;

        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;

            if (item.getType() == material) {
                count += item.getAmount();
            }

            if (item.hasItemMeta() && item.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                if (meta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox box = (ShulkerBox) meta.getBlockState();
                    Inventory shInv = box.getInventory();
                    count += countInInventory(shInv, material);
                }
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
        if (inv == null) return remaining;

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
            if (item.hasItemMeta() && item.getItemMeta() instanceof BlockStateMeta) {
                BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
                if (meta.getBlockState() instanceof ShulkerBox) {
                    ShulkerBox box = (ShulkerBox) meta.getBlockState();
                    Inventory shulkerInv = box.getInventory();

                    int before = remaining;
                    remaining = removeFromInventory(shulkerInv, mat, remaining);

                    // write back modified shulker contents
                    meta.setBlockState(box);
                    item.setItemMeta(meta);
                    inv.setItem(i, item);

                    if (remaining <= 0) return 0;
                }
            }
        }

        return remaining;
    }
}
