package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    private final TestTpa plugin;

    public BackCommand(TestTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (plugin.getTimerManager().hasCooldown(p)) {
            p.sendMessage("§cYou are on cooldown: " + plugin.getTimerManager().getRemainingCooldown(p) + "s");
            return true;
        }

        var loc = plugin.getBackManager().getBackLocation(p);
        if (loc == null) {
            p.sendMessage("§cNo saved death location.");
            return true;
        }

        int cost = 1; // Always 1 for /back
        Material item = plugin.getCostManager().getCostItem(true);

        if (!plugin.getCostManager().tryTakePayment(p, item, cost)) {
            p.sendMessage("§cYou need 1 " + item + " to use /back.");
            return true; // stop teleport if payment fails
        }

        p.teleport(loc);
        p.sendMessage("§aTeleported back.");
        plugin.getTimerManager().applyCooldown(p);

        return true;
    }
}
