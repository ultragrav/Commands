package net.ultragrav.command.wrapper.chat;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.ultragrav.command.wrapper.player.UltraPlayer;

import java.util.Collection;

public class ChatBuilder {
    private static final ChatFormat defaultFormat = new ChatFormat("", "&d", "&f");

    private final Component comp;
    private ChatFormat format = defaultFormat;
    private String lastColours = "";

    /**
     * Create a blank ChatBuilder
     */
    public ChatBuilder() {
        comp = TextComponent.of("");
    }

    /**
     * Create a blank ChatBuilder
     */
    public ChatBuilder(ChatFormat format) {
        this.format = format;
        comp = fromString(format.getPrefix());
    }

    /**
     * Create a ChatBuilder from some text
     *
     * @param text Text
     */
    public ChatBuilder(String text) {
        comp = fromString(text);
        this.lastColours = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * Create a ChatBuilder from some text
     *
     * @param text Text
     */
    public ChatBuilder(ChatFormat format, String text) {
        this.format = format;
        comp = fromString(format.getPrefix() + text);
        this.lastColours = ChatColor.getLastColors(ChatColor.translateAlternateColorCodes('&', text));
    }

    /**
     * Create a ChatBuilder with a specific prefix
     *
     * @param prefix Prefix
     * @return ChatBuilder
     */
    public static ChatBuilder prefix(String prefix) {
        return new ChatBuilder(defaultFormat.setPrefix(prefix));
    }

    /**
     * Convert a String to a colorized TextComponent
     *
     * @param text String
     * @return TextComponent
     */
    private TextComponent fromString(String text) {
        return TextComponent.of(text);
    }

    /**
     * Add a TextComponent to this
     *
     * @param component Component
     * @return this
     */
    public ChatBuilder addComponent(Component component) {
        comp.append(component);
        return this;
    }

    /**
     * Add some text
     *
     * @param str Text
     * @return this
     */
    public ChatBuilder addText(String str) {
        return addComponent(fromString(str));
    }

    /**
     * Add the contents of another ChatBuilder
     *
     * @param builder ChatBuilder
     * @return this
     */
    public ChatBuilder addBuilder(ChatBuilder builder) {
        return addComponent(builder.comp);
    }

    /**
     * Add some text with a ClickEvent
     *
     * @param str Text
     * @param e   ClickEvent
     * @return this
     */
    public ChatBuilder addText(String str, ClickEvent e) {
        TextComponent c = fromString(str);
        c.clickEvent(e);
        return addComponent(c);
    }

    /**
     * Add some text with a HoverEvent
     *
     * @param str Text
     * @param e   HoverEvent
     * @return this
     */
    public ChatBuilder addText(String str, HoverEvent e) {
        TextComponent c = fromString(str);
        c.hoverEvent(e);
        return addComponent(c);
    }

    /**
     * Add some text with a ClickEvent and HoverEvent
     *
     * @param str Text
     * @param e   HoverEvent
     * @param e2  ClickEvent
     * @return this
     */
    public ChatBuilder addText(String str, HoverEvent e, ClickEvent e2) {
        TextComponent c = fromString(str);
        c.hoverEvent(e);
        c.clickEvent(e2);
        return addComponent(c);
    }

    public ChatBuilder copy() {
        return new ChatBuilder().addComponent(this.comp.copy());
    }

    /**
     * Convert this ChatBuilder into a TextComponent for spigot
     *
     * @return TextComponent
     */
    public Component build() {
        return comp;
    }

    /**
     * Send the TextComponent from this ChatBuilder to a player
     *
     * @param player Player
     */
    public void send(UltraPlayer player) {
        player.sendMessage(build());
    }

    /**
     * Send the TextComponent from this ChatBuilder to multiple players
     *
     * @param players Collection of Player
     */
    public void sendAll(Collection<? extends UltraPlayer> players) {
        for (UltraPlayer player : players) {
            if (player == null) {
                continue;
            }
            send(player);
        }
    }
}
