package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

class countDown extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	//攻撃が可能になるまでの時間
	public static int attackCountDown=plugin.getConfig().getInt("NATimer");

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
				if(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() <= locs.size()) {
					if(p.isOnline()) {
						int ran = 0;
						if(plugin.getConfig().getBoolean("isRandom")){
							Random rnd = new Random();
				        	ran = rnd.nextInt(locs.size());
						}
						p.getPlayer().teleport(locs.get(ran));
						locs.remove(ran);
						StartingInventory.StartingInventoryFunc((Player)p);
						//p.getPlayer().sendMessage(BattleRoyale.prefix + ChatColor.GOLD + "ゲームスタート");
						MainUtils.sendTitle((Player)p, "" + ChatColor.GOLD + ChatColor.BOLD + "ゲームスタート", "", 1, 1, 1);
					}
				}else{
					if(p.isOnline()){
						int ran = 0;
						if(plugin.getConfig().getBoolean("isRandom")){
							Random rnd = new Random();
				        	ran = rnd.nextInt(locs.size());
						}
						p.getPlayer().teleport(locs.get(ran));
						StartingInventory.StartingInventoryFunc((Player)p);
						//p.getPlayer().sendMessage(BattleRoyale.prefix + ChatColor.GOLD + "ゲームスタート");
						MainUtils.sendTitle((Player)p, "" + ChatColor.GOLD + ChatColor.BOLD + "ゲームスタート", "", 1, 1, 1);
					}
				}
			}
			
			//禁止区域を選定
			PlusThreadClass.deathRandom = AreaManager.getRandomList();
			PlusThreadClass.deathNotRandom = PlusThreadClass.deathRandom;

			deathA.deathArea();
			PdeathA.setPlusDeath();
			MainConfig.giveMap();
			attackCountDown();
			startEffectTask();
			setPlayer();
		}

		countChat();
		countDown--;

	}

	//プレイヤーの満腹度、HPを満タンに
	@SuppressWarnings("deprecation")
	public void setPlayer() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			player.setHealth(player.getMaxHealth());
			player.setFoodLevel(20);
		}
	}

	//攻撃可能までのカウントダウン用のタスク
	public void attackCountDown(){
		new BukkitRunnable(){

			@Override
			public void run(){
				//攻撃可能になってから30秒後に禁止区域の更新を始める。
				if(attackCountDown == -30){
					PdeathA.plus();
					this.cancel();
				}
				//攻撃可能になるまでの時間が0になったら攻撃を可能にする
				else if(attackCountDown == 0){
					MainListener.Attack = false;
					attackCountDown--;
				}
				else{
					attackCountDown--;
				}
				//スコアボードの設定
				ScoreBoard.scoreSide(true);
			}
		}.runTaskTimer(plugin, 20, 20);
	}

	//5分に1回プレイヤーを発光させるタスク
	public void startEffectTask() {
		new BukkitRunnable() {

			@Override
			public void run() {
				//5分に1回実行される
				if(StartCommand.start == 0) this.cancel();
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player.getGameMode().equals(GameMode.SPECTATOR)) return;
					if(StartCommand.start == 0) this.cancel();
					player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 10000000, 1000000));
				}
			}
		}.runTaskTimer(plugin, 5*60*20, 5*60*20);

		new BukkitRunnable() {
			@Override
			public void run() {
				//10秒遅れで5分に1回実行される
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player.hasPermission("battleroyale.op"))
						continue;
					if(player.getPotionEffect(PotionEffectType.GLOWING) != null && player.getPotionEffect(PotionEffectType.GLOWING).getAmplifier() > 0) {
						//エフェクト削除
						player.removePotionEffect(PotionEffectType.GLOWING);
					}
				}
				if(StartCommand.start == 0) this.cancel();
			}
		}.runTaskTimer(plugin, 5*60*20 + 15*20, 5*60*20);
	}

	@SuppressWarnings("deprecation")
	public void countChat(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		if(countDown!=0){
			for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
				if(p.isOnline()){
					//p.getPlayer().sendMessage(BattleRoyale.prefix + ChatColor.GREEN + "" + countDown + "");
					MainUtils.sendTitle((Player)p, "" + ChatColor.GREEN + ChatColor.BOLD + countDown, "", 1, 1, 1);
				}
			}
		}
	}
}

public class StartCommand extends BattleRoyale {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static String prefix = BattleRoyale.prefix;

	// キル数カウント
	public static HashMap<UUID, Integer> killCount = BattleRoyale.killCount;

	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	DeathArea deathA = new DeathArea();
	PlusDeathArea PdeathA = new PlusDeathArea();

	//ゲームが開始してからの秒数
	public static int gameTimer = 0;

	public static int locX, locY, locZ;
	public static int start=0;
	public static int playCount = 0;
	static int inChestCounter;
	static int nextChance;
	static int itemCounter;
	static int maxInChestCounter;
	static int maxItemPercent;
	static int minItemPercent;

	public static ArrayList<Integer> loc = new ArrayList<Integer>();

	/*
	 * ゲーム開始コマンド
	 */
	public static void startGame() {

		//ゲーム中は1、ゲーム中以外は0
		start = 1;

		if(PlusThreadClass.deathNotRandom.size()<=playCount){
			playCount = 0;
		}else{
			//ゲームが実行された回数
			playCount++;
		}

		PlusThreadClass.loopC=plugin.getConfig().getIntegerList("Timer").get(0);
		plugin.reloadConfig();

		setChest();
		ScoreBoard.scoreSide(true);
		//スペクターモード以外のプレイヤーをサバイバルモードに
		setSurvival();

		countDown cd = new countDown();
		cd.runTaskTimer(plugin, 0, 20);
	}

	//サバイバルモードに変更
	public static void setSurvival() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(!player.getGameMode().equals(GameMode.SPECTATOR)) {
				if(!player.hasPermission("battleroyale.op")) {
					//スペクターモード以外のプレイヤーをサバイバルモードに
					player.setGameMode(GameMode.SURVIVAL);
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void setChest(){
		FileConfiguration chestItemsConfig = MainConfig.getChestItemsConfig();

		for(int i = 1; i <= plugin.getConfig().getInt("chestCounter"); i++){

			/*
			 * configから取得
			 */
			//チェストに入るアイテムの登録数を取得
			itemCounter = chestItemsConfig.getInt("chestItem.setItemCounter");
			//チェストに入るアイテムの数の幅を取得
			maxItemPercent = chestItemsConfig.getInt("chestItem.maxValue");
			minItemPercent = chestItemsConfig.getInt("chestItem.minValue");
			//チェストのロケーションを取得
			locX = plugin.getConfig().getInt("chestlocations"+i+".x");
			locY = plugin.getConfig().getInt("chestlocations"+i+".y");
			locZ = plugin.getConfig().getInt("chestlocations"+i+".z");

			//チェストのインベントリを取得
			Block block = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);
			block.setType(Material.CHEST);
			Chest chest = (Chest)block.getState();
			Inventory inv = chest.getInventory();
			//インベントリの中身を一度クリア
			inv.clear();

			//値保存
			MainListener.bBLOCK.add(block);
			MainListener.bDATA.add(block.getData());
			MainListener.bMAT.add(block.getType());

			//チェストにアイテムを配置する際の場所
			List<Integer> inChestLocation = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26);
			Collections.shuffle(inChestLocation);

			//チェストにアイテムを配置した回数
			inChestCounter = 0;

			//チェストに入るアイテムの個数
			if(maxItemPercent == minItemPercent){
				maxInChestCounter = minItemPercent;
			}else{
				maxInChestCounter = (int)(Math.random()*1000) % (maxItemPercent - minItemPercent) + minItemPercent;
			}

			for(; inChestCounter<maxInChestCounter; inChestCounter++){
				int id = (int)((Math.random()*1000)%itemCounter+1);
				Material material = Material.getMaterial(chestItemsConfig.getString("chestItem.item"+id));
				ItemStack itemStack;
				if(material.equals(Material.POTION) || material.equals(Material.SPLASH_POTION)) {
					PotionType potionType = PotionType.valueOf(chestItemsConfig.getString("chestItem.item"+ id + "_effectType"));
					
					itemStack = MainUtils.getPotion(potionType, 
							chestItemsConfig.getInt("chestItem.item"+id + "_sec"), 
							chestItemsConfig.getInt("chestItem.item"+id + "_level"),
							chestItemsConfig.getInt("chestItem.item"+ id + "_amount"));
				}else {
					itemStack = new ItemStack(material,1);
					ItemMeta meta = itemStack.getItemMeta();

					if(chestItemsConfig.getString("chestItem.item"+id + "_name") != null) {
						String name = chestItemsConfig.getString("chestItem.item"+id + "_name");

						meta.setDisplayName(name);
						itemStack.setItemMeta(meta);
					}else {
						if(material==Material.BOW){
							inv.setItem(inChestLocation.get(inChestCounter), new ItemStack(Material.ARROW, chestItemsConfig.getInt("chestItem.item" + id + "_amount")));
							inChestCounter++;
						}else {
							if(chestItemsConfig.getString("chestItem.item" + id + "_amount") != null){
								itemStack.setAmount(chestItemsConfig.getInt("chestItem.item" + id + "_amount"));
							}
						}
					}

					itemStack.setDurability((short) chestItemsConfig.getInt("chestItem.item" + id + "_damage"));

					if(chestItemsConfig.getString("chestItem.item" + id + "_unbreakable") != null){
						meta.setUnbreakable(chestItemsConfig.getBoolean("chestItem.item" + id + "_unbreakable"));
						itemStack.setItemMeta(meta);
					}
				}

				inv.setItem(inChestLocation.get(inChestCounter), itemStack);
			}
		}
	}
}
