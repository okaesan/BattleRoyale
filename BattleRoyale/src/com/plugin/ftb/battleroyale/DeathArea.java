package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

class ThreadClass extends Thread{
	public void run(){
		DeathArea deathA = new DeathArea();
		try{

			//1秒間停止させます。

			Thread.sleep(5000);
		}
		catch(InterruptedException e){

		}
		deathA.deathArea(null);
	}
}

public class DeathArea extends Thread{

	public static BattleRoyale plugin = BattleRoyale.plugin;
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static ArrayList<Integer> locL = new ArrayList<Integer>();
	public static ArrayList<Integer> locR = new ArrayList<Integer>();

	@SuppressWarnings("deprecation")
	public void deathArea(Player _player){
		ThreadClass th = new ThreadClass();
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

		while(board.getTeam(TEAM_ALIVE_NAME).getSize()>0){
			_player.sendMessage("wa-isss");
			locL = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsL");
			locR = (ArrayList<Integer>) plugin.getConfig().getIntegerList("stagelocationsR");

			if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					p.getPlayer().sendMessage("yeahi");
					if(p.isOnline()){
						p.getPlayer().sendMessage("hey");
						if((int)locL.get(0)<(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)>(int)p.getPlayer().getLocation().getX()
								||(int)locL.get(1)<(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)>(int)p.getPlayer().getLocation().getZ()){
							p.getPlayer().setHealth(0.0D);
						}

					}
				}
			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					p.getPlayer().sendMessage("yeahi");
					if(p.isOnline()){
						p.getPlayer().sendMessage("hey");
						if((int)locL.get(0)>(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)<(int)p.getPlayer().getLocation().getX()
								||(int)locL.get(1)>(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)<(int)p.getPlayer().getLocation().getZ()){
							p.getPlayer().setHealth(0.0D);
						}

					}
				}

			}else if ((int)locL.get(0)>=(int)locR.get(0)&&(int)locL.get(1)<(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					p.getPlayer().sendMessage("yeahi");
					if(p.isOnline()){
						p.getPlayer().sendMessage("hey");
						if((int)locL.get(0)<(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)>(int)p.getPlayer().getLocation().getX()
								||(int)locL.get(1)>(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)<(int)p.getPlayer().getLocation().getZ()){
							p.getPlayer().setHealth(0.0D);
						}

					}
				}

			}else if ((int)locL.get(0)<(int)locR.get(0)&&(int)locL.get(1)>=(int)locR.get(1)){
				for(OfflinePlayer p : board.getTeam(TEAM_ALIVE_NAME).getPlayers()){
					p.getPlayer().sendMessage("yeahi");
					if(p.isOnline()){
						p.getPlayer().sendMessage("hey");
						if((int)locL.get(0)>(int)p.getPlayer().getLocation().getX()||(int)locR.get(0)<(int)p.getPlayer().getLocation().getX()
								||(int)locL.get(1)<(int)p.getPlayer().getLocation().getZ()||(int)locR.get(1)>(int)p.getPlayer().getLocation().getZ()){
							p.getPlayer().setHealth(0.0D);
						}

					}
				}
			}else{

			}
			th.start();
		}
	}
}
