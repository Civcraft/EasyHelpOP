package com.bigbrainiac10.simplehelpop;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.bigbrainiac10.simplehelpop.database.Database;

public class SimpleHelpOp extends JavaPlugin{

	private static Database _db;
	private static SimpleHelpOp _instance;
	private static Logger _logger;
	
	public void onEnable(){
		_instance = this;
		_logger = this.getLogger();
		
		saveDefaultConfig();
		reloadConfig();
		new SHOConfigManager(getConfig());
		
		initializeDatabase();
		
		registerListeners();
		registerCommands();
	}
	
	public void onDisable(){
		_db.close();
	}
	
	public static void Log(String message){
		_logger.log(Level.INFO, "[SimpleHelpOp] " + message);
	}
	
	public static Database getDB(){
		return _db;
	}
	
	public static SimpleHelpOp getInstance(){
		return _instance;
	}
	
	private void initializeDatabase(){
		String host = SHOConfigManager.getHostName();
		String user = SHOConfigManager.getUserName();
		String password = SHOConfigManager.getPassword();
		int port = SHOConfigManager.getPort();
		String dbName = SHOConfigManager.getDBName();
		
		_db = new Database(host, port, dbName, user, password, getLogger());
	}
	
	private void registerListeners(){
		
	}
	
	private void registerCommands(){
		
	}
	
}
