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

        if (from == null || to == null) return plugin.getConfig().getInt("teleport_cost.minimum_price", 1);

        double distance = from.distance(to);

        double scaler = plugin.getConfig().getDouble("teleport_cost.scaler", 1.2);
        int minimum = plugin.getConfig().getInt("teleport_cost.minimum_price", 1);

        String dimKey = DimensionUtil.getDimensionKey(to);
        double dimMult = plugin.getConfig().getDouble("teleport_cost.dimension_multipliers." + dimKey, 1.0);

        double raw = (minimum + distance) * scaler * dimMult;
        int finalCost = (int) Math.ceil(raw);

        if (finalCost < minimum) finalCost = minimum;

        return finalCost;
    }

    public Material getCostItem(boolean isBack) {

        boolean customBack = plugin.getConfig().getBoolean("teleport_cost.back_uses_custom_item", false);

        String matStr;
        if (isBack && customBack) {
            matStr = plugin.getConfig().getString("teleport_cost.back_item", "COMPASS");
        } else {
            matStr = plugin.getConfig().getString("teleport_cost.item", "ENDER_PEARL");
        }

        Material m = Material.matchMaterial(matStr);
        if (m == null) {
            plugin.getLogger().warning("Invalid material in config: " + matStr + " — defaulting to ENDER_PEARL");
            return Material.ENDER_PEARL;
        }
        return m;
    }

    public boolean tryTakePayment(Player player, Material item, int amount) {
        if (player == null || item == null || amount <= 0) return false;
        return ItemSearch.removeItemsDeep(player, item, amount);
    }
}
