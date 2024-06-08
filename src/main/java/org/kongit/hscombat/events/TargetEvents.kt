package org.kongit.hscombat.events

import com.github.horangshop.lib.plugin.listener.HSListener
import org.bukkit.entity.Entity
import org.bukkit.entity.Monster
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.scheduler.BukkitRunnable
import org.kongit.hscombat.HSCombat
import org.kongit.hscombat.util.CombatUtil
import org.kongit.hscombat.util.CombatUtil.Companion.assist
import org.kongit.hscombat.util.CombatUtil.Companion.combat
import java.util.*


class TargetEvents : HSListener(HSCombat.getInstance()) {

    private val targetTasks: MutableMap<UUID, BukkitRunnable> = HashMap()

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity: Entity = event.entity
        val target: Entity = event.target ?: return

        if (entity is Monster && target is Player) {
            // 기존의 스케줄러가 있으면 취소
            targetTasks[target.uniqueId]?.cancel()
            // 새로운 스케줄러 시작
            val task = object : BukkitRunnable() {
                override fun run() {
                    if (entity.isDead) this.cancel()
                    if (entity.target == target) { CombatUtil().enter(target)
                    } else {
                        this.cancel()
                        targetTasks.remove(target.uniqueId)
                    }
                }
            }
            task.runTaskTimer(HSCombat.getInstance()!!, 0L, 15L) // 1초(20틱)마다 실행
            targetTasks[target.uniqueId] = task
        }
    }

    @EventHandler
    fun onPlayerAssist(event: EntityDamageByEntityEvent) {
        if (event.entity is Player || event.damager is Player) {
            val attacker = event.damager
            val target = event.entity
            if (attacker is Player) {
                if (assist[target] == null) assist[target] = mutableListOf()
                if (!assist[target]!!.contains(attacker)) assist[target]!!.add(attacker)
                CombatUtil().enter(attacker)
            }
        }
    }

    @EventHandler
    fun onPlayerKill(event: EntityDeathEvent) {
        val target = event.entity
        if (assist[target] == null) return

        for (assister in assist[target] ?: mutableListOf()) {

            if (target.killer != assister) { assister.sendMessage("Assist ${target.name}")
            } else { assister.sendMessage("Kill ${target.name}") }

        }


    }

}