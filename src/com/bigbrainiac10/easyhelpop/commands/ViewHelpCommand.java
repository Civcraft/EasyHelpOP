package com.bigbrainiac10.easyhelpop.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
			return onOpCommand(sender, args);
		}

		Player player = (Player) sender;

		if (args.length == 0) {
			new ViewMenu(plugin.getHelpOPData().getUnansweredQuestions(), " Viewer", player,
					ViewType.UNANSWERED);
			return true;
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("all")) {
				List<HelpQuestion> aq = null;

				try {
					aq = plugin.getHelpOPData().getAllQuestions();
				} catch (SQLException e) {
					plugin.getLogger().log(Level.WARNING, "Failed to find any questions, DB error", e);
					player.sendMessage("Database Failure, could not retrieve questions.");
					return true;
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
						} else {
							plugin.getLogger().log(Level.INFO, "Failed to find question id: {0}", args[1]);
							player.sendMessage(idUnknown);
						}
						return true;
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
						} else {
							plugin.getLogger().log(Level.INFO, "Failed to find questions by player: {0}", args[1]);
							player.sendMessage(playerUnknown);
						}
						return true;
					} catch (SQLException e) {
						plugin.getLogger().log(Level.SEVERE, "Failed to view questions by player", e);
						player.sendMessage(generalFailure);
					}
				}
			}
		}

		return false;
	}

	private boolean onOpCommand(CommandSender sender, String[] args) {
		List<HelpQuestion> aq = null;
		if (args.length == 0) {
			aq = plugin.getHelpOPData().getUnansweredQuestions();
					
			sender.sendMessage(ViewType.UNANSWERED + " Question List");
		} else if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("all")) {
				try {
					aq = plugin.getHelpOPData().getAllQuestions();
				} catch (SQLException e) {
					plugin.getLogger().log(Level.WARNING, "Failed to find any questions, DB error", e);
					sender.sendMessage("Database Failure, could not retrieve questions.");
					return true;
				}

				sender.sendMessage(ViewType.ALL + " Question List");
			} else if (args.length >= 2) {
				if (args[0].equalsIgnoreCase("id")) {
					try {
						HelpQuestion hq = plugin.getHelpOPData().getUnansweredByID(Integer.parseInt(args[1]));
						if (hq != null) {
							aq = new ArrayList<HelpQuestion>();
							aq.add(hq);
						}
						
						if (aq != null) {
							sender.sendMessage(ViewType.ID + " " + args[1] + " Question List");
						} else {
							plugin.getLogger().log(Level.INFO, "Failed to find question id: {0}", args[1]);
							sender.sendMessage(idUnknown);
							return true;
						}
					} catch (NumberFormatException e) {
						plugin.getLogger().log(Level.SEVERE, "Failed to view question by id", e);
						sender.sendMessage(generalFailure);
						return true;
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
							sender.sendMessage(ViewType.NAME + " " + args[1] + " Question List");
						} else {
							plugin.getLogger().log(Level.INFO, "Failed to find questions by player: {0}", args[1]);
							sender.sendMessage(playerUnknown);
							return true;
						}
					} catch (SQLException e) {
						plugin.getLogger().log(Level.SEVERE, "Failed to view questions by player", e);
						sender.sendMessage(generalFailure);
					}
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
		
		if (aq == null) {
			sender.sendMessage("  Nothing found.");
		} else {
			for (HelpQuestion question : aq) {
				StringBuffer qq = new StringBuffer("  ");
				qq.append(question.getEntryID());
				qq.append(". ");
				qq.append(Bukkit.getServer().getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName());
				qq.append(" Asked \"");
				qq.append(ChatColor.WHITE);
				qq.append(question.getQuestion());
				qq.append(ChatColor.RESET);
				qq.append("\" ");
				if(!(question.replier_uuid == null)){
					qq.append("Reply: ");
					qq.append(ChatColor.WHITE);
					qq.append(question.reply);
					qq.append(ChatColor.RESET);
				}
				sender.sendMessage(qq.toString());
			}
		}
		return true;
	}
}
