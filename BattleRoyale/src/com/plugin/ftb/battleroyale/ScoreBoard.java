package com.plugin.ftb.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoard {

	public void onBoard(Player p){

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

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

		o.getScore(ChatColor.RED + "Kill " + ChatColor.RESET + "≫ " + ChatColor.BOLD + MainListener.killCount.get(p)).setScore(5);
		o.getScore(" ").setScore(4);
		o.getScore(ChatColor.AQUA + "部活 " + ChatColor.RESET + "≫ " + ChatColor.BOLD + "test部").setScore(3);
		o.getScore("").setScore(2);
		o.getScore(ChatColor.GREEN + "チーム " + ChatColor.RESET + "≫ " + ChatColor.BOLD + "test").setScore(1);

		p.setScoreboard(board);
		p.sendMessage("ad");
	}
}
