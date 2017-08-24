package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

public class PlusDeathArea {

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static ArrayList<Integer> locL = new ArrayList<Integer>();
	public static ArrayList<Integer> locR = new ArrayList<Integer>();
	public static ArrayList<Integer> plusDeathX = new ArrayList<Integer>();
	public static ArrayList<Integer> plusDeathZ = new ArrayList<Integer>();

	public static int betaLx, betaLz, betaRx, betaRz;
	public static int deathAreaCount=0;
	public static int beta=0;

	public void setPlusDeath(){
		locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsL");
		locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsR");

		betaLx = (int)locL.get(0);
		betaLz = (int)locL.get(1);
		betaRx = (int)locR.get(0);
		betaRz = (int)locR.get(1);

		/*         z軸
		 *          |
		 *          |
		 *          |
		 * ---------+--------→ x軸
		 *          |
		 *          |
		 *          |
		 *          ↓
		 */
		if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){

			//Lがx座標、z座標共にRより大きい場合
			do{
				if(betaLz>=(int)locR.get(1)){
					if(betaLx>=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx-=64;
					}else{
						betaLx = (int)locL.get(0);
						betaLz-=64;
					}
				}else{
					break;
				}
			}while(true);

		}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){

			//Lがx座標、z座標共にRより小さい場合
			do{
				if(betaLz<=(int)locR.get(1)){
					if(betaLx<=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx+=64;
					}else{
						betaLx = (int)locL.get(0);
						betaLz+=64;
					}
				}else{
					break;
				}
			}while(true);


		}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){

			//x座標はLが大きく、z座標はRが大きい場合
			do{
				if(betaLz<=(int)locR.get(1)){
					if(betaLx>=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx-=64;
					}else{
						betaLx = (int)locL.get(0);
						betaLz+=64;
					}
				}else{
					break;
				}
			}while(true);


		}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){

			//x座標はRが大きく、z座標はLが大きい場合
			do{
				if(betaLz>=(int)locR.get(1)){
					if(betaLx<=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx+=64;
					}else{
						betaLx = (int)locL.get(0);
						betaLz-=64;
					}
				}else{
					break;
				}
			}while(true);
		}
	}

	public void plus(){

		PlusThreadClass pth = new PlusThreadClass();
		PlusDeathThreadClass pdth = new PlusDeathThreadClass();

		pth.runTaskTimer(plugin, 0, 20);
		pdth.runTaskTimer(plugin, 0, 20);
	}
}

class PlusThreadClass extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

	//規定の追加方法の場合
	public static HashMap<Integer, List<Integer>> deathNotRandom = new HashMap<Integer, List<Integer>>();
	//抽選
	public static List<Integer> rootRandom = new ArrayList<Integer>();
	//完全ランダムの場合
	public static List<Integer> deathRandom = new ArrayList<Integer>();

	//猶予段階のエリア
	public static ArrayList<Integer> deathRandomCount = new ArrayList<Integer>();
	//猶予後のエリア
	public static ArrayList<Integer> deathRandomCountPast = new ArrayList<Integer>();

	public static ArrayList<Integer> Timer = new ArrayList<Integer>();

	public static int count=0;
	public static int countPast=0;
	//禁止区域が追加されるまでのカウンター
	public static int loopC=plugin.getConfig().getIntegerList("Timer").get(0);
	//攻撃が可能になるまでの時間
	public static int attackCountDown=plugin.getConfig().getInt("NATimer");

	/*
	 *スコアボードに禁止区域追加までの時間を表示させるため、追加方法の処理を変更しました
	 */
	@SuppressWarnings("deprecation")
	public void run(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		//禁止区域追加30秒前
		if(loopC==30){
			if(!(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size()>1)){
				//Bukkit.broadcastMessage("test");
				this.cancel();
				return;
			}
			if(PlusDeathArea.beta==0){

				//規定の追加方法の場合
				deathRandom = Arrays.asList(0,3,2,1,15,13,5,6,14,7,4,11,10,9,8,12);
				deathNotRandom.put(1, deathRandom);
				deathRandom = Arrays.asList(12,10,5,13,11,3,15,14,6,7,9,8,4,2,0,1);
				deathNotRandom.put(2, deathRandom);
				deathRandom = Arrays.asList(12,8,11,0,2,3,4,1,5,7,6,9,13,14,10,15);
				deathNotRandom.put(3, deathRandom);
				deathRandom = Arrays.asList(13,15,1,0,10,14,3,4,11,2,6,7,5,8,9,12);
				deathNotRandom.put(4, deathRandom);
				deathRandom = Arrays.asList(6,14,5,9,13,12,15,8,4,0,10,1,2,11,7,3);
				deathNotRandom.put(5, deathRandom);
				deathRandom = Arrays.asList(9,10,6,5,4,8,12,13,14,15,11,7,3,2,1,0);
				deathNotRandom.put(6, deathRandom);
				deathRandom = Arrays.asList(5,3,15,6,7,11,2,10,1,14,13,10,0,12,8,4);
				deathNotRandom.put(7, deathRandom);

				//とりあえず1パターン用
				//deathRandom = Arrays.asList(0,3,2,1,15,13,5,6,14,7,4,11,10,9,8,12);
			}
			deathRandomCount.add(count);
			//Bukkit.broadcastMessage(BattleRoyale.prefix + ChatColor.RED + "30秒後" + ChatColor.GRAY + "に禁止区域が追加されます。");
			MainUtils.sendTitleToEveryone(ChatColor.RED + "30秒後", ChatColor.WHITE + "禁止区域が追加されます。", 1, 3, 1);
			count++;
		}

		//禁止区域追加
		if(loopC==0){
			if(!(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size()>1)){
				this.cancel();
				return;
			}
			deathRandomCountPast.add(countPast);
			//Bukkit.broadcastMessage(BattleRoyale.prefix + ChatColor.RED + "禁止区域が追加されました。");
			MainUtils.sendTitleToEveryone(ChatColor.RED + "禁止区域", ChatColor.RED + "が追加されました。", 1, 3, 1);
			countPast++;
			//二週目からはずっと同じ一定時間
			loopC=plugin.getConfig().getIntegerList("Timer").get(1);
		}

		//攻撃可能になるまでの時間が0になったら攻撃を可能にする
		if(attackCountDown == 0){
			MainListener.Attack = false;
		}else{
			attackCountDown--;
		}

		//１秒ごとにカウントを減らしていく。
		loopC--;
		//ゲームがスタートしてからの秒数を毎秒増やしていく
		StartCommand.gameTimer++;

		//スコアボードの設定
		ScoreBoard.scoreSide(true);
	}
}

class PlusDeathThreadClass extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;

	public static ArrayList<Integer> locL = new ArrayList<Integer>();
	public static ArrayList<Integer> locR = new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	public void run(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsL");
		locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsR");

		if(!(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size()>1)){
        	this.cancel();
        }

		for(int i:PlusThreadClass.deathRandomCountPast){
			//Bukkit.broadcastMessage(PlusThreadClass.deathRanCount.size() + ", " + PlusThreadClass.deathRan.size());
			if(PlusThreadClass.deathRandomCount.isEmpty() || PlusThreadClass.deathRandom.isEmpty()){
				return;
			}

			//規定の追加方法の場合
			int r = PlusThreadClass.deathNotRandom.get(PlusThreadClass.rootRandom.get(StartCommand.playCount)).get(i);

			int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
			int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);

			if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));
					if(p.isOnline()){
						if(pdaX-64<=(int)p.getPlayer().getLocation().getX()&&pdaX>=(int)p.getPlayer().getLocation().getX()
								&&pdaZ-64<=(int)p.getPlayer().getLocation().getZ()&&pdaZ>=(int)p.getPlayer().getLocation().getZ()){

							p.getPlayer().setHealth(0.0D);

						}
					}
				}
			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));
					if(p.isOnline()){
						if(pdaX+64>=(int)p.getPlayer().getLocation().getX()&&pdaX<=(int)p.getPlayer().getLocation().getX()
								&&pdaZ+64>=(int)p.getPlayer().getLocation().getZ()&&pdaZ<=(int)p.getPlayer().getLocation().getZ()){

							p.getPlayer().setHealth(0.0D);

						}
					}
				}

			}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));
					if(p.isOnline()){
						if(pdaX-64<=(int)p.getPlayer().getLocation().getX()&&pdaX>=(int)p.getPlayer().getLocation().getX()
								&&pdaZ+64>=(int)p.getPlayer().getLocation().getZ()&&pdaZ<=(int)p.getPlayer().getLocation().getZ()){

							p.getPlayer().setHealth(0.0D);

						}
					}
				}

			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));
					if(p.isOnline()){
						if(pdaX+64>=(int)p.getPlayer().getLocation().getX()&&pdaX<=(int)p.getPlayer().getLocation().getX()
								&&pdaZ-64<=(int)p.getPlayer().getLocation().getZ()&&pdaZ>=(int)p.getPlayer().getLocation().getZ()){

							p.getPlayer().setHealth(0.0D);

						}
					}
				}
			}
		}
	}
}
