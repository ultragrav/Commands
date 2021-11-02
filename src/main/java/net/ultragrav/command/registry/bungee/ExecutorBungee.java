package net.ultragrav.command.registry.bungee;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.util.ArrayUtils;

import java.util.Collections;
import java.util.List;

public class ExecutorBungee extends Command implements TabExecutor {
    @Getter
    private final UltraCommand command;

    public ExecutorBungee(UltraCommand command) {
        super(command.getAliases().get(0), null, command.getAliases().toArray(new String[0]));
        this.command = command;
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        command.execute(UtilBungee.wrap(commandSender), command.getAliases().get(0), Lists.newArrayList(args));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        List<String> args = ArrayUtils.listNonNull(strings);
        List<String> ret = this.getCommand().getTabCompletions(UtilBungee.wrap(commandSender), args);

//        int retSize = ret.size();
//        int maxSize = 20;
//        if (retSize > maxSize) {
//            return Collections.emptyList();
//        }
        return ret;
    }
}
