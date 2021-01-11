package net.ultragrav.command.wrapper.chat;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.kyori.text.format.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextUtil {
    private static final TextDecoration[] DECORATIONS;
    private static final List<TextFormat> formats = new ArrayList<>();

    static {
        formats.addAll(Arrays.asList(TextColor.values()));
        DECORATIONS = TextDecoration.values();
        formats.addAll(Arrays.asList(DECORATIONS));
        formats.add(TextColor.WHITE);
    }

    public static Component comp(String text) {
        return deserialize(text, '&');
    }

    private static TextFormat formatByLegacyChar(final char legacy) {
        final int index = "0123456789abcdefklmnor".indexOf(legacy);
        return (index == -1) ? null : formats.get(index);
    }

    private static TextComponent deserialize(final String input, final char character) {
        int next = input.lastIndexOf(character, input.length() - 2);
        if (next == -1) {
            return TextComponent.of(input);
        }
        final List<TextComponent> parts = new ArrayList<>();
        TextComponent.Builder current = null;
        boolean reset = false;
        int pos = input.length();
        do {
            final TextFormat format = formatByLegacyChar(input.charAt(next + 1));
            if (format != null) {
                final int from = next + 2;
                if (from != pos) {
                    if (current != null) {
                        if (reset) {
                            parts.add(current.build());
                            reset = false;
                            current = TextComponent.builder();
                        } else {
                            current = TextComponent.builder().append(current.build());
                        }
                    } else {
                        current = TextComponent.builder();
                    }
                    current.content(input.substring(from, pos));
                } else if (current == null) {
                    current = TextComponent.builder();
                }
                reset |= applyFormat(current, format);
                pos = next;
            }
            next = input.lastIndexOf(character, next - 1);
        } while (next != -1);
        if (current != null) {
            parts.add(current.build());
        }
        Collections.reverse(parts);
        return ((TextComponent.builder((pos > 0) ? input.substring(0, pos) : "")).append(parts)).build();
    }

    private static boolean applyFormat(final TextComponent.Builder builder, final TextFormat format) {
        if (format instanceof TextColor) {
            builder.colorIfAbsent((TextColor) format);
            return true;
        }
        if (format instanceof TextDecoration) {
            builder.decoration((TextDecoration) format, TextDecoration.State.TRUE);
            return false;
        }
        throw new IllegalArgumentException(String.format("unknown format '%s'", format.getClass()));
    }
}
