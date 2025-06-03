package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import com.solecloth7.cosmeticsPluginOPL.util.ColorUtil
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.*

sealed class NicknameTicketCosmetic {
    abstract val id: UUID
    abstract fun toItem(): ItemStack

    data class Unused(
        override val id: UUID = UUID.randomUUID(),
        val customModelData: Int = 40007
    ) : NicknameTicketCosmetic() {
        override fun toItem(): ItemStack {
            val item = ItemStack(Material.PAPER)
            val meta = item.itemMeta!!
            meta.setCustomModelData(customModelData)
            meta.setDisplayName("§7Nickname Ticket")
            meta.lore = listOf(
                "§7Use this ticket to set a custom nickname.",
                "§7Click to redeem."
            )
            item.itemMeta = meta
            return item
        }
    }

    data class Used(
        override val id: UUID = UUID.randomUUID(),
        var nickname: String,
        var quality: String = "basic",
        var registered: Boolean = false,
        var gradientName: String? = null,
        var gradientHex: List<String> = emptyList(),
        var gradientBold: Boolean = false,
        var lore: List<String>? = null,
        val customModelData: Int = 40008
    ) : NicknameTicketCosmetic() {
        override fun toItem(): ItemStack {
            val item = ItemStack(Material.PAPER)
            val meta = item.itemMeta!!

            val name = if (!gradientName.isNullOrEmpty() && gradientHex.isNotEmpty()) {
                ColorUtil.gradientName(gradientName!!, gradientHex, gradientBold)
            } else {
                nickname
            }

            meta.setCustomModelData(customModelData)
            meta.setDisplayName("§7Nickname: $name")

            val newLore = lore?.toMutableList() ?: mutableListOf()
            if (newLore.none { it.contains("Level") }) {
                newLore.add("§8Sets your display name to: §f~$nickname")
                newLore.add("§7Click to equip this nickname.")
                newLore.add("§8Level §71§f§8 ${quality.replaceFirstChar { it.uppercase() }} Nickname")
                if (registered) newLore.add("§c❤️ Messages Sent: 0")
            }

            meta.lore = newLore
            item.itemMeta = meta
            return item
        }
    }

    companion object {
        fun fromItem(item: ItemStack): NicknameTicketCosmetic? {
            val meta = item.itemMeta ?: return null
            return when (meta.customModelData) {
                40007 -> Unused()
                40008 -> {
                    val displayName = meta.displayName ?: return null
                    val nickname = displayName.removePrefix("§7Nickname: ")
                    Used(nickname = nickname)
                }
                else -> null
            }
        }
    }
}
