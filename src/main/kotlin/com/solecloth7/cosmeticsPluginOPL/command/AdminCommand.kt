package com.solecloth7.cosmeticsPluginOPL.command

import com.solecloth7.cosmeticsPluginOPL.command.sub.ItemSubcommand
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class AdminCommand : CommandExecutor, TabCompleter {
    private val subcommands = listOf(
        ItemSubcommand()
    )

    private val subcommandMap = subcommands.associateBy { it.name.lowercase() }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.isEmpty()) {
            sender.sendMessage("§bAvailable subcommands: §e${subcommands.joinToString(", ") { it.name }}")
            return true
        }

        val sub = subcommandMap[args[0].lowercase()]
        if (sub != null) {
            sub.execute(sender, args.drop(1))
        } else {
            sender.sendMessage("§cUnknown subcommand. Use: /$label <${subcommands.joinToString("|") { it.name }}>")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String> {
        if (args.size == 1) {
            val current = args[0].lowercase()
            return subcommands.map { it.name }.filter { it.startsWith(current) }
        }

        val sub = subcommandMap[args[0].lowercase()] ?: return emptyList()
        return sub.tabComplete(sender, args.drop(1))
    }
}
