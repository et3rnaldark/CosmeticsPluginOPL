package com.solecloth7.cosmeticsPluginOPL.command.modify

import com.solecloth7.cosmeticsPluginOPL.admin.AdminBackpackSession
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object NicknameTicketModifyCommand {
    fun handle(sender: CommandSender, args: List<String>) {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command.")
            return
        }

        val targetUUID = AdminBackpackSession.getTarget(sender.uniqueId)
        val target = targetUUID?.let { Bukkit.getPlayer(it) }
        if (target == null) {
            sender.sendMessage("§cNo player selected.")
            return
        }

        val slot = AdminBackpackSession.getSelectedSlot(sender.uniqueId)
        if (slot == null) {
            sender.sendMessage("§cNo slot selected.")
            return
        }

        val cosmetic = NicknameTicketManager.getCosmetics(target).getOrNull(slot)
        if (cosmetic !is NicknameTicketCosmetic.Used) {
            sender.sendMessage("§cSelected item is not a used nickname ticket.")
            return
        }

        if (args.size < 2) {
            sender.sendMessage("§cUsage: /a item modify nickname_ticket <nickname|quality|registered> <value>")
            return
        }

        val updated = when (args[0].lowercase()) {
            "nickname" -> cosmetic.copy(nickname = args.getOrNull(1) ?: cosmetic.nickname)
            "quality" -> cosmetic.copy(quality = args.getOrNull(1) ?: cosmetic.quality)
            "registered" -> {
                val bool = args.getOrNull(1)?.toBooleanStrictOrNull()
                if (bool == null) {
                    sender.sendMessage("§cValue for 'registered' must be true or false.")
                    return
                }
                cosmetic.copy(registered = bool)
            }
            else -> {
                sender.sendMessage("§cUnknown modifier: ${args[0]}")
                return
            }
        }

        NicknameTicketManager.setCosmetic(target, slot, updated)
        sender.sendMessage("§aUpdated nickname ticket for ${target.name}.")
    }
}
