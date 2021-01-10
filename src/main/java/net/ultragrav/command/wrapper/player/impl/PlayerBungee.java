package net.ultragrav.command.wrapper.player.impl;

import net.kyori.text.Component;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.impl.SenderBungee;
import net.ultragrav.command.wrapper.sender.impl.SenderSpigot;
import org.bukkit.entity.Player;

public class PlayerBungee extends SenderBungee implements UltraPlayer {
    ProxiedPlayer player;

    public PlayerBungee(ProxiedPlayer player) {
        super(player);
        this.player = player;
    }

    @Override
    public void sendMessage(Component comp) {
        // TODO: Implement
    }
}
