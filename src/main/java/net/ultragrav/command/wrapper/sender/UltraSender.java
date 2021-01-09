package net.ultragrav.command.wrapper.sender;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

public interface UltraSender {
    void sendMessage(String msg);
    boolean hasPermission(String perm);

    UUID getUniqueId();

    boolean isPlayer();
}
