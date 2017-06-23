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
		o.getScore("" + ChatColor.RED + "Kills " + ChatColor.RESET + ": " + String.valueOf(MainListener.killCount.get(p))).setScore(5);;
		o.getScore("" + ChatColor.AQUA + "Club " + ChatColor.RESET + ": " + "test部").setScore(4);
		o.getScore("" + ChatColor.GREEN + "Team " + ChatColor.RESET + ": " + "test").setScore(3);
		o.getScore("").setScore(2);
		o.getScore("" + ChatColor.YELLOW + "Players left " + ChatColor.RESET + ": " + tboard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()).setScore(1);
		o.getScore("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Next DArea " + ChatColor.RESET + ": " + String.valueOf(PlusThreadClass.loopC)).setScore(0);

		/*
		 * 変数の中身が変わった状態で再セットされると、変数の部分だけが変わらずに全部がコピーされたのが何個も複製されるため、変数変更前の部分は削除する。
		 * 何を言ってるかよくわからないと思うので、理解してもらうには下のコード消して試してもらえると早いかもです。
		 */
		o.getScoreboard().resetScores("" + ChatColor.RED + "Kills " + ChatColor.RESET + ": " + String.valueOf(MainListener.killCount.get(p)-1));
		o.getScoreboard().resetScores("" + ChatColor.YELLOW + "Players left " + ChatColor.RESET + ": " + String.valueOf(tboard.getTeam(TEAM_ALIVE_NAME).getPlayers().size()-1));
		o.getScoreboard().resetScores("" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Next DArea " + ChatColor.RESET + ": " + String.valueOf(PlusThreadClass.loopC+1));

		if(ok==0){
			p.setScoreboard(manager.getNewScoreboard());
		}

		p.setScoreboard(board);
	}
}
