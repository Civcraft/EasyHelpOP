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
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SHOConfigManager;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class HelpOPData {
	private Database db;
	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
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
				+ "reply_time timestamp,"
				+ "replier_uuid varchar(36),"
				+ "reply text,"
				+ "viewed bool NOT NULL,"
				+ "PRIMARY KEY(help_id));");
	}
	
	public HelpQuestion extractHelpQuestion(ResultSet results) throws SQLException {
		int helpID = results.getInt("help_id");
		Timestamp askTime = results.getTimestamp("ask_time");
		String askerUUID = results.getString("asker_uuid");
		String question = results.getString("question");
		Timestamp replyTime = results.getTimestamp("reply_time");
		String replier_uuid = results.getString("replier_uuid");
		String reply = results.getString("reply");
		boolean viewed = results.getBoolean("viewed");
		
		return new HelpQuestion(helpID, askTime, askerUUID, question, replyTime, replier_uuid, reply, viewed);
	}
	
	public List<HelpQuestion> getUnviewedQuestions(Player player) throws SQLException{
		PreparedStatement ps = db.prepareStatement("SELECT * FROM help_requests WHERE asker_uuid=? AND viewed=False;");
		
		ps.setString(1, player.getUniqueId().toString());
		
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			unansweredQuestions.add(extractHelpQuestion(results));
		}
		return unansweredQuestions;
	}
	
	public List<HelpQuestion> getAllQuestions() throws SQLException{
		PreparedStatement ps = db.prepareStatement("SELECT * FROM help_requests;");
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			unansweredQuestions.add(extractHelpQuestion(results));
		}
		return unansweredQuestions;
	}
	
	public void removeUnansweredQuestion(HelpQuestion question){
		for(HelpQuestion q : questionList){
			if(q.getEntryID() == question.getEntryID()){
				questionList.remove(q);
				break;
			}
		}
	}
	
	public HelpQuestion getUnansweredByID(int id){
		HelpQuestion q = null;
		
		for(HelpQuestion question : questionList){
			if(question.getEntryID() == id){
				q = question;
				break;
			}
		}
		
		return q;
	}
	
	public List<HelpQuestion> getUnansweredQuestions(){
		return questionList;
	}
	
	private List<HelpQuestion> getUnansweredQuestionsFromDB() throws SQLException{
		PreparedStatement ps = db.prepareStatement("SELECT * FROM help_requests WHERE reply IS NULL;");
		ResultSet results = ps.executeQuery();
		
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		
		while(results.next()){
			unansweredQuestions.add(extractHelpQuestion(results));
		}
		return unansweredQuestions;
	}
	
	public HelpQuestion addQuestion(String askerUUID, String question) throws SQLException{
		PreparedStatement ps = db.prepareStatement("INSERT INTO help_requests(ask_time, asker_uuid, question, viewed) VALUES(?,?,?,?);");

		Timestamp time = Timestamp.from(Instant.now());
		
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
			SimpleHelpOp.Log("Using fallback last insert id retrieval");
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
