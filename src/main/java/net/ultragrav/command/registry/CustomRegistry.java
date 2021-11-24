package net.ultragrav.command.registry;

import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create a custom registry to use UltraCommands for your own
 * games or other projects!
 * <p>
 * Note: Commands are case INSENSITIVE.
 * <p>
 * Remember to make your own implementations of
 * {@link net.ultragrav.command.wrapper.player.UltraPlayer UltraPlayer}, and if you want to, {@link UltraSender UltraSender}
 */
public class CustomRegistry implements Registry {
    @Getter
    private static CustomRegistry instance;

    private Map<String, UltraCommand> registered = new HashMap<>();

    public CustomRegistry() {
        instance = this;
    }

    @Override
    public void register(UltraCommand cmd) {
        cmd.getAliases().forEach(str -> registered.put(str.toLowerCase(), cmd));
    }

    /**
     * Execute the specified command
     *
     * @param sender  Command sender (executor)
     * @param command Command name
     * @param args    Arguments
     * @return {@code false} if no such command was found, {@code true} otherwise
     */
    public boolean execute(UltraSender sender, String command, List<String> args) {
        UltraCommand cmd = registered.get(command.toLowerCase());
        if (cmd == null) {
            return false;
        }
        cmd.execute(sender, command, new ArrayList<>(args));
        return true;
    }

    /**
     * Get tab completions for the specified command
     *
     * @param sender Command sender (executor)
     * @param command Command name
     * @param args Arguments
     * @return Tab completions (or empty list if no such command exists)
     */
    public List<String> tabComplete(UltraSender sender, String command, List<String> args) {
        UltraCommand cmd = registered.get(command.toLowerCase());
        if (cmd == null) {
            return new ArrayList<>();
        }
        return cmd.getTabCompletions(sender, new ArrayList<>(args));
    }
}
