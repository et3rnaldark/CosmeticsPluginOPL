package com.solecloth7.cosmeticsPluginOPL

import org.bukkit.entity.Player
import java.util.UUID

object AdminSelectionManager {
    private val selectedCosmeticSlots = mutableMapOf<UUID, Int>()

    fun select(admin: Player, target: Player, slot: Int) {
        selectedCosmeticSlots[admin.uniqueId] = slot
        admin.sendMessage("§bSelected slot $slot from ${target.name}'s backpack.")
    }

    fun getSelectedSlot(admin: Player): Int? = selectedCosmeticSlots[admin.uniqueId]
    fun clear(admin: Player) { selectedCosmeticSlots.remove(admin.uniqueId) }
}
