package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class BackManager {

    private final TestTpa plugin;
    private final File file;
    private final YamlConfiguration config;
    private final HashMap<UUID, Location> cache = new HashMap<>();

    public BackManager(TestTpa plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "back.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create back.yml file!");
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        loadAll();
    }

    private void loadAll() {
        if (!config.contains("back")) return;

        for (String key : config.getConfigurationSection("back").getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                Location loc = config.getLocation("back." + key);
                if (loc != null) cache.put(uuid, loc);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in back.yml: " + key);
            }
        }
    }

    private void save() {
        try {
            for (UUID uuid : cache.keySet()) {
                config.set("back." + uuid.toString(), cache.get(uuid));
            }
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save back locations!");
            e.printStackTrace();
        }
    }

    public void setBackLocation(Player player, Location location) {
        cache.put(player.getUniqueId(), location);
        save();
    }

    public Location getBackLocation(Player player) {
        return cache.get(player.getUniqueId());
    }

    public void saveBackLocations() {
        save();
    }
}
