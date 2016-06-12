package com.bigbrainiac10.easyhelpop.viewmenu;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.ScheduledInventoryOpen;

import com.bigbrainiac10.easyhelpop.HelpQuestion;
import com.bigbrainiac10.easyhelpop.EasyHelpOp;
import com.bigbrainiac10.easyhelpop.Utility;

public class ViewMenu{

	private final EasyHelpOp plugin = EasyHelpOp.getInstance();
	
	private List<HelpQuestion> questions;
	
	private int pageNum = 1;
	private int pageNumMax;
	
	private String title;
	
	private Player player;
	
	private ViewType viewType;
	
	public ViewMenu(List<HelpQuestion> questions, String title, Player player, ViewType viewType){
		this.questions = questions;
		
		double roundedPages = Math.ceil((float)this.questions.size()/45);
		this.pageNumMax = ((int)roundedPages > 0) ? (int)roundedPages : 1;
		
		this.title = title;
		
		this.player = player;
		
		this.viewType = viewType;
	
		openMenu();
	}
	
	public void openMenu(){
		ClickableInventory.forceCloseInventory(player);
		
		ClickableInventory menu = new ClickableInventory(54, title+" - Page #"+pageNum);
		
		for(int i=(pageNum*45)-45; i<pageNum*45; i++){
			if(i > questions.size()-1)
				break;
			
			HelpQuestion question = questions.get(i);
			
			List<String> lore = new ArrayList<String>();
			
			lore.add("Sent in by: "+Bukkit.getServer().getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName());
			lore.add(ChatColor.GRAY + "Question:");
			
			for(String str : Utility.loreWrap(question.getQuestion()).split("\n")){
				lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + str);
			}
			
			if(!(question.replier_uuid == null)){
				lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Reply:");
				for(String str : Utility.loreWrap(question.reply).split("\n")){
					lore.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "" + ChatColor.BOLD + str);
				}
			}
			
			ItemStack item = createItem(Material.BOOK, 
					"Question ID: "+question.getEntryID(),
					lore);
			
			if (question.replier_uuid != null) {
				item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
			}
			
			Clickable clickItem = new Clickable(item){
				@Override
				public void clicked(Player p) {
					int id = Integer.parseInt(this.getItemStack().getItemMeta().getDisplayName().split(" ")[2]);
					HelpQuestion q = plugin.getHelpOPData().getUnansweredByID(id);
					
					if (q == null || q.replier_uuid != null) {
						return;
					}
					
					ConversationFactory cf = new ConversationFactory(plugin);
					Conversation conv = cf.withFirstPrompt(new ReplyQuestionConversation(q, p))
								.withLocalEcho(true)
								.withEscapeSequence("cancel")
								.withModality(false)
								.buildConversation(p);
					conv.begin();
					
					ClickableInventory.forceCloseInventory(p);
				}
			};
			
			menu.addSlot(clickItem);
		}
		
		ItemStack backItem = createItem(Material.ARROW, "Back Page", null);
		ItemStack forwardItem = createItem(Material.ARROW, "Forward Page", null);
		ItemStack closeItem = createItem(Material.BARRIER, "Close", null);
		
		Clickable backClick = new Clickable(backItem){
			@Override
			public void clicked(Player p){
				setPageNum(pageNum-1);
				openMenu();
			}
		};
		
		Clickable forwardClick = new Clickable(forwardItem){
			@Override
			public void clicked(Player p){
				setPageNum(pageNum+1);
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
		
		ScheduledInventoryOpen.schedule(plugin, menu, player);
	}
	
	private ItemStack createItem(Material material, String title, List<String> lore){
		ItemStack item = new ItemStack(material);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(title);
		
		if(lore != null)
			meta.setLore(lore);
			
		item.setItemMeta(meta);
		
		return item;
	}
	
	public void setPageNum(int num){
		pageNum = Math.max(1, Math.min(pageNumMax, num));
		EasyHelpOp.Log("Page Number: {0}", pageNum);
		EasyHelpOp.Log("Max Page Number: {0}", pageNumMax);
		
	}
	}
