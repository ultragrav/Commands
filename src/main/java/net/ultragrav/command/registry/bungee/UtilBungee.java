package net.ultragrav.command.registry.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.ultragrav.command.wrapper.player.impl.PlayerBungee;
import net.ultragrav.command.wrapper.sender.UltraSender;
import net.ultragrav.command.wrapper.sender.impl.SenderBungee;

public class UtilBungee {
    public static UltraSender wrap(CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            return new PlayerBungee((ProxiedPlayer) sender);
        } else {
            return new SenderBungee(sender);
        }
    }
}
