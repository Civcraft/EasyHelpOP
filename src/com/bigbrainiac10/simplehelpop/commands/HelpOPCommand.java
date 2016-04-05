package com.bigbrainiac10.simplehelpop.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import vg.civcraft.mc.civmodcore.command.PlayerCommand;
import vg.civcraft.mc.mercury.MercuryAPI;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class HelpOPCommand extends PlayerCommand {

	public HelpOPCommand(String name) {
		super(name);
		setIdentifier("helpop");
		setDescription("Asks a server operator for help");
		setUsage("/helpop <message>");
		setArguments(0, 100);
	}

	@Override
	public boolean execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("If console needs help, we're all doomed");
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage(ChatColor.RED + "You have to enter a message");
			return true;
		}

		Player player = (Player) sender;

		StringBuilder sb = new StringBuilder();

		for (String arg : args) {
			sb.append(arg).append(" ");
		}

		String message = sb.toString().trim();

		HelpQuestion hq = null;

		hq = SimpleHelpOp.getHelpOPData().addQuestion(
				player.getUniqueId(), message);
		if (hq != null) {
			QuestionCreatedEvent qe = new QuestionCreatedEvent(hq);
			Bukkit.getServer().getPluginManager().callEvent(qe);
			if (SimpleHelpOp.isMercuryEnabled()) {
				MercuryAPI.sendGlobalMessage("question|"+player.getName(), "SimpleHelpOp");
			}
		} else {
			player.sendMessage(ChatColor.RED
					+ "Failed to send question, please try again later");
		}
		player.sendMessage(ChatColor.GREEN + "Your question was sent!");

		return true;
	}
	
	public List<String> tabComplete(CommandSender sender, String[] args) {
		return null;
	}

}
