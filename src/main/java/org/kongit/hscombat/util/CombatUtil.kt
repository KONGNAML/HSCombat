package org.kongit.hscombat.util

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import org.kongit.hscombat.HSCombat
import java.util.*


class CombatUtil {

    companion object {
        private val playerTasks: MutableMap<UUID, BukkitTask> = HashMap()
        var combat:MutableMap<UUID,Long?> = mutableMapOf()
        var assist:MutableMap<Entity,MutableList<Player>> = mutableMapOf()
        fun getTasks() : MutableMap<UUID, BukkitTask> { return playerTasks }
    }

    fun enter(player: Player) {
        val playerUUID = player.uniqueId
        if (combat[playerUUID] == null) player.sendMessage("전투 시작")
        combat[playerUUID] = Date().time
        if (playerTasks.containsKey(playerUUID)) { playerTasks[playerUUID]!!.cancel() }


        // 새로운 작업 생성 및 저장
        val task = object : BukkitRunnable() {
            override fun run() {
                if ( combat[playerUUID] == null) return
                if (Date().time - combat[playerUUID]!! > 10000) {
                    end(player)
                }
            }
        }.runTaskTimer(HSCombat.getInstance()!!, 0L, 20L) // 0틱 후 시작, 100틱(5초)마다 실행

        playerTasks[playerUUID] = task
    }

    fun end(player: Player) {
        val playerUUID = player.uniqueId
        combat[playerUUID] = null

        // 작업이 존재하면 취소하고 맵에서 제거
        if (playerTasks.containsKey(playerUUID)) {
            player.sendMessage("전투 종료")
            playerTasks[playerUUID]!!.cancel()
            playerTasks.remove(playerUUID)
        }
    }

}