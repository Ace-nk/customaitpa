package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TpaAcceptCommand implements CommandExecutor {

    private final TestTpa plugin;

    public TpaAcceptCommand(TestTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player target)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (!plugin.getTpaManager().hasIncoming(target)) {
            target.sendMessage("§cYou have no pending teleport requests.");
            return true;
        }

        Player requester = plugin.getTpaManager().getRequester(target);
        if (requester == null || !requester.isOnline()) {
            target.sendMessage("§cRequester is offline.");
            plugin.getTpaManager().deny(target);
            return true;
        }

        // Accept and clean up maps
        plugin.getTpaManager().accept(target);

        int warmup = (int) plugin.getTimerManager().getWarmupRemaining(requester);
        if (warmup <= 0) warmup = (int) plugin.getConfig().getLong("teleport_timers.warmup_seconds", 3);

        requester.sendMessage("§eTeleporting in §c" + warmup + "§e seconds. Don't move!");
        plugin.getTimerManager().startWarmup(requester);

        // schedule teleport after warmup seconds
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!requester.isOnline()) return;
                if (!plugin.getTimerManager().isWarmupDone(requester)) {
                    // not yet done; reschedule a short delay to check later
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // do nothing here; warmup will be handled in the next check
                        }
                    }.runTaskLater(plugin, 5L);
                    return;
                }
                // perform teleport
                requester.teleport(target.getLocation());
                requester.sendMessage("§aTeleported!");
                plugin.getTimerManager().applyCooldown(requester);
            }
        }.runTaskLater(plugin, warmup * 20L);

        return true;
    }
}
