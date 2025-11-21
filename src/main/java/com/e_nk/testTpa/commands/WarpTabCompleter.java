package com.e_nk.testTpa.commands;

import com.e_nk.testTpa.TestTpa;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class WarpTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        List<String> list = new ArrayList<>();

        // /warp <subcommand>
        if (args.length == 1) {
            list.add("create");
            list.add("delete");
            list.add("to");
            return list;
        }

        // /warp create|delete|to <warpname>
        if (args.length == 2) {

            // USE WarpManager.getAllWarps()  (your actual method)
            list.addAll(
                    TestTpa.getInstance()
                            .getWarpManager()
                            .getAllWarps()
                            .keySet()
            );

            return list;
        }

        return list;
    }
}
