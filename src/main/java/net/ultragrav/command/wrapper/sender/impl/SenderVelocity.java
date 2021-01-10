package net.ultragrav.command.wrapper.sender.impl;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
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
    public void sendMessage(String msg) {
        sender.sendMessage(new ChatBuilder(msg).build());
    }

    @Override
    public boolean hasPermission(String perm) {
        return sender.hasPermission(perm);
    }

    @Override
    public UUID getUniqueId() {
        return sender instanceof Player ? ((Player) sender).getUniqueId() : null;
    }

    @Override
    public boolean isPlayer() {
        return sender instanceof Player;
    }
}
