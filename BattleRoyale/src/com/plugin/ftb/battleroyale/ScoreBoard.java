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

	@SuppressWarnings("deprecation")
	//intのokはゲームが終了しているかどうかの変数、0=ゲーム終了時、1=ゲーム中
	public static void scoreSide(boolean nowPlay){
		Scoreboard teamBoard = plugin.getServer().getScoreboardManager().getMainScoreboard();

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
		o.getScore("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "禁止エリア追加まで " + ChatColor.RESET + ": ").setScore(PlusThreadClass.loopC);
		//o.getScore("" + ChatColor.RED + "倒した数 " + ChatColor.RESET + ": " + p.getStatistic(Statistic.JUMP)).setScore(-1);;
		//o.getScore("" + ChatColor.AQUA + "部活 " + ChatColor.RESET + ": " + "test部").setScore(-2);
		//o.getScore("" + ChatColor.GREEN + "チーム " + ChatColor.RESET + ": " + "test").setScore(-3);
		o.getScore("").setScore(-1);
		o.getScore("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()).setScore(-2);

		/*
		 * 変数の中身が変わった状態で再セットされると、変数の部分だけが変わらずに全部がコピーされたのが何個も複製されるため、変数変更前の部分は削除する。
		 * 何を言ってるかよくわからないと思うので、理解してもらうには下のコード消して試してもらえると早いかもです。
		 */
		o.getScoreboard().resetScores("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + String.valueOf(teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()-1));
		o.getScoreboard().resetScores("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + String.valueOf(teamBoard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()+1));

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
		o.getScore(killer).setScore(MainListener.killCount.get(killer));

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
}
