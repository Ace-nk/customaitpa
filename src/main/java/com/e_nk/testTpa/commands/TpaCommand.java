package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final TestTpa plugin;

    public TpaCommand() {
        this.plugin = TestTpa.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("§cUsage: /" + label + " <player>");
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            p.sendMessage("§cPlayer not found!");
            return true;
        }

        if (plugin.getTimerManager().hasCooldown(p)) {
            p.sendMessage("§cYou are on cooldown: " + plugin.getTimerManager().getRemainingCooldown(p) + "s");
            return true;
        }

        int cost = plugin.getCostManager().calculateCost(p.getLocation(), target.getLocation(), false);
        Material item = plugin.getCostManager().getCostItem(false);

        if (!plugin.getCostManager().tryTakePayment(p, item, cost)) {
            p.sendMessage("§cYou need " + cost + " " + item + " to send a TPA request!");
            return true;
        }

        plugin.getTpaManager().sendTpaRequest(p, target);
        p.sendMessage("§aTPA request sent to §e" + target.getName() + "§a!");
        plugin.getTimerManager().applyCooldown(p);
        return true;
    }
}
