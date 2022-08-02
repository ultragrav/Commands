package net.ultragrav.command.registry.spigot;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_12_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete;
import net.ultragrav.command.registry.RegistryManager;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class SpigotPacketHandler extends ChannelDuplexHandler {
    private Player player;

    public SpigotPacketHandler(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PacketPlayInTabComplete) {
            RegistrySpigot registrySpigot = (RegistrySpigot) RegistryManager.getCurrentRegistry();

            PacketPlayInTabComplete packet = (PacketPlayInTabComplete) msg;
            boolean isAsync = registrySpigot.checkAsync(packet.a());
            if (!isAsync) {
                super.channelRead(ctx, msg);
            } else {
                registrySpigot.getTabCompleteExecutor().submit(() -> {
                    List<String> completions = registrySpigot.tabCompletePacket(player, packet.a());
                    PacketPlayOutTabComplete tabCompletePacket = new PacketPlayOutTabComplete(completions.toArray(new String[0]));
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(tabCompletePacket);
                });
            }
        }
    }
}
