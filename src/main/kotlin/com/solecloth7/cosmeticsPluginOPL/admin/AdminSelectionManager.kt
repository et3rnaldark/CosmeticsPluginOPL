package com.solecloth7.cosmeticsPluginOPL.admin

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknamePaintCosmetic
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

object AdminSelectionManager {
    // Maps admin UUID → target UUID + cosmetic slot
    private val selections = mutableMapOf<UUID, Pair<UUID, Int>>()

    fun select(adminId: UUID, targetId: UUID, slot: Int) {
        selections[adminId] = Pair(targetId, slot)
    }

    fun getSelection(adminId: UUID): Pair<UUID, Int>? {
        return selections[adminId]
    }

    fun clear(adminId: UUID) {
        selections.remove(adminId)
    }

    fun getSelected(sender: CommandSender): NicknamePaintCosmetic? {
        val admin = (sender as? Player) ?: return null
        val (targetId, slot) = selections[admin.uniqueId] ?: return null
        val target = Bukkit.getPlayer(targetId) ?: return null

        val backpack = JsonBackpackStorage.loadBackpack(target)
        return backpack.nicknamePaintCosmetics.getOrNull(slot)
    }

    fun getSelectedOwner(sender: CommandSender): Player? {
        val admin = (sender as? Player) ?: return null
        val (targetId, _) = selections[admin.uniqueId] ?: return null
        return Bukkit.getPlayer(targetId)
    }
}
