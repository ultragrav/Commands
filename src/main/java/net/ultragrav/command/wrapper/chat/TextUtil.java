package net.ultragrav.command.wrapper.chat;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.serializer.ComponentSerializers;

@SuppressWarnings("deprecation")
public class TextUtil {
    public static TextComponent comp(String str) {
        return ComponentSerializers.LEGACY.deserialize(str, '&');
    }

    public static String legacy(Component comp) {
        return ComponentSerializers.LEGACY.serialize(comp);
    }
}
