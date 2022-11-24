package net.ultragrav.command.wrapper.sender;

import net.ultragrav.chat.components.Component;
import net.ultragrav.command.wrapper.chat.TextUtil;

import java.util.UUID;

public interface UltraSender {
    Object getWrappedObject();

    void sendMessage(Component msg);

    default void sendMessage(String msg) {
        sendMessage(TextUtil.comp(msg));
    }

    boolean hasPermission(String perm);

    UUID getUniqueId();

    default boolean isPlayer() {
        return false;
    }
}
