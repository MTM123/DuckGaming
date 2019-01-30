package me.skorrloregaming.lockette;

import me.skorrloregaming.Server;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

public class LocketteDoorCloser implements Runnable {
	private static int doorTask = -1;
	private final PriorityQueue<closeTask> closeTaskList = new PriorityQueue<closeTask>();

	public LocketteDoorCloser(Lockette instance) {
	}

	protected boolean start() {
		if (doorTask != -1)
			return (false);
		doorTask = Server.getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(Server.getPlugin(), this, 100, 10);
		if (doorTask == -1)
			return (true);
		return (false);
	}

	protected boolean stop() {
		if (doorTask == -1)
			return (false);
		Server.getPlugin().getServer().getScheduler().cancelTask(doorTask);
		doorTask = -1;
		cleanup();
		return (false);
	}

	protected void cleanup() {
		closeTask door;
		while (!closeTaskList.isEmpty()) {
			door = closeTaskList.poll();
			if (door == null)
				break;
			close(door);
		}
	}

	@Override
	public void run() {
		if (closeTaskList.isEmpty())
			return;
		Date time = new Date();
		closeTask door;
		while (time.after(closeTaskList.peek().time)) {
			door = closeTaskList.poll();
			if (door == null)
				break;
			close(door);
			if (closeTaskList.isEmpty())
				break;
		}
	}

	private void close(closeTask door) {
		Lockette.toggleHalfDoor(door.world.getBlockAt(door.x, door.y, door.z), door.effect);
	}

	public void add(List<Block> list, boolean auto, int delta) {
		if (list == null)
			return;
		if (list.isEmpty())
			return;
		Iterator<closeTask> it;
		Iterator<Block> itb;
		closeTask task;
		Block block;
		World world = list.get(0).getWorld();
		it = closeTaskList.iterator();
		while (it.hasNext()) {
			task = it.next();
			if (!task.world.equals(world))
				continue;
			itb = list.iterator();
			while (itb.hasNext()) {
				block = itb.next();
				if ((block.getX() == task.x) && (block.getY() == task.y) && (block.getZ() == task.z)) {
					it.remove();
					itb.remove();
					break;
				}
			}
		}
		if (!auto)
			return;
		if (list.isEmpty())
			return;
		Date time = new Date();
		time.setTime(time.getTime() + (delta * 1000L));
		for (int x = 0; x < list.size(); ++x) {
			closeTaskList.add(new closeTask(time, list.get(x), x == 0));
		}
	}

	protected class closeTask implements Comparable<closeTask> {
		Date time;
		World world;
		int x, y, z;
		boolean effect;

		public closeTask(Date taskTime, Block block, boolean taskEffect) {
			time = taskTime;
			world = block.getWorld();
			x = block.getX();
			y = block.getY();
			z = block.getZ();
			effect = taskEffect;
		}

		@Override
		public int compareTo(closeTask arg) {
			return (time.compareTo(arg.time));
		}
	}
}
