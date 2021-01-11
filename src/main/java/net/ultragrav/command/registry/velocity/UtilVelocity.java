package net.ultragrav.command.registry.velocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import net.ultragrav.command.wrapper.player.impl.PlayerVelocity;
import net.ultragrav.command.wrapper.sender.UltraSender;
import net.ultragrav.command.wrapper.sender.impl.SenderVelocity;

public class UtilVelocity {
    public static UltraSender wrap(CommandSource sender) {
        if (sender instanceof Player) {
            return new PlayerVelocity((Player) sender);
        } else {
            return new SenderVelocity(sender);
        }
    }
}
