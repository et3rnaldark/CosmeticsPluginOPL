package com.solecloth7.cosmeticsPluginOPL.listener

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.util.ChatNicknameInputManager
import com.solecloth7.cosmeticsPluginOPL.util.ColorUtil
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class ChatColorListener(private val plugin: JavaPlugin) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onChat(e: AsyncPlayerChatEvent) {
        val player = e.player

        // ✅ Prevent chat color handling if nickname input is active
        if (ChatNicknameInputManager.isWaiting(player)) {
            e.isCancelled = true
            val msg = e.message
            Bukkit.getScheduler().runTask(plugin, Runnable {
                ChatNicknameInputManager.handleChat(player, msg)
            })
            return
        }

        val cosmetic = CosmeticManager.getEquippedCosmetic(player) ?: return

        // Apply gradient to the chat message
        e.message = ColorUtil.gradientName(e.message, cosmetic.hexColors, cosmetic.bold)

        if (!cosmetic.registered) return

        val before = cosmetic.messagesSent
        cosmetic.messagesSent += 1
        val after = cosmetic.messagesSent

        val beforeName = getMilestoneName(before)
        val afterName = getMilestoneName(after)

        if (beforeName != afterName) {
            Bukkit.getScheduler().runTask(plugin, Runnable {
                val preview = ColorUtil.gradientName(cosmetic.text, cosmetic.hexColors, cosmetic.bold)
                val msg = "§6${player.name}'s §eRegistered Chat Color has leveled up to ${cosmetic.quality.replaceFirstChar { it.uppercase() }} §c§l$afterName §c§lChat Color: §r$preview"
                Bukkit.broadcastMessage(msg)
            })
        }

        // Save the updated count
        CosmeticManager.updateCosmetic(player, cosmetic)
    }

    private fun getMilestoneName(count: Int): String = when {
        count >= 1000 -> "Transcendent"
        count >= 500 -> "Radiant"
        count >= 100 -> "Refined"
        count >= 10 -> "Basic"
        else -> "Ostentatious"
    }
}
