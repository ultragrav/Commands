package net.ultragrav.command.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Used when we want to force stop our command, and the we can get the error message to send to the command sender.
 */
@RequiredArgsConstructor
public final class CommandException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Message we are sending to the player.
	 */
	@Getter
	private final String message;
}