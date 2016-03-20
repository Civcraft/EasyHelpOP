package com.bigbrainiac10.simplehelpop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.Utility;
import com.bigbrainiac10.simplehelpop.ViewMenu;

public class ViewUnansweredCommand implements CommandExecutor {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		
		if(!cmd.getName().equalsIgnoreCase("viewunanswered"))
			return false;
		
		if(!(player.hasPermission("simplehelpop.replyhelp")))
			return false;
		
		ViewMenu viewMenu = new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), "Question Viewer" , player);
		viewMenu.showInventory();
		
		
		return true;
	}
	
}
