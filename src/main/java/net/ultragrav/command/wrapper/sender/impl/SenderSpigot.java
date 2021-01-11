package net.ultragrav.command.wrapper.sender.impl;

import net.kyori.text.Component;
import net.ultragrav.command.wrapper.chat.impl.ConverterSpBg;
import net.ultragrav.command.wrapper.sender.UltraSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SenderSpigot implements UltraSender {
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
        sender.spigot().sendMessage(ConverterSpBg.toSpBg(msg));
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
