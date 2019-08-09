package me.skorrloregaming.ping;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Reflection;
import me.skorrloregaming.Server;
import me.skorrloregaming.impl.NpcPlayer;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.skorrloregaming.*;

public class DuplexHandler extends ChannelDuplexHandler {

	private final Field serverPingField = Reflection.getFirstFieldByType(CraftGo.Packet.Ping.getPacketType(), CraftGo.Packet.Ping.getServerPing());

	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if ((CraftGo.Packet.Ping.getPacketType().isAssignableFrom(msg.getClass()))) {
			PingReply reply = constructReply(msg, ctx);
			super.write(ctx, constructPacket(reply), promise);
			return;
		}
		super.write(ctx, msg, promise);
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		super.channelRead(ctx, msg);
	}

	private PingReply constructReply(Object rawPacket, ChannelHandlerContext ctx) {
		try {
			Object packet = serverPingField.get(rawPacket);
			String motd = CraftGo.Packet.Ping.getMOTD(packet);
			CraftGo.Packet.Ping.PlayerSample playerSample = CraftGo.Packet.Ping.getPlayerSample(packet);
			CraftGo.Packet.Ping.ServerInfo serverInfo = CraftGo.Packet.Ping.getServerInfo(packet);
			int max = playerSample.getMaxPlayers();
			int online = playerSample.getOnlinePlayers();
			int protocolVersion = serverInfo.getProtocolVersion();
			String protocolName = serverInfo.getProtocolName();
			Object profiles = playerSample.getProfiles();
			List<String> list = new ArrayList<>();
			for (int i = 0; i < Array.getLength(profiles); i++) {
				list.add((String) CraftGo.GameProfile.valueOf(Array.get(profiles, i)).getName());
			}
			return new PingReply(ctx, motd, online, max, protocolVersion, protocolName, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Object constructPacket(PingReply reply) {
		try {
			int playerCount = reply.getOnlinePlayers() + Server.getInstance().getNpcPlayers().size();
			Object sample = Array.newInstance(CraftGo.GameProfile.getComponentType(), playerCount);
			for (int i = 0; i < reply.getPlayerSample().size(); i++) {
				String profile = reply.getPlayerSample().get(i);
				UUID id = UUID.fromString(CraftGo.Player.getUUID(profile, true));
				if (id == null)
					throw new Exception("Failed to retrieve online-mode uuid for player \"" + profile + "\".");
				Array.set(sample, i, CraftGo.GameProfile.newInstance(id, profile).get());
			}
			for (int i = 0; i < Server.getInstance().getNpcPlayers().size(); i++) {
				NpcPlayer profile = Server.getInstance().getNpcPlayers().get(i);
				Object gameProfile = CraftGo.GameProfile.newInstance(profile.getOnlineId(), profile.getName()).get();
				Array.set(sample, reply.getPlayerSample().size() + i, gameProfile);
			}
			Object ping = CraftGo.Packet.Ping.getServerPing().newInstance();
			CraftGo.Packet.Ping.setMOTD(ping, reply.getMOTD());
			CraftGo.Packet.Ping.setPlayerSample(ping, new CraftGo.Packet.Ping.PlayerSample(reply.getMaxPlayers(), playerCount, sample));
			if (Server.getInstance().getBanConfig().getData().contains(reply.getHostAddress().replace(".", "x"))) {
				CraftGo.Packet.Ping.setServerInfo(ping, new CraftGo.Packet.Ping.ServerInfo("You are banned.", -1));
			} else {
				CraftGo.Packet.Ping.setServerInfo(ping, new CraftGo.Packet.Ping.ServerInfo(reply.getProtocolName(), reply.getProtocolVersion()));
			}
			CraftGo.Packet.Ping.setFavicon(ping, (String) CraftGo.Packet.Ping.getCraftIconCache().getField("value").get(reply.getIcon()));
			return CraftGo.Packet.Ping.newInstance(ping);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}