package net.ultragrav.command.registry.spigot;

import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.RegisteredListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("unchecked")
public final class RegistrySpigot implements Registry {
    private CommandMap commandMap;
    @Getter
    private ExecutorService tabCompleteExecutor = Executors.newCachedThreadPool();

    public RegistrySpigot() {
        UtilSpigot.initReflection();

        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            this.commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Getter
    private static final Set<UltraCommand> registerCommand = Collections.newSetFromMap(new ConcurrentHashMap<>());

    private void register(String label, UltraCommand command) {
        try {
            unregister(commandMap, label);
            for (String str : command.getAliases()) {
                unregister(commandMap, str);
            }
            commandMap.register(label, new ExecutorSpigot(label, command));
            registerCommand.add(command);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void unregister(CommandMap commandMap, String str) throws NoSuchFieldException, IllegalAccessException {
        if (commandMap.getCommand(str) != null) {
            // Need to remove the command
            Field internalKnownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            internalKnownCommandsField.setAccessible(true);

            Map<String, Command> internalCommands = (Map<String, Command>) internalKnownCommandsField.get(commandMap);
            internalCommands.remove(str.toLowerCase());
        }
    }

    public void register(UltraCommand command) {
        register(command.getAliases().get(0), command);
    }

    public boolean checkAsync(String cmd) {
        if (!cmd.startsWith("/")) {
            return false;
        }
        String cmdName = cmd.split(" ")[0];
        Command command = commandMap.getCommand(cmdName);
        if (!(command instanceof ExecutorSpigot)) {
            return false;
        }
        ExecutorSpigot executor = (ExecutorSpigot) command;
        return executor.getCommand().isAsync();
    }

    public List<String> tabCompletePacket(CommandSender sender, String cmd) {
        String[] split = cmd.split(" ");
        String cmdName = split[0];
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);

        List<String> argsList = Arrays.asList(args);

        Command command = commandMap.getCommand(cmdName);
        if (!(command instanceof ExecutorSpigot)) {
            throw new IllegalStateException("Called tabCompletePacket on a command that isn't an UltraCommand");
        }
        ExecutorSpigot executor = (ExecutorSpigot) command;
        return executor.getCommand().getTabCompletions(UtilSpigot.wrap(sender), argsList);
    }
}