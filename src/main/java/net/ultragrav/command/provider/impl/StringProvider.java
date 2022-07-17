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
public final class StringProvider extends UltraProvider<String> {
	@Getter
	private static final StringProvider instance = new StringProvider();

	@Override
	public String convert(List<String> toConvert, UltraSender sender) throws CommandException {
		String first = toConvert.remove(0);
		if (first.charAt(0) != '"') return first;
		StringBuilder builder = new StringBuilder(first.substring(1));
		while (!toConvert.isEmpty()) {
			String next = toConvert.remove(0);
			if (next.charAt(next.length() - 1) == '"') {
				builder.append(next, 0, next.length() - 1);
				return builder.toString();
			}
			builder.append(next);
		}
		throw new CommandException("Unclosed string");
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