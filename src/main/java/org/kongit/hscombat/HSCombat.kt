package org.kongit.hscombat

import com.comphenix.protocol.ProtocolManager
import com.github.horangshop.lib.HSLib
import com.github.horangshop.lib.plugin.HSPlugin
import org.kongit.hscombat.events.ProtocolLibEvents
import org.kongit.hscombat.events.TargetEvents
import org.kongit.hscombat.util.CombatUtil
import java.util.*


class HSCombat : HSPlugin() {
    companion object {
        private var plugins : HSCombat? = null
        private var protocolManager: ProtocolManager? = null
        fun getInstance() : HSCombat? { return plugins }
        fun getProtocol() : ProtocolManager? { return protocolManager }
    }

    override fun enable() {
        plugins = this
        protocolManager = HSLib.getInstance().dependencies.protocol
        ProtocolLibEvents().registerProtocol()
        registerEvent(TargetEvents())
    }

    override fun disable() {
        plugins = null

        for (task in CombatUtil.getTasks().values) {
            task.cancel()
        }
    }


}
