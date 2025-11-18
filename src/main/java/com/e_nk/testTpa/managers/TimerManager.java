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
        this.warmupSeconds = plugin.getConfig().getInt("teleport_timers.warmup_seconds");
        this.cooldownSeconds = plugin.getConfig().getInt("teleport_timers.cooldown_seconds");
    }

    public boolean hasCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId()) &&
                System.currentTimeMillis() < cooldowns.get(player.getUniqueId());
    }

    public long getRemainingCooldown(Player player) {
        return (cooldowns.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
    }

    public void applyCooldown(Player player) {
        cooldowns.put(player.getUniqueId(),
                System.currentTimeMillis() + cooldownSeconds * 1000);
    }

    public void startWarmup(Player player) {
        warmups.put(player.getUniqueId(),
                System.currentTimeMillis() + warmupSeconds * 1000);
    }

    public boolean isWarmupDone(Player player) {
        return !warmups.containsKey(player.getUniqueId()) ||
                System.currentTimeMillis() >= warmups.get(player.getUniqueId());
    }

    public long getWarmupRemaining(Player player) {
        return (warmups.get(player.getUniqueId()) - System.currentTimeMillis()) / 1000;
    }

    public void cancelWarmup(Player player) {
        warmups.remove(player.getUniqueId());
    }
}
