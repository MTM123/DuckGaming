package me.skorrloregaming.discord.listeners;

import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.discord.DiscordBot;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class MessageListener extends ListenerAdapter {

    private DiscordBot discordBot;

    public MessageListener(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        if (event.getMember() == null)
            return;
        if (event.getMember().getUser() == null)
            return;
        if (Server.getInstance().getPlugin().isEnabled())
            Bukkit.getScheduler().runTask(Server.getInstance().getPlugin(), new Runnable() {

                @Override
                public void run() {
                    String id = event.getMember().getUser().getId();
                    String mention = "<@" + id + ">!";
                    if (event.isFromType(ChannelType.TEXT)) {
                        if (!event.getAuthor().isBot())
                            if (event.getTextChannel().getName().equals(discordBot.getChannelName(Channel.SERVER_CHAT))) {
                                String rawMessage = event.getMessage().getContentDisplay();
                                String memberName = event.getMember().getEffectiveName();
                                String altAddress = null;
                                OfflinePlayer op = CraftGo.Player.getOfflinePlayer(memberName);
                                if (op.hasPlayedBefore() || op.isOnline()) {
                                    String path = "config." + op.getUniqueId().toString();
                                    if (Server.getInstance().getPlugin().getConfig().contains(path + ".ip")) {
                                        altAddress = Server.getInstance().getPlugin().getConfig().getString(path + ".ip");
                                    }
                                }
                                if (altAddress == null || Server.getInstance().getBanConfig().getData().contains(altAddress.replace(".", "x")) || !Server.getInstance().getDiscordVerifyConfig().getData().contains("verified." + id)) {
                                    event.getMessage().delete().queueAfter(2000, TimeUnit.MILLISECONDS);
                                    discordBot.broadcast("Sorry " + mention + ", you are no longer verified as that player.", 2000, Channel.SERVER_CHAT);
                                    Server.getInstance().getDiscordVerifyConfig().getData().set("verified." + id, null);
                                    Server.getInstance().getDiscordVerifyConfig().saveData();
                                    event.getGuild().getController().removeRolesFromMember(event.getMember(), event.getJDA().getRolesByName("Verified", true)).complete();
                                    event.getGuild().getController().setNickname(event.getMember(), event.getMember().getUser().getName()).complete();
                                    return;
                                }
                                rawMessage = LinkServer.getInstance().getAntiCheat().processAntiSwear(op, rawMessage, false, true);
                                String displayName = Link$.getFlashPlayerDisplayName(memberName);
                                String username = event.getMember().getUser().getName() + "#" + event.getMember().getUser().getDiscriminator();
                                TextComponent newLine = new TextComponent(ComponentSerializer.parse("{text: \"\n\"}"));
                                BaseComponent[] nameAndDiscriminator;
                                if (event.getMember().getGame() == null) {
                                    nameAndDiscriminator = new ComponentBuilder(memberName).color(ChatColor.BOLD).append(newLine)
                                            .append(username).create();
                                } else {
                                    nameAndDiscriminator = new ComponentBuilder(memberName).color(ChatColor.BOLD).append(newLine)
                                            .append(username).append(newLine).append(newLine)
                                            .append("Playing " + event.getMember().getGame().getName()).create();
                                }
                                HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, nameAndDiscriminator);
                                BaseComponent[] message = new ComponentBuilder("[").color(ChatColor.GRAY).event(hoverEvent).append("discord")
                                        .color(ChatColor.WHITE).event(hoverEvent).append("] ").color(ChatColor.GRAY).event(hoverEvent)
                                        .append(displayName).color(ChatColor.RESET).event(hoverEvent).append(" " + '\u00BB' + " ")
                                        .color(ChatColor.RESET).event(hoverEvent).append(rawMessage).event(hoverEvent).create();
                                Logger.info(new TextComponent(message).toPlainText(), true);
                                String json = ComponentSerializer.toString(message);
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    CraftGo.Player.sendJson(player, json);
                                }
                                LinkServer.getInstance().getRedisMessenger().broadcast(RedisChannel.CHAT, new MapBuilder().message(json).json(true).build());
                            } else if (event.getTextChannel().getName().equals(discordBot.getChannelName(Channel.SERVER_VERIFY))) {
                                String rawMessage = event.getMessage().getContentDisplay();
                                if (rawMessage.startsWith("?verify")) {
                                    String[] args = rawMessage.split(" ");
                                    if (args.length > 1) {
                                        String codeString = args[1];
                                        try {
                                            int code = Integer.parseInt(codeString);
                                            boolean hit = false;
                                            UUID hitUUID = null;
                                            for (Map.Entry<Integer, UUID> entry : Server.getInstance().getDiscordVerifyPlayers().entrySet()) {
                                                if (entry.getKey().intValue() == code) {
                                                    hitUUID = entry.getValue();
                                                    Server.getInstance().getDiscordVerifyConfig().getData().set("verified." + id, entry.getValue().toString());
                                                    Server.getInstance().getDiscordVerifyConfig().saveData();
                                                    discordBot.broadcast("Thank you for verifying you minecraft account, " + mention + "!", Channel.SERVER_VERIFY);
                                                    hit = true;
                                                }
                                            }
                                            if (!hit) {
                                                event.getMessage().delete().queueAfter(2000, TimeUnit.MILLISECONDS);
                                                discordBot.broadcast("That's not a valid code, you can get one with /verify on the server.", 2000, Channel.SERVER_VERIFY);
                                            } else {
                                                Server.getInstance().getDiscordVerifyPlayers().remove(code, hitUUID);
                                                OfflinePlayer player = Bukkit.getOfflinePlayer(hitUUID);
                                                event.getGuild().getController().addRolesToMember(event.getMember(), event.getJDA().getRolesByName("Verified", true)).complete();
                                                event.getGuild().getController().setNickname(event.getMember(), player.getName()).complete();
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                            event.getMessage().delete().queueAfter(2000, TimeUnit.MILLISECONDS);
                                            discordBot.broadcast("Please use ?verify <code> to link your minecraft account.", 2000, Channel.SERVER_VERIFY);
                                        }
                                    }
                                } else {
                                    event.getMessage().delete().queueAfter(2000, TimeUnit.MILLISECONDS);
                                    discordBot.broadcast("Please use ?verify <code> to link your minecraft account.", 2000, Channel.SERVER_VERIFY);
                                }
                            }
                    }
                }
            });
    }
}
