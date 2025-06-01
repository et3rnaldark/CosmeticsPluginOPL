package com.solecloth7.cosmeticsPluginOPL.cosmetics

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import org.bukkit.entity.Player
import java.util.*

object CosmeticManager {
    private val loadedCosmetics = mutableMapOf<String, MutableList<ChatColorCosmetic>>()
    private val equippedIndex = mutableMapOf<String, Int?>()

    private val titleCosmeticsMap = mutableMapOf<String, MutableList<TitleCosmetic>>()
    private val equippedTitleIdMap = mutableMapOf<String, UUID?>()

    fun load(player: Player) {
        val data = JsonBackpackStorage.loadBackpack(player)
        val uuid = player.uniqueId.toString()

        loadedCosmetics[uuid] = data.chatColorCosmetics.toMutableList()
        equippedIndex[uuid] = data.equippedChatColorIndex

        titleCosmeticsMap[uuid] = data.titleCosmetics.toMutableList()
        equippedTitleIdMap[uuid] = data.equippedTitleId
    }

    fun save(player: Player) {
        val uuid = player.uniqueId.toString()

        val chatColors = loadedCosmetics[uuid] ?: mutableListOf()
        val equipped = equippedIndex[uuid]
        val nicknameTickets = NicknameTicketManager.getCosmetics(player)
        val titles = titleCosmeticsMap[uuid] ?: mutableListOf()
        val equippedTitleId = equippedTitleIdMap[uuid]

        JsonBackpackStorage.saveBackpack(player, chatColors, equipped, nicknameTickets, titles, equippedTitleId)
    }

    // ---------------- ChatColorCosmetic ----------------

    fun giveCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics.getOrPut(player.uniqueId.toString()) { mutableListOf() }
        list.add(cosmetic)
        save(player)
    }

    fun getCosmetics(player: Player): List<ChatColorCosmetic> =
        loadedCosmetics[player.uniqueId.toString()] ?: emptyList()

    fun getMostRecentChatColorCosmetic(player: Player): ChatColorCosmetic? =
        getCosmetics(player).lastOrNull()

    fun updateCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics[player.uniqueId.toString()] ?: return
        val idx = list.indexOfFirst { it === cosmetic }
        if (idx != -1) list[idx] = cosmetic
        save(player)
    }

    fun deleteCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = loadedCosmetics[player.uniqueId.toString()] ?: return
        list.remove(cosmetic)
        if (getEquippedCosmetic(player) === cosmetic) {
            equippedIndex[player.uniqueId.toString()] = null
        }
        save(player)
    }

    fun equipCosmetic(player: Player, cosmetic: ChatColorCosmetic) {
        val list = getCosmetics(player)
        val idx = list.indexOf(cosmetic)
        if (idx != -1) equippedIndex[player.uniqueId.toString()] = idx
        save(player)
    }

    fun unequipCosmetic(player: Player) {
        equippedIndex[player.uniqueId.toString()] = null
        save(player)
    }

    fun getEquippedCosmetic(player: Player): ChatColorCosmetic? {
        val list = getCosmetics(player)
        val idx = equippedIndex[player.uniqueId.toString()]
        return if (idx != null && idx in list.indices) list[idx] else null
    }

    fun getEquippedIndex(player: Player): Int? = equippedIndex[player.uniqueId.toString()]

    // ---------------- TitleCosmetic ----------------

    fun getTitleCosmetics(player: Player): List<TitleCosmetic> =
        titleCosmeticsMap[player.uniqueId.toString()] ?: emptyList()

    fun giveTitleCosmetic(player: Player, cosmetic: TitleCosmetic) {
        val list = titleCosmeticsMap.getOrPut(player.uniqueId.toString()) { mutableListOf() }
        list.add(cosmetic)
        save(player)
    }

    fun updateTitleCosmetic(player: Player, cosmetic: TitleCosmetic) {
        val list = titleCosmeticsMap[player.uniqueId.toString()] ?: return
        val index = list.indexOfFirst { it.id == cosmetic.id }
        if (index != -1) {
            list[index] = cosmetic
            save(player)
        }
    }

    fun deleteTitleCosmetic(player: Player, cosmetic: TitleCosmetic) {
        val uuid = player.uniqueId.toString()
        val list = titleCosmeticsMap[uuid] ?: return
        list.removeIf { it.id == cosmetic.id }
        if (equippedTitleIdMap[uuid] == cosmetic.id) {
            equippedTitleIdMap[uuid] = null
        }
        save(player)
    }

    fun equipTitleCosmetic(player: Player, cosmetic: TitleCosmetic) {
        val list = titleCosmeticsMap[player.uniqueId.toString()] ?: return
        if (list.any { it.id == cosmetic.id }) {
            equippedTitleIdMap[player.uniqueId.toString()] = cosmetic.id
            save(player)
        }
    }

    fun unequipTitleCosmetic(player: Player) {
        equippedTitleIdMap[player.uniqueId.toString()] = null
        save(player)
    }

    fun getEquippedTitle(player: Player): TitleCosmetic? {
        val id = equippedTitleIdMap[player.uniqueId.toString()] ?: return null
        return getTitleCosmetics(player).firstOrNull { it.id == id }
    }
}
