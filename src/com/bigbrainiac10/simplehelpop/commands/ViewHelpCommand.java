package com.bigbrainiac10.simplehelpop.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;

import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.viewmenu.ViewMenu;

public class ViewHelpCommand extends PlayerCommand {

	public ViewHelpCommand(String name) {
		super(name);
		setIdentifier("helpop");
		setDescription("Opens UI to answer player questions");
		setUsage("/viewhelp");
		setArguments(0, 0);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This one's for in-game only, sorry bud.");
			return true;
		}

		Player player = (Player) sender;
		ViewMenu vm = new ViewMenu(SimpleHelpOp.getHelpOPData().getUnansweredQuestions(),
				"Question Viewer", player);
		vm.openMenu();
		return true;
	}

	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}
}
