package me.skorrloregaming;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import me.skorrloregaming.impl.ServerMinigame;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class VaultEconomy implements Economy {
	private long[] timeSinceLastWarning = new long[3];
	
	private boolean isEnabled = false;

	public VaultEconomy() {
		Server.getPlugin().getServer().getPluginManager().registerEvents(new EconomyServerListener(), Server.getPlugin());
		Server.getPlugin().getLogger().info("Vault support enabled.");
	}

	public void setupVault() {
		Plugin vault = Server.getPlugin().getServer().getPluginManager().getPlugin("Vault");
		if (vault == null) {
			return;
		}
		Server.getPlugin().getServer().getServicesManager().register(Economy.class, this, Server.getPlugin(), ServicePriority.Highest);
	}

	@Override
	public boolean isEnabled() {
		return Server.getPlugin() != null;
	}

	@Override
	public String getName() {
		return "SkorrloreGaming";
	}

	@Override
	public String format(double amount) {
		DecimalFormat formatter = new DecimalFormat("###,###,###,###,###");
		return formatter.format(amount);
	}

	@Override
	public String currencyNameSingular() {
		return "SkorrloreGaming";
	}

	@Override
	public String currencyNamePlural() {
		return "SkorrloreGaming";
	}

	@Override
	public double getBalance(String playerName) {
		return getAccountBalance(playerName, null);
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer) {
		return getAccountBalance(offlinePlayer.getName(), offlinePlayer.getUniqueId().toString());
	}

	// TODO: Implement a better way of handling this
	private double getAccountBalance(String playerName, String uuid) {
		if (System.currentTimeMillis() - this.timeSinceLastWarning[0] > 150) {
			Bukkit.getServer().getLogger().warning("Economy does not support cross-world getBalance(String arg0, String arg1) calls.");
			this.timeSinceLastWarning[0] = System.currentTimeMillis();
		}
		return 0.0;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, double amount) {
		return withdraw(playerName, null, amount);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double amount) {
		return withdraw(offlinePlayer.getName(), offlinePlayer.getUniqueId().toString(), amount);
	}

	private EconomyResponse withdraw(String playerName, String uuid, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support cross-world functions!.");
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, double amount) {
		return deposit(playerName, null, amount);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double amount) {
		return deposit(offlinePlayer.getName(), offlinePlayer.getUniqueId().toString(), amount);
	}

	private EconomyResponse deposit(String playerName, String uuid, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support cross-world functions!.");
	}

	// TODO: Implement a better way of handling this
	@Override
	public boolean has(String playerName, double amount) {
		if (System.currentTimeMillis() - this.timeSinceLastWarning[1] > 150) {
			Bukkit.getServer().getLogger().warning("Economy does not support cross-world has(String arg0, double arg1) calls.");
			this.timeSinceLastWarning[1] = System.currentTimeMillis();
		}
		return false;
	}

	// TODO: Implement a better way of handling this
	@Override
	public boolean has(OfflinePlayer offlinePlayer, double amount) {
		if (System.currentTimeMillis() - this.timeSinceLastWarning[2] > 150) {
			Bukkit.getServer().getLogger().warning("Economy does not support cross-world has(OfflinePlayer arg0, double amount) calls.");
			this.timeSinceLastWarning[2] = System.currentTimeMillis();
		}
		return false;
	}

	@Override
	public EconomyResponse createBank(String name, String player) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse createBank(String name, OfflinePlayer offlinePlayer) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse deleteBank(String name) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse bankHas(String name, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse bankWithdraw(String name, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse bankDeposit(String name, double amount) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse isBankOwner(String name, String playerName) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse isBankOwner(String name, OfflinePlayer offlinePlayer) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse isBankMember(String name, String playerName) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse isBankMember(String name, OfflinePlayer offlinePlayer) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public EconomyResponse bankBalance(String name) {
		return new EconomyResponse(0.0D, 0.0D, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Economy does not support bank accounts.");
	}

	@Override
	public List<String> getBanks() {
		return new ArrayList<String>();
	}

	@Override
	public boolean hasBankSupport() {
		return false;
	}

	@Override
	public boolean hasAccount(String playerName) {
		OfflinePlayer player = CraftGo.Player.getOfflinePlayer(playerName);
		if (player.hasPlayedBefore() || player.isOnline())
			return true;
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer) {
		if (offlinePlayer.hasPlayedBefore() || offlinePlayer.isOnline())
			return true;
		return false;
	}

	@Override
	public boolean createPlayerAccount(String playerName) {
		return createAccount(playerName, null);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
		return createAccount(offlinePlayer.getName(), offlinePlayer.getUniqueId().toString());
	}

	private boolean createAccount(String playerName, String uuid) {
		return false;
	}

	@Override
	public int fractionalDigits() {
		return -1;
	}

	@Override
	public boolean hasAccount(String playerName, String worldName) {
		return hasAccount(playerName);
	}

	@Override
	public boolean hasAccount(OfflinePlayer offlinePlayer, String worldName) {
		return hasAccount(offlinePlayer);
	}

	@Override
	public double getBalance(String playerName, String worldName) {
		UUID uid = UUID.randomUUID();
		try {
			uid = UUID.fromString(playerName);
		} catch (IllegalArgumentException ex) {
			String uidString = CraftGo.Player.getUUID(playerName, false);
			if (uidString == null)
				return 0.0;
			uid = UUID.fromString(uidString);
		}
		ServerMinigame minigame = $.getMinigameFromWorld(Bukkit.getWorld(worldName));
		double cash = EconManager.retrieveCash(uid, minigame.toString().toLowerCase());
		return cash;
	}

	@Override
	public double getBalance(OfflinePlayer offlinePlayer, String worldName) {
		ServerMinigame minigame = $.getMinigameFromWorld(Bukkit.getWorld(worldName));
		double cash = EconManager.retrieveCash(offlinePlayer.getUniqueId(), minigame.toString().toLowerCase());
		return cash;
	}

	@Override
	public boolean has(String playerName, String worldName, double amount) {
		if (getBalance(playerName, worldName) >= amount)
			return true;
		return false;
	}

	@Override
	public boolean has(OfflinePlayer offlinePlayer, String worldName, double amount) {
		if (getBalance(offlinePlayer, worldName) >= amount)
			return true;
		return false;
	}

	@Override
	public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
		UUID uid = UUID.randomUUID();
		try {
			uid = UUID.fromString(playerName);
		} catch (IllegalArgumentException ex) {
			String uidString = CraftGo.Player.getUUID(playerName, false);
			if (uidString == null)
				return new EconomyResponse(0.0D, 0.0, EconomyResponse.ResponseType.FAILURE, "UUID could not be determined");
			uid = UUID.fromString(uidString);
		}
		ServerMinigame minigame = $.getMinigameFromWorld(Bukkit.getWorld(worldName));
		double currentBalance = getBalance(playerName, worldName);
		if (has(playerName, worldName, amount)) {
			EconManager.withdrawCash(uid, (int) amount, minigame.toString().toLowerCase());
			return new EconomyResponse(amount, getBalance(playerName, worldName), EconomyResponse.ResponseType.SUCCESS, "");
		}
		return new EconomyResponse(0.0D, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
		ServerMinigame minigame = $.getMinigameFromWorld(Bukkit.getWorld(worldName));
		double currentBalance = getBalance(offlinePlayer, worldName);
		if (has(offlinePlayer, worldName, amount)) {
			EconManager.withdrawCash(offlinePlayer.getUniqueId(), (int) amount, minigame.toString().toLowerCase());
			return new EconomyResponse(amount, getBalance(offlinePlayer, worldName), EconomyResponse.ResponseType.SUCCESS, "");
		}
		return new EconomyResponse(0.0D, currentBalance, EconomyResponse.ResponseType.FAILURE, "Insufficient funds");
	}

	@Override
	public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
		UUID uid = UUID.randomUUID();
		try {
			uid = UUID.fromString(playerName);
		} catch (IllegalArgumentException ex) {
			String uidString = CraftGo.Player.getUUID(playerName, false);
			if (uidString == null)
				return new EconomyResponse(0.0D, 0.0, EconomyResponse.ResponseType.FAILURE, "UUID could not be determined");
			uid = UUID.fromString(uidString);
		}
		ServerMinigame minigame = $.getMinigameFromWorld(Bukkit.getWorld(worldName));
		EconManager.depositCash(uid, (int) amount, minigame.toString().toLowerCase());
		return new EconomyResponse(amount, getBalance(playerName, worldName), EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String worldName, double amount) {
		ServerMinigame minigame = $.getMinigameFromWorld(Bukkit.getWorld(worldName));
		EconManager.depositCash(offlinePlayer.getUniqueId(), (int) amount, minigame.toString().toLowerCase());
		return new EconomyResponse(amount, getBalance(offlinePlayer, worldName), EconomyResponse.ResponseType.SUCCESS, "");
	}

	@Override
	public boolean createPlayerAccount(String playerName, String worldName) {
		return createPlayerAccount(playerName);
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String worldName) {
		return createPlayerAccount(offlinePlayer);
	}

	public class EconomyServerListener implements Listener {
		public EconomyServerListener() {
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginEnable(PluginEnableEvent event) {
			if (!VaultEconomy.this.isEnabled) {
				Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("SkorrloreGaming");
				if ((plugin != null) && (plugin.isEnabled())) {
					VaultEconomy.this.isEnabled = true;
					Server.getPlugin().getLogger().info("Vault support enabled.");
				}
			}
		}

		@EventHandler(priority = EventPriority.MONITOR)
		public void onPluginDisable(PluginDisableEvent event) {
			if ((VaultEconomy.this.isEnabled) && (event.getPlugin().getDescription().getName().equals("SkorrloreGaming"))) {
				VaultEconomy.this.isEnabled = false;
				Bukkit.getLogger().info("[SkorrloreGaming] Vault support disabled.");
			}
		}
	}
}
