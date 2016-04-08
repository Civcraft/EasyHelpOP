package com.bigbrainiac10.easyhelpop.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.EHOConfigManager;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.Utility;
import com.bigbrainiac10.easyhelpop.database.HelpOPData;

public class PlayerListener implements Listener {
	
	private EasyHelpOp plugin = EasyHelpOp.getInstance();
	private HelpOPData helpData = plugin.getHelpOPData();
	private final String unansweredReady = Utility.safeToColor(EHOConfigManager.getPlayerMessage("unansweredReady"));
	private final String replyMsg = Utility.safeToColor(EHOConfigManager.getPlayerMessage("replyReceived"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		List<HelpQuestion> qq = new ArrayList<HelpQuestion>();
		try {
			qq = helpData.getUnviewedQuestions(player);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to retrieve questions for joining player", e);
		}

		final UUID p = player.getUniqueId();
		
		if (qq != null && qq.size() > 0) {
			final List<HelpQuestion> q = qq;
			
			new BukkitRunnable() {
				@Override
				public void run() {
					Player player = Bukkit.getPlayer(p);
					if (player == null) return;
					for(HelpQuestion question : q){
						OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(question.replier_uuid));
						
						player.sendMessage( replyMsg
								.replace("%HELPER%", p != null ? p.getName() : "A helper")
								.replace("%QUESTION%", question.getQuestion())
								.replace("%ANSWER%", question.reply));
						
						question.setViewed(true);
						
						try {
							helpData.updateQuestion(question);
							helpData.removeAnsweredQuestion(question);
						} catch (SQLException e) {
							plugin.getLogger().log(Level.SEVERE, "Failed to update question.", e);
						}
					}
				}
			}.runTaskLater(plugin, 20l);
		}
		
		if (player.hasPermission("easyhelpop.replyhelp")){
			List<?> u = helpData.getUnansweredQuestions();
			if (u != null && u.size() > 0) {
				new BukkitRunnable() {

					@Override
					public void run() {
						Player player = Bukkit.getPlayer(p);
						if (player == null) return;
						player.sendMessage(unansweredReady);
					}
				}.runTaskLater(plugin, 40l);
			}
		}
	}
	
}
