package com.bigbrainiac10.simplehelpop.listeners;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.database.HelpOPData;

public class PlayerListener implements Listener {
	
	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	private HelpOPData helpData = plugin.getHelpOPData();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		List<HelpQuestion> q = new ArrayList<HelpQuestion>();
		try {
			q = helpData.getUnviewedQuestions(player);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(HelpQuestion question : q){
			OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid));
			
			player.sendMessage(p.getName() + "answered your question!");
			player.sendMessage("You asked: " + question.getQuestion());
			player.sendMessage("They replied: "+question.reply);
			
			question.setViewed(true);
			
			try {
				helpData.updateQuestion(question);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
