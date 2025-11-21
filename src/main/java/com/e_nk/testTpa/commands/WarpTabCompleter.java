package com.e_nk.testTpa.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (!(sender instanceof Player)) return completions;

        switch (args.length) {
            case 1 -> {
                List<String> options = List.of("to", "create", "delete");
                for (String opt : options) {
                    if (opt.startsWith(args[0].toLowerCase())) completions.add(opt);
                }
            }
            case 2 -> {
                if (args[0].equalsIgnoreCase("to") || args[0].equalsIgnoreCase("delete")) {
                    for (String warp : WarpCommand.getWarps().keySet()) {
                        if (warp.toLowerCase().startsWith(args[1].toLowerCase())) completions.add(warp);
                    }
                }
            }
        }
        return completions;
    }
}
