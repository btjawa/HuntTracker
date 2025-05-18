package com.btjawa.hunttracker

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin


class HuntTracker : JavaPlugin() {
    companion object {
        lateinit var instance: HuntTracker private set
    }
    @Suppress("UnstableApiUsage")
    override fun onEnable() {
        // Plugin startup logic
        logger.info("HuntTracker enabled")
        instance = this
        server.pluginManager.registerEvents(Listeners(), this)
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { commands ->
            commands.registrar().register(TrackCommand.createCommand().build())
            commands.registrar().register(HuntTrackerCommand.createCommand().build())
        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        logger.info("HuntTracker disabled")
        HandlerList.unregisterAll(this)
    }
}