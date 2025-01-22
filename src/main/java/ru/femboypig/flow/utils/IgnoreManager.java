package ru.femboypig.flow.utils;

import org.bukkit.entity.Player;
import ru.femboypig.flow.Flow;

import java.util.*;

public class IgnoreManager {
    private final Flow plugin;
    private final Map<UUID, Set<UUID>> ignoredPlayers;

    public IgnoreManager(Flow plugin) {
        this.plugin = plugin;
        this.ignoredPlayers = new HashMap<>();
    }

    public void ignorePlayer(Player player, Player ignored) {
        UUID playerUUID = player.getUniqueId();
        UUID ignoredUUID = ignored.getUniqueId();

        ignoredPlayers.computeIfAbsent(playerUUID, k -> new HashSet<>()).add(ignoredUUID);
    }

    public void unignorePlayer(Player player, Player ignored) {
        UUID playerUUID = player.getUniqueId();
        UUID ignoredUUID = ignored.getUniqueId();

        if (ignoredPlayers.containsKey(playerUUID)) {
            ignoredPlayers.get(playerUUID).remove(ignoredUUID);
        }
    }

    public boolean isIgnored(Player sender, Player recipient) {
        return ignoredPlayers.containsKey(recipient.getUniqueId()) &&
               ignoredPlayers.get(recipient.getUniqueId()).contains(sender.getUniqueId());
    }

    public Set<UUID> getIgnoredPlayers(Player player) {
        return ignoredPlayers.getOrDefault(player.getUniqueId(), new HashSet<>());
    }
} 