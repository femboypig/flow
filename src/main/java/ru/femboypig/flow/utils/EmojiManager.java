package ru.femboypig.flow.utils;

import ru.femboypig.flow.Flow;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;

public class EmojiManager {
    private final Flow plugin;
    private final Map<String, String> emojis;

    public EmojiManager(Flow plugin) {
        this.plugin = plugin;
        this.emojis = new HashMap<>();
        loadEmojis();
    }

    private void loadEmojis() {
        for (String key : plugin.getConfig().getConfigurationSection("emojis").getKeys(false)) {
            emojis.put(key, plugin.getConfig().getString("emojis." + key));
        }
    }

    public String replaceEmojis(String message, Player player) {
        if (!player.hasPermission("flow.emojis")) {
            return message;
        }

        String result = message;
        for (Map.Entry<String, String> emoji : emojis.entrySet()) {
            result = result.replace(emoji.getKey(), emoji.getValue());
        }
        return result;
    }
} 