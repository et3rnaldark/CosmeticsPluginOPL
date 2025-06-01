package com.solecloth7.cosmeticsPluginOPL.cosmetics

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import org.bukkit.entity.Player

object NicknameTicketManager {
    private val loaded = mutableMapOf<String, MutableList<NicknameTicketCosmetic>>()
    private val equippedNicknameMap = mutableMapOf<String, String?>()

    fun load(player: Player) {
        val data = JsonBackpackStorage.loadBackpack(player)
        loaded[player.uniqueId.toString()] = data.nicknameTicketCosmetics.toMutableList()
    }

    fun save(player: Player) {
        val chatColorData = CosmeticManager.getCosmetics(player)
        val nicknameData = loaded[player.uniqueId.toString()] ?: mutableListOf()
        JsonBackpackStorage.saveBackpack(player, chatColorData, CosmeticManager.getEquippedIndex(player), nicknameData)
    }

    fun getCosmetics(player: Player): List<NicknameTicketCosmetic> {
        return loaded[player.uniqueId.toString()] ?: emptyList()
    }

    fun setCosmetic(player: Player, index: Int, cosmetic: NicknameTicketCosmetic) {
        val list = loaded[player.uniqueId.toString()] ?: return
        if (index in list.indices) {
            list[index] = cosmetic
            save(player)
        }
    }

    fun setAll(player: Player, newList: List<NicknameTicketCosmetic>) {
        loaded[player.uniqueId.toString()] = newList.toMutableList()
        save(player)
    }

    fun equip(player: Player, ticket: NicknameTicketCosmetic.Used) {
        equippedNicknameMap[player.uniqueId.toString()] = ticket.nickname
        player.setDisplayName("~${ticket.nickname}")  // Ensure correct formatting
        save(player)
    }

    fun unequip(player: Player) {
        equippedNicknameMap[player.uniqueId.toString()] = null
        player.setDisplayName(player.name)  // Revert display name to original
        save(player)
    }

    fun isEquipped(player: Player, ticket: NicknameTicketCosmetic.Used): Boolean {
        return equippedNicknameMap[player.uniqueId.toString()] == ticket.nickname
    }

    fun getEquippedNickname(player: Player): NicknameTicketCosmetic.Used? {
        val name = equippedNicknameMap[player.uniqueId.toString()] ?: return null
        return getCosmetics(player).filterIsInstance<NicknameTicketCosmetic.Used>().firstOrNull {
            it.nickname == name
        }
    }

    fun giveCosmetic(player: Player, cosmetic: NicknameTicketCosmetic) {
        val list = loaded.getOrPut(player.uniqueId.toString()) { mutableListOf() }
        list.add(cosmetic)
        save(player)
    }
}
