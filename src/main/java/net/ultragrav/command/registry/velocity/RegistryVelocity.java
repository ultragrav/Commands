package net.ultragrav.command.registry.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;

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
