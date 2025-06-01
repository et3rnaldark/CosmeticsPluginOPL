package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

object AdminCosmeticModifyGUI {
    fun open(admin: Player, target: Player, cosmetic: ChatColorCosmetic) {
        val inv = Bukkit.createInventory(null, 9, "Modify Cosmetic")
        inv.setItem(4, cosmetic.toItem())
        inv.setItem(8, ItemStack(Material.ANVIL).apply {
            val meta = itemMeta
            meta?.setDisplayName("§aModify This Cosmetic")
            itemMeta = meta
        })
        AdminModifySession.data[admin.uniqueId] = Pair(target.uniqueId, cosmetic)
        admin.openInventory(inv)
    }

    object AdminModifySession {
        val data = mutableMapOf<UUID, Pair<UUID, ChatColorCosmetic>>() // adminId -> (targetId, cosmetic)
    }

    fun handleClick(admin: Player, event: InventoryClickEvent) {
        event.isCancelled = true
        if (event.slot == 8) {
            val (targetId, cosmetic) = AdminModifySession.data[admin.uniqueId] ?: return
            val target = Bukkit.getPlayer(targetId) ?: return
            admin.sendMessage("§bType the new values in chat, e.g.: #54F4AD,#4A5CEF Summer 2025: Electric Waves,bold")
        }
    }
}
