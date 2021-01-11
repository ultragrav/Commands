package net.ultragrav.command.registry.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.proxy.Velocity;
import com.velocitypowered.proxy.VelocityServer;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;
import org.slf4j.Logger;

public class RegistryVelocity implements Registry {
    private static RegistryVelocity instance;

    public static void init(ProxyServer server) {
        if (instance != null) {
            return;
        }
        instance = new RegistryVelocity(server);
    }

    private final ProxyServer server;

    private RegistryVelocity(ProxyServer server) {
        this.server = server;
    }

    @Override
    public void register(UltraCommand cmd) {
        server.getCommandManager().register(new ExecutorVelocity(cmd), cmd.getAliases().toArray(new String[0]));
    }
}
