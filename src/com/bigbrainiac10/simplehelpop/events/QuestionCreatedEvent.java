package com.bigbrainiac10.simplehelpop.events;

import vg.civcraft.mc.civmodcore.interfaces.CustomEvent;

import com.bigbrainiac10.simplehelpop.HelpQuestion;

public class QuestionCreatedEvent extends CustomEvent{
	
	private HelpQuestion question;
	
	public QuestionCreatedEvent(HelpQuestion question){
		this.question = question;
	}
	
	public HelpQuestion getQuestion(){
		return question;
	}
}
