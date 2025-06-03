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
    fun getDisplayName(): String {
        // Ensure quality is included in the title's color formatting, but not as a prefix
        return when (quality.lowercase()) {
            "ascendant" -> {
                "§x§E§E§8§8§0§0§lA" +
                        "§x§D§E§A§1§0§0§ls" +
                        "§x§E§4§C§1§0§0§lc" +
                        "§x§E§4§D§4§4§9§le" +
                        "§x§E§4§D§B§8§D§ln" +
                        "§x§E§B§D§A§4§C§ld" +
                        "§x§D§8§B§7§0§0§la" +
                        "§x§D§E§A§1§0§0§ln" +
                        "§x§F§1§8§9§0§0§lt §6Title: §8[§7$title§8]" // Only title with Ascendant color
            }
            "legendary" -> {
                "§6§lLegendary Title: §8[§7$title§8]§f" // Proper color formatting without prefix
            }
            "mythic" -> {
                "§5Mythic Title: §8[§7$title§8]§f" // Apply proper formatting for mythic titles
            }
            "rare" -> {
                "§2Rare Title: §8[§7$title§8]§f" // Apply proper formatting for rare titles
            }
            "unobtainable" -> {
                "§f§r家 §d§lTitle: §f§8[§7$title§8]§f"
            }
            else -> {
                "§7Basic Title: §8[§7$title§8]"  // Default for all other qualities
            }
        }
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

        // Manually set the display name and ensure it's not italicized
        val displayName = getDisplayName()

        // Explicitly use ChatColor codes to prevent italic and add the desired style
        meta.setDisplayName(displayName)

        // Lore creation: Add the quality tag with the proper formatting
        val lore = mutableListOf<String>()
        if (registered) {
            lore.add("§cRegistered")
            lore.add("§7Messages Sent: $messagesSent")

        }

        // Adding the "Messages Sent" line

        // Adding tags section for item
        val tags = mutableListOf<String>()
        tags.add("Tradable")
        tags.add("Marketable")
        if (registered) tags.add("Registered")

        val tagTextColor = "§x§7§7§8§E§9§A" // Ensure tag colors
        val coloredTags = tags.joinToString(", ") { "$tagTextColor$it" }
        lore.add("${"§x§5§A§7§6§8§3"}Tags: $coloredTags")

        // Apply the final lore to the item meta
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
