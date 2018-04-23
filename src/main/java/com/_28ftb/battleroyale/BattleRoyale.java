package com._28ftb.battleroyale;

import com._28ftb.battleroyale.clubs.Club;
import com._28ftb.battleroyale.clubs.ClubManager;
import com._28ftb.battleroyale.clubs.data.Archery;
import com._28ftb.battleroyale.clubs.data.BaseBall;
import com._28ftb.battleroyale.clubs.data.Drama;
import com._28ftb.battleroyale.clubs.data.Effects;
import com._28ftb.battleroyale.clubs.data.Karate;
import com._28ftb.battleroyale.clubs.data.Kendou;
import com._28ftb.battleroyale.clubs.data.Science;
import com._28ftb.battleroyale.clubs.data.TeaCeremony;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  // マップ用ピクセル
  public static BufferedImage image;

  // 部活設定
  public static Map<UUID, Club> playersClub = new HashMap<>();

  @SuppressWarnings("deprecation")
  @Override
  public void onEnable() {
    plugin = this;

    // コマンド登録
    getCommand("battleroyale").setExecutor(new MainCommandExecutor(this));

    // タブ補完登録
    getCommand("battleroyale").setTabCompleter(new MainTabCompleter());

    // config書き出し
    this.saveDefaultConfig();
    MainConfig.saveDefaultChestItemsConfig();
    MainConfig.saveDefaultFirstItemsConfig();
    MainConfig.saveDefaultProtectedBlocksConfig();
    // configをロード
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

    // 部活関係のイベント登録
    // getServer().getPluginManager().registerEvents(new Test(), this);
    getServer().getPluginManager().registerEvents(new Archery(), this);
    getServer().getPluginManager().registerEvents(new BaseBall(), this);
    getServer().getPluginManager().registerEvents(new Karate(), this);
    getServer().getPluginManager().registerEvents(new Kendou(), this);
    getServer().getPluginManager().registerEvents(new TeaCeremony(), this);
    getServer().getPluginManager().registerEvents(new Science(), this);
    getServer().getPluginManager().registerEvents(new Drama(), this);
    getServer().getPluginManager().registerEvents(new Effects(), this);

    // システムに部活を登録
    ClubManager.addClub(new Club("Dummy"), "所属なし");
    ClubManager.addClub(new Club("Archery"), "弓道部");
    // ClubManager.addClub(new Club("Test"),"テスト部");
    ClubManager.addClub(new Club("BaseBall"), "野球部");
    ClubManager.addClub(new Club("Kendou"), "剣道部");
    ClubManager.addClub(new Club("Swimming"), "水泳部");
    ClubManager.addClub(new Club("TrackAndField"), "陸上部");
    ClubManager.addClub(new Club("BasketBall"), "バスケ部");
    ClubManager.addClub(new Club("VolleyBall"), "バレー部");
    ClubManager.addClub(new Club("Karate"), "空手部");
    ClubManager.addClub(new Club("TeaCeremony"), "茶道部");
    ClubManager.addClub(new Club("Soccer"), "サッカー部");
    ClubManager.addClub(new Club("Science"), "科学部");
    ClubManager.addClub(new Club("Drama"), "演劇部");

    List<String> list = new ArrayList<>();
    list.add("ダミーの部活です。");
    ClubManager.getClub("Dummy").setInfo(list);
    list.clear();

    list.add("弓を最大まで引いてしばらく引き続けると追尾弾になります。");
    ClubManager.getClub("Archery").setInfo(list);
    list.clear();

    /*
     * list.add("テストの部活です。"); list.add("仮作成した機能等が含まれます。");
     * ClubManager.getClub("Test").setInfo(list); list.clear();
     */

    list.add("投擲速度が上昇します。");
    ClubManager.getClub("BaseBall").setInfo(list);
    list.clear();

    list.add("剣の攻撃力が上昇します。");
    ClubManager.getClub("Kendou").setInfo(list);
    list.clear();

    list.add("水中での移動速度が上がります。");
    ClubManager.getClub("Swimming").setInfo(list);
    list.clear();

    list.add("移動速度が上昇します。");
    ClubManager.getClub("TrackAndField").setInfo(list);
    list.clear();

    list.add("ジャンプ力が上昇します。");
    ClubManager.getClub("BasketBall").setInfo(list);
    list.clear();

    list.add("ジャンプ力が上昇します。");
    ClubManager.getClub("VolleyBall").setInfo(list);
    list.clear();

    list.add("攻撃力と防御力が上昇します。");
    ClubManager.getClub("Karate").setInfo(list);
    list.clear();

    list.add("回復力が上昇します。");
    ClubManager.getClub("TeaCeremony").setInfo(list);
    list.clear();

    list.add("ジャンプ力と移動速度が上昇します。");
    ClubManager.getClub("Soccer").setInfo(list);
    list.clear();

    list.add("ダメージポーションの威力が上昇します。");
    ClubManager.getClub("Science").setInfo(list);
    list.clear();

    list.add("シフトを押すことで透明になれますが、移動速度減少と盲目になります。");
    ClubManager.getClub("Drama").setInfo(list);
    list.clear();
  }

  @Override
  public void onDisable() {
    // sMapCanvas canvas = CustomMap.mainCanvas;
    for (int x = 0; x < 128; x++) {
      for (int y = 0; y < 128; y++) {
        // canvas.setPixel(x, y, canvas.getBasePixel(x, y));
      }
    }
  }

  /**
   * メッセージをブロードキャストするメソッド。簡略化しただけです。デバッグ用。
   *
   * @param message ブロードキャストするメッセージ
   */
  public static void broadcast(String message) {
    Bukkit.getServer().broadcastMessage(message);
  }

  /**
   * プラグイン班にメッセージを送るメソッド。
   * アカウント名決め打ち。デバッグ用。
   *
   * @param message 送信するメッセージ
   */
  public static void sendToPluginTeam(String message) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      String playerName = player.getName();
      if (playerName.equals("kanaami") || playerName.equals("Jump131")
          || playerName.equals("rushoko")) {
        player.sendMessage(message);
      }
    }
  }
}