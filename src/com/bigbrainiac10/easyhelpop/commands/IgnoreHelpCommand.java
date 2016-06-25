package com.bigbrainiac10.easyhelpop.commands;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

import com.bigbrainiac10.easyhelpop.EHOConfigManager;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.Utility;
import com.bigbrainiac10.easyhelpop.events.QuestionAnsweredEvent;
import com.bigbrainiac10.easyhelpop.viewmenu.ConsoleReplyQuestionConversation;

public class IgnoreHelpCommand implements CommandExecutor {

	private final EasyHelpOp plugin;
	private final String idUnknown = Utility.safeToColor(EHOConfigManager.getPlayerMessage("idUnknown"));
	private final String generalFailure = Utility.safeToColor(EHOConfigManager.getPlayerMessage("generalFailure"));
	private final String ignoreQuestion = Utility.safeToColor(EHOConfigManager.getPlayerMessage("ignoreQuestion"));
	private final String reserved = Utility.safeToColor(EHOConfigManager.getPlayerMessage("reserved"));

	public IgnoreHelpCommand(EasyHelpOp plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			try {
				HelpQuestion question = plugin.getHelpOPData().getUnansweredByID(Integer.parseInt(args[0]));
				
				if (question != null) {
					if (question.reserve()) {
						Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
						
						question.replyTime = time;
						question.reply = "";
						question.replier_uuid = (sender instanceof Player ? ((Player)sender).getUniqueId().toString() : sender.getName());
						question.setViewed(true);
						
						try {
							plugin.getHelpOPData().updateQuestion(question);
							sender.sendMessage(ignoreQuestion.replace("%QUESTION%", question.getQuestion()));
						} catch (SQLException e) {
							plugin.getLogger().log(Level.SEVERE, "Failed to update question.", e);
							sender.sendMessage(generalFailure);
						}
						question.release();
					} else {
						sender.sendMessage(reserved);
					}
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
