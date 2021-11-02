package net.ultragrav.command.provider.impl.spigot;

import lombok.Getter;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.EZProvider;
import net.ultragrav.command.provider.UltraProvider;
import net.ultragrav.command.wrapper.sender.UltraSender;
import org.bukkit.GameMode;

import java.util.HashMap;
import java.util.Map;

public class GameModeProvider extends EZProvider<GameMode> {
    private static final Map<String, GameMode> map = new HashMap<>();
    static {
        map.put("creative", GameMode.CREATIVE);
        map.put("survival", GameMode.SURVIVAL);
        map.put("adventure", GameMode.ADVENTURE);
        map.put("spectator", GameMode.SPECTATOR);
        map.put("1", GameMode.CREATIVE);
        map.put("0", GameMode.SURVIVAL);
        map.put("2", GameMode.ADVENTURE);
        map.put("3", GameMode.SPECTATOR);
        map.put("c", GameMode.CREATIVE);
        map.put("s", GameMode.SURVIVAL);
        map.put("a", GameMode.ADVENTURE);
        map.put("sp", GameMode.SPECTATOR);
    }

    @Getter
    public static final GameModeProvider instance = new GameModeProvider();

    private GameModeProvider() {
        super("gamemode", map);
    }
}
