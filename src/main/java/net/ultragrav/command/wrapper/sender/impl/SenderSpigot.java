package net.ultragrav.command.wrapper.sender.impl;

import net.ultragrav.chat.components.Component;
import net.ultragrav.chat.converters.BungeeConverter;
import net.ultragrav.chat.converters.BungeeTextConverter;
import net.ultragrav.chat.converters.LegacyConverter;
import net.ultragrav.command.wrapper.sender.UltraSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SenderSpigot implements UltraSender {
    public static boolean componentSender = false;

    private CommandSender sender;

    public SenderSpigot(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public Object getWrappedObject() {
        return sender;
    }

    @Override
    public void sendMessage(Component msg) {
        if (componentSender) {
            sender.spigot().sendMessage(BungeeTextConverter.INSTANCE.convert(msg));
        } else {
            sender.sendMessage(LegacyConverter.MINECRAFT.convert(msg));
        }
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
