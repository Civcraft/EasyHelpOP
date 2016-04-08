package com.bigbrainiac10.easyhelpop.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.EHOConfigManager;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.Utility;
import com.bigbrainiac10.easyhelpop.viewmenu.ViewMenu;
import com.bigbrainiac10.easyhelpop.viewmenu.ViewType;

public class ViewHelpCommand implements CommandExecutor {

	private final EasyHelpOp plugin;
	private final String playerUnknown = Utility.safeToColor(EHOConfigManager.getPlayerMessage("playerUnknown"));
	private final String idUnknown = Utility.safeToColor(EHOConfigManager.getPlayerMessage("idUnknown"));
	private final String generalFailure = Utility.safeToColor(EHOConfigManager.getPlayerMessage("generalFailure"));

	public ViewHelpCommand(EasyHelpOp plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This one's for in-game only, sorry bud.");
			return false;
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), "Question Viewer", player,
					ViewType.UNANSWERED);
			return true;
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("all")) {
				List<HelpQuestion> aq = null;

				try {
					aq = plugin.getHelpOPData().getAllQuestions();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				new ViewMenu(aq, "Question Viewer", player, ViewType.ALL);
				return true;
			} else if (args.length >= 2) {
				List<HelpQuestion> aq = null;
				if (args[0].equalsIgnoreCase("id")) {
					try {
						HelpQuestion hq = plugin.getHelpOPData().getUnansweredByID(Integer.parseInt(args[1]));
						if (hq != null) {
							aq = new ArrayList<HelpQuestion>();
							aq.add(hq);
						}
						if (aq != null) {
							new ViewMenu(aq, "Question Viewer", player, ViewType.ID);
							return true;
						} else {
							player.sendMessage(idUnknown);
						}
					} catch (NumberFormatException e) {
						plugin.getLogger().log(Level.SEVERE, "Failed to view question by id", e);
						player.sendMessage(generalFailure);
					}
				} else if (args[0].equalsIgnoreCase("name")) {
					try {
						@SuppressWarnings("deprecation")
						OfflinePlayer pq = Bukkit.getOfflinePlayer(args[1]);
						if (pq != null) {
							aq = plugin.getHelpOPData().getUnansweredQuestions(pq.getUniqueId());
						} else {
							aq = null;
						}
						if (aq != null) {
							new ViewMenu(aq, "Question Viewer", player, ViewType.NAME);
							return true;
						} else {
							player.sendMessage(playerUnknown);
						}
					} catch (SQLException e) {
						plugin.getLogger().log(Level.SEVERE, "Failed to view questions by player", e);
						player.sendMessage(generalFailure);
					}
				}
			}
		}

		return false;
	}
}
