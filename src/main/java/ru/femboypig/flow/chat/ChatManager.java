package ru.femboypig.flow.chat;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.femboypig.flow.Flow;
import ru.femboypig.flow.utils.MessageFormatter;

import java.util.*;

public class ChatManager {
    private final Flow plugin;
    private final Map<UUID, UUID> lastMessageSender;
    private final MessageFormatter formatter;
    private int localChatRadius;
    private String globalChatPrefix;
    private String localFormat;
    private String globalFormat;
    private String privateOutgoingFormat;
    private String privateIncomingFormat;

    public ChatManager(Flow plugin) {
        this.plugin = plugin;
        this.lastMessageSender = new HashMap<>();
        this.formatter = new MessageFormatter(plugin);
        loadConfig();
    }

    public void loadConfig() {
        this.localChatRadius = plugin.getConfig().getInt("local-chat-radius", 100);
        this.globalChatPrefix = plugin.getConfig().getString("global-chat-prefix", "!");
        this.localFormat = plugin.getConfig().getString("format.local", "%luckperms_prefix%%player_name% &8→ &f%message%");
        this.globalFormat = plugin.getConfig().getString("format.global", "&6[G] %luckperms_prefix%%player_name% &8→ &f%message%");
        this.privateOutgoingFormat = plugin.getConfig().getString("format.private.outgoing", "&d→ &7%recipient%&8: &f%message%");
        this.privateIncomingFormat = plugin.getConfig().getString("format.private.incoming", "&dFrom &7%sender%&8: &f%message%");
    }

    public void handleChat(Player sender, String message) {
        // Check for spam
        if (plugin.getAntiSpamManager().isSpamming(sender)) {
            sender.sendMessage(ChatColor.RED + "Please wait before sending another message!");
            return;
        }

        // Filter banned words
        if (plugin.getChatFilter().containsBannedWords(message) && !sender.hasPermission("flow.bypass.filter")) {
            sender.sendMessage(ChatColor.RED + "Your message contains banned words!");
            return;
        }
        
        // Process message
        String processedMessage = message;
        
        // Apply filter
        if (!sender.hasPermission("flow.bypass.filter")) {
            processedMessage = plugin.getChatFilter().filterMessage(processedMessage);
        }
        
        // Process emojis
        processedMessage = plugin.getEmojiManager().replaceEmojis(processedMessage, sender);
        
        // Process mentions
        processedMessage = plugin.getMentionManager().processMentions(processedMessage, sender);

        // Determine message type and send
        if (processedMessage.startsWith(globalChatPrefix)) {
            sendGlobalMessage(sender, processedMessage.substring(globalChatPrefix.length()).trim());
        } else {
            sendLocalMessage(sender, processedMessage);
        }
    }

    public void sendLocalMessage(Player sender, String message) {
        TextComponent component = formatter.formatMessage(sender, message, localFormat);

        sender.getNearbyEntities(localChatRadius, localChatRadius, localChatRadius).stream()
                .filter(entity -> entity instanceof Player)
                .map(entity -> (Player) entity)
                .filter(player -> !plugin.getIgnoreManager().isIgnored(sender, player))
                .forEach(player -> player.spigot().sendMessage(component));
        
        sender.spigot().sendMessage(component);
    }

    public void sendGlobalMessage(Player sender, String message) {
        TextComponent component = formatter.formatMessage(sender, message, globalFormat);
        
        plugin.getServer().getOnlinePlayers().stream()
                .filter(player -> !plugin.getIgnoreManager().isIgnored(sender, player))
                .forEach(player -> player.spigot().sendMessage(component));
    }

    public void sendPrivateMessage(Player sender, Player recipient, String message) {
        // Check for ignore
        if (plugin.getIgnoreManager().isIgnored(sender, recipient)) {
            sender.sendMessage(ChatColor.RED + "This player is ignoring your messages!");
            return;
        }

        TextComponent outgoingComponent = formatter.formatMessage(sender, message,
                privateOutgoingFormat.replace("%recipient%", recipient.getName()));
        TextComponent incomingComponent = formatter.formatMessage(sender, message,
                privateIncomingFormat.replace("%sender%", sender.getName()));

        sender.spigot().sendMessage(outgoingComponent);
        recipient.spigot().sendMessage(incomingComponent);
        lastMessageSender.put(recipient.getUniqueId(), sender.getUniqueId());
    }

    public UUID getLastMessageSender(Player player) {
        return lastMessageSender.get(player.getUniqueId());
    }
} 