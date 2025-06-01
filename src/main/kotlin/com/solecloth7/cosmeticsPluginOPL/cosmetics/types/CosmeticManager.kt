package com.solecloth7.cosmeticsPluginOPL.cosmetics

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import org.bukkit.entity.Player

object CosmeticManager {
    private val loadedCosmetics = mutableMapOf<String, MutableList<ChatColorCosmetic>>()
    private val equippedIndex = mutableMapOf<String, Int?>()

    fun load(player: Player) {
        val data = JsonBackpackStorage.loadBackpack(player)
        loadedCosmetics[player.uniqueId.toString()] = data.chatColorCosmetics.toMutableList()
        equippedIndex[player.uniqueId.toString()] = data.equippedChatColorIndex
        println("Loaded cosmetics for ${player.name}: $data")
    }


    fun deleteCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics[player.uniqueId.toString()] ?: return
        list.remove(cosmetic)
        if (getEquippedCosmetic(player) === cosmetic) {
            equippedIndex[player.uniqueId.toString()] = null
        }
        save(player)
    }

    fun unequipCosmetic(player: Player) {
        equippedIndex[player.uniqueId.toString()] = null
        save(player)
    }

    fun save(player: Player) {
        val chatColors = loadedCosmetics[player.uniqueId.toString()] ?: mutableListOf()
        val equipped = equippedIndex[player.uniqueId.toString()]
        val nicknameTickets = NicknameTicketManager.getCosmetics(player)
        JsonBackpackStorage.saveBackpack(player, chatColors, equipped, nicknameTickets)
    }

    fun giveCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics.getOrPut(player.uniqueId.toString()) { mutableListOf() }
        list.add(cosmetic)
        save(player)
    }

    fun getCosmetics(player: Player): List<ChatColorCosmetic> =
        loadedCosmetics[player.uniqueId.toString()] ?: emptyList()

    fun getMostRecentChatColorCosmetic(player: Player): ChatColorCosmetic? =
        loadedCosmetics[player.uniqueId.toString()]?.lastOrNull()

    fun updateCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics[player.uniqueId.toString()] ?: return
        val idx = list.indexOfFirst { it === cosmetic }
        if (idx != -1) list[idx] = cosmetic
        save(player)
    }

    fun equipCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics[player.uniqueId.toString()] ?: return
        val idx = list.indexOf(cosmetic)
        if (idx != -1) equippedIndex[player.uniqueId.toString()] = idx
        save(player)
    }

    fun getEquippedCosmetic(player: Player): ChatColorCosmetic? {
        val list = loadedCosmetics[player.uniqueId.toString()] ?: return null
        val idx = equippedIndex[player.uniqueId.toString()]
        return if (idx != null && idx in list.indices) list[idx] else null
    }
    fun getEquippedIndex(player: Player): Int? {
        return equippedIndex[player.uniqueId.toString()]
    }

}
