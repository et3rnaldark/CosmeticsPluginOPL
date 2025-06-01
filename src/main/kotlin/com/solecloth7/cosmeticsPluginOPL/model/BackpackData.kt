package com.solecloth7.cosmeticsPluginOPL.model

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic

data class BackpackData(
    val chatColorCosmetics: List<ChatColorCosmetic> = emptyList(),
    val nicknameTicketCosmetics: List<NicknameTicketCosmetic> = emptyList(),
    val equippedChatColorIndex: Int? = null
)
