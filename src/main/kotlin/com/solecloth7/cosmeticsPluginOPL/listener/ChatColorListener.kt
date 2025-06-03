package com.solecloth7.cosmeticsPluginOPL.listener

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
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

        // If nickname input is active
        if (ChatNicknameInputManager.isWaiting(player)) {
            e.isCancelled = true
            Bukkit.getScheduler().runTask(plugin, Runnable {
                ChatNicknameInputManager.handleChat(player, e.message)
            })
            return
        }

        // Cancel vanilla chat and broadcast our own
        e.isCancelled = true

        Bukkit.getScheduler().runTask(plugin, Runnable {
            val nickname = NicknameTicketManager.getEquippedNickname(player)?.nickname
            val name = if (nickname != null) "§8~§7$nickname" else player.name

            val title = CosmeticManager.getEquippedTitle(player)?.title?.let {
                "§8[§7$it§8] "
            } ?: ""


            val chatColor = CosmeticManager.getEquippedCosmetic(player)
            val message = chatColor?.let {
                ColorUtil.gradientName(e.message, it.hexColors, it.bold)
            } ?: e.message

            // Broadcast final formatted message
            Bukkit.broadcastMessage("$title§7$name§8: §7$message")

            // Track registered chat milestones
            if (chatColor?.registered == true) {
                val before = chatColor.messagesSent
                chatColor.messagesSent += 1
                val after = chatColor.messagesSent

                val beforeName = getMilestoneName(before)
                val afterName = getMilestoneName(after)

                if (beforeName != afterName) {
                    val preview = ColorUtil.gradientName(chatColor.text, chatColor.hexColors, chatColor.bold)
                    val msg = "§6${player.name}'s §eRegistered Chat Color has leveled up to ${chatColor.quality.replaceFirstChar { it.uppercase() }} §c§l$afterName §c§lChat Color: §r$preview"
                    Bukkit.broadcastMessage(msg)
                }

                CosmeticManager.updateCosmetic(player, chatColor)
            }
        })
    }

    private fun getMilestoneName(count: Int): String = when {
        count >= 1000 -> "Transcendent"
        count >= 500 -> "Radiant"
        count >= 100 -> "Refined"
        count >= 10 -> "Basic"
        else -> "Ostentatious"
    }
}
