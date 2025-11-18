package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WarpManager {

    private final TestTpa plugin;
    private final HashMap<String, Location> warps = new HashMap<>();

    private final File warpFile;
    private YamlConfiguration warpConfig;

    public WarpManager(TestTpa plugin) {
        this.plugin = plugin;
        this.warpFile = new File(plugin.getDataFolder(), plugin.getConfig().getString("storage.warp_file"));
        loadWarps();
    }

    public void setWarp(String name, Location loc) {
        warps.put(name.toLowerCase(), loc);
        saveWarps();
    }

    public void deleteWarp(String name) {
        warps.remove(name.toLowerCase());
        saveWarps();
    }

    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    private void loadWarps() {
        if (!warpFile.exists()) return;

        warpConfig = YamlConfiguration.loadConfiguration(warpFile);

        for (String name : warpConfig.getKeys(false)) {
            Location loc = warpConfig.getLocation(name);
            if (loc != null) warps.put(name, loc);
        }
    }

    public void saveWarps() {
        warpConfig = new YamlConfiguration();

        for (String name : warps.keySet()) {
            warpConfig.set(name, warps.get(name));
        }

        try {
            warpConfig.save(warpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
