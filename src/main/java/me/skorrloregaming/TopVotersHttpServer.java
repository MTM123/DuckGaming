package me.skorrloregaming;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import me.skorrloregaming.impl.Switches;
import me.skorrloregaming.impl.Switches.SwitchIntString;

public class TopVotersHttpServer implements Runnable {
	public SSLServerSocket server = null;
	public boolean running = false;

	public TopVotersHttpServer(int port) {
		try {
			String keyStore = new File(System.getProperty("user.dir"), "server.keystore").getPath();
			String trustStore = new File(System.getProperty("user.dir"), "server.truststore").getPath();
			Server.getPlugin().getLogger().info("Using keyStore located at " + keyStore);
			System.setProperty("javax.net.ssl.keyStore", keyStore);
			System.setProperty("javax.net.ssl.keyStorePassword", "cloudflare");
			Server.getPlugin().getLogger().info("Using trustStore located at " + trustStore);
			System.setProperty("javax.net.ssl.trustStrore", trustStore);
			System.setProperty("javax.net.ssl.trustStorePassword", "cloudflare");
			SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
			server = (SSLServerSocket) ssf.createServerSocket(port);
			Server.getPlugin().getLogger().info("Top voters web server enabled on port " + port + ".");
			new Thread(this).start();
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
				sb.append("<style>");
				sb.append("th, td {");
				sb.append("margin: 0;");
				sb.append("padding: 3.5px;");
				sb.append("text-align: center;");
				sb.append("}");
				sb.append("</style>");
				sb.append("</head>");
				sb.append("<body style=\"margin: 0; padding: 0;\">");
				sb.append("<table style=\"width: 100%;\">");
				sb.append("<caption>Summed total of votes from all websites</caption>");
				sb.append("<tr>");
				sb.append("<th>Username</th>");
				sb.append("<th>Votes this month</th> ");
				sb.append("</tr>");
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				int year = calendar.get(Calendar.YEAR);
				int monthId = calendar.get(Calendar.MONTH);
				String[] keys = Server.getMonthlyVoteConfig().getData().getConfigurationSection("config").getKeys(false).toArray(new String[0]);
				List<SwitchIntString> validKeys = new ArrayList<SwitchIntString>();
				for (String username : keys) {
					int votes = Server.getVoteListener().getMonthlyVotes(username, year, monthId);
					if (votes > 0) {
						validKeys.add(new SwitchIntString(votes, username));
					}
				}
				validKeys.sort(myComparator);
				SwitchIntString[] trimmedKeys = Arrays.copyOf(validKeys.toArray(new SwitchIntString[0]), 5);
				for (SwitchIntString key : trimmedKeys) {
					if (key == null) {
						sb.append("<tr><td></td><td></td></tr>");
					} else {
						sb.append("<tr>");
						sb.append("<td>" + key.getArg1() + "</td>");
						sb.append("<td>" + key.getArg0() + "</td>");
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
				System.out.print(new String(messageBytes));
				TopVotersHttpClient client = new TopVotersHttpClient(socket);
				client.bind();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
