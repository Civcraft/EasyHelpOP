package com.bigbrainiac10.simplehelpop.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class HelpOPData {
	private Database db;
	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	//private List<HelpQuestion> questionList;
	
	private int lastHelpID;
	
	public HelpOPData(Database db){
		this.db = db;
		if(db.connect()){
			createTables();
		}
		
		//this.questionList = new ArrayList<HelpQuestion>();
		try {
			this.lastHelpID = getLastID();
		} catch(SQLException e){
			plugin.getLogger().log(Level.SEVERE, "Wasn't able to get the ", e);
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}
	
	private void createTables(){
		db.execute("CREATE TABLE IF NOT EXISTS help_requests("
				+ "help_id int NOT NULL AUTO_INCREMENT,"
				+ "ask_time timestamp NOT NULL,"
				+ "asker_uuid varchar(128) NOT NULL,"
				+ "question text NOT NULL,"
				+ "reply_time int,"
				+ "replier_uuid varchar(128),"
				+ "reply text,"
				+ "PRIMARY KEY(help_id));");
	}
	
	private void reconnect(){
		if(db.isConnected())
			return;
		
		db.connect();
	}
	
	private int getLastID() throws SQLException{
		reconnect();
		
		PreparedStatement ps = db.prepareStatement("SELECT LAST_INSERT_ID();");
		Object object_lastID = ps.executeQuery().getInt(1);
		int lastID;
		
		try{
			lastID = (int)object_lastID;
		}catch(Exception e){
			lastID = 0;
		}
		
		return lastID;
	}
	
	public List<HelpQuestion> getUnansweredQuestions() throws SQLException{
		reconnect();
		PreparedStatement ps = db.prepareStatement("SELECT * FROM help_requests WHERE reply_time IS NULL");
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			int helpID = results.getInt("help_id");
			Timestamp askTime = results.getTimestamp("ask_time");
			String askerUUID = results.getString("asker_uuid");
			String question = results.getString("question");
			
			unansweredQuestions.add(new HelpQuestion(helpID, askTime, askerUUID, question));
		}
		
		
		return unansweredQuestions;
	}
	
	public boolean addQuestion(String askerUUID, String question) throws SQLException{
		PreparedStatement ps = db.prepareStatement("INSERT INTO help_requests(ask_time, asker_uuid, question) VALUES(?,?,?);");
		
		Timestamp time = new Timestamp(Calendar.getInstance().getTime().getTime());
		
		ps.setTimestamp(1, time);
		ps.setString(2, askerUUID);
		ps.setString(3, question);
		
		HelpQuestion helpQuestion = new HelpQuestion(lastHelpID+1, time, askerUUID, question);
		lastHelpID++;
		
		Bukkit.getServer().getPluginManager().callEvent(new QuestionCreatedEvent(helpQuestion));
		
		return false;
	}
	
	public boolean updateQuestion(HelpQuestion question) throws SQLException{
		PreparedStatement ps = db.prepareStatement("UPDATE help_requests SET ask_time=?, asker_uuid=?, question=?, reply_time=?, replier_uuid=? reply=? WHERE help_id=?;");
		
		ps.setTimestamp(1, question.ask_time);
		ps.setString(2, question.asker_uuid);
		ps.setString(3, question.getQuestion());
		ps.setTimestamp(4, question.replyTime);
		ps.setString(5, question.replier_uuid);
		ps.setString(6, question.reply);
		ps.setInt(7, question.getEntryID());
		
		int rowsAffected = ps.executeUpdate();
		
		return rowsAffected > 0;
	}
	
	
}
