package com.bigbrainiac10.easyhelpop.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.bigbrainiac10.easyhelpop.HelpQuestion;

public class QuestionAnsweredEvent extends Event{

	private static final HandlerList handlers = new HandlerList();
	
	private HelpQuestion question;
	
	public QuestionAnsweredEvent(HelpQuestion question){
		this.question = question;
	}
	
	public HelpQuestion getQuestion(){
		return question;
	}

	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
}
