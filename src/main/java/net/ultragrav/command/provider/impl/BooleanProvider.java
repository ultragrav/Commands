package net.ultragrav.command.provider.impl;

import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;

import java.util.ArrayList;
import java.util.List;

public final class BooleanProvider extends UltraProvider<Boolean> {
    @Getter
    private static final BooleanProvider instance = new BooleanProvider();

    @Override
    public Boolean convert(@NonNull String toConvert) throws CommandException {
        try {
            return Boolean.parseBoolean(toConvert);
        } catch (NumberFormatException ex) {
            throw new CommandException("Required: boolean (true/false), provided '" + toConvert + "'");
        }
    }

    @Override
    public List<String> tabComplete(@NonNull String toComplete) {
        toComplete = toComplete.toLowerCase();
        List<String> ret = new ArrayList<>();

        String[] poss = {"true", "false"};

        for (String str : poss) {
            if (str.startsWith(toComplete)) {
                ret.add(str);
            }
        }

        return ret;
    }

    @Override
    public String getArgumentDescription() {
        return "true/false";
    }
}