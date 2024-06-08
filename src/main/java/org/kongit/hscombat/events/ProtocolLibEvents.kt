package org.kongit.hscombat.events

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import org.kongit.hscombat.HSCombat
import org.kongit.hscombat.util.CombatUtil
import org.kongit.hscombat.util.CombatUtil.Companion.combat
import java.util.*

class ProtocolLibEvents {
    fun registerProtocol() {

        HSCombat.getProtocol()!!.addPacketListener(object  : PacketAdapter(HSCombat.getInstance() , PacketType.Play.Server.PLAYER_COMBAT_KILL) {
            override fun onPacketSending(event: PacketEvent) {

                CombatUtil().end(event.player)

            }
        })

        HSCombat.getProtocol()!!.addPacketListener(object  : PacketAdapter(HSCombat.getInstance() , PacketType.Play.Server.PLAYER_COMBAT_ENTER) {
            override fun onPacketSending(event: PacketEvent) {

                CombatUtil().enter(event.player)

            }
        })

        HSCombat.getProtocol()!!.addPacketListener(object  : PacketAdapter(HSCombat.getInstance() , PacketType.Play.Server.PLAYER_COMBAT_END) {
            override fun onPacketSending(event: PacketEvent) {

                CombatUtil().end(event.player)


            }
        })

        HSCombat.getProtocol()!!.addPacketListener(object  : PacketAdapter(HSCombat.getInstance() , PacketType.Play.Client.CLIENT_COMMAND) {
            override fun onPacketReceiving(event: PacketEvent?) {

                if (event == null) return

                if (combat[event.player.uniqueId] != null) {
                    event.isCancelled = true
                    event.player.sendMessage("전투중에는 명령어 사용이 불가능합니다.")
                }


            }

        })
    }
}