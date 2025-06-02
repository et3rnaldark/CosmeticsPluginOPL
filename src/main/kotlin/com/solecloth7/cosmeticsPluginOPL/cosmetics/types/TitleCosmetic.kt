package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import com.solecloth7.cosmeticsPluginOPL.util.ColorUtil
import java.util.UUID

data class TitleCosmetic(
    val id: UUID = UUID.randomUUID(),
    var title: String = "Untitled",
    var quality: String = "basic",
    var registered: Boolean = false,
    var messagesSent: Int = 0
) {
    // Get display name with quality before title, with color gradient applied
    fun getDisplayName(): String {
        // This part is for displaying the quality in the backpack but not in chat
        val preview = when (quality.lowercase()) {
            "ascendant" -> {
                // Gradient for Ascendant titles, showing quality in backpack
                "§x§E§E§8§8§0§0§lA" +
                        "§x§D§E§A§1§0§0§ls" +
                        "§x§E§4§C§1§0§0§lc" +
                        "§x§E§4§D§4§4§9§le" +
                        "§x§E§4§D§B§8§D§ln" +
                        "§x§E§B§D§A§4§C§ld" +
                        "§x§D§8§B§7§0§0§la" +
                        "§x§D§E§A§1§0§0§ln" +
                        "§x§F§1§8§9§0§0§lt §r$title"
            }
            "unobtainable" -> {
                "§f§r家 §d§lUnobtainable Title: $title"
            }
            "basic" -> {
                "§7Basic Title: $title"
            }
            "legendary" -> {
                "§6§lLegendary Title: $title"
            }
            "mythic" -> {
                "§5Mythic Title: $title"
            }
            "rare" -> {
                "§2Rare Title: $title"
            }
            else -> {
                "§7Title: $title"
            }
        }

        return title
    }


    fun getLore(): List<String> {
        return listOfNotNull(
            "§7Title: $title",
            "§7Quality: $quality",
            if (registered) "§cRegistered" else null
        )
    }

    fun toItem(): ItemStack {
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta!!

        // Add a unique custom model data based on quality
        val modelData = when (quality.lowercase()) {
            "ascendant" -> 40005
            "unobtainable" -> 40006
            else -> 40009
        }

        meta.setCustomModelData(modelData)
        meta.setDisplayName(getDisplayName())

        val lore = getLore().toMutableList()

        // Adding the title quality info
        lore.add("§7Messages Sent: $messagesSent")

        meta.lore = lore
        item.itemMeta = meta
        return item
    }

    // Get level name based on messages sent
    fun getLevelName(): String {
        return when {
            messagesSent >= 1000 -> "Transcendent"
            messagesSent >= 500 -> "Radiant"
            messagesSent >= 100 -> "Refined"
            messagesSent >= 10 -> "Basic"
            else -> "Ostentatious"
        }
    }
}
