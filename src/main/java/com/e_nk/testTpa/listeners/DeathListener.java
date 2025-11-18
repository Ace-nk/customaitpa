package com.e_nk.testTpa.listeners;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final TestTpa plugin;

    public DeathListener(TestTpa plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        plugin.getBackManager().setBackLocation(e.getEntity(), e.getEntity().getLocation());
    }
}
