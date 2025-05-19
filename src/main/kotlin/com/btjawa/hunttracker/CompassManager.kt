package com.btjawa.hunttracker

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.CompassMeta
import org.bukkit.persistence.PersistentDataType

object CompassManager {
    private val key = NamespacedKey(HuntTracker.instance, "hunttracker_compass")
    fun newCompass(location: Location? = null): ItemStack {
        val compass = ItemStack(Material.COMPASS)
        val meta = compass.itemMeta as CompassMeta
        meta.lodestone = location
        meta.isLodestoneTracked = false
        meta.displayName(
            Component.text("Tracker", NamedTextColor.YELLOW)
        )
        meta.lore(
            listOf(Component.text("No track target", NamedTextColor.RED))
        )
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.addEnchant(Enchantment.POWER, 1, true)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        compass.itemMeta = meta
        return compass
    }
    fun updateCompass(hunter: Player, target: Player) {
        val compass = hunter.inventory.contents.firstOrNull {
            it?.itemMeta?.persistentDataContainer?.has(key, PersistentDataType.BYTE) == true
        } ?: return
        val meta = compass.itemMeta as CompassMeta
        val loc = target.location
        meta.lodestone = loc.clone()
        meta.displayName(
            Component.text("Tracker (${target.name})", NamedTextColor.YELLOW)
        )
        meta.lore(
            listOf(
                Component.text("World: ${target.world.name}", NamedTextColor.AQUA),
                Component.text("x: ${loc.blockX}, y: ${loc.blockY}, z: ${loc.blockZ}", NamedTextColor.AQUA)
            )
        )
        compass.itemMeta = meta
    }
    fun clearCompass(target: Player) {
        val inventory = target.inventory
        for (item in inventory.contents) {
            if (item?.type != Material.COMPASS) continue
            if (item.itemMeta.persistentDataContainer.has(key, PersistentDataType.BYTE)) {
                inventory.remove(item)
            }
        }
    }
    fun getKey() = key
}