package net.ultragrav.command.provider.impl.spigot;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerProvider extends UltraProvider<Player> {
	@Getter
	private static final PlayerProvider instance = new PlayerProvider();

	@Override
	public Player convert(@NonNull final String toConvert) throws CommandException {

		Player player = Bukkit.getPlayer(toConvert);

		if (player != null && player.isOnline()) {
			return player;
		}

		throw new CommandException("No player online with name '" + toConvert + "'.");
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete) {
		List<String> toSend = Lists.newArrayList();

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