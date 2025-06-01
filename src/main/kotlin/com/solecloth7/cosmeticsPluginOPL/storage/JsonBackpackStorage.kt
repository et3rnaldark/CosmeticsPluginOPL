package com.solecloth7.cosmeticsPluginOPL.storage

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.solecloth7.cosmeticsPluginOPL.model.BackpackData
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import org.bukkit.entity.Player
import org.codehaus.plexus.util.FileUtils.getFile
import java.io.File
import java.util.UUID

object JsonBackpackStorage {
    private val gson = GsonBuilder().setPrettyPrinting().create()
    lateinit var dataFolder: File

    fun getFileFor(player: Player): File = File(dataFolder, "${player.uniqueId}.json")

    fun saveBackpack(
        player: Player,
        cosmetics: List<ChatColorCosmetic>,
        equipped: Int?,
        nicknameTickets: List<NicknameTicketCosmetic>,
        titleCosmetics: List<TitleCosmetic>,
        equippedTitleId: UUID?
    ) {
        val file = getFileFor(player)
        val json = JsonObject()

        json.add("chatColorCosmetics", gson.toJsonTree(cosmetics))
        json.addProperty("equippedChatColorIndex", equipped)
        json.add("nicknameTicketCosmetics", gson.toJsonTree(nicknameTickets))
        json.add("titleCosmetics", gson.toJsonTree(titleCosmetics))
        json.addProperty("equippedTitleId", equippedTitleId?.toString())

        file.writeText(gson.toJson(json))
    }

    fun loadBackpack(player: Player): BackpackData {
        val file = getFileFor(player)
        if (!file.exists()) {
            println("⚠️ Backpack file does not exist for ${player.name} (${file.absolutePath})")
            return BackpackData()
        }

        return try {
            val content = file.readText()
            val data = gson.fromJson(content, BackpackData::class.java) ?: BackpackData()
            println("✅ Loaded backpack for ${player.name} from ${file.absolutePath}")
            data
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("❌ Failed to load backpack for ${player.name} from ${file.absolutePath}")
            BackpackData()
        }
    }
}
