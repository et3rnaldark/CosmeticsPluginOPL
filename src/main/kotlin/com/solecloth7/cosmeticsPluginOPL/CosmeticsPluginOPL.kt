package com.solecloth7.cosmeticsPluginOPL

import com.solecloth7.cosmeticsPluginOPL.command.AdminCommand
import com.solecloth7.cosmeticsPluginOPL.command.BackpackCommand
import com.solecloth7.cosmeticsPluginOPL.listener.BackpackListener
import com.solecloth7.cosmeticsPluginOPL.listener.ChatColorListener
import com.solecloth7.cosmeticsPluginOPL.listener.PlayerJoinListener
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import org.bukkit.plugin.java.JavaPlugin

class CosmeticsPluginOPL : JavaPlugin() {

    override fun onEnable() {
        JsonBackpackStorage.dataFolder = dataFolder.resolve("backpacks")
        getCommand("backpack")?.setExecutor(BackpackCommand())
        getCommand("a")?.setExecutor(AdminCommand())
        getCommand("a")?.tabCompleter = AdminCommand()
        server.pluginManager.registerEvents(ChatColorListener(this), this)
        server.pluginManager.registerEvents(BackpackListener(this), this)
        server.pluginManager.registerEvents(PlayerJoinListener(), this)

        logger.info("Plugin OPLCosmetics enabled with version 1.0.0")
    }
// Test
    override fun onDisable() {
        logger.info("Plugin OPLCosmetics Disabled with version 1.0.0.")
    }
}
