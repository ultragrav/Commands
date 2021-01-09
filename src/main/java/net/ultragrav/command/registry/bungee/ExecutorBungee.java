package net.ultragrav.command.registry.bungee;

import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.ultragrav.command.UltraCommand;

public class ExecutorBungee extends Command {
    @Getter
    private final UltraCommand command;

    public ExecutorBungee(String label, UltraCommand command) {
        super(label, command.getPermission(), command.getAliases().toArray(new String[0]));
        this.command = command;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {

    }
}
