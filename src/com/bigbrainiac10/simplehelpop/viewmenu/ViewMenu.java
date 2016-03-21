package com.bigbrainiac10.simplehelpop.viewmenu;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.bigbrainiac10.simplehelpop.HelpQuestion;
import com.bigbrainiac10.simplehelpop.SimpleHelpOp;

public class ViewMenu implements Listener {

	private final SimpleHelpOp plugin = SimpleHelpOp.getInstance();
	
	private List<HelpQuestion> questions;
	
	private Inventory inv;
	
	private int pageNum = 1;
	private int pageNumMax;
	
	private String title;
	
	private Player player;
			
	public ViewMenu(List<HelpQuestion> questions, String title, Player player){
		this.questions = questions;
		
		double roundedPages = Math.ceil((float)this.questions.size()/45);
		this.pageNumMax = ((int)roundedPages > 0) ? (int)roundedPages : 1;
		
		this.title = title;
		
		this.player = player;
		
		this.inv = generateInventory();
		
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
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
							ChatColor.GRAY + "" + ChatColor.ITALIC + WordUtils.wrap(question.getQuestion(), 20)));
				
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
			ConversationFactory cf = new ConversationFactory(plugin);
			Conversation conv = cf.withFirstPrompt(new ReplyQuestionConversation()).withLocalEcho(true).withEscapeSequence("cancel").buildConversation(eventPlayer);
			conv.begin();
		}
	}
	
}
