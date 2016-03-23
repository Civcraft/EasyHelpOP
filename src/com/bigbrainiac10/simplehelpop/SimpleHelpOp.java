package com.bigbrainiac10.simplehelpop;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.bigbrainiac10.simplehelpop.commands.HelpOPCommand;
import com.bigbrainiac10.simplehelpop.commands.ViewHelpCommand;
import com.bigbrainiac10.simplehelpop.database.Database;
import com.bigbrainiac10.simplehelpop.database.HelpOPData;
import com.bigbrainiac10.simplehelpop.listeners.PlayerListener;
import com.bigbrainiac10.simplehelpop.listeners.QuestionListener;

public class SimpleHelpOp extends JavaPlugin{

	private static Database _db;
	private static SimpleHelpOp _instance;
	private static Logger _logger;
	private static HelpOPData _helpData;
	
	public void onEnable(){
		_instance = this;
		_logger = this.getLogger();
		
		saveDefaultConfig();
		reloadConfig();
		new SHOConfigManager(getConfig());
		
		initializeDatabase();
		_helpData = new HelpOPData(_db);
		
		registerListeners();
		registerCommands();
	}
	
	public void onDisable(){
		_db.close();
	}
	
	public static void Log(String message){
		_logger.log(Level.INFO, "[SimpleHelpOp] " + message);
	}
	
	public Database getDB(){
		return _db;
	}
	
	public static SimpleHelpOp getInstance(){
		return _instance;
	}
	
	public HelpOPData getHelpOPData(){
		return _helpData;
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
		_instance.getServer().getPluginManager().registerEvents(new QuestionListener(), _instance);
		_instance.getServer().getPluginManager().registerEvents(new PlayerListener(), _instance);
	}
	
	private void registerCommands(){
		_instance.getCommand("helpop").setExecutor(new HelpOPCommand());
		_instance.getCommand("viewhelp").setExecutor(new ViewHelpCommand());
	}
	
}
