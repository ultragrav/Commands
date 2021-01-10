package net.ultragrav.command.wrapper.chat;

import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;

@SuppressWarnings("deprecation")
public class TextUtil {
    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static TextComponent comp(String str) {
        return ComponentSerializers.LEGACY.deserialize(str, '&');
    }
}
