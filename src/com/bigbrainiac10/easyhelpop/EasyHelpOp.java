package com.bigbrainiac10.easyhelpop;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.bigbrainiac10.easyhelpop.commands.HelpOPCommand;
import com.bigbrainiac10.easyhelpop.commands.ViewHelpCommand;
import com.bigbrainiac10.easyhelpop.database.Database;
import com.bigbrainiac10.easyhelpop.database.HelpOPData;
import com.bigbrainiac10.easyhelpop.listeners.PlayerListener;
import com.bigbrainiac10.easyhelpop.listeners.QuestionListener;

public class EasyHelpOp extends JavaPlugin{

	private static Database _db;
	private static EasyHelpOp _instance;
	private static Logger _logger;
	private static HelpOPData _helpData;
	
	public void onEnable(){
		_instance = this;
		_logger = this.getLogger();
		
		saveDefaultConfig();
		reloadConfig();
		new EHOConfigManager(getConfig());
		
		initializeDatabase();
		_helpData = new HelpOPData(_db);
		
		registerListeners();
		registerCommands();
	}
	
	public void onDisable(){
		_db.close();
	}
	
	public static void Log(String message){
		Log(Level.INFO, message);
	}

	public static void Log(String message, Object...vars){
		Log(Level.INFO, message, vars);
	}

	public static void Log(Level level, String message, Object...vars){
		_logger.log(level, message, vars);
	}

	public Database getDB(){
		return _db;
	}
	
	public static EasyHelpOp getInstance(){
		return _instance;
	}
	
	public HelpOPData getHelpOPData(){
		return _helpData;
	}
	
	private void initializeDatabase(){
		String host = EHOConfigManager.getHostName();
		String user = EHOConfigManager.getUserName();
		String password = EHOConfigManager.getPassword();
		int port = EHOConfigManager.getPort();
		String dbName = EHOConfigManager.getDBName();
		
		_db = new Database(host, port, dbName, user, password, getLogger());
	}
	
	private void registerListeners(){
		_instance.getServer().getPluginManager().registerEvents(new QuestionListener(), _instance);
		_instance.getServer().getPluginManager().registerEvents(new PlayerListener(), _instance);
	}
	
	private void registerCommands(){
		_instance.getCommand("helpop").setExecutor(new HelpOPCommand(this));
		_instance.getCommand("viewhelp").setExecutor(new ViewHelpCommand(this));
	}
	
}
