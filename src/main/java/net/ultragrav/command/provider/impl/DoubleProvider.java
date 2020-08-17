package net.ultragrav.command.provider.impl;

import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;

import java.util.Collections;
import java.util.List;

public final class DoubleProvider extends UltraProvider<Double> {
	@Getter
	private static final DoubleProvider instance = new DoubleProvider();

	@Override
	public Double convert(@NonNull final String toConvert) throws CommandException {
		try {
			return Double.parseDouble(toConvert);
		} catch (NumberFormatException exception) {
			throw new CommandException("Required: decimal number, provided '" + toConvert + "'");
		}
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "decimal number";
	}
}