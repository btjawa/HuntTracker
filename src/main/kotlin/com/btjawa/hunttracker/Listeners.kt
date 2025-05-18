package com.btjawa.hunttracker

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent

class Listeners : Listener {
    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.sendMessage(
            Component.text("[HuntTracker] Welcome to the game!", NamedTextColor.BLUE)
        )
    }

    @EventHandler
    fun onTargetMove(event: PlayerMoveEvent) {
        val target = event.player
        TrackingManager.allEntries()
            .filter { it.value.target == target.uniqueId }
            .forEach {
                it.value.location = target.location.clone()
                it.value.lastUpdated = System.currentTimeMillis()
            }
    }
}