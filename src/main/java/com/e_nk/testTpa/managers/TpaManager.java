package com.e_nk.testTpa.managers;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TpaManager {

    private final TestTpa plugin;

    // requester → target
    private final HashMap<UUID, UUID> outgoing = new HashMap<>();
    // target → requester
    private final HashMap<UUID, UUID> incoming = new HashMap<>();

    private final int requestExpireSeconds;

    public TpaManager(TestTpa plugin) {
        this.plugin = plugin;
        this.requestExpireSeconds = plugin.getConfig().getInt("tpa_request_expiration_seconds");
    }

    public void sendTpaRequest(Player requester, Player target) {
        outgoing.put(requester.getUniqueId(), target.getUniqueId());
        incoming.put(target.getUniqueId(), requester.getUniqueId());

        requester.sendMessage("§aTPA request sent to " + target.getName());
        target.sendMessage("§e" + requester.getName() + " wants to teleport to you. /tpaccept or /tpdeny");

        // Auto-expire after X seconds
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (incoming.containsKey(target.getUniqueId()) &&
                    incoming.get(target.getUniqueId()).equals(requester.getUniqueId())) {

                cancel(requester);
                requester.sendMessage("§cYour TPA request to " + target.getName() + " expired.");
                target.sendMessage("§cTPA request from " + requester.getName() + " expired.");
            }
        }, requestExpireSeconds * 20L);
    }

    public boolean hasIncoming(Player target) {
        return incoming.containsKey(target.getUniqueId());
    }

    public Player getRequester(Player target) {
        UUID id = incoming.get(target.getUniqueId());
        return id != null ? plugin.getServer().getPlayer(id) : null;
    }

    public void accept(Player target) {
        UUID requesterId = incoming.get(target.getUniqueId());
        if (requesterId == null) return;

        Player requester = plugin.getServer().getPlayer(requesterId);
        if (requester == null) return;

        requester.sendMessage("§aYour TPA request was accepted!");
        target.sendMessage("§aYou accepted " + requester.getName() + "'s request.");

        // Clean up
        outgoing.remove(requesterId);
        incoming.remove(target.getUniqueId());
    }

    public void deny(Player target) {
        UUID requesterId = incoming.get(target.getUniqueId());
        if (requesterId == null) return;

        Player requester = plugin.getServer().getPlayer(requesterId);
        if (requester != null) {
            requester.sendMessage("§cYour TPA request was denied.");
        }

        target.sendMessage("§cRequest denied.");

        outgoing.remove(requesterId);
        incoming.remove(target.getUniqueId());
    }

    public void cancel(Player requester) {
        UUID targetId = outgoing.get(requester.getUniqueId());
        if (targetId == null) return;

        Player target = plugin.getServer().getPlayer(targetId);
        if (target != null) {
            target.sendMessage("§c" + requester.getName() + " cancelled their TPA request.");
        }

        requester.sendMessage("§cTPA request cancelled.");

        outgoing.remove(requester.getUniqueId());
        incoming.remove(targetId);
    }
}
