package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import com.solecloth7.cosmeticsPluginOPL.util.ColorUtil
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

data class NicknamePaintCosmetic(
    val id: UUID = UUID.randomUUID(),
    var hexColors: List<String> = listOf("#ffffff"),
    var bold: Boolean = false,
    var quality: String = "basic",
    var text: String = "Unknown Nickname Paint"
) {

    fun getDisplayName(): String {
        val raw = when (quality.lowercase()) {
            "mythic" -> "§5Mythic"
            "legendary" -> "§6Legendary"
            "ascendant" -> "§e§lAscendant"
            else -> "§7Basic"
        }

        return "$raw Nickname Paint"
    }

    fun toItemStack(): ItemStack {
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta ?: return item

        meta.setCustomModelData(40009)
        meta.setDisplayName(getDisplayName())
        val gradientPreview = ColorUtil.gradientName("Preview", hexColors, bold)
        meta.lore = listOf("§7Applies to nicknames", "§f$gradientPreview")

        item.itemMeta = meta
        return item
    }

    // ✅ Add this to fix unresolved reference
    fun toItem(): ItemStack = toItemStack()

    companion object {
        fun default(): NicknamePaintCosmetic {
            return NicknamePaintCosmetic(
                hexColors = listOf("#ffffff"),
                bold = false,
                quality = "basic"
            )
        }
    }
}
