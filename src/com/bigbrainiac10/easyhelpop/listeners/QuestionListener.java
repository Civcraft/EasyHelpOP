package com.bigbrainiac10.easyhelpop.listeners;

import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.EHOConfigManager;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.Utility;
import com.bigbrainiac10.easyhelpop.events.QuestionAnsweredEvent;
import com.bigbrainiac10.easyhelpop.events.QuestionCreatedEvent;

public class QuestionListener implements Listener {

	private EasyHelpOp plugin = EasyHelpOp.getInstance();
	
	private final String alertMsg = Utility.safeToColor(EHOConfigManager.getPlayerMessage("helperAlert"));
	private final String replyMsg = Utility.safeToColor(EHOConfigManager.getPlayerMessage("replyReceived"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void questionCreated(QuestionCreatedEvent event){
		HelpQuestion question = event.getQuestion();
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.hasPermission("easyhelpop.replyhelp")){
				player.sendMessage( alertMsg
						.replace("%PLAYER%", Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName())
						.replace("%MESSAGE%", question.getQuestion())
						.replace("%ID%", Integer.toString(question.getEntryID())));
			}
		}
		
		EasyHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " requested help. (Help ID " + question.getEntryID() + ")");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void questionAnswered(QuestionAnsweredEvent event){
		HelpQuestion question = event.getQuestion();
		
		question.release();
		
		Player p = null;
		UUID pu = null;
		try {
			pu = UUID.fromString(question.replier_uuid);
		} catch (IllegalArgumentException iae) {
			pu = null;
		}
		
		if (pu != null) {
			p = Bukkit.getPlayer(pu);
			if (p == null) {
				p = (Player) Bukkit.getOfflinePlayer(pu);
			}
		}
		Player a = Bukkit.getPlayer(UUID.fromString(question.asker_uuid));
		if (a != null) {
			a.sendMessage( replyMsg
					.replace("%HELPER%", p != null ? p.getName() : "A helper")
					.replace("%QUESTION%", question.getQuestion())
					.replace("%ANSWER%", question.reply));
			
			question.setViewed(true);
			
			try {
				plugin.getHelpOPData().updateQuestion(question);
				plugin.getHelpOPData().removeAnsweredQuestion(question);
			} catch (SQLException e) {
				plugin.getLogger().log(Level.SEVERE, "Failed to update question.", e);
			}
			EasyHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " received an answer and has seen it. (Help ID " + question.getEntryID() + ")");
		} else {
			EasyHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " received an answer. (Help ID " + question.getEntryID() + ")");
		}

	}
	
}
