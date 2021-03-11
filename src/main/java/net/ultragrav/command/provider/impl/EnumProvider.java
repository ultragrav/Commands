package net.ultragrav.command.provider.impl;

import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract provider to allow simple creation of providers for any enum
 * Note: Use super(clazz, true) to make the provider case-sensitive
 *
 * @param <T> Enum
 */
public abstract class EnumProvider<T extends Enum<T>> extends UltraProvider<Enum<T>> {
    private Class<T> enumClass;
    private boolean caseSens = false;

    public EnumProvider(Class<T> enumClass) {
        this.enumClass = enumClass;
    }
    public EnumProvider(Class<T> enumClass, boolean caseSens) {
        this.enumClass = enumClass;
        this.caseSens = caseSens;
    }

    @Override
    public Enum<T> convert(@NonNull String toConvert) throws CommandException {
        String n = toConvert;
        if (!caseSens) {
            T[] consts = enumClass.getEnumConstants();
            for (T t : consts) {
                if (t.name().equalsIgnoreCase(n)) {
                    n = t.name();
                }
            }
        }
        try {
            return Enum.valueOf(enumClass, n);
        } catch (EnumConstantNotPresentException | IllegalArgumentException e) {
            throw new CommandException("No constant found: " + toConvert +
                    ", possible values: " + String.join(", ",
                    ArrayUtils.enumName(enumClass.getEnumConstants())));
        }
    }

    @Override
    public List<String> tabComplete(@NonNull String toComplete) {
        List<String> ret = new ArrayList<>();
        for (T t : enumClass.getEnumConstants()) {
            if (t.name().toLowerCase().startsWith(toComplete)) {
                ret.add(t.name());
            }
        }
        return ret;
    }

    @Override
    public String getArgumentDescription() {
        return enumClass.getSimpleName();
    }
}
