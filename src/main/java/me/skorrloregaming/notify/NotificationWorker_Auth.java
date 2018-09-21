package me.skorrloregaming.notify;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.bukkit.OfflinePlayer;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Logger;
import me.skorrloregaming.Server;

public class NotificationWorker_Auth implements Runnable {
	private Socket socket;

	public NotificationWorker_Auth(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		try {
			String st = dateFormat.format(new Date()) + " [INFO] ";
			socket.getOutputStream().write((st + "Connection success." + '\n' + st + "Please type the username you'd like to use.").getBytes());
			socket.getOutputStream().flush();
			NotificationWorker_AuthLoop authLoop = new NotificationWorker_AuthLoop(socket);
			Thread authLoopThread = new Thread(authLoop);
			authLoopThread.start();
			byte[] packet1bytes = new byte[512];
			int read1 = socket.getInputStream().read(packet1bytes);
			byte[] packet1bytesF = Arrays.copyOf(packet1bytes, read1);
			String username = new String(packet1bytesF);
			String password = null;
			OfflinePlayer player = CraftGo.Player.getOfflinePlayer(username);
			if (!player.hasPlayedBefore() && !player.isOnline()) {
				Server.getNotifyWorker().playAccountNotRegistered(socket);
				socket.close();
				return;
			}
			String path = "config." + player.getUniqueId();
			if (!Server.getPlugin().getConfig().contains(path)) {
				Server.getNotifyWorker().playAccountNotRegistered(socket);
				socket.close();
				return;
			}
			String address = Server.getPlugin().getConfig().getString(path + ".ip");
			Logger.info("Checking banned signature (" + address.replace("x", ".") + ") ...", true);
			if (Server.getBanConfig().getData().contains(address)) {
				Logger.info("Complete banned signature found, disallowing connection..", true);
				socket.getOutputStream().write((dateFormat.format(new Date()) + " [INFO] " + "Connection terminated, the specified account is banned from the server.").getBytes());
				socket.getOutputStream().flush();
				Thread.sleep(200);
				socket.close();
				return;
			}
			if (!($.getAuthenticationSuite() == null)) {
				Object authObject = $.getAuthenticationSuite();
				if (!((fr.xephi.authme.api.v3.AuthMeApi) authObject).isRegistered(username)) {
					Server.getNotifyWorker().playAccountNotRegistered(socket);
					socket.close();
					return;
				}
				socket.getOutputStream().write((dateFormat.format(new Date()) + " [INFO] " + "Please type the authentication password for the account.").getBytes());
				socket.getOutputStream().flush();
				authLoop.stage++;
				byte[] packet2bytes = new byte[512];
				int read2 = socket.getInputStream().read(packet2bytes);
				byte[] packet2bytesF = Arrays.copyOf(packet2bytes, read2);
				password = new String(packet2bytesF);
				if (((fr.xephi.authme.api.v3.AuthMeApi) authObject).checkPassword(username, password)) {
					socket.getOutputStream().write((dateFormat.format(new Date()) + " [INFO] " + "Connection authenticated.").getBytes());
					socket.getOutputStream().flush();
					authLoop.stop = true;
				} else {
					socket.getOutputStream().write((dateFormat.format(new Date()) + " [INFO] " + "The specified authentication password is not correct.").getBytes());
					socket.getOutputStream().flush();
					socket.close();
					return;
				}
			}
			Client client = new Client(socket, username, password);
			new ClientConnectEvent(client);
			NotificationWorker_Listen listen = new NotificationWorker_Listen(client);
			Thread thread = new Thread(listen);
			thread.setName("Notification Worker, Listen Thread");
			thread.start();
			Server.getNotifyWorker().clients.add(client);
		} catch (Exception ex1) {
			try {
				try {
					socket.getOutputStream().write((dateFormat.format(new Date()) + " [INFO] " + "An internal error occured while processing authentication.").getBytes());
					socket.getOutputStream().flush();
					Thread.sleep(200);
				} catch (Exception ex2) {
				}
				socket.close();
			} catch (Exception ex3) {
			}
		}
	}
}
