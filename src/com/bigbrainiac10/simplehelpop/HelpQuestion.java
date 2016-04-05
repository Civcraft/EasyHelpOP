package com.bigbrainiac10.simplehelpop;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Represents a question asked by a player, it's answer if one exists and all
 * data associated with this question
 *
 */
public class HelpQuestion {

	private int entryID;
	private Timestamp ask_time;
	private UUID asker_uuid;
	private String question;
	private Timestamp replyTime;
	private UUID replier_uuid;
	private String reply;

	private boolean viewed;

	public HelpQuestion(int entryID, Timestamp ask_time, UUID asker_uuid,
			String question, Timestamp replyTime, UUID replier_uuid,
			String reply, boolean viewed) {
		this.entryID = entryID;
		this.ask_time = ask_time;
		this.asker_uuid = asker_uuid;
		this.question = question;
		this.replyTime = replyTime;
		this.replier_uuid = replier_uuid;
		this.reply = reply;
		this.viewed = viewed;
	}

	public HelpQuestion(int entryID, Timestamp ask_time, UUID asker_uuid,
			String question) {
		this.entryID = entryID;
		this.ask_time = ask_time;
		this.asker_uuid = asker_uuid;
		this.question = question;
		this.replyTime = null;
		this.replier_uuid = null;
		this.reply = null;
		this.viewed = false;
	}

	public void setViewed(boolean viewed) {
		this.viewed = viewed;
	}

	public boolean getViewed() {
		return this.viewed;
	}

	/**
	 * @return The question asked
	 */
	public String getQuestion() {
		return this.question;
	}

	/**
	 * @return Entry id of this question with which it is uniquely identifed
	 */
	public int getEntryID() {
		return this.entryID;
	}

	/**
	 * @return Time at which the question was asked
	 */
	public Timestamp getAskTime() {
		return ask_time;
	}

	/**
	 * @return Player who asked the question
	 */
	public UUID getAskedUUID() {
		return asker_uuid;
	}

	/**
	 * @return Time at which the question was answered or null if it wasnt
	 *         answered yet
	 */
	public Timestamp getReplyTime() {
		return replyTime;
	}

	/**
	 * @return OP who answered the question or null if it wasnt answered yet
	 */
	public UUID getReplierUUID() {
		return replier_uuid;
	}

	/**
	 * @return OP reply to the question or null if no reply was given yet
	 */
	public String getReply() {
		return reply;
	}
	
	/**
	 * Sets the reply to this question
	 * @param New reply
	 */
	public void setReply(String reply) {
		this.reply = reply;
	}
	
	/**
	 * Sets the replies to this question
	 * @param UUID of the replier
	 */
	public void setReplier(UUID replier) {
		this.replier_uuid = replier;
	}
	
	/**
	 * Sets the time at which this question was answered
	 * @param timestamp at which the question was answered
	 */
	public void setReplyTime(Timestamp time) {
		this.replyTime = time;
	}
}
