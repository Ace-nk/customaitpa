package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final TestTpa plugin;

    public TpaCommand(TestTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage("§cUsage: /tpa <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null || !target.isOnline()) {
            p.sendMessage("§cThat player is not online.");
            return true;
        }

        if (target == p) {
            p.sendMessage("§cYou cannot tpa to yourself.");
            return true;
        }

        plugin.getTpaManager().sendTpaRequest(p, target);
        return true;
    }
}
