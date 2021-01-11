package net.ultragrav.command.provider;

import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;

import java.util.List;

/**
 * @param <T> Type to convert
 */
public abstract class UltraProvider<T> {
	public abstract T convert(@NonNull String toConvert) throws CommandException;

	public abstract List<String> tabComplete(@NonNull String toComplete);

	public abstract String getArgumentDescription();

	public T defaultNullValue() {
		return null;
	}
}