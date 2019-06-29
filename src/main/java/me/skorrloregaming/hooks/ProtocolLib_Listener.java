package me.skorrloregaming.hooks;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.StructureModifier;
import me.skorrloregaming.$;
import me.skorrloregaming.impl.ServerMinigame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ProtocolLib_Listener {
	private Plugin plugin;

	public ProtocolLib_Listener(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register() {
		ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener) new PacketAdapter(this.plugin, new PacketType[]{PacketType.Play.Server.UPDATE_TIME}) {

			public void onPacketSending(PacketEvent event) {
				Player player = event.getPlayer();
				ServerMinigame minigame = $.getCurrentMinigame(player);
				PacketContainer packet = event.getPacket();
				if (minigame == ServerMinigame.SKYFIGHT) {
					packet.getLongs().write(1, 10000L);
				} else if ($.daylightMinigames.contains(minigame.toString().toLowerCase())) {
					packet.getLongs().write(1, 8000L);
				} else if (minigame == ServerMinigame.KITPVP) {
					packet.getLongs().write(1, 14000L);
				}
				event.setPacket(packet);
			}
		});
	}

}