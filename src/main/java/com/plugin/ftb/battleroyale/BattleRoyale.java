package com.plugin.ftb.battleroyale;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class BattleRoyale extends JavaPlugin {
	/*
	 * Scoreboard関連変数
	 */
	// 生存者チーム
	public static final String TEAM_ALIVE_NAME = "team_alive";
	private Team teamAlive;
	// 死亡者チーム
	public static final String TEAM_DEAD_NAME = "team_dead";
	private Team teamDead;

	/*
	 * ゲーム進行関連変数
	 */
	// キル数カウント
	public static HashMap<UUID, Integer> killCount = new HashMap<UUID, Integer>();
	// ポイントカウント
	public static HashMap<Integer, Integer> rankSort = new HashMap<Integer, Integer>();

	/*
	 * その他
	 */
	public static BattleRoyale plugin;
	// メッセージ用接頭文字列C
	public static String prefix = ChatColor.GRAY + "[BattleRoyale]";
	//マップ用ピクセル
	public static BufferedImage image;

	@Override
	public void onEnable() {
		plugin = this;

		// コマンド登録
		getCommand("battleroyale").setExecutor(new MainCommandExecutor(this));

		//タブ補完登録
		getCommand("battleroyale").setTabCompleter(new MainTabCompleter());

		// config書き出し
		this.saveDefaultConfig();
		MainConfig.saveDefaultChestItemsConfig();
		MainConfig.saveDefaultFirstItemsConfig();
		MainConfig.saveDefaultProtectedBlocksConfig();
		//configをロード
		MainConfig.loadConfig();


		// イベントリスナ登録
		getServer().getPluginManager().registerEvents(new MainListener(), this);
		getServer().getPluginManager().registerEvents(new SignJoin(), this);

		// メインスコアボードを取得
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getMainScoreboard();

		// チームを新規作成
		teamAlive = board.getTeam(TEAM_ALIVE_NAME);
		if (teamAlive != null) {
			teamAlive.unregister();
		}
		teamAlive = board.registerNewTeam(TEAM_ALIVE_NAME);
		teamAlive.setDisplayName("team alive");
		teamAlive.setAllowFriendlyFire(true);

		teamDead = board.getTeam(TEAM_DEAD_NAME);
		if (teamDead != null) {
			teamDead.unregister();
		}
		teamDead = board.registerNewTeam(TEAM_DEAD_NAME);
		teamDead.setPrefix(ChatColor.GRAY.toString());
		teamDead.setSuffix(ChatColor.RESET.toString());
		teamDead.setDisplayName("team dead");
		teamDead.setAllowFriendlyFire(false);
	}

	@Override
	public void onDisable(){
		//sMapCanvas canvas = CustomMap.mainCanvas;
		for(int x=0; x<128; x++){
			for(int y=0; y<128; y++){
				//canvas.setPixel(x, y, canvas.getBasePixel(x, y));
			}
		}
	}

	/*
	 * ブロードキャストするメソッド。簡略化しただけです。デバッグ用
	 *
	 * @param message ブロードキャストするメッセージ
	 */
	public static void broadcast(String message) {
		Bukkit.getServer().broadcastMessage(message);
	}
	
	public static void sendToPluginTeam(String message) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			String playerName = player.getName();
			if(playerName.equals("kanaami") || playerName.equals("Jump131") || playerName.equals("rushoko")) {
				player.sendMessage(message);
			}
		}
	}
}