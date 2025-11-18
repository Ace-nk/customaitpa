package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaCancelCommand implements CommandExecutor {

    private final TestTpa plugin;

    public TpaCancelCommand(TestTpa plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Players only.");
            return true;
        }

        plugin.getTpaManager().cancel(p);
        return true;
    }
}
