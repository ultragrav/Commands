package net.ultragrav.command.registry.spigot;

import net.ultragrav.command.wrapper.player.impl.PlayerSpigot;
import net.ultragrav.command.wrapper.sender.UltraSender;
import net.ultragrav.command.wrapper.sender.impl.SenderSpigot;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UtilSpigot {
    public static UltraSender wrap(CommandSender sender) {
        if (sender instanceof Player) {
            return new PlayerSpigot((Player) sender);
        } else {
            return new SenderSpigot(sender);
        }
    }
}