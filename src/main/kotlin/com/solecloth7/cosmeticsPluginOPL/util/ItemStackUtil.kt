package com.solecloth7.cosmeticsPluginOPL.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.util.*
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.Inventory

object ItemStackUtil {
    fun itemStackToBase64(item: ItemStack?): String? {
        if (item == null) return null
        val config = YamlConfiguration()
        config.set("i", item)
        return Base64.getEncoder().encodeToString(config.saveToString().toByteArray())
    }

    fun itemStackFromBase64(data: String?): ItemStack? {
        if (data == null) return null
        val bytes = Base64.getDecoder().decode(data)
        val yaml = YamlConfiguration()
        yaml.load(String(bytes))
        return yaml.getItemStack("i")
    }

    fun create(material: Material, name: String, lore: List<String>, customModelData: Int): ItemStack {
        val item = ItemStack(material)
        val meta = item.itemMeta!!
        meta.setDisplayName(name)
        meta.lore = lore
        meta.setCustomModelData(customModelData)
        item.itemMeta = meta
        return item
    }
}
