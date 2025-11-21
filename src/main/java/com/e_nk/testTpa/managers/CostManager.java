package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CostManager {

    private final TestTpa plugin;

    public CostManager(TestTpa plugin) {
        this.plugin = plugin;
    }

    public Material getCostItem(boolean isBackOrWarp) {
        return isBackOrWarp ? Material.ENDER_PEARL : Material.EMERALD;
    }

    public boolean tryTakePayment(Player player, Material item, int amount) {
        ItemStack stack = new ItemStack(item, amount);

        if (!player.getInventory().containsAtLeast(stack, amount)) return false;

        player.getInventory().removeItem(stack);
        return true;
    }

    public int calculateCost(org.bukkit.Location from, org.bukkit.Location to, boolean isBack) {
        if (isBack) return 1;
        double distance = from.distance(to);
        return Math.max(1, (int) Math.ceil(distance / 50.0));
    }
}
