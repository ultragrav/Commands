package net.ultragrav.command.provider.impl.spigot;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Provider for spigot players
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerProvider extends UltraProvider<Player> {
    @Getter
    private static final PlayerProvider instance = new PlayerProvider();

    @Override
    public Player convert(@NonNull final String toConvert, UltraSender sender) throws CommandException {

        Player player = Bukkit.getPlayer(toConvert);

        if (player != null && player.isOnline()) {
            return player;
        }

        throw new CommandException("No player online with name '" + toConvert + "'.");
    }

    @Override
    public List<String> tabComplete(@NonNull final String toComplete, UltraSender sender) {
        List<String> toSend = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers())
            if (player.getName().toLowerCase().startsWith(toComplete))
                toSend.add(player.getName());

        return toSend;
    }

    @Override
    public String getArgumentDescription() {
        return "player";
    }
}