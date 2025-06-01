package com.solecloth7.cosmeticsPluginOPL.command.sub.item

import com.solecloth7.cosmeticsPluginOPL.command.AdminSubcommand
import com.solecloth7.cosmeticsPluginOPL.command.modify.ChatColorModifyCommand
import com.solecloth7.cosmeticsPluginOPL.command.modify.NicknameTicketModifyCommand
import com.solecloth7.cosmeticsPluginOPL.command.modify.TitleModifyCommand
import org.bukkit.command.CommandSender

class ItemModifySubcommand : AdminSubcommand {
    override val name = "modify"
    override val description = "Modify a selected cosmetic's properties."

    override fun execute(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /a item modify <chat_color|nickname_ticket|title> [...]")
            return
        }

        when (val type = args[0].lowercase()) {
            "chat_color" -> ChatColorModifyCommand.handle(sender, args.drop(1))
            "nickname_ticket" -> NicknameTicketModifyCommand.handle(sender, args.drop(1))
            "title" -> TitleModifyCommand.handle(sender, args.drop(1))
            else -> sender.sendMessage("§cUnsupported cosmetic type: $type")
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        return when (args.size) {
            1 -> listOf("chat_color", "nickname_ticket", "title").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> when (args[0].lowercase()) {
                "chat_color" -> listOf("paint", "quality", "registered", "note_0", "note_1", "note_2", "note_3", "note_4", "crown", "level")
                "nickname_ticket" -> listOf("nickname", "quality", "registered")
                "title" -> listOf("title", "quality", "registered")
                else -> emptyList()
            }
            3 -> {
                when (args[0].lowercase()) {
                    "title" -> listOf("basic", "legendary", "mythic", "ascendant", "rare", "unobtainable")
                    else -> emptyList()
                }
            }
            else -> emptyList()
        }
    }
}
