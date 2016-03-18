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

import net.md_5.bungee.api.ChatColor;

public class HelpOPCommand implements CommandExecutor {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
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
			player.sendMessage(ChatColor.RED + "Usage: /helpop [message]");
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 1; i < args.length; i++){
			sb.append(args[i]).append(" ");
		}
		 
		String allArgs = sb.toString().trim();
		
		try {
			plugin.getHelpOPData().addQuestion(player.getUniqueId().toString(), allArgs);
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Failed to add question.", e);
		}
		
		
		return false;
	}

}
