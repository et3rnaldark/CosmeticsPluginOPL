// NicknameTicketManager.kt
package com.solecloth7.cosmeticsPluginOPL.cosmetics

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.types.MetaNode
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
        val titleCosmetics = CosmeticManager.getTitleCosmetics(player)
        val equippedTitleId = CosmeticManager.getEquippedTitle(player)?.id
        val paints = CosmeticManager.getNicknamePaints(player)
        JsonBackpackStorage.saveBackpack(player, chatColorData, CosmeticManager.getEquippedIndex(player), nicknameData, paints, titleCosmetics, equippedTitleId)
    }

    fun getCosmetics(player: Player): List<NicknameTicketCosmetic> =
        loaded[player.uniqueId.toString()] ?: emptyList()

    fun setCosmetic(player: Player, index: Int, cosmetic: NicknameTicketCosmetic) {
        val list = loaded[player.uniqueId.toString()] ?: return
        if (index in list.indices) {
            list[index] = cosmetic
            save(player)
        }
    }

    fun updateCosmetic(player: Player, cosmetic: NicknameTicketCosmetic) {
        val list = loaded[player.uniqueId.toString()] ?: return
        val index = list.indexOfFirst { it.id == cosmetic.id }
        if (index != -1) {
            list[index] = cosmetic
            save(player)
        }
    }

    fun deleteNicknamePaint(player: Player, paint: com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknamePaintCosmetic) {
        CosmeticManager.deleteNicknamePaint(player, paint)
    }

    fun applyNickname(player: Player, nickname: String) {
        val luckPerms = LuckPermsProvider.get()
        val user = luckPerms.userManager.getUser(player.uniqueId) ?: return

        user.data().clear { it is MetaNode && it.key == "nickname" }

        val node = MetaNode.builder("nickname", "§8~§f$nickname").build()
        user.data().add(node)

        luckPerms.userManager.saveUser(user)
    }

    fun setAll(player: Player, newList: List<NicknameTicketCosmetic>) {
        loaded[player.uniqueId.toString()] = newList.toMutableList()
        save(player)
    }

    fun equip(player: Player, ticket: NicknameTicketCosmetic.Used) {
        equippedNicknameMap[player.uniqueId.toString()] = ticket.nickname
        player.setDisplayName("~${ticket.nickname}")
        applyNickname(player, ticket.nickname)
        save(player)
    }

    fun unequip(player: Player) {
        equippedNicknameMap[player.uniqueId.toString()] = null
        player.setDisplayName(player.name)

        val luckPerms = LuckPermsProvider.get()
        val user = luckPerms.userManager.getUser(player.uniqueId)
        user?.data()?.clear { it is MetaNode && it.key == "nickname" }
        if (user != null) luckPerms.userManager.saveUser(user)

        save(player)
    }

    fun isEquipped(player: Player, ticket: NicknameTicketCosmetic.Used): Boolean =
        equippedNicknameMap[player.uniqueId.toString()] == ticket.nickname

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
