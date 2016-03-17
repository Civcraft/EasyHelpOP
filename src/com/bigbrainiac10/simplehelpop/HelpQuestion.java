package com.bigbrainiac10.simplehelpop;

import java.sql.Timestamp;
import java.util.UUID;

public class HelpQuestion {

	private int entryID;
	public Timestamp ask_time;
	public String asker_uuid;
	private String question;
	public Timestamp replyTime;
	public String replier_uuid;
	public String reply;
	
	private boolean answered;
	
	public HelpQuestion(int entryID, Timestamp ask_time, String asker_uuid, String question, Timestamp replyTime, String replier_uuid, String reply){
		this.entryID = entryID;
		this.ask_time = ask_time;
		this.asker_uuid = asker_uuid;
		this.question = question;
		this.replyTime = replyTime;
		this.replier_uuid = replier_uuid;
		this.reply = reply;
		
		this.answered = true;
	}
	
	public HelpQuestion(int entryID, Timestamp ask_time, String asker_uuid, String question){
		this.entryID = entryID;
		this.ask_time = ask_time;
		this.asker_uuid = asker_uuid;
		this.question = question;
		this.replyTime = null;
		this.replier_uuid = null;
		this.reply = null;
		
		this.answered = false;
	}
	
	public boolean updateQuestionInDB(){
		return true;
	}
	
	public boolean isAnswered(){
		return this.answered;
	}
	
	public void setReplyTime(Timestamp time){
		this.replyTime = time;
	}
	
	public String getQuestion(){
		return this.question;
	}
	
	public int getEntryID(){
		return this.entryID;
	}
	
}
