package net.ultragrav.command.provider;

import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.List;

/**
 * @param <T> Type to convert
 */
public abstract class UltraProvider<T> {
    @Deprecated
    public T convert(@NonNull String toConvert) {
        return null;
    }

    public T convert(String toConvert, UltraSender sender) throws CommandException {
        return convert(toConvert);
    }

    public T convert(List<String> toConvert, UltraSender sender) throws CommandException {
        return convert(toConvert.remove(0), sender);
    }

    @Deprecated
    public List<String> tabComplete(@NonNull String toComplete) {
        return null;
    }

    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        return tabComplete(toComplete);
    }

    public List<String> tabComplete(List<String> toComplete, UltraSender sender) {
        return tabComplete(String.join(" ", toComplete), sender);
    }

    public abstract String getArgumentDescription();

    public T defaultNullValue() {
        return null;
    }

    /**
     * Whether this parameter should only be used asynchronously.
     * Usually used for providers that may block the thread.
     *
     * @return {@code true} if this provider should be used asynchronously
     */
    public boolean requireAsync() {
        return false;
    }
}