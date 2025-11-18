package com.e_nk.testTpa.util;

import org.bukkit.Location;

public class DimensionUtil {

    public static String getDimensionKey(Location loc) {
        return loc.getWorld().getName(); // "world", "world_nether", "world_the_end"
    }
}
