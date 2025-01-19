package ru.femboypig.flow.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.femboypig.flow.Flow;

import java.util.UUID;

public class ReplyCommand implements CommandExecutor {
    private final Flow plugin;

    public ReplyCommand(Flow plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command is only for players!");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /r <message>");
            return true;
        }

        Player player = (Player) sender;
        UUID lastSenderUUID = plugin.getChatManager().getLastMessageSender(player);

        if (lastSenderUUID == null) {
            sender.sendMessage(ChatColor.RED + "Nobody to reply to!");
            return true;
        }

        Player target = Bukkit.getPlayer(lastSenderUUID);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player is offline!");
            return true;
        }

        StringBuilder message = new StringBuilder();
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        plugin.getChatManager().sendPrivateMessage(player, target, message.toString().trim());
        return true;
    }
}