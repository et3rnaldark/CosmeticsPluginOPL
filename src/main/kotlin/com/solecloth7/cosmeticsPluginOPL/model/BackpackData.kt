package com.solecloth7.cosmeticsPluginOPL.model

import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknamePaintCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import java.util.UUID

data class BackpackData(
    val chatColorCosmetics: List<ChatColorCosmetic> = emptyList(),
    val nicknameTicketCosmetics: List<NicknameTicketCosmetic> = emptyList(),
    val nicknamePaintCosmetics: List<NicknamePaintCosmetic> = emptyList(),
    val titleCosmetics: List<TitleCosmetic> = emptyList(),
    val equippedChatColorIndex: Int? = null,
    val equippedTitleId: UUID? = null
)
