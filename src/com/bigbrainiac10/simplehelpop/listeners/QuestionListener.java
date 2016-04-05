package com.bigbrainiac10.simplehelpop.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import vg.civcraft.mc.namelayer.NameAPI;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.events.QuestionAnsweredEvent;
import com.bigbrainiac10.simplehelpop.events.QuestionCreatedEvent;

public class QuestionListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void questionCreated(QuestionCreatedEvent event) {
		HelpQuestion question = event.getQuestion();
		String name;
		if (SimpleHelpOp.isNameLayerEnabled()) {
			name = NameAPI.getCurrentName(question.getAskedUUID());
		} else {
			name = Bukkit.getServer().getOfflinePlayer(question.getAskedUUID())
					.getName();
		}
		for (Player player : Bukkit.getServer().getOnlinePlayers()) {
			if (player.hasPermission("simplehelpop.replyhelp")) {
				player.sendMessage(ChatColor.DARK_AQUA + name
						+ " asked a question, run /viewhelp to answer it");
			}
		}

		SimpleHelpOp.Log("Player " + name + " requested help. (Help ID "
				+ question.getEntryID() + ")");
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void questionAnswered(QuestionAnsweredEvent event) {
		HelpQuestion question = event.getQuestion();
		Player p = Bukkit.getPlayer(question.getAskedUUID());
		if (p != null) {
			p.sendMessage(ChatColor.GOLD + "You asked: " + ChatColor.GRAY
					+ question.getQuestion());
			p.sendMessage(ChatColor.GOLD + "A mod replied: "
					+ ChatColor.GRAY + question.getReply());
			question.setViewed(true);
			SimpleHelpOp.getHelpOPData().updateQuestion(question);

		}

	}

}
