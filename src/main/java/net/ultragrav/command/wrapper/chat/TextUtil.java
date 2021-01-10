package net.ultragrav.command.wrapper.chat;

public class TextUtil {
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
