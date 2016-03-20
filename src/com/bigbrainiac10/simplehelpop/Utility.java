package com.bigbrainiac10.simplehelpop;

import org.bukkit.ChatColor;

public class Utility {

	public static String safeToColor(String str){
		return str.replace('&', ChatColor.COLOR_CHAR);
	}
	
	public static String colorToSafe(String str){
		return str.replace(ChatColor.COLOR_CHAR, '&');
	}
	
}
