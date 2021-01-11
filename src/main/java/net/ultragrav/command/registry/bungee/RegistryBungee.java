package net.ultragrav.command.registry.bungee;

import net.md_5.bungee.api.ProxyServer;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;

public class RegistryBungee implements Registry {
    @Override
    public void register(UltraCommand cmd) {
        ProxyServer.getInstance().getPluginManager().registerCommand(null, new ExecutorBungee(cmd));
    }
}
