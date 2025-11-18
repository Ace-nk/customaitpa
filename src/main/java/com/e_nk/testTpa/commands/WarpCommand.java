package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final TestTpa plugin;

    public WarpCommand(TestTpa plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length < 1) {
            p.sendMessage("§cUsage: /warp <create|delete|to> <name>");
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "create" -> {
                if (args.length < 2) {
                    p.sendMessage("§c/warp create <name>");
                    return true;
                }
                plugin.getWarpManager().setWarp(args[1], p.getLocation());
                p.sendMessage("§aWarp created.");
            }

            case "delete" -> {
                if (args.length < 2) {
                    p.sendMessage("§c/warp delete <name>");
                    return true;
                }
                plugin.getWarpManager().deleteWarp(args[1]);
                p.sendMessage("§aWarp deleted.");
            }

            case "to" -> {
                if (args.length < 2) {
                    p.sendMessage("§c/warp to <name>");
                    return true;
                }

                Location warpLoc = plugin.getWarpManager().getWarp(args[1]);
                if (warpLoc == null) {
                    p.sendMessage("§cWarp does not exist.");
                    return true;
                }

                int cost = plugin.getCostManager().calculateCost(p.getLocation(), warpLoc, false);
                var item = plugin.getCostManager().getCostItem(false);

                if (!plugin.getCostManager().tryTakePayment(p, item, cost)) {
                    p.sendMessage("§cYou need " + cost + " " + item + ".");
                    return true;
                }

                p.teleport(warpLoc);
                p.sendMessage("§aWarped!");
            }

            default -> p.sendMessage("§cUnknown subcommand.");
        }

        return true;
    }
}
