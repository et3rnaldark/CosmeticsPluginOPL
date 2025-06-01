package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import com.solecloth7.cosmeticsPluginOPL.gui.ItemMoveSession

object EquipGUI {
    private const val SIZE = 9

    fun openChatColor(player: Player, cosmetic: ChatColorCosmetic) {
        val inv = Bukkit.createInventory(null, SIZE, "Equip Chat Color")
        inv.setItem(4, cosmetic.toItem())

        val equipped = CosmeticManager.getEquippedCosmetic(player)
        val isEquipped = equipped === cosmetic

        inv.setItem(0, createPane(Material.BARRIER, "§4§lDelete Cosmetic"))
        inv.setItem(2, createPane(Material.RED_STAINED_GLASS_PANE, if (isEquipped) "§cUnequip" else "§cCancel"))
        inv.setItem(6, createPane(Material.LIME_STAINED_GLASS_PANE, "§a§lEquip"))
        inv.setItem(8, createPane(Material.YELLOW_STAINED_GLASS_PANE, "§6§lMove Item"))  // Move Item Button
        player.openInventory(inv)
    }

    fun openNicknameTicket(player: Player, cosmetic: NicknameTicketCosmetic.Used) {
        val inv = Bukkit.createInventory(null, SIZE, "Equip Nickname")
        inv.setItem(4, cosmetic.toItem())

        val equipped = NicknameTicketManager.getEquippedNickname(player)
        val isEquipped = equipped?.nickname == cosmetic.nickname

        inv.setItem(0, createPane(Material.BARRIER, "§4§lDelete Nickname"))
        inv.setItem(2, createPane(Material.RED_STAINED_GLASS_PANE, if (isEquipped) "§cUnequip Nickname" else "§cCancel"))
        inv.setItem(6, createPane(Material.LIME_STAINED_GLASS_PANE, "§a§lEquip Nickname"))
        inv.setItem(8, createPane(Material.YELLOW_STAINED_GLASS_PANE, "§6§lMove Item"))  // Move Item Button
        player.openInventory(inv)
    }

    fun openTitle(player: Player, cosmetic: TitleCosmetic) {
        val inv = Bukkit.createInventory(null, SIZE, "Equip Title")
        inv.setItem(4, cosmetic.toItem())

        val equipped = CosmeticManager.getEquippedTitle(player)
        val isEquipped = equipped?.id == cosmetic.id

        inv.setItem(0, createPane(Material.BARRIER, "§4§lDelete Title"))
        inv.setItem(2, createPane(Material.RED_STAINED_GLASS_PANE, if (isEquipped) "§cUnequip Title" else "§cCancel"))
        inv.setItem(6, createPane(Material.LIME_STAINED_GLASS_PANE, "§a§lEquip Title"))
        inv.setItem(8, createPane(Material.YELLOW_STAINED_GLASS_PANE, "§6§lMove Item"))  // Move Item Button
        player.openInventory(inv)
    }

    fun handleChatColorClick(player: Player, event: InventoryClickEvent, cosmetic: ChatColorCosmetic) {
        val equipped = CosmeticManager.getEquippedCosmetic(player)
        val isEquipped = equipped === cosmetic
        event.isCancelled = true

        when (event.rawSlot) {
            0 -> {
                CosmeticManager.deleteCosmetic(player, cosmetic)
                player.sendMessage("§cDeleted: §r${cosmetic.displayName}")
            }
            6 -> {
                if (!isEquipped) {
                    CosmeticManager.equipCosmetic(player, cosmetic)
                    player.sendMessage("§aEquipped: §r${cosmetic.displayName}")
                } else {
                    player.sendMessage("§cThis cosmetic is already equipped!")
                }
            }
            2 -> {
                if (isEquipped) {
                    CosmeticManager.unequipCosmetic(player)
                    player.sendMessage("§cUnequipped chat color.")
                }
            }
            8 -> {
                // Move Item logic for this slot
                ItemMoveSession.startMove(player.uniqueId, 4)
                player.sendMessage("§eClick a new slot to move this item.")
            }
        }
        player.closeInventory()
    }

    fun handleNicknameClick(player: Player, event: InventoryClickEvent, cosmetic: NicknameTicketCosmetic.Used) {
        event.isCancelled = true

        when (event.rawSlot) {
            0 -> {
                val tickets = NicknameTicketManager.getCosmetics(player).toMutableList()
                val index = tickets.indexOfFirst { it is NicknameTicketCosmetic.Used && it.nickname == cosmetic.nickname }
                if (index != -1) {
                    if (NicknameTicketManager.isEquipped(player, cosmetic)) {
                        NicknameTicketManager.unequip(player)
                    }

                    tickets.removeAt(index)
                    NicknameTicketManager.setAll(player, tickets)
                    player.sendMessage("§cDeleted nickname: ~${cosmetic.nickname}")
                } else {
                    player.sendMessage("§cCould not find the nickname ticket to delete.")
                }
            }
            6 -> {
                if (!NicknameTicketManager.isEquipped(player, cosmetic)) {
                    NicknameTicketManager.equip(player, cosmetic)
                    player.sendMessage("§aEquipped nickname: ~${cosmetic.nickname}")
                } else {
                    player.sendMessage("§cThis nickname is already equipped!")
                }
            }
            2 -> {
                if (NicknameTicketManager.isEquipped(player, cosmetic)) {
                    NicknameTicketManager.unequip(player)
                    player.sendMessage("§cUnequipped nickname.")
                }
            }
            8 -> {
                // Move Item logic for this slot
                ItemMoveSession.startMove(player.uniqueId, 4)
                player.sendMessage("§eClick a new slot to move this item.")
            }
        }

        player.closeInventory()
    }

    fun handleTitleClick(player: Player, event: InventoryClickEvent, cosmetic: TitleCosmetic) {
        event.isCancelled = true

        val equipped = CosmeticManager.getEquippedTitle(player)
        val isEquipped = equipped?.id == cosmetic.id

        when (event.rawSlot) {
            0 -> {
                CosmeticManager.deleteTitleCosmetic(player, cosmetic)
                player.sendMessage("§cDeleted title: ${cosmetic.getDisplayName()}")
            }
            6 -> {
                if (!isEquipped) {
                    CosmeticManager.equipTitleCosmetic(player, cosmetic)
                    player.sendMessage("§aEquipped title: ${cosmetic.getDisplayName()}")
                } else {
                    player.sendMessage("§cThis title is already equipped!")
                }
            }
            2 -> {
                if (isEquipped) {
                    CosmeticManager.unequipTitleCosmetic(player)
                    player.sendMessage("§cUnequipped title.")
                }
            }
            8 -> {
                // Move Item logic for this slot
                ItemMoveSession.startMove(player.uniqueId, 4)
                player.sendMessage("§eClick a new slot to move this item.")
            }
        }

        player.closeInventory()
    }

    private fun createPane(material: Material, name: String): ItemStack {
        return ItemStack(material).apply {
            val meta = itemMeta
            meta?.setDisplayName(name)
            itemMeta = meta
        }
    }
}
