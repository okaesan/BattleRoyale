package com.plugin.ftb.battleroyale;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

public class SignJoin implements Listener {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public Sign s;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSign(SignChangeEvent e){
        if(e.getLine(0).equals("[BattleRoyale]")&&e.getLine(1).equals("joincounter")&&e.getLine(2).equals("Join to Click")){
        	e.setLine(0, ChatColor.DARK_AQUA + "[BattleRoyale]");
        	e.setLine(1, ChatColor.BOLD + String.valueOf(plugin.getServer().getScoreboardManager().getMainScoreboard().getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50));
        	e.setLine(2, ChatColor.GREEN + "Join to Click");
        	e.setLine(3, "-------------");
        	MainConfig.setSign(e.getBlock().getLocation());
        }

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void joinSign(PlayerInteractEvent e){
		if (e.getAction()==Action.RIGHT_CLICK_BLOCK){
            if (e.getClickedBlock().getType()==Material.WALL_SIGN || e.getClickedBlock().getType()==Material.SIGN_POST){
            	Sign s = ((Sign) e.getClickedBlock().getState());

            	Player _player = (Player)e.getPlayer();
            	Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

            	if(s.getLine(0).equals(ChatColor.DARK_AQUA + "[BattleRoyale]")&&s.getLine(1).equals(ChatColor.BOLD + String.valueOf(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50))
            			&&s.getLine(2).equals(ChatColor.GREEN + "Join to Click")&&s.getLine(3).equals("-------------")){
            		if(board.getTeam(TEAM_ALIVE_NAME).hasPlayer(_player)){
            			_player.sendMessage(ChatColor.GREEN + "既に参加しています！");
            			return;
            		}

            		board.getTeam(TEAM_ALIVE_NAME).addPlayer(_player);

            		s.setLine(1, ChatColor.BOLD + String.valueOf(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50));
            		s.update();

            		MainListener.killCount.put(_player, 0);
            		for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
            			new ScoreBoard().onBoard((Player) p);
            		}

            		giveItems(_player);

            		return;
            	}
            }
        }

	}

	public void giveItems(Player player){
		Random md = new Random();
		player.getEquipment().clear();
		player.getInventory().clear();
		int itemran = md.nextInt(4);
		if (itemran == 0) {
			ItemStack item = new ItemStack(Material.WOOD_SWORD);
			player.getInventory().addItem(item);
		} else if (itemran == 1) {
			ItemStack item = new ItemStack(Material.STONE_SWORD);
			player.getInventory().addItem(item);
		} else if (itemran == 2) {
			ItemStack item = new ItemStack(Material.GOLD_SWORD);
			player.getInventory().addItem(item);
		} else if (itemran == 3) {
			ItemStack item = new ItemStack(Material.IRON_SWORD);
			player.getInventory().addItem(item);
		} else {
			ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
			player.getInventory().addItem(item);
		}
		itemran = md.nextInt(4);
		if (itemran == 0) {
			ItemStack item = new ItemStack(Material.LEATHER_CHESTPLATE);
			player.getInventory().addItem(item);
		} else if (itemran == 1) {
			ItemStack item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
			player.getInventory().addItem(item);
		} else if (itemran == 2) {
			ItemStack item = new ItemStack(Material.GOLD_CHESTPLATE);
			player.getInventory().addItem(item);
		} else if (itemran == 3) {
			ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
			player.getInventory().addItem(item);
		} else {
			ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
			player.getInventory().addItem(item);
		}
	}
}