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
        this.prefix = color(prefix);
        this.primary = color(primary);
        this.secondary = color(secondary);
    }

    private static String color(String str) {
        return TextUtil.color(str);
    }

    public ChatFormat setPrefix(String prefix) {
        return new ChatFormat(prefix, primary, secondary);
    }

    public String format(String chat) {
        return TextUtil.color(chat.replaceAll("#p", primary).replaceAll("#s", secondary));
    }

    public String formatPrefix(String chat) {
        return prefix + format(chat);
    }
}
