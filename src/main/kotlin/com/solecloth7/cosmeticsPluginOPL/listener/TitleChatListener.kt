package com.solecloth7.cosmeticsPluginOPL.listener

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class TitleChatListener(private val plugin: JavaPlugin) : Listener {

    private val milestones = listOf(10, 100, 500, 1000)
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player
        val cosmetic = CosmeticManager.getEquippedTitle(player) ?: return
        if (!cosmetic.registered) return

        val before = cosmetic.messagesSent
        cosmetic.messagesSent++
        val after = cosmetic.messagesSent

        CosmeticManager.updateTitleCosmetic(player, cosmetic)

        milestones.firstOrNull { it in (before + 1)..after}?.let {
            val levelName = cosmetic.getLevelName()
            val plainMessage = "§c${player.name}'s Registered Title has leveled up to §c$levelName Title: §r${cosmetic.title}"
            Bukkit.broadcastMessage(plainMessage)
        }

        val playerName = player.displayName
        e.format = "$playerName %2\$s"
    }
}