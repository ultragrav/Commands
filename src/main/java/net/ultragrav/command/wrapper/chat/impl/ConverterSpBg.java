package net.ultragrav.command.wrapper.chat.impl;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

public class ConverterSpBg {
    public static BaseComponent toSpBg(Component comp) {
        BaseComponent ret = null;
        if (comp instanceof TextComponent) {
            ret = convertTextComponent((TextComponent) comp);
        }

        if (ret == null) {
            throw new RuntimeException("Unimplemented component conversion: " + comp.getClass().getName());
        }
        for (Component child : comp.children()) {
            ret.addExtra(toSpBg(child));
        }
        return ret;
    }

    private static net.md_5.bungee.api.chat.TextComponent convertTextComponent(TextComponent comp) {
        net.md_5.bungee.api.chat.TextComponent ret = new net.md_5.bungee.api.chat.TextComponent(comp.content());
        writeColor(comp, ret);
        return ret;
    }

    private static void writeColor(Component source, BaseComponent target) {
        TextColor color = source.color();
        if (color != null) {
            target.setColor(convertColor(color));
        }

        target.setBold(convert(source.decoration(TextDecoration.BOLD)));
        target.setUnderlined(convert(source.decoration(TextDecoration.UNDERLINE)));
        target.setItalic(convert(source.decoration(TextDecoration.ITALIC)));
        target.setStrikethrough(convert(source.decoration(TextDecoration.STRIKETHROUGH)));
        target.setObfuscated(convert(source.decoration(TextDecoration.OBFUSCATED)));
    }

    private static Boolean convert(TextDecoration.State state) {
        if (state == null) {
            return null;
        }
        switch (state) {
            case TRUE:
                return true;
            case FALSE:
                return false;
            case NOT_SET:
                return null;
        }
        return null;
    }

    private static ChatColor convertColor(TextColor color) {
        return ChatColor.valueOf(color.name());
    }
}
