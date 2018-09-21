package me.skorrloregaming.scoreboard;

public class ScoreboardTask {
	private Class<?> scoreboard;
	private int taskId;
	private DisplayType displayType;

	public ScoreboardTask(Class<?> scoreboard, int taskId, DisplayType displayType) {
		this.setScoreboard(scoreboard);
		this.setTaskId(taskId);
		this.setDisplayType(displayType);
	}

	public Class<?> getScoreboard() {
		return scoreboard;
	}

	public void setScoreboard(Class<?> arg0) {
		this.scoreboard = arg0;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public DisplayType getDisplayType() {
		return displayType;
	}

	public void setDisplayType(DisplayType arg1) {
		this.displayType = arg1;
	}
}
