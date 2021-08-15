package net.ultragrav.command.wrapper.player.impl;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.impl.SenderBungee;

public class PlayerBungee extends SenderBungee implements UltraPlayer {
    ProxiedPlayer player;

    public PlayerBungee(ProxiedPlayer player) {
        super(player);
        this.player = player;
    }
}
