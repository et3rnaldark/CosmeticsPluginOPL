package com.solecloth7.cosmeticsPluginOPL.command

import org.bukkit.command.CommandSender

interface AdminSubcommand {
    val name: String
    val description: String

    fun execute(sender: CommandSender, args: List<String>)
    fun tabComplete(sender: CommandSender, args: List<String>): List<String>
}
