package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import com.solecloth7.cosmeticsPluginOPL.util.ColorUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

data class ChatColorCosmetic(
    var hexColors: List<String> = listOf("#FFFFFF"),
    var text: String = "White",
    var bold: Boolean = false,
    var quality: String = "basic",
    var registered: Boolean = false,
    var messagesSent: Int = 0,
    var alwaysEquipped: Boolean = false,
    var level: Int = (1..100).random(),
    val customModelData: Int = 40500,
    var notes: MutableMap<Int, String> = mutableMapOf(),
    var crown: String? = null

) {
    val milestoneName: String
        get() = when {
            messagesSent >= 1000 -> "Transcendent"
            messagesSent >= 500 -> "Radiant"
            messagesSent >= 100 -> "Refined"
            messagesSent >= 10 -> "Basic"
            else -> "Ostentatious"
        }

    val displayName: String
        get() {
            val preview = ColorUtil.gradientName(text, hexColors, bold)
            return when {
                registered -> {
                    val qualityDisplay = quality.replaceFirstChar { it.uppercase() }
                    if (quality == "ascendant") {
                        "§x§E§E§8§8§0§0§lA" +
                                "§x§D§E§A§1§0§0§ls" +
                                "§x§E§4§C§1§0§0§lc" +
                                "§x§E§4§D§4§4§9§le" +
                                "§x§E§4§D§B§8§D§ln" +
                                "§x§E§B§D§A§4§C§ld" +
                                "§x§D§8§B§7§0§0§la" +
                                "§x§D§E§A§1§0§0§ln" +
                                "§x§F§1§8§9§0§0§lt §r§c§l$milestoneName Chat Color: §r$preview"
                    } else {
                        "§c§lRegistered $qualityDisplay Chat Color: §r$preview"
                    }
                }
                else -> {
                    val prefix = when (quality.lowercase()) {
                        "basic" -> "§7Basic Chat Color: "
                        "legendary" -> "§6§lLegendary Chat Color: "
                        "mythic" -> "§5Mythic Chat Color: "
                        "rare" -> "§2Rare Chat Color: "
                        "unobtainable" -> "§f§r家 §d§lChat Color: §r"
                        "ascendant" -> "§x§E§E§8§8§0§0§lA" +
                                "§x§D§E§A§1§0§0§ls" +
                                "§x§E§4§C§1§0§0§lc" +
                                "§x§E§4§D§4§4§9§le" +
                                "§x§E§4§D§B§8§D§ln" +
                                "§x§E§B§D§A§4§C§ld" +
                                "§x§D§8§B§7§0§0§la" +
                                "§x§D§E§A§1§0§0§ln" +
                                "§x§F§1§8§9§0§0§lt §6§lChat Color: §r"
                        else -> "§7Chat Color: "
                    }
                    prefix + preview
                }
            }
        }

    fun toItem(): ItemStack {
        val item = ItemStack(Material.PAPER)
        val meta = item.itemMeta!!

        val modelData = when (quality.lowercase()) {
            "ascendant" -> 40005
            "unobtainable" -> 40006
            else -> customModelData
        }

        meta.setCustomModelData(modelData)
        meta.setDisplayName(displayName)

        val lore = mutableListOf<String>()
        val gray = "§x§6§E§6§E§6§E"
        val tagLabelColor = "§x§5§A§7§6§8§3" // #5A7683
        val tagTextColor = "§x§7§7§8§E§9§A" // #5C7886

        // Info line
        val infoParts = mutableListOf<String>()
        infoParts.add("${gray}Level §7$level")
        infoParts.add("${gray}${quality.replaceFirstChar { it.uppercase() }}")
        if (registered) infoParts.add("${gray}Registered")
        if (alwaysEquipped) infoParts.add("${gray}Always Equipped")
        infoParts.add("${gray}Chat Color")
        lore.add(infoParts.joinToString(" "))

        // Messages Sent (if registered)
        if (registered) {
            lore.add("§c✎ Messages Sent: $messagesSent")
        }

        // Tags section with hex colors
        val tags = mutableListOf<String>()
        lore.add(" ")
        if (alwaysEquipped) tags.add("Always Equipped")
        tags.add("Tradable")
        tags.add("Marketable")
        if (registered) tags.add("Registered")
        val coloredTags = tags.joinToString(", ") { "$tagTextColor$it" }
        lore.add("${tagLabelColor}Tags: $coloredTags")
        // Notes section
        if (notes.isNotEmpty()) {
            lore.add("") // blank line after tags
            for (i in 0..4) {
                val note = notes[i]
                if (note != null) {
                    lore.add("§7✎ Note $i: §7$note")
                }
            }
        }
        crown?.let {
            lore.add("§eCrown: $it")
        }
        // Footer
        lore.add("§8Click on this Cosmetic to equip!")

        meta.lore = lore
        item.itemMeta = meta
        return item
    }

// Final line

    companion object {
        fun default() = ChatColorCosmetic(
            hexColors = listOf("#FFFFFF"),
            text = "White",
            bold = false,
            quality = "basic",
            registered = false,
            messagesSent = 0,
            alwaysEquipped = false
        )
    }
}
