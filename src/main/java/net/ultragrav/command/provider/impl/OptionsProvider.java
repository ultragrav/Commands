package net.ultragrav.command.provider.impl;

import com.google.common.collect.Lists;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Allows the creation of a provider that takes any number of options
 * and creates tab completion and checks, useful to avoid unnecessary
 * enums
 *
 * @author UltraDev
 * @since 1.2.7
 */
public class OptionsProvider extends UltraProvider<String> {
    private String name;
    private List<String> options;

    public OptionsProvider(String name, String... options) {
        this.name = name;
        this.options = Arrays.stream(options).map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    @Override
    public String convert(@NonNull String s, UltraSender sender) throws CommandException {
        String str = s.toLowerCase();
        if (!options.contains(str)) {
            throw new CommandException("Invalid " + name);
        }
        return str;
    }

    @Override
    public List<String> tabComplete(@NonNull String s, UltraSender sender) {
        List<String> toSend = Lists.newArrayList();

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
