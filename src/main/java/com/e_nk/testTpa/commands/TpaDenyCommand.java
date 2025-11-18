package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaDenyCommand implements CommandExecutor {

    private final TestTpa plugin;

    public TpaDenyCommand(TestTpa plugin) {
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

        plugin.getTpaManager().deny(target);
        return true;
    }
}
