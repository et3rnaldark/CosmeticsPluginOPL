package com.solecloth7.cosmeticsPluginOPL.command.create

import com.solecloth7.cosmeticsPluginOPL.admin.AdminBackpackSession
import com.solecloth7.cosmeticsPluginOPL.cosmetics.CosmeticManager
import com.solecloth7.cosmeticsPluginOPL.cosmetics.types.TitleCosmetic
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object TitleCreateCommand {
    fun handle(sender: CommandSender, args: List<String>) {
        if (args.size < 1) {
            sender.sendMessage("§cUsage: /a item create <player> title")
            return
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sender.sendMessage("§cPlayer not found.")
            return
        }

        val cosmetic = TitleCosmetic()
        CosmeticManager.giveTitleCosmetic(target, cosmetic)

        sender.sendMessage("§aCreated title cosmetic for ${target.name}.")
        target.sendMessage("§aYou received a §eTitle Cosmetic§a!")
    }
}
