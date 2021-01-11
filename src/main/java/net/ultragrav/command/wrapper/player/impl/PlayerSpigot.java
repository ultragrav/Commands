package net.ultragrav.command.wrapper.player.impl;

import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.impl.SenderSpigot;
import org.bukkit.entity.Player;

public class PlayerSpigot extends SenderSpigot implements UltraPlayer {
    Player player;

    public PlayerSpigot(Player player) {
        super(player);
        this.player = player;
    }
}
