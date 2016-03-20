package com.bigbrainiac10.simplehelpop.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.bigbrainiac10.simplehelpop.HelpQuestion;

public class QuestionCreatedEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	
	private boolean cancelled;
	private HelpQuestion question;
	
	public QuestionCreatedEvent(HelpQuestion question){
		this.question = question;
	}
	
	public HelpQuestion getQuestion(){
		return question;
	}
	
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled){
		this.cancelled = cancelled;
	}

	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}

	
	
}
