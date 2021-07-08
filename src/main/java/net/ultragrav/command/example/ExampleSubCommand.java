package net.ultragrav.command.example;

import net.ultragrav.command.Parameter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.provider.impl.StringProvider;

import java.util.List;

public class ExampleSubCommand extends UltraCommand {
    public ExampleSubCommand() {
        this.addAlias("sub");

        this.setAllowConsole(false);

        this.addParameter(Parameter.builder(StringProvider.getInstance()).name("message").varArg(true).build());
    }

    @Override
    protected void perform() {
        List<String> message = getArgument(0);

        tell(String.join(" ", message));
    }
}
