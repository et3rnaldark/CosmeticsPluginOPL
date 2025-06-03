package com.solecloth7.cosmeticsPluginOPL.gui

import com.solecloth7.cosmeticsPluginOPL.cosmetics.NicknameTicketManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknamePaintCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.util.ColorUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

object NicknamePaintGUI {
    private const val TITLE = "Select a Nickname Ticket"
    private const val SIZE = 36
    private val paintSessions = mutableMapOf<Player, NicknamePaintCosmetic>()

    fun open(player: Player, paint: NicknamePaintCosmetic) {
        val inv: Inventory = Bukkit.createInventory(null, SIZE, TITLE)
        paintSessions[player] = paint

        val nicknameTickets = NicknameTicketManager.getCosmetics(player)
            .filterIsInstance<NicknameTicketCosmetic.Used>()

        nicknameTickets.forEachIndexed { index, ticket ->
            val item = ItemStack(Material.PAPER)
            val meta = item.itemMeta!!
            val gradient = ticket.gradientHex?.let {
                ColorUtil.gradientName("Nickname: ${ticket.nickname}", it, ticket.gradientBold ?: false)
            } ?: "§7Nickname: §f${ticket.nickname}"
            meta.setDisplayName(gradient)
            val loreList = mutableListOf<String>()
            if (ticket.lore != null) loreList.addAll(ticket.lore!!)
            meta.lore = loreList
            item.itemMeta = meta
            inv.setItem(index, item)
        }

        player.openInventory(inv)
    }

    fun handleClick(e: InventoryClickEvent) {
        val player = e.whoClicked as? Player ?: return
        val inventory = e.clickedInventory ?: return
        if (e.view.title != TITLE || inventory.type != InventoryType.CHEST) return

        e.isCancelled = true

        if (e.click.isShiftClick || !e.click.isLeftClick) return

        val paint = paintSessions[player] ?: return
        val index = e.slot
        val tickets = NicknameTicketManager.getCosmetics(player).filterIsInstance<NicknameTicketCosmetic.Used>()
        if (index !in tickets.indices) return

        val ticket = tickets[index]
        val paintName = paint.text
        val gradient = ColorUtil.gradientName(paintName, paint.hexColors, paint.bold)
        val paintedLore = "§7Painted: $gradient"

        ticket.lore = (ticket.lore ?: listOf()) + paintedLore
        ticket.gradientHex = paint.hexColors
        ticket.gradientBold = paint.bold
        ticket.gradientName = paintName

        NicknameTicketManager.updateCosmetic(player, ticket)
        NicknameTicketManager.deleteNicknamePaint(player, paint)

        player.closeInventory()
        player.sendMessage("§aPainted nickname ticket with §f$gradient§a.")
        paintSessions.remove(player)
    }
}
