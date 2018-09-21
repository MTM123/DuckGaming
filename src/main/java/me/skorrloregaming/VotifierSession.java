package me.skorrloregaming;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import me.skorrloregaming.impl.Vote;

public class VotifierSession {
	public static final AttributeKey<VotifierSession> KEY = AttributeKey.valueOf("votifier_session");
	private ProtocolVersion version = ProtocolVersion.UNKNOWN;
	private final String challenge;
	private static final SecureRandom RANDOM = new SecureRandom();

	public VotifierSession() {
		challenge = newToken();
	}

	public static String newToken() {
		return new BigInteger(130, RANDOM).toString(32);
	}

	public void setVersion(ProtocolVersion version) {
		if (this.version != ProtocolVersion.UNKNOWN)
			throw new IllegalStateException("Protocol version already switched");
		this.version = version;
	}

	public ProtocolVersion getVersion() {
		return version;
	}

	public String getChallenge() {
		return challenge;
	}

	public enum ProtocolVersion {
		UNKNOWN, ONE, TWO
	}

	public static class VotifierGreetingHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			VotifierSession session = ctx.channel().attr(VotifierSession.KEY).get();
			VotifierPlugin plugin = ctx.channel().attr(VotifierPlugin.KEY).get();
			String version = "VOTIFIER " + plugin.getVersion() + " " + session.getChallenge() + "\n";
			ByteBuf versionBuf = Unpooled.copiedBuffer(version, StandardCharsets.UTF_8);
			ctx.writeAndFlush(versionBuf);
		}
	}

	public interface VoteHandler {
		void onVoteReceived(Channel channel, Vote vote, VotifierSession.ProtocolVersion protocolVersion) throws Exception;

		void onError(Channel channel, Throwable throwable);
	}

	public interface VotifierPlugin {
		AttributeKey<VotifierPlugin> KEY = AttributeKey.valueOf("votifier_plugin");

		Map<String, Key> getTokens();

		KeyPair getProtocolV1Key();

		String getVersion();
	}

	public interface VoteReceiver {
		void onVote(Vote vote) throws Exception;

		void onException(Throwable throwable);
	}
}
