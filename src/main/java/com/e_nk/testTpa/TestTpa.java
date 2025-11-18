package com.e_nk.testTpa;

import com.e_nk.testTpa.managers.*;
import com.e_nk.testTpa.listeners.*;
import com.e_nk.testTpa.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class TestTpa extends JavaPlugin {

    private static TestTpa instance;

    // Managers
    private TimerManager timerManager;
    private TpaManager tpaManager;
    private WarpManager warpManager;
    private BackManager backManager;
    private CostManager costManager;

    public static TestTpa getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        // Initialize managers
        timerManager = new TimerManager(this);
        tpaManager = new TpaManager(this);
        warpManager = new WarpManager(this);
        costManager = new CostManager(this);
        backManager = new BackManager(getDataFolder());

        // Register commands
        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpahere").setExecutor(new TpaHereCommand(this));
        getCommand("tpaccept").setExecutor(new TpaAcceptCommand(this));
        getCommand("tpdeny").setExecutor(new TpaDenyCommand(this));
        getCommand("tpacancel").setExecutor(new TpaCancelCommand(this));
        getCommand("warp").setExecutor(new WarpCommand(this));
        getCommand("back").setExecutor(new BackCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
        getServer().getPluginManager().registerEvents(new MovementCancelWarmupListener(this), this);

        getLogger().info("TestTpa enabled successfully.");
    }

    @Override
    public void onDisable() {
        if (warpManager != null) warpManager.saveWarps();
        if (backManager != null) backManager.saveBackLocations();
        getLogger().info("TestTpa disabled.");
    }

    public TimerManager getTimerManager() { return timerManager; }
    public TpaManager getTpaManager() { return tpaManager; }
    public WarpManager getWarpManager() { return warpManager; }
    public BackManager getBackManager() { return backManager; }
    public CostManager getCostManager() { return costManager; }
}
