package net.ultragrav.command.parameters;

import lombok.AllArgsConstructor;
import net.ultragrav.command.exception.CommandException;

import java.util.function.Function;

@AllArgsConstructor
public class ParameterCondition<T> {
    private Function<T, Boolean> condition;
    private String message;

    public ParameterCondition(Function<T, Boolean> condition) {
        this(condition, "&cInvalid argument");
    }

    public void confirm(T t) {
        if (!condition.apply(t)) throw new CommandException(message);
    }
}
