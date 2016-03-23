package com.bigbrainiac10.simplehelpop.viewmenu;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;

public class ReplyQuestionConversation extends StringPrompt{

	private HelpQuestion question;
	private Player player;
	
	public ReplyQuestionConversation(HelpQuestion question, Player player){
		this.question = question;
		this.player = player;
	}
	
	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		return null;
	}

	@Override
	public String getPromptText(ConversationContext con) {
		return "Enter the reply to this question. Type cancel to exit out of prompt.";
	}

}
