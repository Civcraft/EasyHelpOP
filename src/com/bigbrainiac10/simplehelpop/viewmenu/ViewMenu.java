package com.bigbrainiac10.simplehelpop.viewmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.ScheduledInventoryOpen;
import vg.civcraft.mc.civmodcore.itemHandling.ISUtils;
import vg.civcraft.mc.namelayer.NameAPI;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;

public class ViewMenu {

	private List<HelpQuestion> questions;
	private int pageNum = 1;
	private int pageNumMax;

	private String title;

	private Player player;

	public ViewMenu(List<HelpQuestion> questions, String title, Player player) {
		this.questions = questions;

		double roundedPages = Math.ceil((float) this.questions.size() / 45);
		this.pageNumMax = ((int) roundedPages > 0) ? (int) roundedPages : 1;

		this.title = title;

		this.player = player;

		openMenu();
	}

	public void openMenu() {
		ClickableInventory.forceCloseInventory(player);

		ClickableInventory menu = new ClickableInventory(54, title
				+ " - Page #" + pageNum);

		for (int i = (pageNum * 45) - 45; (i < pageNum * 45)
				&& (i < questions.size()); i++) {

			HelpQuestion question = questions.get(i);
			ItemStack is = new ItemStack(Material.EYE_OF_ENDER);
			ISUtils.setName(is, ChatColor.YELLOW + "Ticket " + question.getEntryID());
			String name;
			if (SimpleHelpOp.isNameLayerEnabled()) {
				name = NameAPI.getCurrentName(question.getAskedUUID());
			} else {
				name = Bukkit.getServer()
						.getOfflinePlayer(question.getAskedUUID()).getName();
			}
			ISUtils.addLore(is, ChatColor.GOLD + "Sent in by: " + name);
			ISUtils.addLore(is, ChatColor.GRAY + "Question:");

			for (String str : loreWrap(question.getQuestion()).split(
					"\n")) {
				ISUtils.addLore(is, ChatColor.GRAY + "" + ChatColor.ITALIC + str);
			}

			if (question.getReplierUUID() != null) {
				//question was already answered
				ISUtils.addLore(is, ChatColor.GRAY + "" + ChatColor.BOLD + "Reply:");
				for (String str : loreWrap(question.getReply()).split("\n")) {
					ISUtils.addLore(is, ChatColor.GRAY + "" + ChatColor.ITALIC + ""
							+ ChatColor.BOLD + str);
				}
			}
			else {
				//question is unanswered, so we add an enchant to mark that
				ItemMeta im = is.getItemMeta();
				im.addEnchant(Enchantment.DURABILITY, 1, true);
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				is.setItemMeta(im);
			}
			
			Clickable clickItem = new Clickable(is) {
				@Override
				public void clicked(Player p) {
					int id = Integer.parseInt(this.getItemStack().getItemMeta()
							.getDisplayName().split(" ")[1]);
					HelpQuestion q = SimpleHelpOp.getHelpOPData().getUnansweredByID(
							id);
					if (q.getReplierUUID() != null) {
						p.sendMessage(ChatColor.RED + "This question was already answered");
						return;
					}

					ConversationFactory cf = new ConversationFactory(SimpleHelpOp.getInstance());
					Conversation conv = cf
							.withFirstPrompt(
									new ReplyQuestionConversation(q, p))
							.withLocalEcho(true).withEscapeSequence("cancel")
							.withModality(false).buildConversation(p);
					conv.begin();

					ClickableInventory.forceCloseInventory(p);
				}
			};

			menu.addSlot(clickItem);
		}

		ItemStack backItem = new ItemStack(Material.ARROW);
		ISUtils.setLore(backItem, ChatColor.AQUA + "Back Page");
		ItemStack forwardItem = new ItemStack(Material.ARROW);
		ISUtils.setLore(forwardItem, ChatColor.AQUA + "Forward Page");
		ItemStack closeItem = new ItemStack(Material.BARRIER);
		ISUtils.setLore(closeItem, ChatColor.RED + "Close");

		Clickable backClick = new Clickable(backItem) {
			@Override
			public void clicked(Player p) {
				setPageNum(pageNum - 1);
				openMenu();
			}
		};

		Clickable forwardClick = new Clickable(forwardItem) {
			@Override
			public void clicked(Player p) {
				setPageNum(pageNum + 1);
				openMenu();
			}
		};

		Clickable closeClick = new Clickable(closeItem) {
			@Override
			public void clicked(Player p) {
				ClickableInventory.forceCloseInventory(player);
			}
		};

		menu.setSlot(backClick, 45);
		menu.setSlot(closeClick, 49);
		menu.setSlot(forwardClick, 53);

		ScheduledInventoryOpen.schedule(SimpleHelpOp.getInstance(), menu, player);
	}

	private void setPageNum(int num) {
		pageNum = Math.max(1, Math.min(pageNumMax, num));
		SimpleHelpOp.Log("Page Number: {0}", pageNum);
		SimpleHelpOp.Log("Max Page Number: {0}", pageNumMax);

	}

	private static String loreWrap(String str) {
		if (str.length() < 20) {
			return str;
		}
		String[] wordList = str.split(" ");

		int nextCheck = 20;

		StringBuilder sb = new StringBuilder();
		for (String word : wordList) {
			if (sb.length() > nextCheck) {
				sb.append("\n");
				nextCheck += 20;
			}

			sb.append(word).append(" ");
		}

		String wrappedString = sb.toString().trim();

		return wrappedString;
	}
}
