package net.ultragrav.command.wrapper.chat.impl;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.checkerframework.checker.nullness.qual.Nullable;

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
        writeEvents(comp, ret);
        return ret;
    }

    private static void writeEvents(Component source, BaseComponent target) {
        @Nullable HoverEvent hover = source.hoverEvent();
        if (hover != null) {
            target.setHoverEvent(convertHover(hover));
        }

        ClickEvent click = source.clickEvent();
        if (click != null) {
            target.setClickEvent(convertClick(click));
        }
    }

    private static net.md_5.bungee.api.chat.HoverEvent convertHover(HoverEvent hover) {
        return new net.md_5.bungee.api.chat.HoverEvent(convertHoverAction(hover.action()), new BaseComponent[]{toSpBg(hover.value())});
    }
    private static net.md_5.bungee.api.chat.HoverEvent.Action convertHoverAction(HoverEvent.Action action) {
        return net.md_5.bungee.api.chat.HoverEvent.Action.valueOf(action.name());
    }

    private static net.md_5.bungee.api.chat.ClickEvent convertClick(ClickEvent click) {
        return new net.md_5.bungee.api.chat.ClickEvent(convertClickAction(click.action()), click.value());
    }
    private static net.md_5.bungee.api.chat.ClickEvent.Action convertClickAction(ClickEvent.Action action) {
        return net.md_5.bungee.api.chat.ClickEvent.Action.valueOf(action.name());
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
