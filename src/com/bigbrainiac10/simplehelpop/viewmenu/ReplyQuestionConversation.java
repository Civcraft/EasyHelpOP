package com.bigbrainiac10.simplehelpop.viewmenu;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import vg.civcraft.mc.namelayer.NameAPI;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.events.QuestionAnsweredEvent;

public class ReplyQuestionConversation extends StringPrompt {

	private HelpQuestion question;
	private UUID player;

	public ReplyQuestionConversation(HelpQuestion question, Player player) {
		this.question = question;
		this.player = player.getUniqueId();
	}

	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());

		question.setReplyTime(time);
		question.setReply(answer);
		question.setReplier(player);
		SimpleHelpOp.getHelpOPData().updateQuestion(question);
		QuestionAnsweredEvent qe = new QuestionAnsweredEvent(question);
		Bukkit.getServer().getPluginManager().callEvent(qe);
		con.getForWhom()
				.sendRawMessage(ChatColor.GREEN + "Your reply was sent");

		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext con) {
		String name;
		if (SimpleHelpOp.isNameLayerEnabled()) {
			name = NameAPI.getCurrentName(question.getAskedUUID());
		} else {
			name = Bukkit.getServer()
					.getOfflinePlayer(question.getAskedUUID()).getName();
		}
		return ChatColor.GOLD + "Enter a reply to: " +  question.getQuestion() + " asked by " + name;
	}

}
