package net.ultragrav.command.wrapper.sender.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.kyori.text.Component;
import net.ultragrav.command.wrapper.chat.ChatBuilder;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.UUID;

public class SenderVelocity implements UltraSender {
    private final CommandSource sender;

    public SenderVelocity(CommandSource sender) {
        this.sender = sender;
    }

    @Override
    public Object getWrappedObject() {
        return sender;
    }

    @Override
    public void sendMessage(Component msg) {
        sender.sendMessage(msg);
    }

    @Override
    public boolean hasPermission(String perm) {
        return sender.hasPermission(perm);
    }

    @Override
    public UUID getUniqueId() {
        return sender instanceof Player ? ((Player) sender).getUniqueId() : null;
    }
}
