package ru.femboypig.flow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.femboypig.flow.Flow;

public class IgnoreCommand implements CommandExecutor {
    private final Flow plugin;

    public IgnoreCommand(Flow plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only for players!");
            return true;
        }

        if (args.length != 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /ignore <player>");
            return true;
        }

        Player player = (Player) sender;
        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found!");
            return true;
        }

        if (target == player) {
            sender.sendMessage(ChatColor.RED + "You cannot ignore yourself!");
            return true;
        }

        if (plugin.getIgnoreManager().isIgnored(target, player)) {
            plugin.getIgnoreManager().unignorePlayer(player, target);
            sender.sendMessage(ChatColor.GREEN + "You are no longer ignoring " + target.getName());
        } else {
            plugin.getIgnoreManager().ignorePlayer(player, target);
            sender.sendMessage(ChatColor.GREEN + "You are now ignoring " + target.getName());
        }

        return true;
    }
} 