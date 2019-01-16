package me.skorrloregaming.scoreboard;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.skorrloregaming.Server;

public interface DisposableScoreboard {
	public ConcurrentMap<Player, ScoreboardTask> taskIdentifiers = new ConcurrentHashMap<>();
	public ConcurrentMap<Integer, Boolean> useSecondaryScoreboard = new ConcurrentHashMap<>();

	public abstract void refreshScoreboard(Player player, boolean clearValues);

	default int unregister(Player player) {
		if (taskIdentifiers.containsKey(player)) {
			int taskId = taskIdentifiers.get(player).getTaskId();
			Bukkit.getScheduler().cancelTask(taskId);
			useSecondaryScoreboard.remove(taskId);
			taskIdentifiers.remove(player);
			return 1;
		}
		return 0;
	}

	default ScoreboardType getCurrentScoreboardType(Player player, boolean silent) {
		if (taskIdentifiers.containsKey(player)) {
			ScoreboardTask task = taskIdentifiers.get(player);
			boolean value = useSecondaryScoreboard.get(task.getTaskId()).booleanValue();
			if (value) {
				return ScoreboardType.Primary;
			} else {
				return ScoreboardType.Secondary;
			}
		}
		return null;
	}

	default ScoreboardType getCurrentScoreboardType(Player player) {
		return getCurrentScoreboardType(player, false);
	}

	default boolean schedule(Player player) {
		return schedule(player, false);
	}
	
	default boolean schedule(Player player, boolean clearValues) {
		if (!Server.isRunning())
			return false;
		ScoreboardType type = getCurrentScoreboardType(player);
		if (!(type == null) && !(type == ScoreboardType.Primary))
			return false;
		refreshScoreboard(player, clearValues);
		return true;
	}

	default boolean schedule(Player player, DisplayType displayType, Class<?> primaryScoreboard, Class<?> secondaryScoreboard) {
		if (!Server.isRunning())
			return false;
		ScoreboardType type = getCurrentScoreboardType(player, true);
		if (displayType == DisplayType.Secondary) {
			if (!(type == null) && type == ScoreboardType.Secondary) {
				try {
					DisposableScoreboard secondary = ((DisposableScoreboard) secondaryScoreboard.newInstance());
					secondary.refreshScoreboard(player, false);
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			return false;
		}
		if (displayType == DisplayType.Primary) {
			refreshScoreboard(player, false);
			return true;
		}
		if (taskIdentifiers.containsKey(player)) {
			if (displayType == DisplayType.Primary) {
				boolean isSecondary = useSecondaryScoreboard.get(taskIdentifiers.get(player).getTaskId()).booleanValue();
				if (isSecondary) {
					refreshScoreboard(player, false);
					return true;
				}
			}
		}
		long delay = 0L;
		if (displayType == DisplayType.Five_Second_Period)
			delay = 100L;
		if (displayType == DisplayType.Ten_Second_Period)
			delay = 200L;
		if (taskIdentifiers.containsKey(player)) {
			ScoreboardTask scoreboardTask = taskIdentifiers.get(player);
			Bukkit.getScheduler().cancelTask(scoreboardTask.getTaskId());
			taskIdentifiers.remove(player);
			Bukkit.getServer().getLogger().warning("Task identifier for " + player.getName() + " was not unregistered before schedule() call.");
		}
		int taskId = Bukkit.getScheduler().runTaskTimer(Server.getPlugin(), new Runnable() {
			private int taskId = 0;

			@Override
			public void run() {
				if (taskIdentifiers.containsKey(player)) {
					taskId = taskIdentifiers.get(player).getTaskId();
					boolean useSecondaryScoreboardValue = useSecondaryScoreboard.get(taskId);
					if (useSecondaryScoreboardValue) {
						try {
							((DisposableScoreboard) secondaryScoreboard.newInstance()).refreshScoreboard(player, true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						useSecondaryScoreboard.put(taskId, false);
					} else {
						refreshScoreboard(player, true);
						useSecondaryScoreboard.put(taskId, true);
					}
				} else {
					Bukkit.getScheduler().cancelTask(taskId);
					Bukkit.getServer().getLogger().warning(this.getClass().getName() + " was not properly cancelled.");
				}
			}
		}, delay, delay).getTaskId();
		taskIdentifiers.put(player, new ScoreboardTask(primaryScoreboard, taskId, displayType));
		useSecondaryScoreboard.put(taskId, true);
		refreshScoreboard(player, false);
		return true;
	}
}
