package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import com.solecloth7.cosmeticsPluginOPL.util.ItemStackUtil
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.UUID

data class TitleCosmetic(
    val id: UUID = UUID.randomUUID(),
    var title: String = "Untitled",
    var quality: String = "basic",
    var registered: Boolean = false,
    var messagesSent: Int = 0
) {
    fun getDisplayName(): String {
        return "§8[§7$title§8]"
    }

    fun getLore(): List<String> {
        return listOfNotNull(
            "§7Title: $title",
            "§7Quality: $quality",
            if (registered) "§cRegistered" else null
        )
    }

    fun toItem(): ItemStack {
        return ItemStackUtil.create(
            Material.PAPER,
            getDisplayName(),
            getLore(),
            40009
        )
    }

    fun getLevelName(): String {
        return when {
            messagesSent >= 1000 -> "Trascendent"
            messagesSent >= 500 -> "Radiant"
            messagesSent >= 100 -> "Refined"
            messagesSent >= 10 -> "Basic"
            else -> "Ostentatious"
        }
    }
}