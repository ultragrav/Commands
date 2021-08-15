package net.ultragrav.command.wrapper.sender.impl;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.ultragrav.chat.components.Component;
import net.ultragrav.chat.converters.BungeeConverter;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.UUID;

public class SenderBungee implements UltraSender {
    private final CommandSender sender;

    public SenderBungee(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public Object getWrappedObject() {
        return sender;
    }

    @Override
    public void sendMessage(Component msg) {
        sender.sendMessage(BungeeConverter.INSTANCE.convert(msg));
    }

    @Override
    public boolean hasPermission(String perm) {
        return sender.hasPermission(perm);
    }

    @Override
    public UUID getUniqueId() {
        return sender instanceof ProxiedPlayer ? ((ProxiedPlayer) sender).getUniqueId() : null;
    }
}
