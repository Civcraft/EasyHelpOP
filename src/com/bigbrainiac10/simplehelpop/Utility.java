package com.bigbrainiac10.simplehelpop;

import java.util.logging.Level;

import org.bukkit.ChatColor;

public class Utility {

	public static String safeToColor(String str){
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
	public static String colorToSafe(String str){
		return str.replace(ChatColor.COLOR_CHAR, '&');
	}
	

	public static String loreWrap(String str){
		if(str.length() < 20){
			return str;
		}
		String[] wordList = str.split(" ");
		
		int nextCheck = 20;
		
		StringBuilder sb = new StringBuilder();
		for (String word : wordList){
			if(sb.length() > nextCheck){
				sb.append("\n");
				nextCheck += 20;
			}
				
			sb.append(word).append(" ");
		}
		 
		String wrappedString = sb.toString().trim();
		
		return wrappedString;
	}
	
}
