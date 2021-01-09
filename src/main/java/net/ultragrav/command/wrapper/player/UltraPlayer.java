package net.ultragrav.command.wrapper.player;

import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.UUID;

public interface UltraPlayer extends UltraSender {
    default boolean isPlayer() {
        return true;
    }
}
