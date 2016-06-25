package com.bigbrainiac10.easyhelpop.commands;

import java.sql.SQLException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.EHOConfigManager;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.Utility;
import com.bigbrainiac10.easyhelpop.events.QuestionCreatedEvent;

public class HelpOPCommand implements CommandExecutor {

	private final EasyHelpOp plugin;
	private final String addedMsg = Utility.safeToColor(EHOConfigManager.getPlayerMessage("questionAdded"));
	private final String failureMsg = Utility.safeToColor(EHOConfigManager.getPlayerMessage("questionFailure"));
	
	public HelpOPCommand(EasyHelpOp plugin) {
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
			return true;
		}
		
		player.sendMessage(addedMsg);
		
		return true;
	}

}
