package com.bigbrainiac10.simplehelpop.listeners;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.Utility;
import com.bigbrainiac10.simplehelpop.events.QuestionAnsweredEvent;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class QuestionListener implements Listener {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	private final String alertMsg = Utility.safeToColor(SHOConfigManager.getPlayerMessage("helperAlert"));
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void questionCreated(QuestionCreatedEvent event){
		HelpQuestion question = event.getQuestion();
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.hasPermission("simplehelpop.replyhelp")){
				player.sendMessage( alertMsg
						.replace("%PLAYER%", Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName())
						.replace("%MESSAGE%", question.getQuestion())
						.replace("%ID%", Integer.toString(question.getEntryID())));
			}
		}
		
		SimpleHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " requested help. (Help ID " + question.getEntryID() + ")");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void questionAnswered(QuestionAnsweredEvent event){
		HelpQuestion question = event.getQuestion();
		
		Player p = Bukkit.getPlayer(UUID.fromString(question.replier_uuid));
		if (p == null) {
			p = (Player) Bukkit.getOfflinePlayer(UUID.fromString(question.replier_uuid));
		}
		Player a = Bukkit.getPlayer(UUID.fromString(question.asker_uuid));
		if (a != null) {
			if (p != null) {
				a.sendMessage(p.getName() + " answered your question!");
			} else {
				a.sendMessage("A helper answered your question!");
			}
			a.sendMessage("You asked: " + question.getQuestion() + "\nThey replied: "+ question.reply);
			
			question.setViewed(true);
			
			try {
				plugin.getHelpOPData().updateQuestion(question);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			SimpleHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " received an answer and has seen it. (Help ID " + question.getEntryID() + ")");
		} else {
			SimpleHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " received an answer. (Help ID " + question.getEntryID() + ")");
		}

	}
	
}
