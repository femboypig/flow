package ru.femboypig.flow;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.femboypig.flow.chat.ChatManager;
import ru.femboypig.flow.commands.*;
import ru.femboypig.flow.listeners.ChatListener;
import ru.femboypig.flow.utils.*;

public final class Flow extends JavaPlugin {
    private static Flow instance;
    private ChatManager chatManager;
    private AntiSpamManager antiSpamManager;
    private ChatFilter chatFilter;
    private IgnoreManager ignoreManager;
    private EmojiManager emojiManager;
    private MentionManager mentionManager;

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
        this.antiSpamManager = new AntiSpamManager(this);
        this.chatFilter = new ChatFilter(this);
        this.ignoreManager = new IgnoreManager(this);
        this.emojiManager = new EmojiManager(this);
        this.mentionManager = new MentionManager(this);
        this.chatManager = new ChatManager(this);
        
        // Register listeners
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        
        // Register commands
        getCommand("msg").setExecutor(new MessageCommand(this));
        getCommand("reply").setExecutor(new ReplyCommand(this));
        getCommand("flowreload").setExecutor(new FlowReloadCommand(this));
        getCommand("ignore").setExecutor(new IgnoreCommand(this));
        
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

    public AntiSpamManager getAntiSpamManager() {
        return antiSpamManager;
    }

    public ChatFilter getChatFilter() {
        return chatFilter;
    }

    public IgnoreManager getIgnoreManager() {
        return ignoreManager;
    }

    public EmojiManager getEmojiManager() {
        return emojiManager;
    }

    public MentionManager getMentionManager() {
        return mentionManager;
    }
}
