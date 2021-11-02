package net.ultragrav.command.provider.impl;

import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Allows the creation of a provider that generates a list
 * of options for any player, allowing tab completion and
 * checking for valid arguments
 *
 * @author UltraDev
 * @since 1.3.0
 */
public class DynamicOptionsProvider extends UltraProvider<String> {
    private String name;
    private Function<UltraSender, List<String>> generator;

    public DynamicOptionsProvider(String name, Function<UltraSender, List<String>> generator) {
        this.name = name;
        this.generator = generator;
    }

    @Override
    public String convert(@NonNull String s, UltraSender sender) throws CommandException {
        List<String> options = generator.apply(sender);

        String str = s.toLowerCase();
        if (!options.contains(str)) {
            throw new CommandException("Invalid " + name);
        }
        return str;
    }

    @Override
    public List<String> tabComplete(@NonNull String s, UltraSender sender) {
        List<String> options = generator.apply(sender);
        List<String> toSend = new ArrayList<>();

        for (String str : options)
            if (str.startsWith(s))
                toSend.add(str);

        return toSend;
    }

    @Override
    public String getArgumentDescription() {
        return name;
    }
}
