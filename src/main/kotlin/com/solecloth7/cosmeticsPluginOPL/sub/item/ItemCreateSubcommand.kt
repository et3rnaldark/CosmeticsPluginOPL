package com.solecloth7.cosmeticsPluginOPL.command.sub.item

import com.solecloth7.cosmeticsPluginOPL.command.AdminSubcommand
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknamePaintCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
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
                val cosmetic = ChatColorCosmetic.default()
                CosmeticManager.giveCosmetic(target, cosmetic)
                val index = CosmeticManager.getCosmetics(target).indexOf(cosmetic)
                if (index != -1 && sender is Player) {
                    com.solecloth7.cosmeticsPluginOPL.admin.AdminSelectionManager.select(sender.uniqueId, target.uniqueId, index)
                    sender.sendMessage("§aGave and selected default Chat Color for §e${target.name}§a.")
                }
            }

            "nickname_ticket" -> {
                NicknameTicketManager.load(target)
                val cosmetic = NicknameTicketCosmetic.Unused()
                NicknameTicketManager.giveCosmetic(target, cosmetic)
                val index = NicknameTicketManager.getCosmetics(target).indexOf(cosmetic)
                if (index != -1 && sender is Player) {
                    com.solecloth7.cosmeticsPluginOPL.admin.AdminSelectionManager.select(sender.uniqueId, target.uniqueId, index)
                    sender.sendMessage("§aGave and selected Nickname Ticket for §e${target.name}§a.")
                }
            }

            "title" -> {
                CosmeticManager.load(target)
                val cosmetic = TitleCosmetic()
                CosmeticManager.giveTitleCosmetic(target, cosmetic)
                val index = CosmeticManager.getTitleCosmetics(target).indexOfFirst { it.id == cosmetic.id }
                if (index != -1 && sender is Player) {
                    com.solecloth7.cosmeticsPluginOPL.admin.AdminSelectionManager.select(sender.uniqueId, target.uniqueId, index)
                    sender.sendMessage("§aGave and selected Title Cosmetic for §e${target.name}§a.")
                }
            }
            "nickname_paint" -> {
                CosmeticManager.load(target)
                CosmeticManager.giveNicknamePaint(target, NicknamePaintCosmetic.default())
                sender.sendMessage("§aGave Nickname Paint to §e${target.name}§a.")
            }
            else -> {
                sender.sendMessage("§cUnsupported cosmetic type: $type")
            }
        }
    }
    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        return when (args.size) {
            1 -> Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> listOf("chat_color", "nickname_ticket", "title", "nickname_paint").filter { it.startsWith(args[1], ignoreCase = true) }
            else -> emptyList()
        }
    }
}
