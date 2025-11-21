package com.e_nk.testTpa.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TpaCommand implements CommandExecutor {

    // Key: Target UUID, Value: Sender UUID
    private static final HashMap<UUID, UUID> pendingRequests = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can run this command!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6Available TPA commands: /tpa <player>, /tpahere <player>, /tpaaccept, /tpadeny");
            return true;
        }

        switch (cmd.getName().toLowerCase()) {
            case "tpa":
                if (args.length != 1) return false;
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null || !target.isOnline()) {
                    player.sendMessage("§cPlayer not found!");
                    return true;
                }
                pendingRequests.put(target.getUniqueId(), player.getUniqueId());
                player.sendMessage("§aTeleport request sent to " + target.getName());
                target.sendMessage("§e" + player.getName() + " wants to teleport to you! Use /tpaaccept or /tpadeny");
                break;

            case "tpahere":
                if (args.length != 1) return false;
                Player targetHere = Bukkit.getPlayer(args[0]);
                if (targetHere == null || !targetHere.isOnline()) {
                    player.sendMessage("§cPlayer not found!");
                    return true;
                }
                pendingRequests.put(targetHere.getUniqueId(), player.getUniqueId());
                player.sendMessage("§aTeleport request sent to " + targetHere.getName());
                targetHere.sendMessage("§e" + player.getName() + " wants you to teleport to them! Use /tpaaccept or /tpadeny");
                break;

            case "tpaaccept":
                if (!pendingRequests.containsKey(player.getUniqueId())) {
                    player.sendMessage("§cNo pending teleport requests!");
                    return true;
                }
                UUID senderId = pendingRequests.get(player.getUniqueId());
                Player requestSender = Bukkit.getPlayer(senderId);
                if (requestSender != null && requestSender.isOnline()) {
                    requestSender.teleport(player.getLocation());
                    requestSender.sendMessage("§aTeleport accepted!");
                    player.sendMessage("§aYou accepted the teleport request!");
                } else {
                    player.sendMessage("§cRequest sender is not online.");
                }
                pendingRequests.remove(player.getUniqueId());
                break;

            case "tpadeny":
                if (!pendingRequests.containsKey(player.getUniqueId())) {
                    player.sendMessage("§cNo pending teleport requests!");
                    return true;
                }
                pendingRequests.remove(player.getUniqueId());
                player.sendMessage("§cTeleport request denied!");
                break;

            default:
                return false;
        }

        return true;
    }
}
