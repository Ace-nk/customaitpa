package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import com.e_nk.testTpa.managers.WarpManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final TestTpa plugin;

    public WarpCommand() {
        this.plugin = TestTpa.getInstance();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("§aWarp commands: /warp create <name>, /warp delete <name>, /warp to <name>");
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {
            case "create" -> {
                if (args.length < 2) {
                    p.sendMessage("§cUsage: /warp create <name>");
                    return true;
                }
                String name = args[1];
                plugin.getWarpManager().addWarp(name, p.getLocation());
                p.sendMessage("§aWarp §e" + name + " §acreated!");
            }
            case "delete" -> {
                if (args.length < 2) {
                    p.sendMessage("§cUsage: /warp delete <name>");
                    return true;
                }
                String name = args[1];
                plugin.getWarpManager().removeWarp(name);
                p.sendMessage("§aWarp §e" + name + " §adeleted!");
            }
            case "to" -> {
                if (args.length < 2) {
                    p.sendMessage("§cUsage: /warp to <name>");
                    return true;
                }
                String name = args[1];
                Location loc = plugin.getWarpManager().getWarp(name);
                if (loc == null) {
                    p.sendMessage("§cWarp §e" + name + " §cdoes not exist!");
                    return true;
                }

                if (plugin.getTimerManager().hasCooldown(p)) {
                    p.sendMessage("§cYou are on cooldown: " + plugin.getTimerManager().getRemainingCooldown(p) + "s");
                    return true;
                }

                int cost = plugin.getCostManager().calculateCost(p.getLocation(), loc, false);
                Material item = plugin.getCostManager().getCostItem(false);

                if (!plugin.getCostManager().tryTakePayment(p, item, cost)) {
                    p.sendMessage("§cYou need " + cost + " " + item + " to warp!");
                    return true;
                }

                p.teleport(loc);
                p.sendMessage("§aWarped to §e" + name + "§a!");
                plugin.getTimerManager().applyCooldown(p);
            }
            default -> p.sendMessage("§cUnknown warp command. Use /warp create, delete, or to.");
        }

        return true;
    }
}
