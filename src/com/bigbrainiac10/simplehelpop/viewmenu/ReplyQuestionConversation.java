package com.bigbrainiac10.simplehelpop.viewmenu;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class ReplyQuestionConversation extends StringPrompt{

	
	
	@Override
	public Prompt acceptInput(ConversationContext con, String answer) {
		return null;
	}

	@Override
	public String getPromptText(ConversationContext arg0) {
		return "Enter the reply to this question. Type cancel to exit out of prompt.";
	}

}
