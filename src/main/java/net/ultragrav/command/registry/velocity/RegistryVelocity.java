package net.ultragrav.command.registry.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;
import org.slf4j.Logger;

@Plugin(id = "CommandInject", name = "CommandInject", version = "1.0-SNAPSHOT", description = "Velocity is dumb so this is required to inject commands", authors = {"UltraDev"})
public class RegistryVelocity implements Registry {
    public static RegistryVelocity instance;

    private ProxyServer server;

    @Inject
    public RegistryVelocity(ProxyServer server, Logger logger) {
        this.server = server;

        instance = this;

        logger.info("Velocity command hook created");
    }

    @Override
    public void register(UltraCommand cmd) {
        server.getCommandManager().register(new ExecutorVelocity(cmd), cmd.getAliases().toArray(new String[0]));
    }
}
