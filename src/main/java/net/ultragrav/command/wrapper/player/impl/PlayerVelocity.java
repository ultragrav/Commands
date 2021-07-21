package net.ultragrav.command.wrapper.player.impl;

import com.velocitypowered.api.proxy.Player;
import net.ultragrav.command.wrapper.player.UltraPlayer;
import net.ultragrav.command.wrapper.sender.impl.SenderVelocity;

public class PlayerVelocity extends SenderVelocity implements UltraPlayer {
    Player player;

    public PlayerVelocity(Player player) {
        super(player);
        this.player = player;
    }
}
