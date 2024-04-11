package net.ultragrav.command.registry.minestom.sender

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player
import net.ultragrav.chat.components.Component
import net.ultragrav.chat.converters.LegacyConverter
import net.ultragrav.command.wrapper.sender.UltraSender
import java.util.*

open class SenderMinestom(val wrapped: CommandSender) : UltraSender {
    override fun getWrappedObject(): CommandSender {
        return wrapped
    }

    override fun sendMessage(msg: Component) {
        wrapped.sendMessage(LegacyComponentSerializer.legacySection().deserialize(LegacyConverter.MINECRAFT.convert(msg)))
    }

    override fun sendMessage(msg: String) {
        wrapped.sendMessage(msg)
    }

    override fun hasPermission(perm: String): Boolean {
        return wrapped.hasPermission(perm)
    }

    override fun getUniqueId(): UUID? {
        return if (wrapped is Player) wrapped.uuid else null
    }
}