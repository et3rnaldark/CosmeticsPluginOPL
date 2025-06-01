package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.admin.AdminBackpackSession
import com.solecloth7.cosmeticsPluginOPL.admin.AdminSelectionManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.util.ChatNicknameInputManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object BackpackGUI {
    private const val SIZE = 36
    private const val TITLE = "Cosmetic Backpack"
    private const val VIEWING_PREFIX = "Viewing Backpack: "
    private const val SELECTING_PREFIX = "Selecting Backpack: "

    fun open(player: Player) {
        val inv: Inventory = Bukkit.createInventory(null, SIZE, TITLE)
        val chatColorCosmetics = CosmeticManager.getCosmetics(player)
        val nicknameTickets = NicknameTicketManager.getCosmetics(player)
        val allCosmetics = chatColorCosmetics + nicknameTickets

        for ((i, cosmetic) in allCosmetics.withIndex()) {
            if (i < SIZE) {
                val item = when (cosmetic) {
                    is ChatColorCosmetic -> cosmetic.toItem()
                    is NicknameTicketCosmetic -> cosmetic.toItem()
                    else -> continue
                }
                inv.setItem(i, item)
            }
        }
        for (i in allCosmetics.size until SIZE) {
            inv.setItem(i, ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
                val meta = itemMeta
                meta?.setDisplayName("§7Cosmetic Slot")
                itemMeta = meta
            })
        }

        player.openInventory(inv)
    }

    fun open(admin: Player, target: Player, allowSelect: Boolean) {
        val title = if (allowSelect) "$SELECTING_PREFIX${target.name}" else "$VIEWING_PREFIX${target.name}"
        val inv: Inventory = Bukkit.createInventory(null, SIZE, title)
        val chatColorCosmetics = CosmeticManager.getCosmetics(target)
        val nicknameTickets = NicknameTicketManager.getCosmetics(target)
        val allCosmetics = chatColorCosmetics + nicknameTickets

        for ((i, cosmetic) in allCosmetics.withIndex()) {
            if (i < SIZE) {
                val item = when (cosmetic) {
                    is ChatColorCosmetic -> cosmetic.toItem()
                    is NicknameTicketCosmetic -> cosmetic.toItem()
                    else -> continue
                }
                inv.setItem(i, item)
            }
        }

        for (i in allCosmetics.size until SIZE) {
            inv.setItem(i, ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
                val meta = itemMeta
                meta?.setDisplayName("§7Cosmetic Slot")
                itemMeta = meta
            })
        }

        AdminBackpackSession.set(admin.uniqueId, target.uniqueId, allowSelect)
        admin.openInventory(inv)
    }

    fun handleClick(player: Player, event: InventoryClickEvent) {
        val clicked = event.currentItem ?: return
        if (clicked.type != Material.PAPER) {
            event.isCancelled = true
            return
        }

        event.isCancelled = true

        val chatColors = CosmeticManager.getCosmetics(player)
        val nicknameTickets = NicknameTicketManager.getCosmetics(player)
        val combined = chatColors + nicknameTickets
        val cosmetic = combined.getOrNull(event.slot) ?: return

        when (cosmetic) {
            is ChatColorCosmetic -> {
                EquipGUI.openChatColor(player, cosmetic)
            }
            is NicknameTicketCosmetic.Unused -> {
                player.closeInventory()
                player.sendMessage("§7Enter your desired nickname in chat:")
                val index = event.slot
                ChatNicknameInputManager.requestInput(player, cosmetic)
            }
            is NicknameTicketCosmetic.Used -> {
                EquipGUI.openNicknameTicket(player, cosmetic)
            }
        }
    }

    fun handleAdminClick(admin: Player, target: Player, event: InventoryClickEvent) {
        event.isCancelled = true
        val clicked = event.currentItem ?: return
        if (clicked.type != Material.PAPER) return

        val chatColors = CosmeticManager.getCosmetics(target)
        val nicknameTickets = NicknameTicketManager.getCosmetics(target)
        val combined = chatColors + nicknameTickets

        val cosmetic = combined.getOrNull(event.slot) ?: return

        AdminBackpackSession.setSelectedSlot(admin.uniqueId, event.slot)
        admin.sendMessage("§aSelected '${cosmetic.javaClass.simpleName}' from ${target.name}'s backpack!")
        admin.closeInventory()
    }
}
