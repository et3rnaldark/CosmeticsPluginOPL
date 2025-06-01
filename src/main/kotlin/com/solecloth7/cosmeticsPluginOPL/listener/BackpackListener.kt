package com.solecloth7.cosmeticsPluginOPL.listener

import com.solecloth7.cosmeticsPluginOPL.gui.BackpackGUI
import com.solecloth7.cosmeticsPluginOPL.gui.EquipGUI
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
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class BackpackListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val title = e.view.title

        val clickedItem = e.currentItem
        if (clickedItem?.type == Material.GRAY_STAINED_GLASS_PANE) {
            e.isCancelled = true
            return
        }

        when {
            title == "Cosmetic Backpack" -> BackpackGUI.handleClick(player, e)

            title.startsWith("Viewing Backpack: ") -> {
                e.isCancelled = true
            }

            title.startsWith("Selecting Backpack: ") -> {
                val targetName = title.removePrefix("Selecting Backpack: ").trim()
                val target = Bukkit.getPlayerExact(targetName)
                if (target != null && AdminBackpackSession.getAllowSelect(player.uniqueId)) {
                    BackpackGUI.handleAdminClick(player, target, e)
                } else {
                    e.isCancelled = true
                    player.sendMessage("§cCould not resolve player from inventory title.")
                }
            }

            title == "Equip Chat Color" -> {
                val cosmetic = CosmeticManager.getCosmetics(player)
                    .find { it.toItem().isSimilar(e.inventory.getItem(4)) } as? ChatColorCosmetic

                if (cosmetic != null) {
                    EquipGUI.handleChatColorClick(player, e, cosmetic)
                } else {
                    e.isCancelled = true
                }
            }

            title == "Equip Nickname" -> {
                val cosmetic = NicknameTicketManager.getCosmetics(player)
                    .filterIsInstance<NicknameTicketCosmetic.Used>()
                    .find { it.toItem().isSimilar(e.inventory.getItem(4)) }

                if (cosmetic != null) {
                    EquipGUI.handleNicknameClick(player, e, cosmetic)
                } else {
                    e.isCancelled = true
                    player.sendMessage("§cFailed to identify the equipped nickname ticket.")
                }
            }

            title == "Equip Title" -> {
                val cosmetic = CosmeticManager.getTitleCosmetics(player)
                    .find { it.toItem().isSimilar(e.inventory.getItem(4)) }

                if (cosmetic != null) {
                    EquipGUI.handleTitleClick(player, e, cosmetic)
                } else {
                    e.isCancelled = true
                    player.sendMessage("§cFailed to identify the equipped title cosmetic.")
                }
            }
        }
    }

    @EventHandler
    fun onDrag(e: InventoryDragEvent) {
        val title = e.view.title
        if (title == "Cosmetic Backpack" ||
            title == "Equip Chat Color" ||
            title == "Equip Nickname" ||
            title == "Equip Title" ||
            title.startsWith("Viewing Backpack: ") ||
            title.startsWith("Selecting Backpack: ")
        ) {
            e.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        if (ChatNicknameInputManager.isWaiting(player)) {
            e.isCancelled = true
            Bukkit.getScheduler().runTask(plugin, Runnable {
                ChatNicknameInputManager.handleChat(player, e.message)
            })
        }
    }
}
