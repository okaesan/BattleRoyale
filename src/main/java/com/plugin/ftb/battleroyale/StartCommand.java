package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

class countDown extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	DeathArea deathA = new DeathArea();
	PlusDeathArea PdeathA = new PlusDeathArea();

	int countDown = 10;

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void run(){

		if(countDown == 0){
			this.cancel();

			Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

			ArrayList<Location> locs = new ArrayList<Location>();
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
		}

		countChat();
		countDown--;

	}

	@SuppressWarnings("deprecation")
	public void countChat(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		if(countDown!=0){
			for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				if(p.isOnline()){
					p.getPlayer().sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "" + countDown + "");
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

	//ゲームが開始してからの秒数
	public static int gameTimer = 0;

	public static int locX, locY, locZ, start=0;
	static int inChestCounter, nextChance, itemCounter;

	public static ArrayList<Integer> loc = new ArrayList<Integer>();

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

	@SuppressWarnings("deprecation")
	public static void setChest(){
		FileConfiguration chestItemsConfig = MainConfig.getChestItemsConfig();

		for(int i = 1; i <= plugin.getConfig().getInt("chestCounter"); i++){

			//チェストのロケーションを取得
			locX = plugin.getConfig().getInt("chestlocations"+i+".x");
			locY = plugin.getConfig().getInt("chestlocations"+i+".y");
			locZ = plugin.getConfig().getInt("chestlocations"+i+".z");

			//チェストのインベントリを取得
			Block block = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);
			block.setType(Material.CHEST);
			Chest chest = (Chest)block.getState();
			Inventory inv = chest.getInventory();

			//値保存
			MainListener.bBLOCK.add(block);
			MainListener.bDATA.add(block.getData());
			MainListener.bMAT.add(block.getType());

			//チェストにアイテムを配置する際の場所
			List<Integer> inChestLocation = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26);
			Collections.shuffle(inChestLocation);

			//チェストにアイテムを配置した回数
			inChestCounter = 0;

			itemCounter = chestItemsConfig.getInt("chestItem.setItemCounter");

			do{
				int id = (int)((Math.random()*1000)%itemCounter+1);

				Material material = Material.getMaterial(chestItemsConfig.getString("chestItem.item"+id));
				ItemStack itemStack = new ItemStack(material,1);
				ItemMeta meta = itemStack.getItemMeta();

				if(chestItemsConfig.getString("chestItem.item"+id + "_name") != null && chestItemsConfig.getString("chestItem.item"+id + "_amount") != null) {
					String name = "§e" + chestItemsConfig.getString("chestItem.item"+id + "_name") + "§e ▪ «" + chestItemsConfig.getString("chestItem.item"+id + "_amount") + "»";

					meta.setDisplayName(name);
					itemStack.setItemMeta(meta);
				}
				if(material==Material.BOW){
					inv.setItem(inChestLocation.get(inChestCounter), new ItemStack(Material.ARROW, chestItemsConfig.getInt("chestItem.item" + id + "_amount")));
					inChestCounter++;
				}

				if(chestItemsConfig.getString("chestItem.item" + id + "_amount") != null){
					itemStack.setAmount(chestItemsConfig.getInt("chestItem.item" + id + "_amount"));
				}

				itemStack.setDurability((short) chestItemsConfig.getInt("chestItem.item" + id + "_damage"));

				if(chestItemsConfig.getString("chestItem.item" + id + "_unbreakable") != null){
					meta.setUnbreakable(chestItemsConfig.getBoolean("chestItem.item" + id + "_unbreakable"));
					itemStack.setItemMeta(meta);
				}

				inv.setItem(inChestLocation.get(inChestCounter), itemStack);

				if(inChestCounter==2){
					break;
				}
				inChestCounter++;
				nextChance = (int)(Math.random()*100) % 3;
			}while(nextChance!=0);
		}
	}
}
