package com.plugin.ftb.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoard {

	public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;

	@SuppressWarnings("deprecation")
	//intのokはゲームが終了しているかどうかの変数、0=ゲーム終了時、1=ゲーム中
	public void onBoard(Player p, int ok){

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();
		Scoreboard tboard = BattleRoyale.plugin.getServer().getScoreboardManager().getMainScoreboard();

		// オブジェクティブが既に登録されているかどうか確認し、
		// 登録されていないなら新規作成します。
		Objective o = board.getObjective("brStatus");
		if ( o == null ) {
			o = board.registerNewObjective("brStatus", "dummy");
		}
		// Objectiveをどこに表示するかを設定します。
		// SIDEBAR、PLAYER_LIST、BELOW_NAME が指定できます。
		o.setDisplaySlot(DisplaySlot.SIDEBAR);

		// Objective の表示名を設定します。
		o.setDisplayName("" + ChatColor.BLUE + ChatColor.BOLD + "≫ BattleRoyale ≪");

		//スコアボードの中身
		o.getScore("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "禁止エリア追加まで " + ChatColor.RESET + ": ").setScore(PlusThreadClass.loopC);
		o.getScore("" + ChatColor.RED + "倒した数 " + ChatColor.RESET + ": " + String.valueOf(MainListener.killCount.get(p))).setScore(-1);;
		o.getScore("" + ChatColor.AQUA + "部活 " + ChatColor.RESET + ": " + "test部").setScore(-2);
		o.getScore("" + ChatColor.GREEN + "チーム " + ChatColor.RESET + ": " + "test").setScore(-3);
		o.getScore("").setScore(-3);
		o.getScore("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + tboard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()).setScore(-4);

		/*
		 * 変数の中身が変わった状態で再セットされると、変数の部分だけが変わらずに全部がコピーされたのが何個も複製されるため、変数変更前の部分は削除する。
		 * 何を言ってるかよくわからないと思うので、理解してもらうには下のコード消して試してもらえると早いかもです。
		 */
		o.getScoreboard().resetScores("" + ChatColor.RED + "倒した数 " + ChatColor.RESET + ": " + String.valueOf(MainListener.killCount.get(p)-1));
		o.getScoreboard().resetScores("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + String.valueOf(tboard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()-1));
		o.getScoreboard().resetScores("" + ChatColor.YELLOW + "残り人数 " + ChatColor.RESET + ": " + String.valueOf(tboard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()+1));

		if(ok==0){
			o.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		}

		p.setScoreboard(board);
	}
}
