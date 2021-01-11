package net.ultragrav.command.wrapper.sender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.text.Component;

import java.util.UUID;

public interface UltraSender {
    Object getWrappedObject();

    void sendMessage(Component msg);
    boolean hasPermission(String perm);

    UUID getUniqueId();

    boolean isPlayer();
}
