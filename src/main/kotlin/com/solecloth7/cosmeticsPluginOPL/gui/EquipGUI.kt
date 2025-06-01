package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object EquipGUI {
    private const val SIZE = 9

    // Method for handling ChatColorCosmetic
    fun openChatColor(player: Player, cosmetic: ChatColorCosmetic) {
        val inv = Bukkit.createInventory(null, SIZE, "Equip Chat Color")
        inv.setItem(4, cosmetic.toItem())

        val equipped = CosmeticManager.getEquippedCosmetic(player)
        val isEquipped = equipped === cosmetic

        inv.setItem(0, createPane(Material.BARRIER, "§4§lDelete Cosmetic"))
        inv.setItem(2, createPane(Material.RED_STAINED_GLASS_PANE, if (isEquipped) "§cUnequip" else "§cCancel"))
        inv.setItem(6, createPane(Material.LIME_STAINED_GLASS_PANE, "§a§lEquip"))

        player.openInventory(inv)
    }

    // Method for handling NicknameTicketCosmetic.Used
    fun openNicknameTicket(player: Player, cosmetic: NicknameTicketCosmetic.Used) {
        val inv = Bukkit.createInventory(null, SIZE, "Equip Nickname")
        inv.setItem(4, cosmetic.toItem())

        val equipped = NicknameTicketManager.getEquippedNickname(player)
        val isEquipped = equipped?.nickname == cosmetic.nickname

        inv.setItem(0, createPane(Material.BARRIER, "§4§lDelete Nickname"))
        inv.setItem(2, createPane(Material.RED_STAINED_GLASS_PANE, if (isEquipped) "§cUnequip Nickname" else "§cCancel"))
        inv.setItem(6, createPane(Material.LIME_STAINED_GLASS_PANE, "§a§lEquip Nickname"))

        player.openInventory(inv)
    }

    // Handle ChatColorCosmetic Click
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
        }
        player.closeInventory()
    }

    // Handle NicknameTicketCosmetic.Used Click
    fun handleNicknameClick(player: Player, event: InventoryClickEvent, cosmetic: NicknameTicketCosmetic.Used) {
        event.isCancelled = true

        when (event.rawSlot) {
            0 -> {
                val tickets = NicknameTicketManager.getCosmetics(player).toMutableList()
                val index = tickets.indexOfFirst { it is NicknameTicketCosmetic.Used && it.nickname == cosmetic.nickname }
                if (index != -1) {
                    tickets.removeAt(index)
                    NicknameTicketManager.setAll(player, tickets)
                    player.sendMessage("§cDeleted nickname: ~${cosmetic.nickname}")
                } else {
                    player.sendMessage("§cCould not find the nickname ticket to delete.")
                }
            }
            6 -> {
                // Update this to fix the equip condition
                if (!cosmetic.nickname.startsWith("~") && cosmetic.nickname != player.displayName) {
                    NicknameTicketManager.equip(player, cosmetic)
                    player.sendMessage("§aEquipped nickname: ~${cosmetic.nickname}")
                } else {
                    player.sendMessage("§cThis nickname is already equipped!")
                }
            }
            2 -> {
                val equipped = NicknameTicketManager.getEquippedNickname(player)
                if (equipped?.nickname == cosmetic.nickname) {
                    NicknameTicketManager.unequip(player)
                    player.sendMessage("§cUnequipped nickname.")
                }
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
