<<<<<<< HEAD
package com.bigbrainiac10.simplehelpop.listeners;

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
		
		/*for(HelpQuestion question : helpData.getUnansweredQuestions()){
			
		}*/
	}
	
}
=======
package com.bigbrainiac10.simplehelpop.listeners;

import org.bukkit.event.Listener;

public class PlayerListener implements Listener {
	
	
}
>>>>>>> 3ba9d0140ec71b8c634cc384b95303aba29055a7
