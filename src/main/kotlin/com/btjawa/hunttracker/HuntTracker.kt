package com.btjawa.hunttracker

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.java.JavaPlugin

class HuntTracker : JavaPlugin(), Listener {

    override fun onEnable() {
        // Plugin startup logic
        server.pluginManager.registerEvents(this, this);
        logger.info("HuntTracker enabled");
    }

    override fun onDisable() {
        // Plugin shutdown logic
        HandlerList.unregisterAll(this as Listener);
        logger.info("HuntTracker disabled");
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        server.broadcast(
            Component.text("${event.player.name}, Hello World!", NamedTextColor.GREEN)
        );
    }
}
