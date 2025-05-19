package com.btjawa.hunttracker

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.persistence.PersistentDataType

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
        val entries = TrackingManager.allEntries()
        entries
            .filter { it.value.target == target.uniqueId }
            .forEach {
                val hunter = Bukkit.getPlayer(it.key) ?: return@forEach
                if (hunter.world.name != target.world.name) {
                    return@forEach
                }
                val current = it.value.location
                val updated = target.location
                if (
                    current.blockX == updated.blockX &&
                    current.blockY == updated.blockY &&
                    current.blockZ == updated.blockZ
                ) return@forEach
                it.value.location = updated.clone()
                it.value.lastUpdated = System.currentTimeMillis()
                CompassManager.updateCompass(hunter, target)
            }
    }

    @EventHandler
    fun onWorldChange(event: PlayerChangedWorldEvent) {
        val target = event.player
        val entries = TrackingManager.allEntries()
        entries
            .filter { it.value.target == target.uniqueId }
            .forEach {
                val hunter = Bukkit.getPlayer(it.key) ?: return@forEach
                hunter.sendMessage(
                    Component.text(target.name, NamedTextColor.YELLOW)
                        .append(Component.text(" has reached to ", NamedTextColor.AQUA))
                        .append(Component.text(target.world.name, NamedTextColor.YELLOW))
                        .append(Component.text(" !"))
                        .appendNewline()
                        .append(Component.text("For now, your compass will point to ", NamedTextColor.AQUA))
                        .append(Component.text(target.name, NamedTextColor.YELLOW))
                        .append(Component.text("'s last location in this world.", NamedTextColor.AQUA))
                )
            }
    }

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val target = event.player
        if (
            event.itemDrop.itemStack.persistentDataContainer.has(
                CompassManager.getKey(), PersistentDataType.BYTE
            )
        ) {
            event.isCancelled = true
        }
    }
}