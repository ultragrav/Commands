package net.ultragrav.command.registry.minestom;

import net.minestom.server.MinecraftServer;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;

@SuppressWarnings("unchecked")
public final class RegistryMinestom implements Registry {
    public void register(UltraCommand command) {
        MinecraftServer.getCommandManager().register(new MinestomCommand(command));
    }
}