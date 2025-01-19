package ru.femboypig.flow.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.femboypig.flow.Flow;

public class ChatListener implements Listener {
    private final Flow plugin;

    public ChatListener(Flow plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        
        // Синхронизируем обработку чата с основным потоком
        Bukkit.getScheduler().runTask(plugin, () -> 
            plugin.getChatManager().handleChat(event.getPlayer(), event.getMessage()));
    }
} 