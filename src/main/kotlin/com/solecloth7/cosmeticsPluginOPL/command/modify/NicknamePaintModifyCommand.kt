package com.solecloth7.cosmeticsPluginOPL.command.modify

import com.solecloth7.cosmeticsPluginOPL.admin.AdminSelectionManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.NicknamePaintCosmetic
import com.solecloth7.cosmeticsPluginOPL.storage.JsonBackpackStorage
import org.bukkit.command.CommandSender

object NicknamePaintModifyCommand {
    fun handle(sender: CommandSender, args: List<String>) {
        val selected = AdminSelectionManager.getSelected(sender)
        if (selected !is NicknamePaintCosmetic) {
            sender.sendMessage("§cSelected item is not a nickname paint cosmetic.")
            return
        }

        if (args.isEmpty()) {
            sender.sendMessage("§cUsage: /a item modify nickname_paint <paint|quality> ...")
            return
        }

        when (args[0].lowercase()) {
            "paint" -> {
                if (args.size < 3) {
                    sender.sendMessage("§cUsage: /a item modify nickname_paint paint <hex1,hex2,...> <name>[,bold]")
                    return
                }

                val hexParts = args[1].split(",").map { it.trim() }.filter { it.startsWith("#") }
                val nameAndBold = args.drop(2).joinToString(" ").split(",")
                val name = nameAndBold.getOrNull(0)?.trim() ?: "Unnamed"
                val bold = nameAndBold.getOrNull(1)?.equals("bold", ignoreCase = true) ?: false

                selected.hexColors = hexParts
                selected.text = name
                selected.bold = bold

                sender.sendMessage("§aUpdated nickname paint gradient to: §f$name §7(${hexParts.size} colors)")
            }

            "quality" -> {
                if (args.size < 2) {
                    sender.sendMessage("§cUsage: /a item modify nickname_paint quality <type>")
                    return
                }

                selected.quality = args[1]
                sender.sendMessage("§aUpdated quality to §e${selected.quality}")
            }

            else -> sender.sendMessage("§cUnknown subcommand: ${args[0]}")
        }

        val owner = AdminSelectionManager.getSelectedOwner(sender)
        if (owner != null) {
            val index = owner.let { JsonBackpackStorage.loadBackpack(it).nicknamePaintCosmetics.indexOfFirst { it.id == selected.id } }
            if (index != -1) {
                JsonBackpackStorage.saveNicknamePaint(owner, index, selected)
            }
        }
    }
}
