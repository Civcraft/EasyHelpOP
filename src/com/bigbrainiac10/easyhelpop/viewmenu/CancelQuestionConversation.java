package com.bigbrainiac10.easyhelpop.viewmenu;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationCanceller;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ExactMatchConversationCanceller;

import com.bigbrainiac10.easyhelpop.HelpQuestion;

/**
 * Capturing cancel in order to release.
 * 
 * @author ProgrammerDan
 */
public class CancelQuestionConversation extends ExactMatchConversationCanceller {
	private HelpQuestion question;
	public CancelQuestionConversation(HelpQuestion question) {
		super("cancel");
		this.question = question;
	}
	@Override
	public boolean cancelBasedOnInput(ConversationContext context, String input) {
		if (super.cancelBasedOnInput(context, input)) {
			context.getForWhom().sendRawMessage(ChatColor.DARK_GRAY + "Reply cancelled");
			this.question.release();
			return true;
		}
		return false;
	}
	
	@Override
    public ConversationCanceller clone() {
        return new CancelQuestionConversation(question);
    }
}
