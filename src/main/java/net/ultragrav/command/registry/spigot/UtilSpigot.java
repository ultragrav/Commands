package net.ultragrav.command.registry.spigot;

import io.netty.channel.Channel;
import net.ultragrav.command.wrapper.player.impl.PlayerSpigot;
import net.ultragrav.command.wrapper.sender.UltraSender;
import net.ultragrav.command.wrapper.sender.impl.SenderSpigot;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UtilSpigot {
    static String nmsVersion;
    private static Class<?> craftPlayerClass;
    private static Method getHandleMethod;
    private static Field playerConnectionField;
    private static Field networkManagerField;
    private static Field channelField;
    private static Method sendPacketMethod;

    static {
        try {
            nmsVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
            craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + nmsVersion + ".entity.CraftPlayer");
            getHandleMethod = craftPlayerClass.getMethod("getHandle");
            playerConnectionField = Class.forName("net.minecraft.server." + nmsVersion + ".EntityPlayer")
                    .getDeclaredField("playerConnection");
            networkManagerField = Class.forName("net.minecraft.server." + nmsVersion + ".PlayerConnection")
                    .getDeclaredField("networkManager");
            channelField = Class.forName("net.minecraft.server." + nmsVersion + ".NetworkManager")
                    .getDeclaredField("channel");
            sendPacketMethod = Class.forName("net.minecraft.server." + nmsVersion + ".PlayerConnection")
                    .getMethod("sendPacket", Class.forName("net.minecraft.server." + nmsVersion + ".Packet"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UltraSender wrap(CommandSender sender) {
        if (sender instanceof Player) {
            return new PlayerSpigot((Player) sender);
        } else {
            return new SenderSpigot(sender);
        }
    }

    public static void initReflection() {
        try {
            CommandSender.class.getDeclaredMethod("spigot");
            SenderSpigot.componentSender = true;
        } catch (NoSuchMethodException ex) {
            SenderSpigot.componentSender = false;
        }

        registerEvent(PlayerJoinEvent.class, (listener, event) -> inject(((PlayerJoinEvent) event).getPlayer()));
        registerEvent(PlayerQuitEvent.class, (listener, event) -> unInject(((PlayerQuitEvent) event).getPlayer()));
    }

    private static void registerEvent(Class<? extends Event> event, EventExecutor executor) {
        getEventListeners(event).register(new RegisteredListener(new Listener() {
        }, executor, EventPriority.NORMAL,
                DummyPlugin.instance, false));
    }

    private static HandlerList getEventListeners(Class<? extends Event> type) {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            throw new IllegalPluginAccessException(e.toString());
        }
    }

    private static Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
        try {
            clazz.getDeclaredMethod("getHandlerList");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null
                    && !clazz.getSuperclass().equals(Event.class)
                    && Event.class.isAssignableFrom(clazz.getSuperclass())) {
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            } else {
                throw new IllegalPluginAccessException("Unable to find handler list for event " + clazz.getName() + ". Static getHandlerList method required!");
            }
        }
    }

    public static void inject(Player player) {
        //Inject a packet listener into the player's connection.
        try {
            Object entityPlayer = getHandleMethod.invoke(player);
            Object connection = playerConnectionField.get(entityPlayer);
            Object networkManager = networkManagerField.get(connection);
            Channel channel = (Channel) channelField.get(networkManager);

            channel.eventLoop().submit(() -> {
                if (channel.pipeline().get("skyblock_packet_listener") != null) {
                    //Remove it.
                    channel.pipeline().remove("skyblock_packet_listener");

                }
                //Add it.
                channel.pipeline()
                        .addBefore("packet_handler", "skyblock_packet_listener", new SpigotPacketHandler(player));
            });
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void unInject(Player player) {
        //Remove the packet listener from the player's connection.

        try {
            Object entityPlayer = getHandleMethod.invoke(player);
            Object connection = playerConnectionField.get(entityPlayer);
            Object networkManager = networkManagerField.get(connection);
            Channel channel = (Channel) channelField.get(networkManager);

            channel.eventLoop().submit(() -> {
                if (channel.pipeline().get("skyblock_packet_listener") != null) {
                    //Remove it.
                    channel.pipeline().remove("skyblock_packet_listener");

                }
            });
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object entityPlayer = getHandleMethod.invoke(player);
            Object connection = playerConnectionField.get(entityPlayer);
            sendPacketMethod.invoke(connection, packet);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
