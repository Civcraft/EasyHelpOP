package com.bigbrainiac10.simplehelpop.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.Utility;
import com.bigbrainiac10.simplehelpop.database.HelpOPData;

import net.md_5.bungee.api.ChatColor;

public class HelpOPCommand implements CommandExecutor {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	private final String addedMsg = Utility.safeToColor(SHOConfigManager.getPlayerMessage("questionAdded"));
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		
		if(!cmd.getName().equalsIgnoreCase("helpop"))
			return false;
		
		if(!(player.hasPermission("simplehelpop.requesthelp")))
			return false;
		
		if(args.length < 1){
			player.sendMessage(ChatColor.RED + "Usage: /helpop [MESSAGE]");
			return false;
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (String arg : args){
			sb.append(arg).append(" ");
		}
		 
		String message = sb.toString().trim();
		
		try {
			plugin.getHelpOPData().addQuestion(player.getUniqueId().toString(), message);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to add question.", e);
			return false;
		}
		
		player.sendMessage(addedMsg);
		
		return true;
	}

}
