package net.ultragrav.command.provider.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Collections;
import java.util.List;

/**
 * Provider for an integer number (integer)
 *
 * Does not support tab completion
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IntegerProvider extends UltraProvider<Integer> {
	@Getter
	private static final IntegerProvider instance = new IntegerProvider();

	@Override
	public Integer convert(@NonNull final String toConvert, UltraSender sender) throws CommandException {
		try {
			return Integer.parseInt(toConvert);
		} catch (NumberFormatException exception) {
			throw new CommandException("Required: Integer, Given: '" + toConvert + "'");
		}
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete, UltraSender sender) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "integer number";
	}
}