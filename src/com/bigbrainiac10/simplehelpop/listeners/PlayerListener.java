package com.bigbrainiac10.simplehelpop.listeners;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;

public class PlayerListener implements Listener {

	private SimpleHelpOp plugin = SimpleHelpOp.getInstance();

	@EventHandler(priority = EventPriority.NORMAL)
	public void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();

		List<HelpQuestion> qq = SimpleHelpOp.getHelpOPData().getUnviewedQuestions(player);

		final UUID p = player.getUniqueId();

		if (qq != null && qq.size() > 0) {
			final List<HelpQuestion> q = qq;

			new BukkitRunnable() {
				@Override
				public void run() {
					Player player = Bukkit.getPlayer(p);
					if (player == null)
						return;
					for (HelpQuestion question : q) {
						player.sendMessage(ChatColor.GOLD + "You asked: " + ChatColor.GRAY + question.getQuestion());
						player.sendMessage(ChatColor.GOLD + "A mod replied: " + ChatColor.GRAY + question.getReply());
						question.setViewed(true);
						SimpleHelpOp.getHelpOPData().updateQuestion(question);
					}
				}
			}.runTaskLater(plugin, 20l);
		}

		if (player.hasPermission("simplehelpop.replyhelp")) {
			List<HelpQuestion> u = SimpleHelpOp.getHelpOPData().getUnansweredQuestionsFromDB();
			if (u != null && u.size() > 0) {
				new BukkitRunnable() {

					@Override
					public void run() {
						Player player = Bukkit.getPlayer(p);
						if (player == null)
							return;
						player.sendMessage(ChatColor.AQUA + "There are unanswered questions from players. Run /viewhelp to answer them");
					}
				}.runTaskLater(plugin, 40l);
			}
		}
	}

}
