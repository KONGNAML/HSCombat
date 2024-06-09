package org.kongit.hscombat.util

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import java.util.*

class BossBarUtil {

    companion object {
        private val bossBars: MutableMap<UUID, BossBar> = HashMap()
    }

    fun createBossBar(player: Player, title: String, color: BarColor, style: BarStyle,keep: Boolean = false) {
        val bossBar = Bukkit.createBossBar(title, color, style)
        if (!keep) removeBossBar(player)
        bossBar.addPlayer(player)
        bossBars[player.uniqueId] = bossBar
    }

    fun updateBossBar(player: Player, progress: Double) {
        val bossBar = bossBars[player.uniqueId]
        if (bossBar != null) {
            bossBar.progress = progress
            bossBars[player.uniqueId] = bossBar
        }
    }

    fun removeBossBar(player: Player) {
        val bossBar = bossBars[player.uniqueId]
        if (bossBar != null) {
            bossBar.removePlayer(player)
            bossBars.remove(player.uniqueId)
        }
    }

    fun setBossBarTitle(player: Player, title: String) {
        val bossBar = bossBars[player.uniqueId]
        if (bossBar != null) {
            bossBar.setTitle(title)
        }
    }

    fun setBossBarColor(player: Player, color: BarColor) {
        val bossBar = bossBars[player.uniqueId]
        if (bossBar != null) {
            bossBar.setColor(color)
        }
    }

    fun setBossBarStyle(player: Player, style: BarStyle) {
        val bossBar = bossBars[player.uniqueId]
        if (bossBar != null) {
            bossBar.setStyle(style)
        }
    }
}