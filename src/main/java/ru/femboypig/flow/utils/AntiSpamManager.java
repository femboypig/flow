package ru.femboypig.flow.utils;

import org.bukkit.entity.Player;
import ru.femboypig.flow.Flow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AntiSpamManager {
    private final Flow plugin;
    private final Map<UUID, Long> lastMessageTime;
    private final Map<UUID, Integer> messageCount;
    private final int cooldownSeconds;
    private final int maxMessages;
    private final int timeWindowSeconds;

    public AntiSpamManager(Flow plugin) {
        this.plugin = plugin;
        this.lastMessageTime = new HashMap<>();
        this.messageCount = new HashMap<>();
        this.cooldownSeconds = plugin.getConfig().getInt("anti-spam.cooldown", 2);
        this.maxMessages = plugin.getConfig().getInt("anti-spam.max-messages", 5);
        this.timeWindowSeconds = plugin.getConfig().getInt("anti-spam.time-window", 10);
    }

    public boolean isSpamming(Player player) {
        if (player.hasPermission("flow.bypass.antispam")) {
            return false;
        }

        UUID uuid = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Проверка кулдауна между сообщениями
        if (lastMessageTime.containsKey(uuid)) {
            long timeDiff = currentTime - lastMessageTime.get(uuid);
            if (timeDiff < cooldownSeconds * 1000) {
                return true;
            }
        }

        // Проверка количества сообщений в временном окне
        messageCount.putIfAbsent(uuid, 0);
        messageCount.put(uuid, messageCount.get(uuid) + 1);

        plugin.getServer().getScheduler().runTaskLater(plugin, 
            () -> messageCount.put(uuid, messageCount.get(uuid) - 1), 
            timeWindowSeconds * 20L);

        if (messageCount.get(uuid) > maxMessages) {
            return true;
        }

        lastMessageTime.put(uuid, currentTime);
        return false;
    }
} 