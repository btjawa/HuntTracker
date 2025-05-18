package com.btjawa.hunttracker

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

public object HuntTrackerCommand {
    @Suppress("UnstableApiUsage")
    fun createCommand(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("hunttracker")
            .then(Commands.literal("addhunter")
                .then(Commands.argument("target", ArgumentTypes.player())
                    .executes { ctx ->
                        val playerSelector = ctx.getArgument("target", PlayerSelectorArgumentResolver::class.java)
                        val target = playerSelector.resolve(ctx.source).firstOrNull()
                        val sender = ctx.source.sender
                        if (target == null) {
                            sender.sendMessage("Player not found.")
                            return@executes 0
                        }
                        if (TrackingManager.isHunter(target.uniqueId)) {
                            sender.sendMessage("${target.name} is already a hunter.")
                            return@executes 0
                        }
                        TrackingManager.add(target.uniqueId)
                        Command.SINGLE_SUCCESS
                    }
                )
            )
            .then(Commands.literal("reset")
                .executes { ctx ->
                    Command.SINGLE_SUCCESS
                })
            .then(Commands.literal("status"))
    }
}