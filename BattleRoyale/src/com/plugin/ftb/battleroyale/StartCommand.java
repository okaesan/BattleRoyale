package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

class attack extends BukkitRunnable{

	@Override
	public void run(){
		MainListener.Attack = false;
	}
}

class countDown extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	DeathArea deathA = new DeathArea();
	PlusDeathArea PdeathA = new PlusDeathArea();

	int _countdown = 10;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void run(){

		if(_countdown == 0){

			this.cancel();

			Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

			ArrayList<Location> locs = new ArrayList<>();
			for(ArrayList<Integer> point : (ArrayList<ArrayList<Integer>>) plugin.getConfig().get("Startpoints")){
				locs.add(new Location(Bukkit.getWorld("world"), point.get(0), point.get(1), point.get(2)));
			}

			for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				if(p.isOnline()){
					int ran = 0;
					if(plugin.getConfig().getBoolean("isRandom")){
						Random rnd = new Random();
			        	ran = rnd.nextInt(locs.size());
					}

					p.getPlayer().teleport(locs.get(ran));
					StartingInventory.StartingInventoryFunc((Player)p);
					p.getPlayer().sendMessage(BattleRoyale.prefix + ChatColor.GOLD + "ゲームスタート");
				}
			}

			deathA.deathArea();
			PdeathA.setPlusDeath();
			PdeathA.plus();
			MainConfig.giveMap();
			new attack().runTaskLater(plugin, plugin.getConfig().getInt("NATimer")*20);

		}

		countChat();
		_countdown--;

	}

	@SuppressWarnings("deprecation")
	public void countChat(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		if(_countdown!=0){
			for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				if(p.isOnline()){
					p.getPlayer().sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "" + _countdown + "");
				}
			}
		}
	}
}

public class StartCommand extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static String prefix = BattleRoyale.prefix;

	// キル数カウント
	public static HashMap<Player, Integer> killCount = BattleRoyale.killCount;

	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	DeathArea deathA = new DeathArea();
	PlusDeathArea PdeathA = new PlusDeathArea();

	public static int locX, locY, locZ, start=0;
	static int c, r, item;
	public static ArrayList<Integer> loc = new ArrayList<>();

	/*
	 * ゲーム開始コマンド
	 */
	public static void startGame() {

		//ゲーム中は1、ゲーム中以外は0
		start = 1;

		PlusThreadClass.loopC=plugin.getConfig().getIntegerList("Timer").get(0);
		plugin.reloadConfig();

		setChest();
		ScoreBoard.scoreSide(true);

		countDown cd = new countDown();
		cd.runTaskTimer(plugin, 0, 20);
	}

	public static void setChest(){

		for(int i = 1; i <= plugin.getConfig().getInt("chestCounter"); i++){

			locX = plugin.getConfig().getInt("chestlocations"+i+".x");
			locY = plugin.getConfig().getInt("chestlocations"+i+".y");
			locZ = plugin.getConfig().getInt("chestlocations"+i+".z");

			Block block = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);
			block.setType(Material.CHEST);
			Chest chest = (Chest)block.getState();
			Inventory inv = chest.getInventory();

			c = 0;

			do{
				item = plugin.getConfig().getInt("chestItem.setItemCounter");
				Material material = Material.getMaterial(plugin.getConfig().getString("chestItem.item"+(int)((Math.random()*1000)%item+1)));

				inv.setItem((int)(Math.random()*729)/27, new ItemStack(material,1));

				c++;
				r = (int)(Math.random() * 1000 + 22) % 22 - c;
			}while(r>0);
		}
	}
}
