package net.ultragrav.command.registry.minestom

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandContext
import net.ultragrav.command.UltraCommand

class MinestomCommand(val wrapped: UltraCommand) : Command(wrapped.aliases.first(), *wrapped.aliases.drop(1).toTypedArray()) {
    init {
        addSyntax({ sender: CommandSender, context: CommandContext -> }, ArgumentMinestom(wrapped))
    }

    override fun globalListener(sender: CommandSender, context: CommandContext, command: String) {
        val split = context.input.split(" ")
        wrapped.execute(UtilMinestom.wrap(sender), split[0], split.drop(1).toMutableList())
    }
}