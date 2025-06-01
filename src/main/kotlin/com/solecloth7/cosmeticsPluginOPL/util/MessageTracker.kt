package com.solecloth7.cosmeticsPluginOPL.util

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import org.bukkit.entity.Player
import java.util.*

object MessageTracker {
    private val messageCounts = mutableMapOf<UUID, MutableMap<ChatColorCosmetic, Int>>()

    fun increment(player: Player, cosmetic: ChatColorCosmetic) {
        val map = messageCounts.computeIfAbsent(player.uniqueId) { mutableMapOf() }
        map[cosmetic] = map.getOrDefault(cosmetic, 0) + 1
    }

    fun getCount(player: Player, cosmetic: ChatColorCosmetic): Int {
        return messageCounts[player.uniqueId]?.getOrDefault(cosmetic, 0) ?: 0
    }

    fun clear(player: Player) {
        messageCounts.remove(player.uniqueId)
    }
}
