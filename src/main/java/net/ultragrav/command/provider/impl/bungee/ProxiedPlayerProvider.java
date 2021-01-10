package net.ultragrav.command.provider.impl.bungee;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.ultragrav.command.exception.CommandException;
import net.ultragrav.command.provider.UltraProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public final class ProxiedPlayerProvider extends UltraProvider<ProxiedPlayer> {
	@Getter
	private static final ProxiedPlayerProvider instance = new ProxiedPlayerProvider();

	@Override
	public ProxiedPlayer convert(@NonNull final String toConvert) throws CommandException {
		ProxiedPlayer player = ProxyServer.getInstance().getPlayer(toConvert);

		if (player != null && player.isConnected()) {
			return player;
		}

		throw new CommandException("No player online with name '" + toConvert + "'.");
	}

	@Override
	public List<String> tabComplete(@NonNull final String toComplete) {
		List<String> toSend = Lists.newArrayList();

		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
			if (player.getName().toLowerCase().startsWith(toComplete))
				toSend.add(player.getName());

		return toSend;
	}

	@Override
	public String getArgumentDescription() {
		return "player";
	}
}