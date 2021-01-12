package net.ultragrav.command.platform;

import net.ultragrav.command.UltraCommand;
import org.bukkit.entity.Player;

public abstract class SpigotCommand extends UltraCommand {
    public Player getSpigotPlayer() {
        return (Player) getPlayer().getWrappedObject();
    }
}
