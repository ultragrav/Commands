package net.ultragrav.command.registry.velocity;

import com.google.common.collect.Lists;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.util.ArrayUtils;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collections;
import java.util.List;

public class ExecutorVelocity implements Command {
    @Getter
    private final UltraCommand command;

    public ExecutorVelocity(UltraCommand command) {
        this.command = command;
    }

    @Override
    public void execute(CommandSource commandSource, String @NonNull [] strings) {
        command.execute(UtilVelocity.wrap(commandSource), Lists.newArrayList(strings));
    }

    @Override
    public List<String> suggest(CommandSource source, String @NonNull [] currentArgs) {
        List<String> args = ArrayUtils.listNonNull(currentArgs);
        List<String> ret = this.getCommand().getTabCompletions(UtilVelocity.wrap(source), args);

//        int retSize = ret.size();
//        int maxSize = 20;
//        if (retSize > maxSize) {
//            return Collections.emptyList();
//        }
        return ret;
    }
}
