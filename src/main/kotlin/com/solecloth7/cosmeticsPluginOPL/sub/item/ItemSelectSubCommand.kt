package com.solecloth7.cosmeticsPluginOPL.command.sub.item

import com.solecloth7.cosmeticsPluginOPL.command.AdminSubcommand
import com.solecloth7.cosmeticsPluginOPL.gui.BackpackGUI
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ItemSelectSubcommand : AdminSubcommand {
    override val name = "select"
    override val description = "Select and view another player's backpack"

    override fun execute(sender: CommandSender, args: List<String>) {
        if (sender !is Player) {
            sender.sendMessage("§cOnly players can use this command.")
            return
        }
        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /a item select <player>")
            return
        }
        val target = Bukkit.getPlayerExact(args[0])
        if (target == null) {
            sender.sendMessage("§cPlayer not found.")
            return
        }
        BackpackGUI.open(sender, target, allowSelect = true)
        sender.sendMessage("§aOpened ${target.name}'s backpack.")
    }

    override fun tabComplete(sender: CommandSender, args: List<String>): List<String> {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().map { it.name }.filter { it.startsWith(args[0], ignoreCase = true) }
        }
        return emptyList()
    }
}
