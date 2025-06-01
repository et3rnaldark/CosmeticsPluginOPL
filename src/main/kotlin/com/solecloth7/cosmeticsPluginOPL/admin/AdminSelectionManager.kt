package com.solecloth7.cosmeticsPluginOPL.admin

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
}
