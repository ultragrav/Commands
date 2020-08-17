package net.ultragrav.command.provider.impl;

import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;

import java.util.Collections;
import java.util.List;

public final class LongProvider extends UltraProvider<Long> {

	@Getter
	private static final LongProvider instance = new LongProvider();


	@Override
	public Long convert(@NonNull final String toConvert) throws CommandException {
		try {
			return Long.parseLong(toConvert);
		} catch (NumberFormatException exception) {
			throw new CommandException("Required: Long Number, Given: '" + toConvert + "'");
		}
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "long number";
	}
}