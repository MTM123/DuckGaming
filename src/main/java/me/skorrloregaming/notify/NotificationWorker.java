package me.skorrloregaming.notify;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.skorrloregaming.$;
import me.skorrloregaming.CraftGo;
import me.skorrloregaming.Server;
import me.skorrloregaming.commands.ListCmd;

public class NotificationWorker implements Runnable {
	public final int PORT;
	private ServerSocket server = null;
	public ArrayList<Client> clients = new ArrayList<Client>();

	public NotificationWorker(int port) {
		this.PORT = port;
	}

	public boolean start() {
		if (!(server == null) && !server.isClosed())
			return false;
		try {
			server = new ServerSocket(PORT);
			Thread thread = new Thread(this);
			thread.setName("Notification Worker");
			thread.start();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean stop(boolean wait) throws InterruptedException {
		if (server == null)
			return false;
		if (server.isClosed())
			return false;
		if (Bukkit.getServer().getOnlineMode())
			return false;
		try {
			server.close();
			clients.clear();
			while (wait && !server.isClosed())
				Thread.sleep(10);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean stop() throws InterruptedException {
		return stop(false);
	}

	public int broadcast(String message, int minRankId) {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		message = dateFormat.format(date) + " [INFO] " + message;
		Client[] clientsArr = clients.toArray(new Client[0]).clone();
		int totalSent = 0;
		for (int i = 0; i < clientsArr.length; i++) {
			Client client = clientsArr[i];
			OfflinePlayer player = CraftGo.Player.getOfflinePlayer(client.getUsername());
			if (player.isOnline() || player.hasPlayedBefore()) {
				if ($.getRankId(player.getUniqueId()) >= minRankId) {
					try {
						client.socket.getOutputStream().write((ChatColor.stripColor(message) + '\n').getBytes());
						client.socket.getOutputStream().flush();
						totalSent++;
					} catch (IOException e) {
						kickPlayer(client, null);
					}
				}
			}
		}
		return totalSent;
	}

	public int broadcast(String message) {
		return broadcast(message, -1);
	}

	public boolean hasPermissionExecute(OfflinePlayer player) {
		return (player.isOp() || $.validRanksNotifyWorkerExecuteCommand.contains($.getRank(player.getUniqueId())));
	}

	public int kickPlayer(String username, String reason) {
		Client[] clientsArr = clients.toArray(new Client[0]).clone();
		int totalSent = 0;
		for (int i = 0; i < clientsArr.length; i++) {
			Client client = clientsArr[i];
			if (client.getUsername().equals(username.toString())) {
				clients.remove(i);
				clients.trimToSize();
				totalSent++;
				try {
					if (!(reason == null)) {
						client.socket.getOutputStream().write(ChatColor.stripColor("You have been forcibly kicked from the server." + '\n' + reason).getBytes());
						client.socket.getOutputStream().flush();
						Thread.sleep(200);
					}
					client.socket.close();
					new ClientDisconnectEvent(client);
				} catch (IOException e) {
				} catch (InterruptedException e) {
				}
			}
		}
		return totalSent;
	}

	public int kickPlayer(Client client, String reason) {
		int response = kickPlayer(client.getUsername(), reason);
		if (response == 0) {
			try {
				client.socket.close();
				response++;
			} catch (Exception ex) {
			}
		}
		return response;
	}

	public void handle(Client client, byte[] data) {
		Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				String message = new String(data);
				OfflinePlayer player = CraftGo.Player.getOfflinePlayer(client.getUsername());
				if (player.isOnline() || player.hasPlayedBefore()) {
					if (message.charAt(0) == '/') {
						try {
							client.socket.getOutputStream().write(("Commands are not supported through notification worker." + '\n').getBytes());
							client.socket.getOutputStream().flush();
						} catch (Exception ex) {
						}
						ListCmd.listOnlinePlayers(new AlternateClient(client));
					} else {
						String displayName = $.getFlashPlayerDisplayName(player);
						String msg = ChatColor.GRAY + "[" + ChatColor.WHITE + Server.getLastKnownHubWorld() + ChatColor.GRAY + "] " + ChatColor.RESET + "<" + displayName + ChatColor.RESET + "> " + message;
						msg = Server.getAntiCheat().processAntiSwear(player, msg);
						broadcast(msg);
						Bukkit.broadcastMessage(msg);
						for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
							if (!onlinePlayer.getName().equals(player.getName())) {
								int rankID = $.getRankId(onlinePlayer);
								if (onlinePlayer.isOp() || rankID > -1) {
									onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
								} else if (msg.contains(onlinePlayer.getName())) {
									onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
								}
							}
						}
					}
				} else {
					kickPlayer(client, "The account you were logged into no longer exists.");
				}
			}
		});
	}

	public void playAccountNotRegistered(Socket socket) throws IOException {
		socket.getOutputStream().write(("The specified account username is not registered." + '\n' + "The username is case-sensitive if you must know.").getBytes());
		socket.getOutputStream().flush();
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = server.accept();
				NotificationWorker_Auth auth = new NotificationWorker_Auth(socket);
				Thread thread = new Thread(auth);
				thread.setName("Notification Worker, Authentication Thread");
				thread.start();
			}
		} catch (Exception ex) {
		}
	}
}
