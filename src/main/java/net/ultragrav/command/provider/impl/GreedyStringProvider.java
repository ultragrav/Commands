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
 * Provider for a string
 * Allows any string input (no checks are done)
 *
 * Does not support tab completion
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GreedyStringProvider extends UltraProvider<String> {
	@Getter
	private static final GreedyStringProvider instance = new GreedyStringProvider();

	@Override
	public String convert(List<String> toConvert, UltraSender sender) throws CommandException {
		return String.join(" ", toConvert);
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete, UltraSender sender) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "string";
	}
}