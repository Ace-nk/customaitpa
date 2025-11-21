package com.e_nk.testTpa.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class WarpCommand implements CommandExecutor {

    // Global warp storage: Name -> Location
    private static final HashMap<String, Location> warps = new HashMap<>();
    private static final double CROSS_WORLD_COST = 100; // Example flat cost for cross-world warp

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use warps.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6Warp commands: /warp to, /warp create, /warp delete");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "to":
                if (args.length == 1) {
                    player.sendMessage("§6Available warps: §e" + String.join(", ", warps.keySet()));
                    return true;
                }

                String warpName = args[1];
                if (!warps.containsKey(warpName)) {
                    player.sendMessage("§cWarp does not exist.");
                    return true;
                }

                Location targetLoc = warps.get(warpName);

                // Example cost handling (ignores cross-world distance)
                double cost;
                if (!player.getWorld().equals(targetLoc.getWorld())) {
                    cost = CROSS_WORLD_COST;
                } else {
                    cost = player.getLocation().distance(targetLoc);
                }

                player.teleport(targetLoc);
                player.sendMessage("§aWarped to §e" + warpName + " §7(Cost: " + (int) cost + ")");
                break;

            case "create":
                if (args.length != 2) {
                    player.sendMessage("Usage: /warp create <name>");
                    return true;
                }
                String newWarp = args[1];
                warps.put(newWarp, player.getLocation());
                player.sendMessage("§aWarp §e" + newWarp + " §acreated!");
                break;

            case "delete":
                if (args.length != 2) {
                    player.sendMessage("Usage: /warp delete <name>");
                    return true;
                }
                String deleteWarp = args[1];
                if (warps.remove(deleteWarp) != null) {
                    player.sendMessage("§cWarp §e" + deleteWarp + " §cremoved!");
                } else {
                    player.sendMessage("§cWarp does not exist.");
                }
                break;

            default:
                player.sendMessage("§cUnknown warp command.");
                break;
        }

        return true;
    }

    public static HashMap<String, Location> getWarps() {
        return warps;
    }
}
