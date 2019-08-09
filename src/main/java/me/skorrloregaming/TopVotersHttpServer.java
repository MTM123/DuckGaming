package me.skorrloregaming;

import me.skorrloregaming.impl.Service;
import me.skorrloregaming.impl.Switches;
import me.skorrloregaming.impl.Switches.SwitchIntString;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import me.skorrloregaming.*;

public class TopVotersHttpServer implements Runnable {
	public ServerSocket server = null;
	public boolean running = false;

	public TopVotersHttpServer(int port) {
		try {
			server = new ServerSocket(port);
			Server.getInstance().getPlugin().getLogger().info("Top voters web server enabled on port " + port + ".");
			Server.getInstance().getBukkitTasks().add(Bukkit.getScheduler().runTaskAsynchronously(Server.getInstance().getPlugin(), this));
			running = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		running = false;
	}

	public class TopVotersHttpClient implements Runnable {
		private Socket socket;

		public TopVotersHttpClient(Socket socket) {
			this.socket = socket;
		}

		public void bind() {
			Thread thread = new Thread(this);
			thread.start();
		}

		public String formatAddress(Socket socket) {
			String addr = socket.getRemoteSocketAddress().toString().split(":")[0].replace("/", "");
			if (addr.equals("0"))
				addr = "127.0.0.1";
			return addr;
		}

		Comparator<Switches.SwitchIntString> myComparator = new Comparator<Switches.SwitchIntString>() {
			public int compare(Switches.SwitchIntString o1, Switches.SwitchIntString o2) {
				return Integer.compare(o2.getArg0(), o1.getArg0());
			}
		};

		@Override
		public void run() {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("<!DOCTYPE html>");
				sb.append("<html>");
				sb.append("<head>");
				sb.append("<script>");
				sb.append("function resize() {");
				sb.append("	   var height = document.getElementsByTagName('html')[0].scrollHeight;");
				sb.append("	   window.parent.postMessage(['setHeight', height], '*');");
				sb.append("}");
				sb.append("</script>");
				sb.append("<style>");
				sb.append("th, td {");
				sb.append("margin: 0;");
				sb.append("padding: 3.5px;");
				sb.append("text-align: center;");
				sb.append("}");
				sb.append("</style>");
				sb.append("</head>");
				sb.append("<body style=\"margin: 0; padding: 0;\" onLoad='resize();'>");
				sb.append("<table style=\"width: 100%;\">");
				sb.append("<caption>Summed total of votes from all websites</caption>");
				sb.append("<tr>");
				sb.append("<th>Username</th>");
				sb.append("<th>Votes this month</th> ");
				sb.append("<th>1</th>");
				sb.append("<th>2</th>");
				sb.append("<th>3</th>");
				sb.append("<th>4</th>");
				sb.append("<th>5</th>");
				sb.append("<th>6</th>");
				sb.append("<th>7</th>");
				sb.append("<th>8</th>");
				sb.append("<th>9</th>");
				sb.append("<th>10</th>");
				sb.append("</tr>");
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				int year = calendar.get(Calendar.YEAR);
				int monthId = calendar.get(Calendar.MONTH);
				String[] keys = Server.getInstance().getMonthlyVoteConfig().getData().getConfigurationSection("config").getKeys(false).toArray(new String[0]);
				List<SwitchIntString> validKeys = new ArrayList<SwitchIntString>();
				for (String username : keys) {
					int votes = Server.getInstance().getVoteManager().getMonthlyVotes(username, year, monthId);
					if (votes > 0) {
						validKeys.add(new SwitchIntString(votes, username));
					}
				}
				validKeys.sort(myComparator);
				for (SwitchIntString key : validKeys) {
					if (key == null) {
						sb.append("<tr><td></td><td></td></tr>");
					} else {
						sb.append("<tr>");
						sb.append("<td>" + key.getArg1() + "</td>");
						sb.append("<td>" + key.getArg0() + "</td>");
						for (int i = 0; i < 10; i++) {
							Service service = Service.values()[i];
							long arg0 = service.getPriority().getDelay();
							if (service.getPriority().isEpoch()) {
								long timestamp = Server.getInstance().getVoteManager().getLastVoteForService(key.getArg1(), service.getName());
								if (!(timestamp == 0)) {
									switch (service.getPriority()) {
										case midnight:
											arg0 = Server.getInstance().getVoteManager().getMidnight(timestamp);
											break;
										case midnightGreenwich:
											arg0 = Server.getInstance().getVoteManager().getMidnightGreenwich(timestamp);
											break;
										default:
											break;
									}
								}
							}
							sb.append("<td>" + Server.getInstance().getVoteManager().getFriendlyTimeDifference(key.getArg1(), service.getName(), arg0, service.getPriority().isEpoch()) + "</td>");
						}
						sb.append("</tr>");
					}
				}
				sb.append("</table>");
				sb.append("</body>");
				sb.append("</html>");
				byte[] data = sb.toString().getBytes();
				socket.getOutputStream().write("HTTP/1.1 200 OK\r\n".getBytes());
				socket.getOutputStream().write(("Content-Length: " + data.length + "\r\n").getBytes());
				socket.getOutputStream().write("Connection: close\r\n".getBytes());
				socket.getOutputStream().write("Content-Type: text/html; charset=iso-8859-1\r\n\r\n".getBytes());
				socket.getOutputStream().write(data);
				socket.getOutputStream().flush();
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					socket.close();
				} catch (Exception e1) {
				}
			}
		}
	}

	@Override
	public void run() {
		while (running) {
			try {
				Socket socket = server.accept();
				byte[] messageBytes = new byte[8192];
				socket.getInputStream().read(messageBytes);
				TopVotersHttpClient client = new TopVotersHttpClient(socket);
				client.bind();
			} catch (Exception e) {
			}
		}
	}
}
