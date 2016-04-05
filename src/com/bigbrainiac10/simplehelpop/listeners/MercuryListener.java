package com.bigbrainiac10.simplehelpop.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;

import vg.civcraft.mc.mercury.events.AsyncPluginBroadcastMessageEvent;

public class MercuryListener implements Listener {

	@EventHandler
	public void asyncBroadCast(AsyncPluginBroadcastMessageEvent e) {
		if (!e.getChannel().equals("SimpleHelpOp")) {
			return;
		}
		String[] data = e.getMessage().split("\\|");
		if (data[0].equals("question")) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.hasPermission("simplehelpop.replyhelp")) {
					p.sendMessage(ChatColor.DARK_AQUA + data[1]
							+ " asked a question, run /viewhelp to answer it");
				}
			}
		} else {
			// assume it's an answer
			Player p = Bukkit.getPlayer(UUID.fromString(data[1]));
			if (p != null) {
				List<HelpQuestion> qq = SimpleHelpOp.getHelpOPData()
						.getUnviewedQuestions(p);
				if (qq != null && qq.size() > 0) {
					for (HelpQuestion question : qq) {
						p.sendMessage(ChatColor.GOLD + "You asked: "
								+ ChatColor.GRAY + question.getQuestion());
						p.sendMessage(ChatColor.GOLD + "A mod replied: "
								+ ChatColor.GRAY + question.getReply());
						question.setViewed(true);
						SimpleHelpOp.getHelpOPData().updateQuestion(question);
					}
				}
			}
		}
	}

}
