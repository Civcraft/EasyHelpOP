package com.bigbrainiac10.easyhelpop.commands;

import java.util.logging.Level;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;

import com.bigbrainiac10.easyhelpop.EHOConfigManager;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.Utility;
import com.bigbrainiac10.easyhelpop.viewmenu.ConsoleReplyQuestionConversation;

public class ReplyHelpCommand implements CommandExecutor {

	private final EasyHelpOp plugin;
	private final String idUnknown = Utility.safeToColor(EHOConfigManager.getPlayerMessage("idUnknown"));
	private final String generalFailure = Utility.safeToColor(EHOConfigManager.getPlayerMessage("generalFailure"));

	public ReplyHelpCommand(EasyHelpOp plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Conversable && args.length == 1) {
			try {
				HelpQuestion hq = plugin.getHelpOPData().getUnansweredByID(Integer.parseInt(args[0]));
	
				if (hq != null) {
					ConversationFactory cf = new ConversationFactory(plugin);
					Conversation conv = cf.withFirstPrompt(new ConsoleReplyQuestionConversation(hq, sender))
								.withLocalEcho(true)
								.withEscapeSequence("cancel")
								.withModality(false)
								.buildConversation((Conversable)sender);
					conv.begin();
				} else {
					plugin.getLogger().log(Level.INFO, "Failed to find question id: {0}", args[0]);
					sender.sendMessage(idUnknown);
				}
				return true;
			} catch (NumberFormatException e) {
				plugin.getLogger().log(Level.SEVERE, "Failed to view question by id", e);
				sender.sendMessage(generalFailure);
			}
		}

		return false;
	}

}
