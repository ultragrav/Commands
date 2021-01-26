package net.ultragrav.command.example;

import net.ultragrav.command.UltraCommand;

public class ExampleSubCommand extends UltraCommand {
    public ExampleSubCommand() {
        this.addAlias("sub");

        this.setAllowConsole(false);
    }

    @Override
    protected void perform() {
        //getPlayer().teleport(getPlayer().getLocation().add(0, getArgument(0), 0));
    }
}
