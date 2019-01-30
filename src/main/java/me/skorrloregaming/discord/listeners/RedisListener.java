package me.skorrloregaming.discord.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.skorrloregaming.Server;
import me.skorrloregaming.discord.Channel;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Optional;
import java.util.UUID;

public class RedisListener extends JedisPubSub implements Listener {

	private Optional<JedisPool> jedisPool = Optional.empty();

	private final Gson gson = new Gson();

	/**
	 * Just a random UUID as a placeholder for what could be a configurable
	 * server ID which would allow other servers to identify where a message
	 * came from. Currently this is just used to make sure messages are not
	 * duped.
	 */
	private final UUID serverID = UUID.randomUUID();

	private boolean connectToRedis() {
		Server.getPlugin().getLogger().info("Connecting to redis..");
		String hostname = Server.getPlugin().getConfig().getString("settings.redis.hostname", "localhost");
		int port = Server.getPlugin().getConfig().getInt("settings.redis.port", 6379);
		String password = Server.getPlugin().getConfig().getString("settings.redis.password");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxWaitMillis(10 * 1000);
		if (password == null || password.equals("")) {
			jedisPool = Optional.ofNullable(new JedisPool(poolConfig, hostname, port, 0));
		} else {
			jedisPool = Optional.ofNullable(new JedisPool(poolConfig, hostname, port, 0, password));
		}
		return jedisPool.isPresent();
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
		getPool().ifPresent((pool) -> {
			try (Jedis jedis = pool.getResource()) {
				jedis.subscribe(this, "slgn:discord");
			} catch (Exception ex) {
			}
		});
	}

	public void unregister() {
		close();
	}

	@Override
	public void onMessage(String channel, String request) {
		if (channel.equalsIgnoreCase("slgn:chat")) {
			JsonObject obj = gson.fromJson(request, JsonObject.class);
			if (obj != null) {
				String servername = obj.get("serverName").getAsString();
				if (!servername.equalsIgnoreCase(serverID.toString())) {
					String message = obj.get("message").getAsString();
					String discordChannel = obj.get("discordChannel").getAsString();
					Server.getDiscordBot().broadcast(message, Channel.valueOf(discordChannel));
				}
			}
		}
	}

}
