package com.bigbrainiac10.simplehelpop.events;

import vg.civcraft.mc.civmodcore.interfaces.CustomEvent;

import com.bigbrainiac10.simplehelpop.HelpQuestion;

public class QuestionAnsweredEvent extends CustomEvent{
	
	private HelpQuestion question;
	
	public QuestionAnsweredEvent(HelpQuestion question){
		this.question = question;
	}
	
	public HelpQuestion getQuestion(){
		return question;
	}
	
}
