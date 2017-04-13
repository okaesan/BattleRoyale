package com.plugin.ftb.battleroyale;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartPointCommand implements CommandExecutor {

	//addコマンドを実行したプレイヤーリスト
	public static ArrayList<Player> adders = new ArrayList<>();
	//removeコマンドを実行したプレイヤーリスト
	public static ArrayList<Player> removers = new ArrayList<>();

	public static String prefix = BattleRoyale.prefix;

	public StartPointCommand(BattleRoyale plugin) {
	}

	/*
	 * 武器,防具配布用音符ブロックを登録、削除
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			if (args.length != 1) {
				sender.sendMessage(prefix + ChatColor.GRAY + "/startpoint " + ChatColor.RED + "add\n" +
						   prefix + ChatColor.GRAY + "/startpoint " + ChatColor.RED + "remove");
				return true;
			}

			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("add")) {
				adders.add(player);
				if (removers.contains(player)) {
					removers.remove(player);
				}

				sender.sendMessage(prefix + ChatColor.GREEN + "音符ブロックを右クリックしてください。");
			} else if (args[0].equalsIgnoreCase("remove")) {
				removers.add(player);
				if (adders.contains(player)) {
					adders.remove(player);
				}
				sender.sendMessage(prefix + ChatColor.GREEN + "音符ブロックを右クリックしてください。");
			} else {
				sender.sendMessage(prefix + ChatColor.GRAY + "/startpoint " + ChatColor.RED + "add\n" +
						   prefix + ChatColor.GRAY + "/startpoint " + ChatColor.RED + "remove");
			}
			return true;
		} else {
			sender.sendMessage(prefix + ChatColor.RED + "ゲーム内から実行してください。");
			return true;
		}
	}
}
