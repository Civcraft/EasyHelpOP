package com.bigbrainiac10.simplehelpop;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.bigbrainiac10.simplehelpop.database.Database;
import com.bigbrainiac10.simplehelpop.database.HelpOPData;

public class HelpOPManager {

	private HelpOPData helpData;
	private Database db;
	private SimpleHelpOp plugin;
	
	private List<HelpQuestion> questionList;
	
	private int lastHelpID;
	
	public HelpOPManager(Database db){
		this.helpData = new HelpOPData(db);
		this.db = db;
		this.plugin = SimpleHelpOp.getInstance();
		
		this.questionList = new ArrayList<HelpQuestion>();
		try {
			this.lastHelpID = getLastID();
		} catch(SQLException e){
			plugin.getLogger().log(Level.SEVERE, "Wasn't able to get the ", e);
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}
	
	private int getLastID() throws SQLException{
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
	
	public boolean createQuestion(String asker_uuid, String question){
		
		
		lastHelpID++;
		
		return true;
	}
	
}
