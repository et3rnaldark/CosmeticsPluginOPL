package com.solecloth7.cosmeticsPluginOPL.command.sub

import com.solecloth7.cosmeticsPluginOPL.command.AdminSubcommand
import com.solecloth7.cosmeticsPluginOPL.command.sub.item.ItemCreateSubcommand
import com.solecloth7.cosmeticsPluginOPL.command.sub.item.ItemModifySubcommand
import com.solecloth7.cosmeticsPluginOPL.command.sub.item.ItemSelectSubcommand
import org.bukkit.command.CommandSender

class ItemSubcommand : AdminSubcommand {
    override val name = "item"
    override val description = "Item-related commands"
    private val subcommands = listOf(
        ItemCreateSubcommand(),
        ItemModifySubcommand(),
        ItemSelectSubcommand(),
    )

    override fun execute(sender: CommandSender, args: List<String>) {
        if (args.isEmpty()) {
            sender.sendMessage("§7Available: create, modify")
            return
        }
        val sub = subcommands.find { it.name.equals(args[0], ignoreCase = true) }
        if (sub != null) {
            sub.execute(sender, args.drop(1))
        } else {
            sender.sendMessage("§cUnknown subcommand: ${args[0]}")
        }
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        if (args.isEmpty() || args.size == 1) {
            return subcommands.map { it.name }.filter { it.startsWith(args.getOrNull(0) ?: "") }
        }
        val sub = subcommands.find { it.name.equals(args[0], ignoreCase = true) } ?: return emptyList()
        return sub.tabComplete(sender, args.drop(1))
    }
}
