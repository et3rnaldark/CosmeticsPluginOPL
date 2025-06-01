package com.solecloth7.cosmeticsPluginOPL.command.modify

import com.solecloth7.cosmeticsPluginOPL.admin.AdminBackpackSession
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TitleModifyCommand {
    fun handle(sender: CommandSender, args: List<String>) {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command.")
            return
        }

        val target = AdminBackpackSession.getTarget(sender.uniqueId)?.let { Bukkit.getPlayer(it) }
        val index = AdminBackpackSession.getSelectedSlot(sender.uniqueId)
        if (target == null || index == null) {
            sender.sendMessage("§cYou must be selecting a cosmetic first.")
            return
        }

        val cosmetic = CosmeticManager.getCosmetics(target).getOrNull(index) as? TitleCosmetic
        if (cosmetic == null) {
            sender.sendMessage("§cSelected item is not a title cosmetic.")
            return
        }

        when (args.getOrNull(0)?.lowercase()) {
            "title" -> {
                val newTitle = args.drop(1).joinToString(" ").take(30)
                cosmetic.title = newTitle
                CosmeticManager.updateTitleCosmetic(target, cosmetic)
                sender.sendMessage("§aSet title to: §7$newTitle")
            }
            "quality" -> {
                val newQuality = args.getOrNull(1)?.lowercase()
                if (newQuality !in listOf("basic", "legendary", "mythic", "ascendant", "rare", "unobtainable")) {
                    sender.sendMessage("§cInvalid quality.")
                    return
                }
                cosmetic.quality = newQuality!!
                CosmeticManager.updateTitleCosmetic(target, cosmetic)
                sender.sendMessage("§aUpdated quality to $newQuality.")
            }
            "registered" -> {
                val value = args.getOrNull(1)?.toBooleanStrictOrNull()
                if (value == null) {
                    sender.sendMessage("§cInvalid value. Use true or false.")
                    return
                }
                cosmetic.registered = value
                CosmeticManager.updateTitleCosmetic(target, cosmetic)
                sender.sendMessage("§aSet registered to $value.")
            }
            else -> {
                sender.sendMessage("§cInvalid modify argument. Use: title, quality, registered")
            }
        }
    }
}
