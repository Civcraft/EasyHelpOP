package com.bigbrainiac10.simplehelpop.viewmenu;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
<<<<<<< HEAD
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;

public class ReplyQuestionConversation extends StringPrompt{

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	private HelpQuestion question;
	private Player player;
	
=======

public class ReplyQuestionConversation extends StringPrompt{

	private HelpQuestion question;
	private Player player;
	
>>>>>>> 0c444d8413c2df75966e8445ce049fd42064bf90
	public ReplyQuestionConversation(HelpQuestion question, Player player){
		this.question = question;
		this.player = player;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());
		
		question.replyTime = time;
		question.reply = answer;
		question.replier_uuid = player.getUniqueId().toString();
		
		try {
			plugin.getHelpOPData().updateQuestion(question);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return "Enter the reply to this question. Type cancel to exit out of prompt.";
	}

}
