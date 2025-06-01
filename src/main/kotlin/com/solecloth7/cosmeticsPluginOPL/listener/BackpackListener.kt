package com.solecloth7.cosmeticsPluginOPL.listener

import com.solecloth7.cosmeticsPluginOPL.gui.BackpackGUI
import com.solecloth7.cosmeticsPluginOPL.gui.EquipGUI
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.gui.AdminBackpackGUI
import com.solecloth7.cosmeticsPluginOPL.gui.AdminCosmeticModifyGUI
import com.solecloth7.cosmeticsPluginOPL.util.ChatNicknameInputManager
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class BackpackListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title

        // Prevent dragging panes or clicking glass
        val clickedItem = e.currentItem
        if (clickedItem?.type == Material.GRAY_STAINED_GLASS_PANE ||
            clickedItem?.type?.name?.contains("STAINED_GLASS_PANE") == true
        ) {
            e.isCancelled = true
            return
        }

        when {
            title == "Cosmetic Backpack" -> BackpackGUI.handleClick(player, e)
            title == "Equip Chat Color" -> {
                val cosmetics = CosmeticManager.getCosmetics(player)
                val cosmetic = cosmetics.find { it.toItem().isSimilar(e.inventory.getItem(4)) }
                if (cosmetic != null) {
                    EquipGUI.handleChatColorClick(player, e, cosmetic)  // Correctly passing ChatColorCosmetic to the method
                } else {
                    e.isCancelled = true
                }
            }
            title == "Equip Nickname" -> {
                val nicknameTickets = NicknameTicketManager.getCosmetics(player).filterIsInstance<NicknameTicketCosmetic.Used>()
                val cosmetic = nicknameTickets.find { it.toItem().isSimilar(e.inventory.getItem(4)) }
                if (cosmetic != null) {
                    EquipGUI.handleNicknameClick(player, e, cosmetic)  // Correctly passing NicknameTicketCosmetic.Used to the method
                } else {
                    e.isCancelled = true
                    player.sendMessage("§cFailed to identify the equipped nickname ticket.")
                }
            }

            title.startsWith("Viewing Backpack: ") -> { e.isCancelled = true }
            title.startsWith("Selecting Backpack: ") -> {
                val name = title.removePrefix("Selecting Backpack:").trim()
                val target = Bukkit.getPlayerExact(name)
                if (target != null) BackpackGUI.handleAdminClick(player, target, e)
                else {
                    e.isCancelled = true
                    player.sendMessage("§cCould not resolve player from inventory title.")
                }
            }
            title == "Modify Cosmetic" -> AdminCosmeticModifyGUI.handleClick(player, e)
        }
    }

    @EventHandler
    fun onDrag(e: InventoryDragEvent) {
        val title = e.view.title
        if (title == "Cosmetic Backpack" || title == "Equip Chat Color" ||
            title == "Equip Nickname" ||
            title.startsWith("Viewing Backpack") || title.startsWith("Selecting Backpack")
        ) {
            e.isCancelled = true
        }
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        val cosmetic = CosmeticManager.getEquippedCosmetic(player)
        applyTablistName(player, cosmetic)
    }

    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        if (ChatNicknameInputManager.isWaiting(player)) {
            e.isCancelled = true
            val message = e.message
            Bukkit.getScheduler().runTask(plugin, Runnable {
                ChatNicknameInputManager.handleChat(player, message)
            })
        }
    }

    private fun applyTablistName(player: Player, cosmetic: com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic?) {
        val crown = cosmetic?.crown?.takeIf { it.isNotBlank() } ?: ""
        player.setPlayerListName(if (crown.isNotEmpty()) "$crown ${'$'}{player.name}" else player.name)
    }
}
