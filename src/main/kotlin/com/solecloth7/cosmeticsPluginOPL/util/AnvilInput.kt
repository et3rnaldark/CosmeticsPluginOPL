package com.solecloth7.cosmeticsPluginOPL.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player

object AnvilInput {
    fun open(player: Player, placeholder: String, callback: (String) -> Unit) {
        // Implement using a real anvil GUI library or your own system
        // For now, this is just a placeholder so it compiles.
        player.sendMessage("§7(Anvil GUI placeholder would open here with default text: $placeholder)")
        callback("ExampleNickname") // Replace with actual typed nickname from real GUI
    }
}
