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
 * Provider for an integer number (long)
 *
 * Does not support tab completion
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LongProvider extends UltraProvider<Long> {
	@Getter
	private static final LongProvider instance = new LongProvider();

	@Override
	public Long convert(@NonNull final String toConvert, UltraSender sender) throws CommandException {
		try {
			return Long.parseLong(toConvert);
		} catch (NumberFormatException exception) {
			throw new CommandException("Required: Long Number, Given: '" + toConvert + "'");
		}
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete, UltraSender sender) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "long number";
	}
}