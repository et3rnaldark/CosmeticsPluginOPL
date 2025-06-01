package com.solecloth7.cosmeticsPluginOPL.storage

import com.google.gson.*
import com.solecloth7.cosmeticsPluginOPL.model.BackpackData
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.ChatColorCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknameTicketCosmetic.*
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import org.bukkit.entity.Player
import java.io.File
import java.lang.reflect.Type
import java.util.*

object JsonBackpackStorage {
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(NicknameTicketCosmetic::class.java, NicknameTicketCosmeticAdapter())
        .setPrettyPrinting()
        .create()

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

    private class NicknameTicketCosmeticAdapter : JsonDeserializer<NicknameTicketCosmetic>, JsonSerializer<NicknameTicketCosmetic> {
        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): NicknameTicketCosmetic {
            val obj = json.asJsonObject
            return if (obj.has("nickname")) {
                context.deserialize<Used>(json, Used::class.java)
            } else {
                context.deserialize<Unused>(json, Unused::class.java)
            }
        }

        override fun serialize(src: NicknameTicketCosmetic, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
            return when (src) {
                is Used -> context.serialize(src, Used::class.java)
                is Unused -> context.serialize(src, Unused::class.java)
            }
        }
    }
}
