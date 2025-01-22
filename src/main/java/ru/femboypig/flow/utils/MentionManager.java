package ru.femboypig.flow.utils;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import ru.femboypig.flow.Flow;

public class MentionManager {
    private final Flow plugin;

    public MentionManager(Flow plugin) {
        this.plugin = plugin;
    }

    public String processMentions(String message, Player sender) {
        String processedMessage = message;
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            String mention = "@" + player.getName();
            if (message.contains(mention)) {
                // Заменяем упоминание на цветное
                processedMessage = processedMessage.replace(mention, 
                    plugin.getConfig().getString("mentions.format", "&e@%player%&r")
                        .replace("%player%", player.getName()));
                
                // Проигрываем звук и отправляем Title если игрок онлайн
                if (player.isOnline() && !player.equals(sender)) {
                    notifyPlayer(player, sender);
                }
            }
        }
        
        return processedMessage;
    }

    private void notifyPlayer(Player mentioned, Player sender) {
        // Проигрываем звук
        if (plugin.getConfig().getBoolean("mentions.sound.enabled", true)) {
            String soundName = plugin.getConfig().getString("mentions.sound.type", "ENTITY_EXPERIENCE_ORB_PICKUP");
            float volume = (float) plugin.getConfig().getDouble("mentions.sound.volume", 1.0);
            float pitch = (float) plugin.getConfig().getDouble("mentions.sound.pitch", 1.0);
            
            mentioned.playSound(mentioned.getLocation(), Sound.valueOf(soundName), volume, pitch);
        }

        // Обновленный метод отправки Title для 1.20
        if (plugin.getConfig().getBoolean("mentions.title.enabled", true)) {
            String title = plugin.getConfig().getString("mentions.title.text", "&eMention from %player%")
                .replace("%player%", sender.getName());
            mentioned.sendTitle("", ChatColor.translateAlternateColorCodes('&', title), 
                10, 40, 10);
        }
    }
} 