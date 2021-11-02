package net.ultragrav.command.registry.velocity;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import lombok.Getter;
import net.ultragrav.command.UltraCommand;
import net.ultragrav.command.util.ArrayUtils;

import java.util.Arrays;
import java.util.List;

public class ExecutorVelocity implements Command {
    @Getter
    private final UltraCommand command;

    public ExecutorVelocity(UltraCommand command) {
        this.command = command;
    }

    @Override
    public void execute(CommandSource commandSource, String[] strings) {
        command.execute(UtilVelocity.wrap(commandSource), command.getAliases().get(0), Arrays.asList(strings));
    }

    @Override
    public List<String> suggest(CommandSource source, String[] currentArgs) {
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
