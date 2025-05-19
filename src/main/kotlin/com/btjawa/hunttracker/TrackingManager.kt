package com.btjawa.hunttracker

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.*

data class TrackingEntry(
    var target: UUID,
    var location: Location,
    var lastUpdated: Long = System.currentTimeMillis()
)

object TrackingManager {
    private var trackers = mutableListOf<UUID>()
    private var trackingMap = mutableMapOf<UUID, TrackingEntry>()
    fun add(hunter: UUID) {
        val target = Bukkit.getPlayer(hunter) ?: return
        trackers.add(hunter)
        target.sendMessage(
            Component.text("${target.name}, You become a hunter!", NamedTextColor.AQUA)
                .appendNewline()
                .append(Component.text("Use ", NamedTextColor.BLUE))
                .append(Component.text("/track <player>", NamedTextColor.YELLOW))
                .append(Component.text(" to track a target.", NamedTextColor.BLUE))
                .appendNewline()
                .append(Component.text("Since we cannot modify NBT directly, please don't hold the Tracker compass.", NamedTextColor.BLUE))
        )
        CompassManager.clearCompass(target)
        target.inventory.addItem(
            CompassManager.newCompass()
        )
    }
    fun track(from: UUID, to: UUID) {
        if (!isHunter(from)) return
        val hunter = Bukkit.getPlayer(from) ?: return
        val target = Bukkit.getPlayer(to) ?: return
        trackingMap[from] = TrackingEntry(
            to,
            target.location.clone(),
            System.currentTimeMillis()
        )
        CompassManager.updateCompass(hunter, target)
    }
    fun allEntries() = trackingMap.entries
    fun isHunter(hunter: UUID) = trackers.any { it == hunter }
}