package com.e_nk.testTpa.listeners;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementCancelWarmupListener implements Listener {

    private final TestTpa plugin;

    public MovementCancelWarmupListener(TestTpa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

        if (!plugin.getTimerManager().isWarmupDone(event.getPlayer())) {
            if (event.getFrom().distance(event.getTo()) > 0.1) {
                plugin.getTimerManager().cancelWarmup(event.getPlayer());
                event.getPlayer().sendMessage("§cTeleport cancelled — you moved!");
            }
        }
    }
}
