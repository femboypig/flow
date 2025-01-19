package ru.femboypig.flow.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import ru.femboypig.flow.Flow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MessageFormatter {
    private final Flow plugin;
    private final SimpleDateFormat timeFormat;

    public MessageFormatter(Flow plugin) {
        this.plugin = plugin;
        this.timeFormat = new SimpleDateFormat(plugin.getConfig().getString("time-format", "HH:mm:ss"));
    }

    public TextComponent formatMessage(Player sender, String message, String format) {
        String formattedMessage = format
                .replace("%player_name%", sender.getName())
                .replace("%message%", message);

        formattedMessage = PlaceholderAPI.setPlaceholders(sender, formattedMessage);
        formattedMessage = ChatColor.translateAlternateColorCodes('&', formattedMessage);

        TextComponent component = new TextComponent(formattedMessage);

        if (plugin.getConfig().getBoolean("hover.enabled", true)) {
            component.setHoverEvent(createHoverEvent(sender));
        }

        if (plugin.getConfig().getBoolean("click.enabled", true)) {
            component.setClickEvent(createClickEvent(sender));
        }

        return component;
    }

    private HoverEvent createHoverEvent(Player player) {
        List<String> hoverFormat = plugin.getConfig().getStringList("hover.format");
        
        String hoverText = hoverFormat.stream()
                .map(line -> line
                        .replace("%player_name%", player.getName())
                        .replace("%time%", timeFormat.format(new Date()))
                        .replace("%player_ping%", String.valueOf(player.getPing())))
                .map(line -> PlaceholderAPI.setPlaceholders(player, line))
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.joining("\n"));

        return new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
                new ComponentBuilder(hoverText).create());
    }

    private ClickEvent createClickEvent(Player player) {
        String action = plugin.getConfig().getString("click.action", "SUGGEST_COMMAND");
        String value = plugin.getConfig().getString("click.value", "/msg %player_name% ")
                .replace("%player_name%", player.getName());

        return new ClickEvent(ClickEvent.Action.valueOf(action), value);
    }
} 