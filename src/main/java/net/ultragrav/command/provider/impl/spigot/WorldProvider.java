package net.ultragrav.command.provider.impl.spigot;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public final class WorldProvider extends UltraProvider<World> {
    @Getter
    private static final WorldProvider instance = new WorldProvider();

    @Override
    public World convert(@NonNull final String toConvert) throws CommandException {

        World world = Bukkit.getWorld(toConvert);

        if (world != null)
            return world;

        throw new CommandException("No world found with the name of '" + toConvert + "'.");
    }

    @Override
    public List<String> tabComplete(@NonNull final String toComplete) {

        List<String> ret = Lists.newArrayList();

        for (World world : Bukkit.getWorlds()) {
            if (world.getName().toLowerCase().startsWith(toComplete)) {
                ret.add(world.getName());
            }
        }

        return ret;
    }

    @Override
    public String getArgumentDescription() {
        return "world";
    }
}