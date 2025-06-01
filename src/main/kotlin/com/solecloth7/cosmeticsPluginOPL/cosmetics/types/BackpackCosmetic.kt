package com.solecloth7.cosmeticsPluginOPL.cosmetics.types

import org.bukkit.inventory.ItemStack

interface BackpackCosmetic {
    fun toItem(): ItemStack
}