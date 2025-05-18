package com.btjawa.hunttracker

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
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
            listOf(Component.text("Points to the specified target", NamedTextColor.YELLOW))
        )
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        meta.addEnchant(Enchantment.POWER, 1, true)
        meta.persistentDataContainer.set(key, PersistentDataType.BYTE, 1)
        compass.itemMeta = meta
        return compass
    }
    fun clearCompass(player: Player) {
        val inventory = player.inventory
        for (item in inventory.contents) {
            if (item?.type != Material.COMPASS) continue
            if (item.itemMeta.persistentDataContainer.has(key, PersistentDataType.BYTE)) {
                inventory.remove(item)
            }
        }
    }
}