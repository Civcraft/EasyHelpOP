package com.bigbrainiac10.easyhelpop;

import org.bukkit.configuration.file.FileConfiguration;

public class EHOConfigManager {

	private static FileConfiguration config;
	
	public EHOConfigManager(FileConfiguration con){
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
	
	public static String getPlayerMessage(String configName){
		return config.getString("msg."+configName);
	}
	
	public static boolean getDebug(){
		return config.getBoolean("debug");
	}

	public static long getReservationTimeout() {
		return config.getLong("reservationTimeout", 60000l);
	}
}
