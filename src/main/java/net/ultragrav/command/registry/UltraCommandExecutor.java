package net.ultragrav.command.registry;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

/**
 * A wrapper for the UltraCommand class that allows it to be registered to bukkit.
 */
public final class UltraCommandExecutor extends Command {
	@Getter
	private final UltraCommand command;

	public UltraCommandExecutor(String label, UltraCommand command) {
		super(label);
		this.command = command;
		if (!command.getAliases().isEmpty()) this.setAliases(command.getAliases());
	}

	@Override
	public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
		command.execute(sender, Lists.newArrayList(args));
		return false;
	}


	@Override
	public List<String> tabComplete(final CommandSender sender, final String alias, final String[] rawArgs) throws IllegalArgumentException {
		List<String> args = Lists.newArrayList();
		for (int i = 0; i < rawArgs.length - 1; i++) {
			String str = rawArgs[i];
			if (str == null) continue;
			if (str.isEmpty()) continue;
			args.add(str);
		}
		args.add(rawArgs[rawArgs.length - 1]);
		List<String> ret = this.getCommand().getTabCompletions(sender, args);

		int retSize = ret.size();
		int maxSize = 20;
		if (retSize > maxSize) {
			return Collections.emptyList();
		}
		return ret;
	}
}