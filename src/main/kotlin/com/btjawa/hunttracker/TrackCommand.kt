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

object TrackCommand {
    @Suppress("UnstableApiUsage")
    fun createCommand(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("track")
            .then(Commands.argument("target", ArgumentTypes.player())
                .executes { ctx ->
                    val playerSelector = ctx.getArgument("target", PlayerSelectorArgumentResolver::class.java)
                    val target = playerSelector.resolve(ctx.source).firstOrNull()
                    val sender = ctx.source.sender
                    if (sender !is Player) {
                        sender.sendMessage("Only players can use this command.")
                        return@executes 0
                    }
                    if (!TrackingManager.isHunter(sender.uniqueId)) {
                        sender.sendMessage(
                            Component.text("You are not the hunter! Please contact OP.", NamedTextColor.RED)
                        )
                        return@executes 0
                    }
                    if (target == null) {
                        sender.sendMessage("Player not found.")
                        return@executes 0
                    }
                    TrackingManager.track(sender.uniqueId, target.uniqueId)
                    sender.sendMessage(
                        Component.text("You're now tracking ${target.name}.", NamedTextColor.YELLOW)
                    )
                    target.sendMessage(
                        Component.text("${sender.name} has started tracking you!", NamedTextColor.RED)
                    )
                    Command.SINGLE_SUCCESS
                })
    }
}