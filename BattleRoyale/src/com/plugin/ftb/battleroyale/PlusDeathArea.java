package com.plugin.ftb.battleroyale;

import java.util.ArrayList;
import java.util.Collections;

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
			betaLx = (int)locL.get(0);
			betaLz = (int)locL.get(1);
			betaRx = (int)locR.get(0);
			betaRz = (int)locR.get(1);

			do{
				if(betaLz>=(int)locR.get(1)){
					if(betaLx>=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx-=16;
					}else{
						betaLx = (int)locL.get(0);
						betaLz-=16;
					}
				}else{
					break;
				}
			}while(true);

		}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){

			//Lがx座標、z座標共にRより小さい場合
			betaLx = (int)locL.get(0);
			betaLz = (int)locL.get(1);
			betaRx = (int)locR.get(0);
			betaRz = (int)locR.get(1);

			do{
				if(betaLz<=(int)locR.get(1)){
					if(betaLx<=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx+=16;
					}else{
						betaLx = (int)locL.get(0);
						betaLz+=16;
					}
				}else{
					break;
				}
			}while(true);


		}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){

			//x座標はLが大きく、z座標はRが大きい場合
			betaLx = (int)locL.get(0);
			betaLz = (int)locL.get(1);
			betaRx = (int)locR.get(0);
			betaRz = (int)locR.get(1);

			do{
				if(betaLz<=(int)locR.get(1)){
					if(betaLx>=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx-=16;
					}else{
						betaLx = (int)locL.get(0);
						betaLz+=16;
					}
				}else{
					break;
				}
			}while(true);


		}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){

			//x座標はRが大きく、z座標はLが大きい場合
			betaLx = (int)locL.get(0);
			betaLz = (int)locL.get(1);
			betaRx = (int)locR.get(0);
			betaRz = (int)locR.get(1);

			do{
				if(betaLz>=(int)locR.get(1)){
					if(betaLx<=(int)locR.get(0)){
						plusDeathX.add(betaLx);
						plusDeathZ.add(betaLz);
						betaLx+=16;
					}else{
						betaLx = (int)locL.get(0);
						betaLz-=16;
					}
				}else{
					break;
				}
			}while(true);

		}else{

		}
	}

	public void plus(){

		ArrayList<Integer> Timer = new ArrayList<Integer>();

		PlusThreadClass pth = new PlusThreadClass();
		PlusDeathThreadClass pdth = new PlusDeathThreadClass();

		Timer = (ArrayList<Integer>) plugin.getConfig().getIntegerList("Timer");

		pth.runTaskTimer(plugin,Timer.get(0),Timer.get(1));
		pdth.runTaskTimer(plugin, 0, 100);
	}
}

class PlusThreadClass extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;

	public static ArrayList<Integer> deathRan = new ArrayList<Integer>();

	//猶予段階のエリア
	public static ArrayList<Integer> deathRanCount = new ArrayList<Integer>();
	//猶予後のエリア
	public static ArrayList<Integer> deathRanCountPast = new ArrayList<Integer>();

	int count=0;
	int countPast=0;

	@SuppressWarnings("deprecation")
	public void run(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		if(!(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size()>1)){
        	this.cancel();
        	count=0;
        	countPast=0;
        	PlusDeathArea.beta=0;
        	return;
        }

		if(PlusDeathArea.beta==0){
			for(int i=0; i<PlusDeathArea.plusDeathX.size(); i++){
				deathRan.add(PlusDeathArea.beta);

				PlusDeathArea.beta++;
				Collections.shuffle(deathRan);
			}
		}
		deathRanCount.add(count);
		count++;

		/*
		 * 30秒後に追加
		 */
		new BukkitRunnable() {
            @Override
            public void run() {
            	deathRanCountPast.add(countPast);
            	countPast++;
            }
        }.runTaskLater(plugin, 600);
	}
}

class PlusDeathThreadClass extends BukkitRunnable{

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;

	public static ArrayList<Integer> locL = new ArrayList<Integer>();
	public static ArrayList<Integer> locR = new ArrayList<Integer>();

	public static int betaLx, betaLz, betaRx, betaRz;

	//deathAreaCount++;

	@SuppressWarnings("deprecation")
	public void run(){
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsL");
		locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsR");

		if(!(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size()>1)){
        	this.cancel();
        }

		for(int i:PlusThreadClass.deathRanCountPast){

			int r = PlusThreadClass.deathRan.get(i);

			int pdaX = (int)PlusDeathArea.plusDeathX.get(r);
			int pdaZ = (int)PlusDeathArea.plusDeathZ.get(r);

				if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){

						//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));

						if(p.isOnline()){
							if(pdaX-16<=(int)p.getPlayer().getLocation().getX()&&pdaX>=(int)p.getPlayer().getLocation().getX()
									&&pdaZ-16<=(int)p.getPlayer().getLocation().getZ()&&pdaZ>=(int)p.getPlayer().getLocation().getZ()){

								p.getPlayer().setHealth(0.0D);

							}
						}
					}
				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){

						//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));

						if(p.isOnline()){
							if(pdaX+16>=(int)p.getPlayer().getLocation().getX()&&pdaX<=(int)p.getPlayer().getLocation().getX()
									&&pdaZ+16>=(int)p.getPlayer().getLocation().getZ()&&pdaZ<=(int)p.getPlayer().getLocation().getZ()){

								p.getPlayer().setHealth(0.0D);

							}
						}
					}

				}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
					for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){

						//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));

						if(p.isOnline()){
							if(pdaX-16<=(int)p.getPlayer().getLocation().getX()&&pdaX>=(int)p.getPlayer().getLocation().getX()
									&&pdaZ+16>=(int)p.getPlayer().getLocation().getZ()&&pdaZ<=(int)p.getPlayer().getLocation().getZ()){

								p.getPlayer().setHealth(0.0D);

							}
						}
					}

				}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
					for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){

						//p.getPlayer().sendMessage(String.valueOf(PlusThreadClass.deathRan));

						if(p.isOnline()){
							if(pdaX+16>=(int)p.getPlayer().getLocation().getX()&&pdaX<=(int)p.getPlayer().getLocation().getX()
									&&pdaZ-16<=(int)p.getPlayer().getLocation().getZ()&&pdaZ>=(int)p.getPlayer().getLocation().getZ()){

								p.getPlayer().setHealth(0.0D);

							}
						}
					}
				}else{

				}
		}
	}
}
