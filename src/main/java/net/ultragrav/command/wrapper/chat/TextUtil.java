package net.ultragrav.command.wrapper.chat;

import net.ultragrav.chat.components.Component;
import net.kyori.text.serializer.ComponentSerializers;
import net.ultragrav.chat.components.TextComponent;
import net.ultragrav.chat.converters.LegacyConverter;

@SuppressWarnings("deprecation")
public class TextUtil {
    public static Component comp(String str) {
        return LegacyConverter.AMPERSAND.convert(str);
    }

    public static String legacy(Component comp) {
        return LegacyConverter.MINECRAFT.convert(comp);
    }
}
