package com.e_nk.testTpa.util;

import org.bukkit.Location;
import org.bukkit.World;

public class DimensionUtil {

    // Return normalized keys to match config: overworld | nether | end
    public static String getDimensionKey(Location loc) {
        if (loc == null || loc.getWorld() == null) return "overworld";
        World.Environment env = loc.getWorld().getEnvironment();
        switch (env) {
            case NETHER: return "nether";
            case THE_END: return "end";
            default: return "overworld";
        }
    }
}
