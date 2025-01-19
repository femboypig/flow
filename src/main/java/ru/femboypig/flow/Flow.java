package ru.femboypig.flow;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.femboypig.flow.chat.ChatManager;
import ru.femboypig.flow.commands.*;
import ru.femboypig.flow.listeners.ChatListener;

public final class Flow extends JavaPlugin {
    private static Flow instance;
    private ChatManager chatManager;

    @Override
    public void onEnable() {
        instance = this;
        
        // Load configuration
        saveDefaultConfig();
        
        // Check for PlaceholderAPI
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("PlaceholderAPI not found! Prefixes won't work!");
            getLogger().warning("Download PlaceholderAPI: https://www.spigotmc.org/resources/placeholderapi.6245/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        
        // Initialize managers
        this.chatManager = new ChatManager(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        
        // Register commands
        getCommand("msg").setExecutor(new MessageCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("flowreload").setExecutor(new FlowReloadCommand(this));
        
        getLogger().info("Flow has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("Flow has been successfully disabled!");
    }

    public static Flow getInstance() {
        return instance;
    }

    public ChatManager getChatManager() {
        return chatManager;
    }
}
