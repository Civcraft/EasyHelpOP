package com.bigbrainiac10.simplehelpop.commands;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.Utility;

import net.md_5.bungee.api.ChatColor;

public class ReplyHelpOPCommand implements CommandExecutor {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	private final String replyMsg = Utility.safeToColor(SHOConfigManager.getPlayerMessage("replyAdded"));
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		
		if(!cmd.getName().equalsIgnoreCase("replyhelp"))
			return false;
		
		if(!(player.hasPermission("simplehelpop.replyhelp")))
			return false;
		
		if(args.length < 2){
			player.sendMessage(ChatColor.RED + "Usage: /replyhelp [ID] [MESSAGE]");
			return false;
		}
		
		int questionID;
		
		try {
			questionID = Integer.parseInt(args[0]);
		}catch(Exception e){
			player.sendMessage(ChatColor.RED + "ID needs to be an integer");
			return false;
		}
		
		HelpQuestion replyQuestion = null;
		
		for(HelpQuestion question : plugin.getHelpOPData().getUnansweredQuestions()){
			if(question.getEntryID() == questionID){
				replyQuestion = question;
				break;
			}
		}
		
		if(replyQuestion == null){
			player.sendMessage(ChatColor.RED + "No question was found with an ID of " + questionID);
			return false;
		}
		
		args = (String[]) ArrayUtils.remove(args, 0);
		
		StringBuilder sb = new StringBuilder();
		
		for (String arg : args){
			sb.append(arg).append(" ");
		}
		 
		String message = sb.toString().trim();
		
		Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());
		
		replyQuestion.replier_uuid = player.getUniqueId().toString();
		replyQuestion.reply = message;
		replyQuestion.replyTime = time;
		
		try {
			plugin.getHelpOPData().updateQuestion(replyQuestion);
			plugin.getHelpOPData().removeUnansweredQuestion(replyQuestion);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to update question.", e);
			return false;
		}
		
		player.sendMessage(replyMsg);
		
		return true;
	}
	
}
