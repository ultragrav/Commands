package net.ultragrav.command.registry.spigot;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.registry.Registry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class RegistrySpigot implements Registry {
    @Getter
    private static final Set<UltraCommand> registerCommand = Sets.newConcurrentHashSet();

    private void register(String label, UltraCommand command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
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
            Field internalKnownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            internalKnownCommandsField.setAccessible(true);

            Map<String, Command> internalCommands = (Map<String, Command>) internalKnownCommandsField.get(commandMap);
            internalCommands.remove(str.toLowerCase());
        }
    }

    public void register(UltraCommand command) {
        register(command.getAliases().get(0), command);
    }
}