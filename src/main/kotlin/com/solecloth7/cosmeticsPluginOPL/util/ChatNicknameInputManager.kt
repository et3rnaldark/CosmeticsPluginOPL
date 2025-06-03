package com.solecloth7.cosmeticsPluginOPL.util

import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.gui.BackpackGUI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

object ChatNicknameInputManager {
    private val pending = mutableMapOf<UUID, NicknameTicketCosmetic.Unused>()

    fun requestInput(player: Player, ticket: NicknameTicketCosmetic.Unused) {
        player.sendMessage("§bType your Nickname in chat. Type 'cancel' to abort.")
        pending[player.uniqueId] = ticket
        player.closeInventory()
    }

    fun handleChat(player: Player, message: String): Boolean {
        val ticket = pending[player.uniqueId] ?: return false

        if (message.equals("cancel", ignoreCase = true)) {
            player.sendMessage("§cCancelled nickname input.")
            pending.remove(player.uniqueId)
            return true
        }

        val cleanedNickname = message.replace(Regex("§[0-9A-FK-ORa-fk-or]"), "")

        val list = NicknameTicketManager.getCosmetics(player).toMutableList()
        val index = list.indexOfFirst { it === ticket }
        if (index == -1) {
            player.sendMessage("§c§lError: §cNickname Ticket wasn't found or was already used.")
            pending.remove(player.uniqueId)
            return true
        }

        val used = NicknameTicketCosmetic.Used(
            nickname = cleanedNickname,
            quality = "basic",
            registered = false
        )

        list[index] = used
        NicknameTicketManager.setAll(player, list)

        pending.remove(player.uniqueId)
        player.sendMessage("§aNickname set to §8~§7$cleanedNickname§a!")
        BackpackGUI.open(player)
        return true
    }

    fun isWaiting(player: Player): Boolean = player.uniqueId in pending
}

// Cosmetic item render functions remain unchanged:
fun NicknameTicketCosmetic.Used.toItem(): ItemStack {
    val item = ItemStack(Material.PAPER)
    val meta = item.itemMeta!!
    meta.setCustomModelData(40008)

    val prefix = when (quality.lowercase()) {
        "ascendant" -> "§x§E§E§8§8§0§0§lA" +
                "§x§D§E§A§1§0§0§ls" +
                "§x§E§4§C§1§0§0§lc" +
                "§x§E§4§D§4§4§9§le" +
                "§x§E§4§D§B§8§D§ln" +
                "§x§E§B§D§A§4§C§ld" +
                "§x§D§8§B§7§0§0§la" +
                "§x§D§E§A§1§0§0§ln" +
                "§x§F§1§8§9§0§0§lt §6§lNickname: §7"
        "unobtainable" -> "§f§r家 §d§lNickname: §7"
        "legendary" -> "§6§lLegendary Nickname: §7"
        "mythic" -> "§5Mythic Nickname: §7"
        "rare" -> "§2Rare Nickname: §7"
        "basic" -> "§7Nickname: "
        else -> "§7Nickname: "
    }

    meta.setDisplayName("$prefix$nickname")
    val lore = mutableListOf<String>()
    lore.add("§8Sets your display name to: §f~$nickname")
    lore.add("§7Click to equip this nickname.")
    lore.add("§8Level ${1 + (0..99).random()} ${quality.replaceFirstChar { it.uppercase() }} Nickname")
    if (registered) lore.add("§c❤️ Messages Sent: 0")

    meta.lore = lore
    item.itemMeta = meta
    return item
}

fun NicknameTicketCosmetic.Unused.toItem(): ItemStack {
    val item = ItemStack(Material.PAPER)
    val meta = item.itemMeta!!
    meta.setCustomModelData(40007)
    meta.setDisplayName("§7Nickname Ticket")
    meta.lore = listOf(
        "§7Use this ticket to set a custom nickname.",
        "§7Click to redeem."
    )
    item.itemMeta = meta
    return item
}
