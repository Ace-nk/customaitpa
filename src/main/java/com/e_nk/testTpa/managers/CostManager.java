package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import com.e_nk.testTpa.util.DimensionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CostManager {

    private final TestTpa plugin;

    public CostManager(TestTpa plugin) {
        this.plugin = plugin;
    }

    public int calculateCost(Location from, Location to, boolean isBack) {

        double distance = from.distance(to);

        double scaler = plugin.getConfig().getDouble("teleport_cost.scaler");
        int minimum = plugin.getConfig().getInt("teleport_cost.minimum_price");

        String dim = DimensionUtil.getDimensionKey(from);

        double dimMult = plugin.getConfig().getDouble("teleport_cost.dimension_multipliers." + dim, 1.0);

        double cost = Math.sqrt(distance) * scaler * dimMult;

        int finalCost = (int) Math.ceil(cost);

        if (finalCost < minimum) finalCost = minimum;

        return finalCost;
    }

    public Material getCostItem(boolean isBack) {

        boolean customBack = plugin.getConfig().getBoolean("teleport_cost.back_uses_custom_item");

        if (isBack && customBack) {
            return Material.matchMaterial(plugin.getConfig().getString("teleport_cost.back_item"));
        }

        return Material.matchMaterial(plugin.getConfig().getString("teleport_cost.item"));
    }

    public boolean tryTakePayment(Player player, Material item, int amount) {
        return ItemSearch.removeItemsDeep(player, item, amount);
    }
}
