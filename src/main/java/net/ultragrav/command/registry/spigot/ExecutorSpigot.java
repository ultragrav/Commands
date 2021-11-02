package net.ultragrav.command.registry.spigot;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.util.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A wrapper for the UltraCommand class that allows it to be registered to bukkit.
 */
public final class ExecutorSpigot extends Command {
	@Getter
	private final UltraCommand command;

	public ExecutorSpigot(String label, UltraCommand command) {
		super(label);
		this.command = command;
		List<String> aliases = new ArrayList<>(command.getAliases());
		aliases.remove(label);
		if (!aliases.isEmpty()) this.setAliases(aliases);
	}

	@Override
	public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
		command.execute(UtilSpigot.wrap(sender), commandLabel, Lists.newArrayList(args));
		return false;
	}


	@Override
	public List<String> tabComplete(final CommandSender sender, final String alias, final String[] rawArgs) throws IllegalArgumentException {
		List<String> args = ArrayUtils.listNonNull(rawArgs);
		List<String> ret = this.getCommand().getTabCompletions(UtilSpigot.wrap(sender), args);

//		int retSize = ret.size();
//		int maxSize = 20;
//		if (retSize > maxSize) {
//			return Collections.emptyList();
//		}
		return ret;
	}
}