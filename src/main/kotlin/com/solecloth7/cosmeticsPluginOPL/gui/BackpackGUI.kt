package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.admin.AdminBackpackSession
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import com.solecloth7.cosmeticsPluginOPL.util.ChatNicknameInputManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object BackpackGUI {
    private const val SIZE = 36
    private const val VIEWING_PREFIX = "Viewing Backpack: "
    private const val SELECTING_PREFIX = "Selecting Backpack: "
    private const val TITLE = "Cosmetic Backpack" // Define the TITLE constant

    fun open(player: Player) {
        openInternal(player, player, false)
    }

    fun open(admin: Player, target: Player, allowSelect: Boolean) {
        AdminBackpackSession.set(admin.uniqueId, target.uniqueId, allowSelect)
        openInternal(admin, target, allowSelect)
    }

    private fun openInternal(viewer: Player, target: Player, allowSelect: Boolean) {
        val title = if (viewer == target) TITLE else {
            if (allowSelect) "$SELECTING_PREFIX${target.name}" else "$VIEWING_PREFIX${target.name}"
        }

        val inv: Inventory = Bukkit.createInventory(null, SIZE, title)
        val chatColors = CosmeticManager.getCosmetics(target)
        val nicknames = NicknameTicketManager.getCosmetics(target)
        val titles = CosmeticManager.getTitleCosmetics(target)
        val allCosmetics = chatColors + nicknames + titles

        for ((i, cosmetic) in allCosmetics.withIndex()) {
            if (i >= SIZE) break
            val item = when (cosmetic) {
                is ChatColorCosmetic -> cosmetic.toItem()
                is NicknameTicketCosmetic -> cosmetic.toItem()
                is TitleCosmetic -> cosmetic.toItem()
                else -> continue
            }
            inv.setItem(i, item)
        }

        for (i in allCosmetics.size until SIZE) {
            inv.setItem(i, ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
                itemMeta = itemMeta?.apply { setDisplayName("§7Cosmetic Slot") }
            })
        }

        viewer.openInventory(inv)
    }

    fun handleClick(player: Player, event: InventoryClickEvent) {
        val slot = event.slot
        val clicked = event.currentItem ?: return
        val inventory = event.clickedInventory ?: return

        if (event.view.title != TITLE) return
        event.isCancelled = true

        val all = mutableListOf<Any>()
        all.addAll(CosmeticManager.getCosmetics(player))
        all.addAll(NicknameTicketManager.getCosmetics(player))
        all.addAll(CosmeticManager.getTitleCosmetics(player))

        // Handle interactions with cosmetics
        val cosmetic = all.getOrNull(slot) ?: return
        when (cosmetic) {
            is ChatColorCosmetic -> EquipGUI.openChatColor(player, cosmetic)
            is NicknameTicketCosmetic.Unused -> {
                player.closeInventory()
                player.sendMessage("§7Enter your desired nickname in chat: ")
                ChatNicknameInputManager.requestInput(player, cosmetic)
            }
            is NicknameTicketCosmetic.Used -> EquipGUI.openNicknameTicket(player, cosmetic)
            is TitleCosmetic -> EquipGUI.openTitle(player, cosmetic)
        }
    }

    fun handleAdminClick(admin: Player, target: Player, event: InventoryClickEvent) {
        event.isCancelled = true
        val clicked = event.currentItem ?: return

        if (clicked.type == Material.PAPER) {
            val combined = CosmeticManager.getCosmetics(target) +
                    NicknameTicketManager.getCosmetics(target) +
                    CosmeticManager.getTitleCosmetics(target)

            val cosmetic = combined.getOrNull(event.slot) ?: return

            AdminBackpackSession.setSelectedSlot(admin.uniqueId, event.slot)
            admin.sendMessage("§aSelected '${cosmetic.javaClass.simpleName}' from ${target.name}'s backpack!")
            admin.closeInventory()
        }
    }
}
