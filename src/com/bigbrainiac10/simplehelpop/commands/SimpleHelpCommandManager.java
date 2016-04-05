package com.bigbrainiac10.simplehelpop.commands;

import vg.civcraft.mc.civmodcore.command.CommandHandler;

public class SimpleHelpCommandManager extends CommandHandler {
	
	public void registerCommands() {
		addCommands(new ViewHelpCommand("viewhelp"));
		addCommands(new HelpOPCommand("helpop"));
	}

}
