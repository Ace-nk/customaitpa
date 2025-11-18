package com.e_nk.testTpa.managers;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class BackManager {

    private final File file;
    private final YamlConfiguration config;
    private final HashMap<UUID, Location> cache = new HashMap<>();

    public BackManager(File pluginFolder) {
        this.file = new File(pluginFolder, "back.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        loadAll();
    }

    /** ---------------------------------------------
     *  SAVE & LOAD SYSTEM
     *  --------------------------------------------- */

    private void loadAll() {
        if (!config.contains("back")) return;

        for (String key : config.getConfigurationSection("back").getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            Location loc = config.getLocation("back." + key);
            if (loc != null) cache.put(uuid, loc);
        }
    }

    private void save() {
        try {
            for (UUID uuid : cache.keySet()) {
                config.set("back." + uuid.toString(), cache.get(uuid));
            }
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** ---------------------------------------------
     *  API
     *  --------------------------------------------- */

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


