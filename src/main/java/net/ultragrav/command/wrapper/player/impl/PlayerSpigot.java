package net.ultragrav.command.wrapper.player.impl;

import net.ultragrav.chat.components.Component;
import net.ultragrav.chat.converters.BungeeConverter;
import net.ultragrav.chat.converters.BungeeTextConverter;
import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.impl.SenderSpigot;
import org.bukkit.entity.Player;

public class PlayerSpigot extends SenderSpigot implements UltraPlayer {
    Player player;

    public PlayerSpigot(Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public void sendMessage(Component msg) {
        player.spigot().sendMessage(BungeeTextConverter.INSTANCE.convert(msg));
    }
}
