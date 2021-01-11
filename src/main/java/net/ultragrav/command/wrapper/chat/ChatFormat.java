package net.ultragrav.command.wrapper.chat;

import lombok.Getter;

@Getter
public class ChatFormat {
    private String prefix;
    private String primary;
    private String secondary;

    public ChatFormat() {
        this("");
    }

    public ChatFormat(String prefix) {
        this(prefix, "");
    }

    public ChatFormat(String prefix, String primary) {
        this(prefix, primary, "");
    }

    public ChatFormat(String prefix, String primary, String secondary) {
        this.prefix = prefix;
        this.primary = primary;
        this.secondary = secondary;
    }

    public ChatFormat setPrefix(String prefix) {
        return new ChatFormat(prefix, primary, secondary);
    }

    public String format(String chat) {
        return chat.replaceAll("#p", primary).replaceAll("#s", secondary);
    }

    public String formatPrefix(String chat) {
        return prefix + format(chat);
    }
}
