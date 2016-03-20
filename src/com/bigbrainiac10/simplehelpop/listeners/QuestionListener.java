<<<<<<< HEAD
package com.bigbrainiac10.simplehelpop.listeners;

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
				player.sendMessage(alertMsg.replace("%PLAYER%", Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName()).replace("%ID%", Integer.toString(question.getEntryID())));
			}
		}
		
		SimpleHelpOp.Log("Player " + Bukkit.getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName() + " requested help. (Help ID " + question.getEntryID() + ")");
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void questionAnswered(QuestionAnsweredEvent event){
		HelpQuestion question = event.getQuestion();
		
	}
	
}
=======
package com.bigbrainiac10.simplehelpop.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class QuestionListener implements Listener {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	private final String alertMsg = SHOConfigManager.getHelperAlert();
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void questionCreated(QuestionCreatedEvent event){
		HelpQuestion question = event.getQuestion();
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			if(player.hasPermission("simplehelpop.replyhelp")){
				player.sendMessage(alertMsg.replace("%PLAYER%", Bukkit.getOfflinePlayer(question.asker_uuid).getName()).replace("%ID%", Integer.toString(question.getEntryID())));
			}
		}
	}
	
}
>>>>>>> 3ba9d0140ec71b8c634cc384b95303aba29055a7
