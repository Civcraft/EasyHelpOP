package com.bigbrainiac10.simplehelpop.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;

public class DatabaseManager {
	private Database db;
	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();

	public DatabaseManager(Database db) {
		this.db = db;
		if (db.connect()) {
			createTables();
		}
	}

	private void createTables() {
		db.execute("CREATE TABLE IF NOT EXISTS help_requests("
				+ "help_id int NOT NULL AUTO_INCREMENT,"
				+ "ask_time timestamp NOT NULL,"
				+ "asker_uuid varchar(36) NOT NULL,"
				+ "question text NOT NULL,"
				+ "reply_time timestamp NULL DEFAULT NULL,"
				+ "replier_uuid varchar(36) DEFAULT NULL,"
				+ "reply text DEFAULT NULL," + "viewed bool NOT NULL,"
				+ "PRIMARY KEY(help_id));");
	}

	public HelpQuestion extractHelpQuestion(ResultSet results)
			throws SQLException {
		int helpID = results.getInt(1);
		Timestamp askTime = results.getTimestamp(2, Calendar.getInstance());
		UUID askerUUID = UUID.fromString(results.getString(3));
		String question = results.getString(4);
		Timestamp replyTime = results.getTimestamp(5, Calendar.getInstance());
		UUID replier_uuid = UUID.fromString(results.getString(6));
		String reply = results.getString(7);
		boolean viewed = results.getBoolean(8);

		return new HelpQuestion(helpID, askTime, askerUUID, question,
				replyTime, replier_uuid, reply, viewed);
	}

	public List<HelpQuestion> getUnviewedQuestions(Player player) {
		if (player == null) {
			SimpleHelpOp.Log(Level.WARNING,
					"Unable to get unviewed questions for a null player");
			return new ArrayList<HelpQuestion>();
		}
		List<HelpQuestion> unviewedQuestions = new ArrayList<HelpQuestion>();
		PreparedStatement ps = db
				.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests WHERE asker_uuid=? AND reply IS NOT NULL AND viewed=False;");
		try {
			ps.setString(1, player.getUniqueId().toString());

			ResultSet results = ps.executeQuery();

			while (results.next()) {
				unviewedQuestions.add(extractHelpQuestion(results));
			}
		} catch (SQLException e) {
			plugin.getLogger().log(
					Level.WARNING,
					"Failure while getting unviewed questions for "
							+ player.getName());
			e.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {

			}
		}
		return unviewedQuestions;
	}

	public List<HelpQuestion> getUnansweredQuestions(UUID player)
			throws SQLException {
		if (player == null) {
			SimpleHelpOp.Log(Level.WARNING,
					"Unable to get unanswered questions for a null player");
			return new ArrayList<HelpQuestion>();
		}
		PreparedStatement ps = db
				.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests WHERE asker_uuid=? AND reply IS NULL;");

		ps.setString(1, player.toString());

		ResultSet results = ps.executeQuery();

		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();

		while (results.next()) {
			try {
				unansweredQuestions.add(extractHelpQuestion(results));
			} catch (SQLException sqe) {
				plugin.getLogger().log(Level.WARNING,
						"Failure while extracting a help question:", sqe);
			} finally {
				try {
					ps.close();
				} catch (Exception ex) {

				}
			}
		}
		return unansweredQuestions;
	}

	public List<HelpQuestion> getAllQuestions() {
		PreparedStatement ps = db
				.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests;");
		List<HelpQuestion> allQuestions = new ArrayList<HelpQuestion>();
		try {
			ResultSet results = ps.executeQuery();

			while (results.next()) {
				allQuestions.add(extractHelpQuestion(results));
			}
		} catch (SQLException sqe) {
			plugin.getLogger().log(Level.WARNING,
					"Failure while extracting a help question:", sqe);
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {

			}
		}
		return allQuestions;
	}

	public List<HelpQuestion> getUnansweredQuestionsFromDB() {
		PreparedStatement ps = db
				.prepareStatement("SELECT help_id, ask_time, asker_uuid, question, reply_time, replier_uuid, reply, viewed FROM help_requests WHERE reply IS NULL;");
		List<HelpQuestion> unansweredQuestions = new ArrayList<HelpQuestion>();
		try {
			ps.execute();

			ResultSet results = ps.getResultSet();

			while (results.next()) {
				unansweredQuestions.add(extractHelpQuestion(results));
			}
		} catch (SQLException sqe) {
			plugin.getLogger().log(Level.WARNING,
					"Failure while extracting a help question:", sqe);
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {

			}
		}
		return unansweredQuestions;
	}

	public HelpQuestion addQuestion(UUID askerUUID, String question) {
		PreparedStatement ps = db
				.prepareStatement("INSERT INTO help_requests(ask_time, asker_uuid, question, viewed) VALUES(?,?,?,?);");

		Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
		HelpQuestion helpQuestion = null;
		try {
			ps.setTimestamp(1, time);
			ps.setString(2, askerUUID.toString());
			ps.setString(3, question);
			ps.setBoolean(4, false);

			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();

			if (rs.next()) {
				helpQuestion = new HelpQuestion(rs.getInt(1), time, askerUUID,
						question);
			} else {
				SimpleHelpOp.Log("Using fallback last insert id retrieval");
				ps = db.prepareStatement("SELECT LAST_INSERT_ID();");
				ps.execute();
				rs = ps.getResultSet();
				if (rs.next()) {
					helpQuestion = new HelpQuestion(rs.getInt(1), time,
							askerUUID, question);
				} else {
					SimpleHelpOp.Log(Level.SEVERE,
							"Could not add question to database, data:", time,
							askerUUID, question);
					return null;
				}
			}
		} catch (SQLException e) {
			SimpleHelpOp.Log(Level.SEVERE,
					"Error while adding question to database, data:", time,
					askerUUID, question);
			e.printStackTrace();
			return null;
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {

			}
		}
		return helpQuestion;
	}

	public boolean updateQuestion(HelpQuestion question) {
		PreparedStatement ps = db
				.prepareStatement("UPDATE help_requests SET ask_time=?, asker_uuid=?, question=?, reply_time=?, replier_uuid=?, reply=?, viewed=? WHERE help_id=?;");
		int rowsAffected = 0;
		try {
			ps.setTimestamp(1, question.getAskTime());
			ps.setString(2, question.getAskedUUID().toString());
			ps.setString(3, question.getQuestion());
			ps.setTimestamp(4, question.getReplyTime());
			ps.setString(5, question.getReplierUUID().toString());
			ps.setString(6, question.getReply());
			ps.setBoolean(7, question.getViewed());
			ps.setInt(8, question.getEntryID());

			rowsAffected = ps.executeUpdate();
		} catch (SQLException e) {
			SimpleHelpOp.Log(Level.SEVERE, "Error while updating question "
					+ question.getEntryID());
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
			} catch (Exception ex) {

			}
		}
		return rowsAffected > 0;
	}
}
