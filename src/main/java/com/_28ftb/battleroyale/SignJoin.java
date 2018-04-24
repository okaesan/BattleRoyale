package com._28ftb.battleroyale;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scoreboard.Scoreboard;

public class SignJoin implements Listener {

  public static BattleRoyale plugin = BattleRoyale.plugin;
  public Sign s;

  // チーム名
  public static final String TEAM_ALIVE_NAME = BattleRoyale.TEAM_ALIVE_NAME;
  public static final String TEAM_DEAD_NAME = BattleRoyale.TEAM_DEAD_NAME;

  // 看板の制作
  @SuppressWarnings("deprecation")
  @EventHandler
  public void onSign(SignChangeEvent e) {
    if (e.getLine(0).equals("[BattleRoyale]") && e.getLine(1).equals("joincounter")
        && e.getLine(2).equals("Join to Click")) {
      e.setLine(0, ChatColor.DARK_AQUA + "[BattleRoyale]");
      e.setLine(1, ChatColor.BOLD + String.valueOf(plugin.getServer().getScoreboardManager()
          .getMainScoreboard().getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50));
      e.setLine(2, ChatColor.GREEN + "Join to Click");
      e.setLine(3, "-------------");
      MainConfig.setSign(e.getBlock().getLocation());
    }
  }

  // 制作された看板を右クリックした時の動作
  @SuppressWarnings({ "deprecation" })
  @EventHandler
  public void joinSign(PlayerInteractEvent e) {
    if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if (e.getClickedBlock().getType() == Material.WALL_SIGN
          || e.getClickedBlock().getType() == Material.SIGN_POST) {
        Sign s = ((Sign) e.getClickedBlock().getState());

        Player player = (Player) e.getPlayer();
        Scoreboard board = plugin.getServer().getScoreboardManager().getMainScoreboard();

        if (s.getLine(0).equals(ChatColor.DARK_AQUA + "[BattleRoyale]")
            && s.getLine(1)
                .equals(ChatColor.BOLD
                    + String.valueOf(board.getTeam(TEAM_ALIVE_NAME).getPlayers().size() + "/" + 50))
            && s.getLine(2).equals(ChatColor.GREEN + "Join to Click")
            && s.getLine(3).equals("-------------")) {

          JoinSystem.onJoin(player);
        }
      }
    }
  }
}
