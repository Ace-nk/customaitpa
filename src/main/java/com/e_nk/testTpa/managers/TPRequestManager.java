package com.e_nk.testTpa.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TPRequestManager {

    private final HashMap<UUID, UUID> pending = new HashMap<>();
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();

    private final int requestTimeoutSeconds;
    private final int warmupSeconds;
    private final int cooldownSeconds;

    public TPRequestManager(int requestTimeoutSeconds, int warmupSeconds, int cooldownSeconds) {
        this.requestTimeoutSeconds = requestTimeoutSeconds;
        this.warmupSeconds = warmupSeconds;
        this.cooldownSeconds = cooldownSeconds;
    }

    /** ---------------------------------------------
     *  COOLDOWN SYSTEM
     *  --------------------------------------------- */

    public boolean isOnCooldown(Player player) {
        if (!cooldowns.containsKey(player.getUniqueId())) return false;
        return (System.currentTimeMillis() - cooldowns.get(player.getUniqueId())) <
                cooldownSeconds * 1000L;
    }

    public long getCooldownRemaining(Player player) {
        long used = System.currentTimeMillis() - cooldowns.get(player.getUniqueId());
        return cooldownSeconds - (used / 1000L);
    }

    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }

    /** ---------------------------------------------
     *  REQUEST SYSTEM
     *  --------------------------------------------- */

    public void createRequest(Player sender, Player target) {
        pending.put(target.getUniqueId(), sender.getUniqueId());

        sender.sendMessage("§aTpa request sent to §e" + target.getName());
        target.sendMessage("§e" + sender.getName() + " §ahas requested to teleport to you.");
        target.sendMessage("§aType §e/accept §aor §c/deny");

        // auto-expire
        Bukkit.getScheduler().runTaskLater(
                Bukkit.getPluginManager().getPlugin("TestTpa"),
                () -> {
                    if (pending.containsKey(target.getUniqueId()) &&
                            pending.get(target.getUniqueId()).equals(sender.getUniqueId())) {

                        pending.remove(target.getUniqueId());
                        sender.sendMessage("§cYour tpa request to " + target.getName() + " expired.");
                        target.sendMessage("§cTpa request from " + sender.getName() + " expired.");
                    }
                },
                requestTimeoutSeconds * 20L
        );
    }

    public boolean hasRequest(Player target) {
        return pending.containsKey(target.getUniqueId());
    }

    public Player getSender(Player target) {
        UUID uuid = pending.get(target.getUniqueId());
        return uuid == null ? null : Bukkit.getPlayer(uuid);
    }

    public void removeRequest(Player target) {
        pending.remove(target.getUniqueId());
    }

    public int getWarmupSeconds() {
        return warmupSeconds;
    }
}
