package com.solecloth7.cosmeticsPluginOPL.gui

import java.util.*

object ItemMoveSession {
    private val moveMap = mutableMapOf<UUID, Int>()

    fun startMove(playerId: UUID, slot: Int) {
        moveMap[playerId] = slot
    }

    fun getMovingSlot(playerId: UUID): Int? = moveMap[playerId]

    fun clear(playerId: UUID) {
        moveMap.remove(playerId)
    }
}
