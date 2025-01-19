package ru.femboypig.flow.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import ru.femboypig.flow.Flow;

public class FlowReloadCommand implements CommandExecutor {
    private final Flow plugin;

    public FlowReloadCommand(Flow plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("flow.reload")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
        }

        plugin.reloadConfig();
        plugin.getChatManager().loadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration successfully reloaded!");
        return true;
    }
}