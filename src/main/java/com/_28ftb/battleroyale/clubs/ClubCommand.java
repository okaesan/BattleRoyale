package com._28ftb.battleroyale.clubs;

import com._28ftb.battleroyale.BattleRoyale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClubCommand implements CommandExecutor {

  public static BattleRoyale plugin = BattleRoyale.plugin;

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

    if (sender instanceof Player) {
      if (args.length == 0) {
        return false;
      }

      Player player = (Player) sender;

      switch (args[0]) {
        case "set":
          if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "プレーヤーを選択してください。");
            return true;
          } else if (args.length == 2) {
            player.sendMessage(ChatColor.RED + "部活名を入力してください。");
            return true;
          } else if (args.length > 3) {
            player.sendMessage(ChatColor.RED + "長すぎます。");
            return true;
          } else if (Bukkit.getPlayerExact(args[1]) == null) {
            player.sendMessage(ChatColor.RED + args[1] + "というプレイヤーは存在しません。");
            return true;
          } else {
            if (ClubManager.isClub(args[2])) {
              ClubManager.setClub(Bukkit.getPlayerExact(args[1]), ClubManager.getClub(args[2]));
              player.sendMessage(ChatColor.GREEN + "プレイヤー <" + args[1] + "> に部活 <"
                  + ClubManager.getClub(args[2]).getDisplayName() + "> を設定しました。");
              return true;
            } else {
              player.sendMessage(ChatColor.RED + "部活 <" + args[2] + "> は存在しません。");
              return true;
            }
          }

        case "remove":
          if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "プレーヤーを選択してください。");
            return true;
          } else if (args.length > 2) {
            player.sendMessage(ChatColor.RED + "長すぎます。");
            return true;
          } else if (Bukkit.getPlayerExact(args[1]) == null) {
            player.sendMessage(ChatColor.RED + args[1] + "というプレイヤーは存在しません。");
            return true;
          } else {
            if (ClubManager.hasClub(Bukkit.getPlayerExact(args[1]))) {
              String c = ClubManager.getClub(Bukkit.getPlayerExact(args[1])).getDisplayName();
              ClubManager.removeClub(Bukkit.getPlayerExact(args[1]));
              player.sendMessage(
                  ChatColor.GREEN + "プレイヤー <" + args[1] + "> から部活 <" + c + "> を削除しました。");
              return true;
            } else {
              player.sendMessage(ChatColor.RED + "部活は設定されていません。");
              return true;
            }
          }

        case "get":
          if (args.length == 1) {
            player.sendMessage(ChatColor.RED + "プレーヤーを選択してください。");
            return true;
          } else if (args.length > 2) {
            player.sendMessage(ChatColor.RED + "長すぎます。");
            return true;
          } else if (Bukkit.getPlayerExact(args[1]) == null) {
            player.sendMessage(ChatColor.RED + args[1] + "というプレイヤーは存在しません。");
            return true;
          } else {

            if (ClubManager.hasClub(Bukkit.getPlayerExact(args[1]))) {

              player.sendMessage("プレイヤー " + ChatColor.LIGHT_PURPLE + "<"
                  + Bukkit.getPlayerExact(args[1]).getName() + ">" + ChatColor.RESET + " の部活");
              player.sendMessage(ChatColor.AQUA + "<"
                  + ClubManager.getClub(Bukkit.getPlayerExact(args[1])).getName() + "> : <"
                  + ClubManager.getClub(Bukkit.getPlayerExact(args[1])).getDisplayName() + ">");
              return true;
            } else {
              player.sendMessage(ChatColor.RED + "部活は設定されていません。");
              return true;
            }
          }

        case "list":
          player.sendMessage(ChatColor.AQUA + "<システム名> : <表示される名前>");
          for (Club c : ClubManager.getClubList()) {
            player.sendMessage(
                ChatColor.AQUA + "<" + c.getName() + "> : <" + c.getDisplayName() + ">");
          }
          return true;

        default:
          return false;
      }
    }
    return true;
  }
}
