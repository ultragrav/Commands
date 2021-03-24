package net.ultragrav.command.provider.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Provider for durations / time in multiple formats
 *
 * Does not support tab completion
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DurationProvider extends UltraProvider<Date> {
	@Getter
	private static final DurationProvider instance = new DurationProvider();

	public static long smartParseDuration(String s) {
		if (s.contains(":")) {
			String[] parts = s.split(":");
			String hours = parts[0];
			String minutes = parts[1];
			if (charCount(':', s) >= 2) {
				// hh:mm:ss
				String seconds = parts[2];
				try {
					int h = Integer.parseInt(hours);
					int m = Integer.parseInt(minutes);
					int sec = Integer.parseInt(seconds);
					return ((h * 60L + m) * 60 + sec) * 1000;
				} catch (NumberFormatException ex) {
					return 0;
				}
			} else {
				// hh:mm
				try {
					int h = Integer.parseInt(hours);
					int m = Integer.parseInt(minutes);
					return (h * 60L + m) * 60 * 1000;
				} catch (NumberFormatException ex) {
					return 0;
				}
			}
		} else {
			return parseDurationSimple(s);
		}
	}

	public static long parseDurationSimple(String str) {
		int h = parseTime(str, 'h');
		int m = parseTime(str, 'm');
		int s = parseTime(str, 's');

		if (h == -1 || m == -1 || s == -1) {
			return -1;
		}

		long hoursToMS = (h * 60 * 60 * 1000L);
		long minutesToMS = (m * 60 * 1000L);
		long secondsToMS = (s * 1000L);

		return hoursToMS + minutesToMS + secondsToMS;
	}

	public static int charCount(char c, String s) {
		int y = 0;
		for (char x : s.toCharArray()) {
			if (x == c) {
				y++;
			}
		}
		return y;
	}

	public static int parseTime(String s, char c) {
		s = s.toLowerCase();
		if (s.indexOf(c) > -1) {
			if (charCount(c, s) == 1) {
				int index = s.indexOf(c);
				return getCountAt(s, index);
			} else {
				return -1; // error: can't have more than one occurrence of a time modifier
			}
		}
		return 0;
	}

	public static int getCountAt(String s, int index) {
		if (index > 0) {
			int start = index - 1;
			char[] chars = s.toCharArray();
			while (true) {
				if (chars.length > start && start > 0) {
					char c = chars[start];
					System.out.println(c);
					if (!isTimeModifier(c)) {
						System.out.println(start);
						start--;
						continue;
					} else {
						start++;
					}
				}
				break;
			}
			String countStr = s.substring(start, index);
			try {
				return Integer.parseInt(countStr);
			} catch (NumberFormatException ex) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	public static boolean isTimeModifier(char c) {
		return c == 'h' || c == 'm' || c == 's';
	}


	@Override
	public Date convert(@NonNull final String toConvert, UltraSender sender) throws CommandException {
		try {
			long l = smartParseDuration(toConvert);
			if (l != -1) {
				return new Date(l);
			} else {
				throw new CommandException("Duration must be in format hh:mm or hh:mm:ss or 1h2m3s");
			}
		} catch (Exception ex) {
			throw new CommandException("Duration must be in format hh:mm or hh:mm:ss or 1h2m3s");
		}
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete, UltraSender sender) {
		return Collections.emptyList();
	}

	@Override
	public String getArgumentDescription() {
		return "duration";
	}
}