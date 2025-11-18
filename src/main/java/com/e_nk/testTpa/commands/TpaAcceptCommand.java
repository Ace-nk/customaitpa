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
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player target)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (!plugin.getTpaManager().hasIncoming(target)) {
            target.sendMessage("§cNo pending TPA requests.");
            return true;
        }

        Player requester = plugin.getTpaManager().getRequester(target);
        if (requester == null) {
            target.sendMessage("§cThe requester is offline.");
            plugin.getTpaManager().deny(target);
            return true;
        }

        // mark request accepted
        plugin.getTpaManager().accept(target);

        int warmup = plugin.getTimerManager().getWarmupRemaining(requester) > 0 ?
                (int) plugin.getTimerManager().getWarmupRemaining(requester) :
                plugin.getConfig().getInt("teleport_timers.warmup_seconds");

        requester.sendMessage("§eTeleporting in §c" + warmup + "§e seconds. Don't move!");

        plugin.getTimerManager().startWarmup(requester);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!plugin.getTimerManager().isWarmupDone(requester)) return;

                requester.teleport(target.getLocation());
                requester.sendMessage("§aTeleported!");
                plugin.getTimerManager().applyCooldown(requester);
            }
        }.runTaskLater(plugin, warmup * 20L);

        return true;
    }
}
