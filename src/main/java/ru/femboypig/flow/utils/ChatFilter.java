package ru.femboypig.flow.utils;

import ru.femboypig.flow.Flow;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class ChatFilter {
    private final Set<String> bannedWords;
    private final Flow plugin;
    private final Pattern pattern;

    public ChatFilter(Flow plugin) {
        this.plugin = plugin;
        this.bannedWords = new HashSet<>(plugin.getConfig().getStringList("chat-filter.banned-words"));
        this.pattern = Pattern.compile(String.join("|", bannedWords), Pattern.CASE_INSENSITIVE);
    }

    public String filterMessage(String message) {
        if (bannedWords.isEmpty()) {
            return message;
        }

        String filtered = message;
        for (String word : bannedWords) {
            String replacement = "*".repeat(word.length());
            filtered = filtered.replaceAll("(?i)" + Pattern.quote(word), replacement);
        }
        return filtered;
    }

    public boolean containsBannedWords(String message) {
        return pattern.matcher(message).find();
    }
} 