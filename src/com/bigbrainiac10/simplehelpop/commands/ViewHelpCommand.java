package com.bigbrainiac10.simplehelpop.commands;

<<<<<<< HEAD
import java.sql.SQLException;
import java.util.List;

=======
>>>>>>> 0c444d8413c2df75966e8445ce049fd42064bf90
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
<<<<<<< HEAD
import org.bukkit.metadata.FixedMetadataValue;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.viewmenu.ViewMenu;
=======

import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.viewmenu.ViewMenu;
import com.bigbrainiac10.simplehelpop.viewmenu.ViewType;
>>>>>>> 0c444d8413c2df75966e8445ce049fd42064bf90

import net.md_5.bungee.api.ChatColor;

public class ViewHelpCommand implements CommandExecutor {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		
		if(!cmd.getName().equalsIgnoreCase("viewhelp"))
			return false;
		
		if(!(player.hasPermission("simplehelpop.replyhelp")))
			return false;
		
		if(args.length == 0){
<<<<<<< HEAD
			ViewMenu viewMenu = new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), "Question Viewer" , player);
			return true;
		}else if(args.length >= 1){
			if(args[0].equalsIgnoreCase("all")){
				List<HelpQuestion> aq = null;
				
				try {
					aq = plugin.getHelpOPData().getAllQuestions();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				ViewMenu viewMenu = new ViewMenu(aq, "Question Viewer" , player);
				return true;
				//viewMenu.showInventory();
=======
			
		}else if(args.length >= 1){
			if(args[0].equalsIgnoreCase("all")){
				ViewMenu viewMenu = new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), "Question Viewer" , player, ViewType.ALL);
				viewMenu.showInventory();
>>>>>>> 0c444d8413c2df75966e8445ce049fd42064bf90
			}else if(args[0].equalsIgnoreCase("id")){
				if(!(args.length >= 2))
					showHelp(player);
			}else if(args[0].equalsIgnoreCase("name")){
				if(!(args.length >= 2))
					showHelp(player);
			}
		}
		
		/*
		ViewMenu viewMenu = new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), "Question Viewer" , player);
		viewMenu.showInventory();
		*/
		
		
		
		return showHelp(player);
	}

	private boolean showHelp(Player player){
		player.sendMessage(ChatColor.RED + "Usage: /viewhelp [all|id|name [ID|NAME]");
		return false;
	}
	
}
