package me.skorrloregaming.ping;

import io.netty.channel.Channel;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Reflection;
import me.skorrloregaming.Server;
import me.skorrloregaming.ServerGet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import me.skorrloregaming.*;

public class PingInjector implements Listener {

	private Object server;
	private List<?> networkManagers;
	public boolean running = false;

	public PingInjector() {
		ServerGet.get().getPlugin().getServer().getPluginManager().registerEvents(this, ServerGet.get().getPlugin());
	}

	public void register() {
		try {
			this.networkManagers = Collections.synchronizedList((List<?>) getNetworkManagerList(CraftGo.CraftServer.getServerConnection()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		running = true;
	}

	public void unregister() {
		server = null;
		networkManagers = null;
		running = false;
	}

	public void injectOpenConnections() {
		try {
			Field field = Reflection.getFirstFieldByType(CraftGo.CraftServer.getNetworkManager(), Channel.class);
			field.setAccessible(true);
			for (Object manager : this.networkManagers) {
				Channel channel = (Channel) field.get(manager);
				try {
					if ((channel.pipeline().context("ping_handler") == null) && (channel.pipeline().context("packet_handler") != null)) {
						channel.pipeline().addBefore("packet_handler", "ping_handler", new DuplexHandler());
					}
				} catch (Exception e) {
					e.printStackTrace();
					final SocketAddress address = channel.remoteAddress();
					if (address instanceof InetSocketAddress) {
						final InetSocketAddress inetAddress = (InetSocketAddress) address;
						final String addr = inetAddress.getAddress().getHostAddress();
						Logger.getGlobal().warning("Duplex processing for /" + addr + ":" + inetAddress.getPort() + ", has failed! This is likely to be caused by premature closing of the stream, but there are other possible causes such as fake packets. If more of these errors come from the same address, you most likely can safely ignore the issue.");
						return;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getNetworkManagerList(Object conn) {
		try {
			Method[] arrayOfMethod;
			int j = (arrayOfMethod = conn.getClass().getDeclaredMethods()).length;
			for (int i = 0; i < j; i++) {
				Method method = arrayOfMethod[i];
				method.setAccessible(true);
				if (method.getReturnType() == List.class) {
					return method.invoke(null, new Object[]{conn});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void serverListPing(ServerListPingEvent event) {
		if (!running)
			return;
		String motd = ServerGet.get().getServerMotd();
		if (!(ServerGet.get().getTempMotd().equals("/unspecified"))) {
			motd = ServerGet.get().getTempMotd();
		}
		event.setMotd(motd);
		injectOpenConnections();
	}
}
