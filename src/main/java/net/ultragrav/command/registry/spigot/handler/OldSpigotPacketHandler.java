package net.ultragrav.command.registry.spigot.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import net.ultragrav.command.registry.RegistryManager;
import net.ultragrav.command.registry.spigot.RegistrySpigot;
import net.ultragrav.command.registry.spigot.UtilSpigot;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class OldSpigotPacketHandler extends ChannelDuplexHandler {
    private static Class<?> packetPlayInTabComplete;
    private static Class<?> packetPlayOutTabComplete;

    private static Field commandField;

    private static Constructor<?> packetPlayOutTabCompleteConstructor;

    static {
        String nmsVersion = UtilSpigot.nmsVersion;

        try {
            packetPlayInTabComplete = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayInTabComplete");
            packetPlayOutTabComplete = Class.forName("net.minecraft.server." + nmsVersion + ".PacketPlayOutTabComplete");

            commandField = packetPlayInTabComplete.getDeclaredFields()[0];
            commandField.setAccessible(true);

            packetPlayOutTabCompleteConstructor = packetPlayOutTabComplete.getConstructor(String[].class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Player player;

    public OldSpigotPacketHandler(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (packetPlayInTabComplete.isInstance(msg)) {
            try {
                RegistrySpigot registrySpigot = (RegistrySpigot) RegistryManager.getCurrentRegistry();

                String command = (String) commandField.get(msg);
                boolean isAsync = registrySpigot.checkAsync(command);
                if (!isAsync) {
                    super.channelRead(ctx, msg);
                } else {
                    registrySpigot.getTabCompleteExecutor().submit(() -> {
                        List<String> completions = registrySpigot.tabCompletePacket(player, command);
                        try {
                            Object packet = packetPlayOutTabCompleteConstructor
                                    .newInstance(new Object[]{completions.toArray(new String[0])});
                            UtilSpigot.sendPacket(player, packet);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
