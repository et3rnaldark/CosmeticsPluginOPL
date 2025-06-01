package com.solecloth7.cosmeticsPluginOPL.listener

import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class PlayerJoinListener : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player
        CosmeticManager.load(player)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        CosmeticManager.save(e.player)
    }
}
