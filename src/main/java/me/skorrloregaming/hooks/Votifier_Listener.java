package me.skorrloregaming.hooks;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.skorrloregaming.*;
import me.skorrloregaming.discord.Channel;
import me.skorrloregaming.impl.ServerMinigame;
import me.skorrloregaming.impl.Service;
import me.skorrloregaming.impl.ServicePriority;
import me.skorrloregaming.redis.MapBuilder;
import me.skorrloregaming.redis.RedisChannel;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.text.DecimalFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Votifier_Listener implements Listener {

	public void register() {
		Bukkit.getPluginManager().registerEvents(this, Server.getPlugin());
	}

	@EventHandler
	public void onVote(VotifierEvent event) {
		Server.getVoteManager().onVote(event, true);
	}
}