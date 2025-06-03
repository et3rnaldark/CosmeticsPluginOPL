package com.solecloth7.cosmeticsPluginOPL.command.sub.item
import com.solecloth7.cosmeticsPluginOPL.command.modify.NicknamePaintModifyCommand

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
            "nickname_paint" -> NicknamePaintModifyCommand.handle(sender, args.drop(1))

            else -> sender.sendMessage("§cUnsupported cosmetic type: $type")
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        return when (args.size) {
            1 -> listOf("chat_color", "nickname_ticket", "title").filter { it.startsWith(args[0], ignoreCase = true) }

            2 -> when (args[0].lowercase()) {
                "chat_color" -> listOf("paint", "quality", "registered", "note_0", "note_1", "note_2", "note_3", "note_4", "crown", "level")
                    .filter { it.startsWith(args[1], ignoreCase = true) }

                "nickname_ticket" -> listOf("nickname", "quality", "registered")
                    .filter { it.startsWith(args[1], ignoreCase = true) }

                "title" -> listOf("title", "quality", "registered")
                    .filter { it.startsWith(args[1], ignoreCase = true) }
                "nickname_paint" -> listOf("paint", "quality")
                    .filter { it.startsWith(args[1], ignoreCase = true)}
                else -> emptyList()
            }
            2 -> when(args[0].lowercase()) {
                "chat_color" -> if(args[1].lowercase() == "quality") {
                    listOf("basic", "legendary", "mythic", "ascendant", "rare", "unobtainable")
                        .filter { it.startsWith(args[2], ignoreCase = true) }
                } else emptyList()
                else -> emptyList()
            }
            3 -> when (args[0].lowercase()) {
                "title" -> if (args[1].lowercase() == "quality") {
                    listOf("basic", "legendary", "mythic", "ascendant", "rare", "unobtainable")
                        .filter { it.startsWith(args[2], ignoreCase = true) }

                } else emptyList()
                else -> emptyList()
            }

            else -> emptyList()
        }
    }
}
