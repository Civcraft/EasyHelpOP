package com.bigbrainiac10.simplehelpop.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.viewmenu.ViewMenu;
import com.bigbrainiac10.simplehelpop.viewmenu.ViewType;

public class ViewHelpCommand implements CommandExecutor {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))
			return false;
		
		Player player = (Player)sender;
		
		if(args.length == 0){
			new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), "Question Viewer", player, ViewType.UNANSWERED);
			return true;
		}else if(args.length >= 1){
			if(args[0].equalsIgnoreCase("all")){
				List<HelpQuestion> aq = null;
				
				try {
					aq = plugin.getHelpOPData().getAllQuestions();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				new ViewMenu(aq, "Question Viewer" , player, ViewType.ALL);
				return true;
			}else if(args.length >= 2) {
				// TODO refactor etc.
				List<HelpQuestion> aq = null;
				if (args[0].equalsIgnoreCase("id")){
					try {
						HelpQuestion hq = plugin.getHelpOPData().getUnansweredByID(Integer.parseInt(args[1]));
						if (hq != null) {
							aq = new ArrayList<HelpQuestion>();
							aq.add(hq);
						}
						if (aq != null) {
							new ViewMenu(aq, "Question Viewer", player, ViewType.ID);
							return true;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}else if(args[0].equalsIgnoreCase("name")){
					try {
						Player pq = Bukkit.getPlayer(args[1]);
						if (pq != null) {
							aq = plugin.getHelpOPData().getUnviewedQuestions(pq);
						}
						if (aq != null) {
							new ViewMenu(aq, "Question Viewer", player, ViewType.NAME);
							return true;
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return false;
	}

}
