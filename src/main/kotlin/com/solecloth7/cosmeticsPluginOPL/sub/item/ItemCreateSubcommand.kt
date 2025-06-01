package com.solecloth7.cosmeticsPluginOPL.command.sub.item

import com.solecloth7.cosmeticsPluginOPL.command.AdminSubcommand
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ItemCreateSubcommand : AdminSubcommand {
    override val name = "create"
    override val description = "Creates a new cosmetic item for the given player."

    override fun execute(sender: CommandSender, args: List<String>) {
        if (args.size != 2) {
            sender.sendMessage("§cUsage: /a item create <player> <cosmetic>")
            return
        }

        val targetName = args[0]
        val type = args[1].lowercase()
        val target = Bukkit.getPlayerExact(targetName)

        if (target == null) {
            sender.sendMessage("§cPlayer '$targetName' is not online.")
            return
        }

        when (type) {
            "chat_color" -> {
                CosmeticManager.load(target)
                CosmeticManager.giveCosmetic(target, ChatColorCosmetic.default())
                sender.sendMessage("§aGave default Chat Color to §e${target.name}§a.")
            }

            "nickname_ticket" -> {
                NicknameTicketManager.load(target)
                NicknameTicketManager.giveCosmetic(target, NicknameTicketCosmetic.Unused())
                sender.sendMessage("§aGave Nickname Ticket to §e${target.name}§a.")
            }

            else -> {
                sender.sendMessage("§cUnsupported cosmetic type: $type")
            }
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> listOf("chat_color", "nickname_ticket").filter { it.startsWith(args[1], ignoreCase = true) }
            else -> emptyList()
        }
    }
}
