package net.ultragrav.command.provider.impl;

import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class EnumProvider<T extends Enum<T>> extends UltraProvider<Enum<T>> {
    private Class<T> enumClass;

    public EnumProvider(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public Enum<T> convert(@NonNull String toConvert) throws CommandException {
        try {
            return Enum.valueOf(enumClass, toConvert);
        } catch(EnumConstantNotPresentException e) {
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
