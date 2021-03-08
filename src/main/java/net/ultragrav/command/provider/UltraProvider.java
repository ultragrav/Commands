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

	public abstract List<String> tabComplete(@NonNull String toComplete);

	public abstract String getArgumentDescription();

	public T defaultNullValue() {
		return null;
	}
}