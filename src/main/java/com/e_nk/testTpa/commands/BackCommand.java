package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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

        // Fixed cost for /back
        int cost = 1;
        var item = plugin.getCostManager().getCostItem(true);

        if (!plugin.getCostManager().tryTakePayment(p, item, cost)) {
            p.sendMessage("§cYou need 1 " + item + " to use /back.");
            return true;
        }

        p.teleport(loc);
        p.sendMessage("§aTeleported back.");
        plugin.getTimerManager().applyCooldown(p);

        return true;
    }
}
