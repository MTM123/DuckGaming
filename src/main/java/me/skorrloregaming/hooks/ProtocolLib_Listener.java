package me.skorrloregaming.hooks;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.ScheduledPacket;

import me.skorrloregaming.Server;

public class ProtocolLib_Listener {

	ProtocolManager protocolManager = null;

	PacketAdapter zeroLatencyAdapter = null;

	public void register() {
		protocolManager = ProtocolLibrary.getProtocolManager();
		zeroLatencyAdapter = new ZeroLatency(Server.getPlugin(), ListenerPriority.HIGHEST, new PacketType[] { PacketType.Status.Server.SERVER_INFO });
		protocolManager.addPacketListener(zeroLatencyAdapter);
	}

	public void unregister() {
		protocolManager.removePacketListener(zeroLatencyAdapter);
	}

	public class ZeroLatency extends PacketAdapter {

		public ZeroLatency(Plugin plugin, ListenerPriority listenerPriority, PacketType[] types) {
			super(plugin, listenerPriority, types);
		}

		@Override
		public void onPacketSending(PacketEvent event) {
			PacketContainer pongPacket = new PacketContainer(PacketType.Status.Server.PONG);
			pongPacket.getLongs().write(0, Long.valueOf(0L));
			event.schedule(new ScheduledPacket(pongPacket, event.getPlayer(), false));
		}

	}

}
