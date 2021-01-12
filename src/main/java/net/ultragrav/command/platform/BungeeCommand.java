package net.ultragrav.command.platform;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.ultragrav.command.UltraCommand;

public abstract class BungeeCommand extends UltraCommand {
    public ProxiedPlayer getBungeePlayer() {
        return (ProxiedPlayer) getPlayer().getWrappedObject();
    }
}
