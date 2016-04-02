package com.bigbrainiac10.simplehelpop.commands;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.Utility;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class HelpOPCommand implements CommandExecutor {

	private final SimpleHelpOp plugin;
	private final String addedMsg = Utility.safeToColor(SHOConfigManager.getPlayerMessage("questionAdded"));
	private final String failureMsg = Utility.safeToColor(SHOConfigManager.getPlayerMessage("questionFailure"));
	
	public HelpOPCommand(SimpleHelpOp plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("If console needs help, we're all doomed");
			return false;
		}
		
		if (args.length < 1) {
			return false;
		}
		
		Player player = (Player)sender;
		
		StringBuilder sb = new StringBuilder();
		
		for (String arg : args){
			sb.append(arg).append(" ");
		}
		 
		String message = sb.toString().trim();
		
		HelpQuestion hq = null;
		
		try {
			hq = plugin.getHelpOPData().addQuestion(player.getUniqueId().toString(), message);
			QuestionCreatedEvent qe = new QuestionCreatedEvent(hq);
			Bukkit.getServer().getPluginManager().callEvent(qe);
		} catch (SQLException e) {
			player.sendMessage(failureMsg);
			plugin.getLogger().log(Level.SEVERE, "Failed to add question.", e);
			return false;
		}
		
		player.sendMessage(addedMsg);
		
		return true;
	}

}
