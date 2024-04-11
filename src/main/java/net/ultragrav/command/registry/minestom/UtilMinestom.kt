package net.ultragrav.command.registry.minestom

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import net.ultragrav.command.registry.minestom.sender.PlayerMinestom
import net.ultragrav.command.registry.minestom.sender.SenderMinestom
import net.ultragrav.command.wrapper.player.impl.PlayerSpigot
import net.ultragrav.command.wrapper.sender.UltraSender
import net.ultragrav.command.wrapper.sender.impl.SenderSpigot
object UtilMinestom {
    fun wrap(sender: CommandSender): UltraSender {
        return if (sender is Player) {
            PlayerMinestom(sender)
        } else {
            SenderMinestom(sender)
        }
    }
}