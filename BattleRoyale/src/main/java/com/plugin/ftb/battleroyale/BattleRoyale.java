package com.plugin.ftb.battleroyale;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class BattleRoyale extends JavaPlugin{
	
	public static BattleRoyale plugin;
	
	//生存者チーム
  public static final String TEAM_ALIVE_NAME = "team_alive";
  private Team teamAlive;
  //死亡者チーム
  public static final String TEAM_DEAD_NAME = "team_dead"; 
  private Team teamDead;
    
 	//キル数カウント
 	public static HashMap<Player, Integer> killCount = new HashMap<>();
	//ポイントカウント
 	public static HashMap<Player, Integer> pointCount = new HashMap<>();
    
	@Override
	public void onEnable(){
		
		plugin = this;

		getServer().getPluginManager().registerEvents(new MainListener(), this);	//イベントリスナを登録
		
		// メインスコアボードを取得
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
 
		// チームを新規作成
		teamAlive = board.getTeam(TEAM_ALIVE_NAME);
		if(teamAlive != null){
			teamAlive.unregister();
		}
	    	teamAlive = board.registerNewTeam(TEAM_ALIVE_NAME);
	    	teamAlive.setDisplayName("team aive");
	    	teamAlive.setAllowFriendlyFire(true);

		teamDead = board.getTeam(TEAM_DEAD_NAME);
		if(teamDead != null){
			teamDead.unregister();
		}
		teamDead = board.registerNewTeam(TEAM_DEAD_NAME);
		teamDead.setPrefix(ChatColor.GRAY.toString());
		teamDead.setSuffix(ChatColor.RESET.toString());
		teamDead.setDisplayName("team dead");
		teamDead.setAllowFriendlyFire(false);
	}

	/*
	 *ブロードキャストするメソッド。簡略化しただけです。デバッグ用
	 *@param message ブロードキャストするメッセージ
	 */
	public static void broadcast(String message){
		Bukkit.getServer().broadcastMessage(message);
	}
}
