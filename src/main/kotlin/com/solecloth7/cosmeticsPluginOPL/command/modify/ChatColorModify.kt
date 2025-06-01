package com.solecloth7.cosmeticsPluginOPL.command.modify

import com.solecloth7.cosmeticsPluginOPL.admin.AdminBackpackSession
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ChatColorModifyCommand {

    fun handle(sender: CommandSender, args: List<String>) {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command.")
            return
        }

        val targetUUID = AdminBackpackSession.getTarget(sender.uniqueId)
        val target = targetUUID?.let { Bukkit.getPlayer(it) }
        if (target == null) {
            sender.sendMessage("§cYou must be viewing a player's backpack using /a item select <player>.")
            return
        }

        val index = AdminBackpackSession.getSelectedSlot(sender.uniqueId)
        if (index == null) {
            sender.sendMessage("§cYou must select a cosmetic slot first.")
            return
        }

        val cosmetic = CosmeticManager.getCosmetics(target).getOrNull(index)
        if (cosmetic !is ChatColorCosmetic) {
            sender.sendMessage("§cSelected item is not a chat color cosmetic.")
            return
        }

        when (val key = args.getOrNull(0)?.lowercase()) {
            "paint" -> {
                val hexes = args.getOrNull(1)?.split(",") ?: emptyList()
                if (hexes.isEmpty() || hexes.any { !it.matches(Regex("^#[0-9a-fA-F]{6}$")) }) {
                    sender.sendMessage("§cInvalid hex color format. Use #RRGGBB,#RRGGBB,...")
                    return
                }

                val rawArgs = args.drop(2)
                val bold = rawArgs.any { it.equals("bold", ignoreCase = true) }
                val nameParts = rawArgs.filterNot { it.equals("bold", ignoreCase = true) }
                val name = if (nameParts.isEmpty()) "Custom" else nameParts.joinToString(" ")

                cosmetic.hexColors = hexes
                cosmetic.text = name
                cosmetic.bold = bold
                CosmeticManager.updateCosmetic(target, cosmetic)
                sender.sendMessage("§aUpdated paint to §f$name §awith gradient §f$hexes")
            }

            "quality" -> {
                val quality = args.getOrNull(1)
                if (quality == null || quality.lowercase() !in listOf("basic", "mythic", "legendary", "ascendant", "rare", "unobtainable")) {
                    sender.sendMessage("§cInvalid quality. Must be one of: basic, mythic, legendary, ascendant, rare, unobtainable.")
                    return
                }
                cosmetic.quality = quality
                CosmeticManager.updateCosmetic(target, cosmetic)
                sender.sendMessage("§aUpdated cosmetic quality to $quality.")
            }

            "registered" -> {
                val value = args.getOrNull(1)?.toBooleanStrictOrNull()
                if (value == null) {
                    sender.sendMessage("§cInvalid value. Use true or false.")
                    return
                }
                cosmetic.registered = value
                CosmeticManager.updateCosmetic(target, cosmetic)
                sender.sendMessage("§aSet registered to $value.")
            }

            in listOf("note_0", "note_1", "note_2", "note_3", "note_4") -> {
                val noteIndex = key!!.removePrefix("note_").toIntOrNull()
                if (noteIndex == null || noteIndex !in 0..4) {
                    sender.sendMessage("§cInvalid note index.")
                    return
                }
                val value = args.drop(1).joinToString(" ").trim()
                cosmetic.notes[noteIndex] = value
                CosmeticManager.updateCosmetic(target, cosmetic)
                sender.sendMessage("§aUpdated Note $noteIndex to: §f$value")
            }

            else -> {
                sender.sendMessage("§cInvalid modify command. Options: paint, quality, registered, note_0...note_4")
            }
        }
    }
}
