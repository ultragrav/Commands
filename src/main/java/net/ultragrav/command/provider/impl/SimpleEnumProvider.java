package net.ultragrav.command.provider.impl;

import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.util.ArrayUtils;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract provider to allow simple creation of providers for any enum
 * Note: Use super(clazz, true) to make the provider case-sensitive
 *
 * @param <T> Enum
 */
public class SimpleEnumProvider<T extends Enum<T>> extends EnumProvider<T> {
    private String name;

    public SimpleEnumProvider(String name, Class<T> enumClass) {
        super(enumClass);
        this.name = name;
    }
    public SimpleEnumProvider(String name, Class<T> enumClass, boolean caseSens) {
        super(enumClass, caseSens);
        this.name = name;
    }

    @Override
    public String getArgumentDescription() {
        return name;
    }
}
