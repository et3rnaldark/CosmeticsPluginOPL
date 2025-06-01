package com.solecloth7.cosmeticsPluginOPL.admin

import java.util.*

object AdminBackpackSession {
    private val viewingMap = mutableMapOf<UUID, Pair<UUID, Boolean>>() // admin → (target, allowSelect)
    private val selectedSlotMap = mutableMapOf<UUID, Int>() // admin → selected slot

    fun set(adminId: UUID, targetId: UUID, allowSelect: Boolean) {
        viewingMap[adminId] = targetId to allowSelect
    }

    fun getTarget(adminId: UUID): UUID? = viewingMap[adminId]?.first

    fun isSelecting(adminId: UUID): Boolean = viewingMap[adminId]?.second ?: false

    fun setSelectedSlot(adminId: UUID, slot: Int) {
        selectedSlotMap[adminId] = slot
    }

    fun getSelectedSlot(adminId: UUID): Int? = selectedSlotMap[adminId]

    fun clear(adminId: UUID) {
        viewingMap.remove(adminId)
        selectedSlotMap.remove(adminId)
    }

    // Use these in read-only contexts only
    fun getAllViewing(): Map<UUID, UUID> = viewingMap.mapValues { it.value.first }
    fun getAllSelectedSlots(): Map<UUID, Int> = selectedSlotMap
}
