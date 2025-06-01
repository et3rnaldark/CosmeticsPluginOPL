package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.admin.AdminSelectionManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

object AdminBackpackGUI {
    private const val SIZE = 36

    fun open(admin: Player, target: Player) {
        val inv = Bukkit.createInventory(null, SIZE, "Viewing Backpack: ${target.name}")
        val cosmetics = CosmeticManager.getCosmetics(target)
        for ((i, cosmetic) in cosmetics.withIndex()) {
            if (i < SIZE) {
                inv.setItem(i, cosmetic.toItem())
            }
        }
        for (i in cosmetics.size until SIZE) {
            inv.setItem(i, ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
                val meta = itemMeta
                meta?.setDisplayName("§7Cosmetic Slot")
                itemMeta = meta
            })
        }
        AdminBackpackSession.set(admin.uniqueId, target.uniqueId, allowSelect = true)
        admin.openInventory(inv)
    }

    object AdminBackpackSession {
        val currentlyViewing = mutableMapOf<UUID, Pair<UUID, Boolean>>()

        fun set(adminId: UUID, targetId: UUID, allowSelect: Boolean) {
            currentlyViewing[adminId] = targetId to allowSelect
        }

        fun getTarget(adminId: UUID): UUID? = currentlyViewing[adminId]?.first
        fun isSelecting(adminId: UUID): Boolean = currentlyViewing[adminId]?.second ?: false

        fun clear(adminId: UUID) = currentlyViewing.remove(adminId)
    }


    fun handleClick(admin: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        val targetId = AdminBackpackSession.getTarget(admin.uniqueId)
        if (targetId == null) {
            println("❌ No target stored in AdminBackpackSession for ${admin.name}")
            return
        }

        val target = Bukkit.getPlayer(targetId)
        if (target == null) {
            println("❌ Target player is not online: $targetId")
            return
        }

        if (event.clickedInventory != event.view.topInventory) {
            println("❌ Click wasn't in top inventory")
            return
        }

        val clicked = event.currentItem
        if (clicked == null || clicked.type != Material.PAPER) {
            println("❌ Clicked item is null or not PAPER")
            return
        }

        val cosmetics = CosmeticManager.getCosmetics(target)
        val cosmetic = cosmetics.getOrNull(event.slot)
        if (cosmetic == null) {
            println("❌ No cosmetic found at slot ${event.slot}")
            return
        }

        println("✅ Selection successful: ${cosmetic.text}")
        if (!AdminBackpackSession.isSelecting(admin.uniqueId)) {
            admin.sendMessage("§cYou are only viewing this backpack and cannot select items.")
            return
        }

        AdminSelectionManager.select(admin.uniqueId, target.uniqueId, event.slot)
        admin.sendMessage("§aSelected '${cosmetic.text}' from ${target.name}'s backpack!")
        admin.closeInventory()
        admin.sendMessage("§aSelected '${cosmetic.text}' from ${target.name}'s backpack!")
        admin.closeInventory()
    }
}
