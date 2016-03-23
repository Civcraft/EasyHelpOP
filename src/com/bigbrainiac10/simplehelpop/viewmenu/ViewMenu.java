package com.bigbrainiac10.simplehelpop.viewmenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import vg.civcraft.mc.civmodcore.inventorygui.Clickable;
import vg.civcraft.mc.civmodcore.inventorygui.ClickableInventory;
import vg.civcraft.mc.civmodcore.inventorygui.ScheduledInventoryOpen;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;
import com.bigbrainiac10.simplehelpop.Utility;

public class ViewMenu{

	private final SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	private List<HelpQuestion> questions;
	
	//private Inventory inv;
	
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
		
		//this.inv = generateInventory();
		
		openMenu();
		
		//Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
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
			
			/*ItemStack item = createItem(Material.BOOK, 
					"Question ID: "+question.getEntryID(), 
					Arrays.asList("Sent in by: "+Bukkit.getServer().getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName(), 
							ChatColor.GRAY + "Question:", 
							ChatColor.GRAY + "" + ChatColor.ITALIC + WordUtils.wrap(question.getQuestion(), 20, "\n", false)));*/
			
			if(!(question.replier_uuid == null))
				item.addEnchantment(Enchantment.DURABILITY, 1);
			
			Clickable clickItem = new Clickable(item){
				@Override
				public void clicked(Player p) {
					int id = Integer.parseInt(this.getItemStack().getItemMeta().getDisplayName().split(" ")[2]);
					HelpQuestion q = plugin.getHelpOPData().getUnansweredByID(id);
					
					if(!(q.replier_uuid == null))
						return;
					
					ConversationFactory cf = new ConversationFactory(plugin);
					Conversation conv = cf.withFirstPrompt(new ReplyQuestionConversation(q, p)).withLocalEcho(true).withEscapeSequence("cancel").buildConversation(p);
					conv.begin();
					
					ClickableInventory.forceCloseInventory(p);
				}
			};
			
			menu.addSlot(clickItem);
			
		}
		
		ItemStack backItem = createItem(Material.ARROW, "Back Page", null);
		ItemStack forwardItem = createItem(Material.ARROW, "Forward Page", null);
		
		Clickable backClick = new Clickable(backItem){
			@Override
			public void clicked(Player p){
				setPageNum(pageNum-1);
				openMenu();
			}
		};
		
		Clickable forwardClick = new Clickable(backItem){
			@Override
			public void clicked(Player p){
				setPageNum(pageNum+1);
				openMenu();
			}
		};
		
		menu.setSlot(backClick, 45);
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
		plugin.Log("Page Number: "+pageNum);
		plugin.Log("Max Page Number: "+pageNumMax);
		
	}
	
	/*
	public void showInventory(){
		player.openInventory(inv);
	}
	
	public void setPageNum(int num){
		pageNum = Math.max(1, Math.min(pageNumMax, num));
		plugin.Log("Page Number: "+pageNum);
		plugin.Log("Max Page Number: "+pageNumMax);
		
	}
	
	public Inventory generateInventory(){
		Inventory inv = Bukkit.createInventory(player, 54, title+" - Page #"+pageNum);
		
		for(int i=(pageNum*45)-45; i<pageNum*45; i++){
			if(i > questions.size()-1)
				break;
			
			HelpQuestion question = questions.get(i);
			
			ItemStack item = createItem(Material.BOOK, 
					"Question ID: "+question.getEntryID(), 
					Arrays.asList("Sent in by: "+Bukkit.getServer().getOfflinePlayer(UUID.fromString(question.asker_uuid)).getName(), 
							ChatColor.GRAY + "Question:", 
							ChatColor.GRAY + "" + ChatColor.ITALIC + WordUtils.wrap(question.getQuestion(), 20, "\n", false)));
				
			inv.addItem(item);
			
		}
		
		ItemStack backItem = createItem(Material.ARROW, "Back Page", null);
		ItemStack forwardItem = createItem(Material.ARROW, "Forward Page", null);
		
		inv.setItem(45, backItem);
		inv.setItem(53, forwardItem);
		
		return inv;
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onInventoryClick(InventoryClickEvent event){
		Player eventPlayer = (Player)event.getWhoClicked();
		
		if(!eventPlayer.equals(player))
			return;
		
		event.setCancelled(true);
		
		ItemStack item = event.getCurrentItem();
		
		if(item == null)
			return;
		
		if(item.getItemMeta().getDisplayName().equals("Back Page")){
			setPageNum(pageNum-1);
			player.closeInventory();
			inv = generateInventory();
			showInventory();
		}else if(item.getItemMeta().getDisplayName().equals("Forward Page")){			
			setPageNum(pageNum+1);
			player.closeInventory();
			inv = generateInventory();
			showInventory();
		}else if(item.getItemMeta().getDisplayName().contains("Question ID")){
			
			player.closeInventory();
			
			if(viewType == ViewType.UNANSWERED){
				int id = Integer.parseInt(item.getItemMeta().getDisplayName().split(" ")[1]);
				HelpQuestion q = null;
				
				for(HelpQuestion question : questions){
					if(question.getEntryID() == id){
						q = question;
						break;
					}
				}
				
<<<<<<< HEAD
=======
				
>>>>>>> 0c444d8413c2df75966e8445ce049fd42064bf90
				ConversationFactory cf = new ConversationFactory(plugin);
				Conversation conv = cf.withFirstPrompt(new ReplyQuestionConversation(q, player)).withLocalEcho(true).withEscapeSequence("cancel").buildConversation(eventPlayer);
				conv.begin();
			}
		}
	}
	*/
	
}
