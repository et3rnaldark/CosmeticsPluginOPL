package com.solecloth7.cosmeticsPluginOPL.gui

import org.bukkit.entity.Player

object AdminSelectView {
    private val viewing = mutableMapOf<Player, Player>()
    fun setViewing(admin: Player, target: Player) { viewing[admin] = target }
    fun getViewing(admin: Player): Player? = viewing[admin]
    fun clear(admin: Player) { viewing.remove(admin) }
}
