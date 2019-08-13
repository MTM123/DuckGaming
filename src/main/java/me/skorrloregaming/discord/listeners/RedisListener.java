package me.skorrloregaming.discord.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.skorrloregaming.Server;
import me.skorrloregaming.discord.Channel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Optional;

public class RedisListener extends JedisPubSub implements Listener {

	private Optional<JedisPool> jedisPool = Optional.empty();

	private final Gson gson = new Gson();

	private RedisListener instance;

	private boolean connectToRedis() {
		instance = this;
		Server.getInstance().getPlugin().getLogger().info("Connecting to Redis..");
		String hostname = Server.getInstance().getPlugin().getConfig().getString("settings.redis.hostname", "localhost");
		int port = Server.getInstance().getPlugin().getConfig().getInt("settings.redis.port", 6379);
		String password = Server.getInstance().getPlugin().getConfig().getString("settings.redis.password");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxWaitMillis(10 * 1000);
		if (password == null || password.equals("")) {
			jedisPool = Optional.ofNullable(new JedisPool(poolConfig, hostname, port, 0));
		} else {
			jedisPool = Optional.ofNullable(new JedisPool(poolConfig, hostname, port, 0, password));
		}
		return jedisPool.isPresent();
	}

	private RedisListener getInstance() {
		return instance;
	}

	private boolean close() {
		if (jedisPool.isPresent()) {
			jedisPool.get().destroy();
			return true;
		}
		return false;
	}

	public Optional<JedisPool> getPool() {
		if (!jedisPool.isPresent() || jedisPool.get().isClosed()) {
			connectToRedis();
		}
		return jedisPool;
	}

	public void register() {
		connectToRedis();
		Server.getInstance().getPlugin().getLogger().info("Connected to Redis!");
		Bukkit.getScheduler().runTaskAsynchronously(Server.getInstance().getPlugin(), new Runnable() {

			@Override
			public void run() {
				getPool().ifPresent((pool) -> {
					try (Jedis jedis = pool.getResource()) {
						jedis.subscribe(getInstance(), "slgn:discord");
					} catch (Exception ex) {
					}
				});
			}
		});
	}

	public void unregister() {
		close();
	}

	@Override
	public void onMessage(String channel, String request) {
		if (channel.equalsIgnoreCase("slgn:discord")) {
			JsonObject obj = gson.fromJson(request, JsonObject.class);
			if (obj != null) {
				String message = ChatColor.stripColor(obj.get("message").getAsString());
				String discordChannel = obj.get("discordChannel").getAsString();
				Server.getInstance().getDiscordBot().broadcast(message, Channel.valueOf(discordChannel));
			}
		}
	}

}
