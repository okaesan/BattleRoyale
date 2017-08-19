package com.plugin.ftb.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoard {

	public static BattleRoyale plugin = BattleRoyale.plugin;

	//スコアボード
	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public static Scoreboard battleBoard = manager.getNewScoreboard();

	// チーム名
	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
	public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;
	
	//過去の人数
	public static int playerSize = 0;

	@SuppressWarnings("deprecation")
	//intのokはゲームが終了しているかどうかの変数、0=ゲーム終了時、1=ゲーム中
	public static void scoreSide(boolean nowPlay){
		Scoreboard teamBoard = plugin.getServer().getScoreboardManager().getMainScoreboard();
		int maxPersons = teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()+teamBoard.getTeam(TEAM_DEAD_NAME).getPlayers().size();

		// オブジェクティブが既に登録されているかどうか確認し、
		// 登録されていないなら新規作成します。
		Objective o = battleBoard.getObjective("brStatus");
		if ( o == null ) {
			o = battleBoard.registerNewObjective("brStatus", "dummy");
			// Objective の表示名を設定します。
			o.setDisplayName("" + ChatColor.BLUE + ChatColor.BOLD + "≫ BattleRoyale ≪");
			// Objectiveをどこに表示するかを設定します。
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
		}

		if(nowPlay==false){
			o.unregister();
			return;
		}

		//スコアボードの中身
		o.getScore("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "禁止エリア追加まで " + ChatColor.RESET + ": " + PlusThreadClass.loopC).setScore(2);
		if(PlusThreadClass.attackCountDown!=0){
			o.getScore("" + ChatColor.RED + ChatColor.BOLD + "攻撃可能になるまで" + ChatColor.RESET + ": " + PlusThreadClass.attackCountDown).setScore(1);
		}else{
			o.getScore("" + ChatColor.RED + ChatColor.BOLD + "攻撃可能").setScore(1);
		}
		//o.getScore("" + ChatColor.AQUA + "部活 " + ChatColor.RESET + ": " + "test部").setScore(-2);
		//o.getScore("" + ChatColor.GREEN + "チーム " + ChatColor.RESET + ": " + "test").setScore(-3);
		o.getScore("").setScore(0);
		
		o.getScoreboard().resetScores("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + playerSize);
		playerSize = teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers().size();
		
		o.getScore("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()).setScore(-1);
		
		o.getScoreboard().resetScores("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "禁止エリア追加まで " + ChatColor.RESET + ": " + String.valueOf(PlusThreadClass.loopC+1));
		o.getScoreboard().resetScores("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "禁止エリア追加まで " + ChatColor.RESET + ": 0");
		o.getScoreboard().resetScores("" + ChatColor.RED + ChatColor.BOLD + "攻撃可能になるまで" + ChatColor.RESET + ": " + String.valueOf(PlusThreadClass.attackCountDown+1));

		for(OfflinePlayer p : teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers()){
			if(p.isOnline()==true){
				((Player) p).setScoreboard(battleBoard);
			}
		}
		for(OfflinePlayer p : teamBoard.getTeam(TEAM_DEAD_NAME).getPlayers()){
			if(p.isOnline()==true){
				((Player) p).setScoreboard(battleBoard);
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void scoreList(Player killer, boolean nowPlay){
		Scoreboard teamBoard = plugin.getServer().getScoreboardManager().getMainScoreboard();

		// オブジェクティブが既に登録されているかどうか確認し、
		// 登録されていないなら新規作成します。
		Objective o = battleBoard.getObjective("brKills");
		if ( o == null ) {
			o = battleBoard.registerNewObjective("brKills", "dummy");
			// Objective の表示名を設定します。
			o.setDisplayName("brKills");
			// Objectiveをどこに表示するかを設定します。
			o.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		}

		if(nowPlay==false){
			o.unregister();
			return;
		}

		//スコアボードの中身
		o.getScore(killer).setScore(MainListener.killCount.get(killer.getUniqueId()));

		for(OfflinePlayer p : teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers()){
			if(p.isOnline()==true){
				((Player) p).setScoreboard(battleBoard);
			}
		}
		for(OfflinePlayer p : teamBoard.getTeam(TEAM_DEAD_NAME).getPlayers()){
			if(p.isOnline()==true){
				((Player) p).setScoreboard(battleBoard);
			}
		}
	}
	
	//スコアボードをリロード(unregister)する
	public static void reloadScoreboard() {
		Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();
		Objective brStatus = board.getObjective("brStatus");
		Objective brKills = board.getObjective("brKills");
		if(brStatus != null) brStatus.unregister();
		if(brKills != null) brKills.unregister();
	}
}
