package net.ultragrav.command.example;

import net.ultragrav.command.UltraCommand;

public class ExampleCommand extends UltraCommand {
    public ExampleCommand() {
        this.addAlias("example");

        this.addChildren(
                new ExampleSubCommand()
        );
    }
}
