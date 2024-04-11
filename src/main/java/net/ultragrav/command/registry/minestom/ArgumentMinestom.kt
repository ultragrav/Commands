package net.ultragrav.command.registry.minestom

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import net.minestom.server.utils.binary.BinaryWriter
import net.ultragrav.command.UltraCommand

class ArgumentMinestom(val command: UltraCommand) : Argument<Unit>("args", true, true) {
    init {
        setSuggestionCallback { sender, context, suggestion ->
            val split = context.input.split(" ").drop(1).toMutableList()
            val sugs = command.getTabCompletions(UtilMinestom.wrap(sender), split)
            sugs.forEach {
                suggestion.addEntry(SuggestionEntry(it))
            }
        }
    }

    override fun parse(p0: CommandSender, p1: String) {}

    override fun parser(): String {
        return "brigadier:string"
    }

    override fun nodeProperties(): ByteArray? {
        return BinaryWriter.makeArray { packetWriter: BinaryWriter ->
            packetWriter.writeVarInt(
                1
            )
        }
    }
}