package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

sealed class NicknameTicketCosmetic {
    abstract fun toItem(): ItemStack

    data class Unused(val customModelData: Int = 40007) : NicknameTicketCosmetic() {
        override fun toItem(): ItemStack {
            val item = ItemStack(Material.PAPER)
            val meta = item.itemMeta!!
            meta.setCustomModelData(customModelData)
            meta.setDisplayName("§7Nickname Ticket")
            meta.lore = listOf(
                "§7Use this ticket to set a custom nickname.",
                "§7Click to redeem.",
                )
            item.itemMeta = meta
            return item
        }
    }

    data class Used(
        val nickname: String,
        val quality: String = "basic",
        val registered: Boolean = false,
        val customModelData: Int = 40008
    ) : NicknameTicketCosmetic() {
        override fun toItem(): ItemStack {
            val item = ItemStack(Material.PAPER)
            val meta = item.itemMeta!!
            meta.setCustomModelData(customModelData)
            meta.setDisplayName("§7Nickname: $nickname")

            val lore = mutableListOf<String>()
            lore.add("§8Sets your display name to: §f~$nickname")
            lore.add("§7Click to equip this nickname.")
            lore.add("§8Level §7${(1..100).random()} §8${quality.capitalize()} Nickname")
            if (registered) {
                lore.add("§c❤️ Messages Sent: 0")
            }
            item.itemMeta = meta.apply { this.lore = lore }

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
                    Used(nickname)
                }
                else -> null
            }
        }
    }
}
