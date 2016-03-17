package com.bigbrainiac10.simplehelpop.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.SHOConfigManager;

public class HelpOPCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		
		if(!player.hasPermission("simplehelpop.requesthelp"))
			return false;
		
		
		
		
		return false;
	}

}
