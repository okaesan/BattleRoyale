package com._28ftb.battleroyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

public class JoinSystem {
  public static BattleRoyale plugin = BattleRoyale.plugin;

  // チーム名
  public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
  public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

  static int locX = plugin.getConfig().getInt("SignValue.x");
  static int locY = plugin.getConfig().getInt("SignValue.y");
  static int locZ = plugin.getConfig().getInt("SignValue.z");

  @SuppressWarnings("deprecation")
  public static void onJoin(Player player) {
    Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

    if (board.getTeam(TEAM_ALIVE_NAME).hasPlayer(player)) {
      player.sendMessage(ChatColor.GREEN + "既に参加しています！");
      return;
    }

    board.getTeam(TEAM_ALIVE_NAME).addPlayer(player);

    Block block = Bukkit.getWorld("world").getBlockAt(locX, locY, locZ);

    // 参加用の看板の値の更新 (参加人数0に戻す)
    if (block.getType() == Material.WALL_SIGN || block.getType() == Material.SIGN_POST) {
      Sign joinSign = (Sign) block.getState();
      joinSign.setLine(1, ChatColor.BOLD
          + String.valueOf(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50));
      joinSign.update();
    }

    MainListener.killCount.put(player.getUniqueId(), 0);

    // スコアボードの設定
    ScoreBoard.scoreSide(true);
    ScoreBoard.scoreList(player, true);
  }
}
