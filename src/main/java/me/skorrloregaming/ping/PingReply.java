package me.skorrloregaming.ping;

import io.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.bukkit.util.CachedServerIcon;

import java.net.InetSocketAddress;
import java.util.List;

public class PingReply {
    private String motd;
    private int onlinePlayers;
    private int maxPlayers;
    private int protocolVersion;
    private String protocolName;
    private List<String> playerSample;
    private boolean hidePlayers = false;
    private CachedServerIcon icon = Bukkit.getServerIcon();
    private String hostAddress;

    public PingReply(ChannelHandlerContext ctx, String motd, int onlinePlayers, int maxPlayers, int protocolVersion, String protocolName, List<String> playerSample) {
        this.motd = motd;
        this.onlinePlayers = onlinePlayers;
        this.maxPlayers = maxPlayers;
        this.protocolVersion = protocolVersion;
        this.protocolName = protocolName;
        this.playerSample = playerSample;
        this.hostAddress = (((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress());
    }

    public int getOnlinePlayers() {
        return this.onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getMOTD() {
        return this.motd;
    }

    public void setMOTD(String motd) {
        this.motd = motd;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolName() {
        return this.protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public List<String> getPlayerSample() {
        return this.playerSample;
    }

    public void setPlayerSample(List<String> playerSample) {
        this.playerSample = playerSample;
    }

    public boolean arePlayersHidden() {
        return this.hidePlayers;
    }

    public CachedServerIcon getIcon() {
        return this.icon;
    }

    public void setIcon(CachedServerIcon icon) {
        this.icon = icon;
    }

    public void hidePlayers(boolean hide) {
        this.hidePlayers = hide;
    }

    public String getHostAddress() {
        return hostAddress;
    }
}
