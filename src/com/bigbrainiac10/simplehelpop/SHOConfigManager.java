package com.bigbrainiac10.simplehelpop;

import org.bukkit.configuration.file.FileConfiguration;

public class SHOConfigManager {

	private static FileConfiguration config;
	
	public SHOConfigManager(FileConfiguration con){
		config = con;
	}
	
	public static String getHostName(){
		return config.getString("db.hostname");
	}
	
	public static String getUserName(){
		return config.getString("db.user");
	}
	
	public static String getPassword(){
		return config.getString("db.pass");
	}
	
	public static int getPort(){
		return config.getInt("db.port");
	}
	
	public static String getDBName(){
		return config.getString("db.dbname");
	}
	
	/*
	public static String getHelperAlert(){
		return config.getString("msg.helperAlert");
	}
	*/
	
	public static String getPlayerMessage(String configName){
		return config.getString("msg."+configName);
	}
	
	
	public static boolean getDebug(){
		return config.getBoolean("debug");
	}
	
}
