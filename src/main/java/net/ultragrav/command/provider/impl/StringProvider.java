package net.ultragrav.command.provider.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringProvider extends UltraProvider<String> {
	@Getter
	private static final StringProvider instance = new StringProvider();

	@Override
	public String convert(@NonNull final String toConvert) throws CommandException {
		return toConvert;
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "string";
	}
}