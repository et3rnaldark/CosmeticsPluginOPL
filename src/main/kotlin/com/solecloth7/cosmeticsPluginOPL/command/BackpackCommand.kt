package com.solecloth7.cosmeticsPluginOPL.command

import com.solecloth7.cosmeticsPluginOPL.gui.BackpackGUI
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class BackpackCommand : CommandExecutor, TabCompleter {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command.")
            return true
        }

        if (args.isEmpty()) {
            BackpackGUI.open(sender) // open own backpack
        } else {
            val target = Bukkit.getPlayer(args[0])
            if (target == null || !target.isOnline) {
                sender.sendMessage("§cPlayer '${args[0]}' is not online.")
                return true
            }

            // ✅ View-only mode when viewing others
            BackpackGUI.open(sender, target, allowSelect = false)
            sender.sendMessage("§aOpened ${target.name}'s backpack.")
        }

        return true
    }
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        return if (args.size == 1) {
            Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], ignoreCase = true) }
        } else {
            emptyList()
        }
    }
}
