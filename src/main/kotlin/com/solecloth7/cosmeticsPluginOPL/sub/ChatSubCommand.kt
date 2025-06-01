package com.solecloth7.cosmeticsPluginOPL.command.sub

import com.solecloth7.cosmeticsPluginOPL.command.AdminSubcommand
import org.bukkit.command.CommandSender

class ChatSubcommand : AdminSubcommand {
    override val name = "chat"
    override val description = "Admin chat command."

    override fun execute(sender: CommandSender, args: List<String>) {
        sender.sendMessage("§bYou used §e/a chat§b!")
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        return emptyList()
    }
}
