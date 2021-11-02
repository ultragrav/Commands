package net.ultragrav.command.provider.impl.spigot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Provider for spigot worlds
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldProvider extends UltraProvider<World> {
    @Getter
    private static final WorldProvider instance = new WorldProvider();

    @Override
    public World convert(@NonNull final String toConvert, UltraSender sender) throws CommandException {

        World world = Bukkit.getWorld(toConvert);

        if (world != null)
            return world;

        throw new CommandException("No world found with the name of '" + toConvert + "'.");
    }

    @Override
    public List<String> tabComplete(@NonNull final String toComplete, UltraSender sender) {

        List<String> ret = new ArrayList<>();

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