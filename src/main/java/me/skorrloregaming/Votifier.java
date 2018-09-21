package me.skorrloregaming;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.JSONObject;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import me.skorrloregaming.VotifierSession.VoteHandler;
import me.skorrloregaming.VotifierSession.VotifierPlugin;
import me.skorrloregaming.impl.Vote;

public class Votifier implements VoteHandler, VotifierPlugin {
	private static Votifier instance;
	private String version;
	private Channel serverChannel;
	private NioEventLoopGroup serverGroup;
	private KeyPair keyPair;
	private boolean debug;
	private Map<String, Key> tokens = new HashMap<>();

	public void onEnable() {
		Votifier.instance = this;
		version = Server.getPlugin().getDescription().getVersion();
		if (!Server.getPlugin().getDataFolder().exists()) {
			Server.getPlugin().getDataFolder().mkdir();
		}
		File config = new File(Server.getPlugin().getDataFolder() + "/vote_config.yml");
		YamlConfiguration cfg;
		File rsaDirectory = new File(Server.getPlugin().getDataFolder() + "/rsa");
		String hostAddr = Bukkit.getServer().getIp();
		if (hostAddr == null || hostAddr.length() == 0)
			hostAddr = "0.0.0.0";
		if (!config.exists()) {
			try {
				Server.getPlugin().getLogger().info("Configuring Votifier for the first time...");
				config.createNewFile();
				String cfgStr = new String(ByteStreams.toByteArray(Server.getPlugin().getResource("bukkitConfig.yml")), StandardCharsets.UTF_8);
				String token = VotifierSession.newToken();
				cfgStr = cfgStr.replace("%default_token%", token).replace("%ip%", hostAddr);
				Files.write(cfgStr, config, StandardCharsets.UTF_8);
			} catch (Exception ex) {
				Server.getPlugin().getLogger().log(Level.SEVERE, "Error creating configuration file", ex);
				gracefulExit();
				return;
			}
		}
		cfg = YamlConfiguration.loadConfiguration(config);
		try {
			if (!rsaDirectory.exists()) {
				rsaDirectory.mkdir();
				keyPair = RSAKeygen.generate(2048);
				RSAKeygen.save(rsaDirectory, keyPair);
			} else {
				keyPair = RSAKeygen.load(rsaDirectory);
			}
		} catch (Exception ex) {
			Server.getPlugin().getLogger().log(Level.SEVERE, "Error reading configuration file or RSA tokens", ex);
			gracefulExit();
			return;
		}
		debug = cfg.getBoolean("debug", false);
		ConfigurationSection tokenSection = cfg.getConfigurationSection("tokens");
		if (tokenSection != null) {
			Map<String, Object> websites = tokenSection.getValues(false);
			for (Map.Entry<String, Object> website : websites.entrySet()) {
				tokens.put(website.getKey(), createKeyFrom(website.getValue().toString()));
				Server.getPlugin().getLogger().info("Loaded token for website: " + website.getKey());
			}
		} else {
			String token = VotifierSession.newToken();
			tokenSection = cfg.createSection("tokens");
			tokenSection.set("default", token);
			tokens.put("default", createKeyFrom(token));
			try {
				cfg.save(config);
			} catch (IOException e) {
				Server.getPlugin().getLogger().log(Level.SEVERE, "Error generating Votifier token", e);
				gracefulExit();
				return;
			}
		}
		final String host = cfg.getString("host", hostAddr);
		final int port = cfg.getInt("port", 8192);
		if (debug)
			Server.getPlugin().getLogger().info("Debug mode enabled has been enabled in Votifier.");
		final boolean disablev1 = cfg.getBoolean("disable-v1-protocol");
		if (disablev1)
			Server.getPlugin().getLogger().info("Votifier protocol v1 parsing has been disabled.");
		serverGroup = new NioEventLoopGroup(1);
		new ServerBootstrap().channel(NioServerSocketChannel.class).group(serverGroup).childHandler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel channel) throws Exception {
				channel.attr(VotifierSession.KEY).set(new VotifierSession());
				channel.attr(VotifierPlugin.KEY).set(Votifier.this);
				channel.pipeline().addLast("greetingHandler", new VotifierSession.VotifierGreetingHandler());
				channel.pipeline().addLast("protocolDifferentiator", new VotifierProtocolDifferentiator(false, !disablev1));
				channel.pipeline().addLast("voteHandler", new VoteInboundHandler(Votifier.this));
			}
		}).bind(host, port).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if (future.isSuccess()) {
					serverChannel = future.channel();
					Server.getPlugin().getLogger().info("Votifier enabled on socket " + serverChannel.localAddress() + ".");
				} else {
					SocketAddress socketAddress = future.channel().localAddress();
					if (socketAddress == null) {
						socketAddress = new InetSocketAddress(host, port);
					}
					Server.getPlugin().getLogger().log(Level.SEVERE, "Votifier was not able to bind to " + socketAddress, future.cause());
				}
			}
		});
	}

	public void onDisable() {
		if (serverGroup != null) {
			if (serverChannel != null)
				serverChannel.close();
			serverGroup.shutdownGracefully();
		}
		Server.getPlugin().getLogger().info("Votifier disabled.");
	}

	private void gracefulExit() {
		Server.getPlugin().getLogger().log(Level.SEVERE, "Votifier did not initialize properly!");
	}

	public static Votifier getInstance() {
		return instance;
	}

	public String getVersion() {
		return version;
	}

	public boolean isDebug() {
		return debug;
	}

	@Override
	public Map<String, Key> getTokens() {
		return tokens;
	}

	@Override
	public KeyPair getProtocolV1Key() {
		return keyPair;
	}

	@Override
	public void onVoteReceived(Channel channel, final Vote vote, VotifierSession.ProtocolVersion protocolVersion) throws Exception {
		if (debug) {
			if (protocolVersion == VotifierSession.ProtocolVersion.ONE) {
				Server.getPlugin().getLogger().info("Got a protocol v1 vote record from " + channel.remoteAddress() + " -> " + vote);
			} else {
				Server.getPlugin().getLogger().info("Got a protocol v2 vote record from " + channel.remoteAddress() + " -> " + vote);
			}
		}
		Bukkit.getScheduler().runTask(Server.getPlugin(), new Runnable() {
			@Override
			public void run() {
				Server.getVoteListener().onVote(vote, protocolVersion, true);
			}
		});
	}

	@Override
	public void onError(Channel channel, Throwable throwable) {
		if (debug) {
			Server.getPlugin().getLogger().log(Level.SEVERE, "Unable to process vote from " + channel.remoteAddress(), throwable);
		} else {
			Server.getPlugin().getLogger().log(Level.SEVERE, "Unable to process vote from " + channel.remoteAddress());
		}
	}

	public static Key createKeyFrom(String token) {
		return new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}

	public static class RSAKeygen {
		public static byte[] encrypt(byte[] data, PublicKey key) throws Exception {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		}

		public static byte[] decrypt(byte[] data, PrivateKey key) throws Exception {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(data);
		}

		public static void save(File directory, KeyPair keyPair) throws Exception {
			PrivateKey privateKey = keyPair.getPrivate();
			PublicKey publicKey = keyPair.getPublic();
			X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicKey.getEncoded());
			PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			try (FileOutputStream publicOut = new FileOutputStream(directory + "/public.key"); FileOutputStream privateOut = new FileOutputStream(directory + "/private.key")) {
				publicOut.write(Base64.getEncoder().encode(publicSpec.getEncoded()));
				privateOut.write(Base64.getEncoder().encode(privateSpec.getEncoded()));
			}
		}

		public static KeyPair load(File directory) throws Exception {
			File publicKeyFile = new File(directory + "/public.key");
			byte[] encodedPublicKey = java.nio.file.Files.readAllBytes(publicKeyFile.toPath());
			encodedPublicKey = Base64.getDecoder().decode(new String(encodedPublicKey, StandardCharsets.UTF_8));
			File privateKeyFile = new File(directory + "/private.key");
			byte[] encodedPrivateKey = java.nio.file.Files.readAllBytes(privateKeyFile.toPath());
			encodedPrivateKey = Base64.getDecoder().decode(new String(encodedPrivateKey, StandardCharsets.UTF_8));
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			return new KeyPair(publicKey, privateKey);
		}

		public static KeyPair generate(int bits) throws Exception {
			KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
			RSAKeyGenParameterSpec spec = new RSAKeyGenParameterSpec(bits, RSAKeyGenParameterSpec.F4);
			keygen.initialize(spec);
			return keygen.generateKeyPair();
		}
	}

	public static class VotifierProtocol2Decoder extends MessageToMessageDecoder<String> {
		private static final SecureRandom RANDOM = new SecureRandom();

		@Override
		protected void decode(ChannelHandlerContext ctx, String s, List<Object> list) throws Exception {
			JSONObject voteMessage = new JSONObject(s);
			VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();
			JSONObject votePayload = new JSONObject(voteMessage.getString("payload"));
			if (!votePayload.getString("challenge").equals(session.getChallenge())) {
				throw new CorruptedFrameException("Challenge is not valid");
			}
			VotifierPlugin plugin = ctx.channel().attr(VotifierPlugin.KEY).get();
			Key key = plugin.getTokens().get(votePayload.getString("serviceName"));
			if (key == null) {
				key = plugin.getTokens().get("default");
				if (key == null) {
					throw new RuntimeException("Unknown service '" + votePayload.getString("serviceName") + "'");
				}
			}
			String sigHash = voteMessage.getString("signature");
			byte[] sigBytes = Base64.getDecoder().decode(sigHash);
			if (!hmacEqual(sigBytes, voteMessage.getString("payload").getBytes(StandardCharsets.UTF_8), key)) {
				throw new CorruptedFrameException("Signature is not valid (invalid token?)");
			}
			if (votePayload.has("uuid")) {
				UUID.fromString(votePayload.getString("uuid"));
			}
			if (votePayload.getString("username").length() > 16) {
				throw new CorruptedFrameException("Username too long");
			}
			Vote vote = new Vote(votePayload);
			list.add(vote);
			ctx.pipeline().remove(this);
		}

		private boolean hmacEqual(byte[] sig, byte[] message, Key key) throws NoSuchAlgorithmException, InvalidKeyException {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(key);
			byte[] calculatedSig = mac.doFinal(message);
			byte[] randomKey = new byte[32];
			RANDOM.nextBytes(randomKey);
			Mac mac2 = Mac.getInstance("HmacSHA256");
			mac2.init(new SecretKeySpec(randomKey, "HmacSHA256"));
			byte[] clientSig = mac2.doFinal(sig);
			mac2.reset();
			byte[] realSig = mac2.doFinal(calculatedSig);
			return MessageDigest.isEqual(clientSig, realSig);
		}
	}

	public static class VotifierProtocol1Decoder extends ByteToMessageDecoder {
		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
			if (buf.readableBytes() < 256) {
				return;
			}
			byte[] block = new byte[buf.readableBytes()];
			buf.getBytes(0, block);
			buf.readerIndex(buf.readableBytes());
			VotifierPlugin plugin = ctx.channel().attr(VotifierPlugin.KEY).get();
			try {
				block = RSAKeygen.decrypt(block, plugin.getProtocolV1Key().getPrivate());
			} catch (Exception e) {
				throw new CorruptedFrameException("Could not decrypt data (is your key correct?)", e);
			}
			String all = new String(block, StandardCharsets.US_ASCII);
			String[] split = all.split("\n");
			if (split.length < 5) {
				throw new CorruptedFrameException("Not enough fields specified in vote.");
			}
			if (!split[0].equals("VOTE")) {
				throw new CorruptedFrameException("VOTE opcode not found");
			}
			Vote vote = new Vote(split[1], split[2], split[3], split[4]);
			list.add(vote);
			ctx.pipeline().remove(this);
		}
	}

	public static class VotifierProtocolDifferentiator extends ByteToMessageDecoder {
		private static final short PROTOCOL_2_MAGIC = 0x733A;
		private final boolean testMode;
		private final boolean allowv1;

		public VotifierProtocolDifferentiator(boolean testMode, boolean allowv1) {
			this.testMode = testMode;
			this.allowv1 = allowv1;
		}

		@Override
		protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
			int readable = buf.readableBytes();
			if (readable < 2) {
				return;
			}
			short readMagic = buf.readShort();
			buf.readerIndex(0);
			VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();
			if (readMagic == PROTOCOL_2_MAGIC) {
				session.setVersion(VotifierSession.ProtocolVersion.TWO);
				if (!testMode) {
					ctx.pipeline().addAfter("protocolDifferentiator", "protocol2LengthDecoder", new LengthFieldBasedFrameDecoder(1024, 2, 2, 0, 4));
					ctx.pipeline().addAfter("protocol2LengthDecoder", "protocol2StringDecoder", new StringDecoder(StandardCharsets.UTF_8));
					ctx.pipeline().addAfter("protocol2StringDecoder", "protocol2VoteDecoder", new VotifierProtocol2Decoder());
					ctx.pipeline().addAfter("protocol2VoteDecoder", "protocol2StringEncoder", new StringEncoder(StandardCharsets.UTF_8));
					ctx.pipeline().remove(this);
				}
			} else {
				if (!allowv1) {
					throw new CorruptedFrameException("This server only accepts well-formed Votifier v2 packets.");
				}
				session.setVersion(VotifierSession.ProtocolVersion.ONE);
				if (!testMode) {
					ctx.pipeline().addAfter("protocolDifferentiator", "protocol1Handler", new VotifierProtocol1Decoder());
					ctx.pipeline().remove(this);
				}
			}
		}
	}

	public class VoteInboundHandler extends SimpleChannelInboundHandler<Vote> {
		private final VoteHandler handler;

		public VoteInboundHandler(VoteHandler handler) {
			this.handler = handler;
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, final Vote vote) throws Exception {
			VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();
			handler.onVoteReceived(ctx.channel(), vote, session.getVersion());
			if (session.getVersion() == VotifierSession.ProtocolVersion.ONE) {
				ctx.close();
			} else {
				JSONObject object = new JSONObject();
				object.put("status", "ok");
				ctx.writeAndFlush(object.toString() + "\r\n").addListener(ChannelFutureListener.CLOSE);
			}
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();
			handler.onError(ctx.channel(), cause);
			if (session.getVersion() == VotifierSession.ProtocolVersion.TWO) {
				JSONObject object = new JSONObject();
				object.put("status", "error");
				object.put("cause", cause.getClass().getSimpleName());
				object.put("error", cause.getMessage());
				ctx.writeAndFlush(object.toString() + "\r\n").addListener(ChannelFutureListener.CLOSE);
			} else {
				ctx.close();
			}
		}
	}
}