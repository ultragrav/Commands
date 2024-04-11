package net.ultragrav.command.registry.minestom.sender

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import net.ultragrav.chat.components.Component
import net.ultragrav.chat.converters.KyoriConverter
import net.ultragrav.command.wrapper.sender.UltraSender
import java.util.*

class PlayerMinestom(val wrappedPlayer: Player) : SenderMinestom(wrappedPlayer) {
    override fun getWrappedObject(): Player {
        return wrappedPlayer
    }

    override fun getUniqueId(): UUID {
        return wrappedPlayer.uuid
    }
}