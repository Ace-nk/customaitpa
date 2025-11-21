package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class WarpManager {

    private final TestTpa plugin;
    private final File file;
    private final YamlConfiguration config;
    private final HashMap<String, Location> warps = new HashMap<>();

    public WarpManager(TestTpa plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "warps.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create warps.yml file!");
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        loadWarps();
    }

    public void addWarp(String name, Location loc) {
        warps.put(name.toLowerCase(), loc);
        saveWarps();
    }

    public void removeWarp(String name) {
        warps.remove(name.toLowerCase());
        config.set("warps." + name.toLowerCase(), null);
        saveConfig();
    }

    public Location getWarp(String name) {
        return warps.get(name.toLowerCase());
    }

    public HashMap<String, Location> getAllWarps() {
        return warps;
    }

    public void saveWarps() {
        try {
            for (String key : warps.keySet()) {
                config.set("warps." + key, warps.get(key));
            }
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save warps.yml!");
            e.printStackTrace();
        }
    }

    public void loadWarps() {
        if (!config.contains("warps")) return;

        for (String key : config.getConfigurationSection("warps").getKeys(false)) {
            Location loc = config.getLocation("warps." + key);
            if (loc != null) warps.put(key.toLowerCase(), loc);
        }
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save warps.yml!");
            e.printStackTrace();
        }
    }
}
