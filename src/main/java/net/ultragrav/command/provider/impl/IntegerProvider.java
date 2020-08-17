package net.ultragrav.command.provider.impl;

import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;

import java.util.Collections;
import java.util.List;

public final class IntegerProvider extends UltraProvider<Integer> {
	@Getter
	private static final IntegerProvider instance = new IntegerProvider();

	@Override
	public Integer convert(@NonNull final String toConvert) throws CommandException {
		try {
			return Integer.parseInt(toConvert);
		} catch (NumberFormatException exception) {
			throw new CommandException("Required: Integer, Given: '" + toConvert + "'");
		}
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "integer number";
	}
}