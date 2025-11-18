package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TimerManager {

    private final TestTpa plugin;

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final HashMap<UUID, Long> warmups = new HashMap<>();

    private final long warmupSeconds;
    private final long cooldownSeconds;

    public TimerManager(TestTpa plugin) {
        this.plugin = plugin;
        this.warmupSeconds = plugin.getConfig().getLong("teleport_timers.warmup_seconds", 3);
        this.cooldownSeconds = plugin.getConfig().getLong("teleport_timers.cooldown_seconds", 10);
    }

    public boolean hasCooldown(Player player) {
        Long t = cooldowns.get(player.getUniqueId());
        return t != null && System.currentTimeMillis() < t;
    }

    public long getRemainingCooldown(Player player) {
        Long t = cooldowns.get(player.getUniqueId());
        if (t == null) return 0L;
        long remaining = (t - System.currentTimeMillis()) / 1000;
        return Math.max(0L, remaining);
    }

    public void applyCooldown(Player player) {
        cooldowns.put(player.getUniqueId(),
                System.currentTimeMillis() + cooldownSeconds * 1000L);
    }

    public void startWarmup(Player player) {
        warmups.put(player.getUniqueId(),
                System.currentTimeMillis() + warmupSeconds * 1000L);
    }

    // Safe: returns true if warmup either not present (done) or time passed
    public boolean isWarmupDone(Player player) {
        Long t = warmups.get(player.getUniqueId());
        if (t == null) return true; // not in warmup, so considered done
        return System.currentTimeMillis() >= t;
    }

    public long getWarmupRemaining(Player player) {
        Long t = warmups.get(player.getUniqueId());
        if (t == null) return 0L;
        long remaining = (t - System.currentTimeMillis()) / 1000;
        return Math.max(0L, remaining);
    }

    public void cancelWarmup(Player player) {
        warmups.remove(player.getUniqueId());
    }
}
