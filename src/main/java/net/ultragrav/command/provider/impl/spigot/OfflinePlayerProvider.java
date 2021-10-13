package net.ultragrav.command.provider.impl.spigot;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Provider to get an offline player, some methods used are deprecated
 * may not work in newer versions of spigot
 */
public class OfflinePlayerProvider extends UltraProvider<OfflinePlayer> {
    @Getter
    private static final OfflinePlayerProvider instance = new OfflinePlayerProvider();

    private OfflinePlayerProvider() {}

    @Override
    public OfflinePlayer convert(@NonNull String s, UltraSender sender) throws CommandException {
        OfflinePlayer op = Bukkit.getOfflinePlayer(s);
        if (op == null || (!op.isOnline() && !op.hasPlayedBefore())) {
            throw new CommandException("&cPlayer not found.");
        }
        return op;
    }

    @Override
    public List<String> tabComplete(@NonNull String toComplete, UltraSender sender) {
        List<String> toSend = Lists.newArrayList();

        for (OfflinePlayer player : Bukkit.getOfflinePlayers())
            if ((player.isOnline() || player.hasPlayedBefore()) && player.getName().toLowerCase().startsWith(toComplete))
                toSend.add(player.getName());

        return toSend;
    }

    @Override
    public String getArgumentDescription() {
        return "player";
    }
}
