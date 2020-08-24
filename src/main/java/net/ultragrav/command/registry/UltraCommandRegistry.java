package net.ultragrav.command.registry;

import com.google.common.collect.Sets;
import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public final class UltraCommandRegistry {
    @Getter
    private static final Set<UltraCommand> registerCommand = Sets.newConcurrentHashSet();

    public static void register(String label, UltraCommand command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            if (commandMap.getCommand(label) != null) {
                // Need to remove the command
                Field internalKnownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
                internalKnownCommandsField.setAccessible(true);

                Map<String, Command> internalCommands = (Map<String, Command>) internalKnownCommandsField.get(commandMap);
                internalCommands.remove(label.toLowerCase());
            }
            commandMap.register(label, new UltraCommandExecutor(label, command));
            registerCommand.add(command);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void register(UltraCommand command) {
        register(command.getAliases().get(0), command);
    }
}