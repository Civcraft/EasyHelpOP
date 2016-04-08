package com.bigbrainiac10.easyhelpop.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;

public class HelpOPData {
	private Database db;
	private EasyHelpOp plugin = EasyHelpOp.getInstance();
	
	private List<HelpQuestion> questionList;
	
	public HelpOPData(Database db){
		this.db = db;
		if(db.connect()){
			createTables();
		}
		
		try {
			this.questionList = getUnansweredQuestionsFromDB();
		} catch (SQLException e) {
			plugin.getLogger().log(Level.SEVERE, "Wasn't able to get unanswered questions", e);
		}
	}
	
	private void createTables(){
		db.execute("CREATE TABLE IF NOT EXISTS help_requests("
				+ "help_id int NOT NULL AUTO_INCREMENT,"
				+ "ask_time timestamp NOT NULL,"
				+ "asker_uuid varchar(36) NOT NULL,"
				+ "question text NOT NULL,"
				+ "reply_time timestamp NULL DEFAULT NULL,"
				+ "replier_uuid varchar(36) DEFAULT NULL,"
				+ "reply text DEFAULT NULL,"
				+ "viewed bool NOT NULL,"
				+ "PRIMARY KEY(help_id));");
	}
	
	public HelpQuestion extractHelpQuestion(ResultSet results) throws SQLException {
		int helpID = results.getInt(1);
		Timestamp askTime = results.getTimestamp(2, Calendar.getInstance());
		String askerUUID = results.getString(3);
		String question = results.getString(4);
		Timestamp replyTime = results.getTimestamp(5, Calendar.getInstance());
		String replier_uuid = results.getString(6);
		String reply = results.getString(7);
		boolean viewed = results.getBoolean(8);
		
		return new HelpQuestion(helpID, askTime, askerUUID, question, replyTime, replier_uuid, reply, viewed);
	}
	
	public List<HelpQuestion> getUnviewedQuestions(Player player) throws SQLException{
		if (player == null) {
			EasyHelpOp.Log(Level.WARNING, "Unable to get unviewed questions for a null player");
			return new ArrayList<HelpQuestion>();
		}
		PreparedStatement ps = db.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests WHERE asker_uuid=? AND reply IS NOT NULL AND viewed=False;");
		
		ps.setString(1, player.getUniqueId().toString());
		
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> unviewedQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			unviewedQuestions.add(extractHelpQuestion(results));
		}
		return unviewedQuestions;
	}
	
	public List<HelpQuestion> getUnansweredQuestions(UUID player) throws SQLException{
		if (player == null) {
			EasyHelpOp.Log(Level.WARNING, "Unable to get unanswered questions for a null player");
			return new ArrayList<HelpQuestion>();
		}
		PreparedStatement ps = db.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests WHERE asker_uuid=? AND reply IS NULL;");
		
		ps.setString(1, player.toString());
		
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			try {
				unansweredQuestions.add(extractHelpQuestion(results));
			} catch (SQLException sqe) {
				plugin.getLogger().log(Level.WARNING, "Failure while extracting a help question:", sqe);
			}
		}
		return unansweredQuestions;
	}
	
	public List<HelpQuestion> getAllQuestions() throws SQLException{
		PreparedStatement ps = db.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests;");
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> allQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			try {
				allQuestions.add(extractHelpQuestion(results));
			} catch (SQLException sqe) {
				plugin.getLogger().log(Level.WARNING, "Failure while extracting a help question:", sqe);
			}
		}
		return allQuestions;
	}
	
	public void removeAnsweredQuestion(HelpQuestion question){
		for(HelpQuestion q : questionList){
			if(q.getEntryID() == question.getEntryID()){
				questionList.remove(q);
				break;
			}
		}
	}
	
	public HelpQuestion getUnansweredByID(int id){
		HelpQuestion q = null;
		
		for(HelpQuestion question : getUnansweredQuestions()){
			if(question.getEntryID() == id){
				q = question;
				break;
			}
		}
		
		return q;
	}
	
	public List<HelpQuestion> getUnansweredQuestions(){
		if (questionList == null || questionList.size() == 0) {
			try {
				questionList = getUnansweredQuestionsFromDB();
			} catch (SQLException se) {
				plugin.getLogger().log(Level.WARNING, "Unable to refresh question list from database", se);
				questionList = new ArrayList<>();
			}
		}
		return questionList;
	}
	
	private List<HelpQuestion> getUnansweredQuestionsFromDB() throws SQLException{
		PreparedStatement ps = db.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests WHERE reply IS NULL;");
		ps.execute();
		
		ResultSet results = ps.getResultSet();
		
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			try {
				unansweredQuestions.add(extractHelpQuestion(results));
			} catch (SQLException sqe) {
				plugin.getLogger().log(Level.WARNING, "Failure while extracting a help question:", sqe);
			}
		}
		return unansweredQuestions;
	}
	
	public HelpQuestion addQuestion(String askerUUID, String question) throws SQLException{
		PreparedStatement ps = db.prepareStatement("INSERT INTO help_requests(ask_time, asker_uuid, question, viewed) VALUES(?,?,?,?);");

		Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
		
		ps.setTimestamp(1, time);
		ps.setString(2, askerUUID);
		ps.setString(3, question);
		ps.setBoolean(4, false);

		ps.executeUpdate();
		
		ResultSet rs = ps.getGeneratedKeys();
		
		HelpQuestion helpQuestion = null;
		
		if (rs.next()) {
			helpQuestion = new HelpQuestion(rs.getInt(1), time, askerUUID, question);
		} else {
			EasyHelpOp.Log("Using fallback last insert id retrieval");
			ps = db.prepareStatement("SELECT LAST_INSERT_ID();");
			ps.execute();
			rs = ps.getResultSet();
			if (rs.next()) {
				helpQuestion = new HelpQuestion(rs.getInt(1), time, askerUUID, question);
			} else {
				throw new SQLException("Unable to retrieve last insert ID!");
			}
		}
		
		questionList.add(helpQuestion);
		
		return helpQuestion;
	}
	
	public boolean updateQuestion(HelpQuestion question) throws SQLException{
		PreparedStatement ps = db.prepareStatement("UPDATE help_requests SET ask_time=?, asker_uuid=?, question=?, reply_time=?, replier_uuid=?, reply=?, viewed=? WHERE help_id=?;");
		
		ps.setTimestamp(1, question.ask_time);
		ps.setString(2, question.asker_uuid);
		ps.setString(3, question.getQuestion());
		ps.setTimestamp(4, question.replyTime);
		ps.setString(5, question.replier_uuid);
		ps.setString(6, question.reply);
		ps.setBoolean(7, question.getViewed());
		ps.setInt(8, question.getEntryID());
		
		int rowsAffected = ps.executeUpdate();
		
		return rowsAffected > 0;
	}
	
	
}
